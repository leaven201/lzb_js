package survivance;

/**
 * @author CC
 * ���ܣ�������/����������/����׼������
 * ��ע��ע�ⷺ�͵�ʹ��
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
	//���ٵ���·�ɲ��Ըı�     @Carson 2017/11/16
    public static int mark;//���ڱ�־ѡ������·���㷨
//    public static int i;
    private List<Traffic> affectedTrafficList = new LinkedList<Traffic>();//��¼��Ӱ���ҵ��
    private List<PickedItem>ItemList=new LinkedList<>();//��¼������·�ĸ�����ֵ
    public static int ceshisur=0;
	/**
	 * �������ƣ�setFault �������ã��趨������·isbroken��false ���������fiber��· ���ز�����TRUE
	 */
    //����������һ�����ػ�������һ�Ѷ��� ������
	public static void setFault(List<FiberLink> FiberLinkList1) { // ���ù���״̬
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
	 * �������ƣ�setback �������ã��ָ�������·״̬ΪTRUE bsurviceΪfalse ���������fiber��· ���ز�����TRUE
	 */
	public static void setback(List<FiberLink> FiberLinkList1) { // �ָ���������
		for (int i = 0; i < FiberLinkList1.size(); ++i) {
			for(WDMLink wl:FiberLinkList1.get(i).getCarriedWDMLinkList()) {
			wl.setBroken(true);
			}
        // surLinkList1.get(i).setSurvived(false);
		}
	}
	/*
	 * ������ܶ�ν�����·�ɵ�����
	 * ֻ��Ԥ�Ȱ����е�ȡ���������жϣ������ͨ���Ļ������ٽ���dynamRouting
	 * ����Ҫ����һ���ж�
	 */
	/**
	 * func:FindAllFault input:route
	 * ����������ڶ�λ���еĹ���
	 */
	public static List<PickedItem> FindAllFault(Route route) {
		System.out.println("����FindAllFault");
		int locate = 0;
		Survivance sur = new Survivance();
		PickedItem temp = new PickedItem();
		//���ֻ��һ����·��ֱ�ӷ���һ���յ�itemlist����judge�лᱻ�ж�Ϊ�޷�ʹ�ö�̬��·��
		if(route.getWDMLinkList().size()==1) {
		    return sur.ItemList;
		}else {//û��ֻ��һ����·����������Ƕ�����·�����
		for (int i = 0; i < route.getWDMLinkList().size(); i++) {// ��ѯ·�������е�wdmlink
			if (!route.getWDMLinkList().get(i).isBroken()) {// ��������˴��󣬴���λ����i
				if (i == route.getWDMLinkList().size() - 1) {// ���1 i��ĩβ,����·���ж�����·
					temp.setFromNode(route.getNodeList().get(i));
					temp.setToNode(route.getNodeList().get(i + 1));
					temp.setFromNum(i);// ������Ϊ�ǹ��ϵ�һ����·�����һ����·�ı��
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
				} else {//���2 i���м䣬����·���ж�����·
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
							if(i==0) {//���i����λ��j��ĩλ����judge��ͻᱻ�ж�Ϊ����ʹ�ö�̬��·��
								temp.setWavei(0);
								temp.setWavej(0);
							}else {//i������λ��j��ĩλ
							temp.setWavei(route.getWaveLengthIdList().get(i - 1));
							}
							sur.getItemList().add(temp);
							locate = j;// �������ֵ�Ļ�i��������
						}
						if (route.getWDMLinkList().get(j).isBroken()) {
							temp.setFromNode(route.getNodeList().get(i));
							temp.setToNode(route.getNodeList().get(j));
							temp.setFromNum(i);
							temp.setToNum(j);
							for (int k = i; k < j; k++) {
								temp.WaveLengthIDList.add(route.getWaveLengthIdList().get(k));
							}

							// ��ѯ�ж���ĩ�ڵ��ǲ��ǲ���ת���ڵ�
							for (int l = 0; l < route.getWaveChangedNode().size(); l++) {
								if (route.getWaveChangedNode().contains(temp.getFromNode())) {
									temp.setFromChanged(true);
								}
								if (route.getWaveChangedNode().contains(temp.getToNode())) {
									temp.setToChanged(true);
								}
							}
							if (i == 0 && j != route.getWDMLinkList().size() - 1) {// �����ڵ�һ��
								temp.setWavej(route.getWaveLengthIdList().get(j + 1));
							}
							if (i != 0 && j != route.getWDMLinkList().size() - 1) {// �������м�
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
	 * ������������ж��Ƿ��ܹ���̬��·��
	 */
	public static boolean Judge(Route route) {// ����һ��
		System.out.println("������judge");
		// ����ֵfalse������ʹ�ö�̬��·�ɲ��� true�������ʹ�ö�̬��·�ɲ���
		Route tempRoute = new Route();// ������¼��һ���ܼ���ɹ��Ķ�̬·��
		List<PickedItem> templist = new LinkedList<>();
		List<Integer> WaveAbleToUse = new LinkedList<>();
		templist = FindAllFault(route);
		Survivance sur = new Survivance();
		if(templist.size()==0) {
			return false;//���ֻ��һ����·��ֱ�ӷ���false
		}else {
		for (int j = 0; j < WDMLink.WDMLinkList.size(); j++) {
			if (!WDMLink.WDMLinkList.get(j).isBroken()) {
				WDMLink.WDMLinkList.get(j).setActive(false);
			}
		}
		for (int i = 0; i < templist.size(); i++) {
			// �ж�·���Ƿ����
	/*lty		tempRoute = ResourceAlloc.reRoute(route, templist.get(i).getFromNode(), templist.get(i).getToNode(),
					Survivance.mark);// ��·���ǲ���������Դ
			//tempRoute = ResourceAlloc.allocateResourceByWaveAndTwoNode(route.getBelongsTraffic(), 2, waveNum, Survivance.mark, templist.get(i).getFromNode(), templist.get(i).getToNode())
			if (tempRoute == null)
				return false;// �㲻��·������ʹ�ö�̬��·��
			// �жϲ����Ƿ����ʹ�ö�̬��·�ɲ���
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
				return false;// �����·��û�п��еĲ���������ʹ�ö�̬��·��
			} else {
				if (templist.get(i).getFromNum() == 0) {// ��һ����·��
					if (!templist.get(i).isToChanged()) {
						for (int k = 0; k < WaveAbleToUse.size(); k++) {
							if (!WaveAbleToUse.contains(templist.get(i).getWavej())) {
								return false;
							}
						}
					}
				}
				if (templist.get(i).getToNum() == route.getWDMLinkList().size() - 1) {// ���һ����·����
					if (!templist.get(i).isFromChanged()) {// �����ǲ����������
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
					if (!(templist.get(i).isFromChanged() && templist.get(i).isToChanged())) {// ͷ����ĩ�ڵ㶼���ǲ���ת���ڵ�
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
		return true;// ����ж϶�ͨ���ˣ��ͷ���1������ʹ�ö�̬��·��
	  }
	}
	
	
	/**
	 * func:DynamicRouting input: route 
	 * ��������ۺ�������̬·�ɲ���
	 * @return 
	 */
	public static Route DynamicRouting(Route route) {
		System.out.println("������dynamicRouting");
		boolean flag=true;//��־�������·��û�д����
		PickedItem Item=new PickedItem();//ȡ���������������
		List<Integer> WaveAbleToUse = new LinkedList<>();//���õĲ�����
		Route fromRoute=new Route();
		Route toRoute=new Route();
		Route dynamicRoute=new Route();//�Ѿ�����ɹ��Ķ�̬·��
		Route newRoute=new Route();//ƴ�Ӻõ���·��
		Route tempRoute=new Route();
		tempRoute=route;
		int i=0;
		for (i = 0; i < route.getWDMLinkList().size(); i++) {
			if (!route.getWDMLinkList().get(i).isBroken()) {
				//���1 i��ĩβ
				if (i == route.getWDMLinkList().size() - 1) {
					Item.setFromNode(route.getNodeList().get(i));
					Item.setToNode(route.getNodeList().get(i + 1));
					Item.setFromNum(i);//������Ϊ�ǹ��ϵ�һ����·�����һ����·�ı��
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

				} else {//�ų���i=j=size-1�������Ҳ����i����ĩβ
					//���2,3,4
					for (int j = i; j < route.getWDMLinkList().size(); j++) {
						//���2�����j�����i����ǰ��������ֻ�������м�
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
						//������˵��j�������һ�����������������i�ںͲ��ڿ�ͷ������j���ڽ�β������
						if (route.getWDMLinkList().get(j).isBroken()) {
							Item.setFromNode(route.getNodeList().get(i));
							Item.setToNode(route.getNodeList().get(j));
							Item.setFromNum(i);
							Item.setToNum(j-1);
							for (int k = i; k < j; k++) {
								Item.WaveLengthIDList.add(route.getWaveLengthIdList().get(k));
							}
							// ��ѯ�ж���ĩ�ڵ��ǲ��ǲ���ת���ڵ�
							for (int l = 0; l < route.getWaveChangedNode().size(); l++) {
								if (route.getWaveChangedNode().contains(Item.getFromNode())) {
									Item.setFromChanged(true);
								}
								if (route.getWaveChangedNode().contains(Item.getToNode())) {
									Item.setToChanged(true);
								}
							}
							if (i == 0 && j != route.getWDMLinkList().size() - 1) {// �����ڵ�һ��
								Item.setWavej(route.getWaveLengthIdList().get(j + 1));
							}
							if (i != 0 && j != route.getWDMLinkList().size() - 1) {// �������м�
								Item.setWavei(route.getWaveLengthIdList().get(i - 1));
								Item.setWavej(route.getWaveLengthIdList().get(j + 1));
							}
							break;
						}
					}	
				}	
				flag = false;// ��־��·�г����˹���
				break;
			}
		} // Ѱ�ҵ����д����λ��
		if (!flag) {
//			faultType = LocateFault(route, i);
			// ��·��
			fromRoute = SpliceFromRoute(route, i);
			toRoute = SpliceToRoute(route, Item.getToNum());//�Ƿ񳬽��˻�Ҫ���ж�
/*lty			// ���ж�̬��·�ɵļ���
			dynamicRoute = ResourceAlloc.reRoute(route, Item.getFromNode(), Item.getToNode(), Survivance.mark);// ������·��
			//�ж�ʹ���ĸ�����
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
			
			if (Item.getFromNum() == 0) {// ��һ����·��
				if (Item.isToChanged()) {//ĩ�ڵ��ǲ���ת���ڵ�
					dynamicRoute = ResourceAlloc.allocWaveForRoute(dynamicRoute, WaveAbleToUse.get(0),route.getBelongsTraffic());
				}else {//ĩ�ڵ㲻�ǲ���ת���ڵ�
					for(int k=0;k<WaveAbleToUse.size();k++) {
						if (WaveAbleToUse.contains(Item.getWavej())) {//���õĲ�������ĩ�ڵ�Ĳ�������Ҫ��ĩ�ڵ�Ĳ���
							dynamicRoute=ResourceAlloc.allocWaveForRoute(dynamicRoute, Item.getWavej(),route.getBelongsTraffic());
						}
					}
				}
			}
			if (Item.getToNum()==route.getWDMLinkList().size()-1) {//���һ����·��
				if (Item.isFromChanged()) {//�׽ڵ��ǲ���ת���ڵ�
					dynamicRoute=ResourceAlloc.allocWaveForRoute(dynamicRoute,WaveAbleToUse.get(0),route.getBelongsTraffic());					
				}else {//�׽ڵ㲻�ǲ���ת���ڵ�
						if (WaveAbleToUse.contains(Item.getWavei())) {//���õĲ��������׽ڵ�Ĳ�������Ҫ���׽ڵ�Ĳ���
							dynamicRoute=ResourceAlloc.allocWaveForRoute(dynamicRoute, Item.getWavei(),route.getBelongsTraffic());
						}
				}
			}
			if(Item.getFromNum()!=0 && Item.getToNum()!=route.getWDMLinkList().size()-1) {//�����м�
				//��ĩ�ڵ㶼���ǲ���ת���ڵ㣬״̬FF��ֻ����ĩ����һ�����Ҵ����ڿ��ò������ʱ��ſ�����
				if (Item.isFromChanged()==false&&Item.isToChanged()==false) {
					if (Item.getWavei()==Item.getWavej()&&WaveAbleToUse.contains(Item.getWavei())) {
						dynamicRoute=ResourceAlloc.allocWaveForRoute(dynamicRoute, Item.getWavei(),route.getBelongsTraffic());
					}
				}
				//״̬TT�����ò��������ѡһ������
				if(Item.isFromChanged()==true&&Item.isToChanged()==true) {
					dynamicRoute=ResourceAlloc.allocWaveForRoute(dynamicRoute, WaveAbleToUse.get(0),route.getBelongsTraffic());
				}
				//״̬FT���ж��׽ڵ�Ĳ����Ƿ���ã����þ���
				if(Item.isFromChanged()==false&&Item.isToChanged()==false) {
					if(WaveAbleToUse.contains(Item.getWavei())) {
						dynamicRoute=ResourceAlloc.allocWaveForRoute(dynamicRoute, Item.getWavei(),route.getBelongsTraffic());
					}
				}
				//״̬TF���ж�ĩ�ڵ�Ĳ����Ƿ���ã����þ���
				if(Item.isFromChanged()==true&&Item.isToChanged()==false) {
					if(WaveAbleToUse.contains(Item.getWavej())){
						dynamicRoute=ResourceAlloc.allocWaveForRoute(dynamicRoute, Item.getWavej(),route.getBelongsTraffic());						
					}
				}
			}
			// ƴ·����Ϊ�´�����
			
			newRoute = Joint(fromRoute, toRoute, dynamicRoute);			
			newRoute = DynamicRouting(newRoute);
		}
//		//���л��ݣ���Ϊһ����̬·�ɼ��㲻�ɹ���ǰ���μ���ɹ���Ҫ�������Դ
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
//				//����ϵĽڵ��ǲ���ת���Ľڵ㣬Ҫ���䲻���ӻ���ٲ���ת���ڵ�Ĳ���
//				if(nItem.getnFromWaveLengthID()!=nItem.getnToWaveLengthID()) {
//					
//				}
//			    if((Route.isRouteHaveWave(dynamicRoute, nItem.getnFromWaveLengthID(),route.getBelongsTraffic())//����ͬʱ�ɷ�����һ�������ɷ���
//			    		&&Route.isRouteHaveWave(dynamicRoute, nItem.getnToWaveLengthID(),route.getBelongsTraffic()))){
//			    	if(nItem.getnFromWaveLengthID()!=nItem.getnToWaveLengthID()) {
//			    		
//			    	}
//			    	
//			    }
//			    else {if (Route.isRouteHaveWave(dynamicRoute, nItem.getnFromWaveLengthID(),route.getBelongsTraffic())){
//			    	dynamicRoute=ResourceAlloc.allocWaveForRoute(dynamicRoute, nItem.getnFromWaveLengthID(), route.getBelongsTraffic());
//			    }
//			    if (Route.isRouteHaveWave(dynamicRoute, nItem.getnToWaveLengthID(),route.getBelongsTraffic())){//�ڶ��������ɷ���
//			    	dynamicRoute=ResourceAlloc.allocWaveForRoute(dynamicRoute, nItem.getnToWaveLengthID(), route.getBelongsTraffic());
//			    }}
//			}


//	/**
//	 * func:LocateFault  input:route flag output:fault type
//	 * flag��������·��һ�γ��ֵ�λ��
//	 * type��1 �ڵ����  2 ��·����
//	 */
//	public static int LocateFault(Route route,int flag) {
//		int type = 0;
//		if (flag == route.getWDMLinkList().size() - 1) {
//			type = 2;// ��λ��ĩ�˴���Ϊ��·����,ͬ�����׶��Զ�����Ϊ��·����
//		} else {
//			if (!route.getWDMLinkList().get(flag + 1).isBroken()) {
//				type = 1;// �ڵ����
//			}
//			if (route.getWDMLinkList().get(flag + 1).isBroken()) {
//				type = 2;// ��·����
//			}
//		}
//		return type;
//	}
	/**
	 * �������ƣ� SpliceFromRoute  ����route flag ��� FromRoute ����ĵ��ñ�����Satfault֮��
	 * flag:���ϵ�λ��
     * ���������Ӧ������Route
	 * 
	 */
	public static Route SpliceFromRoute(Route route,int flag) {
		Route fromRoute = new Route();
		//�˿ڵ����в��ǰ�˳��ģ����ﴦ��ɰ�˳��ġ�
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
		//�˿ڴ������
	    // ���flag=0Ҳ���Ƕ�����ͷλ�ã������ѭ������ִ�У�fromRoute��null
	    // �����ǲ����Ļ�Ҫ���Ӻܶ��
		fromRoute.setFrom(route.getFrom());// ��������to����Ϊƴ�ӵ�ʱ��ע���ᱻ���
		fromRoute.setHops(flag);
		fromRoute.setBelongsTraffic(route.getBelongsTraffic());
		fromRoute.setNotConsistent(route.isNotConsistent());
		for (int j = 0; j < flag; j++) {
			fromRoute.getNodeList().add(route.getNodeList().get(j)); // ȡ���Ľڵ㲻�������˵Ľڵ�
			fromRoute.getWDMLinkList().add(route.getWDMLinkList().get(j));
			fromRoute.getWaveLengthIdList().add(route.getWaveLengthIdList().get(j));
//			fromRoute.getPortList().add(portlist.get(j)); // ȡ���Ķ˿ڲ��������˵Ķ˿�
//			fromRoute.getSimPortList().add(simportlist.get(j));
//			// ����flag������
		}
		fromRoute.setUsedWaveList(fromRoute.getUsedWaveList());
		fromRoute.setWaveChangedNode(fromRoute.getWaveLengthIdList());
		return fromRoute;
	}
    /**
	 * �������� SpliceToRoute ����route flag ���Route
	 * flag:���ϵ�λ��
	 */
    public static Route SpliceToRoute(Route route,int flag) {
    	Route toRoute=new Route();
    	//�˿ڵ����в��ǰ�˳��ģ����ﴦ��ɶ�Ӧ�ġ�
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
		//�˿ڴ������
    	//���i=link�ĳ���-1�������ѭ������ִ�У�ToRoute��null
    	toRoute.setTo(route.getTo());
    	toRoute.setHops(flag);//�������
    	toRoute.setBelongsTraffic(route.getBelongsTraffic());
    	toRoute.setNotConsistent(route.isNotConsistent());
    	
    	for(int j=flag+1;j<route.getWDMLinkList().size();j++) {
    		toRoute.getNodeList().add(route.getNodeList().get(j+1));//ȡ���Ľڵ㲻�������˵Ľڵ�
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
     * �������ƣ�Joint �������ã�ƴ��·�� �������FromRoute ToRoute ComputeRoute
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
	 * �������ƣ�reCompute �������ã�Ϊ����ҵ��������·���ָ�ҵ�� ���������traffic��· ���ز�����
	 */
	public static void reCompute(List<Traffic> trafficList,CommonNode node) {
		// assert FiberLink.allFiberLinkList.size() == 35 : "��·�ı���";// 10.27
		for (int i = 0; i < trafficList.size(); ++i) {
			Traffic tra = trafficList.get(i);
			int type = mark;
			switch (tra.getFaultType()) {// ҵ���������1 ���� 2 ���� 3��������
			case 1: // 1Ϊ������Ӱ��
				switch (tra.getProtectLevel()) {// ҵ�񱣻��ȼ�
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
					if(tra.getPreRoute()!=null&&tra.getPreRoute().getWDMLinkList()!=null //�ж�ר��Ԥ���Ƿ����,ͬʱҪ�ж�ҵ�����ĩ�ڵ��Ƿ����
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
							// ��̬��·���������osnr�����ã�������Ļ��Ͷ�����·�ɽ�������
							if(tra.getResumeRoute() == null) {
								Route route = null;
								// ������·����״̬
								// ���ر���·���ڵ���Ϊ������
								ResourceAlloc.setMustAvoidUnactive(tra);

								// ����ҵ�����������ò��������������Ӧ��SRLGΪ�����
								ResourceAlloc.setRelatedTrafficUnactive(tra, DataSave.separate);
								
								//���ù�����·������
								for (int j = 0; j < WDMLink.WDMLinkList.size(); j++) {
									if (!WDMLink.WDMLinkList.get(j).isBroken()) {
										WDMLink.WDMLinkList.get(j).setActive(false);
									}
								}
								// ���ñ���·�ɲ����������㶯̬·��
								if (tra.getProtectRoute() != null) {
									ResourceAlloc.setRouteLinkUnactive(tra.getProtectRoute());
								}


								// ����Դ�������·��Ϊ������ ���룺�Ƿ��Ǽ���Ԥ��·��
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
									if (CommonAlloc.allocateWaveLength3(route)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
										// CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "Ԥ��·�ɼ���Դ����ɹ���\r\n");
										tra.setDynamicRoute(route);// ���·�ɲ�Ϊ��������Ԥ����·��
										tra.setStatus(TrafficStatus.��̬��·���ѷ���);
										tra.setResumeRoute(route);
									}
								}

							}

						}else {//����ҪOSNR�ж�
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
					if (tra.getProtectRoute() != null && tra.getProtectRoute().getFiberLinkList() != null//����ʹ��1+1�ı���·����Ϊ�ָ�·��
							&& tra.getProtectRoute().getFiberLinkList().size() != 0
							&& !(tra.getFromNode().equals(node))
							&& !(tra.getToNode().equals(node))){
						tra.setResumeRoute(tra.getProtectRoute());
					}else {
						if(tra.getPreRoute()!=null&&tra.getPreRoute().getFiberLinkList()!=null //�ж�ר��Ԥ���Ƿ���ڣ�ͬʱ�ж�ҵ�����ĩ�ڵ��Ƿ����
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
			case 2: // 2Ϊ������Ӱ��
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
			case 3: // 3Ϊ������������Ӱ��
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
					if(tra.getPreRoute()!=null&&tra.getPreRoute().getFiberLinkList()!=null //�ж�ר��Ԥ���Ƿ����
					&& tra.getPreRoute().getFiberLinkList().size()!=0
					&& !(tra.getFromNode().equals(node))
					&& !(tra.getToNode().equals(node))) {
						tra.setResumeRoute(tra.getPreRoute());
					}else {
						break;
					}
					break;
				case PROTECTandRESTORATION:
					if(tra.getPreRoute()!=null&&tra.getPreRoute().getFiberLinkList()!=null //�ж�ר��Ԥ���Ƿ����
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
			// assert FiberLink.allFiberLinkList.size() == 35 : "��·�ı���";// 10.27

	}

	/**
	 * ���ܣ�����ҵ��Ļָ�·��״̬Ϊb
	 * 
	 * @param tra
	 * @param b
	 */
	private static void setLinkStatus(Traffic tra, boolean b) {
		Route resumeRoute = tra.getResumeRoute();
		if (resumeRoute == null)
			return;// �Ҳ���·ʱ�� 2016.11.1
		for (WDMLink bl : resumeRoute.getWDMLinkList()) {
			bl.setActive(b);
		}
	}

	/**
	 * ���ܣ�ͨ����Ӱ���linkList�õ���Ӱ���ҵ�񣬲�����ҵ��Ĺ�������
	 * 
	 * @param linkList
	 * 
	 */
	public List<Traffic> getAffectedTraffic(List<FiberLink> affectedLinkList) {

		// assert FiberLink.allFiberLinkList.size() == 35 : "��·�ı���";// 10.27
        affectedTrafficList.clear();
		for (int i = 0; i < affectedLinkList.size(); ++i) // ������Ӱ�����·
		{
			FiberLink bLink = affectedLinkList.get(i);
			for (WDMLink wLink : bLink.getCarriedWDMLinkList()) {
				if (wLink.getCarriedTrafficList() == null)
					return null; // CC 2016.10.20 Ϊû�г���ҵ�����·��������ֹ�쳣
				List<Traffic> temptraList = wLink.getCarriedTrafficList(); // ��Ӱ�����·�ϵ�ҵ����
				ceshisur+=temptraList.size();
				if (temptraList != null) {
					for (int j = 0; j < temptraList.size(); ++j) {// ������ǰ��·�ϵ�ҵ��
						Traffic tra = temptraList.get(j);// ��ҵ���·��������һ���ܵ�Ӱ��
						System.out.println("ҵ���������"+tra.getName());
						if (!affectedTrafficList.contains(tra)) {// �ж��Ƿ���ֹ��ظ�����Ӱ��ҵ��
							if (tra.getWorkRoute().getWDMLinkList().contains(wLink)) {// �жϹ���·���Ƿ������ǰ��Ӱ����·
								tra.setFaultType(1); // ������Ӱ��

								for (FiberLink curbl : affectedLinkList) {
									if (tra.getProtectRoute() == null)
										break;
									if (tra.getProtectRoute().getFiberLinkList().contains(curbl)) {
										tra.setFaultType(3); // �����ͱ�������Ӱ��
										break;
									}
								}
							} // ��������·�ɽ���
							else if (tra.getProtectRoute() != null) {
								if (tra.getProtectRoute().getWDMLinkList().contains(wLink)) {
									tra.setFaultType(2); // ������Ӱ�졢

									for (FiberLink curbl : affectedLinkList) {
										if (tra.getWorkRoute().getFiberLinkList().contains(curbl)) {
											tra.setFaultType(3); // �����ͱ�������Ӱ��
											break;
										}
									}
								}
							} // ��������·�ɽ���
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
	 * �������ƣ�getNodeItem ����Route flag ���pickeditem @author Carson
	 * flag:���ϵ�λ��
	 */
//	//ȡ�����ϵĵ�ǰ�����ϵĲ��������˵Ľڵ㣬flag��
//	public static PickedItem getNodeItem(Route route,int flag) {
//		PickedItem pick=new PickedItem();
//			pick.setnFromNode(route.getWDMLinkList().get(flag).getFromNode());
//			pick.setnToNode(route.getWDMLinkList().get(flag+1).getToNode());
//			pick.setnFromWaveLengthID(flag);
//			pick.setnToWaveLengthID(flag+1);
//		return pick;
//	}
//	/**
//	 * �������ƣ�getLinkItem  ����Route flag ���pickedItem
//	 * flag�����ϵ�λ��
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
//     * �������ƣ�getEffectedItem ����FiberLinkList  @author Carson 
//     */
//    public  void getEffectItem(List<FiberLink>FiberLinkList) {
//    	for(FiberLink fl: FiberLinkList) {//������Ӱ���fiberlink
//    		List<WDMLink>WdmLinkList=fl.getCarriedWDMLinkList();//�����Ӱ���wdmlink
//    		for(WDMLink wl: WdmLinkList) {//������Ӱ���wdmlink
//    			if(wl.getCarriedTrafficList()==null) {//��ֹû�г���ҵ�����·����
//    				continue;
//    			}
//    			List<Traffic>traList= getAffectedTraffic(FiberLinkList);//�����Ӱ���ҵ��
//    			affectedTrafficList.clear();//�����Ӱ���ҵ��
//    			if(traList!=null) {//�ж���Ӱ��ҵ���Ƿ�Ϊ��
//    				for(Traffic tra:traList) {//������Ӱ���ҵ��
//    					PickedItem pick=new PickedItem();
//    					if (!affectedTrafficList.contains(tra)){//�ж���Ӱ���ҵ���������ظ���
//    						switch (tra.getFaultType()) {
//    						case 1://����·����Ӱ��
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
//    									pick.setwFromWaveLengthID(0);//��Ϊ�������Ǵ�1-80������Ϊ0�ǲ�����
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
//    						case 2://����·����Ӱ��
//    							if(tra.getProtectRoute().getWDMLinkList().contains(wl)) {//���ü���һ���жϣ���Ϊǰ���Ѿ��ж��Ǳ���·����Ӱ����
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
//        								}//��������͹���ͬʱ��Ӱ����
//        								//�����͹���ƾʲôͬʱ��Ӱ�죿1+1����Ҫ���������ô����·�ɸ�����û�б���·�ɰ��� ��ϵ�����»���ֹ����ͱ���ͬ�³�����������
//        								//���Ǽ�����ԵĻ���ҵ���Ƕ����wdm�Ƕ��������������ô��֤��¼�����Ķ������Է��룿
//        								//���ܷ��������£�����ѡ·��·����ôȥ��Ӧҵ��  
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
	 * �������ƣ�getNodeEffectLink �������ã�ͨ���ڵ���Ѱ�����ڵ����· ���������node�ڵ� ���ز�����True
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
