package data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import enums.Layer;

public class LinkRGroup implements Serializable {

	private int ID; // 风险链路组号
	private String name; // 风险链路组名称
	private Layer belongLayer; // 所属层
	private boolean isNatrue = true; // 是否是自动生成的，默认是
	public static boolean srlg = true;// 标志是否考虑srlg
	public static boolean clicked;// 是否点击了“自动映射”,false表示没有点击

	private List<FiberLink> SRLGFiberLinkList = null; // Fiber层链路的风险链路组包含的链路
	private List<WDMLink> SRLGWDMLinkList = null; // WDM层链路的风险链路组包含的链路
	// private List<FiberLink> m_lBasicLinkList = null; //
	// 本变量表示，该链路组所基于的Fiber链只对高层次有意义，对低层就是其本身，
	// // 仅与自动映射的组有此属性，人为生成的没有
	public static List<LinkRGroup> SRLGroupList = new LinkedList<LinkRGroup>(); // 存储所有风险链路组的静态List

	@Override
	public String toString() {
		return "LinkRGroup [ID=" + ID + ", name=" + name + "]";
	}
	
	//用于输出一个SRLG组
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
		
		for (int i = 0; i < SRLGroupList.size(); i++)// 按FIBER SRLG进行映射
		{
			
			if (SRLGroupList.get(i).getBelongLayer().equals(Layer.Fiber)) {
				System.out.println("fiber1");
				List<FiberLink> fl = SRLGroupList.get(i).getSRLGFiberLinkList();
				// System.out.println(fl);
				int a = SRLGroupList.get(i).getID();
				// WDM fiberid+10000;10000设置 为了区别
				String str = "W-SRG" + a;
				a = a + 10000;
				
				LinkRGroup wsrg = new LinkRGroup(a, "WDM", str);
				for (int j = 0; j < fl.size(); j++)// 对该FIBER层srlg组中的每条fiberlink循环
				{
					FiberLink temp = fl.get(j);
					// System.out.println(temp.getCarriedWDMLinkList());
					for (int k = 0; k < temp.getCarriedWDMLinkList().size(); k++)// 确定承载的链路没有在组中重复
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

		for (int i = 0; i < FiberLink.fiberLinkList.size(); i++)// 按照fiberlink映射,共享同一fiberLink的wdmLink在同一srlg(本程序没有这种情况)
		{
			if (FiberLink.fiberLinkList.get(i).getFiberRelatedList().size() == 0)// 不在srlg组中,为了避免重复考虑 上面的情况
			{
				FiberLink fl = FiberLink.fiberLinkList.get(i);
				int a = fl.getId();
				// WDM fiberid+100;100随意设置 为了区别
				String str = "W-SRG" + a;
				a = a + 100;
				LinkRGroup wsrg = new LinkRGroup(a, "WDM", str, fl);
				for (int k = 0; k < fl.getCarriedWDMLinkList().size(); k++)// 确定承载的链路没有在组中重复
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
	 * 本函数通过FIBER查询其对应的上层srg组
	 * 
	 * @param fl:需要查找对应SRG组的FIBERLink
	 * @param layer：需要获得的srg所属层
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
			str = "错误的层填写，非法组";
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
