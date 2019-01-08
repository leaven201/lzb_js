package dialog;
/**
 * ����������ʾ�ڵ㡢��·��ҵ��ĸ�������
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
    private final Object[] columnNames = {"������","����ֵ"};
    private final Object[] fiberlinkProperty = {"ID","����","	�׽ڵ�","ĩ�ڵ�","��·����(km)","���������","������","״̬"};
    private final Object[] ShortWavelinkProperty = {"ID","����","	�׽ڵ�","ĩ�ڵ�","��·����(km)","���������","��·����(Gbps)","״̬"};
    private final Object[] SatellitelinkProperty = {"ID","����","	�׽ڵ�","ĩ�ڵ�","��·����(km)","���������","��·����(Gbps)","״̬"};
	private final Object[] nodeProperty = {"ID","����","���� ","γ��","�ڵ�����	","״̬"};
	private final Object[] trafficProperty = {"ID","����","�׽ڵ� ","ĩ�ڵ�","ҵ������","�����ȼ�"};
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
			   };//���ñ����ݲ��ɱ༭	;
		displayModel.setRowCount(11);
		display.setModel(displayModel);
		display.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		TableColumnModel tcm = display.getColumnModel();  
		tcm.getColumn(1).setPreferredWidth(119);
		display.getTableHeader().setPreferredSize(new   Dimension(1,   24));
		display.getTableHeader().setReorderingAllowed(false);//�в��ɶ�
		for(int i = 0;i<display.getRowCount();i++){
			display.setRowHeight(20);
		}
	    
	    JScrollPane pane1 = new JScrollPane (display);
		MyUtil.makeFace(display);//����JTable ��ɫ
		Pane.add(pane1);	
		box.getSelectionModel().addDataBoxSelectionListener(//ѡ�ж������ CC 12.13
			    new DataBoxSelectionListener() {
			     public void selectionChanged(DataBoxSelectionEvent e) {
			      if ((e.getBoxSelectionModel().getAllSelectedElement() != null)
			        && (e.getBoxSelectionModel().getAllSelectedElement().size() == 1)){
			    	  initData((Element) e.getElements().iterator().next());	
			    	  }                        
			     }
			    });
	
	  box.addElementPropertyChangeListener(new PropertyChangeListener(){//��Ӷ������ 12.13
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
									case OLA:displayModel.setValueAt("��ͨ����", 2, 1);	
										break;
									case ROADM:displayModel.setValueAt("��", 2, 1);	
										break;
									case OTN:displayModel.setValueAt("��", 2, 1);	
										break;
//									case ����_�̲�_����:displayModel.setValueAt("����-�̲�-����", 2, 1);	
//										break;
//									case ����:
//										break;
//									case �̲�:
//										break;
//									case �̲�_����:
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
		 if(element.getClass().equals(UINode.class)){//"ID","����","���� ","γ��","�ڵ�����	","��������","״̬","���"
			 if(traffic != null){
				 if(traffic.getWorkRoute()!=null){
					 recoveryRoutecol();//CC
				 }
			 }
			 node = CommonNode.getNode(Integer.parseInt(String.valueOf(element.getID())) );
			 DecimalFormat df=new DecimalFormat("#.000000");
			 Object[] nodeValue ={node.getId(),node.getName(),df.format(node.getLongitude()),df.format(node.getLatitude()),
					node.isActive()?"����":"δ����"} ;
			 switch(node.getNodeType()){//12.13 CC 
				case OLA:displayModel.setValueAt("��ͨ����", 2, 1);	
					break;
				case ROADM:displayModel.setValueAt("��", 2, 1);	
					break;
				case OTN:displayModel.setValueAt("��", 2, 1);	
					break;
//				case ����_�̲�_����:displayModel.setValueAt("����-�̲�-����", 2, 1);	
//					break;
//				case ����:
//					break;
//				case �̲�:
//					break;
//				case �̲�_����:
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
		 else if(element.getClass().equals(UIFiberLink.class)){//���ù��� ��ʼ����������
			 if(traffic != null){
				 if(traffic.getWorkRoute()!=null){
					 recoveryRoutecol();
				 }
			 }
			 UIFiberLink uilink =(UIFiberLink)element;
			 link = uilink.getFiberLink();
			 String type1=link.getLinkLayer()+"";
			  if(type1.equals("Fiber")){
				  linktype="����";
			  }
			 Object[] linkValue = {link.getId(),link.getName(),link.getFromNode().getName(),link.getToNode().getName(),
					 link.getLength(),linktype,link.getSize()," ",link.isActive()?"����":"δ����"};
			 for(int i = 0;i < 7;i++){			 
				 displayModel.setValueAt(linkValue[i], i, 1);		 		
				 displayModel.setValueAt(fiberlinkProperty [i], i, 0);		
				
			 }
			 StringBuffer buffer=new StringBuffer();	//CC 626 	
//			 for(int i = 0 ;i < link.getM_lRelate().size();i++){
//				 buffer.append(link.getM_lRelate().get(i).getM_sName());
//				 if(i!=link.getM_lRelate().size()-1){
		//			 buffer.append("���");
//					 }
//			 }		 
		     displayModel.setValueAt(buffer, 7, 1);
			 
			 }
		 else if(element.getClass().equals(UIOTNLink.class)){//��ʼ���̲���·����
			 if(traffic != null){
				 if(traffic.getWorkRoute()!=null){
					 recoveryRoutecol();// CC 626
				 }
			 }
//			 "ID","����","	�׽ڵ�","ĩ�ڵ�","��·����","��·����","��·��Դ","��·����","״̬",
			 UIOTNLink uilink =(UIOTNLink)element;
			 OTNLink link = uilink.getEleLink();
			 String type2=link.getLinkLayer()+"";
			  if(type2.equals("ShortWave")){
				  linktype="�̲�";
			  }
			 
			 Object[] linkValue = {link.getId(),link.getName(),link.getFromNode().getName(),link.getToNode().getName(),
					 link.getLength(),linktype,link.getSize(),link.getRate()," ",link.isActive()?"����":"δ����"};
			
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
		//			 buffer.append("���2");
//					 }
//			 }		 
		     displayModel.setValueAt(buffer, 8, 1);
			 
			 }
		 else if(element.getClass().equals(UIWDMLink.class)){//��ʼ��������·����
			 if(traffic != null){
				 if(traffic.getWorkRoute()!=null){
					 recoveryRoutecol();
				 }
			 }
//			 "ID","����","	�׽ڵ�","ĩ�ڵ�","��·����","���������","ʱ϶��","��·����","SRLG","	���","״̬","���ز�"
			 UIWDMLink uilink =(UIWDMLink)element;
			 WDMLink link = uilink.getOptLink();
			 String type3=link.getLinkLayer()+"";
			  if(type3.equals("Satellite")){
				  linktype="����";
			  }
			 Object[] linkValue = {link.getId(),link.getName(),link.getFromNode().getName(),link.getToNode().getName(),
					 link.getLength(),linktype,link.getSize(),link.getRate()," ",link.isActive()?"����":"δ����"};
			 for(int i = 0;i < 7;i++){			 
				 displayModel.setValueAt(linkValue[i], i, 1);		 		
				 displayModel.setValueAt(SatellitelinkProperty [i], i, 0);		
				
			 }
			 StringBuffer buffer=new StringBuffer();		
//			 for(int i = 0 ;i < link.getM_lRelate().size();i++){
//				 buffer.append(link.getM_lRelate().get(i).getM_sName());
//				 if(i!=link.getM_lRelate().size()-1){
			//		 buffer.append("���3");
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
			 
			 //ѡ��ҵ��    
			 //"����","�׽ڵ� ","ĩ�ڵ�","ҵ������","�����ȼ�","ҵ����","״̬","���"
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
						setRoutecol(traffic,Layer.Fiber);//����·����ɫ CC 626 
				 }
			}
		 }
	 }
	 private void setRoutecol(Traffic tra, Layer layer){//����·����ɫ
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
	 public static void recoveryRoutecol(){//������·��ɫ����

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
     private Traffic getTrafficID(int id){//ҵ������Ψһ
    	 for(int i = 0;i<Traffic.trafficList.size();i++){
    		 if(Traffic.trafficList.get(i).getId()== id)
    			 return Traffic.trafficList.get(i);
    	 }
    		  return null;
     }
     
     private Traffic getTrafficName(String name){//�����Ƽ���
    	 for(int i = 0;i<Traffic.trafficList.size();i++){
    		 if(Traffic.trafficList.get(i).getName()== name)
    			 return Traffic.trafficList.get(i);
    	 }
    	 
    	 return null;
     }
     
	}


class MyUtil {

	/**
	   * �˷�����һ��̬�����ǽ����յ���JTAble������ż�зֱ����óɱ�ɫ������ɫ
	   * @param table JTable
	   */
	public static void makeFace(JTable table) {

	   try {
	    DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
	     public Component getTableCellRendererComponent(JTable table,
	       Object value, boolean isSelected, boolean hasFocus,
	       int row, int column) {
	    	 if (row % 2 == 0)
	  	       setBackground(Color.white); // ���������е�ɫ
	  	      else if (row % 2 == 1)
	  	       setBackground(new Color(223, 240, 250)); // ����ż���е�ɫ
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
		       setBackground(Color.white); // ���������е�ɫ
		      else if (row % 2 == 1)
		       setBackground(color); // ����ż���е�ɫ
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
