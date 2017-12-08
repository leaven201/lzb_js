package data;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import enums.Layer;
import enums.TrafficGroupType;
import enums.TrafficLevel;
import enums.TrafficRate;
import enums.TrafficStatus;

public class TrafficGroup implements Serializable{
	private String name=null;
	private int groupId=Integer.MAX_VALUE;    //业务组编号,初始化为无穷大表示没有
	private int relatedId=Integer.MAX_VALUE-99;  //相关联的业务组id,初始化为无穷大表示没有关联
	private TrafficGroupType TraGroType=null;//共享风险业务组类型 
	private List<Traffic> trafficList = new LinkedList<Traffic>(); // 存储该业务组的业务
	public static List<TrafficGroup> grouptrafficGroupList = new LinkedList<TrafficGroup>();//存储所有的关联业务组
    
	
	private static final long serialVersionUID = 5289998508883929161L;
	private String m_sName; // 业务组名称
	public static List<TrafficGroup> s_lTrafficGroupList = new LinkedList<TrafficGroup>(); // 存储所有TrafficGroup静态链
	private boolean m_bSame;// 要求路由一致true 相斥 false
	private List<Traffic> m_lTrafficList = new LinkedList<Traffic>(); // 存储环业务组包含的业务
	
	//构造方法     输入1、业务；2、关联业务组id；3、相关联组id 
	public TrafficGroup(int groupid,Traffic tra) {
		this.groupId=groupid;
		this.trafficList.add(tra);
        TrafficGroup.grouptrafficGroupList.add(this);
		//将业务加入该业务组
	}
	
	


	public String toString() {
		return "关联业务组id"+this.groupId+"  包含业务："+this.trafficList;
	}
	//输出该关联业务组的名字
	public String toString1() {
		StringBuilder b1=new StringBuilder();
		for(int i=0;i<this.trafficList.size();i++)
		{
			b1.append("<"+this.trafficList.get(i).getFromNode().getName()+"-"+this.trafficList.get(i).getToNode().getName()+">"+this.trafficList.get(i).getId());
		}
		return b1.toString();
	}
	
//	public boolean isM_bSame() {
//		return m_bSame;
//	}
//
//	public void setM_bSame(boolean mBSame) {
//		m_bSame = mBSame;
//	}

//	public static TrafficGroup getTrafficGroup(String name) {
//		for (int i = 0; i < s_lTrafficGroupList.size(); i++) {
//			if (s_lTrafficGroupList.get(i).getM_sName().equals(name))
//				return s_lTrafficGroupList.get(i);
//		}
//		return null;
//	}

//	public int getM_nID() {
//		return m_nID;
//	}
//
//	public void setM_nID(int mNID) {
//		m_nID = mNID;
//	}

//	public String getM_sName() {
//		return m_sName;
//	}
//
//	public void setM_sName(String mSName) {
//		m_sName = mSName;
//	}

//	public List<Traffic> getM_lTrafficList() {
//		return m_lTrafficList;
//	}
//
//	public void setM_lTrafficList(List<Traffic> mLTrafficList) {
//		m_lTrafficList = mLTrafficList;
//	}
//
//	public static List<TrafficGroup> getS_lTrafficGroup() {
//		return s_lTrafficGroupList;
//	}
//
//	public static void setS_lTrafficGroup(List<TrafficGroup> sLTrafficGroup) {
//		s_lTrafficGroupList = sLTrafficGroup;
//	}

//	public void delTraffic(Traffic traffic) {
//		this.getM_lTrafficList().remove(traffic);
//
//	}

//	public void addTraffic(Traffic t) {
//
//		if (!m_lTrafficList.contains(t)) {
//			m_lTrafficList.add(t);
//
//		}
//	}
//
//	public static void addTrafficGroup(TrafficGroup tg) {
//		s_lTrafficGroupList.add(tg);
//	}
//
//	public static void delTrafficGroup(TrafficGroup tg) {
//		s_lTrafficGroupList.remove(tg);
//	}

//	public static TrafficGroup getTrafficGroup(int id) {
//		Iterator<TrafficGroup> it = s_lTrafficGroupList.iterator();
//		while (it.hasNext()) {
//			TrafficGroup theTrafficGroup = it.next();
//			if (theTrafficGroup.getM_nID() == id)
//				return theTrafficGroup;
//		}
//		return null;
//	}

	public Traffic getTraffic(int id) {
		Iterator<Traffic> it = this.m_lTrafficList.iterator();
		while (it.hasNext()) {
			Traffic tr = it.next();
			if (tr.getId() == id)
				return tr;
		}
		return null;
	}	
	public int getGroupId() {
		return groupId;
	}
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	public int getRelatedId() {
		return relatedId;
	}
	public void setRelatedId(int relatedId) {
		this.relatedId = relatedId;
	}
	public static List<TrafficGroup> getGrouptrafficGroupList() {
		return grouptrafficGroupList;
	}
	public static void setGrouptrafficGroupList(List<TrafficGroup> grouptrafficGroupList) {
		TrafficGroup.grouptrafficGroupList = grouptrafficGroupList;
	}
	//判断grouptrafficGroupList里面是否有groupId为i的TrafficGroup
	public static boolean isHaveGroupId(int i) {
		Iterator<TrafficGroup> it = grouptrafficGroupList.iterator();
		while(it.hasNext()) {
			TrafficGroup tg=it.next();
			if(tg.getGroupId()==i)
				return true;
		}
		return false;
	}
	//通过关联业务组的groupId返回关联业务组
	public static TrafficGroup gettrafficGroupById(int id) {
		Iterator<TrafficGroup> it = grouptrafficGroupList.iterator();
		while(it.hasNext()) {
			TrafficGroup tg=it.next();
			if(tg.getGroupId()==id)
				return tg;				
		}
		return null;
	}




	public List<Traffic> getTrafficList() {
		return trafficList;
	}

	public void setTrafficList(List<Traffic> trafficList) {
		this.trafficList = trafficList;
	}




	public String getName() {
		return name;
	}




	public void setName(String name) {
		this.name = name;
	}
	

}
