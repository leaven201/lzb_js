package data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.crypto.Data;

import enums.Layer;
import enums.PortRate;
import enums.Status;
import enums.TrafficRate;

/**
 * WDM����·
 * 
 * @author ����ʫ��
 *
 */
public class WDMLink extends BasicLink implements Serializable {

	private int id;
	private String name;
	private String othername;
	private CommonNode fromNode;// ��·Դ�ڵ�
	private CommonNode toNode;// ��·�޽ڵ�
	private int zhongjiNum;// ��wdm��·����OLA�ڵ���
	private double length;// ��WDM��·�ĳ���
	private double weight;// Ȩ��
	private boolean isActive;// �Ƿ񼤻�
	private int waveNum;// ������
	private int remainResource;// ʣ����Դ����ʣ�ನ����
	private double inPower = 1;// ����⹦�ʣ�dBm��
	private int SumOfOSNR = 0;// ��WDM��·��Ӧ��Fiber����·��OSNRֵ�ܺ�
	private double nrate; // �洢double���͵����ʣ���λ��G��
	private PortRate rate;
	private Layer linkLayer;// ������
	private Port fromPort;// ��ʼ�˿�
	private Port toPort;// �����˿�
	private boolean flexibleRaster;// ���դ�� //@TY�� 2017/9/23
	private int systemRate;// ϵͳ���� //2017/9/23
	private double attenuation;// ����˥��
	private double PMD;
	private int FiberStage = 1;// ���˽׶�
	private String LinkType;// ��������
	private int workUsedWaveNum = 0;// ���ڹ����Ĳ�������
	private int otherUsedWaveNum = 0;// ���������Ĳ�������

	// private List<FiberLink> fiberLinkList = new
	// LinkedList<FiberLink>();//��WDMlink������fiberLink
	private List<WaveLength> waveLengthList = new LinkedList<>();// ���Դ��ÿ��WDMlink�Ĳ�����Ϣ
	private List<FiberLink> fiberLinkList = new LinkedList<FiberLink>();
	private List<Traffic> carriedTrafficList = new LinkedList<>();// ���ص�ҵ��
	public static LinkedList<WDMLink> WDMLinkList = new LinkedList<WDMLink>();// �������WDMLink
	private List<LinkRGroup> wdmRelatedList = new LinkedList<LinkRGroup>(); // ��·���ڵĹ��������·��

	public WDMLink() {
	}

	// ���췽��
	public WDMLink(CommonNode fromNode, CommonNode toNode, int zhongjiNum, double length, boolean isActive) {

		this.fromNode = fromNode;
		this.toNode = toNode;
		this.zhongjiNum = zhongjiNum;
		this.waveNum = DataSave.waveNum;
		this.rate = DataSave.judge;
		this.flexibleRaster = DataSave.flexibleRaster;
		this.nrate = PortRate.Rate2Num(rate);
		this.length = length;
		this.isActive = isActive;
		this.remainResource = waveNum;
		this.id = WDMLinkList.size();
		// wdmlink������������ڵ�ֻ��1��link������Ϊ����-ĩ��������ĩ-�ף�
		if (this.commonFTWDMLinkNum() == 0) {
			this.name = fromNode.getName() + "-" + toNode.getName();
			this.othername = toNode.getName() + "-" + fromNode.getName();
		}
		// ���WDMLinklist������1������ĩ�ڵ����·�����������Ϊ����-ĩ-2������ԭ�������޸�Ϊ����-ĩ-1��
		if (this.commonFTWDMLinkNum() == 1) {
			for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
				WDMLink link = WDMLink.WDMLinkList.get(i);
				if ((this.fromNode.getName().equals(link.getFromNode().getName())
						&& this.toNode.getName().equals(link.getToNode().getName()))
						|| (this.toNode.getName().equals(link.getFromNode().getName())
								&& this.fromNode.getName().equals(link.getToNode().getName()))) {
					link.setName(link.getFromNode().getName() + "-" + link.getToNode().getName() + "-" + 1);
					link.setOthername(link.getToNode().getName() + "-" + link.getFromNode().getName() + "-" + 1);
					this.name = fromNode.getName() + "-" + toNode.getName() + "-" + 2;
					this.othername = toNode.getName() + "-" + fromNode.getName() + 2;
				}
			}
		}
		// ���WDMLinklist������2�������ϸ���ĩ�ڵ����·�����������Ϊ����-ĩ-x��
		if (this.commonFTWDMLinkNum() != 0 && this.commonFTWDMLinkNum() != 1) {
			this.name = fromNode.getName() + "-" + toNode.getName() + "-" + (this.commonFTWDMLinkNum() + 1);
			this.othername = toNode.getName() + "-" + fromNode.getName() + "-" + (this.commonFTWDMLinkNum() + 1);
		}
		fiberLinkList = new LinkedList<FiberLink>();
		WDMLinkList.add(this);
		// ��ʼ��������Դ
		int i = 1;
		while (i < waveNum + 1) { // ϵͳ�����겨��������80�����ͳ�ʼ��80���������뵽WaveLengthList��
			WaveLength wl = new WaveLength(i, this.fromNode, this.toNode, this, Status.����);
			WaveLength.addWaveLengthPort(wl);
			this.getWaveLengthList().add(wl);
			++i;
		}
		// ������·��Ӧ�ڵ�Ķȣ�����ʼ���ڵ��updown
		fromNode.setUpDown();
		toNode.setUpDown();

	}

	// �����·���췽��
	public WDMLink(int id, String name, CommonNode fromNode, CommonNode toNode, double length, Layer linkLayer,
			boolean isActive, double att, double pmd, String linktype, int linkstage) {

		this.id = id;
		this.name = name;
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.linkLayer = linkLayer;
		this.isActive = isActive;
		this.waveNum = DataSave.waveNum;
		this.rate = DataSave.judge;
		this.flexibleRaster = DataSave.flexibleRaster;
		this.nrate = PortRate.Rate2Num(rate);
		this.attenuation = att;
		this.PMD = pmd;
		this.FiberStage = linkstage;
		this.LinkType = linktype;
		this.length = length;

	}

	// public WDMLink(int id, String name, CommonNode fromNode, CommonNode toNode,
	// double length, PortRate rate,
	// boolean isActive,Layer linkLayer) {
	// this.id = id;
	// this.name = name;
	// this.fromNode = fromNode;
	// this.toNode = toNode;
	// this.length = length;
	// this.rate = rate;
	// this.isActive = isActive;
	// this.linkLayer=linkLayer;
	// this.flexibleRaster=DataSave.flexibleRaster;
	// this.systemRate=DataSave.systemRate;
	// this.waveNum=DataSave.waveNum;
	// WDMLinkList.add(this);
	// }

	// ���ع���ʹ�õĲ�����
	public String workedUseWave() {
		List<WaveLength> wavelist = this.getWaveLengthList();
		String a = "";
		for (int i = 0; i < wavelist.size(); i++) {
			if (wavelist.get(i).getStatus() == Status.����)
				a = a + (i + 1) + ",";
		}
		return a;
	}

	// ���ر����ͻָ�ʹ�õĲ�����
	public String otherUseWave() {
		List<WaveLength> wavelist = this.getWaveLengthList();
		String a = "";
		for (int i = 0; i < wavelist.size(); i++) {
			if (wavelist.get(i).getStatus() == Status.���� || wavelist.get(i).getStatus() == Status.�ָ�)
				a = a + (i + 1) + ",";
		}
		return a;
	}

	// ����·���Ƶõ���·
	public static WDMLink getWDMLink(String name) {
		Iterator<WDMLink> it = WDMLink.WDMLinkList.iterator();
		while (it.hasNext()) {
			WDMLink link = it.next();
			if (link.getName().equals(name))
				return link;
		}
		return null;
	}

	// ����·id�õ���·
	public static WDMLink getWDMLink(int id) {
		Iterator<WDMLink> it = WDMLink.WDMLinkList.iterator();
		while (it.hasNext()) {
			WDMLink theLink = it.next();
			if (theLink.getId() == id)
				return theLink;
		}
		return null;
	}

	// ������ĩ�ڵ����������·
	public static WDMLink getWDMLink(String from, String to) {
		Iterator<WDMLink> it = WDMLink.WDMLinkList.iterator();
		while (it.hasNext()) {
			WDMLink link = it.next();
			if ((link.getFromNode().getName().equals(from) && link.getToNode().getName().equals(to))
					|| (link.getFromNode().getName().equals(to) && link.getToNode().getName().equals(from)))
				return link;
		}
		return null;
	}

	// ����·id�õ���·
	public static WDMLink getLinkByID(int id) {
		Iterator<WDMLink> it = WDMLink.WDMLinkList.iterator();
		while (it.hasNext()) {
			WDMLink theLink = it.next();
			if (theLink.getId() == id)
				return theLink;
		}
		return null;
	}
	// //��дtoString����
	// public String toString() {
	// return (this.fromNode.getName() + "����" + this.toNode.getName());
	// }

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

	public String getOthername() {
		return othername;
	}

	public void setOthername(String othername) {
		this.othername = othername;
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

	public boolean isFlexibleRaster() {
		return flexibleRaster;
	}

	public void setFlexibleRaster(boolean flexibleRaster) {
		this.flexibleRaster = flexibleRaster;
	}

	public int getWaveNum() {
		return waveNum;
	}

	public void setWaveNum(int waveNum) {
		this.waveNum = waveNum;
	}

	public int getSystemRate() {
		return systemRate;
	}

	public void setSystemRate(int systemRate) {
		this.systemRate = systemRate;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public static LinkedList<WDMLink> getWDMLinkList() {
		return WDMLinkList;
	}

	public static void setWDMLinkList(LinkedList<WDMLink> wDMLinkList) {
		WDMLinkList = wDMLinkList;
	}

	public List<FiberLink> getFiberLinkList() {
		return fiberLinkList;
	}

	public void setFiberLinkList(List<FiberLink> fiberLinkList) {
		this.fiberLinkList = fiberLinkList;
	}
	// public LinkedList<FiberLink> getfiberLinkList() {
	// return fiberLinkList;
	// }
	//
	// public void setfiberLinkList(LinkedList<FiberLink> fiberLinkList) {
	// fiberLinkList = fiberLinkList;
	// }

	public List<LinkRGroup> getWdmRelatedList() {
		return wdmRelatedList;
	}

	public void setWdmRelatedList(List<LinkRGroup> wdmRelatedList) {
		this.wdmRelatedList = wdmRelatedList;
	}

	public List<Traffic> getCarriedTrafficList() {
		return carriedTrafficList;
	}

	public void setCarriedTrafficList(List<Traffic> carriedTrafficList) {
		this.carriedTrafficList = carriedTrafficList;
	}

	public int getZhongjiNum() {
		return zhongjiNum;
	}

	public void setZhongjiNum(int zhongjiNum) {
		this.zhongjiNum = zhongjiNum;
	}

	public int getRemainResource() {
		return remainResource;
	}

	public void setRemainResource(int remainResource) {
		this.remainResource = remainResource;
	}

	public double getInPower() {
		return inPower;
	}

	public void setInPower(double inPower) {
		this.inPower = inPower;
	}

	public List<WaveLength> getWaveLengthList() {

		return waveLengthList;
	}

	public void setWaveLengthList(List<WaveLength> waveLengthList) {
		this.waveLengthList = waveLengthList;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	// ��ӡ����·�ѱ�ʹ���ڹ����Ĳ���
	public static void printWorkUsedWave(WDMLink link) {
		List<WaveLength> wavelist = link.getWaveLengthList();
		// System.out.print("��������:");
		for (int i = 0; i < wavelist.size(); i++) {
			if (wavelist.get(i).getStatus() == Status.����)
				System.out.print((i + 1) + ",");
		}
		// System.out.println();
	}

	// ��ӡ����·�ѱ�ʹ���ڹ����Ĳ���
	public static void printOtherUsedWave(WDMLink link) {
		List<WaveLength> wavelist = link.getWaveLengthList();
		System.out.print("�������ָ�����:");
		for (int i = 0; i < wavelist.size(); i++) {
			if (wavelist.get(i).getStatus() == Status.����)
				System.out.print((i + 1) + ",");
			if (wavelist.get(i).getStatus() == Status.�ָ�)
				System.out.print((i + 1) + ",");
		}
		System.out.println();
	}

	public int getWorkUsedWaveNum() {

		List<WaveLength> wavelist = this.getWaveLengthList();
		for (int i = 0; i < wavelist.size(); i++) {
			if (wavelist.get(i).getStatus() == Status.����)
				workUsedWaveNum++;
		}
		return workUsedWaveNum;
	}

	public void setWorkUsedWaveNum(int workUsedWaveNum) {
		this.workUsedWaveNum = workUsedWaveNum;
	}

	public int getOtherUsedWaveNum() {
		List<WaveLength> wavelist = this.getWaveLengthList();
		for (int i = 0; i < wavelist.size(); i++) {
			if (wavelist.get(i).getStatus() == Status.���� || wavelist.get(i).getStatus() == Status.�ָ�)
				otherUsedWaveNum++;
		}
		return otherUsedWaveNum;
	}

	public void setOtherUsedWaveNum(int otherUsedWaveNum) {
		this.otherUsedWaveNum = otherUsedWaveNum;
	}

	public static WDMLink getLink(String name) {
		Iterator<WDMLink> it = WDMLink.WDMLinkList.iterator();
		while (it.hasNext()) {
			WDMLink link = it.next();
			if (link.getName().equals(name) || link.getOthername().equals(name))
				return link;
		}
		return null;
	}

	// �ҳ�ͬԴ�޽ڵ��WDMlink������
	public int commonFTWDMLinkNum() {
		int num = 0;
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink link = WDMLink.WDMLinkList.get(i);
			if ((this.fromNode.getName().equals(link.getFromNode().getName())
					&& this.toNode.getName().equals(link.getToNode().getName()))
					|| (this.toNode.getName().equals(link.getFromNode().getName())
							&& this.fromNode.getName().equals(link.getToNode().getName()))) {
				num++;
			}
		}
		return num;
	}
	
    //ͨ������id��ȡ����
	public WaveLength getWavelengthById(int id) {
		List<WaveLength> waveLengthList=this.getWaveLengthList();
		for(int i=0;i<waveLengthList.size();i++) {
			if(waveLengthList.get(i).getID()==id)
				return waveLengthList.get(i);
		}
		return null;
	}

}
