package data;
import java.util.LinkedList;
import java.util.List;

import enums.PortRate;
/**
 * @author TY宇少爷
 *2017/9/22
 */
public class DataSave {
public static int reRoutePolicy = 1;//重路由策略选择，默认是预置重路由1，动态重路由2
public static int waveNum=80;//波道
public static PortRate judge=PortRate.G100;//判断用户选择了哪个速率
public static int systemRate=100;//系统速率
public static boolean flexibleRaster=false;//灵活栅格
public static int locknum=0;//锁定的波道数
public static int separate=1;//关联业务组路由分离的标志位，如果只链路分离为1 如果节点和链路共同分离为2
public static int separate1=1;//关联业务组路由分离的标志位，如果只工作分离为1 如果工作和保护共同分离为2
public static int onlyPre = 0;
public static int BackToBack =11;//背靠背指标
public static int BackToBackA =12;
public static int BackToBackB =20;
public static int BackToBackC =28;
public static double Gain=22;//放大器增益
public static double AboveNF=4.5;
public static double BelowNF=5;
public static double indexA=0.3;//自定义算法中系数，距离
public static double indexB=0.3;//跳数
public static double indexC=0.4;//负载
public static boolean OSNR = true;
public static double AF = 4.5;//放大器噪声系数
public static double OSC = 0.25;//0.25osc合、分波单元模块插损
public static double road = 0.2;//0.2线路侧可调衰耗器插损
//测试用
public void default1(int a,PortRate b,boolean c) {
this.waveNum=a;
this.judge=b;
this.flexibleRaster=c;
	
}

}
	


