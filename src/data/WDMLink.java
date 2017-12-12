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
 * WDM层链路
 * 
 * @author 豹读诗书
 *
 */
public class WDMLink extends BasicLink implements Serializable {

	private int id;
	private String name;
	private String othername;
	private CommonNode fromNode;// 链路源节点
	private CommonNode toNode;// 链路宿节点
	private int zhongjiNum;// 该wdm链路所含OLA节点数
	private double length;// 该WDM链路的长度
	private double weight;// 权重
	private boolean isActive;// 是否激活
	private int waveNum;// 波道数
	private int remainResource;// 剩余资源，即剩余波道数
	private double inPower = 1;// 输入光功率（dBm）
	private int SumOfOSNR = 0;// 该WDM链路对应的Fiber层链路的OSNR值总和
	private double nrate; // 存储double类型的速率，单位是G；
	private PortRate rate;
	private Layer linkLayer;// 所属层
	private Port fromPort;// 起始端口
	private Port toPort;// 结束端口
	private boolean flexibleRaster;// 灵活栅格 //@TY宇 2017/9/23
	private int systemRate;// 系统速率 //2017/9/23
	private double attenuation;// 光纤衰减
	private double PMD;
	private int FiberStage = 1;// 光纤阶段
	private String LinkType;// 光纤类型
	private int workUsedWaveNum = 0;// 用于工作的波长总数
	private int otherUsedWaveNum = 0;// 用于其他的波长总数

	// private List<FiberLink> fiberLinkList = new
	// LinkedList<FiberLink>();//该WDMlink所含的fiberLink
	private List<WaveLength> waveLengthList = new LinkedList<>();// 用以存放每条WDMlink的波道信息
	private List<FiberLink> fiberLinkList = new LinkedList<FiberLink>();
	private List<Traffic> carriedTrafficList = new LinkedList<>();// 承载的业务
	public static LinkedList<WDMLink> WDMLinkList = new LinkedList<WDMLink>();// 存放所有WDMLink
	private List<LinkRGroup> wdmRelatedList = new LinkedList<LinkRGroup>(); // 链路所在的共享风险链路组
	public  LinkedList<WDMLink> parallelLinkList = new LinkedList<>(); //存放平行边

	public WDMLink() {
	}

	// 构造方法
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
		// wdmlink命名，如果两节点只有1条link则名字为（首-末）别名（末-首）
		if (this.commonFTWDMLinkNum() == 0) {
			this.name = fromNode.getName() + "-" + toNode.getName();
			this.othername = toNode.getName() + "-" + fromNode.getName();
		}
		// 如果WDMLinklist里已有1条该首末节点的链路，则该条命名为（首-末-2）并将原有那条修改为（首-末-1）
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
		// 如果WDMLinklist里已有2条及以上该首末节点的链路，则该条命名为（首-末-x）
		if (this.commonFTWDMLinkNum() != 0 && this.commonFTWDMLinkNum() != 1) {
			this.name = fromNode.getName() + "-" + toNode.getName() + "-" + (this.commonFTWDMLinkNum() + 1);
			this.othername = toNode.getName() + "-" + fromNode.getName() + "-" + (this.commonFTWDMLinkNum() + 1);
		}
		fiberLinkList = new LinkedList<FiberLink>();
		WDMLinkList.add(this);
		// 初始化波长资源
		int i = 1;
		while (i < waveNum + 1) { // 系统设置完波道数后（如80），就初始化80个波长加入到WaveLengthList里
			WaveLength wl = new WaveLength(i, this.fromNode, this.toNode, this, Status.空闲);
			WaveLength.addWaveLengthPort(wl);
			this.getWaveLengthList().add(wl);
			++i;
		}
		// 设置链路对应节点的度，来初始化节点的updown
		fromNode.setUpDown();
		toNode.setUpDown();

	}

	// 添加链路构造方法
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

	// 返回工作使用的波长号
	public String workedUseWave() {
		List<WaveLength> wavelist = this.getWaveLengthList();
		String a = "";
		for (int i = 0; i < wavelist.size(); i++) {
			if (wavelist.get(i).getStatus() == Status.工作)
				a = a + (i + 1) + ",";
		}
		return a;
	}

	// 返回保护和恢复使用的波长号
	public String otherUseWave() {
		List<WaveLength> wavelist = this.getWaveLengthList();
		String a = "";
		for (int i = 0; i < wavelist.size(); i++) {
			if (wavelist.get(i).getStatus() == Status.保护 || wavelist.get(i).getStatus() == Status.恢复)
				a = a + (i + 1) + ",";
		}
		return a;
	}

	// 由链路名称得到链路
	public static WDMLink getWDMLink(String name) {
		Iterator<WDMLink> it = WDMLink.WDMLinkList.iterator();
		while (it.hasNext()) {
			WDMLink link = it.next();
			if (link.getName().equals(name))
				return link;
		}
		return null;
	}

	// 由链路id得到链路
	public static WDMLink getWDMLink(int id) {
		Iterator<WDMLink> it = WDMLink.WDMLinkList.iterator();
		while (it.hasNext()) {
			WDMLink theLink = it.next();
			if (theLink.getId() == id)
				return theLink;
		}
		return null;
	}

	// 根据首末节点的名字找链路
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

	// 由链路id得到链路
	public static WDMLink getLinkByID(int id) {
		Iterator<WDMLink> it = WDMLink.WDMLinkList.iterator();
		while (it.hasNext()) {
			WDMLink theLink = it.next();
			if (theLink.getId() == id)
				return theLink;
		}
		return null;
	}
	// //重写toString方法
	// public String toString() {
	// return (this.fromNode.getName() + "——" + this.toNode.getName());
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

	public LinkedList<WDMLink> getParallelLinkList() {
		return parallelLinkList;
	}

	public void setParallelLinkList(LinkedList<WDMLink> parallelLinkList) {
		this.parallelLinkList = parallelLinkList;
	}

	// 打印该链路已被使用于工作的波长
	public static void printWorkUsedWave(WDMLink link) {
		List<WaveLength> wavelist = link.getWaveLengthList();
		// System.out.print("工作波长:");
		for (int i = 0; i < wavelist.size(); i++) {
			if (wavelist.get(i).getStatus() == Status.工作)
				System.out.print((i + 1) + ",");
		}
		// System.out.println();
	}

	// 打印该链路已被使用于工作的波长
	public static void printOtherUsedWave(WDMLink link) {
		List<WaveLength> wavelist = link.getWaveLengthList();
		System.out.print("保护及恢复波长:");
		for (int i = 0; i < wavelist.size(); i++) {
			if (wavelist.get(i).getStatus() == Status.保护)
				System.out.print((i + 1) + ",");
			if (wavelist.get(i).getStatus() == Status.恢复)
				System.out.print((i + 1) + ",");
		}
		System.out.println();
	}

	public int getWorkUsedWaveNum() {

		List<WaveLength> wavelist = this.getWaveLengthList();
		for (int i = 0; i < wavelist.size(); i++) {
			if (wavelist.get(i).getStatus() == Status.工作)
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
			if (wavelist.get(i).getStatus() == Status.保护 || wavelist.get(i).getStatus() == Status.恢复)
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

	// 找出同源宿节点的WDMlink的数量
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
	
    //通过波道id获取波道
	public WaveLength getWavelengthById(int id) {
		List<WaveLength> waveLengthList=this.getWaveLengthList();
		for(int i=0;i<waveLengthList.size();i++) {
			if(waveLengthList.get(i).getID()==id)
				return waveLengthList.get(i);
		}
		return null;
	}
	//设置parallelLinkList
	public void createParallelLinkList() {
		for(int i=0;i<WDMLink.WDMLinkList.size();i++) {
			WDMLink link=WDMLink.WDMLinkList.get(i);
			if((this.getFromNode().getName().equals(link.getFromNode().getName())&&this.getToNode().getName().equals(link.getToNode().getName()))
					||(this.getFromNode().getName().equals(link.getToNode().getName())&&this.getToNode().getName().equals(link.getFromNode().getName()))) {
				this.parallelLinkList.add(link);
			}
		}
	}
	

}
