package test;

import java.util.Iterator;

import algorithm.CommonAlloc;
import algorithm.OSNR;
import algorithm.ResourceAlloc;
import algorithm.RouteAlloc;
import data.BasicLink;
import data.CommonNode;
import data.DataSave;
import data.FiberLink;
import data.Route;
//import data.SpanLink;
import data.Traffic;
import data.TrafficGroup;
import data.WDMLink;
import dataControl.*;
import enums.NodeType;
import enums.PortRate;

/*
 * 测试业务
 */
public class test_1 {
    public static void main(String[] args){
	
    
    DataSave test1=new DataSave();
    test1.default1(80,PortRate.G100,false);
	NodeData dbs = new NodeData();
	dbs.inputNode("附表1：拓扑导入模板.xls");
	System.out.println(CommonNode.allNodeList);
	
	LinkData dbsl = new LinkData();
	dbsl.inputLink("附表1：拓扑导入模板.xls");
//	System.out.println("wdm:"+WDMLink.WDMLinkList);
//	System.out.println(WDMLink.WDMLinkList.get(5).isActive());
//	System.out.println(WDMLink.WDMLinkList.get(1).getWaveNum());
//	System.out.println();
//	
//    System.out.println("上下路分组数:"+CommonNode.getNode(26).getUpDown()+"     WSS"+CommonNode.getNode(26).getWSS());
//    
//    
//	//测试OSNR-Fiber计算值
//	Iterator<FiberLink> it1 = FiberLink.fiberLinkList.iterator();
//	while(it1.hasNext()) {
//		FiberLink f1=it1.next();
//		System.out.println("OSNR计算值："+f1.getOSNRCount()+"   跨段："+f1.getDissipation());
//	}
//	//测试每个WDM链路所包含的Fiber链路
//	Iterator<WDMLink> it2 = WDMLink.WDMLinkList.iterator();
//	while(it2.hasNext()) {
//		WDMLink f1=it2.next();
//		System.out.println("fiberLinkList："+f1.getFiberLinkList());
////		System.out.println("该WDMlink的OSNR计算总值："+f1.getSumOfOSNR());
//	}	
	TrafficData dbst = new TrafficData();
	dbst.inputTraffic("附表2：业务导入模板.xls");
//	
//	System.out.println("luyou123123123"+Traffic.trafficList.get(7).getWorkRoute());
//	System.out.println(Traffic.trafficList);
//	
//	System.out.println(LinkData.aveLinkLength);
//	ResourceAlloc.allocateResource(Traffic.trafficList, 0);
	
	for(int i =0;i<Traffic.trafficList.size();i++) {
		if(Traffic.trafficList.get(i).getMustPassLink()!=null)
			System.out.println(Traffic.trafficList.get(i).getMustPassLink());
	}
	
	ResourceAlloc.allocateResource(Traffic.trafficList, 1);
//	Iterator<Traffic> it = Traffic.trafficList.iterator();
//	while (it.hasNext()) {
//		Traffic traffic = it.next();
//		Route route = RouteAlloc.findWDMRoute(traffic, 2);
//		System.out.println("路由:"+route);
//	}
	for(int i=0;i<100;i++) {System.out.println("56666666666666"+Traffic.trafficList.get(i).getMustAvoidLink());}
	for(int i=0;i<100;i++) {
	System.out.println("工作路由："+Traffic.trafficList.get(i).getWorkRoute()
			+"保护路由："+Traffic.trafficList.get(i).getProtectRoute()
			+"预置重路由："+Traffic.trafficList.get(i).getPreRoute()
			+"路由波长："+Traffic.trafficList.get(i).getWorkRoute().getWaveLengthIdList()
			+"波长转换数："+(Traffic.trafficList.get(i).getWorkRoute().getUsedWaveList().size()-1)
			+"波长变换节点"+(Traffic.trafficList.get(i).getWorkRoute().getWaveChangedNode())
			+"业务的数量："+Traffic.trafficList.get(i).getTrafficNum()
			);
//	System.out.println("保护路由："+Traffic.trafficList.get(i).getProtectRoute()+"路由波长："+Traffic.trafficList.get(i).getWorkRoute().getWaveLengthIdList()
//			+"波长转换数："+(Traffic.trafficList.get(i).getProtectRoute().getUsedWaveList().size()-1)
//			+"波长变换节点"+(Traffic.trafficList.get(i).getProtectRoute().getWaveChangedNode()));
	}
//	+"波长转换数："+(Traffic.trafficList.get(i).getWorkRoute().getUsedWaveList().size()-1)
	//
	System.out.println("12312312312312312312321"+WDMLink.WDMLinkList.get(1).getWaveLengthList().toString());
	//测试链路上波道使用情况
	for(int i=0;i<WDMLink.WDMLinkList.size();i++) {
	System.out.println("链路波道剩余资源："+WDMLink.WDMLinkList.get(i).getRemainResource());
	System.out.print(WDMLink.WDMLinkList.get(i).getWorkUsedWaveNum()+"       ");
	WDMLink.printWorkUsedWave(WDMLink.WDMLinkList.get(i));
	System.out.print(WDMLink.WDMLinkList.get(i).getOtherUsedWaveNum()+"       ");
	WDMLink.printOtherUsedWave(WDMLink.WDMLinkList.get(i));}	
	//
    System.out.println("分配失败的业务！！！！！！！！！！！！！！"+ResourceAlloc.failedTraffic);
    

    System.out.println(TrafficGroup.gettrafficGroupById(1));
    
    
    for(int i=0;i<CommonNode.ROADM_NodeList.size();i++) {
    	System.out.println(CommonNode.ROADM_NodeList.get(i).getName()+"用于工作的电中继数："+CommonNode.ROADM_NodeList.get(i).getWorkOTUNum());
    }
    System.out.println(Traffic.trafficList.get(46).getWorkRoute().getWaveLengthIdList());
    
    
    for(int i=0;i<WDMLink.WDMLinkList.size();i++) {
    	WDMLink link=WDMLink.WDMLinkList.get(i);
//    	if((link.getFromNode().getName().equals("A")
//				&& link.getToNode().getName().equals("R"))
//				|| (link.getFromNode().getName().equals("R")
//						&&link.getToNode().getName().equals("A"))) {
//    		System.out.println(link.getName());   		
//    	}
//    	if((link.getFromNode().getName().equals("M")
//				&& link.getToNode().getName().equals("N"))
//				|| (link.getFromNode().getName().equals("N")
//						&&link.getToNode().getName().equals("M"))) {
//    		System.out.println(link.getName());   		
//    	}
//    	if((link.getFromNode().getName().equals("I")
//				&& link.getToNode().getName().equals("J"))
//				|| (link.getFromNode().getName().equals("J")
//						&&link.getToNode().getName().equals("I"))) {
//    		System.out.println(link.getName());   		
//    	}
    	
    }

//	LinkData dbsl = new LinkData();
//	dbsl.inputLink("电信表格3.0.xls");
//	System.out.println("wdm"+WDMLink.allWDMLinkList);
//	System.out.println("fiber"+FiberLink.allFiberLinkList);
//
//	TrafficData dbst = new TrafficData();
//	dbst.inputTraffic("电信表格3.0.xls");
//	System.out.println(Traffic.trafficList);
//	ResourceAlloc.allocateResource(Traffic.trafficList, 0);
//	
//	OSNR osnr=new OSNR();
//	StringBuffer s=new StringBuffer();
//	System.out.println(s);
//	System.out.println(CommonAlloc.fallBuffer);//没意义，每算一次路由都会被清空

//	for (Traffic tra : Traffic.trafficList) {
//		System.out.println("工作路由："+tra.getWorkRoute()+":"+osnr.calculateOSNR(tra.getWorkRoute()));
//		System.out.println("保护路由："+tra.getProtectRoute()+":"+osnr.calculateOSNR(tra.getProtectRoute()));
//	}

	
    for(int i=0;i<WDMLink.WDMLinkList.size();i++) {
    	WDMLink link=WDMLink.WDMLinkList.get(i);
    	System.out.println(link.getName()+":"+link.getRemainResource());
    }
    System.out.println();
    
   
	for(int i=0;i<WDMLink.WDMLinkList.size();i++) {
		WDMLink link=WDMLink.WDMLinkList.get(i);
		System.out.println(link.getParallelLinkList());
	}
	
	
	
    
    }
}

