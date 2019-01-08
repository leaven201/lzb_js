package data;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import algorithm.OSNR;
import enums.Status;

/**
 * ·��
 * 
 * @author ����ʫ��
 *
 */
public class Route implements Serializable{

	private int id;
	private String name;
	private CommonNode from;
	private CommonNode to;
	private int hops;// ����������·��
	private Traffic belongsTraffic; // ����ҵ��
	private double Length;//·�ɳ���
	private boolean isSuccess; // �Ƿ����·��
	private boolean notConsistent;// �Ƿ񲨳���һ��
	private List<Port> SimPortList; // ģ�����ռ�õĶ˿�
	private List<Port> portList; // ռ�õĶ˿ڣ���������֧·�˿ں�hops������·�˿�


	private List<CommonNode> nodeList = new LinkedList<>();// �洢·���а��������з�OA�ڵ�
	private List<WDMLink> WDMLinkList = new LinkedList<WDMLink>();
	private List<FiberLink> fiberLinkList = new LinkedList<FiberLink>();
	private List<Integer> waveLengthIdList = new LinkedList<>();// �洢·��ÿ����·ռ�õĲ������List
	private List<Integer> usedWaveList = new LinkedList<>();// ʹ�ò���,��·�����˼�����ͬ�Ĳ������������м�
	private List<CommonNode> waveChangedNode = new LinkedList<>();// ��Ų����ı��˵Ľڵ�

	// private int fiberTSID = -1; //����������·ռ�õ���ʼʱ϶����Ϊʱ϶һ����������һ����
	// private int fiberWLID = -1; // ������·ռ�õ���ʼ��������Ϊ����һ����������һ����
	// private List<BasicLink> linkList = new LinkedList<BasicLink>();// �洢·���а�������·
	// private List<WDMLink2> WDMLinkList2 = new LinkedList<WDMLink2>();
	// private List<OTNLink> OTNLinkList = new LinkedList<OTNLink>();

	// 10.31 lzb+
	// ���췽��
	public Route(CommonNode from, CommonNode to, List<WDMLink> linkList, List<CommonNode> nodeList,
			Traffic belongsTraffic, int flag) {
		this.from = from;
		this.to = to;
		this.WDMLinkList = linkList;
		this.nodeList = nodeList;
		this.belongsTraffic = belongsTraffic;

	}// -

	// 11.27 lzb+
	public Route() {

	}

	public Route(List<WDMLink> linkList, List<CommonNode> nodeList) {
		this.WDMLinkList = linkList;
		this.nodeList = nodeList;
	}

	// ���췽���������ֹ�����ҵ��·��
	public Route(CommonNode from, CommonNode to, List<WDMLink> linkList, List<CommonNode> nodeList) {
		this.from = from;
		this.to = to;
		this.WDMLinkList = linkList;
		this.nodeList = nodeList;

	}
	public Route(CommonNode from, CommonNode to, List<WDMLink> linkList, List<CommonNode> nodeList,Traffic traffic) {
		this.from = from;
		this.to = to;
		this.WDMLinkList = linkList;
		this.nodeList = nodeList;
		this.belongsTraffic = traffic;
	}

	// ���췽��
	public Route(CommonNode from, CommonNode to, List<FiberLink> fiberLinkList, List<CommonNode> nodeList, int hops,
			Traffic belongsTraffic) {// ��Ҫ���ù��˽�����·
		this.from = from;
		this.to = to;
		this.fiberLinkList = fiberLinkList;
		this.nodeList = nodeList;
		this.hops = hops;
		this.belongsTraffic = belongsTraffic;
	}

	// ���췽��
	// public Route(CommonNode from, CommonNode to, List<FiberLink> fiberLinkList,
	// List<CommonNode> nodeList) {// ��Ҫ���ù��˽�����·
	// this.from = from;
	// this.to = to;
	// this.fiberLinkList = fiberLinkList;
	// this.nodeList = nodeList;
	// }

//	public void clearRoute(){
//	 fiberLinkList.clear();
//	 fiberNodeList.clear();
//	 WDMLinkList.clear();
//	 WDMNodeList.clear();
//	 SourceID.clear();
//	 portSourceID.clear();
//	
//	 }

	// �ж������route�Ƿ���ȷ
	public static boolean isRouteTrue(String str) {
		String node[] = str.split("-");
		if (node.length != 0) {
			CommonNode no1 = CommonNode.getNode(node[0]);
			CommonNode no2 = CommonNode.getNode(node.length - 1);
		}
		if (node.length == 1)
			return false;
		for (int i = 0; i < (node.length - 1); i++) {
			WDMLink thelink = WDMLink.getWDMLink(node[i], node[i + 1]);
			CommonNode no = CommonNode.getNode(node[i]);
			if (no == null || thelink == null)
				return false;
		}
		return true;
	}

	// �˹�����ҵ��·��-����������ַ�������·��
	public static Route rgRoute(String str) {
		CommonNode from = new CommonNode();
		CommonNode to = new CommonNode();
		List<CommonNode> rgnodelist = new LinkedList<>();
		List<WDMLink> rgWDMLinkList = new LinkedList<WDMLink>();
		String node[] = str.split("-");
		for (int i = 0; i < node.length; i++) {
			CommonNode no = CommonNode.getNode(node[i]);
			if (no != null) {
				rgnodelist.add(no);
			}
		}
		for (int i = 0; i < (node.length - 1); i++) {
			WDMLink thelink = WDMLink.getWDMLink(node[i], node[i + 1]);
			if (thelink != null)
				rgWDMLinkList.add(thelink);
		}
		from = CommonNode.getNode(node[0]);
		to = CommonNode.getNode(node[node.length - 1]);
		Route route = new Route(from, to, rgWDMLinkList, rgnodelist);

		return route;
	}

	// ����ָ���ַ���-������ַ���,������Ӧ�ĵĽڵ����rgnodelist��
	public static List<CommonNode> rgnodelist(String str) {
		List<CommonNode> rgnodelist = new LinkedList<>();
		String node[] = str.split("-");
		for (int i = 0; i < node.length; i++) {
			CommonNode no = CommonNode.getNode(node[i]);
			rgnodelist.add(no);
		}
		return rgnodelist;
	}

	public static List<WDMLink> rgwdmlinklist(String str) {
		List<WDMLink> rgWDMLinkList = new LinkedList<WDMLink>();
		String node[] = str.split("-");
		for (int i = 0; i < (node.length - 1); i++) {
			WDMLink thelink = WDMLink.getWDMLink(node[i], node[i + 1]);
			rgWDMLinkList.add(thelink);
		}
		return rgWDMLinkList;
	}

	// ��дusedWaveList��toString����
	public String toStingwave() {
		StringBuilder a = new StringBuilder();
		for (int i = 0; i < this.usedWaveList.size(); i++) {
			if (i == 0 && i == (this.usedWaveList.size() - 1)) {
				a = a.append((this.usedWaveList.get(i) ));
			} else if (i == 0) {
				a = a.append((this.usedWaveList.get(i) ) + ",");
			} else if (i == (this.usedWaveList.size() - 1)) {
				a = a.append((this.usedWaveList.get(i) ));
			} else if (i != 0 && i != (this.usedWaveList.size() - 1)) {
				a = a.append((this.usedWaveList.get(i) ) + ",");
			}
		}
		return a.toString();
	}
	

	@Override
	public String toString() {// ��дtoString��������ʾ��ӡRoute����ʱ�����¸�ʽ��ӡ
		StringBuffer buffer = new StringBuffer();
		if (this != null && null != this.getNodeList()) {
			for (int n = 0; n < this.getNodeList().size(); n++) {
				buffer.append(this.getNodeList().get(n).getName());
				if (n != this.getNodeList().size() - 1) {
					buffer.append("--<"+this.getWDMLinkList().get(n).getEnglishname()+">--");
//					bufferw.append("--");
				}
			}
		}return buffer.toString();
	}

	// setter��getter����
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CommonNode getFrom() {
		return from;
	}

	public void setFrom(CommonNode from) {
		this.from = from;
	}

	public CommonNode getTo() {
		return to;
	}

	public void setTo(CommonNode to) {
		this.to = to;
	}

	public int getHops() {
		return hops;
	}

	public void setHops(int hops) {
		this.hops = hops;
	}

	public Traffic getBelongTraffic() {
		return belongsTraffic;
	}

	public void setBelongTraffic(Traffic belongTraffic) {
		this.belongsTraffic = belongTraffic;
	}

	public boolean isSuccess() {
		return isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public List<CommonNode> getNodeList() {
		return nodeList;
	}

	public void setNodeList(List<CommonNode> nodeList) {
		this.nodeList = nodeList;
	}

	public List<Port> getSimPortList() {
		return SimPortList;
	}

	public void setSimPortList(List<Port> simPortList) {
		SimPortList = simPortList;
	}

	public List<Port> getPortList() {
		return portList;
	}

	public void setPortList(List<Port> portList) {
		this.portList = portList;
	}

	public List<FiberLink> getFiberLinkList() {
		return fiberLinkList;
	}

	public void setFiberLinkList(List<FiberLink> fiberLinkList) {
		this.fiberLinkList = fiberLinkList;
	}

	public Traffic getBelongsTraffic() {
		return belongsTraffic;
	}

	public void setBelongsTraffic(Traffic belongsTraffic) {
		this.belongsTraffic = belongsTraffic;
	}
	

	public double getLength() {
		List<WDMLink> wdmlist = this.getWDMLinkList();
		double length = 0;
		for(WDMLink link : wdmlist) {
			length = length + link.getLength();
		}
		return length;
	}

	public void setLength(double length) {
		Length = length;
	}

	public List<WDMLink> getWDMLinkList() {
		return WDMLinkList;
	}

	public void setWDMLinkList(List<WDMLink> wDMLinkList) {
		WDMLinkList = wDMLinkList;
	}

	public List<Integer> getWaveLengthIdList() {
		return waveLengthIdList;
	}
	/**
	 * 
	 * �����·��ʹ�õĲ������
	 * @return
	   @author Lin
	   @Date 2018��10��19�� ����12:08:23
	 */
	public String getWaveLengthIdListToString() {
		StringBuffer s = new StringBuffer();
		LinkedList<Integer> list = new LinkedList();
		for(int i=0;i<this.getWaveLengthIdList().size();i++) {
			if(!list.contains(this.getWaveLengthIdList().get(i))) {
				list.add(this.getWaveLengthIdList().get(i));
			}
		}
		String res = "";
		for(Integer wave : list) {
			res = res + wave + ",";
		}
		if(res.length() > 0) {
			return res.substring(0, res.length()-1);
		}else {
			return res;
		}
	}
	
	
	//��routeʹ���˲���ת����node
//	public String waveChangedNode() {
//		StringBuilder b = new StringBuilder();
//		if (this != null && null != this.getNodeList()) {
//			for (int i = 0; i < this.getWaveChangedNode().size(); i++) {
//				b.append(this.getWaveChangedNode().get(i).getName() + ",");
//			}
//		}
//		return b.toString();
//	}
	/**
	 * �����·�ɵĵ��м̽ڵ�
	 * @return
	   @author Lin
	   @Date 2018��10��19�� ����12:09:05
	 */
	public String waveChangedNode() {
		if(this == null || this.getWDMLinkList().size()<=1) {
			return "";
		}
		StringBuilder b = new StringBuilder();
		for(int i=0;i<this.getWaveLengthIdList().size()-1;i++) {
			if(this.getWaveLengthIdList().get(i) != this.getWaveLengthIdList().get(i+1)) {
				b.append(this.getNodeList().get(i+1).getName()+",");
			}
		}
		return b.toString();
	}

	public static boolean HaveRepeatLink(Route r1, Route r2) {
		List<WDMLink> list1 = r1.WDMLinkList;
		List<WDMLink> list2 = r2.WDMLinkList;
		boolean a = false;
		for (int i = 0; i < list1.size(); i++) {
			WDMLink link1 = list1.get(i);
			for (int j = 0; j < list2.size(); j++) {
				WDMLink link2 = list2.get(j);
				if (link1.getId() == link2.getId()) {
					a = true;
				}
			}
		}
		//SRLG
		for(int i=0;i<list1.size();i++) {
			WDMLink link1 = list1.get(i);
			if(link1.belongSRLG()!=null) {
				for(int j=0;j<link1.belongSRLG().getSRLGWDMLinkList().size();j++) {
					WDMLink srlglink=link1.belongSRLG().getSRLGWDMLinkList().get(j);
					for(int k=0;k<list2.size();k++) {
						WDMLink link2 = list2.get(k);
						if(srlglink.getId()==link2.getId()) {
							a=true;
						}
					}
				}
			}
		}
		return a;
	}

	public void setWaveLengthIdList(List<Integer> waveLengthIdList) {
		 for(int i=0;i<waveLengthIdList.size();i++) {
		 waveLengthIdList.set(i, waveLengthIdList.get(i)+1);
		 }
		this.waveLengthIdList = waveLengthIdList;
	}

	public boolean isNotConsistent() {
		return notConsistent;
	}

	public void setNotConsistent(boolean notConsistent) {
		this.notConsistent = notConsistent;
	}

	public void setUsedWaveList(List<Integer> waveLengthIdList) {
		if (waveLengthIdList.size() != 0)
			this.usedWaveList.add(waveLengthIdList.get(0));
		for (int i = 0; i < waveLengthIdList.size(); i++) {
			if (waveLengthIdList.get(i) != this.usedWaveList.get(this.usedWaveList.size() - 1))
				this.usedWaveList.add((waveLengthIdList.get(i)));
		}
	}

	public List<Integer> getUsedWaveList() {
		return usedWaveList;
	}

	// ��ò���ת���Ľڵ�
	public List<CommonNode> getWaveChangedNode() {
		return waveChangedNode;
	}

	// ������
	public void setWaveChangedNode(List<Integer> waveLengthIdList) {
		if (waveLengthIdList.size() != 0) {
			for (int i = 0; i < waveLengthIdList.size() - 1; i++) {
				if (waveLengthIdList.get(i) != waveLengthIdList.get(i + 1)) {
					waveChangedNode.add(this.nodeList.get(i + 1));
					this.nodeList.get(i + 1).setWorkOTUNum();
				}
			}
		}

	}

	// �������ָ���
	public void setWaveChangedNode1(List<Integer> waveLengthIdList) {
		if (waveLengthIdList.size() != 0) {
			for (int i = 0; i < waveLengthIdList.size() - 1; i++) {
				if (waveLengthIdList.get(i) != waveLengthIdList.get(i + 1)) {
					waveChangedNode.add(this.nodeList.get(i + 1));
					this.nodeList.get(i + 1).setRestoreOTUNum();
					;
				}
			}
		}

	}

	// ����·������WDMlink����·�ܳ���
	public double routelength() {
		double len = 0;
		for (int i = 0; i < WDMLinkList.size(); i++) {
			len = len + WDMLinkList.get(i).getLength();
		}
		return len;
	}

	// ĳ��ҵ��Ĺ���·�������Ԥ�ò�����ҵ����е�ÿ��ҵ��Ĺ���·�ɶ����룬�����Ԥ�ò������Ա�����
	public static boolean isPreWaveCanShared(Traffic tra, List<Traffic> List) {
		for (int i = 0; i < List.size(); i++) {
			if (List.get(i).getWorkRoute() != null && tra.getWorkRoute() != null) {
				if (Route.HaveRepeatLink(tra.getWorkRoute(), List.get(i).getWorkRoute())) {
					return false;
				}
			}
		}
		return true;
	}

	// ƴ���бؾ��ڵ�ҵ�������·��route1��from-MustPassNode route2��MustPassNode-to
	public static Route combineRoute(Route route1, Route route2) {
		Route route = new Route();
		if (route1 != null && route2 != null) {
			route.getWDMLinkList().addAll(route1.getWDMLinkList());
			route.getWDMLinkList().addAll(route2.getWDMLinkList());
			route.getNodeList().addAll(route1.getNodeList());
			route.getNodeList().remove(route.getNodeList().size() - 1);
			route.getNodeList().addAll(route2.getNodeList());
		}
		return route;
	}

	// ƴ���бؾ���·������·��route1��from-MustPassLink.fromNode route2��MustPassLink.toNode-to
	public static Route combineRoute1(Route route1, Route route2, WDMLink mustpasslink) {
		Route route = new Route();
		if (route1 != null && route2 != null) {
			route.getWDMLinkList().addAll(route1.getWDMLinkList());
			route.getWDMLinkList().add(mustpasslink);
			route.getWDMLinkList().addAll(route2.getWDMLinkList());
			route.getNodeList().addAll(route1.getNodeList());
			route.getNodeList().addAll(route2.getNodeList());
		}
		if (route1 == null && route2 != null) {
			route.getWDMLinkList().add(mustpasslink);
			route.getWDMLinkList().addAll(route2.getWDMLinkList());
			if (mustpasslink.getFromNode().getName().equals(route2.getNodeList().get(0).getName())) {
				route.getNodeList().add(mustpasslink.getToNode());
			}
			if (!mustpasslink.getFromNode().getName().equals(route2.getNodeList().get(0).getName())) {
				route.getNodeList().add(mustpasslink.getFromNode());
			}
			route.getNodeList().addAll(route2.getNodeList());
		}
		if (route1 != null && route2 == null) {
			route.getWDMLinkList().addAll(route1.getWDMLinkList());
			route.getWDMLinkList().add(mustpasslink);
			route.getNodeList().addAll(route1.getNodeList());
			if (route1.getNodeList().get(route1.getNodeList().size() - 1).getName().equals(mustpasslink.getToNode())) {
				route.getNodeList().add(mustpasslink.getFromNode());
			}
			if (!route1.getNodeList().get(route1.getNodeList().size() - 1).getName().equals(mustpasslink.getToNode()))
				route.getNodeList().add(mustpasslink.getToNode());
		}
		return route;
	}

	// �˷���ֻ���ڶ�̬·����
	// �ж�ĳ��·�ɵ���·���Ƿ��п��ò���wave,ע�⣺�˲�����1-80
	// �������˵������wave���ã�1-waveΪ���� 2-waveΪ�ָ������Ҹ�ҵ��Ĺ���·����wave���ص�ҵ��Ĺ���·�ɷ���
	public static boolean isRouteHaveWave(Route route,int wave,Traffic traffic) {
			List<WDMLink> linklist = route.getWDMLinkList(); 
			for(int i=0;i<linklist.size();i++) {
				WDMLink link=linklist.get(i);
				WaveLength wl=link.getWavelengthById(wave);
				//���wl��״̬���ǿ���   ����   �ָ����ҿɹ���   �����false
//				if(wl.getStatus().equals(Status.����))
//					return true;
//				else if(wl.getStatus().equals(Status.�ָ�)&&isPreWaveCanShared(traffic, wl.getDynamicTrafficList()))
//						return true;
				if(!((isPreWaveCanShared(traffic, wl.getDynamicTrafficList())
						&&wl.getStatus().equals(Status.�ָ�))
						||wl.getStatus().equals(Status.����)))
					return false;
			}
			return true;
		}
	
	//�ж�ĳ�������Ƿ�����·�ɣ�����/����--�ȸò����ǲ�����ģ�ÿһ���ķ�������
		public static boolean isRouteCanUseWave(Route route,WaveLength wave,Traffic traffic) {
			List<WDMLink> linklist = route.getWDMLinkList(); 
			int waveNum=wave.getID();//�����ţ�1-80��
			for(int i=0;i<linklist.size();i++) {
				WDMLink link=linklist.get(i);
				WaveLength wl=link.getWavelengthById(waveNum);
				//�жϸ�wave�Ƿ����
				//����������1����wave���ǿ���״̬��2����wave��״̬���Ӳ�������ʣ��������ҵ�����
				if(wl.getStatus().equals(Status.����)||
						(wl.getStatus().equals(Status.�Ӳ���)&&wave.getM_nFree()>traffic.getNrate()))
					continue;
				else
					return false;
			}
			return true;
		}
		
		//�ж�ĳ�������Ƿ�����·�ɣ�Ԥ�ã��Ȳ����ɹ���ÿһ���ķ�������
	public static boolean isRouteCanUseWave_shared(Route route,WaveLength wave,Traffic traffic) {
			List<WDMLink> linklist = route.getWDMLinkList(); 
			int waveNum=wave.getID();//�����ţ�1-80��
			for(int i=0;i<linklist.size();i++) {
				WDMLink link=linklist.get(i);
				WaveLength wl=link.getWavelengthById(waveNum);
				//�жϸ�wave�Ƿ����
				//����������1����wave���ǿ���״̬��2����wave��״̬���Ӳ�������ʣ��������ҵ�����   
				//3����wave��״̬���Ӳ���,��ҵ����wave�����ϳ��ص�ҵ��Ĺ���·��ȫ�������룬����ʣ�����+Ԥ��·��ռ�ô������ҵ����� 
				if(wl.getStatus().equals(Status.����)||
						(wl.getStatus().equals(Status.�Ӳ���)&&wave.getM_nFree()>traffic.getNrate())
						||((isPreWaveCanShared(traffic, wave.preTrafficList))&&(wl.getStatus().equals(Status.�Ӳ���)&&wave.getM_nFree()+wave.preUsed()>traffic.getNrate()))
						)
					continue;
				else
					return false;
			}
			return true;
		}
	
	//Ѱ�ҳ�һ��·���У����в�����Դ����x����·
	public  LinkedList<WDMLink> resoureLessLink(Route route,int x){
		LinkedList<WDMLink> resourceLessLinkList=new LinkedList<>();
		for(int i=0;i<route.getWDMLinkList().size();i++) {
			WDMLink link=route.getWDMLinkList().get(i);
			int remainResource=0;
			for(int j=0;j<link.getWaveLengthList().size();j++) {
				if(link.getWaveLengthList().get(j).getStatus().equals(Status.����))
					remainResource++;
			}
			if(remainResource<x)
				resourceLessLinkList.add(link);
		}
		return resourceLessLinkList;
	}
	//Ѱ�ҳ�һ��·���У�û��ƽ�бߵ���·
	public  LinkedList<WDMLink> noparalLink(Route route){
		LinkedList<WDMLink> noparalLinkList=new LinkedList<>();
		for(int i=0;i<route.getWDMLinkList().size();i++) {
			WDMLink link=route.getWDMLinkList().get(i);
			if(link.getParallelLinkList().size()==1) {
				noparalLinkList.add(link);
			}
		}
		return noparalLinkList;
	}
	//�ҳ�·���д���srlg����·
	public  LinkedList<WDMLink> srlglink(Route route)
	{
		LinkedList<WDMLink> srlglinkList = new LinkedList<>();
		for (int i = 0; i < route.getWDMLinkList().size(); i++)
		{
			WDMLink link = (WDMLink)route.getWDMLinkList().get(i);
			if (link.belongSRLG() != null)
				srlglinkList.add(link);
		}

		return srlglinkList;
	}
	//�ж�����·���Ƿ񾭹���ͬ�Ľڵ㣬�ǵĻ���������й���ҵ����ģ��;�����Ҫ�ò�ͬ��ƽ�б�
	public static boolean isPassSameNode(Route route1, Route route2)
	{
		if (route1 == null || route2 == null)
			return false;
		if (route1.getNodeList().size() != route2.getNodeList().size())
			return false;
		for (int i = 0; i < route1.getNodeList().size(); i++)
			if (!((CommonNode)route1.getNodeList().get(i)).getName().equals(((CommonNode)route2.getNodeList().get(i)).getName()))
				return false;
		return true;
	}
	//������·����Ϊ������ͬ����·���������ò�ͬƽ�б�
	public static void setPassSameLink(Route route1, Route route2)
	{
		for (int i = 0; i < route1.getWDMLinkList().size(); i++)
		{
			WDMLink link1 = (WDMLink)route1.getWDMLinkList().get(i);
			if (link1.getParallelLinkList().size() > 1 && link1.isActive())
				route2.getWDMLinkList().set(i, link1);
		}
	}
	//����node����ɵ�routeת��Ϊ������route
	public static List<Route> fromNodeToRoute(List<List<CommonNode>> ladders,Traffic traffic) {
		
		List<Route> routelist = new LinkedList<>();

		//�������㼤��������pathɾ��
		for(int j=0;j<ladders.size();j++) {
			List<CommonNode> nodelist = ladders.get(j);
			for(int i=0;i<nodelist.size()-1;i++) {
				CommonNode node1 = nodelist.get(i);
				CommonNode node2 = nodelist.get(i+1);
				List<WDMLink> linklist = WDMLink.getWDMLink(node1, node2);
				//�ж�linklist���Ƿ��м�����·��û�еĻ��ͽ���path��ladders��ɾ��
				boolean isActive = false;
				for(WDMLink link : linklist) {
					if(link.isActive()) {
						isActive = true;
					}
				}
				if(isActive == false) {
					ladders.remove(j);
					j--;
					break;
				}
			}
		}
		//node���ɵ�path��Ϊroute
		for(int i=0;i<ladders.size();i++) {
			List<CommonNode> nodelist = ladders.get(i);
			List<WDMLink> WDMLinklist = new LinkedList<>();
			for(int j=0;j<nodelist.size()-1;j++) {
				CommonNode node1 = nodelist.get(j);
				CommonNode node2 = nodelist.get(j+1);
				List<WDMLink> linklist = WDMLink.getWDMLink(node1, node2);
				//�޳���������·
				for(int k=0;k<linklist.size();k++) {
					WDMLink link = linklist.get(k);
					if(link.isActive() == false) {
						linklist.remove(k);
						k--;
					}
				}
				if(linklist.size() == 1) {
					WDMLinklist.addAll(linklist);
				}else {
					//����û��SRLG�ҿ��ò�����Դ����
					List<WDMLink> nosrlg = new LinkedList<>();
					for(WDMLink link :linklist) {
						if(link.belongSRLG() == null) {
							nosrlg.add(link);
						}
					}
					if(nosrlg.size()!=0) {
						linklist = nosrlg;
					}
					int max = 0;
					for(WDMLink link : linklist) {
						int wavesource = link.getRemainResource();
						if(wavesource > max) {
							max = wavesource;
						}
					}
					for(WDMLink link : linklist) {
						if(link.getRemainResource() == max) {
							WDMLinklist.add(link);
							break;
						}
					}
				}
			}
			Route route = new Route(nodelist.get(0), nodelist.get(nodelist.size()-1), WDMLinklist, nodelist);
			for (WDMLink wdmLink : WDMLinklist) {
				route.getFiberLinkList().addAll(wdmLink.getFiberLinkList());
			}
			route.setBelongsTraffic(traffic);
			routelist.add(route);
		}
		return routelist;
	}
	//��routelist���� ����
	public static void sortHopMethod(List<Route> routelist) {
		Collections.sort(routelist, new Comparator(){  
	        @Override  
	        public int compare(Object o1, Object o2) {  
	            Route route1=(Route)o1;  
	            Route route2=(Route)o2;  
	            if(route1.getNodeList().size()>route2.getNodeList().size()){  
	                return 1;  
	            }else if(route1.getNodeList().size()==route2.getNodeList().size()){  
	                return 0;  
	            }else{  
	                return -1;  
	            }  
	        }             
	    }); 
	}
	//��routelist���� ����
	public static void sortLengthMethod(List<Route> routelist) {
		Collections.sort(routelist, new Comparator() {
			@Override
			public int compare(Object o1, Object o2) {
				Route route1 = (Route) o1;
				Route route2 = (Route) o2;
				if (route1.getLength() > route2.getLength()) {
					return 1;
				} else if (route1.getLength() == route2.getLength()) {
					return 0;
				} else {
					return -1;
				}
			}
		});
	}

	public boolean meetOSNR() {
		if (this != null) {
			double simuOSNR = OSNR.calculateOSNR(this);
			double crossOSNR = OSNR.crossOSNR(this);
			if (simuOSNR > crossOSNR) {
				return true;
			}
		}
		return false;
	}
	
	//�Ƴ�routelist�в�����OSNR������
	public static void killOSNR(List<Route> routelist) {
		for(int i=0;i<routelist.size();i++) {
			Route route = routelist.get(i);
			double simuOSNR = OSNR.calculateOSNR(route);
			double crossOSNR = OSNR.crossOSNR(route);
			if(simuOSNR < crossOSNR) {
				routelist.remove(i);
				i--;
			}
		}
	}
	//�ҳ�һ��route�ж��ٸ�һ�²���
	//����route��·�����ͣ�������Ԥ�ã�flag��1����/���� 2��Ԥ��
	public static LinkedList<Integer> consistWaveNum(Route route, int flag, Traffic tra) {
		LinkedList<Integer> consist = new LinkedList<>();
		List<WDMLink> wdmlist = route.getWDMLinkList();
		for (int i = 1; i <= DataSave.waveNum; i++) {
			boolean isConsist = true;
			for (WDMLink link : wdmlist) {
				WaveLength wave = link.getWavelengthById(i);
				if (flag == 1) {
					if (!wave.getStatus().equals(Status.����)) {
						isConsist = false;
						break;
					}
				}
				if (flag == 2) {
					if (!(wave.getStatus().equals(Status.����) || (wave.getStatus().equals(Status.�ָ�)
							&& Route.isPreWaveCanShared(tra, wave.preTrafficList)))) {
						isConsist = false;
						break;
					}
				}
			}
			if (isConsist) {
				consist.add(i);
			}
		}
		return consist;
	}
	/**
	 * 
	 * @Desc ���ݶ�̬��·�ɲ���ʱ����ĵķ���·��resumeRoute ���ýڵ��OTUʹ������Լ���·�Ĳ���ʹ��
	 * @param resumeRoute
	   @author Lin
	   @Date 2018��10��15�� ����11:28:05
	 */
	public static void setDynNodeAndLinkParams(Route resumeRoute) {
		List<Integer> waveLengthIdList = resumeRoute.getWaveLengthIdList();
		LinkedList<CommonNode> waveChangedNode = new LinkedList<>();
		System.out.println("*********************************55" + waveLengthIdList);
		for (int i = 0; i < waveLengthIdList.size() - 1; i++) {
			int front = waveLengthIdList.get(i);
			int last = waveLengthIdList.get(i + 1);
			if (front != last) {
				waveChangedNode.add(resumeRoute.getNodeList().get(i + 1));
			}
		}
		for (CommonNode node : waveChangedNode) {
			int[] dynOTU = node.getDynUsedOTU();
			dynOTU[1]++;
		}
		for (int i = 0; i < resumeRoute.getWDMLinkList().size(); i++) {
			int[] dynUsedLink = resumeRoute.getWDMLinkList().get(i).getDynUsedLink();
			if(!resumeRoute.getWDMLinkList().get(i).getWavelengthById(waveLengthIdList.get(i)).getStatus().equals(Status.����)) {
			dynUsedLink[waveLengthIdList.get(i)] = 1;}
		}
	}
	


	
	

}
