package test;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import algorithm.CommonAlloc;
import algorithm.OSNR;
import algorithm.ResourceAlloc;
import algorithm.RouteAlloc;
import algorithm.Suggest;
import algorithm.algorithm;
import data.BasicLink;
import data.CommonNode;
import data.DataSave;
import data.FiberLink;
import data.LinkRGroup;
import data.Route;
//import data.SpanLink;
import data.Traffic;
import data.TrafficGroup;
import data.WDMLink;
import data.WaveLength;
import dataControl.*;
import enums.NodeType;
import enums.PortRate;
import enums.Status;
import survivance.Evaluation;

/*
 * ����ҵ��
 */
public class test_1 {
	public static void main(String[] args) {

		DataSave test1 = new DataSave();
		test1.default1(80, PortRate.G100, false);
		NodeData dbs = new NodeData();
		dbs.inputNode("����1�����˵���ģ�� - վ��ʵ�� - ��OA��˥��NEW.xls");
		System.out.println(CommonNode.allNodeList);

		LinkData dbsl = new LinkData();
		dbsl.inputLink("����1�����˵���ģ�� - վ��ʵ�� - ��OA��˥��NEW.xls");
		
		TrafficData dbst = new TrafficData();
		dbst.inputTraffic("����2��ҵ����ģ�� - ʵ��new.xls");
		
		

		
		data.DataSave.locknum = 10;
		DataSave.OSNR = true;
		ResourceAlloc.allocateResource1(Traffic.trafficList, 0);
		for(int i=0;i<20;i++) {
			System.out.println(Traffic.trafficList.get(i).getWorkRoute());
		}
		double osnr = OSNR.calculateOSNR(Traffic.trafficList.get(1).getWorkRoute());
		System.out.println(osnr);


	}
}
