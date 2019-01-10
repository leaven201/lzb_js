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
	public static StringBuffer portFallBuffer = new StringBuffer();// �ڵ�˿���Դ����ʧ����־

	public static boolean allocatePort(Route route) {
		LinkedList<Port> portList = new LinkedList<Port>();// �洢��·��·�ɵ�port
		Traffic traffic = route.getBelongsTraffic();
		List<WDMLink> wdmlinkList = route.getWDMLinkList();// ������·�Ҷ˿ڣ�������
		int hopSum = wdmlinkList.size();
		///////////////// ���ȿ�ʼ����ҵ���׽ڵ��֧·�˿�
		Port port = findPort(traffic, PortKind.֧·�˿�, traffic.getFromNode(), traffic.getNrate(), null);
		if (port == null) {
			portFallBuffer.append("ҵ��-" + traffic.getName() + "(" + traffic.getId() + ") �ڽڵ�: "
					+ traffic.getFromNode().getName() + "����" + traffic.getNrate() + "G֧·�˿ڿ���" + "\r\n");
			return false;
		}
		portList.add(port);
		port.getCarriedTraffic().add(traffic);

		// ��ʼ����ҵ��ĩ�ڵ��֧·�˿�
		port = findPort(traffic, PortKind.֧·�˿�, traffic.getToNode(), traffic.getNrate(), null);
		if (port == null) {
			portFallBuffer.append("ҵ��-" + traffic.getName() + "(" + traffic.getId() + ") �ڽڵ�: "
					+ traffic.getToNode().getName() + "����" + traffic.getNrate() + "G֧·�˿ڿ���" + "\r\n");
			return false;
		}
		portList.add(port);
		port.getCarriedTraffic().add(traffic);

		/////////////////// �п���֧·�˿�,��ʼ����·�˿�
		for (int hop = 0; hop < hopSum; hop++) {
			WDMLink link = wdmlinkList.get(hop);
			port = findPort(traffic, PortKind.��·�˿�, link.getFromNode(), traffic.getNrate(), link.getToNode());// ǰ��˿�
			if (port == null) {
				portFallBuffer.append("ҵ��-" + traffic.getName() + "(" + traffic.getId() + ") �ڽڵ�: "
						+ link.getFromNode().getName() + "����" + traffic.getNrate() + "G" + "��·�˿ڿ���" + "\r\n");
				return false;
			}
			portList.add(port);
			port.getCarriedTraffic().add(traffic);

			// ����������·��������ĩ�ڵ�����·�˿�
			port = findPort(traffic, PortKind.֧·�˿�, traffic.getToNode(), traffic.getNrate(), null);
			if (port == null) {
				portFallBuffer.append("ҵ��-" + traffic.getName() + "(" + traffic.getId() + ") �ڽڵ�: "
						+ traffic.getToNode().getName() + "����" + traffic.getNrate() + "G֧·�˿ڿ���" + "\r\n");
				return false;
			}
			portList.add(port);
			port.getCarriedTraffic().add(traffic);

		} // ��·�˿ڷ������

		// ��ʼ��portlist��Ķ˿�״̬���и��£�1��2��֧·�˿�
		for (int i = 0; i < portList.size(); i++) {
			port = portList.get(i);
			if (i == 0) {// ������֧·�˿�
				port.setStatus(PortStatus.��·);
				port.setRemainResource(0);
			} else if (i == 1) {// ������֧·�˿�
				port.setStatus(PortStatus.��·);
				port.setRemainResource(0);
			} else {// �����Ķ�����·�˿�
				port.setStatus(PortStatus.ͨ��);
				port.setRemainResource(0);
			}
			// port.getCarriedTraffic().add(traffic);
		}
		route.setPortList(portList);// �������һ��˵���˿��Ѿ�����ɹ���������ա�
		Port.usedPortList.addAll(portList);// �������
		return true;
	}

	/**
	 * ������ʧ�ܲ���Ҫ�ͷţ� ����ר�õĶ˿ڷ��䣬�������������ù��Ķ˿ڷ�����·�ɵĲ�ͬlist����
	 * 
	 * @param route
	 * @return
	 */
	public static boolean SimAllocatePort(Route route) {
		LinkedList<Port> portList = new LinkedList<Port>();// �洢��·��·�ɵ�port

		Traffic traffic = route.getBelongsTraffic();
		List<WDMLink> linkList = route.getWDMLinkList();// ������·�Ҷ˿ڣ�������
		int hopSum = linkList.size();
		// ���ȿ�ʼ����ҵ���׽ڵ��֧·�˿�
		Port port = findPort(traffic, PortKind.֧·�˿�, traffic.getFromNode(), traffic.getNrate(), null);
		if (port == null) {
			portFallBuffer.append("�˿ڷ���ʧ�ܣ�\r\nҵ��-" + traffic.getName() + "(" + traffic.getId() + ") �ڽڵ�: "
					+ traffic.getFromNode().getName() + "����" + traffic.getNrate() + "G֧·�˿ڿ���" + "\r\n");
			return false;
		}
		portList.add(port);
		port.getCarriedTraffic().add(traffic);

		// ��ʼ����ҵ��ĩ�ڵ��֧·�˿�
		port = findPort(traffic, PortKind.֧·�˿�, traffic.getToNode(), traffic.getNrate(), null);
		if (port == null) {
			portFallBuffer.append("�˿ڷ���ʧ�ܣ�\r\nҵ��-" + traffic.getName() + "(" + traffic.getId() + ") �ڽڵ�: "
					+ traffic.getToNode().getName() + "����" + traffic.getNrate() + "G֧·�˿ڿ���" + "\r\n");
			return false;
		}
		portList.add(port);
		port.getCarriedTraffic().add(traffic);

		// �п���֧·�˿�,��ʼ����·�˿�
		for (int hop = 0; hop < hopSum; hop++) {
			BasicLink link = linkList.get(hop);// ����������·���������׽ڵ�����·�˿�
			port = findPort(traffic, PortKind.��·�˿�, link.getFromNode(), traffic.getNrate(), link.getToNode());// ǰ��˿�
			if (port == null) {
				portFallBuffer.append("�˿ڷ���ʧ�ܣ�\r\nҵ��-" + traffic.getName() + "(" + traffic.getId() + "): "
						+ traffic.getName() + " �ڽڵ�: " + link.getFromNode().getName() + "����" + traffic.getNrate()
						+ "G��·�˿ڿ���" + "\r\n");
				return false;
			}
			portList.add(port);
			port.getCarriedTraffic().add(traffic);

			// ����������·��������ĩ�ڵ�����·�˿�
			port = findPort(traffic, PortKind.��·�˿�, link.getToNode(), traffic.getNrate(), link.getFromNode());// ����˿�
			if (port == null) {
				portFallBuffer.append("�˿ڷ���ʧ�ܣ�\r\nҵ��-" + traffic.getName() + "(" + traffic.getId() + ") �ڽڵ�: "
						+ link.getToNode().getName() + "����" + traffic.getNrate() + "G��·�˿ڿ���" + "\r\n");
				return false;
			}
			portList.add(port);
			port.getCarriedTraffic().add(traffic);

		} // ��·�˿ڷ������

		// ��ʼ��portlist��Ķ˿�״̬���и��£�1��2��֧·�˿�
		for (int i = 0; i < portList.size(); i++) {
			port = portList.get(i);
			if (i == 0) {// ������֧·�˿�
				port.setStatus(PortStatus.��·);
				port.setRemainResource(0);
			} else if (i == 1) {// ������֧·�˿�
				port.setStatus(PortStatus.��·);
				port.setRemainResource(0);
			} else {// �����Ķ�����·�˿�
				port.setStatus(PortStatus.ͨ��);
				// if(port.getType().equals(PortType.�̲�))
				// port.setRemainResource(0);//12.14 �̲��˿�bu'fu'yon
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
	 * �˺������ܣ�ΪwdmLink�ҵ�ָ�����ͣ���·/֧·�� ָ�����ʵĶ˿�
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
			// �ҵ��������͵Ŀ���֧·�˿�
			if (port.getKind().equals(kind) && kind.equals(PortKind.֧·�˿�) && port.getNrate() == rate
					&& (port.getStatus().equals(PortStatus.����) || (!port.getStatus().equals(PortStatus.����)
							&& (tra.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION
									|| tra.getProtectLevel() == TrafficLevel.RESTORATION))))
				return port;
			else if (port.getKind().equals(kind) && kind.equals(PortKind.��·�˿�) && (port.getRemainResource() == 1
					|| (port.getRemainResource() != 1 && (tra.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION
							|| tra.getProtectLevel() == TrafficLevel.RESTORATION)))
					&& (port.getStatus().equals(PortStatus.����) || port.getStatus().equals(PortStatus.ͨ��))
					&& direction.equals(port.getDirection()))
				return port;
//			CommonAlloc.fallBuffer.append("ҵ��:" + tra +":"+port.getKind()+ "�˿�"+port.getId()+"����·�ɼ���Դ����ɹ���\r\n");
		}
		CommonAlloc.fallBuffer.append("shibeile");
		return null;
	}

//    /**
//     * �˺�������Ϊ�ҵ���ָ���ڵ�ָ��type������/�̲�/���ǣ���ָ�����ͣ���·/֧·�� ��ָ�����ʵĶ˿�
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
//	case ����:
//	    it = node.getFiberPortList();
//	    for (Port port : it) {
//		// �ҵ��������͵Ŀ���֧·�˿�
//		if (port.getKind().equals(kind) && kind.equals(PortKind.֧·�˿�) && port.getNrate() == rate
//			&& port.getStatus().equals(PortStatus.����))
//		    return port;
//		// �ҵ��������͵���·�˿ڣ���Ҫ�ж�ʣ����Դ�Ƿ����ҵ������ʣ�ע���ǿ��Ը��õ�
//		else if (port.getKind().equals(kind) && kind.equals(PortKind.��·�˿�) && (port.getRemainResource() >= rate)
//			&& (port.getStatus().equals(PortStatus.����) || port.getStatus().equals(PortStatus.ͨ��))
//			&& direction.equals(port.getDirection()))
//		    return port;
//	    }
//	    return null;
//	default:
//	    return null;
//	}
//	// for(Port port:it){//�����һ���ж�һ��type��kind���Ѿ�����洢��
//	// //�ҵ��������͵Ŀ���֧·�˿�
//	// if(port.getKind().equals(kind) && kind.equals(PortKind.֧·�˿�)&&
//	// port.getNrate()==rate
//	// && port.getStatus().equals(PortStatus.����)) return port;
//	// //�ҵ��������͵���·�˿ڣ���Ҫ�ж�ʣ����Դ�Ƿ����ҵ������ʣ�ע���ǿ��Ը��õ�
//	// else if
//	// (port.getType().equals(PortType.����)&&port.getKind().equals(kind) &&
//	// kind.equals(PortKind.��·�˿�)
//	// && (port.getRemainResource()>rate/0.155) &&
//	// (port.getStatus().equals(PortStatus.����)||port.getStatus().equals(PortStatus.ͨ��))
//	// && direction.equals(port.getDirection()))return port;
//	// //�ҵ������������͵���·�˿ڣ���Ϊ���ܸ�������ֻҪ�ж�ʣ����Դ�Ƿ�Ϊ1
//	// else if
//	// (!port.getType().equals(PortType.����)&&port.getKind().equals(kind) &&
//	// kind.equals(PortKind.��·�˿�)
//	// && (port.getRemainResource()==1) &&
//	// (port.getStatus().equals(PortStatus.����)||port.getStatus().equals(PortStatus.ͨ��))
//	// && direction.equals(port.getDirection()))return port;
//	// }//ѭ������
//	// return null;
//    }

	/*public static PortType LinkType2Port(LinkType type) {// ����·����ת��Ϊ�˿�����
		switch (type) {
		case Fiber:
			return PortType.����;
		case Satellite:
			return PortType.����;
		case ShortWave:
			return PortType.�̲�;
		default:
			break;
		}
		return null;

	}*/

}
