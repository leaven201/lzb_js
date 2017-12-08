package survivance;

/**
 * @author CC
 * ���ܣ�������/����������/����׼������
 * ��ע��ע�ⷺ�͵�ʹ��
 * @date 9.16
 */
import java.util.LinkedList;
import java.util.List;

import algorithm.CommonAlloc;
import algorithm.PortAlloc;
import algorithm.RouteAlloc;
import data.CommonNode;
import data.FiberLink;
import data.LinkRGroup;
import data.Route;
import data.FiberLink;
import data.Traffic;
import data.WDMLink;

import java.util.Iterator;

public class Survivance {
    public static int mark;//���ڱ�־ѡ������·���㷨
	private List<Traffic> affectedTrafficList = new LinkedList<Traffic>();

	/**
	 * �������ƣ�setFault �������ã��趨������·status��false ��isSurviceΪTRUE ���������fiber��· ���ز�����TRUE
	 */
	public static void setFault(List<FiberLink> FiberLinkList1) { // ���ù���״̬
		for (int i = 0; i < FiberLinkList1.size(); ++i) {
			for(WDMLink wl:FiberLinkList1.get(i).getCarriedWDMLinkList()) {
			wl.setActive(false);
			}
			// surLinkList1.get(i).setSurvived(true);
		}
	}

	/**
	 * �������ƣ�setback �������ã��ָ�������·״̬ΪTRUE bsurviceΪfalse ���������fiber��· ���ز�����TRUE
	 */
	public static void setback(List<FiberLink> FiberLinkList1) { // �ָ���������
//		List<FiberLink> FiberLinkList=new LinkedList<FiberLink>();
//		if(isSRLG==true)
//		{
//			for(int i=0;i<FiberLinkList1.size();++i){ //SRLG �ѹ���ҵ�����е�ҵ�����·������FiberLinkList1
//				for(int j=0;j<FiberLinkList1.get(i).getFiberRelatedList().size();++j){
//					for(int k=0;k<FiberLinkList1.get(i).getFiberRelatedList().get(j).getSRLGFiberLinkList().size();++k){
//						FiberLinkList.add(FiberLinkList1.get(i).getFiberRelatedList().get(j).getSRLGFiberLinkList().get(k));
//					}//�����������ظ��������
//				}
//			}
//		}
		for (int i = 0; i < FiberLinkList1.size(); ++i) {
			for(WDMLink wl:FiberLinkList1.get(i).getCarriedWDMLinkList()) {
			wl.setActive(true);
			}
			// surLinkList1.get(i).setSurvived(false);
		}
	}

	/**
	 * �������ƣ�reCompute �������ã�Ϊ����ҵ��������·���ָ�ҵ�� ���������traffic��· ���ز�����
	 */
	public static void reCompute(List<Traffic> trafficList) {
		// assert FiberLink.allFiberLinkList.size() == 35 : "��·�ı���";// 10.27

		for (int i = 0; i < trafficList.size(); ++i) {
			Traffic tra = trafficList.get(i);
			int type = mark;// ��С�����㷨
			switch (tra.getFaultType()) {// ҵ���������1 ���� 2 ���� 3��������
			case 1: // 1Ϊ������Ӱ��
				switch (tra.getProtectLevel()) {// ҵ�񱣻��ȼ�
				case PERMANENT11:
					if (tra.getProtectRoute() != null && tra.getProtectRoute().getFiberLinkList() != null
							&& tra.getProtectRoute().getFiberLinkList().size() != 0) {
						tra.setResumeRoute(tra.getProtectRoute());
						setLinkStatus(tra, false);

						algorithm.ResourceAlloc.allocateRscSingle(tra, type, 1,0);// ����ָ�·�ɵı���·��
						setLinkStatus(tra, true);

					} else {
						algorithm.ResourceAlloc.allocateRscSingle(tra, type, 0,0);
					}
					break;
				case NORMAL11:
					if (tra.getProtectRoute() != null && tra.getProtectRoute().getFiberLinkList() != null
							&& tra.getProtectRoute().getFiberLinkList().size() != 0) {
						tra.setResumeRoute(tra.getProtectRoute());
					}

					break;
				case RESTORATION:
					if(tra.getPreRoute()!=null&&tra.getPreRoute().getFiberLinkList()!=null //�ж�ר��Ԥ���Ƿ����
					&& tra.getPreRoute().getFiberLinkList().size()!=0) {
						tra.setResumeRoute(tra.getPreRoute());
					}else {
					algorithm.ResourceAlloc.allocateRscSingle(tra, type, 0,0);
					}
					break;
				case PROTECTandRESTORATION:
					if (tra.getProtectRoute() != null && tra.getProtectRoute().getFiberLinkList() != null//����ʹ��1+1�ı���·����Ϊ�ָ�·��
							&& tra.getProtectRoute().getFiberLinkList().size() != 0) {
						tra.setResumeRoute(tra.getProtectRoute());
					}else {
						if(tra.getPreRoute()!=null&&tra.getPreRoute().getFiberLinkList()!=null //�ж�ר��Ԥ���Ƿ����
						&& tra.getPreRoute().getFiberLinkList().size()!=0) {
						   tra.setResumeRoute(tra.getPreRoute());
						}else {
						algorithm.ResourceAlloc.allocateRscSingle(tra, type, 0,0);
						}
					}
					break;

//				case PresetRESTORATION:// ��1+1����
//					if (tra.getProtectRoute() != null && tra.getProtectRoute().getFiberLinkList() != null
//							&& tra.getProtectRoute().getFiberLinkList().size() != 0) {
//						tra.setResumeRoute(tra.getProtectRoute());
//					}
//					break;
				case NONPROTECT:
					break;
				default:
					break;
				}
				break;
			case 2: // 2Ϊ������Ӱ��
				switch (tra.getProtectLevel()) {
				case PERMANENT11:
					tra.setResumeRoute(tra.getWorkRoute());
					setLinkStatus(tra, false);
					algorithm.ResourceAlloc.allocateRscSingle(tra, type, 1,0);
					setLinkStatus(tra, true);
					break;
				case NORMAL11:
					tra.setResumeRoute(tra.getWorkRoute());
					break;
				case RESTORATION:
					tra.setResumeRoute(tra.getWorkRoute());
					break;
				case PresetRESTORATION:
					tra.setResumeRoute(tra.getWorkRoute());
					break;
				case PROTECTandRESTORATION:
					tra.setResumeRoute(tra.getWorkRoute());
					break;
				case NONPROTECT:
					tra.setResumeRoute(tra.getWorkRoute());
					break;
				default:
					break;
				}
				break;
			case 3: // 3Ϊ������������Ӱ��
				switch (tra.getProtectLevel()) {
				case PERMANENT11:
					algorithm.ResourceAlloc.allocateRscSingle(tra, type, 0,0);
					setLinkStatus(tra, false);
					algorithm.ResourceAlloc.allocateRscSingle(tra, type, 1,0);
					setLinkStatus(tra, true);
					break;
				case NORMAL11:
					break;
				case RESTORATION:
					if(tra.getPreRoute()!=null&&tra.getPreRoute().getFiberLinkList()!=null
					&&tra.getPreRoute().getFiberLinkList().size()!=0) {
						tra.setResumeRoute(tra.getPreRoute());
					}else {
						break;
					}
					break;
				case PROTECTandRESTORATION:
					if(tra.getPreRoute()!=null&&tra.getPreRoute().getFiberLinkList()!=null
						&&tra.getPreRoute().getFiberLinkList().size()!=0){
							tra.setResumeRoute(tra.getPreRoute());
						}else {
							break;
						}
					break;
				case PresetRESTORATION:
					break;
				case NONPROTECT:
					break;
				default:
					break;
				}
				break;
			}// end switch
		} // end for
			// assert FiberLink.allFiberLinkList.size() == 35 : "��·�ı���";// 10.27

	}

	/**
	 * ���ܣ�����ҵ��Ļָ�·��״̬Ϊb
	 * 
	 * @param tra
	 * @param b
	 */
	private static void setLinkStatus(Traffic tra, boolean b) {
		Route resumeRoute = tra.getResumeRoute();
		if (resumeRoute == null)
			return;// �Ҳ���·ʱ�� 2016.11.1
		for (WDMLink bl : resumeRoute.getWDMLinkList()) {
			bl.setActive(b);
		}
	}

	/**
	 * ���ܣ�ͨ����Ӱ���linkList�õ���Ӱ���ҵ�񣬲�����ҵ��Ĺ�������
	 * 
	 * @param linkList
	 * 
	 */
	public List<Traffic> getAffectedTraffic(List<FiberLink> affectedLinkList) {

		// assert FiberLink.allFiberLinkList.size() == 35 : "��·�ı���";// 10.27

		for (int i = 0; i < affectedLinkList.size(); ++i) // ������Ӱ�����·
		{
			FiberLink bLink = affectedLinkList.get(i);
			for (WDMLink wLink : bLink.getCarriedWDMLinkList()) {
				if (wLink.getCarriedTrafficList() == null)
					return null; // CC 2016.10.20 Ϊû�г���ҵ�����·��������ֹ�쳣

				List<Traffic> temptraList = wLink.getCarriedTrafficList(); // ��Ӱ�����·�ϵ�ҵ����
				if (temptraList != null) {
					for (int j = 0; j < temptraList.size(); ++j) {// ������ǰ��·�ϵ�ҵ��
						Traffic tra = temptraList.get(j);// ��ҵ���·��������һ���ܵ�Ӱ��
						if (!affectedTrafficList.contains(tra)) {// �ж��Ƿ���ֹ��ظ�����Ӱ��ҵ��
							if (tra.getWorkRoute().getWDMLinkList().contains(wLink)) {// �жϹ���·���Ƿ������ǰ��Ӱ����·
								tra.setFaultType(1); // ������Ӱ��

								for (FiberLink curbl : affectedLinkList) {
									if (tra.getProtectRoute() == null)
										break;
									if (tra.getProtectRoute().getFiberLinkList().contains(curbl)) {
										tra.setFaultType(3); // �����ͱ�������Ӱ��
										break;
									}
								}

							} // ��������·�ɽ���
							else if (tra.getProtectRoute() != null) {
								if (tra.getProtectRoute().getWDMLinkList().contains(wLink)) {
									tra.setFaultType(2); // ������Ӱ�졢

									for (FiberLink curbl : affectedLinkList) {
										if (tra.getWorkRoute().getFiberLinkList().contains(curbl)) {
											tra.setFaultType(3); // �����ͱ�������Ӱ��
											break;
										}
									}
								}
							} // ��������·�ɽ���
							affectedTrafficList.add(tra);
						}
					}
				}
			}
		}
		return affectedTrafficList;
	}

	/**
	 * �������ƣ�getNodeEffectLink �������ã�ͨ���ڵ���Ѱ�����ڵ����· ���������node�ڵ� ���ز�����True
	 */

	public static List<FiberLink> getNodeEffectLink(CommonNode node) {
		List<FiberLink> basicLinkList = new LinkedList<FiberLink>();
		Iterator<FiberLink> it = FiberLink.getFiberLinkList().iterator();
		while (it.hasNext()) {
			FiberLink fl = it.next();
			if (fl.getFromNode().equals(node) || fl.getToNode().equals(node))
				basicLinkList.add(fl);
		}
		return basicLinkList;
	}
}
