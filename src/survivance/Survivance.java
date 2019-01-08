package survivance;

/**
 * @author CC
 * 功能：生存性/抗毁性评估/仿真准备工作
 * 备注：注意泛型的使用
 * @date 9.16
 */
import java.util.LinkedList;
import java.util.List;

import algorithm.CommonAlloc;
import algorithm.PortAlloc;
import algorithm.ResourceAlloc;
import algorithm.RouteAlloc;
import data.CommonNode;
import data.DataSave;
import data.FiberLink;
import data.LinkRGroup;
import data.Port;
import data.Route;
import data.FiberLink;
import data.Traffic;
import data.WDMLink;
import data.WaveLength;
import enums.TrafficStatus;

import java.awt.event.ItemListener;
import java.util.Iterator;
import survivance.PickedItem;

public class Survivance {
	//抗毁的重路由策略改变     @Carson 2017/11/16
    public static int mark;//用于标志选择哪种路由算法
//    public static int i;
    private List<Traffic> affectedTrafficList = new LinkedList<Traffic>();//记录受影响的业务
    private List<PickedItem>ItemList=new LinkedList<>();//记录用于算路的各种数值
    public static int ceshisur=0;
	/**
	 * 函数名称：setFault 函数作用：设定故障链路isbroken：false 输入参数：fiber链路 返回参数：TRUE
	 */
    //这里是设置一条短呢还是设置一堆断呢 现在是
	public static void setFault(List<FiberLink> FiberLinkList1) { // 设置故障状态
		for (int i = 0; i < FiberLinkList1.size(); ++i) {
			for(WDMLink wl:FiberLinkList1.get(i).getCarriedWDMLinkList()) {
			wl.setBroken(false);
			if(!wl.isBroken()) {
			System.out.println("wdmlink"+wl);
			}
			}
			// surLinkList1.get(i).setSurvived(true);
		}
	}

	/**
	 * 函数名称：setback 函数作用：恢复故障链路状态为TRUE bsurvice为false 输入参数：fiber链路 返回参数：TRUE
	 */
	public static void setback(List<FiberLink> FiberLinkList1) { // 恢复故障设置
		for (int i = 0; i < FiberLinkList1.size(); ++i) {
			for(WDMLink wl:FiberLinkList1.get(i).getCarriedWDMLinkList()) {
			wl.setBroken(true);
			}
        // surLinkList1.get(i).setSurvived(false);
		}
	}
	/*
	 * 解决不能多次进行重路由的问题
	 * 只能预先把所有的取出来进行判断，如果是通过的话，就再进行dynamRouting
	 * 所以要进行一个判断
	 */
	/**
	 * func:FindAllFault input:route
	 * 这个函数用于定位所有的故障
	 */
	public static List<PickedItem> FindAllFault(Route route) {
		System.out.println("进入FindAllFault");
		int locate = 0;
		Survivance sur = new Survivance();
		PickedItem temp = new PickedItem();
		//如果只有一条链路，直接返回一个空的itemlist，在judge中会被判断为无法使用动态重路由
		if(route.getWDMLinkList().size()==1) {
		    return sur.ItemList;
		}else {//没有只有一条链路的情况，都是多条链路的情况
		for (int i = 0; i < route.getWDMLinkList().size(); i++) {// 轮询路由中所有的wdmlink
			if (!route.getWDMLinkList().get(i).isBroken()) {// 如果发现了错误，错误位置是i
				if (i == route.getWDMLinkList().size() - 1) {// 情况1 i在末尾,整条路由有多条链路
					temp.setFromNode(route.getNodeList().get(i));
					temp.setToNode(route.getNodeList().get(i + 1));
					temp.setFromNum(i);// 这里认为是故障第一条链路和最后一条链路的编号
					temp.setToNum(i);
					temp.WaveLengthIDList.add(route.getWaveLengthIdList().get(i));
					for (int l = 0; l < route.getWaveChangedNode().size(); l++) {
						if (route.getWaveChangedNode().contains(temp.getFromNode())) {
							temp.setFromChanged(true);
						}
						if (route.getWaveChangedNode().contains(temp.getToNode())) {
							temp.setToChanged(true);
						}
					}
					temp.setWavei(route.getWaveLengthIdList().get(i - 1));
					sur.getItemList().add(temp);
				} else {//情况2 i在中间，整条路由有多条链路
					for (int j = i; j < route.getWDMLinkList().size(); j++) {
						if (j == route.getWDMLinkList().size() - 1) {
							temp.setFromNode(route.getNodeList().get(i));
							temp.setToNode(route.getNodeList().get(j + 1));
							temp.setFromNum(i);
							temp.setToNum(j);
							for (int k = i; k < j + 1; k++) {
								temp.WaveLengthIDList.add(route.getWaveLengthIdList().get(k));
							}
							for (int l = 0; l < route.getWaveChangedNode().size(); l++) {
								if (route.getWaveChangedNode().contains(temp.getFromNode())) {
									temp.setFromChanged(true);
								}
								if (route.getWaveChangedNode().contains(temp.getToNode())) {
									temp.setToChanged(true);
								}
							}
							if(i==0) {//如果i在首位，j在末位，在judge里就会被判断为不能使用动态重路由
								temp.setWavei(0);
								temp.setWavej(0);
							}else {//i不在首位，j在末位
							temp.setWavei(route.getWaveLengthIdList().get(i - 1));
							}
							sur.getItemList().add(temp);
							locate = j;// 如果不赋值的话i跳不出来
						}
						if (route.getWDMLinkList().get(j).isBroken()) {
							temp.setFromNode(route.getNodeList().get(i));
							temp.setToNode(route.getNodeList().get(j));
							temp.setFromNum(i);
							temp.setToNum(j);
							for (int k = i; k < j; k++) {
								temp.WaveLengthIDList.add(route.getWaveLengthIdList().get(k));
							}

							// 轮询判断首末节点是不是波长转换节点
							for (int l = 0; l < route.getWaveChangedNode().size(); l++) {
								if (route.getWaveChangedNode().contains(temp.getFromNode())) {
									temp.setFromChanged(true);
								}
								if (route.getWaveChangedNode().contains(temp.getToNode())) {
									temp.setToChanged(true);
								}
							}
							if (i == 0 && j != route.getWDMLinkList().size() - 1) {// 断纤在第一个
								temp.setWavej(route.getWaveLengthIdList().get(j + 1));
							}
							if (i != 0 && j != route.getWDMLinkList().size() - 1) {// 断纤在中间
								temp.setWavei(route.getWaveLengthIdList().get(i - 1));
								temp.setWavej(route.getWaveLengthIdList().get(j + 1));
							}
							locate = j;
							sur.getItemList().add(temp);
							break;
						}
					}
					i = locate;
					break;
				}  
			}
		}
		return sur.ItemList;
	  }
	}
	/**
	 * func:Judge input:route
	 * 这个函数用于判断是否能够动态重路由
	 */
	public static boolean Judge(Route route) {// 返回一个
		System.out.println("进入了judge");
		// 返回值false代表不能使用动态重路由策略 true代表可以使用动态重路由策略
		Route tempRoute = new Route();// 用来记录不一定能计算成功的动态路由
		List<PickedItem> templist = new LinkedList<>();
		List<Integer> WaveAbleToUse = new LinkedList<>();
		templist = FindAllFault(route);
		Survivance sur = new Survivance();
		if(templist.size()==0) {
			return false;//如果只有一条链路，直接返回false
		}else {
		for (int j = 0; j < WDMLink.WDMLinkList.size(); j++) {
			if (!WDMLink.WDMLinkList.get(j).isBroken()) {
				WDMLink.WDMLinkList.get(j).setActive(false);
			}
		}
		for (int i = 0; i < templist.size(); i++) {
			// 判断路由是否可算
	/*lty		tempRoute = ResourceAlloc.reRoute(route, templist.get(i).getFromNode(), templist.get(i).getToNode(),
					Survivance.mark);// 算路但是并不分配资源
			//tempRoute = ResourceAlloc.allocateResourceByWaveAndTwoNode(route.getBelongsTraffic(), 2, waveNum, Survivance.mark, templist.get(i).getFromNode(), templist.get(i).getToNode())
			if (tempRoute == null)
				return false;// 算不出路，不能使用动态重路由
			// 判断波长是否可以使用动态重路由策略
			for (int j = 0; j < templist.get(i).getWaveLengthIDList().size(); j++) {
				if (Route.isRouteHaveWave(tempRoute, templist.get(i).getWaveLengthIDList().get(j),
						route.getBelongsTraffic())) {
					WaveAbleToUse.add(templist.get(i).getWaveLengthIDList().get(j));
				}
			}*/
			
			for (int j = 0; j < templist.get(i).getWaveLengthIDList().size(); j++)
			{
				ResourceAlloc.setlinkActiveDynamic(route);
				tempRoute = ResourceAlloc.findRouteByWaveDynamic(route.getBelongsTraffic(), templist.get(i).getWaveLengthIDList().get(j), mark, templist.get(i).getFromNode(), templist.get(i).getToNode());
				if (tempRoute != null)
					WaveAbleToUse.add(templist.get(i).getWaveLengthIDList().get(j));
			}
			
			if (WaveAbleToUse.size() == 0) {
				return false;// 算出的路上没有空闲的波长，不能使用动态重路由
			} else {
				if (templist.get(i).getFromNum() == 0) {// 第一条链路断
					if (!templist.get(i).isToChanged()) {
						for (int k = 0; k < WaveAbleToUse.size(); k++) {
							if (!WaveAbleToUse.contains(templist.get(i).getWavej())) {
								return false;
							}
						}
					}
				}
				if (templist.get(i).getToNum() == route.getWDMLinkList().size() - 1) {// 最后一条链路断了
					if (!templist.get(i).isFromChanged()) {// 这里是不是有问题的
						for (int k = 0; k < WaveAbleToUse.size(); k++) {
							if (!WaveAbleToUse.contains(templist.get(i).getWavei())) {
								return false;
							}
						}
					}
				}
				if (templist.get(i).getFromNum() == 0
						&& templist.get(i).getToNum() == route.getWDMLinkList().size() - 1) {
					return false;
				}
				if (templist.get(i).getFromNum() != 0
						&& templist.get(i).getToNum() != route.getWDMLinkList().size() - 1) {
					if (!(templist.get(i).isFromChanged() && templist.get(i).isToChanged())) {// 头结点和末节点都不是波长转换节点
						if (!(WaveAbleToUse.contains(templist.get(i).getWavei())
								|| WaveAbleToUse.contains(templist.get(i).getWavej()))) {
							return false;
						}
					}
					if (templist.get(i).isFromChanged() == false && templist.get(i).isToChanged() == true) {
						if (!WaveAbleToUse.contains(templist.get(i).getWavei())) {
							return false;
						}
					}
					if (templist.get(i).isFromChanged() == true && templist.get(i).isToChanged() == false) {
						if (!WaveAbleToUse.contains(templist.get(i).getWavej())) {
							return false;
						}
					}
				}
			}
		}
		return true;// 如果判断都通过了，就返回1，可以使用动态重路由
	  }
	}
	
	
	/**
	 * func:DynamicRouting input: route 
	 * 这个函数综合整个动态路由策略
	 * @return 
	 */
	public static Route DynamicRouting(Route route) {
		System.out.println("进入了dynamicRouting");
		boolean flag=true;//标志后面的链路是没有错误的
		PickedItem Item=new PickedItem();//取出用作计算的数据
		List<Integer> WaveAbleToUse = new LinkedList<>();//可用的波长数
		Route fromRoute=new Route();
		Route toRoute=new Route();
		Route dynamicRoute=new Route();//已经计算成功的动态路由
		Route newRoute=new Route();//拼接好的新路由
		Route tempRoute=new Route();
		tempRoute=route;
		int i=0;
		for (i = 0; i < route.getWDMLinkList().size(); i++) {
			if (!route.getWDMLinkList().get(i).isBroken()) {
				//情况1 i在末尾
				if (i == route.getWDMLinkList().size() - 1) {
					Item.setFromNode(route.getNodeList().get(i));
					Item.setToNode(route.getNodeList().get(i + 1));
					Item.setFromNum(i);//这里认为是故障第一条链路和最后一条链路的标号
					Item.setToNum(i);
					Item.WaveLengthIDList.add(route.getWaveLengthIdList().get(i));
					for (int l = 0; l < route.getWaveChangedNode().size(); l++) {
						if (route.getWaveChangedNode().contains(Item.getFromNode())) {
							Item.setFromChanged(true);
						}
						if (route.getWaveChangedNode().contains(Item.getToNode())) {
							Item.setToChanged(true);
						}
					}
					Item.setWavei(route.getWaveLengthIdList().get(i-1));

				} else {//排除了i=j=size-1的情况，也就是i不在末尾
					//情况2,3,4
					for (int j = i; j < route.getWDMLinkList().size(); j++) {
						//情况2，如果j在最后，i在最前进不来，只可能在中间
						if (j==route.getWDMLinkList().size()-1) {
							Item.setFromNode(route.getNodeList().get(i));
							Item.setToNode(route.getNodeList().get(j+1));
							Item.setFromNum(i);
							Item.setToNum(j);
							for(int k=i;k<j+1;k++) {
								Item.WaveLengthIDList.add(route.getWaveLengthIdList().get(k));
							}
							for (int l = 0; l < route.getWaveChangedNode().size(); l++) {
								if (route.getWaveChangedNode().contains(Item.getFromNode())) {
									Item.setFromChanged(true);
								}
								if (route.getWaveChangedNode().contains(Item.getToNode())) {
									Item.setToChanged(true);
								}
							}
							Item.setWavei(route.getWaveLengthIdList().get(i-1));
						}
						//进来就说明j不是最后一个，包含两种情况，i在和不在开头，并且j不在结尾的问题
						if (route.getWDMLinkList().get(j).isBroken()) {
							Item.setFromNode(route.getNodeList().get(i));
							Item.setToNode(route.getNodeList().get(j));
							Item.setFromNum(i);
							Item.setToNum(j-1);
							for (int k = i; k < j; k++) {
								Item.WaveLengthIDList.add(route.getWaveLengthIdList().get(k));
							}
							// 轮询判断首末节点是不是波长转换节点
							for (int l = 0; l < route.getWaveChangedNode().size(); l++) {
								if (route.getWaveChangedNode().contains(Item.getFromNode())) {
									Item.setFromChanged(true);
								}
								if (route.getWaveChangedNode().contains(Item.getToNode())) {
									Item.setToChanged(true);
								}
							}
							if (i == 0 && j != route.getWDMLinkList().size() - 1) {// 断纤在第一个
								Item.setWavej(route.getWaveLengthIdList().get(j + 1));
							}
							if (i != 0 && j != route.getWDMLinkList().size() - 1) {// 断纤在中间
								Item.setWavei(route.getWaveLengthIdList().get(i - 1));
								Item.setWavej(route.getWaveLengthIdList().get(j + 1));
							}
							break;
						}
					}	
				}	
				flag = false;// 标志链路中出现了故障
				break;
			}
		} // 寻找到了有错误的位置
		if (!flag) {
//			faultType = LocateFault(route, i);
			// 切路由
			fromRoute = SpliceFromRoute(route, i);
			toRoute = SpliceToRoute(route, Item.getToNum());//是否超界了还要再判断
/*lty			// 进行动态重路由的计算
			dynamicRoute = ResourceAlloc.reRoute(route, Item.getFromNode(), Item.getToNode(), Survivance.mark);// 计算重路由
			//判断使用哪个波长
			for (int k = 0; k < Item.getWaveLengthIDList().size(); k++) {
				if (Route.isRouteHaveWave(dynamicRoute, Item.getWaveLengthIDList().get(k), route.getBelongsTraffic())) {
					WaveAbleToUse.add(Item.getWaveLengthIDList().get(k));
				}
			}*/
			for (int k = 0; k < Item.getWaveLengthIDList().size(); k++)
			{
				ResourceAlloc.setlinkActiveDynamic(route);
				dynamicRoute = ResourceAlloc.findRouteByWaveDynamic(route.getBelongsTraffic(), Item.getWaveLengthIDList().get(k), mark, Item.getFromNode(), Item.getToNode());
				if (dynamicRoute != null)
					WaveAbleToUse.add(Item.getWaveLengthIDList().get(k));
			}
			
			if (Item.getFromNum() == 0) {// 第一条链路断
				if (Item.isToChanged()) {//末节点是波长转换节点
					dynamicRoute = ResourceAlloc.allocWaveForRoute(dynamicRoute, WaveAbleToUse.get(0),route.getBelongsTraffic());
				}else {//末节点不是波长转换节点
					for(int k=0;k<WaveAbleToUse.size();k++) {
						if (WaveAbleToUse.contains(Item.getWavej())) {//可用的波长里有末节点的波长，就要用末节点的波长
							dynamicRoute=ResourceAlloc.allocWaveForRoute(dynamicRoute, Item.getWavej(),route.getBelongsTraffic());
						}
					}
				}
			}
			if (Item.getToNum()==route.getWDMLinkList().size()-1) {//最后一条链路断
				if (Item.isFromChanged()) {//首节点是波长转换节点
					dynamicRoute=ResourceAlloc.allocWaveForRoute(dynamicRoute,WaveAbleToUse.get(0),route.getBelongsTraffic());					
				}else {//首节点不是波长转换节点
						if (WaveAbleToUse.contains(Item.getWavei())) {//可用的波长里有首节点的波长，就要用首节点的波长
							dynamicRoute=ResourceAlloc.allocWaveForRoute(dynamicRoute, Item.getWavei(),route.getBelongsTraffic());
						}
				}
			}
			if(Item.getFromNum()!=0 && Item.getToNum()!=route.getWDMLinkList().size()-1) {//断在中间
				//首末节点都不是波长转换节点，状态FF，只有首末波长一样并且存在于可用波长里的时候才可以用
				if (Item.isFromChanged()==false&&Item.isToChanged()==false) {
					if (Item.getWavei()==Item.getWavej()&&WaveAbleToUse.contains(Item.getWavei())) {
						dynamicRoute=ResourceAlloc.allocWaveForRoute(dynamicRoute, Item.getWavei(),route.getBelongsTraffic());
					}
				}
				//状态TT，可用波长里随便选一个就行
				if(Item.isFromChanged()==true&&Item.isToChanged()==true) {
					dynamicRoute=ResourceAlloc.allocWaveForRoute(dynamicRoute, WaveAbleToUse.get(0),route.getBelongsTraffic());
				}
				//状态FT，判断首节点的波长是否可用，可用就用
				if(Item.isFromChanged()==false&&Item.isToChanged()==false) {
					if(WaveAbleToUse.contains(Item.getWavei())) {
						dynamicRoute=ResourceAlloc.allocWaveForRoute(dynamicRoute, Item.getWavei(),route.getBelongsTraffic());
					}
				}
				//状态TF，判断末节点的波长是否可用，可用就用
				if(Item.isFromChanged()==true&&Item.isToChanged()==false) {
					if(WaveAbleToUse.contains(Item.getWavej())){
						dynamicRoute=ResourceAlloc.allocWaveForRoute(dynamicRoute, Item.getWavej(),route.getBelongsTraffic());						
					}
				}
			}
			// 拼路由作为下次输入
			
			newRoute = Joint(fromRoute, toRoute, dynamicRoute);			
			newRoute = DynamicRouting(newRoute);
		}
//		//进行回溯，因为一旦动态路由计算不成功，前几次计算成功的要清除其资源
//		if(newRoute==null) {
//			
//		}
		if(newRoute.getWDMLinkList().size()==0) {
			return tempRoute;
		}
		return newRoute;
	}


		

//				if (nItem.getnFromWaveLengthID()==nItem.getnToWaveLengthID()){
//			    	dynamicRoute=ResourceAlloc.allocWaveForRoute(dynamicRoute, nItem.getnFromWaveLengthID(), route.getBelongsTraffic());
//				}
//				//如果断的节点是波长转换的节点，要分配不增加或减少波长转换节点的波长
//				if(nItem.getnFromWaveLengthID()!=nItem.getnToWaveLengthID()) {
//					
//				}
//			    if((Route.isRouteHaveWave(dynamicRoute, nItem.getnFromWaveLengthID(),route.getBelongsTraffic())//两个同时可分配或第一个波长可分配
//			    		&&Route.isRouteHaveWave(dynamicRoute, nItem.getnToWaveLengthID(),route.getBelongsTraffic()))){
//			    	if(nItem.getnFromWaveLengthID()!=nItem.getnToWaveLengthID()) {
//			    		
//			    	}
//			    	
//			    }
//			    else {if (Route.isRouteHaveWave(dynamicRoute, nItem.getnFromWaveLengthID(),route.getBelongsTraffic())){
//			    	dynamicRoute=ResourceAlloc.allocWaveForRoute(dynamicRoute, nItem.getnFromWaveLengthID(), route.getBelongsTraffic());
//			    }
//			    if (Route.isRouteHaveWave(dynamicRoute, nItem.getnToWaveLengthID(),route.getBelongsTraffic())){//第二个波长可分配
//			    	dynamicRoute=ResourceAlloc.allocWaveForRoute(dynamicRoute, nItem.getnToWaveLengthID(), route.getBelongsTraffic());
//			    }}
//			}


//	/**
//	 * func:LocateFault  input:route flag output:fault type
//	 * flag：故障链路第一次出现的位置
//	 * type：1 节点故障  2 链路故障
//	 */
//	public static int LocateFault(Route route,int flag) {
//		int type = 0;
//		if (flag == route.getWDMLinkList().size() - 1) {
//			type = 2;// 定位在末端处理为链路故障,同样的首端自动处理为链路故障
//		} else {
//			if (!route.getWDMLinkList().get(flag + 1).isBroken()) {
//				type = 1;// 节点故障
//			}
//			if (route.getWDMLinkList().get(flag + 1).isBroken()) {
//				type = 2;// 链路故障
//			}
//		}
//		return type;
//	}
	/**
	 * 函数名称： SpliceFromRoute  输入route flag 输出 FromRoute 这个的调用必须在Satfault之后
	 * flag:故障的位置
     * 这里的输入应该是用Route
	 * 
	 */
	public static Route SpliceFromRoute(Route route,int flag) {
		Route fromRoute = new Route();
		//端口的排列不是按顺序的，这里处理成按顺序的。
//		List<Port> portlist = new LinkedList<>();
//		List<Port> simportlist = new LinkedList<>();
//		portlist = route.getPortList();
//		simportlist = route.getSimPortList();
//		Port temp = new Port(); 
//		temp = portlist.get(1);
//		portlist.remove(1);
//		portlist.add(temp);
//		temp = simportlist.get(1);
//		simportlist.remove(1);
//		simportlist.add(temp);
		//端口处理完毕
	    // 如果flag=0也就是断纤在头位置，下面的循环不会执行，fromRoute是null
	    // 属性是不够的还要增加很多个
		fromRoute.setFrom(route.getFrom());// 不用输入to，因为拼接的时候注定会被替代
		fromRoute.setHops(flag);
		fromRoute.setBelongsTraffic(route.getBelongsTraffic());
		fromRoute.setNotConsistent(route.isNotConsistent());
		for (int j = 0; j < flag; j++) {
			fromRoute.getNodeList().add(route.getNodeList().get(j)); // 取出的节点不包括短纤的节点
			fromRoute.getWDMLinkList().add(route.getWDMLinkList().get(j));
			fromRoute.getWaveLengthIdList().add(route.getWaveLengthIdList().get(j));
//			fromRoute.getPortList().add(portlist.get(j)); // 取出的端口不包括短纤的端口
//			fromRoute.getSimPortList().add(simportlist.get(j));
//			// 还有flag的问题
		}
		fromRoute.setUsedWaveList(fromRoute.getUsedWaveList());
		fromRoute.setWaveChangedNode(fromRoute.getWaveLengthIdList());
		return fromRoute;
	}
    /**
	 * 函数名称 SpliceToRoute 输入route flag 输出Route
	 * flag:故障的位置
	 */
    public static Route SpliceToRoute(Route route,int flag) {
    	Route toRoute=new Route();
    	//端口的排列不是按顺序的，这里处理成对应的。
//		List<Port> portlist = new LinkedList<>();
//		List<Port> simportlist = new LinkedList<>();
//		portlist = route.getPortList();
//		simportlist = route.getSimPortList();
//		Port temp = new Port();
//		temp = portlist.get(1);
//		portlist.remove(1);
//		portlist.add(temp);
//		temp = simportlist.get(1);
//		simportlist.remove(1);
//		simportlist.add(temp);
		//端口处理完毕
    	//如果i=link的长度-1，下面的循环不会执行，ToRoute是null
    	toRoute.setTo(route.getTo());
    	toRoute.setHops(flag);//这个不对
    	toRoute.setBelongsTraffic(route.getBelongsTraffic());
    	toRoute.setNotConsistent(route.isNotConsistent());
    	
    	for(int j=flag+1;j<route.getWDMLinkList().size();j++) {
    		toRoute.getNodeList().add(route.getNodeList().get(j+1));//取出的节点不包括短纤的节点
    		toRoute.getWDMLinkList().add(route.getWDMLinkList().get(j));
    		toRoute.getWaveLengthIdList().add(route.getWaveLengthIdList().get(j));
//    		toRoute.getPortList().add(route.getPortList().get(j+1));//
//    		toRoute.getSimPortList().add(route.getSimPortList().get(j+1));//
    	}
    	toRoute.setUsedWaveList(toRoute.getUsedWaveList());
    	toRoute.setWaveChangedNode(toRoute.getWaveLengthIdList());
    	return toRoute; 	
    }
    
    /**
     * 函数名称：Joint 函数作用：拼接路由 输入参数FromRoute ToRoute ComputeRoute
     */
    public static Route Joint(Route FromRoute,Route ToRoute,Route ComputeRoute) {
    	
    	Route route =new Route();
    	route.setFrom(FromRoute.getFrom());
    	route.setTo(ToRoute.getTo());
    	route.setHops(FromRoute.getHops()+ToRoute.getHops()+ComputeRoute.getHops());
    	route.setBelongsTraffic(FromRoute.getBelongsTraffic());
    	route.setNotConsistent(ComputeRoute.isNotConsistent());
		if (FromRoute != null) {
			for (int i = 0; i < FromRoute.getWDMLinkList().size(); i++) {
				route.getWDMLinkList().add(FromRoute.getWDMLinkList().get(i));
				route.getWaveLengthIdList().add(FromRoute.getWaveLengthIdList().get(i));
//				route.getSimPortList().add(FromRoute.getSimPortList().get(i));
//				route.getPortList().add(FromRoute.getPortList().get(i));
			}
			for(int j=0;j<FromRoute.getNodeList().size();j++) {
				route.getNodeList().add(FromRoute.getNodeList().get(j));
			}
			for(int k=0;k<FromRoute.getUsedWaveList().size();k++) {
				if(FromRoute.getUsedWaveList().size()!=0) {
				route.getUsedWaveList().add(FromRoute.getUsedWaveList().get(k));
				}
			}
		}
		if (ComputeRoute != null) {
			for (int i = 0; i < ComputeRoute.getWDMLinkList().size(); i++) {
				route.getWDMLinkList().add(ComputeRoute.getWDMLinkList().get(i));
				route.getWaveLengthIdList().add(ComputeRoute.getWaveLengthIdList().get(i));
//				route.getSimPortList().add(ComputeRoute.getSimPortList().get(i));
//				route.getPortList().add(ComputeRoute.getPortList().get(i));
			}
			for(int j=0;j<ComputeRoute.getNodeList().size();j++) {
				route.getNodeList().add(ComputeRoute.getNodeList().get(j));
			}
			for(int k=0;k<ComputeRoute.getUsedWaveList().size();k++) {
				if(ComputeRoute.getUsedWaveList().size()!=0) {
				route.getUsedWaveList().add(ComputeRoute.getUsedWaveList().get(k));
				}
			}
		}
		if (ToRoute !=null) {
			for (int i=0;i<ToRoute.getWDMLinkList().size();i++) {
				route.getWDMLinkList().add(ToRoute.getWDMLinkList().get(i));
				route.getWaveLengthIdList().add(ToRoute.getWaveLengthIdList().get(i));
//				route.getSimPortList().add(ToRoute.getSimPortList().get(i));
//				route.getPortList().add(ToRoute.getPortList().get(i));
			}
			for(int j=0;j<ToRoute.getNodeList().size();j++) {
				route.getNodeList().add(ToRoute.getNodeList().get(j));
			}
			for(int k=0;k<ToRoute.getUsedWaveList().size();k++) {
				if(ToRoute.getUsedWaveList().size()!=0) {
				route.getUsedWaveList().add(ToRoute.getUsedWaveList().get(k));
				}
			}
		}
    	return route;
    }
	/**
	 * 函数名称：reCompute 函数作用：为故障业务重新算路，恢复业务 输入参数：traffic链路 返回参数：
	 */
	public static void reCompute(List<Traffic> trafficList,CommonNode node) {
		// assert FiberLink.allFiberLinkList.size() == 35 : "链路改变了";// 10.27
		for (int i = 0; i < trafficList.size(); ++i) {
			Traffic tra = trafficList.get(i);
			int type = mark;
			switch (tra.getFaultType()) {// 业务故障类型1 工作 2 保护 3工作保护
			case 1: // 1为工作受影响
				switch (tra.getProtectLevel()) {// 业务保护等级
				case PERMANENT11:
					if (tra.getProtectRoute() != null && tra.getProtectRoute().getFiberLinkList() != null
							&& tra.getProtectRoute().getFiberLinkList().size() != 0) {
						tra.setResumeRoute(tra.getProtectRoute());
						setLinkStatus(tra, false);
						if (Judge(tra.getWorkRoute())) {
							for(int j=0;j<WDMLink.WDMLinkList.size();j++) {
								if(!WDMLink.WDMLinkList.get(j).isBroken()) {
									WDMLink.WDMLinkList.get(j).setActive(false);
								}
							}
							tra.setResumeRoutePro(DynamicRouting(tra.getWorkRoute()));							
						}else {
							for(int j=0;j<WDMLink.WDMLinkList.size();j++) {
								if(!WDMLink.WDMLinkList.get(j).isBroken()) {
									WDMLink.WDMLinkList.get(j).setActive(false);
								}
							}
							tra.setResumeRoutePro(algorithm.ResourceAlloc.reRouteForTraffic(tra.getWorkRoute(),Survivance.mark));
						}
						setLinkStatus(tra, true);
					} else {
						if (Judge(tra.getWorkRoute())) {
							for(int j=0;j<WDMLink.WDMLinkList.size();j++) {
								if(!WDMLink.WDMLinkList.get(j).isBroken()) {
									WDMLink.WDMLinkList.get(j).setActive(false);
								}
							}
							tra.setResumeRoute(DynamicRouting(tra.getWorkRoute()));
						}else {
							for(int j=0;j<WDMLink.WDMLinkList.size();j++) {
								if(!WDMLink.WDMLinkList.get(j).isBroken()) {
									WDMLink.WDMLinkList.get(j).setActive(false);
								}
							}
							tra.setResumeRoute(algorithm.ResourceAlloc.reRouteForTraffic(tra.getWorkRoute(),Survivance.mark));
						}
					}
					break;
				case NORMAL11:
					if (tra.getProtectRoute() != null && tra.getProtectRoute().getFiberLinkList() != null
							&& tra.getProtectRoute().getFiberLinkList().size() != 0) {
						tra.setResumeRoute(tra.getProtectRoute());
					}
					break;
				case RESTORATION:
					if(tra.getPreRoute()!=null&&tra.getPreRoute().getWDMLinkList()!=null //判断专享预制是否存在,同时要判断业务的首末节点是否断了
					&& tra.getPreRoute().getWDMLinkList().size()!=0 
					&& !(tra.getFromNode().equals(node))
					&& !(tra.getToNode().equals(node))) {
						tra.setResumeRoute(tra.getPreRoute());
					}else {
						if (DataSave.OSNR) {
							for (int j = 0; j < WDMLink.WDMLinkList.size(); j++) {
								if (!WDMLink.WDMLinkList.get(j).isBroken()) {
									WDMLink.WDMLinkList.get(j).setActive(false);
								}
							}
							if (Judge(tra.getWorkRoute())) {
								Route resumroute = DynamicRouting(tra.getWorkRoute());
								if(resumroute.meetOSNR()) {
								tra.setResumeRoute(resumroute);
								}
							}
							// 动态重路由如果满足osnr则启用，不满足的话就对整条路由进行重算
							if(tra.getResumeRoute() == null) {
								Route route = null;
								// 设置链路激活状态
								// 将必避链路、节点设为不激活
								ResourceAlloc.setMustAvoidUnactive(tra);

								// 关联业务组设置设置不激活（包括设置相应的SRLG为不激活）
								ResourceAlloc.setRelatedTrafficUnactive(tra, DataSave.separate);
								
								//设置故障链路不激活
								for (int j = 0; j < WDMLink.WDMLinkList.size(); j++) {
									if (!WDMLink.WDMLinkList.get(j).isBroken()) {
										WDMLink.WDMLinkList.get(j).setActive(false);
									}
								}
								// 设置保护路由不激活来计算动态路由
								if (tra.getProtectRoute() != null) {
									ResourceAlloc.setRouteLinkUnactive(tra.getProtectRoute());
								}


								// 将资源不足的链路设为不激活 输入：是否是计算预置路由
								LinkedList<WDMLink> canActiveLinks = ResourceAlloc.resourceNotEnough(true);

								
								route = ResourceAlloc.findMeetOSNRroute(route, tra, Survivance.mark, 2);
								if (route == null) {
									for (WDMLink link : canActiveLinks) {
										link.setActive(true);
									}
									route = ResourceAlloc.findMeetOSNRroute(route, tra, Survivance.mark, 2);
								}
								for (int k = 0; k < WDMLink.WDMLinkList.size(); k++) {
									WDMLink.WDMLinkList.get(k).setActive(true);
								}

								if (route != null && route.getWDMLinkList().size() != 0) {
									if (CommonAlloc.allocateWaveLength3(route)) {// 对光纤链路分配时隙成功，并且分配端口成功
										// CommonAlloc.fallBuffer.append("业务:" + traffic + "预置路由及资源分配成功！\r\n");
										tra.setDynamicRoute(route);// 如果路由不为空则设置预置重路由
										tra.setStatus(TrafficStatus.动态重路由已分配);
										tra.setResumeRoute(route);
									}
								}

							}

						}else {//不需要OSNR判断
							if (Judge(tra.getWorkRoute())) {
								for(int j=0;j<WDMLink.WDMLinkList.size();j++) {
									if(!WDMLink.WDMLinkList.get(j).isBroken()) {
										WDMLink.WDMLinkList.get(j).setActive(false);
									}
								}
								tra.setResumeRoute(DynamicRouting(tra.getWorkRoute()));						
							}else {
								for(int j=0;j<WDMLink.WDMLinkList.size();j++) {
									if(!WDMLink.WDMLinkList.get(j).isBroken()) {
										WDMLink.WDMLinkList.get(j).setActive(false);
									}
								}
								
								Route newr=null;
								newr = ResourceAlloc.allocateResourceByWave1(tra, 2, Survivance.mark);
								if(newr != null && newr.getWDMLinkList().size()!=0) {
									System.out.println(tra+"111111111111111111111111111111111111111111111111");
									tra.setResumeRoute(newr);
								}else {
									for(int j=0;j<WDMLink.WDMLinkList.size();j++) {
										if(!WDMLink.WDMLinkList.get(j).isBroken()) {
											WDMLink.WDMLinkList.get(j).setActive(false);
										}
									}
									System.out.println(tra+"22222222222222222222222222222222222222222222222222");
									tra.setResumeRoute(algorithm.ResourceAlloc.reRouteForTraffic(tra.getWorkRoute(),Survivance.mark));
									}
								
								//tra.setResumeRoute(algorithm.ResourceAlloc.reRouteForTraffic(tra.getWorkRoute(),Survivance.mark));
							}
						}
						
					}
					break;
				case PROTECTandRESTORATION:
					if (tra.getProtectRoute() != null && tra.getProtectRoute().getFiberLinkList() != null//优先使用1+1的保护路由作为恢复路由
							&& tra.getProtectRoute().getFiberLinkList().size() != 0
							&& !(tra.getFromNode().equals(node))
							&& !(tra.getToNode().equals(node))){
						tra.setResumeRoute(tra.getProtectRoute());
					}else {
						if(tra.getPreRoute()!=null&&tra.getPreRoute().getFiberLinkList()!=null //判断专享预制是否存在，同时判断业务的首末节点是否断了
						&& tra.getPreRoute().getFiberLinkList().size()!=0
						&& !(tra.getFromNode().equals(node))
						&& !(tra.getToNode().equals(node))) {
						   tra.setResumeRoute(tra.getPreRoute());
						}else {
							if (Judge(tra.getWorkRoute())) {
								for(int j=0;j<WDMLink.WDMLinkList.size();j++) {
									if(!WDMLink.WDMLinkList.get(j).isBroken()) {
										WDMLink.WDMLinkList.get(j).setActive(false);
									}
								}
								tra.setResumeRoute(DynamicRouting(tra.getWorkRoute()));							
							}else {
								for(int j=0;j<WDMLink.WDMLinkList.size();j++) {
									if(!WDMLink.WDMLinkList.get(j).isBroken()) {
										WDMLink.WDMLinkList.get(j).setActive(false);
									}
								}
								tra.setResumeRoute(algorithm.ResourceAlloc.reRouteForTraffic(tra.getWorkRoute(),Survivance.mark));
							}
						}
					}
					break;
				case NONPROTECT:
					break;
				default:
					break;
				}
				break;
			case 2: // 2为保护受影响
				switch (tra.getProtectLevel()) {
				case PERMANENT11:
					tra.setResumeRoute(tra.getWorkRoute());
					setLinkStatus(tra, false);
					if (Judge(tra.getProtectRoute())) {
						for(int j=0;j<WDMLink.WDMLinkList.size();j++) {
							if(!WDMLink.WDMLinkList.get(j).isBroken()) {
								WDMLink.WDMLinkList.get(j).setActive(false);
							}
						}
						tra.setResumeRoutePro(DynamicRouting(tra.getProtectRoute()));					
					}else {
						for(int j=0;j<WDMLink.WDMLinkList.size();j++) {
							if(!WDMLink.WDMLinkList.get(j).isBroken()) {
								WDMLink.WDMLinkList.get(j).setActive(false);
							}
						}
						tra.setResumeRoutePro(algorithm.ResourceAlloc.reRouteForTraffic(tra.getProtectRoute(),Survivance.mark));
					}
					setLinkStatus(tra, true);
					break;
				case NORMAL11:
					tra.setResumeRoute(tra.getWorkRoute());
					break;
				case RESTORATION:
					tra.setResumeRoute(tra.getWorkRoute());
					break;
				case PROTECTandRESTORATION:
					tra.setResumeRoute(tra.getWorkRoute());
					break;
				case NONPROTECT:
					tra.setResumeRoute(tra.getWorkRoute());
					break;
				default:
					break;
				}
				break;
			case 3: // 3为工作保护都受影响
				switch (tra.getProtectLevel()) {
				case PERMANENT11:
					if (Judge(tra.getWorkRoute())) {
						for(int j=0;j<WDMLink.WDMLinkList.size();j++) {
							if(!WDMLink.WDMLinkList.get(j).isBroken()) {
								WDMLink.WDMLinkList.get(j).setActive(false);
							}
						}
						tra.setResumeRoute(DynamicRouting(tra.getWorkRoute()));						
					}else {
						for(int j=0;j<WDMLink.WDMLinkList.size();j++) {
							if(!WDMLink.WDMLinkList.get(j).isBroken()) {
								WDMLink.WDMLinkList.get(j).setActive(false);
							}
						}
						tra.setResumeRoute(algorithm.ResourceAlloc.reRouteForTraffic(tra.getWorkRoute(),Survivance.mark));
					}
					setLinkStatus(tra, false);
					if (Judge(tra.getProtectRoute())) {
						for(int j=0;j<WDMLink.WDMLinkList.size();j++) {
							if(!WDMLink.WDMLinkList.get(j).isBroken()) {
								WDMLink.WDMLinkList.get(j).setActive(false);
							}
						}
						tra.setResumeRoutePro(DynamicRouting(tra.getProtectRoute()));					
					}else {
						for(int j=0;j<WDMLink.WDMLinkList.size();j++) {
							if(!WDMLink.WDMLinkList.get(j).isBroken()) {
								WDMLink.WDMLinkList.get(j).setActive(false);
							}
						}
						tra.setResumeRoutePro(algorithm.ResourceAlloc.reRouteForTraffic(tra.getProtectRoute(),Survivance.mark));
					}					
					setLinkStatus(tra, true);
					break;
				case NORMAL11:
					break;
				case RESTORATION:
					if(tra.getPreRoute()!=null&&tra.getPreRoute().getFiberLinkList()!=null //判断专享预制是否存在
					&& tra.getPreRoute().getFiberLinkList().size()!=0
					&& !(tra.getFromNode().equals(node))
					&& !(tra.getToNode().equals(node))) {
						tra.setResumeRoute(tra.getPreRoute());
					}else {
						break;
					}
					break;
				case PROTECTandRESTORATION:
					if(tra.getPreRoute()!=null&&tra.getPreRoute().getFiberLinkList()!=null //判断专享预制是否存在
					&& tra.getPreRoute().getFiberLinkList().size()!=0
					&& !(tra.getFromNode().equals(node))
				    && !(tra.getToNode().equals(node))){
						tra.setResumeRoute(tra.getPreRoute());
					}else {
						break;
					}
					break;
				case NONPROTECT:
					break;
				default:
					break;
				}
				break;
			}// end switch
		} // end for
			// assert FiberLink.allFiberLinkList.size() == 35 : "链路改变了";// 10.27

	}

	/**
	 * 功能：设置业务的恢复路由状态为b
	 * 
	 * @param tra
	 * @param b
	 */
	private static void setLinkStatus(Traffic tra, boolean b) {
		Route resumeRoute = tra.getResumeRoute();
		if (resumeRoute == null)
			return;// 找不着路时的 2016.11.1
		for (WDMLink bl : resumeRoute.getWDMLinkList()) {
			bl.setActive(b);
		}
	}

	/**
	 * 功能：通过受影响的linkList得到受影响的业务，并设置业务的故障类型
	 * 
	 * @param linkList
	 * 
	 */
	public List<Traffic> getAffectedTraffic(List<FiberLink> affectedLinkList) {

		// assert FiberLink.allFiberLinkList.size() == 35 : "链路改变了";// 10.27
        affectedTrafficList.clear();
		for (int i = 0; i < affectedLinkList.size(); ++i) // 遍历受影响的链路
		{
			FiberLink bLink = affectedLinkList.get(i);
			for (WDMLink wLink : bLink.getCarriedWDMLinkList()) {
				if (wLink.getCarriedTrafficList() == null)
					return null; // CC 2016.10.20 为没有承载业务的链路做保护防止异常
				List<Traffic> temptraList = wLink.getCarriedTrafficList(); // 受影响的链路上的业务链
				ceshisur+=temptraList.size();
				if (temptraList != null) {
					for (int j = 0; j < temptraList.size(); ++j) {// 遍历当前链路上的业务
						Traffic tra = temptraList.get(j);// 此业务的路由至少有一条受到影响
						System.out.println("业务的名称是"+tra.getName());
						if (!affectedTrafficList.contains(tra)) {// 判断是否出现过重复的受影响业务
							if (tra.getWorkRoute().getWDMLinkList().contains(wLink)) {// 判断工作路由是否包含当前受影响链路
								tra.setFaultType(1); // 工作受影响

								for (FiberLink curbl : affectedLinkList) {
									if (tra.getProtectRoute() == null)
										break;
									if (tra.getProtectRoute().getFiberLinkList().contains(curbl)) {
										tra.setFaultType(3); // 工作和保护都受影响
										break;
									}
								}
							} // 包含工作路由结束
							else if (tra.getProtectRoute() != null) {
								if (tra.getProtectRoute().getWDMLinkList().contains(wLink)) {
									tra.setFaultType(2); // 保护受影响、

									for (FiberLink curbl : affectedLinkList) {
										if (tra.getWorkRoute().getFiberLinkList().contains(curbl)) {
											tra.setFaultType(3); // 工作和保护都受影响
											break;
										}
									}
								}
							} // 包含保护路由结束
							if(tra.getFaultType()!=0) {
							   affectedTrafficList.add(tra);
							}
						}
					}
				}
			}
		}
		return affectedTrafficList;
	
	}
	/**
	 * 函数名称：getNodeItem 输入Route flag 输出pickeditem @author Carson
	 * flag:故障的位置
	 */
//	//取出故障的当前断纤上的波长，断纤的节点，flag。
//	public static PickedItem getNodeItem(Route route,int flag) {
//		PickedItem pick=new PickedItem();
//			pick.setnFromNode(route.getWDMLinkList().get(flag).getFromNode());
//			pick.setnToNode(route.getWDMLinkList().get(flag+1).getToNode());
//			pick.setnFromWaveLengthID(flag);
//			pick.setnToWaveLengthID(flag+1);
//		return pick;
//	}
//	/**
//	 * 函数名称：getLinkItem  输入Route flag 输出pickedItem
//	 * flag：故障的位置
//	 */ 
//	public static PickedItem getLinkItem(Route route,int flag) {
//		PickedItem pick=new PickedItem(); 
//		{
//			pick.setlFromNode(route.getWDMLinkList().get(flag).getFromNode());
//			pick.setlToNode(route.getWDMLinkList().get(flag).getToNode());
//			pick.setlWaveLengthID(route.getWaveLengthIdList().get(flag));
//		}
//		return pick;
//	}
	
//    /**
//     * 函数名称：getEffectedItem 输入FiberLinkList  @author Carson 
//     */
//    public  void getEffectItem(List<FiberLink>FiberLinkList) {
//    	for(FiberLink fl: FiberLinkList) {//遍历受影响的fiberlink
//    		List<WDMLink>WdmLinkList=fl.getCarriedWDMLinkList();//存放收影响的wdmlink
//    		for(WDMLink wl: WdmLinkList) {//遍历受影响的wdmlink
//    			if(wl.getCarriedTrafficList()==null) {//防止没有承载业务的链路报错
//    				continue;
//    			}
//    			List<Traffic>traList= getAffectedTraffic(FiberLinkList);//存放受影响的业务
//    			affectedTrafficList.clear();//清空受影响的业务
//    			if(traList!=null) {//判断受影响业务是否为空
//    				for(Traffic tra:traList) {//遍历受影响的业务
//    					PickedItem pick=new PickedItem();
//    					if (!affectedTrafficList.contains(tra)){//判断受影响的业务中有无重复的
//    						switch (tra.getFaultType()) {
//    						case 1://工作路由受影响
//    							for(int i=0;i<tra.getWorkRoute().getWDMLinkList().size();i++) {
//    								WDMLink wl1=tra.getWorkRoute().getWDMLinkList().get(i);
//    								if(wl1.equals(wl)&&(i!=0||i!=tra.getWorkRoute().getWDMLinkList().size()-1)) {
//    									pick.setTrafficId(tra.getTrafficNum());
//    									pick.setwFromNode(wl.getFromNode());
//    									pick.setwToNode(wl.getToNode());
//    									pick.setwFromWaveLengthID(tra.getWorkRoute().getWaveLengthIdList().get(i-1));
//    									pick.setwToWaveLengthID(tra.getWorkRoute().getWaveLengthIdList().get(i+1));
//    									pick.setwFromPort(tra.getWorkRoute().getPortList().get(i-1));
//    								    pick.setwToPort(tra.getWorkRoute().getPortList().get(i+1));
//    								}else if (wl1.equals(wl)&&i==0) {
//    									pick.setTrafficId(tra.getTrafficNum());
//    									pick.setwFromNode(wl.getFromNode());
//    									pick.setwToNode(wl.getToNode());
//    									pick.setwFromWaveLengthID(0);//因为波道号是从1-80所以认为0是不存在
//    									pick.setwToWaveLengthID(tra.getWorkRoute().getWaveLengthIdList().get(i+1));
//    									pick.setwFromPort(null);
//    								    pick.setwToPort(tra.getWorkRoute().getPortList().get(i+1));
//    								}else if (wl1.equals(wl)&&i==tra.getWorkRoute().getWDMLinkList().size()-1) {
//    									pick.setTrafficId(tra.getTrafficNum());
//    									pick.setwFromNode(wl.getFromNode());
//    									pick.setwToNode(wl.getToNode());
//    									pick.setwFromWaveLengthID(tra.getWorkRoute().getWaveLengthIdList().get(i-1));
//    									pick.setwToWaveLengthID(0);
//    									pick.setwFromPort(tra.getWorkRoute().getPortList().get(i-1));
//    								    pick.setwToPort(null);
//    								}		
//    							}  		
//    						break;
//    						case 2://保护路由受影响
//    							if(tra.getProtectRoute().getWDMLinkList().contains(wl)) {//不用加这一句判断，因为前面已经判定是保护路由受影响了
//        							for(int j=0;j<tra.getProtectRoute().getWDMLinkList().size();j++) {
//        								WDMLink wl2=tra.getProtectRoute().getWDMLinkList().get(j);
//        								if(wl2.equals(wl)&&(j!=0||j!=tra.getWorkRoute().getWDMLinkList().size()-1)) {
//        									pick.setTrafficId(tra.getTrafficNum());
//        									pick.setpFromNode(wl.getFromNode());
//        									pick.setpToNode(wl.getToNode());
//        									pick.setpFromWaveLengthID(tra.getProtectRoute().getWaveLengthIdList().get(j-1));
//        									pick.setpToWaveLengthID(tra.getProtectRoute().getWaveLengthIdList().get(j+1));
//        									pick.setpFromPort(tra.getProtectRoute().getPortList().get(j-1));
//        									pick.setpToPort(tra.getProtectRoute().getPortList().get(j+1));
//        								}else if(wl2.equals(wl)&&j==0) {
//        									pick.setTrafficId(tra.getTrafficNum());
//        									pick.setpFromNode(wl.getFromNode());
//        									pick.setpToNode(wl.getToNode());
//        									pick.setpFromWaveLengthID(0);
//        									pick.setpToWaveLengthID(tra.getProtectRoute().getWaveLengthIdList().get(j+1));
//        									pick.setpFromPort(null);
//        									pick.setpToPort(tra.getProtectRoute().getPortList().get(j+1));		
//        								}else if (wl2.equals(wl)&&j==tra.getProtectRoute().getWDMLinkList().size()-1) {
//        									pick.setTrafficId(tra.getTrafficNum());
//        									pick.setpFromNode(wl.getFromNode());
//        									pick.setpToNode(wl.getToNode());
//        									pick.setpFromWaveLengthID(tra.getProtectRoute().getWaveLengthIdList().get(j-1));
//        									pick.setpToWaveLengthID(0);
//        									pick.setpFromPort(tra.getProtectRoute().getPortList().get(j-1));
//        									pick.setpToPort(null);
//        								}//如果保护和工作同时受影响呢
//        								//保护和工作凭什么同时受影响？1+1不是要求物理分离么，重路由根本就没有保护路由啊。 多断的情况下会出现工作和保护同事出现问题的情况
//        								//我们假设可以的话，业务是多个，wdm是多条的情况下我怎么保证记录下来的东西可以分离？
//        								//不能分离的情况下，重新选路的路由怎么去对应业务  
//        							} //for end
//        						}//case2 if end
//    							break;
//    						}//switch end
//    					  }  //if end
//    					ItemList.add(pick);
//    					affectedTrafficList.add(tra);
//    				}
//    			}
//    		}
//    	}   			
//    }    		  
	/**
	 * 函数名称：getNodeEffectLink 函数作用：通过节点搜寻包含节点的链路 输入参数：node节点 返回参数：True
	 */
	public static List<FiberLink> getNodeEffectLink(CommonNode node) {
		List<FiberLink> basicLinkList = new LinkedList<FiberLink>();
		Iterator<FiberLink> it = FiberLink.getFiberLinkList().iterator();
		while (it.hasNext()) {
			FiberLink fl = it.next();
			if (fl.getFromNode().equals(node) || fl.getToNode().equals(node))
				basicLinkList.add(fl);
		}
		return basicLinkList;
	}

	public List<PickedItem> getItemList() {
		return ItemList;
	}

	public void setItemList(List<PickedItem> itemList) {
		ItemList = itemList;
	}


}
