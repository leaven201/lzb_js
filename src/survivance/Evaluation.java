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
	public static int ceshi=0;
    public static int ceshilianlu=0;
    public static int ceshi0=0;
    public static int ceshi1=0;
    public static int ceshi2=0;
    public static int ceshi3=0;
	/**
	 * �ڵ㵥��
	 * 
	 * @param node
	 * @return ��Ӱ���ҵ��
	 */
	public static List<Traffic> nodeEvaluation(CommonNode node) {
		Survivance surv = new Survivance();
		List<FiberLink> basicLink = Survivance.getNodeEffectLink(node);
//		for (int j=0;j<basicLink.size();j++) {
//			System.out.println("������е���Ӱ����·"+basicLink.get(j));			
//		}
//		for(int i=0;i<WDMLink.WDMLinkList.size();i++) {
//			System.out.println("���������·�Ĺ���״̬"+i+"   "+WDMLink.WDMLinkList.get(i).isBroken());
//			System.out.println("���������·�ı��"+WDMLink.WDMLinkList.get(i));
//		}
		Survivance.setFault(basicLink);// ���ù���״̬
//		for(int i=0;i<WDMLink.WDMLinkList.size();i++) {
//			System.out.println("���������·�Ĺ���״̬"+i+"   "+WDMLink.WDMLinkList.get(i).isBroken());
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
//		System.out.println("������е���Ӱ��ҵ��"+trafficList);
		List<Traffic> tl = evaluation(trafficList, 1,node);
		System.out.println(tl);
		for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
			WDMLink.WDMLinkList.get(i).setActive(true);
		}
		Survivance.setback(basicLink);
		return tl;
	}
	
	
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
			Evaluation.clearRsmRoute(tralist);
			
		}
//        System.out.println("�������ҵ��ֵ"+ceshi);
//        System.out.println("���������·��ֵ"+ceshilianlu);
//        System.out.println("���û������ҵ��"+ceshi0);
//        System.out.println("���������Ӱ���ҵ��"+ceshi1);
//        System.out.println("���������Ӱ���ҵ��"+ceshi2);
//        System.out.println("�������������Ӱ���ҵ��"+ceshi3);
//        System.out.println("�������sur"+Survivance.ceshisur);
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
	 * ��·����
	 * 
	 * @param link
	 * @return
	 */
	public static List<Traffic> linkEvaluation(FiberLink link) {
		//��ʼ��ÿ���ڵ��dynUsedOTU[1]Ϊ0
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
		System.out.println("+++++�����Ӱ���������·"+basicLink.get(i));
		}
		//����SRLG����·���ϵ�Ӱ��
		Survivance surv = new Survivance();
		List<Traffic> trafficList = surv.getAffectedTraffic(basicLink);
		for(int i=0;i<trafficList.size();i++) {
			System.out.println("+++++���������Ӱ���ҵ��"+trafficList.get(i));
			Traffic tra = trafficList.get(i);
			tra.setResumeRoute(null);
			tra.setResumeRoutePro(null);
		}
		if (trafficList == null || trafficList.size() == 0)
			return null; // 10.20 ������ÿ�ҵ���ж�
		// System.out.println("��Ӱ��ҵ�������"+tl.size());
		Survivance.setFault(basicLink);
		List<Traffic> tl = evaluation(trafficList, 2,null);
		
		
		
		for (Traffic tr : tl) {
			tr.setFaultType(0);
		}
		Survivance.setback(basicLink);
		return tl;
	}

	/**
	 * ��·����ѭ��
	 * 
	 * @param allLinkList
	 */
	//8.6
	public static void listEvaluation(LinkedList<FiberLink> allLinkList) {
		linkCutoff = 0;
		linkKeep = 0;
		linkDown = 0;
		
		//ÿ�ε���ѭ����ʱ�򶼳�ʼ���ڵ�dynUsedOTU[]����·��dynUsedLink[]����
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
			// �жϴ˴η������˶��ٸ�OTU��������ڵ�ǰʹ�õ�OTU��dynOTU[0]�������
			for (CommonNode node : CommonNode.allNodeList) {
				node.countUpdown1(node);//���´˴η����õ�����·ģ��
				int[] dynUsedOTU = node.getDynUsedOTU();
				if (dynUsedOTU[1] > dynUsedOTU[0]) {
					dynUsedOTU[0] = dynUsedOTU[1];
				}
			}
			
			Evaluation.clearRsmRoute(tralist);
		}

	}

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
		List<Traffic> tl=evaluation(tra,3,null);
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
        List<Traffic>tl=evaluation(trafficList,4,null);
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
	

	/**
	 * ���̣��ͷš����¼��㡢ͳ�ơ��ָ���Ӱ��ҵ�� ���������㷨
	 * ͳ��
	 * @param trafficList
	 * @param flag ͳ�Ƶ����͡�1=�ڵ㵥�ϣ�2=��·���ϣ�3=�˹����ö�ϣ�4=SRLG����
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
//		Evaluation.refresh();//���ϵ�ʱ��Ҳ��Ҫ�����Ӱ����� 12.1
//		RscRelease.rscRelease(trafficList);// �ͷ���Դ

		Survivance.reCompute(trafficList,node);// ���¼���(��Ҫ���Ƕ��ָֻ�����)

		// assert FiberLink.allFiberLinkList.size() == 35 : "��·�ı���";// 10.27

		// ͳ�ƹ��ϻָ�������жϡ����������ֵĴ���
		for (int j = 0; j < trafficList.size(); ++j) {
			Traffic tra = trafficList.get(j);
			switch (tra.getProtectLevel()) {
			case PERMANENT11:
				if (tra.getResumeRoute() == null || tra.getResumeRoute().getWDMLinkList().size() == 0) {// û�лָ�·��,���ж�
					linkcutoff++;
					cutoffnum++;
					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getResumeRoutePro() == null || tra.getResumeRoutePro().getWDMLinkList().size() == 0)) {// �лָ�·���޻ָ�������������
					linkdown++;
					downnum++;
//					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getResumeRoutePro() != null && tra.getResumeRoutePro().getWDMLinkList().size() != 0)) {// �лָ��лָ�����,������
					linkkeep++;
					keepnum++;
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				}
				break;
			case NORMAL11:
				if (tra.getResumeRoute() == null || tra.getResumeRoute().getWDMLinkList().size() == 0) {// ����
					linkcutoff++;
					cutoffnum++;
					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getResumeRoutePro() == null || tra.getResumeRoutePro().getWDMLinkList().size() == 0)) {// 1+1ֻ�б���
					linkkeep++;
					keepnum++;
//					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getResumeRoutePro() != null && tra.getResumeRoutePro().getWDMLinkList().size() != 0)) {// ����
					linkkeep++;
					keepnum++;
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				}
				break;
			case RESTORATION:
				if (tra.getResumeRoute() == null || tra.getResumeRoute().getWDMLinkList().size() == 0) {// ����
					linkcutoff++;
					cutoffnum++;
					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)) {// ����
					linkkeep++;
					keepnum++;
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				}
				break;
			case NONPROTECT:
				if (tra.getResumeRoute() == null || tra.getResumeRoute().getWDMLinkList().size() == 0) {// �ж�
					linkcutoff++;
					cutoffnum++;
					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				} else if (tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0) {// ����
					linkkeep++;
					keepnum++;
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				}
				break;
			case PROTECTandRESTORATION:// ����+�ָ�
				if (tra.getResumeRoute() == null || tra.getResumeRoute().getWDMLinkList().size() == 0) {// û�лָ�·��,���ж�
					linkcutoff++;
					cutoffnum++;
					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getPreRoute() == null || tra.getPreRoute().getWDMLinkList().size() == 0)) {// �лָ�·���޻ָ�������������
					linkkeep++;
					keepnum++;
//					tra.setFailNum(tra.getFailNum() + 1);
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				} else if ((tra.getResumeRoute() != null && tra.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra.getPreRoute() != null && tra.getPreRoute().getWDMLinkList().size() != 0)) {// �лָ��лָ�����,������
					linkkeep++;
					keepnum++;
					tra.setEffectNum(tra.getEffectNum() + 1);
					sum++;
				}
				// tra.setProtectLevel(TrafficLevel.RESTORATION);//�������𽵼�
				break;
			default:
				break;
			}// end switch

			switch (flag) {
			case 1:// �ڵ㵥���ðٷֱ�,ѭ���ô�����
				nodeCutoff = nodeCutoff+cutoffnum;//��Ҫ�ٿ���һ��
				nodeKeep = nodeKeep+keepnum;
				nodeDown = nodeDown+downnum;

				if (sum != 0) {
					nCutoffPercent = (float) linkcutoff / sum;
					nKeepPercent = (float) linkkeep / sum;
					nDownPercent = (float) linkdown / sum;
//					nUneffPercent = 1 - nCutoffPercent - nKeepPercent - nDownPercent;
				}
				break;
			case 2:// ��·�����ðٷֱ�,ѭ���ô�����
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
			case 3:// �˹����ö��ֻ�аٷֱȣ���ѭ��û�д���

				if (tra.getEffectNum() != 0) {
					multiCutoffPer = (float) linkcutoff / sum;
					multiKeepPer = (float) linkkeep / sum;
					multiDownPer = (float) linkdown / sum;
//					multiUneffPer = 1 - multiCutoffPer - multiKeepPer - multiDownPer;
				}
				break;
			
			case 4://SRLG�����ðٷֱȣ�ѭ���ô���
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
//		RscRestore.rscRestore(trafficList);// �ָ�������ǰ״̬ 11.6
		//2018.10.15 ��¼�˴η���ʹ�õĲ���ת����
        RouteAlloc.releaseRouteForSim1();
		return trafficList;
	}
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
