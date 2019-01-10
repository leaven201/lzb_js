package test;

import java.util.HashSet;
import java.util.List;

import algorithm.OSNR;
import algorithm.ResourceAlloc;
import data.CommonNode;
import data.DataSave;
//import data.SpanLink;
import data.Traffic;
import data.WDMLink;
import dataControl.LinkData;
import dataControl.NodeData;
import dataControl.TrafficData;
import enums.PortRate;

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

		HashSet<CommonNode> dict = ResourceAlloc.setdict();
		CommonNode node1 = CommonNode.getNode("�Ž����");
		CommonNode node2 = CommonNode.getNode("�Ͼ����й�");
		CommonNode node3 = CommonNode.getNode("�ߺ�΢��¥");

		System.out.println(node1 + " " + node2 + " " + node3);
		List<List<CommonNode>> nodeRoutes = ResourceAlloc.findAllRoute(CommonNode.getNode("�Ž����"),
				CommonNode.getNode("�Ͼ����й�"), dict);

		for (List<CommonNode> list : nodeRoutes) {
			System.out.println(list.size());
		}
	}
}
