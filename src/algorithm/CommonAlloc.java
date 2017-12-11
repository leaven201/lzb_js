package algorithm;

import java.util.LinkedList;
import java.util.List;

import data.CommonNode;
import data.Route;
import data.Traffic;
import data.WDMLink;
import enums.Status;
import enums.TrafficLevel;

/**
 * ���ܣ����в�����Դ�ķ��� ˼·����ҵ��·����wdmLink��Ӧ��fiberLink���в������䣬ͬһwdmLink�����㲨��һ����
 * 
 * @author CC
 * @since 2016/7/4
 */
public class CommonAlloc {
	// public static boolean notConsistent;// �Ƿ�һ��
	public static StringBuffer fallBuffer = new StringBuffer();// �����ݴ���Դ����ʧ�����

	// ���乤��������Դ
	public static boolean allocateWaveLength(Route route) {
		List<WDMLink> wdmLinkList = route.getWDMLinkList(); // wdmLinkList�����·�ɵ�WDM��·
		if (wdmLinkList.size() == 0) {// Ϊʲô�����ж�Ϊ�գ�
			// return true;
			return false;
		}
		Traffic traffic = route.getBelongsTraffic();// ·������ҵ��
		LinkedList<Integer> startWaveLengthList = new LinkedList<Integer>();// ���ÿ����ϵͳ�����е���ʼ����
		int startWaveLength = 0;
		for (WDMLink wdmLink : wdmLinkList) {
			if (traffic.getNrate() > wdmLink.getNrate()) {// �ж��Ƿ���������ǰ���������
				fallBuffer.append("ҵ��(" + traffic.getFromNode().getName() + "-" + traffic.getToNode().getName()
						+ ")������<" + traffic.getNrate() + "G>���ڹ�����· (" + wdmLink.getFromNode().getName() + "-"
						+ wdmLink.getToNode().getName() + ")������<" + wdmLink.getNrate() + "G>\r\n");// ȥ��ǿ��ת����·����Ϊ����
				return false;
			} else if (wdmLink.getRemainResource() < 1) {// �����Ƿ�����
				Traffic.failtrafficList.add(traffic);
				fallBuffer.append(
						"ҵ��(" + traffic.getFromNode().getName() + "-" + traffic.getToNode().getName() + ")���貨����Դ����");
				return false;
			}
		} // ͨ������ѭ����֪ҵ���������ʼ�������ԴҪ�󣬿�ʼ���в�������

		if (!findWaveLength(route, startWaveLength, startWaveLengthList, wdmLinkList, true)) {
			if (!findWaveLength(route, startWaveLength, startWaveLengthList, wdmLinkList, false))
				return false;
		}
		startWaveLength = startWaveLengthList.getFirst();
		for (int hop = 0; hop < wdmLinkList.size(); hop++) {// Ϊÿһ��wdmLink��·������������
			int startWL = startWaveLengthList.get(hop);// ������һ��ʱʹ��
			WDMLink wdmLink = wdmLinkList.get(hop);
			// ���ÿ��в���Ϊ������������·
			// �������·�ĵ�startWL����״̬Ϊ���У����ߵ�startWL����״̬Ϊ�����Ҹ�ҵ��ı����ȼ�Ϊ��·�ɲ��Ҹò�����Ԥ����
			if (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.����)
					|| (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.����)
							&& (traffic.getProtectLevel() == TrafficLevel.PresetRESTORATION
									|| traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION)
							&& wdmLink.getWaveLengthList().get(startWL).isPre() == true)) {
				wdmLink.getWaveLengthList().get(startWL).setStatus(Status.����);// �ı乤��״̬
				wdmLink.getWaveLengthList().get(startWL).setCarriedTraffic(traffic);// Ϊ������ӳ���ҵ��
				wdmLink.getCarriedTrafficList().add(traffic);// Ϊ��·��ӳ���ҵ��
				wdmLink.setRemainResource(wdmLink.getRemainResource() - 1);// ������·ʣ����Դ
				// if (Suggest.isSuggested == true)
				// wdmLink.getWaveLengthList().get(startWL).setUsed(true);// ���ʹ�ù��Ĳ���
				// if (Suggest.isKanghui == true)
				// wdmLink.getWaveLengthList().get(startWL).setYongguo(true);//
				// ��ǿ����ù��Ĳ���2017.10.25
				// if (traffic.getProtectLevel() == TrafficLevel.PresetRESTORATION)
				// wdmLink.getWaveLengthList().get(startWL).setPre(true);// 2017.10.14
			}
		} // �����������
			/////////////////////////////////////
			// ����в���ת���ڵ㣬������startWaveLength=0;

		// fallBuffer.append(route.getBelongsTraffic() + "������Դ����ɹ���\r\n ");
		// route.setFiberWLID(startWaveLengthList.getFirst());//�в���ת�����ܣ���һ���ƺ�û��
		route.setWaveLengthIdList(startWaveLengthList);// �ָ���Դʱ���õ�
		route.setUsedWaveList(startWaveLengthList); // ���õĲ�ͬ�����������м̻��õ�
		route.setWaveChangedNode(startWaveLengthList);
		// if (route.isNotConsistent() == true) {// ���������һ��
		// if (Suggest.isSuggested == true)// 2017.10.14
		// judge(route);
		// if(Suggest.isKanghui==true)
		// judge2(route);
		// }
		return true;

	}

	// ���䱣��������Դ
	public static boolean allocateWaveLength1(Route route) {
		List<WDMLink> wdmLinkList = route.getWDMLinkList(); // wdmLinkList�����·�ɵ�WDM��·
		if (wdmLinkList.size() == 0) {// Ϊʲô�����ж�Ϊ�գ�
			// return true;
			return false;
		}
		Traffic traffic = route.getBelongsTraffic();// ·������ҵ��
		LinkedList<Integer> startWaveLengthList = new LinkedList<Integer>();// ���ÿ����ϵͳ�����е���ʼ����
		int startWaveLength = 0;
		for (WDMLink wdmLink : wdmLinkList) {
			if (traffic.getNrate() > wdmLink.getNrate()) {// �ж��Ƿ���������ǰ���������
				fallBuffer.append("ҵ��(" + traffic.getFromNode().getName() + "-" + traffic.getToNode().getName()
						+ ")������<" + traffic.getNrate() + "G>���ڹ�����· (" + wdmLink.getFromNode().getName() + "-"
						+ wdmLink.getToNode().getName() + ")������<" + wdmLink.getNrate() + "G>\r\n");// ȥ��ǿ��ת����·����Ϊ����
				return false;
			} else if (wdmLink.getRemainResource() < 1) {// �����Ƿ�����

				fallBuffer.append(
						"ҵ��(" + traffic.getFromNode().getName() + "-" + traffic.getToNode().getName() + ")���貨����Դ����");
				return false;
			}
		} // ͨ������ѭ����֪ҵ���������ʼ�������ԴҪ�󣬿�ʼ���в�������

		if (!findWaveLength(route, startWaveLength, startWaveLengthList, wdmLinkList, true)) {
			if (!findWaveLength(route, startWaveLength, startWaveLengthList, wdmLinkList, false))
				// fallBuffer.append("��·û���㹻�Ĳ������Է��䣡\r\n");
				return false;
		}
		startWaveLength = startWaveLengthList.getFirst();
		for (int hop = 0; hop < wdmLinkList.size(); hop++) {// Ϊÿһ��wdmLink��·������������
			int startWL = startWaveLengthList.get(hop);// ������һ��ʱʹ��
			WDMLink wdmLink = wdmLinkList.get(hop);
			// ���ÿ��в���Ϊ������������·
			if (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.����)
					|| (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.����)
							&& traffic.getProtectLevel() == TrafficLevel.PresetRESTORATION
							&& wdmLink.getWaveLengthList().get(startWL).isPre() == true)) {
				wdmLink.getWaveLengthList().get(startWL).setStatus(Status.����);// �ı乤��״̬
				wdmLink.getWaveLengthList().get(startWL).setCarriedTraffic(traffic);// Ϊ������ӳ���ҵ��
				wdmLink.getCarriedTrafficList().add(traffic);// Ϊ��·��ӳ���ҵ��
				wdmLink.setRemainResource(wdmLink.getRemainResource() - 1);// ������·ʣ����Դ
				// if (Suggest.isSuggested == true)
				// wdmLink.getWaveLengthList().get(startWL).setUsed(true);// ���ʹ�ù��Ĳ���
				// if (Suggest.isKanghui == true)
				// wdmLink.getWaveLengthList().get(startWL).setYongguo(true);//
				// ��ǿ����ù��Ĳ���2017.10.25
				// if (traffic.getProtectLevel() == TrafficLevel.PresetRESTORATION)
				// wdmLink.getWaveLengthList().get(startWL).setPre(true);// 2017.10.14
			}
		} // �����������
			/////////////////////////////////////
			// ����в���ת���ڵ㣬������startWaveLength=0;

		// fallBuffer.append(route.getBelongsTraffic() + "������Դ����ɹ���\r\n ");
		// route.setFiberWLID(startWaveLengthList.getFirst());//�в���ת�����ܣ���һ���ƺ�û��
		route.setWaveLengthIdList(startWaveLengthList);// �ָ���Դʱ���õ�
		route.setUsedWaveList(startWaveLengthList); // ���õĲ�ͬ�����������м̻��õ�
		route.setWaveChangedNode1(startWaveLengthList); // �洢���в���ת���Ľڵ�
		// if (route.isNotConsistent() == true) {// ���������һ��
		// if (Suggest.isSuggested == true)// 2017.10.14
		// judge(route);
		// if(Suggest.isKanghui==true)
		// judge2(route);
		// }
		return true;

	}

	// ����Ԥ�ûָ�������Դ
	public static boolean allocateWaveLength2(Route route) {
		List<WDMLink> wdmLinkList = route.getWDMLinkList(); // wdmLinkList�����·�ɵ�WDM��·
		if (wdmLinkList.size() == 0) {// Ϊʲô�����ж�Ϊ�գ�
			// return true;
			return false;
		}
		Traffic traffic = route.getBelongsTraffic();// ·������ҵ��
		LinkedList<Integer> startWaveLengthList = new LinkedList<Integer>();// ���ÿ����ϵͳ�����е���ʼ����
		int startWaveLength = 0;
		for (WDMLink wdmLink : wdmLinkList) {
			if (traffic.getNrate() > wdmLink.getNrate()) {// �ж��Ƿ���������ǰ���������
				fallBuffer.append("ҵ��(" + traffic.getFromNode().getName() + "-" + traffic.getToNode().getName()
						+ ")������<" + traffic.getNrate() + "G>���ڹ�����· (" + wdmLink.getFromNode().getName() + "-"
						+ wdmLink.getToNode().getName() + ")������<" + wdmLink.getNrate() + "G>\r\n");// ȥ��ǿ��ת����·����Ϊ����
				return false;
			} else if (wdmLink.getRemainResource() < 1) {// �����Ƿ�����

				fallBuffer.append(
						"ҵ��(" + traffic.getFromNode().getName() + "-" + traffic.getToNode().getName() + ")���貨����Դ����");
				return false;
			}
		} // ͨ������ѭ����֪ҵ���������ʼ�������ԴҪ�󣬿�ʼ���в�������

		if (!findWaveLength1(route, startWaveLength, startWaveLengthList, wdmLinkList, true, traffic)) // ������ò���һ���Ժ�δ�ҵ�
		// �����ò�����һ����ȥ��
		{
			// fallBuffer.append("��·û���㹻�������������Է��䣡\r\n");
			if (!findWaveLength1(route, startWaveLength, startWaveLengthList, wdmLinkList, false, traffic))// ���������һ��Ҳ��������䲻�ɹ�
				// fallBuffer.append("��·û���㹻�Ĳ������Է��䣡\r\n");
				return false;
		}
		startWaveLength = startWaveLengthList.getFirst();
		for (int hop = 0; hop < wdmLinkList.size(); hop++) {// Ϊÿһ��wdmLink��·������������
			int startWL = startWaveLengthList.get(hop);// ������һ��ʱʹ��
			WDMLink wdmLink = wdmLinkList.get(hop);
			// ���ÿ��в���Ϊ������������·
			if (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.����)
					|| (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.�ָ�)
							&& (traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION
									|| traffic.getProtectLevel() == TrafficLevel.RESTORATION)
					// && wdmLink.getWaveLengthList().get(startWL).isPre() == true
					)) {
				if (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.����)) {
					wdmLink.setRemainResource(wdmLink.getRemainResource() - 1);
				} // ������·ʣ����Դ
				wdmLink.getWaveLengthList().get(startWL).setStatus(Status.�ָ�);// �ı乤��״̬
				// wdmLink.getWaveLengthList().get(startWL).setCarriedTraffic(traffic);//Ϊ������ӳ���ҵ��
				wdmLink.getWaveLengthList().get(startWL).getPreTrafficList().add(traffic);// ����ò�����Ԥ��ҵ���
				wdmLink.getCarriedTrafficList().add(traffic);// Ϊ��·��ӳ���ҵ��
				// ����ô�ʹ�õ��ǲ���ԭ��״̬�ǿ��в�������·ʣ����Դ�ż��٣����ʹ�õĲ���ԭ��״̬�ǻָ��������򲨵���Դ����
				
					// if (Suggest.isSuggested == true)
					// wdmLink.getWaveLengthList().get(startWL).setUsed(true);// ���ʹ�ù��Ĳ���
					// if (Suggest.isKanghui == true)
					// wdmLink.getWaveLengthList().get(startWL).setYongguo(true);//
					// ��ǿ����ù��Ĳ���2017.10.25
				if (traffic.getProtectLevel() == TrafficLevel.PresetRESTORATION
						|| traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION)
					wdmLink.getWaveLengthList().get(startWL).setPre(true);// 2017.10.14
			}
		} // �����������
			/////////////////////////////////////
			// ����в���ת���ڵ㣬������startWaveLength=0;

		// fallBuffer.append(route.getBelongsTraffic() + "������Դ����ɹ���\r\n ");
		// route.setFiberWLID(startWaveLengthList.getFirst());//�в���ת�����ܣ���һ���ƺ�û��
		route.setWaveLengthIdList(startWaveLengthList);// �ָ���Դʱ���õ�
		route.setUsedWaveList(startWaveLengthList); // ���õĲ�ͬ�����������м̻��õ�
		route.setWaveChangedNode1(startWaveLengthList); // �洢���в���ת���Ľڵ�
		// if (route.isNotConsistent() == true) {// ���������һ��
		// if (Suggest.isSuggested == true)// 2017.10.14
		// judge(route);
		// if(Suggest.isKanghui==true)
		// judge2(route);
		// }
		return true;

	}
	
	//��̬�ָ���������
	public static boolean allocateWaveLength3(Route route) {
		List<WDMLink> wdmLinkList = route.getWDMLinkList(); // wdmLinkList�����·�ɵ�WDM��·
		if (wdmLinkList.size() == 0) {// Ϊʲô�����ж�Ϊ�գ�
			// return true;
			return false;
		}
		Traffic traffic = route.getBelongsTraffic();// ·������ҵ��
		LinkedList<Integer> startWaveLengthList = new LinkedList<Integer>();// ���ÿ����ϵͳ�����е���ʼ����
		int startWaveLength = 0;
		for (WDMLink wdmLink : wdmLinkList) {
			if (traffic.getNrate() > wdmLink.getNrate()) {// �ж��Ƿ���������ǰ���������
				fallBuffer.append("ҵ��(" + traffic.getFromNode().getName() + "-" + traffic.getToNode().getName()
						+ ")������<" + traffic.getNrate() + "G>���ڹ�����· (" + wdmLink.getFromNode().getName() + "-"
						+ wdmLink.getToNode().getName() + ")������<" + wdmLink.getNrate() + "G>\r\n");// ȥ��ǿ��ת����·����Ϊ����
				return false;
			} else if (wdmLink.getRemainResource() < 1) {// �����Ƿ�����

				fallBuffer.append(
						"ҵ��(" + traffic.getFromNode().getName() + "-" + traffic.getToNode().getName() + ")���貨����Դ����");
				return false;
			}
		} // ͨ������ѭ����֪ҵ���������ʼ�������ԴҪ�󣬿�ʼ���в�������

		if (!findWaveLength2(route, startWaveLength, startWaveLengthList, wdmLinkList, true, traffic)) // ������ò���һ���Ժ�δ�ҵ�
		// �����ò�����һ����ȥ��
		{
			// fallBuffer.append("��·û���㹻�������������Է��䣡\r\n");
			if (!findWaveLength2(route, startWaveLength, startWaveLengthList, wdmLinkList, false, traffic))// ���������һ��Ҳ��������䲻�ɹ�
				// fallBuffer.append("��·û���㹻�Ĳ������Է��䣡\r\n");
				return false;
		}
		startWaveLength = startWaveLengthList.getFirst();
		for (int hop = 0; hop < wdmLinkList.size(); hop++) {// Ϊÿһ��wdmLink��·������������
			int startWL = startWaveLengthList.get(hop);// ������һ��ʱʹ��
			WDMLink wdmLink = wdmLinkList.get(hop);
			// ���ÿ��в���Ϊ������������·
			if (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.����)
					|| (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.�ָ�)
							&& (traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION
									|| traffic.getProtectLevel() == TrafficLevel.RESTORATION)
					// && wdmLink.getWaveLengthList().get(startWL).isPre() == true
					)) {
				if (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.����)) {
					wdmLink.setRemainResource(wdmLink.getRemainResource() - 1);
				} // ������·ʣ����Դ
				wdmLink.getWaveLengthList().get(startWL).setStatus(Status.�ָ�);// �ı乤��״̬
				// wdmLink.getWaveLengthList().get(startWL).setCarriedTraffic(traffic);//Ϊ������ӳ���ҵ��
				wdmLink.getWaveLengthList().get(startWL).getDynamicTrafficList().add(traffic);// ����ò����Ķ�̬ҵ���
				wdmLink.getCarriedTrafficList().add(traffic);// Ϊ��·��ӳ���ҵ��
				// ����ô�ʹ�õ��ǲ���ԭ��״̬�ǿ��в�������·ʣ����Դ�ż��٣����ʹ�õĲ���ԭ��״̬�ǻָ��������򲨵���Դ����
				
					// if (Suggest.isSuggested == true)
					// wdmLink.getWaveLengthList().get(startWL).setUsed(true);// ���ʹ�ù��Ĳ���
					// if (Suggest.isKanghui == true)
					// wdmLink.getWaveLengthList().get(startWL).setYongguo(true);//
					// ��ǿ����ù��Ĳ���2017.10.25
				if (traffic.getProtectLevel() == TrafficLevel.PresetRESTORATION
						|| traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION)
					wdmLink.getWaveLengthList().get(startWL).setPre(true);// 2017.10.14
			}
		} // �����������
			/////////////////////////////////////
			// ����в���ת���ڵ㣬������startWaveLength=0;

		// fallBuffer.append(route.getBelongsTraffic() + "������Դ����ɹ���\r\n ");
		// route.setFiberWLID(startWaveLengthList.getFirst());//�в���ת�����ܣ���һ���ƺ�û��
		route.setWaveLengthIdList(startWaveLengthList);// �ָ���Դʱ���õ�
		route.setUsedWaveList(startWaveLengthList); // ���õĲ�ͬ�����������м̻��õ�
		route.setWaveChangedNode1(startWaveLengthList); // �洢���в���ת���Ľڵ�
		// if (route.isNotConsistent() == true) {// ���������һ��
		// if (Suggest.isSuggested == true)// 2017.10.14
		// judge(route);
		// if(Suggest.isKanghui==true)
		// judge2(route);
		// }
		return true;

	}
	

	// ����Ѱ�ҹ����ͱ���·�ɲ����ķ���
	public static boolean findWaveLength(Route route, int start, LinkedList<Integer> startWaveLength,
			List<WDMLink> wdmLinkList, boolean isConsistent) {
		// isConsitent��ʾ�Ƿ�Ҫ�󲨳�һ���ԣ�trueΪ���ǣ�falseΪ������
		// if (start >= fiberLinkList.get(0).getWaveNum()) {
		// start=0
		if ((start >= data.DataSave.waveNum)) {
			// fallBuffer.append(route+"��·û���㹻�������������Է��䣡\r\n");
			return false;
		}

		if (startWaveLength.size() != 0)// �����Ϊ�գ������
			startWaveLength.clear();
		int hopSum = wdmLinkList.size(); // ��¼��·�ɵ�����
		int cc = start;// ��¼start��ʼֵ���Ա���ÿ����·���Գ�ʼֵ��ʼѰ��
		for (int hop = 0; hop < hopSum; hop++) {
			WDMLink wdmLink = wdmLinkList.get(hop);
			for (int i = cc; i < wdmLink.getWaveLengthList().size(); i++) {// ��cc��ʼ�ҵ�80���������·�ĵ�cc������Դ���У�
																			// ��cc���루�����ҵ����ÿ����·���õĲ����ı��startWaveLengh
																			// Ȼ��ȥ��һ����Ѱ�Ҳ���
				if (wdmLink.getWaveLengthList().get(i).getStatus().equals(Status.����)) {
					startWaveLength.add(i);
					cc = i;// �ı��ʼ�������
					break;
				}
			}
		}
		if (startWaveLength.size() != hopSum) {// �����߲����ʱ��˵��ĳ��wdmLinkû�п��õĲ�����Դ
			return false;
		}

		// ����������Ƿ�Ҫ���в���һ�����ж�
		if (isConsistent == true) {
			/**
			 * ��������ÿ����·���貨�����һ���ж�
			 */       
			int max = startWaveLength.getFirst();
			int min = max;
			for (int i : startWaveLength) {
				max = max > i ? max : i; // ѭ����maxΪstartWaveLength�е����ֵ
				min = min < i ? min : i; // ѭ����minΪstartWaveLength�е���Сֵ
			}
			if (max != min) {// ��ʾ��·��ʼ������Ų�ȫһ���������ֵ��ʼ������
				startWaveLength.clear();
				return findWaveLength(route, max, startWaveLength, wdmLinkList, true);
			} else  //max==min,˵������һ�£�return true˵������һ�·���ɹ�
				return true;
		} else {// ����Ҫ�󲨳�һ���ԣ���ֱ�ӷ���true
			route.setNotConsistent(true);
			return true;
		}
	}

	// ����Ѱ��Ԥ��·�ɲ����ķ���
	public static boolean findWaveLength1(Route route, int start, LinkedList<Integer> startWaveLength,
			List<WDMLink> wdmLinkList, boolean isConsistent, Traffic tra) {
		// isConsitent��ʾ�Ƿ�Ҫ�󲨳�һ���ԣ�trueΪ���ǣ�falseΪ������
		// if (start >= fiberLinkList.get(0).getWaveNum()) {
		// start=0
		if ((start >= data.DataSave.waveNum)) {
			// fallBuffer.append(route+"��·û���㹻�������������Է��䣡\r\n");
			return false;
		}

		if (startWaveLength.size() != 0)// �����Ϊ�գ������
			startWaveLength.clear();
		int hopSum = wdmLinkList.size(); // ��¼��·�ɵ�����
		int cc = start;// ��¼start��ʼֵ���Ա���ÿ����·���Գ�ʼֵ��ʼѰ��
		for (int hop = 0; hop < hopSum; hop++) {
			WDMLink wdmLink = wdmLinkList.get(hop);
			for (int i = cc; i < wdmLink.getWaveLengthList().size(); i++) {// ��cc��ʼ�ҵ�80���������·�ĵ�cc������Դ���У�
																			// ��cc���루�����ҵ����ÿ����·���õĲ����ı��startWaveLengh
																			// Ȼ��ȥ��һ����Ѱ�Ҳ���
				if (wdmLink.getWaveLengthList().get(i).getStatus().equals(Status.����)// �ò�����ԴΪ����
						// �ò�����״̬�ǻָ������Ҹò������ص�ҵ���·�����·�ɷ��룬���Ҹ�ҵ���Ԥ�ù�����share
						|| (wdmLink.getWaveLengthList().get(i).getStatus().equals(Status.�ָ�)
								&& Route.isPreWaveCanShared(tra, wdmLink.getWaveLengthList().get(i).preTrafficList))) {
					startWaveLength.add(i);
					cc = i;// �ı��ʼ�������
					break;
				}
			}
		}
		if (startWaveLength.size() != hopSum) {// �����߲����ʱ��˵��ĳ��wdmLinkû�п��õĲ�����Դ
			return false;
		}

		// ����������Ƿ�Ҫ���в���һ�����ж�
		if (isConsistent == true) {
			/**
			 * ��������ÿ����·���貨�����һ���ж�
			 */
			int max = startWaveLength.getFirst();
			int min = max;
			for (int i : startWaveLength) {
				max = max > i ? max : i; // ѭ����maxΪstartWaveLength�е����ֵ
				min = min < i ? min : i; // ѭ����minΪstartWaveLength�е���Сֵ
			}
			if (max != min) {// ��ʾ��·��ʼ������Ų�ȫһ���������ֵ��ʼ������
				startWaveLength.clear();
				return findWaveLength1(route, max, startWaveLength, wdmLinkList, true, tra);
			} else
				return true;
		} else {// ����Ҫ�󲨳�һ���ԣ���ֱ�ӷ���true
			route.setNotConsistent(true);
			return true;
		}
	}
	
	// ����Ѱ�Ҷ�̬�ָ�·�ɲ����ķ���
	public static boolean findWaveLength2(Route route, int start, LinkedList<Integer> startWaveLength,
			List<WDMLink> wdmLinkList, boolean isConsistent, Traffic tra) {
		// isConsitent��ʾ�Ƿ�Ҫ�󲨳�һ���ԣ�trueΪ���ǣ�falseΪ������
		// if (start >= fiberLinkList.get(0).getWaveNum()) {
		// start=0
		if ((start >= data.DataSave.waveNum)) {
			// fallBuffer.append(route+"��·û���㹻�������������Է��䣡\r\n");
			return false;
		}

		if (startWaveLength.size() != 0)// �����Ϊ�գ������
			startWaveLength.clear();
		int hopSum = wdmLinkList.size(); // ��¼��·�ɵ�����
		int cc = start;// ��¼start��ʼֵ���Ա���ÿ����·���Գ�ʼֵ��ʼѰ��
		for (int hop = 0; hop < hopSum; hop++) {
			WDMLink wdmLink = wdmLinkList.get(hop);
			for (int i = cc; i < wdmLink.getWaveLengthList().size(); i++) {// ��cc��ʼ�ҵ�80���������·�ĵ�cc������Դ���У�
																			// ��cc���루�����ҵ����ÿ����·���õĲ����ı��startWaveLengh
																			// Ȼ��ȥ��һ����Ѱ�Ҳ���
				if (wdmLink.getWaveLengthList().get(i).getStatus().equals(Status.����)// �ò�����ԴΪ����
						// �ò�����״̬�ǻָ������Ҹò������ص�ҵ���·�����·�ɷ��룬���Ҹ�ҵ���Ԥ�ù�����share
						|| (wdmLink.getWaveLengthList().get(i).getStatus().equals(Status.�ָ�)
								&& Route.isPreWaveCanShared(tra, wdmLink.getWaveLengthList().get(i).dynamicTrafficList))) {
					startWaveLength.add(i);
					cc = i;// �ı��ʼ�������
					break;
				}
			}
		}
		if (startWaveLength.size() != hopSum) {// �����߲����ʱ��˵��ĳ��wdmLinkû�п��õĲ�����Դ
			return false;
		}

		// ����������Ƿ�Ҫ���в���һ�����ж�
		if (isConsistent == true) {
			/**
			 * ��������ÿ����·���貨�����һ���ж�
			 */
			int max = startWaveLength.getFirst();
			int min = max;
			for (int i : startWaveLength) {
				max = max > i ? max : i; // ѭ����maxΪstartWaveLength�е����ֵ
				min = min < i ? min : i; // ѭ����minΪstartWaveLength�е���Сֵ
			}
			if (max != min) {// ��ʾ��·��ʼ������Ų�ȫһ���������ֵ��ʼ������
				startWaveLength.clear();
				return findWaveLength1(route, max, startWaveLength, wdmLinkList, true, tra);
			} else
				return true;
		} else {// ����Ҫ�󲨳�һ���ԣ���ֱ�ӷ���true
			route.setNotConsistent(true);
			return true;
		}
	}

	/*
	 * // �ж���Ҫ����ת�����ܵĽڵ� public static void judge(Route route) { if (route == null)
	 * return; int k = route.getWaveLengthIdList().get(0); for (int i = 1; i <
	 * route.getWaveLengthIdList().size(); i++) { if (k !=
	 * route.getWaveLengthIdList().get(i)) { CommonNode node =
	 * route.getWDMLinkList().get(i).getFromNode(); if (Suggest.flag == 1) {//
	 * ���ٲ���ת�� if (!Suggest.conversionNodeList.contains(node))// ȥ��
	 * Suggest.conversionNodeList.add(node); // return; } else {// ����ת�� if
	 * (!Suggest.conversionNodeList.contains(node))// ȥ��
	 * Suggest.conversionNodeList.add(node); if
	 * (!Suggest.conversionNodeList2.contains(node))// ȥ��
	 * Suggest.conversionNodeList2.add(node); // return; } } } }
	 */
	/*
	 * // �ж���Ҫ����ת�����ܵĽڵ�,���ٷ���ר��--2017.10.26 public static void judge2(Route route) {
	 * if (route == null) return; int k = route.getWaveLengthIdList().get(0); for
	 * (int i = 1; i < route.getWaveLengthIdList().size(); i++) { if (k !=
	 * route.getWaveLengthIdList().get(i)) { CommonNode node =
	 * route.getWDMLinkList().get(i).getFromNode(); if
	 * (!Suggest.converNodeKanghuiList.contains(node))// ȥ��
	 * Suggest.converNodeKanghuiList.add(node); } } }
	 */
}
