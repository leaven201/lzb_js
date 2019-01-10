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
 * 测试业务
 */
public class test_1 {
	public static void main(String[] args) {

		DataSave test1 = new DataSave();
		test1.default1(80, PortRate.G100, false);
		NodeData dbs = new NodeData();
		dbs.inputNode("附表1：拓扑导入模板 - 站点实名 - 含OA及衰耗NEW.xls");
		System.out.println(CommonNode.allNodeList);

		LinkData dbsl = new LinkData();
		dbsl.inputLink("附表1：拓扑导入模板 - 站点实名 - 含OA及衰耗NEW.xls");

		TrafficData dbst = new TrafficData();
		dbst.inputTraffic("附表2：业务导入模板 - 实际new.xls");

		HashSet<CommonNode> dict = ResourceAlloc.setdict();
		CommonNode node1 = CommonNode.getNode("九江浔阳");
		CommonNode node2 = CommonNode.getNode("南京大行宫");
		CommonNode node3 = CommonNode.getNode("芜湖微波楼");

		System.out.println(node1 + " " + node2 + " " + node3);
		List<List<CommonNode>> nodeRoutes = ResourceAlloc.findAllRoute(CommonNode.getNode("九江浔阳"),
				CommonNode.getNode("南京大行宫"), dict);

		for (List<CommonNode> list : nodeRoutes) {
			System.out.println(list.size());
		}
	}
}
