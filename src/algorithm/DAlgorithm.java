package algorithm;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import data.*;
import dataControl.LinkData;

public class DAlgorithm {
	private static final double Infinity = 99999;

	/*
	 * flag=1 ���� ; =0 ����; =2 ���ؾ����㷨������flag��ʼ����·��ͬ��Ȩ��
	 */
	public static boolean Initialization(int flag, List<WDMLink> s) {// ��ʼ��FiberLink,���ӳ��ʱ����

		List<WDMLink> theLinkList = s;// theLinkList�Ǵ����������ʼ���������
		// ��ʼ����·Ȩ��
		for (int i = 0; i < theLinkList.size(); ++i) {// ��δ�������·�ͽڵ��theLinkList��ȥ��������ĳ�ʼ��Ȩ��
			WDMLink theLink = theLinkList.get(i);
			if (!theLink.isActive() || !theLink.getFromNode().isActive() || !theLink.getToNode().isActive()) {
				theLinkList.remove(i);
				--i; // ��ΪremoveԪ���Ժ󣬺����Ԫ�ػ�����ǰ��
			}
		}
		Iterator<WDMLink> itLink = theLinkList.iterator();// ��ʼ��Ȩ��flag=1 ����;
		if (flag == 1)// ����
		{
			while (itLink.hasNext()) {
				WDMLink theLink = itLink.next();
				theLink.setWeight(1);
			//	theLink.setWeight(1*0.4+(theLink.getZhongjiNum())*0.6);
			}
		} else if (flag == 0) {// ����
			while (itLink.hasNext()) {
				WDMLink theLink = itLink.next();
				//���ø���·�ĳ��ȳ���ƽ����·����ʹ���һ
	//			double weight = ((theLink.getLength()/LinkData.aveLinkLength)*0.4+(theLink.getZhongjiNum())*0.6);
				theLink.setWeight(theLink.getLength());
			}
		}

		// else if (flag == 2) {// ���ؾ����㷨����·Ȩ������
		// while (itLink.hasNext()) {
		// FiberLink theLink = itLink.next();
		// double free = 0.1 + theLink.getRemainWaveNum();
		// double lenght = theLink.getLength();
		// double weight = (double) (lenght / free);
		// theLink.setWeight(weight);
		// }
		// }
		return true;
	}// ֻʹ����theLinkList

	/*
	 * flag=1 ���� ; =0 ����; =2 ���ؾ����㷨������flag��ʼ����·��ͬ��Ȩ��
	 */
	public static boolean Initialization(List<WDMLink> s, int flag) {// ��ʼ��WDMLink

		List<WDMLink> theLinkList = s;// theLinkList�Ǵ����������ʼ���������
		// ��ʼ����·Ȩ��
		for (int i = 0; i < theLinkList.size(); ++i) {// ��δ�������·�ͽڵ��theLinkList��ȥ��������ĳ�ʼ��Ȩ��
			WDMLink theLink = theLinkList.get(i);
			if (!theLink.isActive() || !theLink.getFromNode().isActive() || !theLink.getToNode().isActive()) {
				theLinkList.remove(i);
				--i; // ��ΪremoveԪ���Ժ󣬺����Ԫ�ػ�����ǰ��
			}
		}
		Iterator<WDMLink> itLink = theLinkList.iterator();// ��ʼ��Ȩ��flag=1 ����;
		if (flag == 1)// ����
		{
			while (itLink.hasNext()) {
				WDMLink theLink = itLink.next();
				double w = 0;
				for (SpanLink fLink : theLink.getSpanLinkList())
					w += 1;
				theLink.setWeight(w);
			}
		} else if (flag == 0) {// ����
			while (itLink.hasNext()) {
				WDMLink theLink = itLink.next();
				int l = 0;
				for (SpanLink fl : theLink.getSpanLinkList()) {
					l += fl.getLength();
				}
				theLink.setWeight(l);
			}
		}
		return true;
	}// ֻʹ����theLinkList

	/*
	 * �������ƣ�SetNodeRankID �������ã������нڵ�����ID,Ϊ�˹���Ȩ�ر��ڽӾ��� �����������Ҫ�����㷨ID�Ľڵ����� ���ز��������ؽڵ�����
	 */
	public static int SetNodeRankID(List<CommonNode> theNodeList) {
		if (theNodeList.isEmpty()) {
			return 0;
		}
		int NodeSum = 0;
		int nNodeRankID = 0;
		Iterator<CommonNode> it = theNodeList.listIterator();
		while (it.hasNext()) {
			CommonNode theNode = it.next();
			theNode.setRankID(nNodeRankID);// ID�Ŵ�0��ʼ��˳������
			nNodeRankID++;
			NodeSum++;
		}
		return NodeSum;
	}

	public static boolean dijkstra(int flag, CommonNode from, CommonNode to, List<CommonNode> nodeList,
			List<WDMLink> linkList, LinkedList<WDMLink> returnLinkList, LinkedList<CommonNode> returnNodeList) {// ���wdmLink

		List<CommonNode> theNodeList = nodeList;
		List<WDMLink> theLinkList = new LinkedList<WDMLink>();
		theLinkList.addAll(linkList);// 10.27
		
		/*
		 * ����������ƽ�бߵķ���
		 */

		Initialization(flag, theLinkList);// ��ʼ����·Ȩ��
		int nNodeSum = SetNodeRankID(theNodeList);// ��ʼ���ڵ�id

		double[][] Adjacency = new double[nNodeSum][nNodeSum];
		// ��ʼ���ڽӾ��󣬰ѶԽ��ߵ�ֵ����Ϊ0��������������ڸ�����·ֵ�����䳤�ȣ�����Ϊ����
		for (int i = 0; i < nNodeSum; i++) {
			for (int j = 0; j < nNodeSum; j++) {
				if (i != j) {
					Adjacency[i][j] = Infinity;
				} else {
					Adjacency[i][j] = 0;
				}
			}
		}
		Iterator<WDMLink> it = theLinkList.iterator();
		while (it.hasNext()) {
			WDMLink thelink = it.next();
			if (thelink.isActive() == true)// ֻ������·״̬Ϊ�������·
			{
				int fromID = thelink.getFromNode().getRankID();
				int toID = thelink.getToNode().getRankID();
				if (thelink.getWeight() < Adjacency[fromID][toID]) {
					Adjacency[fromID][toID] = thelink.getWeight();
				}
				if (thelink.getWeight() < Adjacency[toID][fromID]) {
					Adjacency[toID][fromID] = thelink.getWeight();
				}
			}
		}
		// ��ʼ���ڽӾ������

		// D�㷨���õ�һ��Prev[]�����ʾһ��·��
		double Weight_min = Infinity;
		int source = from.getRankID();// ��ʼֵ����ʼ�ڵ��rankid
		int v = source;
		boolean isFound[] = new boolean[nNodeSum];// ��¼�ڵ��Ƿ񱻱��
		double Distance[] = new double[nNodeSum];// ��¼�ڵ�Ȩ��ֵ�������ڵ㵽��Դ�ڵ�Ŀɼ�����
		int Prev[] = new int[nNodeSum]; // Prev[i]��i��ǰ���ڵ�
		for (int i = 0; i < nNodeSum; i++)// ��ʼ��������������
		{
			isFound[i] = false;// ��ʾ��ʼδ�����
			Distance[i] = Adjacency[source][i];// ֱ��
			Prev[i] = (Distance[i] < Weight_min) ? source : -1; // Prev[i]=source,����ֱ�-1����ֱ��
		}
		isFound[source] = true; // ������ʼ�ڵ����������
		Distance[source] = 0;
		Prev[source] = -1;

		for (int i = 0; i < nNodeSum; i++)// һ��ֻ��ȡ����һ���㣬����ȡN-1�μ���
		{
			Weight_min = Infinity;
			for (int w = 0; w < nNodeSum; w++)// ��ѭ���ҵ�����source����ĵ�v
			{
				if ((!isFound[w]) && Distance[w] < Weight_min) // ����ȫ��δ��ǵĵ㣨Found[w]==false����ÿ��ѭ����ǵĵ㶼�ᱻ�ı�
				{
					v = w; // v���ڴ�����source����ĵ�
					Weight_min = Distance[w];// ��source��������ĵ㣬��source֮�䳤��
				}
			}
			isFound[v] = true; // �����SOURCE����ĵ�
			for (int w = 0; w < nNodeSum; w++) // ���½ڵ�Ȩ��ֵ
			{
				if ((!isFound[w]) && (Weight_min + Adjacency[v][w] < Distance[w]))// source����v����w
				// <
				// source����w
				{
					Distance[w] = Weight_min + Adjacency[v][w];
					Prev[w] = v;
				}
			}
		} // ���˵õ���source�����е�����·��

		// D�㷨���õ�һ��Prev[]�����ʾһ��·��
		int dest = to.getRankID();
		returnNodeList.addFirst(to);
		while (dest != -1) // ��destǰ���ڵ��ǰ�����ġ�������������ǰ�����������
		{
			int u = dest;
			dest = Prev[dest];
			if (dest != -1) {
				// ������·������Prev[]ǰ���ڵ������ҵ����ϵ���·������Path·��list
				Iterator<WDMLink> itlink = theLinkList.listIterator();
				WDMLink pLink = new WDMLink();
				// List<Link> pList = new LinkedList<Link>();
				while (itlink.hasNext()) {
					WDMLink thelink = itlink.next();
					if (thelink.isActive() == true) {
						if (((thelink.getFromNode().getRankID() == u) && (thelink.getToNode().getRankID() == dest))// ����Prev�ҵ��˰�����β�ڵ����·���������·����һ����
								|| ((thelink.getToNode().getRankID() == u)
										&& (thelink.getFromNode().getRankID() == dest))) {
							pLink = thelink;// pLink���ǴӺ���ǰ���ĵ�i����·

							// if(pLink.getFromNode() == null)
							// {
							// pLink = thelink;
							//// pList.add(pLink);
							// }
							// else
							// {
							// if (thelink.getWeight() < pLink.getWeight())
							// {
							// pLink = thelink;
							//// pList.add(pLink);
							// }
							// }
						}
					}
				}

				returnLinkList.addFirst(pLink);
				// �������ȥ���ظ��ĵ�
				if (returnNodeList.contains(pLink.getFromNode()))
					returnNodeList.addFirst(pLink.getToNode());
				else
					returnNodeList.addFirst(pLink.getFromNode());
			}
		}
		if (returnNodeList.size() == 1) {// ���ֻ��һ���ڵ㣬��ʾû��·����Ѱ·ʧ��
			returnNodeList.clear();
			return false;
		}
		return true;
	}
	
	
	//����ƽ�бߵķ���
	public static void removeParallelLink(int flag,List<WDMLink> linkList) {
		
	}
	

	public static boolean dijkstra(int flag, CommonNode from, CommonNode to, List<CommonNode> nodeList,
			LinkedList<SpanLink> linkList, LinkedList<SpanLink> returnLinkList, LinkedList<CommonNode> returnNodeList) {// ֻ���������·
		// TODO Auto-generated method stub

		List<CommonNode> theNodeList = nodeList;
		// List<FiberLink> theLinkList = linkList;//10.27
		List<SpanLink> theLinkList = new LinkedList<SpanLink>();
		theLinkList.addAll(linkList);// 10.27
		Initialization(flag, theLinkList);// ��ʼ����·Ȩ��
		int nNodeSum = SetNodeRankID(theNodeList);// ��ʼ���ڵ�id

		double[][] Adjacency = new double[nNodeSum][nNodeSum];
		// ��ʼ���ڽӾ��󣬰ѶԽ��ߵ�ֵ����Ϊ0��������������ڸ�����·ֵ�����䳤�ȣ�����Ϊ����
		for (int i = 0; i < nNodeSum; i++) {
			for (int j = 0; j < nNodeSum; j++) {
				if (i != j) {
					Adjacency[i][j] = Infinity;
				} else {
					Adjacency[i][j] = 0;
				}
			}
		}
		Iterator<SpanLink> it = theLinkList.iterator();
		while (it.hasNext()) {
			SpanLink thelink = it.next();
			if (thelink.isActive() == true) {
				int fromID = thelink.getFromNode().getRankID();
				int toID = thelink.getToNode().getRankID();
				if (thelink.getWeight() < Adjacency[fromID][toID]) {
					Adjacency[fromID][toID] = thelink.getWeight();
				}
				if (thelink.getWeight() < Adjacency[toID][fromID]) {
					Adjacency[toID][fromID] = thelink.getWeight();
				}
			}
		}
		// ��ʼ���ڽӾ������

		// D�㷨���õ�һ��Prev[]�����ʾһ��·��
		double Weight_min = Infinity;
		int source = from.getRankID();// ��ʼֵ����ʼ�ڵ��rankid
		int v = source;
		boolean isFound[] = new boolean[nNodeSum];// ��¼�ڵ��Ƿ񱻱��
		double Distance[] = new double[nNodeSum];// ��¼�ڵ�Ȩ��ֵ�������ڵ㵽��Դ�ڵ�Ŀɼ�����
		int Prev[] = new int[nNodeSum]; // Prev[i]:i��ǰ���ڵ�
		for (int i = 0; i < nNodeSum; i++)// ��ʼ��������������
		{
			isFound[i] = false;// ��ʾ��ʼδ�����
			Distance[i] = Adjacency[source][i];// ֱ��
			Prev[i] = (Distance[i] < Weight_min) ? source : -1; // Prev[i]=source,����ֱ�-1����ֱ��
		}
		isFound[source] = true; // ������ʼ�ڵ����������
		Distance[source] = 0;
		Prev[source] = -1;

		for (int i = 0; i < nNodeSum; i++)// һ��ֻ��ȡ����һ���㣬����ȡN-1�μ���
		{
			Weight_min = Infinity;
			for (int w = 0; w < nNodeSum; w++)// ��ѭ���ҵ�����source����ĵ�v
			{
				if ((!isFound[w]) && Distance[w] < Weight_min) // ����ȫ��δ��ǵĵ㣨Found[w]==false����ÿ��ѭ����ǵĵ㶼�ᱻ�ı�
				{
					v = w; // v���ڴ�����source����ĵ�
					Weight_min = Distance[w];// ��source��������ĵ㣬��source֮�䳤��
				}
			}
			isFound[v] = true; // �����SOURCE����ĵ�
			for (int w = 0; w < nNodeSum; w++) // ���½ڵ�Ȩ��ֵ
			{
				if ((!isFound[w]) && (Weight_min + Adjacency[v][w] < Distance[w]))// source����v����w
				// <
				// source����w
				{
					Distance[w] = Weight_min + Adjacency[v][w];
					Prev[w] = v;
				}
			}
		} // ���˵õ���source�����е�����·��

		// D�㷨���õ�һ��Prev[]�����ʾһ��·��
		int dest = to.getRankID();
		returnNodeList.addFirst(to);
		while (dest != -1) // ��destǰ���ڵ��ǰ�����ġ�������������ǰ�����������
		{
			int u = dest;
			dest = Prev[dest];
			if (dest != -1) {
				// ������·������Prev[]ǰ���ڵ������ҵ����ϵ���·������Path·��list
				Iterator<SpanLink> itlink = theLinkList.listIterator();
				SpanLink pLink = new SpanLink();
				// List<Link> pList = new LinkedList<Link>();
				while (itlink.hasNext()) {
					SpanLink thelink = itlink.next();
					if (thelink.isActive() == true) {
						if (((thelink.getFromNode().getRankID() == u) && (thelink.getToNode().getRankID() == dest))// ����Prev�ҵ��˰�����β�ڵ����·���������·����һ����
								|| ((thelink.getToNode().getRankID() == u)
										&& (thelink.getFromNode().getRankID() == dest))) {
							pLink = thelink;// pLink���ǴӺ���ǰ���ĵ�i����·

							// if(pLink.getFromNode() == null)
							// {
							// pLink = thelink;
							//// pList.add(pLink);
							// }
							// else
							// {
							// if (thelink.getWeight() < pLink.getWeight())
							// {
							// pLink = thelink;
							//// pList.add(pLink);
							// }
							// }
						}
					}
				}
				returnLinkList.addFirst(pLink);
				if (returnNodeList.contains(pLink.getFromNode()))
					returnNodeList.addFirst(pLink.getToNode());
				else
					returnNodeList.addFirst(pLink.getFromNode());
			}
		}
		if (returnNodeList.size() == 1) {// ���ֻ��һ���ڵ㣬��ʾû��·����Ѱ·ʧ��
			returnNodeList.clear();
			return false;
		}
		return true;
	}

}
