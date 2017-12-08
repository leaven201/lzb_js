//package layerroute;
//
//import java.util.LinkedList;
//import java.util.List;
//import java.util.ListIterator;
//import algorithm.algorithm;
//import data.*;
//import enums.Layer;
//import enums.PortRate;
//import enums.PortStatus;
//import enums.PortType;
//import algorithm.*;
//
///*
//类名称：AutoRoute
//类作用：层间路由自动映射
//类作者：sunday
//最后修改时间：2010-5-
//*/
//public class AutoRoute {
//    public AutoRoute() {
//    }
//
//    public static String errmessage = "";
//
//    /*
//     * 函数名称：SetFiberLinklist 函数作用：为一条WDMLink设置层间路由
//     * 输入参数：需要设置层间路由的WDMLink，对应fiber层的路由List<FiberLink> 返回参数：void
//     */
//    public static void WDMSetFiberLinklist(WDMLink wdmlink, List<FiberLink> fiberlinklist) {
//	if (wdmlink.getFiberLinkList().isEmpty()) {
//	    ListIterator<FiberLink> it = fiberlinklist.listIterator();
//	    while (it.hasNext()) {
//		FiberLink itlink = it.next();
//		wdmlink.getFiberLinkList().add(itlink);
//		// wdmlink.setFiberrLinkList(itlink);
//	    }
//	}
//    }
//
//    /*
//     * 函数名称：WDMAutoRoute 函数作用：为WDMLink计算层间路由 输入参数： 返回参数：void
//     */
//    public static boolean WDMAutoRoute(WDMLink wdmlink) {
//	// int nNodeSum
//	// =Algorithm.SetNodeRankID(CommonNode.s_lFiberNodeList);//设置节点id
//	wdmlink.getFiberLinkList().clear();
//	if (wdmlink.getFiberLinkList().isEmpty()) {
//	    CommonNode from = wdmlink.getFromNode();
//	    CommonNode to = wdmlink.getToNode();
//	    LinkedList<FiberLink> poolLinkList = new LinkedList<FiberLink>();
//	    Route route = RouteAlloc.findFiberRoute(from, to, 1);
//	    if (route != null)
//		WDMSetFiberLinklist(wdmlink, route.getFiberLinkList());
//	    if (wdmlink.getFiberLinkList().size() == 0) {
//		errmessage = "底层无可用链路";
//		return false;
//	    }
//	    // if(!commonAlloc.dealWithInterLayerFiber(wdmlink,Layer.WDM )){
//	    // errmessage = "层间路由资源分配失败";
//	    // wdmlink.getFiberLinkList().clear();
//	    // return false;
//	    // }
//	    int length = 0;
//	    for (int i = 0; i < route.getFiberLinkList().size(); i++) {
//		length += (int) route.getFiberLinkList().get(i).getLength();
//	    }
//	    if (length != 0)
//		wdmlink.setLength(length);
//	}
//	errmessage = "成功生成层间路由";
//	return true;
//    }
//
////    public static boolean OTNAutoRoute(WDMLink wdmlink) {
////	// int nNodeSum
////	// =Algorithm.SetNodeRankID(CommonNode.s_lFiberNodeList);//设置节点id
////	wdmlink.getFiberLinkList().clear();
////	if (wdmlink.getFiberLinkList().isEmpty()) {
////	    CommonNode from = wdmlink.getFromNode();
////	    CommonNode to = wdmlink.getToNode();
////	    LinkedList<FiberLink> poolLinkList = new LinkedList<FiberLink>();
////	    Route route = algorithm.RWAMain(from, to, Layer.FIBER, 2, poolLinkList);
////	    if (route != null)
////		WDMSetFiberLinklist(wdmlink, route.getFiberLinkList());
////	    if (wdmlink.getFiberLinkList().size() == 0) {
////		errmessage = "底层无可用链路";
////		return false;
////	    }
////	    if (!commonAlloc.dealWithInterLayerFiber(wdmlink, Layer.OTN)) {
////		errmessage = "层间路由资源分配失败";
////		wdmlink.getFiberLinkList().clear();
////		return false;
////	    }
////	    int length = 0;
////	    for (int i = 0; i < route.getFiberLinkList().size(); i++) {
////		length += (int) route.getFiberLinkList().get(i).getLength();
////	    }
////	    if (length != 0)
////		wdmlink.setLength(length);
////	}
////	errmessage = "成功生成层间路由";
////	return true;
////    }
//
//}
