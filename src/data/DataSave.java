package data;
import java.util.LinkedList;
import java.util.List;

import enums.PortRate;
/**
 * @author TY����ү
 *2017/9/22
 */
public class DataSave {
public static int reRoutePolicy = 1;//��·�ɲ���ѡ��Ĭ����Ԥ����·��1����̬��·��2
public static int waveNum=80;//����
public static PortRate judge=PortRate.G100;//�ж��û�ѡ�����ĸ�����
public static int systemRate=100;//ϵͳ����
public static boolean flexibleRaster=false;//���դ��
public static int locknum=0;//�����Ĳ�����
public static int separate=1;//����ҵ����·�ɷ���ı�־λ�����ֻ��·����Ϊ1 ����ڵ����·��ͬ����Ϊ2
public static int separate1=1;//����ҵ����·�ɷ���ı�־λ�����ֻ��������Ϊ1 ��������ͱ�����ͬ����Ϊ2
public static int onlyPre = 0;
public static int BackToBack =11;//������ָ��
public static int BackToBackA =12;
public static int BackToBackB =20;
public static int BackToBackC =28;
public static double Gain=22;//�Ŵ�������
public static double AboveNF=4.5;
public static double BelowNF=5;
public static double indexA=0.3;//�Զ����㷨��ϵ��������
public static double indexB=0.3;//����
public static double indexC=0.4;//����
public static boolean OSNR = true;
public static double AF = 4.5;//�Ŵ�������ϵ��
public static double OSC = 0.25;//0.25osc�ϡ��ֲ���Ԫģ�����
public static double road = 0.2;//0.2��·��ɵ�˥��������
//������
public void default1(int a,PortRate b,boolean c) {
this.waveNum=a;
this.judge=b;
this.flexibleRaster=c;
	
}

}
	


