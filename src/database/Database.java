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

	// 序列化对象到文件
	public static void serialize(String filename) {

		try {
			// 创建一个对象输出流，将对象输出到文件
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename));

			oos.writeObject(CommonNode.allNodeList); // 序列化节点目录
			// oos.writeObject(LinkRGroup.s_lRGroupList); //序列化LinkRGroup
			// oos.writeObject(SDHRing.s_lSDHRingList); //序列化SDH环目录
			// oos.writeObject(TrafficRGroup.s_lTrafficRGroup); //序列化TrafficRGroup
			// oos.writeObject(WDMSystem.s_lWDMSystemList); //序列化WDM系统目录
			oos.writeObject(FiberLink.fiberLinkList); // 序列化Fiber链路目录
			// oos.writeObject(SDHLink.s_lSDHLinkList); //序列化SDH链路目录
			oos.writeObject(WDMLink.WDMLinkList); // 序列化WDM链路目录
			//oos.writeObject(OTNLink.OTNLinkList); // 序列化OTN链路目录
			// oos.writeObject(SDHLink.s_lASONLinkList); //序列化ASON链路目录
			// oos.writeObject(Network.m_lNetworkList); //序列化网络链路目录
			// oos.writeObject(Traffic.s_lWDMTrafficList); //序列化WDMTraffic链路
			// oos.writeObject(Traffic.s_lSDHTrafficList); //序列化SDHTraffic链路
			// oos.writeObject(Traffic.s_lOTNTrafficList); //序列化OTNTraffic链路
			// oos.writeObject(Traffic.s_lASONTrafficList); //序列化ASONTraffic链路
			// oos.writeObject(Traffic.s_lFiberTrafficList); //序列化FiberTraffic链路
			// oos.writeObject(TrafficGroup.s_lTrafficGroupList); //序列化FiberTraffic链路
			oos.writeObject(Traffic.trafficList); // 序列化trafficList链路
			// oos.writeObject(Domain.s_lDomainList); //序列化域
			// lzb
			oos.writeObject(DataSave.flexibleRaster);
			oos.writeObject(DataSave.judge);
			oos.writeObject(DataSave.systemRate);
			oos.writeObject(DataSave.waveNum);
			oos.writeObject(CommonNode.ROADM_NodeList);
//			oos.writeObject(CommonNode.allFiberNodeList);
			oos.writeObject(CommonNode.OLA_NodeList);
			oos.writeObject(TrafficGroup.grouptrafficGroupList); // 序列化TrafficGroup
			// -


			oos.writeObject((String) fileProperty);// 序列化文件属性28

			oos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(CommonNode.allNodeList.size());
	}

	// 从文件反序列化到对象
	public static void deserialize(String fileName) {
		try {
			// 创建一个对象输入流，从文件读取对象
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

	// 运行上述序列化和反序列化函数
	public static void main(String[] args) {
		serialize("E:\\database.dat");
		System.out.println("序列化完毕");

		deserialize("E:\\database.dat");
		System.out.println("反序列化完毕");
	}

}
