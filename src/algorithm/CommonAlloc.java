package algorithm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import data.CommonNode;
import data.DataSave;
import data.Route;
import data.Traffic;
import data.WDMLink;
import data.WaveLength;
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

		if (!findWaveLength(route, startWaveLength, startWaveLengthList, wdmLinkList, true)) {//�ò���һ������
//			if (!findWaveLength(route, startWaveLength, startWaveLengthList, wdmLinkList, false))//���ò���һ������
//				return false;
//			LinkedList<LinkedList<Integer>> allcombination = CommonAlloc.WaveAllocNoconsist(route, 1, traffic);
//			startWaveLengthList = CommonAlloc.changedLess(allcombination);
			startWaveLengthList = CommonAlloc.findBestWaveAlloc(route, traffic, 1);
			
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
//			if (!findWaveLength(route, startWaveLength, startWaveLengthList, wdmLinkList, false))
//				// fallBuffer.append("��·û���㹻�Ĳ������Է��䣡\r\n");
//				return false;
//			LinkedList<LinkedList<Integer>> allcombination = CommonAlloc.WaveAllocNoconsist(route, 1, traffic);
//			startWaveLengthList = CommonAlloc.changedLess(allcombination);
			startWaveLengthList = CommonAlloc.findBestWaveAlloc(route, traffic, 1);
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
//			if (!findWaveLength1(route, startWaveLength, startWaveLengthList, wdmLinkList, false, traffic))// ���������һ��Ҳ��������䲻�ɹ�
//				// fallBuffer.append("��·û���㹻�Ĳ������Է��䣡\r\n");
//				return false;
//			LinkedList<LinkedList<Integer>> allcombination = CommonAlloc.WaveAllocNoconsist(route, 2, traffic);
//			startWaveLengthList = CommonAlloc.changedLess(allcombination);
			startWaveLengthList = CommonAlloc.findBestWaveAlloc(route, traffic, 2);
			
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

		if (!findWaveLength1(route, startWaveLength, startWaveLengthList, wdmLinkList, true, traffic)) // ������ò���һ���Ժ�δ�ҵ�
		// �����ò�����һ����ȥ��
		{
			// fallBuffer.append("��·û���㹻�������������Է��䣡\r\n");
//			if (!findWaveLength2(route, startWaveLength, startWaveLengthList, wdmLinkList, false, traffic))// ���������һ��Ҳ��������䲻�ɹ�
//				// fallBuffer.append("��·û���㹻�Ĳ������Է��䣡\r\n");
//				return false;
//			LinkedList<LinkedList<Integer>> allcombination = CommonAlloc.WaveAllocNoconsist(route, 2, traffic);
//			startWaveLengthList = CommonAlloc.changedLess(allcombination);
			startWaveLengthList = CommonAlloc.findBestWaveAlloc(route, traffic, 2);
		}
		startWaveLength = startWaveLengthList.getFirst();
		for (int hop = 0; hop < wdmLinkList.size(); hop++) {// Ϊÿһ��wdmLink��·������������
			int startWL = startWaveLengthList.get(hop);// ������һ��ʱʹ��
			WDMLink wdmLink = wdmLinkList.get(hop);
			 //���ÿ��в���Ϊ������������·
//			if (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.����)
//					|| (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.�ָ�)
//							&& (traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION
//									|| traffic.getProtectLevel() == TrafficLevel.RESTORATION)
//					// && wdmLink.getWaveLengthList().get(startWL).isPre() == true
//					)
//					) {
				if (wdmLink.getWaveLengthList().get(startWL).getStatus().equals(Status.����)) {
					wdmLink.setRemainResource(wdmLink.getRemainResource() - 1);
				} // ������·ʣ����Դ
				wdmLink.getWaveLengthList().get(startWL).setStatus(Status.����);// �ı乤��״̬
				// wdmLink.getWaveLengthList().get(startWL).setCarriedTraffic(traffic);//Ϊ������ӳ���ҵ��
				wdmLink.getWaveLengthList().get(startWL).getDynamicTrafficList().add(traffic);// ����ò����Ķ�̬ҵ���
				//���ò����Ƿ�Ϊ����ʱʹ����Ϊtrue
				//wdmLink.getCarriedTrafficList().add(traffic);// Ϊ��·��ӳ���ҵ��
				// ����ô�ʹ�õ��ǲ���ԭ��״̬�ǿ��в�������·ʣ����Դ�ż��٣����ʹ�õĲ���ԭ��״̬�ǻָ��������򲨵���Դ����
				
					// if (Suggest.isSuggested == true)
					// wdmLink.getWaveLengthList().get(startWL).setUsed(true);// ���ʹ�ù��Ĳ���
					// if (Suggest.isKanghui == true)
					// wdmLink.getWaveLengthList().get(startWL).setYongguo(true);//
					// ��ǿ����ù��Ĳ���2017.10.25
				if (traffic.getProtectLevel() == TrafficLevel.PresetRESTORATION
						|| traffic.getProtectLevel() == TrafficLevel.PROTECTandRESTORATION)
					wdmLink.getWaveLengthList().get(startWL).setPre(true);// 2017.10.14
//			}
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
		if ((start >= data.DataSave.waveNum - DataSave.locknum-DataSave.onlyPre)) {
			// fallBuffer.append(route+"��·û���㹻�������������Է��䣡\r\n");
			return false;
		}

		if (startWaveLength.size() != 0)// �����Ϊ�գ������
			startWaveLength.clear();
		int hopSum = wdmLinkList.size(); // ��¼��·�ɵ�����
		int cc = start;// ��¼start��ʼֵ���Ա���ÿ����·���Գ�ʼֵ��ʼѰ��
		for (int hop = 0; hop < hopSum; hop++) {
			WDMLink wdmLink = wdmLinkList.get(hop);
			for (int i = cc; i < wdmLink.getWaveLengthList().size()- DataSave.locknum-DataSave.onlyPre; i++) {// ��cc��ʼ�ҵ�80���������·�ĵ�cc������Դ���У�
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
		if ((start >= data.DataSave.waveNum - DataSave.locknum)) {
			// fallBuffer.append(route+"��·û���㹻�������������Է��䣡\r\n");
			return false;
		}

		if (startWaveLength.size() != 0)// �����Ϊ�գ������
			startWaveLength.clear();
		int hopSum = wdmLinkList.size(); // ��¼��·�ɵ�����
		int cc = start;// ��¼start��ʼֵ���Ա���ÿ����·���Գ�ʼֵ��ʼѰ��
		for (int hop = 0; hop < hopSum; hop++) {
			WDMLink wdmLink = wdmLinkList.get(hop);
			for (int i = cc; i < wdmLink.getWaveLengthList().size()- DataSave.locknum; i++) {// ��cc��ʼ�ҵ�80���������·�ĵ�cc������Դ���У�
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
								&& Route.isPreWaveCanShared(tra, wdmLink.getWaveLengthList().get(i).preTrafficList)))
				{
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
				return findWaveLength2(route, max, startWaveLength, wdmLinkList, true, tra);
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
	//�Ӳ��������乤��·�ɺͱ���·��
		public static boolean EallocateWaveLength_workandpro(Route route) {
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

			if (!EfindWaveLength_workandpro(route, startWaveLength, startWaveLengthList, wdmLinkList, true,traffic)) {
				if (!EfindWaveLength_workandpro(route, startWaveLength, startWaveLengthList, wdmLinkList, false,traffic))
					return false;
			}
			startWaveLength = startWaveLengthList.getFirst();
			for (int hop = 0; hop < wdmLinkList.size(); hop++) {// Ϊÿһ��wdmLink��·������������
				int startWL = startWaveLengthList.get(hop);// ������һ��ʱʹ��
				WDMLink wdmLink = wdmLinkList.get(hop);
				
				//ֻҪ���������������˵�����Ǳ������Ӳ�������ģ���˶�Ӧ�Ĳ���״̬����Ϊ�Ӳ���
				wdmLink.getWaveLengthList().get(startWL).setStatus(Status.�Ӳ���);// �ı乤��״̬
				wdmLink.getWaveLengthList().get(startWL).carriedTrafficList.add(traffic);// Ϊ������ӳ���ҵ��
				wdmLink.getCarriedTrafficList().add(traffic);// Ϊ��·��ӳ���ҵ��
				//���²�����Դ��Ϣ
				wdmLink.getWaveLengthList().get(startWL).updateWaveResource();
				//������·��Դ��Ϣ
				wdmLink.updateLinkResoucre();
				
				
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
		
		//�Ӳ�������Ԥ��·��
		public static boolean EallocateWaveLength_pre(Route route) {
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

			if (!EfindWaveLength_pre(route, startWaveLength, startWaveLengthList, wdmLinkList, true,traffic)) {
				if (!EfindWaveLength_pre(route, startWaveLength, startWaveLengthList, wdmLinkList, false,traffic))
					return false;
			}
			startWaveLength = startWaveLengthList.getFirst();
			for (int hop = 0; hop < wdmLinkList.size(); hop++) {// Ϊÿһ��wdmLink��·������������
				int startWL = startWaveLengthList.get(hop);// ������һ��ʱʹ��
				WDMLink wdmLink = wdmLinkList.get(hop);
				
				//ֻҪ���������������˵�����Ǳ������Ӳ�������ģ���˶�Ӧ�Ĳ���״̬����Ϊ�Ӳ���
				wdmLink.getWaveLengthList().get(startWL).setStatus(Status.�Ӳ���);// �ı乤��״̬
				wdmLink.getWaveLengthList().get(startWL).getPreTrafficList().add(traffic);// Ϊ������ӳ���ҵ��
				wdmLink.getCarriedTrafficList().add(traffic);// Ϊ��·��ӳ���ҵ��
				//���²�����Դ��Ϣ
				wdmLink.getWaveLengthList().get(startWL).updateWaveResource();
				//������·��Դ��Ϣ
				wdmLink.updateLinkResoucre();
				
				
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
		
		//�Ӳ���--Ϊ�����ͱ���·��Ѱ�ҿ��ò���
		public static boolean EfindWaveLength_workandpro(Route route, int start,
				LinkedList<Integer> startWaveLengthList, List<WDMLink> wdmLinkList, boolean isConsistent,Traffic traffic) {
			
			boolean useSubWave=false;//�ж��Ƿ�ʹ�����Ӳ���
			if ((start >= data.DataSave.waveNum)) {
				// fallBuffer.append(route+"��·û���㹻�������������Է��䣡\r\n");
				return false;
			}
			if (startWaveLengthList.size() != 0)// ���洢��ҵ����ÿ����·�����ò����ŵı��Ƿ�Ϊ��
				startWaveLengthList.clear();
			int hopSum = wdmLinkList.size(); // ��¼��·�ɵ�����
			int cc = start;// ��¼start��ʼֵ���Ա���ÿ����·���Գ�ʼֵ��ʼѰ��
			
			//�����Ƿ��������ʹ���Ӳ�����Ƭ
			for(int hop=0;hop<hopSum;hop++) {//ѭ��1��ѭ��·��ÿһ����·
				WDMLink wdmLink = wdmLinkList.get(hop);
				for (int i = 0; i < wdmLink.getWaveLengthList().size(); i++) {//ѭ��2��ѭ����·ÿһ������
					WaveLength wave=wdmLink.getWaveLengthList().get(i);
					//�жϸò����Ƿ��ѱ��Ӳ���ҵ�����ù�
					if(wave.getStatus().equals(Status.�Ӳ���)) {
						//�жϸ�wave��route��ÿһ�����Ƿ������������
						if(Route.isRouteCanUseWave(route, wave, traffic)) {//trueΪ���ã�������startWaveLengthList(0-79)
							for(int o=0;o<hopSum;o++) {
								startWaveLengthList.add(i);
							}
							useSubWave=true;
							break;//����ѭ��2
						}
					}
				}
				if(useSubWave)
					break;//����ѭ��1
			}
			
			
			
			//�����������Ӳ�����ʱ���ֱ�Ӱ�����һ����ȥ�Ҿ���
			if(!useSubWave) {
			
				for (int hop = 0; hop < hopSum; hop++) {
					WDMLink wdmLink = wdmLinkList.get(hop);
					for (int i = cc; i < wdmLink.getWaveLengthList().size(); i++) {// ��cc��ʼ�ҵ�79���������·�ĵ�cc������Դ���У�
																					// ��cc���루�����ҵ����ÿ����·���õĲ����ı��startWaveLengh
																					// Ȼ��ȥ��һ����Ѱ�Ҳ���
						if (wdmLink.getWaveLengthList().get(i).getStatus().equals(Status.����)) {
							startWaveLengthList.add(i);
							cc = i;// �ı��ʼ�������
							break;
						}
					}
				}
			}
			
			if (startWaveLengthList.size() != hopSum) {// �����߲����ʱ��˵��ĳ��wdmLinkû�п��õĲ�����Դ
				return false;
			}

			// ����������Ƿ�Ҫ���в���һ�����ж�
			if (isConsistent == true) {
				/**
				 * ��������ÿ����·���貨�����һ���ж�
				 */    
				
				int max = startWaveLengthList.getFirst();
				int min = max;
				for (int i : startWaveLengthList) {
					max = max > i ? max : i; // ѭ����maxΪstartWaveLength�е����ֵ
					min = min < i ? min : i; // ѭ����minΪstartWaveLength�е���Сֵ
				}
				if (max != min) {// ��ʾ��·��ʼ������Ų�ȫһ���������ֵ��ʼ������
					startWaveLengthList.clear();
					return findWaveLength(route, max, startWaveLengthList, wdmLinkList, true);
				} else  //max==min,˵������һ�£�return true˵������һ�·���ɹ�
					return true;
			} else {// ����Ҫ�󲨳�һ���ԣ���ֱ�ӷ���true
				route.setNotConsistent(true);
				return true;
			}
			//return false;
		}
		
		
		//�Ӳ���--ΪԤ��·��Ѱ�ҿ��ò���
			public static boolean EfindWaveLength_pre(Route route, int start,
					LinkedList<Integer> startWaveLengthList, List<WDMLink> wdmLinkList, boolean isConsistent,Traffic traffic) {
				
				boolean useSubWave=false;//�ж��Ƿ�ʹ�����Ӳ���
				if ((start >= data.DataSave.waveNum)) {
					// fallBuffer.append(route+"��·û���㹻�������������Է��䣡\r\n");
					return false;
				}
				if (startWaveLengthList.size() != 0)// ���洢��ҵ����ÿ����·�����ò����ŵı��Ƿ�Ϊ��
					startWaveLengthList.clear();
				int hopSum = wdmLinkList.size(); // ��¼��·�ɵ�����
				int cc = start;// ��¼start��ʼֵ���Ա���ÿ����·���Գ�ʼֵ��ʼѰ��
				
				//�����Ƿ��������ʹ���Ӳ�����Ƭ
				for(int hop=0;hop<hopSum;hop++) {//ѭ��1��ѭ��·��ÿһ����·
					WDMLink wdmLink = wdmLinkList.get(hop);
					for (int i = 0; i < wdmLink.getWaveLengthList().size(); i++) {//ѭ��2��ѭ����·ÿһ������
						WaveLength wave=wdmLink.getWaveLengthList().get(i);
						//�жϸò����Ƿ��ѱ��Ӳ���ҵ�����ù�
						if(wave.getStatus().equals(Status.�Ӳ���)) {
							//�жϸ�wave��route��ÿһ�����Ƿ������������
							if(Route.isRouteCanUseWave_shared(route, wave, traffic)) {//trueΪ���ã�������startWaveLengthList(0-79)
								for(int o=0;o<hopSum;o++) {
									startWaveLengthList.add(i);
								}
								useSubWave=true;
								break;//����ѭ��2
							}
						}
					}
					if(useSubWave)
						break;//����ѭ��1
				}
				
				
				
				//�����������Ӳ�����ʱ���ֱ�Ӱ�����һ����ȥ�Ҿ���
				if(!useSubWave) {
				
					for (int hop = 0; hop < hopSum; hop++) {
						WDMLink wdmLink = wdmLinkList.get(hop);
						for (int i = cc; i < wdmLink.getWaveLengthList().size(); i++) {// ��cc��ʼ�ҵ�79���������·�ĵ�cc������Դ���У�
																						// ��cc���루�����ҵ����ÿ����·���õĲ����ı��startWaveLengh
																						// Ȼ��ȥ��һ����Ѱ�Ҳ���
							if (wdmLink.getWaveLengthList().get(i).getStatus().equals(Status.����)
									|| (wdmLink.getWaveLengthList().get(i).getStatus().equals(Status.�ָ�)
											&& Route.isPreWaveCanShared(traffic, wdmLink.getWaveLengthList().get(i).preTrafficList))) {
								startWaveLengthList.add(i);
								cc = i;// �ı��ʼ�������
								break;
							}
						}
					}
				}
				
				if (startWaveLengthList.size() != hopSum) {// �����߲����ʱ��˵��ĳ��wdmLinkû�п��õĲ�����Դ
					return false;
				}

				// ����������Ƿ�Ҫ���в���һ�����ж�
				if (isConsistent == true) {
					/**
					 * ��������ÿ����·���貨�����һ���ж�
					 */    
					
					int max = startWaveLengthList.getFirst();
					int min = max;
					for (int i : startWaveLengthList) {
						max = max > i ? max : i; // ѭ����maxΪstartWaveLength�е����ֵ
						min = min < i ? min : i; // ѭ����minΪstartWaveLength�е���Сֵ
					}
					if (max != min) {// ��ʾ��·��ʼ������Ų�ȫһ���������ֵ��ʼ������
						startWaveLengthList.clear();
						return findWaveLength(route, max, startWaveLengthList, wdmLinkList, true);
					} else  //max==min,˵������һ�£�return true˵������һ�·���ɹ�
						return true;
				} else {// ����Ҫ�󲨳�һ���ԣ���ֱ�ӷ���true
					route.setNotConsistent(true);
					return true;
				}
				//return false;
			}
			
			
	//������һ�����޷�����ʱ�Ĳ���������ԣ���Ҫ�����ٵĲ���ת��  flag: 1��������   2Ԥ��
	public static LinkedList<LinkedList<Integer>> WaveAllocNoconsist(Route route,int flag, Traffic tra) {
		LinkedList<LinkedList<Integer>> list = new LinkedList();
		for(int i=0; i<route.getWDMLinkList().size();i++) {
			WDMLink link = route.getWDMLinkList().get(i);
			LinkedList<Integer> wavelist = new LinkedList();
			for(int j=0;j<link.getWaveLengthList().size();j++) {
				WaveLength wave = link.getWaveLengthList().get(j);
				if(flag == 1) {//��������
					if(wave.getStatus().equals(Status.����)) {
						wavelist.add(j);
					}
				}
				else if(flag == 2) {//Ԥ��
					if(wave.getStatus().equals(Status.����)// �ò�����ԴΪ����
							// �ò�����״̬�ǻָ������Ҹò������ص�ҵ���·�����·�ɷ��룬���Ҹ�ҵ���Ԥ�ù�����share
							|| (wave.getStatus().equals(Status.�ָ�)
									&& Route.isPreWaveCanShared(tra, wave.preTrafficList))) {
						wavelist.add(j);
					}
				}
			}
			list.add(wavelist);
		}
		return allCombination(list);
		
	}
	//n��list ÿ��list��ȡ1�����������
	public static LinkedList<LinkedList<Integer>> allCombination(LinkedList<LinkedList<Integer>> list) {
		int hop = list.size();
		LinkedList<Integer> list1 = list.get(0);
		LinkedList<Integer> list2 = list.get(1);
		int len1 = list.get(0).size();
		int len2 = list.get(1).size();
		LinkedList<LinkedList<Integer>> Combination = new LinkedList();
		for(int i=0; i<len1; i++) {
			for(int j=0; j<len2; j++) {
				LinkedList<Integer> newList = new LinkedList();
				newList.add(list1.get(i));
				newList.add(list2.get(j));
				Combination.add(newList);
			}
		}
		
		int index = 2;		
//		LinkedList<LinkedList<Integer>> allCombination = new LinkedList();
//		allCombination = combine(Combination, list, hop, index);
		return combine(Combination, list, hop, index);
	}
	
	public static LinkedList<LinkedList<Integer>> combine(LinkedList<LinkedList<Integer>> list1,LinkedList<LinkedList<Integer>> list,int hop, int index){
		
		if(list1.get(0).size() == hop) {
			return list1;
		}
		
		LinkedList<Integer> list2 = list.get(index);
		int len1 = list1.size();
		int len2 = list2.size();
		
		LinkedList<LinkedList<Integer>> thelist = new LinkedList();
		
		for(int i=0; i<len1; i++) {
			for(int j=0; j<len2; j++) {
				LinkedList<Integer> newList = new LinkedList();
				newList.addAll(list1.get(i));
				newList.add(list2.get(j));
				thelist.add(newList);
			}
		}
		
		return combine(thelist, list, hop, index+1);
	}
	
	public static LinkedList<Integer> changedLess(LinkedList<LinkedList<Integer>> combination){
		LinkedList<Integer> less = new LinkedList();
		int min = 100;
		for(LinkedList<Integer> i : combination) {
			int sum = sumOfChange(i);
			if(sum<min) {
				less = i;
			}
		}
		return less;
	}
	public static int sumOfChange(LinkedList<Integer> list) {
		int sum=0;
		for(int i=1;i<list.size();i++) {
			if(list.get(i-1)!=list.get(i)) {
				sum++;
			}
		}
		return sum;
	}
	//��������wave��·�ɵ�startindex��ʼ�� ������������
	public static int sumOfWaveiConsist(int wave,Route route, int startIndex,Traffic tra,int flag) {
		int sum = 0;
		for(int i=startIndex; i<route.getWDMLinkList().size();i++) {
			WDMLink link = route.getWDMLinkList().get(i);
			if(link.isLinkHaveWavei(wave, flag, tra)) {
				sum++;
			}else {
				return sum;
			}
		}
		return sum;
	}
	//�����ӵ�i����ʼ���ĸ�����������������õ�    res[0] ��������  res[1]������0-79��
	public static int[] bestWave(Route route,Traffic tra,int startIndex,int flag) {
		int[] res = new int[2];
		res[0]=res[1]=-1;
		for(int i=0;i<80;i++) {
			int sum = sumOfWaveiConsist(i+1, route, startIndex, tra, flag);
			if(sum>res[0]) {
				res[0] = sum;
				res[1] = i;
			}
		}
		return res;
	}
	
	
	//�ҳ�route��ʹ���м����ٵĲ������    // �жϸ���·�Ƿ񲨵�i���� flag=1(����/����) flag=2(Ԥ��)
	public static LinkedList<Integer> findBestWaveAlloc(Route route,Traffic tra, int flag) {
		LinkedList<Integer> best = new LinkedList();
		int[] res = bestWave(route, tra, 0, flag);
		for(int i=0;i<res[0];i++) {
			best.add(res[1]);
		}
		while(best.size() < route.getWDMLinkList().size()){
			res = bestWave(route, tra, best.size(), flag);
			for(int i=0;i<res[0];i++) {
				best.add(res[1]);
			}
		}
		return best;
	}
	
	
}
