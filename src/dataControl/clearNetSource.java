package dataControl;


public class clearNetSource {

	public static void clearNetSource(){
		TrafficData.clearTraffic();    //清空业务资源
		LinkData.clearLink();          //清空链路资源
		NodeData.clearNode();          //清空节点资源
	}
}
