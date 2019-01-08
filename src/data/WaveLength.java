package data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import enums.*;

/**
 * ��������Դ�ĵ�λ
 * 
 * @author ����ʫ��
 *
 */
public class WaveLength implements Serializable {

	private CommonNode fNode;
	private CommonNode tNode;
	private Port fromPort;// ��ʼ�˿ڣ��˿ںͲ����󶨣�
	private Port toPort;// �����˿�
	private int ID; // �������
	private PortRate rate;// ��������
	private Status status = Status.����; // ����ʹ��״̬ ����,����,����,�ָ�,����
	private boolean simUse = false;
	private WDMLink holder = null; // ��������WDMLink
	private boolean used;//����Ƿ�ʹ�ù������ڹ滮����
	private boolean isPre;//�Ƿ�Ԥ�ûָ�·��ռ��
	private boolean yongguo;//����Ƿ�ʹ�ù������ڿ��ٷ���
	private boolean isSimUse=false;//����Ƿ�Ϊ���ٷ���ʱ�ã����ڿ��ٷ�������Դ
	private double m_nCaption; // ����������ҵ����
	private double m_nFree=100; // ����ʣ��ɳ���ҵ����
	private double m_nUsed=0; // �����ѳ���ҵ����
	
	
	public List<Traffic> carriedTrafficList = new LinkedList<>();// �Ӳ������ص�ҵ��
	private Traffic carriedTraffic; // ����ҵ��
	//��������£�һ������ֻ����һ��ҵ�񣬵�������������Ԥ��(��̬)�õģ��������������ͬʱ���ض��ҵ���Ƕ��ҵ���Ԥ��·��ʹ�ò�������ֻҪ��Щҵ��Ĺ���·���Ƿ����
    public List<Traffic> preTrafficList=new LinkedList<>();//�����Ԥ��ʹ�õĲ�����������ҵ��
    public List<Traffic> dynamicTrafficList=new LinkedList<>();//����Ƕ�̬ʹ�õĲ�����������ҵ��

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
	


	// ΪWDMLINK��·�����˽ڵ������·�˿�
	public static void addWaveLengthPort(WaveLength wave) {
		CommonNode from = wave.getfNode();
		int fromportid = 0;
		CommonNode to = wave.gettNode();
		int toportid = 0;
		fromportid = from.getPortList().size() + 1;
		toportid = to.getPortList().size() + 1;
		// ʼ�˿�
		Port fromPort = new Port(fromportid, from, wave, wave.getRate(), PortKind.��·�˿�, PortStatus.����);
		from.getPortList().add(fromPort);
		wave.setFromPort(fromPort);
		// fromPort.getCarriedLinkList().add(link);
		fromPort.setDirection(to);
		// ĩ�˿�
		Port toPort = new Port(toportid, to, wave, wave.getRate(), PortKind.��·�˿�, PortStatus.����);
		to.getPortList().add(toPort);
		wave.setToPort(toPort);
		// toPort.getCarriedLinkList().add(link);
		toPort.setDirection(from);
	}

	// setter��getter����
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
	

	public List<Traffic> getCarriedTrafficList() {
		return carriedTrafficList;
	}

	public void setCarriedTrafficList(List<Traffic> carriedTrafficList) {
		this.carriedTrafficList = carriedTrafficList;
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

	public boolean isSimUse() {
		return simUse;
	}

	public void setSimUse(boolean simUse) {
		this.simUse = simUse;
	}
	

	public double getM_nFree() {
		return m_nFree;
	}

	public void setM_nFree(double m_nFree) {
		this.m_nFree = m_nFree;
	}

	public double getM_nUsed() {
		return m_nUsed;
	}

	public void setM_nUsed(double m_nUsed) {
		this.m_nUsed = m_nUsed;
	}
	

	public double getM_nCaption() {
		return m_nCaption;
	}

	public void setM_nCaption(double m_nCaption) {
		this.m_nCaption = m_nCaption;
	}

	//���²�����Դ
	public void updateWaveResource() {
		double used=0;
		double max=0;
		for(int i=0;i<this.getCarriedTrafficList().size();i++) {
			used=used+this.getCarriedTrafficList().get(i).getNrate();
		}
	    if(this.getPreTrafficList()!=null&&this.getPreTrafficList().size()!=0) {
	    	for(int j=0;j<this.getPreTrafficList().size();j++) {
	    		if(this.getPreTrafficList().get(j).getNrate()>max) {
	    			max=this.getPreTrafficList().get(j).getNrate();
	    		}
	    	}
	    }
	    used=used+max;
	    this.setM_nFree(100-used);
	    this.setM_nUsed(used);
	}
	//���ز�����Ԥ��·��ռ�õ���Դ
	public double preUsed() {
		double preused=0;
		if(this.getPreTrafficList()!=null&&this.getPreTrafficList().size()!=0) {
	    	for(int j=0;j<this.getPreTrafficList().size();j++) {
	    		if(this.getPreTrafficList().get(j).getNrate()>preused) {
	    			preused=this.getPreTrafficList().get(j).getNrate();
	    		}
	    	}
	    }
		return preused;
	}
	
	
	
	

}
