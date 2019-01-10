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
 * ���ܣ����๩�滮�򵼵��ã��ǽ�����Դ���������ڡ� ��Դ�����Ϊ��·��Դ���䣨CommonAlloc���Ͷ˿���Դ���䣨PortAlloc�� ��
 * ��������Դ������ ��
 * 
 * @author CC
 * @since 2016/7/4
 */
public class ResourceAlloc {
	public static int index = 1;
	public static List<Traffic> trafficList = new LinkedList<Traffic>(); // �����ҵ���б�ľ���
	public static List<Traffic> failedTraffic = new LinkedList<Traffic>();// �洢ʧ�ܵ�ҵ��
	// ��Ź���·�ɼ���ʧ�ܵ�ҵ��
	LinkedList<Traffic> workFailure = new LinkedList<>();
	// ��ű���·�ɼ���ʧ�ܵ�ҵ��
	LinkedList<Traffic> protectFailure = new LinkedList<>();
	// ���Ԥ��·�ɼ���ʧ�ܵ�ҵ��
	LinkedList<Traffic> preFailure = new LinkedList<>();
	// public static int lock = DataSave.locknum; // ����

	public static void allocateResource(List<Traffic> traList, int flag) {// flag=0:��̳��ȣ�=1����С��
		Survivance.mark = flag;// ���ڿ��ٷ����б�Ƿ����ʱ��ʹ�õ�����·���㷨
		CommonAlloc.fallBuffer = new StringBuffer();// ��·��Դ������־
		PortAlloc.portFallBuffer = new StringBuffer();// �˿���Դ������־

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

		trafficList.addAll(traList);// ������һ��ҵ���б���trafficList�Ĳ�������Ӱ��traList
		// Collections.reverse(traList);

		// ��Ź���·�ɼ���ʧ�ܵ�ҵ��
		LinkedList<Traffic> workFailure = new LinkedList<>();
		// ��ű���·�ɼ���ʧ�ܵ�ҵ��
		LinkedList<Traffic> protectFailure = new LinkedList<>();
		// ���Ԥ��·�ɼ���ʧ�ܵ�ҵ��
		LinkedList<Traffic> preFailure = new LinkedList<>();

		Route route = null;
		OSNR osnr = new OSNR();
		if (!RouteAlloc.isLimitHop)
			RouteAlloc.hopLimit = Integer.MAX_VALUE; // ������������(�����������������Ͱ���������Ϊ�����)
		else if (RouteAlloc.hopLimit == Integer.MAX_VALUE)
			RouteAlloc.hopLimit = 5; // Ĭ����������Ϊ5

		Iterator<Traffic> it = trafficList.iterator();
		while (it.hasNext()) {// ѭ������������ÿ��ҵ��Ĺ���·��/����·�ɣ�����У�

			index++;
			Traffic traffic = it.next();

			// ���ر���·���ڵ���Ϊ������
			ResourceAlloc.setMustAvoidUnactive(traffic);

			// ����ҵ�����������ò��������������Ӧ��SRLGΪ�����
			ResourceAlloc.setRelatedTrafficUnactive(traffic, DataSave.separate);

			// ����Դ�������·��Ϊ������ ���룺�Ƿ��Ǽ���Ԥ��·��
			LinkedList<WDMLink> canActiveLinks = ResourceAlloc.resourceNotEnough(false);

			// ����SRLG����·��Ϊ������
			// LinkedList<WDMLink> srlgLink = ResourceAlloc.setSRLGFalse();

			// �ؾ��ڵ㡢�ؾ���·,��·
			route = ResourceAlloc.routeForTraffic(traffic, flag);

			// ����ҵ����ҵ�񣬵�·������·��ƽ�б�ʱ������ʹ��û��SRLG����·
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
			// ����ҵ����ҵ��Ӧ�þ�����һ����·�������Ƿ�ɢ�ڶ��ƽ�б���
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
				// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// �������osnrҪ��
				if (1 > 0) {// Ϊ�˱ܿ�OSNR���� ��3��ʱҪ�ĵ�
					Traffic tra = route.getBelongsTraffic();
					if (tra.isElectricalCrossConnection() == true && tra.getNrate() < 100) {
						if (CommonAlloc.EallocateWaveLength_workandpro(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
							// �˿ڷ���ûд��Ҫ��
							System.out.println("�Ӳ���ҵ��" + traffic.getName() + "����·�ɷ���ɹ�");
							CommonAlloc.fallBuffer.append(
									"ҵ��:" + traffic.getName() + "(" + traffic.getNrate() + ")����·�ɼ���Դ����ɹ���(�Ӳ�������)\r\n");
							traffic.setWorkRoute(route);// ���·�ɲ�Ϊ��������Ϊ����·��
							traffic.setStatus(TrafficStatus.�����ѷ���);
						} else {// ��Դ����ʧ��
							failedTraffic.add(traffic);
							System.out.println("ҵ��" + traffic.getName() + "����·�ɷ���ʧ��");
							traffic.setStatus(TrafficStatus.����);// �����û�о����Ҳ���
							CommonAlloc.fallBuffer.append(
									"ҵ��:" + traffic.getName() + "(" + traffic.getNrate() + ")����·�ɲ�����Դ����ʧ�ܣ�\r\n");
							RouteAlloc.releaseRoute(route);
						}
					} else if (tra.isElectricalCrossConnection() == false || tra.getNrate() == 100) {
						// 100Gҵ��Ҫ�����Ƿ���Ҫ����ת����osnr��������������������㣬��Ҫ�Ӳ�������

						// �ж�route�ɷ�ʹ��һ�²���
						ResourceAlloc.isRouteConsistent(route, traffic, 1, flag, canActiveLinks);

						if (CommonAlloc.allocateWaveLength(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
							System.out.println("ҵ��" + traffic.getName() + "����·�ɷ���ɹ�");
							CommonAlloc.fallBuffer
									.append("ҵ��:" + traffic.getName() + "(" + traffic.getNrate() + ")����·�ɼ���Դ����ɹ���\r\n");
							traffic.setWorkRoute(route);// ���·�ɲ�Ϊ��������Ϊ����·��
							traffic.setStatus(TrafficStatus.�����ѷ���);
						} else {// ��Դ����ʧ��

							failedTraffic.add(traffic);
							workFailure.add(traffic);
							System.out.println("ҵ��" + traffic.getName() + "����·�ɷ���ʧ��");
							traffic.setStatus(TrafficStatus.����);// �����û�о����Ҳ���
							CommonAlloc.fallBuffer.append(
									"ҵ��:" + traffic.getName() + "(" + traffic.getNrate() + ")����·�ɲ�����Դ����ʧ�ܣ�\r\n");
							RouteAlloc.releaseRoute(route);
						}
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

				if (traffic.getWorkRoute() == null) {
					traffic.setStatus(TrafficStatus.�滮ҵ������);// �����û�о����Ҳ���
					CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·����·ʧ�ܣ�\r\n");
				}
			}

			//////// ��ʼ���㱣��·��
			if (traffic.getWorkRoute() != null && (traffic.getProtectLevel() == TrafficLevel.NORMAL11
					|| traffic.getProtectLevel() == TrafficLevel.PERMANENT11
					|| traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION)) {

				// ���ù���·�ɲ����������㱣��·��
				ResourceAlloc.setRouteLinkUnactive(traffic.getWorkRoute());

				// //����srlg��link����
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

					// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// �������osnrҪ��
					// Ϊ�˱ܿ�OSNR 3��Ҫ�Ļ�ȥ
					if (1 > 0) {
						Traffic tra = route.getBelongsTraffic();
						if (tra.isElectricalCrossConnection() == true && tra.getNrate() != 100) {
							if (CommonAlloc.EallocateWaveLength_workandpro(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
								// �˿ڷ���ûд��Ҫ��
								System.out.println("�Ӳ���ҵ��" + traffic.getName() + "����·�ɷ���ɹ�");
								CommonAlloc.fallBuffer.append("ҵ��:" + traffic.getName() + "(" + traffic.getNrate()
										+ ")����·�ɼ���Դ����ɹ���(�Ӳ�������)\r\n");
								traffic.setProtectRoute(route);// ���·�ɲ�Ϊ��������Ϊ����·��
								traffic.setStatus(TrafficStatus.�����ͱ����ѷ���);
							} else {// ��Դ����ʧ��
								failedTraffic.add(traffic);
								System.out.println("ҵ��" + traffic.getName() + "����·�ɷ���ʧ��");
								traffic.setStatus(TrafficStatus.����);// �����û�о����Ҳ���
								CommonAlloc.fallBuffer.append(
										"ҵ��:" + traffic.getName() + "(" + traffic.getNrate() + ")����·�ɲ�����Դ����ʧ�ܣ�\r\n");
								// RouteAlloc.releaseRoute(route);
							}
						} else if (tra.isElectricalCrossConnection() == false || tra.getNrate() == 100) {

							// �ж�route�ɷ�ʹ��һ�²���
							ResourceAlloc.isRouteConsistent(route, traffic, 1, flag, canActiveLinks);

							// 100Gҵ��Ҫ�����Ƿ���Ҫ����ת����osnr��������������������㣬��Ҫ�Ӳ�������
							if (CommonAlloc.allocateWaveLength1(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
								System.out.println("ҵ��" + traffic.getName() + "����·����Դ����ɹ�");
								CommonAlloc.fallBuffer.append(
										"ҵ��:" + traffic.getName() + "(" + traffic.getNrate() + ")����·�ɼ���Դ����ɹ���\r\n");
								traffic.setProtectRoute(route);// ���·�ɲ�Ϊ��������Ϊ����·��
								traffic.setStatus(TrafficStatus.�����ͱ����ѷ���);
							} else {// ��Դ����ʧ��
								failedTraffic.add(traffic);
								protectFailure.add(traffic);
								System.out.println("ҵ��" + traffic.getName() + "����·����Դ����ʧ��");
								traffic.setStatus(TrafficStatus.����);// �����û�о����Ҳ���
								CommonAlloc.fallBuffer.append(
										"ҵ��:" + traffic.getName() + "(" + traffic.getNrate() + ")����·�ɲ�����Դ����ʧ�ܣ�\r\n");
								// RouteAlloc.releaseRoute(route);
							}
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
					CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·����·ʧ�ܣ�\r\n");
				}

			} // ����·�ɼ������

			if (DataSave.separate1 == 2) {
				//////// ��ʼ����ר��Ԥ�ûָ�·��
				if (traffic.getWorkRoute() != null && (traffic.getProtectLevel() == TrafficLevel.RESTORATION)
						|| traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION) {// �������·�ɴ��ڣ����ұ����ȼ�������·��

					ResourceAlloc.setMustAvoidUnactive(traffic);

					// ���ù���·�ɲ�����������Ԥ��·��
					ResourceAlloc.setRouteLinkUnactive(traffic.getWorkRoute());

					// �������·�ɴ��ڣ��򽫱���·����Ϊ������
					if (traffic.getProtectRoute() != null) {
						ResourceAlloc.setRouteLinkUnactive(traffic.getProtectRoute());
					}

					// ������Դ������·
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

						// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// �������osnrҪ��
						// �ܿ�OSNR
						if (1 > 0) {
							Traffic tra = route.getBelongsTraffic();
							if (tra.isElectricalCrossConnection() == true && tra.getNrate() < 100) {
								if (CommonAlloc.EallocateWaveLength_pre(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
									// �˿ڷ���ûд��Ҫ��
									System.out.println("�Ӳ���ҵ��" + traffic.getName() + "Ԥ��·�ɲ�����Դ����ɹ�");
									traffic.setPreRoute(route);// ���·�ɲ�Ϊ��������Ԥ����·��
									CommonAlloc.fallBuffer.append("ҵ��:" + traffic.getName() + "(" + traffic.getNrate()
											+ ")Ԥ��·�ɲ�����Դ����ɹ���(�Ӳ�������)\r\n");
									// traffic.setWorkRoute(route);// ���·�ɲ�Ϊ��������Ϊ����·��
									traffic.setStatus(TrafficStatus.�����ͱ�������·���ѷ���);
								} else {// ��Դ����ʧ��
									CommonAlloc.fallBuffer.append("ҵ��:" + traffic.getName() + "(" + traffic.getNrate()
											+ ")Ԥ��·�ɲ�����Դ����ʧ�ܣ�\r\n");
									// RouteAlloc.releaseRoute(route);
								}
							} else if (tra.isElectricalCrossConnection() == false || tra.getNrate() == 100) {

								// ����һ����·
								ResourceAlloc.isRouteConsistent(route, traffic, 2, flag, null);

								// 100Gҵ��Ҫ�����Ƿ���Ҫ����ת����osnr��������������������㣬��Ҫ�Ӳ�������
								if (CommonAlloc.allocateWaveLength2(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
									System.out.println("ҵ��" + traffic.getName() + "Ԥ��·�ɲ�����Դ����ɹ�");
									traffic.setPreRoute(route);// ���·�ɲ�Ϊ��������Ԥ����·��
									CommonAlloc.fallBuffer.append("ҵ��:" + traffic.getName() + "(" + traffic.getNrate()
											+ ")Ԥ��·�ɲ�����Դ����ɹ���\r\n");
									// traffic.setWorkRoute(route);// ���·�ɲ�Ϊ��������Ϊ����·��
									traffic.setStatus(TrafficStatus.�����ͱ�������·���ѷ���);
								} else {// ��Դ����ʧ��
									// System.out.println(route);
									// for(int i=0;i<route.getWDMLinkList().size();i++) {
									// System.out.println(route.getWDMLinkList().get(i).getName()+"
									// ʣ��"+route.getWDMLinkList().get(i).getRemainResource()+" preʹ��"+
									// route.getWDMLinkList().get(i).preResource());
									// }
									CommonAlloc.fallBuffer.append("ҵ��:" + traffic.getName() + "(" + traffic.getNrate()
											+ ")Ԥ��·�ɲ�����Դ����ʧ�ܣ�\r\n");
									// RouteAlloc.releaseRoute(route);
								}
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
						CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "Ԥ��·����·ʧ�ܣ�\r\n");
					}

				} // ר��Ԥ�ûָ�·��
			}

			// ��������·��Ϊ����
			for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
				WDMLink.WDMLinkList.get(i).setActive(true);
			}
			for (int j = 0; j < CommonNode.allNodeList.size(); j++) {
				CommonNode.allNodeList.get(j).setActive(true);
			}

		}

		if (DataSave.separate1 == 1) {

			// ����Ԥ��·��
			List<Traffic> newlist = Traffic.reRankList(traList);
			for (int i = 0; i < newlist.size(); i++) {
				Traffic traffic = traList.get(i);

				if (traffic.getWorkRoute() != null && (traffic.getProtectLevel() == TrafficLevel.RESTORATION)
						|| traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION) {
					// 1.������·���ڵ�ļ���״̬
					// ���ر���·���ڵ���Ϊ������
					ResourceAlloc.setMustAvoidUnactive(traffic);
					// ������·�ɼ�������Ϊ������
					LinkedList<WDMLink> workList = ResourceAlloc.setRouteLinkUnactive(traffic.getWorkRoute());
					ResourceAlloc.setRouteLinkUnactive(traffic.getProtectRoute());
					// ������ҵ����Ĺ���·����Ϊ������
					LinkedList<WDMLink> relatedList = ResourceAlloc.setRelatedTrafficUnactive(traffic,
							DataSave.separate);

					// 2.����·��
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

					// 3.��������
					if (preroute != null && preroute.getWDMLinkList().size() != 0) {
						if (1 > 0) {
							Traffic tra = traffic;
							if (tra.isElectricalCrossConnection() == true && tra.getNrate() < 100) {
								if (CommonAlloc.EallocateWaveLength_pre(preroute)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
									// �˿ڷ���ûд��Ҫ��
									System.out.println("�Ӳ���ҵ��" + traffic.getName() + "Ԥ��·�ɲ�����Դ����ɹ�");
									traffic.setPreRoute(preroute);// ���·�ɲ�Ϊ��������Ԥ����·��
									CommonAlloc.fallBuffer.append("ҵ��:" + traffic.getName() + "(" + traffic.getNrate()
											+ ")Ԥ��·�ɲ�����Դ����ɹ���(�Ӳ�������)\r\n");
									// traffic.setWorkRoute(route);// ���·�ɲ�Ϊ��������Ϊ����·��
									traffic.setStatus(TrafficStatus.�����ͱ�������·���ѷ���);
								} else {// ��Դ����ʧ��
									CommonAlloc.fallBuffer.append("ҵ��:" + traffic.getName() + "(" + traffic.getNrate()
											+ ")Ԥ��·�ɲ�����Դ����ʧ�ܣ�\r\n");
									// RouteAlloc.releaseRoute(route);
								}
							} else if (tra.isElectricalCrossConnection() == false || tra.getNrate() == 100) {

								// ����һ����·
								ResourceAlloc.isRouteConsistent(preroute, traffic, 2, flag, null);

								// 100Gҵ��Ҫ�����Ƿ���Ҫ����ת����osnr��������������������㣬��Ҫ�Ӳ�������
								if (CommonAlloc.allocateWaveLength2(preroute)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
									System.out.println("ҵ��" + traffic.getName() + "Ԥ��·�ɲ�����Դ����ɹ�");
									traffic.setPreRoute(preroute);// ���·�ɲ�Ϊ��������Ԥ����·��
									CommonAlloc.fallBuffer.append("ҵ��:" + traffic.getName() + "(" + traffic.getNrate()
											+ ")Ԥ��·�ɲ�����Դ����ɹ���\r\n");
									// traffic.setWorkRoute(route);// ���·�ɲ�Ϊ��������Ϊ����·��
									traffic.setStatus(TrafficStatus.�����ͱ�������·���ѷ���);
								} else {// ��Դ����ʧ��
									// System.out.println(route);
									// for(int i=0;i<route.getWDMLinkList().size();i++) {
									// System.out.println(route.getWDMLinkList().get(i).getName()+"
									// ʣ��"+route.getWDMLinkList().get(i).getRemainResource()+" preʹ��"+
									// route.getWDMLinkList().get(i).preResource());
									// }
									CommonAlloc.fallBuffer.append("ҵ��:" + traffic.getName() + "(" + traffic.getNrate()
											+ ")Ԥ��·�ɲ�����Դ����ʧ�ܣ�\r\n");
									// RouteAlloc.releaseRoute(route);
								}
							}
						}
					} else {
						CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "Ԥ��·����·ʧ�ܣ�\r\n");
					}
				}
			}
		}

		// ҵ��ѭ������
		ResourceLogTxt.ResourceLogTxt();// �����Դ������־.
		CommonAlloc.fallBuffer.delete(0, CommonAlloc.fallBuffer.length());
		// System.out.println(CommonNode.allNodeList);//12.14

		trafficList.clear();
		Dlg_PolicySetting.isDesign = true; // CC2017.5.2

	}

	public static void allocateResource1(List<Traffic> traList, int flag) {// flag=0:��̳��ȣ�=1����С��
		Survivance.mark = flag;// ���ڿ��ٷ����б�Ƿ����ʱ��ʹ�õ�����·���㷨
		CommonAlloc.fallBuffer = new StringBuffer();// ��·��Դ������־
		PortAlloc.portFallBuffer = new StringBuffer();// �˿���Դ������־

		trafficList = new LinkedList<Traffic>();

		// trafficList.addAll(traList);// ������һ��ҵ���б���trafficList�Ĳ�������Ӱ��traList
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

		// ��Ź���·�ɼ���ʧ�ܵ�ҵ��
		LinkedList<Traffic> workFailure = new LinkedList<>();
		// ��ű���·�ɼ���ʧ�ܵ�ҵ��
		LinkedList<Traffic> protectFailure = new LinkedList<>();
		// ���Ԥ��·�ɼ���ʧ�ܵ�ҵ��
		LinkedList<Traffic> preFailure = new LinkedList<>();

		Route route = null;
		OSNR osnr = new OSNR();
		if (!RouteAlloc.isLimitHop)
			RouteAlloc.hopLimit = Integer.MAX_VALUE; // ������������(�����������������Ͱ���������Ϊ�����)
		else if (RouteAlloc.hopLimit == Integer.MAX_VALUE)
			RouteAlloc.hopLimit = 5; // Ĭ����������Ϊ5

		Iterator<Traffic> it = trafficList.iterator();
		while (it.hasNext()) {// ѭ������������ÿ��ҵ��Ĺ���·��/����·�ɣ�����У�

			index++;
			Traffic traffic = it.next();

			// ���ر���·���ڵ���Ϊ������
			ResourceAlloc.setMustAvoidUnactive(traffic);

			// ����ҵ�����������ò��������������Ӧ��SRLGΪ�����
			ResourceAlloc.setRelatedTrafficUnactive(traffic, DataSave.separate);

			// ����Դ�������·��Ϊ������ ���룺�Ƿ��Ǽ���Ԥ��·��
			LinkedList<WDMLink> canActiveLinks = ResourceAlloc.resourceNotEnough(false);

			// ����SRLG����·��Ϊ������
			// LinkedList<WDMLink> srlgLink = ResourceAlloc.setSRLGFalse();

			// �ؾ��ڵ㡢�ؾ���·,��·
			route = ResourceAlloc.routeForTraffic(traffic, flag);

			// ����ҵ����ҵ�񣬵�·������·��ƽ�б�ʱ������ʹ��û��SRLG����·
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
			// ����ҵ����ҵ��Ӧ�þ�����һ����·�������Ƿ�ɢ�ڶ��ƽ�б���
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
			// ����ҵ�������ʹ��ͬһ��·��

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
				// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// �������osnrҪ��
				if (1 > 0) {// Ϊ�˱ܿ�OSNR���� ��3��ʱҪ�ĵ�
					Traffic tra = route.getBelongsTraffic();
					if (tra.isElectricalCrossConnection() == true && tra.getNrate() < 100) {
						if (CommonAlloc.EallocateWaveLength_workandpro(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
							// �˿ڷ���ûд��Ҫ��
							System.out.println("�Ӳ���ҵ��" + traffic.getName() + "����·�ɷ���ɹ�");
							CommonAlloc.fallBuffer.append(
									"ҵ��:" + traffic.getName() + "(" + traffic.getNrate() + ")����·�ɼ���Դ����ɹ���(�Ӳ�������)\r\n");
							traffic.setWorkRoute(route);// ���·�ɲ�Ϊ��������Ϊ����·��
							traffic.setStatus(TrafficStatus.�����ѷ���);
						} else {// ��Դ����ʧ��
							failedTraffic.add(traffic);
							System.out.println("ҵ��" + traffic.getName() + "����·�ɷ���ʧ��");
							traffic.setStatus(TrafficStatus.����);// �����û�о����Ҳ���
							CommonAlloc.fallBuffer.append(
									"ҵ��:" + traffic.getName() + "(" + traffic.getNrate() + ")����·�ɲ�����Դ����ʧ�ܣ�\r\n");
							RouteAlloc.releaseRoute(route);
						}
					} else if (tra.isElectricalCrossConnection() == false || tra.getNrate() == 100) {
						// 100Gҵ��Ҫ�����Ƿ���Ҫ����ת����osnr��������������������㣬��Ҫ�Ӳ�������

						// �ж�route�ɷ�ʹ��һ�²���
						ResourceAlloc.isRouteConsistent(route, traffic, 1, flag, canActiveLinks);

						if (CommonAlloc.allocateWaveLength(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
							System.out.println("ҵ��" + traffic.getName() + "����·�ɷ���ɹ�");
							CommonAlloc.fallBuffer
									.append("ҵ��:" + traffic.getName() + "(" + traffic.getNrate() + ")����·�ɼ���Դ����ɹ���\r\n");
							traffic.setWorkRoute(route);// ���·�ɲ�Ϊ��������Ϊ����·��
							traffic.setStatus(TrafficStatus.�����ѷ���);
						} else {// ��Դ����ʧ��

							failedTraffic.add(traffic);
							workFailure.add(traffic);
							System.out.println("ҵ��" + traffic.getName() + "����·�ɷ���ʧ��");
							traffic.setStatus(TrafficStatus.����);// �����û�о����Ҳ���
							CommonAlloc.fallBuffer.append(
									"ҵ��:" + traffic.getName() + "(" + traffic.getNrate() + ")����·�ɲ�����Դ����ʧ�ܣ�\r\n");
							RouteAlloc.releaseRoute(route);
						}
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

				if (traffic.getWorkRoute() == null) {
					traffic.setStatus(TrafficStatus.�滮ҵ������);// �����û�о����Ҳ���
					CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·����·ʧ�ܣ�\r\n");
				}
			}

			//////// ��ʼ���㱣��·��
			if (traffic.getWorkRoute() != null && (traffic.getProtectLevel() == TrafficLevel.NORMAL11
					|| traffic.getProtectLevel() == TrafficLevel.PERMANENT11
					|| traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION)) {

				// ���ù���·�ɲ����������㱣��·��
				ResourceAlloc.setRouteLinkUnactive(traffic.getWorkRoute());

				// //����srlg��link����
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

					// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// �������osnrҪ��
					// Ϊ�˱ܿ�OSNR 3��Ҫ�Ļ�ȥ
					if (1 > 0) {
						Traffic tra = route.getBelongsTraffic();
						if (tra.isElectricalCrossConnection() == true && tra.getNrate() != 100) {
							if (CommonAlloc.EallocateWaveLength_workandpro(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
								// �˿ڷ���ûд��Ҫ��
								System.out.println("�Ӳ���ҵ��" + traffic.getName() + "����·�ɷ���ɹ�");
								CommonAlloc.fallBuffer.append("ҵ��:" + traffic.getName() + "(" + traffic.getNrate()
										+ ")����·�ɼ���Դ����ɹ���(�Ӳ�������)\r\n");
								traffic.setProtectRoute(route);// ���·�ɲ�Ϊ��������Ϊ����·��
								traffic.setStatus(TrafficStatus.�����ͱ����ѷ���);
							} else {// ��Դ����ʧ��
								failedTraffic.add(traffic);
								System.out.println("ҵ��" + traffic.getName() + "����·�ɷ���ʧ��");
								traffic.setStatus(TrafficStatus.����);// �����û�о����Ҳ���
								CommonAlloc.fallBuffer.append(
										"ҵ��:" + traffic.getName() + "(" + traffic.getNrate() + ")����·�ɲ�����Դ����ʧ�ܣ�\r\n");
								// RouteAlloc.releaseRoute(route);
							}
						} else if (tra.isElectricalCrossConnection() == false || tra.getNrate() == 100) {

							// �ж�route�ɷ�ʹ��һ�²���
							ResourceAlloc.isRouteConsistent(route, traffic, 1, flag, canActiveLinks);

							// 100Gҵ��Ҫ�����Ƿ���Ҫ����ת����osnr��������������������㣬��Ҫ�Ӳ�������
							if (CommonAlloc.allocateWaveLength1(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
								System.out.println("ҵ��" + traffic.getName() + "����·����Դ����ɹ�");
								CommonAlloc.fallBuffer.append(
										"ҵ��:" + traffic.getName() + "(" + traffic.getNrate() + ")����·�ɼ���Դ����ɹ���\r\n");
								traffic.setProtectRoute(route);// ���·�ɲ�Ϊ��������Ϊ����·��
								traffic.setStatus(TrafficStatus.�����ͱ����ѷ���);
							} else {// ��Դ����ʧ��
								failedTraffic.add(traffic);
								protectFailure.add(traffic);
								System.out.println("ҵ��" + traffic.getName() + "����·����Դ����ʧ��");
								traffic.setStatus(TrafficStatus.����);// �����û�о����Ҳ���
								CommonAlloc.fallBuffer.append(
										"ҵ��:" + traffic.getName() + "(" + traffic.getNrate() + ")����·�ɲ�����Դ����ʧ�ܣ�\r\n");
								// RouteAlloc.releaseRoute(route);
							}
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
					CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·����·ʧ�ܣ�\r\n");
				}

			} // ����·�ɼ������

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
			for (int j = 0; j < CommonNode.allNodeList.size(); j++) {
				CommonNode.allNodeList.get(j).setActive(true);
			}

		} // ҵ��ѭ������
		ResourceLogTxt.ResourceLogTxt();// �����Դ������־.
		CommonAlloc.fallBuffer.delete(0, CommonAlloc.fallBuffer.length());
		// System.out.println(CommonNode.allNodeList);//12.14

		trafficList.clear();
		Dlg_PolicySetting.isDesign = true; // CC2017.5.2

	}

	public static void allocateResource2(List<Traffic> traList, int flag) {// flag=0:��̳��ȣ�=1����С��
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
			/*
			 * 12.26֮ǰ�ؾ��ر������wdmlinkʱ // ����·��û�п��ò�����Դ����·��Ϊ������ for (int i = 0; i <
			 * WDMLink.WDMLinkList.size(); i++) { if
			 * (WDMLink.WDMLinkList.get(i).getRemainResource() == 0)
			 * WDMLink.WDMLinkList.get(i).setActive(false); } Traffic traffic = it.next();
			 * // ���ر���·��Ϊ������ if (traffic.getMustAvoidLink() != null) {
			 * traffic.getMustAvoidLink().setActive(false); } // ����ܱܽڵ���������·��Ϊ������ if
			 * (traffic.getMustAvoidNode() != null) { CommonNode mANode =
			 * traffic.getMustAvoidNode();// �رܽڵ� for (int i = 0; i <
			 * WDMLink.WDMLinkList.size(); i++) { if
			 * (WDMLink.WDMLinkList.get(i).getFromNode().getName().equals(mANode.getName())
			 * || WDMLink.WDMLinkList.get(i).getToNode().getName().equals(mANode.getName()))
			 * WDMLink.WDMLinkList.get(i).setActive(false); } }
			 * 
			 * // ����ҵ���� if (traffic.getTrafficgroup() != null)// ���ҵ��Ĺ���ҵ���鲻Ϊ�� { for (int i
			 * = 0; i < Traffic.trafficList.size(); i++) { Traffic tra =
			 * Traffic.trafficList.get(i); // �ж��Ƿ���һ�����չ���ҵ���鲢�Ҳ���ͬһ��ҵ�� if
			 * (tra.getTrafficgroup() != null && tra.getTrafficgroup().getId() ==
			 * traffic.getTrafficgroup().getId() && tra.getRankId() != traffic.getRankId())
			 * { if (tra.getWorkRoute() != null) {// ����������ҵ����·�ɴ��� for (int j = 0; j <
			 * tra.getWorkRoute().getWDMLinkList().size(); j++)
			 * tra.getWorkRoute().getWDMLinkList().get(j).setActive(false); } } } }
			 * 
			 * // ����ҵ����-����й���ҵ���飬�����֮�������ҵ��Ĺ���·����·��Ϊfalse // if(traffic.getM_cGroup()!=null)
			 * { // int groupId=traffic.getM_cGroup().getGroupId(); // for(int
			 * i=0;i<TrafficGroup.grouptrafficGroupList.size();i++) //
			 * if(TrafficGroup.grouptrafficGroupList.get(i).getRelatedId()==groupId)//
			 * �ҳ���֮������ҵ���� // { // for(int //
			 * j=0;j<TrafficGroup.grouptrafficGroupList.get(i).getTrafficList().size();j++)
			 * // { //�������·�ɴ��� //
			 * if(TrafficGroup.grouptrafficGroupList.get(i).getTrafficList().get(j).
			 * getWorkRoute()!=null) // { //������·�ɵ�WDMLink��Ϊfalse // for(int //
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
			 * traffic.getMustPassNode()); Route route2 = null; // Ϊ�����ظ���Ҫ��route1��Ӧ��·��Ϊ������
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
			 * = null; // Ϊ�����ظ���Ҫ��route1��Ӧ��·��Ϊ������ if (route1 != null) { List<WDMLink> list =
			 * route1.getWDMLinkList(); for (WDMLink wdmLink : list) {
			 * wdmLink.setActive(false); for (FiberLink link : wdmLink.getFiberLinkList()) {
			 * link.setActive(false); } } } route2 =
			 * RouteAlloc.findWDMRouteByTwoNode(traffic, flag,
			 * traffic.getMustPassLink().getToNode(), traffic.getToNode()); //
			 * ������·�ĵ�toNode��fromNode������ȥ���п������route1��route2�Ƿ����Ҫ�ж�һ�� if (route1 != null &&
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

			// ����·��û�п��ò�����Դ����·��Ϊ������
			for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
				if ((WDMLink.WDMLinkList.get(i).getRemainResource() == 0))
					WDMLink.WDMLinkList.get(i).setActive(false);
			}
			Traffic traffic = it.next();
			// ���ر���·��Ϊ������
			if (traffic.getMustAvoidLink() != null) {
				// �ҳ�fiberlink��Ӧ��wdmlink
				WDMLink wdmMustAvoidLink = traffic.getMustAvoidLink().getBelongWDMLink();
				if (wdmMustAvoidLink != null) {
					wdmMustAvoidLink.setActive(false);
				}
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
				// �ҳ�fiberlink��Ӧ��wdmlink
				WDMLink wdmMustPassLink = traffic.getMustPassLink().getBelongWDMLink();
				Route route1 = null;
				route1 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, traffic.getFromNode(),
						wdmMustPassLink.getFromNode());
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
				route2 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, wdmMustPassLink.getToNode(),
						traffic.getToNode());
				// ������·�ĵ�toNode��fromNode������ȥ���п������route1��route2�Ƿ����Ҫ�ж�һ��
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
				// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// �������osnrҪ��
				if (1 > 0) {// Ϊ�˱ܿ�OSNR���� ��3��ʱҪ�ĵ�
					Traffic tra = route.getBelongsTraffic();
					if (tra.isElectricalCrossConnection() == true && tra.getNrate() < 100) {
						if (CommonAlloc.EallocateWaveLength_workandpro(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
							// �˿ڷ���ûд��Ҫ��
							System.out.println("�Ӳ���ҵ��" + traffic.getName() + "����·�ɷ���ɹ�");
							CommonAlloc.fallBuffer.append(
									"ҵ��:" + traffic.getName() + "(" + traffic.getNrate() + ")����·�ɼ���Դ����ɹ���(�Ӳ�������)\r\n");
							traffic.setWorkRoute(route);// ���·�ɲ�Ϊ��������Ϊ����·��
							traffic.setStatus(TrafficStatus.�����ѷ���);
						} else {// ��Դ����ʧ��
							failedTraffic.add(traffic);
							System.out.println("ҵ��" + traffic.getName() + "����·�ɷ���ʧ��");
							traffic.setStatus(TrafficStatus.����);// �����û�о����Ҳ���
							CommonAlloc.fallBuffer.append(
									"ҵ��:" + traffic.getName() + "(" + traffic.getNrate() + ")����·�ɲ�����Դ����ʧ�ܣ�\r\n");
							RouteAlloc.releaseRoute(route);
						}
					} else if (tra.isElectricalCrossConnection() == false || tra.getNrate() == 100) {
						// 100Gҵ��Ҫ�����Ƿ���Ҫ����ת����osnr��������������������㣬��Ҫ�Ӳ�������

						// �ж�route�ɷ�ʹ��һ�²���
						ResourceAlloc.isRouteConsistent(route, traffic, 1, flag, null);

						if (CommonAlloc.allocateWaveLength(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
							System.out.println("ҵ��" + traffic.getName() + "����·�ɷ���ɹ�");
							CommonAlloc.fallBuffer
									.append("ҵ��:" + traffic.getName() + "(" + traffic.getNrate() + ")����·�ɼ���Դ����ɹ���\r\n");
							traffic.setWorkRoute(route);// ���·�ɲ�Ϊ��������Ϊ����·��
							traffic.setStatus(TrafficStatus.�����ѷ���);
						} else {// ��Դ����ʧ��
							failedTraffic.add(traffic);
							System.out.println("ҵ��" + traffic.getName() + "����·�ɷ���ʧ��");
							traffic.setStatus(TrafficStatus.����);// �����û�о����Ҳ���
							CommonAlloc.fallBuffer.append(
									"ҵ��:" + traffic.getName() + "(" + traffic.getNrate() + ")����·�ɲ�����Դ����ʧ�ܣ�\r\n");
							RouteAlloc.releaseRoute(route);
						}
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
				if (traffic.getWorkRoute() == null) {
					traffic.setStatus(TrafficStatus.�滮ҵ������);// �����û�о����Ҳ���
					CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·����·ʧ�ܣ�\r\n");
				}
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
						Traffic tra = route.getBelongsTraffic();
						if (tra.isElectricalCrossConnection() == true && tra.getNrate() != 100) {
							if (CommonAlloc.EallocateWaveLength_workandpro(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
								// �˿ڷ���ûд��Ҫ��
								System.out.println("�Ӳ���ҵ��" + traffic.getName() + "����·�ɷ���ɹ�");
								CommonAlloc.fallBuffer.append("ҵ��:" + traffic.getName() + "(" + traffic.getNrate()
										+ ")����·�ɼ���Դ����ɹ���(�Ӳ�������)\r\n");
								traffic.setProtectRoute(route);// ���·�ɲ�Ϊ��������Ϊ����·��
								traffic.setStatus(TrafficStatus.�����ͱ����ѷ���);
							} else {// ��Դ����ʧ��
								failedTraffic.add(traffic);
								System.out.println("ҵ��" + traffic.getName() + "����·�ɷ���ʧ��");
								traffic.setStatus(TrafficStatus.����);// �����û�о����Ҳ���
								CommonAlloc.fallBuffer.append(
										"ҵ��:" + traffic.getName() + "(" + traffic.getNrate() + ")����·�ɲ�����Դ����ʧ�ܣ�\r\n");
								// RouteAlloc.releaseRoute(route);
							}
						} else if (tra.isElectricalCrossConnection() == false || tra.getNrate() == 100) {
							// 100Gҵ��Ҫ�����Ƿ���Ҫ����ת����osnr��������������������㣬��Ҫ�Ӳ�������

							// �ж�route�ɷ�ʹ��һ�²���
							LinkedList<Integer> startWaveLength = new LinkedList<Integer>();
							boolean isConsistent = CommonAlloc.findWaveLength(route, 0, startWaveLength,
									route.getWDMLinkList(), true);
							// ���������һ�£��Ͱ�������·��
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
									// �ҳ��������ٵģ�����Դ�������ٵ�
									consistentRoute = bestRoute(routeList);
								}
							}
							if (consistentRoute != null && consistentRoute.getWDMLinkList().size() != 0) {
								route = consistentRoute;
							}

							if (CommonAlloc.allocateWaveLength1(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
								System.out.println("ҵ��" + traffic.getName() + "����·����Դ����ɹ�");
								CommonAlloc.fallBuffer.append(
										"ҵ��:" + traffic.getName() + "(" + traffic.getNrate() + ")����·�ɼ���Դ����ɹ���\r\n");
								traffic.setProtectRoute(route);// ���·�ɲ�Ϊ��������Ϊ����·��
								traffic.setStatus(TrafficStatus.�����ͱ����ѷ���);
							} else {// ��Դ����ʧ��
								failedTraffic.add(traffic);
								System.out.println("ҵ��" + traffic.getName() + "����·����Դ����ʧ��");
								traffic.setStatus(TrafficStatus.����);// �����û�о����Ҳ���
								CommonAlloc.fallBuffer.append(
										"ҵ��:" + traffic.getName() + "(" + traffic.getNrate() + ")����·�ɲ�����Դ����ʧ�ܣ�\r\n");
								// RouteAlloc.releaseRoute(route);
							}
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
					CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·����·ʧ�ܣ�\r\n");
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
	 * @param tra     ������ҵ��
	 * @param flag    flag=0 ����;=1 ���� ; =2 ���ؾ����㷨
	 * @param rop     �ָ�·�ɻ��ǻָ�·�ɵı���·�� ,0:�ָ���1���ָ�����
	 * @param suggest 0:���ٷ����� 1:�滮������
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
				// RouteAlloc.releaseRoute(route);
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
			// RouteAlloc.releaseRoute(route);
		}

		// ResourceLogTxt.ResourceLogTxt();// �����Դ������־.
		// CommonAlloc.fallBuffer.delete(0, CommonAlloc.fallBuffer.length());
		// trafficList.clear();
	}

	/**
	 * ������һ��ҵ�����·�ɵļ��㣬��Ҫ���� ����·�ɡ�����·�ɡ�Ԥ�ûָ�·��
	 * 
	 * @param tra  ������ҵ��
	 * @param flag flag=0 ����;=1 ���� ; =2 ���ؾ����㷨
	 * @param rop  �ָ�·�ɻ��ǻָ�·�ɵı���·�� ,0:�ָ���1���ָ�����
	 */
	public static void allocateOneTraffic(Traffic tra, int flag, int rop) {

	}

	/**
	 * ��̬��·�ɻָ�ʱ���� ���ԣ�1�����Զϵ���· 2���ж������·���Ƿ��п��ò���wave 3.1�����������Դ 3.2��û���������ҵ��������·��������Դ
	 */
	// ��·�������·��δ������Դ
	public static Route reRoute(Route oldroute, CommonNode from, CommonNode to, int flag) {

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
			if (oldroute.getWDMLinkList().get(i).getParallelLinkList().size() > 1) {
				for (int j = 0; j < oldroute.getWDMLinkList().get(i).getParallelLinkList().size(); j++) {
					oldroute.getWDMLinkList().get(i).getParallelLinkList().get(j).setActive(false);
				}
			}
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
		// Route route2 = null; // ��ҵ�����ĩ�ڵ���·�����ã����route1��Դ���䲻�ɹ�����route2
		// ��ʱ���ص�routeֻ����returnnodelist��returnlinklist�������ԣ��ȸ�route������Щ�ڵ����·
		route1 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, from, to);
		// route2 = RouteAlloc.findWDMRoute(traffic, flag);

		// ��·��ɺ�Ϳ��Խ����е���·��������
		// ��������·��Ϊ����
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink.WDMLinkList.get(i).setActive(true);
		}

		if (route1 != null && route1.getWDMLinkList().size() != 0) {
			return route1;
		}
		return null;

		// if (route1 != null && route1.getWDMLinkList().size() != 0) {
		// // ��ʼ�ж����route���Ƿ��п��ò�����Դ
		// if (Route.isRouteHaveWave(route1, wave, traffic)) {//
		// true���У���Ϊ��route���䲨����Դwave
		// for (int hop = 0; hop < route1.getWDMLinkList().size(); hop++) {//
		// Ϊÿһ��wdmLink��·������������
		// WDMLink wdmLink = route1.getWDMLinkList().get(hop);
		// {
		// if (wdmLink.getWaveLengthList().get(wave).getStatus().equals(Status.����)) {
		// wdmLink.setRemainResource(wdmLink.getRemainResource() - 1);
		// } // ������·ʣ����Դ
		// wdmLink.getWaveLengthList().get(wave).setStatus(Status.�ָ�);// �ı乤��״̬
		// wdmLink.getWaveLengthList().get(wave).getDynamicTrafficList().add(traffic);//
		// ����ò����Ķ�̬ҵ���
		// wdmLink.getCarriedTrafficList().add(traffic);// Ϊ��·��ӳ���ҵ��
		// }
		// }
		// return route1;
		// } // �����������
		// // ���waveû����Դ��������ҵ����ĩ�ڵ������route2������Դ����
		// if (!Route.isRouteHaveWave(route1, wave, traffic)) {
		// if (route2 != null && route2.getWDMLinkList().size() != 0) {
		// // if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {//
		// �������osnrҪ��
		// // �ܿ�OSNR
		// if (1 > 0) {
		// if (CommonAlloc.allocateWaveLength3(route2) &&
		// PortAlloc.allocatePort(route2)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
		// // CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "Ԥ��·�ɼ���Դ����ɹ���\r\n");
		// traffic.setDynamicRoute(route2);// ���·�ɲ�Ϊ��������Ԥ����·��
		// traffic.setStatus(TrafficStatus.��̬��·���ѷ���);
		// return route2;
		// } else {// ��Դ����ʧ��
		//
		// // CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "Ԥ��·�ɲ�����˿���Դ����ʧ�ܣ�\r\n");
		// RouteAlloc.releaseRoute(route2);
		// }
		// }
		// // else {// ���osnr��������
		// // if (Suggest.isSuggested == true) {// 2017.10.14
		// // OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
		// // OSNR.OSNRRouteList.add(route);//10.19
		// // }
		// // CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·��OSNR������������\r\n");
		// // }
		// } else {
		// // ��·ʧ��
		// // CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "���ڲ�����Դ����Ԥ��·����·ʧ�ܣ�\r\n");
		// }
		// }
		// }
		// else if (route2 != null && route2.getWDMLinkList().size() != 0) {
		// // if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {//
		// �������osnrҪ��
		// // �ܿ�OSNR
		// if (1 > 0) {
		// if (CommonAlloc.allocateWaveLength3(route2) &&
		// PortAlloc.allocatePort(route2)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
		// // CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "Ԥ��·�ɼ���Դ����ɹ���\r\n");
		// traffic.setDynamicRoute(route2);// ���·�ɲ�Ϊ��������Ԥ����·��
		// traffic.setStatus(TrafficStatus.��̬��·���ѷ���);
		// return route2;
		// } else {// ��Դ����ʧ��
		//
		// // CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "Ԥ��·�ɲ�����˿���Դ����ʧ�ܣ�\r\n");
		// RouteAlloc.releaseRoute(route2);
		// }
		// }
		// // else {// ���osnr��������
		// // if (Suggest.isSuggested == true) {// 2017.10.14
		// // OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11
		// // OSNR.OSNRRouteList.add(route);//10.19
		// // }
		// // CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·��OSNR������������\r\n");
		// // }
		// }

	}
	/*
	 * // �ڵ�� public static Route reRoute1(Route oldroute, CommonNode from,
	 * CommonNode to, int wave1, int wave2, int flag) {
	 * 
	 * Traffic traffic = oldroute.getBelongsTraffic(); List<WDMLink> workLinkList =
	 * traffic.getWorkRoute().getWDMLinkList();
	 * 
	 * // ����·��û�п��ò�����Դ����·��Ϊ������ for (int i = 0; i < WDMLink.WDMLinkList.size(); i++)
	 * { if ((WDMLink.WDMLinkList.get(i).getRemainResource() == 0))
	 * WDMLink.WDMLinkList.get(i).setActive(false); } // ��ԭ·����Ϊ������ for (int i = 0; i
	 * < oldroute.getWDMLinkList().size(); i++) {
	 * oldroute.getWDMLinkList().get(i).setActive(false); }
	 * 
	 * // ���ù���·�ɲ����������㶯̬�ָ�·�� for (WDMLink wdmLink : workLinkList) {
	 * wdmLink.setActive(false); for (FiberLink link : wdmLink.getFiberLinkList()) {
	 * link.setActive(false); } // ����srlg,�����������һ����־λ��ѡ���Ƿ���srlg if (LinkRGroup.srlg
	 * == true) { for (LinkRGroup group : wdmLink.getWdmRelatedList()) { for
	 * (WDMLink wLink : group.getSRLGWDMLinkList()) { wLink.setActive(false); } } }
	 * } // �������·�ɴ��ڣ��򽫱���·����Ϊ������ if (traffic.getProtectRoute() != null) {
	 * List<WDMLink> protectLinkList = traffic.getProtectRoute().getWDMLinkList();
	 * for (WDMLink wdmLink : protectLinkList) { wdmLink.setActive(false); for
	 * (FiberLink link : wdmLink.getFiberLinkList()) {// ���ñ���·�ɲ����������㶯̬·��
	 * link.setActive(false); } // ����srlg,�����������һ����־λ��ѡ���Ƿ���srlg if (LinkRGroup.srlg
	 * == true) { for (LinkRGroup group : wdmLink.getWdmRelatedList()) { for
	 * (WDMLink wLink : group.getSRLGWDMLinkList()) { wLink.setActive(false); } } }
	 * } }
	 * 
	 * // �����ҵ���й���ҵ���飬��������Ϊ�������ָ�·�ɾ����룬���������ҵ�����ҵ��Ļָ�·����Ϊfalse if
	 * (traffic.getTrafficgroup() != null &&
	 * traffic.getTrafficgroup().getType().equals("�������ָ�·�ɾ�����"))// ���ҵ��Ĺ���ҵ���鲻Ϊ�� {
	 * for (int i = 0; i < Traffic.trafficList.size(); i++) { Traffic tra =
	 * Traffic.trafficList.get(i); // �ж��Ƿ���һ�����չ���ҵ���鲢�Ҳ���ͬһ��ҵ�� if
	 * (tra.getTrafficgroup() != null && tra.getTrafficgroup().getId() ==
	 * traffic.getTrafficgroup().getId() && tra.getRankId() != traffic.getRankId())
	 * { if (tra.getDynamicRoute() != null) {// ����������ҵ��̬�ָ�·�ɴ��� for (int j = 0; j <
	 * tra.getDynamicRoute().getWDMLinkList().size(); j++)
	 * tra.getDynamicRoute().getWDMLinkList().get(j).setActive(false); } } } }
	 * 
	 * // ��ʼ��· Route route1 = null; // �ԶϵĽڵ���· Route route2 = null; //
	 * ��ҵ�����ĩ�ڵ���·�����ã����route1��Դ���䲻�ɹ�����route2 //
	 * ��ʱ���ص�routeֻ����returnnodelist��returnlinklist�������ԣ��ȸ�route������Щ�ڵ����· route1 =
	 * RouteAlloc.findWDMRouteByTwoNode(traffic, flag, from, to); route2 =
	 * RouteAlloc.findWDMRoute(traffic, flag);
	 * 
	 * // ��·��ɺ�Ϳ��Խ����е���·�������� // ��������·��Ϊ���� for (int i = 0; i <
	 * WDMLink.WDMLinkList.size(); i++) {
	 * WDMLink.WDMLinkList.get(i).setActive(true); }
	 * 
	 * if (route1 != null && route1.getWDMLinkList().size() != 0) { //
	 * ��ʼ�ж����route���Ƿ��п��ò�����Դ if (Route.isRouteHaveWave(route1, wave1, traffic)) {//
	 * true���У���Ϊ��route���䲨����Դwave-- for (int hop = 0; hop <
	 * route1.getWDMLinkList().size(); hop++) {// Ϊÿһ��wdmLink��·������������ WDMLink
	 * wdmLink = route1.getWDMLinkList().get(hop); { if
	 * (wdmLink.getWaveLengthList().get(wave1).getStatus().equals(Status.����)) {
	 * wdmLink.setRemainResource(wdmLink.getRemainResource() - 1); } // ������·ʣ����Դ
	 * wdmLink.getWaveLengthList().get(wave1).setStatus(Status.�ָ�);// �ı乤��״̬
	 * wdmLink.getWaveLengthList().get(wave1).getDynamicTrafficList().add(traffic);/
	 * / ����ò����Ķ�̬ҵ��� wdmLink.getCarriedTrafficList().add(traffic);// Ϊ��·��ӳ���ҵ�� } }
	 * return route1; } // ����������� if (Route.isRouteHaveWave(route1, wave2, traffic))
	 * {// true���У���Ϊ��route���䲨����Դwave for (int hop = 0; hop <
	 * route1.getWDMLinkList().size(); hop++) {// Ϊÿһ��wdmLink��·������������ WDMLink
	 * wdmLink = route1.getWDMLinkList().get(hop); { if
	 * (wdmLink.getWaveLengthList().get(wave2).getStatus().equals(Status.����)) {
	 * wdmLink.setRemainResource(wdmLink.getRemainResource() - 1); } // ������·ʣ����Դ
	 * wdmLink.getWaveLengthList().get(wave2).setStatus(Status.�ָ�);// �ı乤��״̬
	 * wdmLink.getWaveLengthList().get(wave2).getDynamicTrafficList().add(traffic);/
	 * / ����ò����Ķ�̬ҵ��� wdmLink.getCarriedTrafficList().add(traffic);// Ϊ��·��ӳ���ҵ�� } }
	 * return route1; } // ����������� // ���wave1��wave2��û����Դ��������ҵ����ĩ�ڵ������route2������Դ���� if
	 * (Route.isRouteHaveWave(route1, wave1, traffic) == false &&
	 * Route.isRouteHaveWave(route1, wave2, traffic) == false) { if (route2 != null
	 * && route2.getWDMLinkList().size() != 0) { // if (osnr.calculateOSNR(route) >=
	 * Dlg_PolicySetting.osnRGate) {// �������osnrҪ�� // �ܿ�OSNR if (1 > 0) { if
	 * (CommonAlloc.allocateWaveLength3(route2) && PortAlloc.allocatePort(route2))
	 * {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ� // CommonAlloc.fallBuffer.append("ҵ��:" + traffic +
	 * "Ԥ��·�ɼ���Դ����ɹ���\r\n"); traffic.setDynamicRoute(route2);// ���·�ɲ�Ϊ��������Ԥ����·��
	 * traffic.setStatus(TrafficStatus.��̬��·���ѷ���); return route2; } else {// ��Դ����ʧ��
	 * 
	 * // CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "Ԥ��·�ɲ�����˿���Դ����ʧ�ܣ�\r\n");
	 * RouteAlloc.releaseRoute(route2); } } // else {// ���osnr�������� // if
	 * (Suggest.isSuggested == true) {// 2017.10.14 //
	 * OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11 //
	 * OSNR.OSNRRouteList.add(route);//10.19 // } //
	 * CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·��OSNR������������\r\n"); // } }
	 * else { // ��·ʧ�� // CommonAlloc.fallBuffer.append("ҵ��:" + traffic +
	 * "���ڲ�����Դ����Ԥ��·����·ʧ�ܣ�\r\n"); } } } else if (route2 != null &&
	 * route2.getWDMLinkList().size() != 0) { // if (osnr.calculateOSNR(route) >=
	 * Dlg_PolicySetting.osnRGate) {// �������osnrҪ�� // �ܿ�OSNR if (1 > 0) { if
	 * (CommonAlloc.allocateWaveLength3(route2) && PortAlloc.allocatePort(route2))
	 * {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ� // CommonAlloc.fallBuffer.append("ҵ��:" + traffic +
	 * "Ԥ��·�ɼ���Դ����ɹ���\r\n"); traffic.setDynamicRoute(route2);// ���·�ɲ�Ϊ��������Ԥ����·��
	 * traffic.setStatus(TrafficStatus.��̬��·���ѷ���); return route2; } else {// ��Դ����ʧ��
	 * 
	 * // CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "Ԥ��·�ɲ�����˿���Դ����ʧ�ܣ�\r\n");
	 * RouteAlloc.releaseRoute(route2); } } // else {// ���osnr�������� // if
	 * (Suggest.isSuggested == true) {// 2017.10.14 //
	 * OSNR.allOSNRList.add(OSNR.select(route));// wb 2017.10.11 //
	 * OSNR.OSNRRouteList.add(route);//10.19 // } //
	 * CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·��OSNR������������\r\n"); // } }
	 * return null; }
	 */
	// �ж�·���Ƿ��п��ò���wave����Route.isRouteHaveWave(route, wave, traffic)

	// ��̬��·��ʱ���жϸ�·���в���waveΪ����䲨��wave
	public static Route allocWaveForRoute(Route route, int wave, Traffic traffic) {
		for (int hop = 0; hop < route.getWDMLinkList().size(); hop++) {// Ϊÿһ��wdmLink��·������������
			WDMLink wdmLink = route.getWDMLinkList().get(hop);
			{
				// ��Ҫע����õ�wave�ǲ�����1-80��get��wave���Ǵ�0-79�����Ե�ȡ���Ĳ���Ϊwaveʱ��ӦΪwave-1
				if (wdmLink.getWaveLengthList().get(wave - 1).getStatus().equals(Status.����)) {
					wdmLink.setRemainResource(wdmLink.getRemainResource() - 1);
				} // ������·ʣ����Դ
				wdmLink.getWaveLengthList().get(wave - 1).setStatus(Status.����);// �ı乤��״̬
				wdmLink.getWaveLengthList().get(wave - 1).getDynamicTrafficList().add(traffic);// ����ò����Ķ�̬ҵ���
				// wdmLink.getCarriedTrafficList().add(traffic);// Ϊ��·��ӳ���ҵ��
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

	// ��̬��·�ɣ� 3.2��û���������ҵ��������·��������Դ
	public static Route reRouteForTraffic(Route oldroute, int flag) {

		Traffic traffic = oldroute.getBelongsTraffic();
		List<WDMLink> workLinkList = traffic.getWorkRoute().getWDMLinkList();

		WDMLink.updateAllLinkResource();

		// ����·��û�п��ò�����Դ����·��Ϊ������
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			if ((WDMLink.WDMLinkList.get(i).getRemainResource() == 0))
				WDMLink.WDMLinkList.get(i).setActive(false);
		}

		// ���ر���·��Ϊ������
		if (traffic.getMustAvoidLink() != null) {
			// �ҳ�fiberlink��Ӧ��wdmlink
			WDMLink wdmMustAvoidLink = traffic.getMustAvoidLink().getBelongWDMLink();
			if (wdmMustAvoidLink != null) {
				wdmMustAvoidLink.setActive(false);
			}
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

		// // ��ԭ·����Ϊ������
		// for (int i = 0; i < oldroute.getWDMLinkList().size(); i++) {
		// oldroute.getWDMLinkList().get(i).setActive(false);
		// if (oldroute.getWDMLinkList().get(i).getParallelLinkList().size() > 1) {
		// for (int j = 0; j <
		// oldroute.getWDMLinkList().get(i).getParallelLinkList().size(); j++) {
		// oldroute.getWDMLinkList().get(i).getParallelLinkList().get(j).setActive(false);
		// }
		// }
		// }

		// ���ù���·�ɲ����������㶯̬�ָ�·��(����̬·��ʱ������Ҫ��ԭ���Ĺ���·����Ϊ���������㱣��·�ɣ���ֻ�ǽ�������·��Ϊ����������ſ��Ծ�������ԭ·��)
		// for (WDMLink wdmLink : workLinkList) {
		// wdmLink.setActive(false);
		// for (FiberLink link : wdmLink.getFiberLinkList()) {
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
		ResourceAlloc.setRelatedTrafficUnactive(traffic, DataSave.separate);

		Route route = null;
		route = RouteAlloc.findWDMRoute(traffic, flag);

		// ��������·��Ϊ����
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink.WDMLinkList.get(i).setActive(true);
		}

		if (route != null && route.getWDMLinkList().size() != 0) {
			// if (osnr.calculateOSNR(route) >= Dlg_PolicySetting.osnRGate) {// �������osnrҪ��
			// �ܿ�OSNR
			// // ����һ����·
			// ResourceAlloc.isRouteConsistent(route, traffic, 2, flag,null);
			if (1 > 0) {
				if (CommonAlloc.allocateWaveLength3(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
					// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "Ԥ��·�ɼ���Դ����ɹ���\r\n");
					traffic.setDynamicRoute(route);// ���·�ɲ�Ϊ��������Ԥ����·��
					traffic.setStatus(TrafficStatus.��̬��·���ѷ���);
					return route;
				}
				// else {// ��Դ����ʧ��
				//
				// // CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "Ԥ��·�ɲ�����˿���Դ����ʧ�ܣ�\r\n");
				// RouteAlloc.releaseRoute(route);
				// }
			}
		}
		return null;
	}

	/*
	 * ���ݲ���ƽ�����·�ɼ��� ���룺1��treҵ�� 2��flag1·�ɵȼ���1������/��������2��Ԥ�ã� 3���������wave 4��flag2·�ɲ���
	 */
	public static Route allocateResourceByWave(Traffic traffic, int flag1, int waveNum, int flag2) {
		// ���������false��link����������ЩlinkҪ���¼���
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

		// ���ر���·���ڵ���Ϊ������
		ResourceAlloc.setMustAvoidUnactive(traffic);

		// ���ù�����·������
		for (int j = 0; j < WDMLink.WDMLinkList.size(); j++) {
			if (!WDMLink.WDMLinkList.get(j).isBroken()) {
				WDMLink.WDMLinkList.get(j).setActive(false);
			}
		}

		// ����ҵ�����������ò��������������Ӧ��SRLGΪ�����
		ResourceAlloc.setRelatedTrafficUnactive(traffic, DataSave.separate);

		// ����Դ�������·��Ϊ������
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink link = WDMLink.WDMLinkList.get(i);
			if (link.getRemainResource() == 0) {
				link.setActive(false);
			}
		}

		// ���ñ���·�ɲ����������㶯̬·��
		if (traffic.getProtectRoute() != null) {
			ResourceAlloc.setRouteLinkUnactive(traffic.getProtectRoute());
		}

		Route route = RouteAlloc.findWDMRoute(traffic, flag2);

		// Ѱ�Ҹ���·
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
					// ��������·�ɣ�������·�ɵ���Դ������·��С��ԭ·�ɣ�������·��ȡ����·��
					if (newRoute != null && newRoute.getWDMLinkList().size() != 0
							&& newRoute.resoureLessLink(newRoute, 40).size() < route.resoureLessLink(route, 40).size()
							&& (newRoute.getWDMLinkList().size() - route.getWDMLinkList().size() < 3))
						route = newRoute;
				}
			}
		}
		// �ж�route�ɷ�ʹ��һ�²���
		LinkedList<Integer> startWaveLength = new LinkedList<Integer>();
		boolean isConsistent = false;
		if (route != null) {
			isConsistent = CommonAlloc.findWaveLength1(route, 0, startWaveLength, route.getWDMLinkList(), true,
					traffic);
		}
		// boolean isConsistent = false;
		// ���������һ�£��Ͱ�������·��
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
				// �ҳ��������ٵģ�����Դ�������ٵ�
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
			if (CommonAlloc.allocateWaveLength3(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
				// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "Ԥ��·�ɼ���Դ����ɹ���\r\n");
				traffic.setDynamicRoute(route);// ���·�ɲ�Ϊ��������Ԥ����·��
				traffic.setStatus(TrafficStatus.��̬��·���ѷ���);
				return route;
			}
		}
		return route;
	}

	// �ҳ��������ٲ��Ҳ�����Դ������·���ٵ�route
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
		// �ҳ�routeList1�У�������Դ������·���ٵ�route
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
		// �ҳ�routeList2�У���ƽ�б�����route
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

	// ����ҵ�����������ò��������������Ӧ��SRLGΪ����� flag 1:��·���� 2����·���ڵ�
	public static LinkedList<WDMLink> setRelatedTrafficUnactive(Traffic traffic, int flag) {
		LinkedList<WDMLink> list = new LinkedList<>();
		if (traffic.getTrafficgroup() != null)// ���ҵ��Ĺ���ҵ���鲻Ϊ��
		{
			for (int i = 0; i < Traffic.trafficList.size(); i++) {
				Traffic tra = Traffic.trafficList.get(i);
				// �ж��Ƿ���һ�����չ���ҵ���鲢�Ҳ���ͬһ��ҵ��
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
					if (tra.getWorkRoute() != null) {// ����������ҵ����·�ɴ���
						for (int j = 0; j < tra.getWorkRoute().getWDMLinkList().size(); j++) {
							WDMLink link = tra.getWorkRoute().getWDMLinkList().get(j);
							if (link.isActive()) {
								list.add(link);
							}
							// ����ҵ���鲻ʹ��ͬһ����·
							link.setActive(false);

							// ����ҵ���鲻ʹ��ͬһ��SRLG���е���·
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

	// �ر���·���ڵ���Ϊ������
	public static void setMustAvoidUnactive(Traffic traffic) {

		LinkedList<WDMLink> canBreakActive = new LinkedList<>();
		// ��ȫ������
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink link = WDMLink.WDMLinkList.get(i);
			link.setActive(true);

		}
		for (int i = 0; i < CommonNode.allNodeList.size(); i++) {
			CommonNode.allNodeList.get(i).setActive(true);
		}
		if (traffic.getMustAvoidLink() != null) {
			// �ҳ�fiberlink��Ӧ��wdmlink
			WDMLink wdmMustAvoidLink = traffic.getMustAvoidLink().getBelongWDMLink();
			if (wdmMustAvoidLink != null) {
				wdmMustAvoidLink.setActive(false);
			}
		}
		// ����ܱܽڵ���������·��Ϊ������
		if (traffic.getMustAvoidNode() != null) {
			CommonNode mANode = traffic.getMustAvoidNode();// �رܽڵ�
			mANode.setActive(false);

			// CommonNode mANode = traffic.getMustAvoidNode();// �رܽڵ�
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
				if (!pre) {// ����Ԥ��
					if (link.getRemainResource() <= 10 + DataSave.locknum) {
						link.setActive(false);
						list.add(link);
					}
				} else {// Ԥ��
					if (link.getRemainResource() <= DataSave.locknum) {
						link.setActive(false);
						list.add(link);
					}
				}
			}
		}
		return list;
	}

	// �ؾ��ڵ㡢�ؾ���·,��·
	public static Route routeForTraffic(Traffic traffic, int flag) {
		Route route = null;
		if (traffic.getMustPassLink() == null && traffic.getMustPassNode() == null) {
			// �й���ҵ�����ҵ���·�ɣ�Ӧ�þ��������������ҵ�����ĩ�ڵ�
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
			// Ϊ�����ظ���Ҫ��route1��Ӧ��·��Ϊ������,�����Ľڵ�ҲҪ��Ϊ������
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
				// �ڵ�
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
			// �ҳ�fiberlink��Ӧ��wdmlink
			WDMLink wdmMustPassLink = traffic.getMustPassLink().getBelongWDMLink();
			Route route1 = null;
			route1 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, traffic.getFromNode(),
					wdmMustPassLink.getFromNode());
			Route route2 = null;
			// Ϊ�����ظ���Ҫ��route1��Ӧ��·��Ϊ������
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
				// �ڵ�
				for (CommonNode node : nodelist) {
					node.setActive(false);
				}
			}
			route2 = RouteAlloc.findWDMRouteByTwoNode(traffic, flag, wdmMustPassLink.getToNode(), traffic.getToNode());
			// ������·�ĵ�toNode��fromNode������ȥ���п������route1��route2�Ƿ����Ҫ�ж�һ��
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
		// Ѱ�Ҹ���·
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

	// �ж�route�Ƿ����ʹ�ò���һ��
	public static boolean isRouteConsist(Route route, Traffic traffic, int flag) {
		// �ж�route�ɷ�ʹ��һ�²���
		LinkedList<Integer> startWaveLength = new LinkedList<Integer>();
		boolean isConsistent = false;
		if (route != null) {
			if (flag == 2) {// Ԥ��
				isConsistent = CommonAlloc.findWaveLength1(route, 0, startWaveLength, route.getWDMLinkList(), true,
						traffic);
			} else {
				isConsistent = CommonAlloc.findWaveLength(route, 0, startWaveLength, route.getWDMLinkList(), true);
			}
		}
		return isConsistent;
	}

	// �ж�route�Ƿ����ʹ�ò���һ���� flag1:1����/����/ 2Ԥ�� flag2��·�ɲ���
	public static void isRouteConsistent(Route route, Traffic traffic, int flag1, int flag2, LinkedList<WDMLink> list) {

		// �ж�route�ɷ�ʹ��һ�²���
		LinkedList<Integer> startWaveLength = new LinkedList<Integer>();
		boolean isConsistent = false;
		if (route != null) {
			if (flag1 == 2) {// Ԥ��
				isConsistent = CommonAlloc.findWaveLength1(route, 0, startWaveLength, route.getWDMLinkList(), true,
						traffic);
			} else {
				isConsistent = CommonAlloc.findWaveLength(route, 0, startWaveLength, route.getWDMLinkList(), true);
			}
		}
		// boolean isConsistent = false;
		// ���������һ�£��Ͱ�������·��
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
				// �ҳ��������ٵģ�����Դ�������ٵ�;����OSNR
				consistentRoute = bestRoute(routeList);
			}
		}
		if (consistentRoute != null && consistentRoute.getWDMLinkList().size() != 0) {
			route = consistentRoute;
		}
		// ����ڲ���ƽ�����Ҳ���·�ɣ����Խ���Դ������·�⿪����
		if (consistentRoute == null) {
			// ����
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
					// �ҳ��������ٵģ�����Դ�������ٵ�
					consistentRoute = bestRoute(routeList);
				}
			}
			// ���²�����
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

	// ����ĳ��route������linkΪ���������SRLG��·
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

			// SRLG�費����
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

					// ��������·�ɣ�������·�ɵ���Դ������·��С��ԭ·�ɣ�������·��ȡ����·��
					if (newRoute != null && newRoute.getWDMLinkList().size() != 0
							&& newRoute.resoureLessLink(newRoute, 40).size() < route.resoureLessLink(route, 40).size()
							&& (newRoute.getWDMLinkList().size() - route.getWDMLinkList().size() < 3))
						route = newRoute;
				}
			}
		}
	}

	// ����SRLG��Link��Ϊfalse
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
		// ����ǹ���ҵ���飬��Ӧ������ʹ��û��SRLG��route
		if (route != null && route.getWDMLinkList().size() != 0 && traffic.getTrafficgroup() != null) {
			// ����ǹ���ҵ����ĵ�һ��ҵ��ʱ��Ӧ�þ�������ʹ��SRLG��·
			// һ����Ҫ���¼���
			LinkedList<WDMLink> setFalse = new LinkedList<>();

			// ����бرܽڵ���߱ر���·���ȼ���
			// ���ر���·��Ϊ����
			if (traffic.getMustAvoidLink() != null) {
				// �ҳ�fiberlink��Ӧ��wdmlink
				WDMLink wdmMustAvoidLink = traffic.getMustAvoidLink().getBelongWDMLink();
				if (wdmMustAvoidLink != null) {
					wdmMustAvoidLink.setActive(true);
				}
			}
			// ����ܱܽڵ���������·��Ϊ����
			if (traffic.getMustAvoidNode() != null) {
				CommonNode mANode = traffic.getMustAvoidNode();// �رܽڵ�
				mANode.setActive(true);
			}

			// ��route����SRLG��Ϊ������
			for (int i = 0; i < route.getWDMLinkList().size(); i++) {
				WDMLink link = route.getWDMLinkList().get(i);
				link.setActive(false);
				setFalse.add(link);
				// SRLG�費����
				if (link.belongSRLG() != null) {
					for (int j = 0; j < link.belongSRLG().getSRLGWDMLinkList().size(); j++) {
						WDMLink srlgLink = link.belongSRLG().getSRLGWDMLinkList().get(j);
						srlgLink.setActive(false);
						setFalse.add(srlgLink);
					}
				}
			}

			if (traffic.getTrafficgroup() != null)// ���ҵ��Ĺ���ҵ���鲻Ϊ��
			{
				for (int i = 0; i < Traffic.trafficList.size(); i++) {
					Traffic tra = Traffic.trafficList.get(i);
					// �ж��Ƿ���һ�����չ���ҵ���鲢�Ҳ���ͬһ��ҵ��
					if (tra.getTrafficgroup() != null
							&& tra.getTrafficgroup().getId() == traffic.getTrafficgroup().getId()
							&& tra.getRankId() != traffic.getRankId() && (!tra.getTrafficgroup().getBelongGroup()
									.equals(traffic.getTrafficgroup().getBelongGroup()))) {
						if (tra.getWorkRoute() == null) {// ����������ҵ����·�ɻ�δ����
							Route relatedRoute = RouteAlloc.findWDMRoute(tra, flag);
							// �������ҵ����·��ʧ��,����Ҫ��������route,routeӦ����ʹ����SRLG��·
							if (relatedRoute == null || relatedRoute.getWDMLinkList().size() == 0) {
								succ = false;
								// �Ƚ���Ϊ�������������Ϊ�����Ϊ�����������Ϊ������
								for (int j = 0; j < setFalse.size(); j++) {
									setFalse.get(j).setActive(true);
								}
								// ���ر���·��Ϊ������
								if (traffic.getMustAvoidLink() != null) {
									// �ҳ�fiberlink��Ӧ��wdmlink
									WDMLink wdmMustAvoidLink = traffic.getMustAvoidLink().getBelongWDMLink();
									if (wdmMustAvoidLink != null) {
										wdmMustAvoidLink.setActive(false);
									}
								}
								// ����ܱܽڵ���������·��Ϊ������
								if (traffic.getMustAvoidNode() != null) {
									CommonNode mANode = traffic.getMustAvoidNode();// �رܽڵ�
									mANode.setActive(false);
								}

								// һ����Ҫ���¼���
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
								// Ѱ�Ҹ���·

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
										// ��������·�ɣ�������·�ɵ���Դ������·��С��ԭ·�ɣ�������·��ȡ����·��
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

	// ����ROADM�ڵ㣬��������ȫ·��ʱ��Ҫ�õĵ�dict
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
			// �ҵ�crt�������ڽӽڵ�
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
				// �����������������̵�
				// if(distance.containsKey(next) && distance.get(crt) == distance.get(next)+1) {
				// �����������
				if (distance.containsKey(next) && !path.contains(next)) {
					dfs(ladders, path, next, start, distance, map);
				}
			}
		}
		path.remove(path.size() - 1);
	}

	// flag2:1����������2Ԥ��
	public static Route findMeetOSNRroute(Route route, Traffic traffic, int flag, int flag2) {
		// if (traffic.getTrafficgroup() == null ||
		// traffic.getTrafficgroup().getBelongGroup().equals("B")) {
		Route onlymeetOSNR = null;
		// �ж�route�Ƿ�����OSNR
		if (route == null || route.meetOSNR() == false) {
			HashSet<CommonNode> dict = setdict();
			List<List<CommonNode>> ladders = findAllRoute(traffic.getFromNode(), traffic.getToNode(), dict);
			List<Route> routelist = Route.fromNodeToRoute(ladders, traffic);
			if (flag == 0) {// ��С������·�ɰ���С��������
				Route.sortHopMethod(routelist);
			} else {// ��̣���·�ɰ���������,Ĭ�ϰ���̳���
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
//			// �й���ҵ�����ͬһ��ҵ��Ӧ��ʹ����ͬ��·��
//			// �ж�route�Ƿ�����OSNR
//			if (route == null || route.meetOSNR() == false) {
//				HashSet<CommonNode> dict = setdict();
//				List<List<CommonNode>> ladders = findAllRoute(traffic.getFromNode(), traffic.getToNode(), dict);
//				List<Route> routelist = Route.fromNodeToRoute(ladders, traffic);
//				if (flag == 0) {// ��С������·�ɰ���С��������
//					Route.sortHopMethod(routelist);
//				} else {// ��̣���·�ɰ���������,Ĭ�ϰ���̳���
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

	// �������·�ɣ�Ȼ��ѡ��һ����ԽϺõ�·��
	public static void findbetterRouteFromK(int policy, int routeType, Traffic traffic, int K) {
		HashSet<CommonNode> dict = setdict();
		List<List<CommonNode>> ladders = findAllRoute(traffic.getFromNode(), traffic.getToNode(), dict);
		List<Route> routelist = Route.fromNodeToRoute(ladders, traffic);
		if (policy == 0) {// ��̣���·�ɰ���������
			if (traffic.getTrafficgroup() != null) {
				// ����ҵ����İ���С����
				Route.sortHopMethod(routelist);
			} else {
				Route.sortLengthMethod(routelist);
			}
		} else {//// 1��С������·�ɰ���С��������,Ĭ�ϰ���С��
			Route.sortHopMethod(routelist);
		}
//		//�����OSNRҪ�Ƴ�������OSNR������
//		if(DataSave.OSNR) {
//			for(int i=0;i<routelist.size();i++) {
//				Route route = routelist.get(i);
//				//������OSNR���Ƴ�
//				if(!route.meetOSNR()) {
//					routelist.remove(i);
//				}
//			}
//		}
//		//�Ƴ������㲨��һ���Ե�
//		List<Route> copylist = routelist;
//		for(int i=0;i<routelist.size();i++) {
//			Route route = routelist.get(i);
//			//������OSNR���Ƴ�
//			if(!route.meetOSNR()) {
//				routelist.remove(i);
//			}
//		}

	}

	// flag2:1����������2Ԥ��
	public static Route findMeetOSNRelatedRroute(Route route, Traffic traffic, int flag, int flag2) {
//		if (traffic.getTrafficgroup() == null ) {
		Route onlymeetOSNR = null;
		// �ж�route�Ƿ�����OSNR
		if (route == null || route.meetOSNR() == false) {
			HashSet<CommonNode> dict = setdict();
			List<List<CommonNode>> ladders = findAllRoute(traffic.getFromNode(), traffic.getToNode(), dict);
			List<Route> routelist = Route.fromNodeToRoute(ladders, traffic);
			if (flag == 0) {// ��С������·�ɰ���С��������
				Route.sortHopMethod(routelist);
			} else {// ��̣���·�ɰ���������,Ĭ�ϰ���̳���
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
//			//�й���ҵ����
//			if(route == null || route.meetOSNR() == false || Route.consistWaveNum(route, flag2, traffic).size() < traffic.getEffectNum()) {
//				Route onlymeetOSNR = null;
//				Route consistAndMeetOSNR = null;
//				HashSet<CommonNode> dict = setdict();
//				List<List<CommonNode>> ladders = findAllRoute(traffic.getFromNode(), traffic.getToNode(), dict);
//				List<Route> routelist = Route.fromNodeToRoute(ladders, traffic);
//				if (flag == 0) {// ��С������·�ɰ���С��������
//					Route.sortHopMethod(routelist);
//				} else {// ��̣���·�ɰ���������,Ĭ�ϰ���̳���
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
//				//��routeMeetOSNRList�ҳ���õ�route��
//				
//				//��routeConsistAndMeetOSNRList�ҳ���õ�route
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

	// ����������Դ����
	public static void allocateResourceGrid(List<Traffic> traList, int flag) {
		// flag=0:��̳��ȣ�=1����С��
		Survivance.mark = flag;// ���ڿ��ٷ����б�Ƿ����ʱ��ʹ�õ�����·���㷨
		CommonAlloc.fallBuffer = new StringBuffer();// ��·��Դ������־
		PortAlloc.portFallBuffer = new StringBuffer();// �˿���Դ������־
		trafficList = new LinkedList<Traffic>();
		trafficList.addAll(traList);// ������һ��ҵ���б���trafficList�Ĳ�������Ӱ��traList
		// Collections.reverse(traList);

		// ��Ź���·�ɼ���ʧ�ܵ�ҵ��
		LinkedList<Traffic> workFailure = new LinkedList<>();
		// ��ű���·�ɼ���ʧ�ܵ�ҵ��
		LinkedList<Traffic> protectFailure = new LinkedList<>();
		// ���Ԥ��·�ɼ���ʧ�ܵ�ҵ��
		LinkedList<Traffic> preFailure = new LinkedList<>();

		Route route = null;
		OSNR osnr = new OSNR();
		if (!RouteAlloc.isLimitHop) {
			RouteAlloc.hopLimit = Integer.MAX_VALUE;
		} // ������������(�����������������Ͱ���������Ϊ�����)
		else if (RouteAlloc.hopLimit == Integer.MAX_VALUE) {
			RouteAlloc.hopLimit = 5;
		} // Ĭ����������Ϊ5

		Iterator<Traffic> it = trafficList.iterator();
		while (it.hasNext()) {// ѭ������������ÿ��ҵ��Ĺ���·��/����·�ɣ�����У�

			index++;
			Traffic traffic = it.next();

			// ���ر���·���ڵ���Ϊ������
			ResourceAlloc.setMustAvoidUnactive(traffic);

			// ����ҵ�����������ò��������������Ӧ��SRLGΪ�����
			ResourceAlloc.setRelatedTrafficUnactive(traffic, DataSave.separate);

			HashSet<CommonNode> dict = ResourceAlloc.setdict();
			
			//������������������·�ɵ㼯
			List<List<CommonNode>> nodeRoutes = ResourceAlloc.findAllRoute(traffic.getFromNode(),
					traffic.getToNode(), dict);
			
			
			
			
			
		}

	}

}
