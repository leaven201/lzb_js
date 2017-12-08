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
 * �����������
 * 
 * @author CC
 * @since 10.8
 */
public class Evaluation {
    public static List<Traffic>PutOutTraffic=new LinkedList<>();
	// ��ѭ�õ������������õ��ǰٷֱ�
    
    public static int linkCutoff = 0; // ��ѭ��·����ʱ�жϴ���
	public static int linkKeep = 0; // ��ѭ��·����ʱ���ִ���
	public static int linkDown = 0; // ��ѭ��·����ʱ��������

	public static int nodeCutoff = 0; // ��ѭ�ڵ�����ʱ�жϴ���  �ڵ㵥��
	public static int nodeKeep = 0; // ��ѭ�ڵ�����ʱ���ִ���    �ڵ㵥��
	public static int nodeDown = 0; // ��ѭ�ڵ�����ʱ��������   �ڵ㵥��

	public static int scutoff=0;       //��ѭSRLG����ʱ�жϴ���
	public static int skeep=0;         //��ѭSRLG����ʱ���ִ���
	public static int sdown=0;         //��ѭSRLG����ʱ��������
	
	public static float nCutoffPercent = 0; // ���ڵ�����ʱ�жϰٷֱ�
	public static float nKeepPercent = 0; // ���ڵ�����ʱ���ְٷֱ�
	public static float nDownPercent = 0; // ���ڵ�����ʱ�����ٷֱ�
	public static float nUneffPercent = 0; // ���ڵ�����ʱδ��Ӱ��ٷֱ�

	public static int trasum = Traffic.trafficList.size(); // ҵ������
	public static float cutoffPercent = 0; // ����ʱ�жϰٷֱ�
	public static float keepPercent = 0; // ����ʱ���ְٷֱ�
	public static float downPercent = 0; // ����ʱ�����ٷֱ�
	public static float uneffPercent = 0; // ����ʱδ��Ӱ��ٷֱ�
	
	public static float skeeppercent=0; //��SRLG����ʱ���ְٷֱ�
	public static float scutoffpercent=0;//��SRLG����ʱ�жϰٷֱ�
	public static float sdownpercent=0; //��SRLG����ʱ�����ٷֱ�
	public static float suneffpercent=0;//��SRLG����ʱ�����ٷֱ�

	public static int dLinkCutoff = 0; // ˫����ѭ��·����ʱ�жϴ���
	public static int dLinkKeep = 0; // ˫����ѭ��·����ʱ���ִ���
	public static int dLinkDown = 0; // ˫����ѭ��·����ʱ��������

	public static int dNodeCutoff = 0; // ˫�Ͻڵ���ѭ��·����ʱ�жϴ���
	public static int dNodeKeep = 0; // ˫�Ͻڵ���ѭ��·����ʱ���ִ���
	public static int dNodeDown = 0; // ˫�Ͻڵ���ѭ��·����ʱ��������

	public static float multiLinkCutoffPer = 0; // ����ʱ�жϰٷֱ�
	public static float multiLinkKeepPer = 0; // ����ʱ���ְٷֱ�
	public static float multiLinkDownPer = 0; // ����ʱ�����ٷֱ�
	public static float multiLinkUneffPer = 0; // ����ʱδ��Ӱ��ٷֱ�

	public static float multiNodeCutoffPer = 0; // ��ڵ�ʱ�жϰٷֱ�
	public static float multiNodeKeepPer = 0; // ��ڵ�ʱ���ְٷֱ�
	public static float multiNodeDownPer = 0; // ��ڵ�ʱ�����ٷֱ�
	public static float multiNodeUneffPer = 0; // ��ڵ�ʱδ��Ӱ��ٷֱ�
	
	public static float multiCutoffPer = 0; // �˹�ʱ�жϰٷֱ�
	public static float multiKeepPer = 0; // �˹�ʱ���ְٷֱ�
	public static float multiDownPer = 0; // �˹�ʱ�����ٷֱ�
	public static float multiUneffPer = 0; // �˹�ʱδ��Ӱ��ٷֱ�
	

	/**
	 * �ڵ㵥��
	 * 
	 * @param node
	 * @return ��Ӱ���ҵ��
	 */
	public static List<Traffic> nodeEvaluation(CommonNode node) {
		Survivance surv = new Survivance();
		List<FiberLink> basicLink = Survivance.getNodeEffectLink(node);
		Survivance.setFault(basicLink);// ���ù���״̬
		List<Traffic> trafficList = surv.getAffectedTraffic(basicLink);
		List<Traffic> tl = evaluation(trafficList, 1);
		Survivance.setback(basicLink);
		return tl;
	}

//	/**
//	 * �ڵ㵥��2��
//	 * 
//	 * @param node
//	 * @param times
//	 *            ���ϴ���
//	 * @return ��Ӱ���ҵ��
//	 */
//	public static List<Traffic> nodeEvaluation(CommonNode node, int times,int suggset) {
//		Survivance surv = new Survivance();
//		List<FiberLink> basicLink = Survivance.getNodeEffectLink(node);
//		Survivance.setFault(basicLink);// ���ù���״̬
//		List<Traffic> trafficList = surv.getAffectedTraffic(basicLink);
//		List<Traffic> tl = evaluation(trafficList, 3, 1);// 1�ε���
//
//		// 2�ι��ϲ���
//		for (Traffic tra : Traffic.trafficList) {// ���¹���·�ɺͱ���·��
//			if (tra.getResumeRoute() != null)
//				tra.setWorkRoute(tra.getResumeRoute());
//			if (tra.getResumeRoutePro() != null)
//				tra.setProtectRoute(tra.getResumeRoutePro());
//		}
//		List<Traffic> trafficList2 = surv.getAffectedTraffic(basicLink);
//		evaluation(trafficList, 3, 2);// 2�ε���
//
//		Survivance.setback(basicLink);
//		return tl;
//	}

	/**
	 * �ڵ㵥��ѭ��
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
			//******����Traffic��ʱ��������PutOutTraffic,�����ÿ��ѭ����Ӱ���ҵ��*******//
			//����ĵط����������ṩ�ľ��ǻָ�·�ɣ��ڱ�ĵ�5�У����õķ�����traffic.getREsumeRoute().getWDMLinkList().get(n).getName()//
			Evaluation.clearRsmRoute(tralist);
		}
		JOptionPane.showMessageDialog(null, "�����ѵ���");
		TrafficDatabase.index=0;
		Suggest.isKanghui=false;//2017.10.25
	
		
		// System.out.println("ѭ���ڵ����ҵ���жϴ�����"+nodecutoff);
		// System.out.println("ѭ���ڵ����ҵ�񱣳ִ�����"+nodekeep);
		// System.out.println("ѭ���ڵ����ҵ�񽵼�������"+nodedown);
	}

//	/**
//	 * �ڵ���
//	 * 
//	 * @param nodeList
//	 * @return
//	 */
//	public static List<Traffic> multiNodeEva(List<CommonNode> nodeList) {
//		Survivance surv = new Survivance();
//		List<FiberLink> allEffectLinik = new LinkedList<FiberLink>();// ���������Ӱ�����·
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
//		Survivance.setFault(allEffectLinik);// ���ù���״̬
//		List<Traffic> trafficList = surv.getAffectedTraffic(allEffectLinik);
//		List<Traffic> tl = evaluation(trafficList, 4);
//		Survivance.setback(allEffectLinik);
//		return tl;
//	}

//	/**
//	 * �ڵ�˫��ѭ��
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
//		for (int i = 0; i < allNodeList.size(); ++i) {// ��ѡһ���ڵ�����нڵ��������
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
//		// System.out.println("ѭ���ڵ����ҵ���жϴ�����"+nodecutoff);
//		// System.out.println("ѭ���ڵ����ҵ�񱣳ִ�����"+nodekeep);
//		// System.out.println("ѭ���ڵ����ҵ�񽵼�������"+nodedown);
//	}

	/**
	 * ��·����
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
			return null; // 10.20 ������ÿ�ҵ���ж�
		// System.out.println("��Ӱ��ҵ�������"+tl.size());
		Survivance.setFault(basicLink);
		List<Traffic> tl = evaluation(trafficList, 2);
		Survivance.setback(basicLink);
		return tl;
	}

	/**
	 * ��·����ѭ��
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
			//******����Traffic��ʱ��������PutOutTraffic,�����ÿ��ѭ����Ӱ���ҵ��*******//
			//����ĵط����������ṩ�ľ��ǻָ�·�ɣ��ڱ�ĵ�5�У����õķ�����traffic.getREsumeRoute().getWDMLinkList().get(n).getName()//
			Evaluation.clearRsmRoute(tralist);
		}
		JOptionPane.showMessageDialog(null, "�����ѵ���");
		TrafficDatabase.index=0;
		Suggest.isKanghui=false;//2017.10.25
//		System.out.println("ѭ����·����ҵ���жϴ�����" + linkCutoff);
//		System.out.println("ѭ����·����ҵ�񱣳ִ�����" + linkKeep);
//		System.out.println("ѭ����·����ҵ�񽵼�������" + linkDown);
	}

//	/**
//	 * ��·���
//	 * 
//	 * @param link
//	 * @return
//	 */
//	public static List<Traffic> multiLinkEva(List<FiberLink> linkList) {
//		Survivance surv = new Survivance();
//		List<Traffic> trafficList = surv.getAffectedTraffic(linkList);
//		if (trafficList == null || trafficList.size() == 0)
//			return null; // 10.20 ������ÿ�ҵ���ж�
//		// System.out.println("��Ӱ��ҵ�������"+tl.size());
//		Survivance.setFault(linkList);
//		List<Traffic> tl = evaluation(trafficList, 2);
//		Survivance.setback(linkList);
//		return tl;
//	}	
    /**
     * �˹����ýڵ���·���
     * ����  linklist nodelist
     * ���  ҵ��
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
	 * SRLG����
	 * ���� Srlg group
	 * ���  ҵ��
	 */
    public static List<Traffic> SRLGEvaluation(LinkRGroup srlg)
    {
    	Survivance surv = new Survivance();
    	List<FiberLink> fiberlinklist=srlg.getSRLGFiberLinkList();
    	List<Traffic> trafficList=surv.getAffectedTraffic(fiberlinklist);
		if(trafficList==null||trafficList.size()==0)
			return null;//��ҵ���ж�
		Survivance.setFault(fiberlinklist);
        List<Traffic>tl=evaluation(trafficList,4);
        Survivance.setback(fiberlinklist);
        return tl;	
    }


    /**
     * SRLG����ѭ��
     * ���� SRLG group List
     * ��� ҵ��
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
//	 * ��·˫��ѭ��
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
//		// System.out.println("ѭ���ڵ����ҵ���жϴ�����"+nodecutoff);
//		// System.out.println("ѭ���ڵ����ҵ�񱣳ִ�����"+nodekeep);
//		// System.out.println("ѭ���ڵ����ҵ�񽵼�������"+nodedown);
//	}

	/**
	 * ���̣��ͷš����¼��㡢ͳ�ơ��ָ���Ӱ��ҵ�� ���������㷨
	 * ͳ��
	 * @param trafficList
	 * @param flag ͳ�Ƶ����͡�1=�ڵ㵥�ϣ�2=��·���ϣ�3=�˹����ö�ϣ�4=SRLG����
	 * @return
	 */
	public static List<Traffic> evaluation(List<Traffic> trafficList, int flag) {
		int linkcutoff = 0;
		int linkdown = 0;
		int linkkeep = 0;

		RscRelease.rscRelease(trafficList);// �ͷ���Դ

		Survivance.reCompute(trafficList);// ���¼���(��Ҫ���Ƕ��ָֻ�����)

		// assert FiberLink.allFiberLinkList.size() == 35 : "��·�ı���";// 10.27

		// ͳ�ƹ��ϻָ�������жϡ����������ֵĴ���
		for (int j = 0; j < trafficList.size(); ++j) {
			Traffic tra = trafficList.get(j);
			switch (tra.getProtectLevel()) {
			case PERMANENT11:
				if (tra.getResumeRoute() == null || tra.getResumeRoute().getWDMLinkList().size() == 0) {// û�лָ�·��,���ж�
					linkcutoff++;
					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getResumeRoutePro() == null || tra.getResumeRoutePro().getWDMLinkList().size() == 0)) {// �лָ�·���޻ָ�������������
					linkdown++;
//					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getResumeRoutePro() != null && tra.getResumeRoutePro().getWDMLinkList().size() != 0)) {// �лָ��лָ�����,������
					linkkeep++;
					tra.setEffectNum(tra.getEffectNum() + 1);
				}
				break;
			case NORMAL11:
				if (tra.getResumeRoute() == null || tra.getResumeRoute().getWDMLinkList().size() == 0) {// ����
					linkcutoff++;
					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getResumeRoutePro() == null || tra.getResumeRoutePro().getWDMLinkList().size() == 0)) {// ����
					linkdown++;
//					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getResumeRoutePro() != null && tra.getResumeRoutePro().getWDMLinkList().size() != 0)) {// ����
					linkkeep++;
					tra.setEffectNum(tra.getEffectNum() + 1);
				}
				break;
			case RESTORATION:
				if (tra.getResumeRoute() == null || tra.getResumeRoute().getWDMLinkList().size() == 0) {// û�лָ�·��,���ж�
					linkcutoff++;
					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getPreRoute() == null || tra.getPreRoute().getWDMLinkList().size() == 0)) {// �лָ�·���޻ָ�������������
					linkdown++;
//					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getPreRoute() != null && tra.getPreRoute().getWDMLinkList().size() != 0)) {// �лָ��лָ�����,������
					linkkeep++;
					tra.setEffectNum(tra.getEffectNum() + 1);
				}
				break;
			case NONPROTECT:
				if (tra.getResumeRoute() == null || tra.getResumeRoute().getWDMLinkList().size() == 0) {// �ж�
					linkcutoff++;
					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
				} else if (tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0) {// ����
					linkkeep++;
					tra.setEffectNum(tra.getEffectNum() + 1);
				}
				break;
			case PROTECTandRESTORATION:// ����+�ָ�
				if (tra.getResumeRoute() == null || tra.getResumeRoute().getWDMLinkList().size() == 0) {// û�лָ�·��,���ж�
					linkcutoff++;
					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getPreRoute() == null || tra.getPreRoute().getWDMLinkList().size() == 0)) {// �лָ�·���޻ָ�������������
					linkdown++;
//					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getPreRoute() != null && tra.getPreRoute().getWDMLinkList().size() != 0)) {// �лָ��лָ�����,������
					linkkeep++;
					tra.setEffectNum(tra.getEffectNum() + 1);
				}
				// tra.setProtectLevel(TrafficLevel.RESTORATION);//�������𽵼�
				break;
//			case PresetRESTORATION:// ר��Ԥ�ûָ�
//				if (tra.getResumeRoute() == null || tra.getResumeRoute().getWDMLinkList().size() == 0) {// ����
//					linkcutoff++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
//						&& (tra.getResumeRoutePro() == null || tra.getResumeRoutePro().getWDMLinkList().size() == 0)) {// ����
//					linkdown++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
//						&& (tra.getResumeRoutePro() != null && tra.getResumeRoutePro().getWDMLinkList().size() != 0)) {// ����
//					linkkeep++;
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				}
//				break;
			default:
				break;
			}// end switch

			switch (flag) {
			case 1:// �ڵ㵥���ðٷֱ�,ѭ���ô�����
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
			case 2:// ��·�����ðٷֱ�,ѭ���ô�����
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
			case 3:// �˹����ö��ֻ�аٷֱȣ���ѭ��û�д���
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
			
			case 4://SRLG�����ðٷֱȣ�ѭ���ô���
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

		RscRestore.rscRestore(trafficList);// �ָ�������ǰ״̬ 11.6

		return trafficList;
	}

	/**
	 * ����ͬһҵ����ι��Ϸ��� ���̣��ͷš����¼��㡢ͳ�ơ��ָ���Ӱ��ҵ�� ���������㷨
	 *
	 * 
	 * @param trafficList
	 * @param flag
	 *            ͳ�Ƶ����͡�1=��·���ϣ�2=��·˫�ϣ�3=�ڵ㵥�ϣ�4=�ڵ�˫��
	 * @param times
	 *            ���ϴ���
	 * @return
	 */
//	public static List<Traffic> evaluation(List<Traffic> trafficList, int flag, int times) {
//		int linkcutoff = 0;
//		int linkdown = 0;
//		int linkkeep = 0;
//
//		RscRelease.rscRelease(trafficList);// �ͷ���Դ
//
//		Survivance.reCompute(trafficList);// ���¼���(��Ҫ���Ƕ��ָֻ�����)
//
//		// assert FiberLink.allFiberLinkList.size() == 35 : "��·�ı���";// 10.27
//
//		// ͳ�ƹ��ϻָ�������жϡ����������ֵĴ���
//		for (int j = 0; j < trafficList.size(); ++j) {
//			Traffic tra = trafficList.get(j);
//			switch (tra.getProtectLevel()) {
//			case PERMANENT11:
//				if (tra.getResumeRoute() == null || tra.getResumeRoute().getFiberLinkList().size() == 0) {// û�лָ�·��,���ж�
//					linkcutoff++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getFiberLinkList().size() != 0)
//						&& (tra.getResumeRoutePro() == null || tra.getResumeRoutePro().getFiberLinkList().size() == 0)) {// �лָ�·���޻ָ�������������
//					linkdown++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getFiberLinkList().size() != 0)
//						&& (tra.getResumeRoutePro() != null && tra.getResumeRoutePro().getFiberLinkList().size() != 0)) {// �лָ��лָ�����,������
//					linkkeep++;
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				}
//				break;
//			case NORMAL11:
//				if (tra.getResumeRoute() == null || tra.getResumeRoute().getFiberLinkList().size() == 0) {// ����
//					linkcutoff++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getFiberLinkList().size() != 0)
//						&& (tra.getResumeRoutePro() == null || tra.getResumeRoutePro().getFiberLinkList().size() == 0)) {// ����
//					linkdown++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getFiberLinkList().size() != 0)
//						&& (tra.getResumeRoutePro() != null && tra.getResumeRoutePro().getFiberLinkList().size() != 0)) {// ����
//					linkkeep++;
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				}
//				// tra.setProtectLevel(TrafficLevel.NONPROTECT);//�������𽵼�,������������
//				break;
//			case RESTORATION:
//				if (tra.getResumeRoute() == null || tra.getResumeRoute().getFiberLinkList().size() == 0) {// �ж�
//					linkcutoff++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if (tra.getResumeRoute() != null && tra.getResumeRoute().getFiberLinkList().size() != 0) {// ����
//					linkkeep++;
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				}
//				break;
//			case NONPROTECT:
//				if (tra.getResumeRoute() == null || tra.getResumeRoute().getFiberLinkList().size() == 0) {// �ж�
//					linkcutoff++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if (tra.getResumeRoute() != null && tra.getResumeRoute().getFiberLinkList().size() != 0) {// ����
//					linkkeep++;
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				}
//				break;
//			case PROTECTandRESTORATION:// ����+�ָ�
//				if (tra.getResumeRoute() == null || tra.getResumeRoute().getFiberLinkList().size() == 0) {// û�лָ�·��,���ж�
//					linkcutoff++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getFiberLinkList().size() != 0)
//						&& (tra.getResumeRoutePro() == null || tra.getResumeRoutePro().getFiberLinkList().size() == 0)) {// �лָ�·���޻ָ�������������
//					linkdown++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getFiberLinkList().size() != 0)
//						&& (tra.getResumeRoutePro() != null && tra.getResumeRoutePro().getFiberLinkList().size() != 0)) {// �лָ��лָ�����,������
//					linkkeep++;
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				}
//				// tra.setProtectLevel(TrafficLevel.RESTORATION);//�������𽵼�
//				break;
//			case PresetRESTORATION:// ר��Ԥ�ûָ�
//				if (tra.getResumeRoute() == null || tra.getResumeRoute().getFiberLinkList().size() == 0) {// û�лָ�·��,���ж�
//					linkcutoff++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getFiberLinkList().size() != 0)
//						&& (tra.getResumeRoutePro() == null || tra.getResumeRoutePro().getFiberLinkList().size() == 0)) {// �лָ�·���޻ָ�������������
//					linkdown++;
//					tra.setFailNum(tra.getFailNum() + 1);
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getFiberLinkList().size() != 0)
//						&& (tra.getResumeRoutePro() != null && tra.getResumeRoutePro().getFiberLinkList().size() != 0)) {// �лָ��лָ�����,������
//					linkkeep++;
//					tra.setEffectNum(tra.getEffectNum() + 1);
//				}
//				break;
//			}// end switch
//
//			switch (flag) {
//			case 1:// ��·�����ðٷֱ�,ѭ���ô�����
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
//			case 2:// ��·˫���ðٷֱ�,ѭ���ô�����
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
//			case 3:// �ڵ㵥���ðٷֱ�,ѭ���ô�����
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
//			case 4:// �ڵ�˫���ðٷֱ�,ѭ���ô�����
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
//		// RscRestore.rscRestore(trafficList);// �ָ�������ǰ״̬ 11.6
//		RscRelease.rscRelease(trafficList);
//
//		return trafficList;
//	}

	/**
	 * 
	 * @param tralist
	 *            ����ÿ�ι��Ϸ������ջָ�·��
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
	 * (ˢ��)��ֵҵ�����Ӱ��/ʧ�ܴ���
	 */
	public static void refresh() {
		for (int i = 0; i < Traffic.trafficList.size(); ++i) {
			Traffic tra = Traffic.trafficList.get(i);
			tra.setFailNum(0);
			tra.setEffectNum(0);
		}
	}


}
