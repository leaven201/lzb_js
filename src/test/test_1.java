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
 * ����ҵ��
 */
public class test_1 {
    public static void main(String[] args){
	
    
    DataSave test1=new DataSave();
    test1.default1(80,PortRate.G100,false);
	NodeData dbs = new NodeData();
	dbs.inputNode("����1�����˵���ģ��.xls");
	System.out.println(CommonNode.allNodeList);
	
	LinkData dbsl = new LinkData();
	dbsl.inputLink("����1�����˵���ģ��.xls");
//	System.out.println("wdm:"+WDMLink.WDMLinkList);
//	System.out.println(WDMLink.WDMLinkList.get(5).isActive());
//	System.out.println(WDMLink.WDMLinkList.get(1).getWaveNum());
//	System.out.println();
//	
//    System.out.println("����·������:"+CommonNode.getNode(26).getUpDown()+"     WSS"+CommonNode.getNode(26).getWSS());
//    
//    
//	//����OSNR-Fiber����ֵ
//	Iterator<FiberLink> it1 = FiberLink.fiberLinkList.iterator();
//	while(it1.hasNext()) {
//		FiberLink f1=it1.next();
//		System.out.println("OSNR����ֵ��"+f1.getOSNRCount()+"   ��Σ�"+f1.getDissipation());
//	}
//	//����ÿ��WDM��·��������Fiber��·
//	Iterator<WDMLink> it2 = WDMLink.WDMLinkList.iterator();
//	while(it2.hasNext()) {
//		WDMLink f1=it2.next();
//		System.out.println("fiberLinkList��"+f1.getFiberLinkList());
////		System.out.println("��WDMlink��OSNR������ֵ��"+f1.getSumOfOSNR());
//	}	
	TrafficData dbst = new TrafficData();
	dbst.inputTraffic("����2��ҵ����ģ��.xls");
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
//		System.out.println("·��:"+route);
//	}
	for(int i=0;i<100;i++) {System.out.println("56666666666666"+Traffic.trafficList.get(i).getMustAvoidLink());}
	for(int i=0;i<100;i++) {
	System.out.println("����·�ɣ�"+Traffic.trafficList.get(i).getWorkRoute()
			+"����·�ɣ�"+Traffic.trafficList.get(i).getProtectRoute()
			+"Ԥ����·�ɣ�"+Traffic.trafficList.get(i).getPreRoute()
			+"·�ɲ�����"+Traffic.trafficList.get(i).getWorkRoute().getWaveLengthIdList()
			+"����ת������"+(Traffic.trafficList.get(i).getWorkRoute().getUsedWaveList().size()-1)
			+"�����任�ڵ�"+(Traffic.trafficList.get(i).getWorkRoute().getWaveChangedNode())
			+"ҵ���������"+Traffic.trafficList.get(i).getTrafficNum()
			);
//	System.out.println("����·�ɣ�"+Traffic.trafficList.get(i).getProtectRoute()+"·�ɲ�����"+Traffic.trafficList.get(i).getWorkRoute().getWaveLengthIdList()
//			+"����ת������"+(Traffic.trafficList.get(i).getProtectRoute().getUsedWaveList().size()-1)
//			+"�����任�ڵ�"+(Traffic.trafficList.get(i).getProtectRoute().getWaveChangedNode()));
	}
//	+"����ת������"+(Traffic.trafficList.get(i).getWorkRoute().getUsedWaveList().size()-1)
	//
	System.out.println("12312312312312312312321"+WDMLink.WDMLinkList.get(1).getWaveLengthList().toString());
	//������·�ϲ���ʹ�����
	for(int i=0;i<WDMLink.WDMLinkList.size();i++) {
	System.out.println("��·����ʣ����Դ��"+WDMLink.WDMLinkList.get(i).getRemainResource());
	System.out.print(WDMLink.WDMLinkList.get(i).getWorkUsedWaveNum()+"       ");
	WDMLink.printWorkUsedWave(WDMLink.WDMLinkList.get(i));
	System.out.print(WDMLink.WDMLinkList.get(i).getOtherUsedWaveNum()+"       ");
	WDMLink.printOtherUsedWave(WDMLink.WDMLinkList.get(i));}	
	//
    System.out.println("����ʧ�ܵ�ҵ�񣡣�������������������������"+ResourceAlloc.failedTraffic);
    

    System.out.println(TrafficGroup.gettrafficGroupById(1));
    
    
    for(int i=0;i<CommonNode.ROADM_NodeList.size();i++) {
    	System.out.println(CommonNode.ROADM_NodeList.get(i).getName()+"���ڹ����ĵ��м�����"+CommonNode.ROADM_NodeList.get(i).getWorkOTUNum());
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
//	dbsl.inputLink("���ű��3.0.xls");
//	System.out.println("wdm"+WDMLink.allWDMLinkList);
//	System.out.println("fiber"+FiberLink.allFiberLinkList);
//
//	TrafficData dbst = new TrafficData();
//	dbst.inputTraffic("���ű��3.0.xls");
//	System.out.println(Traffic.trafficList);
//	ResourceAlloc.allocateResource(Traffic.trafficList, 0);
//	
//	OSNR osnr=new OSNR();
//	StringBuffer s=new StringBuffer();
//	System.out.println(s);
//	System.out.println(CommonAlloc.fallBuffer);//û���壬ÿ��һ��·�ɶ��ᱻ���

//	for (Traffic tra : Traffic.trafficList) {
//		System.out.println("����·�ɣ�"+tra.getWorkRoute()+":"+osnr.calculateOSNR(tra.getWorkRoute()));
//		System.out.println("����·�ɣ�"+tra.getProtectRoute()+":"+osnr.calculateOSNR(tra.getProtectRoute()));
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

