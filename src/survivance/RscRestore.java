package survivance;

import java.util.Iterator;
import java.util.List;
import data.Port;
import data.Route;
import data.Traffic;
import data.WDMLink;
import enums.PortStatus;
import enums.Status;
import enums.TrafficLevel;

public class RscRestore {
	/**
	 * �ָ�ҵ����ǰռ�õ���Դ �����
	 * 
	 * @param traList
	 */
	public static void rscRestore(List<Traffic> traList) {
		Iterator<Traffic> it = traList.iterator();
		while (it.hasNext()) {
			Traffic tra = it.next();
			int faultType = tra.getFaultType();
			switch (faultType) {
			case 0://����
				break;
			case 1://����·�ɻٻ�
				if (tra.getProtectLevel().equals(TrafficLevel.PERMANENT11)) {
					RscRelease.rscSimRelease(tra.getResumeRoutePro()); // �ͷŻָ�����ռ�õ���Դ
				}
				if (tra.getProtectLevel().equals(TrafficLevel.RESTORATION)) {
					RscRelease.rscSimRelease(tra.getResumeRoute());// �ͷŻָ�ռ�õ���Դ
				}
				if(tra.getProtectLevel().equals(TrafficLevel.PresetRESTORATION)) {
					RscRelease.rscSimRelease(tra.getResumeRoute());// �ͷŻָ�ռ�õ���Դ
				}
//				tra.setResumeRoute(null);//2017.10.13
//				tra.setResumeRoutePro(null);//2017.10.13
				restoreSource(tra.getWorkRoute()); // ��������ռ����Դ
				restorePort(tra.getWorkRoute());
				break;
			case 2://����·�ɻٻ�
				if (tra.getProtectLevel().equals(TrafficLevel.PERMANENT11)) {
					RscRelease.rscSimRelease(tra.getResumeRoutePro()); // �ͷŻָ�����ռ�õ���Դ
				}
//				tra.setResumeRoute(null);//2017.10.13
//				tra.setResumeRoutePro(null);//2017.10.13
				restoreSource(tra.getProtectRoute()); // ��������ռ����Դ
				restorePort(tra.getProtectRoute());
				break;
			case 3://�����������ٻ�
				if (tra.getProtectLevel().equals(TrafficLevel.PERMANENT11)) {
					RscRelease.rscSimRelease(tra.getResumeRoutePro());// �ͷŻָ�����ռ�õ���Դ
					RscRelease.rscSimRelease(tra.getResumeRoute());// �ͷŻָ�ռ�õ���Դ
				}
//				tra.setResumeRoute(null);//2017.10.13
//				tra.setResumeRoutePro(null);//2017.10.13
				restoreSource(tra.getWorkRoute()); // ��������ռ����Դ
				restorePort(tra.getWorkRoute());
				restoreSource(tra.getProtectRoute()); // ��������ռ����Դ
				restorePort(tra.getProtectRoute());
				break;
			default:
				break;
			}
		}
	}// end rscRestore

	/**
	 * ��rscRestore���ã��ָ�·�ɵ���Դ
	 * 
	 * @param curRoute
	 * @return
	 */
	public static boolean restoreSource(Route curRoute) {
		System.out.println("111");
		if (curRoute.getWDMLinkList().size() != 0 && curRoute.getWaveLengthIdList().size() == 0)
			return false;
		for (int i = 0; i < curRoute.getWDMLinkList().size(); i++) {
			WDMLink wdmLink = curRoute.getWDMLinkList().get(i);
			int id = curRoute.getWaveLengthIdList().get(i);// �õ�·��ռ�õĲ������
 			wdmLink.getWaveLengthList().get(id).setCarriedTraffic(curRoute.getBelongsTraffic());// Ϊʱ϶��ӳ���ҵ��
			wdmLink.getCarriedTrafficList().add(curRoute.getBelongsTraffic());// Ϊ��·��ӳ���ҵ��
			wdmLink.setRemainResource(wdmLink.getRemainResource() + 1);// ������·ʣ����Դ
			if(curRoute.getBelongsTraffic().getProtectLevel().equals(TrafficLevel.PresetRESTORATION)) {
			wdmLink.getWaveLengthList().get(id).setPre(true);
			}
			wdmLink.getWaveLengthList().get(id).setPre(false);//2017.10.13
		} // ����������
		return true;
	}

	/**
	 * �ָ�·��ռ�õĶ˿���Դ����rscResore����
	 * 
	 * @param route
	 */
	public static void restorePort(Route route) {
		if (route != null && route.getPortList() != null) {
			for (int i = 0; i < route.getPortList().size(); i++) {
				Port port = route.getPortList().get(i);
				switch (i) {
				case 0:
					port.setStatus(PortStatus.��·);
					port.setRemainResource(1);
				    if(route.getBelongsTraffic().getProtectLevel().equals(TrafficLevel.PresetRESTORATION))
					port.setIspre(true);
					port.setIspre(false);//2017.10.13
					break;
				case 1:
					port.setStatus(PortStatus.��·);
					port.setRemainResource(1);
					if(route.getBelongsTraffic().getProtectLevel().equals(TrafficLevel.PresetRESTORATION))
					port.setIspre(true);
					port.setIspre(false);//2017.10.13
					break;
				default:// ��·�˿�
					port.setStatus(PortStatus.ͨ��);
					port.setRemainResource(1);
					if(route.getBelongsTraffic().getProtectLevel().equals(TrafficLevel.PresetRESTORATION))
					port.setIspre(true);
					port.setIspre(false);//2017.10.13
					break;
				}// end switch
			} // end for
		}
	}
}
