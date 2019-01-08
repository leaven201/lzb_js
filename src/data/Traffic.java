package data;

import enums.*;

import java.io.Serializable;
/**
 * 业务
 * @author 豹读诗书
 *
 */
import java.util.*;

import algorithm.RouteAlloc;

public class Traffic implements Serializable{

    private int id;
    private int rankId;   //业务序号
    private String name;
    private CommonNode fromNode;
    private CommonNode toNode;
    private TrafficRate rate; // 枚举型业务速率
    private double nrate; // 存储double类型的速率，单位是G；
    private TrafficStatus status; // 业务状态，初始未分配
    private int trafficNum;//业务数量
    private TrafficLevel protectLevel; // 业务保护等级
    private boolean isElectricalCrossConnection;//是否电交叉
    private boolean isShare;//预置共享
    private CommonNode MustPassNode;//必经站点
    private CommonNode MustAvoidNode;//必避站点
    private FiberLink MustPassLink;//必经光纤
    private FiberLink MustAvoidLink;//必避光纤
    private boolean isHaveTraffic=false;//是否已有业务
    private Route existRoute=null;//已有业务路由
    private int existWaveLength=0;//已有业务波长
//    private int relatedID=Integer.MAX_VALUE;//关联业务组编号
    private TrafficGroupNew trafficgroup=null;//风险共享业务组
    private int numRank; //同源同宿业务中的第几个
    private String trafficId;//业务编号    （常规-   关联业务组-）
    private int faultType; // 业务故障类型，0正常，1工作路由故障，2保护路由故障，3都故障
//    private boolean isFromOTN; // 业务是否通过OTN由多个业务聚合而成
    private int effectNum; // 受影响次数，生存性使用
    private int failNum; // 恢复失败次数，生存性使用
    private Route workRoute=null; // 工作路由
    private Route protectRoute=null; // 保护路由
    private Route preRoute=null;//专享预制路由
    private Route dynamicRoute=null;
    private Route resumeRoute=null; // 抗毁仿真使用：恢复路由
    private Route resumeRoutePro=null; // 抗毁仿真使用：恢复路由的保护路由
    private Layer layer;
    
    public static List<Traffic> trafficList=new LinkedList<>();//存储所有业务
    public static List<Traffic> failtrafficList=new LinkedList<>();//储存分配失败的业务
//    private TrafficGroup m_cGroup=null; // 所属业务组
//    private int idOfTrafficGroup;//关联业务组索引
//    private TrafficGroupType TraGroType;//共享风险业务组类型
    //private TrafficType type;//业务类型
    
    //lzb10.10++ 江苏业务构造方法
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
	    

	//构造方法
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
    public void releaseTraffic(Traffic tr, int i) {// 释放业务占用的路由资源
	// TODO 自动生成的方法存根
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
    
    //清空线路端口
    public void releasePortSource(Traffic tr, int i) {// 0=所有，1=工作路由，2=保护路由，清空一个业务的端口资源;3=预置路由
	// TODO 自动生成的方法存根
	if (tr.getWorkRoute() == null)
	    return;// 11.24
	List<Port> portList = null;
	if (i == 0) {
	    portList = tr.getWorkRoute().getPortList();
	    if (tr.getProtectRoute() != null)
		portList.addAll(tr.getProtectRoute().getPortList());// 一起清除工作路由和保护路由的端口
	    if(tr.getPreRoute()!=null)
	    	portList.addAll(tr.getPreRoute().getPortList());//一起清除工作路由和保护路由和预置路由的端口
	} else if (i == 1) {
	    portList = tr.getWorkRoute().getPortList();
	} else if (i == 2) {
	    portList = tr.getProtectRoute().getPortList();
	} else if (i == 3) {
		portList = tr.getPreRoute().getPortList();
	}

	for (Port port : portList) {
	    if (port.getCarriedTraffic().remove(tr)) {// cc 12.2
		if (PortKind.线路端口 == port.getKind()) {// 如果是线路端口
//		    port.setRemainResource(port.getRemainResource() + tr.getNrate());
			 port.setRemainResource(100);
		    // port.getCarriedLink().remove(tr); //cc 11.24
		    if (port.getCarriedTraffic().size() == 0)
			port.setStatus(PortStatus.空闲);
		} else {// 是支路端口
//		    port.setRemainResource(port.getNrate());
			port.setRemainResource(100);
		    port.getCarriedTraffic().clear();
		    port.setStatus(PortStatus.空闲);
		}
	    } // 端口完毕
	}
    }
    
    //清空支路端口
    public void delTrafficPort(Traffic tr) {
	// TODO Auto-generated method stub

	PortRate erate = TrafficRate.T2PRate(tr.getRate());
	CommonNode from = tr.getFromNode();
	CommonNode to = tr.getToNode();
	int portnum = 2;
	for (int a = 0; a < portnum; a++) {// 删除首节点端口
	    List<Port> portlist = from.getPortList();
	    for (int i = 0; i < portlist.size(); ++i) {
		Port p = portlist.get(i);
		if (p.getRate().equals(erate) && p.getStatus().equals(PortStatus.空闲)
			&& p.getKind().equals(PortKind.支路端口)) {
		    portlist.remove(p);
		    break;
		}
	    }
	}

	for (int a = 0; a < portnum; a++) {// 删除末节点端口
	    List<Port> portlist = to.getPortList();
	    for (int i = 0; i < portlist.size(); ++i) {
		Port p = portlist.get(i);
		if (p.getRate().equals(erate) && p.getStatus().equals(PortStatus.空闲)
			&& p.getKind().equals(PortKind.支路端口)) {
		    portlist.remove(p);
		    break;
		}
	    }
	}
    }
    
    public static void delTraffic(Traffic t) {
	// 删除前先释放资源
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
	return (this.fromNode.getName() + "——" + this.toNode.getName()+"-"+this.getId());
    }
    public int getRankId() {
		return rankId;
	}


	public void setRankId(int rankId) {
		this.rankId = rankId;
	}


	//setter、getter方法
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
	public static void	addTraffic(Traffic e){//静态方法用以维护链表，注意与实际对象无关
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
	public static void releaseAllPortResource() {// 清空业务表占用的所有资源
		for (Port port : Port.usedPortList) {
			port.getCarriedTraffic().clear();
			port.setRemainResource(1);
			port.setStatus(PortStatus.空闲);
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
				tra.setTrafficId("常规-"+(i+1));
			}else {
				tra.setTrafficId(tra.getTrafficgroup().getTheName()+tra.getNumRank());
			}
		}
	}
	


	
}
