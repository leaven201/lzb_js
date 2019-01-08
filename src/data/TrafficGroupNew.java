package data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import enums.TrafficGroupType;

public class TrafficGroupNew implements Serializable{
	private String name="���չ���ҵ����";     //���չ���ҵ����
	private int id;                      //���չ���ҵ����1A��1
	private String belongGroup;          //���չ���ҵ����1A��A
	private  String type;      //���չ���ҵ��������
	private String theName;   //����������ҵ����1-A�������㵼��
	
	public  List<Traffic> grouptrafficList=new LinkedList<>();//������������ҵ��
	public static List<TrafficGroupNew> grouplist=new LinkedList<>();
	
	//���췽��
	public TrafficGroupNew(int id,String belongGroup) {

		this.id=id;
		this.belongGroup=belongGroup;
		TrafficGroupNew.grouplist.add(this);
		this.theName = "����ҵ����"+id+"-"+belongGroup+"_";
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
			if(!a.equals(list.get(i).getName())) {//��һҵ������һҵ����ͬһ����ĩ��
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
