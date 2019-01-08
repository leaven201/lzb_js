package data;

import java.io.Serializable;
/**
 * 节点
 * @author 豹读诗书
 *
 */
import java.util.*;
import enums.*;
public class CommonNode implements Serializable{

	private static final long serialVersionUID = -3348972325115332375L;
	
    private int id;// 节点ID，唯一
    private String name;// 节点名称，唯一
    private String OtherName;//节点别名
    private double longitude;// 经度
    private double latitude;// 维度
    private boolean isActive;// 是否激活
    private boolean isOA;// 是否为光放节点
    private boolean isROADM;// 是否支持ROADM
    private int maxPortNum;// 节点最多包含的端口数
    private NodeType nodeType;//节点类型（ROADM OA OTN）
    private int rankID;
    private boolean isEleRegeneration=true;//是否支持电再生，默认可以
    private boolean isWaveConversion=true;//是否支持波长转换，默认可以
	private boolean iszhongji=true;//lzb+9.21是否允许中继，默认可以
	private int WSS=9;//WSS维度
	private int upDown=0;//上下路分组数
	private int workOTUNum=0;//用于工作的中继OTU数量
	private int restoreOTUNum=0;//用于恢复的中继OTU数量
	private int usedUpdown=0; //计算该节点当前使用了多少个上下路模块
	private int maxUpdown = 0;//最大本地上下路分组数
	private int dynMaxUpdown = 0;//动态重路由时最大上下路分组数
	private int dynUsedUpdown=0; //动态重路由计算该节点当前使用了多少个上下路模块
	private int degree = 0; //节点的度
	private int[] dynUsedOTU = new int[2];//统计动态重路由时使用的OTU数量 0：max:本次仿真该节点最多需要使用多少OTU 1：current：某个link断时该节点需要使用多少OTU
	
	
	
//	public static List<CommonNode> allFiberNodeList=new LinkedList<>();//存储光纤节点
	public static List<CommonNode> ROADM_NodeList = new LinkedList<>();// 储存ROADM节点
	public static List<CommonNode> OLA_NodeList = new LinkedList<>();// 储存OLA节点
	public static List<CommonNode> allNodeList = new LinkedList<CommonNode>(); // 存储所有节点
	private List<Port> portList = new LinkedList<>();// 存放节点包含的端口
    
    
//    private boolean isOTN;//是否具有OTN功能
//    private double switchCapacity;//交叉容量
//    private int numOB;//OB数量
//    private int numOP;
//    private int numOA;
//	  private boolean flexibleRaster;//灵活栅格  lzb-9.21上下路分组数
//    private int waveNum;//波道数
//    private int systemRate;//系统速率   lzb-9.21上下路分组数
//    public static List<CommonNode> allOptNodeList=new LinkedList<>();//存储光节点
//    public static List<CommonNode> allElecNodeList=new LinkedList<>();//存储电节点

//    private List<Port> fiberPortList=new LinkedList<>();// 存储节点包含的端口
//    private List<Port> elePortList=new LinkedList<>();// 存储节点包含的端口
//    private List<Port> optPortList=new LinkedList<>();// 存储节点包含的端口
	
	public CommonNode(){
    }
	//lzb+9.21   新的构造方法
	public CommonNode(int id, String name, String otherName, double longitude, double latitude,
			NodeType nodeType, boolean iszhongji, int WSS, int maxUpdown,boolean isActive) {
		super();
		this.id = id;
		this.name = name;
		OtherName = otherName;
		this.longitude = longitude;
		this.latitude = latitude;
		this.nodeType = nodeType;
		this.iszhongji = iszhongji;
		this.WSS = WSS;
		this.maxUpdown = maxUpdown;
		this.isActive=true;//新建节点后初始化为激活
		this.maxPortNum=Integer.MAX_VALUE;//可用端口数初始化为无穷大
		

		allNodeList.add(this);
		if (nodeType.equals(NodeType.ROADM)) {// 判断加入哪个list
			ROADM_NodeList.add(this);
		} else if (nodeType.equals(NodeType.OLA)) {
			OLA_NodeList.add(this);
			//OLA节点没有中继、WSS、上下路分组、点交叉、波长转换的概念
			this.iszhongji=false;
			this.WSS=0;
			this.upDown=0;
			this.isEleRegeneration=false;
			this.isWaveConversion=false;						
		}
	}
	
	//添加ROADM节点构造方法
	public CommonNode(int id, String name, String otherName, double longitude, double latitude,
			 boolean iszhongji,int WSS,int upDown) {
		super();
		this.id = id;
		this.name = name;
		OtherName = otherName;
		this.longitude = longitude;
		this.latitude = latitude;
		this.nodeType = NodeType.ROADM;
		this.iszhongji = iszhongji;
		this.WSS = WSS;
		this.upDown = upDown;
		this.isActive=true;//新建节点后初始化为激活

		this.maxPortNum=Integer.MAX_VALUE;//可用端口数初始化为无穷大
		allNodeList.add(this);
		ROADM_NodeList.add(this);
		
	}
	//添加OLA节点构造方法
		public CommonNode(int id, String name, String otherName, double longitude, double latitude) {
			super();
			this.id = id;
			this.name = name;
			OtherName = otherName;
			this.longitude = longitude;
			this.latitude = latitude;
			this.nodeType = NodeType.OLA;
			this.iszhongji = false;
			this.WSS = 0;
			this.upDown = 0;
			this.isActive=true;//新建节点后初始化为激活

			this.maxPortNum=Integer.MAX_VALUE;//可用端口数初始化为无穷大
			allNodeList.add(this);
			OLA_NodeList.add(this);
			
		}
	
    
    
    
    
//    //构造方法
//    public CommonNode(int id, String name, double longitude, double latitude, boolean isActive, boolean isOA, 
//	    boolean isROADM,boolean isOTN, int maxPortNum, double switchCapacity,NodeType nodeType,
//	    int numOB,int numOP,int numOA,boolean isEleRegeneration,boolean isWaveConversion,String OtherName) {
//	
//	this.id = id;
//	this.name = name;
//	this.setOtherName(OtherName);
//	this.longitude = longitude;
//	this.latitude = latitude;
//	this.isActive = isActive;
//	this.isOA = isOA;
//	this.isROADM = isROADM;
//	this.isOTN = isOTN;
//	this.maxPortNum = maxPortNum;
//	this.switchCapacity = switchCapacity;
//	this.nodeType=nodeType;
//	this.numOB=numOB;
//	this.numOP=numOP;
//	this.numOA=numOA;
//	this.isEleRegeneration=isEleRegeneration;
//	this.isWaveConversion=isWaveConversion;
//	allNodeList.add(this);
	
//	if(nodeType.equals(NodeType.ROADM)){//判断加入哪个list
//	    allOptNodeList.add(this);
//	}else if(nodeType.equals(NodeType.OTN)){
//	    allElecNodeList.add(this);
//	}
//    }
    
    public CommonNode(int id, String name, String otherName, double longitude, double latitude, NodeType nodeType) {
		super();
		this.id = id;
		this.name = name;
		OtherName = otherName;
		this.longitude = longitude;
		this.latitude = latitude;
		this.nodeType = nodeType;
		allNodeList.add(this);
	}
    

    


    //判断节点id是否重复
    public static boolean isIdRepeat(int id){
	Iterator<CommonNode> it=CommonNode.allNodeList.iterator();
	while(it.hasNext()){
	    CommonNode node=it.next();
	    if(node.getId()==id){
		return true;
	    }
	}
	return false;
    }
    //根据名字得到节点
    public static CommonNode getNodeByName(String name) {
    	Iterator<CommonNode> it = CommonNode.allNodeList.iterator();
    	while(it.hasNext()) {
    		CommonNode node=it.next();
    		if (node.getName().equals(name)) {
				return node;
			}
    	}return null;
    } 
    
	//lzb+9.22
	//判断节点名称是否重复
	public static boolean isNameRepeat(String name) {
		Iterator<CommonNode> it = CommonNode.allNodeList.iterator();
		while (it.hasNext()) {
			CommonNode node = it.next();
			if (node.getName()== name) {
				return true;
			}
		}
		return false;
	}//判断节点别名是否重复
	public static boolean isOtherNameRepeat(String OtherName) {
		Iterator<CommonNode> it = CommonNode.allNodeList.iterator();
		while (it.hasNext()) {
			CommonNode node = it.next();
			if (node.getOtherName()== OtherName) {
				return true;
			}
		}
		return false;
	}
	
	
    public int getDynMaxUpdown() {
		return dynMaxUpdown;
	}
	public void setDynMaxUpdown(int dynMaxUpdown) {
		this.dynMaxUpdown = dynMaxUpdown;
	}
	public int getDynUsedUpdown() {
		return dynUsedUpdown;
	}
	public void setDynUsedUpdown(int dynUsedUpdown) {
		this.dynUsedUpdown = dynUsedUpdown;
	}
	public int[] getDynUsedOTU() {
		return dynUsedOTU;
	}
	public void setDynUsedOTU(int[] dynUsedOTU) {
		this.dynUsedOTU = dynUsedOTU;
	}
	public static void	addCommonNode(CommonNode e){//静态方法用以维护链表，注意与实际对象无关
    	allNodeList.add(e);

	}			
	public static void	delCommonNode(CommonNode e){
		allNodeList.remove(e);
	}
	public static void addROADMNode(CommonNode e){
		ROADM_NodeList.add(e);
		allNodeList.add(e);
	}
	public static void	delROADMNode(CommonNode e){
		ROADM_NodeList.remove(e);
		allNodeList.remove(e);
	}
	public static void addOLANode(CommonNode e){
		OLA_NodeList.add(e);
		allNodeList.add(e);
	}
	public static void	delOLANode(CommonNode e){
		OLA_NodeList.remove(e);
		allNodeList.remove(e);
	}	
	//lzb-9.22
	
	public int getUsedUpdown() {
		return usedUpdown;
	}
	public void setUsedUpdown(int usedUpdown) {
		this.usedUpdown = usedUpdown;
	}
	public int getMaxUpdown() {
		return maxUpdown;
	}
	public void setMaxUpdown(int maxUpdown) {
		this.maxUpdown = maxUpdown;
	}
	public int getDegree() {
		return degree;
	}
	public void setDegree(int degree) {
		this.degree = degree;
	}
	//lzb+9.22
	//得到对应id的光放节点并返回其属性
	public static CommonNode getROADMNode(int ID) {
		Iterator<CommonNode> it = CommonNode.ROADM_NodeList.iterator();
		while (it.hasNext()) {
			CommonNode commonNode = it.next();
			if (commonNode.getId() == ID)
				return commonNode;
		}
		return null;
	}
	
	//得到对应id的光放节点并返回其属性
	public static CommonNode getOLANode(int ID) {
		Iterator<CommonNode> it = CommonNode.OLA_NodeList.iterator();
		while (it.hasNext()) {
			CommonNode commonNode = it.next();
			if (commonNode.getId() == ID)
				return commonNode;
		}
		return null;
	}//lzb-9.22
    
    //由节点名称得到相应节点
    public static CommonNode getNode(String name) {
	Iterator<CommonNode> it = CommonNode.allNodeList.iterator();
	while (it.hasNext()) {
	    CommonNode commonNode = it.next();
	    if (commonNode.getName().equals(name))
		return commonNode;
	}
	return null;
    }
    
    public static CommonNode getNode(int ID) {
	Iterator<CommonNode> it = CommonNode.allNodeList.iterator();
	while (it.hasNext()) {
	    CommonNode commonNode = it.next();
	    if (commonNode.getId() == ID)
		return commonNode;
	}
	return null;
    }
    
  //判断某个名称的节点是否存在
    public static CommonNode isNode(String name) {
	Iterator<CommonNode> it = CommonNode.allNodeList.iterator();
	while (it.hasNext()) {
	    CommonNode commonNode = it.next();
	    if (commonNode.getName().equals(name))
		return commonNode;
	}
	return null;
    }
    
//    public void addPort(Port p) {// 根据端口类型添加到对应的链表中去
//	PortType layer = p.getType();
//	switch (layer) {
//	case OTN:
//	    fiberPortList.add(p);
//	    break;
//	case Fiber:
//	    optPortList.add(p);
//	    break;
//	case WDM:
//	    elePortList.add(p);
//	    break;
//	default:
//	    break;
//	}
//    }
    
//    //搜索链接
//public static List searchlink(CommonNode node,String layer){
//		
//		List linklist = new LinkedList();
//		
//		if(layer.trim().equals("FIBER"))
//		{
//			for(int i=0;i<FiberLink.fiberLinkList.size();i++){
//				if(FiberLink.fiberLinkList.get(i).getM_cFromNode()==node)
//				{
//					linklist.add(FiberLink.fiberLinkList.get(i));
//				}
//				else if(FiberLink.fiberLinkList.get(i).getM_cToNode()==node)
//				{
//					linklist.add(FiberLink.fiberLinkList.get(i));
//				}
//			}
//		}
//		else if(layer.trim().equals("WDM"))
//		{
//			for(int i=0;i<WDMLink.WDMLinkList.size();i++){
//				if(WDMLink.WDMLinkList.get(i).getM_cFromNode()==node)
//				{
//					linklist.add(WDMLink.WDMLinkList.get(i));
//				}
//				else if(WDMLink.WDMLinkList.get(i).getM_cToNode()==node)
//				{
//					linklist.add(WDMLink.WDMLinkList.get(i));
//				}
//			}
//		}
//		else if(layer.trim().equals("OTN"))
//		{
//			for(int i=0;i<OTNLink.OTNLinkList.size();i++){
//				if(OTNLink.OTNLinkList.get(i).getM_cFromNode()==node)
//				{
//					linklist.add(OTNLink.OTNLinkList.get(i));
//				}
//				else if(OTNLink.OTNLinkList.get(i).getM_cToNode()==node)
//				{
//					linklist.add(OTNLink.OTNLinkList.get(i));
//				}
//			}
//		}
//		else if(layer.trim().equals("SDH"))
//		{
//			for(int i=0;i<SDHLink.s_lSDHLinkList.size();i++){
//				if(SDHLink.s_lSDHLinkList.get(i).getM_cFromNode()==node)
//				{
//					linklist.add(SDHLink.s_lSDHLinkList.get(i));
//				}
//				else if(SDHLink.s_lSDHLinkList.get(i).getM_cToNode()==node)
//				{
//					linklist.add(SDHLink.s_lSDHLinkList.get(i));
//				}
//			}
//		}
//		else if(layer.trim().equals("ASON"))
//		{
//			for(int i=0;i<SDHLink.s_lASONLinkList.size();i++){
//				if(SDHLink.s_lASONLinkList.get(i).getM_cFromNode()==node)
//				{
//					linklist.add(SDHLink.s_lASONLinkList.get(i));
//				}
//				else if(SDHLink.s_lASONLinkList.get(i).getM_cToNode()==node)
//				{
//					linklist.add(SDHLink.s_lASONLinkList.get(i));
//				}
//			}
//		}
//		
//		return linklist;
//		
//	}	
  
    
    
    @Override
    public String toString() {
	return "Node [id=" + id + ", name=" + name + " iszhongji=" + iszhongji() + " type =" + nodeType + "]";
    }
    
 // setter、getter方法
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

 	public String getOtherName() {
 		return OtherName;
 	}

 	public void setOtherName(String otherName) {
 		OtherName = otherName;
 	}

 	public double getLongitude() {
 		return longitude;
 	}

 	public void setLongitude(double longitute) {
 		this.longitude = longitute;
 	}

 	public double getLatitude() {
 		return latitude;
 	}

 	public void setLatitude(double latitude) {
 		this.latitude = latitude;
 	}

 	public List<Port> getPortList() {
 		return portList;
 	}

 	public void setPort(List<Port> port) {
 		this.portList = port;
 	}

 	public int getMaxPortNum() {
 		return maxPortNum;
 	}

 	public void setMaxPortNum(int maxPortNum) {
 		this.maxPortNum = maxPortNum;
 	}

 	public boolean isActive() {
 		return isActive;
 	}

 	public void setActive(boolean isActive) {
 		this.isActive = isActive;
 	}

 	public boolean isOA() {
 		return isOA;
 	}

 	public void setOA(boolean isOA) {
 		this.isOA = isOA;
 	}

 	public boolean isROADM() {
 		return isROADM;
 	}

 	public void setROADM(boolean isROADM) {
 		this.isROADM = isROADM;
 	}

// 	public boolean isOTN() {
// 		return isOTN;
// 	}
//
// 	public void setOTN(boolean isOTN) {
// 		this.isOTN = isOTN;
// 	}
//
// 	public double getSwitchCapacity() {
// 		return switchCapacity;
// 	}
//
// 	public void setSwitchCapacity(double switchCapacity) {
// 		this.switchCapacity = switchCapacity;
// 	}

 	public NodeType getType() {
 		return nodeType;
 	}

 	public void setType(NodeType type) {
 		this.nodeType = type;
 	}

 	public NodeType getNodeType() {
 		return nodeType;
 	}

 	public void setNodeType(NodeType nodeType) {
 		this.nodeType = nodeType;
 	}

 	public boolean isEleRegeneration() {
 		return isEleRegeneration;
 	}

 	public void setEleRegeneration(boolean isEleRegeneration) {
 		this.isEleRegeneration = isEleRegeneration;
 	}

 	public boolean isWaveConversion() {
 		return isWaveConversion;
 	}

 	public void setWaveCoversion(boolean isWaveConversion) {
 		this.isWaveConversion = isWaveConversion;
 	}

// 	public List<Port> getFiberPortList() {
// 		return fiberPortList;
// 	}
//
// 	public void setFiberPortList(List<Port> fiberPortList) {
// 		this.fiberPortList = fiberPortList;
// 	}
//
// 	public List<Port> getOptPortList() {
// 		return optPortList;
// 	}
//
// 	public void setOptPortList(List<Port> optPortList) {
// 		this.optPortList = optPortList;
// 	}
//
// 	public List<Port> getElePortList() {
// 		return elePortList;
// 	}
//
// 	public void setElePortList(List<Port> elePortList) {
// 		this.elePortList = elePortList;
// 	}

 	public int getRankID() {
 		return rankID;
 	}

 	public void setRankID(int rankID) {
 		this.rankID = rankID;
 	}

// 	public int getM_nSubnetNum() {
// 		return m_nSubnetNum;
// 	}

 	public boolean isM_bStatus() {
 		return isActive;
 	}

 	public static List<CommonNode> getM_lsCommonNode() {
 		return allNodeList;
 	}

 	public String getM_sName() {
 		return name;
 	}
 	public int getWSS() {
 		return WSS;
 	}

 	public void setWSS(int wSS) {
 		WSS = wSS;
 	}

 	public int getUpDown() {
 		return upDown;
 	}

 	public void setUpDown(int upDown) {
 		this.upDown = upDown;
 	}
 	//用于初始化updown
 	public void setUpDown() {
 		this.upDown = upDown+1;
 	}

 
 	public boolean iszhongji() {
 		return iszhongji;
 	}

 	public void setiszhongji(boolean iszhongji) {
 		this.iszhongji = iszhongji;
 	}
	public int getWorkOTUNum() {
		return workOTUNum;
	}
	public void setWorkOTUNum(int workOTUNum) {
		this.workOTUNum = workOTUNum;
	}
	public void setWorkOTUNum() {
		this.workOTUNum = workOTUNum+1;
	}
	public int getRestoreOTUNum() {
		return restoreOTUNum;
	}
	public void setRestoreOTUNum(int restoreOTUNum) {
		this.restoreOTUNum = restoreOTUNum;
	}
	public void setRestoreOTUNum() {
		this.restoreOTUNum = restoreOTUNum+1;
	}
	public boolean isIszhongji() {
		return iszhongji;
	}
	public void setIszhongji(boolean iszhongji) {
		this.iszhongji = iszhongji;
	}
	public static List<CommonNode> getROADM_NodeList() {
		return ROADM_NodeList;
	}
	public static void setROADM_NodeList(List<CommonNode> rOADM_NodeList) {
		ROADM_NodeList = rOADM_NodeList;
	}
	public static List<CommonNode> getOLA_NodeList() {
		return OLA_NodeList;
	}
	public static void setOLA_NodeList(List<CommonNode> oLA_NodeList) {
		OLA_NodeList = oLA_NodeList;
	}
	public static List<CommonNode> getAllNodeList() {
		return allNodeList;
	}
	public static void setAllNodeList(List<CommonNode> allNodeList) {
		CommonNode.allNodeList = allNodeList;
	}
	//重新定义节点的经纬度，使拓扑呈现在中心
	public static void reDefineSite() {
		double latitudeSum=0;
		double longitudeSum=0;
		for(int i=0;i<CommonNode.allNodeList.size();i++) {
			latitudeSum=latitudeSum+CommonNode.allNodeList.get(i).getLatitude();
			longitudeSum=longitudeSum+CommonNode.allNodeList.get(i).getLongitude();
		}
		double latitudeAve=latitudeSum/CommonNode.allNodeList.size();
		double longitudeAve=longitudeSum/CommonNode.allNodeList.size();
		for(int i=0;i<CommonNode.allNodeList.size();i++) {
			double nodelati=CommonNode.allNodeList.get(i).getLatitude();
			double nodelongi=CommonNode.allNodeList.get(i).getLongitude();
			CommonNode.allNodeList.get(i).setLatitude((nodelati/latitudeAve)*35);
			System.out.println(CommonNode.allNodeList.get(i).getLatitude());
			CommonNode.allNodeList.get(i).setLongitude((nodelongi/longitudeAve)*100);
			System.out.println(CommonNode.allNodeList.get(i).getLongitude());

		}
	}
	
	/**
	 * @Desc 计算该节点使用了多少个本地上下路模块
	 * @param node
	 * @return
	   @author Lin
	   @Date 2018年10月8日 下午4:35:24
	 */
	public int countUpdown(CommonNode node) {
		LinkedList<WDMLink> wdmList = WDMLink.WDMLinkList;
		LinkedList<WDMLink> nodeLinkedList = new LinkedList<>();
		for (WDMLink wdmLink : wdmList) {
			if(wdmLink.getFromNode().getName().equals(node.getName()) || wdmLink.getToNode().getName().equals(node.getName())) {
				nodeLinkedList.add(wdmLink);
			}
		}
		for(int i=1;i<=DataSave.waveNum;i++) {
			int num = 0;
			for (WDMLink wdmLink : nodeLinkedList) {
				if(!wdmLink.getWavelengthById(i).getStatus().equals(Status.空闲)) {
					num++;
				}
			}
			if(num > node.getUsedUpdown()) {
				node.setUsedUpdown(num);
			}
		}
		return node.getUsedUpdown();
	}

	public int countUpdown1(CommonNode node) {
		LinkedList<WDMLink> wdmList = WDMLink.WDMLinkList;
		LinkedList<WDMLink> nodeLinkedList = new LinkedList<>();
		for (WDMLink wdmLink : wdmList) {
			if (wdmLink.getFromNode().getName().equals(node.getName())
					|| wdmLink.getToNode().getName().equals(node.getName())) {
				nodeLinkedList.add(wdmLink);
			}
		}
		for (int i = 1; i <= DataSave.waveNum; i++) {
			int num = 0;
			for (WDMLink wdmLink : nodeLinkedList) {
				if (!wdmLink.getWavelengthById(i).getStatus().equals(Status.空闲)) {
					num++;
				}
			}
			if (num > node.getDynUsedUpdown()) {
				node.setDynUsedUpdown(num);
			}
		}
		return node.getDynUsedUpdown();
	}
	/**
	 * 
	 * @Desc 计算节点的度
	 * @param node
	 * @return
	   @author Lin
	   @Date 2018年10月8日 下午5:00:50
	 */
	public int countDegree(CommonNode node) {
		int degree = 0;
		LinkedList<WDMLink> wdmList = WDMLink.WDMLinkList;
		for (WDMLink wdmLink : wdmList) {
			if(wdmLink.getFromNode().getName().equals(node.getName()) || wdmLink.getToNode().getName().equals(node.getName())) {
				degree++;
			}
		}
		return degree;
	}

    

}
