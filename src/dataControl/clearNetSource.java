package dataControl;


public class clearNetSource {

	public static void clearNetSource(){
		TrafficData.clearTraffic();    //���ҵ����Դ
		LinkData.clearLink();          //�����·��Դ
		NodeData.clearNode();          //��սڵ���Դ
	}
}
