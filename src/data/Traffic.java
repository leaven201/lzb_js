package data;

import enums.*;

import java.io.Serializable;
/**
 * ҵ��
 * @author ����ʫ��
 *
 */
import java.util.*;

import algorithm.RouteAlloc;

public class Traffic implements Serializable{

    private int id;
    private int rankId;   //ҵ�����
    private String name;
    private CommonNode fromNode;
    private CommonNode toNode;
    private TrafficRate rate; // ö����ҵ������
    private double nrate; // �洢double���͵����ʣ���λ��G��
    private TrafficStatus status; // ҵ��״̬����ʼδ����
    private int trafficNum;//ҵ������
    private TrafficLevel protectLevel; // ҵ�񱣻��ȼ�
    private boolean isElectricalCrossConnection;//�Ƿ�罻��
    private boolean isShare;//Ԥ�ù���
    private CommonNode MustPassNode;//�ؾ�վ��
    private CommonNode MustAvoidNode;//�ر�վ��
    private FiberLink MustPassLink;//�ؾ�����
    private FiberLink MustAvoidLink;//�رܹ���
    private boolean isHaveTraffic=false;//�Ƿ�����ҵ��
    private Route existRoute=null;//����ҵ��·��
    private int existWaveLength=0;//����ҵ�񲨳�
//    private int relatedID=Integer.MAX_VALUE;//����ҵ������
    private TrafficGroupNew trafficgroup=null;//���չ���ҵ����
    private int numRank; //ͬԴͬ��ҵ���еĵڼ���
    private String trafficId;//ҵ����    ������-   ����ҵ����-��
    private int faultType; // ҵ��������ͣ�0������1����·�ɹ��ϣ�2����·�ɹ��ϣ�3������
//    private boolean isFromOTN; // ҵ���Ƿ�ͨ��OTN�ɶ��ҵ��ۺ϶���
    private int effectNum; // ��Ӱ�������������ʹ��
    private int failNum; // �ָ�ʧ�ܴ�����������ʹ��
    private Route workRoute=null; // ����·��
    private Route protectRoute=null; // ����·��
    private Route preRoute=null;//ר��Ԥ��·��
    private Route dynamicRoute=null;
    private Route resumeRoute=null; // ���ٷ���ʹ�ã��ָ�·��
    private Route resumeRoutePro=null; // ���ٷ���ʹ�ã��ָ�·�ɵı���·��
    private Layer layer;
    
    public static List<Traffic> trafficList=new LinkedList<>();//�洢����ҵ��
    public static List<Traffic> failtrafficList=new LinkedList<>();//�������ʧ�ܵ�ҵ��
//    private TrafficGroup m_cGroup=null; // ����ҵ����
//    private int idOfTrafficGroup;//����ҵ��������
//    private TrafficGroupType TraGroType;//�������ҵ��������
    //private TrafficType type;//ҵ������
    
    //lzb10.10++ ����ҵ���췽��
    public Traffic(int id,int rankId, CommonNode fromNode, CommonNode toNode, TrafficRate rate, int trafficNum,
    		TrafficLevel protectLevel, boolean isElectricalCrossConnection, boolean isShare,
    		TrafficGroupNew trafficgroup, CommonNode MustPassNode, CommonNode MustAvoidNode,
    		FiberLink MustPassLink, FiberLink MustAvoidLink, boolean isHaveTraffic, Route existRoute, int existWaveLength) {
		this.id = id;
		this.rankId=rankId;
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.rate = rate;
		this.trafficNum = trafficNum;
		this.protectLevel = protectLevel;
		this.isElectricalCrossConnection = isElectricalCrossConnection;
		this.isShare = isShare;
		this.trafficgroup=trafficgroup;
		this.MustPassNode = MustPassNode;
		this.MustAvoidNode = MustAvoidNode;
		this.MustPassLink = MustPassLink;
		this.MustAvoidLink = MustAvoidLink;
		this.isHaveTraffic = isHaveTraffic;
		this.existRoute = existRoute;
		this.existWaveLength = existWaveLength;
		this.name=fromNode.getName()+"-"+toNode.getName();
    }//-- 

  public Traffic(int id,String name, CommonNode fromNode, CommonNode toNode, TrafficRate rate, int trafficNum, String rank,
			boolean isElectricalCrossConnection, String relatedTraffic, CommonNode mustPassNode,
			CommonNode mustAvoidNode, FiberLink mustPassLink, FiberLink mustAvoidLink) {
		super();
		this.id = id;
		this.name=name;
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.rate = rate;
//		TrafficNum = trafficNum;
//		this.rank = rank;
		this.isElectricalCrossConnection = isElectricalCrossConnection;
//		RelatedTraffic = relatedTraffic;
		MustPassNode = mustPassNode;
		MustAvoidNode = mustAvoidNode;
		MustPassLink = mustPassLink;
		MustAvoidLink = mustAvoidLink;
	}
    public Traffic(int id, String name, CommonNode fromNode, CommonNode toNode, TrafficRate rate,
		    TrafficLevel protectLevel) {
		this.id = id;
		this.name = name;
		this.fromNode = fromNode;
		this.toNode = toNode;
		this.protectLevel = protectLevel;
		this.nrate = nrate;
	    }
	    

	//���췽��
    public Traffic(int id, String name, CommonNode fromNode, CommonNode toNode, TrafficRate rate,
	    TrafficLevel protectLevel,TrafficStatus status) {
	this.id = id;
	this.name = name;
	this.fromNode = fromNode;
	this.toNode = toNode;
	this.status = status;
	this.protectLevel = protectLevel;
	this.nrate = nrate;
    }
    
    public Traffic(int ID,String name,String from,String to,TrafficRate rate,TrafficLevel level,Layer layer,TrafficStatus status){
    	this.id = ID;
    	this.name = name;
		if(CommonNode.getNode(from)!= null){
			this.fromNode= CommonNode.getNode(from);
		}
		if(CommonNode.getNode(to)!= null){
			this.toNode= CommonNode.getNode(to);
		}		
		this.rate = rate;
		this.protectLevel = level;
		this.layer = layer;
		this.status = status;
		faultType=0;
//		m_ncutoff=0;
	}
    public void releaseTraffic(Traffic tr, int i) {// �ͷ�ҵ��ռ�õ�·����Դ
	// TODO �Զ����ɵķ������
	if (i == 0) {
	    RouteAlloc.releaseRoute(tr.getWorkRoute());
	    RouteAlloc.releaseRoute(tr.getProtectRoute());
	    RouteAlloc.releaseRoute(tr.getPreRoute());
	    RouteAlloc.releaseRoute(tr.getDynamicRoute());
	    tr.setWorkRoute(null);
	    tr.setProtectRoute(null);
	    tr.setPreRoute(null);
	    tr.setDynamicRoute(null);
	}
	if (i == 1) {
	    RouteAlloc.releaseRoute(tr.getWorkRoute());
	    tr.setWorkRoute(null);
	}
	if (i == 2) {
	    RouteAlloc.releaseRoute(tr.getProtectRoute());
	    tr.setProtectRoute(null);
	}
	if(i == 3) {
		RouteAlloc.releaseRoute(tr.getPreRoute());
		tr.setPreRoute(null);
	}
	if(i == 4) {
		RouteAlloc.releaseRoute(tr.getDynamicRoute());
		tr.setDynamicRoute(null);
	}
	
    }
    
    //�����·�˿�
    public void releasePortSource(Traffic tr, int i) {// 0=���У�1=����·�ɣ�2=����·�ɣ����һ��ҵ��Ķ˿���Դ;3=Ԥ��·��
	// TODO �Զ����ɵķ������
	if (tr.getWorkRoute() == null)
	    return;// 11.24
	List<Port> portList = null;
	if (i == 0) {
	    portList = tr.getWorkRoute().getPortList();
	    if (tr.getProtectRoute() != null)
		portList.addAll(tr.getProtectRoute().getPortList());// һ���������·�ɺͱ���·�ɵĶ˿�
	    if(tr.getPreRoute()!=null)
	    	portList.addAll(tr.getPreRoute().getPortList());//һ���������·�ɺͱ���·�ɺ�Ԥ��·�ɵĶ˿�
	} else if (i == 1) {
	    portList = tr.getWorkRoute().getPortList();
	} else if (i == 2) {
	    portList = tr.getProtectRoute().getPortList();
	} else if (i == 3) {
		portList = tr.getPreRoute().getPortList();
	}

	for (Port port : portList) {
	    if (port.getCarriedTraffic().remove(tr)) {// cc 12.2
		if (PortKind.��·�˿� == port.getKind()) {// �������·�˿�
//		    port.setRemainResource(port.getRemainResource() + tr.getNrate());
			 port.setRemainResource(100);
		    // port.getCarriedLink().remove(tr); //cc 11.24
		    if (port.getCarriedTraffic().size() == 0)
			port.setStatus(PortStatus.����);
		} else {// ��֧·�˿�
//		    port.setRemainResource(port.getNrate());
			port.setRemainResource(100);
		    port.getCarriedTraffic().clear();
		    port.setStatus(PortStatus.����);
		}
	    } // �˿����
	}
    }
    
    //���֧·�˿�
    public void delTrafficPort(Traffic tr) {
	// TODO Auto-generated method stub

	PortRate erate = TrafficRate.T2PRate(tr.getRate());
	CommonNode from = tr.getFromNode();
	CommonNode to = tr.getToNode();
	int portnum = 2;
	for (int a = 0; a < portnum; a++) {// ɾ���׽ڵ�˿�
	    List<Port> portlist = from.getPortList();
	    for (int i = 0; i < portlist.size(); ++i) {
		Port p = portlist.get(i);
		if (p.getRate().equals(erate) && p.getStatus().equals(PortStatus.����)
			&& p.getKind().equals(PortKind.֧·�˿�)) {
		    portlist.remove(p);
		    break;
		}
	    }
	}

	for (int a = 0; a < portnum; a++) {// ɾ��ĩ�ڵ�˿�
	    List<Port> portlist = to.getPortList();
	    for (int i = 0; i < portlist.size(); ++i) {
		Port p = portlist.get(i);
		if (p.getRate().equals(erate) && p.getStatus().equals(PortStatus.����)
			&& p.getKind().equals(PortKind.֧·�˿�)) {
		    portlist.remove(p);
		    break;
		}
	    }
	}
    }
    
    public static void delTraffic(Traffic t) {
	// ɾ��ǰ���ͷ���Դ
	t.releaseTraffic(t, 0);
	t.releaseTraffic(t, 1);
	t.releaseTraffic(t, 2);
	t.releaseTraffic(t, 3);

	t.releasePortSource(t, 0);
	t.releasePortSource(t, 1);
	t.releasePortSource(t, 2);
	t.releasePortSource(t, 3);

	t.delTrafficPort(t);
    }
    
    
    @Override
    public String toString() {
	return (this.fromNode.getName() + "����" + this.toNode.getName()+"-"+this.getId());
    }
    public int getRankId() {
		return rankId;
	}


	public void setRankId(int rankId) {
		this.rankId = rankId;
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
    

    public int getNumRank() {
		return numRank;
	}

	public void setNumRank(int numRank) {
		this.numRank = numRank;
	}

	public TrafficStatus getStatus() {
        return status;
    }

    public void setStatus(TrafficStatus status) {
        this.status = status;
    }

    public TrafficLevel getProtectLevel() {
        return protectLevel;
    }

    public void setProtectLevel(TrafficLevel protectLevel) {
        this.protectLevel = protectLevel;
    }

    public int getFaultType() {
        return faultType;
    }

    public void setFaultType(int faultType) {
        this.faultType = faultType;
    }


    public int getEffectNum() {
        return effectNum;
    }

    public void setEffectNum(int effectNum) {
        this.effectNum = effectNum;
    }

    public int getFailNum() {
        return failNum;
    }

    public void setFailNum(int failNum) {
        this.failNum = failNum;
    }
    

    public int getTrafficNum() {
		return trafficNum;
	}


	public void setTrafficNum(int trafficNum) {
		this.trafficNum = trafficNum;
	}


	public double getNrate() {
        return nrate;
    }

    public void setNrate(double nrate) {
        this.nrate = nrate;
    }

    public Route getWorkRoute() {
        return workRoute;
    }

    public void setWorkRoute(Route workRoute) {
        this.workRoute = workRoute;
    }

    public Route getProtectRoute() {
        return protectRoute;
    }

    public void setProtectRoute(Route protectRoute) {
        this.protectRoute = protectRoute;
    }
    

    public Route getPreRoute() {
		return preRoute;
	}


	public void setPreRoute(Route preRoute) {
		this.preRoute = preRoute;
	}


	public Route getResumeRoute() {
        return resumeRoute;
    }

    public void setResumeRoute(Route restoreRoute) {
        this.resumeRoute = restoreRoute;
    }

    public Route getResumeRoutePro() {
        return resumeRoutePro;
    }

    public void setResumeRoutePro(Route restoreRoutePro) {
        this.resumeRoutePro = restoreRoutePro;
    }

    public static List<Traffic> getTrafficList() {
        return trafficList;
    }

    public static void setTrafficList(List<Traffic> trafficList) {
        Traffic.trafficList = trafficList;
    }

    public boolean isHaveTraffic() {
		return isHaveTraffic;
	}

	public void setHaveTraffic(boolean isHaveTraffic) {
		this.isHaveTraffic = isHaveTraffic;
	}

	public TrafficRate getRate() {
        return rate;
    }

    public void setRate(TrafficRate rate) {
        this.rate = rate;
    }
    

//	public String getRank() {
//		return rank;
//	}
//
//	public void setRank(String rank) {
//		this.rank = rank;
//	}




	public boolean isElectricalCrossConnection() {
		return isElectricalCrossConnection;
	}

	public void setElectricalCrossConnection(boolean isElectricalCrossConnection) {
		this.isElectricalCrossConnection = isElectricalCrossConnection;
	}

//	public String getRelatedTraffic() {
//		return RelatedTraffic;
//	}
//
//	public void setRelatedTraffic(String relatedTraffic) {
//		RelatedTraffic = relatedTraffic;
//	}

	public CommonNode getMustPassNode() {
		return MustPassNode;
	}

	public void setMustPassNode(CommonNode mustPassNode) {
		MustPassNode = mustPassNode;
	}

	public CommonNode getMustAvoidNode() {
		return MustAvoidNode;
	}

	public void setMustAvoidNode(CommonNode mustAvoidNode) {
		MustAvoidNode = mustAvoidNode;
	}

	public FiberLink getMustPassLink() {
		return MustPassLink;
	}

	public void setMustPassLink(FiberLink mustPassLink) {
		MustPassLink = mustPassLink;
	}

	public FiberLink getMustAvoidLink() {
		return MustAvoidLink;
	}

	public void setMustAvoidLink(FiberLink mustAvoidLink) {
		MustAvoidLink = mustAvoidLink;
	}
	

public Route getExistRoute() {
		return existRoute;
	}


	public void setExistRoute(Route existRoute) {
		this.existRoute = existRoute;
	}


//	public WaveLength getExistWaveLength() {
//		return existWaveLength;
//	}
//
//
//	public void setExistWaveLength(WaveLength existWaveLength) {
//		this.existWaveLength = existWaveLength;
//	}


	//	public int getTrafficNum() {
//		return TrafficNum;
//	}
//
//	public void setTrafficNum(int trafficNum) {
//		TrafficNum = trafficNum;
//	}
	public static void setS_lWDMTrafficList(List<Traffic> sLWDMTrafficList) {
		trafficList = sLWDMTrafficList;
	}
	public static void	addTraffic(Traffic e){//��̬��������ά������ע����ʵ�ʶ����޹�
		trafficList.add(e);
	}
	public Layer getM_eLayer() {
		return layer;
	}
	public TrafficLevel getM_eLevel() {
		return protectLevel;
	}
	public void setM_eLevel(TrafficLevel mELevel) {
		protectLevel = mELevel;
	}
	public Route getM_cResumeRoute() {
		return resumeRoute;
	}
	public void setM_cResumeRoute(Route mCResumeRoute) {
		resumeRoute = mCResumeRoute;
	}
	public Route getM_cResumeRouteP() {
		return resumeRoutePro;
	}
	public void setM_cResumeRouteP(Route mCResumeRouteP) {
		resumeRoutePro = mCResumeRouteP;
	}
	public StringBuffer workedRoute() {
		StringBuffer bufferw = new StringBuffer();
		if (this.getWorkRoute() != null && null != this.getWorkRoute().getNodeList()) {
			for (int n = 0; n < this.getWorkRoute().getNodeList().size(); n++) {
				bufferw.append(this.getWorkRoute().getNodeList().get(n).getName());
				if (n != this.getWorkRoute().getNodeList().size() - 1) {
					bufferw.append("--<"+this.getWorkRoute().getWDMLinkList().get(n).getName()+">--");
//					bufferw.append("--");
				}
			}
		}return bufferw;
	}
	public String waveChangedNode() {
		StringBuilder b=new StringBuilder();
		if (this.getWorkRoute() != null && null != this.getWorkRoute().getNodeList()) {
		for(int i=0;i<this.getWorkRoute().getWaveChangedNode().size();i++) {
			b.append(this.getWorkRoute().getWaveChangedNode().get(i).getName()+",");
		}}
		return b.toString();
	}
	
	public static Traffic getTraffic(int id){
		Traffic b=null;
		Iterator<Traffic> l = trafficList.iterator();
		while (l.hasNext()) {
			Traffic a = l.next();
			if(a.getId()==id) {
			b=a;	
			}
			}
		return b;
	}
	
	public static List<Traffic> reRankList(List<Traffic> TraList){
		List<Traffic> list = new LinkedList<>();
		for(int i=0; i<TraList.size();i++) {
			Traffic tra = TraList.get(i);
			if(tra.getTrafficgroup()!=null) {
				list.add(tra);
			}
		}
		for(int i=0; i<TraList.size();i++) {
			Traffic tra = TraList.get(i);
			if(tra.getTrafficgroup()==null) {
				list.add(tra);
			}
		}
		return list;
	}
	

//	
//	public void setM_cGroup(String group) {
//		
//		if(m_cGroup==null){
//			if(TrafficGroup.getTrafficGroup(group)!=null)
//		       m_cGroup = TrafficGroup.getTrafficGroup(group);  
//		if(m_cGroup==null)
//			m_cGroup=new TrafficGroup(group);		
//		m_cGroup.addTraffic(this);}
//		
//		else{
//			m_cGroup.delTraffic(this);
//			m_cGroup = TrafficGroup.getTrafficGroup(group);
//			if(m_cGroup!=null)
//			m_cGroup.addTraffic(this);
//			}
//}
	public static void releaseAllPortResource() {// ���ҵ���ռ�õ�������Դ
		for (Port port : Port.usedPortList) {
			port.getCarriedTraffic().clear();
			port.setRemainResource(1);
			port.setStatus(PortStatus.����);
		}
	}


	public TrafficGroupNew getTrafficgroup() {
		return trafficgroup;
	}


	public void setTrafficgroup(TrafficGroupNew trafficgroup) {
		this.trafficgroup = trafficgroup;
	}

	public Route getDynamicRoute() {
		return dynamicRoute;
	}

	public void setDynamicRoute(Route dynamicRoute) {
		this.dynamicRoute = dynamicRoute;
	}

	public String getTrafficId() {
		return trafficId;
	}

	public void setTrafficId(String trafficId) {
		this.trafficId = trafficId;
	}
	public static void setAllTrafficId() {
		for(int i=0;i<Traffic.trafficList.size();i++) {
			Traffic tra = Traffic.trafficList.get(i);
			if(tra.getTrafficgroup()==null) {
				tra.setTrafficId("����-"+(i+1));
			}else {
				tra.setTrafficId(tra.getTrafficgroup().getTheName()+tra.getNumRank());
			}
		}
	}
	


	
}
