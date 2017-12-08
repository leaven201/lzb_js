package dataControl;
/**
 * 输入输出Node，添加删除Node，判断是否承载业务
 * @author CC
 * 
 */

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import data.*;
import database.NodeDataBase;
import enums.NodeType;
import enums.PortStatus;

public class NodeData {
    public static String s_sNodeMsg = "节点链路信息未导入";

    public static void clearNode() {
	CommonNode.allNodeList.clear();
	NodeDataBase.flag = true;
	System.gc();// 运行Garbage collector
    }

    public void inputNode(String str) {
	NodeDataBase dbs = new NodeDataBase();
	dbs.inputNode(str);
	if (dbs.msgr.equals(""))
	    s_sNodeMsg = dbs.msg;
	else
	    s_sNodeMsg = dbs.msgr;
    }

    public void outputNode(String str) {
	NodeDataBase node = new NodeDataBase();
	node.outputNode(str);
	s_sNodeMsg = node.msg;
    }

    public void addNode(int id, String name, double longitude, double latitude, boolean isActive, boolean isOA, 
	    boolean isROADM,boolean isOTN, int maxPortNum, double switchCapacity,NodeType nodeType,
	    int numOB,int numOP,int numOA,boolean isEleRegeneration,boolean isWaveConversion) {
//	CommonNode node = new CommonNode(id, name, longitude, latitude,isActive, isOA, 
//		    isROADM,isOTN, maxPortNum, switchCapacity,nodeType,numOB,numOP,numOA,isEleRegeneration,isWaveConversion);
	s_sNodeMsg = "添加一节点";
    }
    
	//lzb+3.22 添加节点
//	public void addNode(int id, String name, String otherName, double longitude, double latitude, NodeType nodeType,boolean iszhongji, int WSS, int upDown )
//	{
//		CommonNode node = new CommonNode(id, name, otherName, longitude, latitude,nodeType, iszhongji,WSS,upDown);
//		CommonNode.addCommonNode(node);
//	}
	
	public void addROADMNode(int id, String name, String otherName, double longitude, double latitude,boolean iszhongji, int WSS, int upDown )
	{
		CommonNode node = new CommonNode(id,name,otherName,longitude,latitude,iszhongji,WSS,upDown);
//		CommonNode.addROADMNode(node);
	}
	public void addOLANode(int id, String name, String otherName, double longitude, double latitude)
	{
		CommonNode node = new CommonNode(id,name,otherName,longitude,latitude);
//		CommonNode.addOLANode(node);
	}//lzb-9.22
    

    public boolean delNode(CommonNode node) {// 去除与node相关的链路，然后删除node
	LinkData linkdata = new LinkData();
	// System.out.println("fiber");
	for (int f = 0; f < BasicLink.allLinkList.size(); f++) {
	    if (BasicLink.allLinkList.get(f).getFromNode() == node
		    || node == BasicLink.allLinkList.get(f).getToNode()) {
		System.out.println(BasicLink.allLinkList.get(f).getName());
		linkdata.delLink(BasicLink.allLinkList.get(f));
		f = f - 1;// 这一步很重要
	    }
	}

	CommonNode.allNodeList.remove(node);
	return true;
    }

    public boolean isCarryTraffic(CommonNode node) { // 判断节点上是否承载业务
	List<Port> allportsList = new LinkedList<Port>();
	allportsList.addAll(node.getFiberPortList());
	allportsList.addAll(node.getOptPortList());
	allportsList.addAll(node.getElePortList());
	for (int i = 0; i < allportsList.size(); i++) {
	    Port port = allportsList.get(i);
	    // if(port.getM_eStatue().equals(PortStatus.通过)||port.getM_eStatue().equals(PortStatus.上路)||
	    // port.getM_eStatue().equals(PortStatus.下路))
	    // {
	    // return true ;
	    // }
	    if (port.getCarriedTraffic().size() != 0) {
		return true;
	    }
	}

	return false;

    }
}