package algorithm;

import java.util.LinkedList;
import java.util.List;
import data.*;
import dialog.Dlg_PolicySetting;
import enums.*;
import java.util.Iterator;

/***
 * ���ܣ���Ҫ�ǵ���D�㷨����·�ɵļ��㣬��������·���Լ�����·�ɵļ��㣬 �Լ����������·��ͨ�������������·������ ��ʱ����������·�ļ��㡣
 * 
 * @author CC
 * @since 2016/7/4
 */
public class RouteAlloc {
	public static boolean isLimitHop = false; // �Ƿ���������

	public static int hopLimit = Integer.MAX_VALUE;// 12.9��������

//	public static Route findFiberRoute(Traffic tra, int flag) {// ��ҵ��tra���й���·�ɷ��䣬
//		// flag=0 ����;
//		// =1���� ;
//		// =2 ���ؾ����㷨
//		LinkedList<SpanLink> returnLinkList = new LinkedList<SpanLink>();// ��·����洢���������route���FiberLinkList
//		LinkedList<CommonNode> returnNodeList = new LinkedList<CommonNode>();// ��·��������Node
//
//		if (DAlgorithm.dijkstra(flag, tra.getFromNode(), tra.getToNode(), CommonNode.allNodeList,
//				SpanLink.allSpanLinkList, returnLinkList, returnNodeList) && returnLinkList.size() <= hopLimit) {// �����·�ɹ�
//			Route route = new Route(tra.getFromNode(), tra.getToNode(), returnLinkList, returnNodeList, tra);
//			// route.getFiberLinkList().addAll(returnLinkList);//���񴴽��µ�·�ɵĹ����У��Ѿ���ֵ�˰�
//			return route;
//		} else
//			return null;
//	}

//	// ����wdm��fiber���·��ӳ��ʱ����ķ���
//	public static Route findSpanRoute(CommonNode from, CommonNode to, int flag) {
//
//		LinkedList<SpanLink> returnLinkList = new LinkedList<SpanLink>();// ��·����洢���������route���FiberLinkList
//		LinkedList<CommonNode> returnNodeList = new LinkedList<CommonNode>();// ��·��������Node
//
//		if (DAlgorithm.dijkstra(flag, from, to, CommonNode.allNodeList, SpanLink.allSpanLinkList, returnLinkList,
//				returnNodeList) && returnLinkList.size() <= hopLimit) {// �����·�ɹ�
//			Route route = new Route(from, to, returnLinkList, returnNodeList);
//			// route.getFiberLinkList().addAll(returnLinkList);
//			return route;
//		} else
//			return null;
//	}

	// ʵ�ʷ���Ϊҵ��Ѱ��·�ɵ����
	public static Route findWDMRoute(Traffic tra, int flag) {
		// ��ҵ��tra����wdmlink·�ɷ��䣬flag=1���� ;
		// =0 ����;
		// =2 ���ؾ����㷨

		// //ÿ����·֮ǰ���½��в��·��ӳ��
		// if (FiberLink.fiberLinkList.size() != 0) {// Ϊwdmlink������·��
		// for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
		// if (!WDMLink.WDMLinkList.get(i).getFiberLinkList().isEmpty())//
		// ��Ϊ�գ�ֱ��ӳ��
		// AutoRoute.WDMAutoRoute(WDMLink.WDMLinkList.get(i));
		// }
		// }
		LinkedList<WDMLink> returnLinkList = new LinkedList<WDMLink>();// ��·����洢���������route���FiberLinkList
		LinkedList<CommonNode> returnNodeList = new LinkedList<CommonNode>();// ��·��������Node

		if (DAlgorithm.dijkstra(flag, tra.getFromNode(), tra.getToNode(), CommonNode.allNodeList,
				WDMLink.WDMLinkList, returnLinkList, returnNodeList) && returnLinkList.size() <= hopLimit) {// �����·�ɹ�
			Route route = new Route(tra.getFromNode(), tra.getToNode(), returnLinkList, returnNodeList, tra,0);
			for (WDMLink wdmLink : returnLinkList) {
				route.getFiberLinkList().addAll(wdmLink.getFiberLinkList());
			}
			return route;
		} else
			return null;
	}
	public static Route findWDMRouteHaveMustPassNode1(Traffic tra, int flag,CommonNode node) {
		// ��ҵ��tra����wdmlink·�ɷ��䣬flag=1���� ;
		// =0 ����;
		// =2 ���ؾ����㷨

		// //ÿ����·֮ǰ���½��в��·��ӳ��
		// if (FiberLink.fiberLinkList.size() != 0) {// Ϊwdmlink������·��
		// for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
		// if (!WDMLink.WDMLinkList.get(i).getFiberLinkList().isEmpty())//
		// ��Ϊ�գ�ֱ��ӳ��
		// AutoRoute.WDMAutoRoute(WDMLink.WDMLinkList.get(i));
		// }
		// }
		LinkedList<WDMLink> returnLinkList = new LinkedList<WDMLink>();// ��·����洢���������route���FiberLinkList
		LinkedList<CommonNode> returnNodeList = new LinkedList<CommonNode>();// ��·��������Node

		if (DAlgorithm.dijkstra(flag, tra.getFromNode(), node, CommonNode.allNodeList,
				WDMLink.WDMLinkList, returnLinkList, returnNodeList) && returnLinkList.size() <= hopLimit) {// �����·�ɹ�
			Route route = new Route(returnLinkList, returnNodeList);
			for (WDMLink wdmLink : returnLinkList) {
				route.getFiberLinkList().addAll(wdmLink.getFiberLinkList());
			}
			return route;
		} else
			return null;
	}
	public static Route findWDMRouteHaveMustPassNode2(Traffic tra, int flag,CommonNode node) {
		// ��ҵ��tra����wdmlink·�ɷ��䣬flag=1���� ;
		// =0 ����;
		// =2 ���ؾ����㷨

		// //ÿ����·֮ǰ���½��в��·��ӳ��
		// if (FiberLink.fiberLinkList.size() != 0) {// Ϊwdmlink������·��
		// for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
		// if (!WDMLink.WDMLinkList.get(i).getFiberLinkList().isEmpty())//
		// ��Ϊ�գ�ֱ��ӳ��
		// AutoRoute.WDMAutoRoute(WDMLink.WDMLinkList.get(i));
		// }
		// }
		LinkedList<WDMLink> returnLinkList = new LinkedList<WDMLink>();// ��·����洢���������route���FiberLinkList
		LinkedList<CommonNode> returnNodeList = new LinkedList<CommonNode>();// ��·��������Node

		if (DAlgorithm.dijkstra(flag, node, tra.getToNode(), CommonNode.allNodeList,
				WDMLink.WDMLinkList, returnLinkList, returnNodeList) && returnLinkList.size() <= hopLimit) {// �����·�ɹ�
			Route route = new Route(returnLinkList, returnNodeList);
			for (WDMLink wdmLink : returnLinkList) {
				route.getFiberLinkList().addAll(wdmLink.getFiberLinkList());
			}
			return route;
		} else
			return null;
	}
	public static Route findWDMRouteByTwoNode(Traffic tra, int flag,CommonNode node1,CommonNode node2) {
		// ��ҵ��tra����wdmlink·�ɷ��䣬flag=1���� ;
		// =0 ����;
		// =2 ���ؾ����㷨

		// //ÿ����·֮ǰ���½��в��·��ӳ��
		// if (FiberLink.fiberLinkList.size() != 0) {// Ϊwdmlink������·��
		// for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
		// if (!WDMLink.WDMLinkList.get(i).getFiberLinkList().isEmpty())//
		// ��Ϊ�գ�ֱ��ӳ��
		// AutoRoute.WDMAutoRoute(WDMLink.WDMLinkList.get(i));
		// }
		// }
		LinkedList<WDMLink> returnLinkList = new LinkedList<WDMLink>();// ��·����洢���������route���FiberLinkList
		LinkedList<CommonNode> returnNodeList = new LinkedList<CommonNode>();// ��·��������Node

		if (DAlgorithm.dijkstra(flag, node1, node2, CommonNode.allNodeList,
				WDMLink.WDMLinkList, returnLinkList, returnNodeList) && returnLinkList.size() <= hopLimit) {// �����·�ɹ�
			Route route = new Route(returnLinkList, returnNodeList);
			for (WDMLink wdmLink : returnLinkList) {
				route.getFiberLinkList().addAll(wdmLink.getFiberLinkList());
			}
			return route;
		} else
			return null;
	}
	

	public static boolean isRouteEmpty(List<Traffic> tral) {// �ж�ҵ���б��Ƿ���ڹ���·��
		for (Traffic tra : tral) {
			if (!(tra.getWorkRoute() == null))
				return false;
		}
		return true;
	}

	//�������ר��
	public static void clearAllTrafficRoute2(List<Traffic> tral) {// ���ҵ�����ҵ������ռ�õ���Դ
		for (Traffic tra : tral) {
			if ((tra.getWorkRoute() == null))// 12.9
				continue;// �ж�������Ҫ�޸�CC 12.9 ���޸�
			else if (tra.getWorkRoute().getWDMLinkList() != null) {// 12.9
				List<WDMLink> wdmList = tra.getWorkRoute().getWDMLinkList();
				if (tra.getProtectRoute() != null && tra.getProtectRoute().getWDMLinkList().size() != 0)
					wdmList.addAll(tra.getProtectRoute().getWDMLinkList());
				for (WDMLink wl : wdmList) {
					wl.getCarriedTrafficList().clear();
					wl.setRemainResource(wl.getWaveNum());
					for (WaveLength ts : wl.getWaveLengthList()) {
						ts.setCarriedTraffic(null);
						ts.setStatus(Status.����);
					}
				}
			}
			// ��·������Ϊ��
			tra.setWorkRoute(null);
			tra.setProtectRoute(null);
			tra.setResumeRoute(null);//2017.10.18
			tra.setResumeRoutePro(null);//2017.10.18
		} // ѭ�����
			//// wb 2017.10.13
		for (WDMLink wlink : WDMLink.WDMLinkList) {// �ָ�״̬
			for (WaveLength wave : wlink.getWaveLengthList()) {
//				wave.setUsed(false);
				wave.setPre(false);
			}
		}
//		for (CommonNode node : CommonNode.allNodeList) {
//			node.setRegenerated(false);//�����нڵ�������������Ϊfalse
//			for (Port port : node.getPortList()) {
////				port.setUsed(false);
//				port.setIspre(false);
//			}
//		}
//		// ���wdmlinksrlg�ͷǵ������ɵ�spanlinksrlg
//		Iterator<LinkRGroup> it = LinkRGroup.SRLGroupList.iterator();
//		while (it.hasNext()) {
//			LinkRGroup group = it.next();
//			if ((group.getBelongLayer().equals(Layer.Span) && group.isNatrue() == false)
//					|| group.getBelongLayer().equals(Layer.WDM)) {
//				it.remove();
//			}
//		}
		
//		LinkRGroup.clicked = false;// �ָ�Ϊδӳ��
//		Suggest.isSurvived = false;// ���ٻָ�Ĭ������
//		Suggest.conversionNodeList.clear();// ���
//		Suggest.conversionNodeList2.clear();// ���
//		Suggest.allosnrNodeList.clear();//2017.10.17
//		Suggest.allosnrNodeList2.clear();//2017.10.17
//		OSNR.OSNRRouteList.clear();//2017.10.25
//		OSNR.allOSNRList.clear();// ���osnrlist
		////
		Traffic.releaseAllPortResource();// �ͷ����ж˿���Դ
		Dlg_PolicySetting.isDesign = false;// CC 2017.5.2
	}
	public static void clearAllTrafficRoute(List<Traffic> tral) {// ���ҵ�����ҵ������ռ�õ���Դ
		for (Traffic tra : tral) {
			if ((tra.getWorkRoute() == null))// 12.9
				continue;// �ж�������Ҫ�޸�CC 12.9 ���޸�
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
						ts.setStatus(Status.����);
					}
				}
			}
			// ��·������Ϊ��
			tra.setWorkRoute(null);
			tra.setProtectRoute(null);
			tra.setPreRoute(null);
			tra.setResumeRoute(null);//2017.10.18
			tra.setResumeRoutePro(null);//2017.10.18
		} // ѭ�����
			//// wb 2017.10.13
		for (WDMLink wlink : WDMLink.WDMLinkList) {// �ָ�״̬
			for (WaveLength wave : wlink.getWaveLengthList()) {
				wave.setUsed(false);
				wave.setPre(false);
			}
		}
//		for (CommonNode node : CommonNode.allNodeList) {
//			node.setRegenerated(false);//�����нڵ�������������Ϊfalse
//			for (Port port : node.getPortList()) {
//				port.setUsed(false);
//				port.setIspre(false);
//			}
//		}
//		// ���wdmlinksrlg�ͷǵ������ɵ�spanlinksrlg
//		Iterator<LinkRGroup> it = LinkRGroup.SRLGroupList.iterator();
//		while (it.hasNext()) {
//			LinkRGroup group = it.next();
//			if ((group.getBelongLayer().equals(Layer.Fiber) && group.isNatrue() == false)
//					|| group.getBelongLayer().equals(Layer.WDM)) {
//				it.remove();
//			}
//		}
		
		LinkRGroup.clicked = false;// �ָ�Ϊδӳ��
		Suggest.isSurvived = false;// ���ٻָ�Ĭ������
		Suggest.conversionNodeList.clear();// ���
		Suggest.conversionNodeList2.clear();// ���
		Suggest.allosnrNodeList.clear();//2017.10.17
		Suggest.allosnrNodeList2.clear();//2017.10.17
//		OSNR.OSNRRouteList.clear();//2017.10.25
//		OSNR.allOSNRList.clear();// ���osnrlist
		////
		//��������·��Ϊ����״̬
		for(int i=0;i<WDMLink.WDMLinkList.size();i++) {
			WDMLink.WDMLinkList.get(i).setActive(true);
		}

		Traffic.releaseAllPortResource();// �ͷ����ж˿���Դ
		Dlg_PolicySetting.isDesign = false;// CC 2017.5.2
	}

	public static void releaseRoute(Route route) {// �ͷ�·�ɳ���ҵ��ռ����Դ��
		if (route == null)
			return;
		Traffic tra = route.getBelongTraffic();
		List<WDMLink> wdmLinkList = route.getWDMLinkList();
		for (WDMLink wdmLink : wdmLinkList) {
			if (wdmLink.getCarriedTrafficList().remove(tra)) {// ����Ҫ�ж��Ƿ��������Ϊ��᷵��BOOLEAN
				wdmLink.setRemainResource(wdmLink.getRemainResource() + 1);
				for (WaveLength wl : wdmLink.getWaveLengthList()) {
					if (wl.getCarriedTraffic() == null)
						continue;
					else if (wl.getCarriedTraffic().equals(tra)) {
						wl.setCarriedTraffic(null);
						wl.setStatus(Status.����);
						wl.setPre(false);// 2027.10.13
					}
				}
			}
		}
	}

}
