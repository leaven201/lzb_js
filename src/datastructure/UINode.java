package datastructure;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import data.*;
import javax.swing.ImageIcon;

import twaver.GeoCoordinate;
import twaver.Node;
import twaver.gis.TWaverGisConst;
import design.NetDesign_zs;
import enums.NodeType;

public class UINode extends Node{//ע��̳е���Taver��Node
//	��Ա����
	public static double a=11.70;				//x=a*longitude+b y=c*latitude+d
	public static double b=-667.41;
	public static double c=-15.09;
	public static double d=858.43;
	private CommonNode commonnode;   //��̨�ڵ�
	private static Color UINodeColor=Color.yellow;
	private String icon = "/resource/node2.png";
	private double Longitude ;
	private double Latitude;
	private GeoCoordinate Coordinate;
//�ڵ���
	
	public static List<UINode> s_lUINodeList=new LinkedList<UINode>();
	
//���췽��
	public UINode(CommonNode commonNode){
		
		super(""+commonNode.getId());
		setName(commonNode.getName());
		commonnode = commonNode;
			this.setVisible(true);
//			if(commonNode.isSupportSatellite() && commonNode.isSupportShortWave()) 
//			icon = "/resource/�������Ƕ̲�.png";
//			else if(commonNode.isSupportSatellite()) icon = "/resource/��������.png";
//			else if(commonNode.isSupportShortWave()) icon = "/resource/���˶̲�.png";
			if(commonNode.getNodeType()==NodeType.OLA){
				icon = "/resource/zhongji1.png";
			}else{
				icon = "/resource/����1.png";
			}
			
			setImage(icon);
//			this.setVisible(true); CC:�ڵ�����ͼƬ
//			icon = "/resource/node-2.png";
//			setImage(icon);
//	
//			this.setVisible(true);
//			icon = "/resource/node2.png";
//			 setImage(icon);
//
//			 icon = "/resource/node2.png";
//			 setImage(icon);
		putClientProperty(TWaverGisConst.GEOCOORDINATE,new GeoCoordinate(commonNode.getLongitude(), commonNode.getLatitude()) );

		//		System.out.println("    "+this.getGeoCoordinate());
	
	}
//	�෽��
	
	public void setGeoCoordinate(double longitude,double latitude){
		Longitude = longitude;
		Latitude = latitude;
		putClientProperty(TWaverGisConst.GEOCOORDINATE, new GeoCoordinate(Longitude, Latitude));
	}
	public GeoCoordinate getCoordinate() {
		return Coordinate;
	}

	public void setCoordinate(GeoCoordinate coordinate) {
		Coordinate = coordinate;
	}

	public static void addNode(UINode node){
		s_lUINodeList.add(node);
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public static void clearAll(){
		s_lUINodeList.clear();
	}
	public static void setColor(Color color){
		ListIterator<UINode> it = s_lUINodeList.listIterator(); 
		while(it.hasNext())
		{
			it.next().putRenderColor(color);		
		}
		UINodeColor=color;
	}
	public  static Color getColor(){
           return UINodeColor;
	}
	public static UINode getThisNode(int id){
		ListIterator<UINode> it = s_lUINodeList.listIterator(); 
		while(it.hasNext())
		{
			if(it.next().getCommonNode().getId()==id)
				break;
		}
		return it.previous();
	}
	//��Ա����
	public static UINode getUINode(String name){
		for(int i = 0;i < s_lUINodeList.size();i++){
			if(s_lUINodeList.get(i).getName().equals(name)){
				return s_lUINodeList.get(i);
			}
		}
		return null;
	}
	public CommonNode getCommonNode(){
		return commonnode;
	}
	public void setCommonNode(CommonNode node){
		commonnode =node;
	}

}
