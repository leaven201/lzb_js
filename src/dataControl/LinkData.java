package dataControl;

/******
 * ���������·���ݣ��������ô�excel���������·�ĺ������������½�������·�Լ�ɾ��������·�ĺ���
 * @author CC
 * 
 */
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import data.*;
import database.*;
import enums.*;
import layerroute.AutoRoute;

public class LinkData {
	public LinkData() {
	}

	public String msgw = "";
	public int m_nBasicLink[];// ��������
	public int m_nBasicLinkSize;
	public String m_sBasicLinkLayer;
	public static String s_sFiberMsg = "Fiber��·��Ϣδ����";
	public static String s_sWDMMsg = "WDM��·��Ϣδ����";
	public static String s_sOTNMsg = "������·��Ϣδ����";
	public static String s_serr = "";
	public static double aveLinkLength = 0;// ��·��ƽ������

	public static void clearLink() {

		//BasicLink.allLinkList.clear();
		FiberLink.fiberLinkList.clear();
		WDMLink.WDMLinkList.clear();
		OTNLink.OTNLinkList.clear();

		LinkDataBase.flagf = true;// ������· CC 625
		LinkDataBase.flagw = true;
		LinkDataBase.flago = true;
		NodeDataBase.flag = true;// ����ڵ�
		System.gc();
	}

	public void inputLink(String str) { // ynh:�ӵ����FiberLink������˳�WDMLink
		LinkDataBase link = new LinkDataBase();
		link.inputFiberLink(str);

		{
			FiberLink Rlink = null;
			WDMLink WDMlink = null;
			Iterator<FiberLink> l = FiberLink.fiberLinkList.iterator();
			int k = 0;//wdmlink��Ŀ
			double length = 0;
			while (l.hasNext()) {
				Rlink = l.next();
				length = Rlink.getLength();
				aveLinkLength = Rlink.getLength() + aveLinkLength; // lzb+���о���Ϊ����һ��ƽ����·����
				int id = Rlink.getId();
				CommonNode fromNode = Rlink.getFromNode();
				CommonNode toNode = Rlink.getToNode();
				int sumofosnr = 0;
				if (fromNode.getNodeType().equals(NodeType.ROADM) && toNode.getNodeType().equals(NodeType.ROADM)) {
					WDMlink = new WDMLink(fromNode, toNode, 0, Rlink.getLength(), true);// �½�WDM��·
					k++;
					WDMlink.getFiberLinkList().add(Rlink); // �õ�ÿһ��WDM��·���м���·
					Rlink.getCarriedWDMLinkList().add(WDMlink);// 2017.11.10
					// System.out.println(WDMlink2.fiberLinkList);
				}

				else if (fromNode.getNodeType().equals(NodeType.ROADM) && toNode.getNodeType().equals(NodeType.OLA)) {
					LinkedList<FiberLink> List = new LinkedList<FiberLink>();
					List.add(FiberLink.getFiberLink(id));
					LinkedList<FiberLink> List2 = new LinkedList<FiberLink>();
					List2.add(Rlink);
					CommonNode TNode = toNode;
					int j = id;
					while (TNode.getNodeType().equals(NodeType.OLA)) {
						j++;
						//CommonNode FNode = CommonNode.getNode(TNode.getName());
						Rlink = FiberLink.getFiberLink(j);
						List2.add(Rlink);
						TNode = Rlink.getToNode();
						length = length + Rlink.getLength();
					}
					if (TNode.getNodeType().equals(NodeType.ROADM)) {
						WDMlink = new WDMLink(fromNode, TNode, j, length, true);// �½�WDM��·
						k++;
						WDMlink.setFiberLinkList(List2); // �õ�ÿһ��WDM��·���м���·
						for(FiberLink fiber:List2) {//2017.11.10
							fiber.getCarriedWDMLinkList().add(WDMlink);
						}
						// System.out.println(WDMlink2.fiberLinkList);
					}

				}
			}
			//WDMLinkList���������   ��ʼ����parallelLinkList
			for(int i=0;i<WDMLink.WDMLinkList.size();i++) {
				WDMLink linkk=WDMLink.WDMLinkList.get(i);
				linkk.createParallelLinkList();
			}
			LinkRGroup.automation();
			aveLinkLength = aveLinkLength / FiberLink.fiberLinkList.size();
			msgw = "�ɹ�����" + k + "��WDM��·����" + "\n";
			System.out.println(msgw);
		}

		if (link.msgerr.equals(""))
			s_sFiberMsg = link.msgf;
		else
			s_sFiberMsg = link.msgerr;

		// link.inputWDMLink(str);
		if (link.msgerr.equals(""))
			s_sWDMMsg = link.msgw;
		else
			s_sWDMMsg = link.msgerr;

		// link.inputOTNLink(str);
		if (link.msgerr.equals(""))
			s_sOTNMsg = link.msgo;
		else
			s_sOTNMsg = link.msgerr;

		// if(FiberLink.fiberLinkList.size()!=0){//Ϊwdmlink������·��
		// for(int i=0;i<WDMLink.WDMLinkList.size();i++){
		//
		// if(WDMLink.WDMLinkList.get(i).getFiberLinkList().isEmpty())
		// AutoRoute.WDMAutoRoute(WDMLink.WDMLinkList.get(i));
		// }
		// }
	}

	public void outputLink(String str) {
		LinkDataBase link = new LinkDataBase();
		link.outputLink(str);
		s_sFiberMsg = link.msgf;

		s_serr = link.msgerr;
	}

	public void addLink(int id, String name, CommonNode fromNode, CommonNode toNode, double length, int waveNum,
			boolean isActive, String SRLG, Layer linkLayer, double inPower, double spanLoss, double NF) {
		switch (linkLayer) {
		case Fiber:
			new FiberLink(id, name, fromNode, toNode, length, waveNum, isActive, SRLG, linkLayer, inPower, spanLoss,
					NF);
			break;
		// case WDM:
		// new WDMLink(id, name,fromNode, toNode, length, rate,size,
		// isActive,SRLG,linkLayer);
		// break;
		// case OTN:
		// new OTNLink(id, name,fromNode, toNode, length, rate,size,
		// isActive,SRLG,linkLayer);
		// break;
		default:
			break;

		}

	}

	public boolean delFiberlink(FiberLink fiberlink) {// ֻ�й�����·��Ҫ�ͷ���Դ CC 625

		// if (fiberlink.getCarriedTraffic() != null) // �ͷ�fiberkink�ϵ�ҵ����Դ CC
		// {
		// Iterator<Traffic> it = fiberlink.getCarriedTraffic().iterator();
		// List<Traffic> trList = new LinkedList<Traffic>();
		// trList.addAll(fiberlink.getCarriedTraffic());
		// while (it.hasNext()) {
		// Traffic tr = it.next();
		// tr.releaseTraffic(tr, 0);
		// tr.releasePortSource(tr, 0);
		// }
		// for(int i=0;i<trList.size();++i){
		// Traffic trf=trList.get(i);
		// if(trf.getWorkRoute()!=null)
		// {
		// if(trf.getWorkRoute().getLinkList()!=null)
		// trf.getWorkRoute().clearRoute();
		// }
		// if(trf.getProtectRoute()!=null)
		// {
		// if(trf.getProtectRoute().getLinkList()!=null)
		// trf.getProtectRoute().clearRoute();
		// }
		//
		// }
		// }
		// CC�޸ģ��ӵ�������
		FiberLink.fiberLinkList.remove(fiberlink);
		return true;

	}

	public boolean delOptLink(WDMLink optLink) {
		// TODO �Զ����ɵķ������
		WDMLink.WDMLinkList.remove(optLink);
		return true;
	}

	public boolean delEleLink(OTNLink eleLink) {
		// TODO �Զ����ɵķ������
		OTNLink.OTNLinkList.remove(eleLink);
		return true;
	}

	public void delLink(BasicLink link) {

		if (BasicLink.getCarriedTraffic() != null) // �ͷ�fiberkink�ϵ�ҵ����Դ CC
		{
			Iterator<Traffic> it = link.getCarriedTraffic().iterator();
			List<Traffic> trList = new LinkedList<Traffic>();
			trList.addAll(link.getCarriedTraffic()); // ò�ƺ���û�õ�
			while (it.hasNext()) {
				Traffic tr = it.next();
				tr.releaseTraffic(tr, 0);
				tr.releasePortSource(tr, 0);
			} // end while
				// TODO Auto-generated method stub
				// switch (basicLink.getClass().getName()) {
				// case "data.FiberLink" :delFiberlink((FiberLink) basicLink);
				// case "data.SatelliteLink":delSatelliteLink((SatelliteLink)
				// basicLink);
				// case "data.ShortWaveLink":delShortWaveLink((ShortWaveLink)
				// basicLink);
				//
				// }
		} // end if
		BasicLink.allLinkList.remove(link);
		System.out.println(link.getClass().getName());
		switch (link.getClass().getName()) {// ֵ�ý��
		case "data.FiberLink":
			delFiberlink((FiberLink) link);
			break;
		case "data.EleLink":
			delEleLink((OTNLink) link);
			break;
		case "data.OptLink":
			delOptLink((WDMLink) link);
			break;
		}
	}
}