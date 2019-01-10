package algorithm;

import java.util.LinkedList;
import java.util.List;

import data.CommonNode;
import data.WaveLength;

public class Suggest {
	public static boolean isKanghui;// 标记是否为抗毁部分2017.10.25
	public static boolean isSuggested;// 标记是否为规划建议部分
	public static boolean isDouble;// ture 双断 false 单断
	public static boolean isSurvived;// 是否已经考虑过抗毁
	public static int flag;// =0波长转换分析，=1抗毁波长转换分析

	public static List<WaveLength> zuichuWaveLengthlist;// 只存放最初路由分配的波长list
	public static List<CommonNode> allosnrNodeList = new LinkedList<>();// 存放所有建议再生节点(考虑抗毁)
	public static List<CommonNode> allosnrNodeList2 = new LinkedList<>();// 存放所有建议再生节点(不考虑抗毁)
	public static List<CommonNode> conversionNodeList = new LinkedList<>();// 存储所有波长转换节点(考虑抗毁)
	public static List<CommonNode> conversionNodeList2 = new LinkedList<>();// 存储所有波长转换节点(不考虑抗毁)
	public static List<CommonNode> converNodeKanghuiList = new LinkedList<>();// 存储抗毁的波长转换节点--2017.10.26

	/*// 给list中的节点赋予再生功能
	public static void regenerate(List<CommonNode> list) {
		for (CommonNode node : list) {
			node.setRegenerated(true);
		}
	}

	// 取消再生功能
	public static void cancelRegenerate(List<CommonNode> list) {
		for (CommonNode node : list) {
			node.setRegenerated(false);
		}
	}

	// 中继分析验证总入口,flag=0中继分析，=1抗毁中继分析
	public static void suggest(int flag) {
		OSNR osnr = new OSNR();
		List<CommonNode> list = Suggest.suggestOSNR(OSNR.allOSNRList);
		if (list != null && list.size() != 0) {
			if (flag == 1) {// 抗毁
				for (CommonNode node : list) {
					if (!contan(Suggest.allosnrNodeList, node))
						Suggest.allosnrNodeList.add(node);
				}
			} else {
				for (CommonNode node : list) {// 正常规划时也要考虑对后续抗毁的影响
					if (!contan(Suggest.allosnrNodeList, node))
						Suggest.allosnrNodeList.add(node);
				}
				for (CommonNode node : list) {
					if (!contan(Suggest.allosnrNodeList2, node))
						Suggest.allosnrNodeList2.add(node);
				}
			}
			Suggest.regenerate(list);// 增加再生功能
//			if (flag == 1) {// 抗毁
//				if (Suggest.isDouble == true) {// 双断
//					Evaluation.dNodeListEvaluation(CommonNode.allNodeList, 1);
//					Evaluation.dLinkListEvaluation(SpanLink.allSpanLinkList, 1);
//				} else if (Suggest.isDouble == false) {// 单断
//					// System.out.println("danduan");
//					Evaluation.nodelistEvaluation(CommonNode.allNodeList, 1);
//					Evaluation.listEvaluation(SpanLink.allSpanLinkList, 1);
//				}
//			}
			for (Route route : OSNR.OSNRRouteList) {
				System.out.println(route);
				if (osnr.calculateOSNR(route) < Dlg_PolicySetting.osnRGate) {
					List<CommonNode> nl = OSNR.select(route);
					CommonNode n = nl.get(nl.size() - 1);
					if (flag == 1) {// 抗毁
						if (!contan(Suggest.allosnrNodeList, n))
							Suggest.allosnrNodeList.add(n);
					} else {
						if (!contan(Suggest.allosnrNodeList, n))
							Suggest.allosnrNodeList.add(n);
						if (!contan(Suggest.allosnrNodeList2, n))
							Suggest.allosnrNodeList2.add(n);
					}
				}
			}
			OSNR.OSNRRouteList.clear();
			Suggest.cancelRegenerate(CommonNode.allNodeList);// 清空再生功能
			// if (!RouteAlloc.isRouteEmpty(Traffic.trafficList)) {// 若路由不为空
			// RouteAlloc.clearAllTrafficRoute3(Traffic.trafficList);// 清空资源
			// ResourceAlloc.allocateResource(Traffic.trafficList,
			// Dlg_PolicySetting.mark);// 重计算
			// }

		}
	}

	// 有交集，先取交集节点，没有交集取最后一个节点
	public static List<CommonNode> suggestOSNR(List<List<CommonNode>> osnrlist) {
		if (osnrlist == null || osnrlist.size() == 0) {
			return null;
		}
		List<List<CommonNode>> olist = new LinkedList<List<CommonNode>>();
		olist.addAll(osnrlist);// 备份一份，防止影响原来数据
		System.out.println("osnrlistsize" + olist.size());
		List<CommonNode> allnode = new LinkedList<>();// 存放所有节点
		// List<List<CommonNode>> deletelist = new LinkedList<List<CommonNode>>();//
		// 存放要删除的nlist
		List<CommonNode> list = new LinkedList<>();// 存放返回结果

		for (List<CommonNode> nlist : olist) {// 遍历所有的节点
			for (CommonNode node : nlist) {
				node.setTimes(node.getTimes() + 1);// node的times属性加1
				if (!allnode.contains(node)) {// 去重
					allnode.add(node);
				}
			}
		}
		Collections.sort(allnode);// 将节点按照times属性升序排列
		// for (CommonNode node : allnode) {// 测试
		// System.out.println(node.getName() + ":" + node.getTimes());
		// }
		while (allnode.size() != 0 && allnode.get(allnode.size() - 1).getTimes() > 1) {
			// Collections.sort(allnode);// 将节点按照times属性升序排列
			// 取出次数最大的进行判断
			CommonNode node1 = allnode.get(allnode.size() - 1);
			list.add(node1);// 加入结果集
			for (int i = 0; i < olist.size(); i++) {// 遍历所有的nlist
				List<CommonNode> nlist1 = olist.get(i);
				if (nlist1.contains(node1)) {
					// System.out.println(node1.getName());
					for (CommonNode node2 : nlist1) {// 包含node1的nlist中所有节点times-1,即更新times属性
						node2.setTimes(node2.getTimes() - 1);
					} // 更新结束
					olist.remove(nlist1);// 移除包含node1的nlist
					i--;
				}
			}
			Collections.sort(allnode);// 将节点按照times属性升序排列
		}
		// System.out.println("osnr" + list);
		// 把剩余nlist中最后一个节点加入list
		for (List<CommonNode> nlist : olist) {
			list.add(nlist.get(nlist.size() - 1));
		}
		// 清空所有node的times,防止影响以后的计算
		for (CommonNode node : CommonNode.allNodeList) {
			node.setTimes(0);
		}
		return list;
	}*/

	public static boolean conversion(CommonNode n, List<CommonNode> l2) {
		if (l2.contains(n)) {
			return true;
		}
		return false;
	}

	public static boolean contan(List<CommonNode> list, CommonNode n) {
		for (CommonNode node : list) {
			if (node.getName().equals(n.getName())) {
				return true;
			}
		}
		return false;
	}

	public static void main(String args[]) {
		StringBuffer sb = new StringBuffer();
		System.out.println(sb.toString().equals(""));
	}
}
