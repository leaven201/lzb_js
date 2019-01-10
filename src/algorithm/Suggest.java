package algorithm;

import java.util.LinkedList;
import java.util.List;

import data.CommonNode;
import data.WaveLength;

public class Suggest {
	public static boolean isKanghui;// ����Ƿ�Ϊ���ٲ���2017.10.25
	public static boolean isSuggested;// ����Ƿ�Ϊ�滮���鲿��
	public static boolean isDouble;// ture ˫�� false ����
	public static boolean isSurvived;// �Ƿ��Ѿ����ǹ�����
	public static int flag;// =0����ת��������=1���ٲ���ת������

	public static List<WaveLength> zuichuWaveLengthlist;// ֻ������·�ɷ���Ĳ���list
	public static List<CommonNode> allosnrNodeList = new LinkedList<>();// ������н��������ڵ�(���ǿ���)
	public static List<CommonNode> allosnrNodeList2 = new LinkedList<>();// ������н��������ڵ�(�����ǿ���)
	public static List<CommonNode> conversionNodeList = new LinkedList<>();// �洢���в���ת���ڵ�(���ǿ���)
	public static List<CommonNode> conversionNodeList2 = new LinkedList<>();// �洢���в���ת���ڵ�(�����ǿ���)
	public static List<CommonNode> converNodeKanghuiList = new LinkedList<>();// �洢���ٵĲ���ת���ڵ�--2017.10.26

	/*// ��list�еĽڵ㸳����������
	public static void regenerate(List<CommonNode> list) {
		for (CommonNode node : list) {
			node.setRegenerated(true);
		}
	}

	// ȡ����������
	public static void cancelRegenerate(List<CommonNode> list) {
		for (CommonNode node : list) {
			node.setRegenerated(false);
		}
	}

	// �м̷�����֤�����,flag=0�м̷�����=1�����м̷���
	public static void suggest(int flag) {
		OSNR osnr = new OSNR();
		List<CommonNode> list = Suggest.suggestOSNR(OSNR.allOSNRList);
		if (list != null && list.size() != 0) {
			if (flag == 1) {// ����
				for (CommonNode node : list) {
					if (!contan(Suggest.allosnrNodeList, node))
						Suggest.allosnrNodeList.add(node);
				}
			} else {
				for (CommonNode node : list) {// �����滮ʱҲҪ���ǶԺ������ٵ�Ӱ��
					if (!contan(Suggest.allosnrNodeList, node))
						Suggest.allosnrNodeList.add(node);
				}
				for (CommonNode node : list) {
					if (!contan(Suggest.allosnrNodeList2, node))
						Suggest.allosnrNodeList2.add(node);
				}
			}
			Suggest.regenerate(list);// ������������
//			if (flag == 1) {// ����
//				if (Suggest.isDouble == true) {// ˫��
//					Evaluation.dNodeListEvaluation(CommonNode.allNodeList, 1);
//					Evaluation.dLinkListEvaluation(SpanLink.allSpanLinkList, 1);
//				} else if (Suggest.isDouble == false) {// ����
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
					if (flag == 1) {// ����
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
			Suggest.cancelRegenerate(CommonNode.allNodeList);// �����������
			// if (!RouteAlloc.isRouteEmpty(Traffic.trafficList)) {// ��·�ɲ�Ϊ��
			// RouteAlloc.clearAllTrafficRoute3(Traffic.trafficList);// �����Դ
			// ResourceAlloc.allocateResource(Traffic.trafficList,
			// Dlg_PolicySetting.mark);// �ؼ���
			// }

		}
	}

	// �н�������ȡ�����ڵ㣬û�н���ȡ���һ���ڵ�
	public static List<CommonNode> suggestOSNR(List<List<CommonNode>> osnrlist) {
		if (osnrlist == null || osnrlist.size() == 0) {
			return null;
		}
		List<List<CommonNode>> olist = new LinkedList<List<CommonNode>>();
		olist.addAll(osnrlist);// ����һ�ݣ���ֹӰ��ԭ������
		System.out.println("osnrlistsize" + olist.size());
		List<CommonNode> allnode = new LinkedList<>();// ������нڵ�
		// List<List<CommonNode>> deletelist = new LinkedList<List<CommonNode>>();//
		// ���Ҫɾ����nlist
		List<CommonNode> list = new LinkedList<>();// ��ŷ��ؽ��

		for (List<CommonNode> nlist : olist) {// �������еĽڵ�
			for (CommonNode node : nlist) {
				node.setTimes(node.getTimes() + 1);// node��times���Լ�1
				if (!allnode.contains(node)) {// ȥ��
					allnode.add(node);
				}
			}
		}
		Collections.sort(allnode);// ���ڵ㰴��times������������
		// for (CommonNode node : allnode) {// ����
		// System.out.println(node.getName() + ":" + node.getTimes());
		// }
		while (allnode.size() != 0 && allnode.get(allnode.size() - 1).getTimes() > 1) {
			// Collections.sort(allnode);// ���ڵ㰴��times������������
			// ȡ���������Ľ����ж�
			CommonNode node1 = allnode.get(allnode.size() - 1);
			list.add(node1);// ��������
			for (int i = 0; i < olist.size(); i++) {// �������е�nlist
				List<CommonNode> nlist1 = olist.get(i);
				if (nlist1.contains(node1)) {
					// System.out.println(node1.getName());
					for (CommonNode node2 : nlist1) {// ����node1��nlist�����нڵ�times-1,������times����
						node2.setTimes(node2.getTimes() - 1);
					} // ���½���
					olist.remove(nlist1);// �Ƴ�����node1��nlist
					i--;
				}
			}
			Collections.sort(allnode);// ���ڵ㰴��times������������
		}
		// System.out.println("osnr" + list);
		// ��ʣ��nlist�����һ���ڵ����list
		for (List<CommonNode> nlist : olist) {
			list.add(nlist.get(nlist.size() - 1));
		}
		// �������node��times,��ֹӰ���Ժ�ļ���
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
