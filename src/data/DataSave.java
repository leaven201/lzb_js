package data;
import java.util.LinkedList;
import java.util.List;

import enums.PortRate;
/**
 * @author TY宇少爷
 *2017/9/22
 */
public class DataSave {
public static int waveNum=80;//波道
public static PortRate judge=PortRate.G100;//判断用户选择了哪个速率
public static int systemRate=100;//系统速率
public static boolean flexibleRaster=false;//灵活栅格


//测试用
public void default1(int a,PortRate b,boolean c) {
this.waveNum=a;
this.judge=b;
this.flexibleRaster=c;
	
}

}
	


