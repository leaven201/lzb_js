package data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import enums.*;

/**
 * �˿�
 * 
 * @author ����ʫ��
 *
 */
public class Port implements Serializable{

	private int id;
	private CommonNode belongNode;// �˿������ڵ�
	private WaveLength belongWaveLength;// �˿�������·
	private PortRate rate; // �˿ڵ����ʣ�������ж˿�100G�������·�ڷ�10G��100G
	private double nrate; // �洢float���͵����ʣ���λ��G��
	private PortKind kind; // �˿ڷ�Ϊ֧·�˿ں���·�˿�
	private PortStatus status; // �˿ڵ�״̬
	private int remainResource; // �˿�ʣ����Դ 0������;1������
	private CommonNode direction; // �˿�������·ָ��ķ���
	private boolean used;//��Ƕ˿��Ƿ�ʹ�ù������ڹ滮����
	private boolean ispre;//�Ƿ�Ԥ��·��ռ��
	private boolean yongguo;//��Ƕ˿��Ƿ�ʹ�ù������ڿ��ٷ���
	
	private List<Traffic> carriedTraffic = new LinkedList<>();
	public static LinkedList<Port> usedPortList = new LinkedList<Port>();// �洢ʹ�ù��Ķ˿ڣ��ڶ˿ڷ�������Ժ�������,�������

	// ���췽��
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

	// ��дtoString()����
	@Override
	public String toString() {
		return "Port [id=" + id + ", belongNode=" + belongNode + ", belongWaveLength=" + belongWaveLength + ", kind="
				+ kind + "]";
	}

	// setter��getter����
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
