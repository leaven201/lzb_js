package algorithm;

import java.util.LinkedList;
import java.util.List;
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

//	public static Route findFiberRoute(Traffic tra, int flag) {// 对业务tra进行光纤路由分配，
//		// flag=0 长度;
//		// =1跳数 ;
//		// =2 负载均衡算法
//		LinkedList<SpanLink> returnLinkList = new LinkedList<SpanLink>();// 算路结果存储在这里，还有route里的FiberLinkList
//		LinkedList<CommonNode> returnNodeList = new LinkedList<CommonNode>();// 链路所经过的Node
//
//		if (DAlgorithm.dijkstra(flag, tra.getFromNode(), tra.getToNode(), CommonNode.allNodeList,
//				SpanLink.allSpanLinkList, returnLinkList, returnNodeList) && returnLinkList.size() <= hopLimit) {// 如果算路成功
//			Route route = new Route(tra.getFromNode(), tra.getToNode(), returnLinkList, returnNodeList, tra);
//			// route.getFiberLinkList().addAll(returnLinkList);//好像创建新的路由的过程中，已经赋值了吧
//			return route;
//		} else
//			return null;
//	}

//	// 进行wdm和fiber层间路由映射时所需的方法
//	public static Route findSpanRoute(CommonNode from, CommonNode to, int flag) {
//
//		LinkedList<SpanLink> returnLinkList = new LinkedList<SpanLink>();// 算路结果存储在这里，还有route里的FiberLinkList
//		LinkedList<CommonNode> returnNodeList = new LinkedList<CommonNode>();// 链路所经过的Node
//
//		if (DAlgorithm.dijkstra(flag, from, to, CommonNode.allNodeList, SpanLink.allSpanLinkList, returnLinkList,
//				returnNodeList) && returnLinkList.size() <= hopLimit) {// 如果算路成功
//			Route route = new Route(from, to, returnLinkList, returnNodeList);
//			// route.getFiberLinkList().addAll(returnLinkList);
//			return route;
//		} else
//			return null;
//	}

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

		if (DAlgorithm.dijkstra(flag, tra.getFromNode(), tra.getToNode(), CommonNode.allNodeList,
				WDMLink.WDMLinkList, returnLinkList, returnNodeList) && returnLinkList.size() <= hopLimit) {// 如果算路成功
			Route route = new Route(tra.getFromNode(), tra.getToNode(), returnLinkList, returnNodeList, tra,0);
			for (WDMLink wdmLink : returnLinkList) {
				route.getFiberLinkList().addAll(wdmLink.getFiberLinkList());
			}
			return route;
		} else
			return null;
	}
	public static Route findWDMRouteHaveMustPassNode1(Traffic tra, int flag,CommonNode node) {
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

		if (DAlgorithm.dijkstra(flag, tra.getFromNode(), node, CommonNode.allNodeList,
				WDMLink.WDMLinkList, returnLinkList, returnNodeList) && returnLinkList.size() <= hopLimit) {// 如果算路成功
			Route route = new Route(returnLinkList, returnNodeList);
			for (WDMLink wdmLink : returnLinkList) {
				route.getFiberLinkList().addAll(wdmLink.getFiberLinkList());
			}
			return route;
		} else
			return null;
	}
	public static Route findWDMRouteHaveMustPassNode2(Traffic tra, int flag,CommonNode node) {
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

		if (DAlgorithm.dijkstra(flag, node, tra.getToNode(), CommonNode.allNodeList,
				WDMLink.WDMLinkList, returnLinkList, returnNodeList) && returnLinkList.size() <= hopLimit) {// 如果算路成功
			Route route = new Route(returnLinkList, returnNodeList);
			for (WDMLink wdmLink : returnLinkList) {
				route.getFiberLinkList().addAll(wdmLink.getFiberLinkList());
			}
			return route;
		} else
			return null;
	}
	public static Route findWDMRouteByTwoNode(Traffic tra, int flag,CommonNode node1,CommonNode node2) {
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

		if (DAlgorithm.dijkstra(flag, node1, node2, CommonNode.allNodeList,
				WDMLink.WDMLinkList, returnLinkList, returnNodeList) && returnLinkList.size() <= hopLimit) {// 如果算路成功
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

	//抗毁输出专用
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
						ts.setStatus(Status.空闲);
					}
				}
			}
			// 将路由设置为空
			tra.setWorkRoute(null);
			tra.setProtectRoute(null);
			tra.setResumeRoute(null);//2017.10.18
			tra.setResumeRoutePro(null);//2017.10.18
		} // 循环完毕
			//// wb 2017.10.13
		for (WDMLink wlink : WDMLink.WDMLinkList) {// 恢复状态
			for (WaveLength wave : wlink.getWaveLengthList()) {
//				wave.setUsed(false);
				wave.setPre(false);
			}
		}
//		for (CommonNode node : CommonNode.allNodeList) {
//			node.setRegenerated(false);//将所有节点再生功能重置为false
//			for (Port port : node.getPortList()) {
////				port.setUsed(false);
//				port.setIspre(false);
//			}
//		}
//		// 清空wdmlinksrlg和非导入生成的spanlinksrlg
//		Iterator<LinkRGroup> it = LinkRGroup.SRLGroupList.iterator();
//		while (it.hasNext()) {
//			LinkRGroup group = it.next();
//			if ((group.getBelongLayer().equals(Layer.Span) && group.isNatrue() == false)
//					|| group.getBelongLayer().equals(Layer.WDM)) {
//				it.remove();
//			}
//		}
		
//		LinkRGroup.clicked = false;// 恢复为未映射
//		Suggest.isSurvived = false;// 抗毁恢复默认设置
//		Suggest.conversionNodeList.clear();// 清空
//		Suggest.conversionNodeList2.clear();// 清空
//		Suggest.allosnrNodeList.clear();//2017.10.17
//		Suggest.allosnrNodeList2.clear();//2017.10.17
//		OSNR.OSNRRouteList.clear();//2017.10.25
//		OSNR.allOSNRList.clear();// 清空osnrlist
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
				if(tra.getPreRoute()!=null&&tra.getPreRoute().getWDMLinkList().size()!=0)
					wdmList.addAll(tra.getPreRoute().getWDMLinkList());
				for (WDMLink wl : wdmList) {
					wl.getCarriedTrafficList().clear();
					wl.setRemainResource(DataSave.waveNum);
					for (WaveLength ts : wl.getWaveLengthList()) {
						ts.setCarriedTraffic(null);
						ts.setStatus(Status.空闲);
					}
				}
			}
			// 将路由设置为空
			tra.setWorkRoute(null);
			tra.setProtectRoute(null);
			tra.setPreRoute(null);
			tra.setResumeRoute(null);//2017.10.18
			tra.setResumeRoutePro(null);//2017.10.18
		} // 循环完毕
			//// wb 2017.10.13
		for (WDMLink wlink : WDMLink.WDMLinkList) {// 恢复状态
			for (WaveLength wave : wlink.getWaveLengthList()) {
				wave.setUsed(false);
				wave.setPre(false);
			}
		}
//		for (CommonNode node : CommonNode.allNodeList) {
//			node.setRegenerated(false);//将所有节点再生功能重置为false
//			for (Port port : node.getPortList()) {
//				port.setUsed(false);
//				port.setIspre(false);
//			}
//		}
//		// 清空wdmlinksrlg和非导入生成的spanlinksrlg
//		Iterator<LinkRGroup> it = LinkRGroup.SRLGroupList.iterator();
//		while (it.hasNext()) {
//			LinkRGroup group = it.next();
//			if ((group.getBelongLayer().equals(Layer.Fiber) && group.isNatrue() == false)
//					|| group.getBelongLayer().equals(Layer.WDM)) {
//				it.remove();
//			}
//		}
		
		LinkRGroup.clicked = false;// 恢复为未映射
		Suggest.isSurvived = false;// 抗毁恢复默认设置
		Suggest.conversionNodeList.clear();// 清空
		Suggest.conversionNodeList2.clear();// 清空
		Suggest.allosnrNodeList.clear();//2017.10.17
		Suggest.allosnrNodeList2.clear();//2017.10.17
//		OSNR.OSNRRouteList.clear();//2017.10.25
//		OSNR.allOSNRList.clear();// 清空osnrlist
		////
		//将所有链路设为激活状态
		for(int i=0;i<WDMLink.WDMLinkList.size();i++) {
			WDMLink.WDMLinkList.get(i).setActive(true);
		}

		Traffic.releaseAllPortResource();// 释放所有端口资源
		Dlg_PolicySetting.isDesign = false;// CC 2017.5.2
	}

	public static void releaseRoute(Route route) {// 释放路由承载业务占用资源，
		if (route == null)
			return;
		Traffic tra = route.getBelongTraffic();
		List<WDMLink> wdmLinkList = route.getWDMLinkList();
		for (WDMLink wdmLink : wdmLinkList) {
			if (wdmLink.getCarriedTrafficList().remove(tra)) {// 不需要判断是否包含，因为其会返回BOOLEAN
				wdmLink.setRemainResource(wdmLink.getRemainResource() + 1);
				for (WaveLength wl : wdmLink.getWaveLengthList()) {
					if (wl.getCarriedTraffic() == null)
						continue;
					else if (wl.getCarriedTraffic().equals(tra)) {
						wl.setCarriedTraffic(null);
						wl.setStatus(Status.空闲);
						wl.setPre(false);// 2027.10.13
					}
				}
			}
		}
	}

}
