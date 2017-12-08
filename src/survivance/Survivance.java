package survivance;

/**
 * @author CC
 * 功能：生存性/抗毁性评估/仿真准备工作
 * 备注：注意泛型的使用
 * @date 9.16
 */
import java.util.LinkedList;
import java.util.List;

import algorithm.CommonAlloc;
import algorithm.PortAlloc;
import algorithm.RouteAlloc;
import data.CommonNode;
import data.FiberLink;
import data.LinkRGroup;
import data.Route;
import data.FiberLink;
import data.Traffic;
import data.WDMLink;

import java.util.Iterator;

public class Survivance {
    public static int mark;//用于标志选择哪种路由算法
	private List<Traffic> affectedTrafficList = new LinkedList<Traffic>();

	/**
	 * 函数名称：setFault 函数作用：设定故障链路status：false ，isSurvice为TRUE 输入参数：fiber链路 返回参数：TRUE
	 */
	public static void setFault(List<FiberLink> FiberLinkList1) { // 设置故障状态
		for (int i = 0; i < FiberLinkList1.size(); ++i) {
			for(WDMLink wl:FiberLinkList1.get(i).getCarriedWDMLinkList()) {
			wl.setActive(false);
			}
			// surLinkList1.get(i).setSurvived(true);
		}
	}

	/**
	 * 函数名称：setback 函数作用：恢复故障链路状态为TRUE bsurvice为false 输入参数：fiber链路 返回参数：TRUE
	 */
	public static void setback(List<FiberLink> FiberLinkList1) { // 恢复故障设置
//		List<FiberLink> FiberLinkList=new LinkedList<FiberLink>();
//		if(isSRLG==true)
//		{
//			for(int i=0;i<FiberLinkList1.size();++i){ //SRLG 把关联业务组中的业务的链路都加入FiberLinkList1
//				for(int j=0;j<FiberLinkList1.get(i).getFiberRelatedList().size();++j){
//					for(int k=0;k<FiberLinkList1.get(i).getFiberRelatedList().get(j).getSRLGFiberLinkList().size();++k){
//						FiberLinkList.add(FiberLinkList1.get(i).getFiberRelatedList().get(j).getSRLGFiberLinkList().get(k));
//					}//这里面是有重复的情况的
//				}
//			}
//		}
		for (int i = 0; i < FiberLinkList1.size(); ++i) {
			for(WDMLink wl:FiberLinkList1.get(i).getCarriedWDMLinkList()) {
			wl.setActive(true);
			}
			// surLinkList1.get(i).setSurvived(false);
		}
	}

	/**
	 * 函数名称：reCompute 函数作用：为故障业务重新算路，恢复业务 输入参数：traffic链路 返回参数：
	 */
	public static void reCompute(List<Traffic> trafficList) {
		// assert FiberLink.allFiberLinkList.size() == 35 : "链路改变了";// 10.27

		for (int i = 0; i < trafficList.size(); ++i) {
			Traffic tra = trafficList.get(i);
			int type = mark;// 最小长度算法
			switch (tra.getFaultType()) {// 业务故障类型1 工作 2 保护 3工作保护
			case 1: // 1为工作受影响
				switch (tra.getProtectLevel()) {// 业务保护等级
				case PERMANENT11:
					if (tra.getProtectRoute() != null && tra.getProtectRoute().getFiberLinkList() != null
							&& tra.getProtectRoute().getFiberLinkList().size() != 0) {
						tra.setResumeRoute(tra.getProtectRoute());
						setLinkStatus(tra, false);

						algorithm.ResourceAlloc.allocateRscSingle(tra, type, 1,0);// 计算恢复路由的保护路由
						setLinkStatus(tra, true);

					} else {
						algorithm.ResourceAlloc.allocateRscSingle(tra, type, 0,0);
					}
					break;
				case NORMAL11:
					if (tra.getProtectRoute() != null && tra.getProtectRoute().getFiberLinkList() != null
							&& tra.getProtectRoute().getFiberLinkList().size() != 0) {
						tra.setResumeRoute(tra.getProtectRoute());
					}

					break;
				case RESTORATION:
					if(tra.getPreRoute()!=null&&tra.getPreRoute().getFiberLinkList()!=null //判断专享预制是否存在
					&& tra.getPreRoute().getFiberLinkList().size()!=0) {
						tra.setResumeRoute(tra.getPreRoute());
					}else {
					algorithm.ResourceAlloc.allocateRscSingle(tra, type, 0,0);
					}
					break;
				case PROTECTandRESTORATION:
					if (tra.getProtectRoute() != null && tra.getProtectRoute().getFiberLinkList() != null//优先使用1+1的保护路由作为恢复路由
							&& tra.getProtectRoute().getFiberLinkList().size() != 0) {
						tra.setResumeRoute(tra.getProtectRoute());
					}else {
						if(tra.getPreRoute()!=null&&tra.getPreRoute().getFiberLinkList()!=null //判断专享预制是否存在
						&& tra.getPreRoute().getFiberLinkList().size()!=0) {
						   tra.setResumeRoute(tra.getPreRoute());
						}else {
						algorithm.ResourceAlloc.allocateRscSingle(tra, type, 0,0);
						}
					}
					break;

//				case PresetRESTORATION:// 和1+1类似
//					if (tra.getProtectRoute() != null && tra.getProtectRoute().getFiberLinkList() != null
//							&& tra.getProtectRoute().getFiberLinkList().size() != 0) {
//						tra.setResumeRoute(tra.getProtectRoute());
//					}
//					break;
				case NONPROTECT:
					break;
				default:
					break;
				}
				break;
			case 2: // 2为保护受影响
				switch (tra.getProtectLevel()) {
				case PERMANENT11:
					tra.setResumeRoute(tra.getWorkRoute());
					setLinkStatus(tra, false);
					algorithm.ResourceAlloc.allocateRscSingle(tra, type, 1,0);
					setLinkStatus(tra, true);
					break;
				case NORMAL11:
					tra.setResumeRoute(tra.getWorkRoute());
					break;
				case RESTORATION:
					tra.setResumeRoute(tra.getWorkRoute());
					break;
				case PresetRESTORATION:
					tra.setResumeRoute(tra.getWorkRoute());
					break;
				case PROTECTandRESTORATION:
					tra.setResumeRoute(tra.getWorkRoute());
					break;
				case NONPROTECT:
					tra.setResumeRoute(tra.getWorkRoute());
					break;
				default:
					break;
				}
				break;
			case 3: // 3为工作保护都受影响
				switch (tra.getProtectLevel()) {
				case PERMANENT11:
					algorithm.ResourceAlloc.allocateRscSingle(tra, type, 0,0);
					setLinkStatus(tra, false);
					algorithm.ResourceAlloc.allocateRscSingle(tra, type, 1,0);
					setLinkStatus(tra, true);
					break;
				case NORMAL11:
					break;
				case RESTORATION:
					if(tra.getPreRoute()!=null&&tra.getPreRoute().getFiberLinkList()!=null
					&&tra.getPreRoute().getFiberLinkList().size()!=0) {
						tra.setResumeRoute(tra.getPreRoute());
					}else {
						break;
					}
					break;
				case PROTECTandRESTORATION:
					if(tra.getPreRoute()!=null&&tra.getPreRoute().getFiberLinkList()!=null
						&&tra.getPreRoute().getFiberLinkList().size()!=0){
							tra.setResumeRoute(tra.getPreRoute());
						}else {
							break;
						}
					break;
				case PresetRESTORATION:
					break;
				case NONPROTECT:
					break;
				default:
					break;
				}
				break;
			}// end switch
		} // end for
			// assert FiberLink.allFiberLinkList.size() == 35 : "链路改变了";// 10.27

	}

	/**
	 * 功能：设置业务的恢复路由状态为b
	 * 
	 * @param tra
	 * @param b
	 */
	private static void setLinkStatus(Traffic tra, boolean b) {
		Route resumeRoute = tra.getResumeRoute();
		if (resumeRoute == null)
			return;// 找不着路时的 2016.11.1
		for (WDMLink bl : resumeRoute.getWDMLinkList()) {
			bl.setActive(b);
		}
	}

	/**
	 * 功能：通过受影响的linkList得到受影响的业务，并设置业务的故障类型
	 * 
	 * @param linkList
	 * 
	 */
	public List<Traffic> getAffectedTraffic(List<FiberLink> affectedLinkList) {

		// assert FiberLink.allFiberLinkList.size() == 35 : "链路改变了";// 10.27

		for (int i = 0; i < affectedLinkList.size(); ++i) // 遍历受影响的链路
		{
			FiberLink bLink = affectedLinkList.get(i);
			for (WDMLink wLink : bLink.getCarriedWDMLinkList()) {
				if (wLink.getCarriedTrafficList() == null)
					return null; // CC 2016.10.20 为没有承载业务的链路做保护防止异常

				List<Traffic> temptraList = wLink.getCarriedTrafficList(); // 受影响的链路上的业务链
				if (temptraList != null) {
					for (int j = 0; j < temptraList.size(); ++j) {// 遍历当前链路上的业务
						Traffic tra = temptraList.get(j);// 此业务的路由至少有一条受到影响
						if (!affectedTrafficList.contains(tra)) {// 判断是否出现过重复的受影响业务
							if (tra.getWorkRoute().getWDMLinkList().contains(wLink)) {// 判断工作路由是否包含当前受影响链路
								tra.setFaultType(1); // 工作受影响

								for (FiberLink curbl : affectedLinkList) {
									if (tra.getProtectRoute() == null)
										break;
									if (tra.getProtectRoute().getFiberLinkList().contains(curbl)) {
										tra.setFaultType(3); // 工作和保护都受影响
										break;
									}
								}

							} // 包含工作路由结束
							else if (tra.getProtectRoute() != null) {
								if (tra.getProtectRoute().getWDMLinkList().contains(wLink)) {
									tra.setFaultType(2); // 保护受影响、

									for (FiberLink curbl : affectedLinkList) {
										if (tra.getWorkRoute().getFiberLinkList().contains(curbl)) {
											tra.setFaultType(3); // 工作和保护都受影响
											break;
										}
									}
								}
							} // 包含保护路由结束
							affectedTrafficList.add(tra);
						}
					}
				}
			}
		}
		return affectedTrafficList;
	}

	/**
	 * 函数名称：getNodeEffectLink 函数作用：通过节点搜寻包含节点的链路 输入参数：node节点 返回参数：True
	 */

	public static List<FiberLink> getNodeEffectLink(CommonNode node) {
		List<FiberLink> basicLinkList = new LinkedList<FiberLink>();
		Iterator<FiberLink> it = FiberLink.getFiberLinkList().iterator();
		while (it.hasNext()) {
			FiberLink fl = it.next();
			if (fl.getFromNode().equals(node) || fl.getToNode().equals(node))
				basicLinkList.add(fl);
		}
		return basicLinkList;
	}
}
