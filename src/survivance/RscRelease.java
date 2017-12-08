package survivance;

import java.util.Iterator;
import java.util.List;

import algorithm.RouteAlloc;
import data.*;
import enums.PortStatus;
import enums.Status;
import enums.TrafficLevel;

/**
 * 功能：释放故障业务占用的资源
 * 
 * @author CC
 *
 */

public class RscRelease {
	/**
	 * 模拟释放路由的资源，其实只是没有清空承载的业务，其他的链路资源清空
	 * 
	 * @param route
	 */
	private static void simReleaseRoute(Route route) {
		if (route == null)
			return;
		Traffic tra = route.getBelongTraffic();
		List<WDMLink> wdmLinkList = route.getWDMLinkList();
		for (WDMLink wdmLink : wdmLinkList) {
			if (wdmLink.getCarriedTrafficList().remove(tra)) {// 不需要判断是否包含，因为其会返回BOOLEAN10.21
				wdmLink.setRemainResource(wdmLink.getRemainResource() + 1);
				for (WaveLength wlength : wdmLink.getWaveLengthList()) {
					if (wlength.getCarriedTraffic() == null)
						continue;
					else if (wlength.getCarriedTraffic().equals(tra)) {
						wlength.setCarriedTraffic(null);
						wlength.setStatus(Status.空闲);
						wlength.setPre(false);// 2027.10.13
					}
				}
			}
		}

	}

	/**
	 * 释放当前路由的实际端口
	 * 
	 * @param route
	 */
	public static void releasePort(Route route) {
		if (route != null && route.getPortList() != null) {
			for (int i = 0; i < route.getPortList().size(); i++) {
				Port port = route.getPortList().get(i);
				switch (i) {
				case 0:
					port.setStatus(PortStatus.空闲);// 支路端口
					port.setRemainResource(1);
					port.setIspre(false);// 2017.10.13
					break;
				case 1:
					port.setStatus(PortStatus.空闲);// 支路端口
					port.setRemainResource(1);
					port.setIspre(false);// 2017.10.13
					break;
				default:// 线路端口
					port.setRemainResource(1);
					if (port.getRemainResource() == 1)
						port.setStatus(PortStatus.空闲);
					port.setIspre(false);// 2017.10.13
					break;
				}// end switch
			} // end for
		}
	}

	/**
	 * 释放仿真占用的端口，区别在于route 的portlist不一样
	 * 
	 * @param route
	 */
	public static void releaseSimPort(Route route) {
		if (route != null && route.getSimPortList() != null) {
			for (int i = 0; i < route.getSimPortList().size(); i++) {
				Port port = route.getSimPortList().get(i);
				switch (i) {
				case 0:
					port.setStatus(PortStatus.空闲);// 支路端口
					port.setRemainResource(1);
					port.setIspre(false);// 2017.10.13
					break;
				case 1:
					port.setStatus(PortStatus.空闲);// 支路端口
					port.setRemainResource(1);
					port.setIspre(false);// 2017.10.13
					break;
				default:// 线路端口
					// port.setRemainResource(port.getRemainResource() +
					// route.getBelongsTraffic().getNrate());
					port.setRemainResource(1);
					if (port.getRemainResource() == 1)
						port.setStatus(PortStatus.空闲);
					port.setIspre(false);// 2017.10.13
					break;
				}// end switch
			} // end for
		}
	}

	/**
	 * 
	 * 根据业务的故障类型来自动释放业务资源
	 * 
	 * @param trafficLink
	 * @param faulttype
	 *            0正常，1工作路由故障，2保护路由故障，3都故障
	 */
	public static void rscRelease(List<Traffic> trafficList) {
		// 断言失败后提示“链路失败了”
		// assert FiberLink.allFiberLinkList.size() == 35 : "链路改变了";// 10.27

		Iterator<Traffic> it = trafficList.iterator();
		while (it.hasNext()) {
			Traffic theTraffic = it.next();
			int faultType = theTraffic.getFaultType();
			switch (faultType) {
			case 0:
				break;
			case 1:
				rscRelease(theTraffic.getWorkRoute());
				break;
			case 2:
				rscRelease(theTraffic.getProtectRoute());
				break;
			case 3:
				rscRelease(theTraffic.getWorkRoute());
				rscRelease(theTraffic.getProtectRoute());
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 释放 工作路由、保护路由占用的线路资源和端口资源
	 * 
	 * @param route
	 */
	public static void rscRelease(Route route) {
		if (route != null && route.getFiberLinkList().size() > 0) {
			releasePort(route);
			simReleaseRoute(route);
		}
		// RouteAlloc.releaseRoute(route);

	}

	/**
	 * 释放路由 恢复路由 和 恢复保护路由 占用的线路资源和端口资源
	 * 
	 * @param route
	 */
	public static void rscSimRelease(Route route) {
		if (route != null && route.getFiberLinkList().size() > 0) {// 10.21 11.7
			releaseSimPort(route);
			RouteAlloc.releaseRoute(route);
		}
	}

}
