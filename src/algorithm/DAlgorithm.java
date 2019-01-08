package algorithm;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.crypto.Data;

import data.*;
import dataControl.LinkData;

public class DAlgorithm {
	private static final double Infinity = 99999;

	/*
	 * flag=1 跳数 ; =0 长度; =2 负载均衡算法，根据flag初始化链路不同的权重
	 */
	public static boolean Initialization(int flag, List<WDMLink> s) {// 初始化FiberLink,层间映射时调用

		List<WDMLink> theLinkList = s;// theLinkList是传入的链表，初始化这个链表
		// 初始化链路权重
		for (int i = 0; i < theLinkList.size(); ++i) {// 把未激活的链路和节点从theLinkList中去掉，其余的初始化权重
			WDMLink theLink = theLinkList.get(i);
			if (!theLink.isActive() || !theLink.getFromNode().isActive() || !theLink.getToNode().isActive()) {
				theLinkList.remove(i);
				--i; // 因为remove元素以后，后面的元素会依次前移
			}
		}
		Iterator<WDMLink> itLink = theLinkList.iterator();// 初始化权重flag=1 跳数;
		if (flag == 1)// 跳数
		{
			while (itLink.hasNext()) {
				WDMLink theLink = itLink.next();
				theLink.setWeight(1);
			//	theLink.setWeight(1*0.4+(theLink.getZhongjiNum())*0.6);
			}
		} else if (flag == 0) {// 长度
			while (itLink.hasNext()) {
				WDMLink theLink = itLink.next();
				//先用该链路的长度除以平均链路长度使其归一
	//			double weight = ((theLink.getLength()/LinkData.aveLinkLength)*0.4+(theLink.getZhongjiNum())*0.6);
				theLink.setWeight(theLink.getLength());
			}
		}else if(flag == 2) {//负载均衡
			while(itLink.hasNext()) {
				WDMLink theLink = itLink.next();
				theLink.setWeight(DataSave.waveNum + 1 - theLink.getRemainResource());
			}
		}else if(flag == 3) {//自定义算法
			while(itLink.hasNext()) {
				WDMLink theLink = itLink.next();
				//if(DataSave.indexA!=1 && DataSave.indexB!=1 && DataSave.indexC!=1) {
					theLink.setWeight(data.DataSave.indexB + (data.DataSave.indexA)*(theLink.getLength()/WDMLink.aveLength()) 
							+ (data.DataSave.indexC)*((DataSave.waveNum-theLink.getRemainResource())/(DataSave.waveNum/2)) );
				//}
//				else if(DataSave.indexA == 1) {//距离
//						theLink.setWeight(theLink.getLength());
//				}
//				else if(DataSave.indexB == 1) {//跳数
//					theLink.setWeight(1);
//				}
//				else if(DataSave.indexC == 1) {//负载
//					theLink.setWeight(DataSave.waveNum + 1 - theLink.getRemainResource());
//				}
				
			}
		}

		// else if (flag == 2) {// 负载均衡算法的链路权重设置
		// while (itLink.hasNext()) {
		// FiberLink theLink = itLink.next();
		// double free = 0.1 + theLink.getRemainWaveNum();
		// double lenght = theLink.getLength();
		// double weight = (double) (lenght / free);
		// theLink.setWeight(weight);
		// }
		// }
		return true;
	}// 只使用了theLinkList

	/*
	 * flag=1 跳数 ; =0 长度; =2 负载均衡算法，根据flag初始化链路不同的权重
	 */
	public static boolean Initialization(List<WDMLink> s, int flag) {// 初始化WDMLink

		List<WDMLink> theLinkList = s;// theLinkList是传入的链表，初始化这个链表
		// 初始化链路权重
		for (int i = 0; i < theLinkList.size(); ++i) {// 把未激活的链路和节点从theLinkList中去掉，其余的初始化权重
			WDMLink theLink = theLinkList.get(i);
			if (!theLink.isActive() || !theLink.getFromNode().isActive() || !theLink.getToNode().isActive()) {
				theLinkList.remove(i);
				--i; // 因为remove元素以后，后面的元素会依次前移
			}
		}
		Iterator<WDMLink> itLink = theLinkList.iterator();// 初始化权重flag=1 跳数;
		if (flag == 1)// 跳数
		{
			while (itLink.hasNext()) {
				WDMLink theLink = itLink.next();
				double w = 0;
				for (SpanLink fLink : theLink.getSpanLinkList())
					w += 1;
				theLink.setWeight(w);
			}
		} else if (flag == 0) {// 长度
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
	}// 只使用了theLinkList

	/*
	 * 函数名称：SetNodeRankID 函数作用：给所有节点设置ID,为了构造权重表邻接矩阵 输入参数：需要设置算法ID的节点链表 返回参数：返回节点总数
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
			theNode.setRankID(nNodeRankID);// ID号从0开始按顺序排列
			nNodeRankID++;
			NodeSum++;
		}
		return NodeSum;
	}

	public static boolean dijkstra(int flag, CommonNode from, CommonNode to, List<CommonNode> nodeList,
			List<WDMLink> linkList, LinkedList<WDMLink> returnLinkList, LinkedList<CommonNode> returnNodeList) {// 针对wdmLink

		List<CommonNode> theNodeList = nodeList;
		List<WDMLink> theLinkList = new LinkedList<WDMLink>();
		theLinkList.addAll(linkList);// 10.27
		Initialization(flag, theLinkList);// 初始化链路权重
		
		//消除平行边的方法
		LinkedList<WDMLink> paralinkSetFalse=removeParallelLink();
		
		
		int nNodeSum = SetNodeRankID(theNodeList);// 初始化节点id

		double[][] Adjacency = new double[nNodeSum][nNodeSum];
		// 初始化邻接矩阵，把对角线的值设置为0，其他的如果存在该条链路值就是其长度，否则为无穷
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
			if (thelink.isActive() == true)// 只计算链路状态为激活的链路
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
		// 初始化邻接矩阵完成

		// D算法，得到一个Prev[]数组表示一条路由
		double Weight_min = Infinity;
		int source = from.getRankID();// 初始值是起始节点的rankid
		int v = source;
		boolean isFound[] = new boolean[nNodeSum];// 记录节点是否被标记
		double Distance[] = new double[nNodeSum];// 记录节点权重值，即各节点到达源节点的可见距离
		int Prev[] = new int[nNodeSum]; // Prev[i]是i的前驱节点
		for (int i = 0; i < nNodeSum; i++)// 初始化上面三个数组
		{
			isFound[i] = false;// 表示起始未被标记
			Distance[i] = Adjacency[source][i];// 直达
			Prev[i] = (Distance[i] < Weight_min) ? source : -1; // Prev[i]=source,可以直达，-1不可直达
		}
		isFound[source] = true; // 设置起始节点的三个属性
		Distance[source] = 0;
		Prev[source] = -1;

		for (int i = 0; i < nNodeSum; i++)// 一次只能取出来一个点，所以取N-1次即可
		{
			Weight_min = Infinity;
			for (int w = 0; w < nNodeSum; w++)// 此循环找到距离source最近的点v
			{
				if ((!isFound[w]) && Distance[w] < Weight_min) // 遍历全网未标记的点（Found[w]==false），每次循环标记的点都会被改变
				{
					v = w; // v现在代表离source最近的点
					Weight_min = Distance[w];// 与source距离最近的点，与source之间长度
				}
			}
			isFound[v] = true; // 标记离SOURCE最近的点
			for (int w = 0; w < nNodeSum; w++) // 更新节点权重值
			{
				if ((!isFound[w]) && (Weight_min + Adjacency[v][w] < Distance[w]))// source——v——w
				// <
				// source——w
				{
					Distance[w] = Weight_min + Adjacency[v][w];
					Prev[w] = v;
				}
			}
		} // 至此得到了source到所有点的最短路径

		// D算法，得到一个Prev[]数组表示一条路由
		int dest = to.getRankID();
		returnNodeList.addFirst(to);
		while (dest != -1) // 在dest前驱节点的前驱结点的…………还存在前驱结点的情况下
		{
			int u = dest;
			dest = Prev[dest];
			if (dest != -1) {
				// 遍历链路，根据Prev[]前驱节点数组找到符合的链路，生成Path路由list
				Iterator<WDMLink> itlink = theLinkList.listIterator();
				WDMLink pLink = new WDMLink();
				// List<Link> pList = new LinkedList<Link>();
				while (itlink.hasNext()) {
					WDMLink thelink = itlink.next();
					if (thelink.isActive() == true) {
						if (((thelink.getFromNode().getRankID() == u) && (thelink.getToNode().getRankID() == dest))// 根据Prev找到了包含首尾节点的链路，就是最短路径的一部分
								|| ((thelink.getToNode().getRankID() == u)
										&& (thelink.getFromNode().getRankID() == dest))) {
							pLink = thelink;// pLink就是从后往前数的第i条链路

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
				// 好像可以去掉重复的点
				if (returnNodeList.contains(pLink.getFromNode()))
					returnNodeList.addFirst(pLink.getToNode());
				else
					returnNodeList.addFirst(pLink.getFromNode());
			}
		}
		//算法结束，消除平行边中设置不激活的链路要重新激活
		if(paralinkSetFalse!=null) {
			for(int i=0;i<paralinkSetFalse.size();i++) {
				paralinkSetFalse.get(i).setActive(true);
			}
		}
		
		if (returnNodeList.size() == 1) {// 如果只有一个节点，表示没有路径，寻路失败
			returnNodeList.clear();
			return false;
		}
		return true;
	}
	

	

	public static boolean dijkstra(int flag, CommonNode from, CommonNode to, List<CommonNode> nodeList,
			LinkedList<SpanLink> linkList, LinkedList<SpanLink> returnLinkList, LinkedList<CommonNode> returnNodeList) {// 只计算光纤链路
		// TODO Auto-generated method stub

		List<CommonNode> theNodeList = nodeList;
		// List<FiberLink> theLinkList = linkList;//10.27
		List<SpanLink> theLinkList = new LinkedList<SpanLink>();
		theLinkList.addAll(linkList);// 10.27
		Initialization(flag, theLinkList);// 初始化链路权重
		int nNodeSum = SetNodeRankID(theNodeList);// 初始化节点id

		double[][] Adjacency = new double[nNodeSum][nNodeSum];
		// 初始化邻接矩阵，把对角线的值设置为0，其他的如果存在该条链路值就是其长度，否则为无穷
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
		// 初始化邻接矩阵完成

		// D算法，得到一个Prev[]数组表示一条路由
		double Weight_min = Infinity;
		int source = from.getRankID();// 初始值是起始节点的rankid
		int v = source;
		boolean isFound[] = new boolean[nNodeSum];// 记录节点是否被标记
		double Distance[] = new double[nNodeSum];// 记录节点权重值，即各节点到达源节点的可见距离
		int Prev[] = new int[nNodeSum]; // Prev[i]:i的前驱节点
		for (int i = 0; i < nNodeSum; i++)// 初始化上面三个数组
		{
			isFound[i] = false;// 表示起始未被标记
			Distance[i] = Adjacency[source][i];// 直达
			Prev[i] = (Distance[i] < Weight_min) ? source : -1; // Prev[i]=source,可以直达，-1不可直达
		}
		isFound[source] = true; // 设置起始节点的三个属性
		Distance[source] = 0;
		Prev[source] = -1;

		for (int i = 0; i < nNodeSum; i++)// 一次只能取出来一个点，所以取N-1次即可
		{
			Weight_min = Infinity;
			for (int w = 0; w < nNodeSum; w++)// 此循环找到距离source最近的点v
			{
				if ((!isFound[w]) && Distance[w] < Weight_min) // 遍历全网未标记的点（Found[w]==false），每次循环标记的点都会被改变
				{
					v = w; // v现在代表离source最近的点
					Weight_min = Distance[w];// 与source距离最近的点，与source之间长度
				}
			}
			isFound[v] = true; // 标记离SOURCE最近的点
			for (int w = 0; w < nNodeSum; w++) // 更新节点权重值
			{
				if ((!isFound[w]) && (Weight_min + Adjacency[v][w] < Distance[w]))// source——v——w
				// <
				// source——w
				{
					Distance[w] = Weight_min + Adjacency[v][w];
					Prev[w] = v;
				}
			}
		} // 至此得到了source到所有点的最短路径

		// D算法，得到一个Prev[]数组表示一条路由
		int dest = to.getRankID();
		returnNodeList.addFirst(to);
		while (dest != -1) // 在dest前驱节点的前驱结点的…………还存在前驱结点的情况下
		{
			int u = dest;
			dest = Prev[dest];
			if (dest != -1) {
				// 遍历链路，根据Prev[]前驱节点数组找到符合的链路，生成Path路由list
				Iterator<SpanLink> itlink = theLinkList.listIterator();
				SpanLink pLink = new SpanLink();
				// List<Link> pList = new LinkedList<Link>();
				while (itlink.hasNext()) {
					SpanLink thelink = itlink.next();
					if (thelink.isActive() == true) {
						if (((thelink.getFromNode().getRankID() == u) && (thelink.getToNode().getRankID() == dest))// 根据Prev找到了包含首尾节点的链路，就是最短路径的一部分
								|| ((thelink.getToNode().getRankID() == u)
										&& (thelink.getFromNode().getRankID() == dest))) {
							pLink = thelink;// pLink就是从后往前数的第i条链路

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
		if (returnNodeList.size() == 1) {// 如果只有一个节点，表示没有路径，寻路失败
			returnNodeList.clear();
			return false;
		}
		return true;
	}
	
	//消除平行边的方法
	public static LinkedList<WDMLink> removeParallelLink() {
		LinkedList<WDMLink> setFalseLink=new LinkedList<>();
		for(int i=0;i<WDMLink.WDMLinkList.size();i++) {
			WDMLink lk=WDMLink.WDMLinkList.get(i);
			lk.updateLinkResoucre();
			LinkedList<WDMLink> parallel=lk.getParallelLinkList();
			//size==1说明没有平行边，直接找下一个链路
			if(parallel.size()==1)
				continue;
			//有平行边
			else {
				//只找出状态为激活的平行边
				LinkedList<WDMLink> activeLink=new LinkedList<>();
				for(int n=0;n<parallel.size();n++) {
					if(parallel.get(n).isActive()==true)
						activeLink.add(parallel.get(n));
				}
				if(activeLink.size()==0||activeLink.size()==1)
					continue;
				int minindex=0;
				double currentWeight=activeLink.get(0).getRemainResource();
				//在状态为激活的平行边中，将可用波道最多的链路激活其余不激活
				for(int j=0;j<activeLink.size();j++) {
					WDMLink plk=activeLink.get(j);
					plk.setActive(false);
					setFalseLink.add(plk);
					//链路的weight比currentWeiht小，则更新minindex为链路的index，currentWeight为链路的weight
					if(plk.getRemainResource()>currentWeight)
					{
						minindex=j;
						currentWeight=plk.getRemainResource();
					}		
				}
				//将权值最小的链路激活
				activeLink.get(minindex).setActive(true);
			}
		}
		return setFalseLink;
	}

}
