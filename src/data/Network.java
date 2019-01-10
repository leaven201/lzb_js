package data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

// 用于实现不同域的存储（表示不同域（网络））

public class Network implements Serializable {

	public Network(String name, int type, int id) {
		m_nName = name;
		m_nNetType = type;
		m_nId = id;
		m_lInNetNodeList = new LinkedList<CommonNode>();
		m_lInNetLinkList = new LinkedList<BasicLink>();
		m_lGatewayLinkList = new LinkedList<BasicLink>();
		m_lGatewayNodeList = new LinkedList<CommonNode>(); // wx 用于绿地规划 分层节点之间的网关节点
		m_lInNetFiberLinkList = new LinkedList<FiberLink>();
		m_lGatewayFiberLinkList = new LinkedList<FiberLink>();
		m_lInNetWDMLinkList = new LinkedList<WDMLink>();
		m_lGatewayWDMLinkList = new LinkedList<WDMLink>();
		m_lInNetOTNLinkList = new LinkedList<WDMLink>();
		m_lGatewayOTNLinkList = new LinkedList<WDMLink>();
//		m_lInNetSDHLinkList=new LinkedList<SDHLink>();
//		m_lGatewaySDHLinkList=new LinkedList<SDHLink>();
//		m_lInNetASONLinkList=new LinkedList<SDHLink>();
//		m_lGatewayASONLinkList=new LinkedList<SDHLink>();
	}

	private int m_nId; // 网络域号，对应于节点与链路中的子网号
	private String m_nName; // 网络名称
	private int m_nNetType; // 1表示核心网，2表示军区网，3表示军区内城域网

	public int getM_nId() {
		return m_nId;
	}

	public void setM_nId(int m_nId) {
		this.m_nId = m_nId;
	}

	public String getM_nName() {
		return m_nName;
	}

	public void setM_nName(String m_nName) {
		this.m_nName = m_nName;
	}

	public int getM_nNetType() {
		return m_nNetType;
	}

	public void setM_nNetType(int m_nNetType) {
		this.m_nNetType = m_nNetType;
	}

	public List<CommonNode> getM_lInNetNodeList() {
		return m_lInNetNodeList;
	}

	public void setM_lInNetNodeList(List<CommonNode> m_lInNetNodeList) {
		this.m_lInNetNodeList = m_lInNetNodeList;
	}

	public List<BasicLink> getM_lInNetLinkList() {
		return m_lInNetLinkList;
	}

	public void setM_lInNetLinkList(List<BasicLink> m_lInNetLinkList) {
		this.m_lInNetLinkList = m_lInNetLinkList;
	}

	public List<BasicLink> getM_lGatewayLinkList() {
		return m_lGatewayLinkList;
	}

	public void setM_lGatewayLinkList(List<BasicLink> m_lGatewayLinkList) {
		this.m_lGatewayLinkList = m_lGatewayLinkList;
	}

	public Network getM_nUpperNetwork() {
		return m_nUpperNetwork;
	}

	private Network m_nUpperNetwork; // 所属上层网络，即城域属于哪个军区网络，军区网络属于核心网络,核心网为null
	private List<CommonNode> m_lInNetNodeList; // 网络节点
	private List<BasicLink> m_lInNetLinkList; // 网络内部链路
	private List<BasicLink> m_lGatewayLinkList; // 与外部连接的链路，网络间链路,表示与上层网络的连接关系
	private List<FiberLink> m_lInNetFiberLinkList; // 网内fiber链
	private List<FiberLink> m_lGatewayFiberLinkList; // 与外部连接的链路
	private List<WDMLink> m_lInNetWDMLinkList;
	private List<WDMLink> m_lGatewayWDMLinkList;
	private List<WDMLink> m_lInNetOTNLinkList;
	private List<WDMLink> m_lGatewayOTNLinkList;
//	private List<SDHLink> m_lInNetSDHLinkList;
//	private List<SDHLink> m_lGatewaySDHLinkList;
//	private List<SDHLink> m_lInNetASONLinkList;
//	private List<SDHLink> m_lGatewayASONLinkList;
	private List<CommonNode> m_lGatewayNodeList;// wx 12.25

	public List<WDMLink> getM_lInNetWDMLinkList() {
		return m_lInNetWDMLinkList;
	}

	public void setM_lInNetWDMLinkList(List<WDMLink> mLInNetWDMLinkList) {
		m_lInNetWDMLinkList = mLInNetWDMLinkList;
	}

	public List<WDMLink> getM_lGatewayWDMLinkList() {
		return m_lGatewayWDMLinkList;
	}

	public void setM_lGatewayWDMLinkList(List<WDMLink> mLGatewayWDMLinkList) {
		m_lGatewayWDMLinkList = mLGatewayWDMLinkList;
	}

	public List<WDMLink> getM_lInNetOTNLinkList() {
		return m_lInNetOTNLinkList;
	}

	public void setM_lInNetOTNLinkList(List<WDMLink> mLInNetOTNLinkList) {
		m_lInNetOTNLinkList = mLInNetOTNLinkList;
	}

	public List<WDMLink> getM_lGatewayOTNLinkList() {
		return m_lGatewayOTNLinkList;
	}

	public void setM_lGatewayOTNLinkList(List<WDMLink> mLGatewayOTNLinkList) {
		m_lGatewayOTNLinkList = mLGatewayOTNLinkList;
	}

//	public List<SDHLink> getM_lInNetSDHLinkList() {
//		return m_lInNetSDHLinkList;
//	}
//	public void setM_lInNetSDHLinkList(List<SDHLink> mLInNetSDHLinkList) {
//		m_lInNetSDHLinkList = mLInNetSDHLinkList;
//	}
//	public List<SDHLink> getM_lGatewaySDHLinkList() {
//		return m_lGatewaySDHLinkList;
//	}
//	public void setM_lGatewaySDHLinkList(List<SDHLink> mLGatewaySDHLinkList) {
//		m_lGatewaySDHLinkList = mLGatewaySDHLinkList;
//	}
//	public List<SDHLink> getM_lInNetASONLinkList() {
//		return m_lInNetASONLinkList;
//	}
//	public void setM_lInNetASONLinkList(List<SDHLink> mLInNetASONLinkList) {
//		m_lInNetASONLinkList = mLInNetASONLinkList;
//	}
//	public List<SDHLink> getM_lGatewayASONLinkList() {
//		return m_lGatewayASONLinkList;
//	}
//	public void setM_lGatewayASONLinkList(List<SDHLink> mLGatewayASONLinkList) {
//		m_lGatewayASONLinkList = mLGatewayASONLinkList;
//	}
	public List<FiberLink> getM_lInNetFiberLinkList() {
		return m_lInNetFiberLinkList;
	}

	public void setM_lInNetFiberLinkList(List<FiberLink> mLInNetFiberLinkList) {
		m_lInNetFiberLinkList = mLInNetFiberLinkList;
	}

	public List<FiberLink> getM_lGatewayFiberLinkList() {
		return m_lGatewayFiberLinkList;
	}

	public void setM_lGatewayFiberLinkList(List<FiberLink> mLGatewayFiberLinkList) {
		m_lGatewayFiberLinkList = mLGatewayFiberLinkList;
	}

	public List<CommonNode> getM_lGatewayNodeList() {// wx 2013.12.25
		return m_lGatewayNodeList;
	}

	public void setM_lGatewayNodeList(List<CommonNode> mLGatewayNodeList) {// wx 2013.12.25
		m_lGatewayNodeList = mLGatewayNodeList;

	}

	public void addGatewayNodeList(CommonNode node) {
		m_lGatewayNodeList.add(node);// wx 2013.12.25
	}

	public static List<Network> m_lNetworkList = new LinkedList<Network>();

	public void setM_nUpperNetwork(Network net) {
		m_nUpperNetwork = net;
	}

	public void addNode(CommonNode node) {
		m_lInNetNodeList.add(node);
	}

	/*
	 * public void addInNetLink(BasicLink link){ switch(link.getM_eLayer()){ case
	 * Fiber: this.m_lInNetFiberLinkList.add((FiberLink)link);
	 * 
	 * break; case WDM: this.m_lInNetWDMLinkList.add((WDMLink)link); break; case
	 * OTN: this.m_lInNetOTNLinkList.add((WDMLink)link); break; // case SDH: //
	 * this.m_lInNetSDHLinkList.add((SDHLink)link); // break; // case ASON: //
	 * this.m_lInNetASONLinkList.add((SDHLink)link); default: break; } //
	 * m_lInNetLinkList.add(link); }
	 */
//	public void addGatewayLinkList(BasicLink link){
//		switch(link.getM_eLayer()){
//		case Fiber:
//			this.m_lGatewayFiberLinkList.add((FiberLink)link);
//			
//			break;
//		case WDM:
//			this.m_lGatewayWDMLinkList.add((WDMLink)link);
//			break;
//		case OTN:
//			this.m_lGatewayOTNLinkList.add((WDMLink)link);
//			break;
////		case SDH:
////			this.m_lGatewaySDHLinkList.add((SDHLink)link);
////			break;
////		case ASON:
////			this.m_lGatewayASONLinkList.add((SDHLink)link);
//		default:
//			break;
//		}
////		m_lGatewayLinkList.add(link);
//	}
	public void delGatewayLinkList(BasicLink link) {
		m_lGatewayLinkList.remove(link);
	}

	public void delNode(CommonNode node) {
		m_lInNetNodeList.remove(node);
	}

	public void delInNetLink(BasicLink link) {
		m_lInNetLinkList.remove(link);
	}

//	public static void delNetwork(Network net){
//		
//		Iterator<BasicLink> itLink=net.m_lGatewayLinkList.iterator();
//		while(itLink.hasNext()){
//			BasicLink theLink=itLink.next();
//			switch(theLink.getM_eLayer()){
//			case Fiber:
//				FiberLink.fiberLinkList.remove(theLink);
//				break;
//			case WDM:
//				WDMLink.WDMLinkList.remove(theLink);
//				break;
//			case OTN:
//				OTNLink.OTNLinkList.remove(theLink);
//				break;
////			case SDH:
////				SDHLink.s_lSDHLinkList.remove(theLink);
////				break;
////			case ASON:
////				SDHLink.s_lASONLinkList.remove(theLink);
//			default:
//				break;
//			}
//		}
//		
//		itLink=net.m_lInNetLinkList.iterator();
//		while(itLink.hasNext()){
//			BasicLink theLink=itLink.next();
//			switch(theLink.getM_eLayer()){
//			case Fiber:
//				FiberLink.fiberLinkList.remove(theLink);
//				break;
//			case WDM:
//				WDMLink.WDMLinkList.remove(theLink);
//				break;
//			case OTN:
//				OTNLink.OTNLinkList.remove(theLink);
//				break;
////			case SDH:
////				SDHLink.s_lSDHLinkList.remove(theLink);
////				break;
////			case ASON:
////				SDHLink.s_lASONLinkList.remove(theLink);
//			default:
//				break;
//			}
//		}
//		
//		Iterator<CommonNode> itNode=net.m_lInNetNodeList.iterator();
//		while(itNode.hasNext()){
//			CommonNode theNode=itNode.next();
//			CommonNode.allNodeList.remove(theNode);
//		}
//		
//		m_lNetworkList.remove(net);
//	}

	// 通过网络id找到相应网络

	public static Network searchNetwork(int id) {
		Iterator<Network> it = Network.m_lNetworkList.iterator();
		while (it.hasNext()) {
			Network net = it.next();
			if (net.getM_nId() == id)
				return net;
		}
		return null;
	}

	// 找到网络中可用的所有的出口节点

//public List<CommonNode> searchOutNode(){
//	List<CommonNode> nodeList=new LinkedList<CommonNode>();
//	Iterator<BasicLink> it=this.m_lGatewayLinkList.iterator();
//	while(it.hasNext()){
//		BasicLink theLink=it.next();
//		if(theLink.isM_bStatus())
//			if(theLink.getM_cFromNode().getM_nSubnetNum()==this.m_nId)
//			{
//				if(theLink.getM_cFromNode().isM_bStatus())
//					nodeList.add(theLink.getM_cFromNode());
//			}
//			else{
//				if(theLink.getM_cToNode().isM_bStatus())
//					nodeList.add(theLink.getM_cToNode());
//			}
//	}
//	return nodeList;
//}
//public List<CommonNode> searchOutNode(Layer layer){
//	List<CommonNode> nodeList=new LinkedList<CommonNode>();
//	Iterator it=null;
//	switch(layer){
//	case Fiber:
//		it=this.m_lGatewayFiberLinkList.iterator();
//		break;
//	case WDM:
//		it=this.m_lGatewayWDMLinkList.iterator();
//		break;
//	case OTN:
//		it=this.m_lGatewayOTNLinkList.iterator();
//		break;
////	case ASON:
////		it=this.m_lGatewayASONLinkList.iterator();
////		break;
////	case SDH:
////		it=this.m_lGatewaySDHLinkList.iterator();
////		break;
//	default:
//		break;
//	}
//	while(it.hasNext()){
//		BasicLink theLink=(BasicLink)it.next();
//		if(theLink.isM_bStatus()&&theLink.getM_eLayer().equals(layer))
//			if(theLink.getM_cFromNode().getM_nSubnetNum()==this.m_nId)
//			{
//				if(theLink.getM_cFromNode().isM_bStatus()&&!nodeList.contains(theLink.getM_cFromNode()))
//					nodeList.add(theLink.getM_cFromNode());
//			}
//			if(theLink.getM_cToNode().getM_nSubnetNum()==this.m_nId)
//			{
//				if(theLink.getM_cToNode().isM_bStatus()&&!nodeList.contains(theLink.getM_cToNode()))
//					nodeList.add(theLink.getM_cToNode());
//			}
//	}
//	return nodeList;
//}

	// 由出口节点找到出口链路

//	public BasicLink searchOutLink(CommonNode node) {
//		Iterator<BasicLink> it = this.m_lGatewayLinkList.iterator();
//		while (it.hasNext()) {
//			BasicLink theLink = it.next();
//			if (theLink.getM_cFromNode() == node || theLink.getM_cToNode() == node) {
//				return theLink;
//			}
//		}
//		return null;
//	}

//	public BasicLink searchOutLink(CommonNode node, Layer layer, CommonNode tonode) {
//		Iterator it = null;
//		switch (layer) {
//		case Fiber:
//			it = this.getM_lGatewayFiberLinkList().iterator();
//			break;
//		case WDM:
//			it = this.getM_lGatewayWDMLinkList().iterator();
//			break;
//		case OTN:
//			it = this.getM_lGatewayOTNLinkList().iterator();
//			break;
////	case SDH:
////		it=this.getM_lGatewaySDHLinkList().iterator();
////		break;
////	case ASON:
////		it=this.getM_lGatewayASONLinkList().iterator();
////		break;
//		}
//		if (it != null) {
//			List<BasicLink> linklist = new LinkedList<BasicLink>();
//			while (it.hasNext()) {
//				BasicLink theLink = (BasicLink) it.next();
//				if (theLink.isM_bStatus()) {
//					if (theLink.getM_cFromNode() == node || theLink.getM_cToNode() == node) {
//						linklist.add(theLink);
////			return theLink;
//					}
//				}
//			}
//			for (int i = 0; i < linklist.size(); ++i) {
//				BasicLink link = linklist.get(i);
//				if (link.isM_bStatus()) {
//					if (link.getM_cFromNode().equals(tonode) || link.getM_cToNode().equals(tonode))
//						return link;
//				}
//			}
//			if (linklist.size() != 0)
//				return linklist.get(0);
//		}
//		return null;
//	}

//	public BasicLink searchOutLink(CommonNode node, Layer layer) {
//		Iterator it = null;
//		switch (layer) {
//		case Fiber:
//			it = this.getM_lGatewayFiberLinkList().iterator();
//			break;
//		case WDM:
//			it = this.getM_lGatewayWDMLinkList().iterator();
//			break;
//		case OTN:
//			it = this.getM_lGatewayOTNLinkList().iterator();
//			break;
////	case SDH:
////		it=this.getM_lGatewaySDHLinkList().iterator();
////		break;
////	case ASON:
////		it=this.getM_lGatewayASONLinkList().iterator();
////		break;
//		}
//		BasicLink temp = new BasicLink();
//		if (it != null) {
//			double min_W = algorithm.hoplimit;
//
//			while (it.hasNext()) {
//				BasicLink theLink = (BasicLink) it.next();
//				if (theLink.isM_bStatus()) {
//					if (theLink.getM_cFromNode() == node || theLink.getM_cToNode() == node) {
//						// min_W = theLink.getM_dLength();
//						if (min_W > theLink.getM_dLength()) {
//							temp = theLink;
//							min_W = theLink.getM_dLength();
//						}
//					}
//				}
//			}
//		}
//		return temp;
//	}
}
