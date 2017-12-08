package database;

import java.io.*;
import java.util.*;

import data.*;
import enums.PortRate;

public class Database {

	private static String fileProperty;

	public static String getFileProperty() {
		return fileProperty;
	}

	public static void setFileProperty(String fileproperty) {
		fileProperty = fileproperty;
	}

	// ���л������ļ�
	public static void serialize(String filename) {

		try {
			// ����һ�������������������������ļ�
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));

			oos.writeObject(CommonNode.allNodeList); // ���л��ڵ�Ŀ¼
			// oos.writeObject(LinkRGroup.s_lRGroupList); //���л�LinkRGroup
			// oos.writeObject(SDHRing.s_lSDHRingList); //���л�SDH��Ŀ¼
			// oos.writeObject(TrafficRGroup.s_lTrafficRGroup); //���л�TrafficRGroup
			// oos.writeObject(WDMSystem.s_lWDMSystemList); //���л�WDMϵͳĿ¼
			oos.writeObject(FiberLink.fiberLinkList); // ���л�Fiber��·Ŀ¼
			// oos.writeObject(SDHLink.s_lSDHLinkList); //���л�SDH��·Ŀ¼
			oos.writeObject(WDMLink.WDMLinkList); // ���л�WDM��·Ŀ¼
			//oos.writeObject(OTNLink.OTNLinkList); // ���л�OTN��·Ŀ¼
			// oos.writeObject(SDHLink.s_lASONLinkList); //���л�ASON��·Ŀ¼
			// oos.writeObject(Network.m_lNetworkList); //���л�������·Ŀ¼
			// oos.writeObject(Traffic.s_lWDMTrafficList); //���л�WDMTraffic��·
			// oos.writeObject(Traffic.s_lSDHTrafficList); //���л�SDHTraffic��·
			// oos.writeObject(Traffic.s_lOTNTrafficList); //���л�OTNTraffic��·
			// oos.writeObject(Traffic.s_lASONTrafficList); //���л�ASONTraffic��·
			// oos.writeObject(Traffic.s_lFiberTrafficList); //���л�FiberTraffic��·
			// oos.writeObject(TrafficGroup.s_lTrafficGroupList); //���л�FiberTraffic��·
			oos.writeObject(Traffic.trafficList); // ���л�trafficList��·
			// oos.writeObject(Domain.s_lDomainList); //���л���
			// lzb
			oos.writeObject(DataSave.flexibleRaster);
			oos.writeObject(DataSave.judge);
			oos.writeObject(DataSave.systemRate);
			oos.writeObject(DataSave.waveNum);
			oos.writeObject(CommonNode.ROADM_NodeList);
//			oos.writeObject(CommonNode.allFiberNodeList);
			oos.writeObject(CommonNode.OLA_NodeList);
			oos.writeObject(TrafficGroup.grouptrafficGroupList); // ���л�TrafficGroup
			// -


			oos.writeObject((String) fileProperty);// ���л��ļ�����28

			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(CommonNode.allNodeList.size());
	}

	// ���ļ������л�������
	public static void deserialize(String fileName) {
		try {
			// ����һ�����������������ļ���ȡ����
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
			List<CommonNode> nl = (List<CommonNode>) (ois.readObject());
			CommonNode.allNodeList = nl;
			// List<LinkRGroup> lrg = (List<LinkRGroup>)(ois.readObject());
			// LinkRGroup.s_lRGroupList = lrg;
			// List<SDHRing> sdhrl = (List<SDHRing>)(ois.readObject());
			// SDHRing.s_lSDHRingList = sdhrl;
			// List<TrafficRGroup> trg = (List<TrafficRGroup>)(ois.readObject());
			// TrafficRGroup.s_lTrafficRGroup = trg;
			// List<WDMSystem> wdms = (List<WDMSystem>)(ois.readObject());
			// WDMSystem.s_lWDMSystemList = wdms;
			LinkedList<FiberLink> fll = (LinkedList<FiberLink>) (ois.readObject());
			FiberLink.fiberLinkList = fll;
			// List<SDHLink> sdhll = (List<SDHLink>)(ois.readObject());
			// SDHLink.s_lSDHLinkList = sdhll;
			LinkedList<WDMLink> wdmll = (LinkedList<WDMLink>) (ois.readObject());
			WDMLink.WDMLinkList = wdmll;
			//LinkedList<OTNLink> otnll = (LinkedList<OTNLink>) (ois.readObject());
			//OTNLink.OTNLinkList = otnll;
			// List<OTNLink> asonll = (List<OTNLink>)(ois.readObject());
			// SDHLink.s_lASONLinkList = asonll;
			// List<Network> nw = (List<Network>)(ois.readObject());
			// Network.m_lNetworkList = nw;
			LinkedList<Traffic> wdmtl = (LinkedList<Traffic>) (ois.readObject());
			Traffic.trafficList = wdmtl;
			// List<Traffic> wdmtl = (List<Traffic>)(ois.readObject());
			// Traffic.WDMTrafficList = wdmtl;
			// List<Traffic> sdhtl = (List<Traffic>)(ois.readObject());
			// Traffic.s_lSDHTrafficList = sdhtl;
			// List<Traffic> otntl = (List<Traffic>)(ois.readObject());
			// Traffic.s_lOTNTrafficList = otntl;
			// List<Traffic> asontl = (List<Traffic>)(ois.readObject());
			// Traffic.s_lASONTrafficList = asontl;
			// List<Traffic> ftl = (List<Traffic>)(ois.readObject());
			// Traffic.s_lFiberTrafficList = ftl;
			// List<TrafficGroup> tr = (List<TrafficGroup>)(ois.readObject());
			// TrafficGroup.s_lTrafficGroupList = tr;
			// List<Domain> domain=(List<Domain>)(ois.readObject());
			// Domain.s_lDomainList=domain;
			// fileProperty=(String)ois.readObject();

			boolean x1 = (Boolean) (ois.readObject());
			DataSave.flexibleRaster = x1;
			PortRate x2 = (PortRate) ois.readObject();
			DataSave.judge = x2;
			int x3 = (Integer) (ois.readObject());
			DataSave.systemRate = x3;
			int x4 = (Integer) (ois.readObject());
			DataSave.waveNum = x4;
			List<CommonNode> rn = (List<CommonNode>) (ois.readObject());
			CommonNode.ROADM_NodeList=rn;
//			List<CommonNode> fn = (List<CommonNode>) (ois.readObject());
//			CommonNode.allFiberNodeList=fn;
			List<CommonNode> on = (List<CommonNode>) (ois.readObject());
			CommonNode.OLA_NodeList=on;
			List<TrafficGroup> trg = (List<TrafficGroup>) (ois.readObject());
			TrafficGroup.grouptrafficGroupList = trg;

			ois.close();
			// System.out.println(wdmnl.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// �����������л��ͷ����л�����
	public static void main(String[] args) {
		serialize("E:\\database.dat");
		System.out.println("���л����");

		deserialize("E:\\database.dat");
		System.out.println("�����л����");
	}

}
