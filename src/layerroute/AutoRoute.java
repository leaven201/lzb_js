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
//�����ƣ�AutoRoute
//�����ã����·���Զ�ӳ��
//�����ߣ�sunday
//����޸�ʱ�䣺2010-5-
//*/
//public class AutoRoute {
//    public AutoRoute() {
//    }
//
//    public static String errmessage = "";
//
//    /*
//     * �������ƣ�SetFiberLinklist �������ã�Ϊһ��WDMLink���ò��·��
//     * �����������Ҫ���ò��·�ɵ�WDMLink����Ӧfiber���·��List<FiberLink> ���ز�����void
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
//     * �������ƣ�WDMAutoRoute �������ã�ΪWDMLink������·�� ��������� ���ز�����void
//     */
//    public static boolean WDMAutoRoute(WDMLink wdmlink) {
//	// int nNodeSum
//	// =Algorithm.SetNodeRankID(CommonNode.s_lFiberNodeList);//���ýڵ�id
//	wdmlink.getFiberLinkList().clear();
//	if (wdmlink.getFiberLinkList().isEmpty()) {
//	    CommonNode from = wdmlink.getFromNode();
//	    CommonNode to = wdmlink.getToNode();
//	    LinkedList<FiberLink> poolLinkList = new LinkedList<FiberLink>();
//	    Route route = RouteAlloc.findFiberRoute(from, to, 1);
//	    if (route != null)
//		WDMSetFiberLinklist(wdmlink, route.getFiberLinkList());
//	    if (wdmlink.getFiberLinkList().size() == 0) {
//		errmessage = "�ײ��޿�����·";
//		return false;
//	    }
//	    // if(!commonAlloc.dealWithInterLayerFiber(wdmlink,Layer.WDM )){
//	    // errmessage = "���·����Դ����ʧ��";
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
//	errmessage = "�ɹ����ɲ��·��";
//	return true;
//    }
//
////    public static boolean OTNAutoRoute(WDMLink wdmlink) {
////	// int nNodeSum
////	// =Algorithm.SetNodeRankID(CommonNode.s_lFiberNodeList);//���ýڵ�id
////	wdmlink.getFiberLinkList().clear();
////	if (wdmlink.getFiberLinkList().isEmpty()) {
////	    CommonNode from = wdmlink.getFromNode();
////	    CommonNode to = wdmlink.getToNode();
////	    LinkedList<FiberLink> poolLinkList = new LinkedList<FiberLink>();
////	    Route route = algorithm.RWAMain(from, to, Layer.FIBER, 2, poolLinkList);
////	    if (route != null)
////		WDMSetFiberLinklist(wdmlink, route.getFiberLinkList());
////	    if (wdmlink.getFiberLinkList().size() == 0) {
////		errmessage = "�ײ��޿�����·";
////		return false;
////	    }
////	    if (!commonAlloc.dealWithInterLayerFiber(wdmlink, Layer.OTN)) {
////		errmessage = "���·����Դ����ʧ��";
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
////	errmessage = "�ɹ����ɲ��·��";
////	return true;
////    }
//
//}
