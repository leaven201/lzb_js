package algorithm;

import java.util.LinkedList;
import java.util.List;

import com4j.COM4J;
import data.*;
import dialog.Dlg_PolicySetting;
import enums.*;
import java.util.Iterator;

/***
 * 功能：主要是调用D算法进行路由的计算，包括工作路由以及保护路由的计算， 以及如果光纤算路不通情况下其他的算路方法。 暂时不包括次优路的计算。
 * 
 * @author CC
 * @since 2016/7/4
 */
public class RouteAlloc {
	public static boolean isLimitHop = false; // 是否限制跳数

	public static int hopLimit = Integer.MAX_VALUE;// 12.9跳数限制

	// public static Route findFiberRoute(Traffic tra, int flag) {// 对业务tra进行光纤路由分配，
	// // flag=0 长度;
	// // =1跳数 ;
	// // =2 负载均衡算法
	// LinkedList<SpanLink> returnLinkList = new LinkedList<SpanLink>();//
	// 算路结果存储在这里，还有route里的FiberLinkList
	// LinkedList<CommonNode> returnNodeList = new LinkedList<CommonNode>();//
	// 链路所经过的Node
	//
	// if (DAlgorithm.dijkstra(flag, tra.getFromNode(), tra.getToNode(),
	// CommonNode.allNodeList,
	// SpanLink.allSpanLinkList, returnLinkList, returnNodeList) &&
	// returnLinkList.size() <= hopLimit) {// 如果算路成功
	// Route route = new Route(tra.getFromNode(), tra.getToNode(), returnLinkList,
	// returnNodeList, tra);
	// // route.getFiberLinkList().addAll(returnLinkList);//好像创建新的路由的过程中，已经赋值了吧
	// return route;
	// } else
	// return null;
	// }

	// // 进行wdm和fiber层间路由映射时所需的方法
	// public static Route findSpanRoute(CommonNode from, CommonNode to, int flag) {
	//
	// LinkedList<SpanLink> returnLinkList = new LinkedList<SpanLink>();//
	// 算路结果存储在这里，还有route里的FiberLinkList
	// LinkedList<CommonNode> returnNodeList = new LinkedList<CommonNode>();//
	// 链路所经过的Node
	//
	// if (DAlgorithm.dijkstra(flag, from, to, CommonNode.allNodeList,
	// SpanLink.allSpanLinkList, returnLinkList,
	// returnNodeList) && returnLinkList.size() <= hopLimit) {// 如果算路成功
	// Route route = new Route(from, to, returnLinkList, returnNodeList);
	// // route.getFiberLinkList().addAll(returnLinkList);
	// return route;
	// } else
	// return null;
	// }

	// 实际分配为业务寻找路由的入口
	public static Route findWDMRoute(Traffic tra, int flag) {
		// 对业务tra进行wdmlink路由分配，flag=1跳数 ;
		// =0 长度;
		// =2 负载均衡算法

		// //每次算路之前重新进行层间路由映射
		// if (FiberLink.fiberLinkList.size() != 0) {// 为wdmlink计算层间路由
		// for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
		// if (!WDMLink.WDMLinkList.get(i).getFiberLinkList().isEmpty())//
		// 若为空，直接映射
		// AutoRoute.WDMAutoRoute(WDMLink.WDMLinkList.get(i));
		// }
		// }
		LinkedList<WDMLink> returnLinkList = new LinkedList<WDMLink>();// 算路结果存储在这里，还有route里的FiberLinkList
		LinkedList<CommonNode> returnNodeList = new LinkedList<CommonNode>();// 链路所经过的Node

		if (DAlgorithm.dijkstra(flag, tra.getFromNode(), tra.getToNode(), CommonNode.allNodeList, WDMLink.WDMLinkList,
				returnLinkList, returnNodeList) && returnLinkList.size() <= hopLimit) {// 如果算路成功
			Route route = new Route(tra.getFromNode(), tra.getToNode(), returnLinkList, returnNodeList, tra, 0);
			for (WDMLink wdmLink : returnLinkList) {
				route.getFiberLinkList().addAll(wdmLink.getFiberLinkList());
			}
			return route;
		} else
			return null;
	}

	public static Route findWDMRouteHaveMustPassNode1(Traffic tra, int flag, CommonNode node) {
		// 对业务tra进行wdmlink路由分配，flag=1跳数 ;
		// =0 长度;
		// =2 负载均衡算法

		// //每次算路之前重新进行层间路由映射
		// if (FiberLink.fiberLinkList.size() != 0) {// 为wdmlink计算层间路由
		// for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
		// if (!WDMLink.WDMLinkList.get(i).getFiberLinkList().isEmpty())//
		// 若为空，直接映射
		// AutoRoute.WDMAutoRoute(WDMLink.WDMLinkList.get(i));
		// }
		// }
		LinkedList<WDMLink> returnLinkList = new LinkedList<WDMLink>();// 算路结果存储在这里，还有route里的FiberLinkList
		LinkedList<CommonNode> returnNodeList = new LinkedList<CommonNode>();// 链路所经过的Node

		if (DAlgorithm.dijkstra(flag, tra.getFromNode(), node, CommonNode.allNodeList, WDMLink.WDMLinkList,
				returnLinkList, returnNodeList) && returnLinkList.size() <= hopLimit) {// 如果算路成功
			Route route = new Route(returnLinkList, returnNodeList);
			for (WDMLink wdmLink : returnLinkList) {
				route.getFiberLinkList().addAll(wdmLink.getFiberLinkList());
			}
			return route;
		} else
			return null;
	}

	public static Route findWDMRouteHaveMustPassNode2(Traffic tra, int flag, CommonNode node) {
		// 对业务tra进行wdmlink路由分配，flag=1跳数 ;
		// =0 长度;
		// =2 负载均衡算法

		// //每次算路之前重新进行层间路由映射
		// if (FiberLink.fiberLinkList.size() != 0) {// 为wdmlink计算层间路由
		// for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
		// if (!WDMLink.WDMLinkList.get(i).getFiberLinkList().isEmpty())//
		// 若为空，直接映射
		// AutoRoute.WDMAutoRoute(WDMLink.WDMLinkList.get(i));
		// }
		// }
		LinkedList<WDMLink> returnLinkList = new LinkedList<WDMLink>();// 算路结果存储在这里，还有route里的FiberLinkList
		LinkedList<CommonNode> returnNodeList = new LinkedList<CommonNode>();// 链路所经过的Node

		if (DAlgorithm.dijkstra(flag, node, tra.getToNode(), CommonNode.allNodeList, WDMLink.WDMLinkList,
				returnLinkList, returnNodeList) && returnLinkList.size() <= hopLimit) {// 如果算路成功
			Route route = new Route(returnLinkList, returnNodeList);
			for (WDMLink wdmLink : returnLinkList) {
				route.getFiberLinkList().addAll(wdmLink.getFiberLinkList());
			}
			return route;
		} else
			return null;
	}

	public static Route findWDMRouteByTwoNode(Traffic tra, int flag, CommonNode node1, CommonNode node2) {
		// 对业务tra进行wdmlink路由分配，flag=1跳数 ;
		// =0 长度;
		// =2 负载均衡算法

		// //每次算路之前重新进行层间路由映射
		// if (FiberLink.fiberLinkList.size() != 0) {// 为wdmlink计算层间路由
		// for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
		// if (!WDMLink.WDMLinkList.get(i).getFiberLinkList().isEmpty())//
		// 若为空，直接映射
		// AutoRoute.WDMAutoRoute(WDMLink.WDMLinkList.get(i));
		// }
		// }
		LinkedList<WDMLink> returnLinkList = new LinkedList<WDMLink>();// 算路结果存储在这里，还有route里的FiberLinkList
		LinkedList<CommonNode> returnNodeList = new LinkedList<CommonNode>();// 链路所经过的Node

		if (DAlgorithm.dijkstra(flag, node1, node2, CommonNode.allNodeList, WDMLink.WDMLinkList, returnLinkList,
				returnNodeList) && returnLinkList.size() <= hopLimit) {// 如果算路成功
			Route route = new Route(returnLinkList, returnNodeList);
			for (WDMLink wdmLink : returnLinkList) {
				route.getFiberLinkList().addAll(wdmLink.getFiberLinkList());
			}
			return route;
		} else
			return null;
	}

	public static boolean isRouteEmpty(List<Traffic> tral) {// 判断业务列表是否存在工作路由
		for (Traffic tra : tral) {
			if (!(tra.getWorkRoute() == null))
				return false;
		}
		return true;
	}

	// 抗毁输出专用
	public static void clearAllTrafficRoute2(List<Traffic> tral) {// 清空业务表上业务所有占用的资源
		for (Traffic tra : tral) {
			if ((tra.getWorkRoute() == null))// 12.9
				continue;// 判断条件需要修改CC 12.9 已修改
			else if (tra.getWorkRoute().getWDMLinkList() != null) {// 12.9
				List<WDMLink> wdmList = tra.getWorkRoute().getWDMLinkList();
				if (tra.getProtectRoute() != null && tra.getProtectRoute().getWDMLinkList().size() != 0)
					wdmList.addAll(tra.getProtectRoute().getWDMLinkList());
				for (WDMLink wl : wdmList) {
					wl.getCarriedTrafficList().clear();
					wl.setRemainResource(wl.getWaveNum());
					for (WaveLength ts : wl.getWaveLengthList()) {
						ts.setCarriedTraffic(null);
						ts.setCarriedTrafficList(null);
						ts.setStatus(Status.空闲);
					}
				}
			}
			// 将路由设置为空
			tra.setWorkRoute(null);
			tra.setProtectRoute(null);
			tra.setResumeRoute(null);// 2017.10.18
			tra.setResumeRoutePro(null);// 2017.10.18
		} // 循环完毕
			//// wb 2017.10.13
		for (WDMLink wlink : WDMLink.WDMLinkList) {// 恢复状态
			for (WaveLength wave : wlink.getWaveLengthList()) {
				// wave.setUsed(false);
				wave.setPre(false);
			}
		}
		// for (CommonNode node : CommonNode.allNodeList) {
		// node.setRegenerated(false);//将所有节点再生功能重置为false
		// for (Port port : node.getPortList()) {
		//// port.setUsed(false);
		// port.setIspre(false);
		// }
		// }
		// // 清空wdmlinksrlg和非导入生成的spanlinksrlg
		// Iterator<LinkRGroup> it = LinkRGroup.SRLGroupList.iterator();
		// while (it.hasNext()) {
		// LinkRGroup group = it.next();
		// if ((group.getBelongLayer().equals(Layer.Span) && group.isNatrue() == false)
		// || group.getBelongLayer().equals(Layer.WDM)) {
		// it.remove();
		// }
		// }

		// LinkRGroup.clicked = false;// 恢复为未映射
		// Suggest.isSurvived = false;// 抗毁恢复默认设置
		// Suggest.conversionNodeList.clear();// 清空
		// Suggest.conversionNodeList2.clear();// 清空
		// Suggest.allosnrNodeList.clear();//2017.10.17
		// Suggest.allosnrNodeList2.clear();//2017.10.17
		// OSNR.OSNRRouteList.clear();//2017.10.25
		// OSNR.allOSNRList.clear();// 清空osnrlist
		////
		Traffic.releaseAllPortResource();// 释放所有端口资源
		Dlg_PolicySetting.isDesign = false;// CC 2017.5.2
	}

	public static void clearAllTrafficRoute(List<Traffic> tral) {// 清空业务表上业务所有占用的资源
		for (Traffic tra : tral) {
			if ((tra.getWorkRoute() == null))// 12.9
				continue;// 判断条件需要修改CC 12.9 已修改
			else if (tra.getWorkRoute().getWDMLinkList() != null) {// 12.9
				List<WDMLink> wdmList = tra.getWorkRoute().getWDMLinkList();
				if (tra.getProtectRoute() != null && tra.getProtectRoute().getWDMLinkList().size() != 0)
					wdmList.addAll(tra.getProtectRoute().getWDMLinkList());
				if (tra.getPreRoute() != null && tra.getPreRoute().getWDMLinkList().size() != 0)
					wdmList.addAll(tra.getPreRoute().getWDMLinkList());
				for (WDMLink wl : wdmList) {
					wl.getCarriedTrafficList().clear();
					wl.setRemainResource(DataSave.waveNum);
					wl.setWorkUsedWaveNum(0);
					wl.setOtherUsedWaveNum(0);
					for (WaveLength ts : wl.getWaveLengthList()) {
						ts.setCarriedTraffic(null);
						ts.getCarriedTrafficList().clear();
						ts.getDynamicTrafficList().clear();
						ts.getPreTrafficList().clear();
						ts.setStatus(Status.空闲);
					}
				}
				
			}
			// 将路由设置为空
			tra.setWorkRoute(null);
			tra.setProtectRoute(null);
			tra.setPreRoute(null);
			tra.setResumeRoute(null);// 2017.10.18
			tra.setResumeRoutePro(null);// 2017.10.18
		} // 循环完毕
		
		//清空所有链路及波道资源
		for(WDMLink link : WDMLink.WDMLinkList) {
			link.setRemainResource(DataSave.waveNum);
			link.setWorkUsedWaveNum(0);
			link.setOtherUsedWaveNum(0);
			link.getCarriedTrafficList().clear();
			link.setDynUsedLink(new int[81]);
			for(WaveLength wave : link.getWaveLengthList()) {
				wave.carriedTrafficList.clear();
				wave.dynamicTrafficList.clear();
				wave.preTrafficList.clear();
				wave.setCarriedTraffic(null);
				wave.setStatus(Status.空闲);
				wave.setM_nFree(100);
				wave.setM_nUsed(0);
			}
		}
		for(CommonNode node : CommonNode.allNodeList) {
			node.setWorkOTUNum(0);
			node.setRestoreOTUNum(0);
			node.setDynUsedUpdown(0);
			node.setUsedUpdown(0);
			
		}
		
		
			//// wb 2017.10.13
		for (WDMLink wlink : WDMLink.WDMLinkList) {// 恢复状态
			for (WaveLength wave : wlink.getWaveLengthList()) {
				wave.setUsed(false);
				wave.setPre(false);
			}
		}
		// for (CommonNode node : CommonNode.allNodeList) {
		// node.setRegenerated(false);//将所有节点再生功能重置为false
		// for (Port port : node.getPortList()) {
		// port.setUsed(false);
		// port.setIspre(false);
		// }
		// }
		// // 清空wdmlinksrlg和非导入生成的spanlinksrlg
		// Iterator<LinkRGroup> it = LinkRGroup.SRLGroupList.iterator();
		// while (it.hasNext()) {
		// LinkRGroup group = it.next();
		// if ((group.getBelongLayer().equals(Layer.Fiber) && group.isNatrue() == false)
		// || group.getBelongLayer().equals(Layer.WDM)) {
		// it.remove();
		// }
		// }

		LinkRGroup.clicked = false;// 恢复为未映射
		Suggest.isSurvived = false;// 抗毁恢复默认设置
		Suggest.conversionNodeList.clear();// 清空
		Suggest.conversionNodeList2.clear();// 清空
		Suggest.allosnrNodeList.clear();// 2017.10.17
		Suggest.allosnrNodeList2.clear();// 2017.10.17
		// OSNR.OSNRRouteList.clear();//2017.10.25
		// OSNR.allOSNRList.clear();// 清空osnrlist
		////
		// 将所有链路设为激活状态
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink.WDMLinkList.get(i).setActive(true);
		}

		Traffic.releaseAllPortResource();// 释放所有端口资源
		Dlg_PolicySetting.isDesign = false;// CC 2017.5.2
	}

	// public static void releaseRoute(Route route) {// 释放路由承载业务占用资源，
	// if (route == null)
	// return;
	// Traffic tra = route.getBelongTraffic();
	// List<WDMLink> wdmLinkList = route.getWDMLinkList();
	// for (WDMLink wdmLink : wdmLinkList) {
	// //当返回为true时，说明这个wdmlink承载了该业务
	// if (wdmLink.getCarriedTrafficList().remove(tra)) {// 不需要判断是否包含，因为其会返回BOOLEAN
	// wdmLink.setRemainResource(wdmLink.getRemainResource() + 1);
	// for (WaveLength wl : wdmLink.getWaveLengthList()) {
	// if (wl.getCarriedTraffic() == null)
	// continue;
	// else if (wl.getCarriedTraffic().equals(tra)) {
	// wl.setCarriedTraffic(null);
	// wl.setStatus(Status.空闲);
	// wl.setPre(false);// 2027.10.13
	// }
	// }
	// }
	// }
	// }

	public static void releaseRoute(Route route) {// 释放路由承载业务占用资源，
		if (route == null)
			return;
		Traffic tra = route.getBelongTraffic();
		List<WDMLink> wdmLinkList = route.getWDMLinkList();
		for (WDMLink wdmLink : wdmLinkList) {
			// 当返回为true时，说明这个wdmlink承载了该业务
			if (wdmLink.getCarriedTrafficList().remove(tra)) {// 不需要判断是否包含，因为其会返回BOOLEAN

				// 链路资源是否需要+1需要谨慎考虑！首先链路资源跟波道数有关，每个波道的状态有 空闲、工作、保护、恢复
				// 情况1：波道状态如果是工作、保护的话就需要+1，并且将状态调回空闲、保护
				// 情况2：波道状态 如果是恢复时
				// （2.1）这个波道的preTrafficList、dynamicTrafficList如果size是1，说明是第一次被设为恢复，需要资源+1并把状态调回空闲
				// （2.2）这个波道的preTrafficList、dynamicTrafficList如果size>1，说明不是第一次被设为恢复，则资源及状态都不需要改变
				for (WaveLength wl : wdmLink.getWaveLengthList()) {
					if (wl.getCarriedTraffic() == null && wl.getDynamicTrafficList() == null
							&& wl.getPreTrafficList() == null)
						continue;
					// 波道wl的承载业务不为空，说明他们是被分配为工作或者保护，情况（1）
					else if (wl.getCarriedTraffic() != null) {
						if (wl.getCarriedTraffic().equals(tra)) {
							wdmLink.setRemainResource(wdmLink.getRemainResource() + 1);
							wl.setCarriedTraffic(null);
							wl.setStatus(Status.空闲);
							wl.setPre(false);
						} // 2027.10.13
					}
					// 波道wl的承载动态业务列表不为空，说明他们是被分配为动态恢复，情况（2）
					else if (wl.getDynamicTrafficList() != null) {
						// 当返回true时，说明这个DynamicTrafficList包含这个tra
						if (wl.getDynamicTrafficList().remove(tra)) {
							// remove这个tra以后如果DynamicTrafficList的大小为0则为情况2.1
							if (wl.getDynamicTrafficList().size() == 0) {
								wdmLink.setRemainResource(wdmLink.getRemainResource() + 1);
								wl.setStatus(Status.空闲);
							}
							// remove这个tra以后如果DynamicTrafficList的大小大于0则为情况2.2
							if (wl.getDynamicTrafficList().size() > 0) {
								// 链路可用资源不变
								// 波道状态不变
							}
						}
					} else if (wl.getPreTrafficList() != null) {
						// 当返回true时，说明这个PreTrafficList包含这个tra
						if (wl.getPreTrafficList().remove(tra)) {
							// remove这个tra以后如果PreTrafficList的大小为0则为情况2.1
							if (wl.getPreTrafficList().size() == 0) {
								wdmLink.setRemainResource(wdmLink.getRemainResource() + 1);
								wl.setStatus(Status.空闲);
							}
							// remove这个tra以后如果PreTrafficList的大小大于0则为情况2.2
							if (wl.getPreTrafficList().size() > 0) {
								// 链路可用资源不变
								// 波道状态不变
							}
						}
					}
				}
			}
		}
	}
	/*
	 * 抗毁仿真时，当计算出仿真路由simRoute时，在仿真结束后，需要清空仿真路由占用的资源 清空时需要与受影响路由进行比较，得出清空的具体办法
	 * 假设原有路由为：A-B-C-D-E-F 当C-D链路受损时，simRoute为：A-B-C-G-H-D-E-F
	 * 
	 * 1、对比 哪些是相同的链路， 新增的链路资源全部释放 2、相同链路， 对比simRoute上面使用的资源与原路由使用资源是否相同 相同不清，不同则清
	 * 
	 */
	// public static void releaseRouteForSim(Route brokenRoute,Route
	// simRoute,Traffic carriedTraffic) {
	// //找出新增的链路并且释放其资源
	// for(int i=0;i<simRoute.getWDMLinkList().size();i++) {
	// WDMLink newLink=simRoute.getWDMLinkList().get(i);
	// for(int j=0;j<brokenRoute.getWDMLinkList().size();j++) {
	// WDMLink oldLink=brokenRoute.getWDMLinkList().get(j);
	// //如果找到相同的说明不是新增的，则继续找下一个newLink
	// if(newLink.getName().equals(oldLink.getName()))
	// break;
	// //如果找到了oldLink的最后一条，都还没有找到一样的，则为新增的，将其资源释放
	// if((!newLink.getName().equals(oldLink.getName()))&&j==brokenRoute.getWDMLinkList().size()-1)
	//
	//
	// }
	// }
	//
	//
	//
	// }

	/*
	 * 功能：释放抗毁仿真时占用的资源 实现思路：
	 * waveLength中有carriedTraffic和preTrafficList及dynamicTrafficList属性
	 * carriedTraffic：当该波长承载的是工作或者保护路由时，会将相应的业务添加进来
	 * preTrafficList和dynamicTrafficList：当该波长承载的预置路由或动态路由时会将相应的业务添加进来
	 * 
	 * 抗毁仿真时，当遇到某路由受影响并且需要重新算路由时，算出的simRoute都属于动态路由，都会加入dynamicTrafficList中
	 * 
	 * 因此清资源的时候只需要判断该waveLength的dynamicTrafficList是否为空就知道是否需要清资源了
	 * 
	 * 注意1：如果carriedTraffic和dynamicTrafficList同时不为空，则说明这个波长是仿真时复用了原来的资源，则不需要清
	 * 注意2：相应链路的remainResource是否需要+1问题 具体看releaseRoute(Route route)方法中的思路
	 * 注意3：wavelength的状态要从恢复置为（1、工作；2、保护；3、恢复）的哪种？
	 */

/*	public static void releaseRouteForSim() {
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink wdmlink = WDMLink.WDMLinkList.get(i);
			for (int j = 0; j < wdmlink.getWaveLengthList().size(); j++) {
				WaveLength wl = wdmlink.getWaveLengthList().get(j);
				// 如果wavelength的dynamicTrafficList为空说明抗毁仿真时并没有用到该波道，不需要清空资源
				if (wl.getDynamicTrafficList().size() == 0)
					continue;

				// 如果wavelength的dynamicTrafficList不为空并且CarriedTraffic为空，则需要清资源
				if (wl.getDynamicTrafficList().size() != 0 && wl.getCarriedTraffic() == null) {
					// 如果preTrafficList为空，该wavelength只涉及了抗毁仿真中的资源使用，
					// 因此只需要将其DynamicTrafficList清空并且把其状态改为空闲,并且该链路的可用资源要+1
					if (wl.getPreTrafficList().size() == 0) {
						wl.getDynamicTrafficList().clear();
						wl.setStatus(Status.空闲);
						wl.getHolder().setRemainResource(wl.getHolder().getRemainResource() + 1);
					}
					// 如果preTrafficList不为空，说明该波长原来也承载了预置路由，
					// 因此需要把DynamicTrafficList清空并且将状态改为恢复
					if (wl.getPreTrafficList().size() > 0) {
						wl.getDynamicTrafficList().clear();
						wl.setStatus(Status.恢复);
					}
				}
				// 如果wavelength的dynamicTrafficList不为空且CarriedTraffic也不为空，则不需要清资源(+1)
				// 需将wavelength的状态调回原来的状态（工作、保护）
				// getCarriedTraffic()!=null就说明该wavelength承载的是工作或保护而不是预置。preTrafficList必为空
				if (wl.getDynamicTrafficList().size() != 0 && wl.getCarriedTraffic() != null) {

					wl.getDynamicTrafficList().clear();
					Traffic tra = wl.getCarriedTraffic();
					// 业务故障类型，0正常，1工作路由故障，2保护路由故障，3都故障（必定是故障了，所以只可能为1、2、3）
					int type = tra.getFaultType();
					// 有dynamicTrafficList说明一定是有重新算路了
					// 工作路由故障
					if (type == 1)
						wl.setStatus(Status.工作);
					// 保护路由故障
					if (type == 2)
						wl.setStatus(Status.保护);
					// 工作和恢复都故障的时候，需要判断这个波道到时是承载工作路由，还是保护路由
					if (type == 3) {
						for (int z = 0; z < tra.getWorkRoute().getWDMLinkList().size(); z++) {
							if (tra.getWorkRoute().getWDMLinkList().get(z).equals(wl.getHolder()))
								wl.setStatus(Status.工作);
						}
						for (int x = 0; x < tra.getProtectRoute().getWDMLinkList().size(); x++) {
							if (tra.getProtectRoute().getWDMLinkList().get(x).equals(wl.getHolder()))
								wl.setStatus(Status.保护);
						}
					}

				}
			}
		}
	}*/

	public static void releaseRouteForSim1() {
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink wdmlink = WDMLink.WDMLinkList.get(i);
			for (int j = 0; j < wdmlink.getWaveLengthList().size(); j++) {
				WaveLength wl = wdmlink.getWaveLengthList().get(j);

				if (wl.getStatus().equals(Status.仿真)) {
					// 如果wavelength的dynamicTrafficList不为空并且CarriedTraffic为空，则需要清资源
					if (wl.getDynamicTrafficList().size() != 0 && wl.getCarriedTraffic()==null) {
						// 如果preTrafficList为空，该wavelength只涉及了抗毁仿真中的资源使用，
						// 因此只需要将其DynamicTrafficList清空并且把其状态改为空闲,并且该链路的可用资源要+1
						if (wl.getPreTrafficList().size() == 0) {
							wl.getDynamicTrafficList().clear();
							wl.setStatus(Status.空闲);
						//	wl.getHolder().setRemainResource(wl.getHolder().getRemainResource() + 1);
							wl.getHolder().updateLinkResoucre();
						}
						// 如果preTrafficList不为空，说明该波长原来也承载了预置路由，
						// 因此需要把DynamicTrafficList清空并且将状态改为恢复
						if (wl.getPreTrafficList().size() > 0) {
							wl.getDynamicTrafficList().clear();
							wl.setStatus(Status.恢复);
							wl.getHolder().updateLinkResoucre();
						}
					}
					if (wl.getDynamicTrafficList().size() != 0 && wl.getCarriedTraffic() != null) {

						wl.getDynamicTrafficList().clear();
						Traffic tra = wl.getCarriedTraffic();
						// 业务故障类型，0正常，1工作路由故障，2保护路由故障，3都故障（必定是故障了，所以只可能为1、2、3）
						int type = tra.getFaultType();
						// 有dynamicTrafficList说明一定是有重新算路了
						// 工作路由故障
						if (type == 1)
							wl.setStatus(Status.工作);
						// 保护路由故障
						if (type == 2)
							wl.setStatus(Status.保护);
						// 工作和恢复都故障的时候，需要判断这个波道到时是承载工作路由，还是保护路由
						if (type == 3) {
							for (int z = 0; z < tra.getWorkRoute().getWDMLinkList().size(); z++) {
								if (tra.getWorkRoute().getWDMLinkList().get(z).equals(wl.getHolder()))
									wl.setStatus(Status.工作);
							}
							for (int x = 0; x < tra.getProtectRoute().getWDMLinkList().size(); x++) {
								if (tra.getProtectRoute().getWDMLinkList().get(x).equals(wl.getHolder()))
									wl.setStatus(Status.保护);
							}
						}

					}

				}				
			}
		}
	}

}
