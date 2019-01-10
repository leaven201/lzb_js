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
	private String englishname;
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
	private boolean isBroken = true;
	private int[] dynUsedLink = new int[81];//ͳ�ƶ�̬��·��ʱʹ�õĲ�����0��δʹ�� 1��ʹ�ã���1��80��
	private boolean[] solts = new boolean[320];//Ƶ϶����
	
	public boolean[] getSolts() {
		return solts;
	}

	public void setSolts(boolean[] solts) {
		this.solts = solts;
	}
	private String unactiveReason= "" ;    //Ϊ����ʱ����������ԭ��

	// private List<FiberLink> fiberLinkList = new
	// LinkedList<FiberLink>();//��WDMlink������fiberLink
	private List<WaveLength> waveLengthList = new LinkedList<>();// ���Դ��ÿ��WDMlink�Ĳ�����Ϣ
	private List<FiberLink> fiberLinkList = new LinkedList<FiberLink>();
	private List<Traffic> carriedTrafficList = new LinkedList<>();// ���ص�ҵ��
	public static LinkedList<WDMLink> WDMLinkList = new LinkedList<WDMLink>();// �������WDMLink
	private List<LinkRGroup> wdmRelatedList = new LinkedList<LinkRGroup>(); // ��·���ڵĹ��������·��
	public LinkedList<WDMLink> parallelLinkList = new LinkedList<>(); // ���ƽ�б�

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
			this.englishname = fromNode.getOtherName() + "" + toNode.getOtherName();
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
					link.setEnglishname(fromNode.getOtherName() + "" + toNode.getOtherName()+"" + 1);
					this.name = fromNode.getName() + "-" + toNode.getName() + "-" + 2;
					this.othername = toNode.getName() + "-" + fromNode.getName() + "-" + 2;
					this.englishname = fromNode.getOtherName() + "" + toNode.getOtherName() + "" + 2;
				}
			}
		}
		// ���WDMLinklist������2�������ϸ���ĩ�ڵ����·�����������Ϊ����-ĩ-x��
		if (this.commonFTWDMLinkNum() != 0 && this.commonFTWDMLinkNum() != 1) {
			this.name = fromNode.getName() + "-" + toNode.getName() + "-" + (this.commonFTWDMLinkNum() + 1);
			this.othername = toNode.getName() + "-" + fromNode.getName() + "-" + (this.commonFTWDMLinkNum() + 1);
			this.englishname = fromNode.getOtherName() + "" + toNode.getOtherName() + "" + (this.commonFTWDMLinkNum() + 1);
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
		if (a.equals(""))
			return a;
		else
			return a.substring(0, a.length() - 1);
	}

	// ���ر����ͻָ�ʹ�õĲ�����
	public String otherUseWave() {
		List<WaveLength> wavelist = this.getWaveLengthList();
		String a = "";
		for (int i = 0; i < wavelist.size(); i++) {
			if (wavelist.get(i).getStatus() == Status.���� || wavelist.get(i).getStatus() == Status.�ָ�)
				a = a + (i + 1) + ",";
		}
		if (a.equals(""))
			return a;
		else
			return a.substring(0, a.length() - 1);
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
	public static List<WDMLink> getWDMLink(CommonNode from, CommonNode to){
		List<WDMLink> res = new LinkedList<>();
		for(WDMLink link : WDMLink.WDMLinkList) {
			if ((link.getFromNode().equals(from) && link.getToNode().equals(to))
					|| (link.getFromNode().equals(to) && link.getToNode().equals(from))) {
				res.add(link);
			}
		}
		return res;
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

	public int[] getDynUsedLink() {
		return dynUsedLink;
	}

	public void setDynUsedLink(int[] dynUsedLink) {
		this.dynUsedLink = dynUsedLink;
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
	

	public String getEnglishname() {
		return englishname;
	}

	public void setEnglishname(String englishname) {
		this.englishname = englishname;
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

	public boolean isBroken() {
		return isBroken;
	}

	public void setBroken(boolean isBroken) {
		this.isBroken = isBroken;
	}

	public List<WaveLength> getWaveLengthList() {

		return waveLengthList;
	}

	public void setWaveLengthList(List<WaveLength> waveLengthList) {
		this.waveLengthList = waveLengthList;
	}
	

	public String getUnactiveReason() {
		return unactiveReason;
	}

	public void setUnactiveReason(String unactiveReason) {
		this.unactiveReason = unactiveReason;
	}
	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public LinkedList<WDMLink> getParallelLinkList() {
		return parallelLinkList;
	}

	public void setParallelLinkList(LinkedList<WDMLink> parallelLinkList) {
		this.parallelLinkList = parallelLinkList;
	}

	// ��ӡ����·�ѱ�ʹ���ڹ����Ĳ���
	public static void printWorkUsedWave(WDMLink link) {
		List<WaveLength> wavelist = link.getWaveLengthList();
		System.out.print("��������:");
		for (int i = 0; i < wavelist.size(); i++) {
			if (wavelist.get(i).getStatus() == Status.����)
				System.out.print((i + 1) + ",");
		}
		// System.out.println();
	}

	// ��ӡ����·�ѱ�ʹ���ڻָ��Ĳ���
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

	// ͨ������id��ȡ����
	public WaveLength getWavelengthById(int id) {
		List<WaveLength> waveLengthList = this.getWaveLengthList();
		for (int i = 0; i < waveLengthList.size(); i++) {
			if (waveLengthList.get(i).getID() == id)
				return waveLengthList.get(i);
		}
		return null;
	}

	// ����parallelLinkList
	public void createParallelLinkList() {
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink link = WDMLink.WDMLinkList.get(i);
			if ((this.getFromNode().getName().equals(link.getFromNode().getName())
					&& this.getToNode().getName().equals(link.getToNode().getName()))
					|| (this.getFromNode().getName().equals(link.getToNode().getName())
							&& this.getToNode().getName().equals(link.getFromNode().getName()))) {
				this.parallelLinkList.add(link);
			}
		}
	}
	//����������·����Դ��Ϣ
		public static void updateAllLinkResource() {
			for(int i=0; i<WDMLink.WDMLinkList.size();i++) {
				WDMLink link = WDMLink.WDMLinkList.get(i);
				link.updateLinkResoucre();
			}
		}

	// ������·��Դ
	public void updateLinkResoucre() {
		int remain = 0;
		for (int i = 0; i < this.getWaveLengthList().size(); i++) {
			WaveLength wl = this.getWaveLengthList().get(i);
			if (wl.getStatus().equals(Status.����))
				remain = remain + 1;
		}
		this.setRemainResource(remain);
	}
	
	//�鿴��·�ж��ٲ���������Ԥ��
		public int preResource() {
			int preResource = 0;
			for (int i = 0; i < this.getWaveLengthList().size(); i++) {
				WaveLength wl = this.getWaveLengthList().get(i);
				if (wl.getStatus().equals(Status.�ָ�))
					preResource++;
			}
			return preResource;
		}

	// �ж�ĳ��������Դn�ǲ���ÿ����·��ƽ����·ֻҪ��һ���о��У����С�
	public static boolean isAllLinkHaveN(int n) {
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			// û��ƽ����·
			if (WDMLink.WDMLinkList.get(i).getParallelLinkList().size() == 1) {
				// �������n���ǿ����򷵻�false
				if (!WDMLink.WDMLinkList.get(i).getWavelengthById(n).getStatus().equals(Status.����))
					return false;
			}
			// ��ƽ����·
			if (WDMLink.WDMLinkList.get(i).getParallelLinkList().size() > 1) {
				for (int j = 0; j < WDMLink.WDMLinkList.get(i).getParallelLinkList().size(); j++) {
					if (WDMLink.WDMLinkList.get(i).getParallelLinkList().get(j).getWavelengthById(n).getStatus()
							.equals(Status.����))
						break;
					if (j == WDMLink.WDMLinkList.get(i).getParallelLinkList().size() - 1 && (!WDMLink.WDMLinkList.get(i)
							.getParallelLinkList().get(j).getWavelengthById(n).getStatus().equals(Status.����)))
						return false;
				}
			}
		}
		return true;
	}

	// Ѱ��ȫ�����ĸ�������Դ��������·���еģ�Ϊ�˲�ʹ���м�,ƽ����·��ֻ����һ���о���
	public static LinkedList<Integer> allLinkHaveWaveNum() {
		LinkedList<Integer> waves = new LinkedList<>();
		for (int i = 1; i < 81; i++) {
			if (WDMLink.isAllLinkHaveN(i))
				waves.add(i);
		}
		return waves;
	}

	// ���ظ�wdmlink���ڵ�SRLG��
	public LinkRGroup belongSRLG() {
		// ��������SRLG��
		for (int i = 0; i < LinkRGroup.SRLGroupList.size(); i++) {
			// ����ÿ��SRLG���е�����wdmlink
			if (LinkRGroup.SRLGroupList.get(i).getSRLGWDMLinkList() != null
					&& LinkRGroup.SRLGroupList.get(i).getSRLGWDMLinkList().size() != 0) {
				for (int j = 0; j < LinkRGroup.SRLGroupList.get(i).getSRLGWDMLinkList().size(); j++) {
					if (LinkRGroup.SRLGroupList.get(i).getSRLGWDMLinkList().get(j).getName().equals(this.getName()))
						return LinkRGroup.SRLGroupList.get(i);
				}
			}
		}
		return null;
	}

	// �жϸ���·�Ƿ񲨵�i���� flag=1(����/����) flag=2(Ԥ��)
	public boolean isLinkHaveWavei(int waveNum, int flag, Traffic tra) {
		if (flag == 1) {
			if (this.getWavelengthById(waveNum).getStatus().equals(Status.����))
				return true;
		}
		if (flag == 2) {
			if ((this.getWavelengthById(waveNum).getStatus().equals(Status.����))
					|| ((this.getWavelengthById(waveNum).getStatus().equals(Status.�ָ�))
							&& Route.isPreWaveCanShared(tra, this.getWavelengthById(waveNum).preTrafficList)))
				return true;
		}
		return false;
	}
	
	//������wdmlink��fiberlink��1��1�Ĺ�ϵ������Ӣ��������fiberlink����ͬ��
	public static void setwdmengname() {

		for(int i=0; i<WDMLink.WDMLinkList.size();i++) {
			WDMLink link = WDMLink.WDMLinkList.get(i);
			if(link.getFiberLinkList().size()==1) {
				link.setEnglishname(link.getFiberLinkList().get(0).getName());
			}else if(link.getFiberLinkList().size()>1) {
				if(link.getFiberLinkList().get(0).getName().equals(link.getFiberLinkList().get(1).getName())) {
					link.setEnglishname(link.getFiberLinkList().get(0).getName());
				}
			}
		}	
	}
	//������·��ƽ������
	public static double aveLength() {
		int sum = 0;
		for(int i=0; i<WDMLink.WDMLinkList.size();i++) {
			sum += WDMLink.WDMLinkList.get(i).getLength();
		}
		double ave = sum/WDMLink.WDMLinkList.size();
		return ave;
	}

}
