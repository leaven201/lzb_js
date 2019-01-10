package algorithm;

import java.util.*;

import javax.xml.crypto.Data;

import data.*;

/*
 * ���ܣ��ֶμ���OSNR
 * OBA��OPA��ô�����д���ȶ
 */
public class OSNR {
	private int constant = 58;
	private double inPower;// ���˹��ˣ�dBm��
	private double NF;// ����ϵ��(dB)
	private double L;// ˥����dB��
	private int N;// ��Ŷ�����
	private double G_BA;// ǰ�÷Ŵ�������
	private double G_PA;// ����h�Ŵ�������
	private double allNoise;// ��������Ҳ��ǰ������
	private double ownNoise;// ��������
	private double OSNR;

	// ����ĳ·�ɵĵ�Ч�����
	public static double crossSum(Route route) {
		List<FiberLink> fiberlist = route.getFiberLinkList();
		double crosssum = 0;
		if (fiberlist.size() != 0) {
			for (FiberLink fiber : fiberlist) {
				crosssum += fiber.getDissipation();
			}
		} else {
			for (WDMLink link : route.getWDMLinkList()) {
				fiberlist.addAll(link.getFiberLinkList());
			}
			for (FiberLink fiber : fiberlist) {
				crosssum += fiber.getDissipation();
			}
		}
		return crosssum;
	}

	// ����ĳ·�ɵĵ�Ч�����
	public static double crossSum(List<List<WDMLink>> linkRoute) {
		List<FiberLink> fiberlist = new LinkedList<>();
		double crosssum = 0;

		for (List<WDMLink> links : linkRoute) {
			WDMLink link = links.get(0);
			fiberlist.addAll(link.getFiberLinkList());
		}
		for (FiberLink fiber : fiberlist) {
			double osnrfiber = fiber.getOSNRCount();
			crosssum += fiber.getDissipation();
		}
		return crosssum;
	}

	// ����ĳ��·�ɵ�osnr
	public static double calculateOSNR(Route route) {
		double sum = 0;
		List<FiberLink> fiberlist = route.getFiberLinkList();
		if (fiberlist.size() != 0) {
			for (FiberLink fiber : fiberlist) {
				double osnrfiber = fiber.getOSNRCount();

//				System.out.println(fiber+" ������"+osnrfiber+" Q:"+Q+" K:"+K+" J:"+J+" O:"+O+" I:"+I);
				sum += osnrfiber;
			}
		} else {
			for (WDMLink link : route.getWDMLinkList()) {
				fiberlist.addAll(link.getFiberLinkList());
			}
			for (FiberLink fiber : fiberlist) {
				double osnrfiber = fiber.getOSNRCount();
				sum += osnrfiber;
			}
		}
		double OSNR;
		OSNR = 58 - 10 * Math.log10(sum);
		return OSNR;
	}

	// ����ĳ��·��OSNR
	public static double calculateOSNR(List<List<WDMLink>> linkRoute) {
		double sum = 0;
		List<FiberLink> fiberlist = new LinkedList<>();

		for (List<WDMLink> links : linkRoute) {
			WDMLink link = links.get(0);
			fiberlist.addAll(link.getFiberLinkList());
		}
		for (FiberLink fiber : fiberlist) {
			double osnrfiber = fiber.getOSNRCount();
			sum += osnrfiber;
		}
		double OSNR;
		OSNR = 58 - 10 * Math.log10(sum);
		return OSNR;
	}

	// ����ĳ��·�ɵ�OSNR����
	public static double crossOSNR(Route route) {
		double crossOSNR;
		double yuliang;
		double crossSum = crossSum(route);

		if (crossSum <= DataSave.BackToBackA) {
			yuliang = 4.5;
		} else if (crossSum <= DataSave.BackToBackB) {
			yuliang = 5;
		} else if (crossSum <= DataSave.BackToBackC) {
			yuliang = 5.5;
		} else {
			yuliang = 6;
		}
		crossOSNR = yuliang + DataSave.BackToBack;
		return crossOSNR;
	}
	//3:16QAM 2:QPSK 1:BPSK 0:������OSNR����
	public static int tiaoZhiGeShi(double osnr,double limit) {
		if (osnr > DataSave._16QAM) {
			return 3;
		} else if (osnr > DataSave.QPSK) {
			return 2;
		} else if (osnr > limit){
			return 1;
		}else {
			return 0;
		}
	}

//    public double calculateOSNR(Traffic tra,int flag ){//flag=1 ����·�� ��flag=2 ����·��;
//							//flag=3 �ָ�·�� ;flag=4 �ָ�·�ɵı���·��
////	OSNR osnr=new OSNR();
//	//OBA����
//	G_BA = tra.getG_BA();
//	if (tra.getG_BA() == 0) {
//	    allNoise=ownNoise=0;
//	}else{
//	    NF=;
//	    allNoise=ownNoise=this.calculateOwnNoise(NF, G_BA);
//	}
//	inPower=;
//	OSNR=inPower-allNoise;
//	
//	
//	//OLA����
//	List<FiberLink> list=new LinkedList<FiberLink>() 
//	switch(flag){
//	    case 1:
//		list=tra.getWorkRoute().getFiberLinkList();
//		break;
//	    case 2:
//		list=tra.getProtectRoute().getFiberLinkList();
//		break;
//	    case 3:
//		list=tra.getResumeRoute().getFiberLinkList();
//		break;
//	    case 4:
//		list=tra.getResumeRoutePro().getFiberLinkList();
//		break;
//	}
//	for(FiberLink fiberLink:list){
//	    L=fiberLink.getSpanLoss();
//	    NF=fiberLink.getNF();
//	    inPower=fiberLink.getInPower();
//	    ownNoise=this.calculateOwnNoise(NF, L);
//	    allNoise=this.calculateAllNoise(allNoise, ownNoise);
//	    OSNR=inPower-allNoise;
//	}
//	
//	//OPA����
//	G_PA = tra.getG_PA();
//	if (tra.getG_PA() == 0) {
//	    return OSNR;
//	}else{
//	    NF=;
//	    ownNoise=this.calculateOwnNoise(NF, G_PA);
//	    allNoise=allNoise-L+G_PA;
//	    allNoise=this.calculateAllNoise(allNoise, ownNoise);
//	    inPower=; 
//	    OSNR=inPower-allNoise;
//	}
// 	
//	return OSNR;
//    }

	public double calculateOwnNoise(double nf, double x) {
		// NF+10log(power(10,GBA/10)-1)-58
		return (nf + 10 * Math.log10(Math.pow(10, x / 10) - 1) - 58);
	}

	public double calculateAllNoise(double x, double y) {

		// 10log(power(10,ǰ�������ۻ�������/10)+power(10��������������/10))
		return (10 * Math.log10(Math.pow(10, x / 10) + Math.pow(10, y / 10)));
	}

	// ����OSNR����֮��Ҫ���¼���OSNR��ֵ
	public static void resetOSNR() {
		for (FiberLink link : FiberLink.fiberLinkList) {
			double Q = data.DataSave.AF;
			double K = data.DataSave.OSC;
			double J = data.DataSave.road;
			double O = link.getInputPower();
			double I = link.getAttenuation();

			double zengyi = I + J + K;
			// �����������÷Ŵ�������ϵ��
			if (zengyi <= DataSave.Gain) {
				Q = DataSave.BelowNF;
			} else {
				Q = DataSave.AboveNF;
			}

			// ���ݹ������ͣ��Լ�����˥�����õ���ƽ�����˹⹦��
			if (I <= 25) {
				if (link.getLinkType().equals("G.652")) {
					link.setInputPower(1);
				}
				if (link.getLinkType().equals("G.655")) {
					link.setInputPower(0);
				}
			} else if (I <= 32) {
				if (link.getLinkType().equals("G.652")) {
					link.setInputPower(2);
				}
				if (link.getLinkType().equals("G.655")) {
					link.setInputPower(1);
				}
			} else if (I <= 37) {
				if (link.getLinkType().equals("G.652")) {
					link.setInputPower(4);
				}
				if (link.getLinkType().equals("G.655")) {
					link.setInputPower(2);
				}
			} else {
				if (link.getLinkType().equals("G.652")) {
					link.setInputPower(7);
				}
				if (link.getLinkType().equals("G.655")) {
					link.setInputPower(4);
				}
			}
			link.setOSNRCount(Math.pow(10, ((I + J + K + Q - O) / 10)));
		}
	}
}
