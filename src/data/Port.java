package data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import enums.*;

/**
 * 端口
 * 
 * @author 豹读诗书
 *
 */
public class Port implements Serializable{

	private int id;
	private CommonNode belongNode;// 端口所属节点
	private WaveLength belongWaveLength;// 端口所属链路
	private PortRate rate; // 端口的速率，光层所有端口100G，电层线路口分10G与100G
	private double nrate; // 存储float类型的速率，单位是G；
	private PortKind kind; // 端口分为支路端口和线路端口
	private PortStatus status; // 端口的状态
	private int remainResource; // 端口剩余资源 0代表无;1代表有
	private CommonNode direction; // 端口连接链路指向的方向
	private boolean used;//标记端口是否被使用过，用于规划建议
	private boolean ispre;//是否被预置路由占用
	private boolean yongguo;//标记端口是否被使用过，用于抗毁仿真
	
	private List<Traffic> carriedTraffic = new LinkedList<>();
	public static LinkedList<Port> usedPortList = new LinkedList<Port>();// 存储使用过的端口，在端口分配完成以后有内容,方便清空

	// 构造方法
	public Port(int id, CommonNode belongNode, WaveLength belongWaveLength, PortRate rate, PortKind kind,
			PortStatus status) {

		this.id = id;
		this.belongNode = belongNode;
		this.rate = rate;
		this.nrate = PortRate.Rate2Num(rate);
		this.belongWaveLength = belongWaveLength;
		this.kind = kind;
		this.status = status;
		this.remainResource = 1;
	}

	public Port(int id, CommonNode belongNode, PortRate rate, PortKind kind, PortStatus status) {
		super();
		this.id = id;
		this.belongNode = belongNode;
		this.rate = rate;
		this.nrate = PortRate.Rate2Num(rate);
		this.kind = kind;
		this.status = status;
	}

	// 重写toString()方法
	@Override
	public String toString() {
		return "Port [id=" + id + ", belongNode=" + belongNode + ", belongWaveLength=" + belongWaveLength + ", kind="
				+ kind + "]";
	}

	// setter、getter方法
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public CommonNode getBelongNode() {
		return belongNode;
	}

	public void setBelongNode(CommonNode belongNode) {
		this.belongNode = belongNode;
	}

	public PortRate getRate() {
		return rate;
	}

	public void setRate(PortRate rate) {
		this.rate = rate;
	}

	public double getNrate() {
		return nrate;
	}

	public void setNrate(double nrate) {
		this.nrate = nrate;
	}

	public PortKind getKind() {
		return kind;
	}

	public void setKind(PortKind kind) {
		this.kind = kind;
	}

	public PortStatus getStatus() {
		return status;
	}

	public void setStatus(PortStatus status) {
		this.status = status;
	}

	public int getRemainResource() {
		return remainResource;
	}

	public void setRemainResource(int remainResource) {
		this.remainResource = remainResource;
	}

	public List<Traffic> getCarriedTraffic() {
		return carriedTraffic;
	}

	public void setCarriedTraffic(List<Traffic> carriedTraffic) {
		this.carriedTraffic = carriedTraffic;
	}

	public static LinkedList<Port> getUsedPortList() {
		return usedPortList;
	}

	public static void setUsedPortList(LinkedList<Port> usedPortList) {
		Port.usedPortList = usedPortList;
	}

	public CommonNode getDirection() {
		return direction;
	}

	public void setDirection(CommonNode direction) {
		this.direction = direction;
	}

	public WaveLength getBelongWaveLength() {
		return belongWaveLength;
	}

	public void setBelongWaveLength(WaveLength belongWaveLength) {
		this.belongWaveLength = belongWaveLength;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public boolean isIspre() {
		return ispre;
	}

	public void setIspre(boolean ispre) {
		this.ispre = ispre;
	}

	public boolean isYongguo() {
		return yongguo;
	}

	public void setYongguo(boolean yongguo) {
		this.yongguo = yongguo;
	}
}
