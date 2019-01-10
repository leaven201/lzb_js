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
	public static boolean isSRLG = false; // 是否需要SRLG
	public static int expansion = 0; // 0(手动扩容) 1(自动扩容)
	public static int expansionNum = 0; // 扩容指引值
	public static int Fiberexpansion = 16; // 光纤扩容
	public static int WDMexpansion = 80; // WDM扩容波长
	public static double maxresource = 1.0; // 链路允许最大资源占用门限
	public static boolean ifhoplimit = false; // 是否限制最大跳数
	public static int hoplimit = Integer.MAX_VALUE; // 最大跳数
	// public static JProgressBar pbar = new JProgressBar(); //进度条
	public static int newValue; // 进度条初始值
	public static int year = Integer.MAX_VALUE; // 规划年份
	public static int m = 20; // 综合最小代价法m取值
	public static int b = 10;
	public static int tafficlevel; // 不考虑业务等级时业务的默认等级
	public static boolean groupOrlevel = true; // 按分组规划(true)或按分层规划(false)
//    public static boolean ifRelate = true; // 是否考虑关联业务组
//    public static TrafficGroup trafficgroup; // 规划业务组
	public static String trafficlayer;
	public static boolean isCiyoulu = false; // 是否进行次优路计算

	
	 * 为业务路由的总入口,先将业务按不自动扩容计算，而后若业务阻塞并自动扩容要求，则扩容 输入：需要算路的业务(链) 输出：void
	 
	public static void RWAForTraffic(List<Traffic> trList, int Hop_Lenght) {
		int Expansion = expansion;// 保存扩容标记
		expansion = 0;
		// 初始化资源分配情况的日志
		commonAlloc.sFallbuffer = new StringBuffer();// 线程安全
		portAlloc.sPortFallBuffer = new StringBuffer();

		TrafficList = new LinkedList<Traffic>();
		TrafficList.addAll(trList);

		// 算路顺序为先等级高，粒度大的业务
		rwaForTraffic(TrafficList, Hop_Lenght);

		//////////////////////////////////////////////////////////////////////////////////////////
		 是否自动扩容，若是则直接计算，否则直接退出 
		Iterator<Traffic> it = TrafficList.iterator();
		List<Traffic> TraList = new LinkedList<Traffic>();
		while (it.hasNext()) {// 将阻塞的业务过滤出来，进行扩容
			Traffic trf = it.next();
			if (trf.getM_eStatus().equals(TrafficStatus.阻塞))
				TraList.add(trf);
		}

		expansion = Expansion;// 还原扩容标记
		if (expansion == 0)// 不作自动扩容
		{

		} else if (expansion == 1) {// 扩容
			// 清空扩容端口
			Iterator<CommonNode> itNode = CommonNode.m_lCommonNode.iterator();
			while (itNode.hasNext()) {
				CommonNode theNode = itNode.next();
				theNode.getM_lExPort().clear();
			}
			Iterator<FiberLink> itFiberLink = FiberLink.s_lFiberLinkList.iterator();// 光纤扩容
			while (itFiberLink.hasNext()) {
				FiberLink theLink = itFiberLink.next();
				theLink.getM_lExFiber().clear();
				int id = theLink.getM_nSize();
				for (int nFiber = 0; nFiber < Fiberexpansion; nFiber++) {
					Fiber fiber = new Fiber(id + nFiber);
					theLink.getM_lExFiber().add(fiber);
				}
			}

			Iterator<WDMLink> itWDMLink = WDMLink.s_lWDMLinkList.iterator();// WDM波道扩容
			while (itWDMLink.hasNext()) {
				WDMLink theLink = itWDMLink.next();
				int k = 1;
				int rate = (int) theLink.getM_nRate();// 链路速率
				int id = theLink.getM_nSize(); // 使扩容波道号与原波道号不重合
				theLink.getM_lExWavelengthList().clear();
				while (k <= WDMexpansion) {
					Wavelength_2 wave = new Wavelength_2(theLink, id + k, (int) rate);
					theLink.getM_lExWavelengthList().add(wave);
					++k;
				}
			}

			rwaForTraffic(TraList, Hop_Lenght);// 将阻塞的业务重新扩容后计算

			// 清空扩容资源
			// Exclear.clearExInfo();

		}
		//////////////////////////////////////////////////////////////////////////////////////////
		// 将资源分配日志写入txt中
		ResourceLogTxt.ResourceLogTxt();
		commonAlloc.sFallbuffer = new StringBuffer();
		portAlloc.sPortFallBuffer = new StringBuffer();

		expansion = 0;
		hoplimit = Define.infinity;
		TrafficList = null;
	}

	
	 * 计算业务路由与资源分配
	 
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

		// Fiber层业务路由与资源分配
		// *****************************************************************************************************************************************
		it = FiberTrf.iterator();
		while (it.hasNext()) {
			Traffic trf = it.next();
			// 工作路由
			LinkedList returnList = new LinkedList();
			Route route = RWAMain(trf.getFromNode(), trf.getToNode(), trf.getM_eLayer(), Hop_Lenght, returnList);
			if (route != null && route.getFiberLinkList().size() <= hoplimit)
				trf.setWorkRoute(route);

			rmRepeat(trf, 0);// firesky 2012 4.19
			if (commonAlloc.ResourceAssignment(trf, 0)) {
				trf.setStatus(TrafficStatus.工作已分配);
				if (trf.getProtectLevel().equals(TrafficLevel.PERMANENT11)
						|| trf.getProtectLevel().equals(TrafficLevel.NORMAL11)) {
					// 保护路由,工作路由经过节点,链路状态设为false,计算SRLG无关保护路由
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
						trf.setStatus(TrafficStatus.工作和保护已分配);
					// 保护路由

					// 恢复设置
					itLink = trf.getWorkRoute().getFiberLinkList().iterator();
					while (itLink.hasNext()) {
						BasicLink theLink = itLink.next();
						theLink.setActive(true);
					}
				}
			} else {
				// 计算次优路
				route = trf.getWorkRoute();
				Route rt = SuboptimalRoute(trf, route, Hop_Lenght);
				if (rt != null && rt.getFiberLinkList().size() <= hoplimit)
					trf.setWorkRoute(rt);
				rmRepeat(trf, 0);// firesky 2012 4.19
				if (!commonAlloc.ResourceAssignment(trf, 0)) {
					trf.getWorkRoute().clearRoute();
					trf.setStatus(TrafficStatus.阻塞);
				} else
					trf.setStatus(TrafficStatus.工作已分配);
			}
		}

		// wdm层业务路由与资源分配
		// *****************************************************************************************************************************************
		it = WDMTrf.iterator();
		while (it.hasNext()) {
			Traffic trf = it.next();
			// 工作路由
			isCiyoulu = false;
			LinkedList returnList = new LinkedList();
			Route route = RWAMain(trf.getM_nFromNode(), trf.getM_nToNode(), trf.getM_eLayer(), Hop_Lenght, returnList);
			if (route != null && route.getM_lWDMLinkList().size() <= hoplimit)
				trf.setM_cWorkRoute(route);
			rmRepeat(trf, 0);// firesky 2012 4.19
			if (AllocateSource.allocateSource(trf, 0))
			// if(commonAlloc.ResourceAssignment(trf, 0))
			{
				trf.setM_eStatus(TrafficStatus.工作已分配);
				if (trf.getM_eLevel().equals(TrafficLevel.PERMANENT11)
						|| trf.getM_eLevel().equals(TrafficLevel.NORMAL11)) {
					// 保护路由,工作路由经过节点,链路状态设为false,计算SRLG无关保护路由
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
					// 保护路由
					if (!AllocateSource.allocateSource(trf, 1))
					// if(!commonAlloc.ResourceAssignment(trf, 1))
					{
						trf.getM_cProtectRoute().clearRoute();
					} else
						trf.setM_eStatus(TrafficStatus.工作和保护已分配);
					// 恢复设置
					itLink = trf.getM_cWorkRoute().getM_lWDMLinkList().iterator();
					while (itLink.hasNext()) {
						BasicLink theLink = itLink.next();
						theLink.setM_bStatus(true);
					}
				}
			} else {
				// 计算次优路
				isCiyoulu = true;
				route = trf.getM_cWorkRoute();
				Route rt = SuboptimalRoute(trf, route, Hop_Lenght);
				if (rt != null && rt.getM_lWDMLinkList().size() <= hoplimit)
					trf.setM_cWorkRoute(rt);
				rmRepeat(trf, 0);// firesky 2012 4.19
				if (!AllocateSource.allocateSource(trf, 0)) {
					trf.getM_cWorkRoute().clearRoute();
					trf.setM_eStatus(TrafficStatus.阻塞);
				} else
					trf.setM_eStatus(TrafficStatus.工作已分配);
			}
		}

		// otn层业务分配与资源分配
		// *****************************************************************************************************************************************
		it = OTNTrf.iterator();
		while (it.hasNext()) {
			Traffic trf = it.next();
			// 工作路由
			isCiyoulu = false;
			LinkedList returnList = new LinkedList();
			Route route = RWAMain(trf.getM_nFromNode(), trf.getM_nToNode(), trf.getM_eLayer(), Hop_Lenght, returnList);
			if (route != null && route.getM_lOTNLinkList().size() <= hoplimit)
				trf.setM_cWorkRoute(route);
			rmRepeat(trf, 0);// firesky 2012 4.19
			if (AllocateSource.allocateSource(trf, 0))
			// if(commonAlloc.ResourceAssignment(trf, 0))
			{
				trf.setM_eStatus(TrafficStatus.工作已分配);
				if (trf.getM_eLevel().equals(TrafficLevel.PERMANENT11)
						|| trf.getM_eLevel().equals(TrafficLevel.NORMAL11)) {
					// 保护路由,工作路由经过节点,链路状态设为false,计算SRLG无关保护路由
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
						trf.setM_eStatus(TrafficStatus.工作和保护已分配);
					// 保护路由

					// 恢复设置
					itLink = trf.getM_cWorkRoute().getM_lOTNLinkList().iterator();
					while (itLink.hasNext()) {
						BasicLink theLink = itLink.next();
						theLink.setM_bStatus(true);
					}
				}
			} else {
				// 计算次优路
				isCiyoulu = true;
				route = trf.getM_cWorkRoute();
				Route rt = SuboptimalRoute(trf, route, Hop_Lenght);
				if (rt != null && rt.getM_lOTNLinkList().size() <= hoplimit)
					trf.setM_cWorkRoute(rt);
				rmRepeat(trf, 1);// firesky 2012 4.19
				if (!AllocateSource.allocateSource(trf, 0)) {
					trf.getM_cWorkRoute().clearRoute();
					trf.setM_eStatus(TrafficStatus.阻塞);
				} else
					trf.setM_eStatus(TrafficStatus.工作已分配);
			}
		}
	}

	
	 * 路由算法入口rwa
	 
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

	
	 * 业务首末节点在同一个城域网络域内
	 
	public static Route RWAForInNet(CommonNode from, CommonNode to, int Hop_Lenght) {
		DijkstraAlgorithm.clear();
		int fromNetID = from.getM_nSubnetNum();
		int toNetID = to.getM_nSubnetNum();

		Network fromNet = Network.searchNetwork(fromNetID);
		Network toNet = Network.searchNetwork(toNetID);

		if (fromNet.equals(toNet)) {
			// 若节点在同一个城域网络内，则直接算路
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

	
	 * 业务首末节点在同一个骨干网络域内，一共进行2层3域算路
	 
	public static Route RWAForCrossMainNet(CommonNode from, CommonNode to, int Hop_Lenght) {

		Route route = new Route();
		int fromNetID = from.getM_nSubnetNum();
		int toNetID = to.getM_nSubnetNum();

		Network fromNet = Network.searchNetwork(fromNetID);
		Network toNet = Network.searchNetwork(toNetID);

		Network fromMainNet = fromNet.getM_nUpperNetwork();// 找到对应上层骨干网
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
		// 加入from至上层的级间链路
		route.getM_lLinkList().add(from_to_MainLink);

		// 计算to至入口节点
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

		// 加入to至上层的级间链路
		LinkList.add(to_to_MainLink);

		// 上层算路
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

	
	 * 业务要通过核心网络进行路由，一共要跨3级5域进行算路,域间链路采用随机选路
	 
	public static Route RWAForCrossCoreNet(CommonNode from, CommonNode to, int Hop_Lenght) {
		Route route = new Route();// 保存算路结果

		int fromNetID = from.getM_nSubnetNum();
		int toNetID = to.getM_nSubnetNum();

		Network fromNet = Network.searchNetwork(fromNetID); // 首节点所在的网络
		Network toNet = Network.searchNetwork(toNetID); // 末节点所在的网络
		Network FromMainNet = fromNet.getM_nUpperNetwork(); // 首节点所在的(上层)骨干网络
		Network ToMainNet = toNet.getM_nUpperNetwork(); // 末节点所在的（上层）骨干网络

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
		// 加入from至上层的级间链路
		route.getM_lLinkList().add(from_to_MainLink);

		// 计算to至入口节点
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

		// 加入to至上层的级间链路
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

	
	 * 函数名称：clearWorkRoute 函数作用：清空业务的工作路由 输入参数：需要清空工作路由的业务链表 返回参数：返回节点总数
	 
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

	
	 * 函数名称：clearProtectRoute 函数作用：清空业务的保护路由 输入参数：需要清空保护路由的业务链表 返回参数：true
	 
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

	
	 * 函数名称：clearResumeRoute 函数作用：清空业务的恢复路由 输入参数：需要清空恢复路由的业务链表 返回参数：true
	 
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

	
	 * 路由算法总入口，所有调用算法都从这里开始 输入：from,to,计算所在层layer,算法类型type，算路结果保存在returnList; =1跳数
	 * =2 长度 =3 bsp =4 综合最小 返回：void
	 
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
			// d算法按跳数
			route = RouteAlgorithm(Layer.OTN, from, to, type);
			break;
		case 2:
			// d算法按长度
			route = RouteAlgorithm(Layer.OTN, from, to, type);
			break;
		case 3:
			// bsp算法
			route = RouteAlgorithm(Layer.OTN, from, to, type);
			break;
		case 4:
			// 综合最小
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
			// d算法按跳数
			route = RouteAlgorithm(Layer.WDM, from, to, type);
			break;
		case 2:
			// d算法按长度
			route = RouteAlgorithm(Layer.WDM, from, to, type);
			break;
		case 3:
			// bsp算法
			route = RouteAlgorithm(Layer.WDM, from, to, type);
			break;
		case 4:
			// 综合最小
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
			// d算法按跳数
			route = RouteAlgorithm(Layer.FIBER, from, to, type);
			break;
		case 2:
			// d算法按长度
			route = RouteAlgorithm(Layer.FIBER, from, to, type);
			break;
		case 3:
			// bsp算法
			route = RouteAlgorithm(Layer.FIBER, from, to, type);
			break;
		case 4:
			// 综合最小
			route = RouteAlgorithm(Layer.FIBER, from, to, type);
			break;
		default:
			break;
		}
		return route;
	}

	
	 * 将两路由合为一个路由
	 

	
	 * 反向存路由,将rt反向存在route中
	 
	public static Route ReverseRoute(Layer layer, Route route, Route rt) {// 为何要反向存储路由
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

	
	 * net 类型相同
	 
	public static Route netTypeInSame(CommonNode from, CommonNode to, int type, Layer layer) {
		Route route = null;
		Network FromNet = Network.searchNetwork(from.getM_nSubnetNum());
		Network ToNet = Network.searchNetwork(to.getM_nSubnetNum());
		if (FromNet.equals(ToNet)) {
			route = RWAForInNet(layer, from, to, type);// 两个节点在同一子网中
		} else {
			Network FromMainNet = FromNet.getM_nUpperNetwork();
			Network ToMainNet = ToNet.getM_nUpperNetwork();
			if (FromMainNet.equals(ToMainNet))// 如果上层网络号相同
			{
				route = RWAForCrossMainNet(layer, from, to, type);// 两个节点不在同一子网中，但是有相同的上层网络
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

	
	 * 计算层间路由 type=1 跳数 2长度
	 
	public static void calWDMLayer_IN_FiberLayer(WDMLink wdmlink, int type) {// 没被调用。。
		CommonNode from = wdmlink.getM_cFromNode();
		CommonNode to = wdmlink.getM_cToNode();
		LinkedList returnList = new LinkedList();

		Route route = RWAForWDM(from, to, type, returnList);
		if (route == null)
			return;
		wdmlink.setM_lFiberLinkList(route.getM_lFiberLinkList()); // 计算路由
		// 分配资源

		return;
	}

	
	 * 判断业务路由是否为空，若不为空，返回false
	 
	public static boolean isRouteEmpty(List<Traffic> TrafficList) {
		Iterator<Traffic> it = TrafficList.iterator();
		while (it.hasNext()) {
			Traffic tra = it.next();
			if (tra.getWorkRoute() != null)
				return false;
		}
		return true;
	}

	
	 * 清空业务占用的资源 用于业务规划中的重新规划
	 
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

	
	 * 计算次优路： 策略：当前路由计算失败，且不为空，则必为资源分配失败，那么只需要将资源分配失败的链路删除后再进行一次重路由，成功则继续分配资源 失败则返回
	 
	public static Route SuboptimalRoute(Traffic trf, Route route, int type) {
		Route rt = null;
		List LinkList = new LinkedList();// 用于保存不满足资源要求的链路
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

		// 设置链路状态，将不能满足资源要求的链路标记为false
		it = LinkList.iterator();
		while (it.hasNext()) {
			BasicLink theLink = (BasicLink) it.next();
			theLink.setM_bStatus(false);
		}

		// 调用算法算路
		LinkedList returnList = new LinkedList();
		rt = RWAMain(trf.getM_nFromNode(), trf.getM_nToNode(), trf.getM_eLayer(), type, returnList);

		// 设置链路状态，将不能满足资源要求的链路还原为true
		it = LinkList.iterator();
		while (it.hasNext()) {
			BasicLink theLink = (BasicLink) it.next();
			theLink.setM_bStatus(true);
		}

		return rt;
	}
	
	 * 网络优化算法
	 

	public static void networkOpticAglothms(List<Traffic> trafficList) {
		List<Traffic> trfList = new LinkedList<Traffic>();
		// 将阻塞的业务重新算路，算法为负载均衡
		Iterator<Traffic> it = trafficList.iterator();
		while (it.hasNext()) {
			Traffic trf = it.next();
			if (trf.getM_eStatus().equals(TrafficStatus.阻塞))
				trfList.add(trf);
		}

		RWAForTraffic(trfList, 5);// 负载均衡算法算路

		// 找到链路利用率高于80%的链路承载的业务
		Iterator itLink = FiberLink.s_lFiberLinkList.iterator();
		FiberLink.reflashUtilization();
		while (itLink.hasNext()) {
			FiberLink theLink = (FiberLink) itLink.next();
			if (theLink.getM_dUtilization() > 0.8) {

			}

		}
	}

	
	 * 去除路由中重复节点
	 
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