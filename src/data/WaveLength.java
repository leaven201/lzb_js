package data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import enums.*;

/**
 * 波长，资源的单位
 * 
 * @author 豹读诗书
 *
 */
public class WaveLength implements Serializable {

	private CommonNode fNode;
	private CommonNode tNode;
	private Port fromPort;// 起始端口（端口和波长绑定）
	private Port toPort;// 结束端口
	private int ID; // 波长编号
	private PortRate rate;// 波长带宽
	private Status status = Status.空闲; // 波长使用状态 空闲,工作,保护,恢复,可用
	private WDMLink holder = null; // 波长所属WDMLink
	private Traffic carriedTraffic; // 承载业务
	private boolean used;//标记是否使用过，用于规划建议
	private boolean isPre;//是否被预置恢复路由占用
	private boolean yongguo;//标记是否使用过，用于抗毁仿真
	
	//正常情况下，一个波道只承载一个业务，但如果这个波道是预置(动态)用的，则这个波道可能同时承载多个业务（是多个业务的预置路由使用波长），只要这些业务的工作路由是分离的
    public List<Traffic> preTrafficList=new LinkedList<>();//存放是预置使用的波道所包含的业务
    public List<Traffic> dynamicTrafficList=new LinkedList<>();//存放是动态使用的波道所包含的业务

	public WaveLength(int id, CommonNode fNode, CommonNode tNode, WDMLink holder, Status status) {
		this.ID = id;
		this.fNode = fNode;
		this.tNode = tNode;
		this.holder = holder;
		this.status = status;
		this.rate = holder.getRate();
	}

	@Override
//	public String toString() {
//		return "Timeslot [ID=" + ID + ", status=" + status + ",holder=" + holder + "]";
//	}
	public String toString() {
		return ""+ID;
	}
	public String toString1() {
		return ""+(ID+1);
	}
	


	// 为WDMLINK链路的两端节点添加线路端口
	public static void addWaveLengthPort(WaveLength wave) {
		CommonNode from = wave.getfNode();
		CommonNode to = wave.gettNode();
		int fromportid = 0;
		int toportid = 0;
		fromportid = from.getPortList().size() + 1;
		toportid = to.getPortList().size() + 1;
		// 始端口
		Port fromPort = new Port(fromportid, from, wave, wave.getRate(), PortKind.线路端口, PortStatus.空闲);
		from.getPortList().add(fromPort);
		wave.setFromPort(fromPort);
		// fromPort.getCarriedLinkList().add(link);
		fromPort.setDirection(to);
		// 末端口
		Port toPort = new Port(toportid, to, wave, wave.getRate(), PortKind.线路端口, PortStatus.空闲);
		to.getPortList().add(toPort);
		wave.setToPort(toPort);
		// toPort.getCarriedLinkList().add(link);
		toPort.setDirection(from);
	}

	// setter、getter方法
	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Traffic getCarriedTraffic() {
		return carriedTraffic;
	}

	public void setCarriedTraffic(Traffic carriedTraffic) {
		this.carriedTraffic = carriedTraffic;
	}

	public WDMLink getHolder() {
		return holder;
	}

	public void setHolder(WDMLink holder) {
		this.holder = holder;
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

	public CommonNode getfNode() {
		return fNode;
	}

	public void setfNode(CommonNode fNode) {
		this.fNode = fNode;
	}

	public CommonNode gettNode() {
		return tNode;
	}

	public void settNode(CommonNode tNode) {
		this.tNode = tNode;
	}

	public PortRate getRate() {
		return rate;
	}

	public void setRate(PortRate rate) {
		this.rate = rate;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public boolean isPre() {
		return isPre;
	}

	public void setPre(boolean isPre) {
		this.isPre = isPre;
	}

	public boolean isYongguo() {
		return yongguo;
	}

	public void setYongguo(boolean yongguo) {
		this.yongguo = yongguo;
	}

	public List<Traffic> getPreTrafficList() {
		return preTrafficList;
	}

	public void setPreTrafficList(List<Traffic> preTrafficList) {
		this.preTrafficList = preTrafficList;
	}

	public List<Traffic> getDynamicTrafficList() {
		return dynamicTrafficList;
	}

	public void setDynamicTrafficList(List<Traffic> dynamicTrafficList) {
		this.dynamicTrafficList = dynamicTrafficList;
	}
	
	
	

}
