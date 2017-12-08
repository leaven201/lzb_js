package greenDesign;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.StyledEditorKit.ForegroundAction;

import org.omg.CORBA.PUBLIC_MEMBER;
//
//import rcAllocate.NewAlloc;
//import rcAllocate.ResourceLogTxt;
//import rcAllocate.commonAlloc;
//import rcAllocate.portAlloc;
import twaver.Link;

//import algorithm.DijkstraAlgorithm;
import algorithm.algorithm;

import data.BasicLink;
import data.CommonNode;
//import data.Fiber;
import data.FiberLink;
import data.LinkRGroup;
import data.Network;
import data.Route;
//import data.SDHLink;
//import data.Timeslot;
import data.Traffic;
import data.WDMLink;
//import data.WDMSystem;
//import data.Wavelength;
import dataControl.LinkData;
import dataControl.TrafficData;
import dataControl.clearNetSource;
import design.NetDesign;
//import enums.Define;
import enums.Layer;
import enums.Status;
import enums.TrafficLevel;
import enums.TrafficRate;
import enums.TrafficStatus;

public class greenDesign {
	public static String msg=""; 
	public static int s=0;
	public static List linkList;//存放所有链路，包括网关链路和网内链路
	private static List sdhlinkList;//构建全连接网
	private static List linkcantdel=new LinkedList();
	private static List<Traffic> trfList;
	private static ArrayList<Double> utilList=new ArrayList<Double>();
	private static List<CommonNode> nodeList;
	private static Layer trfLayer;
	private static double e1=0;
	private static double e0=0;
	private static Object linktodel=null;
	private final static int infi=499;
	private static int mID=10000;
	public static boolean success= true;
	public static int s_setlimit;//wx
	public static int s_uselimit;//wx
	private static double linktodel_utl;//wx记录当前待删除链路的利用率
//	public static PrintWriter pw;
	public static LinkedList<CommonNode> GatewayNodeList;//wx 存放绿地中的网关节点
	public  static  LinkedList<CommonNode> 	InNetNodeList;//wx存放 网内节点
	//private static Network net=new Network("green",1,1);//核心网
	private static ArrayList<Integer> linkNumList=new ArrayList<Integer>();
	private static ArrayList<Integer> linkUsed=new ArrayList<Integer>();	
	public static boolean start(List<Traffic> trafficList,List<CommonNode> NodeList,Layer l,double target, double min,int setinglimit,int usinglimit,int flag){//wx 加了两个参数
//		try {
////			pw = new PrintWriter( new FileWriter( "C:\\打印日志.txt" ) );
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}f		
		s_setlimit=setinglimit;//wx
		s_uselimit=usinglimit;//wx
		
		trfList=trafficList;
		nodeList=NodeList;
		//Network.m_lNetworkList.add(net);
		trfLayer=l;
		e1=min;
		System.out.println(" 待规划业务速率 ：");
		for(int i=0; i!=trfList.size(); i++){
			System.out.println(trfList.get(i).getName()+"--"+trfList.get(i).getRate());
		}
		switch(trfLayer){
		case WDM:
			s=1;
			linkList=new LinkedList<WDMLink>();
			linktodel=new WDMLink();
			Traffic.setS_lWDMTrafficList(trfList);
			//Traffic.getS_lASONTrafficList().clear();//wx 3.4
			//Traffic.getS_lSDHTrafficList().clear();
			for (int y=0;y!=trfList.size();y++){
				trfList.get(y).setM_eLayer(Layer.WDM);
			}
			break;
//		case ASON://wx 3.4
//			s=2;
//			linkList=new LinkedList<SDHLink>();
//			linktodel=new SDHLink();			
//			//Traffic.s_lSDHTrafficList=trfList;
//			Traffic.s_lASONTrafficList=trfList;//wx 3.4
//			Traffic.getS_lWDMTrafficList().clear();
//			for (int y=0;y!=trfList.size();y++){
//				trfList.get(y).setM_eLayer(Layer.ASON);
//			}
//			break;
		default:
			return false;
		}
		//createAllConNetwork();//建立全连接拓扑
		createAllConNetwork2();//建立全连接拓扑
		
		//wx 3.5将资源分配占用资源释放
		switch (trfLayer) {
//		case ASON:
//			for (int  num= 0;  num< trafficList.size(); num++) {
//				Traffic trf=trafficList.get(num);
//				for (int i = 0; i < greenDesign.linkList.size(); i++) {
//					SDHLink linkin=(SDHLink)greenDesign.linkList.get(i);			
//						linkin.setM_nFree(setinglimit);
//						for (int j = 0; j != trf.getM_nRate(); j++) {
//							if(linkin.getTimeslot(j ).getM_eStatus().equals(Status.工作)||linkin.getTimeslot(j ).getM_eStatus().equals(Status.承载)||linkin.getTimeslot(j ).getM_eStatus().equals(Status.保护)||linkin.getTimeslot(j ).getM_eStatus().equals(Status.保护恢复)||linkin.getTimeslot(j ).getM_eStatus().equals(Status.保护恢复))
//							linkin.getTimeslot(j ).setM_eStatus(Status.空闲);
//							linkin.getTimeslot(j).setnFree(63);
//							if (linkin.getM_cCarriedTraffic().contains(trf)) {
//								linkin.delTraffic(trf); 
//							}
//							
//						}
//					
//				}
//			}
//			break;
			case WDM:
				for (int jj = 0; jj < linkList.size(); jj++) {
					WDMLink tmplink=(WDMLink)linkList.get(jj);
					tmplink.getCarriedTrafficList();
					for (int ii = 0; ii < tmplink.getCarriedTrafficList().size(); ii++) {
						tmplink.delTraffic(tmplink.getCarriedTrafficList().get(ii));
					}
				}
				for (int  num= 0;  num< trafficList.size(); num++) {
					Traffic trf=trafficList.get(num);
					for (int i = 0; i < greenDesign.linkList.size(); i++) {
						WDMLink linkin=(WDMLink)greenDesign.linkList.get(i);			
							linkin.setM_nFree(500);
							for (int j = 0; j != trf.getM_nRate(); j++) {
								if(linkin.getM_lWavelength(j).getM_eStatus().equals(Status.工作)||linkin.getM_lWavelength(j).getM_eStatus().equals(Status.承载)||linkin.getM_lWavelength(j).getM_eStatus().equals(Status.保护)||linkin.getM_lWavelength(j).getM_eStatus().equals(Status.保护恢复)||linkin.getM_lWavelength(j).getM_eStatus().equals(Status.保护恢复))
								linkin.getM_lWavelength(j).setM_eStatus(Status.空闲);
								linkin.getM_lWavelength(j).setM_nFree(400);
								if (linkin.getM_cCarriedTraffic().contains(trf)) {
									linkin.delTraffic(trf); 
								}
								
							}
						
					}
				}

		default:
			break;
		}				
				
		plan2();//计算每个方向的路由并且资源分配
//		for (int i = 0; i < linkList.size(); i++) {
//			WDMLink templink=(WDMLink)linkList.get(i);
//			for (int j = 0; j < templink.getM_nSize(); j++) {
//				Wavelength tmpWavelength=templink.getM_lWavelength(j);
//				if (tmpWavelength.getM_eStatus()==Status.工作) {
//					System.out.println("链路名称："+templink.getM_sName()+"波道号："+tmpWavelength.getM_nID());
//					
//				}
//			}
//		}
		findLinkNumber( setinglimit, usinglimit);//计算每个方向链路数，设置空闲波道数目 ，set s_lWDMlink 在plan2()中资源分配里面将对s_lWDMlink进行轮询 设置单个链路的利用率
		initialize();//链路利用率utilList链表清空，初始化
		//plan();//为什么要再plan一次？ wx
		plan2();//每个方向的链路数目可能产生变化，所以要重新计算路由和资源分配


		if(!success){
			JOptionPane.showMessageDialog(null,"业务不能规划成功。");
			return false;
		}
		e0=findNetUtil();
		System.out.println("=================");
		System.out.println("各条链路利用率：");
		for(int i=0;i!=linkList.size();i++){
			System.out.println(((BasicLink)(linkList.get(i))).getName() + " 利用率： " + utilList.get(i));
		}
		System.out.println("=================");
		int times=0;
		int timek=linkList.size();
		if (flag==1) {
			//节点度数大于等于2，正常绿地规划
			for(int i=0;i!=timek;i++){//这里有问题，规划次数限制成了28次，即链路的数量，所以需要进行修改！
				e0=findNetUtil();//wx 3.5更新e0值
				//System.out.println("全网平均利用率e0："+e0);
				if (((!(getMinUtil()<e1))&&(!(e0<target)))||linkList.size()==linkcantdel.size())
					break;
				findLink();//寻找待删除的链路，放入linktodel中
				if((getNodeDegree(((BasicLink) linktodel).getM_cFromNode())>2)&&(getNodeDegree(( ((BasicLink) linktodel).getM_cToNode()))>2)){
					times++;
					initialize();//清空每条链路的工作路由和保护路由
					if (linktodel_utl==0) {
						linkList.remove(linktodel);//先删除链路利用率为0，满足维度要求的链路，直到利用率为0的链路不可删除
						System.out.println("删除链路："+((BasicLink)(linktodel)).getName());
					}
					else if (linktodel_utl>0&&e0<target) {//待删除链路利用率大于0，但是网络平均利用率不满足，所以需要接着删除
						linkList.remove(linktodel);
						System.out.println("删除链路："+((BasicLink)(linktodel)).getName());
					}
					else {
						System.out.println("此次不删除链路");
					}
					//linkList.remove(linktodel);
					changeNet();			
				//	plan();
					plan2();
					if(!success){
						success=true;																	
						linkList.add(linktodel);
						changeNet();
						System.out.println("添加链路："+((BasicLink)(linktodel)).getName());
						//plan();
						plan2();
						e0=findNetUtil();
						System.out.println("=================");
						System.out.println("plan失败，把待删除链路重新加入linklist，再次打印各条链路利用率：");
						for(int g=0;g!=linkList.size();g++){
							System.out.println(((BasicLink)(linkList.get(g))).getName() + " 利用率： " + utilList.get(g));
						}
						System.out.println("=================");
						linkcantdel.add(linktodel);
						System.out.println("times"+times);
						continue;
					}
					e0=findNetUtil();//计算并且打印全网平均利用率
					System.out.println("=================");
					System.out.println("plan成功，打印各条链路利用率：");
					for(int g=0;g!=linkList.size();g++){
						System.out.println(((BasicLink)(linkList.get(g))).getName() + " 利用率： " + utilList.get(g));
					}
					System.out.println("=================");
					System.out.println("times"+times);
				}
				else{
					times++;
					linkcantdel.add(linktodel);
					e0=findNetUtil();
					System.out.println("=================");
					System.out.println("链路节点维度不大于2，重新打印各条链路利用率：");
					for(int g=0;g!=linkList.size();g++){
						System.out.println(((BasicLink)(linkList.get(g))).getName() + " 利用率： " + utilList.get(g));
					}
					System.out.println("=================");
					System.out.println("times"+times);
					continue;
				}
			}
		}
	else if(flag==0){
		//节点度数大于等于0即可，不考虑节点度数的绿地规划.链路利用率等于0时候可以删除 wx 2014.1.4
		for (int j = 0; j < timek; j++) {
			//这里有问题，规划次数限制成了28次，即链路的数量，所以需要进行修改！
			if (((!(getMinUtil()<e1))&&(!(e0<target)))||linkList.size()==linkcantdel.size())
				break;
			findLink();//寻找待删除的链路，放入linktodel中
			if (getMinUtil()!=0) {//如果删完了利用率为0的链路，删除利用率不为0的链路时候，发现链路的首末节点度数小于等于2，则此链路不能删除！ 即 如果节点度数等于2，只能删除利用率为0的链路。
				if ((getNodeDegree(((BasicLink) linktodel).getM_cFromNode())<=2)||(getNodeDegree(((BasicLink) linktodel).getM_cToNode())<=2)) {
					times++;
					linkcantdel.add(linktodel);
					e0=findNetUtil();
					System.out.println("=================");
					System.out.println("各条链路利用率：");
					for(int g=0;g!=linkList.size();g++){
						System.out.println(((BasicLink)(linkList.get(g))).getName() + " 利用率： " + utilList.get(g));
					}
					System.out.println("=================");
					System.out.println("times"+times);
					continue;
				}
			}
			if((getNodeDegree(((BasicLink) linktodel).getM_cFromNode())>0)&&(getNodeDegree(( ((BasicLink) linktodel).getM_cToNode()))>0)){
				times++;
				initialize();//清空每条链路的工作路由和保护路由
				linkList.remove(linktodel);
				changeNet();
				System.out.println("删除链路："+((BasicLink)(linktodel)).getName());
			//	plan();
				plan2();
				if(!success){
					success=true;
  				    linkList.add(linktodel);
					changeNet();
					System.out.println("添加链路："+((BasicLink)(linktodel)).getName());
					//plan();
					plan2();
					e0=findNetUtil();
					System.out.println("=================");
					System.out.println("各条链路利用率：");
					for(int g=0;g!=linkList.size();g++){
						System.out.println(((BasicLink)(linkList.get(g))).getName() + " 利用率： " + utilList.get(g));
					}
					System.out.println("=================");
					linkcantdel.add(linktodel);
					System.out.println("times"+times);
					continue;
				}
				e0=findNetUtil();
				System.out.println("=================");
				System.out.println("各条链路利用率：");
				for(int g=0;g!=linkList.size();g++){
					System.out.println(((BasicLink)(linkList.get(g))).getName() + " 利用率： " + utilList.get(g));
				}
				System.out.println("=================");
				System.out.println("times"+times);
			}
			else{
				times++;
				linkcantdel.add(linktodel);
				e0=findNetUtil();
				System.out.println("=================");
				System.out.println("各条链路利用率：");
				for(int g=0;g!=linkList.size();g++){
					System.out.println(((BasicLink)(linkList.get(g))).getName() + " 利用率： " + utilList.get(g));
				}
				System.out.println("=================");
				System.out.println("times"+times);
				continue;
			}
		
		}

	}
	
		if (!(e0<target))
		{
			System.out.println("新建拓扑规划成功");
			JOptionPane.showMessageDialog(null,"新建拓扑规划成功");
			ArrayList mark = new ArrayList<Integer>();
			for(int i=0;i<linkList.size();i++)
				mark.add((Integer)0);
			
			if(trfLayer == Layer.WDM)
			    for(int i=0;i!=linkList.size();i++)
			    {
			    	WDMLink refWDMLink = (((WDMLink)linkList.get(i)));
			    	int nUsed = 0;				    	
			    	
			    	for(int j=i;j!=linkList.size();j++)
			    	{		    	
			    		WDMLink tempWDMLink = (((WDMLink)linkList.get(j)));		    		
			    		if(tempWDMLink.getName() == refWDMLink.getName())
			    		{
			    			FiberLink objectFiberLink = null;
			    			if(nUsed > 36 || ((Integer)mark.get(i)) == 0)         //如果大于最大纤芯数或者是处理的第一条链路
			    			{
			    				int length=(int)GetDistance(tempWDMLink.getM_cFromNode().getLatitude(),tempWDMLink.getM_cFromNode().getLongitude(),tempWDMLink.getM_cToNode().getLatitude(),tempWDMLink.getM_cToNode().getLongitude());
			    				objectFiberLink = new FiberLink(tempWDMLink.getId(),tempWDMLink.getName(),tempWDMLink.getFromNode().getName(),tempWDMLink.getToNode().getName(),Layer.WDM,length,true,1,2012,36);
			    				FiberLink.fiberLinkList.add(objectFiberLink);          //添加拓扑中一条FiberLink		    				
			    				nUsed++;
			    			}
			    			else
			    			{
                                for(int k=0;k!=FiberLink.fiberLinkList.size();k++)
                                {
                                    if(sameFiberLink(FiberLink.fiberLinkList.get(k).getM_cFromNode().getId(),
                                        		FiberLink.fiberLinkList.get(k).getM_cToNode().getId()))
                                        	objectFiberLink = FiberLink.getFiberLink(k);                                 	
                                } 				                             
			    			}
			    								    	
//				    		WDMLink.getS_lWDMLinkList().add(((WDMLink)linkList.get(j)));       //拓扑中添加WDMLink
				    		tempWDMLink.getM_lFiberLinkList().add(objectFiberLink);       //把WDM链路承载在Fiber上
				    		mark.set(j, 1);
			    		}
			    		commonAlloc.dealWithInterLayerFiber(tempWDMLink, Layer.WDM);
			    		int size=linkList.size();
			    	}
			    }
			
			
			if(trfLayer == Layer.ASON)
			    for(int i=0;i!=linkList.size();i++)
			    {
			    	SDHLink refSDHLink = (((SDHLink)linkList.get(i)));
			    	int nUsed = 0;
			    	for(int j=i;j!=linkList.size();j++)
			    	{		    	
			    		SDHLink tempSDHLink = (((SDHLink)linkList.get(j)));		    		
			    		if(tempSDHLink.getM_sName() == refSDHLink.getM_sName())
			    		{
			    			FiberLink objectFiberLink = null;
			    			if(nUsed > 36 || ((Integer)mark.get(i)) == 0)
			    			{
			    				int length=(int)GetDistance(tempSDHLink.getM_cFromNode().getM_dLatitude(),tempSDHLink.getM_cFromNode().getM_dLongitude(),tempSDHLink.getM_cToNode().getM_dLatitude(),tempSDHLink.getM_cToNode().getM_dLongitude());
			    				objectFiberLink = new FiberLink(tempSDHLink.getM_nID(),tempSDHLink.getM_sName(),tempSDHLink.getM_cFromNode().getM_sName(),tempSDHLink.getM_cToNode().getM_sName(),Layer.ASON,length,true,1,2012,36);
			    				objectFiberLink.setM_sName(tempSDHLink.getM_sName());
			    				FiberLink.fiberLinkList.add(objectFiberLink);
			    				nUsed++;
			    			}
			    			else
			    			{			    				
			    				for(int k=0;k!=FiberLink.fiberLinkList.size();k++)
                                {
                                    if(sameFiberLink(FiberLink.fiberLinkList.get(k).getM_cFromNode().getId(),
                                    		FiberLink.fiberLinkList.get(k).getM_cToNode().getId()))
                                    	objectFiberLink = FiberLink.getFiberLink(k);                                	
                                }                               
			    			}
			    								    	
//			    			SDHLink.getS_lSDHLinkList().add(((SDHLink)linkList.get(j)));         //添加拓扑
			    			tempSDHLink.getM_lFiberLinkList().add(objectFiberLink);           //承载SDH到Fiber上
			    			tempSDHLink.setM_nType(1);
			    			mark.set(j, 1);
			    		}
			    		commonAlloc.dealWithInterLayerFiber(tempSDHLink, Layer.ASON);
			    	}
			    }
			System.out.println("经过(" + times + ")次规划，新建拓扑规划成功");
//			pw.close();
		    return true;
		}
		else
		{
			System.out.println("经过(" + times + ")次规划，新建拓扑规划失败");
			JOptionPane.showMessageDialog(null,"经过(" + times + ")次规划，新建拓扑规划失败");
//			pw.close();
			if(msg.equals("")){
				
				double a=findNetUtil()*100;				
				java.text.DecimalFormat   df1=new   java.text.DecimalFormat( "#.## "); 
				msg="经过(" + times + ")次规划，新建拓扑规划未能达到最低利用率。\n目前网络的平均利用率为："+df1.format(a)+"%";}
			return false;
		}
	}
	private static boolean sameFiberLink(int nFrom,int nTo)
	{
		for(int i=0;i!=FiberLink.fiberLinkList.size();i++)
		{
			FiberLink getLink = FiberLink.fiberLinkList.get(i);
			if ((getLink.getM_cFromNode().getId() == nFrom && getLink
					.getM_cToNode().getId() == nTo)|| 
				(getLink.getM_cFromNode().getId() == nTo && getLink
							.getM_cToNode().getId() == nFrom))
				return true;
		}
		
		return false;
	}
//	static void createAllConNetwork(){//建立全连接网络
//		for (int i=0;i!=nodeList.size()-1;i++){
//			int from_net_id=nodeList.get(i).getM_nSubnetNum();//首节点子网号
//			int from_netType=Network.searchNetwork(from_net_id).getM_nNetType();//首节点所属层类型
//			
//			for (int j=i+1;j!=nodeList.size();j++){
//				 int to_net_id=nodeList.get(j).getM_nSubnetNum();//末节点子网号
//				 int to_netType=Network.searchNetwork(to_net_id).getM_nNetType();//末节点所属层类型
//				if (trfLayer == Layer.WDM)									 				      
//					if (from_net_id==to_net_id||Network.searchNetwork(from_net_id).getM_nUpperNetwork().equals(Network.searchNetwork(to_net_id))||Network.searchNetwork(to_net_id).getM_nUpperNetwork().equals(Network.searchNetwork(from_net_id)))
//					{//如果首末节点在同一子网或者 首节点上层网络是末节点 或者末节点上层网络是首节点，这两个节点间可以构建链路
//					 //如果两个节点在同一层中不同子网中，这两节点不能构建链路，意思是不能直达
//						linkList.add(getNewWDMLink(nodeList.get(i),nodeList.get(j)));
//					}
//					
//				if (trfLayer == Layer.SDH)
//					if (from_net_id==to_net_id||Network.searchNetwork(from_net_id).getM_nUpperNetwork().equals(Network.searchNetwork(to_net_id))||Network.searchNetwork(to_net_id).getM_nUpperNetwork().equals(Network.searchNetwork(from_net_id))) {
//						linkList.add(getNewSDHLink(nodeList.get(i),nodeList.get(j)));
//					}
//					
//			}
//		}
//		if(trfLayer == Layer.WDM){
//			
//		    net.setM_lInNetWDMLinkList(linkList);
//		    net.setM_lInNetNodeList(nodeList);
//		}
//		if(trfLayer == Layer.SDH){
//			net.setM_lInNetSDHLinkList(linkList);
//			net.setM_lInNetNodeList(nodeList);
//		}
//	}
	/*
	 * 函数功能：分层分域构建全连接网络。如果两个节点属于同一子网，构建连接链路；如果两个节点不在同一子网，但是属于上下两个子网（从属关系，不能跨层），也可以构建连接；其余情况均不可。同一层内不同子网两个节点不能构建连接
	 * wx 2013.12.25
	 */
	static void createAllConNetwork2(){
		//建立全连接网络
				for (int i=0;i!=nodeList.size()-1;i++){
					int from_net_id=nodeList.get(i).getM_nSubnetNum();//首节点子网号
					int from_netType=Network.searchNetwork(from_net_id).getM_nNetType();//首节点所属层类型
					
					for (int j=i+1;j!=nodeList.size();j++){
						 int to_net_id=nodeList.get(j).getM_nSubnetNum();//末节点子网号
						 int to_netType=Network.searchNetwork(to_net_id).getM_nNetType();//末节点所属层类型
						if (trfLayer == Layer.WDM)	{
							if (from_net_id==to_net_id)
							{//如果首末节点在同一子网这两个节点间可以构建链路
							 //如果两个节点在同一层中不同子网中，这两节点不能构建链路，意思是不能直达
								linkList.add(getNewWDMLink(nodeList.get(i),nodeList.get(j)));//存放所有链路
								//Network.searchNetwork(from_net_id).setM_lInNetWDMLinkList(linkList);
								Network.searchNetwork(from_net_id).addInNetLink(getNewWDMLink(nodeList.get(i),nodeList.get(j)));//每个网络增加网内链路
								Network.searchNetwork(from_net_id).addNode(nodeList.get(i));//每个网络对应增加网内节点
								Network.searchNetwork(to_net_id).addNode(nodeList.get(j));
							}
							else if (Network.searchNetwork(from_net_id).getM_nUpperNetwork().equals(Network.searchNetwork(to_net_id))||Network.searchNetwork(to_net_id).getM_nUpperNetwork().equals(Network.searchNetwork(from_net_id))) {
							//或者 首节点上层网络是末节点 或者末节点上层网络是首节点，这两个节点间可以构建链路;即他们是上下层关系
							WDMLink tempLink=getNewWDMLink(nodeList.get(i),nodeList.get(j));
							linkList.add(tempLink);						
							Network.searchNetwork(from_net_id).addGatewayLinkList(tempLink);//每个网络增加网关链路
							Network.searchNetwork(to_net_id).addGatewayLinkList(tempLink);
							Network.searchNetwork(from_net_id).addGatewayNodeList(nodeList.get(i));//首节点所在网络增加网关节点
							Network.searchNetwork(to_net_id).addGatewayNodeList(nodeList.get(j));//末节点.....
						}
							else continue;
						}								 				      
						
							
//						if (trfLayer == Layer.ASON)
//						{
//							if (from_net_id==to_net_id)
//						    {//如果首末节点在同一子网这两个节点间可以构建链路
//							 //如果两个节点在同一层中不同子网中，这两节点不能构建链路，意思是不能直达
//								linkList.add(getNewASONLink(nodeList.get(i),nodeList.get(j)));//存放所有链路
//								//Network.searchNetwork(from_net_id).setM_lInNetWDMLinkList(linkList);
//								Network.searchNetwork(from_net_id).addInNetLink(getNewASONLink(nodeList.get(i),nodeList.get(j)));//每个网络增加网内链路
//								Network.searchNetwork(from_net_id).addNode(nodeList.get(i));//每个网络对应增加网内节点
//								Network.searchNetwork(to_net_id).addNode(nodeList.get(j));
//							}
//						if (Network.searchNetwork(from_net_id).getM_nUpperNetwork().equals(Network.searchNetwork(to_net_id))||Network.searchNetwork(to_net_id).getM_nUpperNetwork().equals(Network.searchNetwork(from_net_id))) 
//						{
//							//或者 首节点上层网络是末节点 或者末节点上层网络是首节点，这两个节点间可以构建链路;即他们是上下层关系
//							SDHLink tempSDHlink=getNewASONLink(nodeList.get(i),nodeList.get(j));
//							linkList.add(tempSDHlink);
//							Network.searchNetwork(from_net_id).addGatewayLinkList(tempSDHlink);//每个网络增加网关链路
//							Network.searchNetwork(to_net_id).addGatewayLinkList(tempSDHlink);
//							Network.searchNetwork(from_net_id).addGatewayNodeList(nodeList.get(i));//首节点所在网络增加网关节点
//							Network.searchNetwork(to_net_id).addGatewayNodeList(nodeList.get(j));//末节点.....
//						}
//						else continue;
//					}												
				}
			}
	}
	private static double rad(double d)
	{
	   return d * Math.PI / 180.0;
	}
	private static double EARTH_RADIUS = 6378.137; //地球半径
	public static double GetDistance(double lat1, double lng1, double lat2, double lng2)
	{
	   double radLat1 = rad(lat1);
	   double radLat2 = rad(lat2);
	   double a = radLat1 - radLat2;
	   double b = rad(lng1) - rad(lng2);
	   double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
	    Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
	   s = s * EARTH_RADIUS;
	   s = Math.round(s * 10000) / 10000;
	   return s;
	}
	private static WDMLink getNewWDMLink(CommonNode From,CommonNode To){//根据首末节点新建链路
		int length=(int)GetDistance(From.getLatitude(),From.getLongitude(),To.getLatitude(),To.getLongitude());
		WDMLink tmp= new WDMLink(mID++,From.getName() + "-" + To.getName(),From.getName(),To.getName(),Layer.WDM,length,400,infi,true,1,2012);
		tmp.setFromNode(From);
		tmp.setToNode(To);
		tmp.setM_nFree(tmp.getSize());
		for(int i=0;i!=tmp.getSize();i++)
		{
			Wavelength wl = tmp.getM_lWavelength(i);
			wl.getM_cFrom().setNode(From);
			wl.getM_cTo().setNode(To);
		}
		return tmp;
	}
	private static WDMLink getNewWDMLink(CommonNode From,CommonNode To,int n){//新建WDM链路
		int length=(int)GetDistance(From.getLatitude(),From.getLongitude(),To.getLatitude(),To.getLongitude());
		WDMLink tmp= new WDMLink(mID++,From.getName() + "-" + To.getName()+n,From.getName(),To.getName(),Layer.WDM,length,400,infi,true,1,2012);
		tmp.setToNode(To);
		tmp.setM_nFree(tmp.getSize());
		for(int i=0;i!=tmp.getM_nSize();i++)
		{
			Wavelength wl = tmp.getM_lWavelength(i);
			wl.getM_cFrom().setNode(From);
			wl.getM_cTo().setNode(To);
		}
		return tmp;
	}
//	private static SDHLink getNewSDHLink(CommonNode From,CommonNode To){
//		int length=(int)GetDistance(From.getM_dLatitude(),From.getM_dLongitude(),To.getM_dLatitude(),To.getM_dLongitude());
//		SDHLink tmp= new SDHLink(mID++,From.getM_sName() + "-" + To.getM_sName(),From.getM_sName(),To.getM_sName(),Layer.SDH,length,infi,infi,true,1,2012,Layer.FIBER);
//		tmp.setM_cFromNode(From);
//		tmp.setM_cToNode(To);
//		tmp.setM_nFree(tmp.getM_nSize());
//		return tmp;
//	}
//	private static SDHLink getNewASONLink(CommonNode From,CommonNode To){
//		int length=(int)GetDistance(From.getLatitude(),From.getLongitude(),To.getLatitude(),To.getLongitude());
//		SDHLink tmp= new SDHLink(mID++,From.getM_sName() + "-" + To.getM_sName(),From.getM_sName(),To.getM_sName(),Layer.ASON,length,infi,infi,true,1,2012,Layer.FIBER);
//		tmp.setM_cFromNode(From);
//		tmp.setM_cToNode(To);
//		tmp.setM_nFree(tmp.getM_nSize());
//		return tmp;
//	}//WX 3.4
//	private static SDHLink getNewSDHLink(CommonNode From,CommonNode To,int n){
//		int length=(int)GetDistance(From.getLatitude(),From.getLongitude(),To.getLatitude(),To.getLongitude());
//		SDHLink tmp= new SDHLink(mID++,From.getM_sName() + "-" + To.getM_sName(),From.getM_sName(),To.getM_sName(),Layer.SDH,length,infi,infi,true,1,2012,Layer.FIBER);
//		tmp.setM_cFromNode(From);
//		tmp.setM_cToNode(To);
//		tmp.setM_nFree(tmp.getM_nSize());
//		return tmp;
//	}
//	private static SDHLink getNewASONLink(CommonNode From,CommonNode To,int n){
//		int length=(int)GetDistance(From.getM_dLatitude(),From.getM_dLongitude(),To.getM_dLatitude(),To.getM_dLongitude());
//		SDHLink tmp= new SDHLink(mID++,From.getM_sName() + "-" + To.getM_sName(),From.getM_sName(),To.getM_sName(),Layer.ASON,length,infi,infi,true,1,2012,Layer.FIBER);
//		tmp.setM_cFromNode(From);
//		tmp.setM_cToNode(To);
//		tmp.setM_nFree(tmp.getM_nSize());
//		return tmp;
//	}
//	static void plan(){//整体规划入口
//		algorithm.Initial_Link_Weight(linkList,Define.by_length);//初始化链路权重
//		if (trfLayer==Layer.WDM){
//			int tnt=0;//计算业务数量
//			for (int i=0; i!=trfList.size(); i++){//为每条业务计算工作路由和保护路由，进行资源分配
//				tnt++;
//				Traffic temp= trfList.get(i);//从业务链中取出业务，判断速率是否符合条件
//				LinkedList<WDMLink> wdmLinkList= new LinkedList<WDMLink>();
//				if(temp.getM_nRate()!=10&&temp.getM_nRate()!=100&&temp.getM_nRate()!=400&&temp.getM_nRate()!=25) {
//					JOptionPane.showMessageDialog(null,"业务速率与所属层不符合","提示",JOptionPane.INFORMATION_MESSAGE);
//					System.out.println(" 业务速率与所属层速率不符  ");
//					success=false;
//					return;
//				}
//				algorithm.Initial_Link_Weight(linkList,Define.by_length);//初始化链路权重
// 				algorithm.SetNodeRankID(nodeList);
//				D(net,temp.getM_nFromNode(),temp.getM_nToNode(),wdmLinkList);//d算法计算路由，返回的路由放在wdmlinklist中
// 				temp.setM_cWorkRoute(wdmLinkList);//set工作路由
// 				if(wdmLinkList.size()==0) {//如果工作路由链路为空，则释放资源，路由计算失败
//					for(int n=0;n!=tnt-1;n++){
//						trfList.get(n).releaseTraffic(trfList.get(n),0);//释放工作路由占用资源
//						trfList.get(n).getM_cWorkRoute().clearRoute();//清空工作路由
//						if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){
//							trfList.get(n).releaseTraffic(trfList.get(n),1);//释放保护路由占用资源
//							trfList.get(n).getM_cProtectRoute().clearRoute();//清空保护路由
//							
//						}
//					}
//					System.out.println("路由计算失败");
//					success=false;
//					return;
//				}
//				if(!commonAlloc.ResourceAssignment(temp, 0)){//工作路由资源分配失败的情况
//					for(int n=0;n!=tnt-1;n++){
//						trfList.get(n).releaseTraffic(trfList.get(n),0);
//						trfList.get(n).getM_cWorkRoute().clearRoute();
//						if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){
//							trfList.get(n).releaseTraffic(trfList.get(n),1);
//							trfList.get(n).getM_cProtectRoute().clearRoute();
//							
//						}
//					}
//					temp.getM_cWorkRoute().clearRoute();
//					System.out.println("资源分配失败");
//					System.out.println("===========");
//					success=false;
//					return;
//				}
//                if(temp.getM_eLevel().equals(TrafficLevel.PERMANENT11)||temp.getM_eLevel().equals(TrafficLevel.NORMAL11)){//如果业务有保护路由，则需要考虑保护路由的情况
//                	DeleteSRLGList(wdmLinkList,false);//删除srlg，通过设置权重为无限大，表示不可到达
//                	for (int f=0;f<temp.getM_cWorkRoute().getM_lWDMLinkList().size();f++){//从工作路由链路中取出链路，设置未激活
//                		WDMLink theLink=temp.getM_cWorkRoute().getM_lWDMLinkList().get(f);
//                		theLink.setM_bStatus(false);//先将工作路由的链路设置为未激活，方便进一步为保护路由算路
//                	}
//                	LinkedList<WDMLink> prwdmLinkList = new LinkedList<WDMLink>();
//                	D(net,temp.getM_nFromNode(),temp.getM_nToNode(),prwdmLinkList);//d算法计算保护路由
//                	for (int f=0;f<temp.getM_cWorkRoute().getM_lWDMLinkList().size();f++){
//                		WDMLink theLink=temp.getM_cWorkRoute().getM_lWDMLinkList().get(f);
//                		theLink.setM_bStatus(true);//算路后，从工作路由链路中取出链路，设置为激活状态
//                	}
//                	temp.setM_cProtectRoute(prwdmLinkList);//设置保护路由
//                	if(prwdmLinkList.size()==0){//如果保护路由计算失败
//                		for(int n=0;n!=tnt-1;n++){						
//    						trfList.get(n).releaseTraffic(trfList.get(n),0);
//    						trfList.get(n).getM_cWorkRoute().clearRoute();
//    						if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){
//    							trfList.get(n).releaseTraffic(trfList.get(n),1);
//    							trfList.get(n).getM_cProtectRoute().clearRoute();
//    							
//    						}
//    					}
//                		temp.releaseTraffic(temp,0);
//                		temp.getM_cWorkRoute().clearRoute();
//                		System.out.println("保护路由计算失败");
//    					success=false;
//    					return;
//                	}
//                	if(!commonAlloc.ResourceAssignment(temp, 1)){//如果保护路由计算失败
//                		for(int n=0;n!=tnt-1;n++){
//                			trfList.get(n).releaseTraffic(trfList.get(n),0);//释放工作路由、保护路由、并清空链路
//							trfList.get(n).releaseTraffic(trfList.get(n),1);
//                			trfList.get(n).getM_cWorkRoute().clearRoute();
//							trfList.get(n).getM_cProtectRoute().clearRoute();
//							
//						}
//                		temp.releaseTraffic(temp,0);
//                		temp.getM_cWorkRoute().clearRoute();
//						temp.getM_cProtectRoute().clearRoute();
//						System.out.println("工作链路成功，保护链路失败/n");
//						System.out.println("===========");
//						success=false;
//						return;
//                	}
//                }
//                
//                System.out.println(" 业务-"+tnt+"   完成 ");
//			}
//		}
//		if (trfLayer==Layer.SDH) {//同上
//			int tnt=0;
//			for (int i = 0; i != trfList.size(); ++i){
//				tnt++;
//				Traffic aTraffic = trfList.get(i);
//				LinkedList<SDHLink> sdhLinkList = new LinkedList<SDHLink>();
//				if(aTraffic.getM_nRate()!=64&&aTraffic.getM_nRate()!=16&&aTraffic.getM_nRate()!=100&&aTraffic.getM_nRate()!=4&&aTraffic.getM_nRate()!=1&&aTraffic.getM_nRate()!=50&&aTraffic.getM_nRate()!=2){
//					JOptionPane.showMessageDialog(null,"业务速率与所属层不符合","提示",JOptionPane.INFORMATION_MESSAGE);
//					System.out.println(" 业务速率与所属层速率不符  ");
//					success=false;
//					return;
//				}
//				algorithm.Initial_Link_Weight(linkList,Define.by_length);
//				algorithm.SetNodeRankID(nodeList);		
//				D(net,aTraffic.getM_nFromNode(),aTraffic.getM_nToNode(),sdhLinkList);
//				if(sdhLinkList.size() == 0){
//					for(int n=0;n!=tnt-1;n++){					
//						trfList.get(n).releaseTraffic(trfList.get(n),0);
//						trfList.get(n).getM_cWorkRoute().clearRoute();
//						if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){
//							trfList.get(n).releaseTraffic(trfList.get(n),1);
//							trfList.get(n).getM_cProtectRoute().clearRoute();
//						}
//					}
//					System.out.println("路由计算失败");
//					success=false;
//					return;
//				}
//				else{
//					aTraffic.setM_cWorkRoute(sdhLinkList);
//					boolean signal = commonAlloc.ResourceAssignment(aTraffic, 0);
//					if (!signal){
//						for(int n=0;n!=tnt-1;n++){
//							
//							trfList.get(n).releaseTraffic(trfList.get(n),0);
//							trfList.get(n).getM_cWorkRoute().clearRoute();
//							if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){								
//								trfList.get(n).releaseTraffic(trfList.get(n),1);
//								trfList.get(n).getM_cProtectRoute().clearRoute();
//							}
//						}
//						aTraffic.getM_cWorkRoute().clearRoute();
//						System.out.println("资源分配失败");
//						System.out.println("===========");
//						success=false;
//						return;
//					}
//					if (signal)
//						System.out.println("资源分配成功");
//					if (aTraffic.getM_eLevel().equals(TrafficLevel.PERMANENT11)||aTraffic.getM_eLevel().equals(TrafficLevel.NORMAL11)){
//						DeleteSRLGList(sdhLinkList, false);
//						for (int f=0;f<aTraffic.getM_cWorkRoute().getM_lSDHLinkList().size();f++){
//	                		SDHLink theLink=aTraffic.getM_cWorkRoute().getM_lSDHLinkList().get(f);
//	                		theLink.setM_bStatus(false);
//	                	}
//						LinkedList<SDHLink> prsdhLinkList = new LinkedList<SDHLink>();
//						D(net,aTraffic.getM_nFromNode(),aTraffic.getM_nToNode(),prsdhLinkList);
//						for (int f=0;f<aTraffic.getM_cWorkRoute().getM_lSDHLinkList().size();f++){
//		            		SDHLink theLink=aTraffic.getM_cWorkRoute().getM_lSDHLinkList().get(f);
//		            		theLink.setM_bStatus(true);
//		            	}
//						aTraffic.setM_cProtectRoute(prsdhLinkList);
//						if(prsdhLinkList.size()==0){
//	                		for(int n=0;n!=tnt-1;n++){						
//	    						trfList.get(n).releaseTraffic(trfList.get(n),0);
//	    						trfList.get(n).getM_cWorkRoute().clearRoute();
//	    						if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){
//	    							trfList.get(n).releaseTraffic(trfList.get(n),1);
//	    							trfList.get(n).getM_cProtectRoute().clearRoute();
//	    							
//	    						}
//	    					}
//	                		aTraffic.releaseTraffic(aTraffic,0);
//	                		aTraffic.getM_cWorkRoute().clearRoute();
//	                		System.out.println("保护路由计算失败");
//	    					success=false;
//	    					return;
//	                	}
//						if(!commonAlloc.ResourceAssignment(aTraffic, 1)){
//							for(int n=0;n!=tnt-1;n++){							
//								trfList.get(n).releaseTraffic(trfList.get(n),0);
//								trfList.get(n).releaseTraffic(trfList.get(n),1);
//								trfList.get(n).getM_cWorkRoute().clearRoute();
//								trfList.get(n).getM_cProtectRoute().clearRoute();
//							}
//							aTraffic.releaseTraffic(aTraffic,0);
//							aTraffic.getM_cProtectRoute().clearRoute();
//							aTraffic.getM_cWorkRoute().clearRoute();
//							System.out.println("工作链路成功，保护链路失败/n");
//							System.out.println("===========");
//							success=false;
//							return;
//						}
//					}
//				}
//				
//			}
//		}
//	}
	
	public static void plan2(){//wx 分层分域
		//整体规划入口
//		commonAlloc.sFallbuffer=new StringBuffer();//情况资源分配失败列表 wx 3.17
//		portAlloc.sPortFallBuffer=new StringBuffer();//同上
				algorithm.Initial_Link_Weight(linkList,Define.by_length);//初始化链路权重
				if (trfLayer==Layer.WDM){
					int tnt=0;//计算业务数量
					for (int i=0; i!=trfList.size(); i++){//为每条业务计算工作路由和保护路由，进行资源分配
						tnt++;
						Traffic temp= trfList.get(i);//从业务链中取出业务，判断速率是否符合条件
					//	LinkedList<WDMLink> wdmLinkList= new LinkedList<WDMLink>();
						if(temp.getM_nRate()!=10&&temp.getM_nRate()!=100&&temp.getM_nRate()!=400&&temp.getM_nRate()!=25) {
							JOptionPane.showMessageDialog(null,"业务速率与所属层不符合","提示",JOptionPane.INFORMATION_MESSAGE);
							System.out.println(" 业务速率与所属层速率不符  ");
							success=false;
							return;
						}
						
						//判断所属层、域 wx
						
						LinkedList returnList=new LinkedList();
						algorithm.SetNodeRankID(nodeList);
						Route route=algorithm.RWAMain(temp.getFromNode(),temp.getToNode(),temp.getM_eLayer(),2,returnList);//2为按长度							 				
						if(route!=null)
							for (int j = 0; j < route.getM_lWDMLinkList().size(); j++) {
							System.out.println(route.getM_lWDMLinkList().get(j).getM_sName());	
							}//测试 wx
						temp.setM_cWorkRoute(route);//set工作路由
						algorithm.rmRepeat(temp,0);//firesky 2012 4.19
		 				
		 				if(route==null) {//如果工作路由链路为空，则释放资源，路由计算失败
							for(int n=0;n!=tnt-1;n++){
								trfList.get(n).releaseTraffic(trfList.get(n),0);//释放工作路由占用资源
								trfList.get(n).getM_cWorkRoute().clearRoute();//清空工作路由
								if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){
									trfList.get(n).releaseTraffic(trfList.get(n),1);//释放保护路由占用资源
									trfList.get(n).getM_cProtectRoute().clearRoute();//清空保护路由
									
								}
							}
							System.out.println("路由计算失败");
							success=false;
							return;
						}
						if(!commonAlloc.ResourceAssignment(temp, 0)){//工作路由资源分配失败的情况
							for(int n=0;n!=tnt-1;n++){
								trfList.get(n).releaseTraffic(trfList.get(n),0);
								trfList.get(n).getM_cWorkRoute().clearRoute();
								if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){
									trfList.get(n).releaseTraffic(trfList.get(n),1);
									trfList.get(n).getM_cProtectRoute().clearRoute();
									
								}
							}
							temp.getM_cWorkRoute().clearRoute();
							System.out.println("资源分配失败");
							System.out.println("===========");
							success=false;
							return;
						}
		                if(temp.getM_eLevel().equals(TrafficLevel.PERMANENT11)||temp.getM_eLevel().equals(TrafficLevel.NORMAL11)){//如果业务有保护路由，则需要考虑保护路由的情况
		                	DeleteSRLGList(route.getM_lWDMLinkList(),false);//删除srlg，通过设置权重为无限大，表示不可到达
		                	DeleteSRLGList(route.getM_lOTNLinkList(),false);
		                	for (int f=0;f<temp.getM_cWorkRoute().getM_lWDMLinkList().size();f++){//从工作路由链路中取出链路，设置未激活
		                		WDMLink theLink=temp.getM_cWorkRoute().getM_lWDMLinkList().get(f);
		                		theLink.setM_bStatus(false);//先将工作路由的链路设置为未激活，方便进一步为保护路由算路
		                	}
		                	Route routeP=algorithm.RWAMain(temp.getM_nFromNode(),temp.getM_nToNode(),temp.getM_eLayer(),2,returnList);
		    				if(routeP!=null)
		    					temp.setM_cProtectRoute(routeP);
		    				algorithm.rmRepeat(temp,1);//firesky 2012 4.19
//		                	
		                	for (int f=0;f<temp.getM_cWorkRoute().getM_lWDMLinkList().size();f++){
		                		WDMLink theLink=temp.getM_cWorkRoute().getM_lWDMLinkList().get(f);
		                		theLink.setM_bStatus(true);//算路后，从工作路由链路中取出链路，设置为激活状态
		                	}
		                	
		                	if(routeP==null){//如果保护路由计算失败
		                		for(int n=0;n!=tnt-1;n++){						
		    						trfList.get(n).releaseTraffic(trfList.get(n),0);
		    						trfList.get(n).getM_cWorkRoute().clearRoute();
		    						if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){
		    							trfList.get(n).releaseTraffic(trfList.get(n),1);
		    							trfList.get(n).getM_cProtectRoute().clearRoute();
		    							
		    						}
		    					}
		                		temp.releaseTraffic(temp,0);
		                		temp.getM_cWorkRoute().clearRoute();
		                		System.out.println("保护路由计算失败");
		    					success=false;
		    					return;
		                	}
		                	if(!commonAlloc.ResourceAssignment(temp, 1)){//如果保护路由计算失败
		                		for(int n=0;n!=tnt-1;n++){
		                			trfList.get(n).releaseTraffic(trfList.get(n),0);//释放工作路由、保护路由、并清空链路
									trfList.get(n).releaseTraffic(trfList.get(n),1);
		                			trfList.get(n).getM_cWorkRoute().clearRoute();
									trfList.get(n).getM_cProtectRoute().clearRoute();
									
								}
		                		temp.releaseTraffic(temp,0);
		                		temp.getM_cWorkRoute().clearRoute();
								temp.getM_cProtectRoute().clearRoute();
								System.out.println("工作链路成功，保护链路失败/n");
								System.out.println("===========");
								success=false;
								return;
		                	}
		                }
		                
		                System.out.println(" 业务-"+tnt+"   完成 ");
		                for (int ii = 0; ii< linkList.size(); ii++) {
		        			WDMLink templink=(WDMLink)linkList.get(ii);
		        			for (int j = 0; j < templink.getM_nSize(); j++) {
		        				Wavelength tmpWavelength=templink.getM_lWavelength(j);
		        				if (tmpWavelength.getM_eStatus()==Status.工作) {
		        					System.out.println("链路名称："+templink.getM_sName()+"波道号："+tmpWavelength.getM_nID());
		        					
		        				}
		        			}
		        		}
		    		
					}
				}
				
				
				
				else if (trfLayer==Layer.ASON) {
					int tnt=0;//计算业务数量
					for (int i=0; i!=trfList.size(); i++){//为每条业务计算工作路由和保护路由，进行资源分配
						tnt++;
						Traffic temp= trfList.get(i);//从业务链中取出业务，判断速率是否符合条件
						//LinkedList<WDMLink> wdmLinkList= new LinkedList<WDMLink>();
						if(temp.getM_nRate()!=64&&temp.getM_nRate()!=16&&temp.getM_nRate()!=4&&temp.getM_nRate()!=1) {
							JOptionPane.showMessageDialog(null,"业务速率与所属层不符合","提示",JOptionPane.INFORMATION_MESSAGE);
							System.out.println(" 业务速率与所属层速率不符  ");
							success=false;
							return;
						}
						
						//判断所属层、域 wx
						
						LinkedList returnList=new LinkedList();
						algorithm.SetNodeRankID(nodeList);
						Route route=algorithm.RWAMain(temp.getM_nFromNode(),temp.getM_nToNode(),temp.getM_eLayer(),2,returnList);//2为按长度							 				
						if(route!=null)
						temp.setM_cWorkRoute(route);//set工作路由
						algorithm.rmRepeat(temp,0);//firesky 2012 4.19
		 				
		 				if(route==null) {//如果工作路由链路为空，则释放资源，路由计算失败
							for(int n=0;n!=tnt-1;n++){
								trfList.get(n).releaseTraffic(trfList.get(n),0);//释放工作路由占用资源
								trfList.get(n).getM_cWorkRoute().clearRoute();//清空工作路由
								if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){
									trfList.get(n).releaseTraffic(trfList.get(n),1);//释放保护路由占用资源
									trfList.get(n).getM_cProtectRoute().clearRoute();//清空保护路由
									
								}
							}
							System.out.println("路由计算失败");
							success=false;
							return;
						}
						if(!commonAlloc.ResourceAssignment(temp, 0)){//工作路由资源分配失败的情况
							for(int n=0;n!=tnt-1;n++){
								trfList.get(n).releaseTraffic(trfList.get(n),0);
								trfList.get(n).getM_cWorkRoute().clearRoute();
								if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){
									trfList.get(n).releaseTraffic(trfList.get(n),1);
									trfList.get(n).getM_cProtectRoute().clearRoute();
									
								}
							}
							temp.getM_cWorkRoute().clearRoute();
							System.out.println("资源分配失败");
							System.out.println("===========");
							success=false;
							return;
						}
		                if(temp.getM_eLevel().equals(TrafficLevel.PERMANENT11)||temp.getM_eLevel().equals(TrafficLevel.NORMAL11)){//如果业务有保护路由，则需要考虑保护路由的情况
		                	DeleteSRLGList(route.getM_lSDHLinkList(),false);//删除srlg，通过设置权重为无限大，表示不可到达
		                	DeleteSRLGList(route.getM_lASONLinkList(),false);
		                	for (int f=0;f<temp.getM_cWorkRoute().getM_lASONLinkList().size();f++){//从工作路由链路中取出链路，设置未激活
		                		SDHLink theLink = temp.getM_cWorkRoute().getM_lASONLinkList().get(f);
		                		theLink.setM_bStatus(false);//先将工作路由的链路设置为未激活，方便进一步为保护路由算路
		                	}
		                	Route routeP=algorithm.RWAMain(temp.getM_nFromNode(),temp.getM_nToNode(),temp.getM_eLayer(),2,returnList);
		    				if(routeP!=null)
		    					temp.setM_cProtectRoute(routeP);
		    				algorithm.rmRepeat(temp,1);//firesky 2012 4.19
//		                	
		                	for (int f=0;f<temp.getM_cWorkRoute().getM_lASONLinkList().size();f++){
		                		SDHLink theLink= temp.getM_cWorkRoute().getM_lASONLinkList().get(f);
		                		theLink.setM_bStatus(true);//算路后，从工作路由链路中取出链路，设置为激活状态
		                	}
		                	
		                	if(routeP==null){//如果保护路由计算失败
		                		for(int n=0;n!=tnt-1;n++){						
		    						trfList.get(n).releaseTraffic(trfList.get(n),0);
		    						trfList.get(n).getM_cWorkRoute().clearRoute();
		    						if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){
		    							trfList.get(n).releaseTraffic(trfList.get(n),1);
		    							trfList.get(n).getM_cProtectRoute().clearRoute();
		    							
		    						}
		    					}
		                		temp.releaseTraffic(temp,0);
		                		temp.getM_cWorkRoute().clearRoute();
		                		System.out.println("保护路由计算失败");
		    					success=false;
		    					return;
		                	}
		                	if(!commonAlloc.ResourceAssignment(temp, 1)){//如果保护路由计算失败
		                		for(int n=0;n!=tnt-1;n++){
		                			trfList.get(n).releaseTraffic(trfList.get(n),0);//释放工作路由、保护路由、并清空链路
									trfList.get(n).releaseTraffic(trfList.get(n),1);
		                			trfList.get(n).getM_cWorkRoute().clearRoute();
									trfList.get(n).getM_cProtectRoute().clearRoute();
									
								}
		                		temp.releaseTraffic(temp,0);
		                		temp.getM_cWorkRoute().clearRoute();
								temp.getM_cProtectRoute().clearRoute();
								System.out.println("工作链路成功，保护链路失败/n");
								System.out.println("===========");
								success=false;
								return;
		                	}
		                }
		                
		                System.out.println(" 业务-"+tnt+"   完成 ");
					}
				}
				ResourceLogTxt.ResourceLogTxt();//将资源分配日志写入txt中 wx 3.17
			
	}
	public static boolean D(Network net,CommonNode from,CommonNode to,LinkedList returnlinklist){
		//初始化
		List<CommonNode> NodeList=new LinkedList<CommonNode>();
		List LinkList=new LinkedList();
		LinkedList<CommonNode> routeNodeList=new LinkedList<CommonNode>();//计算路由返回结果
		double[] NodeWeight=null;
		NodeList.clear();
		LinkList.clear();
		NodeList.addAll(net.getM_lInNetNodeList());
		if (trfLayer==Layer.WDM){
			LinkList.addAll(net.getM_lInNetWDMLinkList());
		}
//		if (trfLayer==Layer.ASON){
//			LinkList.addAll(net.getM_lInNetSDHLinkList());
//		}
		//去除未激活的节点
		Iterator<CommonNode> itNode=net.getM_lInNetNodeList().iterator();
		while(itNode.hasNext()){
			CommonNode theNode=itNode.next();
			if(!theNode.isM_bStatus())
				NodeList.remove(theNode);
		}

		//去除未激活的链路
		Iterator<BasicLink>  itLink=LinkList.iterator();
		while(itLink.hasNext()){
			BasicLink theLink=itLink.next();
			double weight=theLink.getM_dLength();
			theLink.setM_fWeight((float)weight);
		}
		
		if(LinkList.isEmpty())
			return false;
		int nNodeSum = 0;
		int nNodeRankID=0;
		Iterator<CommonNode> it=NodeList.listIterator();
		 while(it.hasNext())
			{   
			 CommonNode theNode =it.next();
             theNode.setM_nRankID(nNodeRankID);//ID号从0开始按顺序排列
             nNodeRankID++;
             nNodeSum++;
			}
		
		double[][] Adjacency = new double[nNodeSum][nNodeSum];
		for(int i=0; i<nNodeSum; i++)
    	{
    		for(int j=0; j<nNodeSum; j++)
    		{
    			if (i != j)
    			{
    				Adjacency[i][j] = algorithm.Infinity;
    			}
    			else
    			{
    				Adjacency[i][j] = 0;
    			}
    		}
    	}
		Iterator<BasicLink> that=LinkList.iterator();
		while(that.hasNext())
    	{
    		BasicLink thelink =that.next();
    		if (thelink.isM_bStatus() == true)
    		{
    			int fromID=thelink.getM_cFromNode().getM_nRankID();
    			int toID=thelink.getM_cToNode().getM_nRankID();
    			if (thelink.getM_fWeight()<Adjacency[fromID][toID]) 
    			{
    				Adjacency[fromID][toID]= thelink.getM_fWeight();
    			}
    			if (thelink.getM_fWeight()<Adjacency[toID][fromID]) 
    			{
    				Adjacency[toID][fromID]= thelink.getM_fWeight();
    			}
    		}
    	}
		//D算法，得到一个Prev[]数组表示一条路由
    	double Weight_min = algorithm.Infinity;
    	int source = from.getM_nRankID();
    	int v=source;
    	boolean Found[] = new boolean[nNodeSum];//记录节点是否被标记
    	double Distance[]=new double[nNodeSum];//记录节点权重值，即各节点到达源节点的可见距离
    	int Prev[] = new int[nNodeSum];	//Prev[i]:i的前驱节点
    	for(int i=0; i<nNodeSum; i++)
    	{
    		Found[i] = false;
    		Distance[i] = Adjacency[source][i];//直达
    		Prev[i] = (Distance[i]<Weight_min) ? source : -1;	
    	}
    	Found[source] = true;	
    	Distance[source] = 0;
    	Prev[source] = -1;
    	
    	for(int i=0; i<nNodeSum; i++)
    	{
    		Weight_min = algorithm.Infinity;
    		for(int w=0; w<nNodeSum; w++)
    		{
    			if ((!Found[w])&&Distance[w]<Weight_min)   //遍历全网未标记的点（Found[w]==false）
    			{
    				v = w;
    				Weight_min = Distance[w];
    			}
    		}
    		Found[v]=true;
    		for(int w=0; w<nNodeSum; w++)                        //更新节点权重值
    		{
    			if ((!Found[w])&&(Weight_min+Adjacency[v][w]<Distance[w]))
    			{
    				Distance[w] = Weight_min+Adjacency[v][w];
    				Prev[w] = v;
    			}
    		}
    	}
    	//D算法，得到一个Prev[]数组表示一条路由
    	int dest = to.getM_nRankID();
    	routeNodeList.addFirst(to);
    	while (dest!=-1) 
    	{
    		int u = dest;
    		dest = Prev[dest];
    		if (dest!=-1)
    		{
    			//遍历链路，根据Prev[]前驱节点数组找到符合的链路，生成Path路由list
    			Iterator<BasicLink> itlink=LinkList.listIterator();
    			BasicLink pLink = new  BasicLink();
    			while(itlink.hasNext())
    	    	{    
    				BasicLink thelink =itlink.next();
    				if(thelink.isM_bStatus() == true) 
    				{
    					if(((thelink.getM_cFromNode().getM_nRankID() == u) && (thelink.getM_cToNode().getM_nRankID() == dest))
    						||((thelink.getM_cToNode().getM_nRankID() == u) && (thelink.getM_cFromNode().getM_nRankID() == dest)))
    					{
    						if(pLink.getM_cFromNode() == null)
    						{
    							pLink = thelink;
    						}
    						else 
    						{
    							if (thelink.getM_fWeight() < pLink.getM_fWeight())
    							{
    								pLink = thelink;
    							}
    						}
    					}
    				}
    			}
    			returnlinklist.addFirst(pLink);
    			if(routeNodeList.getFirst().equals(pLink.getM_cFromNode()))
    			{
    				routeNodeList.addFirst(pLink.getM_cToNode());
    			}
    			else if(routeNodeList.getFirst().equals(pLink.getM_cToNode()))
    			{
    				routeNodeList.addFirst(pLink.getM_cFromNode());
    			}
            }
    	}
    	if(!routeNodeList.getFirst().equals(from))
			return false;
		return true;
	}
	public static boolean DeleteSRLGList(List linkList,boolean ifSRLG)//????
	{
		if(linkList.isEmpty()){
			return false;
		}  
			
	    if(linkList.get(0).getClass().equals(WDMLink.class)){               //将与工作路由同组的链路权重设置为infinity+1
				ListIterator<WDMLink> it=linkList.listIterator();
				while(it.hasNext())
				{   
					WDMLink theLink =it.next();		
					
					if(theLink.getId()==15){
//						调试
						int a= 0;
					}
					if(theLink.getM_fWeight()!=Define.infinity){
						theLink.setM_fWeight(Define.infinity+1);//
					}
						if(ifSRLG){                                                   
							List<LinkRGroup> WDMLinkGroup = theLink.getM_lRelate();
							ListIterator<LinkRGroup> itGroup=WDMLinkGroup.listIterator();
							while(itGroup.hasNext()){
								LinkRGroup theGroup = itGroup.next();
								List<WDMLink> relateWDMlink =theGroup.getM_lWDMLinkList();
								ListIterator<WDMLink> itrelateWDMlink=relateWDMlink.listIterator();
								while(itrelateWDMlink.hasNext()){					
									WDMLink therelateWDMlink =itrelateWDMlink.next();
									if(therelateWDMlink.getM_fWeight()!=Define.infinity){
										therelateWDMlink.setM_fWeight(Define.infinity+1);
									}
								}
							}		
							}	
					
					else{}
				}
				return true;
			}  
			
			else if(linkList.get(0).getClass().equals(SDHLink.class)){               //将与工作路由同组的链路权重设置为infinity+1
				ListIterator<SDHLink> it=linkList.listIterator();
				while(it.hasNext())
				{   
					SDHLink theLink =it.next();		
					if(theLink.getM_fWeight()!=Define.infinity){
						theLink.setM_fWeight(Define.infinity+1);
						if(ifSRLG){  //将与工作路由同组的链路权重设置为infinity+1
							if(SDHLink.s_lSDHLinkList.contains(theLink)){
							List<LinkRGroup> SDHLinkGroup = theLink.getM_lRelate();
							ListIterator<LinkRGroup> itGroup=SDHLinkGroup.listIterator();
							while(itGroup.hasNext()){
								LinkRGroup theGroup = itGroup.next();
								List<SDHLink> relateSDHlink =theGroup.getM_lSDHLinkList();
								ListIterator<SDHLink> itrelateSDHlink=relateSDHlink.listIterator();
								while(itrelateSDHlink.hasNext()){					
									SDHLink therelateSDHlink =itrelateSDHlink.next();
									if(therelateSDHlink.getM_fWeight()!=Define.infinity){
										therelateSDHlink.setM_fWeight(Define.infinity+1);
									}
									}
								}
							}	
							else if(SDHLink.s_lASONLinkList.contains(theLink)){
								List<LinkRGroup> ASONLinkGroup = theLink.getM_lRelate();
								ListIterator<LinkRGroup> itGroup=ASONLinkGroup.listIterator();
								while(itGroup.hasNext()){
									LinkRGroup theGroup = itGroup.next();
									List<SDHLink> relateASONlink =theGroup.getM_lASONLinkList();
									ListIterator<SDHLink> itrelateASONlink=relateASONlink.listIterator();
									while(itrelateASONlink.hasNext()){					
										SDHLink therelateASONlink =itrelateASONlink.next();
										if(therelateASONlink.getM_fWeight()!=Define.infinity){
											therelateASONlink.setM_fWeight(Define.infinity+1);
										}
										}
									}
								}	
							
							}	
				}
					else{}
				}
				return true;
			}  
			else{
			return false;}
		}
	static void findLinkNumber(int setlimit,int uselimit){//初始化计算链路数目，每72条占用的波道分成一条链路！！刚开始默认链路波道数目为500意为很大
//          for (int i1 = 0; i1 < linkList.size(); i1++) {
//        	      if (linkList.get(i1).getClass().equals(WDMLink.class)) {
//        	    	  WDMLink temp=(WDMLink) linkList.get(i1);
//      				int linkSum=0;
//      				int nUsed=0;
//      				for (int j = 0; j != temp.getM_nSize(); j++) {//遍历链路底层的波道 temp.getM_nsize=500				
//      					if(temp.getM_lWavelength(j).getM_eStatus() == Status.工作)
//      					    nUsed++;	//计算每条链路的波道使用数目				
//      				}
//      				linkUsed.add(nUsed);//每条链路使用的波道数存入linkused链路中
//      				//wx 待修改 单线配置上限和使用上限.用户设置单纤使用上限。useLimit ！！
//      				linkSum = (int) Math.ceil((double)nUsed / (double)uselimit);//向上取整。为什么除以72？？？？如果一条链路占用波道数超过72，则每72条波道分一条链路！！计算链路数目！
//      				if(nUsed==0){//这里linkNumlist中存的数是链路数目，1表示1条链路，2表示2条链路，根据占用波道数目来分
//      					linkNumList.add(1);//如果占用波道数目为0，则链路数目为1
//      				}
//      				else linkNumList.add(linkSum);//存链路数目
//      			
//      			int linkSizeInitial = linkList.size();//28条链路
//      			      			
//      			for (int i=0; i!=linkSizeInitial; i++){//开始默认为全连接网络，根据占用波道是否占满，计算是否需要新加链路
//      				WDMLink tempLink = ((WDMLink)(linkList.get(i)));
//      				//int wlSize = 72;//每条链路72个波道
//      				int wlSize = uselimit;//每条链路波道数目为单纤使用上限数目				
//      				tempLink.resizeWaveLengthList(setlimit);//清空剩余波道
//      				
//                      tempLink.setM_nSize(setlimit);//设置每条链路底层低粒度为72  设置配置上限 80
//                      if (linkUsed.get(i)<wlSize)//如果链路占用波道数小于72，表示未占满，分一条链路。设置链路剩余资源为72-所用波道数
//                          tempLink.setM_nFree(setlimit-linkUsed.get(i));//空闲波道数目为80-used
//                      else//如果链路占用波道数目大于72，则第一条链路占满，分剩余的链路
//                      	tempLink.setM_nFree(0);
//                      for(int j=0;j<linkNumList.get(i)-1;j++)//如果占用波道数目大于72，则第一条链路占满，接着分第二条、第三条链路
//                      {
//                      	WDMLink insertNewLink = getNewWDMLink(tempLink.getM_cFromNode(), tempLink.getM_cToNode(),j+1);
//                      	insertNewLink.resizeWaveLengthList(setlimit);
//                      	insertNewLink.setM_nSize(setlimit);//设置波道为80个 配置上限
//                      	if (j==linkNumList.get(i)-2)//如果是该业务占用的最后一个波道，则设置剩余资源
//                      	    insertNewLink.setM_nFree(linkNumList.get(i)*setlimit-linkUsed.get(i)-(setlimit-uselimit)*(linkNumList.get(i)-1));
//                      	else
//                      		insertNewLink.setM_nFree(setlimit-uselimit);
//                      	linkList.add(insertNewLink);
//                      }
//      			}
//      			//net.setM_lInNetWDMLinkList(linkList);
//      			WDMLink.setS_lWDMLinkList(linkList);
//      			break;
//				}
//        	  else if ((linkList.get(i1).getClass().equals(SDHLink.class))) {
//        		  SDHLink temp = (SDHLink) linkList.get(i1);
//  				int nUsed = 0;
//  				for (int j = 0; j != temp.getM_nSize(); j++) {				
//  					if(temp.getM_lTimeslotList().get(j).getM_eStatus() == Status.工作)
//  					    nUsed++;					
//  				}
//  				linkUsed.add(nUsed);
//  				int nLinkNum = (int) Math.ceil((double)nUsed / (double)uselimit);//wx 64改为uselimit
//  				
//  				if (nUsed==0){
//  					linkNumList.add(1);
//  				}
//  				else linkNumList.add(nLinkNum);
//  			
//  			int k = linkList.size();
//  			for(int i=0;i!=k;i++)
//  			{
//  				SDHLink tempLink = ((SDHLink)(linkList.get(i)));
//  				tempLink.setM_nRate(10);
//                  tempLink.setM_nSize(setlimit);//wx
//                  tempLink.getM_lTimeslotList().clear();
//                  int h=0;
//          		while(h<tempLink.getM_nSize()){
//          			Timeslot tl=new Timeslot(h,tempLink);
//          			tempLink.getM_lTimeslotList().add(tl);
//          			++h;
//          		}
//                  if(linkUsed.get(i)<uselimit)
//                      tempLink.setM_nFree(setlimit-linkUsed.get(i)); 
//                  else
//                  	tempLink.setM_nFree(0);
//          		
//                  for(int j=0;j<linkNumList.get(i)-1;j++)
//                  {
//                  	SDHLink insertNewLink = getNewSDHLink(tempLink.getM_cFromNode(), tempLink.getM_cToNode(),j+1);
//                  	insertNewLink.setM_nSize(setlimit);
//                  	insertNewLink.getM_lTimeslotList().clear();
//                  	int e=0;
//              		while(e<insertNewLink.getM_nSize()){
//              			Timeslot tl=new Timeslot(e,insertNewLink);
//              			insertNewLink.getM_lTimeslotList().add(tl);
//              			++e;
//              		}
//                  	insertNewLink.setM_nRate(10);
//                  	
//                  	if (j==linkNumList.get(i)-2)
//                  	    insertNewLink.setM_nFree(linkNumList.get(i)*setlimit-linkUsed.get(i)-(setlimit-uselimit)*(linkNumList.get(i)-1));
//                  	else
//                  		insertNewLink.setM_nFree(setlimit-uselimit);
//                      linkList.add(insertNewLink);
//                  }
//  			}
//  			//net.setM_lInNetSDHLinkList(linkList); wx 2014.1.1
//  			SDHLink.setS_lSDHLinkList(linkList);
//  			break;
//  			
//				}	
//            }
	
		switch(trfLayer){
		case WDM:
			for (int i=0;i!=linkList.size();i++){
				WDMLink temp=(WDMLink) linkList.get(i);
				int linkSum=0;
				int nUsed=0;
				for (int j = 0; j != temp.getSize(); j++) {//遍历链路底层的波道 temp.getM_nsize=500				
					if(temp.getM_lWavelength(j).getM_eStatus() == Status.工作)
					    nUsed++;	//计算每条链路的波道使用数目				
				}
				linkUsed.add(nUsed);//每条链路使用的波道数存入linkused链路中
				//wx 待修改 单线配置上限和使用上限.用户设置单纤使用上限。useLimit ！！
				linkSum = (int) Math.ceil((double)nUsed / (double)uselimit);//向上取整。为什么除以72？？？？如果一条链路占用波道数超过72，则每72条波道分一条链路！！计算链路数目！
				if(nUsed==0){//这里linkNumlist中存的数是链路数目，1表示1条链路，2表示2条链路，根据占用波道数目来分
					linkNumList.add(1);//如果占用波道数目为0，则链路数目为1
				}
				else linkNumList.add(linkSum);//存链路数目
			}
			int linkSizeInitial = linkList.size();//28条链路
			
			
			for (int i=0; i!=linkSizeInitial; i++){//开始默认为全连接网络，根据占用波道是否占满，计算是否需要新加链路
				WDMLink tempLink = ((WDMLink)(linkList.get(i)));
				//int wlSize = 72;//每条链路72个波道
				int wlSize = uselimit;//每条链路波道数目为单纤使用上限数目				
				tempLink.resizeWaveLengthList(setlimit);//清空剩余波道
				
                tempLink.setM_nSize(setlimit);//设置每条链路底层低粒度为72  设置配置上限 80
                if (linkUsed.get(i)<wlSize)//如果链路占用波道数小于72，表示未占满，分一条链路。设置链路剩余资源为72-所用波道数
                    tempLink.setM_nFree(setlimit-linkUsed.get(i));//空闲波道数目为80-used
                else//如果链路占用波道数目大于72，则第一条链路占满，分剩余的链路
                	tempLink.setM_nFree(0);
                for(int j=0;j<linkNumList.get(i)-1;j++)//如果占用波道数目大于72，则第一条链路占满，接着分第二条、第三条链路
                {
                	WDMLink insertNewLink = getNewWDMLink(tempLink.getM_cFromNode(), tempLink.getM_cToNode(),j+1);
                	insertNewLink.resizeWaveLengthList(setlimit);
                	insertNewLink.setM_nSize(setlimit);//设置波道为80个 配置上限
                	if (j==linkNumList.get(i)-2)//如果是该业务占用的最后一个波道，则设置剩余资源
                	    insertNewLink.setM_nFree(linkNumList.get(i)*setlimit-linkUsed.get(i)-(setlimit-uselimit)*(linkNumList.get(i)-1));
                	else
                		insertNewLink.setM_nFree(setlimit-uselimit);
                	linkList.add(insertNewLink);
                }
			}
			//net.setM_lInNetWDMLinkList(linkList);
			WDMLink.setS_lWDMLinkList(linkList);
			break;
		case ASON:
			for (int i=0;i!=linkList.size();i++){
				SDHLink temp = (SDHLink) linkList.get(i);
				int nUsed = 0;
				for (int j = 0; j != temp.getM_nSize(); j++) {				
					if(temp.getM_lTimeslotList().get(j).getM_eStatus() == Status.工作)
					    nUsed++;					
				}
				linkUsed.add(nUsed);
				int nLinkNum = (int) Math.ceil((double)nUsed / (double)uselimit);//wx 64改为uselimit
				
				if (nUsed==0){
					linkNumList.add(1);
				}
				else linkNumList.add(nLinkNum);
			}
			int k = linkList.size();
			for(int i=0;i!=k;i++)
			{
				SDHLink tempLink = ((SDHLink)(linkList.get(i)));
				tempLink.setM_nRate(10);
                tempLink.setM_nSize(setlimit);//wx
                tempLink.getM_lTimeslotList().clear();
                int h=0;
        		while(h<tempLink.getM_nSize()){
        			Timeslot tl=new Timeslot(h,tempLink);
        			tempLink.getM_lTimeslotList().add(tl);
        			++h;
        		}
                if(linkUsed.get(i)<uselimit)
                    tempLink.setM_nFree(setlimit-linkUsed.get(i)); 
                else
                	tempLink.setM_nFree(0);
        		
                for(int j=0;j<linkNumList.get(i)-1;j++)
                {
                	SDHLink insertNewLink = getNewASONLink(tempLink.getM_cFromNode(), tempLink.getM_cToNode(),j+1);
                	insertNewLink.setM_nSize(setlimit);
                	insertNewLink.getM_lTimeslotList().clear();
                	int e=0;
            		while(e<insertNewLink.getM_nSize()){
            			Timeslot tl=new Timeslot(e,insertNewLink);
            			insertNewLink.getM_lTimeslotList().add(tl);
            			++e;
            		}
                	insertNewLink.setM_nRate(10);
                	
                	if (j==linkNumList.get(i)-2)
                	    insertNewLink.setM_nFree(linkNumList.get(i)*setlimit-linkUsed.get(i)-(setlimit-uselimit)*(linkNumList.get(i)-1));
                	else
                		insertNewLink.setM_nFree(setlimit-uselimit);
                    linkList.add(insertNewLink);
                }
			}
			//net.setM_lInNetSDHLinkList(linkList); wx 2014.1.1
			//SDHLink.setS_lSDHLinkList(linkList); wx 3.4
			SDHLink.setS_lASONLinkList(linkList);
			break;
		default:
			return;
		}
	}
	private static int getNodeDegree(CommonNode NodeIn)
	{ 
		int degree = 0;
		for (int j = 0; j != linkList.size(); ++j) {
			BasicLink aLink = (BasicLink) linkList.get(j);
			if ((aLink.getM_cFromNode().getId() == NodeIn.getId())
					|| aLink.getM_cToNode().getId() == NodeIn.getId()) {
				degree++;
			}
		}
		return degree;
	}
	private static double findNetUtil(){//计算链路利用率
		double avgNet = 0.0f;
		double dUtil = 0.0f;
		SDHLink.reflashUtilization();
		WDMLink.reflashUtilization();
		if (trfLayer==Layer.WDM){
			int dLamdaSum = 0;
			for (int i = 0; i != linkList.size(); i++){
				WDMLink aLink = (WDMLink) linkList.get(i);
				double temp=0;
				for (int j = 0; j != aLink.getM_nSize(); j++){//遍历每条链路的所有波道，计算每条链路的波道占用情况
					temp = (double)(aLink.getM_nRate() - aLink.getM_lWavelength(j).getM_nFree());
					dUtil += temp;
				}
				dLamdaSum+=aLink.getM_nSize();//所有链路的波道之和
				utilList.add((double)aLink.getM_dUtilization());
			}
			avgNet = dUtil/(100*dLamdaSum);//全网平均利用率		
		}
		if (trfLayer==Layer.ASON){
			for (int i = 0; i != linkList.size(); i++) {
				SDHLink link = (SDHLink) linkList.get(i);
				utilList.add((double)link.getM_dUtilization());			
				avgNet += (double)link.getM_dUtilization();
			}
			avgNet = avgNet/(double)linkList.size();
		}
		System.out.println("====================");
		System.out.println("====================");
		System.out.println("全网平均利用率："+avgNet);
		System.out.println("====================");
		System.out.println("====================");
		return avgNet;
	}
	private static void initialize(){
	    utilList.clear();//链路利用率清空
	    for(int i=0;i<Traffic.s_lWDMTrafficList.size();i++){//释放工作路由和保护路由
			Traffic.s_lWDMTrafficList.get(i).releaseTraffic(Traffic.s_lWDMTrafficList.get(i), 0);
			Traffic.s_lWDMTrafficList.get(i).releaseTraffic(Traffic.s_lWDMTrafficList.get(i), 1);
		}	
	    for(int i=0;i<Traffic.s_lASONTrafficList.size();i++){//wx 3.4
			Traffic.s_lASONTrafficList.get(i).releaseTraffic(Traffic.s_lASONTrafficList.get(i), 0);
			Traffic.s_lASONTrafficList.get(i).releaseTraffic(Traffic.s_lASONTrafficList.get(i), 1);
		}
	    for(int i=0; i!=trfList.size(); i++){
	    	Traffic temp= trfList.get(i);
	    	temp.getM_cWorkRoute().clearRoute();
			temp.getM_cProtectRoute().clearRoute();
	    }
    }
	private static double getMinUtil(){//寻找全网利用率最低的链路
		double minRate = 1.0;
		for (int i = 0; i != utilList.size(); i++) {
		if(minRate>(Double) utilList.get(i)){
			minRate = (Double) utilList.get(i);
		}
		}
		return minRate;
	}
	static void findLink(){//寻找待删除的链路，链路利用率最低中最长的那条
		utilList.clear();//清空利用率链路
		findNetUtil();//计算全网平均利用率 和每条链路的利用率 utilList链路里面重新存入各条链路的利用率
		double min=1.0;
		int longest=0;
		double length=1;
		boolean there=false;
		List temp = new LinkedList();
		for (int i=0;i!=utilList.size();i++){//寻找最低利用率，放入min中
			if (utilList.get(i)<min){
				for (int j=0;j!=linkcantdel.size();j++){
					if (linkList.get(i)==linkcantdel.get(j)){
						there=true;
						break;
					}
					else
						continue;
				}
				if (!there)
					min=utilList.get(i);
				there=false;
			}
		}
		for (int i=0;i!=utilList.size();i++){
			if (utilList.get(i)==min){//找到利用率最小的链路，放入temp链路中
				for (int j=0;j!=linkcantdel.size();j++){
					if (linkList.get(i)==linkcantdel.get(j)){
						there=true;
						break;
					}
					else
						continue;
				}
				if (!there)
					temp.add( linkList.get(i));
				there=false;
			}
		}
		for (int i=0;i!=temp.size();i++){//寻找利用率最低的链路中的最长链路
			if (( (BasicLink) temp.get(i)).getM_dLength()>length){
				length=((BasicLink) temp.get(i)).getM_dLength();
				longest=i;//记录序号
			}
		}
		linktodel= temp.get(longest);
		linktodel_utl=min;//wx记录待删除链路利用率
	}		
	
	static void changeNet(){
		switch(trfLayer){
		case WDM:
			//net.setM_lInNetWDMLinkList(linkList);
			WDMLink.setS_lWDMLinkList(linkList);
			break;
			
//		case ASON:
//			//net.setM_lInNetSDHLinkList(linkList);
//			//SDHLink.setS_lSDHLinkList(linkList);wx 3.4
//			SDHLink.setS_lASONLinkList(linkList);
//			break;
			default:
				return;
		}
	}
	
	
}
