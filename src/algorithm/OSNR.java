package algorithm;

import java.util.*;
import data.*;

/*
 * ���ܣ��ֶμ���OSNR
 * OBA��OPA��ô�����д���ȶ
 */
public class OSNR {
    private int constant=58;
    private double inPower;//���˹��ˣ�dBm��
    private double NF;//����ϵ��(dB)
    private double L;//˥����dB��
    private int N;//��Ŷ�����
    private double G_BA;//ǰ�÷Ŵ�������
    private double G_PA;//����h�Ŵ�������
    private double allNoise;//��������Ҳ��ǰ������
    private double ownNoise;//��������
    private double OSNR;
    
    public double calculateOSNR(Traffic tra,int flag ){//flag=1 ����·�� ��flag=2 ����·��;
							//flag=3 �ָ�·�� ;flag=4 �ָ�·�ɵı���·��
//	OSNR osnr=new OSNR();
	//OBA����
	G_BA = tra.getG_BA();
	if (tra.getG_BA() == 0) {
	    allNoise=ownNoise=0;
	}else{
	    NF=;
	    allNoise=ownNoise=this.calculateOwnNoise(NF, G_BA);
	}
	inPower=;
	OSNR=inPower-allNoise;
	
	
	//OLA����
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
	
	//OPA����
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
	//10log(power(10,ǰ�������ۻ�������/10)+power(10��������������/10))
	return ( 10*Math.log10(Math.pow(10, x/10)+Math.pow(10, y/10)) );
    }
}
