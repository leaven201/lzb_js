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

    public static String s_sTrafficMsg = "�ڵ���·��Ϣδ����";

    public void inputTraffic(String str) {
	TrafficDatabase traffic = new TrafficDatabase();
	traffic.inputTraffic(str);
	if (traffic.msgerr.equals(""))
	    s_sTrafficMsg = traffic.msgt;
	else
	    s_sTrafficMsg = traffic.msgerr;
    }

    public static void clearTraffic() {
	if (!RouteAlloc.isRouteEmpty(Traffic.trafficList)) {// ��·�ɲ�Ϊ��
	    int size = Traffic.trafficList.size();
	    Traffic tr;
	    for (int i = 0; i < size; i++) {
		tr = Traffic.trafficList.get(i);
		tr.releasePortSource(tr, 0);// ������Դ�ķ������ͷţ������·�˿�
		tr.releaseTraffic(tr, 0);// ������Դ�ķ������ͷţ�11.22�����·��
		tr.delTrafficPort(tr); // ������Դ�ķ������ͷţ� 11.22�����֧·�˿�
	    }
	}
//	if (Traffic.trafficList != null && Traffic.trafficList.size() != 0)
	Traffic.trafficList.clear();
	System.gc();
	Dlg_PolicySetting.isDesign = false;// CC 2017.5.2
    }

    // public static void clearTrafficAndRsc()
    // {
    // //�ͷ���·��Դ
    // int size=Traffic.trafficList.size();
    // Traffic tr;
    // for(int i=0;i<size;i++){
    // tr=Traffic.trafficList.get(i);
    // tr.releaseTraffic(tr,0);//������Դ�ķ������ͷţ�11.22
    // tr.releasePortSource(tr, 0);//������Դ�ķ������ͷţ�
    // tr.delTrafficPort(tr); //������Դ�ķ������ͷţ���д 11.22
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
