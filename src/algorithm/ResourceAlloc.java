package algorithm;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import data.CommonNode;
import data.FiberLink;
import data.LinkRGroup;
import data.Route;
import data.SpanLink;
import data.Traffic;
import data.TrafficGroup;
import data.WDMLink;
import dialog.Dlg_PolicySetting;
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
	public static List<Traffic> trafficList = new LinkedList<Traffic>(); // 输入的业务列表的镜像
	public static List<Traffic> failedTraffic = new LinkedList<Traffic>();// 存储失败的业务

	public static void allocateResource(List<Traffic> traList, int flag) {// flag=0:最短长度；=1：最小跳
		Survivance.mark = flag;// 用于抗毁仿真中标记分配的时候使用的哪种路由算法
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

			// 将链路上没有可用波道资源的链路设为不激活
			for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
				if ((WDMLink.WDMLinkList.get(i).getRemainResource() == 0))
					WDMLink.WDMLinkList.get(i).setActive(false);
			}
			Traffic traffic = it.next();
			// 将必避链路设为不激活
			if (traffic.getMustAvoidLink() != null) {
				WDMLink.getLink(traffic.getMustAvoidLink().getName()).setActive(false);
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
				Route route1 = null;
				route1 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, traffic.getFromNode(),
						traffic.getMustPassLink().getFromNode());
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
				route2 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, traffic.getMustPassLink().getToNode(),
						traffic.getToNode());
				// 由于链路的的toNode和fromNode变来变去，有可能造成route1与route2是分离的要判断一下
				if (route1 != null && route2 != null) {
					if (route1.getNodeList().get(0).getName().equals(route2.getNodeList().get(0).getName())
							|| route1.getNodeList().get(route1.getNodeList().size() - 1).getName()
									.equals(route2.getNodeList().get(route2.getNodeList().size() - 1).getName())) {
						route1 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, traffic.getFromNode(),
								traffic.getMustPassLink().getToNode());
						if (route1 != null) {
							List<WDMLink> list = route1.getWDMLinkList();
							for (WDMLink wdmLink : list) {
								wdmLink.setActive(false);
								for (FiberLink link : wdmLink.getFiberLinkList()) {
									link.setActive(false);
								}
							}
						}
						route2 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag,
								traffic.getMustPassLink().getFromNode(), traffic.getToNode());
					}
				}
				route = Route.combineRoute1(route1, route2, traffic.getMustPassLink());
				route.setBelongsTraffic(traffic);
				route.setFrom(traffic.getFromNode());
				route.setTo(traffic.getToNode());
			}

			// System.out.println(route);

			if (route != null && route.getWDMLinkList().size() != 0 && traffic.getWorkRoute() == null) {
				// System.out.println(route);
				// System.out.println("os1:"+osnr.calculateOSNR(route));
				// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// 如果满足osnr要求
				if (1 > 0) {// 为了避开OSNR限制 做3期时要改掉
					if (CommonAlloc.allocateWaveLength(route) && PortAlloc.allocatePort(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
						System.out.println("分配成功");
						CommonAlloc.fallBuffer.append("业务:" + traffic + "工作路由及资源分配成功！\r\n");
						traffic.setWorkRoute(route);// 如果路由不为空则设置为工作路由
						traffic.setStatus(TrafficStatus.工作已分配);
					} else {// 资源分配失败
						failedTraffic.add(traffic);
						System.out.println("分配失败");
						traffic.setStatus(TrafficStatus.阻塞);// 如果还没有就是找不到
						CommonAlloc.fallBuffer.append("业务:" + traffic + "工作路由波长或端口资源分配失败！\r\n");
						RouteAlloc.releaseRoute(route);
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

				traffic.setStatus(TrafficStatus.规划业务阻塞);// 如果还没有就是找不到
				CommonAlloc.fallBuffer.append("业务:" + traffic + "由于波道资源不足工作路由算路失败！\r\n");
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
						if (CommonAlloc.allocateWaveLength1(route) && PortAlloc.allocatePort(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
							CommonAlloc.fallBuffer.append("业务:" + traffic + "保护路由及资源分配成功！\r\n");
							traffic.setProtectRoute(route);// 如果路由不为空则设置为保护路由
							traffic.setStatus(TrafficStatus.工作和保护已分配);
						} else {// 资源分配失败
							CommonAlloc.fallBuffer.append("业务:" + traffic + "保护路由波长或端口资源分配失败！\r\n");
							RouteAlloc.releaseRoute(route);
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
					CommonAlloc.fallBuffer.append("业务:" + traffic + "由于波道资源不足保护路由算路失败！\r\n");
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

			//////// 开始计算专享预置恢复路由
			if (traffic.getWorkRoute() != null && (traffic.getProtectLevel() == TrafficLevel.RESTORATION)
					|| traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION) {// 如果工作路由存在，并且保护等级包含重路由
				List<WDMLink> workLinkList = traffic.getWorkRoute().getWDMLinkList();
				for (WDMLink wdmLink : workLinkList) {
					wdmLink.setActive(false);
					for (FiberLink link : wdmLink.getFiberLinkList()) {// 设置工作路由不激活来计算预置路由
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
						for (FiberLink link : wdmLink.getFiberLinkList()) {// 设置保护路由不激活来计算预置路由
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

				route = RouteAlloc.findWDMRoute(traffic, flag);
				if (route != null && route.getWDMLinkList().size() != 0) {
					// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// 如果满足osnr要求
					// 避开OSNR
					if (1 > 0) {
						if (CommonAlloc.allocateWaveLength2(route) && PortAlloc.allocatePort(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
							CommonAlloc.fallBuffer.append("业务:" + traffic + "预置路由及资源分配成功！\r\n");
							traffic.setPreRoute(route);// 如果路由不为空则设置预置重路由
							traffic.setStatus(TrafficStatus.工作和保护和重路由已分配);
						} else {// 资源分配失败

							CommonAlloc.fallBuffer.append("业务:" + traffic + "预置路由波长或端口资源分配失败！\r\n");
							RouteAlloc.releaseRoute(route);
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
					CommonAlloc.fallBuffer.append("业务:" + traffic + "由于波道资源不足预置路由算路失败！\r\n");
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
			} // 专享预置恢复路由

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

		} // 业务循环结束
		ResourceLogTxt.ResourceLogTxt();// 输出资源分配日志.
		CommonAlloc.fallBuffer.delete(0, CommonAlloc.fallBuffer.length());
		// System.out.println(CommonNode.allNodeList);//12.14
		trafficList.clear();
		Dlg_PolicySetting.isDesign = true; // CC2017.5.2

	}

	public static void allocateResource1(List<Traffic> traList, int flag) {// flag=0:最短长度；=1：最小跳
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

			// 将链路上没有可用波道资源的链路设为不激活
			for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
				if (WDMLink.WDMLinkList.get(i).getRemainResource() == 0)
					WDMLink.WDMLinkList.get(i).setActive(false);
			}
			Traffic traffic = it.next();
			// 将必避链路设为不激活
			if (traffic.getMustAvoidLink() != null) {
				traffic.getMustAvoidLink().setActive(false);
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

			// 关联业务组
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

			// 关联业务组-如果有关联业务组，则把与之相关联的业务的工作路由链路设为false
			// if(traffic.getM_cGroup()!=null) {
			// int groupId=traffic.getM_cGroup().getGroupId();
			// for(int i=0;i<TrafficGroup.grouptrafficGroupList.size();i++)
			// if(TrafficGroup.grouptrafficGroupList.get(i).getRelatedId()==groupId)//找出与之关联的业务组
			// {
			// for(int
			// j=0;j<TrafficGroup.grouptrafficGroupList.get(i).getTrafficList().size();j++)
			// { //如果工作路由存在
			// if(TrafficGroup.grouptrafficGroupList.get(i).getTrafficList().get(j).getWorkRoute()!=null)
			// { //将工作路由的WDMLink设为false
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
				Route route1 = null;
				route1 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, traffic.getFromNode(),
						traffic.getMustPassLink().getFromNode());
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
				route2 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, traffic.getMustPassLink().getToNode(),
						traffic.getToNode());
				// 由于链路的的toNode和fromNode变来变去，有可能造成route1与route2是分离的要判断一下
				if (route1 != null && route2 != null) {
					if (route1.getNodeList().get(0).getName().equals(route2.getNodeList().get(0).getName())
							|| route1.getNodeList().get(route1.getNodeList().size() - 1).getName()
									.equals(route2.getNodeList().get(route2.getNodeList().size() - 1).getName())) {
						route1 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, traffic.getFromNode(),
								traffic.getMustPassLink().getToNode());
						if (route1 != null) {
							List<WDMLink> list = route1.getWDMLinkList();
							for (WDMLink wdmLink : list) {
								wdmLink.setActive(false);
								for (FiberLink link : wdmLink.getFiberLinkList()) {
									link.setActive(false);
								}
							}
						}
						route2 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag,
								traffic.getMustPassLink().getFromNode(), traffic.getToNode());
					}
				}
				route = Route.combineRoute1(route1, route2, traffic.getMustPassLink());
				route.setBelongsTraffic(traffic);
				route.setFrom(traffic.getFromNode());
				route.setTo(traffic.getToNode());
			}
			// System.out.println(route);
			if (route != null && route.getWDMLinkList().size() != 0 && traffic.getWorkRoute() == null) {
				// System.out.println(route);
				// System.out.println("os1:"+osnr.calculateOSNR(route));
				// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// 如果满足osnr要求
				if (1 > 0) {// 为了避开OSNR限制 做3期时要改掉
					if (CommonAlloc.allocateWaveLength(route) && PortAlloc.allocatePort(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
						System.out.println("分配成功");
						CommonAlloc.fallBuffer.append("业务:" + traffic + "工作路由及资源分配成功！\r\n");
						traffic.setWorkRoute(route);// 如果路由不为空则设置为工作路由
						traffic.setStatus(TrafficStatus.工作已分配);
					} else {// 资源分配失败
						failedTraffic.add(traffic);
						System.out.println("分配失败");
						traffic.setStatus(TrafficStatus.阻塞);// 如果还没有就是找不到
						CommonAlloc.fallBuffer.append("业务:" + traffic + "工作路由波长或端口资源分配失败！\r\n");
						RouteAlloc.releaseRoute(route);
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

				traffic.setStatus(TrafficStatus.规划业务阻塞);// 如果还没有就是找不到
				CommonAlloc.fallBuffer.append("业务:" + traffic + "由于波道资源不足工作路由算路失败！\r\n");
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
						if (CommonAlloc.allocateWaveLength1(route) && PortAlloc.allocatePort(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
							CommonAlloc.fallBuffer.append("业务:" + traffic + "保护路由及资源分配成功！\r\n");
							traffic.setProtectRoute(route);// 如果路由不为空则设置为保护路由
							traffic.setStatus(TrafficStatus.工作和保护已分配);
						} else {// 资源分配失败
							CommonAlloc.fallBuffer.append("业务:" + traffic + "保护路由波长或端口资源分配失败！\r\n");
							RouteAlloc.releaseRoute(route);
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
					CommonAlloc.fallBuffer.append("业务:" + traffic + "由于波道资源不足保护路由算路失败！\r\n");
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
	 * @param tra
	 *            待分配业务
	 * @param flag
	 *            flag=0 长度;=1 跳数 ; =2 负载均衡算法
	 * @param rop
	 *            恢复路由还是恢复路由的保护路由 ,0:恢复，1：恢复保护
	 * @param suggest
	 *            0:抗毁仿真用 1:规划建议用
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
				RouteAlloc.releaseRoute(route);
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
			RouteAlloc.releaseRoute(route);
		}

		// ResourceLogTxt.ResourceLogTxt();// 输出资源分配日志.
		// CommonAlloc.fallBuffer.delete(0, CommonAlloc.fallBuffer.length());
		// trafficList.clear();
	}

	/**
	 * 单独对一个业务进行路由的计算，主要用在 工作路由、保护路由、预置恢复路由
	 * 
	 * @param tra
	 *            待分配业务
	 * @param flag
	 *            flag=0 长度;=1 跳数 ; =2 负载均衡算法
	 * @param rop
	 *            恢复路由还是恢复路由的保护路由 ,0:恢复，1：恢复保护
	 */
	public static void allocateOneTraffic(Traffic tra, int flag, int rop) {

	}

	/**
	 * 动态重路由恢复时调用
	 */
	// 链路断
	public static Route reRoute(Route oldroute, CommonNode from, CommonNode to, int wave, int flag) {

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
		Route route2 = null; // 以业务的首末节点算路，备用：如果route1资源分配不成功就用route2
		// 此时返回的route只包含returnnodelist和returnlinklist两个属性，既该route经过哪些节点和链路
		route1 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, from, to);
		route2 = RouteAlloc.findWDMRoute(traffic, flag);

		// 算路完成后就可以将所有的链路都激活了
		// 将所有链路设为激活
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink.WDMLinkList.get(i).setActive(true);
		}

		if (route1 != null && route1.getWDMLinkList().size() != 0) {
			// 开始判断这个route上是否有可用波道资源
			if (Route.isRouteHaveWave(route1, wave, traffic)) {// true则有，则为该route分配波道资源wave
				for (int hop = 0; hop < route1.getWDMLinkList().size(); hop++) {// 为每一条wdmLink链路分配连续波长
					WDMLink wdmLink = route1.getWDMLinkList().get(hop);
					{
						if (wdmLink.getWaveLengthList().get(wave).getStatus().equals(Status.空闲)) {
							wdmLink.setRemainResource(wdmLink.getRemainResource() - 1);
						} // 更新链路剩余资源
						wdmLink.getWaveLengthList().get(wave).setStatus(Status.恢复);// 改变工作状态
						wdmLink.getWaveLengthList().get(wave).getDynamicTrafficList().add(traffic);// 加入该波长的动态业务表
						wdmLink.getCarriedTrafficList().add(traffic);// 为链路添加承载业务
					}
				}
				return route1;
			} // 波长分配完毕
				// 如果wave没有资源，则用以业务首末节点算出的route2进行资源分配
			if (!Route.isRouteHaveWave(route1, wave, traffic)) {
				if (route2 != null && route2.getWDMLinkList().size() != 0) {
					// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// 如果满足osnr要求
					// 避开OSNR
					if (1 > 0) {
						if (CommonAlloc.allocateWaveLength3(route2) && PortAlloc.allocatePort(route2)) {// 对光纤链路分配时隙成功，并且分配端口成功
							// CommonAlloc.fallBuffer.append("业务:" + traffic + "预置路由及资源分配成功！\r\n");
							traffic.setDynamicRoute(route2);// 如果路由不为空则设置预置重路由
							traffic.setStatus(TrafficStatus.动态重路由已分配);
							return route2;
						} else {// 资源分配失败

							// CommonAlloc.fallBuffer.append("业务:" + traffic + "预置路由波长或端口资源分配失败！\r\n");
							RouteAlloc.releaseRoute(route2);
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
					// 算路失败
					// CommonAlloc.fallBuffer.append("业务:" + traffic + "由于波道资源不足预置路由算路失败！\r\n");
				}
			}
		}
		else if (route2 != null && route2.getWDMLinkList().size() != 0) {
			// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// 如果满足osnr要求
			// 避开OSNR
			if (1 > 0) {
				if (CommonAlloc.allocateWaveLength3(route2) && PortAlloc.allocatePort(route2)) {// 对光纤链路分配时隙成功，并且分配端口成功
					// CommonAlloc.fallBuffer.append("业务:" + traffic + "预置路由及资源分配成功！\r\n");
					traffic.setDynamicRoute(route2);// 如果路由不为空则设置预置重路由
					traffic.setStatus(TrafficStatus.动态重路由已分配);
					return route2;
				} else {// 资源分配失败

					// CommonAlloc.fallBuffer.append("业务:" + traffic + "预置路由波长或端口资源分配失败！\r\n");
					RouteAlloc.releaseRoute(route2);
				}
			}
			// else {// 如果osnr超过门限
			// if (Suggest.isSuggested == true) {// 2017.10.14
			// OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
			// OSNR.OSNRRouteList.add(route);//10.19
			// }
			// CommonAlloc.fallBuffer.append("业务:" + traffic + "保护路由OSNR不满足条件！\r\n");
			// }
		}
		return null;
	}

	// 节点断
	public static Route reRoute1(Route oldroute, CommonNode from, CommonNode to, int wave1, int wave2, int flag) {

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
		Route route2 = null; // 以业务的首末节点算路，备用：如果route1资源分配不成功就用route2
		// 此时返回的route只包含returnnodelist和returnlinklist两个属性，既该route经过哪些节点和链路
		route1 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, from, to);
		route2 = RouteAlloc.findWDMRoute(traffic, flag);

		// 算路完成后就可以将所有的链路都激活了
		// 将所有链路设为激活
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink.WDMLinkList.get(i).setActive(true);
		}

		if (route1 != null && route1.getWDMLinkList().size() != 0) {
			// 开始判断这个route上是否有可用波道资源
			if (Route.isRouteHaveWave(route1, wave1, traffic)) {// true则有，则为该route分配波道资源wave
				for (int hop = 0; hop < route1.getWDMLinkList().size(); hop++) {// 为每一条wdmLink链路分配连续波长
					WDMLink wdmLink = route1.getWDMLinkList().get(hop);
					{
						if (wdmLink.getWaveLengthList().get(wave1).getStatus().equals(Status.空闲)) {
							wdmLink.setRemainResource(wdmLink.getRemainResource() - 1);
						} // 更新链路剩余资源
						wdmLink.getWaveLengthList().get(wave1).setStatus(Status.恢复);// 改变工作状态
						wdmLink.getWaveLengthList().get(wave1).getDynamicTrafficList().add(traffic);// 加入该波长的动态业务表
						wdmLink.getCarriedTrafficList().add(traffic);// 为链路添加承载业务
					}
				}
				return route1;
			} // 波长分配完毕
			if (Route.isRouteHaveWave(route1, wave2, traffic)) {// true则有，则为该route分配波道资源wave
				for (int hop = 0; hop < route1.getWDMLinkList().size(); hop++) {// 为每一条wdmLink链路分配连续波长
					WDMLink wdmLink = route1.getWDMLinkList().get(hop);
					{
						if (wdmLink.getWaveLengthList().get(wave2).getStatus().equals(Status.空闲)) {
							wdmLink.setRemainResource(wdmLink.getRemainResource() - 1);
						} // 更新链路剩余资源
						wdmLink.getWaveLengthList().get(wave2).setStatus(Status.恢复);// 改变工作状态
						wdmLink.getWaveLengthList().get(wave2).getDynamicTrafficList().add(traffic);// 加入该波长的动态业务表
						wdmLink.getCarriedTrafficList().add(traffic);// 为链路添加承载业务
					}
				}
				return route1;
			} // 波长分配完毕
				// 如果wave1和wave2都没有资源，则用以业务首末节点算出的route2进行资源分配
			if (Route.isRouteHaveWave(route1, wave1, traffic) == false
					&& Route.isRouteHaveWave(route1, wave2, traffic) == false) {
				if (route2 != null && route2.getWDMLinkList().size() != 0) {
					// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// 如果满足osnr要求
					// 避开OSNR
					if (1 > 0) {
						if (CommonAlloc.allocateWaveLength3(route2) && PortAlloc.allocatePort(route2)) {// 对光纤链路分配时隙成功，并且分配端口成功
							// CommonAlloc.fallBuffer.append("业务:" + traffic + "预置路由及资源分配成功！\r\n");
							traffic.setDynamicRoute(route2);// 如果路由不为空则设置预置重路由
							traffic.setStatus(TrafficStatus.动态重路由已分配);
							return route2;
						} else {// 资源分配失败

							// CommonAlloc.fallBuffer.append("业务:" + traffic + "预置路由波长或端口资源分配失败！\r\n");
							RouteAlloc.releaseRoute(route2);
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
					// 算路失败
					// CommonAlloc.fallBuffer.append("业务:" + traffic + "由于波道资源不足预置路由算路失败！\r\n");
				}
			}
		}
		else if (route2 != null && route2.getWDMLinkList().size() != 0) {
			// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// 如果满足osnr要求
			// 避开OSNR
			if (1 > 0) {
				if (CommonAlloc.allocateWaveLength3(route2) && PortAlloc.allocatePort(route2)) {// 对光纤链路分配时隙成功，并且分配端口成功
					// CommonAlloc.fallBuffer.append("业务:" + traffic + "预置路由及资源分配成功！\r\n");
					traffic.setDynamicRoute(route2);// 如果路由不为空则设置预置重路由
					traffic.setStatus(TrafficStatus.动态重路由已分配);
					return route2;
				} else {// 资源分配失败

					// CommonAlloc.fallBuffer.append("业务:" + traffic + "预置路由波长或端口资源分配失败！\r\n");
					RouteAlloc.releaseRoute(route2);
				}
			}
			// else {// 如果osnr超过门限
			// if (Suggest.isSuggested == true) {// 2017.10.14
			// OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
			// OSNR.OSNRRouteList.add(route);//10.19
			// }
			// CommonAlloc.fallBuffer.append("业务:" + traffic + "保护路由OSNR不满足条件！\r\n");
			// }
		}
		return null;
	}
}
