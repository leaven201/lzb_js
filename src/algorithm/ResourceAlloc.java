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
 * ���ܣ����๩�滮�򵼵��ã��ǽ�����Դ���������ڡ� ��Դ�����Ϊ��·��Դ���䣨CommonAlloc���Ͷ˿���Դ���䣨PortAlloc�� ��
 * ��������Դ������ ��
 * 
 * @author CC
 * @since 2016/7/4
 */
public class ResourceAlloc {
	public static List<Traffic> trafficList = new LinkedList<Traffic>(); // �����ҵ���б�ľ���
	public static List<Traffic> failedTraffic = new LinkedList<Traffic>();// �洢ʧ�ܵ�ҵ��

	public static void allocateResource(List<Traffic> traList, int flag) {// flag=0:��̳��ȣ�=1����С��
		Survivance.mark = flag;// ���ڿ��ٷ����б�Ƿ����ʱ��ʹ�õ�����·���㷨
		CommonAlloc.fallBuffer = new StringBuffer();// ��·��Դ������־
		PortAlloc.portFallBuffer = new StringBuffer();// �˿���Դ������־

		trafficList = new LinkedList<Traffic>();

		trafficList.addAll(traList);// ������һ��ҵ���б���trafficList�Ĳ�������Ӱ��traList
		Route route = null;
		OSNR osnr = new OSNR();
		if (!RouteAlloc.isLimitHop)
			RouteAlloc.hopLimit = Integer.MAX_VALUE; // ������������(�����������������Ͱ���������Ϊ�����)
		else if (RouteAlloc.hopLimit == Integer.MAX_VALUE)
			RouteAlloc.hopLimit = 5; // Ĭ����������Ϊ5
		Iterator<Traffic> it = traList.iterator();
		while (it.hasNext()) {// ѭ������������ÿ��ҵ��Ĺ���·��/����·�ɣ�����У�

			// ����·��û�п��ò�����Դ����·��Ϊ������
			for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
				if ((WDMLink.WDMLinkList.get(i).getRemainResource() == 0))
					WDMLink.WDMLinkList.get(i).setActive(false);
			}
			Traffic traffic = it.next();
			// ���ر���·��Ϊ������
			if (traffic.getMustAvoidLink() != null) {
				WDMLink.getLink(traffic.getMustAvoidLink().getName()).setActive(false);
			}
			// ����ܱܽڵ���������·��Ϊ������
			if (traffic.getMustAvoidNode() != null) {
				CommonNode mANode = traffic.getMustAvoidNode();// �رܽڵ�
				for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
					if (WDMLink.WDMLinkList.get(i).getFromNode().getName().equals(mANode.getName())
							|| WDMLink.WDMLinkList.get(i).getToNode().getName().equals(mANode.getName()))
						WDMLink.WDMLinkList.get(i).setActive(false);
				}
			}

			if (traffic.getTrafficgroup() != null)// ���ҵ��Ĺ���ҵ���鲻Ϊ��
			{
				for (int i = 0; i < Traffic.trafficList.size(); i++) {
					Traffic tra = Traffic.trafficList.get(i);
					// �ж��Ƿ���һ�����չ���ҵ���鲢�Ҳ���ͬһ��ҵ��
					if (tra.getTrafficgroup() != null
							&& tra.getTrafficgroup().getId() == traffic.getTrafficgroup().getId()
							&& tra.getRankId() != traffic.getRankId()) {
						if (tra.getWorkRoute() != null) {// ����������ҵ����·�ɴ���
							for (int j = 0; j < tra.getWorkRoute().getWDMLinkList().size(); j++)
								tra.getWorkRoute().getWDMLinkList().get(j).setActive(false);
						}
					}
				}
			}

			// if(traffic.getM_cGroup()!=null) {
			// int groupId=traffic.getM_cGroup().getGroupId();
			// for(int i=0;i<TrafficGroup.grouptrafficGroupList.size();i++)//ѭ������ҵ����
			// if(TrafficGroup.grouptrafficGroupList.get(i).getRelatedId()==groupId)//�ҳ���֮������ҵ����
			// {
			// for(int
			// j=0;j<TrafficGroup.grouptrafficGroupList.get(i).getTrafficList().size();j++)//ѭ����ҵ�����ҵ��
			// { //�������·�ɴ���
			// if(TrafficGroup.grouptrafficGroupList.get(i).getTrafficList().get(j).getWorkRoute()!=null)//�����ҵ���й���·��
			// { //�򽫹���·�ɵ�WDMLink��Ϊfalse
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
				// Ϊ�����ظ���Ҫ��route1��Ӧ��·��Ϊ������
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
				// Ϊ�����ظ���Ҫ��route1��Ӧ��·��Ϊ������
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
				// ������·�ĵ�toNode��fromNode������ȥ���п������route1��route2�Ƿ����Ҫ�ж�һ��
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
				// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// �������osnrҪ��
				if (1 > 0) {// Ϊ�˱ܿ�OSNR���� ��3��ʱҪ�ĵ�
					if (CommonAlloc.allocateWaveLength(route) && PortAlloc.allocatePort(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
						System.out.println("����ɹ�");
						CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·�ɼ���Դ����ɹ���\r\n");
						traffic.setWorkRoute(route);// ���·�ɲ�Ϊ��������Ϊ����·��
						traffic.setStatus(TrafficStatus.�����ѷ���);
					} else {// ��Դ����ʧ��
						failedTraffic.add(traffic);
						System.out.println("����ʧ��");
						traffic.setStatus(TrafficStatus.����);// �����û�о����Ҳ���
						CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·�ɲ�����˿���Դ����ʧ�ܣ�\r\n");
						RouteAlloc.releaseRoute(route);
					}
				}
				// else {// ���osnr��������
				// if (Suggest.isSuggested == true) {// 2017.10.14
				// OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
				// OSNR.OSNRRouteList.add(route);//10.19
				// }
				// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·��OSNR������������\r\n");
				// }
			} else {// wdmlink��·Ϊ��

				traffic.setStatus(TrafficStatus.�滮ҵ������);// �����û�о����Ҳ���
				CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "���ڲ�����Դ���㹤��·����·ʧ�ܣ�\r\n");
			}

			//////// ��ʼ���㱣��·��
			if (traffic.getWorkRoute() != null && (traffic.getProtectLevel() == TrafficLevel.NORMAL11
					|| traffic.getProtectLevel() == TrafficLevel.PERMANENT11
					|| traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION)) {
				List<WDMLink> workLinkList = traffic.getWorkRoute().getWDMLinkList();
				for (WDMLink wdmLink : workLinkList) {
					wdmLink.setActive(false);
					for (FiberLink link : wdmLink.getFiberLinkList()) {// ���ù���·�ɲ����������㱣��·��
						link.setActive(false);
					}
					// ����srlg,�����������һ����־λ��ѡ���Ƿ���srlg
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
					// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// �������osnrҪ��
					// Ϊ�˱ܿ�OSNR 3��Ҫ�Ļ�ȥ
					if (1 > 0) {
						if (CommonAlloc.allocateWaveLength1(route) && PortAlloc.allocatePort(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
							CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·�ɼ���Դ����ɹ���\r\n");
							traffic.setProtectRoute(route);// ���·�ɲ�Ϊ��������Ϊ����·��
							traffic.setStatus(TrafficStatus.�����ͱ����ѷ���);
						} else {// ��Դ����ʧ��
							CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·�ɲ�����˿���Դ����ʧ�ܣ�\r\n");
							RouteAlloc.releaseRoute(route);
						}
					}
					// else {// ���osnr��������
					// if (Suggest.isSuggested == true) {// 2017.10.14
					// OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
					// OSNR.OSNRRouteList.add(route);//10.19
					// }
					// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·��OSNR������������\r\n");
					// }
				} else {
					CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "���ڲ�����Դ���㱣��·����·ʧ�ܣ�\r\n");
				}

				for (WDMLink wdmLink : workLinkList) {// ����·�ɼ�������Ժ󼤻��·����·
					wdmLink.setActive(true);
					for (FiberLink fiberLink : wdmLink.getFiberLinkList()) {
						fiberLink.setActive(true);
					}
					// ����srlg
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
			} // ����·�ɼ������

			//////// ��ʼ����ר��Ԥ�ûָ�·��
			if (traffic.getWorkRoute() != null && (traffic.getProtectLevel() == TrafficLevel.RESTORATION)
					|| traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION) {// �������·�ɴ��ڣ����ұ����ȼ�������·��
				List<WDMLink> workLinkList = traffic.getWorkRoute().getWDMLinkList();
				for (WDMLink wdmLink : workLinkList) {
					wdmLink.setActive(false);
					for (FiberLink link : wdmLink.getFiberLinkList()) {// ���ù���·�ɲ�����������Ԥ��·��
						link.setActive(false);
					}
					// ����srlg,�����������һ����־λ��ѡ���Ƿ���srlg
					if (LinkRGroup.srlg == true) {
						for (LinkRGroup group : wdmLink.getWdmRelatedList()) {
							for (WDMLink wLink : group.getSRLGWDMLinkList()) {
								wLink.setActive(false);
							}
						}
					}
				}
				// �������·�ɴ��ڣ��򽫱���·����Ϊ������
				if (traffic.getProtectRoute() != null) {
					List<WDMLink> protectLinkList = traffic.getProtectRoute().getWDMLinkList();
					for (WDMLink wdmLink : protectLinkList) {
						wdmLink.setActive(false);
						for (FiberLink link : wdmLink.getFiberLinkList()) {// ���ñ���·�ɲ�����������Ԥ��·��
							link.setActive(false);
						}
						// ����srlg,�����������һ����־λ��ѡ���Ƿ���srlg
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
					// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// �������osnrҪ��
					// �ܿ�OSNR
					if (1 > 0) {
						if (CommonAlloc.allocateWaveLength2(route) && PortAlloc.allocatePort(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
							CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "Ԥ��·�ɼ���Դ����ɹ���\r\n");
							traffic.setPreRoute(route);// ���·�ɲ�Ϊ��������Ԥ����·��
							traffic.setStatus(TrafficStatus.�����ͱ�������·���ѷ���);
						} else {// ��Դ����ʧ��

							CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "Ԥ��·�ɲ�����˿���Դ����ʧ�ܣ�\r\n");
							RouteAlloc.releaseRoute(route);
						}
					}
					// else {// ���osnr��������
					// if (Suggest.isSuggested == true) {// 2017.10.14
					// OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
					// OSNR.OSNRRouteList.add(route);//10.19
					// }
					// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·��OSNR������������\r\n");
					// }
				} else {
					CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "���ڲ�����Դ����Ԥ��·����·ʧ�ܣ�\r\n");
				}

				for (WDMLink wdmLink : workLinkList) {// ����·�ɼ�������Ժ󼤻��·����·
					wdmLink.setActive(true);
					for (FiberLink fiberLink : wdmLink.getFiberLinkList()) {
						fiberLink.setActive(true);
					}
					// ����srlg
					if (LinkRGroup.srlg == true) {
						for (LinkRGroup group : wdmLink.getWdmRelatedList()) {
							for (WDMLink wLink : group.getSRLGWDMLinkList()) {
								wLink.setActive(true);
							}
						}
					}
				}
			} // ר��Ԥ�ûָ�·��

			// ���¼���ر�վ�����·
			// ���ر���·��Ϊ����
			// if(traffic.getMustAvoidLink()!=null)
			// {WDMLink.getLink(traffic.getMustAvoidLink().getName()).setActive(true);}
			// //����ܱܽڵ���������·��Ϊ����
			// if(traffic.getMustAvoidNode()!=null)
			// {CommonNode mANode=traffic.getMustAvoidNode();//�رܽڵ�
			// for(int i=0;i<WDMLink.WDMLinkList.size();i++) {
			// if(WDMLink.WDMLinkList.get(i).getFromNode().getName().equals(mANode.getName())
			// ||WDMLink.WDMLinkList.get(i).getToNode().getName().equals(mANode.getName()))
			// WDMLink.WDMLinkList.get(i).setActive(true);
			// }}
			//
			//
			// //���¼������ҵ�����ҵ��
			// if(traffic.getTrafficgroup()!=null)//���ҵ��Ĺ���ҵ���鲻Ϊ��
			// {
			// for(int i=0;i<Traffic.trafficList.size();i++) {
			// Traffic tra=Traffic.trafficList.get(i);
			// //�ж��Ƿ���һ�����չ���ҵ���鲢�Ҳ���ͬһ��ҵ��
			// if(tra.getTrafficgroup()!=null&&tra.getTrafficgroup().getId()==traffic.getTrafficgroup().getId()
			// &&tra.getRankId()!=traffic.getRankId())
			// {
			// if(tra.getWorkRoute()!=null) {//����������ҵ����·�ɴ���
			// for(int j=0;j<tra.getWorkRoute().getWDMLinkList().size();j++)
			// tra.getWorkRoute().getWDMLinkList().get(j).setActive(true);
			// }
			// }
			// }
			// }
			// ��������·��Ϊ����
			for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
				WDMLink.WDMLinkList.get(i).setActive(true);
			}

		} // ҵ��ѭ������
		ResourceLogTxt.ResourceLogTxt();// �����Դ������־.
		CommonAlloc.fallBuffer.delete(0, CommonAlloc.fallBuffer.length());
		// System.out.println(CommonNode.allNodeList);//12.14
		trafficList.clear();
		Dlg_PolicySetting.isDesign = true; // CC2017.5.2

	}

	public static void allocateResource1(List<Traffic> traList, int flag) {// flag=0:��̳��ȣ�=1����С��
		CommonAlloc.fallBuffer = new StringBuffer();// ��·��Դ������־
		PortAlloc.portFallBuffer = new StringBuffer();// �˿���Դ������־

		trafficList = new LinkedList<Traffic>();

		trafficList.addAll(traList);// ������һ��ҵ���б���trafficList�Ĳ�������Ӱ��traList
		Route route = null;
		OSNR osnr = new OSNR();
		if (!RouteAlloc.isLimitHop)
			RouteAlloc.hopLimit = Integer.MAX_VALUE; // ������������(�����������������Ͱ���������Ϊ�����)
		else if (RouteAlloc.hopLimit == Integer.MAX_VALUE)
			RouteAlloc.hopLimit = 5; // Ĭ����������Ϊ5
		Iterator<Traffic> it = traList.iterator();
		while (it.hasNext()) {// ѭ������������ÿ��ҵ��Ĺ���·��/����·�ɣ�����У�

			// ����·��û�п��ò�����Դ����·��Ϊ������
			for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
				if (WDMLink.WDMLinkList.get(i).getRemainResource() == 0)
					WDMLink.WDMLinkList.get(i).setActive(false);
			}
			Traffic traffic = it.next();
			// ���ر���·��Ϊ������
			if (traffic.getMustAvoidLink() != null) {
				traffic.getMustAvoidLink().setActive(false);
			}
			// ����ܱܽڵ���������·��Ϊ������
			if (traffic.getMustAvoidNode() != null) {
				CommonNode mANode = traffic.getMustAvoidNode();// �رܽڵ�
				for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
					if (WDMLink.WDMLinkList.get(i).getFromNode().getName().equals(mANode.getName())
							|| WDMLink.WDMLinkList.get(i).getToNode().getName().equals(mANode.getName()))
						WDMLink.WDMLinkList.get(i).setActive(false);
				}
			}

			// ����ҵ����
			if (traffic.getTrafficgroup() != null)// ���ҵ��Ĺ���ҵ���鲻Ϊ��
			{
				for (int i = 0; i < Traffic.trafficList.size(); i++) {
					Traffic tra = Traffic.trafficList.get(i);
					// �ж��Ƿ���һ�����չ���ҵ���鲢�Ҳ���ͬһ��ҵ��
					if (tra.getTrafficgroup() != null
							&& tra.getTrafficgroup().getId() == traffic.getTrafficgroup().getId()
							&& tra.getRankId() != traffic.getRankId()) {
						if (tra.getWorkRoute() != null) {// ����������ҵ����·�ɴ���
							for (int j = 0; j < tra.getWorkRoute().getWDMLinkList().size(); j++)
								tra.getWorkRoute().getWDMLinkList().get(j).setActive(false);
						}
					}
				}
			}

			// ����ҵ����-����й���ҵ���飬�����֮�������ҵ��Ĺ���·����·��Ϊfalse
			// if(traffic.getM_cGroup()!=null) {
			// int groupId=traffic.getM_cGroup().getGroupId();
			// for(int i=0;i<TrafficGroup.grouptrafficGroupList.size();i++)
			// if(TrafficGroup.grouptrafficGroupList.get(i).getRelatedId()==groupId)//�ҳ���֮������ҵ����
			// {
			// for(int
			// j=0;j<TrafficGroup.grouptrafficGroupList.get(i).getTrafficList().size();j++)
			// { //�������·�ɴ���
			// if(TrafficGroup.grouptrafficGroupList.get(i).getTrafficList().get(j).getWorkRoute()!=null)
			// { //������·�ɵ�WDMLink��Ϊfalse
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
				// Ϊ�����ظ���Ҫ��route1��Ӧ��·��Ϊ������
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
				// Ϊ�����ظ���Ҫ��route1��Ӧ��·��Ϊ������
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
				// ������·�ĵ�toNode��fromNode������ȥ���п������route1��route2�Ƿ����Ҫ�ж�һ��
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
				// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// �������osnrҪ��
				if (1 > 0) {// Ϊ�˱ܿ�OSNR���� ��3��ʱҪ�ĵ�
					if (CommonAlloc.allocateWaveLength(route) && PortAlloc.allocatePort(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
						System.out.println("����ɹ�");
						CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·�ɼ���Դ����ɹ���\r\n");
						traffic.setWorkRoute(route);// ���·�ɲ�Ϊ��������Ϊ����·��
						traffic.setStatus(TrafficStatus.�����ѷ���);
					} else {// ��Դ����ʧ��
						failedTraffic.add(traffic);
						System.out.println("����ʧ��");
						traffic.setStatus(TrafficStatus.����);// �����û�о����Ҳ���
						CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·�ɲ�����˿���Դ����ʧ�ܣ�\r\n");
						RouteAlloc.releaseRoute(route);
					}
				}
				// else {// ���osnr��������
				// if (Suggest.isSuggested == true) {// 2017.10.14
				// OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
				// OSNR.OSNRRouteList.add(route);//10.19
				// }
				// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·��OSNR������������\r\n");
				// }
			} else {// wdmlink��·Ϊ��

				traffic.setStatus(TrafficStatus.�滮ҵ������);// �����û�о����Ҳ���
				CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "���ڲ�����Դ���㹤��·����·ʧ�ܣ�\r\n");
			}

			//////// ��ʼ���㱣��·��
			if (traffic.getWorkRoute() != null && (traffic.getProtectLevel() == TrafficLevel.NORMAL11
					|| traffic.getProtectLevel() == TrafficLevel.PERMANENT11
					|| traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION)) {
				List<WDMLink> workLinkList = traffic.getWorkRoute().getWDMLinkList();
				for (WDMLink wdmLink : workLinkList) {
					wdmLink.setActive(false);
					for (FiberLink link : wdmLink.getFiberLinkList()) {// ���ù���·�ɲ����������㱣��·��
						link.setActive(false);
					}
					// ����srlg,�����������һ����־λ��ѡ���Ƿ���srlg
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
					// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// �������osnrҪ��
					// Ϊ�˱ܿ�OSNR 3��Ҫ�Ļ�ȥ
					if (1 > 0) {
						if (CommonAlloc.allocateWaveLength1(route) && PortAlloc.allocatePort(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
							CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·�ɼ���Դ����ɹ���\r\n");
							traffic.setProtectRoute(route);// ���·�ɲ�Ϊ��������Ϊ����·��
							traffic.setStatus(TrafficStatus.�����ͱ����ѷ���);
						} else {// ��Դ����ʧ��
							CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·�ɲ�����˿���Դ����ʧ�ܣ�\r\n");
							RouteAlloc.releaseRoute(route);
						}
					}
					// else {// ���osnr��������
					// if (Suggest.isSuggested == true) {// 2017.10.14
					// OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
					// OSNR.OSNRRouteList.add(route);//10.19
					// }
					// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·��OSNR������������\r\n");
					// }
				} else {
					CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "���ڲ�����Դ���㱣��·����·ʧ�ܣ�\r\n");
				}

				for (WDMLink wdmLink : workLinkList) {// ����·�ɼ�������Ժ󼤻��·����·
					wdmLink.setActive(true);
					for (FiberLink fiberLink : wdmLink.getFiberLinkList()) {
						fiberLink.setActive(true);
					}
					// ����srlg
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
			} // ����·�ɼ������

			// //���¼���ر�վ�����·
			// //���ر���·��Ϊ����
			// if(traffic.getMustAvoidLink()!=null)
			// {WDMLink.getLink(traffic.getMustAvoidLink().getName()).setActive(true);}
			// //����ܱܽڵ���������·��Ϊ����
			// if(traffic.getMustAvoidNode()!=null)
			// {CommonNode mANode=traffic.getMustAvoidNode();//�رܽڵ�
			// for(int i=0;i<WDMLink.WDMLinkList.size();i++) {
			// if(WDMLink.WDMLinkList.get(i).getFromNode().getName().equals(mANode.getName())
			// ||WDMLink.WDMLinkList.get(i).getToNode().getName().equals(mANode.getName()))
			// WDMLink.WDMLinkList.get(i).setActive(true);
			// }}
			//
			//
			// //���¼������ҵ�����ҵ��
			// if(traffic.getTrafficgroup()!=null)//���ҵ��Ĺ���ҵ���鲻Ϊ��
			// {
			// for(int i=0;i<Traffic.trafficList.size();i++) {
			// Traffic tra=Traffic.trafficList.get(i);
			// //�ж��Ƿ���һ�����չ���ҵ���鲢�Ҳ���ͬһ��ҵ��
			// if(tra.getTrafficgroup()!=null&&tra.getTrafficgroup().getId()==traffic.getTrafficgroup().getId()
			// &&tra.getRankId()!=traffic.getRankId())
			// {
			// if(tra.getWorkRoute()!=null) {//����������ҵ����·�ɴ���
			// for(int j=0;j<tra.getWorkRoute().getWDMLinkList().size();j++)
			// tra.getWorkRoute().getWDMLinkList().get(j).setActive(true);
			// }
			// }
			// }
			// }

			// //////// ��ʼ����ר��Ԥ�ûָ�·��
			// if (traffic.getWorkRoute() != null && (traffic.getProtectLevel() ==
			// TrafficLevel.RESTORATION)
			// ||traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION) {
			// List<WDMLink> workLinkList = traffic.getWorkRoute().getWDMLinkList();
			// for (WDMLink wdmLink : workLinkList) {
			// wdmLink.setActive(false);
			// for (FiberLink link : wdmLink.getFiberLinkList()) {// ���ù���·�ɲ����������㱣��·��
			// link.setActive(false);
			// }
			// // ����srlg,�����������һ����־λ��ѡ���Ƿ���srlg
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
			// �������osnrҪ��
			// //�ܿ�OSNR
			// if(1>0) {
			// if (CommonAlloc.allocateWaveLength2(route) && PortAlloc.allocatePort(route))
			// {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
			// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·�ɼ���Դ����ɹ���\r\n");
			// traffic.setProtectRoute(route);// ���·�ɲ�Ϊ��������Ϊ����·��
			// traffic.setStatus(TrafficStatus.�����ͱ����ѷ���);
			// } else {// ��Դ����ʧ��
			//
			// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·�ɲ�����˿���Դ����ʧ�ܣ�\r\n");
			// RouteAlloc.releaseRoute(route);
			// }
			// }
			//// else {// ���osnr��������
			//// if (Suggest.isSuggested == true) {// 2017.10.14
			//// OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
			//// OSNR.OSNRRouteList.add(route);//10.19
			//// }
			//// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·��OSNR������������\r\n");
			//// }
			// } else {
			// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·����·ʧ�ܣ�\r\n");
			// }
			//
			// for (WDMLink wdmLink : workLinkList) {// ����·�ɼ�������Ժ󼤻��·����·
			// wdmLink.setActive(true);
			// for (FiberLink fiberLink : wdmLink.getFiberLinkList()) {
			// fiberLink.setActive(true);
			// }
			// // ����srlg
			// if (LinkRGroup.srlg == true) {
			// for (LinkRGroup group : wdmLink.getWdmRelatedList()) {
			// for (WDMLink wLink : group.getSRLGWDMLinkList()) {
			// wLink.setActive(true);
			// }
			// }
			// }
			// }
			// } // ר��Ԥ�ûָ�·��

			// ��������·��Ϊ����
			for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
				WDMLink.WDMLinkList.get(i).setActive(true);
			}

		} // ҵ��ѭ������
		ResourceLogTxt.ResourceLogTxt();// �����Դ������־.
		CommonAlloc.fallBuffer.delete(0, CommonAlloc.fallBuffer.length());
		// System.out.println(CommonNode.allNodeList);//12.14
		trafficList.clear();
		Dlg_PolicySetting.isDesign = true; // CC2017.5.2

	}

	/**
	 * ������һ��ҵ�����·�ɵļ��㣬��Ҫ���ڻָ�·�ɼ��ָ�·�ɵı���·������
	 * 
	 * @param tra
	 *            ������ҵ��
	 * @param flag
	 *            flag=0 ����;=1 ���� ; =2 ���ؾ����㷨
	 * @param rop
	 *            �ָ�·�ɻ��ǻָ�·�ɵı���·�� ,0:�ָ���1���ָ�����
	 * @param suggest
	 *            0:���ٷ����� 1:�滮������
	 */
	public static void allocateRscSingle(Traffic tra, int flag, int rop, int suggest) {
		// CommonAlloc.fallBuffer = new StringBuffer();// ��·��Դ������־
		// PortAlloc.portFallBuffer = new StringBuffer();// �˿���Դ������־
		Route route = null;
		OSNR osnr = new OSNR();
		// System.out.println("111");
		route = RouteAlloc.findWDMRoute(tra, flag);
		if (route != null && route.getWDMLinkList().size() != 0) {
			// System.out.println("222");
			// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// �������osnrҪ��
			// // System.out.println("333");
			if (CommonAlloc.allocateWaveLength(route) && PortAlloc.SimAllocatePort(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
				// route.setBelongsTraffic(tra);//1012 CC
				if (rop == 0)
					tra.setResumeRoute(route);// �������·��Ϊ�����µ�·������Ϊ����·��
				if (rop == 1)
					tra.setResumeRoutePro(route);
			} else {// ��Դ����ʧ��
				RouteAlloc.releaseRoute(route);
			}
			// } else {// ���osnr��������
			// if (suggest == 1) {
			// if (Suggest.isSuggested == true) {// 2017.10.14
			// OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
			// OSNR.OSNRRouteList.add(route);//10.19
			// }
			// }
			// }
		} else {// ·�ɷ���ʧ��
			RouteAlloc.releaseRoute(route);
		}

		// ResourceLogTxt.ResourceLogTxt();// �����Դ������־.
		// CommonAlloc.fallBuffer.delete(0, CommonAlloc.fallBuffer.length());
		// trafficList.clear();
	}

	/**
	 * ������һ��ҵ�����·�ɵļ��㣬��Ҫ���� ����·�ɡ�����·�ɡ�Ԥ�ûָ�·��
	 * 
	 * @param tra
	 *            ������ҵ��
	 * @param flag
	 *            flag=0 ����;=1 ���� ; =2 ���ؾ����㷨
	 * @param rop
	 *            �ָ�·�ɻ��ǻָ�·�ɵı���·�� ,0:�ָ���1���ָ�����
	 */
	public static void allocateOneTraffic(Traffic tra, int flag, int rop) {

	}

	/**
	 * ��̬��·�ɻָ�ʱ����
	 */
	// ��·��
	public static Route reRoute(Route oldroute, CommonNode from, CommonNode to, int wave, int flag) {

		Traffic traffic = oldroute.getBelongsTraffic();
		List<WDMLink> workLinkList = traffic.getWorkRoute().getWDMLinkList();

		// ����·��û�п��ò�����Դ����·��Ϊ������
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			if ((WDMLink.WDMLinkList.get(i).getRemainResource() == 0))
				WDMLink.WDMLinkList.get(i).setActive(false);
		}
		// ��ԭ·����Ϊ������
		for (int i = 0; i < oldroute.getWDMLinkList().size(); i++) {
			oldroute.getWDMLinkList().get(i).setActive(false);
		}

		// ���ù���·�ɲ����������㶯̬�ָ�·��
		for (WDMLink wdmLink : workLinkList) {
			wdmLink.setActive(false);
			for (FiberLink link : wdmLink.getFiberLinkList()) {
				link.setActive(false);
			}
			// ����srlg,�����������һ����־λ��ѡ���Ƿ���srlg
			if (LinkRGroup.srlg == true) {
				for (LinkRGroup group : wdmLink.getWdmRelatedList()) {
					for (WDMLink wLink : group.getSRLGWDMLinkList()) {
						wLink.setActive(false);
					}
				}
			}
		}
		// �������·�ɴ��ڣ��򽫱���·����Ϊ������
		if (traffic.getProtectRoute() != null) {
			List<WDMLink> protectLinkList = traffic.getProtectRoute().getWDMLinkList();
			for (WDMLink wdmLink : protectLinkList) {
				wdmLink.setActive(false);
				for (FiberLink link : wdmLink.getFiberLinkList()) {// ���ñ���·�ɲ����������㶯̬·��
					link.setActive(false);
				}
				// ����srlg,�����������һ����־λ��ѡ���Ƿ���srlg
				if (LinkRGroup.srlg == true) {
					for (LinkRGroup group : wdmLink.getWdmRelatedList()) {
						for (WDMLink wLink : group.getSRLGWDMLinkList()) {
							wLink.setActive(false);
						}
					}
				}
			}
		}

		// �����ҵ���й���ҵ���飬��������Ϊ�������ָ�·�ɾ����룬���������ҵ�����ҵ��Ļָ�·����Ϊfalse
		if (traffic.getTrafficgroup() != null && traffic.getTrafficgroup().getType().equals("�������ָ�·�ɾ�����"))// ���ҵ��Ĺ���ҵ���鲻Ϊ��
		{
			for (int i = 0; i < Traffic.trafficList.size(); i++) {
				Traffic tra = Traffic.trafficList.get(i);
				// �ж��Ƿ���һ�����չ���ҵ���鲢�Ҳ���ͬһ��ҵ��
				if (tra.getTrafficgroup() != null && tra.getTrafficgroup().getId() == traffic.getTrafficgroup().getId()
						&& tra.getRankId() != traffic.getRankId()) {
					if (tra.getDynamicRoute() != null) {// ����������ҵ��̬�ָ�·�ɴ���
						for (int j = 0; j < tra.getDynamicRoute().getWDMLinkList().size(); j++)
							tra.getDynamicRoute().getWDMLinkList().get(j).setActive(false);
					}
				}
			}
		}

		// ��ʼ��·
		Route route1 = null; // �ԶϵĽڵ���·
		Route route2 = null; // ��ҵ�����ĩ�ڵ���·�����ã����route1��Դ���䲻�ɹ�����route2
		// ��ʱ���ص�routeֻ����returnnodelist��returnlinklist�������ԣ��ȸ�route������Щ�ڵ����·
		route1 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, from, to);
		route2 = RouteAlloc.findWDMRoute(traffic, flag);

		// ��·��ɺ�Ϳ��Խ����е���·��������
		// ��������·��Ϊ����
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink.WDMLinkList.get(i).setActive(true);
		}

		if (route1 != null && route1.getWDMLinkList().size() != 0) {
			// ��ʼ�ж����route���Ƿ��п��ò�����Դ
			if (Route.isRouteHaveWave(route1, wave, traffic)) {// true���У���Ϊ��route���䲨����Դwave
				for (int hop = 0; hop < route1.getWDMLinkList().size(); hop++) {// Ϊÿһ��wdmLink��·������������
					WDMLink wdmLink = route1.getWDMLinkList().get(hop);
					{
						if (wdmLink.getWaveLengthList().get(wave).getStatus().equals(Status.����)) {
							wdmLink.setRemainResource(wdmLink.getRemainResource() - 1);
						} // ������·ʣ����Դ
						wdmLink.getWaveLengthList().get(wave).setStatus(Status.�ָ�);// �ı乤��״̬
						wdmLink.getWaveLengthList().get(wave).getDynamicTrafficList().add(traffic);// ����ò����Ķ�̬ҵ���
						wdmLink.getCarriedTrafficList().add(traffic);// Ϊ��·��ӳ���ҵ��
					}
				}
				return route1;
			} // �����������
				// ���waveû����Դ��������ҵ����ĩ�ڵ������route2������Դ����
			if (!Route.isRouteHaveWave(route1, wave, traffic)) {
				if (route2 != null && route2.getWDMLinkList().size() != 0) {
					// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// �������osnrҪ��
					// �ܿ�OSNR
					if (1 > 0) {
						if (CommonAlloc.allocateWaveLength3(route2) && PortAlloc.allocatePort(route2)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
							// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "Ԥ��·�ɼ���Դ����ɹ���\r\n");
							traffic.setDynamicRoute(route2);// ���·�ɲ�Ϊ��������Ԥ����·��
							traffic.setStatus(TrafficStatus.��̬��·���ѷ���);
							return route2;
						} else {// ��Դ����ʧ��

							// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "Ԥ��·�ɲ�����˿���Դ����ʧ�ܣ�\r\n");
							RouteAlloc.releaseRoute(route2);
						}
					}
					// else {// ���osnr��������
					// if (Suggest.isSuggested == true) {// 2017.10.14
					// OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
					// OSNR.OSNRRouteList.add(route);//10.19
					// }
					// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·��OSNR������������\r\n");
					// }
				} else {
					// ��·ʧ��
					// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "���ڲ�����Դ����Ԥ��·����·ʧ�ܣ�\r\n");
				}
			}
		}
		else if (route2 != null && route2.getWDMLinkList().size() != 0) {
			// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// �������osnrҪ��
			// �ܿ�OSNR
			if (1 > 0) {
				if (CommonAlloc.allocateWaveLength3(route2) && PortAlloc.allocatePort(route2)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
					// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "Ԥ��·�ɼ���Դ����ɹ���\r\n");
					traffic.setDynamicRoute(route2);// ���·�ɲ�Ϊ��������Ԥ����·��
					traffic.setStatus(TrafficStatus.��̬��·���ѷ���);
					return route2;
				} else {// ��Դ����ʧ��

					// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "Ԥ��·�ɲ�����˿���Դ����ʧ�ܣ�\r\n");
					RouteAlloc.releaseRoute(route2);
				}
			}
			// else {// ���osnr��������
			// if (Suggest.isSuggested == true) {// 2017.10.14
			// OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
			// OSNR.OSNRRouteList.add(route);//10.19
			// }
			// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·��OSNR������������\r\n");
			// }
		}
		return null;
	}

	// �ڵ��
	public static Route reRoute1(Route oldroute, CommonNode from, CommonNode to, int wave1, int wave2, int flag) {

		Traffic traffic = oldroute.getBelongsTraffic();
		List<WDMLink> workLinkList = traffic.getWorkRoute().getWDMLinkList();

		// ����·��û�п��ò�����Դ����·��Ϊ������
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			if ((WDMLink.WDMLinkList.get(i).getRemainResource() == 0))
				WDMLink.WDMLinkList.get(i).setActive(false);
		}
		// ��ԭ·����Ϊ������
		for (int i = 0; i < oldroute.getWDMLinkList().size(); i++) {
			oldroute.getWDMLinkList().get(i).setActive(false);
		}

		// ���ù���·�ɲ����������㶯̬�ָ�·��
		for (WDMLink wdmLink : workLinkList) {
			wdmLink.setActive(false);
			for (FiberLink link : wdmLink.getFiberLinkList()) {
				link.setActive(false);
			}
			// ����srlg,�����������һ����־λ��ѡ���Ƿ���srlg
			if (LinkRGroup.srlg == true) {
				for (LinkRGroup group : wdmLink.getWdmRelatedList()) {
					for (WDMLink wLink : group.getSRLGWDMLinkList()) {
						wLink.setActive(false);
					}
				}
			}
		}
		// �������·�ɴ��ڣ��򽫱���·����Ϊ������
		if (traffic.getProtectRoute() != null) {
			List<WDMLink> protectLinkList = traffic.getProtectRoute().getWDMLinkList();
			for (WDMLink wdmLink : protectLinkList) {
				wdmLink.setActive(false);
				for (FiberLink link : wdmLink.getFiberLinkList()) {// ���ñ���·�ɲ����������㶯̬·��
					link.setActive(false);
				}
				// ����srlg,�����������һ����־λ��ѡ���Ƿ���srlg
				if (LinkRGroup.srlg == true) {
					for (LinkRGroup group : wdmLink.getWdmRelatedList()) {
						for (WDMLink wLink : group.getSRLGWDMLinkList()) {
							wLink.setActive(false);
						}
					}
				}
			}
		}

		// �����ҵ���й���ҵ���飬��������Ϊ�������ָ�·�ɾ����룬���������ҵ�����ҵ��Ļָ�·����Ϊfalse
		if (traffic.getTrafficgroup() != null && traffic.getTrafficgroup().getType().equals("�������ָ�·�ɾ�����"))// ���ҵ��Ĺ���ҵ���鲻Ϊ��
		{
			for (int i = 0; i < Traffic.trafficList.size(); i++) {
				Traffic tra = Traffic.trafficList.get(i);
				// �ж��Ƿ���һ�����չ���ҵ���鲢�Ҳ���ͬһ��ҵ��
				if (tra.getTrafficgroup() != null && tra.getTrafficgroup().getId() == traffic.getTrafficgroup().getId()
						&& tra.getRankId() != traffic.getRankId()) {
					if (tra.getDynamicRoute() != null) {// ����������ҵ��̬�ָ�·�ɴ���
						for (int j = 0; j < tra.getDynamicRoute().getWDMLinkList().size(); j++)
							tra.getDynamicRoute().getWDMLinkList().get(j).setActive(false);
					}
				}
			}
		}

		// ��ʼ��·
		Route route1 = null; // �ԶϵĽڵ���·
		Route route2 = null; // ��ҵ�����ĩ�ڵ���·�����ã����route1��Դ���䲻�ɹ�����route2
		// ��ʱ���ص�routeֻ����returnnodelist��returnlinklist�������ԣ��ȸ�route������Щ�ڵ����·
		route1 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, from, to);
		route2 = RouteAlloc.findWDMRoute(traffic, flag);

		// ��·��ɺ�Ϳ��Խ����е���·��������
		// ��������·��Ϊ����
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink.WDMLinkList.get(i).setActive(true);
		}

		if (route1 != null && route1.getWDMLinkList().size() != 0) {
			// ��ʼ�ж����route���Ƿ��п��ò�����Դ
			if (Route.isRouteHaveWave(route1, wave1, traffic)) {// true���У���Ϊ��route���䲨����Դwave
				for (int hop = 0; hop < route1.getWDMLinkList().size(); hop++) {// Ϊÿһ��wdmLink��·������������
					WDMLink wdmLink = route1.getWDMLinkList().get(hop);
					{
						if (wdmLink.getWaveLengthList().get(wave1).getStatus().equals(Status.����)) {
							wdmLink.setRemainResource(wdmLink.getRemainResource() - 1);
						} // ������·ʣ����Դ
						wdmLink.getWaveLengthList().get(wave1).setStatus(Status.�ָ�);// �ı乤��״̬
						wdmLink.getWaveLengthList().get(wave1).getDynamicTrafficList().add(traffic);// ����ò����Ķ�̬ҵ���
						wdmLink.getCarriedTrafficList().add(traffic);// Ϊ��·��ӳ���ҵ��
					}
				}
				return route1;
			} // �����������
			if (Route.isRouteHaveWave(route1, wave2, traffic)) {// true���У���Ϊ��route���䲨����Դwave
				for (int hop = 0; hop < route1.getWDMLinkList().size(); hop++) {// Ϊÿһ��wdmLink��·������������
					WDMLink wdmLink = route1.getWDMLinkList().get(hop);
					{
						if (wdmLink.getWaveLengthList().get(wave2).getStatus().equals(Status.����)) {
							wdmLink.setRemainResource(wdmLink.getRemainResource() - 1);
						} // ������·ʣ����Դ
						wdmLink.getWaveLengthList().get(wave2).setStatus(Status.�ָ�);// �ı乤��״̬
						wdmLink.getWaveLengthList().get(wave2).getDynamicTrafficList().add(traffic);// ����ò����Ķ�̬ҵ���
						wdmLink.getCarriedTrafficList().add(traffic);// Ϊ��·��ӳ���ҵ��
					}
				}
				return route1;
			} // �����������
				// ���wave1��wave2��û����Դ��������ҵ����ĩ�ڵ������route2������Դ����
			if (Route.isRouteHaveWave(route1, wave1, traffic) == false
					&& Route.isRouteHaveWave(route1, wave2, traffic) == false) {
				if (route2 != null && route2.getWDMLinkList().size() != 0) {
					// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// �������osnrҪ��
					// �ܿ�OSNR
					if (1 > 0) {
						if (CommonAlloc.allocateWaveLength3(route2) && PortAlloc.allocatePort(route2)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
							// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "Ԥ��·�ɼ���Դ����ɹ���\r\n");
							traffic.setDynamicRoute(route2);// ���·�ɲ�Ϊ��������Ԥ����·��
							traffic.setStatus(TrafficStatus.��̬��·���ѷ���);
							return route2;
						} else {// ��Դ����ʧ��

							// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "Ԥ��·�ɲ�����˿���Դ����ʧ�ܣ�\r\n");
							RouteAlloc.releaseRoute(route2);
						}
					}
					// else {// ���osnr��������
					// if (Suggest.isSuggested == true) {// 2017.10.14
					// OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
					// OSNR.OSNRRouteList.add(route);//10.19
					// }
					// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·��OSNR������������\r\n");
					// }
				} else {
					// ��·ʧ��
					// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "���ڲ�����Դ����Ԥ��·����·ʧ�ܣ�\r\n");
				}
			}
		}
		else if (route2 != null && route2.getWDMLinkList().size() != 0) {
			// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// �������osnrҪ��
			// �ܿ�OSNR
			if (1 > 0) {
				if (CommonAlloc.allocateWaveLength3(route2) && PortAlloc.allocatePort(route2)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
					// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "Ԥ��·�ɼ���Դ����ɹ���\r\n");
					traffic.setDynamicRoute(route2);// ���·�ɲ�Ϊ��������Ԥ����·��
					traffic.setStatus(TrafficStatus.��̬��·���ѷ���);
					return route2;
				} else {// ��Դ����ʧ��

					// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "Ԥ��·�ɲ�����˿���Դ����ʧ�ܣ�\r\n");
					RouteAlloc.releaseRoute(route2);
				}
			}
			// else {// ���osnr��������
			// if (Suggest.isSuggested == true) {// 2017.10.14
			// OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
			// OSNR.OSNRRouteList.add(route);//10.19
			// }
			// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·��OSNR������������\r\n");
			// }
		}
		return null;
	}
}
