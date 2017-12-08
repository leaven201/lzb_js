package data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import enums.Status;

/**
 * 路由
 * 
 * @author 豹读诗书
 *
 */
public class Route implements Serializable {

	private int id;
	private String name;
	private CommonNode from;
	private CommonNode to;
	private int hops;// 跳数，即链路数
	private Traffic belongsTraffic; // 所属业务
	private boolean isSuccess; // 是否分配路由
	private boolean notConsistent;// 是否波长不一致
	private List<Port> SimPortList; // 模拟仿真占用的端口
	private List<Port> portList; // 占用的端口，包括两个支路端口和hops个的线路端口

	private List<CommonNode> nodeList = new LinkedList<>();// 存储路由中包含的所有非OA节点
	private List<WDMLink> WDMLinkList = new LinkedList<WDMLink>();
	private List<FiberLink> fiberLinkList = new LinkedList<FiberLink>();
	private List<Integer> waveLengthIdList = new LinkedList<>();// 存储路由每条链路占用的波长序号List
	private List<Integer> usedWaveList = new LinkedList<>();// 使用波长,该路由用了几个不同的波长，用以算中继
	private List<CommonNode> waveChangedNode = new LinkedList<>();// 存放波长改变了的节点

	// private int fiberTSID = -1; //新增光纤链路占用的起始时隙，因为时隙一致性所以是一个数
	// private int fiberWLID = -1; // 光纤链路占用的起始波长，因为波长一致性所以是一个数
	// private List<BasicLink> linkList = new LinkedList<BasicLink>();// 存储路由中包含的链路
	// private List<WDMLink2> WDMLinkList2 = new LinkedList<WDMLink2>();
	// private List<OTNLink> OTNLinkList = new LinkedList<OTNLink>();

	// 10.31 lzb+
	// 构造方法
	public Route(CommonNode from, CommonNode to, List<WDMLink> linkList, List<CommonNode> nodeList,
			Traffic belongsTraffic, int flag) {
		this.from = from;
		this.to = to;
		this.WDMLinkList = linkList;
		this.nodeList = nodeList;
		this.belongsTraffic = belongsTraffic;

	}// -

	// 11.27 lzb+
	public Route() {

	}

	public Route(List<WDMLink> linkList, List<CommonNode> nodeList) {
		this.WDMLinkList = linkList;
		this.nodeList = nodeList;
	}

	// 构造方法，用以手工创建业务路由
	public Route(CommonNode from, CommonNode to, List<WDMLink> linkList, List<CommonNode> nodeList) {
		this.from = from;
		this.to = to;
		this.WDMLinkList = linkList;
		this.nodeList = nodeList;

	}

	// 构造方法
	public Route(CommonNode from, CommonNode to, List<FiberLink> fiberLinkList, List<CommonNode> nodeList, int hops,
			Traffic belongsTraffic) {// 主要是用光纤进行算路
		this.from = from;
		this.to = to;
		this.fiberLinkList = fiberLinkList;
		this.nodeList = nodeList;
		this.hops = hops;
		this.belongsTraffic = belongsTraffic;
	}

	// 构造方法
	// public Route(CommonNode from, CommonNode to, List<FiberLink> fiberLinkList,
	// List<CommonNode> nodeList) {// 主要是用光纤进行算路
	// this.from = from;
	// this.to = to;
	// this.fiberLinkList = fiberLinkList;
	// this.nodeList = nodeList;
	// }

	// public void clearRoute(){
	// fiberLinkList.clear();
	// fiberNodeList.clear();
	// WDMLinkList.clear();
	// WDMNodeList.clear();
	// SourceID.clear();
	// portSourceID.clear();
	//
	// }

	// 判断输入的route是否正确
	public static boolean isRouteTrue(String str) {
		String node[] = str.split("-");
		if (node.length != 0) {
			CommonNode no1 = CommonNode.getNode(node[0]);
			CommonNode no2 = CommonNode.getNode(node.length - 1);
		}
		if (node.length == 1)
			return false;
		for (int i = 0; i < (node.length - 1); i++) {
			WDMLink thelink = WDMLink.getWDMLink(node[i], node[i + 1]);
			CommonNode no = CommonNode.getNode(node[i]);
			if (no == null || thelink == null)
				return false;
		}
		return true;
	}

	// 人工创建业务路由-根据输入的字符串生成路由
	public static Route rgRoute(String str) {
		CommonNode from = new CommonNode();
		CommonNode to = new CommonNode();
		List<CommonNode> rgnodelist = new LinkedList<>();
		List<WDMLink> rgWDMLinkList = new LinkedList<WDMLink>();
		String node[] = str.split("-");
		for (int i = 0; i < node.length; i++) {
			CommonNode no = CommonNode.getNode(node[i]);
			if (no != null) {
				rgnodelist.add(no);
			}
		}
		for (int i = 0; i < (node.length - 1); i++) {
			WDMLink thelink = WDMLink.getWDMLink(node[i], node[i + 1]);
			if (thelink != null)
				rgWDMLinkList.add(thelink);
		}
		from = CommonNode.getNode(node[0]);
		to = CommonNode.getNode(node[node.length - 1]);
		Route route = new Route(from, to, rgWDMLinkList, rgnodelist);

		return route;
	}

	// 根据指定字符“-”拆分字符串,并把相应的的节点存入rgnodelist里
	public static List<CommonNode> rgnodelist(String str) {
		List<CommonNode> rgnodelist = new LinkedList<>();
		String node[] = str.split("-");
		for (int i = 0; i < node.length; i++) {
			CommonNode no = CommonNode.getNode(node[i]);
			rgnodelist.add(no);
		}
		return rgnodelist;
	}

	public static List<WDMLink> rgwdmlinklist(String str) {
		List<WDMLink> rgWDMLinkList = new LinkedList<WDMLink>();
		String node[] = str.split("-");
		for (int i = 0; i < (node.length - 1); i++) {
			WDMLink thelink = WDMLink.getWDMLink(node[i], node[i + 1]);
			rgWDMLinkList.add(thelink);
		}
		return rgWDMLinkList;
	}

	// 重写usedWaveList的toString方法
	public String toStingwave() {
		StringBuilder a = new StringBuilder();
		for (int i = 0; i < this.usedWaveList.size(); i++) {
			if (i == 0 && i == (this.usedWaveList.size() - 1)) {
				a = a.append((this.usedWaveList.get(i) + 1));
			} else if (i == 0) {
				a = a.append((this.usedWaveList.get(i) + 1) + ",");
			} else if (i == (this.usedWaveList.size() - 1)) {
				a = a.append((this.usedWaveList.get(i) + 1));
			} else if (i != 0 && i != (this.usedWaveList.size() - 1)) {
				a = a.append((this.usedWaveList.get(i) + 1) + ",");
			}
		}
		return a.toString();
	}

	@Override
	public String toString() {// 重写toString方法，表示打印Route对象时按以下格式打印
		return this.belongsTraffic + ":" + "wdm:" + this.getWDMLinkList();
	}

	// setter、getter方法
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CommonNode getFrom() {
		return from;
	}

	public void setFrom(CommonNode from) {
		this.from = from;
	}

	public CommonNode getTo() {
		return to;
	}

	public void setTo(CommonNode to) {
		this.to = to;
	}

	public int getHops() {
		return hops;
	}

	public void setHops(int hops) {
		this.hops = hops;
	}

	public Traffic getBelongTraffic() {
		return belongsTraffic;
	}

	public void setBelongTraffic(Traffic belongTraffic) {
		this.belongsTraffic = belongTraffic;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public List<CommonNode> getNodeList() {
		return nodeList;
	}

	public void setNodeList(List<CommonNode> nodeList) {
		this.nodeList = nodeList;
	}

	public List<Port> getSimPortList() {
		return SimPortList;
	}

	public void setSimPortList(List<Port> simPortList) {
		SimPortList = simPortList;
	}

	public List<Port> getPortList() {
		return portList;
	}

	public void setPortList(List<Port> portList) {
		this.portList = portList;
	}

	public List<FiberLink> getFiberLinkList() {
		return fiberLinkList;
	}

	public void setFiberLinkList(List<FiberLink> fiberLinkList) {
		this.fiberLinkList = fiberLinkList;
	}

	public Traffic getBelongsTraffic() {
		return belongsTraffic;
	}

	public void setBelongsTraffic(Traffic belongsTraffic) {
		this.belongsTraffic = belongsTraffic;
	}

	public List<WDMLink> getWDMLinkList() {
		return WDMLinkList;
	}

	public void setWDMLinkList(List<WDMLink> wDMLinkList) {
		WDMLinkList = wDMLinkList;
	}

	public List<Integer> getWaveLengthIdList() {
		return waveLengthIdList;
	}

	public static boolean HaveRepeatLink(Route r1, Route r2) {
		List<WDMLink> list1 = r1.WDMLinkList;
		List<WDMLink> list2 = r2.WDMLinkList;
		boolean a = false;
		for (int i = 0; i < list1.size(); i++) {
			WDMLink link1 = list1.get(i);
			for (int j = 0; j < list2.size(); j++) {
				WDMLink link2 = list2.get(j);
				if (link1.getId() == link2.getId()) {
					a = true;
				}
			}
		}
		return a;
	}

	public void setWaveLengthIdList(List<Integer> waveLengthIdList) {
		// for(int i=0;i<waveLengthIdList.size();i++) {
		// waveLengthIdList.set(i, waveLengthIdList.get(i)+1);
		// }
		this.waveLengthIdList = waveLengthIdList;
	}

	public boolean isNotConsistent() {
		return notConsistent;
	}

	public void setNotConsistent(boolean notConsistent) {
		this.notConsistent = notConsistent;
	}

	public void setUsedWaveList(List<Integer> waveLengthIdList) {
		if (waveLengthIdList.size() != 0)
			this.usedWaveList.add(waveLengthIdList.get(0));
		for (int i = 0; i < waveLengthIdList.size(); i++) {
			if (waveLengthIdList.get(i) != this.usedWaveList.get(this.usedWaveList.size() - 1))
				this.usedWaveList.add((waveLengthIdList.get(i)));
		}
	}

	public List<Integer> getUsedWaveList() {
		return usedWaveList;
	}

	// 获得波长转换的节点
	public List<CommonNode> getWaveChangedNode() {
		return waveChangedNode;
	}

	// 工作的
	public void setWaveChangedNode(List<Integer> waveLengthIdList) {
		if (waveLengthIdList.size() != 0) {
			for (int i = 0; i < waveLengthIdList.size() - 1; i++) {
				if (waveLengthIdList.get(i) != waveLengthIdList.get(i + 1)) {
					waveChangedNode.add(this.nodeList.get(i + 1));
					this.nodeList.get(i + 1).setWorkOTUNum();
				}
			}
		}

	}

	// 保护及恢复的
	public void setWaveChangedNode1(List<Integer> waveLengthIdList) {
		if (waveLengthIdList.size() != 0) {
			for (int i = 0; i < waveLengthIdList.size() - 1; i++) {
				if (waveLengthIdList.get(i) != waveLengthIdList.get(i + 1)) {
					waveChangedNode.add(this.nodeList.get(i + 1));
					this.nodeList.get(i + 1).setRestoreOTUNum();
					;
				}
			}
		}

	}

	// 计算路由所经WDMlink的链路总长度
	public double routelength() {
		double len = 0;
		for (int i = 0; i < WDMLinkList.size(); i++) {
			len = len + WDMLinkList.get(i).getLength();
		}
		return len;
	}

	// 某个业务的工作路由如果和预置波长的业务表中的每个业务的工作路由都分离，则这个预置波长可以被共享
	public static boolean isPreWaveCanShared(Traffic tra, List<Traffic> List) {
		for (int i = 0; i < List.size(); i++) {
			if (List.get(i).getWorkRoute() != null && tra.getWorkRoute() != null) {
				if (Route.HaveRepeatLink(tra.getWorkRoute(), List.get(i).getWorkRoute())) {
					return false;
				}
			}
		}
		return true;
	}

	// 拼接有必经节点业务的两个路由route1：from-MustPassNode route2：MustPassNode-to
	public static Route combineRoute(Route route1, Route route2) {
		Route route = new Route();
		if (route1 != null && route2 != null) {
			route.getWDMLinkList().addAll(route1.getWDMLinkList());
			route.getWDMLinkList().addAll(route2.getWDMLinkList());
			route.getNodeList().addAll(route1.getNodeList());
			route.getNodeList().remove(route.getNodeList().size() - 1);
			route.getNodeList().addAll(route2.getNodeList());
		}
		return route;
	}

	// 拼接有必经链路的两个路由route1：from-MustPassLink.fromNode route2：MustPassLink.toNode-to
	public static Route combineRoute1(Route route1, Route route2, WDMLink mustpasslink) {
		Route route = new Route();
		if (route1 != null && route2 != null) {
			route.getWDMLinkList().addAll(route1.getWDMLinkList());
			route.getWDMLinkList().add(mustpasslink);
			route.getWDMLinkList().addAll(route2.getWDMLinkList());
			route.getNodeList().addAll(route1.getNodeList());
			route.getNodeList().addAll(route2.getNodeList());
		}
		if (route1 == null && route2 != null) {
			route.getWDMLinkList().add(mustpasslink);
			route.getWDMLinkList().addAll(route2.getWDMLinkList());
			if (mustpasslink.getFromNode().getName().equals(route2.getNodeList().get(0).getName())) {
				route.getNodeList().add(mustpasslink.getToNode());
			}
			if (!mustpasslink.getFromNode().getName().equals(route2.getNodeList().get(0).getName())) {
				route.getNodeList().add(mustpasslink.getFromNode());
			}
			route.getNodeList().addAll(route2.getNodeList());
		}
		if (route1 != null && route2 == null) {
			route.getWDMLinkList().addAll(route1.getWDMLinkList());
			route.getWDMLinkList().add(mustpasslink);
			route.getNodeList().addAll(route1.getNodeList());
			if (route1.getNodeList().get(route1.getNodeList().size() - 1).getName().equals(mustpasslink.getToNode())) {
				route.getNodeList().add(mustpasslink.getFromNode());
			}
			if (!route1.getNodeList().get(route1.getNodeList().size() - 1).getName().equals(mustpasslink.getToNode()))
				route.getNodeList().add(mustpasslink.getToNode());
		}
		return route;
	}

	// 此方法只用于动态路由中
	// 判断某个路由的链路上是否有可用波道wave,注意：此波道是1-80
	// 两种情况说明波道wave可用，1-wave为空闲 2-wave为恢复，并且该业务的工作路由与wave承载的业务的工作路由分离
	public static boolean isRouteHaveWave(Route route,int wave,Traffic traffic) {
			List<WDMLink> linklist = route.getWDMLinkList(); 
			for(int i=0;i<linklist.size();i++) {
				WDMLink link=linklist.get(i);
				WaveLength wl=link.getWavelengthById(wave);
				//如果wl的状态不是空闲   或者   恢复并且可共享   则输出false
				if(!((isPreWaveCanShared(traffic, wl.getDynamicTrafficList())&&wl.getStatus().equals(Status.恢复))
						||wl.getStatus().equals(Status.空闲)))
					return false;
			}
			return true;
		}

}
