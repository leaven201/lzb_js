package algorithm;

import java.util.*;
import data.*;

/*
 * 功能：分段计算OSNR
 * OBA和OPA怎么处理有待商榷
 */
public class OSNR {
    private int constant=58;
    private double inPower;//入纤光纤（dBm）
    private double NF;//噪声系数(dB)
    private double L;//衰减（dB）
    private int N;//光放段数量
    private double G_BA;//前置放大器增益
    private double G_PA;//功率h放大器增益
    private double allNoise;//总噪声，也是前级噪声
    private double ownNoise;//本级噪声
    private double OSNR;
    
    public double calculateOSNR(Traffic tra,int flag ){//flag=1 工作路由 ；flag=2 保护路由;
							//flag=3 恢复路由 ;flag=4 恢复路由的保护路由
//	OSNR osnr=new OSNR();
	//OBA部分
	G_BA = tra.getG_BA();
	if (tra.getG_BA() == 0) {
	    allNoise=ownNoise=0;
	}else{
	    NF=;
	    allNoise=ownNoise=this.calculateOwnNoise(NF, G_BA);
	}
	inPower=;
	OSNR=inPower-allNoise;
	
	
	//OLA部分
	List<FiberLink> list=new LinkedList<FiberLink>() 
	switch(flag){
	    case 1:
		list=tra.getWorkRoute().getFiberLinkList();
		break;
	    case 2:
		list=tra.getProtectRoute().getFiberLinkList();
		break;
	    case 3:
		list=tra.getResumeRoute().getFiberLinkList();
		break;
	    case 4:
		list=tra.getResumeRoutePro().getFiberLinkList();
		break;
	}
	for(FiberLink fiberLink:list){
	    L=fiberLink.getSpanLoss();
	    NF=fiberLink.getNF();
	    inPower=fiberLink.getInPower();
	    ownNoise=this.calculateOwnNoise(NF, L);
	    allNoise=this.calculateAllNoise(allNoise, ownNoise);
	    OSNR=inPower-allNoise;
	}
	
	//OPA部分
	G_PA = tra.getG_PA();
	if (tra.getG_PA() == 0) {
	    return OSNR;
	}else{
	    NF=;
	    ownNoise=this.calculateOwnNoise(NF, G_PA);
	    allNoise=allNoise-L+G_PA;
	    allNoise=this.calculateAllNoise(allNoise, ownNoise);
	    inPower=;
	    OSNR=inPower-allNoise;
	}
 	
	return OSNR;
    }
    
    public double calculateOwnNoise(double nf,double x){
	//NF+10log(power(10,GBA/10)-1)-58
	return (nf+10*Math.log10(Math.pow(10, x/10)-1)-58);
    }
    
    public double calculateAllNoise(double x,double y){
	//10log(power(10,前级噪声累积到本级/10)+power(10，本级引入噪声/10))
	return ( 10*Math.log10(Math.pow(10, x/10)+Math.pow(10, y/10)) );
    }
}
