package data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class DaZu implements Serializable{
	
	private String name;
	public List<TrafficGroup> relatedGroupList = new LinkedList<TrafficGroup>();//储存大组包含的关联业务组
	public static List<DaZu> allGroupList = new LinkedList<DaZu>(); //储存所有的关联业务组（大组）
	//构造方法
	public DaZu(String name) {
		this.name=name;
		allGroupList.add(this);

	}
	
	
	public String toString1() {
		StringBuilder b1=new StringBuilder();
		for(int i=0;i<this.relatedGroupList.size();i++) {
			if(i!=this.relatedGroupList.size()-1)
			{b1.append("组 "+this.relatedGroupList.get(i).getName()+"与");}
			if(i==this.relatedGroupList.size()-1) {
				b1.append("组 "+this.relatedGroupList.get(i).getName());
			}
		}return b1.toString();
	}
	
	
	
	public void addGroup(TrafficGroup t) {

		if (!relatedGroupList.contains(t)) {
			relatedGroupList.add(t);

		}
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}



	public  List<TrafficGroup> getRelatedGroupList() {
		return relatedGroupList;
	}



	public  void setRelatedGroupList(List<TrafficGroup> relatedGroupList) {
		this.relatedGroupList = relatedGroupList;
	}



	public static List<DaZu> getAllGroupList() {
		return allGroupList;
	}



	public static void setAllGroupList(List<DaZu> allGroupList) {
		DaZu.allGroupList = allGroupList;
	}
	
	//一个大组里有两个关联业务组，需要将组1的groupId设为组2的relatedID，将组2的groupId设为组1的relatedID
	public void setRelatedId() {
		if(this.relatedGroupList.size()==2)
		{
			TrafficGroup tg1= this.relatedGroupList.get(0);
			TrafficGroup tg2= this.relatedGroupList.get(1);
			tg2.setRelatedId(tg1.getGroupId());
			tg1.setRelatedId(tg2.getGroupId());
		}
	}
	

	public static DaZu getDaZu(String name) {
		for (int i = 0; i < allGroupList.size(); i++) {
			if (allGroupList.get(i).getName().equals(name))
				return allGroupList.get(i);
		}
		return null;
	}
	
	public void delTrafficGroup(TrafficGroup tg) {
		this.relatedGroupList.remove(tg);

	}
	
	public void addTrafficGroup(TrafficGroup tg) {

		if (!relatedGroupList.contains(tg)) {
			relatedGroupList.add(tg);

		}
	}


	
	
	
	
//	public static RelatedGroup getGroup(String name) {
//		for (int i = 0; i < relatedGroupList.size(); i++) {
//			if (relatedGroupList.get(i).getM_sName().equals(name))
//				return s_lTrafficGroupList.get(i);
//		}
//		return null;
//	}
	

}
