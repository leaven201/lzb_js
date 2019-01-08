package data;

import java.util.Iterator;
/**
 * ��·
 */
import java.util.LinkedList;
import java.util.List;
import enums.*;

public class BasicLink {

    private int id;
    private String name;
    private CommonNode fromNode;//��·Դ�ڵ�
    private CommonNode toNode;//��·�޽ڵ�
    private boolean isActive;// �Ƿ񼤻�    
    private int carriedTrafficNum;// ����ҵ�������
    private static double EARTH_RADIUS = 6378.137; // ����뾶
    private Layer linkLayer;//������
    
//    private Port fromPort;// ��ʼ�˿�
//    private Port toPort;// �����˿�
    private double length;
//    private double nrate; // �洢double���͵����ʣ���λ��G��
//    private PortRate rate;
//    private int remainResource;// ��·������Դ
    private int size;// ��·�����²�����ȵ�����,������
//    private double weight;// Ȩ��
//    private String SRLG;
//    private int waveNum;//������
    private List<BasicLink> groupSRLG=new LinkedList<>();//���������·��
//    private static List<Traffic> carriedTraffic = new LinkedList<Traffic>();// ���ص�ҵ��
//    public static List<BasicLink> allLinkList=new LinkedList<>();//���������·
//    public static List<BasicLink> allOptLinkList=new LinkedList<>();
//    public static List<BasicLink> allEleLinkList=new LinkedList<>();
//    public static List<BasicLink> allFiberLinkList=new LinkedList<>();   
//    private LinkedList<WaveLength> waveLengthList = new LinkedList<WaveLength>();

    
    //�޲ι��췽��
    public BasicLink(){
	
    }


//    //�ж�link��id�Ƿ��ظ�
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
//    // �ж���·�������Ƿ��ظ�
//    public static boolean isLinkNameRepeat(String name) {
//	Iterator<BasicLink> it = BasicLink.allLinkList.iterator();
//	while (it.hasNext()) {
//	    BasicLink basicLink = it.next();
//	    if (basicLink.getName().equals(name))
//		return true;
//	}
//	return false;
//    }
    
    //�Ƕ�ת��Ϊ����
    private static double rad(double d) {
	return d * Math.PI / 180.0;
    }
    
    //���ݾ�γ������·����
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
	s = s1 / 10;//����һλС�� CC 11.24
	return s;
    }
    
//    //Ϊ��·�����˽ڵ������·�˿�
//    public static void addFiberLinkPort(FiberLink link) {
//	CommonNode from = link.getFromNode();
//	CommonNode to = link.getToNode();
//	int fromportid = 0;
//	int toportid = 0;
//	fromportid = from.getFiberPortList().size() + 1;
//	toportid = to.getFiberPortList().size() + 1;
//	//ʼ�˿�
//	Port fromPort = new Port(fromportid, from, link,PortType.OTN, link.getRate(), PortKind.��·�˿�, PortStatus.����);
//	from.addPort(fromPort);
//	link.setFromPort(fromPort);
//	fromPort.getCarriedLinkList().add(link);
//	fromPort.setDirection(to);
//	//ĩ�˿�
//	Port toPort = new Port(toportid, to, link,PortType.OTN, link.getRate(), PortKind.��·�˿�, PortStatus.����);
//	to.addPort(toPort);
//	link.setToPort(toPort);
//	toPort.getCarriedLinkList().add(link);
//	toPort.setDirection(from);
//    }
//    
//  //Ϊ�����·�����˽ڵ������·�˿�
//    public static void addOptLinkPort(WDMLink link) {
//	CommonNode from = link.getFromNode();
//	CommonNode to = link.getToNode();
//	int fromportid = 0;
//	int toportid = 0;
//	fromportid = from.getOptPortList().size() + 1;
//	toportid = to.getOptPortList().size() + 1;
//	//ʼ�˿�
//	Port fromPort = new Port(fromportid, from, link,PortType.Fiber, link.getRate(), PortKind.��·�˿�, PortStatus.����);
//	from.addPort(fromPort);
//	link.setFromPort(fromPort);
//	fromPort.getCarriedLinkList().add(link);
//	fromPort.setDirection(to);
//	//ĩ�˿�
//	Port toPort = new Port(toportid, to, link,PortType.Fiber, link.getRate(), PortKind.��·�˿�, PortStatus.����);
//	to.addPort(toPort);
//	link.setToPort(toPort);
//	toPort.getCarriedLinkList().add(link);
//	toPort.setDirection(from);
//    }
//    
//  //Ϊ�����·�����˽ڵ������·�˿�
//    public static void addEleLinkPort(OTNLink link) {
//	CommonNode from = link.getFromNode();
//	CommonNode to = link.getToNode();
//	int fromportid = 0;
//	int toportid = 0;
//	fromportid = from.getElePortList().size() + 1;
//	toportid = to.getElePortList().size() + 1;
//	//ʼ�˿�
//	Port fromPort = new Port(fromportid, from, link,PortType.WDM, link.getRate(), PortKind.��·�˿�, PortStatus.����);
//	from.addPort(fromPort);
//	link.setFromPort(fromPort);
//	fromPort.getCarriedLinkList().add(link);
//	fromPort.setDirection(to);
//	//ĩ�˿�
//	Port toPort = new Port(toportid, to, link,PortType.WDM, link.getRate(), PortKind.��·�˿�, PortStatus.����);
//	to.addPort(toPort);
//	link.setToPort(toPort);
//	toPort.getCarriedLinkList().add(link);
//	toPort.setDirection(from);
//    }
    
    
    @Override
	public String toString() {
		return this.getName();
	}
    
    //setter��getter����
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
