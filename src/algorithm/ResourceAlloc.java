package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import data.CommonNode;
import data.DataSave;
import data.FiberLink;
import data.LinkRGroup;
import data.Route;
import data.Traffic;
import data.WDMLink;
import dialog.Dlg_PolicySetting;
import enums.NodeType;
import enums.Status;
import enums.TrafficLevel;
import enums.TrafficStatus;
import survivance.Survivance;

/***
 * 功能：此类供规划向导调用，是进行资源分配的总入口。 资源分配分为链路资源分配（CommonAlloc）和端口资源分配（PortAlloc） 。
 * 不考虑资源的扩容 。
 * 
 * @author CC
 * @since 2016/7/4
 */
public class ResourceAlloc {
	public static int index = 1;
	public static List<Traffic> trafficList = new LinkedList<Traffic>(); // 输入的业务列表的镜像
	public static List<Traffic> failedTraffic = new LinkedList<Traffic>();// 存储失败的业务
	// 存放工作路由计算失败的业务
	LinkedList<Traffic> workFailure = new LinkedList<>();
	// 存放保护路由计算失败的业务
	LinkedList<Traffic> protectFailure = new LinkedList<>();
	// 存放预置路由计算失败的业务
	LinkedList<Traffic> preFailure = new LinkedList<>();
	// public static int lock = DataSave.locknum; // 锁定

	public static void allocateResource(List<Traffic> traList, int flag) {// flag=0:最短长度；=1：最小跳
		Survivance.mark = flag;// 用于抗毁仿真中标记分配的时候使用的哪种路由算法
		CommonAlloc.fallBuffer = new StringBuffer();// 链路资源分配日志
		PortAlloc.portFallBuffer = new StringBuffer();// 端口资源分配日志

		trafficList = new LinkedList<Traffic>();
//		for (Traffic tra : traList) {
//			if (tra.getTrafficgroup() != null) {
//				trafficList.add(tra);
//			}
//		}
//		for (Traffic tra : traList) {
//			if (tra.getTrafficgroup() == null) {
//				trafficList.add(tra);
//			}
//		}

		trafficList.addAll(traList);// 镜像了一份业务列表，对trafficList的操作不会影响traList
		// Collections.reverse(traList);

		// 存放工作路由计算失败的业务
		LinkedList<Traffic> workFailure = new LinkedList<>();
		// 存放保护路由计算失败的业务
		LinkedList<Traffic> protectFailure = new LinkedList<>();
		// 存放预置路由计算失败的业务
		LinkedList<Traffic> preFailure = new LinkedList<>();

		Route route = null;
		OSNR osnr = new OSNR();
		if (!RouteAlloc.isLimitHop)
			RouteAlloc.hopLimit = Integer.MAX_VALUE; // 新增跳数限制(不过不限制跳数，就把跳数设置为无穷大)
		else if (RouteAlloc.hopLimit == Integer.MAX_VALUE)
			RouteAlloc.hopLimit = 5; // 默认跳数限制为5

		Iterator<Traffic> it = trafficList.iterator();
		while (it.hasNext()) {// 循环计算链表里每个业务的工作路由/保护路由（如果有）

			index++;
			Traffic traffic = it.next();

			// 将必避链路、节点设为不激活
			ResourceAlloc.setMustAvoidUnactive(traffic);

			// 关联业务组设置设置不激活（包括设置相应的SRLG为不激活）
			ResourceAlloc.setRelatedTrafficUnactive(traffic, DataSave.separate);

			// 将资源不足的链路设为不激活 输入：是否是计算预置路由
			LinkedList<WDMLink> canActiveLinks = ResourceAlloc.resourceNotEnough(false);

			// 将有SRLG的链路设为不激活
			// LinkedList<WDMLink> srlgLink = ResourceAlloc.setSRLGFalse();

			// 必经节点、必经链路,算路
			route = ResourceAlloc.routeForTraffic(traffic, flag);

			// 关联业务组业务，当路由中链路有平行边时，优先使用没有SRLG的链路
			if (traffic.getTrafficgroup() != null) {
				if (!traffic.getTrafficgroup().getBelongGroup().equals("") && route != null) {
					for (int i = 0; i < route.getWDMLinkList().size(); i++) {
						WDMLink link = route.getWDMLinkList().get(i);
						if (link.getParallelLinkList().size() > 1) {
							LinkedList<WDMLink> noSRLGLink = new LinkedList<>();
							for (int j = 0; j < link.getParallelLinkList().size(); j++) {
								if (link.getParallelLinkList().get(j).belongSRLG() == null
										&& link.getParallelLinkList().get(j).isActive()) {
									noSRLGLink.add(link.getParallelLinkList().get(j));
								}
							}
							if (noSRLGLink.size() > 0) {
								WDMLink newLink = noSRLGLink.get(0);
								for (int z = 0; z < noSRLGLink.size(); z++) {
									if (noSRLGLink.get(z).getRemainResource() > newLink.getRemainResource()) {
										newLink = noSRLGLink.get(z);
									}
								}
								route.getWDMLinkList().set(i, newLink);
							}
						}
					}
				}
			}
			// 关联业务组业务，应该尽量用一个链路，而不是分散在多个平行边上
			if (traffic.getTrafficgroup() != null && traffic.getTrafficNum() > 1 && traffic.getId() != 1) {
				int traid = traffic.getId();
				Traffic lastTraffic = Traffic.getTraffic(traid - 1);
				Route lastroute = new Route();
				boolean islastOk = false;
				if (lastTraffic.getWorkRoute() != null && lastTraffic.getRankId() == traffic.getRankId()) {
					List<WDMLink> linkList = new LinkedList<>();
					List<CommonNode> nodeList = new LinkedList<>();
					for (WDMLink link : lastTraffic.getWorkRoute().getWDMLinkList()) {
						linkList.add(link);
					}
					for (CommonNode node : lastTraffic.getWorkRoute().getNodeList()) {
						nodeList.add(node);
					}
					lastroute.setFrom(traffic.getFromNode());
					lastroute.setTo(traffic.getToNode());
					lastroute.setWDMLinkList(linkList);
					lastroute.setNodeList(nodeList);
					lastroute.setBelongsTraffic(traffic);
					islastOk = isRouteConsist(lastroute, traffic, 1);
				}
				if (islastOk) {
					route = lastroute;
				} else if (lastTraffic.getRankId() == traffic.getRankId()) {
					Route lastTrafficroute = lastTraffic.getWorkRoute();
					if (Route.isPassSameNode(route, lastTrafficroute))
						Route.setPassSameLink(lastTrafficroute, route);
				}
			}

			if (DataSave.OSNR) {
				System.out.println("HELLOHELLOHELLO");
				route = findMeetOSNRroute(route, traffic, flag, 1);
				if (route == null) {
					for (WDMLink link : canActiveLinks) {
						link.setActive(true);
					}
					route = findMeetOSNRroute(route, traffic, flag, 1);
				}
			}

			// if(!ResourceAlloc.isRealtedTrafficRouteSucced(route, traffic, flag));

			if (route != null && route.getWDMLinkList().size() != 0 && traffic.getWorkRoute() == null) {
				// System.out.println("os1:"+osnr.calculateOSNR(route));
				// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// 如果满足osnr要求
				if (1 > 0) {// 为了避开OSNR限制 做3期时要改掉
					Traffic tra = route.getBelongsTraffic();
					if (tra.isElectricalCrossConnection() == true && tra.getNrate() < 100) {
						if (CommonAlloc.EallocateWaveLength_workandpro(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
							// 端口分配没写，要改
							System.out.println("子波长业务" + traffic.getName() + "工作路由分配成功");
							CommonAlloc.fallBuffer.append(
									"业务:" + traffic.getName() + "(" + traffic.getNrate() + ")工作路由及资源分配成功！(子波长分配)\r\n");
							traffic.setWorkRoute(route);// 如果路由不为空则设置为工作路由
							traffic.setStatus(TrafficStatus.工作已分配);
						} else {// 资源分配失败
							failedTraffic.add(traffic);
							System.out.println("业务" + traffic.getName() + "工作路由分配失败");
							traffic.setStatus(TrafficStatus.阻塞);// 如果还没有就是找不到
							CommonAlloc.fallBuffer.append(
									"业务:" + traffic.getName() + "(" + traffic.getNrate() + ")工作路由波长资源分配失败！\r\n");
							RouteAlloc.releaseRoute(route);
						}
					} else if (tra.isElectricalCrossConnection() == false || tra.getNrate() == 100) {
						// 100G业务还要考虑是否需要波长转换和osnr，如果这两个条件不满足，还要子波长分配

						// 判断route可否使用一致波长
						ResourceAlloc.isRouteConsistent(route, traffic, 1, flag, canActiveLinks);

						if (CommonAlloc.allocateWaveLength(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
							System.out.println("业务" + traffic.getName() + "工作路由分配成功");
							CommonAlloc.fallBuffer
									.append("业务:" + traffic.getName() + "(" + traffic.getNrate() + ")工作路由及资源分配成功！\r\n");
							traffic.setWorkRoute(route);// 如果路由不为空则设置为工作路由
							traffic.setStatus(TrafficStatus.工作已分配);
						} else {// 资源分配失败

							failedTraffic.add(traffic);
							workFailure.add(traffic);
							System.out.println("业务" + traffic.getName() + "工作路由分配失败");
							traffic.setStatus(TrafficStatus.阻塞);// 如果还没有就是找不到
							CommonAlloc.fallBuffer.append(
									"业务:" + traffic.getName() + "(" + traffic.getNrate() + ")工作路由波长资源分配失败！\r\n");
							RouteAlloc.releaseRoute(route);
						}
					}
				}
				// else {// 如果osnr超过门限
				// if (Suggest.isSuggested == true) {// 2017.10.14
				// OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
				// OSNR.OSNRRouteList.add(route);//10.19
				// }
				// CommonAlloc.fallBuffer.append("业务:" + traffic + "工作路由OSNR不满足条件！\r\n");
				// }
			} else {// wdmlink链路为空

				if (traffic.getWorkRoute() == null) {
					traffic.setStatus(TrafficStatus.规划业务阻塞);// 如果还没有就是找不到
					CommonAlloc.fallBuffer.append("业务:" + traffic + "工作路由算路失败！\r\n");
				}
			}

			//////// 开始计算保护路由
			if (traffic.getWorkRoute() != null && (traffic.getProtectLevel() == TrafficLevel.NORMAL11
					|| traffic.getProtectLevel() == TrafficLevel.PERMANENT11
					|| traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION)) {

				// 设置工作路由不激活来计算保护路由
				ResourceAlloc.setRouteLinkUnactive(traffic.getWorkRoute());

				// //将有srlg的link激活
				// for(WDMLink link : srlgLink) {
				// link.setActive(true);
				// }

				route = RouteAlloc.findWDMRoute(traffic, flag);
				ResourceAlloc.findBetterRoute(route, traffic, flag);
				if (DataSave.OSNR) {
					route = findMeetOSNRroute(route, traffic, flag, 1);
					if (route == null) {
						for (WDMLink link : canActiveLinks) {
							link.setActive(true);
						}
						route = findMeetOSNRroute(route, traffic, flag, 1);
					}
				}

				if (route != null && route.getWDMLinkList().size() != 0) {

					// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// 如果满足osnr要求
					// 为了避开OSNR 3期要改回去
					if (1 > 0) {
						Traffic tra = route.getBelongsTraffic();
						if (tra.isElectricalCrossConnection() == true && tra.getNrate() != 100) {
							if (CommonAlloc.EallocateWaveLength_workandpro(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
								// 端口分配没写，要改
								System.out.println("子波长业务" + traffic.getName() + "保护路由分配成功");
								CommonAlloc.fallBuffer.append("业务:" + traffic.getName() + "(" + traffic.getNrate()
										+ ")保护路由及资源分配成功！(子波长分配)\r\n");
								traffic.setProtectRoute(route);// 如果路由不为空则设置为工作路由
								traffic.setStatus(TrafficStatus.工作和保护已分配);
							} else {// 资源分配失败
								failedTraffic.add(traffic);
								System.out.println("业务" + traffic.getName() + "保护路由分配失败");
								traffic.setStatus(TrafficStatus.阻塞);// 如果还没有就是找不到
								CommonAlloc.fallBuffer.append(
										"业务:" + traffic.getName() + "(" + traffic.getNrate() + ")保护路由波长资源分配失败！\r\n");
								// RouteAlloc.releaseRoute(route);
							}
						} else if (tra.isElectricalCrossConnection() == false || tra.getNrate() == 100) {

							// 判断route可否使用一致波长
							ResourceAlloc.isRouteConsistent(route, traffic, 1, flag, canActiveLinks);

							// 100G业务还要考虑是否需要波长转换和osnr，如果这两个条件不满足，还要子波长分配
							if (CommonAlloc.allocateWaveLength1(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
								System.out.println("业务" + traffic.getName() + "保护路由资源分配成功");
								CommonAlloc.fallBuffer.append(
										"业务:" + traffic.getName() + "(" + traffic.getNrate() + ")保护路由及资源分配成功！\r\n");
								traffic.setProtectRoute(route);// 如果路由不为空则设置为工作路由
								traffic.setStatus(TrafficStatus.工作和保护已分配);
							} else {// 资源分配失败
								failedTraffic.add(traffic);
								protectFailure.add(traffic);
								System.out.println("业务" + traffic.getName() + "保护路由资源分配失败");
								traffic.setStatus(TrafficStatus.阻塞);// 如果还没有就是找不到
								CommonAlloc.fallBuffer.append(
										"业务:" + traffic.getName() + "(" + traffic.getNrate() + ")保护路由波长资源分配失败！\r\n");
								// RouteAlloc.releaseRoute(route);
							}
						}

					}
					// else {// 如果osnr超过门限
					// if (Suggest.isSuggested == true) {// 2017.10.14
					// OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
					// OSNR.OSNRRouteList.add(route);//10.19
					// }
					// CommonAlloc.fallBuffer.append("业务:" + traffic + "保护路由OSNR不满足条件！\r\n");
					// }
				} else {
					CommonAlloc.fallBuffer.append("业务:" + traffic + "保护路由算路失败！\r\n");
				}

			} // 保护路由计算完毕

			if (DataSave.separate1 == 2) {
				//////// 开始计算专享预置恢复路由
				if (traffic.getWorkRoute() != null && (traffic.getProtectLevel() == TrafficLevel.RESTORATION)
						|| traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION) {// 如果工作路由存在，并且保护等级包含重路由

					ResourceAlloc.setMustAvoidUnactive(traffic);

					// 设置工作路由不激活来计算预置路由
					ResourceAlloc.setRouteLinkUnactive(traffic.getWorkRoute());

					// 如果保护路由存在，则将保护路由设为不激活
					if (traffic.getProtectRoute() != null) {
						ResourceAlloc.setRouteLinkUnactive(traffic.getProtectRoute());
					}

					// 重设资源不足链路
					for (int i = 0; i < canActiveLinks.size(); i++) {
						canActiveLinks.get(i).setActive(true);
					}
					canActiveLinks = ResourceAlloc.resourceNotEnough(true);

					route = RouteAlloc.findWDMRoute(traffic, flag);
					ResourceAlloc.findBetterRoute(route, traffic, flag);
					if (DataSave.OSNR) {
						route = findMeetOSNRroute(route, traffic, flag, 2);
						if (route == null) {
							for (WDMLink link : canActiveLinks) {
								link.setActive(true);
							}
							route = findMeetOSNRroute(route, traffic, flag, 2);
						}
					}

					if (route != null && route.getWDMLinkList().size() != 0) {

						// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// 如果满足osnr要求
						// 避开OSNR
						if (1 > 0) {
							Traffic tra = route.getBelongsTraffic();
							if (tra.isElectricalCrossConnection() == true && tra.getNrate() < 100) {
								if (CommonAlloc.EallocateWaveLength_pre(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
									// 端口分配没写，要改
									System.out.println("子波长业务" + traffic.getName() + "预置路由波长资源分配成功");
									traffic.setPreRoute(route);// 如果路由不为空则设置预置重路由
									CommonAlloc.fallBuffer.append("业务:" + traffic.getName() + "(" + traffic.getNrate()
											+ ")预置路由波长资源分配成功！(子波长分配)\r\n");
									// traffic.setWorkRoute(route);// 如果路由不为空则设置为工作路由
									traffic.setStatus(TrafficStatus.工作和保护和重路由已分配);
								} else {// 资源分配失败
									CommonAlloc.fallBuffer.append("业务:" + traffic.getName() + "(" + traffic.getNrate()
											+ ")预置路由波长资源分配失败！\r\n");
									// RouteAlloc.releaseRoute(route);
								}
							} else if (tra.isElectricalCrossConnection() == false || tra.getNrate() == 100) {

								// 波长一致找路
								ResourceAlloc.isRouteConsistent(route, traffic, 2, flag, null);

								// 100G业务还要考虑是否需要波长转换和osnr，如果这两个条件不满足，还要子波长分配
								if (CommonAlloc.allocateWaveLength2(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
									System.out.println("业务" + traffic.getName() + "预置路由波长资源分配成功");
									traffic.setPreRoute(route);// 如果路由不为空则设置预置重路由
									CommonAlloc.fallBuffer.append("业务:" + traffic.getName() + "(" + traffic.getNrate()
											+ ")预置路由波长资源分配成功！\r\n");
									// traffic.setWorkRoute(route);// 如果路由不为空则设置为工作路由
									traffic.setStatus(TrafficStatus.工作和保护和重路由已分配);
								} else {// 资源分配失败
									// System.out.println(route);
									// for(int i=0;i<route.getWDMLinkList().size();i++) {
									// System.out.println(route.getWDMLinkList().get(i).getName()+"
									// 剩余"+route.getWDMLinkList().get(i).getRemainResource()+" pre使用"+
									// route.getWDMLinkList().get(i).preResource());
									// }
									CommonAlloc.fallBuffer.append("业务:" + traffic.getName() + "(" + traffic.getNrate()
											+ ")预置路由波长资源分配失败！\r\n");
									// RouteAlloc.releaseRoute(route);
								}
							}
						}
						// else {// 如果osnr超过门限
						// if (Suggest.isSuggested == true) {// 2017.10.14
						// OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
						// OSNR.OSNRRouteList.add(route);//10.19
						// }
						// CommonAlloc.fallBuffer.append("业务:" + traffic + "保护路由OSNR不满足条件！\r\n");
						// }
					} else {
						CommonAlloc.fallBuffer.append("业务:" + traffic + "预置路由算路失败！\r\n");
					}

				} // 专享预置恢复路由
			}

			// 将所有链路设为激活
			for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
				WDMLink.WDMLinkList.get(i).setActive(true);
			}
			for (int j = 0; j < CommonNode.allNodeList.size(); j++) {
				CommonNode.allNodeList.get(j).setActive(true);
			}

		}

		if (DataSave.separate1 == 1) {

			// 计算预置路由
			List<Traffic> newlist = Traffic.reRankList(traList);
			for (int i = 0; i < newlist.size(); i++) {
				Traffic traffic = traList.get(i);

				if (traffic.getWorkRoute() != null && (traffic.getProtectLevel() == TrafficLevel.RESTORATION)
						|| traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION) {
					// 1.设置链路及节点的激活状态
					// 将必避链路、节点设为不激活
					ResourceAlloc.setMustAvoidUnactive(traffic);
					// 将工作路由及保护设为不激活
					LinkedList<WDMLink> workList = ResourceAlloc.setRouteLinkUnactive(traffic.getWorkRoute());
					ResourceAlloc.setRouteLinkUnactive(traffic.getProtectRoute());
					// 将关联业务组的工作路由设为不激活
					LinkedList<WDMLink> relatedList = ResourceAlloc.setRelatedTrafficUnactive(traffic,
							DataSave.separate);

					// 2.计算路由
					Route preroute = RouteAlloc.findWDMRoute(traffic, flag);
					ResourceAlloc.findBetterRoute(preroute, traffic, flag);
					if (preroute == null || preroute.getWDMLinkList().size() == 0) {
						for (WDMLink link : relatedList) {
							link.setActive(true);
						}
						for (WDMLink link : workList) {
							link.setActive(true);
						}
						preroute = RouteAlloc.findWDMRoute(traffic, flag);
						ResourceAlloc.findBetterRoute(preroute, traffic, flag);
					}

					if (traffic.getId() > 1) {
						Traffic last = Traffic.getTraffic(traffic.getId() - 1);
						if (last.getRankId() == traffic.getRankId() && last.getPreRoute() != null) {

							List<WDMLink> wDMLinkList = new LinkedList<>();
							for (WDMLink link : last.getPreRoute().getWDMLinkList()) {
								wDMLinkList.add(link);
							}
							List<CommonNode> nodelist = new LinkedList<>();
							for (CommonNode node : last.getPreRoute().getNodeList()) {
								nodelist.add(node);
							}

							preroute.setWDMLinkList(wDMLinkList);
							preroute.setNodeList(nodelist);
						}
					}

					if (DataSave.OSNR) {
						preroute = findMeetOSNRroute(preroute, traffic, flag, 2);
					}

					// 3.波长分配
					if (preroute != null && preroute.getWDMLinkList().size() != 0) {
						if (1 > 0) {
							Traffic tra = traffic;
							if (tra.isElectricalCrossConnection() == true && tra.getNrate() < 100) {
								if (CommonAlloc.EallocateWaveLength_pre(preroute)) {// 对光纤链路分配时隙成功，并且分配端口成功
									// 端口分配没写，要改
									System.out.println("子波长业务" + traffic.getName() + "预置路由波长资源分配成功");
									traffic.setPreRoute(preroute);// 如果路由不为空则设置预置重路由
									CommonAlloc.fallBuffer.append("业务:" + traffic.getName() + "(" + traffic.getNrate()
											+ ")预置路由波长资源分配成功！(子波长分配)\r\n");
									// traffic.setWorkRoute(route);// 如果路由不为空则设置为工作路由
									traffic.setStatus(TrafficStatus.工作和保护和重路由已分配);
								} else {// 资源分配失败
									CommonAlloc.fallBuffer.append("业务:" + traffic.getName() + "(" + traffic.getNrate()
											+ ")预置路由波长资源分配失败！\r\n");
									// RouteAlloc.releaseRoute(route);
								}
							} else if (tra.isElectricalCrossConnection() == false || tra.getNrate() == 100) {

								// 波长一致找路
								ResourceAlloc.isRouteConsistent(preroute, traffic, 2, flag, null);

								// 100G业务还要考虑是否需要波长转换和osnr，如果这两个条件不满足，还要子波长分配
								if (CommonAlloc.allocateWaveLength2(preroute)) {// 对光纤链路分配时隙成功，并且分配端口成功
									System.out.println("业务" + traffic.getName() + "预置路由波长资源分配成功");
									traffic.setPreRoute(preroute);// 如果路由不为空则设置预置重路由
									CommonAlloc.fallBuffer.append("业务:" + traffic.getName() + "(" + traffic.getNrate()
											+ ")预置路由波长资源分配成功！\r\n");
									// traffic.setWorkRoute(route);// 如果路由不为空则设置为工作路由
									traffic.setStatus(TrafficStatus.工作和保护和重路由已分配);
								} else {// 资源分配失败
									// System.out.println(route);
									// for(int i=0;i<route.getWDMLinkList().size();i++) {
									// System.out.println(route.getWDMLinkList().get(i).getName()+"
									// 剩余"+route.getWDMLinkList().get(i).getRemainResource()+" pre使用"+
									// route.getWDMLinkList().get(i).preResource());
									// }
									CommonAlloc.fallBuffer.append("业务:" + traffic.getName() + "(" + traffic.getNrate()
											+ ")预置路由波长资源分配失败！\r\n");
									// RouteAlloc.releaseRoute(route);
								}
							}
						}
					} else {
						CommonAlloc.fallBuffer.append("业务:" + traffic + "预置路由算路失败！\r\n");
					}
				}
			}
		}

		// 业务循环结束
		ResourceLogTxt.ResourceLogTxt();// 输出资源分配日志.
		CommonAlloc.fallBuffer.delete(0, CommonAlloc.fallBuffer.length());
		// System.out.println(CommonNode.allNodeList);//12.14

		trafficList.clear();
		Dlg_PolicySetting.isDesign = true; // CC2017.5.2

	}

	public static void allocateResource1(List<Traffic> traList, int flag) {// flag=0:最短长度；=1：最小跳
		Survivance.mark = flag;// 用于抗毁仿真中标记分配的时候使用的哪种路由算法
		CommonAlloc.fallBuffer = new StringBuffer();// 链路资源分配日志
		PortAlloc.portFallBuffer = new StringBuffer();// 端口资源分配日志

		trafficList = new LinkedList<Traffic>();

		// trafficList.addAll(traList);// 镜像了一份业务列表，对trafficList的操作不会影响traList
		// Collections.reverse(traList);
		for (Traffic tra : traList) {
			if (tra.getTrafficgroup() != null) {
				trafficList.add(tra);
			}
		}
		for (Traffic tra : traList) {
			if (tra.getTrafficgroup() == null) {
				trafficList.add(tra);
			}
		}

		// 存放工作路由计算失败的业务
		LinkedList<Traffic> workFailure = new LinkedList<>();
		// 存放保护路由计算失败的业务
		LinkedList<Traffic> protectFailure = new LinkedList<>();
		// 存放预置路由计算失败的业务
		LinkedList<Traffic> preFailure = new LinkedList<>();

		Route route = null;
		OSNR osnr = new OSNR();
		if (!RouteAlloc.isLimitHop)
			RouteAlloc.hopLimit = Integer.MAX_VALUE; // 新增跳数限制(不过不限制跳数，就把跳数设置为无穷大)
		else if (RouteAlloc.hopLimit == Integer.MAX_VALUE)
			RouteAlloc.hopLimit = 5; // 默认跳数限制为5

		Iterator<Traffic> it = trafficList.iterator();
		while (it.hasNext()) {// 循环计算链表里每个业务的工作路由/保护路由（如果有）

			index++;
			Traffic traffic = it.next();

			// 将必避链路、节点设为不激活
			ResourceAlloc.setMustAvoidUnactive(traffic);

			// 关联业务组设置设置不激活（包括设置相应的SRLG为不激活）
			ResourceAlloc.setRelatedTrafficUnactive(traffic, DataSave.separate);

			// 将资源不足的链路设为不激活 输入：是否是计算预置路由
			LinkedList<WDMLink> canActiveLinks = ResourceAlloc.resourceNotEnough(false);

			// 将有SRLG的链路设为不激活
			// LinkedList<WDMLink> srlgLink = ResourceAlloc.setSRLGFalse();

			// 必经节点、必经链路,算路
			route = ResourceAlloc.routeForTraffic(traffic, flag);

			// 关联业务组业务，当路由中链路有平行边时，优先使用没有SRLG的链路
			if (traffic.getTrafficgroup() != null) {
				if ((traffic.getTrafficgroup().getBelongGroup().equals("A")
						|| traffic.getTrafficgroup().getBelongGroup().equals("B")) && route != null) {
					for (int i = 0; i < route.getWDMLinkList().size(); i++) {
						WDMLink link = route.getWDMLinkList().get(i);
						if (link.getParallelLinkList().size() > 1) {
							LinkedList<WDMLink> noSRLGLink = new LinkedList<>();
							for (int j = 0; j < link.getParallelLinkList().size(); j++) {
								if (link.getParallelLinkList().get(j).belongSRLG() == null
										&& link.getParallelLinkList().get(j).isActive()) {
									noSRLGLink.add(link.getParallelLinkList().get(j));
								}
							}
							if (noSRLGLink.size() > 0) {
								WDMLink newLink = noSRLGLink.get(0);
								for (int z = 0; z < noSRLGLink.size(); z++) {
									if (noSRLGLink.get(z).getRemainResource() > newLink.getRemainResource()) {
										newLink = noSRLGLink.get(z);
									}
								}
								route.getWDMLinkList().set(i, newLink);
							}
						}
					}
				}
			}
			// 关联业务组业务，应该尽量用一个链路，而不是分散在多个平行边上
			if (traffic.getTrafficgroup() != null && traffic.getTrafficNum() > 1) {
				int traid = traffic.getId();
				Traffic lastTraffic = Traffic.getTraffic(traid - 1);
				if (lastTraffic.getRankId() == traffic.getRankId()) {
					Route lastTrafficroute = lastTraffic.getWorkRoute();
					if (Route.isPassSameNode(route, lastTrafficroute))
						Route.setPassSameLink(lastTrafficroute, route);
				}
			}
			if (traffic.getTrafficgroup() != null && traffic.getTrafficNum() > 1 && traffic.getId() != 1) {
				int traid = traffic.getId();
				Traffic lastTraffic = Traffic.getTraffic(traid - 1);
				if (lastTraffic.getRankId() == traffic.getRankId()) {
					Route lastTrafficroute = lastTraffic.getWorkRoute();
					if (Route.consistWaveNum(lastTrafficroute, 1, traffic).size() > 0) {
						route.setWDMLinkList(lastTrafficroute.getWDMLinkList());
						route.setNodeList(lastTrafficroute.getNodeList());
					}
				}
			}
			// 关联业务组最好使用同一个路由

			if (DataSave.OSNR) {
				System.out.println("HELLOHELLOHELLO");
				route = findMeetOSNRroute(route, traffic, flag, 1);
				if (route == null) {
					for (WDMLink link : canActiveLinks) {
						link.setActive(true);
					}
					route = findMeetOSNRroute(route, traffic, flag, 1);
				}
			}

			// if(!ResourceAlloc.isRealtedTrafficRouteSucced(route, traffic, flag));

			if (route != null && route.getWDMLinkList().size() != 0 && traffic.getWorkRoute() == null) {
				// System.out.println("os1:"+osnr.calculateOSNR(route));
				// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// 如果满足osnr要求
				if (1 > 0) {// 为了避开OSNR限制 做3期时要改掉
					Traffic tra = route.getBelongsTraffic();
					if (tra.isElectricalCrossConnection() == true && tra.getNrate() < 100) {
						if (CommonAlloc.EallocateWaveLength_workandpro(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
							// 端口分配没写，要改
							System.out.println("子波长业务" + traffic.getName() + "工作路由分配成功");
							CommonAlloc.fallBuffer.append(
									"业务:" + traffic.getName() + "(" + traffic.getNrate() + ")工作路由及资源分配成功！(子波长分配)\r\n");
							traffic.setWorkRoute(route);// 如果路由不为空则设置为工作路由
							traffic.setStatus(TrafficStatus.工作已分配);
						} else {// 资源分配失败
							failedTraffic.add(traffic);
							System.out.println("业务" + traffic.getName() + "工作路由分配失败");
							traffic.setStatus(TrafficStatus.阻塞);// 如果还没有就是找不到
							CommonAlloc.fallBuffer.append(
									"业务:" + traffic.getName() + "(" + traffic.getNrate() + ")工作路由波长资源分配失败！\r\n");
							RouteAlloc.releaseRoute(route);
						}
					} else if (tra.isElectricalCrossConnection() == false || tra.getNrate() == 100) {
						// 100G业务还要考虑是否需要波长转换和osnr，如果这两个条件不满足，还要子波长分配

						// 判断route可否使用一致波长
						ResourceAlloc.isRouteConsistent(route, traffic, 1, flag, canActiveLinks);

						if (CommonAlloc.allocateWaveLength(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
							System.out.println("业务" + traffic.getName() + "工作路由分配成功");
							CommonAlloc.fallBuffer
									.append("业务:" + traffic.getName() + "(" + traffic.getNrate() + ")工作路由及资源分配成功！\r\n");
							traffic.setWorkRoute(route);// 如果路由不为空则设置为工作路由
							traffic.setStatus(TrafficStatus.工作已分配);
						} else {// 资源分配失败

							failedTraffic.add(traffic);
							workFailure.add(traffic);
							System.out.println("业务" + traffic.getName() + "工作路由分配失败");
							traffic.setStatus(TrafficStatus.阻塞);// 如果还没有就是找不到
							CommonAlloc.fallBuffer.append(
									"业务:" + traffic.getName() + "(" + traffic.getNrate() + ")工作路由波长资源分配失败！\r\n");
							RouteAlloc.releaseRoute(route);
						}
					}
				}
				// else {// 如果osnr超过门限
				// if (Suggest.isSuggested == true) {// 2017.10.14
				// OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
				// OSNR.OSNRRouteList.add(route);//10.19
				// }
				// CommonAlloc.fallBuffer.append("业务:" + traffic + "工作路由OSNR不满足条件！\r\n");
				// }
			} else {// wdmlink链路为空

				if (traffic.getWorkRoute() == null) {
					traffic.setStatus(TrafficStatus.规划业务阻塞);// 如果还没有就是找不到
					CommonAlloc.fallBuffer.append("业务:" + traffic + "工作路由算路失败！\r\n");
				}
			}

			//////// 开始计算保护路由
			if (traffic.getWorkRoute() != null && (traffic.getProtectLevel() == TrafficLevel.NORMAL11
					|| traffic.getProtectLevel() == TrafficLevel.PERMANENT11
					|| traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION)) {

				// 设置工作路由不激活来计算保护路由
				ResourceAlloc.setRouteLinkUnactive(traffic.getWorkRoute());

				// //将有srlg的link激活
				// for(WDMLink link : srlgLink) {
				// link.setActive(true);
				// }

				route = RouteAlloc.findWDMRoute(traffic, flag);
				ResourceAlloc.findBetterRoute(route, traffic, flag);

				if (DataSave.OSNR) {
					System.out.println("HELLOHELLOHELLO");
					route = findMeetOSNRroute(route, traffic, flag, 1);
					if (route == null) {
						for (WDMLink link : canActiveLinks) {
							link.setActive(true);
						}
						route = findMeetOSNRroute(route, traffic, flag, 1);
					}
				}

				if (route != null && route.getWDMLinkList().size() != 0) {

					// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// 如果满足osnr要求
					// 为了避开OSNR 3期要改回去
					if (1 > 0) {
						Traffic tra = route.getBelongsTraffic();
						if (tra.isElectricalCrossConnection() == true && tra.getNrate() != 100) {
							if (CommonAlloc.EallocateWaveLength_workandpro(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
								// 端口分配没写，要改
								System.out.println("子波长业务" + traffic.getName() + "保护路由分配成功");
								CommonAlloc.fallBuffer.append("业务:" + traffic.getName() + "(" + traffic.getNrate()
										+ ")保护路由及资源分配成功！(子波长分配)\r\n");
								traffic.setProtectRoute(route);// 如果路由不为空则设置为工作路由
								traffic.setStatus(TrafficStatus.工作和保护已分配);
							} else {// 资源分配失败
								failedTraffic.add(traffic);
								System.out.println("业务" + traffic.getName() + "保护路由分配失败");
								traffic.setStatus(TrafficStatus.阻塞);// 如果还没有就是找不到
								CommonAlloc.fallBuffer.append(
										"业务:" + traffic.getName() + "(" + traffic.getNrate() + ")保护路由波长资源分配失败！\r\n");
								// RouteAlloc.releaseRoute(route);
							}
						} else if (tra.isElectricalCrossConnection() == false || tra.getNrate() == 100) {

							// 判断route可否使用一致波长
							ResourceAlloc.isRouteConsistent(route, traffic, 1, flag, canActiveLinks);

							// 100G业务还要考虑是否需要波长转换和osnr，如果这两个条件不满足，还要子波长分配
							if (CommonAlloc.allocateWaveLength1(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
								System.out.println("业务" + traffic.getName() + "保护路由资源分配成功");
								CommonAlloc.fallBuffer.append(
										"业务:" + traffic.getName() + "(" + traffic.getNrate() + ")保护路由及资源分配成功！\r\n");
								traffic.setProtectRoute(route);// 如果路由不为空则设置为工作路由
								traffic.setStatus(TrafficStatus.工作和保护已分配);
							} else {// 资源分配失败
								failedTraffic.add(traffic);
								protectFailure.add(traffic);
								System.out.println("业务" + traffic.getName() + "保护路由资源分配失败");
								traffic.setStatus(TrafficStatus.阻塞);// 如果还没有就是找不到
								CommonAlloc.fallBuffer.append(
										"业务:" + traffic.getName() + "(" + traffic.getNrate() + ")保护路由波长资源分配失败！\r\n");
								// RouteAlloc.releaseRoute(route);
							}
						}

					}
					// else {// 如果osnr超过门限
					// if (Suggest.isSuggested == true) {// 2017.10.14
					// OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
					// OSNR.OSNRRouteList.add(route);//10.19
					// }
					// CommonAlloc.fallBuffer.append("业务:" + traffic + "保护路由OSNR不满足条件！\r\n");
					// }
				} else {
					CommonAlloc.fallBuffer.append("业务:" + traffic + "保护路由算路失败！\r\n");
				}

			} // 保护路由计算完毕

			// 从新激活必避站点和链路
			// 将必避链路设为激活
			// if(traffic.getMustAvoidLink()!=null)
			// {WDMLink.getLink(traffic.getMustAvoidLink().getName()).setActive(true);}
			// //将与避避节点相连的链路设为激活
			// if(traffic.getMustAvoidNode()!=null)
			// {CommonNode mANode=traffic.getMustAvoidNode();//必避节点
			// for(int i=0;i<WDMLink.WDMLinkList.size();i++) {
			// if(WDMLink.WDMLinkList.get(i).getFromNode().getName().equals(mANode.getName())
			// ||WDMLink.WDMLinkList.get(i).getToNode().getName().equals(mANode.getName()))
			// WDMLink.WDMLinkList.get(i).setActive(true);
			// }}
			//
			//
			// //从新激活关联业务组的业务
			// if(traffic.getTrafficgroup()!=null)//如果业务的关联业务组不为空
			// {
			// for(int i=0;i<Traffic.trafficList.size();i++) {
			// Traffic tra=Traffic.trafficList.get(i);
			// //判断是否是一个风险共享业务组并且不是同一个业务
			// if(tra.getTrafficgroup()!=null&&tra.getTrafficgroup().getId()==traffic.getTrafficgroup().getId()
			// &&tra.getRankId()!=traffic.getRankId())
			// {
			// if(tra.getWorkRoute()!=null) {//如果相关联的业务工作路由存在
			// for(int j=0;j<tra.getWorkRoute().getWDMLinkList().size();j++)
			// tra.getWorkRoute().getWDMLinkList().get(j).setActive(true);
			// }
			// }
			// }
			// }
			// 将所有链路设为激活
			for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
				WDMLink.WDMLinkList.get(i).setActive(true);
			}
			for (int j = 0; j < CommonNode.allNodeList.size(); j++) {
				CommonNode.allNodeList.get(j).setActive(true);
			}

		} // 业务循环结束
		ResourceLogTxt.ResourceLogTxt();// 输出资源分配日志.
		CommonAlloc.fallBuffer.delete(0, CommonAlloc.fallBuffer.length());
		// System.out.println(CommonNode.allNodeList);//12.14

		trafficList.clear();
		Dlg_PolicySetting.isDesign = true; // CC2017.5.2

	}

	public static void allocateResource2(List<Traffic> traList, int flag) {// flag=0:最短长度；=1：最小跳
		CommonAlloc.fallBuffer = new StringBuffer();// 链路资源分配日志
		PortAlloc.portFallBuffer = new StringBuffer();// 端口资源分配日志

		trafficList = new LinkedList<Traffic>();

		trafficList.addAll(traList);// 镜像了一份业务列表，对trafficList的操作不会影响traList
		Route route = null;
		OSNR osnr = new OSNR();
		if (!RouteAlloc.isLimitHop)
			RouteAlloc.hopLimit = Integer.MAX_VALUE; // 新增跳数限制(不过不限制跳数，就把跳数设置为无穷大)
		else if (RouteAlloc.hopLimit == Integer.MAX_VALUE)
			RouteAlloc.hopLimit = 5; // 默认跳数限制为5
		Iterator<Traffic> it = traList.iterator();
		while (it.hasNext()) {// 循环计算链表里每个业务的工作路由/保护路由（如果有）
			/*
			 * 12.26之前必经必避设的是wdmlink时 // 将链路上没有可用波道资源的链路设为不激活 for (int i = 0; i <
			 * WDMLink.WDMLinkList.size(); i++) { if
			 * (WDMLink.WDMLinkList.get(i).getRemainResource() == 0)
			 * WDMLink.WDMLinkList.get(i).setActive(false); } Traffic traffic = it.next();
			 * // 将必避链路设为不激活 if (traffic.getMustAvoidLink() != null) {
			 * traffic.getMustAvoidLink().setActive(false); } // 将与避避节点相连的链路设为不激活 if
			 * (traffic.getMustAvoidNode() != null) { CommonNode mANode =
			 * traffic.getMustAvoidNode();// 必避节点 for (int i = 0; i <
			 * WDMLink.WDMLinkList.size(); i++) { if
			 * (WDMLink.WDMLinkList.get(i).getFromNode().getName().equals(mANode.getName())
			 * || WDMLink.WDMLinkList.get(i).getToNode().getName().equals(mANode.getName()))
			 * WDMLink.WDMLinkList.get(i).setActive(false); } }
			 * 
			 * // 关联业务组 if (traffic.getTrafficgroup() != null)// 如果业务的关联业务组不为空 { for (int i
			 * = 0; i < Traffic.trafficList.size(); i++) { Traffic tra =
			 * Traffic.trafficList.get(i); // 判断是否是一个风险共享业务组并且不是同一个业务 if
			 * (tra.getTrafficgroup() != null && tra.getTrafficgroup().getId() ==
			 * traffic.getTrafficgroup().getId() && tra.getRankId() != traffic.getRankId())
			 * { if (tra.getWorkRoute() != null) {// 如果相关联的业务工作路由存在 for (int j = 0; j <
			 * tra.getWorkRoute().getWDMLinkList().size(); j++)
			 * tra.getWorkRoute().getWDMLinkList().get(j).setActive(false); } } } }
			 * 
			 * // 关联业务组-如果有关联业务组，则把与之相关联的业务的工作路由链路设为false // if(traffic.getM_cGroup()!=null)
			 * { // int groupId=traffic.getM_cGroup().getGroupId(); // for(int
			 * i=0;i<TrafficGroup.grouptrafficGroupList.size();i++) //
			 * if(TrafficGroup.grouptrafficGroupList.get(i).getRelatedId()==groupId)//
			 * 找出与之关联的业务组 // { // for(int //
			 * j=0;j<TrafficGroup.grouptrafficGroupList.get(i).getTrafficList().size();j++)
			 * // { //如果工作路由存在 //
			 * if(TrafficGroup.grouptrafficGroupList.get(i).getTrafficList().get(j).
			 * getWorkRoute()!=null) // { //将工作路由的WDMLink设为false // for(int //
			 * k=0;k<TrafficGroup.grouptrafficGroupList.get(i).getTrafficList().get(j).
			 * getWorkRoute().getWDMLinkList().size();k++) // { //
			 * TrafficGroup.grouptrafficGroupList.get(i).getTrafficList().get(j).
			 * getWorkRoute().getWDMLinkList().get(k).setActive(false); // } // } // } // }
			 * // }
			 * 
			 * if (traffic.getMustPassLink() == null && traffic.getMustPassNode() == null) {
			 * route = RouteAlloc.findWDMRoute(traffic, flag); } if
			 * (traffic.getMustPassNode() != null && traffic.getMustPassLink() == null) {
			 * Route route1 = null; route1 =
			 * RouteAlloc.findWDMRouteHaveMustPassNode1(traffic, flag,
			 * traffic.getMustPassNode()); Route route2 = null; // 为避免重复需要把route1相应链路设为不激活
			 * if (route1 != null) { List<WDMLink> list = route1.getWDMLinkList(); for
			 * (WDMLink wdmLink : list) { wdmLink.setActive(false); for (FiberLink link :
			 * wdmLink.getFiberLinkList()) { link.setActive(false); } } } route2 =
			 * RouteAlloc.findWDMRouteHaveMustPassNode2(traffic, flag,
			 * traffic.getMustPassNode());
			 * 
			 * if (route1 != null && route2 != null) { route = Route.combineRoute(route1,
			 * route2); route.setBelongsTraffic(traffic);
			 * route.setFrom(traffic.getFromNode()); route.setTo(traffic.getToNode()); } }
			 * if (traffic.getMustPassLink() != null && traffic.getMustPassNode() == null) {
			 * Route route1 = null; route1 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag,
			 * traffic.getFromNode(), traffic.getMustPassLink().getFromNode()); Route route2
			 * = null; // 为避免重复需要把route1相应链路设为不激活 if (route1 != null) { List<WDMLink> list =
			 * route1.getWDMLinkList(); for (WDMLink wdmLink : list) {
			 * wdmLink.setActive(false); for (FiberLink link : wdmLink.getFiberLinkList()) {
			 * link.setActive(false); } } } route2 =
			 * RouteAlloc.findWDMRouteByTwoNode(traffic, flag,
			 * traffic.getMustPassLink().getToNode(), traffic.getToNode()); //
			 * 由于链路的的toNode和fromNode变来变去，有可能造成route1与route2是分离的要判断一下 if (route1 != null &&
			 * route2 != null) { if
			 * (route1.getNodeList().get(0).getName().equals(route2.getNodeList().get(0).
			 * getName()) || route1.getNodeList().get(route1.getNodeList().size() -
			 * 1).getName() .equals(route2.getNodeList().get(route2.getNodeList().size() -
			 * 1).getName())) { route1 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag,
			 * traffic.getFromNode(), traffic.getMustPassLink().getToNode()); if (route1 !=
			 * null) { List<WDMLink> list = route1.getWDMLinkList(); for (WDMLink wdmLink :
			 * list) { wdmLink.setActive(false); for (FiberLink link :
			 * wdmLink.getFiberLinkList()) { link.setActive(false); } } } route2 =
			 * RouteAlloc.findWDMRouteByTwoNode(traffic, flag,
			 * traffic.getMustPassLink().getFromNode(), traffic.getToNode()); } } route =
			 * Route.combineRoute1(route1, route2, traffic.getMustPassLink());
			 * route.setBelongsTraffic(traffic); route.setFrom(traffic.getFromNode());
			 * route.setTo(traffic.getToNode()); }
			 */
			// System.out.println(route);

			// 将链路上没有可用波道资源的链路设为不激活
			for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
				if ((WDMLink.WDMLinkList.get(i).getRemainResource() == 0))
					WDMLink.WDMLinkList.get(i).setActive(false);
			}
			Traffic traffic = it.next();
			// 将必避链路设为不激活
			if (traffic.getMustAvoidLink() != null) {
				// 找出fiberlink对应的wdmlink
				WDMLink wdmMustAvoidLink = traffic.getMustAvoidLink().getBelongWDMLink();
				if (wdmMustAvoidLink != null) {
					wdmMustAvoidLink.setActive(false);
				}
			}
			// 将与避避节点相连的链路设为不激活
			if (traffic.getMustAvoidNode() != null) {
				CommonNode mANode = traffic.getMustAvoidNode();// 必避节点
				for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
					if (WDMLink.WDMLinkList.get(i).getFromNode().getName().equals(mANode.getName())
							|| WDMLink.WDMLinkList.get(i).getToNode().getName().equals(mANode.getName()))
						WDMLink.WDMLinkList.get(i).setActive(false);
				}
			}

			if (traffic.getTrafficgroup() != null)// 如果业务的关联业务组不为空
			{
				for (int i = 0; i < Traffic.trafficList.size(); i++) {
					Traffic tra = Traffic.trafficList.get(i);
					// 判断是否是一个风险共享业务组并且不是同一个业务
					if (tra.getTrafficgroup() != null
							&& tra.getTrafficgroup().getId() == traffic.getTrafficgroup().getId()
							&& tra.getRankId() != traffic.getRankId()) {
						if (tra.getWorkRoute() != null) {// 如果相关联的业务工作路由存在
							for (int j = 0; j < tra.getWorkRoute().getWDMLinkList().size(); j++)
								tra.getWorkRoute().getWDMLinkList().get(j).setActive(false);
						}
					}
				}
			}

			// if(traffic.getM_cGroup()!=null) {
			// int groupId=traffic.getM_cGroup().getGroupId();
			// for(int i=0;i<TrafficGroup.grouptrafficGroupList.size();i++)//循环所有业务组
			// if(TrafficGroup.grouptrafficGroupList.get(i).getRelatedId()==groupId)//找出与之关联的业务组
			// {
			// for(int
			// j=0;j<TrafficGroup.grouptrafficGroupList.get(i).getTrafficList().size();j++)//循环该业务组的业务
			// { //如果工作路由存在
			// if(TrafficGroup.grouptrafficGroupList.get(i).getTrafficList().get(j).getWorkRoute()!=null)//如果该业务有工作路由
			// { //则将工作路由的WDMLink设为false
			// for(int
			// k=0;k<TrafficGroup.grouptrafficGroupList.get(i).getTrafficList().get(j).getWorkRoute().getWDMLinkList().size();k++)
			// {
			// TrafficGroup.grouptrafficGroupList.get(i).getTrafficList().get(j).getWorkRoute().getWDMLinkList().get(k).setActive(false);
			// }
			// }
			// }
			// }
			// }
			if (traffic.getMustPassLink() == null && traffic.getMustPassNode() == null) {
				route = RouteAlloc.findWDMRoute(traffic, flag);
			}
			if (traffic.getMustPassNode() != null && traffic.getMustPassLink() == null) {
				Route route1 = null;
				route1 = RouteAlloc.findWDMRouteHaveMustPassNode1(traffic, flag, traffic.getMustPassNode());
				Route route2 = null;
				// 为避免重复需要把route1相应链路设为不激活
				if (route1 != null) {
					List<WDMLink> list = route1.getWDMLinkList();
					for (WDMLink wdmLink : list) {
						wdmLink.setActive(false);
						for (FiberLink link : wdmLink.getFiberLinkList()) {
							link.setActive(false);
						}
					}
				}
				route2 = RouteAlloc.findWDMRouteHaveMustPassNode2(traffic, flag, traffic.getMustPassNode());

				if (route1 != null && route2 != null) {
					route = Route.combineRoute(route1, route2);
					route.setBelongsTraffic(traffic);
					route.setFrom(traffic.getFromNode());
					route.setTo(traffic.getToNode());
				}
			}
			if (traffic.getMustPassLink() != null && traffic.getMustPassNode() == null) {
				// 找出fiberlink对应的wdmlink
				WDMLink wdmMustPassLink = traffic.getMustPassLink().getBelongWDMLink();
				Route route1 = null;
				route1 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, traffic.getFromNode(),
						wdmMustPassLink.getFromNode());
				Route route2 = null;
				// 为避免重复需要把route1相应链路设为不激活
				if (route1 != null) {
					List<WDMLink> list = route1.getWDMLinkList();
					for (WDMLink wdmLink : list) {
						wdmLink.setActive(false);
						for (FiberLink link : wdmLink.getFiberLinkList()) {
							link.setActive(false);
						}
					}
				}
				route2 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, wdmMustPassLink.getToNode(),
						traffic.getToNode());
				// 由于链路的的toNode和fromNode变来变去，有可能造成route1与route2是分离的要判断一下
				if (route1 != null && route2 != null) {
					if (route1.getNodeList().get(0).getName().equals(route2.getNodeList().get(0).getName())
							|| route1.getNodeList().get(route1.getNodeList().size() - 1).getName()
									.equals(route2.getNodeList().get(route2.getNodeList().size() - 1).getName())) {
						route1 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, traffic.getFromNode(),
								wdmMustPassLink.getToNode());
						if (route1 != null) {
							List<WDMLink> list = route1.getWDMLinkList();
							for (WDMLink wdmLink : list) {
								wdmLink.setActive(false);
								for (FiberLink link : wdmLink.getFiberLinkList()) {
									link.setActive(false);
								}
							}
						}
						route2 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, wdmMustPassLink.getFromNode(),
								traffic.getToNode());
					}
				}
				route = Route.combineRoute1(route1, route2, wdmMustPassLink);
				route.setBelongsTraffic(traffic);
				route.setFrom(traffic.getFromNode());
				route.setTo(traffic.getToNode());
			}

			if (route != null && route.getWDMLinkList().size() != 0 && traffic.getWorkRoute() == null) {
				// System.out.println(route);
				// System.out.println("os1:"+osnr.calculateOSNR(route));
				// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// 如果满足osnr要求
				if (1 > 0) {// 为了避开OSNR限制 做3期时要改掉
					Traffic tra = route.getBelongsTraffic();
					if (tra.isElectricalCrossConnection() == true && tra.getNrate() < 100) {
						if (CommonAlloc.EallocateWaveLength_workandpro(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
							// 端口分配没写，要改
							System.out.println("子波长业务" + traffic.getName() + "工作路由分配成功");
							CommonAlloc.fallBuffer.append(
									"业务:" + traffic.getName() + "(" + traffic.getNrate() + ")工作路由及资源分配成功！(子波长分配)\r\n");
							traffic.setWorkRoute(route);// 如果路由不为空则设置为工作路由
							traffic.setStatus(TrafficStatus.工作已分配);
						} else {// 资源分配失败
							failedTraffic.add(traffic);
							System.out.println("业务" + traffic.getName() + "工作路由分配失败");
							traffic.setStatus(TrafficStatus.阻塞);// 如果还没有就是找不到
							CommonAlloc.fallBuffer.append(
									"业务:" + traffic.getName() + "(" + traffic.getNrate() + ")工作路由波长资源分配失败！\r\n");
							RouteAlloc.releaseRoute(route);
						}
					} else if (tra.isElectricalCrossConnection() == false || tra.getNrate() == 100) {
						// 100G业务还要考虑是否需要波长转换和osnr，如果这两个条件不满足，还要子波长分配

						// 判断route可否使用一致波长
						ResourceAlloc.isRouteConsistent(route, traffic, 1, flag, null);

						if (CommonAlloc.allocateWaveLength(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
							System.out.println("业务" + traffic.getName() + "工作路由分配成功");
							CommonAlloc.fallBuffer
									.append("业务:" + traffic.getName() + "(" + traffic.getNrate() + ")工作路由及资源分配成功！\r\n");
							traffic.setWorkRoute(route);// 如果路由不为空则设置为工作路由
							traffic.setStatus(TrafficStatus.工作已分配);
						} else {// 资源分配失败
							failedTraffic.add(traffic);
							System.out.println("业务" + traffic.getName() + "工作路由分配失败");
							traffic.setStatus(TrafficStatus.阻塞);// 如果还没有就是找不到
							CommonAlloc.fallBuffer.append(
									"业务:" + traffic.getName() + "(" + traffic.getNrate() + ")工作路由波长资源分配失败！\r\n");
							RouteAlloc.releaseRoute(route);
						}
					}
				}
				// else {// 如果osnr超过门限
				// if (Suggest.isSuggested == true) {// 2017.10.14
				// OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
				// OSNR.OSNRRouteList.add(route);//10.19
				// }
				// CommonAlloc.fallBuffer.append("业务:" + traffic + "工作路由OSNR不满足条件！\r\n");
				// }
			} else {// wdmlink链路为空
				if (traffic.getWorkRoute() == null) {
					traffic.setStatus(TrafficStatus.规划业务阻塞);// 如果还没有就是找不到
					CommonAlloc.fallBuffer.append("业务:" + traffic + "工作路由算路失败！\r\n");
				}
			}

			//////// 开始计算保护路由
			if (traffic.getWorkRoute() != null && (traffic.getProtectLevel() == TrafficLevel.NORMAL11
					|| traffic.getProtectLevel() == TrafficLevel.PERMANENT11
					|| traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION)) {
				List<WDMLink> workLinkList = traffic.getWorkRoute().getWDMLinkList();
				for (WDMLink wdmLink : workLinkList) {
					wdmLink.setActive(false);
					for (FiberLink link : wdmLink.getFiberLinkList()) {// 设置工作路由不激活来计算保护路由
						link.setActive(false);
					}
					// 考虑srlg,可以在这里加一个标志位，选择是否考虑srlg
					if (LinkRGroup.srlg == true) {
						for (LinkRGroup group : wdmLink.getWdmRelatedList()) {
							for (WDMLink wLink : group.getSRLGWDMLinkList()) {
								wLink.setActive(false);
							}
						}
					}
				}
				route = RouteAlloc.findWDMRoute(traffic, flag);
				if (route != null && route.getWDMLinkList().size() != 0) {
					// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// 如果满足osnr要求
					// 为了避开OSNR 3期要改回去
					if (1 > 0) {
						Traffic tra = route.getBelongsTraffic();
						if (tra.isElectricalCrossConnection() == true && tra.getNrate() != 100) {
							if (CommonAlloc.EallocateWaveLength_workandpro(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
								// 端口分配没写，要改
								System.out.println("子波长业务" + traffic.getName() + "保护路由分配成功");
								CommonAlloc.fallBuffer.append("业务:" + traffic.getName() + "(" + traffic.getNrate()
										+ ")保护路由及资源分配成功！(子波长分配)\r\n");
								traffic.setProtectRoute(route);// 如果路由不为空则设置为工作路由
								traffic.setStatus(TrafficStatus.工作和保护已分配);
							} else {// 资源分配失败
								failedTraffic.add(traffic);
								System.out.println("业务" + traffic.getName() + "保护路由分配失败");
								traffic.setStatus(TrafficStatus.阻塞);// 如果还没有就是找不到
								CommonAlloc.fallBuffer.append(
										"业务:" + traffic.getName() + "(" + traffic.getNrate() + ")保护路由波长资源分配失败！\r\n");
								// RouteAlloc.releaseRoute(route);
							}
						} else if (tra.isElectricalCrossConnection() == false || tra.getNrate() == 100) {
							// 100G业务还要考虑是否需要波长转换和osnr，如果这两个条件不满足，还要子波长分配

							// 判断route可否使用一致波长
							LinkedList<Integer> startWaveLength = new LinkedList<Integer>();
							boolean isConsistent = CommonAlloc.findWaveLength(route, 0, startWaveLength,
									route.getWDMLinkList(), true);
							// 如果波长不一致，就按波道找路由
							Route consistentRoute = null;
							if (isConsistent == false) {
								LinkedList<Route> routeList = new LinkedList<>();
								for (int waveNum = 1; waveNum <= data.DataSave.waveNum - DataSave.locknum; waveNum++) {
									Route route1 = allocateResourceByWave(traffic, 1, waveNum, flag);
									if (route1 != null && route1.getWDMLinkList().size() != 0) {
										routeList.add(route1);
									}
								}
								if (routeList != null && routeList.size() >= 0) {
									// 找出跳数最少的；且资源不足最少的
									consistentRoute = bestRoute(routeList);
								}
							}
							if (consistentRoute != null && consistentRoute.getWDMLinkList().size() != 0) {
								route = consistentRoute;
							}

							if (CommonAlloc.allocateWaveLength1(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
								System.out.println("业务" + traffic.getName() + "保护路由资源分配成功");
								CommonAlloc.fallBuffer.append(
										"业务:" + traffic.getName() + "(" + traffic.getNrate() + ")保护路由及资源分配成功！\r\n");
								traffic.setProtectRoute(route);// 如果路由不为空则设置为工作路由
								traffic.setStatus(TrafficStatus.工作和保护已分配);
							} else {// 资源分配失败
								failedTraffic.add(traffic);
								System.out.println("业务" + traffic.getName() + "保护路由资源分配失败");
								traffic.setStatus(TrafficStatus.阻塞);// 如果还没有就是找不到
								CommonAlloc.fallBuffer.append(
										"业务:" + traffic.getName() + "(" + traffic.getNrate() + ")保护路由波长资源分配失败！\r\n");
								// RouteAlloc.releaseRoute(route);
							}
						}

					}
					// else {// 如果osnr超过门限
					// if (Suggest.isSuggested == true) {// 2017.10.14
					// OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
					// OSNR.OSNRRouteList.add(route);//10.19
					// }
					// CommonAlloc.fallBuffer.append("业务:" + traffic + "保护路由OSNR不满足条件！\r\n");
					// }
				} else {
					CommonAlloc.fallBuffer.append("业务:" + traffic + "保护路由算路失败！\r\n");
				}

				for (WDMLink wdmLink : workLinkList) {// 保护路由计算完成以后激活工作路由链路
					wdmLink.setActive(true);
					for (FiberLink fiberLink : wdmLink.getFiberLinkList()) {
						fiberLink.setActive(true);
					}
					// 考虑srlg
					if (LinkRGroup.srlg == true) {
						for (LinkRGroup group : wdmLink.getWdmRelatedList()) {
							for (WDMLink wLink : group.getSRLGWDMLinkList()) {
								wLink.setActive(true);
							}
						}
					}
				}
				// for(WDMLink wdmLink:WDMLink.WDMLinkList){
				// if(!wdmLink.isActive())
				// wdmLink.setActive(true);
				// }
				// for(FiberLink fiberLink:FiberLink.fiberLinkList){
				// if(!fiberLink.isActive())
				// fiberLink.setActive(true);
				// }
			} // 保护路由计算完毕

			// //从新激活必避站点和链路
			// //将必避链路设为激活
			// if(traffic.getMustAvoidLink()!=null)
			// {WDMLink.getLink(traffic.getMustAvoidLink().getName()).setActive(true);}
			// //将与避避节点相连的链路设为激活
			// if(traffic.getMustAvoidNode()!=null)
			// {CommonNode mANode=traffic.getMustAvoidNode();//必避节点
			// for(int i=0;i<WDMLink.WDMLinkList.size();i++) {
			// if(WDMLink.WDMLinkList.get(i).getFromNode().getName().equals(mANode.getName())
			// ||WDMLink.WDMLinkList.get(i).getToNode().getName().equals(mANode.getName()))
			// WDMLink.WDMLinkList.get(i).setActive(true);
			// }}
			//
			//
			// //从新激活关联业务组的业务
			// if(traffic.getTrafficgroup()!=null)//如果业务的关联业务组不为空
			// {
			// for(int i=0;i<Traffic.trafficList.size();i++) {
			// Traffic tra=Traffic.trafficList.get(i);
			// //判断是否是一个风险共享业务组并且不是同一个业务
			// if(tra.getTrafficgroup()!=null&&tra.getTrafficgroup().getId()==traffic.getTrafficgroup().getId()
			// &&tra.getRankId()!=traffic.getRankId())
			// {
			// if(tra.getWorkRoute()!=null) {//如果相关联的业务工作路由存在
			// for(int j=0;j<tra.getWorkRoute().getWDMLinkList().size();j++)
			// tra.getWorkRoute().getWDMLinkList().get(j).setActive(true);
			// }
			// }
			// }
			// }

			// //////// 开始计算专享预置恢复路由
			// if (traffic.getWorkRoute() != null && (traffic.getProtectLevel() ==
			// TrafficLevel.RESTORATION)
			// ||traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION) {
			// List<WDMLink> workLinkList = traffic.getWorkRoute().getWDMLinkList();
			// for (WDMLink wdmLink : workLinkList) {
			// wdmLink.setActive(false);
			// for (FiberLink link : wdmLink.getFiberLinkList()) {// 设置工作路由不激活来计算保护路由
			// link.setActive(false);
			// }
			// // 考虑srlg,可以在这里加一个标志位，选择是否考虑srlg
			// if (LinkRGroup.srlg == true) {
			// for (LinkRGroup group : wdmLink.getWdmRelatedList()) {
			// for (WDMLink wLink : group.getSRLGWDMLinkList()) {
			// wLink.setActive(false);
			// }
			// }
			// }
			// }
			// route = RouteAlloc.findWDMRoute(traffic, flag);
			// if (route != null && route.getWDMLinkList().size() != 0) {
			// // if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {//
			// 如果满足osnr要求
			// //避开OSNR
			// if(1>0) {
			// if (CommonAlloc.allocateWaveLength2(route) && PortAlloc.allocatePort(route))
			// {// 对光纤链路分配时隙成功，并且分配端口成功
			// CommonAlloc.fallBuffer.append("业务:" + traffic + "保护路由及资源分配成功！\r\n");
			// traffic.setProtectRoute(route);// 如果路由不为空则设置为保护路由
			// traffic.setStatus(TrafficStatus.工作和保护已分配);
			// } else {// 资源分配失败
			//
			// CommonAlloc.fallBuffer.append("业务:" + traffic + "保护路由波长或端口资源分配失败！\r\n");
			// RouteAlloc.releaseRoute(route);
			// }
			// }
			//// else {// 如果osnr超过门限
			//// if (Suggest.isSuggested == true) {// 2017.10.14
			//// OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
			//// OSNR.OSNRRouteList.add(route);//10.19
			//// }
			//// CommonAlloc.fallBuffer.append("业务:" + traffic + "保护路由OSNR不满足条件！\r\n");
			//// }
			// } else {
			// CommonAlloc.fallBuffer.append("业务:" + traffic + "保护路由算路失败！\r\n");
			// }
			//
			// for (WDMLink wdmLink : workLinkList) {// 保护路由计算完成以后激活工作路由链路
			// wdmLink.setActive(true);
			// for (FiberLink fiberLink : wdmLink.getFiberLinkList()) {
			// fiberLink.setActive(true);
			// }
			// // 考虑srlg
			// if (LinkRGroup.srlg == true) {
			// for (LinkRGroup group : wdmLink.getWdmRelatedList()) {
			// for (WDMLink wLink : group.getSRLGWDMLinkList()) {
			// wLink.setActive(true);
			// }
			// }
			// }
			// }
			// } // 专享预置恢复路由

			// 将所有链路设为激活
			for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
				WDMLink.WDMLinkList.get(i).setActive(true);
			}

		} // 业务循环结束
		ResourceLogTxt.ResourceLogTxt();// 输出资源分配日志.
		CommonAlloc.fallBuffer.delete(0, CommonAlloc.fallBuffer.length());
		// System.out.println(CommonNode.allNodeList);//12.14
		trafficList.clear();
		Dlg_PolicySetting.isDesign = true; // CC2017.5.2

	}

	/**
	 * 单独对一个业务进行路由的计算，主要用在恢复路由及恢复路由的保护路由上面
	 * 
	 * @param tra     待分配业务
	 * @param flag    flag=0 长度;=1 跳数 ; =2 负载均衡算法
	 * @param rop     恢复路由还是恢复路由的保护路由 ,0:恢复，1：恢复保护
	 * @param suggest 0:抗毁仿真用 1:规划建议用
	 */
	public static void allocateRscSingle(Traffic tra, int flag, int rop, int suggest) {
		// CommonAlloc.fallBuffer = new StringBuffer();// 链路资源分配日志
		// PortAlloc.portFallBuffer = new StringBuffer();// 端口资源分配日志
		Route route = null;
		OSNR osnr = new OSNR();
		// System.out.println("111");
		route = RouteAlloc.findWDMRoute(tra, flag);
		if (route != null && route.getWDMLinkList().size() != 0) {
			// System.out.println("222");
			// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// 如果满足osnr要求
			// // System.out.println("333");
			if (CommonAlloc.allocateWaveLength(route) && PortAlloc.SimAllocatePort(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
				// route.setBelongsTraffic(tra);//1012 CC
				if (rop == 0)
					tra.setResumeRoute(route);// 如果保护路由为空则将新得路由设置为工作路由
				if (rop == 1)
					tra.setResumeRoutePro(route);
			} else {// 资源分配失败
				// RouteAlloc.releaseRoute(route);
			}
			// } else {// 如果osnr超过门限
			// if (suggest == 1) {
			// if (Suggest.isSuggested == true) {// 2017.10.14
			// OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
			// OSNR.OSNRRouteList.add(route);//10.19
			// }
			// }
			// }
		} else {// 路由分配失败
			// RouteAlloc.releaseRoute(route);
		}

		// ResourceLogTxt.ResourceLogTxt();// 输出资源分配日志.
		// CommonAlloc.fallBuffer.delete(0, CommonAlloc.fallBuffer.length());
		// trafficList.clear();
	}

	/**
	 * 单独对一个业务进行路由的计算，主要用在 工作路由、保护路由、预置恢复路由
	 * 
	 * @param tra  待分配业务
	 * @param flag flag=0 长度;=1 跳数 ; =2 负载均衡算法
	 * @param rop  恢复路由还是恢复路由的保护路由 ,0:恢复，1：恢复保护
	 */
	public static void allocateOneTraffic(Traffic tra, int flag, int rop) {

	}

	/**
	 * 动态重路由恢复时调用 策略：1、先以断点算路 2、判断算出的路由是否有可用波长wave 3.1如有则分配资源 3.2如没有则对整个业务重新算路及分配资源
	 */
	// 算路：输出的路由未分配资源
	public static Route reRoute(Route oldroute, CommonNode from, CommonNode to, int flag) {

		Traffic traffic = oldroute.getBelongsTraffic();
		List<WDMLink> workLinkList = traffic.getWorkRoute().getWDMLinkList();

		// 将链路上没有可用波道资源的链路设为不激活
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			if ((WDMLink.WDMLinkList.get(i).getRemainResource() == 0))
				WDMLink.WDMLinkList.get(i).setActive(false);
		}
		// 将原路由设为不激活
		for (int i = 0; i < oldroute.getWDMLinkList().size(); i++) {
			oldroute.getWDMLinkList().get(i).setActive(false);
			if (oldroute.getWDMLinkList().get(i).getParallelLinkList().size() > 1) {
				for (int j = 0; j < oldroute.getWDMLinkList().get(i).getParallelLinkList().size(); j++) {
					oldroute.getWDMLinkList().get(i).getParallelLinkList().get(j).setActive(false);
				}
			}
		}

		// 设置工作路由不激活来计算动态恢复路由
		for (WDMLink wdmLink : workLinkList) {
			wdmLink.setActive(false);
			for (FiberLink link : wdmLink.getFiberLinkList()) {
				link.setActive(false);
			}
			// 考虑srlg,可以在这里加一个标志位，选择是否考虑srlg
			if (LinkRGroup.srlg == true) {
				for (LinkRGroup group : wdmLink.getWdmRelatedList()) {
					for (WDMLink wLink : group.getSRLGWDMLinkList()) {
						wLink.setActive(false);
					}
				}
			}
		}
		// 如果保护路由存在，则将保护路由设为不激活
		if (traffic.getProtectRoute() != null) {
			List<WDMLink> protectLinkList = traffic.getProtectRoute().getWDMLinkList();
			for (WDMLink wdmLink : protectLinkList) {
				wdmLink.setActive(false);
				for (FiberLink link : wdmLink.getFiberLinkList()) {// 设置保护路由不激活来计算动态路由
					link.setActive(false);
				}
				// 考虑srlg,可以在这里加一个标志位，选择是否考虑srlg
				if (LinkRGroup.srlg == true) {
					for (LinkRGroup group : wdmLink.getWdmRelatedList()) {
						for (WDMLink wLink : group.getSRLGWDMLinkList()) {
							wLink.setActive(false);
						}
					}
				}
			}
		}

		// 如果该业务有关联业务组，并且类型为工作、恢复路由均分离，则将相关联的业务组的业务的恢复路由设为false
		if (traffic.getTrafficgroup() != null && traffic.getTrafficgroup().getType().equals("工作、恢复路由均分离"))// 如果业务的关联业务组不为空
		{
			for (int i = 0; i < Traffic.trafficList.size(); i++) {
				Traffic tra = Traffic.trafficList.get(i);
				// 判断是否是一个风险共享业务组并且不是同一个业务
				if (tra.getTrafficgroup() != null && tra.getTrafficgroup().getId() == traffic.getTrafficgroup().getId()
						&& tra.getRankId() != traffic.getRankId()) {
					if (tra.getDynamicRoute() != null) {// 如果相关联的业务动态恢复路由存在
						for (int j = 0; j < tra.getDynamicRoute().getWDMLinkList().size(); j++)
							tra.getDynamicRoute().getWDMLinkList().get(j).setActive(false);
					}
				}
			}
		}

		// 开始算路
		Route route1 = null; // 以断的节点算路
		// Route route2 = null; // 以业务的首末节点算路，备用：如果route1资源分配不成功就用route2
		// 此时返回的route只包含returnnodelist和returnlinklist两个属性，既该route经过哪些节点和链路
		route1 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, from, to);
		// route2 = RouteAlloc.findWDMRoute(traffic, flag);

		// 算路完成后就可以将所有的链路都激活了
		// 将所有链路设为激活
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink.WDMLinkList.get(i).setActive(true);
		}

		if (route1 != null && route1.getWDMLinkList().size() != 0) {
			return route1;
		}
		return null;

		// if (route1 != null && route1.getWDMLinkList().size() != 0) {
		// // 开始判断这个route上是否有可用波道资源
		// if (Route.isRouteHaveWave(route1, wave, traffic)) {//
		// true则有，则为该route分配波道资源wave
		// for (int hop = 0; hop < route1.getWDMLinkList().size(); hop++) {//
		// 为每一条wdmLink链路分配连续波长
		// WDMLink wdmLink = route1.getWDMLinkList().get(hop);
		// {
		// if (wdmLink.getWaveLengthList().get(wave).getStatus().equals(Status.空闲)) {
		// wdmLink.setRemainResource(wdmLink.getRemainResource() - 1);
		// } // 更新链路剩余资源
		// wdmLink.getWaveLengthList().get(wave).setStatus(Status.恢复);// 改变工作状态
		// wdmLink.getWaveLengthList().get(wave).getDynamicTrafficList().add(traffic);//
		// 加入该波长的动态业务表
		// wdmLink.getCarriedTrafficList().add(traffic);// 为链路添加承载业务
		// }
		// }
		// return route1;
		// } // 波长分配完毕
		// // 如果wave没有资源，则用以业务首末节点算出的route2进行资源分配
		// if (!Route.isRouteHaveWave(route1, wave, traffic)) {
		// if (route2 != null && route2.getWDMLinkList().size() != 0) {
		// // if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {//
		// 如果满足osnr要求
		// // 避开OSNR
		// if (1 > 0) {
		// if (CommonAlloc.allocateWaveLength3(route2) &&
		// PortAlloc.allocatePort(route2)) {// 对光纤链路分配时隙成功，并且分配端口成功
		// // CommonAlloc.fallBuffer.append("业务:" + traffic + "预置路由及资源分配成功！\r\n");
		// traffic.setDynamicRoute(route2);// 如果路由不为空则设置预置重路由
		// traffic.setStatus(TrafficStatus.动态重路由已分配);
		// return route2;
		// } else {// 资源分配失败
		//
		// // CommonAlloc.fallBuffer.append("业务:" + traffic + "预置路由波长或端口资源分配失败！\r\n");
		// RouteAlloc.releaseRoute(route2);
		// }
		// }
		// // else {// 如果osnr超过门限
		// // if (Suggest.isSuggested == true) {// 2017.10.14
		// // OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
		// // OSNR.OSNRRouteList.add(route);//10.19
		// // }
		// // CommonAlloc.fallBuffer.append("业务:" + traffic + "保护路由OSNR不满足条件！\r\n");
		// // }
		// } else {
		// // 算路失败
		// // CommonAlloc.fallBuffer.append("业务:" + traffic + "由于波道资源不足预置路由算路失败！\r\n");
		// }
		// }
		// }
		// else if (route2 != null && route2.getWDMLinkList().size() != 0) {
		// // if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {//
		// 如果满足osnr要求
		// // 避开OSNR
		// if (1 > 0) {
		// if (CommonAlloc.allocateWaveLength3(route2) &&
		// PortAlloc.allocatePort(route2)) {// 对光纤链路分配时隙成功，并且分配端口成功
		// // CommonAlloc.fallBuffer.append("业务:" + traffic + "预置路由及资源分配成功！\r\n");
		// traffic.setDynamicRoute(route2);// 如果路由不为空则设置预置重路由
		// traffic.setStatus(TrafficStatus.动态重路由已分配);
		// return route2;
		// } else {// 资源分配失败
		//
		// // CommonAlloc.fallBuffer.append("业务:" + traffic + "预置路由波长或端口资源分配失败！\r\n");
		// RouteAlloc.releaseRoute(route2);
		// }
		// }
		// // else {// 如果osnr超过门限
		// // if (Suggest.isSuggested == true) {// 2017.10.14
		// // OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
		// // OSNR.OSNRRouteList.add(route);//10.19
		// // }
		// // CommonAlloc.fallBuffer.append("业务:" + traffic + "保护路由OSNR不满足条件！\r\n");
		// // }
		// }

	}
	/*
	 * // 节点断 public static Route reRoute1(Route oldroute, CommonNode from,
	 * CommonNode to, int wave1, int wave2, int flag) {
	 * 
	 * Traffic traffic = oldroute.getBelongsTraffic(); List<WDMLink> workLinkList =
	 * traffic.getWorkRoute().getWDMLinkList();
	 * 
	 * // 将链路上没有可用波道资源的链路设为不激活 for (int i = 0; i < WDMLink.WDMLinkList.size(); i++)
	 * { if ((WDMLink.WDMLinkList.get(i).getRemainResource() == 0))
	 * WDMLink.WDMLinkList.get(i).setActive(false); } // 将原路由设为不激活 for (int i = 0; i
	 * < oldroute.getWDMLinkList().size(); i++) {
	 * oldroute.getWDMLinkList().get(i).setActive(false); }
	 * 
	 * // 设置工作路由不激活来计算动态恢复路由 for (WDMLink wdmLink : workLinkList) {
	 * wdmLink.setActive(false); for (FiberLink link : wdmLink.getFiberLinkList()) {
	 * link.setActive(false); } // 考虑srlg,可以在这里加一个标志位，选择是否考虑srlg if (LinkRGroup.srlg
	 * == true) { for (LinkRGroup group : wdmLink.getWdmRelatedList()) { for
	 * (WDMLink wLink : group.getSRLGWDMLinkList()) { wLink.setActive(false); } } }
	 * } // 如果保护路由存在，则将保护路由设为不激活 if (traffic.getProtectRoute() != null) {
	 * List<WDMLink> protectLinkList = traffic.getProtectRoute().getWDMLinkList();
	 * for (WDMLink wdmLink : protectLinkList) { wdmLink.setActive(false); for
	 * (FiberLink link : wdmLink.getFiberLinkList()) {// 设置保护路由不激活来计算动态路由
	 * link.setActive(false); } // 考虑srlg,可以在这里加一个标志位，选择是否考虑srlg if (LinkRGroup.srlg
	 * == true) { for (LinkRGroup group : wdmLink.getWdmRelatedList()) { for
	 * (WDMLink wLink : group.getSRLGWDMLinkList()) { wLink.setActive(false); } } }
	 * } }
	 * 
	 * // 如果该业务有关联业务组，并且类型为工作、恢复路由均分离，则将相关联的业务组的业务的恢复路由设为false if
	 * (traffic.getTrafficgroup() != null &&
	 * traffic.getTrafficgroup().getType().equals("工作、恢复路由均分离"))// 如果业务的关联业务组不为空 {
	 * for (int i = 0; i < Traffic.trafficList.size(); i++) { Traffic tra =
	 * Traffic.trafficList.get(i); // 判断是否是一个风险共享业务组并且不是同一个业务 if
	 * (tra.getTrafficgroup() != null && tra.getTrafficgroup().getId() ==
	 * traffic.getTrafficgroup().getId() && tra.getRankId() != traffic.getRankId())
	 * { if (tra.getDynamicRoute() != null) {// 如果相关联的业务动态恢复路由存在 for (int j = 0; j <
	 * tra.getDynamicRoute().getWDMLinkList().size(); j++)
	 * tra.getDynamicRoute().getWDMLinkList().get(j).setActive(false); } } } }
	 * 
	 * // 开始算路 Route route1 = null; // 以断的节点算路 Route route2 = null; //
	 * 以业务的首末节点算路，备用：如果route1资源分配不成功就用route2 //
	 * 此时返回的route只包含returnnodelist和returnlinklist两个属性，既该route经过哪些节点和链路 route1 =
	 * RouteAlloc.findWDMRouteByTwoNode(traffic, flag, from, to); route2 =
	 * RouteAlloc.findWDMRoute(traffic, flag);
	 * 
	 * // 算路完成后就可以将所有的链路都激活了 // 将所有链路设为激活 for (int i = 0; i <
	 * WDMLink.WDMLinkList.size(); i++) {
	 * WDMLink.WDMLinkList.get(i).setActive(true); }
	 * 
	 * if (route1 != null && route1.getWDMLinkList().size() != 0) { //
	 * 开始判断这个route上是否有可用波道资源 if (Route.isRouteHaveWave(route1, wave1, traffic)) {//
	 * true则有，则为该route分配波道资源wave-- for (int hop = 0; hop <
	 * route1.getWDMLinkList().size(); hop++) {// 为每一条wdmLink链路分配连续波长 WDMLink
	 * wdmLink = route1.getWDMLinkList().get(hop); { if
	 * (wdmLink.getWaveLengthList().get(wave1).getStatus().equals(Status.空闲)) {
	 * wdmLink.setRemainResource(wdmLink.getRemainResource() - 1); } // 更新链路剩余资源
	 * wdmLink.getWaveLengthList().get(wave1).setStatus(Status.恢复);// 改变工作状态
	 * wdmLink.getWaveLengthList().get(wave1).getDynamicTrafficList().add(traffic);/
	 * / 加入该波长的动态业务表 wdmLink.getCarriedTrafficList().add(traffic);// 为链路添加承载业务 } }
	 * return route1; } // 波长分配完毕 if (Route.isRouteHaveWave(route1, wave2, traffic))
	 * {// true则有，则为该route分配波道资源wave for (int hop = 0; hop <
	 * route1.getWDMLinkList().size(); hop++) {// 为每一条wdmLink链路分配连续波长 WDMLink
	 * wdmLink = route1.getWDMLinkList().get(hop); { if
	 * (wdmLink.getWaveLengthList().get(wave2).getStatus().equals(Status.空闲)) {
	 * wdmLink.setRemainResource(wdmLink.getRemainResource() - 1); } // 更新链路剩余资源
	 * wdmLink.getWaveLengthList().get(wave2).setStatus(Status.恢复);// 改变工作状态
	 * wdmLink.getWaveLengthList().get(wave2).getDynamicTrafficList().add(traffic);/
	 * / 加入该波长的动态业务表 wdmLink.getCarriedTrafficList().add(traffic);// 为链路添加承载业务 } }
	 * return route1; } // 波长分配完毕 // 如果wave1和wave2都没有资源，则用以业务首末节点算出的route2进行资源分配 if
	 * (Route.isRouteHaveWave(route1, wave1, traffic) == false &&
	 * Route.isRouteHaveWave(route1, wave2, traffic) == false) { if (route2 != null
	 * && route2.getWDMLinkList().size() != 0) { // if (osnr.calculateOSNR(route) >=
	 * Dlg_PolicySetting.osnRGate) {// 如果满足osnr要求 // 避开OSNR if (1 > 0) { if
	 * (CommonAlloc.allocateWaveLength3(route2) && PortAlloc.allocatePort(route2))
	 * {// 对光纤链路分配时隙成功，并且分配端口成功 // CommonAlloc.fallBuffer.append("业务:" + traffic +
	 * "预置路由及资源分配成功！\r\n"); traffic.setDynamicRoute(route2);// 如果路由不为空则设置预置重路由
	 * traffic.setStatus(TrafficStatus.动态重路由已分配); return route2; } else {// 资源分配失败
	 * 
	 * // CommonAlloc.fallBuffer.append("业务:" + traffic + "预置路由波长或端口资源分配失败！\r\n");
	 * RouteAlloc.releaseRoute(route2); } } // else {// 如果osnr超过门限 // if
	 * (Suggest.isSuggested == true) {// 2017.10.14 //
	 * OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11 //
	 * OSNR.OSNRRouteList.add(route);//10.19 // } //
	 * CommonAlloc.fallBuffer.append("业务:" + traffic + "保护路由OSNR不满足条件！\r\n"); // } }
	 * else { // 算路失败 // CommonAlloc.fallBuffer.append("业务:" + traffic +
	 * "由于波道资源不足预置路由算路失败！\r\n"); } } } else if (route2 != null &&
	 * route2.getWDMLinkList().size() != 0) { // if (osnr.calculateOSNR(route) >=
	 * Dlg_PolicySetting.osnRGate) {// 如果满足osnr要求 // 避开OSNR if (1 > 0) { if
	 * (CommonAlloc.allocateWaveLength3(route2) && PortAlloc.allocatePort(route2))
	 * {// 对光纤链路分配时隙成功，并且分配端口成功 // CommonAlloc.fallBuffer.append("业务:" + traffic +
	 * "预置路由及资源分配成功！\r\n"); traffic.setDynamicRoute(route2);// 如果路由不为空则设置预置重路由
	 * traffic.setStatus(TrafficStatus.动态重路由已分配); return route2; } else {// 资源分配失败
	 * 
	 * // CommonAlloc.fallBuffer.append("业务:" + traffic + "预置路由波长或端口资源分配失败！\r\n");
	 * RouteAlloc.releaseRoute(route2); } } // else {// 如果osnr超过门限 // if
	 * (Suggest.isSuggested == true) {// 2017.10.14 //
	 * OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11 //
	 * OSNR.OSNRRouteList.add(route);//10.19 // } //
	 * CommonAlloc.fallBuffer.append("业务:" + traffic + "保护路由OSNR不满足条件！\r\n"); // } }
	 * return null; }
	 */
	// 判断路由是否有可用波长wave方法Route.isRouteHaveWave(route, wave, traffic)

	// 动态重路由时，判断该路由有波长wave为起分配波长wave
	public static Route allocWaveForRoute(Route route, int wave, Traffic traffic) {
		for (int hop = 0; hop < route.getWDMLinkList().size(); hop++) {// 为每一条wdmLink链路分配连续波长
			WDMLink wdmLink = route.getWDMLinkList().get(hop);
			{
				// 需要注意调用的wave是波长从1-80，get（wave）是从0-79，所以当取到的波长为wave时对应为wave-1
				if (wdmLink.getWaveLengthList().get(wave - 1).getStatus().equals(Status.空闲)) {
					wdmLink.setRemainResource(wdmLink.getRemainResource() - 1);
				} // 更新链路剩余资源
				wdmLink.getWaveLengthList().get(wave - 1).setStatus(Status.仿真);// 改变工作状态
				wdmLink.getWaveLengthList().get(wave - 1).getDynamicTrafficList().add(traffic);// 加入该波长的动态业务表
				// wdmLink.getCarriedTrafficList().add(traffic);// 为链路添加承载业务
			}
		}
		List<Integer> waveLengthIdList = new LinkedList<>();
		for (int j = 0; j < route.getWDMLinkList().size(); j++) {
			waveLengthIdList.add(wave - 1);
		}
		route.setWaveLengthIdList(waveLengthIdList);
		route.setUsedWaveList(waveLengthIdList);
		route.setWaveChangedNode(waveLengthIdList);
		return route;
	}

	// 动态重路由， 3.2如没有则对整个业务重新算路及分配资源
	public static Route reRouteForTraffic(Route oldroute, int flag) {

		Traffic traffic = oldroute.getBelongsTraffic();
		List<WDMLink> workLinkList = traffic.getWorkRoute().getWDMLinkList();

		WDMLink.updateAllLinkResource();

		// 将链路上没有可用波道资源的链路设为不激活
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			if ((WDMLink.WDMLinkList.get(i).getRemainResource() == 0))
				WDMLink.WDMLinkList.get(i).setActive(false);
		}

		// 将必避链路设为不激活
		if (traffic.getMustAvoidLink() != null) {
			// 找出fiberlink对应的wdmlink
			WDMLink wdmMustAvoidLink = traffic.getMustAvoidLink().getBelongWDMLink();
			if (wdmMustAvoidLink != null) {
				wdmMustAvoidLink.setActive(false);
			}
		}
		// 将与避避节点相连的链路设为不激活
		if (traffic.getMustAvoidNode() != null) {
			CommonNode mANode = traffic.getMustAvoidNode();// 必避节点
			for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
				if (WDMLink.WDMLinkList.get(i).getFromNode().getName().equals(mANode.getName())
						|| WDMLink.WDMLinkList.get(i).getToNode().getName().equals(mANode.getName()))
					WDMLink.WDMLinkList.get(i).setActive(false);
			}
		}

		// // 将原路由设为不激活
		// for (int i = 0; i < oldroute.getWDMLinkList().size(); i++) {
		// oldroute.getWDMLinkList().get(i).setActive(false);
		// if (oldroute.getWDMLinkList().get(i).getParallelLinkList().size() > 1) {
		// for (int j = 0; j <
		// oldroute.getWDMLinkList().get(i).getParallelLinkList().size(); j++) {
		// oldroute.getWDMLinkList().get(i).getParallelLinkList().get(j).setActive(false);
		// }
		// }
		// }

		// 设置工作路由不激活来计算动态恢复路由(错误动态路由时，不需要将原来的工作路由设为不激活来算保护路由，而只是将故障链路设为不激活，这样才可以尽量重用原路由)
		// for (WDMLink wdmLink : workLinkList) {
		// wdmLink.setActive(false);
		// for (FiberLink link : wdmLink.getFiberLinkList()) {
		// link.setActive(false);
		// }
		// // 考虑srlg,可以在这里加一个标志位，选择是否考虑srlg
		// if (LinkRGroup.srlg == true) {
		// for (LinkRGroup group : wdmLink.getWdmRelatedList()) {
		// for (WDMLink wLink : group.getSRLGWDMLinkList()) {
		// wLink.setActive(false);
		// }
		// }
		// }
		// }
		// 如果保护路由存在，则将保护路由设为不激活
		if (traffic.getProtectRoute() != null) {
			List<WDMLink> protectLinkList = traffic.getProtectRoute().getWDMLinkList();
			for (WDMLink wdmLink : protectLinkList) {
				wdmLink.setActive(false);
				for (FiberLink link : wdmLink.getFiberLinkList()) {// 设置保护路由不激活来计算动态路由
					link.setActive(false);
				}
				// 考虑srlg,可以在这里加一个标志位，选择是否考虑srlg
				if (LinkRGroup.srlg == true) {
					for (LinkRGroup group : wdmLink.getWdmRelatedList()) {
						for (WDMLink wLink : group.getSRLGWDMLinkList()) {
							wLink.setActive(false);
						}
					}
				}
			}
		}

		// 如果该业务有关联业务组，并且类型为工作、恢复路由均分离，则将相关联的业务组的业务的恢复路由设为false
		ResourceAlloc.setRelatedTrafficUnactive(traffic, DataSave.separate);

		Route route = null;
		route = RouteAlloc.findWDMRoute(traffic, flag);

		// 将所有链路设为激活
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink.WDMLinkList.get(i).setActive(true);
		}

		if (route != null && route.getWDMLinkList().size() != 0) {
			// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// 如果满足osnr要求
			// 避开OSNR
			// // 波长一致找路
			// ResourceAlloc.isRouteConsistent(route, traffic, 2, flag,null);
			if (1 > 0) {
				if (CommonAlloc.allocateWaveLength3(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
					// CommonAlloc.fallBuffer.append("业务:" + traffic + "预置路由及资源分配成功！\r\n");
					traffic.setDynamicRoute(route);// 如果路由不为空则设置预置重路由
					traffic.setStatus(TrafficStatus.动态重路由已分配);
					return route;
				}
				// else {// 资源分配失败
				//
				// // CommonAlloc.fallBuffer.append("业务:" + traffic + "预置路由波长或端口资源分配失败！\r\n");
				// RouteAlloc.releaseRoute(route);
				// }
			}
		}
		return null;
	}

	/*
	 * 根据波长平面进行路由计算 输入：1、tre业务 2、flag1路由等级（1：工作/保护）（2：预置） 3、波道编号wave 4、flag2路由策略
	 */
	public static Route allocateResourceByWave(Traffic traffic, int flag1, int waveNum, int flag2) {
		// 存放设置了false的link，结束后这些link要重新激活
		LinkedList<WDMLink> setFalseLink = new LinkedList<>();
		Route route = null;
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink link = WDMLink.WDMLinkList.get(i);
			if (link.isActive()) {
				if (!link.isLinkHaveWavei(waveNum, flag1, traffic)) {
					link.setActive(false);
					setFalseLink.add(link);
				}
			}
			route = RouteAlloc.findWDMRoute(traffic, flag2);
		}
		for (int j = 0; j < setFalseLink.size(); j++) {
			setFalseLink.get(j).setActive(true);
		}

		return route;
	}

	public static Route findRouteByWaveDynamic(Traffic traffic, int waveNum, int flag2, CommonNode from,
			CommonNode to) {
		LinkedList<WDMLink> setFalseLink = new LinkedList<>();
		Route route = null;
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink link = (WDMLink) WDMLink.WDMLinkList.get(i);
			if (link.isActive() && !link.isLinkHaveWavei(waveNum, 2, traffic)) {
				link.setActive(false);
				setFalseLink.add(link);
			}
			route = RouteAlloc.findWDMRouteByTwoNode(traffic, flag2, from, to);
		}

		for (int j = 0; j < setFalseLink.size(); j++) {
			setFalseLink.get(j).setActive(true);
		}
		return route;
	}

	public static Route allocateResourceByWave1(Traffic traffic, int flag1, int flag2) {

		// 将必避链路、节点设为不激活
		ResourceAlloc.setMustAvoidUnactive(traffic);

		// 设置故障链路不激活
		for (int j = 0; j < WDMLink.WDMLinkList.size(); j++) {
			if (!WDMLink.WDMLinkList.get(j).isBroken()) {
				WDMLink.WDMLinkList.get(j).setActive(false);
			}
		}

		// 关联业务组设置设置不激活（包括设置相应的SRLG为不激活）
		ResourceAlloc.setRelatedTrafficUnactive(traffic, DataSave.separate);

		// 将资源不足的链路设为不激活
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink link = WDMLink.WDMLinkList.get(i);
			if (link.getRemainResource() == 0) {
				link.setActive(false);
			}
		}

		// 设置保护路由不激活来计算动态路由
		if (traffic.getProtectRoute() != null) {
			ResourceAlloc.setRouteLinkUnactive(traffic.getProtectRoute());
		}

		Route route = RouteAlloc.findWDMRoute(traffic, flag2);

		// 寻找更优路
		if (route != null && route.getWDMLinkList().size() != 0) {
			LinkedList<WDMLink> resourceLessList = route.resoureLessLink(route, 27);
			if (traffic.getMustPassLink() == null && traffic.getMustPassNode() == null) {
				if (resourceLessList.size() != 0) {
					for (int i = 0; i < resourceLessList.size(); i++) {
						resourceLessList.get(i).setActive(false);
					}
					Route newRoute = RouteAlloc.findWDMRoute(traffic, flag2);
					for (int i = 0; i < resourceLessList.size(); i++) {
						resourceLessList.get(i).setActive(true);
					}
					// 如果算出新路由，并且新路由的资源不足链路数小于原路由，则用新路由取代旧路由
					if (newRoute != null && newRoute.getWDMLinkList().size() != 0
							&& newRoute.resoureLessLink(newRoute, 40).size() < route.resoureLessLink(route, 40).size()
							&& (newRoute.getWDMLinkList().size() - route.getWDMLinkList().size() < 3))
						route = newRoute;
				}
			}
		}
		// 判断route可否使用一致波长
		LinkedList<Integer> startWaveLength = new LinkedList<Integer>();
		boolean isConsistent = false;
		if (route != null) {
			isConsistent = CommonAlloc.findWaveLength1(route, 0, startWaveLength, route.getWDMLinkList(), true,
					traffic);
		}
		// boolean isConsistent = false;
		// 如果波长不一致，就按波道找路由
		Route consistentRoute = null;
		if (isConsistent == false || route == null) {
			LinkedList<Route> routeList = new LinkedList<>();
			for (int waveNum = 1; waveNum <= data.DataSave.waveNum; waveNum++) {
				Route route1 = allocateResourceByWave(traffic, flag1, waveNum, flag2);
				if (route1 != null && route1.getWDMLinkList().size() != 0) {
					routeList.add(route1);
				}
			}
			if (routeList != null && routeList.size() >= 0) {
				// 找出跳数最少的；且资源不足最少的
				consistentRoute = bestRoute(routeList);
			}
		}
		if (consistentRoute != null && consistentRoute.getWDMLinkList().size() != 0) {
			route = consistentRoute;
		}
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink.WDMLinkList.get(i).setActive(true);
		}

		if (route != null && route.getWDMLinkList().size() != 0) {
			if (CommonAlloc.allocateWaveLength3(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
				// CommonAlloc.fallBuffer.append("业务:" + traffic + "预置路由及资源分配成功！\r\n");
				traffic.setDynamicRoute(route);// 如果路由不为空则设置预置重路由
				traffic.setStatus(TrafficStatus.动态重路由已分配);
				return route;
			}
		}
		return route;
	}

	// 找出跳数最少并且波道资源不足链路最少的route
	public static Route bestRoute(LinkedList<Route> routeList) {
		Route route = null;
		int minStep = Integer.MAX_VALUE;
		// osnr 6.14
		if (DataSave.OSNR) {
			for (int i = 0; i < routeList.size(); i++) {
				Route rou = routeList.get(i);
				double simu = OSNR.calculateOSNR(rou);
				double cross = OSNR.crossOSNR(rou);
				if (simu < cross) {
					routeList.remove(i);
					i--;
				}
			}
		}
		LinkedList<Route> routeList1 = new LinkedList<>();
		for (int i = 0; i < routeList.size(); i++) {
			if (routeList.get(i).getWDMLinkList().size() < minStep) {
				minStep = routeList.get(i).getWDMLinkList().size();
			}
		}
		for (int i = 0; i < routeList.size(); i++) {
			if (routeList.get(i).getWDMLinkList().size() == minStep) {
				routeList1.add(routeList.get(i));
			}
		}
		// 找出routeList1中，波道资源不足链路最少的route
		int resourceLess = Integer.MAX_VALUE;
		for (int i = 0; i < routeList1.size(); i++) {
			if (routeList1.get(i).resoureLessLink(routeList1.get(i), 35).size() < resourceLess) {
				resourceLess = routeList1.get(i).resoureLessLink(routeList1.get(i), 35).size();
			}
		}
		LinkedList<Route> routeList2 = new LinkedList<>();
		for (int i = 0; i < routeList1.size(); i++) {
			if (routeList1.get(i).resoureLessLink(routeList1.get(i), 35).size() == resourceLess) {
				routeList2.add(routeList1.get(i));
			}
		}
		// 找出routeList2中，含平行边最多的route
		int paraNum = 0;
		for (int i = 0; i < routeList2.size(); i++) {
			Route pararoute = routeList2.get(i);
			int num = 0;
			for (int j = 0; j < pararoute.getWDMLinkList().size(); j++) {
				WDMLink paralink = pararoute.getWDMLinkList().get(j);
				if (paralink.getParallelLinkList().size() > 1) {
					num++;
				}
			}
			if (num > paraNum) {
				paraNum = num;
			}
		}
		for (int i = 0; i < routeList2.size(); i++) {
			Route bestroute = routeList2.get(i);
			int num = 0;
			for (int j = 0; j < bestroute.getWDMLinkList().size(); j++) {
				WDMLink paralink = bestroute.getWDMLinkList().get(j);
				if (paralink.getParallelLinkList().size() > 1) {
					num++;
				}
			}
			if (num == paraNum) {
				route = bestroute;
				break;
			}
		}

		return route;
	}

	// 关联业务组设置设置不激活（包括设置相应的SRLG为不激活） flag 1:链路分离 2：链路及节点
	public static LinkedList<WDMLink> setRelatedTrafficUnactive(Traffic traffic, int flag) {
		LinkedList<WDMLink> list = new LinkedList<>();
		if (traffic.getTrafficgroup() != null)// 如果业务的关联业务组不为空
		{
			for (int i = 0; i < Traffic.trafficList.size(); i++) {
				Traffic tra = Traffic.trafficList.get(i);
				// 判断是否是一个风险共享业务组并且不是同一个业务
				if (tra.getTrafficgroup() != null && tra.getTrafficgroup().getId() == traffic.getTrafficgroup().getId()
						&& tra.getRankId() != traffic.getRankId() && (!tra.getTrafficgroup().getBelongGroup()
								.equals(traffic.getTrafficgroup().getBelongGroup()))) {
					// if(!(traffic.getFromNode().getName().equals(tra.getFromNode().getName())||traffic.getFromNode().getName().equals(tra.getToNode().getName())))
					// {
					// tra.getFromNode().setActive(false);
					// }
					// if(!(traffic.getToNode().getName().equals(tra.getFromNode().getName())||traffic.getToNode().getName().equals(tra.getToNode().getName())))
					// {
					// tra.getToNode().setActive(false);
					// }
					if (tra.getWorkRoute() != null) {// 如果相关联的业务工作路由存在
						for (int j = 0; j < tra.getWorkRoute().getWDMLinkList().size(); j++) {
							WDMLink link = tra.getWorkRoute().getWDMLinkList().get(j);
							if (link.isActive()) {
								list.add(link);
							}
							// 关联业务组不使用同一条链路
							link.setActive(false);

							// 关联业务组不使用同一个SRLG组中的链路
							if (link.belongSRLG() != null) {
								for (int p = 0; p < link.belongSRLG().getSRLGWDMLinkList().size(); p++) {
									if (link.belongSRLG().getSRLGWDMLinkList().get(p).isActive()) {
										list.add(link.belongSRLG().getSRLGWDMLinkList().get(p));
									}
									link.belongSRLG().getSRLGWDMLinkList().get(p).setActive(false);

								}
							}
						}
						if (flag == 2) {
							for (CommonNode node : tra.getWorkRoute().getNodeList()) {
								node.setActive(false);
							}
						}

						// tra.getWorkRoute().getNodeList().get(j).setActive(false);
					}
				}
			}
		}
		return list;
	}

	// 必避链路、节点设为不激活
	public static void setMustAvoidUnactive(Traffic traffic) {

		LinkedList<WDMLink> canBreakActive = new LinkedList<>();
		// 先全部激活
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink link = WDMLink.WDMLinkList.get(i);
			link.setActive(true);

		}
		for (int i = 0; i < CommonNode.allNodeList.size(); i++) {
			CommonNode.allNodeList.get(i).setActive(true);
		}
		if (traffic.getMustAvoidLink() != null) {
			// 找出fiberlink对应的wdmlink
			WDMLink wdmMustAvoidLink = traffic.getMustAvoidLink().getBelongWDMLink();
			if (wdmMustAvoidLink != null) {
				wdmMustAvoidLink.setActive(false);
			}
		}
		// 将与避避节点相连的链路设为不激活
		if (traffic.getMustAvoidNode() != null) {
			CommonNode mANode = traffic.getMustAvoidNode();// 必避节点
			mANode.setActive(false);

			// CommonNode mANode = traffic.getMustAvoidNode();// 必避节点
			// for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			// if
			// (WDMLink.WDMLinkList.get(i).getFromNode().getName().equals(mANode.getName())
			// || WDMLink.WDMLinkList.get(i).getToNode().getName().equals(mANode.getName()))
			// WDMLink.WDMLinkList.get(i).setActive(false);
			// }

		}
	}

	public static LinkedList<WDMLink> resourceNotEnough(boolean pre) {
		LinkedList<WDMLink> list = new LinkedList<>();
		WDMLink.updateAllLinkResource();
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink link = WDMLink.WDMLinkList.get(i);
			if (link.isActive()) {
				if (!pre) {// 不是预置
					if (link.getRemainResource() <= 10 + DataSave.locknum) {
						link.setActive(false);
						list.add(link);
					}
				} else {// 预置
					if (link.getRemainResource() <= DataSave.locknum) {
						link.setActive(false);
						list.add(link);
					}
				}
			}
		}
		return list;
	}

	// 必经节点、必经链路,算路
	public static Route routeForTraffic(Traffic traffic, int flag) {
		Route route = null;
		if (traffic.getMustPassLink() == null && traffic.getMustPassNode() == null) {
			// 有关联业务组的业务的路由，应该尽量不适用其关联业务的首末节点
			if (traffic.getTrafficgroup() != null) {
				for (int i = 0; i < Traffic.trafficList.size(); i++) {
					Traffic tra = (Traffic) Traffic.trafficList.get(i);
					if (tra.getTrafficgroup() != null
							&& tra.getTrafficgroup().getId() == traffic.getTrafficgroup().getId()
							&& tra.getRankId() != traffic.getRankId() && !tra.getTrafficgroup().getBelongGroup()
									.equals(traffic.getTrafficgroup().getBelongGroup())) {
						tra.getFromNode().setActive(false);
						tra.getToNode().setActive(false);
						route = RouteAlloc.findWDMRoute(traffic, flag);
						if (route == null) {
							tra.getFromNode().setActive(true);
							tra.getToNode().setActive(true);
						}
					}
				}

			}
			route = RouteAlloc.findWDMRoute(traffic, flag);
		}
		if (traffic.getMustPassNode() != null && traffic.getMustPassLink() == null) {
			Route route1 = null;
			route1 = RouteAlloc.findWDMRouteHaveMustPassNode1(traffic, flag, traffic.getMustPassNode());
			Route route2 = null;
			// 为避免重复需要把route1相应链路设为不激活,经过的节点也要设为不激活
			if (route1 != null) {
				List<WDMLink> list = route1.getWDMLinkList();
				List<CommonNode> nodelist = route1.getNodeList();
				for (WDMLink wdmLink : list) {
					wdmLink.setActive(false);
					for (WDMLink paralink : wdmLink.getParallelLinkList()) {
						paralink.setActive(false);
					}
					for (FiberLink link : wdmLink.getFiberLinkList()) {
						link.setActive(false);
					}
				}
				// 节点
				for (CommonNode node : nodelist) {
					node.setActive(false);
				}
				traffic.getMustPassNode().setActive(true);
			}
			route2 = RouteAlloc.findWDMRouteHaveMustPassNode2(traffic, flag, traffic.getMustPassNode());
			if (route1 != null && route2 != null) {
				route = Route.combineRoute(route1, route2);
				route.setBelongsTraffic(traffic);
				route.setFrom(traffic.getFromNode());
				route.setTo(traffic.getToNode());
			}
		}
		if (traffic.getMustPassLink() != null && traffic.getMustPassNode() == null) {
			// 找出fiberlink对应的wdmlink
			WDMLink wdmMustPassLink = traffic.getMustPassLink().getBelongWDMLink();
			Route route1 = null;
			route1 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, traffic.getFromNode(),
					wdmMustPassLink.getFromNode());
			Route route2 = null;
			// 为避免重复需要把route1相应链路设为不激活
			if (route1 != null) {
				List<WDMLink> list = route1.getWDMLinkList();
				List<CommonNode> nodelist = route1.getNodeList();
				for (WDMLink wdmLink : list) {
					wdmLink.setActive(false);
					for (WDMLink paralink : wdmLink.getParallelLinkList()) {
						paralink.setActive(false);
					}
					for (FiberLink link : wdmLink.getFiberLinkList()) {
						link.setActive(false);
					}
				}
				// 节点
				for (CommonNode node : nodelist) {
					node.setActive(false);
				}
			}
			route2 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, wdmMustPassLink.getToNode(), traffic.getToNode());
			// 由于链路的的toNode和fromNode变来变去，有可能造成route1与route2是分离的要判断一下
			if (route1 != null && route2 != null) {
				if (route1.getNodeList().get(0).getName().equals(route2.getNodeList().get(0).getName())
						|| route1.getNodeList().get(route1.getNodeList().size() - 1).getName()
								.equals(route2.getNodeList().get(route2.getNodeList().size() - 1).getName())) {
					route1 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, traffic.getFromNode(),
							wdmMustPassLink.getToNode());
					if (route1 != null) {
						List<WDMLink> list = route1.getWDMLinkList();
						for (WDMLink wdmLink : list) {
							wdmLink.setActive(false);
							for (FiberLink link : wdmLink.getFiberLinkList()) {
								link.setActive(false);
							}
						}
					}
					route2 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, wdmMustPassLink.getFromNode(),
							traffic.getToNode());
				}
			}
			route = Route.combineRoute1(route1, route2, wdmMustPassLink);
			route.setBelongsTraffic(traffic);
			route.setFrom(traffic.getFromNode());
			route.setTo(traffic.getToNode());
		}
		// 寻找更优路
		if (route != null && route.getWDMLinkList().size() != 0) {
			LinkedList<WDMLink> noparalLinkList = route.noparalLink(route);

			if (traffic.getMustPassLink() == null && traffic.getMustPassNode() == null) {
//				if (noparalLinkList.size() != 0) {
//					for (int i = 0; i < noparalLinkList.size(); i++) {
//						((WDMLink) noparalLinkList.get(i)).setActive(false);
//					}
//					Route noparalroute = RouteAlloc.findWDMRoute(traffic, flag);
//					for (int i = 0; i < noparalLinkList.size(); i++) {
//						((WDMLink) noparalLinkList.get(i)).setActive(true);
//					}
//					if (noparalroute != null && noparalroute.getWDMLinkList().size() != 0) {
//						if(noparalroute.getWDMLinkList().size() <= route.getWDMLinkList().size()) {
//							route = noparalroute;
//						}
//					}
//				}
				LinkedList<WDMLink> resourceLessList = route.resoureLessLink(route, 27);

				if (resourceLessList.size() != 0) {
					for (int i = 0; i < resourceLessList.size(); i++) {
						((WDMLink) resourceLessList.get(i)).setActive(false);
					}

					Route newRoute = RouteAlloc.findWDMRoute(traffic, flag);
					for (int i = 0; i < resourceLessList.size(); i++) {
						((WDMLink) resourceLessList.get(i)).setActive(true);
					}

					if (newRoute != null && newRoute.getWDMLinkList().size() != 0
							&& newRoute.resoureLessLink(newRoute, 40).size() < route.resoureLessLink(route, 40).size()
							&& newRoute.getWDMLinkList().size() - route.getWDMLinkList().size() < 3)
						route = newRoute;
				}
				LinkedList<WDMLink> srlglink = route.srlglink(route);
				if (traffic.getTrafficgroup() != null && srlglink.size() != 0) {
					for (int i = 0; i < srlglink.size(); i++) {
						((WDMLink) srlglink.get(i)).setActive(false);
					}

					Route newRoute2 = RouteAlloc.findWDMRoute(traffic, flag);
					for (int i = 0; i < srlglink.size(); i++) {
						((WDMLink) srlglink.get(i)).setActive(true);
					}

					if (newRoute2 != null && newRoute2.getWDMLinkList().size() != 0)
						route = newRoute2;
				}
			}
		}
//		if (route != null && route.getWDMLinkList().size() != 0) {
//			LinkedList<WDMLink> noparalLinkList = route.noparalLink(route);
//			LinkedList<WDMLink> srlglink = route.srlglink(route);
//			if (traffic.getMustPassLink() == null && traffic.getMustPassNode() == null) {
//				if (noparalLinkList.size() != 0) {
//				for (int i = 0; i < noparalLinkList.size(); i++)
//					((WDMLink) noparalLinkList.get(i)).setActive(false);
//
//				Route newRoute = RouteAlloc.findWDMRoute(traffic, flag);
//				for (int i = 0; i < noparalLinkList.size(); i++)
//					((WDMLink) noparalLinkList.get(i)).setActive(true);
//
//				if (newRoute != null && newRoute.getWDMLinkList().size() != 0
//						&& newRoute.resoureLessLink(newRoute, 40).size() < route.resoureLessLink(route, 40).size()
//						&& newRoute.getWDMLinkList().size() - route.getWDMLinkList().size() < 3 )
//					route = newRoute;
//			}
//				if (traffic.getTrafficgroup() != null && srlglink.size() != 0) {
//				for (int i = 0; i < srlglink.size(); i++)
//					((WDMLink) srlglink.get(i)).setActive(false);
//
//				Route newRoute2 = RouteAlloc.findWDMRoute(traffic, flag);
//				for (int i = 0; i < srlglink.size(); i++)
//					((WDMLink) srlglink.get(i)).setActive(true);
//
//				if (newRoute2 != null && newRoute2.getWDMLinkList().size() != 0)
//					route = newRoute2;
//			}
//			}
//		}

		return route;
	}

	// 判断route是否可以使用波长一致
	public static boolean isRouteConsist(Route route, Traffic traffic, int flag) {
		// 判断route可否使用一致波长
		LinkedList<Integer> startWaveLength = new LinkedList<Integer>();
		boolean isConsistent = false;
		if (route != null) {
			if (flag == 2) {// 预置
				isConsistent = CommonAlloc.findWaveLength1(route, 0, startWaveLength, route.getWDMLinkList(), true,
						traffic);
			} else {
				isConsistent = CommonAlloc.findWaveLength(route, 0, startWaveLength, route.getWDMLinkList(), true);
			}
		}
		return isConsistent;
	}

	// 判断route是否可以使用波长一致性 flag1:1工作/保护/ 2预置 flag2：路由策略
	public static void isRouteConsistent(Route route, Traffic traffic, int flag1, int flag2, LinkedList<WDMLink> list) {

		// 判断route可否使用一致波长
		LinkedList<Integer> startWaveLength = new LinkedList<Integer>();
		boolean isConsistent = false;
		if (route != null) {
			if (flag1 == 2) {// 预置
				isConsistent = CommonAlloc.findWaveLength1(route, 0, startWaveLength, route.getWDMLinkList(), true,
						traffic);
			} else {
				isConsistent = CommonAlloc.findWaveLength(route, 0, startWaveLength, route.getWDMLinkList(), true);
			}
		}
		// boolean isConsistent = false;
		// 如果波长不一致，就按波道找路由
		Route consistentRoute = null;
		if (isConsistent == false || route == null) {
			LinkedList<Route> routeList = new LinkedList<>();
			for (int waveNum = 1; waveNum <= data.DataSave.waveNum - DataSave.locknum; waveNum++) {
				Route route1 = allocateResourceByWave(traffic, flag1, waveNum, flag2);
				if (route1 != null && route1.getWDMLinkList().size() != 0) {
					routeList.add(route1);
				}
			}
			if (routeList != null && routeList.size() >= 0) {
				// 找出跳数最少的；且资源不足最少的;满足OSNR
				consistentRoute = bestRoute(routeList);
			}
		}
		if (consistentRoute != null && consistentRoute.getWDMLinkList().size() != 0) {
			route = consistentRoute;
		}
		// 如果在波长平面中找不到路由，则尝试将资源不足链路解开再找
		if (consistentRoute == null) {
			// 激活
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setActive(true);
				}
			}

			if (isConsistent == false || route == null) {
				LinkedList<Route> routeList = new LinkedList<>();
				for (int waveNum = 1; waveNum <= data.DataSave.waveNum - DataSave.locknum; waveNum++) {
					Route route1 = allocateResourceByWave(traffic, flag1, waveNum, flag2);
					if (route1 != null && route1.getWDMLinkList().size() != 0) {
						routeList.add(route1);
					}
				}
				if (routeList != null && routeList.size() >= 0) {
					// 找出跳数最少的；且资源不足最少的
					consistentRoute = bestRoute(routeList);
				}
			}
			// 重新不激活
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setActive(false);
				}
			}
			if (consistentRoute != null && consistentRoute.getWDMLinkList().size() != 0) {
				route = consistentRoute;
			}
		}
	}

	// 设置某个route的所有link为不激活，包括SRLG链路
	public static LinkedList<WDMLink> setRouteLinkUnactive(Route route) {
		if (route == null) {
			return null;
		}
		LinkedList<WDMLink> list = new LinkedList<>();
		for (int i = 0; i < route.getWDMLinkList().size(); i++) {
			WDMLink link = route.getWDMLinkList().get(i);
			if (link.isActive()) {
				list.add(link);
			}
			link.setActive(false);

			// SRLG设不激活
			if (link.belongSRLG() != null) {
				for (int j = 0; j < link.belongSRLG().getSRLGWDMLinkList().size(); j++) {
					WDMLink srlgLink = link.belongSRLG().getSRLGWDMLinkList().get(j);
					if (srlgLink.isActive()) {
						list.add(srlgLink);
					}
					srlgLink.setActive(false);
				}
			}
		}
		return list;
	}

	public static void findBetterRoute(Route route, Traffic traffic, int flag) {
		if (route != null && route.getWDMLinkList().size() != 0) {
			LinkedList<WDMLink> resourceLessList = route.resoureLessLink(route, 27);
			if (traffic.getMustPassLink() == null && traffic.getMustPassNode() == null) {
				if (resourceLessList.size() != 0) {
					for (int i = 0; i < resourceLessList.size(); i++) {
						resourceLessList.get(i).setActive(false);
					}
					Route newRoute = RouteAlloc.findWDMRoute(traffic, flag);
					for (int i = 0; i < resourceLessList.size(); i++) {
						resourceLessList.get(i).setActive(true);
					}

					// 如果算出新路由，并且新路由的资源不足链路数小于原路由，则用新路由取代旧路由
					if (newRoute != null && newRoute.getWDMLinkList().size() != 0
							&& newRoute.resoureLessLink(newRoute, 40).size() < route.resoureLessLink(route, 40).size()
							&& (newRoute.getWDMLinkList().size() - route.getWDMLinkList().size() < 3))
						route = newRoute;
				}
			}
		}
	}

	// 将有SRLG的Link设为false
	public static LinkedList<WDMLink> setSRLGFalse() {
		LinkedList<WDMLink> srlgList = new LinkedList();
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink link = WDMLink.WDMLinkList.get(i);
			if (link.isActive()) {
				if (link.belongSRLG() != null) {
					link.setActive(false);
					srlgList.add(link);
				}
			}
		}
		return srlgList;
	}

	public static boolean isRealtedTrafficRouteSucced(Route route, Traffic traffic, int flag) {
		boolean succ = true;
		// 如果是关联业务组，则应当尽量使用没有SRLG的route
		if (route != null && route.getWDMLinkList().size() != 0 && traffic.getTrafficgroup() != null) {
			// 如果是关联业务组的第一个业务时，应该尽量避免使用SRLG链路
			// 一会需要重新激活
			LinkedList<WDMLink> setFalse = new LinkedList<>();

			// 如果有必避节点或者必避链路需先激活
			// 将必避链路设为激活
			if (traffic.getMustAvoidLink() != null) {
				// 找出fiberlink对应的wdmlink
				WDMLink wdmMustAvoidLink = traffic.getMustAvoidLink().getBelongWDMLink();
				if (wdmMustAvoidLink != null) {
					wdmMustAvoidLink.setActive(true);
				}
			}
			// 将与避避节点相连的链路设为激活
			if (traffic.getMustAvoidNode() != null) {
				CommonNode mANode = traffic.getMustAvoidNode();// 必避节点
				mANode.setActive(true);
			}

			// 将route及其SRLG设为不激活
			for (int i = 0; i < route.getWDMLinkList().size(); i++) {
				WDMLink link = route.getWDMLinkList().get(i);
				link.setActive(false);
				setFalse.add(link);
				// SRLG设不激活
				if (link.belongSRLG() != null) {
					for (int j = 0; j < link.belongSRLG().getSRLGWDMLinkList().size(); j++) {
						WDMLink srlgLink = link.belongSRLG().getSRLGWDMLinkList().get(j);
						srlgLink.setActive(false);
						setFalse.add(srlgLink);
					}
				}
			}

			if (traffic.getTrafficgroup() != null)// 如果业务的关联业务组不为空
			{
				for (int i = 0; i < Traffic.trafficList.size(); i++) {
					Traffic tra = Traffic.trafficList.get(i);
					// 判断是否是一个风险共享业务组并且不是同一个业务
					if (tra.getTrafficgroup() != null
							&& tra.getTrafficgroup().getId() == traffic.getTrafficgroup().getId()
							&& tra.getRankId() != traffic.getRankId() && (!tra.getTrafficgroup().getBelongGroup()
									.equals(traffic.getTrafficgroup().getBelongGroup()))) {
						if (tra.getWorkRoute() == null) {// 如果相关联的业务工作路由还未计算
							Route relatedRoute = RouteAlloc.findWDMRoute(tra, flag);
							// 如果关联业务组路由失败,则需要重新设置route,route应尽量使用无SRLG链路
							if (relatedRoute == null || relatedRoute.getWDMLinkList().size() == 0) {
								succ = false;
								// 先将设为不激活的重新设为激活，设为激活的重新设为不激活
								for (int j = 0; j < setFalse.size(); j++) {
									setFalse.get(j).setActive(true);
								}
								// 将必避链路设为不激活
								if (traffic.getMustAvoidLink() != null) {
									// 找出fiberlink对应的wdmlink
									WDMLink wdmMustAvoidLink = traffic.getMustAvoidLink().getBelongWDMLink();
									if (wdmMustAvoidLink != null) {
										wdmMustAvoidLink.setActive(false);
									}
								}
								// 将与避避节点相连的链路设为不激活
								if (traffic.getMustAvoidNode() != null) {
									CommonNode mANode = traffic.getMustAvoidNode();// 必避节点
									mANode.setActive(false);
								}

								// 一会需要重新激活
								LinkedList<WDMLink> containsrlg = new LinkedList<>();
								for (int z = 0; z < route.getWDMLinkList().size(); z++) {
									WDMLink zlink = route.getWDMLinkList().get(z);
									if (zlink.belongSRLG() != null) {
										zlink.setActive(false);
										containsrlg.add(zlink);
									}
								}
								Route reRoute = RouteAlloc.findWDMRoute(traffic, flag);
								if (reRoute != null && reRoute.getWDMLinkList().size() != 0) {
									route = reRoute;
								}
								// 寻找更优路

								LinkedList<WDMLink> resourceLessList = route.resoureLessLink(route, 27);
								if (traffic.getMustPassLink() == null && traffic.getMustPassNode() == null) {
									if (resourceLessList.size() != 0) {
										for (int m = 0; m < resourceLessList.size(); m++) {
											resourceLessList.get(i).setActive(false);
										}
										Route newRoute = RouteAlloc.findWDMRoute(traffic, flag);
										for (int m = 0; m < resourceLessList.size(); m++) {
											resourceLessList.get(m).setActive(true);
										}
										// 如果算出新路由，并且新路由的资源不足链路数小于原路由，则用新路由取代旧路由
										if (newRoute != null && newRoute.getWDMLinkList().size() != 0
												&& newRoute.resoureLessLink(newRoute, 40).size() < route
														.resoureLessLink(route, 40).size()
												&& (newRoute.getWDMLinkList().size()
														- route.getWDMLinkList().size() < 3))
											route = newRoute;
									}
								}

								for (WDMLink link : containsrlg) {
									link.setActive(true);
								}
							}

						}
					}
				}
			}

		}
		return succ;
	}

	public static void setlinkActiveDynamic(Route oldroute) {
		Traffic traffic = oldroute.getBelongsTraffic();
		setMustAvoidUnactive(traffic);
		for (int j = 0; j < WDMLink.WDMLinkList.size(); j++)
			if (!((WDMLink) WDMLink.WDMLinkList.get(j)).isBroken())
				((WDMLink) WDMLink.WDMLinkList.get(j)).setActive(false);

		for (int i = 0; i < oldroute.getWDMLinkList().size(); i++)
			((WDMLink) oldroute.getWDMLinkList().get(i)).setActive(false);

		setRelatedTrafficUnactive(traffic, DataSave.separate);
		WDMLink.updateAllLinkResource();
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++)
			if (((WDMLink) WDMLink.WDMLinkList.get(i)).getRemainResource() == 0)
				((WDMLink) WDMLink.WDMLinkList.get(i)).setActive(false);

		if (traffic.getProtectRoute() != null)
			setRouteLinkUnactive(traffic.getProtectRoute());
	}

	public static List<List<CommonNode>> findAllRoute(CommonNode start, CommonNode end, HashSet<CommonNode> dict) {
		List<List<CommonNode>> ladders = new ArrayList<List<CommonNode>>();
		Map<CommonNode, List<CommonNode>> map = new HashMap<CommonNode, List<CommonNode>>();
		Map<CommonNode, Integer> distance = new HashMap<CommonNode, Integer>();
		dict.add(start);
		dict.add(end);

		bfs(map, distance, start, end, dict);
		List<CommonNode> path = new ArrayList<CommonNode>();
		dfs(ladders, path, end, start, distance, map);

		return ladders;
	}

	// 加入ROADM节点，创建搜索全路由时需要用的的dict
	public static HashSet<CommonNode> setdict() {
		HashSet<CommonNode> dict = new HashSet<>();
		for (CommonNode node : CommonNode.allNodeList) {
			if (node.getNodeType().equals(NodeType.ROADM)) {
				dict.add(node);
			}
		}
		return dict;
	}

	public static List<CommonNode> adjacentNode(CommonNode crt, HashSet<CommonNode> dict) {
		List<CommonNode> adjacent = new ArrayList<CommonNode>();
		for (WDMLink link : WDMLink.WDMLinkList) {
			if (link.getFromNode().equals(crt) && link.isActive()) {
				CommonNode adnode = link.getToNode();
				if (!adjacent.contains(adnode)) {
					adjacent.add(adnode);
				}
			} else if (link.getToNode().equals(crt) && link.isActive()) {
				CommonNode adnode = link.getFromNode();
				if (!adjacent.contains(adnode)) {
					adjacent.add(adnode);
				}
			}
		}
		return adjacent;
	}

	public static void bfs(Map<CommonNode, List<CommonNode>> map, Map<CommonNode, Integer> distance, CommonNode start,
			CommonNode end, HashSet<CommonNode> dict) {
		Queue<CommonNode> q = new LinkedList<CommonNode>();
		q.offer(start);
		distance.put(start, 0);
		for (CommonNode node : dict) {
			map.put(node, new ArrayList<CommonNode>());
		}
		while (!q.isEmpty()) {
			CommonNode crt = q.poll();
			// 找到crt的所有邻接节点
			List<CommonNode> nextList = adjacentNode(crt, dict);
			for (CommonNode next : nextList) {
				map.get(next).add(crt);
				if (!distance.containsKey(next)) {
					distance.put(next, distance.get(crt) + 1);
					q.offer(next);
				}
			}
		}
	}

	public static void dfs(List<List<CommonNode>> ladders, List<CommonNode> path, CommonNode crt, CommonNode start,
			Map<CommonNode, Integer> distance, Map<CommonNode, List<CommonNode>> map) {
		path.add(crt);
		if (crt.equals(start)) {
			Collections.reverse(path);
			ladders.add(new ArrayList<CommonNode>(path));
			Collections.reverse(path);
		} else {
			for (CommonNode next : map.get(crt)) {
				// 这个是找所有跳数最短的
				// if(distance.containsKey(next) && distance.get(crt) == distance.get(next)+1) {
				// 这个是找所有
				if (distance.containsKey(next) && !path.contains(next)) {
					dfs(ladders, path, next, start, distance, map);
				}
			}
		}
		path.remove(path.size() - 1);
	}

	// flag2:1工作保护或2预置
	public static Route findMeetOSNRroute(Route route, Traffic traffic, int flag, int flag2) {
		// if (traffic.getTrafficgroup() == null ||
		// traffic.getTrafficgroup().getBelongGroup().equals("B")) {
		Route onlymeetOSNR = null;
		// 判断route是否满足OSNR
		if (route == null || route.meetOSNR() == false) {
			HashSet<CommonNode> dict = setdict();
			List<List<CommonNode>> ladders = findAllRoute(traffic.getFromNode(), traffic.getToNode(), dict);
			List<Route> routelist = Route.fromNodeToRoute(ladders, traffic);
			if (flag == 0) {// 最小跳，将路由按最小跳数排序
				Route.sortHopMethod(routelist);
			} else {// 最短，将路由按长度排序,默认按最短长度
				Route.sortLengthMethod(routelist);
			}

			LinkedList<Route> routeConsistList = new LinkedList<>();
			for (int i = 0; i < routelist.size(); i++) {
				Route r = routelist.get(i);
				if (isRouteConsist(r, traffic, flag2)) {
					routeConsistList.add(r);
				}
			}

			for (int i = 0; i < routelist.size(); i++) {
				Route r = routelist.get(i);
				if (r.meetOSNR()) {
					onlymeetOSNR = r;
					break;
				}
			}
			for (int i = 0; i < routelist.size(); i++) {
				Route r = routelist.get(i);
				if (r.meetOSNR() && isRouteConsist(r, traffic, flag2)) {
					return r;
				}
			}
			return onlymeetOSNR;
		} else
			return route;
	}
//		else {
//			
//			Route onlymeetOSNR = null;
//			Route consistAndMeetOSNR = null;
//			// 有关联业务组的同一个业务应该使用相同的路由
//			// 判断route是否满足OSNR
//			if (route == null || route.meetOSNR() == false) {
//				HashSet<CommonNode> dict = setdict();
//				List<List<CommonNode>> ladders = findAllRoute(traffic.getFromNode(), traffic.getToNode(), dict);
//				List<Route> routelist = Route.fromNodeToRoute(ladders, traffic);
//				if (flag == 0) {// 最小跳，将路由按最小跳数排序
//					Route.sortHopMethod(routelist);
//				} else {// 最短，将路由按长度排序,默认按最短长度
//					Route.sortLengthMethod(routelist);
//				}
//
//				LinkedList<Route> routeConsistList = new LinkedList<>();
//				for (int i = 0; i < routelist.size(); i++) {
//					Route r = routelist.get(i);
//					if (isRouteConsist(r, traffic, flag2)) {
//						routeConsistList.add(r);
//					}
//				}
//
//				for (int i = 0; i < routelist.size(); i++) {
//					Route r = routelist.get(i);
//					if (r.meetOSNR()) {
//						onlymeetOSNR = r;
//						break;
//					}
//				}
//				for (int i = 0; i < routelist.size(); i++) {
//					Route r = routelist.get(i);
//					if (r.meetOSNR() && isRouteConsist(r, traffic, flag2)) {
//						consistAndMeetOSNR =  r;
//						break;
//					}
//				}
//				for (int i = 0; i < routelist.size(); i++) {
//					if(consistAndMeetOSNR == null) {
//						break;
//					}
//					Route r = routelist.get(i);
//					if (r.meetOSNR() && isRouteConsist(r, traffic, flag2) && Route.consistWaveNum(r, flag2, traffic).size() >= traffic.getTrafficNum()) {
//						return  r;
//					}
//				}
//				if(consistAndMeetOSNR != null) {
//					return consistAndMeetOSNR;
//				} else {
//					return onlymeetOSNR;
//				}
//			}else {
//				return route;
//			}
//		}
//	}

	// 算出所有路由，然后选择一个相对较好的路由
	public static void findbetterRouteFromK(int policy, int routeType, Traffic traffic, int K) {
		HashSet<CommonNode> dict = setdict();
		List<List<CommonNode>> ladders = findAllRoute(traffic.getFromNode(), traffic.getToNode(), dict);
		List<Route> routelist = Route.fromNodeToRoute(ladders, traffic);
		if (policy == 0) {// 最短，将路由按长度排序
			if (traffic.getTrafficgroup() != null) {
				// 关联业务组的按最小跳排
				Route.sortHopMethod(routelist);
			} else {
				Route.sortLengthMethod(routelist);
			}
		} else {//// 1最小跳，将路由按最小跳数排序,默认按最小跳
			Route.sortHopMethod(routelist);
		}
//		//如果有OSNR要移除不满足OSNR条件的
//		if(DataSave.OSNR) {
//			for(int i=0;i<routelist.size();i++) {
//				Route route = routelist.get(i);
//				//不满足OSNR就移除
//				if(!route.meetOSNR()) {
//					routelist.remove(i);
//				}
//			}
//		}
//		//移除不满足波长一致性的
//		List<Route> copylist = routelist;
//		for(int i=0;i<routelist.size();i++) {
//			Route route = routelist.get(i);
//			//不满足OSNR就移除
//			if(!route.meetOSNR()) {
//				routelist.remove(i);
//			}
//		}

	}

	// flag2:1工作保护或2预置
	public static Route findMeetOSNRelatedRroute(Route route, Traffic traffic, int flag, int flag2) {
//		if (traffic.getTrafficgroup() == null ) {
		Route onlymeetOSNR = null;
		// 判断route是否满足OSNR
		if (route == null || route.meetOSNR() == false) {
			HashSet<CommonNode> dict = setdict();
			List<List<CommonNode>> ladders = findAllRoute(traffic.getFromNode(), traffic.getToNode(), dict);
			List<Route> routelist = Route.fromNodeToRoute(ladders, traffic);
			if (flag == 0) {// 最小跳，将路由按最小跳数排序
				Route.sortHopMethod(routelist);
			} else {// 最短，将路由按长度排序,默认按最短长度
				Route.sortLengthMethod(routelist);
			}

			LinkedList<Route> routeConsistList = new LinkedList<>();
			for (int i = 0; i < routelist.size(); i++) {
				Route r = routelist.get(i);
				if (isRouteConsist(r, traffic, flag2)) {
					routeConsistList.add(r);
				}
			}

			for (int i = 0; i < routelist.size(); i++) {
				Route r = routelist.get(i);
				if (r.meetOSNR()) {
					onlymeetOSNR = r;
					break;
				}
			}
			for (int i = 0; i < routelist.size(); i++) {
				Route r = routelist.get(i);
				if (r.meetOSNR() && isRouteConsist(r, traffic, flag2)) {
					return r;
				}
			}
			return onlymeetOSNR;
		} else
			return route;
//		}
//		else {
//			//有关联业务组
//			if(route == null || route.meetOSNR() == false || Route.consistWaveNum(route, flag2, traffic).size() < traffic.getEffectNum()) {
//				Route onlymeetOSNR = null;
//				Route consistAndMeetOSNR = null;
//				HashSet<CommonNode> dict = setdict();
//				List<List<CommonNode>> ladders = findAllRoute(traffic.getFromNode(), traffic.getToNode(), dict);
//				List<Route> routelist = Route.fromNodeToRoute(ladders, traffic);
//				if (flag == 0) {// 最小跳，将路由按最小跳数排序
//					Route.sortHopMethod(routelist);
//				} else {// 最短，将路由按长度排序,默认按最短长度
//					Route.sortLengthMethod(routelist);
//				}
//				LinkedList<Route> routeMeetOSNRList = new LinkedList<>();
//				for (int i = 0; i < routelist.size(); i++) {
//					Route r = routelist.get(i);
//					if (r.meetOSNR()) {
//						routeMeetOSNRList.add(r);
//					}
//				}
//
//				LinkedList<Route> routeConsistList = new LinkedList<>();
//				for (int i = 0; i < routelist.size(); i++) {
//					Route r = routelist.get(i);
//					if (isRouteConsist(r, traffic, flag2)) {
//						routeConsistList.add(r);
//					}
//				}
//				LinkedList<Route> routeConsistAndMeetOSNRList = new LinkedList<>();
//
//				for (int i = 0; i < routeConsistList.size(); i++) {
//					Route r = routeConsistList.get(i);
//					if (r.meetOSNR()) {
//						routeConsistAndMeetOSNRList.add(r);
//					}
//				}
//				//从routeMeetOSNRList找出最好的route给
//				
//				//从routeConsistAndMeetOSNRList找出最好的route
//				for (int i = 0; i < routelist.size(); i++) {
//					Route r = routelist.get(i);
//					if (r.meetOSNR() && isRouteConsist(r, traffic, flag2)) {
//						consistAndMeetOSNR = r;
//						break;
//					}
//				}
//				
//				
//				return onlymeetOSNR;
//			
//			}
//		}
	}

	// 灵活光网络资源分配
	public static void allocateResourceGrid(List<Traffic> traList, int flag) {
		// flag=0:最短长度；=1：最小跳
		Survivance.mark = flag;// 用于抗毁仿真中标记分配的时候使用的哪种路由算法
		CommonAlloc.fallBuffer = new StringBuffer();// 链路资源分配日志
		PortAlloc.portFallBuffer = new StringBuffer();// 端口资源分配日志
		trafficList = new LinkedList<Traffic>();
		trafficList.addAll(traList);// 镜像了一份业务列表，对trafficList的操作不会影响traList
		// Collections.reverse(traList);

		// 存放工作路由计算失败的业务
		LinkedList<Traffic> workFailure = new LinkedList<>();
		// 存放保护路由计算失败的业务
		LinkedList<Traffic> protectFailure = new LinkedList<>();
		// 存放预置路由计算失败的业务
		LinkedList<Traffic> preFailure = new LinkedList<>();

		Route route = null;
		OSNR osnr = new OSNR();
		if (!RouteAlloc.isLimitHop) {
			RouteAlloc.hopLimit = Integer.MAX_VALUE;
		} // 新增跳数限制(不过不限制跳数，就把跳数设置为无穷大)
		else if (RouteAlloc.hopLimit == Integer.MAX_VALUE) {
			RouteAlloc.hopLimit = 5;
		} // 默认跳数限制为5

		Iterator<Traffic> it = trafficList.iterator();
		while (it.hasNext()) {// 循环计算链表里每个业务的工作路由/保护路由（如果有）

			index++;
			Traffic traffic = it.next();

			// 将必避链路、节点设为不激活
			ResourceAlloc.setMustAvoidUnactive(traffic);

			// 关联业务组设置设置不激活（包括设置相应的SRLG为不激活）
			ResourceAlloc.setRelatedTrafficUnactive(traffic, DataSave.separate);

			HashSet<CommonNode> dict = ResourceAlloc.setdict();
			
			//计算出所有满足情况的路由点集
			List<List<CommonNode>> nodeRoutes = ResourceAlloc.findAllRoute(traffic.getFromNode(),
					traffic.getToNode(), dict);
			
			
			
			
			
		}

	}

}
