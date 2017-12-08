package survivance;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import algorithm.Suggest;
import data.CommonNode;
import data.Port;
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
	

	/**
	 * 节点单断
	 * 
	 * @param node
	 * @return 受影响的业务
	 */
	public static List<Traffic> nodeEvaluation(CommonNode node) {
		Survivance surv = new Survivance();
		List<FiberLink> basicLink = Survivance.getNodeEffectLink(node);
		Survivance.setFault(basicLink);// 设置故障状态
		List<Traffic> trafficList = surv.getAffectedTraffic(basicLink);
		List<Traffic> tl = evaluation(trafficList, 1);
		Survivance.setback(basicLink);
		return tl;
	}

//	/**
//	 * 节点单断2次
//	 * 
//	 * @param node
//	 * @param times
//	 *            单断次数
//	 * @return 受影响的业务
//	 */
//	public static List<Traffic> nodeEvaluation(CommonNode node, int times,int suggset) {
//		Survivance surv = new Survivance();
//		List<FiberLink> basicLink = Survivance.getNodeEffectLink(node);
//		Survivance.setFault(basicLink);// 设置故障状态
//		List<Traffic> trafficList = surv.getAffectedTraffic(basicLink);
//		List<Traffic> tl = evaluation(trafficList, 3, 1);// 1次单断
//
//		// 2次故障部分
//		for (Traffic tra : Traffic.trafficList) {// 更新工作路由和保护路由
//			if (tra.getResumeRoute() != null)
//				tra.setWorkRoute(tra.getResumeRoute());
//			if (tra.getResumeRoutePro() != null)
//				tra.setProtectRoute(tra.getResumeRoutePro());
//		}
//		List<Traffic> trafficList2 = surv.getAffectedTraffic(basicLink);
//		evaluation(trafficList, 3, 2);// 2次单断
//
//		Survivance.setback(basicLink);
//		return tl;
//	}

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
			
			
			
			if(tralist!=null&&tralist.size()!=0) {
				for(Traffic tra:tralist) {
					Evaluation.PutOutTraffic.add(tra);
				}
			}
		
			NodeDataBase jd=new NodeDataBase();
			jd.OutPutNodeDan(node);
			
			PutOutTraffic.clear();
			//******调用Traffic的时候调用这个PutOutTraffic,这个是每次循环受影响的业务*******//
			//输出的地方我这里能提供的就是恢复路由，在表的第5列，调用的方法是traffic.getREsumeRoute().getWDMLinkList().get(n).getName()//
			Evaluation.clearRsmRoute(tralist);
		}
		JOptionPane.showMessageDialog(null, "数据已导出");
		TrafficDatabase.index=0;
		Suggest.isKanghui=false;//2017.10.25
	
		
		// System.out.println("循环节点故障业务中断次数："+nodecutoff);
		// System.out.println("循环节点故障业务保持次数："+nodekeep);
		// System.out.println("循环节点故障业务降级次数："+nodedown);
	}

//	/**
//	 * 节点多断
//	 * 
//	 * @param nodeList
//	 * @return
//	 */
//	public static List<Traffic> multiNodeEva(List<CommonNode> nodeList) {
//		Survivance surv = new Survivance();
//		List<FiberLink> allEffectLinik = new LinkedList<FiberLink>();// 存放所有受影响的链路
//		for (int i = 0; i < nodeList.size(); ++i) {
//			CommonNode node = nodeList.get(i);
//			List<FiberLink> nodeEffectLink = Survivance.getNodeEffectLink(node);
//			for (int k = 0; k < nodeEffectLink.size(); ++k) {
//				FiberLink fl = nodeEffectLink.get(k);
//				if (!allEffectLinik.contains(fl))
//					allEffectLinik.add(fl);
//			} // end for
//				// fiberlinklist.addAll(surv.getNodeFiberlink(node));
//		} // end for
//		Survivance.setFault(allEffectLinik);// 设置故障状态
//		List<Traffic> trafficList = surv.getAffectedTraffic(allEffectLinik);
//		List<Traffic> tl = evaluation(trafficList, 4);
//		Survivance.setback(allEffectLinik);
//		return tl;
//	}

//	/**
//	 * 节点双断循环
//	 * 
//	 * @param allNodeList
//	 */
//	public static void dNodeListEvaluation(List<CommonNode> allNodeList,int suggset) {
//		// TODO Auto-generated method stub
//		dNodeCutoff = 0;
//		dNodeKeep = 0;
//		dNodeDown = 0;
//		Evaluation.refresh();
//		Suggest.isKanghui=true;//2017.10.25
//		List<CommonNode> nList = new LinkedList<CommonNode>();
//		for (int i = 0; i < allNodeList.size(); ++i) {// 任选一个节点和所有节点两两配对
//			CommonNode node1 = allNodeList.get(i);
//			nList.add(node1);
//			for (int j = i + 1; j < allNodeList.size(); j++) {
//				CommonNode node2 = allNodeList.get(j);
//				nList.add(node2);
//				List<Traffic> tralist = Evaluation.multiNodeEva(nList,suggset);
//				Evaluation.clearRsmRoute(tralist);
//				nList.remove(1);
//			} // end for
//			nList.remove(0);
//		}
//		Suggest.isKanghui=false;//2017.10.25
//		// System.out.println("循环节点故障业务中断次数："+nodecutoff);
//		// System.out.println("循环节点故障业务保持次数："+nodekeep);
//		// System.out.println("循环节点故障业务降级次数："+nodedown);
//	}

	/**
	 * 链路单断
	 * 
	 * @param link
	 * @return
	 */
	public static List<Traffic> linkEvaluation(FiberLink link) {
		List<FiberLink> basicLink = new LinkedList<FiberLink>();
		basicLink.add(link);
		Survivance surv = new Survivance();
		List<Traffic> trafficList = surv.getAffectedTraffic(basicLink);
		if (trafficList == null || trafficList.size() == 0)
			return null; // 10.20 新增获得空业务判断
		// System.out.println("受影响业务个数："+tl.size());
		Survivance.setFault(basicLink);
		List<Traffic> tl = evaluation(trafficList, 2);
		Survivance.setback(basicLink);
		return tl;
	}

	/**
	 * 链路单断循环
	 * 
	 * @param allLinkList
	 */
	public static void listEvaluation(LinkedList<FiberLink> allLinkList) {
		linkCutoff = 0;
		linkKeep = 0;
		linkDown = 0;
		Evaluation.refresh();
		Suggest.isKanghui=true;//2017.10.25
	
		for (int i = 0; i < allLinkList.size(); ++i) {

			FiberLink fl = allLinkList.get(i);
			// this.linkEvaluation(fl,isSRLG);
			List<Traffic> tralist = Evaluation.linkEvaluation(fl);
			if(tralist!=null&&tralist.size()!=0) {
				for(Traffic tra:tralist) {
					Evaluation.PutOutTraffic.add(tra);
				}
			}
		
			TrafficDatabase lldd=new TrafficDatabase();
			lldd.OutPutRoute(fl);
			
			PutOutTraffic.clear();
			//******调用Traffic的时候调用这个PutOutTraffic,这个是每次循环受影响的业务*******//
			//输出的地方我这里能提供的就是恢复路由，在表的第5列，调用的方法是traffic.getREsumeRoute().getWDMLinkList().get(n).getName()//
			Evaluation.clearRsmRoute(tralist);
		}
		JOptionPane.showMessageDialog(null, "数据已导出");
		TrafficDatabase.index=0;
		Suggest.isKanghui=false;//2017.10.25
//		System.out.println("循环链路故障业务中断次数：" + linkCutoff);
//		System.out.println("循环链路故障业务保持次数：" + linkKeep);
//		System.out.println("循环链路故障业务降级次数：" + linkDown);
	}

//	/**
//	 * 链路多断
//	 * 
//	 * @param link
//	 * @return
//	 */
//	public static List<Traffic> multiLinkEva(List<FiberLink> linkList) {
//		Survivance surv = new Survivance();
//		List<Traffic> trafficList = surv.getAffectedTraffic(linkList);
//		if (trafficList == null || trafficList.size() == 0)
//			return null; // 10.20 新增获得空业务判断
//		// System.out.println("受影响业务个数："+tl.size());
//		Survivance.setFault(linkList);
//		List<Traffic> tl = evaluation(trafficList, 2);
//		Survivance.setback(linkList);
//		return tl;
//	}	
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
		List<Traffic> tl=evaluation(tra,3);
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
        List<Traffic>tl=evaluation(trafficList,4);
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
	

//	/**
//	 * 链路双断循环
//	 * 
//	 * @param allLinkList
//	 */
//	public static void dLinkListEvaluation(LinkedList<FiberLink> allLinkList,int suggset) {
//		dLinkCutoff = 0;
//		dLinkKeep = 0;
//		dLinkDown = 0;
//		Evaluation.refresh();
//		Suggest.isKanghui=true;//2017.10.25
//		List<FiberLink> lList = new LinkedList<FiberLink>();
//		for (int i = 0; i < allLinkList.size(); ++i) {
//			FiberLink link1 = allLinkList.get(i);
//			lList.add(link1);
//			for (int j = i + 1; j < allLinkList.size(); j++) {
//				FiberLink link2 = allLinkList.get(j);
//				lList.add(link2);
//				List<Traffic> tralist = Evaluation.multiLinkEva(lList,suggset);
//				Evaluation.clearRsmRoute(tralist);
//				lList.remove(1);
//			} // end for
//			lList.remove(0);
//		}
//		Suggest.isKanghui=false;//2017.10.25
//		// System.out.println("循环节点故障业务中断次数："+nodecutoff);
//		// System.out.println("循环节点故障业务保持次数："+nodekeep);
//		// System.out.println("循环节点故障业务降级次数："+nodedown);
//	}

	/**
	 * 过程：释放、重新计算、统计、恢复受影响业务 核心评估算法
	 * 统计
	 * @param trafficList
	 * @param flag 统计的类型。1=节点单断，2=链路单断，3=人工设置多断，4=SRLG单断
	 * @return
	 */
	public static List<Traffic> evaluation(List<Traffic> trafficList, int flag) {
		int linkcutoff = 0;
		int linkdown = 0;
		int linkkeep = 0;

		RscRelease.rscRelease(trafficList);// 释放资源

		Survivance.reCompute(trafficList);// 重新计算(需要考虑多种恢复策略)

		// assert FiberLink.allFiberLinkList.size() == 35 : "链路改变了";// 10.27

		// 统计故障恢复情况，中断、降级、保持的次数
		for (int j = 0; j < trafficList.size(); ++j) {
			Traffic tra = trafficList.get(j);
			switch (tra.getProtectLevel()) {
			case PERMANENT11:
				if (tra.getResumeRoute() == null || tra.getResumeRoute().getWDMLinkList().size() == 0) {// 没有恢复路由,即中断
					linkcutoff++;
					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getResumeRoutePro() == null || tra.getResumeRoutePro().getWDMLinkList().size() == 0)) {// 有恢复路由无恢复保护，即降级
					linkdown++;
//					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getResumeRoutePro() != null && tra.getResumeRoutePro().getWDMLinkList().size() != 0)) {// 有恢复有恢复保护,即保持
					linkkeep++;
					tra.setEffectNum(tra.getEffectNum() + 1);
				}
				break;
			case NORMAL11:
				if (tra.getResumeRoute() == null || tra.getResumeRoute().getWDMLinkList().size() == 0) {// 故障
					linkcutoff++;
					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getResumeRoutePro() == null || tra.getResumeRoutePro().getWDMLinkList().size() == 0)) {// 降级
					linkdown++;
//					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getResumeRoutePro() != null && tra.getResumeRoutePro().getWDMLinkList().size() != 0)) {// 保持
					linkkeep++;
					tra.setEffectNum(tra.getEffectNum() + 1);
				}
				break;
			case RESTORATION:
				if (tra.getResumeRoute() == null || tra.getResumeRoute().getWDMLinkList().size() == 0) {// 没有恢复路由,即中断
					linkcutoff++;
					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getPreRoute() == null || tra.getPreRoute().getWDMLinkList().size() == 0)) {// 有恢复路由无恢复保护，即降级
					linkdown++;
//					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getPreRoute() != null && tra.getPreRoute().getWDMLinkList().size() != 0)) {// 有恢复有恢复保护,即保持
					linkkeep++;
					tra.setEffectNum(tra.getEffectNum() + 1);
				}
				break;
			case NONPROTECT:
				if (tra.getResumeRoute() == null || tra.getResumeRoute().getWDMLinkList().size() == 0) {// 中断
					linkcutoff++;
					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
				} else if (tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0) {// 保持
					linkkeep++;
					tra.setEffectNum(tra.getEffectNum() + 1);
				}
				break;
			case PROTECTandRESTORATION:// 保护+恢复
				if (tra.getResumeRoute() == null || tra.getResumeRoute().getWDMLinkList().size() == 0) {// 没有恢复路由,即中断
					linkcutoff++;
					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getPreRoute() == null || tra.getPreRoute().getWDMLinkList().size() == 0)) {// 有恢复路由无恢复保护，即降级
					linkdown++;
//					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getPreRoute() != null && tra.getPreRoute().getWDMLinkList().size() != 0)) {// 有恢复有恢复保护,即保持
					linkkeep++;
					tra.setEffectNum(tra.getEffectNum() + 1);
				}
				// tra.setProtectLevel(TrafficLevel.RESTORATION);//保护级别降级
				break;
//			case PresetRESTORATION:// 专享预置恢复
//				if (tra.getResumeRoute() == null || tra.getResumeRoute().getWDMLinkList().size() == 0) {// 故障
//					linkcutoff++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
//						&& (tra.getResumeRoutePro() == null || tra.getResumeRoutePro().getWDMLinkList().size() == 0)) {// 降级
//					linkdown++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
//						&& (tra.getResumeRoutePro() != null && tra.getResumeRoutePro().getWDMLinkList().size() != 0)) {// 保持
//					linkkeep++;
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				}
//				break;
			default:
				break;
			}// end switch

			switch (flag) {
			case 1:// 节点单断用百分比,循环用次数，
				nodeCutoff = nodeCutoff + linkcutoff;
				nodeKeep = nodeKeep + linkkeep;
				nodeDown = nodeDown + linkdown;

				if (trasum != 0) {
					nCutoffPercent = (float) linkcutoff / trasum;
					nKeepPercent = (float) linkkeep / trasum;
					nDownPercent = (float) linkdown / trasum;
					nUneffPercent = 1 - nCutoffPercent - nKeepPercent - nDownPercent;
				}
				break;
			case 2:// 链路单断用百分比,循环用次数，
				linkCutoff = linkCutoff + linkcutoff;
				linkKeep = linkKeep + linkkeep;
				linkDown = linkDown + linkdown;

				if (trasum != 0) {
					cutoffPercent = (float) linkcutoff / trasum;
					keepPercent = (float) linkkeep / trasum;
					downPercent = (float) linkdown / trasum;
					uneffPercent = 1 - cutoffPercent - keepPercent - downPercent;
				}
				break;
			case 3:// 人工设置多断只有百分比，不循环没有次数
//				dLinkCutoff = dLinkCutoff + linkcutoff;
//				dLinkKeep = dLinkKeep + linkkeep;
//				dLinkDown = dLinkDown + linkdown;

				if (trasum != 0) {
					multiCutoffPer = (float) linkcutoff / trasum;
					multiKeepPer = (float) linkkeep / trasum;
					multiDownPer = (float) linkdown / trasum;
					multiUneffPer = 1 - multiCutoffPer - multiKeepPer - multiDownPer;
				}
				break;
			
			case 4://SRLG单断用百分比，循环用次数
				    scutoff=scutoff+linkcutoff;
				    skeep=skeep+linkkeep;
				    sdown=sdown+linkdown;
				if(trasum!=0) {
				    scutoffpercent=(float)linkcutoff/trasum;
				    skeeppercent=(float)linkkeep/trasum;
				    sdownpercent=(float)linkdown/trasum;
				    suneffpercent=1-scutoffpercent-skeeppercent-sdownpercent;
				}				
				
			}// end switch
				// Survivance.setback(basicLink);
		} // end trafficList for

		RscRestore.rscRestore(trafficList);// 恢复仿真以前状态 11.6

		return trafficList;
	}

	/**
	 * 用于同一业务二次故障仿真 过程：释放、重新计算、统计、恢复受影响业务 核心评估算法
	 *
	 * 
	 * @param trafficList
	 * @param flag
	 *            统计的类型。1=链路单断，2=链路双断，3=节点单断，4=节点双断
	 * @param times
	 *            故障次数
	 * @return
	 */
//	public static List<Traffic> evaluation(List<Traffic> trafficList, int flag, int times) {
//		int linkcutoff = 0;
//		int linkdown = 0;
//		int linkkeep = 0;
//
//		RscRelease.rscRelease(trafficList);// 释放资源
//
//		Survivance.reCompute(trafficList);// 重新计算(需要考虑多种恢复策略)
//
//		// assert FiberLink.allFiberLinkList.size() == 35 : "链路改变了";// 10.27
//
//		// 统计故障恢复情况，中断、降级、保持的次数
//		for (int j = 0; j < trafficList.size(); ++j) {
//			Traffic tra = trafficList.get(j);
//			switch (tra.getProtectLevel()) {
//			case PERMANENT11:
//				if (tra.getResumeRoute() == null || tra.getResumeRoute().getFiberLinkList().size() == 0) {// 没有恢复路由,即中断
//					linkcutoff++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getFiberLinkList().size() != 0)
//						&& (tra.getResumeRoutePro() == null || tra.getResumeRoutePro().getFiberLinkList().size() == 0)) {// 有恢复路由无恢复保护，即降级
//					linkdown++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getFiberLinkList().size() != 0)
//						&& (tra.getResumeRoutePro() != null && tra.getResumeRoutePro().getFiberLinkList().size() != 0)) {// 有恢复有恢复保护,即保持
//					linkkeep++;
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				}
//				break;
//			case NORMAL11:
//				if (tra.getResumeRoute() == null || tra.getResumeRoute().getFiberLinkList().size() == 0) {// 故障
//					linkcutoff++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getFiberLinkList().size() != 0)
//						&& (tra.getResumeRoutePro() == null || tra.getResumeRoutePro().getFiberLinkList().size() == 0)) {// 降级
//					linkdown++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getFiberLinkList().size() != 0)
//						&& (tra.getResumeRoutePro() != null && tra.getResumeRoutePro().getFiberLinkList().size() != 0)) {// 保持
//					linkkeep++;
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				}
//				// tra.setProtectLevel(TrafficLevel.NONPROTECT);//保护级别降级,不在这里设置
//				break;
//			case RESTORATION:
//				if (tra.getResumeRoute() == null || tra.getResumeRoute().getFiberLinkList().size() == 0) {// 中断
//					linkcutoff++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if (tra.getResumeRoute() != null && tra.getResumeRoute().getFiberLinkList().size() != 0) {// 保持
//					linkkeep++;
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				}
//				break;
//			case NONPROTECT:
//				if (tra.getResumeRoute() == null || tra.getResumeRoute().getFiberLinkList().size() == 0) {// 中断
//					linkcutoff++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if (tra.getResumeRoute() != null && tra.getResumeRoute().getFiberLinkList().size() != 0) {// 保持
//					linkkeep++;
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				}
//				break;
//			case PROTECTandRESTORATION:// 保护+恢复
//				if (tra.getResumeRoute() == null || tra.getResumeRoute().getFiberLinkList().size() == 0) {// 没有恢复路由,即中断
//					linkcutoff++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getFiberLinkList().size() != 0)
//						&& (tra.getResumeRoutePro() == null || tra.getResumeRoutePro().getFiberLinkList().size() == 0)) {// 有恢复路由无恢复保护，即降级
//					linkdown++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getFiberLinkList().size() != 0)
//						&& (tra.getResumeRoutePro() != null && tra.getResumeRoutePro().getFiberLinkList().size() != 0)) {// 有恢复有恢复保护,即保持
//					linkkeep++;
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				}
//				// tra.setProtectLevel(TrafficLevel.RESTORATION);//保护级别降级
//				break;
//			case PresetRESTORATION:// 专享预置恢复
//				if (tra.getResumeRoute() == null || tra.getResumeRoute().getFiberLinkList().size() == 0) {// 没有恢复路由,即中断
//					linkcutoff++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getFiberLinkList().size() != 0)
//						&& (tra.getResumeRoutePro() == null || tra.getResumeRoutePro().getFiberLinkList().size() == 0)) {// 有恢复路由无恢复保护，即降级
//					linkdown++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getFiberLinkList().size() != 0)
//						&& (tra.getResumeRoutePro() != null && tra.getResumeRoutePro().getFiberLinkList().size() != 0)) {// 有恢复有恢复保护,即保持
//					linkkeep++;
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				}
//				break;
//			}// end switch
//
//			switch (flag) {
//			case 1:// 链路单断用百分比,循环用次数，
//				linkCutoff = linkCutoff + linkcutoff;
//				linkKeep = linkKeep + linkkeep;
//				linkDown = linkDown + linkdown;
//
//				if (trasum != 0) {
//					cutoffPercent = (float) linkcutoff / trasum;
//					keepPercent = (float) linkkeep / trasum;
//					downPercent = (float) linkdown / trasum;
//					uneffPercent = 1 - cutoffPercent - keepPercent - downPercent;
//				}
//				break;
//			case 2:// 链路双断用百分比,循环用次数，
//				dLinkCutoff = dLinkCutoff + linkcutoff;
//				dLinkKeep = dLinkKeep + linkkeep;
//				dLinkDown = dLinkDown + linkdown;
//
//				if (trasum != 0) {
//					multiLinkCutoffPer = (float) linkcutoff / trasum;
//					multiLinkKeepPer = (float) linkkeep / trasum;
//					multiLinkDownPer = (float) linkdown / trasum;
//					multiLinkUneffPer = 1 - cutoffPercent - keepPercent - downPercent;
//				}
//				break;
//			case 3:// 节点单断用百分比,循环用次数，
//				nodeCutoff = nodeCutoff + linkcutoff;
//				nodeKeep = nodeKeep + linkkeep;
//				nodeDown = nodeDown + linkdown;
//
//				if (trasum != 0) {
//					nCutoffPercent = (float) linkcutoff / trasum;
//					nKeepPercent = (float) linkkeep / trasum;
//					nDownPercent = (float) linkdown / trasum;
//					nUneffPercent = 1 - nCutoffPercent - nKeepPercent - nDownPercent;
//				}
//				break;
//			case 4:// 节点双断用百分比,循环用次数，
//				dNodeCutoff = dNodeCutoff + linkcutoff;
//				dNodeKeep = dNodeKeep + linkkeep;
//				dNodeDown = dNodeDown + linkdown;
//
//				if (trasum != 0) {
//					multiNodeCutoffPer = (float) linkcutoff / trasum;
//					multiNodeKeepPer = (float) linkkeep / trasum;
//					multiNodeDownPer = (float) linkdown / trasum;
//					multiNodeUneffPer = 1 - multiNodeCutoffPer - multiNodeKeepPer - multiNodeDownPer;
//				}
//				break;
//			}// end switch
//				// Survivance.setback(basicLink);
//		} // end trafficList for
//
//		// RscRestore.rscRestore(trafficList);// 恢复仿真以前状态 11.6
//		RscRelease.rscRelease(trafficList);
//
//		return trafficList;
//	}

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
