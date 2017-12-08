package data;

import java.io.Serializable;
import java.util.LinkedList;

import datastructure.UIWDMLink;
import enums.Layer;
import enums.PortRate;

/**
 * �����·
 * @author ����ʫ��
 *
 */
public class OTNLink extends BasicLink implements Serializable {
    
    private int id;
    private String name;
    private Layer linkLayer;//������
    private CommonNode fromNode;//��·Դ�ڵ�
    private CommonNode toNode;//��·�޽ڵ�
    private Port fromPort;// ��ʼ�˿�
    private Port toPort;// �����˿�
    private double length;
    private double nrate; // �洢double���͵����ʣ���λ��G��
    private PortRate rate;
    private boolean isActive;// �Ƿ񼤻�
    
    public static LinkedList<OTNLink> OTNLinkList = new LinkedList<OTNLink>();
    

    public OTNLink(int id, String name, CommonNode fromNode, CommonNode toNode, double length, PortRate rate,
	    boolean isActive,Layer linkLayer) {
	this.id = id;
	this.name = name;
	this.fromNode = fromNode;
	this.toNode = toNode;
	this.length = length;
	this.rate = rate;
	this.isActive = isActive;
	this.linkLayer=linkLayer;
	
	OTNLinkList.add(this);
    }


    public OTNLink(OTNLink next) {
		// TODO Auto-generated constructor stub
	}


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


    public Layer getLinkLayer() {
        return linkLayer;
    }


    public void setLinkLayer(Layer linkLayer) {
        this.linkLayer = linkLayer;
    }


    public CommonNode getFromNode() {
        return fromNode;
    }


    public void setFromNode(CommonNode fromNode) {
        this.fromNode = fromNode;
    }


    public CommonNode getToNode() {
        return toNode;
    }


    public void setToNode(CommonNode toNode) {
        this.toNode = toNode;
    }


    public Port getFromPort() {
        return fromPort;
    }


    public void setFromPort(Port fromPort) {
        this.fromPort = fromPort;
    }


    public Port getToPort() {
        return toPort;
    }


    public void setToPort(Port toPort) {
        this.toPort = toPort;
    }


    public double getLength() {
        return length;
    }


    public void setLength(double length) {
        this.length = length;
    }


    public double getNrate() {
        return nrate;
    }


    public void setNrate(double nrate) {
        this.nrate = nrate;
    }


    public PortRate getRate() {
        return rate;
    }


    public void setRate(PortRate rate) {
        this.rate = rate;
    }


    public boolean isActive() {
        return isActive;
    }


    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }


    public static LinkedList<OTNLink> getOTNLinkList() {
        return OTNLinkList;
    }


    public static void setOTNLinkList(LinkedList<OTNLink> OTNLinkList) {
        OTNLinkList = OTNLinkList;
    }


	public static UIWDMLink next() {
		// TODO Auto-generated method stub
		return null;
	}
    
}
