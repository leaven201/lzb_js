package algorithm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import data.CommonNode;
import data.DataSave;
import data.Route;
import data.Traffic;
import data.WDMLink;
import data.WaveLength;
import enums.Status;
import enums.TrafficLevel;

/**
 * 功能：进行波长资源的分配 思路：对业务路由中wdmLink对应的fiberLink进行波长分配，同一wdmLink中满足波长一致性
 * 
 * @author CC
 * @since 2016/7/4
 */
public class CommonAlloc {
	// public static boolean notConsistent;// 是否不一致
	public static StringBuffer fallBuffer = new StringBuffer();// 用于暂存资源分配失败情况

	// 分配工作波长资源
	public static boolean allocateWaveLength(Route route) {
		List<WDMLink> wdmLinkList = route.getWDMLinkList(); // wdmLinkList储存该路由的WDM链路
		if (wdmLinkList.size() == 0) {// 为什么不能判断为空？
			// return true;
			return false;
		}
		Traffic traffic = route.getBelongsTraffic();// 路由所属业务
		LinkedList<Integer> startWaveLengthList = new LinkedList<Integer>();// 标记每个子系统的命中的起始波长
		int startWaveLength = 0;
		for (WDMLink wdmLink : wdmLinkList) {
			if (traffic.getNrate() > wdmLink.getNrate()) {// 判断是否满足分配的前置条件完成
				fallBuffer.append("业务(" + traffic.getFromNode().getName() + "-" + traffic.getToNode().getName()
						+ ")的速率<" + traffic.getNrate() + "G>大于光纤链路 (" + wdmLink.getFromNode().getName() + "-"
						+ wdmLink.getToNode().getName() + ")的速率<" + wdmLink.getNrate() + "G>\r\n");// 去掉强制转换链路速率为整型
				return false;
			} else if (wdmLink.getRemainResource() < 1) {// 检验是否阻塞
				Traffic.failtrafficList.add(traffic);
				fallBuffer.append(
						"业务(" + traffic.getFromNode().getName() + "-" + traffic.getToNode().getName() + ")所需波道资源不足");
				return false;
			}
		} // 通过上述循环可知业务满足速率及波道资源要求，开始进行波长分配

		if (!findWaveLength(route, startWaveLength, startWaveLengthList, wdmLinkList, true)) {//用波长一致性找
//			if (!findWaveLength(route, startWaveLength, startWaveLengthList, wdmLinkList, false))//不用波长一致性找
//				return false;
//			LinkedList<LinkedList<Integer>> allcombination = CommonAlloc.WaveAllocNoconsist(route, 1, traffic);
//			startWaveLengthList = CommonAlloc.changedLess(allcombination);
			startWaveLengthList = CommonAlloc.findBestWaveAlloc(route, traffic, 1);
			
		}
		startWaveLength = startWaveLengthList.getFirst();
		for (int hop = 0; hop < wdmLinkList.size(); hop++) {// 为每一条wdmLink链路分配连续波长
			int startWL = startWaveLengthList.get(hop);// 波长不一致时使用
			WDMLink wdmLink = wdmLinkList.get(hop);
			// 设置空闲波长为工作并承载链路
			// 如果该链路的第startWL波道状态为空闲，或者第startWL波道状态为工作且该业务的保护等级为重路由并且该波道是预置用
			if (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.空闲)
					|| (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.工作)
							&& (traffic.getProtectLevel() == TrafficLevel.PresetRESTORATION
									|| traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION)
							&& wdmLink.getWaveLengthList().get(startWL).isPre() == true)) {
				wdmLink.getWaveLengthList().get(startWL).setStatus(Status.工作);// 改变工作状态
				wdmLink.getWaveLengthList().get(startWL).setCarriedTraffic(traffic);// 为波长添加承载业务
				wdmLink.getCarriedTrafficList().add(traffic);// 为链路添加承载业务
				wdmLink.setRemainResource(wdmLink.getRemainResource() - 1);// 更新链路剩余资源
				// if (Suggest.isSuggested == true)
				// wdmLink.getWaveLengthList().get(startWL).setUsed(true);// 标记使用过的波长
				// if (Suggest.isKanghui == true)
				// wdmLink.getWaveLengthList().get(startWL).setYongguo(true);//
				// 标记抗毁用过的波长2017.10.25
				// if (traffic.getProtectLevel() == TrafficLevel.PresetRESTORATION)
				// wdmLink.getWaveLengthList().get(startWL).setPre(true);// 2017.10.14
			}
		} // 波长分配完毕
			/////////////////////////////////////
			// 如果有波长转换节点，可以令startWaveLength=0;

		// fallBuffer.append(route.getBelongsTraffic() + "波长资源分配成功！\r\n ");
		// route.setFiberWLID(startWaveLengthList.getFirst());//有波长转换功能，这一步似乎没用
		route.setWaveLengthIdList(startWaveLengthList);// 恢复资源时会用到
		route.setUsedWaveList(startWaveLengthList); // 存用的不同波长，计算中继会用到
		route.setWaveChangedNode(startWaveLengthList);
		// if (route.isNotConsistent() == true) {// 如果波长不一致
		// if (Suggest.isSuggested == true)// 2017.10.14
		// judge(route);
		// if(Suggest.isKanghui==true)
		// judge2(route);
		// }
		return true;

	}

	// 分配保护波长资源
	public static boolean allocateWaveLength1(Route route) {
		List<WDMLink> wdmLinkList = route.getWDMLinkList(); // wdmLinkList储存该路由的WDM链路
		if (wdmLinkList.size() == 0) {// 为什么不能判断为空？
			// return true;
			return false;
		}
		Traffic traffic = route.getBelongsTraffic();// 路由所属业务
		LinkedList<Integer> startWaveLengthList = new LinkedList<Integer>();// 标记每个子系统的命中的起始波长
		int startWaveLength = 0;
		for (WDMLink wdmLink : wdmLinkList) {
			if (traffic.getNrate() > wdmLink.getNrate()) {// 判断是否满足分配的前置条件完成
				fallBuffer.append("业务(" + traffic.getFromNode().getName() + "-" + traffic.getToNode().getName()
						+ ")的速率<" + traffic.getNrate() + "G>大于光纤链路 (" + wdmLink.getFromNode().getName() + "-"
						+ wdmLink.getToNode().getName() + ")的速率<" + wdmLink.getNrate() + "G>\r\n");// 去掉强制转换链路速率为整型
				return false;
			} else if (wdmLink.getRemainResource() < 1) {// 检验是否阻塞

				fallBuffer.append(
						"业务(" + traffic.getFromNode().getName() + "-" + traffic.getToNode().getName() + ")所需波道资源不足");
				return false;
			}
		} // 通过上述循环可知业务满足速率及波道资源要求，开始进行波长分配

		if (!findWaveLength(route, startWaveLength, startWaveLengthList, wdmLinkList, true)) {
//			if (!findWaveLength(route, startWaveLength, startWaveLengthList, wdmLinkList, false))
//				// fallBuffer.append("链路没有足够的波长可以分配！\r\n");
//				return false;
//			LinkedList<LinkedList<Integer>> allcombination = CommonAlloc.WaveAllocNoconsist(route, 1, traffic);
//			startWaveLengthList = CommonAlloc.changedLess(allcombination);
			startWaveLengthList = CommonAlloc.findBestWaveAlloc(route, traffic, 1);
		}
		startWaveLength = startWaveLengthList.getFirst();
		for (int hop = 0; hop < wdmLinkList.size(); hop++) {// 为每一条wdmLink链路分配连续波长
			int startWL = startWaveLengthList.get(hop);// 波长不一致时使用
			WDMLink wdmLink = wdmLinkList.get(hop);
			// 设置空闲波长为工作并承载链路
			if (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.空闲)
					|| (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.工作)
							&& traffic.getProtectLevel() == TrafficLevel.PresetRESTORATION
							&& wdmLink.getWaveLengthList().get(startWL).isPre() == true)) {
				wdmLink.getWaveLengthList().get(startWL).setStatus(Status.保护);// 改变工作状态
				wdmLink.getWaveLengthList().get(startWL).setCarriedTraffic(traffic);// 为波长添加承载业务
				wdmLink.getCarriedTrafficList().add(traffic);// 为链路添加承载业务
				wdmLink.setRemainResource(wdmLink.getRemainResource() - 1);// 更新链路剩余资源
				// if (Suggest.isSuggested == true)
				// wdmLink.getWaveLengthList().get(startWL).setUsed(true);// 标记使用过的波长
				// if (Suggest.isKanghui == true)
				// wdmLink.getWaveLengthList().get(startWL).setYongguo(true);//
				// 标记抗毁用过的波长2017.10.25
				// if (traffic.getProtectLevel() == TrafficLevel.PresetRESTORATION)
				// wdmLink.getWaveLengthList().get(startWL).setPre(true);// 2017.10.14
			}
		} // 波长分配完毕
			/////////////////////////////////////
			// 如果有波长转换节点，可以令startWaveLength=0;

		// fallBuffer.append(route.getBelongsTraffic() + "波长资源分配成功！\r\n ");
		// route.setFiberWLID(startWaveLengthList.getFirst());//有波长转换功能，这一步似乎没用
		route.setWaveLengthIdList(startWaveLengthList);// 恢复资源时会用到
		route.setUsedWaveList(startWaveLengthList); // 存用的不同波长，计算中继会用到
		route.setWaveChangedNode1(startWaveLengthList); // 存储进行波长转换的节点
		// if (route.isNotConsistent() == true) {// 如果波长不一致
		// if (Suggest.isSuggested == true)// 2017.10.14
		// judge(route);
		// if(Suggest.isKanghui==true)
		// judge2(route);
		// }
		return true;

	}

	// 分配预置恢复波长资源
	public static boolean allocateWaveLength2(Route route) {
		List<WDMLink> wdmLinkList = route.getWDMLinkList(); // wdmLinkList储存该路由的WDM链路
		if (wdmLinkList.size() == 0) {// 为什么不能判断为空？
			// return true;
			return false;
		}
		Traffic traffic = route.getBelongsTraffic();// 路由所属业务
		LinkedList<Integer> startWaveLengthList = new LinkedList<Integer>();// 标记每个子系统的命中的起始波长
		int startWaveLength = 0;
		for (WDMLink wdmLink : wdmLinkList) {
			if (traffic.getNrate() > wdmLink.getNrate()) {// 判断是否满足分配的前置条件完成
				fallBuffer.append("业务(" + traffic.getFromNode().getName() + "-" + traffic.getToNode().getName()
						+ ")的速率<" + traffic.getNrate() + "G>大于光纤链路 (" + wdmLink.getFromNode().getName() + "-"
						+ wdmLink.getToNode().getName() + ")的速率<" + wdmLink.getNrate() + "G>\r\n");// 去掉强制转换链路速率为整型
				return false;
			} else if (wdmLink.getRemainResource() < 1) {// 检验是否阻塞

				fallBuffer.append(
						"业务(" + traffic.getFromNode().getName() + "-" + traffic.getToNode().getName() + ")所需波道资源不足");
				return false;
			}
		} // 通过上述循环可知业务满足速率及波道资源要求，开始进行波长分配

		if (!findWaveLength1(route, startWaveLength, startWaveLengthList, wdmLinkList, true, traffic)) // 如果利用波长一致性后未找到
		// 则利用波长不一致性去找
		{
			// fallBuffer.append("链路没有足够的连续波长可以分配！\r\n");
//			if (!findWaveLength1(route, startWaveLength, startWaveLengthList, wdmLinkList, false, traffic))// 如果波长不一致也不行则分配不成功
//				// fallBuffer.append("链路没有足够的波长可以分配！\r\n");
//				return false;
//			LinkedList<LinkedList<Integer>> allcombination = CommonAlloc.WaveAllocNoconsist(route, 2, traffic);
//			startWaveLengthList = CommonAlloc.changedLess(allcombination);
			startWaveLengthList = CommonAlloc.findBestWaveAlloc(route, traffic, 2);
			
		}
		startWaveLength = startWaveLengthList.getFirst();
		for (int hop = 0; hop < wdmLinkList.size(); hop++) {// 为每一条wdmLink链路分配连续波长
			int startWL = startWaveLengthList.get(hop);// 波长不一致时使用
			WDMLink wdmLink = wdmLinkList.get(hop);
			// 设置空闲波长为工作并承载链路
			if (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.空闲)
					|| (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.恢复)
							&& (traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION
									|| traffic.getProtectLevel() == TrafficLevel.RESTORATION)
					// && wdmLink.getWaveLengthList().get(startWL).isPre() == true
					)) {
				if (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.空闲)) {
					wdmLink.setRemainResource(wdmLink.getRemainResource() - 1);
				} // 更新链路剩余资源
				wdmLink.getWaveLengthList().get(startWL).setStatus(Status.恢复);// 改变工作状态
				// wdmLink.getWaveLengthList().get(startWL).setCarriedTraffic(traffic);//为波长添加承载业务
				wdmLink.getWaveLengthList().get(startWL).getPreTrafficList().add(traffic);// 加入该波长的预置业务表
				wdmLink.getCarriedTrafficList().add(traffic);// 为链路添加承载业务
				// 如果该次使用的是波道原本状态是空闲波长，链路剩余资源才减少，如果使用的波道原本状态是恢复波长，则波道资源不变
				
					// if (Suggest.isSuggested == true)
					// wdmLink.getWaveLengthList().get(startWL).setUsed(true);// 标记使用过的波长
					// if (Suggest.isKanghui == true)
					// wdmLink.getWaveLengthList().get(startWL).setYongguo(true);//
					// 标记抗毁用过的波长2017.10.25
				if (traffic.getProtectLevel() == TrafficLevel.PresetRESTORATION
						|| traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION)
					wdmLink.getWaveLengthList().get(startWL).setPre(true);// 2017.10.14
			}
		} // 波长分配完毕
			/////////////////////////////////////
			// 如果有波长转换节点，可以令startWaveLength=0;

		// fallBuffer.append(route.getBelongsTraffic() + "波长资源分配成功！\r\n ");
		// route.setFiberWLID(startWaveLengthList.getFirst());//有波长转换功能，这一步似乎没用
		route.setWaveLengthIdList(startWaveLengthList);// 恢复资源时会用到
		route.setUsedWaveList(startWaveLengthList); // 存用的不同波长，计算中继会用到
		route.setWaveChangedNode1(startWaveLengthList); // 存储进行波长转换的节点
		// if (route.isNotConsistent() == true) {// 如果波长不一致
		// if (Suggest.isSuggested == true)// 2017.10.14
		// judge(route);
		// if(Suggest.isKanghui==true)
		// judge2(route);
		// }
		return true;

	}
	
	//动态恢复波长分配
	public static boolean allocateWaveLength3(Route route) {
		List<WDMLink> wdmLinkList = route.getWDMLinkList(); // wdmLinkList储存该路由的WDM链路
		if (wdmLinkList.size() == 0) {// 为什么不能判断为空？
			// return true;
			return false;
		}
		Traffic traffic = route.getBelongsTraffic();// 路由所属业务
		LinkedList<Integer> startWaveLengthList = new LinkedList<Integer>();// 标记每个子系统的命中的起始波长
		int startWaveLength = 0;
		for (WDMLink wdmLink : wdmLinkList) {
			if (traffic.getNrate() > wdmLink.getNrate()) {// 判断是否满足分配的前置条件完成
				fallBuffer.append("业务(" + traffic.getFromNode().getName() + "-" + traffic.getToNode().getName()
						+ ")的速率<" + traffic.getNrate() + "G>大于光纤链路 (" + wdmLink.getFromNode().getName() + "-"
						+ wdmLink.getToNode().getName() + ")的速率<" + wdmLink.getNrate() + "G>\r\n");// 去掉强制转换链路速率为整型
				return false;
			} else if (wdmLink.getRemainResource() < 1) {// 检验是否阻塞

				fallBuffer.append(
						"业务(" + traffic.getFromNode().getName() + "-" + traffic.getToNode().getName() + ")所需波道资源不足");
				return false;
			}
		} // 通过上述循环可知业务满足速率及波道资源要求，开始进行波长分配

		if (!findWaveLength1(route, startWaveLength, startWaveLengthList, wdmLinkList, true, traffic)) // 如果利用波长一致性后未找到
		// 则利用波长不一致性去找
		{
			// fallBuffer.append("链路没有足够的连续波长可以分配！\r\n");
//			if (!findWaveLength2(route, startWaveLength, startWaveLengthList, wdmLinkList, false, traffic))// 如果波长不一致也不行则分配不成功
//				// fallBuffer.append("链路没有足够的波长可以分配！\r\n");
//				return false;
//			LinkedList<LinkedList<Integer>> allcombination = CommonAlloc.WaveAllocNoconsist(route, 2, traffic);
//			startWaveLengthList = CommonAlloc.changedLess(allcombination);
			startWaveLengthList = CommonAlloc.findBestWaveAlloc(route, traffic, 2);
		}
		startWaveLength = startWaveLengthList.getFirst();
		for (int hop = 0; hop < wdmLinkList.size(); hop++) {// 为每一条wdmLink链路分配连续波长
			int startWL = startWaveLengthList.get(hop);// 波长不一致时使用
			WDMLink wdmLink = wdmLinkList.get(hop);
			 //设置空闲波长为工作并承载链路
//			if (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.空闲)
//					|| (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.恢复)
//							&& (traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION
//									|| traffic.getProtectLevel() == TrafficLevel.RESTORATION)
//					// && wdmLink.getWaveLengthList().get(startWL).isPre() == true
//					)
//					) {
				if (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.空闲)) {
					wdmLink.setRemainResource(wdmLink.getRemainResource() - 1);
				} // 更新链路剩余资源
				wdmLink.getWaveLengthList().get(startWL).setStatus(Status.仿真);// 改变工作状态
				// wdmLink.getWaveLengthList().get(startWL).setCarriedTraffic(traffic);//为波长添加承载业务
				wdmLink.getWaveLengthList().get(startWL).getDynamicTrafficList().add(traffic);// 加入该波长的动态业务表
				//将该波长是否为仿真时使用设为true
				//wdmLink.getCarriedTrafficList().add(traffic);// 为链路添加承载业务
				// 如果该次使用的是波道原本状态是空闲波长，链路剩余资源才减少，如果使用的波道原本状态是恢复波长，则波道资源不变
				
					// if (Suggest.isSuggested == true)
					// wdmLink.getWaveLengthList().get(startWL).setUsed(true);// 标记使用过的波长
					// if (Suggest.isKanghui == true)
					// wdmLink.getWaveLengthList().get(startWL).setYongguo(true);//
					// 标记抗毁用过的波长2017.10.25
				if (traffic.getProtectLevel() == TrafficLevel.PresetRESTORATION
						|| traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION)
					wdmLink.getWaveLengthList().get(startWL).setPre(true);// 2017.10.14
//			}
		} // 波长分配完毕
			/////////////////////////////////////
			// 如果有波长转换节点，可以令startWaveLength=0;

		// fallBuffer.append(route.getBelongsTraffic() + "波长资源分配成功！\r\n ");
		// route.setFiberWLID(startWaveLengthList.getFirst());//有波长转换功能，这一步似乎没用
		route.setWaveLengthIdList(startWaveLengthList);// 恢复资源时会用到
		route.setUsedWaveList(startWaveLengthList); // 存用的不同波长，计算中继会用到
		route.setWaveChangedNode1(startWaveLengthList); // 存储进行波长转换的节点
		// if (route.isNotConsistent() == true) {// 如果波长不一致
		// if (Suggest.isSuggested == true)// 2017.10.14
		// judge(route);
		// if(Suggest.isKanghui==true)
		// judge2(route);
		// }
		return true;

	}
	

	// 用以寻找工作和保护路由波长的方法
	public static boolean findWaveLength(Route route, int start, LinkedList<Integer> startWaveLength,
			List<WDMLink> wdmLinkList, boolean isConsistent) {
		// isConsitent表示是否要求波长一致性，true为考虑，false为不考虑
		// if (start >= fiberLinkList.get(0).getWaveNum()) {
		// start=0
		if ((start >= data.DataSave.waveNum - DataSave.locknum-DataSave.onlyPre)) {
			// fallBuffer.append(route+"链路没有足够的连续波长可以分配！\r\n");
			return false;
		}

		if (startWaveLength.size() != 0)// 如果不为空，则清空
			startWaveLength.clear();
		int hopSum = wdmLinkList.size(); // 记录该路由的跳数
		int cc = start;// 记录start初始值，以便在每条链路都以初始值开始寻找
		for (int hop = 0; hop < hopSum; hop++) {
			WDMLink wdmLink = wdmLinkList.get(hop);
			for (int i = cc; i < wdmLink.getWaveLengthList().size()- DataSave.locknum-DataSave.onlyPre; i++) {// 从cc开始找到80，如果该链路的第cc波道资源空闲，
																			// 则将cc加入（储存该业务在每个链路上用的波道的表里）startWaveLengh
																			// 然后去下一跳里寻找波道
				if (wdmLink.getWaveLengthList().get(i).getStatus().equals(Status.空闲)) {
					startWaveLength.add(i);
					cc = i;// 改变初始波长序号
					break;
				}
			}
		}
		if (startWaveLength.size() != hopSum) {// 当二者不相等时，说明某个wdmLink没有可用的波长资源
			return false;
		}

		// 在这里决定是否要进行波长一致性判断
		if (isConsistent == true) {
			/**
			 * 下面新增每条链路所需波长序号一致判断
			 */       
			int max = startWaveLength.getFirst();
			int min = max;
			for (int i : startWaveLength) {
				max = max > i ? max : i; // 循环后max为startWaveLength中的最大值
				min = min < i ? min : i; // 循环后min为startWaveLength中的最小值
			}
			if (max != min) {// 表示链路开始波长序号不全一样，从最大值开始重新找
				startWaveLength.clear();
				return findWaveLength(route, max, startWaveLength, wdmLinkList, true);
			} else  //max==min,说明波长一致，return true说明波长一致分配成功
				return true;
		} else {// 若不要求波长一致性，则直接返回true
			route.setNotConsistent(true);
			return true;
		}
	}

	// 用以寻找预置路由波长的方法
	public static boolean findWaveLength1(Route route, int start, LinkedList<Integer> startWaveLength,
			List<WDMLink> wdmLinkList, boolean isConsistent, Traffic tra) {
		// isConsitent表示是否要求波长一致性，true为考虑，false为不考虑
		// if (start >= fiberLinkList.get(0).getWaveNum()) {
		// start=0
		if ((start >= data.DataSave.waveNum - DataSave.locknum)) {
			// fallBuffer.append(route+"链路没有足够的连续波长可以分配！\r\n");
			return false;
		}

		if (startWaveLength.size() != 0)// 如果不为空，则清空
			startWaveLength.clear();
		int hopSum = wdmLinkList.size(); // 记录该路由的跳数
		int cc = start;// 记录start初始值，以便在每条链路都以初始值开始寻找
		for (int hop = 0; hop < hopSum; hop++) {
			WDMLink wdmLink = wdmLinkList.get(hop);
			for (int i = cc; i < wdmLink.getWaveLengthList().size()- DataSave.locknum; i++) {// 从cc开始找到80，如果该链路的第cc波道资源空闲，
																			// 则将cc加入（储存该业务在每个链路上用的波道的表里）startWaveLengh
																			// 然后去下一跳里寻找波道
				if (wdmLink.getWaveLengthList().get(i).getStatus().equals(Status.空闲)// 该波道资源为空闲
						// 该波道的状态是恢复，并且该波道承载的业务的路由与该路由分离，并且该业务的预置共享是share
						|| (wdmLink.getWaveLengthList().get(i).getStatus().equals(Status.恢复)
								&& Route.isPreWaveCanShared(tra, wdmLink.getWaveLengthList().get(i).preTrafficList))) {
					startWaveLength.add(i);
					cc = i;// 改变初始波长序号
					break;
				}
			}
		}
		if (startWaveLength.size() != hopSum) {// 当二者不相等时，说明某个wdmLink没有可用的波长资源
			return false;
		}

		// 在这里决定是否要进行波长一致性判断
		if (isConsistent == true) {
			/**
			 * 下面新增每条链路所需波长序号一致判断
			 */
			int max = startWaveLength.getFirst();
			int min = max;
			for (int i : startWaveLength) {
				max = max > i ? max : i; // 循环后max为startWaveLength中的最大值
				min = min < i ? min : i; // 循环后min为startWaveLength中的最小值
			}
			if (max != min) {// 表示链路开始波长序号不全一样，从最大值开始重新找
				startWaveLength.clear();
				return findWaveLength1(route, max, startWaveLength, wdmLinkList, true, tra);
			} else
				return true;
		} else {// 若不要求波长一致性，则直接返回true
			route.setNotConsistent(true);
			return true;
		}
	}
	
	// 用以寻找动态恢复路由波长的方法
	public static boolean findWaveLength2(Route route, int start, LinkedList<Integer> startWaveLength,
			List<WDMLink> wdmLinkList, boolean isConsistent, Traffic tra) {
		// isConsitent表示是否要求波长一致性，true为考虑，false为不考虑
		// if (start >= fiberLinkList.get(0).getWaveNum()) {
		// start=0
		if ((start >= data.DataSave.waveNum)) {
			// fallBuffer.append(route+"链路没有足够的连续波长可以分配！\r\n");
			return false;
		}

		if (startWaveLength.size() != 0)// 如果不为空，则清空
			startWaveLength.clear();
		int hopSum = wdmLinkList.size(); // 记录该路由的跳数
		int cc = start;// 记录start初始值，以便在每条链路都以初始值开始寻找
		for (int hop = 0; hop < hopSum; hop++) {
			WDMLink wdmLink = wdmLinkList.get(hop);
			for (int i = cc; i < wdmLink.getWaveLengthList().size(); i++) {// 从cc开始找到80，如果该链路的第cc波道资源空闲，
																			// 则将cc加入（储存该业务在每个链路上用的波道的表里）startWaveLengh
																			// 然后去下一跳里寻找波道
				if (wdmLink.getWaveLengthList().get(i).getStatus().equals(Status.空闲)// 该波道资源为空闲
						// 该波道的状态是恢复，并且该波道承载的业务的路由与该路由分离，并且该业务的预置共享是share
						|| (wdmLink.getWaveLengthList().get(i).getStatus().equals(Status.恢复)
								&& Route.isPreWaveCanShared(tra, wdmLink.getWaveLengthList().get(i).preTrafficList)))
				{
					startWaveLength.add(i);
					cc = i;// 改变初始波长序号
					break;
				}
			}
		}
		if (startWaveLength.size() != hopSum) {// 当二者不相等时，说明某个wdmLink没有可用的波长资源
			return false;
		}

		// 在这里决定是否要进行波长一致性判断
		if (isConsistent == true) {
			/**
			 * 下面新增每条链路所需波长序号一致判断
			 */
			int max = startWaveLength.getFirst();
			int min = max;
			for (int i : startWaveLength) {
				max = max > i ? max : i; // 循环后max为startWaveLength中的最大值
				min = min < i ? min : i; // 循环后min为startWaveLength中的最小值
			}
			if (max != min) {// 表示链路开始波长序号不全一样，从最大值开始重新找
				startWaveLength.clear();
				return findWaveLength2(route, max, startWaveLength, wdmLinkList, true, tra);
			} else
				return true;
		} else {// 若不要求波长一致性，则直接返回true
			route.setNotConsistent(true);
			return true;
		}
	}

	/*
	 * // 判断需要波长转换功能的节点 public static void judge(Route route) { if (route == null)
	 * return; int k = route.getWaveLengthIdList().get(0); for (int i = 1; i <
	 * route.getWaveLengthIdList().size(); i++) { if (k !=
	 * route.getWaveLengthIdList().get(i)) { CommonNode node =
	 * route.getWDMLinkList().get(i).getFromNode(); if (Suggest.flag == 1) {//
	 * 抗毁波长转换 if (!Suggest.conversionNodeList.contains(node))// 去重
	 * Suggest.conversionNodeList.add(node); // return; } else {// 波长转换 if
	 * (!Suggest.conversionNodeList.contains(node))// 去重
	 * Suggest.conversionNodeList.add(node); if
	 * (!Suggest.conversionNodeList2.contains(node))// 去重
	 * Suggest.conversionNodeList2.add(node); // return; } } } }
	 */
	/*
	 * // 判断需要波长转换功能的节点,抗毁仿真专用--2017.10.26 public static void judge2(Route route) {
	 * if (route == null) return; int k = route.getWaveLengthIdList().get(0); for
	 * (int i = 1; i < route.getWaveLengthIdList().size(); i++) { if (k !=
	 * route.getWaveLengthIdList().get(i)) { CommonNode node =
	 * route.getWDMLinkList().get(i).getFromNode(); if
	 * (!Suggest.converNodeKanghuiList.contains(node))// 去重
	 * Suggest.converNodeKanghuiList.add(node); } } }
	 */
	//子波长仅分配工作路由和保护路由
		public static boolean EallocateWaveLength_workandpro(Route route) {
			List<WDMLink> wdmLinkList = route.getWDMLinkList(); // wdmLinkList储存该路由的WDM链路
			if (wdmLinkList.size() == 0) {// 为什么不能判断为空？
				// return true;
				return false;
			}
			Traffic traffic = route.getBelongsTraffic();// 路由所属业务
			LinkedList<Integer> startWaveLengthList = new LinkedList<Integer>();// 标记每个子系统的命中的起始波长
			int startWaveLength = 0;
			for (WDMLink wdmLink : wdmLinkList) {
				if (traffic.getNrate() > wdmLink.getNrate()) {// 判断是否满足分配的前置条件完成
					fallBuffer.append("业务(" + traffic.getFromNode().getName() + "-" + traffic.getToNode().getName()
							+ ")的速率<" + traffic.getNrate() + "G>大于光纤链路 (" + wdmLink.getFromNode().getName() + "-"
							+ wdmLink.getToNode().getName() + ")的速率<" + wdmLink.getNrate() + "G>\r\n");// 去掉强制转换链路速率为整型
					return false;
				} else if (wdmLink.getRemainResource() < 1) {// 检验是否阻塞

					fallBuffer.append(
							"业务(" + traffic.getFromNode().getName() + "-" + traffic.getToNode().getName() + ")所需波道资源不足");
					return false;
				}
			} // 通过上述循环可知业务满足速率及波道资源要求，开始进行波长分配

			if (!EfindWaveLength_workandpro(route, startWaveLength, startWaveLengthList, wdmLinkList, true,traffic)) {
				if (!EfindWaveLength_workandpro(route, startWaveLength, startWaveLengthList, wdmLinkList, false,traffic))
					return false;
			}
			startWaveLength = startWaveLengthList.getFirst();
			for (int hop = 0; hop < wdmLinkList.size(); hop++) {// 为每一条wdmLink链路分配连续波长
				int startWL = startWaveLengthList.get(hop);// 波长不一致时使用
				WDMLink wdmLink = wdmLinkList.get(hop);
				
				//只要进入了这个方法，说明都是被当做子波长处理的，因此对应的波道状态都设为子波长
				wdmLink.getWaveLengthList().get(startWL).setStatus(Status.子波长);// 改变工作状态
				wdmLink.getWaveLengthList().get(startWL).carriedTrafficList.add(traffic);// 为波长添加承载业务
				wdmLink.getCarriedTrafficList().add(traffic);// 为链路添加承载业务
				//更新波道资源信息
				wdmLink.getWaveLengthList().get(startWL).updateWaveResource();
				//更新链路资源信息
				wdmLink.updateLinkResoucre();
				
				
			} // 波长分配完毕
				/////////////////////////////////////
				// 如果有波长转换节点，可以令startWaveLength=0;

			// fallBuffer.append(route.getBelongsTraffic() + "波长资源分配成功！\r\n ");
			// route.setFiberWLID(startWaveLengthList.getFirst());//有波长转换功能，这一步似乎没用
			route.setWaveLengthIdList(startWaveLengthList);// 恢复资源时会用到
			route.setUsedWaveList(startWaveLengthList); // 存用的不同波长，计算中继会用到
			route.setWaveChangedNode(startWaveLengthList);
			// if (route.isNotConsistent() == true) {// 如果波长不一致
			// if (Suggest.isSuggested == true)// 2017.10.14
			// judge(route);
			// if(Suggest.isKanghui==true)
			// judge2(route);
			// }
			return true;
		}
		
		//子波长分配预置路由
		public static boolean EallocateWaveLength_pre(Route route) {
			List<WDMLink> wdmLinkList = route.getWDMLinkList(); // wdmLinkList储存该路由的WDM链路
			if (wdmLinkList.size() == 0) {// 为什么不能判断为空？
				// return true;
				return false;
			}
			Traffic traffic = route.getBelongsTraffic();// 路由所属业务
			LinkedList<Integer> startWaveLengthList = new LinkedList<Integer>();// 标记每个子系统的命中的起始波长
			int startWaveLength = 0;
			for (WDMLink wdmLink : wdmLinkList) {
				if (traffic.getNrate() > wdmLink.getNrate()) {// 判断是否满足分配的前置条件完成
					fallBuffer.append("业务(" + traffic.getFromNode().getName() + "-" + traffic.getToNode().getName()
							+ ")的速率<" + traffic.getNrate() + "G>大于光纤链路 (" + wdmLink.getFromNode().getName() + "-"
							+ wdmLink.getToNode().getName() + ")的速率<" + wdmLink.getNrate() + "G>\r\n");// 去掉强制转换链路速率为整型
					return false;
				} else if (wdmLink.getRemainResource() < 1) {// 检验是否阻塞

					fallBuffer.append(
							"业务(" + traffic.getFromNode().getName() + "-" + traffic.getToNode().getName() + ")所需波道资源不足");
					return false;
				}
			} // 通过上述循环可知业务满足速率及波道资源要求，开始进行波长分配

			if (!EfindWaveLength_pre(route, startWaveLength, startWaveLengthList, wdmLinkList, true,traffic)) {
				if (!EfindWaveLength_pre(route, startWaveLength, startWaveLengthList, wdmLinkList, false,traffic))
					return false;
			}
			startWaveLength = startWaveLengthList.getFirst();
			for (int hop = 0; hop < wdmLinkList.size(); hop++) {// 为每一条wdmLink链路分配连续波长
				int startWL = startWaveLengthList.get(hop);// 波长不一致时使用
				WDMLink wdmLink = wdmLinkList.get(hop);
				
				//只要进入了这个方法，说明都是被当做子波长处理的，因此对应的波道状态都设为子波长
				wdmLink.getWaveLengthList().get(startWL).setStatus(Status.子波长);// 改变工作状态
				wdmLink.getWaveLengthList().get(startWL).getPreTrafficList().add(traffic);// 为波长添加承载业务
				wdmLink.getCarriedTrafficList().add(traffic);// 为链路添加承载业务
				//更新波道资源信息
				wdmLink.getWaveLengthList().get(startWL).updateWaveResource();
				//更新链路资源信息
				wdmLink.updateLinkResoucre();
				
				
			} // 波长分配完毕
				/////////////////////////////////////
				// 如果有波长转换节点，可以令startWaveLength=0;

			// fallBuffer.append(route.getBelongsTraffic() + "波长资源分配成功！\r\n ");
			// route.setFiberWLID(startWaveLengthList.getFirst());//有波长转换功能，这一步似乎没用
			route.setWaveLengthIdList(startWaveLengthList);// 恢复资源时会用到
			route.setUsedWaveList(startWaveLengthList); // 存用的不同波长，计算中继会用到
			route.setWaveChangedNode(startWaveLengthList);
			// if (route.isNotConsistent() == true) {// 如果波长不一致
			// if (Suggest.isSuggested == true)// 2017.10.14
			// judge(route);
			// if(Suggest.isKanghui==true)
			// judge2(route);
			// }
			return true;
		}
		
		//子波长--为工作和保护路由寻找可用波道
		public static boolean EfindWaveLength_workandpro(Route route, int start,
				LinkedList<Integer> startWaveLengthList, List<WDMLink> wdmLinkList, boolean isConsistent,Traffic traffic) {
			
			boolean useSubWave=false;//判断是否使用了子波长
			if ((start >= data.DataSave.waveNum)) {
				// fallBuffer.append(route+"链路没有足够的连续波长可以分配！\r\n");
				return false;
			}
			if (startWaveLengthList.size() != 0)// 检查存储该业务在每条链路上所用波道号的表是否为空
				startWaveLengthList.clear();
			int hopSum = wdmLinkList.size(); // 记录该路由的跳数
			int cc = start;// 记录start初始值，以便在每条链路都以初始值开始寻找
			
			//看看是否可以优先使用子波长碎片
			for(int hop=0;hop<hopSum;hop++) {//循环1：循环路由每一跳链路
				WDMLink wdmLink = wdmLinkList.get(hop);
				for (int i = 0; i < wdmLink.getWaveLengthList().size(); i++) {//循环2：循环链路每一个波道
					WaveLength wave=wdmLink.getWaveLengthList().get(i);
					//判断该波道是否已被子波长业务利用过
					if(wave.getStatus().equals(Status.子波长)) {
						//判断该wave在route的每一跳上是否都满足可用条件
						if(Route.isRouteCanUseWave(route, wave, traffic)) {//true为可用，则设置startWaveLengthList(0-79)
							for(int o=0;o<hopSum;o++) {
								startWaveLengthList.add(i);
							}
							useSubWave=true;
							break;//跳出循环2
						}
					}
				}
				if(useSubWave)
					break;//跳出循环1
			}
			
			
			
			//当不能利用子波长的时候就直接按波长一致性去找就行
			if(!useSubWave) {
			
				for (int hop = 0; hop < hopSum; hop++) {
					WDMLink wdmLink = wdmLinkList.get(hop);
					for (int i = cc; i < wdmLink.getWaveLengthList().size(); i++) {// 从cc开始找到79，如果该链路的第cc波道资源空闲，
																					// 则将cc加入（储存该业务在每个链路上用的波道的表里）startWaveLengh
																					// 然后去下一跳里寻找波道
						if (wdmLink.getWaveLengthList().get(i).getStatus().equals(Status.空闲)) {
							startWaveLengthList.add(i);
							cc = i;// 改变初始波长序号
							break;
						}
					}
				}
			}
			
			if (startWaveLengthList.size() != hopSum) {// 当二者不相等时，说明某个wdmLink没有可用的波长资源
				return false;
			}

			// 在这里决定是否要进行波长一致性判断
			if (isConsistent == true) {
				/**
				 * 下面新增每条链路所需波长序号一致判断
				 */    
				
				int max = startWaveLengthList.getFirst();
				int min = max;
				for (int i : startWaveLengthList) {
					max = max > i ? max : i; // 循环后max为startWaveLength中的最大值
					min = min < i ? min : i; // 循环后min为startWaveLength中的最小值
				}
				if (max != min) {// 表示链路开始波长序号不全一样，从最大值开始重新找
					startWaveLengthList.clear();
					return findWaveLength(route, max, startWaveLengthList, wdmLinkList, true);
				} else  //max==min,说明波长一致，return true说明波长一致分配成功
					return true;
			} else {// 若不要求波长一致性，则直接返回true
				route.setNotConsistent(true);
				return true;
			}
			//return false;
		}
		
		
		//子波长--为预置路由寻找可用波道
			public static boolean EfindWaveLength_pre(Route route, int start,
					LinkedList<Integer> startWaveLengthList, List<WDMLink> wdmLinkList, boolean isConsistent,Traffic traffic) {
				
				boolean useSubWave=false;//判断是否使用了子波长
				if ((start >= data.DataSave.waveNum)) {
					// fallBuffer.append(route+"链路没有足够的连续波长可以分配！\r\n");
					return false;
				}
				if (startWaveLengthList.size() != 0)// 检查存储该业务在每条链路上所用波道号的表是否为空
					startWaveLengthList.clear();
				int hopSum = wdmLinkList.size(); // 记录该路由的跳数
				int cc = start;// 记录start初始值，以便在每条链路都以初始值开始寻找
				
				//看看是否可以优先使用子波长碎片
				for(int hop=0;hop<hopSum;hop++) {//循环1：循环路由每一跳链路
					WDMLink wdmLink = wdmLinkList.get(hop);
					for (int i = 0; i < wdmLink.getWaveLengthList().size(); i++) {//循环2：循环链路每一个波道
						WaveLength wave=wdmLink.getWaveLengthList().get(i);
						//判断该波道是否已被子波长业务利用过
						if(wave.getStatus().equals(Status.子波长)) {
							//判断该wave在route的每一跳上是否都满足可用条件
							if(Route.isRouteCanUseWave_shared(route, wave, traffic)) {//true为可用，则设置startWaveLengthList(0-79)
								for(int o=0;o<hopSum;o++) {
									startWaveLengthList.add(i);
								}
								useSubWave=true;
								break;//跳出循环2
							}
						}
					}
					if(useSubWave)
						break;//跳出循环1
				}
				
				
				
				//当不能利用子波长的时候就直接按波长一致性去找就行
				if(!useSubWave) {
				
					for (int hop = 0; hop < hopSum; hop++) {
						WDMLink wdmLink = wdmLinkList.get(hop);
						for (int i = cc; i < wdmLink.getWaveLengthList().size(); i++) {// 从cc开始找到79，如果该链路的第cc波道资源空闲，
																						// 则将cc加入（储存该业务在每个链路上用的波道的表里）startWaveLengh
																						// 然后去下一跳里寻找波道
							if (wdmLink.getWaveLengthList().get(i).getStatus().equals(Status.空闲)
									|| (wdmLink.getWaveLengthList().get(i).getStatus().equals(Status.恢复)
											&& Route.isPreWaveCanShared(traffic, wdmLink.getWaveLengthList().get(i).preTrafficList))) {
								startWaveLengthList.add(i);
								cc = i;// 改变初始波长序号
								break;
							}
						}
					}
				}
				
				if (startWaveLengthList.size() != hopSum) {// 当二者不相等时，说明某个wdmLink没有可用的波长资源
					return false;
				}

				// 在这里决定是否要进行波长一致性判断
				if (isConsistent == true) {
					/**
					 * 下面新增每条链路所需波长序号一致判断
					 */    
					
					int max = startWaveLengthList.getFirst();
					int min = max;
					for (int i : startWaveLengthList) {
						max = max > i ? max : i; // 循环后max为startWaveLength中的最大值
						min = min < i ? min : i; // 循环后min为startWaveLength中的最小值
					}
					if (max != min) {// 表示链路开始波长序号不全一样，从最大值开始重新找
						startWaveLengthList.clear();
						return findWaveLength(route, max, startWaveLengthList, wdmLinkList, true);
					} else  //max==min,说明波长一致，return true说明波长一致分配成功
						return true;
				} else {// 若不要求波长一致性，则直接返回true
					route.setNotConsistent(true);
					return true;
				}
				//return false;
			}
			
			
	//当波长一致性无法满足时的波长分配策略，需要用最少的波长转换  flag: 1工作保护   2预置
	public static LinkedList<LinkedList<Integer>> WaveAllocNoconsist(Route route,int flag, Traffic tra) {
		LinkedList<LinkedList<Integer>> list = new LinkedList();
		for(int i=0; i<route.getWDMLinkList().size();i++) {
			WDMLink link = route.getWDMLinkList().get(i);
			LinkedList<Integer> wavelist = new LinkedList();
			for(int j=0;j<link.getWaveLengthList().size();j++) {
				WaveLength wave = link.getWaveLengthList().get(j);
				if(flag == 1) {//工作保护
					if(wave.getStatus().equals(Status.空闲)) {
						wavelist.add(j);
					}
				}
				else if(flag == 2) {//预置
					if(wave.getStatus().equals(Status.空闲)// 该波道资源为空闲
							// 该波道的状态是恢复，并且该波道承载的业务的路由与该路由分离，并且该业务的预置共享是share
							|| (wave.getStatus().equals(Status.恢复)
									&& Route.isPreWaveCanShared(tra, wave.preTrafficList))) {
						wavelist.add(j);
					}
				}
			}
			list.add(wavelist);
		}
		return allCombination(list);
		
	}
	//n个list 每个list里取1个的所有组合
	public static LinkedList<LinkedList<Integer>> allCombination(LinkedList<LinkedList<Integer>> list) {
		int hop = list.size();
		LinkedList<Integer> list1 = list.get(0);
		LinkedList<Integer> list2 = list.get(1);
		int len1 = list.get(0).size();
		int len2 = list.get(1).size();
		LinkedList<LinkedList<Integer>> Combination = new LinkedList();
		for(int i=0; i<len1; i++) {
			for(int j=0; j<len2; j++) {
				LinkedList<Integer> newList = new LinkedList();
				newList.add(list1.get(i));
				newList.add(list2.get(j));
				Combination.add(newList);
			}
		}
		
		int index = 2;		
//		LinkedList<LinkedList<Integer>> allCombination = new LinkedList();
//		allCombination = combine(Combination, list, hop, index);
		return combine(Combination, list, hop, index);
	}
	
	public static LinkedList<LinkedList<Integer>> combine(LinkedList<LinkedList<Integer>> list1,LinkedList<LinkedList<Integer>> list,int hop, int index){
		
		if(list1.get(0).size() == hop) {
			return list1;
		}
		
		LinkedList<Integer> list2 = list.get(index);
		int len1 = list1.size();
		int len2 = list2.size();
		
		LinkedList<LinkedList<Integer>> thelist = new LinkedList();
		
		for(int i=0; i<len1; i++) {
			for(int j=0; j<len2; j++) {
				LinkedList<Integer> newList = new LinkedList();
				newList.addAll(list1.get(i));
				newList.add(list2.get(j));
				thelist.add(newList);
			}
		}
		
		return combine(thelist, list, hop, index+1);
	}
	
	public static LinkedList<Integer> changedLess(LinkedList<LinkedList<Integer>> combination){
		LinkedList<Integer> less = new LinkedList();
		int min = 100;
		for(LinkedList<Integer> i : combination) {
			int sum = sumOfChange(i);
			if(sum<min) {
				less = i;
			}
		}
		return less;
	}
	public static int sumOfChange(LinkedList<Integer> list) {
		int sum=0;
		for(int i=1;i<list.size();i++) {
			if(list.get(i-1)!=list.get(i)) {
				sum++;
			}
		}
		return sum;
	}
	//看看波道wave从路由的startindex开始， 连续几跳可用
	public static int sumOfWaveiConsist(int wave,Route route, int startIndex,Traffic tra,int flag) {
		int sum = 0;
		for(int i=startIndex; i<route.getWDMLinkList().size();i++) {
			WDMLink link = route.getWDMLinkList().get(i);
			if(link.isLinkHaveWavei(wave, flag, tra)) {
				sum++;
			}else {
				return sum;
			}
		}
		return sum;
	}
	//看看从第i跳开始，哪个波道是最大连续可用的    res[0] 连续几跳  res[1]波道（0-79）
	public static int[] bestWave(Route route,Traffic tra,int startIndex,int flag) {
		int[] res = new int[2];
		res[0]=res[1]=-1;
		for(int i=0;i<80;i++) {
			int sum = sumOfWaveiConsist(i+1, route, startIndex, tra, flag);
			if(sum>res[0]) {
				res[0] = sum;
				res[1] = i;
			}
		}
		return res;
	}
	
	
	//找出route，使用中继最少的波道组合    // 判断该链路是否波道i可用 flag=1(工作/保护) flag=2(预置)
	public static LinkedList<Integer> findBestWaveAlloc(Route route,Traffic tra, int flag) {
		LinkedList<Integer> best = new LinkedList();
		int[] res = bestWave(route, tra, 0, flag);
		for(int i=0;i<res[0];i++) {
			best.add(res[1]);
		}
		while(best.size() < route.getWDMLinkList().size()){
			res = bestWave(route, tra, best.size(), flag);
			for(int i=0;i<res[0];i++) {
				best.add(res[1]);
			}
		}
		return best;
	}
	
	
}
