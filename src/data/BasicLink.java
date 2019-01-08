package data;

import java.util.Iterator;
/**
 * 链路
 */
import java.util.LinkedList;
import java.util.List;
import enums.*;

public class BasicLink {

    private int id;
    private String name;
    private CommonNode fromNode;//链路源节点
    private CommonNode toNode;//链路宿节点
    private boolean isActive;// 是否激活    
    private int carriedTrafficNum;// 承载业务的数量
    private static double EARTH_RADIUS = 6378.137; // 地球半径
    private Layer linkLayer;//所属层
    
//    private Port fromPort;// 起始端口
//    private Port toPort;// 结束端口
    private double length;
//    private double nrate; // 存储double类型的速率，单位是G；
//    private PortRate rate;
//    private int remainResource;// 链路空闲资源
    private int size;// 链路含有下层低粒度的数量,波道数
//    private double weight;// 权重
//    private String SRLG;
//    private int waveNum;//波道数
    private List<BasicLink> groupSRLG=new LinkedList<>();//共享风险链路组
//    private static List<Traffic> carriedTraffic = new LinkedList<Traffic>();// 承载的业务
//    public static List<BasicLink> allLinkList=new LinkedList<>();//存放所有链路
//    public static List<BasicLink> allOptLinkList=new LinkedList<>();
//    public static List<BasicLink> allEleLinkList=new LinkedList<>();
//    public static List<BasicLink> allFiberLinkList=new LinkedList<>();   
//    private LinkedList<WaveLength> waveLengthList = new LinkedList<WaveLength>();

    
    //无参构造方法
    public BasicLink(){
	
    }


//    //判断link的id是否重复
//    public static boolean isLinkIDRepeat(int id) {
//	Iterator<BasicLink> it = BasicLink.allLinkList.iterator();
//	while (it.hasNext()) {
//	    BasicLink basicLink = it.next();
//	    if (basicLink.getId() == id)
//		return true;
//	}
//	return false;
//    }
//
//    // 判断链路的名称是否重复
//    public static boolean isLinkNameRepeat(String name) {
//	Iterator<BasicLink> it = BasicLink.allLinkList.iterator();
//	while (it.hasNext()) {
//	    BasicLink basicLink = it.next();
//	    if (basicLink.getName().equals(name))
//		return true;
//	}
//	return false;
//    }
    
    //角度转换为弧度
    private static double rad(double d) {
	return d * Math.PI / 180.0;
    }
    
    //根据经纬度求链路长度
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
	double radLat1 = rad(lat1);
	double radLat2 = rad(lat2);
	double a = radLat1 - radLat2;
	double b = rad(lng1) - rad(lng2);
	double s = 2 * Math.asin(Math.sqrt(
			Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
	s = s * EARTH_RADIUS;
	double s1 = Math.round(s * 10) ;
//	double s2 = s1 / 10;
	s = s1 / 10;//保留一位小数 CC 11.24
	return s;
    }
    
//    //为链路的两端节点添加线路端口
//    public static void addFiberLinkPort(FiberLink link) {
//	CommonNode from = link.getFromNode();
//	CommonNode to = link.getToNode();
//	int fromportid = 0;
//	int toportid = 0;
//	fromportid = from.getFiberPortList().size() + 1;
//	toportid = to.getFiberPortList().size() + 1;
//	//始端口
//	Port fromPort = new Port(fromportid, from, link,PortType.OTN, link.getRate(), PortKind.线路端口, PortStatus.空闲);
//	from.addPort(fromPort);
//	link.setFromPort(fromPort);
//	fromPort.getCarriedLinkList().add(link);
//	fromPort.setDirection(to);
//	//末端口
//	Port toPort = new Port(toportid, to, link,PortType.OTN, link.getRate(), PortKind.线路端口, PortStatus.空闲);
//	to.addPort(toPort);
//	link.setToPort(toPort);
//	toPort.getCarriedLinkList().add(link);
//	toPort.setDirection(from);
//    }
//    
//  //为光层链路的两端节点添加线路端口
//    public static void addOptLinkPort(WDMLink link) {
//	CommonNode from = link.getFromNode();
//	CommonNode to = link.getToNode();
//	int fromportid = 0;
//	int toportid = 0;
//	fromportid = from.getOptPortList().size() + 1;
//	toportid = to.getOptPortList().size() + 1;
//	//始端口
//	Port fromPort = new Port(fromportid, from, link,PortType.Fiber, link.getRate(), PortKind.线路端口, PortStatus.空闲);
//	from.addPort(fromPort);
//	link.setFromPort(fromPort);
//	fromPort.getCarriedLinkList().add(link);
//	fromPort.setDirection(to);
//	//末端口
//	Port toPort = new Port(toportid, to, link,PortType.Fiber, link.getRate(), PortKind.线路端口, PortStatus.空闲);
//	to.addPort(toPort);
//	link.setToPort(toPort);
//	toPort.getCarriedLinkList().add(link);
//	toPort.setDirection(from);
//    }
//    
//  //为电层链路的两端节点添加线路端口
//    public static void addEleLinkPort(OTNLink link) {
//	CommonNode from = link.getFromNode();
//	CommonNode to = link.getToNode();
//	int fromportid = 0;
//	int toportid = 0;
//	fromportid = from.getElePortList().size() + 1;
//	toportid = to.getElePortList().size() + 1;
//	//始端口
//	Port fromPort = new Port(fromportid, from, link,PortType.WDM, link.getRate(), PortKind.线路端口, PortStatus.空闲);
//	from.addPort(fromPort);
//	link.setFromPort(fromPort);
//	fromPort.getCarriedLinkList().add(link);
//	fromPort.setDirection(to);
//	//末端口
//	Port toPort = new Port(toportid, to, link,PortType.WDM, link.getRate(), PortKind.线路端口, PortStatus.空闲);
//	to.addPort(toPort);
//	link.setToPort(toPort);
//	toPort.getCarriedLinkList().add(link);
//	toPort.setDirection(from);
//    }
    
    
    @Override
	public String toString() {
		return this.getName();
	}
    
    //setter、getter方法
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
    public boolean isActive() {
    	return isActive;
    }
    public void setActive(boolean isActive) {
    	this.isActive = isActive;
    }
    public void setLinkLayer(Layer linkLayer) {
        this.linkLayer = linkLayer;
    }
    public Layer getLinkLayer() {
        return linkLayer;
    }
    public CommonNode getFromNode() {
        return fromNode;
    }
    public void setFromNode(CommonNode fromNode) {
        this.fromNode = fromNode;
    }
    public int getCarriedTrafficNum() {
    	return carriedTrafficNum;
    }
    public void setCarriedTrafficNum(int carriedTrafficNum) {
    	this.carriedTrafficNum = carriedTrafficNum;
    }
    public CommonNode getToNode() {
        return toNode;
    }
    public void setToNode(CommonNode toNode) {
        this.toNode = toNode;
    }
    public double getLength() {
        return length;
    }
    public void setLength(double length) {
        this.length = length;
    }
//    public double getNrate() {
//        return nrate;
//    }
//    public void setNrate(float nrate) {
//        this.nrate = nrate;
//    }
//    public PortRate getRate() {
//        return rate;
//    }
//    public void setRate(PortRate rate) {
//        this.rate = rate;
//    }
//    public static List<Traffic> getCarriedTraffic() {
//        return carriedTraffic;
//    }
//    public void setCarriedTraffic(List<Traffic> carriedTraffic) {
//        this.carriedTraffic = carriedTraffic;
//    }
//    public int getRemainResource() {
//        return remainResource;
//    }
//    public void setRemainResource(int remainResource) {
//        this.remainResource = remainResource;
//    }
    public int getSize() {
        return size;
    }
    public void setSize(int size) {
        this.size = size;
    }
//    public Port getFromPort() {
//        return fromPort;
//    }
//    public void setFromPort(Port fromPort) {
//        this.fromPort = fromPort;
//    }
//    public Port getToPort() {
//        return toPort;
//    }
//    public void setToPort(Port toPort) {
//        this.toPort = toPort;
//    }
    public List<BasicLink> getGroupSRLG() {
        return groupSRLG;
    }
    public void setGroupSRLG(List<BasicLink> groupSRLG) {
        this.groupSRLG = groupSRLG;
    }
//    public void setNrate(double nrate) {
//        this.nrate = nrate;
//    }
//    public double getWeight() {
//        return weight;
//    }
//    public void setWeight(double weight) {
//        this.weight = weight;
//    }
//    public String getSRLG() {
//        return SRLG;
//    }
//    public void setSRLG(String sRLG) {
//        SRLG = sRLG;
//    }
//    public Layer getM_eLayer() {
//		return linkLayer;
//	}
//    public boolean isM_bStatus() {
//		return isActive;
//	}
//    public CommonNode getM_cFromNode() {
//		return fromNode;
//	}
//    public CommonNode getM_cToNode() {
//		return toNode;
//	}
//    public double getM_dLength() {
//		return length;
//	}
}
