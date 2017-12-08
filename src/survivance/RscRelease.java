package survivance;

import java.util.Iterator;
import java.util.List;

import algorithm.RouteAlloc;
import data.*;
import enums.PortStatus;
import enums.Status;
import enums.TrafficLevel;

/**
 * ���ܣ��ͷŹ���ҵ��ռ�õ���Դ
 * 
 * @author CC
 *
 */

public class RscRelease {
	/**
	 * ģ���ͷ�·�ɵ���Դ����ʵֻ��û����ճ��ص�ҵ����������·��Դ���
	 * 
	 * @param route
	 */
	private static void simReleaseRoute(Route route) {
		if (route == null)
			return;
		Traffic tra = route.getBelongTraffic();
		List<WDMLink> wdmLinkList = route.getWDMLinkList();
		for (WDMLink wdmLink : wdmLinkList) {
			if (wdmLink.getCarriedTrafficList().remove(tra)) {// ����Ҫ�ж��Ƿ��������Ϊ��᷵��BOOLEAN10.21
				wdmLink.setRemainResource(wdmLink.getRemainResource() + 1);
				for (WaveLength wlength : wdmLink.getWaveLengthList()) {
					if (wlength.getCarriedTraffic() == null)
						continue;
					else if (wlength.getCarriedTraffic().equals(tra)) {
						wlength.setCarriedTraffic(null);
						wlength.setStatus(Status.����);
						wlength.setPre(false);// 2027.10.13
					}
				}
			}
		}

	}

	/**
	 * �ͷŵ�ǰ·�ɵ�ʵ�ʶ˿�
	 * 
	 * @param route
	 */
	public static void releasePort(Route route) {
		if (route != null && route.getPortList() != null) {
			for (int i = 0; i < route.getPortList().size(); i++) {
				Port port = route.getPortList().get(i);
				switch (i) {
				case 0:
					port.setStatus(PortStatus.����);// ֧·�˿�
					port.setRemainResource(1);
					port.setIspre(false);// 2017.10.13
					break;
				case 1:
					port.setStatus(PortStatus.����);// ֧·�˿�
					port.setRemainResource(1);
					port.setIspre(false);// 2017.10.13
					break;
				default:// ��·�˿�
					port.setRemainResource(1);
					if (port.getRemainResource() == 1)
						port.setStatus(PortStatus.����);
					port.setIspre(false);// 2017.10.13
					break;
				}// end switch
			} // end for
		}
	}

	/**
	 * �ͷŷ���ռ�õĶ˿ڣ���������route ��portlist��һ��
	 * 
	 * @param route
	 */
	public static void releaseSimPort(Route route) {
		if (route != null && route.getSimPortList() != null) {
			for (int i = 0; i < route.getSimPortList().size(); i++) {
				Port port = route.getSimPortList().get(i);
				switch (i) {
				case 0:
					port.setStatus(PortStatus.����);// ֧·�˿�
					port.setRemainResource(1);
					port.setIspre(false);// 2017.10.13
					break;
				case 1:
					port.setStatus(PortStatus.����);// ֧·�˿�
					port.setRemainResource(1);
					port.setIspre(false);// 2017.10.13
					break;
				default:// ��·�˿�
					// port.setRemainResource(port.getRemainResource() +
					// route.getBelongsTraffic().getNrate());
					port.setRemainResource(1);
					if (port.getRemainResource() == 1)
						port.setStatus(PortStatus.����);
					port.setIspre(false);// 2017.10.13
					break;
				}// end switch
			} // end for
		}
	}

	/**
	 * 
	 * ����ҵ��Ĺ����������Զ��ͷ�ҵ����Դ
	 * 
	 * @param trafficLink
	 * @param faulttype
	 *            0������1����·�ɹ��ϣ�2����·�ɹ��ϣ�3������
	 */
	public static void rscRelease(List<Traffic> trafficList) {
		// ����ʧ�ܺ���ʾ����·ʧ���ˡ�
		// assert FiberLink.allFiberLinkList.size() == 35 : "��·�ı���";// 10.27

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
	 * �ͷ� ����·�ɡ�����·��ռ�õ���·��Դ�Ͷ˿���Դ
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
	 * �ͷ�·�� �ָ�·�� �� �ָ�����·�� ռ�õ���·��Դ�Ͷ˿���Դ
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
