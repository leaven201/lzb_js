package data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import enums.Layer;

public class LinkRGroup implements Serializable {

	private int ID; // ������·���
	private String name; // ������·������
	private Layer belongLayer; // ������
	private boolean isNatrue = true; // �Ƿ����Զ����ɵģ�Ĭ����
	public static boolean srlg = true;// ��־�Ƿ���srlg
	public static boolean clicked;// �Ƿ����ˡ��Զ�ӳ�䡱,false��ʾû�е��

	private List<FiberLink> SRLGFiberLinkList = null; // Fiber����·�ķ�����·���������·
	private List<WDMLink> SRLGWDMLinkList = null; // WDM����·�ķ�����·���������·
	// private List<FiberLink> m_lBasicLinkList = null; //
	// ��������ʾ������·�������ڵ�Fiber��ֻ�Ը߲�������壬�ԵͲ�����䱾��
	// // �����Զ�ӳ������д����ԣ���Ϊ���ɵ�û��
	public static List<LinkRGroup> SRLGroupList = new LinkedList<LinkRGroup>(); // �洢���з�����·��ľ�̬List

	@Override
	public String toString() {
		return "LinkRGroup [ID=" + ID + ", name=" + name + "]";
	}
	
	//�������һ��SRLG��
		public static String PutOutSRLG(List<FiberLink> temp) {
			StringBuilder s=new StringBuilder();
			HashSet<String> set = new HashSet<>();
			set.add(temp.get(0).getName());
			s.append(temp.get(0).getName());
			for(int i=1;i<temp.size();i++) {
				if(!set.contains(temp.get(i).getName())) {
					set.add(temp.get(i).getName());
				    s.append("&"+temp.get(i).getName());
				}
			}
			return s.toString();
		}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		result = prime * result + ((SRLGWDMLinkList == null) ? 0 : SRLGWDMLinkList.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LinkRGroup other = (LinkRGroup) obj;
		if (ID != other.ID)
			return false;
		if (SRLGWDMLinkList == null) {
			if (other.SRLGWDMLinkList != null)
				return false;
		} else if (!SRLGWDMLinkList.equals(other.SRLGWDMLinkList))
			return false;
		return true;
	}

	public LinkRGroup(int nID, String layer, String name) {
		ID = nID;
		belongLayer = Layer.valueOf(layer);
		switch (belongLayer) {
		case Fiber:
			SRLGFiberLinkList = new LinkedList<FiberLink>();
			isNatrue = false;
			break;
		case WDM:
			SRLGWDMLinkList = new LinkedList<WDMLink>();
			break;
		}
		if (SRLGFiberLinkList == null)
			SRLGFiberLinkList = new LinkedList<FiberLink>();
		this.name = name;

	}

	public LinkRGroup(int nID, String layer, String name, FiberLink fl) {
		ID = nID;
		belongLayer = Layer.valueOf(layer);
		switch (belongLayer) {
		case Fiber:
			SRLGFiberLinkList = new LinkedList<FiberLink>();
			isNatrue = false;
			break;
		case WDM:
			SRLGWDMLinkList = new LinkedList<WDMLink>();
			break;
		default:
			break;
		}
		this.name = name;
		if (SRLGFiberLinkList == null)
			SRLGFiberLinkList = new LinkedList<FiberLink>();
		SRLGFiberLinkList.add(fl);

	}

	public LinkRGroup(String name) {
		this.name = name;
		if (SRLGFiberLinkList == null)
			SRLGFiberLinkList = new LinkedList<FiberLink>();
	}

	public static LinkRGroup getLinkRGroup(int id) {
		Iterator<LinkRGroup> it = SRLGroupList.iterator();
		while (it.hasNext()) {
			LinkRGroup SRLG = it.next();
			if (SRLG.getID() == id)
				return SRLG;
		}
		return null;
	}

	public static LinkRGroup getSRLG(String name) {
		for (int i = 0; i < SRLGroupList.size(); i++) {
			if (SRLGroupList.get(i).getName().equals(name)) {
				return SRLGroupList.get(i);
			}
		}
		return null;
	}

	public void addFiberLink(FiberLink fl) {
		if (SRLGFiberLinkList == null)
			SRLGFiberLinkList = new LinkedList<FiberLink>();
		SRLGFiberLinkList.add(fl);
		fl.getFiberRelatedList().add(this);
	}

	public void addWDMLink(WDMLink wl) {
		if (SRLGWDMLinkList == null)
			SRLGWDMLinkList = new LinkedList<WDMLink>();
		SRLGWDMLinkList.add(wl);
		wl.getWdmRelatedList().add(this);
	}

	public static void automation() {
		//2017.11.23 wb
//		for(LinkRGroup lg:SRLGroupList) {
//			if(lg.getBelongLayer().equals(Layer.WDM)) {
//				SRLGroupList.remove(lg);
//			}
//		}
		Iterator<LinkRGroup> it=SRLGroupList.iterator();
		while(it.hasNext()) {
			LinkRGroup lg=it.next();
			if(lg.getBelongLayer().equals(Layer.WDM))
				it.remove();
		}
		
		for (int i = 0; i < SRLGroupList.size(); i++)// ��FIBER SRLG����ӳ��
		{
			
			if (SRLGroupList.get(i).getBelongLayer().equals(Layer.Fiber)) {
				System.out.println("fiber1");
				List<FiberLink> fl = SRLGroupList.get(i).getSRLGFiberLinkList();
				// System.out.println(fl);
				int a = SRLGroupList.get(i).getID();
				// WDM fiberid+10000;10000���� Ϊ������
				String str = "W-SRG" + a;
				a = a + 10000;
				
				LinkRGroup wsrg = new LinkRGroup(a, "WDM", str);
				for (int j = 0; j < fl.size(); j++)// �Ը�FIBER��srlg���е�ÿ��fiberlinkѭ��
				{
					FiberLink temp = fl.get(j);
					// System.out.println(temp.getCarriedWDMLinkList());
					for (int k = 0; k < temp.getCarriedWDMLinkList().size(); k++)// ȷ�����ص���·û���������ظ�
					{
						if (!wsrg.getSRLGWDMLinkList().contains(temp.getCarriedWDMLinkList().get(k))) {
							wsrg.addlWDMLinkList(temp.getCarriedWDMLinkList().get(k));
							wsrg.getSRLGFiberLinkList().add(temp);
						}
					}

				}
				if (wsrg.getSRLGWDMLinkList().size() > 1)
					if (!SRLGroupList.contains(wsrg))
						SRLGroupList.add(wsrg);
//				System.out.println(wsrg.getSRLGFiberLinkList());
//				System.out.println(wsrg.getSRLGWDMLinkList());
			}
		}

		for (int i = 0; i < FiberLink.fiberLinkList.size(); i++)// ����fiberlinkӳ��,����ͬһfiberLink��wdmLink��ͬһsrlg(������û���������)
		{
			if (FiberLink.fiberLinkList.get(i).getFiberRelatedList().size() == 0)// ����srlg����,Ϊ�˱����ظ����� ��������
			{
				FiberLink fl = FiberLink.fiberLinkList.get(i);
				int a = fl.getId();
				// WDM fiberid+100;100�������� Ϊ������
				String str = "W-SRG" + a;
				a = a + 100;
				LinkRGroup wsrg = new LinkRGroup(a, "WDM", str, fl);
				for (int k = 0; k < fl.getCarriedWDMLinkList().size(); k++)// ȷ�����ص���·û���������ظ�
				{
					if (!wsrg.getSRLGWDMLinkList().contains(fl.getCarriedWDMLinkList().get(k)))
						wsrg.addlWDMLinkList(fl.getCarriedWDMLinkList().get(k));
				}

				if (wsrg.getSRLGWDMLinkList().size() > 1)
					SRLGroupList.add(wsrg);
				else if (wsrg.getSRLGWDMLinkList().size() == 1)
					wsrg.delWDMLinkList(wsrg.getSRLGWDMLinkList().get(0));

			}
		}
		
		
	}

	public static void clear() {
		ListIterator<LinkRGroup> it = SRLGroupList.listIterator();
		while (it.hasNext()) {
			LinkRGroup temp = it.next();
			if (temp == null || temp.isNatrue()) {
				switch (temp.getBelongLayer()) {
				case WDM:
					while (temp.getSRLGWDMLinkList().size() != 0)
						temp.delWDMLinkList(temp.getSRLGWDMLinkList().get(0));
					// System.out.println(SRLGWDMLinkList.get(1));
					break;
				case Fiber:
					while (temp.getSRLGFiberLinkList().size() != 0)
						temp.dellFiberLinkList(temp.getSRLGFiberLinkList().get(0));
					break;
				}
				it.remove();
			}

		}
	}

	public void addlFiberLinkList(FiberLink e) {
		if (belongLayer == Layer.Fiber) {
			SRLGFiberLinkList.add(e);
			e.getFiberRelatedList().add(this);
		}

	}

	public void addlWDMLinkList(WDMLink e) {
		if (belongLayer.equals(Layer.WDM)) {
			SRLGWDMLinkList.add(e);
			e.getWdmRelatedList().add(this);
		}

	}

	public void dellFiberLinkList(FiberLink e) {
		SRLGFiberLinkList.remove(e);
		e.getFiberRelatedList().remove(this);
	}

	public void delWDMLinkList(WDMLink e) {
		SRLGWDMLinkList.remove(e);
		e.getWdmRelatedList().remove(this);

	}

	public static void reflash() {
		for (int i = 0; i < SRLGroupList.size(); i++) {
			switch (SRLGroupList.get(i).getBelongLayer()) {
			case Fiber:
				while (SRLGroupList.get(i).getSRLGFiberLinkList().contains(null)) {
					SRLGroupList.get(i).getSRLGFiberLinkList().remove(null);
				}
				if (SRLGroupList.get(i).getSRLGFiberLinkList().size() < 2)
					SRLGroupList.remove(i);
				break;
			case WDM:
				while (SRLGroupList.get(i).getSRLGWDMLinkList().contains(null)) {
					SRLGroupList.get(i).getSRLGWDMLinkList().remove(null);
				}
				if (SRLGroupList.get(i).getSRLGWDMLinkList().size() < 2)
					SRLGroupList.remove(i);
				break;

			}
		}

	}

	/**
	 * ������ͨ��FIBER��ѯ���Ӧ���ϲ�srg��
	 * 
	 * @param fl:��Ҫ���Ҷ�ӦSRG���FIBERLink
	 * @param layer����Ҫ��õ�srg������
	 * @return
	 */
	public static LinkRGroup find(FiberLink fl, Layer layer) {

		for (int i = 0; i < SRLGroupList.size(); i++) {
			if (SRLGroupList.get(i).getBelongLayer().equals(layer)) {
				for (int k = 0; k < SRLGroupList.get(i).getSRLGFiberLinkList().size(); k++) {
					if (SRLGroupList.get(i).getSRLGFiberLinkList().get(k) == fl)
						return SRLGroupList.get(i);
				}

			}
		}
		int a = fl.getId();
		String str;
		if (layer.equals(Layer.WDM)) {
			str = "W-SRG" + a;
			a = a + 10000;
			LinkRGroup srg = new LinkRGroup(a, "WDM", str, fl);
			return srg;
		} else {
			str = "����Ĳ���д���Ƿ���";
			LinkRGroup srg = new LinkRGroup(a, "ASON", str);
			return srg;
		}
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Layer getBelongLayer() {
		return belongLayer;
	}

	public void setBelongLayer(Layer belongLayer) {
		this.belongLayer = belongLayer;
	}

	public boolean isNatrue() {
		return isNatrue;
	}

	public void setNatrue(boolean isNatrue) {
		this.isNatrue = isNatrue;
	}

	public List<WDMLink> getSRLGWDMLinkList() {
		return SRLGWDMLinkList;
	}

	public void setSRLGWDMLinkList(List<WDMLink> sRLGWDMLinkList) {
		SRLGWDMLinkList = sRLGWDMLinkList;
	}

	public static List<LinkRGroup> getSRLGroupList() {
		return SRLGroupList;
	}

	public static void setSRLGroupList(List<LinkRGroup> sRLGroupList) {
		SRLGroupList = sRLGroupList;
	}

	public List<FiberLink> getSRLGFiberLinkList() {
		return SRLGFiberLinkList;
	}

	public void setSRLGFiberLinkList(List<FiberLink> sRLGFiberLinkList) {
		SRLGFiberLinkList = sRLGFiberLinkList;
	}

}
