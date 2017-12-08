package dataControl;

import algorithm.*;
import data.CommonNode;
import data.FiberLink;
import data.Route;
import data.Traffic;
import data.Wavelength_2;
import database.*;
import dialog.Dlg_PolicySetting;
import enums.TrafficGroupType;
import enums.TrafficLevel;
import enums.TrafficRate;

public class TrafficData {

    public static String s_sTrafficMsg = "节点链路信息未导入";

    public void inputTraffic(String str) {
	TrafficDatabase traffic = new TrafficDatabase();
	traffic.inputTraffic(str);
	if (traffic.msgerr.equals(""))
	    s_sTrafficMsg = traffic.msgt;
	else
	    s_sTrafficMsg = traffic.msgerr;
    }

    public static void clearTraffic() {
	if (!RouteAlloc.isRouteEmpty(Traffic.trafficList)) {// 若路由不为空
	    int size = Traffic.trafficList.size();
	    Traffic tr;
	    for (int i = 0; i < size; i++) {
		tr = Traffic.trafficList.get(i);
		tr.releasePortSource(tr, 0);// 关于资源的分配与释放，清空线路端口
		tr.releaseTraffic(tr, 0);// 关于资源的分配与释放，11.22，清空路由
		tr.delTrafficPort(tr); // 关于资源的分配与释放， 11.22，清空支路端口
	    }
	}
//	if (Traffic.trafficList != null && Traffic.trafficList.size() != 0)
	Traffic.trafficList.clear();
	System.gc();
	Dlg_PolicySetting.isDesign = false;// CC 2017.5.2
    }

    // public static void clearTrafficAndRsc()
    // {
    // //释放链路资源
    // int size=Traffic.trafficList.size();
    // Traffic tr;
    // for(int i=0;i<size;i++){
    // tr=Traffic.trafficList.get(i);
    // tr.releaseTraffic(tr,0);//关于资源的分配与释放，11.22
    // tr.releasePortSource(tr, 0);//关于资源的分配与释放，
    // tr.delTrafficPort(tr); //关于资源的分配与释放，待写 11.22
    // }
    // clearTraffic();
    // }

    public void outputTraffic(String str) {
	TrafficDatabase traffic = new TrafficDatabase();
	traffic.outputTraffic(str);
	s_sTrafficMsg = traffic.msgt;
    }

    public static void delTraffic(Traffic tra) {
	tra.releaseTraffic(tra, 0);
	tra.releaseTraffic(tra, 1);
	tra.releaseTraffic(tra, 2);

	Traffic.delTraffic(tra);
    }
    public static void addTraffic(int rankId, CommonNode fromNode, CommonNode toNode, TrafficRate rate, int traNum,
    		TrafficLevel protectLevel, boolean isElectricalCrossConnection, boolean isShare,
    		int idOfTrafficGroup, TrafficGroupType TraGroType)
    {
    	boolean isHaveTraffic=false;
    	Route existRoute=null;
    	Wavelength_2 existWaveLength=null;
    	CommonNode MustPassNode=null;
    	CommonNode MustAvoidNode=null;
    	FiberLink MustPassLink=null;
    	FiberLink MustAvoidLink=null;
    	int id=Traffic.trafficList.size()+1;
    	Traffic t=new Traffic(id,rankId, fromNode, toNode,rate, traNum,
										protectLevel,isElectricalCrossConnection,isShare,
							    		idOfTrafficGroup,TraGroType,MustPassNode,MustAvoidNode,
							    		MustPassLink,MustAvoidLink,isHaveTraffic,existRoute,existWaveLength);
    	Traffic.addTraffic(t);
    }
    
    
    
}
