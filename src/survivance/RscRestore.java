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
	 * 恢复业务以前占用的资源 总入口
	 * 
	 * @param traList
	 */
	public static void rscRestore(List<Traffic> traList) {
		Iterator<Traffic> it = traList.iterator();
		while (it.hasNext()) {
			Traffic tra = it.next();
			int faultType = tra.getFaultType();
			switch (faultType) {
			case 0://正常
				break;
			case 1://工作路由毁坏
				if (tra.getProtectLevel().equals(TrafficLevel.PERMANENT11)) {
					RscRelease.rscSimRelease(tra.getResumeRoutePro()); // 释放恢复保护占用的资源
				}
				if (tra.getProtectLevel().equals(TrafficLevel.RESTORATION)) {
					RscRelease.rscSimRelease(tra.getResumeRoute());// 释放恢复占用的资源
				}
				if(tra.getProtectLevel().equals(TrafficLevel.PresetRESTORATION)) {
					RscRelease.rscSimRelease(tra.getResumeRoute());// 释放恢复占用的资源
				}
//				tra.setResumeRoute(null);//2017.10.13
//				tra.setResumeRoutePro(null);//2017.10.13
				restoreSource(tra.getWorkRoute()); // 工作重新占用资源
				restorePort(tra.getWorkRoute());
				break;
			case 2://保护路由毁坏
				if (tra.getProtectLevel().equals(TrafficLevel.PERMANENT11)) {
					RscRelease.rscSimRelease(tra.getResumeRoutePro()); // 释放恢复保护占用的资源
				}
//				tra.setResumeRoute(null);//2017.10.13
//				tra.setResumeRoutePro(null);//2017.10.13
				restoreSource(tra.getProtectRoute()); // 保护重新占用资源
				restorePort(tra.getProtectRoute());
				break;
			case 3://工作保护都毁坏
				if (tra.getProtectLevel().equals(TrafficLevel.PERMANENT11)) {
					RscRelease.rscSimRelease(tra.getResumeRoutePro());// 释放恢复保护占用的资源
					RscRelease.rscSimRelease(tra.getResumeRoute());// 释放恢复占用的资源
				}
//				tra.setResumeRoute(null);//2017.10.13
//				tra.setResumeRoutePro(null);//2017.10.13
				restoreSource(tra.getWorkRoute()); // 工作重新占用资源
				restorePort(tra.getWorkRoute());
				restoreSource(tra.getProtectRoute()); // 保护重新占用资源
				restorePort(tra.getProtectRoute());
				break;
			default:
				break;
			}
		}
	}// end rscRestore

	/**
	 * 由rscRestore调用，恢复路由的资源
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
			int id = curRoute.getWaveLengthIdList().get(i);// 得到路由占用的波长序号
 			wdmLink.getWaveLengthList().get(id).setCarriedTraffic(curRoute.getBelongsTraffic());// 为时隙添加承载业务
			wdmLink.getCarriedTrafficList().add(curRoute.getBelongsTraffic());// 为链路添加承载业务
			wdmLink.setRemainResource(wdmLink.getRemainResource() + 1);// 更新链路剩余资源
			if(curRoute.getBelongsTraffic().getProtectLevel().equals(TrafficLevel.PresetRESTORATION)) {
			wdmLink.getWaveLengthList().get(id).setPre(true);
			}
			wdmLink.getWaveLengthList().get(id).setPre(false);//2017.10.13
		} // 波长分配完
		return true;
	}

	/**
	 * 恢复路由占用的端口资源，由rscResore调用
	 * 
	 * @param route
	 */
	public static void restorePort(Route route) {
		if (route != null && route.getPortList() != null) {
			for (int i = 0; i < route.getPortList().size(); i++) {
				Port port = route.getPortList().get(i);
				switch (i) {
				case 0:
					port.setStatus(PortStatus.上路);
					port.setRemainResource(1);
				    if(route.getBelongsTraffic().getProtectLevel().equals(TrafficLevel.PresetRESTORATION))
					port.setIspre(true);
					port.setIspre(false);//2017.10.13
					break;
				case 1:
					port.setStatus(PortStatus.下路);
					port.setRemainResource(1);
					if(route.getBelongsTraffic().getProtectLevel().equals(TrafficLevel.PresetRESTORATION))
					port.setIspre(true);
					port.setIspre(false);//2017.10.13
					break;
				default:// 线路端口
					port.setStatus(PortStatus.通过);
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
