package algorithm;

import java.util.LinkedList;
import java.util.List;
import data.BasicLink;
import data.CommonNode;
import data.Port;
import data.Route;
import data.Traffic;
import data.WDMLink;
import enums.PortKind;
import enums.PortStatus;
import enums.PortType;
import enums.TrafficLevel;

public class PortAlloc {
	public static StringBuffer portFallBuffer = new StringBuffer();// 节点端口资源分配失败日志

	public static boolean allocatePort(Route route) {
		LinkedList<Port> portList = new LinkedList<Port>();// 存储链路的路由的port
		Traffic traffic = route.getBelongsTraffic();
		List<WDMLink> wdmlinkList = route.getWDMLinkList();// 根据链路找端口，完美！
		int hopSum = wdmlinkList.size();
		///////////////// 首先开始分配业务首节点的支路端口
		Port port = findPort(traffic, PortKind.支路端口, traffic.getFromNode(), traffic.getNrate(), null);
		if (port == null) {
			portFallBuffer.append("业务-" + traffic.getName() + "(" + traffic.getId() + ") 在节点: "
					+ traffic.getFromNode().getName() + "处无" + traffic.getNrate() + "G支路端口可用" + "\r\n");
			return false;
		}
		portList.add(port);
		port.getCarriedTraffic().add(traffic);

		// 开始分配业务末节点的支路端口
		port = findPort(traffic, PortKind.支路端口, traffic.getToNode(), traffic.getNrate(), null);
		if (port == null) {
			portFallBuffer.append("业务-" + traffic.getName() + "(" + traffic.getId() + ") 在节点: "
					+ traffic.getToNode().getName() + "处无" + traffic.getNrate() + "G支路端口可用" + "\r\n");
			return false;
		}
		portList.add(port);
		port.getCarriedTraffic().add(traffic);

		/////////////////// 有可用支路端口,开始找线路端口
		for (int hop = 0; hop < hopSum; hop++) {
			WDMLink link = wdmlinkList.get(hop);
			port = findPort(traffic, PortKind.线路端口, link.getFromNode(), traffic.getNrate(), link.getToNode());// 前向端口
			if (port == null) {
				portFallBuffer.append("业务-" + traffic.getName() + "(" + traffic.getId() + ") 在节点: "
						+ link.getFromNode().getName() + "处无" + traffic.getNrate() + "G" + "线路端口可用" + "\r\n");
				return false;
			}
			portList.add(port);
			port.getCarriedTraffic().add(traffic);

			// 根据这条链路的类型在末节点找线路端口
			port = findPort(traffic, PortKind.支路端口, traffic.getToNode(), traffic.getNrate(), null);
			if (port == null) {
				portFallBuffer.append("业务-" + traffic.getName() + "(" + traffic.getId() + ") 在节点: "
						+ traffic.getToNode().getName() + "处无" + traffic.getNrate() + "G支路端口可用" + "\r\n");
				return false;
			}
			portList.add(port);
			port.getCarriedTraffic().add(traffic);

		} // 线路端口分配完毕

		// 开始对portlist里的端口状态进行更新，1和2是支路端口
		for (int i = 0; i < portList.size(); i++) {
			port = portList.get(i);
			if (i == 0) {// 表明是支路端口
				port.setStatus(PortStatus.上路);
				port.setRemainResource(0);
			} else if (i == 1) {// 表明是支路端口
				port.setStatus(PortStatus.下路);
				port.setRemainResource(0);
			} else {// 其他的都是线路端口
				port.setStatus(PortStatus.通过);
				port.setRemainResource(0);
			}
			// port.getCarriedTraffic().add(traffic);
		}
		route.setPortList(portList);// 如果到这一步说明端口已经分配成功，无需清空。
		Port.usedPortList.addAll(portList);// 方便清空
		return true;
	}

	/**
	 * （分配失败不需要释放） 仿真专用的端口分配，区别在于最后把用过的端口放在了路由的不同list里面
	 * 
	 * @param route
	 * @return
	 */
	public static boolean SimAllocatePort(Route route) {
		LinkedList<Port> portList = new LinkedList<Port>();// 存储链路的路由的port

		Traffic traffic = route.getBelongsTraffic();
		List<WDMLink> linkList = route.getWDMLinkList();// 根据链路找端口，完美！
		int hopSum = linkList.size();
		// 首先开始分配业务首节点的支路端口
		Port port = findPort(traffic, PortKind.支路端口, traffic.getFromNode(), traffic.getNrate(), null);
		if (port == null) {
			portFallBuffer.append("端口分配失败：\r\n业务-" + traffic.getName() + "(" + traffic.getId() + ") 在节点: "
					+ traffic.getFromNode().getName() + "处无" + traffic.getNrate() + "G支路端口可用" + "\r\n");
			return false;
		}
		portList.add(port);
		port.getCarriedTraffic().add(traffic);

		// 开始分配业务末节点的支路端口
		port = findPort(traffic, PortKind.支路端口, traffic.getToNode(), traffic.getNrate(), null);
		if (port == null) {
			portFallBuffer.append("端口分配失败：\r\n业务-" + traffic.getName() + "(" + traffic.getId() + ") 在节点: "
					+ traffic.getToNode().getName() + "处无" + traffic.getNrate() + "G支路端口可用" + "\r\n");
			return false;
		}
		portList.add(port);
		port.getCarriedTraffic().add(traffic);

		// 有可用支路端口,开始找线路端口
		for (int hop = 0; hop < hopSum; hop++) {
			BasicLink link = linkList.get(hop);// 根据这条链路的类型在首节点找线路端口
			port = findPort(traffic, PortKind.线路端口, link.getFromNode(), traffic.getNrate(), link.getToNode());// 前向端口
			if (port == null) {
				portFallBuffer.append("端口分配失败：\r\n业务-" + traffic.getName() + "(" + traffic.getId() + "): "
						+ traffic.getName() + " 在节点: " + link.getFromNode().getName() + "处无" + traffic.getNrate()
						+ "G线路端口可用" + "\r\n");
				return false;
			}
			portList.add(port);
			port.getCarriedTraffic().add(traffic);

			// 根据这条链路的类型在末节点找线路端口
			port = findPort(traffic, PortKind.线路端口, link.getToNode(), traffic.getNrate(), link.getFromNode());// 后向端口
			if (port == null) {
				portFallBuffer.append("端口分配失败：\r\n业务-" + traffic.getName() + "(" + traffic.getId() + ") 在节点: "
						+ link.getToNode().getName() + "处无" + traffic.getNrate() + "G线路端口可用" + "\r\n");
				return false;
			}
			portList.add(port);
			port.getCarriedTraffic().add(traffic);

		} // 线路端口分配完毕

		// 开始对portlist里的端口状态进行更新，1和2是支路端口
		for (int i = 0; i < portList.size(); i++) {
			port = portList.get(i);
			if (i == 0) {// 表明是支路端口
				port.setStatus(PortStatus.上路);
				port.setRemainResource(0);
			} else if (i == 1) {// 表明是支路端口
				port.setStatus(PortStatus.下路);
				port.setRemainResource(0);
			} else {// 其他的都是线路端口
				port.setStatus(PortStatus.通过);
				// if(port.getType().equals(PortType.短波))
				// port.setRemainResource(0);//12.14 短波端口bu'fu'yon
				// else
				// port.setRemainResource(port.getRemainResource() - traffic.getNrate());
				port.setRemainResource(0);
			}
			// port.getCarriedTraffic().add(traffic);
		}
		route.setSimPortList(portList);
		return true;
	}

	/**
	 * 此函数功能：为wdmLink找到指定类型（线路/支路） 指定速率的端口
	 * 
	 * @param kind
	 * @param node
	 * @param rate
	 * @return Port
	 */
	public static Port findPort(Traffic tra, PortKind kind, CommonNode node, double rate, CommonNode direction) {
		List<Port> it = null;
		it = node.getPortList();
		for (Port port : it) {
			// 找到符合类型的空闲支路端口
			if (port.getKind().equals(kind) && kind.equals(PortKind.支路端口) && port.getNrate() == rate
					&& (port.getStatus().equals(PortStatus.空闲) || (!port.getStatus().equals(PortStatus.空闲)
							&& (tra.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION
									|| tra.getProtectLevel() == TrafficLevel.RESTORATION))))
				return port;
			else if (port.getKind().equals(kind) && kind.equals(PortKind.线路端口) && (port.getRemainResource() == 1
					|| (port.getRemainResource() != 1 && (tra.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION
							|| tra.getProtectLevel() == TrafficLevel.RESTORATION)))
					&& (port.getStatus().equals(PortStatus.空闲) || port.getStatus().equals(PortStatus.通过))
					&& direction.equals(port.getDirection()))
				return port;
//			CommonAlloc.fallBuffer.append("业务:" + tra +":"+port.getKind()+ "端口"+port.getId()+"保护路由及资源分配成功！\r\n");
		}
		CommonAlloc.fallBuffer.append("shibeile");
		return null;
	}

//    /**
//     * 此函数功能为找到在指定节点指定type（光纤/短波/卫星）和指定类型（线路/支路） 中指定速率的端口
//     * 
//     * @param type
//     * @param kind
//     * @param node
//     * @param rate
//     * @return Port
//     */
//    public static Port findPort(Traffic tra, PortKind kind, CommonNode node, float rate, CommonNode direction) {
//	List<Port> it = null;
//	switch (LinkType2Port(type)) {
//	case 光纤:
//	    it = node.getFiberPortList();
//	    for (Port port : it) {
//		// 找到符合类型的空闲支路端口
//		if (port.getKind().equals(kind) && kind.equals(PortKind.支路端口) && port.getNrate() == rate
//			&& port.getStatus().equals(PortStatus.空闲))
//		    return port;
//		// 找到光纤类型的线路端口，需要判断剩余资源是否大于业务的速率，注意是可以复用的
//		else if (port.getKind().equals(kind) && kind.equals(PortKind.线路端口) && (port.getRemainResource() >= rate)
//			&& (port.getStatus().equals(PortStatus.空闲) || port.getStatus().equals(PortStatus.通过))
//			&& direction.equals(port.getDirection()))
//		    return port;
//	    }
//	    return null;
//	default:
//	    return null;
//	}
//	// for(Port port:it){//这样找会多判断一次type和kind，已经分类存储了
//	// //找到符合类型的空闲支路端口
//	// if(port.getKind().equals(kind) && kind.equals(PortKind.支路端口)&&
//	// port.getNrate()==rate
//	// && port.getStatus().equals(PortStatus.空闲)) return port;
//	// //找到光纤类型的线路端口，需要判断剩余资源是否大于业务的速率，注意是可以复用的
//	// else if
//	// (port.getType().equals(PortType.光纤)&&port.getKind().equals(kind) &&
//	// kind.equals(PortKind.线路端口)
//	// && (port.getRemainResource()>rate/0.155) &&
//	// (port.getStatus().equals(PortStatus.空闲)||port.getStatus().equals(PortStatus.通过))
//	// && direction.equals(port.getDirection()))return port;
//	// //找到两外两种类型的线路端口，因为不能复用所以只要判断剩余资源是否为1
//	// else if
//	// (!port.getType().equals(PortType.光纤)&&port.getKind().equals(kind) &&
//	// kind.equals(PortKind.线路端口)
//	// && (port.getRemainResource()==1) &&
//	// (port.getStatus().equals(PortStatus.空闲)||port.getStatus().equals(PortStatus.通过))
//	// && direction.equals(port.getDirection()))return port;
//	// }//循环结束
//	// return null;
//    }

	/*public static PortType LinkType2Port(LinkType type) {// 把链路类型转换为端口类型
		switch (type) {
		case Fiber:
			return PortType.光纤;
		case Satellite:
			return PortType.卫星;
		case ShortWave:
			return PortType.短波;
		default:
			break;
		}
		return null;

	}*/

}
