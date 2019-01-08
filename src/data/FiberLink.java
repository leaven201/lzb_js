package data;

import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import enums.Layer;
import enums.PortRate;

/**
 * ���˲���·
 * 
 * @author ����ʫ��
 *
 */
public class FiberLink extends BasicLink implements Serializable {

	private int id;
	private String name;
	private Layer linkLayer;// ������
	private CommonNode fromNode;// ��·Դ�ڵ�
	private CommonNode toNode;// ��·�޽ڵ�
	private double length;
	private boolean isActive;// �Ƿ񼤻�
	private int remainResource;// ��·������Դ
	private String SRLG;
	private int waveNum;// ������
	private double inPower = 1;
	private double spanLoss;
	private double NF;
	public double attenuation;// ����˥��
	private String LinkType;// ��������
	private double PMD;// ����PMD
	private int FiberStage;// ���˽׶�
	public double inputPower = 1;//����ƽ�����˹⹦��
	public double OSNRCount;// ��fiber��·OSNR����ֵ
	private double Dissipation;// ��Ч�����
	private int size;

	private List<WDMLink> carriedWDMLinkList = new LinkedList<>(); // ���ص��ϲ�wdmLinkList
	private LinkedList<WaveLength> waveLengthList = new LinkedList<WaveLength>();
	public static LinkedList<FiberLink> fiberLinkList = new LinkedList<FiberLink>();// �������fiberlink
	private List<LinkRGroup> fiberRelatedList = new LinkedList<LinkRGroup>(); // ��·���ڵĹ��������·��

	public FiberLink() {

	}

	public FiberLink(int id, String name, CommonNode fromNode, CommonNode toNode, double length, String sRLG,
			double attenuation, String linkType, double pMD, int FiberStage, boolean isActive) {
		this.id = id;
		this.name = name;
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.length = length;
		SRLG = sRLG;
		this.attenuation = attenuation;
		this.LinkType = linkType;
		this.PMD = pMD;
		this.FiberStage = FiberStage;
		this.isActive = isActive;
		OSNRcountHuaWei();
		fiberLinkList.add(this);
		// int i = 1;
		// while (i < waveNum + 1) {
		// WaveLength wl = new WaveLength(i, this);
		// this.getWaveLengthList().add(wl);
		// ++i;
		// }
		//

	}

	public double getOSNRCount() {
		double K = data.DataSave.OSC;
		double J = data.DataSave.road;
		double I = this.attenuation;
		double zengyi = I + J + K;
		double Q = 5;
		//�����������÷Ŵ�������ϵ��
		//�����������÷Ŵ�������ϵ��
				if(zengyi <= DataSave.Gain) {
					Q = DataSave.BelowNF;
				}else {
					Q = DataSave.AboveNF;
				}
		
		double	O = 0;
		//���ݹ������ͣ��Լ�����˥�����õ���ƽ�����˹⹦��
		if(I <= 25) {
			if(this.LinkType.equals("G.652")) {
				O = 1;
			}
			if(this.LinkType.equals("G.655")) {
				O = 0;
			}
		}else if(I <= 32) {
			if(this.LinkType.equals("G.652")) {
				O = 2;
			}
			if(this.LinkType.equals("G.655")) {
				O = 1;
			}
		}else if(I <= 37) {
			if(this.LinkType.equals("G.652")) {
				O = 4;
			}
			if(this.LinkType.equals("G.655")) {
				O = 2;
			}
		}else {
			if(this.LinkType.equals("G.652")) {
				O = 7;
			}
			if(this.LinkType.equals("G.655")) {
				O = 4;
			}
		}		
		double OSNRCount = Math.pow(10, ((I + J + K + Q - O) / 10));
//		System.out.println(this+" ������"+OSNRCount+" Q:"+Q+" K:"+K+" J:"+J+" O:"+O+" I:"+I);
		return OSNRCount;
	}

	public void setOSNRCount(double oSNRCount) {
		OSNRCount = oSNRCount;
	}

	public double getDissipation() {
		return Dissipation;
	}

	public void setDissipation(double oDissipation) {
		Dissipation = oDissipation;
	}

	public FiberLink(int id, String name, CommonNode fromNode, CommonNode toNode, double length, Layer linkLayer,
			boolean isActive, double attenuation, double PMD, String linkType, int jieduan) {
		super();
		this.id = id;
		this.name = name;
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.length = length;
		this.isActive = isActive;
		this.linkLayer = linkLayer;
		this.attenuation = attenuation;
		this.PMD = PMD;
		this.LinkType = linkType;
		this.FiberStage = jieduan;
		// SRLG = sRLG;
		// this.size=size;
		// this.attenuation = attenuation;
		// this.linkLayer=linkLayer;
		// setPMD(pMD);
		// FiberStage = fiberStage;

		fiberLinkList.add(this);

	}

	// public FiberLink(int id, String name,CommonNode fromNode, CommonNode toNode,
	// double length, int waveNum,
	// boolean isActive,String SRLG,Layer linkLayer,double inPower,double
	// spanLoss,double NF) {
	// //super(id, name,fromNode, toNode, length, waveNum,size,
	// isActive,SRLG,linkLayer);
	// // TODO Auto-generated constructor stub
	// this.id=id;
	// this.name=name;
	// this.fromNode=fromNode;
	// this.toNode=toNode;
	// this.length=length;
	// this.waveNum=waveNum;
	// this.isActive=isActive;
	// this.SRLG=SRLG;
	// this.linkLayer=linkLayer;
	// this.inPower=inPower;
	// this.spanLoss=spanLoss;
	// this.NF=NF;
	//
	// fiberLinkList.add(this);
	// int i = 1;
	// while (i < waveNum + 1) {
	// WaveLength wl = new WaveLength(i, this);
	// this.getWaveLengthList().add(wl);
	// ++i;
	// }
	// }

	// �ж�link��id�Ƿ��ظ�
	public static boolean isLinkIDRepeat(int id) {
		Iterator<FiberLink> it = FiberLink.fiberLinkList.iterator();
		while (it.hasNext()) {
			FiberLink Link = it.next();
			if (Link.getId() == id)
				return true;
		}
		return false;
	}

	// �ж���·�������Ƿ��ظ�
	public static boolean isLinkNameRepeat(String name) {
		Iterator<FiberLink> it = FiberLink.fiberLinkList.iterator();
		while (it.hasNext()) {
			FiberLink Link = it.next();
			if (Link.getName().equals(name))
				return true;
		}
		return false;
	}

	public static FiberLink getLink(String name) {
		Iterator<FiberLink> it = FiberLink.fiberLinkList.iterator();
		while (it.hasNext()) {
			FiberLink fiberlink = it.next();
			if (fiberlink.getName().equals(name))
				return fiberlink;
		}
		return null;
	}

	public static FiberLink getFiberLink(int id) {
		Iterator<FiberLink> it = fiberLinkList.iterator();
		while (it.hasNext()) {
			FiberLink theLink = it.next();
			if (theLink.getId() == id)
				return theLink;
		}
		return null;
	}

	public static FiberLink getFiberLink(CommonNode fromNode) {
		Iterator<FiberLink> it = fiberLinkList.iterator();
		while (it.hasNext()) {
			FiberLink theLink = it.next();
			if (theLink.getFromNode().getName() == fromNode.getName())
				return theLink;
		}

		return null;
	}

	public LinkedList<WaveLength> getWaveLengthList() {
		return waveLengthList;
	}

	public void setWaveLengthList(LinkedList<WaveLength> waveLengthList) {
		this.waveLengthList = waveLengthList;
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

	public double getInputPower() {
		return inputPower;
	}

	public void setInputPower(double inputPower) {
		this.inputPower = inputPower;
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

	public double getLength() {
		return length;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public int getRemainResource() {
		return remainResource;
	}

	public void setRemainResource(int remainResource) {
		this.remainResource = remainResource;
	}

	public String getSRLG() {
		return SRLG;
	}

	public void setSRLG(String sRLG) {
		SRLG = sRLG;
	}

	public int getWaveNum() {
		return waveNum;
	}

	public void setWaveNum(int waveNum) {
		this.waveNum = waveNum;
	}

	public double getInPower() {
		return inPower;
	}

	public void setInPower(double inPower) {
		this.inPower = inPower;
	}

	public double getSpanLoss() {
		return spanLoss;
	}

	public void setSpanLoss(double spanLoss) {
		this.spanLoss = spanLoss;
	}

	public double getNF() {
		return NF;
	}

	public void setNF(double nF) {
		NF = nF;
	}

	public static LinkedList<FiberLink> getFiberLinkList() {
		return fiberLinkList;
	}

	public static void setFiberLinkList(LinkedList<FiberLink> fiberLinkList) {
		FiberLink.fiberLinkList = fiberLinkList;
	}

	public double getAttenuation() {
		return attenuation;
	}

	public void setAttenuation(double attenuation) {
		this.attenuation = attenuation;
	}

	public String getLinkType() {
		return LinkType;
	}

	public void setLinkType(String linkType) {
		LinkType = linkType;
	}

	public int getFiberStage() {
		return FiberStage;
	}

	public void setFiberStage(int fiberStage) {
		FiberStage = fiberStage;
	}

	public double getPMD() {
		return PMD;
	}

	public void setPMD(double pMD) {
		PMD = pMD;
	}

	public List<LinkRGroup> getFiberRelatedList() {
		return fiberRelatedList;
	}

	public void setFiberRelatedList(List<LinkRGroup> fiberRelatedList) {
		this.fiberRelatedList = fiberRelatedList;
	}

	public List<WDMLink> getCarriedWDMLinkList() {
		return carriedWDMLinkList;
	}

	public void setCarriedWDMLinkList(List<WDMLink> carriedWDMLinkList) {
		this.carriedWDMLinkList = carriedWDMLinkList;
	}

	public List<WDMLink> getCarriedWDMLink() {
		List<WDMLink> f = new LinkedList<>();
		Iterator<WDMLink> it = WDMLink.WDMLinkList.iterator();
		while (it.hasNext()) {
			WDMLink slink = it.next();
			Iterator<FiberLink> a = slink.getFiberLinkList().iterator();
			while (a.hasNext()) {
				FiberLink fiberlink = a.next();
				if (fiberlink.getName().equals(this.getName())) {
					f.add(slink);
				}
			}
		}
		return f;
	}

	public WDMLink getBelongWDMLink() {
		Iterator<WDMLink> it = WDMLink.WDMLinkList.iterator();
		while (it.hasNext()) {
			WDMLink slink = it.next();
			Iterator<FiberLink> a = slink.getFiberLinkList().iterator();
			while (a.hasNext()) {
				FiberLink fiberlink = a.next();
				if (fiberlink.getName().equals(this.getName())) {
					return slink;
				}
			}
		}
		return null;
	}

	public void OSNRcountOld() {
		// OSNR����
		double xishu = 5; // ϵ��
		double zengyi = Math.max(20, attenuation);// ����
		if (zengyi <= DataSave.Gain) {
			xishu = DataSave.BelowNF;
		} else {
			xishu = DataSave.AboveNF;
		}
		this.OSNRCount = Math.pow(10, ((zengyi + xishu - this.inPower) / 10));

		double dissipation;
		if (attenuation <= 22) {
			dissipation = 1;
		} else {
			dissipation = 1 + (attenuation - 22) * 0.2;
		}
		this.Dissipation = dissipation;
	}
	public void OSNRcountHuaWei() {
		double Q = data.DataSave.AF;
		double K = data.DataSave.OSC;
		double J = data.DataSave.road;
		double O = this.inputPower;
		double I = this.attenuation;
		
		double zengyi = I + J + K;
		//�����������÷Ŵ�������ϵ��
		if(zengyi <= DataSave.Gain) {
			Q = DataSave.BelowNF;
		}else {
			Q = DataSave.AboveNF;
		}
		
		
		//���ݹ������ͣ��Լ�����˥�����õ���ƽ�����˹⹦��
		if(I <= 25) {
			if(this.LinkType.equals("G.652")) {
				this.inputPower = 1;
			}
			if(this.LinkType.equals("G.655")) {
				this.inputPower = 0;
			}
		}else if(I <= 32) {
			if(this.LinkType.equals("G.652")) {
				this.inputPower = 2;
			}
			if(this.LinkType.equals("G.655")) {
				this.inputPower = 1;
			}
		}else if(I <= 37) {
			if(this.LinkType.equals("G.652")) {
				this.inputPower = 4;
			}
			if(this.LinkType.equals("G.655")) {
				this.inputPower = 2;
			}
		}else {
			if(this.LinkType.equals("G.652")) {
				this.inputPower = 7;
			}
			if(this.LinkType.equals("G.655")) {
				this.inputPower = 4;
			}
		}		
		
		this.OSNRCount = Math.pow(10, ((I + J + K + Q - O) / 10));
		
		double dissipation;
		if (this.attenuation <= 22) {
			dissipation = 1;
		} else {
			dissipation = 1 + (this.attenuation - 22) * 0.2;
		}
		this.Dissipation = dissipation;
	}
	

}
