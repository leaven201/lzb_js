package survivance;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import algorithm.RouteAlloc;
import algorithm.Suggest;
import data.CommonNode;
import data.Port;
import data.Route;
import data.FiberLink;
import data.LinkRGroup;
import data.Traffic;
import data.WDMLink;
import data.WaveLength;
import dataControl.TrafficData;
import database.NodeDataBase;
import database.TrafficDatabase;
import design.NetDesign_zs;
import survivance.Evaluation;

/**
 * 生存性总入口
 * 
 * @author CC
 * @since 10.8
 */
public class Evaluation {
    public static List<Traffic>PutOutTraffic=new LinkedList<>();
	// 轮循用到次数，其他用的是百分比
    
    public static int linkCutoff = 0; // 轮循链路评估时中断次数
	public static int linkKeep = 0; // 轮循链路评估时保持次数
	public static int linkDown = 0; // 轮循链路评估时降级次数

	public static int nodeCutoff = 0; // 轮循节点评估时中断次数  节点单断
	public static int nodeKeep = 0; // 轮循节点评估时保持次数    节点单断
	public static int nodeDown = 0; // 轮循节点评估时降级次数   节点单断

	public static int scutoff=0;       //轮循SRLG评估时中断次数
	public static int skeep=0;         //轮循SRLG评估时保持次数
	public static int sdown=0;         //轮循SRLG评估时降级次数
	
	public static float nCutoffPercent = 0; // 单节点评估时中断百分比
	public static float nKeepPercent = 0; // 单节点评估时保持百分比
	public static float nDownPercent = 0; // 单节点评估时降级百分比
	public static float nUneffPercent = 0; // 单节点评估时未受影响百分比

	public static int trasum = Traffic.trafficList.size(); // 业务总数
	public static float cutoffPercent = 0; // 单链时中断百分比
	public static float keepPercent = 0; // 单链时保持百分比
	public static float downPercent = 0; // 单链时降级百分比
	public static float uneffPercent = 0; // 单链时未受影响百分比
	
	public static float skeeppercent=0; //单SRLG评估时保持百分比
	public static float scutoffpercent=0;//单SRLG评估时中断百分比
	public static float sdownpercent=0; //单SRLG评估时降级百分比
	public static float suneffpercent=0;//单SRLG评估时降级百分比

	public static int dLinkCutoff = 0; // 双断轮循链路评估时中断次数
	public static int dLinkKeep = 0; // 双断轮循链路评估时保持次数
	public static int dLinkDown = 0; // 双断轮循链路评估时降级次数

	public static int dNodeCutoff = 0; // 双断节点轮循链路评估时中断次数
	public static int dNodeKeep = 0; // 双断节点轮循链路评估时保持次数
	public static int dNodeDown = 0; // 双断节点轮循链路评估时降级次数

	public static float multiLinkCutoffPer = 0; // 多链时中断百分比
	public static float multiLinkKeepPer = 0; // 多链时保持百分比
	public static float multiLinkDownPer = 0; // 多链时降级百分比
	public static float multiLinkUneffPer = 0; // 多链时未受影响百分比

	public static float multiNodeCutoffPer = 0; // 多节点时中断百分比
	public static float multiNodeKeepPer = 0; // 多节点时保持百分比
	public static float multiNodeDownPer = 0; // 多节点时降级百分比
	public static float multiNodeUneffPer = 0; // 多节点时未受影响百分比
	
	public static float multiCutoffPer = 0; // 人工时中断百分比
	public static float multiKeepPer = 0; // 人工时保持百分比
	public static float multiDownPer = 0; // 人工时降级百分比
	public static float multiUneffPer = 0; // 人工时未受影响百分比
	public static int ceshi=0;
    public static int ceshilianlu=0;
    public static int ceshi0=0;
    public static int ceshi1=0;
    public static int ceshi2=0;
    public static int ceshi3=0;
	/**
	 * 节点单断
	 * 
	 * @param node
	 * @return 受影响的业务
	 */
	public static List<Traffic> nodeEvaluation(CommonNode node) {
		Survivance surv = new Survivance();
		List<FiberLink> basicLink = Survivance.getNodeEffectLink(node);
//		for (int j=0;j<basicLink.size();j++) {
//			System.out.println("输出所有的受影响链路"+basicLink.get(j));			
//		}
//		for(int i=0;i<WDMLink.WDMLinkList.size();i++) {
//			System.out.println("输出所有链路的故障状态"+i+"   "+WDMLink.WDMLinkList.get(i).isBroken());
//			System.out.println("输出所有链路的标号"+WDMLink.WDMLinkList.get(i));
//		}
		Survivance.setFault(basicLink);// 设置故障状态
//		for(int i=0;i<WDMLink.WDMLinkList.size();i++) {
//			System.out.println("输出所有链路的故障状态"+i+"   "+WDMLink.WDMLinkList.get(i).isBroken());
//			
//		}
		List<Traffic> trafficList = surv.getAffectedTraffic(basicLink);
		System.out.println(trafficList);
		for(Traffic tra:trafficList) {
			if (tra.getFaultType()==0) {
				ceshi0++;
			}
			if(tra.getFaultType()==1) {
				ceshi1++;
			}
			if (tra.getFaultType()==2) {
				ceshi2++;
			}
			if (tra.getFaultType()==3) {
				ceshi3++;
			}
		}
		ceshi+=trafficList.size();
		ceshilianlu+=basicLink.size();
//		System.out.println("输出所有的受影响业务"+trafficList);
		List<Traffic> tl = evaluation(trafficList, 1,node);
		System.out.println(tl);
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink.WDMLinkList.get(i).setActive(true);
		}
		Survivance.setback(basicLink);
		return tl;
	}
	
	
	/**
	 * 节点单断循环
	 * 
	 * @param nodelist
	 */
	public static void nodelistEvaluation(List<CommonNode> nodelist) {
		nodeCutoff = 0;
		nodeKeep = 0;
		nodeDown = 0;
		Evaluation.refresh();
		Suggest.isKanghui=true;//2017.10.25
		for (int i = 0; i < nodelist.size(); ++i) {
			CommonNode node = nodelist.get(i);
			// this.nodeEvaluation(node,isSRLG);
			List<Traffic> tralist = Evaluation.nodeEvaluation(node);
			Evaluation.clearRsmRoute(tralist);
			
		}
//        System.out.println("输出测试业务值"+ceshi);
//        System.out.println("输出测试链路的值"+ceshilianlu);
//        System.out.println("输出没进来的业务"+ceshi0);
//        System.out.println("输出工作受影响的业务"+ceshi1);
//        System.out.println("输出保护受影响的业务"+ceshi2);
//        System.out.println("输出工作保护受影响的业务"+ceshi3);
//        System.out.println("输出测试sur"+Survivance.ceshisur);
//        ceshi=0;
//        ceshilianlu=0;
//        ceshi0=0;
//        ceshi1=0;
//        ceshi2=0;
//        ceshi3=0;
//        System.out.println(Traffic.getTraffic(33).getFaultType()+"    "+Traffic.getTraffic(33).getName());
        for(Traffic tra:Traffic.getTrafficList()) {
        	tra.setFaultType(0);
        }
	}


	/**
	 * 链路单断
	 * 
	 * @param link
	 * @return
	 */
	public static List<Traffic> linkEvaluation(FiberLink link) {
		//初始化每个节点的dynUsedOTU[1]为0
		for(CommonNode node : CommonNode.allNodeList) {
			int[] dynUsedOTU = node.getDynUsedOTU();
			dynUsedOTU[1]=0;
		}
		
		List<FiberLink> basicLink = new LinkedList<FiberLink>();
		System.out.println(link.getFiberRelatedList().size()!=0);
		if(link.getFiberRelatedList().size()!=0) {
			for(int i=0;i<link.getFiberRelatedList().size();i++) {
				for(int j=0;j<link.getFiberRelatedList().get(i).getSRLGFiberLinkList().size();j++) {
					basicLink.add(link.getFiberRelatedList().get(i).getSRLGFiberLinkList().get(j));
				}
			}
		}else {
			basicLink.add(link);
			}
		for(int i=0;i<basicLink.size();i++) {
		System.out.println("+++++输出受影响的所有链路"+basicLink.get(i));
		}
		//加入SRLG对链路单断的影响
		Survivance surv = new Survivance();
		List<Traffic> trafficList = surv.getAffectedTraffic(basicLink);
		for(int i=0;i<trafficList.size();i++) {
			System.out.println("+++++输出所有受影响的业务"+trafficList.get(i));
			Traffic tra = trafficList.get(i);
			tra.setResumeRoute(null);
			tra.setResumeRoutePro(null);
		}
		if (trafficList == null || trafficList.size() == 0)
			return null; // 10.20 新增获得空业务判断
		// System.out.println("受影响业务个数："+tl.size());
		Survivance.setFault(basicLink);
		List<Traffic> tl = evaluation(trafficList, 2,null);
		
		
		
		for (Traffic tr : tl) {
			tr.setFaultType(0);
		}
		Survivance.setback(basicLink);
		return tl;
	}

	/**
	 * 链路单断循环
	 * 
	 * @param allLinkList
	 */
	//8.6
	public static void listEvaluation(LinkedList<FiberLink> allLinkList) {
		linkCutoff = 0;
		linkKeep = 0;
		linkDown = 0;
		
		//每次单断循环的时候都初始化节点dynUsedOTU[]和链路的dynUsedLink[]属性
		for(CommonNode node : CommonNode.allNodeList) {
			int[] dynUsedOTU = node.getDynUsedOTU();
			dynUsedOTU[0]=dynUsedOTU[1]=0;
		}
		for(WDMLink link : WDMLink.WDMLinkList) {
			int[] dynUsedLink = link.getDynUsedLink();
			for(int i=0;i<dynUsedLink.length;i++) {
				dynUsedLink[i]=0;
			}
		}
		
		Evaluation.refresh();
		Suggest.isKanghui=true;//2017.10.25
		HashSet<String> set = new HashSet<>();
		LinkedList<FiberLink> norepeat = new LinkedList<>(); 
		for(FiberLink link : allLinkList ) {
			if(!set.contains(link.getName())) {
				set.add(link.getName());
				norepeat.add(link);
			}
		}
	
		for (int i = 0; i < norepeat.size(); ++i) {
			FiberLink fl = norepeat.get(i);
			// this.linkEvaluation(fl,isSRLG);
			List<Traffic> tralist = Evaluation.linkEvaluation(fl);
			if (tralist != null && tralist.size() != 0 ) {
				for (Traffic tra : tralist) {
					if (tra.getResumeRoute() != null) {
						Route.setDynNodeAndLinkParams(tra.getResumeRoute());
					}
				}
			}
			// 判断此次仿真用了多少个OTU，如果大于当前使用的OTU数dynOTU[0]，则更新
			for (CommonNode node : CommonNode.allNodeList) {
				node.countUpdown1(node);//更新此次仿真用的上下路模块
				int[] dynUsedOTU = node.getDynUsedOTU();
				if (dynUsedOTU[1] > dynUsedOTU[0]) {
					dynUsedOTU[0] = dynUsedOTU[1];
				}
			}
			
			Evaluation.clearRsmRoute(tralist);
		}

	}

    /**
     * 人工设置节点链路多断
     * 输入  linklist nodelist
     * 输出  业务
     */
	public static List<Traffic> multiEvaluation(List<FiberLink>linkList,List<CommonNode>nodeList){
		List<FiberLink> nlinklist=new LinkedList<>();
		Survivance surv=new Survivance();
		for(int i=0;i<nodeList.size();i++) {
			CommonNode node=nodeList.get(i);
			nlinklist=Survivance.getNodeEffectLink(node);
		}
		for(FiberLink link:nlinklist) {
			if(!linkList.contains(link)) {
			  linkList.add(link);
			}
		}
		List<Traffic>tra=new LinkedList<>();
		tra=surv.getAffectedTraffic(linkList);
		if(tra==null||tra.size()==0)
			return null;
		Survivance.setFault(linkList);
		List<Traffic> tl=evaluation(tra,3,null);
		Survivance.setback(linkList);
		return tl;
	}
	
	
	/**
	 * SRLG单断
	 * 输入 Srlg group
	 * 输出  业务
	 */
    public static List<Traffic> SRLGEvaluation(LinkRGroup srlg)
    {
    	Survivance surv = new Survivance();
    	List<FiberLink> fiberlinklist=srlg.getSRLGFiberLinkList();
    	List<Traffic> trafficList=surv.getAffectedTraffic(fiberlinklist);
		if(trafficList==null||trafficList.size()==0)
			return null;//空业务判断
		Survivance.setFault(fiberlinklist);
        List<Traffic>tl=evaluation(trafficList,4,null);
        Survivance.setback(fiberlinklist);
        return tl;	
    }


    /**
     * SRLG单断循环
     * 输入 SRLG group List
     * 输出 业务
     */
    public static void SRLGListEvaluation(List<LinkRGroup>srlglist){
    	scutoff=0;
    	skeep=0;
    	sdown=0;
    	Evaluation.refresh();
    	for (int i=0;i<srlglist.size();i++) {
    		LinkRGroup srlg=srlglist.get(i);
    		List<Traffic>tralist=Evaluation.SRLGEvaluation(srlg);
    		Evaluation.clearRsmRoute(tralist);
    	}	
    }
	

	/**
	 * 过程：释放、重新计算、统计、恢复受影响业务 核心评估算法
	 * 统计
	 * @param trafficList
	 * @param flag 统计的类型。1=节点单断，2=链路单断，3=人工设置多断，4=SRLG单断
	 * @return
	 */
	public static List<Traffic> evaluation(List<Traffic> trafficList, int flag, CommonNode node) {
		int linkcutoff = 0;
		int linkdown = 0;
		int linkkeep = 0;
		int cutoffnum=0;
		int downnum=0;
		int keepnum=0;
		int sum=0;
//		Evaluation.refresh();//单断的时候也需要清除受影响次数 12.1
//		RscRelease.rscRelease(trafficList);// 释放资源

		Survivance.reCompute(trafficList,node);// 重新计算(需要考虑多种恢复策略)

		// assert FiberLink.allFiberLinkList.size() == 35 : "链路改变了";// 10.27

		// 统计故障恢复情况，中断、降级、保持的次数
		for (int j = 0; j < trafficList.size(); ++j) {
			Traffic tra = trafficList.get(j);
			switch (tra.getProtectLevel()) {
			case PERMANENT11:
				if (tra.getResumeRoute() == null || tra.getResumeRoute().getWDMLinkList().size() == 0) {// 没有恢复路由,即中断
					linkcutoff++;
					cutoffnum++;
					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getResumeRoutePro() == null || tra.getResumeRoutePro().getWDMLinkList().size() == 0)) {// 有恢复路由无恢复保护，即降级
					linkdown++;
					downnum++;
//					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getResumeRoutePro() != null && tra.getResumeRoutePro().getWDMLinkList().size() != 0)) {// 有恢复有恢复保护,即保持
					linkkeep++;
					keepnum++;
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				}
				break;
			case NORMAL11:
				if (tra.getResumeRoute() == null || tra.getResumeRoute().getWDMLinkList().size() == 0) {// 故障
					linkcutoff++;
					cutoffnum++;
					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getResumeRoutePro() == null || tra.getResumeRoutePro().getWDMLinkList().size() == 0)) {// 1+1只有保持
					linkkeep++;
					keepnum++;
//					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getResumeRoutePro() != null && tra.getResumeRoutePro().getWDMLinkList().size() != 0)) {// 保持
					linkkeep++;
					keepnum++;
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				}
				break;
			case RESTORATION:
				if (tra.getResumeRoute() == null || tra.getResumeRoute().getWDMLinkList().size() == 0) {// 故障
					linkcutoff++;
					cutoffnum++;
					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)) {// 保持
					linkkeep++;
					keepnum++;
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				}
				break;
			case NONPROTECT:
				if (tra.getResumeRoute() == null || tra.getResumeRoute().getWDMLinkList().size() == 0) {// 中断
					linkcutoff++;
					cutoffnum++;
					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				} else if (tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0) {// 保持
					linkkeep++;
					keepnum++;
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				}
				break;
			case PROTECTandRESTORATION:// 保护+恢复
				if (tra.getResumeRoute() == null || tra.getResumeRoute().getWDMLinkList().size() == 0) {// 没有恢复路由,即中断
					linkcutoff++;
					cutoffnum++;
					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getPreRoute() == null || tra.getPreRoute().getWDMLinkList().size() == 0)) {// 有恢复路由无恢复保护，即降级
					linkkeep++;
					keepnum++;
//					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getPreRoute() != null && tra.getPreRoute().getWDMLinkList().size() != 0)) {// 有恢复有恢复保护,即保持
					linkkeep++;
					keepnum++;
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				}
				// tra.setProtectLevel(TrafficLevel.RESTORATION);//保护级别降级
				break;
			default:
				break;
			}// end switch

			switch (flag) {
			case 1:// 节点单断用百分比,循环用次数，
				nodeCutoff = nodeCutoff+cutoffnum;//还要再考虑一下
				nodeKeep = nodeKeep+keepnum;
				nodeDown = nodeDown+downnum;

				if (sum != 0) {
					nCutoffPercent = (float) linkcutoff / sum;
					nKeepPercent = (float) linkkeep / sum;
					nDownPercent = (float) linkdown / sum;
//					nUneffPercent = 1 - nCutoffPercent - nKeepPercent - nDownPercent;
				}
				break;
			case 2:// 链路单断用百分比,循环用次数，
				linkCutoff = linkCutoff + cutoffnum;
				linkKeep = linkKeep + keepnum;
				linkDown = linkDown + downnum;

				if (tra.getEffectNum() != 0) {
					cutoffPercent = (float) linkcutoff / sum;
					keepPercent = (float) linkkeep / sum;
					downPercent = (float) linkdown / sum;
//					uneffPercent = 1 - cutoffPercent - keepPercent - downPercent;
				}
				break;
			case 3:// 人工设置多断只有百分比，不循环没有次数

				if (tra.getEffectNum() != 0) {
					multiCutoffPer = (float) linkcutoff / sum;
					multiKeepPer = (float) linkkeep / sum;
					multiDownPer = (float) linkdown / sum;
//					multiUneffPer = 1 - multiCutoffPer - multiKeepPer - multiDownPer;
				}
				break;
			
			case 4://SRLG单断用百分比，循环用次数
				    scutoff=scutoff+cutoffnum;
				    skeep=skeep+keepnum;
				    sdown=sdown+downnum;
				if(trasum!=0) {
				    scutoffpercent=(float)linkcutoff/sum;
				    skeeppercent=(float)linkkeep/sum;
				    sdownpercent=(float)linkdown/sum;
//				    suneffpercent=1-scutoffpercent-skeeppercent-sdownpercent;
				}				
		
			}// end switch
			cutoffnum=0;
			keepnum=0;
			downnum=0;
			// Survivance.setback(basicLink);
		} // end trafficList for
//		RscRestore.rscRestore(trafficList);// 恢复仿真以前状态 11.6
		//2018.10.15 记录此次仿真使用的波长转换及
        RouteAlloc.releaseRouteForSim1();
		return trafficList;
	}
	/**
	 * 
	 * @param tralist
	 *            用于每次故障仿真后清空恢复路由
	 */
	public static void clearRsmRoute(List<Traffic> tralist) {
		if (tralist == null)
			return;
		for (int i = 0; i < tralist.size(); ++i) {
			Traffic tra = tralist.get(i);
			if (tra.getResumeRoute() != null)
				tra.setResumeRoute(null);
			if (tra.getResumeRoutePro() != null)
				tra.setResumeRoutePro(null);
		}
	}// end clearRsmRoute method

	/**
	 * (刷新)充值业务的受影响/失败次数
	 */
	public static void refresh() {
		for (int i = 0; i < Traffic.trafficList.size(); ++i) {
			Traffic tra = Traffic.trafficList.get(i);
			tra.setFailNum(0);
			tra.setEffectNum(0);
		}
	}
}
