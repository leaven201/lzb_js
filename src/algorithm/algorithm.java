/*package algorithm;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import data.BasicLink;
import data.CommonNode;
import data.Fiber;
import data.FiberLink;
import data.Network;
import data.Route;
import data.Traffic;
import data.WDMLink;
import data.Wavelength_2;
import enums.Layer;
import enums.TrafficLevel;
import enums.TrafficStatus;
import rcAllocate.*;

public class algorithm {

	public static List<Traffic> TrafficList;
	public static boolean isSRLG = false; // �Ƿ���ҪSRLG
	public static int expansion = 0; // 0(�ֶ�����) 1(�Զ�����)
	public static int expansionNum = 0; // ����ָ��ֵ
	public static int Fiberexpansion = 16; // ��������
	public static int WDMexpansion = 80; // WDM���ݲ���
	public static double maxresource = 1.0; // ��·���������Դռ������
	public static boolean ifhoplimit = false; // �Ƿ������������
	public static int hoplimit = Integer.MAX_VALUE; // �������
	// public static JProgressBar pbar = new JProgressBar(); //������
	public static int newValue; // ��������ʼֵ
	public static int year = Integer.MAX_VALUE; // �滮���
	public static int m = 20; // �ۺ���С���۷�mȡֵ
	public static int b = 10;
	public static int tafficlevel; // ������ҵ��ȼ�ʱҵ���Ĭ�ϵȼ�
	public static boolean groupOrlevel = true; // ������滮(true)�򰴷ֲ�滮(false)
//    public static boolean ifRelate = true; // �Ƿ��ǹ���ҵ����
//    public static TrafficGroup trafficgroup; // �滮ҵ����
	public static String trafficlayer;
	public static boolean isCiyoulu = false; // �Ƿ���д���·����

	
	 * Ϊҵ��·�ɵ������,�Ƚ�ҵ�񰴲��Զ����ݼ��㣬������ҵ���������Զ�����Ҫ�������� ���룺��Ҫ��·��ҵ��(��) �����void
	 
	public static void RWAForTraffic(List<Traffic> trList, int Hop_Lenght) {
		int Expansion = expansion;// �������ݱ��
		expansion = 0;
		// ��ʼ����Դ�����������־
		commonAlloc.sFallbuffer = new StringBuffer();// �̰߳�ȫ
		portAlloc.sPortFallBuffer = new StringBuffer();

		TrafficList = new LinkedList<Traffic>();
		TrafficList.addAll(trList);

		// ��·˳��Ϊ�ȵȼ��ߣ����ȴ��ҵ��
		rwaForTraffic(TrafficList, Hop_Lenght);

		//////////////////////////////////////////////////////////////////////////////////////////
		 �Ƿ��Զ����ݣ�������ֱ�Ӽ��㣬����ֱ���˳� 
		Iterator<Traffic> it = TrafficList.iterator();
		List<Traffic> TraList = new LinkedList<Traffic>();
		while (it.hasNext()) {// ��������ҵ����˳�������������
			Traffic trf = it.next();
			if (trf.getM_eStatus().equals(TrafficStatus.����))
				TraList.add(trf);
		}

		expansion = Expansion;// ��ԭ���ݱ��
		if (expansion == 0)// �����Զ�����
		{

		} else if (expansion == 1) {// ����
			// ������ݶ˿�
			Iterator<CommonNode> itNode = CommonNode.m_lCommonNode.iterator();
			while (itNode.hasNext()) {
				CommonNode theNode = itNode.next();
				theNode.getM_lExPort().clear();
			}
			Iterator<FiberLink> itFiberLink = FiberLink.s_lFiberLinkList.iterator();// ��������
			while (itFiberLink.hasNext()) {
				FiberLink theLink = itFiberLink.next();
				theLink.getM_lExFiber().clear();
				int id = theLink.getM_nSize();
				for (int nFiber = 0; nFiber < Fiberexpansion; nFiber++) {
					Fiber fiber = new Fiber(id + nFiber);
					theLink.getM_lExFiber().add(fiber);
				}
			}

			Iterator<WDMLink> itWDMLink = WDMLink.s_lWDMLinkList.iterator();// WDM��������
			while (itWDMLink.hasNext()) {
				WDMLink theLink = itWDMLink.next();
				int k = 1;
				int rate = (int) theLink.getM_nRate();// ��·����
				int id = theLink.getM_nSize(); // ʹ���ݲ�������ԭ�����Ų��غ�
				theLink.getM_lExWavelengthList().clear();
				while (k <= WDMexpansion) {
					Wavelength_2 wave = new Wavelength_2(theLink, id + k, (int) rate);
					theLink.getM_lExWavelengthList().add(wave);
					++k;
				}
			}

			rwaForTraffic(TraList, Hop_Lenght);// ��������ҵ���������ݺ����

			// ���������Դ
			// Exclear.clearExInfo();

		}
		//////////////////////////////////////////////////////////////////////////////////////////
		// ����Դ������־д��txt��
		ResourceLogTxt.ResourceLogTxt();
		commonAlloc.sFallbuffer = new StringBuffer();
		portAlloc.sPortFallBuffer = new StringBuffer();

		expansion = 0;
		hoplimit = Define.infinity;
		TrafficList = null;
	}

	
	 * ����ҵ��·������Դ����
	 
	public static void rwaForTraffic(List<Traffic> trList, int Hop_Lenght) {

		Iterator<Traffic> it = trList.iterator();
		List<Traffic> FiberTrf = new LinkedList<Traffic>();
		List<Traffic> WDMTrf = new LinkedList<Traffic>();
		while (it.hasNext()) {
			Traffic trf = it.next();
			switch (trf.getLayer()) {
			case FIBER:
				FiberTrf.add(trf);
				break;
			case WDM:
				WDMTrf.add(trf);
				break;
			}
		}

		// Fiber��ҵ��·������Դ����
		// *****************************************************************************************************************************************
		it = FiberTrf.iterator();
		while (it.hasNext()) {
			Traffic trf = it.next();
			// ����·��
			LinkedList returnList = new LinkedList();
			Route route = RWAMain(trf.getFromNode(), trf.getToNode(), trf.getM_eLayer(), Hop_Lenght, returnList);
			if (route != null && route.getFiberLinkList().size() <= hoplimit)
				trf.setWorkRoute(route);

			rmRepeat(trf, 0);// firesky 2012 4.19
			if (commonAlloc.ResourceAssignment(trf, 0)) {
				trf.setStatus(TrafficStatus.�����ѷ���);
				if (trf.getProtectLevel().equals(TrafficLevel.PERMANENT11)
						|| trf.getProtectLevel().equals(TrafficLevel.NORMAL11)) {
					// ����·��,����·�ɾ����ڵ�,��·״̬��Ϊfalse,����SRLG�޹ر���·��
					Iterator<FiberLink> itLink = trf.getWorkRoute().getFiberLinkList().iterator();
					while (itLink.hasNext()) {
						FiberLink theLink = itLink.next();
						theLink.setActive(false);
					}
					Route routeP = RWAMain(trf.getFromNode(), trf.getToNode(), trf.getM_eLayer(), Hop_Lenght,
							returnList);
					if (routeP != null && routeP.getM_lFiberLinkList().size() <= hoplimit)
						trf.setProtectRoute(routeP);
					rmRepeat(trf, 1);// firesky 2012 4.19
					if (!commonAlloc.ResourceAssignment(trf, 1)) {
						trf.getProtectRoute().clearRoute();
					} else
						trf.setStatus(TrafficStatus.�����ͱ����ѷ���);
					// ����·��

					// �ָ�����
					itLink = trf.getWorkRoute().getFiberLinkList().iterator();
					while (itLink.hasNext()) {
						BasicLink theLink = itLink.next();
						theLink.setActive(true);
					}
				}
			} else {
				// �������·
				route = trf.getWorkRoute();
				Route rt = SuboptimalRoute(trf, route, Hop_Lenght);
				if (rt != null && rt.getFiberLinkList().size() <= hoplimit)
					trf.setWorkRoute(rt);
				rmRepeat(trf, 0);// firesky 2012 4.19
				if (!commonAlloc.ResourceAssignment(trf, 0)) {
					trf.getWorkRoute().clearRoute();
					trf.setStatus(TrafficStatus.����);
				} else
					trf.setStatus(TrafficStatus.�����ѷ���);
			}
		}

		// wdm��ҵ��·������Դ����
		// *****************************************************************************************************************************************
		it = WDMTrf.iterator();
		while (it.hasNext()) {
			Traffic trf = it.next();
			// ����·��
			isCiyoulu = false;
			LinkedList returnList = new LinkedList();
			Route route = RWAMain(trf.getM_nFromNode(), trf.getM_nToNode(), trf.getM_eLayer(), Hop_Lenght, returnList);
			if (route != null && route.getM_lWDMLinkList().size() <= hoplimit)
				trf.setM_cWorkRoute(route);
			rmRepeat(trf, 0);// firesky 2012 4.19
			if (AllocateSource.allocateSource(trf, 0))
			// if(commonAlloc.ResourceAssignment(trf, 0))
			{
				trf.setM_eStatus(TrafficStatus.�����ѷ���);
				if (trf.getM_eLevel().equals(TrafficLevel.PERMANENT11)
						|| trf.getM_eLevel().equals(TrafficLevel.NORMAL11)) {
					// ����·��,����·�ɾ����ڵ�,��·״̬��Ϊfalse,����SRLG�޹ر���·��
					Iterator<WDMLink> itLink = trf.getM_cWorkRoute().getM_lWDMLinkList().iterator();
					while (itLink.hasNext()) {
						WDMLink theLink = itLink.next();
						theLink.setM_bStatus(false);
					}

					Route routeP = RWAMain(trf.getM_nFromNode(), trf.getM_nToNode(), trf.getM_eLayer(), Hop_Lenght,
							returnList);
					if (routeP != null && routeP.getM_lWDMLinkList().size() <= hoplimit)
						trf.setM_cProtectRoute(routeP);
					rmRepeat(trf, 1);// firesky 2012 4.19
					// ����·��
					if (!AllocateSource.allocateSource(trf, 1))
					// if(!commonAlloc.ResourceAssignment(trf, 1))
					{
						trf.getM_cProtectRoute().clearRoute();
					} else
						trf.setM_eStatus(TrafficStatus.�����ͱ����ѷ���);
					// �ָ�����
					itLink = trf.getM_cWorkRoute().getM_lWDMLinkList().iterator();
					while (itLink.hasNext()) {
						BasicLink theLink = itLink.next();
						theLink.setM_bStatus(true);
					}
				}
			} else {
				// �������·
				isCiyoulu = true;
				route = trf.getM_cWorkRoute();
				Route rt = SuboptimalRoute(trf, route, Hop_Lenght);
				if (rt != null && rt.getM_lWDMLinkList().size() <= hoplimit)
					trf.setM_cWorkRoute(rt);
				rmRepeat(trf, 0);// firesky 2012 4.19
				if (!AllocateSource.allocateSource(trf, 0)) {
					trf.getM_cWorkRoute().clearRoute();
					trf.setM_eStatus(TrafficStatus.����);
				} else
					trf.setM_eStatus(TrafficStatus.�����ѷ���);
			}
		}

		// otn��ҵ���������Դ����
		// *****************************************************************************************************************************************
		it = OTNTrf.iterator();
		while (it.hasNext()) {
			Traffic trf = it.next();
			// ����·��
			isCiyoulu = false;
			LinkedList returnList = new LinkedList();
			Route route = RWAMain(trf.getM_nFromNode(), trf.getM_nToNode(), trf.getM_eLayer(), Hop_Lenght, returnList);
			if (route != null && route.getM_lOTNLinkList().size() <= hoplimit)
				trf.setM_cWorkRoute(route);
			rmRepeat(trf, 0);// firesky 2012 4.19
			if (AllocateSource.allocateSource(trf, 0))
			// if(commonAlloc.ResourceAssignment(trf, 0))
			{
				trf.setM_eStatus(TrafficStatus.�����ѷ���);
				if (trf.getM_eLevel().equals(TrafficLevel.PERMANENT11)
						|| trf.getM_eLevel().equals(TrafficLevel.NORMAL11)) {
					// ����·��,����·�ɾ����ڵ�,��·״̬��Ϊfalse,����SRLG�޹ر���·��
					Iterator<WDMLink> itLink = trf.getM_cWorkRoute().getM_lOTNLinkList().iterator();
					while (itLink.hasNext()) {
						WDMLink theLink = itLink.next();
						theLink.setM_bStatus(false);
					}

					Route routeP = RWAMain(trf.getM_nFromNode(), trf.getM_nToNode(), trf.getM_eLayer(), Hop_Lenght,
							returnList);
					if (routeP != null && routeP.getM_lOTNLinkList().size() <= hoplimit)
						trf.setM_cProtectRoute(routeP);
					rmRepeat(trf, 1);// firesky 2012 4.19
					if (!AllocateSource.allocateSource(trf, 1))
					// if(!commonAlloc.ResourceAssignment(trf, 1))
					{
						trf.getM_cProtectRoute().clearRoute();
					} else
						trf.setM_eStatus(TrafficStatus.�����ͱ����ѷ���);
					// ����·��

					// �ָ�����
					itLink = trf.getM_cWorkRoute().getM_lOTNLinkList().iterator();
					while (itLink.hasNext()) {
						BasicLink theLink = itLink.next();
						theLink.setM_bStatus(true);
					}
				}
			} else {
				// �������·
				isCiyoulu = true;
				route = trf.getM_cWorkRoute();
				Route rt = SuboptimalRoute(trf, route, Hop_Lenght);
				if (rt != null && rt.getM_lOTNLinkList().size() <= hoplimit)
					trf.setM_cWorkRoute(rt);
				rmRepeat(trf, 1);// firesky 2012 4.19
				if (!AllocateSource.allocateSource(trf, 0)) {
					trf.getM_cWorkRoute().clearRoute();
					trf.setM_eStatus(TrafficStatus.����);
				} else
					trf.setM_eStatus(TrafficStatus.�����ѷ���);
			}
		}
	}

	
	 * ·���㷨���rwa
	 
	public static Route RWA(CommonNode from, CommonNode to, int Hop_Lenght) {
		DijkstraAlgorithm.clear();
		Route route = null;
		Network FromNet = Network.searchNetwork(from.getM_nSubnetNum());
		Network ToNet = Network.searchNetwork(to.getM_nSubnetNum());
		if (FromNet.equals(ToNet)) {
			route = RWAForInNet(from, to, Hop_Lenght);
		} else {
			Network FromMainNet = FromNet.getM_nUpperNetwork();
			Network ToMainNet = ToNet.getM_nUpperNetwork();
			if (FromMainNet.equals(ToMainNet)) {
				route = RWAForCrossMainNet(from, to, Hop_Lenght);
			} else {
				Network FromCoreNet = FromMainNet.getM_nUpperNetwork();
				Network ToCoreNet = ToMainNet.getM_nUpperNetwork();
				if (FromCoreNet.equals(ToCoreNet)) {
					route = RWAForCrossCoreNet(from, to, Hop_Lenght);
				}
			}
		}
		return route;
	}

	
	 * ҵ����ĩ�ڵ���ͬһ��������������
	 
	public static Route RWAForInNet(CommonNode from, CommonNode to, int Hop_Lenght) {
		DijkstraAlgorithm.clear();
		int fromNetID = from.getM_nSubnetNum();
		int toNetID = to.getM_nSubnetNum();

		Network fromNet = Network.searchNetwork(fromNetID);
		Network toNet = Network.searchNetwork(toNetID);

		if (fromNet.equals(toNet)) {
			// ���ڵ���ͬһ�����������ڣ���ֱ����·
			boolean flag = DijkstraAlgorithm.dijkstra(fromNet, Hop_Lenght, from, to);
			if (flag) {
				Route route = new Route();
				route.setM_lNodeList(DijkstraAlgorithm.routeNodeList);
				route.setM_lLinkList(DijkstraAlgorithm.routeLinkList);
				return route;
			}
			return null;
		}
		return null;
	}

	
	 * ҵ����ĩ�ڵ���ͬһ���Ǹ��������ڣ�һ������2��3����·
	 
	public static Route RWAForCrossMainNet(CommonNode from, CommonNode to, int Hop_Lenght) {

		Route route = new Route();
		int fromNetID = from.getM_nSubnetNum();
		int toNetID = to.getM_nSubnetNum();

		Network fromNet = Network.searchNetwork(fromNetID);
		Network toNet = Network.searchNetwork(toNetID);

		Network fromMainNet = fromNet.getM_nUpperNetwork();// �ҵ���Ӧ�ϲ�Ǹ���
		Network toMainNet = toNet.getM_nUpperNetwork();

		boolean flag = DijkstraAlgorithm.dijkstraFor3thNet(fromNet, Hop_Lenght, from);
		if (!flag)
			return null;
		route.getM_lLinkList().addAll(DijkstraAlgorithm.routeLinkList);
		route.getM_lNodeList().addAll(DijkstraAlgorithm.routeNodeList);

		if (route.getM_lNodeList().size() == 0)
			return null;

		int toid = route.getM_lNodeList().size() - 1;
		CommonNode from2Main_out_Node = route.getM_lNodeList().get(toid);
		CommonNode fromMainNode = null;
		BasicLink from_to_MainLink = fromNet.searchOutLink(from2Main_out_Node);

		if (from_to_MainLink == null)
			return null;

		if (from_to_MainLink.getM_cFromNode().equals(from2Main_out_Node))
			fromMainNode = from_to_MainLink.getM_cToNode();
		else
			fromMainNode = from_to_MainLink.getM_cFromNode();
		// ����from���ϲ�ļ�����·
		route.getM_lLinkList().add(from_to_MainLink);

		// ����to����ڽڵ�
		flag = DijkstraAlgorithm.dijkstraFor3thNet(toNet, Hop_Lenght, to);
		if (!flag)
			return null;
		if (DijkstraAlgorithm.routeNodeList.size() == 0)
			return null;
		LinkedList<CommonNode> NodeList = new LinkedList<CommonNode>();
		LinkedList<BasicLink> LinkList = new LinkedList<BasicLink>();
		LinkList.addAll(DijkstraAlgorithm.routeLinkList);
		NodeList.addAll(DijkstraAlgorithm.routeNodeList);

		int fromid = NodeList.size() - 1;
		CommonNode Main2to_out_Node = NodeList.get(fromid);
		BasicLink to_to_MainLink = toNet.searchOutLink(Main2to_out_Node);

		if (to_to_MainLink == null)
			return null;
		CommonNode toMainNode = null;
		if (to_to_MainLink.getM_cFromNode().equals(Main2to_out_Node))
			toMainNode = to_to_MainLink.getM_cToNode();
		else
			toMainNode = to_to_MainLink.getM_cFromNode();

		// ����to���ϲ�ļ�����·
		LinkList.add(to_to_MainLink);

		// �ϲ���·
		if (!toMainNode.equals(fromMainNode)) {
			Route rou = RWAForInNet(fromMainNode, toMainNode, Hop_Lenght);
			if (rou == null)
				return null;
			route.getM_lNodeList().addAll(rou.getM_lNodeList());
			route.getM_lLinkList().addAll(rou.getM_lLinkList());
		} else
			route.getM_lNodeList().add(fromMainNode);

		for (int i = LinkList.size() - 1; i >= 0; --i)
			route.getM_lLinkList().add(LinkList.get(i));
		for (int i = NodeList.size() - 1; i >= 0; --i)
			route.getM_lNodeList().add(NodeList.get(i));

		return route;
	}

	
	 * ҵ��Ҫͨ�������������·�ɣ�һ��Ҫ��3��5�������·,�����·�������ѡ·
	 
	public static Route RWAForCrossCoreNet(CommonNode from, CommonNode to, int Hop_Lenght) {
		Route route = new Route();// ������·���

		int fromNetID = from.getM_nSubnetNum();
		int toNetID = to.getM_nSubnetNum();

		Network fromNet = Network.searchNetwork(fromNetID); // �׽ڵ����ڵ�����
		Network toNet = Network.searchNetwork(toNetID); // ĩ�ڵ����ڵ�����
		Network FromMainNet = fromNet.getM_nUpperNetwork(); // �׽ڵ����ڵ�(�ϲ�)�Ǹ�����
		Network ToMainNet = toNet.getM_nUpperNetwork(); // ĩ�ڵ����ڵģ��ϲ㣩�Ǹ�����

		Network CoreNet = FromMainNet.getM_nUpperNetwork();

		boolean flag = DijkstraAlgorithm.dijkstraFor3thNet(fromNet, Hop_Lenght, from);
		if (!flag)
			return null;
		route.getM_lLinkList().addAll(DijkstraAlgorithm.routeLinkList);
		route.getM_lNodeList().addAll(DijkstraAlgorithm.routeNodeList);

		if (route.getM_lNodeList().size() == 0)
			return null;

		int toid = route.getM_lNodeList().size() - 1;
		CommonNode from2Main_out_Node = route.getM_lNodeList().get(toid);
		CommonNode fromMainNode = null;
		BasicLink from_to_MainLink = fromNet.searchOutLink(from2Main_out_Node);

		if (from_to_MainLink == null)
			return null;

		if (from_to_MainLink.getM_cFromNode().equals(from2Main_out_Node))
			fromMainNode = from_to_MainLink.getM_cToNode();
		else
			fromMainNode = from_to_MainLink.getM_cFromNode();
		// ����from���ϲ�ļ�����·
		route.getM_lLinkList().add(from_to_MainLink);

		// ����to����ڽڵ�
		flag = DijkstraAlgorithm.dijkstraFor3thNet(toNet, Hop_Lenght, to);
		if (!flag)
			return null;
		if (DijkstraAlgorithm.routeNodeList.size() == 0)
			return null;
		LinkedList<CommonNode> NodeList = new LinkedList<CommonNode>();
		LinkedList<BasicLink> LinkList = new LinkedList<BasicLink>();
		LinkList.addAll(DijkstraAlgorithm.routeLinkList);
		NodeList.addAll(DijkstraAlgorithm.routeNodeList);

		int fromid = NodeList.size() - 1;
		CommonNode Main2to_out_Node = NodeList.get(fromid);
		BasicLink to_to_MainLink = toNet.searchOutLink(Main2to_out_Node);

		if (to_to_MainLink == null)
			return null;
		CommonNode toMainNode = null;
		if (to_to_MainLink.getM_cFromNode().equals(Main2to_out_Node))
			toMainNode = to_to_MainLink.getM_cToNode();
		else
			toMainNode = to_to_MainLink.getM_cFromNode();

		// ����to���ϲ�ļ�����·
		LinkList.add(to_to_MainLink);

		Route route1 = RWAForCrossMainNet(fromMainNode, toMainNode, Hop_Lenght);

		if (route1 == null)
			return null;
		route.getM_lNodeList().addAll(route1.getM_lNodeList());
		route.getM_lLinkList().addAll(route1.getM_lLinkList());

		for (int i = LinkList.size() - 1; i >= 0; --i)
			route.getM_lLinkList().add(LinkList.get(i));
		for (int i = NodeList.size() - 1; i >= 0; --i)
			route.getM_lNodeList().add(NodeList.get(i));

		return route;
	}

	
	 * �������ƣ�clearWorkRoute �������ã����ҵ��Ĺ���·�� �����������Ҫ��չ���·�ɵ�ҵ������ ���ز��������ؽڵ�����
	 
	public static boolean clearWorkRoute(LinkedList<Traffic> trafficList) {
		ListIterator<Traffic> it = trafficList.listIterator();
		while (it.hasNext()) {
			Traffic theTraffic = it.next();
			switch (theTraffic.getM_eLayer()) {
			case FIBER:
				theTraffic.releaseTraffic(theTraffic, 0);
				theTraffic.getM_cWorkRoute().getM_lFiberLinkList().clear();
				break;
			case WDM:
				theTraffic.releaseTraffic(theTraffic, 0);
				theTraffic.getM_cWorkRoute().getM_lWDMLinkList().clear();
				break;
			case OTN:
				theTraffic.releaseTraffic(theTraffic, 0);
				theTraffic.getM_cWorkRoute().getM_lOTNLinkList().clear();
				break;
			}
			theTraffic.getM_cWorkRoute().getM_lLinkList().clear();
			theTraffic.getM_cWorkRoute().getM_lNodeList().clear();
		}
		return true;
	}

	
	 * �������ƣ�clearProtectRoute �������ã����ҵ��ı���·�� �����������Ҫ��ձ���·�ɵ�ҵ������ ���ز�����true
	 
	public static boolean clearProtectRoute(LinkedList<Traffic> trafficList) {
		ListIterator<Traffic> it = trafficList.listIterator();
		while (it.hasNext()) {
			Traffic theTraffic = it.next();
			switch (theTraffic.getM_eLayer()) {
			case FIBER:
				theTraffic.releaseTraffic(theTraffic, 1);
				theTraffic.getM_cProtectRoute().getM_lFiberLinkList().clear();
				break;
			case WDM:
				theTraffic.releaseTraffic(theTraffic, 1);
				theTraffic.getM_cProtectRoute().getM_lWDMLinkList().clear();
				break;
			case OTN:
				theTraffic.releaseTraffic(theTraffic, 1);
				theTraffic.getM_cProtectRoute().getM_lOTNLinkList().clear();
				break;
			}
			theTraffic.getM_cProtectRoute().getM_lLinkList().clear();
			theTraffic.getM_cProtectRoute().getM_lNodeList().clear();
		}
		return true;
	}

	
	 * �������ƣ�clearResumeRoute �������ã����ҵ��Ļָ�·�� �����������Ҫ��ջָ�·�ɵ�ҵ������ ���ز�����true
	 
	public static boolean clearResumeRoute(LinkedList<Traffic> trafficList) {
		ListIterator<Traffic> it = trafficList.listIterator();
		while (it.hasNext()) {
			Traffic theTraffic = it.next();
			switch (theTraffic.getM_eLayer()) {
			case FIBER:
				theTraffic.getM_cResumeRoute().getM_lFiberLinkList().clear();
				break;
			case WDM:
				theTraffic.getM_cResumeRoute().getM_lWDMLinkList().clear();
				break;
			case OTN:
				theTraffic.getM_cResumeRoute().getM_lOTNLinkList().clear();
				break;
			}
			theTraffic.getM_cResumeRoute().getM_lLinkList().clear();
			theTraffic.getM_cResumeRoute().getM_lNodeList().clear();
		}
		return true;
	}

	
	 * ·���㷨����ڣ����е����㷨�������￪ʼ ���룺from,to,�������ڲ�layer,�㷨����type����·���������returnList; =1����
	 * =2 ���� =3 bsp =4 �ۺ���С ���أ�void
	 
	public static Route RWAMain(CommonNode from, CommonNode to, Layer layer, int type, List returnList) {
		Route route = null;
		switch (layer) {
		case Fiber:
			route = RWAForFiber(from, to, type, returnList);
			break;
		case WDM:
			route = RWAForWDM(from, to, type, returnList);
			break;
		default:
			break;
		}
		return route;
	}

	private static Route RWAForOTN(CommonNode from, CommonNode to, int type, List returnList) {
		Route route = null;
		switch (type) {
		case 0:
		case 1:
		case 5:
			// d�㷨������
			route = RouteAlgorithm(Layer.OTN, from, to, type);
			break;
		case 2:
			// d�㷨������
			route = RouteAlgorithm(Layer.OTN, from, to, type);
			break;
		case 3:
			// bsp�㷨
			route = RouteAlgorithm(Layer.OTN, from, to, type);
			break;
		case 4:
			// �ۺ���С
			route = RouteAlgorithm(Layer.OTN, from, to, type);
			break;
		default:
			break;
		}
		return route;
	}

	private static Route RWAForWDM(CommonNode from, CommonNode to, int type, List returnList) {
		Route route = null;
		switch (type) {
		case 0:
		case 1:
		case 5:
			// d�㷨������
			route = RouteAlgorithm(Layer.WDM, from, to, type);
			break;
		case 2:
			// d�㷨������
			route = RouteAlgorithm(Layer.WDM, from, to, type);
			break;
		case 3:
			// bsp�㷨
			route = RouteAlgorithm(Layer.WDM, from, to, type);
			break;
		case 4:
			// �ۺ���С
			route = RouteAlgorithm(Layer.WDM, from, to, type);
			break;
		default:
			break;
		}
		return route;
	}

	private static Route RWAForFiber(CommonNode from, CommonNode to, int type, List returnList) {
		Route route = null;
		switch (type) {
		case 0:
		case 1:
		case 5:
			// d�㷨������
			route = RouteAlgorithm(Layer.FIBER, from, to, type);
			break;
		case 2:
			// d�㷨������
			route = RouteAlgorithm(Layer.FIBER, from, to, type);
			break;
		case 3:
			// bsp�㷨
			route = RouteAlgorithm(Layer.FIBER, from, to, type);
			break;
		case 4:
			// �ۺ���С
			route = RouteAlgorithm(Layer.FIBER, from, to, type);
			break;
		default:
			break;
		}
		return route;
	}

	
	 * ����·�ɺ�Ϊһ��·��
	 

	
	 * �����·��,��rt�������route��
	 
	public static Route ReverseRoute(Layer layer, Route route, Route rt) {// Ϊ��Ҫ����洢·��
		List<CommonNode> nodeList = null;
		List linkList = null;
		switch (layer) {
		case FIBER:
			nodeList = rt.getM_lFiberNodeList();
			linkList = rt.getM_lFiberLinkList();
			for (int i = nodeList.size() - 1; i >= 0; --i)
				route.getM_lFiberNodeList().add(nodeList.get(i));
			for (int i = linkList.size() - 1; i >= 0; --i)
				route.getM_lFiberLinkList().add((FiberLink) linkList.get(i));
			break;
		case WDM:
			nodeList = rt.getM_lWDMNodeList();
			linkList = rt.getM_lWDMLinkList();
			for (int i = nodeList.size() - 1; i >= 0; --i)
				route.getM_lWDMNodeList().add(nodeList.get(i));
			for (int i = linkList.size() - 1; i >= 0; --i)
				route.getM_lWDMLinkList().add((WDMLink) linkList.get(i));
			break;
		case OTN:
			nodeList = rt.getM_lOTNNodeList();
			linkList = rt.getM_lOTNLinkList();
			for (int i = nodeList.size() - 1; i >= 0; --i)
				route.getM_lOTNNodeList().add(nodeList.get(i));
			for (int i = linkList.size() - 1; i >= 0; --i)
				route.getM_lOTNLinkList().add((WDMLink) linkList.get(i));
			break;
		}
		return route;
	}

	
	 * net ������ͬ
	 
	public static Route netTypeInSame(CommonNode from, CommonNode to, int type, Layer layer) {
		Route route = null;
		Network FromNet = Network.searchNetwork(from.getM_nSubnetNum());
		Network ToNet = Network.searchNetwork(to.getM_nSubnetNum());
		if (FromNet.equals(ToNet)) {
			route = RWAForInNet(layer, from, to, type);// �����ڵ���ͬһ������
		} else {
			Network FromMainNet = FromNet.getM_nUpperNetwork();
			Network ToMainNet = ToNet.getM_nUpperNetwork();
			if (FromMainNet.equals(ToMainNet))// ����ϲ��������ͬ
			{
				route = RWAForCrossMainNet(layer, from, to, type);// �����ڵ㲻��ͬһ�����У���������ͬ���ϲ�����
			} else {
				Network FromCoreNet = FromMainNet.getM_nUpperNetwork();
				Network ToCoreNet = ToMainNet.getM_nUpperNetwork();
				if (FromCoreNet.equals(ToCoreNet)) {
					route = RWAForCrossCoreNet(layer, from, to, type);
				}
			}
		}
		return route;
	}

	public static Route RWAForInNet(Layer layer, CommonNode from, CommonNode to, int type) {
		Route route = null;

		LinkedList returnLinkList = new LinkedList();
		LinkedList<CommonNode> returnNodeList = new LinkedList<CommonNode>();
		switch (type) {
		case 0:
		case 1:
		case 2:
		case 5:
			DijkstraAlgorithm dijkstra = new DijkstraAlgorithm();
			dijkstra.dijkstra(layer, type, from, to, returnLinkList, returnNodeList);
			break;
		case 3:
			break;
		case 4:
			break;
		}
		if (returnLinkList.size() != 0) {
			route = new Route();
			if (layer.equals(Layer.Fiber)) {
				route.setM_lFiberLinkList(returnLinkList);
				route.setM_lFiberNodeList(returnNodeList);
			} else if (layer.equals(Layer.WDM)) {
				route.setM_lWDMLinkList(returnLinkList);
				route.setM_lWDMNodeList(returnNodeList);
			} else if (layer.equals(Layer.OTN)) {
				route.setM_lOTNLinkList(returnLinkList);
				route.setM_lOTNNodeList(returnNodeList);
			}
		}
		return route;
	}

	
	 * ������·�� type=1 ���� 2����
	 
	public static void calWDMLayer_IN_FiberLayer(WDMLink wdmlink, int type) {// û�����á���
		CommonNode from = wdmlink.getM_cFromNode();
		CommonNode to = wdmlink.getM_cToNode();
		LinkedList returnList = new LinkedList();

		Route route = RWAForWDM(from, to, type, returnList);
		if (route == null)
			return;
		wdmlink.setM_lFiberLinkList(route.getM_lFiberLinkList()); // ����·��
		// ������Դ

		return;
	}

	
	 * �ж�ҵ��·���Ƿ�Ϊ�գ�����Ϊ�գ�����false
	 
	public static boolean isRouteEmpty(List<Traffic> TrafficList) {
		Iterator<Traffic> it = TrafficList.iterator();
		while (it.hasNext()) {
			Traffic tra = it.next();
			if (tra.getWorkRoute() != null)
				return false;
		}
		return true;
	}

	
	 * ���ҵ��ռ�õ���Դ ����ҵ��滮�е����¹滮
	 
	public static void clearAllTrafficRoute(List<Traffic> trafficList) {
		Iterator<Traffic> it = trafficList.iterator();
		while (it.hasNext()) {
			Traffic tra = it.next();

			tra.releaseTraffic(tra, 0);
			tra.releaseTraffic(tra, 1);
			tra.releaseTraffic(tra, 2);
			tra.releaseTraffic(tra, 3);
			tra.releasePortSource(tra, 0);
			tra.releasePortSource(tra, 1);
			tra.releasePortSource(tra, 2);
			tra.releasePortSource(tra, 3);

			if (tra.getWorkRoute() != null)
				tra.getM_cWorkRoute().clearRoute();
			if (tra.getM_cProtectRoute() != null)
				tra.getM_cProtectRoute().clearRoute();
			if (tra.getM_cResumeRoute() != null)
				tra.getM_cResumeRoute().clearRoute();
			if (tra.getM_cResumeRouteP() != null)
				tra.getM_cResumeRouteP().clearRoute();
		}
	}

	
	 * �������·�� ���ԣ���ǰ·�ɼ���ʧ�ܣ��Ҳ�Ϊ�գ����Ϊ��Դ����ʧ�ܣ���ôֻ��Ҫ����Դ����ʧ�ܵ���·ɾ�����ٽ���һ����·�ɣ��ɹ������������Դ ʧ���򷵻�
	 
	public static Route SuboptimalRoute(Traffic trf, Route route, int type) {
		Route rt = null;
		List LinkList = new LinkedList();// ���ڱ��治������ԴҪ�����·
		Iterator it;
		switch (trf.getM_eLayer()) {
		case FIBER:
			it = route.getFiberLinkList().iterator();
			while (it.hasNext()) {
				FiberLink theLink = (FiberLink) it.next();
				if (theLink.getM_nFree() >= theLink.getM_nSize())
					LinkList.add(theLink);
			}
			break;
		case WDM:
			it = route.getM_lWDMLinkList().iterator();
			while (it.hasNext()) {
				WDMLink theLink = (WDMLink) it.next();
				theLink.reflashM_nUtilization();
				int nWaveRate = (int) theLink.getM_nRate();
				int nUsed = theLink.getM_nSize() - theLink.getM_nFree();
				if (nWaveRate < trf.getM_nRate() || nUsed <= 0 || algorithm.expansion == 0
						&& theLink.getM_nSize() * theLink.getM_nRate() * theLink.getM_dUtilization()
								+ trf.getRate() > theLink.getM_nSize() * theLink.getM_nRate() * algorithm.maxresource) {
					LinkList.add(theLink);
				}
			}
			break;
		default:
			break;
		}

		// ������·״̬��������������ԴҪ�����·���Ϊfalse
		it = LinkList.iterator();
		while (it.hasNext()) {
			BasicLink theLink = (BasicLink) it.next();
			theLink.setM_bStatus(false);
		}

		// �����㷨��·
		LinkedList returnList = new LinkedList();
		rt = RWAMain(trf.getM_nFromNode(), trf.getM_nToNode(), trf.getM_eLayer(), type, returnList);

		// ������·״̬��������������ԴҪ�����·��ԭΪtrue
		it = LinkList.iterator();
		while (it.hasNext()) {
			BasicLink theLink = (BasicLink) it.next();
			theLink.setM_bStatus(true);
		}

		return rt;
	}
	
	 * �����Ż��㷨
	 

	public static void networkOpticAglothms(List<Traffic> trafficList) {
		List<Traffic> trfList = new LinkedList<Traffic>();
		// ��������ҵ��������·���㷨Ϊ���ؾ���
		Iterator<Traffic> it = trafficList.iterator();
		while (it.hasNext()) {
			Traffic trf = it.next();
			if (trf.getM_eStatus().equals(TrafficStatus.����))
				trfList.add(trf);
		}

		RWAForTraffic(trfList, 5);// ���ؾ����㷨��·

		// �ҵ���·�����ʸ���80%����·���ص�ҵ��
		Iterator itLink = FiberLink.s_lFiberLinkList.iterator();
		FiberLink.reflashUtilization();
		while (itLink.hasNext()) {
			FiberLink theLink = (FiberLink) itLink.next();
			if (theLink.getM_dUtilization() > 0.8) {

			}

		}
	}

	
	 * ȥ��·�����ظ��ڵ�
	 
	public static void rmRepeat(Traffic tr, int nKind) {
		Route route = null;
		List<CommonNode> nodelist = null;
		switch (nKind) {
		case 0:
			route = tr.getWorkRoute();
			break;
		case 1:
			route = tr.getProtectRoute();
			break;
		case 2:
			route = tr.getResumeRoute();
			break;
		case 3:
			route = tr.getResumeRoutePro();
			break;
		default:
			break;
		}
		if (route == null)
			return;

		switch (tr.getM_eLayer()) {
		case FIBER:
			nodelist = route.getM_lFiberNodeList();
			break;
		case WDM:
			nodelist = route.getM_lWDMNodeList();
			break;
		default:
			break;

		}
		for (int i = 0; i < nodelist.size() - 1; i++) {
			if (nodelist.get(i).getM_nID() == nodelist.get(i + 1).getM_nID()) {
				nodelist.remove(i + 1);
				--i;
			}
		}
		return;
	}
}
*/