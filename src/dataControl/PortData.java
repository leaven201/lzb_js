package dataControl;

import java.util.Iterator;

import data.CommonNode;
import database.PortDataBase;

public class PortData {

	public static	String	s_sPortMsg="节点链路信息未导入";
	public void clearPort()
	{
		Iterator<CommonNode> it=CommonNode.allNodeList.iterator();
		while(it.hasNext()){
			CommonNode node=it.next();
			node.getFiberPortList().clear();
			node.getOptPortList().clear();
			node.getElePortList().clear();
		}
		PortDataBase.flag=true;
		System.gc();
	}
//	public void inputPort(String str)
//	{
//		PortDateBase port=new PortDateBase();
//		port.inputPort(str);
//		if(port.msgErro.equals(""))
//			s_sPortMsg=port.msg;
//		else
//			s_sPortMsg=port.msgErro;
//	}
	public void outputPort(String str)
	{
		PortDataBase port=new PortDataBase();
		port.outputPort(str);
		s_sPortMsg=port.msg;
	}
}
