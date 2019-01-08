package data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import enums.TrafficGroupType;

public class TrafficGroupNew implements Serializable{
	private String name="风险共享业务组";     //风险共享业务组
	private int id;                      //风险共享业务组1A的1
	private String belongGroup;          //风险共享业务组1A的A
	private  String type;      //风险共享业务组类型
	private String theName;   //命名（关联业务组1-A），方便导出
	
	public  List<Traffic> grouptrafficList=new LinkedList<>();//储存该组包含的业务
	public static List<TrafficGroupNew> grouplist=new LinkedList<>();
	
	//构造方法
	public TrafficGroupNew(int id,String belongGroup) {

		this.id=id;
		this.belongGroup=belongGroup;
		TrafficGroupNew.grouplist.add(this);
		this.theName = "关联业务组"+id+"-"+belongGroup+"_";
	}
	
	
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBelongGroup() {
		return belongGroup;
	}

	public void setBelongGroup(String belongGroup) {
		this.belongGroup = belongGroup;
	}
	


	public String getTheName() {
		return theName;
	}



	public void setTheName(String theName) {
		this.theName = theName;
	}



	public  List<Traffic> getGrouptrafficList() {
		return grouptrafficList;
	}

	public  void setGrouptrafficList(List<Traffic> grouptrafficList) {
		this.grouptrafficList = grouptrafficList;
	}



	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String trafficAndNum() {
		List<Traffic> list=this.grouptrafficList;
		List<Traffic> list2=new LinkedList<>();
		String a="";
		StringBuilder b=new StringBuilder();
		int n=0;
		for(int i=0;i<list.size();i++) {
			if(!a.equals(list.get(i).getName())) {//下一业务与上一业务不是同一个首末点
				list2.add(list.get(i));
				a=list.get(i).getName();
			}			
		}
		for(int i=0;i<list2.size();i++) {
			b.append(list.get(i).getName()+"("+list.get(i).getTrafficNum()+")");
		}
		return b.toString();
	}
	
	

}
