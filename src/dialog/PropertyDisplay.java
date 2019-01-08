package dialog;
/**
 * 此类用作显示节点、链路、业务的各项属性
 * 
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import twaver.DataBoxSelectionEvent;
import twaver.DataBoxSelectionListener;
import twaver.Element;
import twaver.GeoCoordinate;
import twaver.Link;
import twaver.TDataBox;
import twaver.TWaverConst;
import twaver.gis.TWaverGisConst;
import data.*;
import datastructure.UIFiberLink;
import datastructure.UINode;
import datastructure.*;
import enums.Layer;
import enums.*;


public class PropertyDisplay{
	private static JTable display = new JTable();
	private static DefaultTableModel displayModel = (DefaultTableModel) display.getModel();
    private final Object[] columnNames = {"属性名","参数值"};
    private final Object[] fiberlinkProperty = {"ID","名称","	首节点","末节点","链路长度(km)","所属传输层","光纤数","状态"};
    private final Object[] ShortWavelinkProperty = {"ID","名称","	首节点","末节点","链路长度(km)","所属传输层","链路速率(Gbps)","状态"};
    private final Object[] SatellitelinkProperty = {"ID","名称","	首节点","末节点","链路长度(km)","所属传输层","链路速率(Gbps)","状态"};
	private final Object[] nodeProperty = {"ID","名称","经度 ","纬度","节点类型	","状态"};
	private final Object[] trafficProperty = {"ID","名称","首节点 ","末节点","业务速率","保护等级"};
	 private String linktype = null;
	
	
	private TDataBox box1;
	private CommonNode node; 
	private BasicLink link;
	private Traffic traffic;
	
	private Element eme = null;
	private List<BasicLink> linklist = new LinkedList<BasicLink>();	
	public PropertyDisplay(TDataBox box,JPanel Pane){
		displayModel = new DefaultTableModel(null,columnNames){
			   public boolean isCellEditable(int row, int column) { 
					 return false;
					 }
			   };//设置表内容不可编辑	;
		displayModel.setRowCount(11);
		display.setModel(displayModel);
		display.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnModel tcm = display.getColumnModel();  
		tcm.getColumn(1).setPreferredWidth(119);
		display.getTableHeader().setPreferredSize(new   Dimension(1,   24));
		display.getTableHeader().setReorderingAllowed(false);//列不可动
		for(int i = 0;i<display.getRowCount();i++){
			display.setRowHeight(20);
		}
	    
	    JScrollPane pane1 = new JScrollPane (display);
		MyUtil.makeFace(display);//设置JTable 颜色
		Pane.add(pane1);	
		box.getSelectionModel().addDataBoxSelectionListener(//选中对象监听 CC 12.13
			    new DataBoxSelectionListener() {
			     public void selectionChanged(DataBoxSelectionEvent e) {
			      if ((e.getBoxSelectionModel().getAllSelectedElement() != null)
			        && (e.getBoxSelectionModel().getAllSelectedElement().size() == 1)){
			    	  initData((Element) e.getElements().iterator().next());	
			    	  }                        
			     }
			    });
	
	  box.addElementPropertyChangeListener(new PropertyChangeListener(){//添加对象监听 12.13
		 public void propertyChange(PropertyChangeEvent e){
		 Element element=(Element)e.getSource();
		 if(e.getSource().getClass().equals(UINode.class)){
			 if(e.getOldValue() != null){
				 node = CommonNode.getNode(Integer.parseInt(String.valueOf(element.getID())));  
				 if(node != null){
					 DecimalFormat df=new DecimalFormat("#.000000");
					 UINode uinode = (UINode) element;		 
						 GeoCoordinate coordinate = (GeoCoordinate)element.getClientProperty(TWaverGisConst.GEOCOORDINATE);
						 node.setLongitude(coordinate.getLongitude());
						 node.setLatitude(coordinate.getLatitude()) ;
						 if(displayModel.getValueAt(2, 0)!=null){
							 if(!displayModel.getValueAt(2, 0).equals("")){
								 switch(node.getNodeType()){//12.13 CC 
									case OLA:displayModel.setValueAt("普通光纤", 2, 1);	
										break;
									case ROADM:displayModel.setValueAt("光", 2, 1);	
										break;
									case OTN:displayModel.setValueAt("电", 2, 1);	
										break;
//									case 光纤_短波_卫星:displayModel.setValueAt("光纤-短波-卫星", 2, 1);	
//										break;
//									case 卫星:
//										break;
//									case 短波:
//										break;
//									case 短波_卫星:
//										break;
//									default:
//										break;
									 }		 
//								 displayModel.setValueAt(df.format(node.getLongitude()), 2, 1);	
//								 displayModel.setValueAt(df.format(node.getLatitude()), 3, 1);
							 }
							
						 }
				 }
								
			 }
			 }
		 }
		 });
	 }
	
     public static void cleartable(){//CC
    	 
//    	 for(int i = 0;i < 2;i++){
//   		  for(int j = 0;j < 11;j++)
//   			  displayModel.setValueAt(null, j, i);
//   	  }
    	 while(displayModel.getRowCount()>0){
    		 displayModel.removeRow(displayModel.getRowCount()-1);
    	 }
     }
	 private void initData(Element element){
		
		 cleartable();
		 if(element.getClass().equals(UINode.class)){//"ID","名称","经度 ","纬度","节点类型	","所属子网","状态","年份"
			 if(traffic != null){
				 if(traffic.getWorkRoute()!=null){
					 recoveryRoutecol();//CC
				 }
			 }
			 node = CommonNode.getNode(Integer.parseInt(String.valueOf(element.getID())) );
			 DecimalFormat df=new DecimalFormat("#.000000");
			 Object[] nodeValue ={node.getId(),node.getName(),df.format(node.getLongitude()),df.format(node.getLatitude()),
					node.isActive()?"激活":"未激活"} ;
			 switch(node.getNodeType()){//12.13 CC 
				case OLA:displayModel.setValueAt("普通光纤", 2, 1);	
					break;
				case ROADM:displayModel.setValueAt("光", 2, 1);	
					break;
				case OTN:displayModel.setValueAt("电", 2, 1);	
					break;
//				case 光纤_短波_卫星:displayModel.setValueAt("光纤-短波-卫星", 2, 1);	
//					break;
//				case 卫星:
//					break;
//				case 短波:
//					break;
//				case 短波_卫星:
//					break;
				default:
					break;
				 }		 
				 displayModel.setValueAt(nodeProperty[4], 2, 0);
			 for(int i = 0;i < 2;i++){
				 if(i!=3){
					 displayModel.setValueAt(nodeValue[i], i, 1);		 
					 displayModel.setValueAt(nodeProperty[i], i, 0);
				 }
			 }
		 }			
		 else if(element.getClass().equals(UIFiberLink.class)){//设置光纤 初始化光纤数据
			 if(traffic != null){
				 if(traffic.getWorkRoute()!=null){
					 recoveryRoutecol();
				 }
			 }
			 UIFiberLink uilink =(UIFiberLink)element;
			 link = uilink.getFiberLink();
			 String type1=link.getLinkLayer()+"";
			  if(type1.equals("Fiber")){
				  linktype="光纤";
			  }
			 Object[] linkValue = {link.getId(),link.getName(),link.getFromNode().getName(),link.getToNode().getName(),
					 link.getLength(),linktype,link.getSize()," ",link.isActive()?"激活":"未激活"};
			 for(int i = 0;i < 7;i++){			 
				 displayModel.setValueAt(linkValue[i], i, 1);		 		
				 displayModel.setValueAt(fiberlinkProperty [i], i, 0);		
				
			 }
			 StringBuffer buffer=new StringBuffer();	//CC 626 	
//			 for(int i = 0 ;i < link.getM_lRelate().size();i++){
//				 buffer.append(link.getM_lRelate().get(i).getM_sName());
//				 if(i!=link.getM_lRelate().size()-1){
		//			 buffer.append("你猜");
//					 }
//			 }		 
		     displayModel.setValueAt(buffer, 7, 1);
			 
			 }
		 else if(element.getClass().equals(UIOTNLink.class)){//初始化短波链路数据
			 if(traffic != null){
				 if(traffic.getWorkRoute()!=null){
					 recoveryRoutecol();// CC 626
				 }
			 }
//			 "ID","名称","	首节点","末节点","链路长度","链路类型","链路资源","链路速率","状态",
			 UIOTNLink uilink =(UIOTNLink)element;
			 OTNLink link = uilink.getEleLink();
			 String type2=link.getLinkLayer()+"";
			  if(type2.equals("ShortWave")){
				  linktype="短波";
			  }
			 
			 Object[] linkValue = {link.getId(),link.getName(),link.getFromNode().getName(),link.getToNode().getName(),
					 link.getLength(),linktype,link.getSize(),link.getRate()," ",link.isActive()?"激活":"未激活"};
			
			 for(int i = 0;i < 7;i++){			 
				 displayModel.setValueAt(linkValue[i], i, 1);		 		
				 displayModel.setValueAt(ShortWavelinkProperty [i], i, 0);		
				
			 }
			  String	rate_sw= link.getRate()+"";
			  String string2 = rate_sw.substring(1,rate_sw.length());
				 
					 displayModel.setValueAt(string2+"G", 6, 1);
				
			 StringBuffer buffer=new StringBuffer();		//CC 626
//			 for(int i = 0 ;i < link.getM_lRelate().size();i++){
//				 buffer.append(link.getM_lRelate().get(i).getM_sName());
//				 if(i!=link.getM_lRelate().size()-1){
		//			 buffer.append("你猜2");
//					 }
//			 }		 
		     displayModel.setValueAt(buffer, 8, 1);
			 
			 }
		 else if(element.getClass().equals(UIWDMLink.class)){//初始化卫星链路数据
			 if(traffic != null){
				 if(traffic.getWorkRoute()!=null){
					 recoveryRoutecol();
				 }
			 }
//			 "ID","名称","	首节点","末节点","链路长度","所属传输层","时隙数","链路速率","SRLG","	年份","状态","承载层"
			 UIWDMLink uilink =(UIWDMLink)element;
			 WDMLink link = uilink.getOptLink();
			 String type3=link.getLinkLayer()+"";
			  if(type3.equals("Satellite")){
				  linktype="卫星";
			  }
			 Object[] linkValue = {link.getId(),link.getName(),link.getFromNode().getName(),link.getToNode().getName(),
					 link.getLength(),linktype,link.getSize(),link.getRate()," ",link.isActive()?"激活":"未激活"};
			 for(int i = 0;i < 7;i++){			 
				 displayModel.setValueAt(linkValue[i], i, 1);		 		
				 displayModel.setValueAt(SatellitelinkProperty [i], i, 0);		
				
			 }
			 StringBuffer buffer=new StringBuffer();		
//			 for(int i = 0 ;i < link.getM_lRelate().size();i++){
//				 buffer.append(link.getM_lRelate().get(i).getM_sName());
//				 if(i!=link.getM_lRelate().size()-1){
			//		 buffer.append("你猜3");
//					 }
//			 }		 
		     displayModel.setValueAt(buffer, 8, 1);
		     String rate = link.getRate()+"";
		     String string = rate.substring(1,rate.length());
			 
	    	 displayModel.setValueAt(string+"G", 6, 1);
			 
	    	 

//		     if(link.getRate() > 100){
//		    	 displayModel.setValueAt(link.getRate()+"M", 7, 1);
//		     }
//		     else {
//		    	 displayModel.setValueAt(link.getRate()+"G", 7, 1);
//			}
		     
			 }
		 else{
			 
			 //选中业务    
			 //"名称","首节点 ","末节点","业务速率","保护等级","业务组","状态","年份"
			 if(traffic != null){
				 if(traffic.getWorkRoute()!=null){
					 recoveryRoutecol();
				 }
			 }
			 
			 try{
				 traffic =getTrafficID(Integer.parseInt(String.valueOf(element.getID())));
			 }catch(NumberFormatException ex){
				 
			 }
			
//			if(traffic!=null){
//				Object[] trafficValue = {traffic.getId(),traffic.getName(),traffic.getFromNode().getName(),traffic.getToNode().getName(),traffic.getRate(),
//						 traffic.getProtectLevel(),"",traffic.getStatus()};//,"changdu"+"km"
//				 for(int i = 0;i < 5;i++){			 
//					 displayModel.setValueAt(trafficValue[i], i, 1);		 		
//					 displayModel.setValueAt(trafficProperty [i], i, 0);		
//					
//				 }
//				 if(traffic.getRate().equals(TrafficRate.G100)){
//					 displayModel.setValueAt("100G", 4, 1);	
//				 }
//				 if(traffic.getRate().equals(TrafficRate.G40)){
//					 displayModel.setValueAt("40G", 4, 1);	
//				 }
//				 if(traffic.getRate().equals(TrafficRate.G10)){
//					 displayModel.setValueAt("10G", 4, 1);	
//				 }
//				 if(traffic.getRate().equals(TrafficRate.G2Dot5)){
//					 displayModel.setValueAt("2.5G", 4, 1);	
//				 }
//				 if(traffic.getRate().equals(TrafficRate.GE)){
//					 displayModel.setValueAt("GE", 4, 1);	
//				 }
//				 if(traffic.getRate().equals(TrafficRate.M622)){
//					 displayModel.setValueAt("622M", 4, 1);	
//				 }
//				 if(traffic.getRate().equals(TrafficRate.M155)){
//					 displayModel.setValueAt("155M", 4, 1);	
//				 }
//				 if(traffic.getRate().equals(TrafficRate.FE)){
//					 displayModel.setValueAt("FE", 4, 1);	
//				 }
//				 if(traffic.getRate().equals(TrafficRate.M2)){
//					 displayModel.setValueAt("2M", 4, 1);	
//				 }

//				 StringBuffer buffer=new StringBuffer();
//				 if(traffic.getM_cGroup()()!=null){
//					 for(int i = 0 ;i < traffic.getM_lRelate().size();i++){
//						 buffer.append(traffic.getM_lRelate().get(i).getM_sName());
//						 if(i!=traffic.getM_lRelate().size()-1){
//							 buffer.append(";");
//							 }
//					 }		 
//				     displayModel.setValueAt(buffer, 5, 1);
//				 }
//			}
			
			if(traffic != null){
				if(traffic.getWorkRoute()!=null){
						setRoutecol(traffic,Layer.Fiber);//设置路由颜色 CC 626 
				 }
			}
		 }
	 }
	 private void setRoutecol(Traffic tra, Layer layer){//设置路由颜色
		 if(layer.equals(Layer.Fiber)){
			 List<UIFiberLink> UIFiberLinkList=new LinkedList<UIFiberLink>(); 
			 List<FiberLink> FiberLinkList=new LinkedList<FiberLink>(); 
			 FiberLinkList = tra.getWorkRoute().getFiberLinkList();
			 for(int i = 0;i < FiberLinkList.size();i++){
				 UIFiberLinkList.add(UIFiberLink.getUIFiberLink(FiberLinkList.get(i).getName()));
				
			 }
			 for(int i = 0;i < UIFiberLinkList.size();i++){
				 UIFiberLinkList.get(i).putLinkColor(Color.yellow);
				 UIFiberLinkList.get(i).putLinkFlowing(true);
				 UIFiberLinkList.get(i).putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
				 UIFiberLinkList.get(i).putLink3D(true);
			 }
		 }
//		 if(layer.equals(Layer.OTN)){
//			 List<UIOTNLink> UIShortWaveLinkList=new LinkedList<UIOTNLink>(); 
//			 List<OTNLink> eleLinkList=new LinkedList<OTNLink>(); 
//			 eleLinkList = tra.getWorkRoute().getOTNLinkList();
//			 for(int i = 0;i < eleLinkList.size();i++){
//				 UIShortWaveLinkList.add(UIOTNLink.getUIEleLink(eleLinkList.get(i).getName()));
//			 }
//			 for(int i = 0;i < UIShortWaveLinkList.size();i++){
//				 UIShortWaveLinkList.get(i).putLinkColor(Color.yellow);
//				 UIShortWaveLinkList.get(i).putLinkFlowing(true);
//				 UIShortWaveLinkList.get(i).putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
//				 UIShortWaveLinkList.get(i).putLink3D(true);
//			 }
//		 }
		 if(layer.equals(Layer.WDM)){
			 List<UIWDMLink> UIOptLinkList=new LinkedList<UIWDMLink>(); 
			 List<WDMLink> optLinkList=new LinkedList<WDMLink>(); 
			optLinkList = tra.getWorkRoute().getWDMLinkList();
			 for(int i = 0;i < optLinkList.size();i++){
				 UIOptLinkList.add(UIWDMLink.getUIOptLink(optLinkList.get(i).getName()));
			 }
			 for(int i = 0;i < UIOptLinkList.size();i++){
				 UIOptLinkList.get(i).putLinkColor(Color.yellow);
				 UIOptLinkList.get(i).putLinkFlowing(true);
				 UIOptLinkList.get(i).putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
				 UIOptLinkList.get(i).putLink3D(true);
			 }
		 }
	 }
	 public static void recoveryRoutecol(){//设置链路颜色？？

		   for(int i = 0; i < UIFiberLink.s_lUIFiberLinkList.size(); i++){
			    UIFiberLink thelink = UIFiberLink.s_lUIFiberLinkList.get(i);
	 			FiberLink thefiber = thelink.getFiberLink();

	 				thelink.setthisColor(Color.BLUE);
	 				thelink.putLinkFlowing(false);
	 				thelink.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
	 				thelink.putLink3D(false);
		   }
		   for(int i = 0; i < UIOTNLink.s_lUIEleLinkList.size(); i++){
		       UIOTNLink thelink = UIOTNLink.s_lUIEleLinkList.get(i);
		       OTNLink theShortWave = thelink.getEleLink();
	 				thelink.setthisColor(Color.ORANGE);
	 				thelink.putLinkFlowing(false);
	 				thelink.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
	 				thelink.putLink3D(false);
		   }
		
		   for(int i = 0; i < UIWDMLink.s_lUIWDMLinkList.size(); i++){
		       UIWDMLink thelink = UIWDMLink.s_lUIWDMLinkList.get(i);
		       WDMLink theSatellite = thelink.getOptLink();
	 				thelink.setthisColor(Color.GREEN);
	 				thelink.putLinkFlowing(false);
	 				thelink.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
	 				thelink.putLink3D(false);
	 			}
	
	 }
     private Traffic getTrafficID(int id){//业务名称唯一
    	 for(int i = 0;i<Traffic.trafficList.size();i++){
    		 if(Traffic.trafficList.get(i).getId()== id)
    			 return Traffic.trafficList.get(i);
    	 }
    		  return null;
     }
     
     private Traffic getTrafficName(String name){//按名称检索
    	 for(int i = 0;i<Traffic.trafficList.size();i++){
    		 if(Traffic.trafficList.get(i).getName()== name)
    			 return Traffic.trafficList.get(i);
    	 }
    	 
    	 return null;
     }
     
	}


class MyUtil {

	/**
	   * 此方法是一静态方法是将接收到的JTAble按照奇偶行分别设置成表色和银蓝色
	   * @param table JTable
	   */
	public static void makeFace(JTable table) {

	   try {
	    DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
	     public Component getTableCellRendererComponent(JTable table,
	       Object value, boolean isSelected, boolean hasFocus,
	       int row, int column) {
	    	 if (row % 2 == 0)
	  	       setBackground(Color.white); // 设置奇数行底色
	  	      else if (row % 2 == 1)
	  	       setBackground(new Color(223, 240, 250)); // 设置偶数行底色
	      return super.getTableCellRendererComponent(table, value,
	        isSelected, hasFocus, row, column);
	     }
	    };
	    for (int i = 0; i < table.getColumnCount(); i++) {
	     table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
	    }
	   } catch (Exception ex) {
	    ex.printStackTrace();
	   }

	}
	public static void makeFace(JTable table, final Color color) {

		   try {
		    DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
		     public Component getTableCellRendererComponent(JTable table,
		       Object value, boolean isSelected, boolean hasFocus,
		       int row, int column) {
		      if (row % 2 == 0)
		       setBackground(Color.white); // 设置奇数行底色
		      else if (row % 2 == 1)
		       setBackground(color); // 设置偶数行底色
		      return super.getTableCellRendererComponent(table, value,
		        isSelected, hasFocus, row, column);
		     }
		    };
		    for (int i = 0; i < table.getColumnCount(); i++) {
		     table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
		    }
		   } catch (Exception ex) {
		    ex.printStackTrace();
		   }

		}
	}
