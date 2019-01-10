package algorithm;

import java.util.*;

import javax.xml.crypto.Data;

import data.*;

/*
 * 功能：分段计算OSNR
 * OBA和OPA怎么处理有待商榷
 */
public class OSNR {
	private int constant = 58;
	private double inPower;// 入纤光纤（dBm）
	private double NF;// 噪声系数(dB)
	private double L;// 衰减（dB）
	private int N;// 光放段数量
	private double G_BA;// 前置放大器增益
	private double G_PA;// 功率h放大器增益
	private double allNoise;// 总噪声，也是前级噪声
	private double ownNoise;// 本级噪声
	private double OSNR;

	// 计算某路由的等效跨段数
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

	// 计算某路由的等效跨段数
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

	// 计算某段路由的osnr
	public static double calculateOSNR(Route route) {
		double sum = 0;
		List<FiberLink> fiberlist = route.getFiberLinkList();
		if (fiberlist.size() != 0) {
			for (FiberLink fiber : fiberlist) {
				double osnrfiber = fiber.getOSNRCount();

//				System.out.println(fiber+" 计算量"+osnrfiber+" Q:"+Q+" K:"+K+" J:"+J+" O:"+O+" I:"+I);
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

	// 计算某段路的OSNR
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

	// 计算某段路由的OSNR容限
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
	//3:16QAM 2:QPSK 1:BPSK 0:不满足OSNR条件
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

//    public double calculateOSNR(Traffic tra,int flag ){//flag=1 工作路由 ；flag=2 保护路由;
//							//flag=3 恢复路由 ;flag=4 恢复路由的保护路由
////	OSNR osnr=new OSNR();
//	//OBA部分
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
//	//OLA部分
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
//	//OPA部分
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

		// 10log(power(10,前级噪声累积到本级/10)+power(10，本级引入噪声/10))
		return (10 * Math.log10(Math.pow(10, x / 10) + Math.pow(10, y / 10)));
	}

	// 进行OSNR设置之后要重新计算OSNR的值
	public static void resetOSNR() {
		for (FiberLink link : FiberLink.fiberLinkList) {
			double Q = data.DataSave.AF;
			double K = data.DataSave.OSC;
			double J = data.DataSave.road;
			double O = link.getInputPower();
			double I = link.getAttenuation();

			double zengyi = I + J + K;
			// 根据增益设置放大器噪声系数
			if (zengyi <= DataSave.Gain) {
				Q = DataSave.BelowNF;
			} else {
				Q = DataSave.AboveNF;
			}

			// 根据光纤类型，以及光纤衰减设置单波平均入纤光功率
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
