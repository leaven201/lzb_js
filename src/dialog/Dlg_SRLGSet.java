package dialog;

import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ListIterator;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import data.FiberLink;
import data.LinkRGroup;
//import data.SDHLink;
//import data.SDHRing;
import data.WDMLink;
import dataControl.SRG;
//import dataControl.SRG;
import enums.Layer;

public class Dlg_SRLGSet extends JDialog{                        //代码比较乱，有时间再做修改
	private final Object[] columnNames = {"ID","链路名称","起始节点 ","终止节点"};
	private final String[] ringNames={"SRLG ID","SRLG名称","所含链路"};//列名最好用final修饰
	private DefaultTableModel linkModel;
	private DefaultTableModel srlgModel;
	private DefaultTableModel teamModel;
	private JTable linkTable;
	private JTable srlgTable;
    private JTable teamTable;
    private JComboBox linkLayer;
    private JComboBox srlgLayer;
    private static JLabel title1;
    private JButton srg = new JButton("自动映射");
    public static int status = 2;//state = 0 为编辑，State  = 1 为新建
    public static int editId;//记录被编辑的环Id
    public static int time=0;
//    public Dlg_SRLGSet(Frame owner){MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
//    	super(owner,"共享风险链路组",true);MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
    public Dlg_SRLGSet(){
//    	super(owner,"共享风险链路组",true);
    	this.setSize(900,650);
    	
    	
    	title1 = new JLabel("链路组成员链路");
		title1.setFont(new   java.awt.Font("Dialog",  Font.BOLD,   15)); 
		
    	
    	linkLayer = new JComboBox();
    	srlgLayer = new JComboBox();
    	
    	String[] s1 = {"Fiber层","WDM层"};
    	String[] s2 = {"全部","Fiber层","WDM层"};
    	for(int i = 0;i <s1.length;i++){
    		linkLayer.addItem(s1[i]);
    	}
    	for(int i = 0;i <s2.length;i++){
    		srlgLayer.addItem(s2[i]);
    	}
    	
		teamModel = new DefaultTableModel(null,columnNames);//环链路
		linkModel = new DefaultTableModel(null,columnNames);
		srlgModel = new DefaultTableModel(null,ringNames);
		
		teamModel.setRowCount(30);
		linkModel.setRowCount(30);
		srlgModel.setRowCount(100);
		

    	
//    	Object linkData[][] = new Object[FiberLink.fiberLinkList.size()][4];
//		for(int n = 0;n <FiberLink.fiberLinkList.size();n++){
//			linkData[n][0] = FiberLink.fiberLinkList.get(n).getId();
//			linkData[n][1] = FiberLink.fiberLinkList.get(n).getName();
//			linkData[n][2] = FiberLink.fiberLinkList.get(n).getFromNode().getM_sName();
//			linkData[n][3] = FiberLink.fiberLinkList.get(n).getToNode().getM_sName();
//		}
		linkModel = new DefaultTableModel(null,columnNames);

		if(linkModel.getRowCount() < 30){
			Vector data = new Vector();
			int num = linkModel.getRowCount();
			for(int x = 0;x < 30- num; x++)
				linkModel.addRow(data);
           
		}
		refreshlinkTable();
		
		Object srlgData[][] = new Object[LinkRGroup.SRLGroupList.size()][3];
		for(int n = 0;n <LinkRGroup.SRLGroupList.size();n++){
			srlgData[n][0] = LinkRGroup.SRLGroupList.get(n).getID();
			srlgData[n][1] = LinkRGroup.SRLGroupList.get(n).getName();
			StringBuffer buffer=new StringBuffer();		
			switch(LinkRGroup.SRLGroupList.get(n).getBelongLayer()){
			case Fiber:
				for(int i = 0; i < LinkRGroup.SRLGroupList.get(n).getSRLGFiberLinkList().size();i++){
					 buffer.append(LinkRGroup.SRLGroupList.get(n).getSRLGFiberLinkList().get(i).getName());
					 if(i != LinkRGroup.SRLGroupList.get(n).getSRLGFiberLinkList().size()-1)
						 buffer.append(" ; ");
				}
				srlgData[n][2] = buffer;
				break;
			case WDM:
				//System.out.println(" LinkRGroup.s_lRGroup.get(n).getM_lWDMLinkList().size()"+ LinkRGroup.s_lRGroup.get(n).getM_lWDMLinkList().size());
			    for(int i = 0; i < LinkRGroup.SRLGroupList.get(n).getSRLGWDMLinkList().size();i++){
				     buffer.append(LinkRGroup.SRLGroupList.get(n).getSRLGWDMLinkList().get(i).getName());
				     if(i != LinkRGroup.SRLGroupList.get(n).getSRLGWDMLinkList().size()-1)
					      buffer.append(" ; ");
			    }
			    srlgData[n][2] = buffer;
			    break;
//			case OTN:
//				//System.out.println(" LinkRGroup.s_lRGroup.get(n).getM_lWDMLinkList().size()"+ LinkRGroup.s_lRGroup.get(n).getM_lWDMLinkList().size());
//			    for(int i = 0; i < LinkRGroup.s_lRGroupList.get(n).getM_lOTNLinkList().size();i++){
//				     buffer.append(LinkRGroup.s_lRGroupList.get(n).getM_lOTNLinkList().get(i).getM_sName());
//				     if(i != LinkRGroup.s_lRGroupList.get(n).getM_lOTNLinkList().size()-1)
//					      buffer.append(" ; ");
//			    }
//			    srlgData[n][2] = buffer;
//			    break;
//			case SDH:
//				
//			     for(int i = 0; i < LinkRGroup.s_lRGroupList.get(n).getM_lSDHLinkList().size();i++){
//				     buffer.append(LinkRGroup.s_lRGroupList.get(n).getM_lSDHLinkList().get(i).getM_sName());		
//				     if(i != LinkRGroup.s_lRGroupList.get(n).getM_lSDHLinkList().size()-1)
//					     buffer.append(" ; ");				 
//			   }
//			   srlgData[n][2] = buffer;
//			break;
//			case ASON:
//				//System.out.println(" LinkRGroup.s_lRGroup.get(n).getM_lSDHLinkList().size()"+ LinkRGroup.s_lRGroup.get(n).getM_lWDMLinkList().size());
//			     for(int i = 0; i < LinkRGroup.s_lRGroupList.get(n).getM_lASONLinkList().size();i++){
//				     buffer.append(LinkRGroup.s_lRGroupList.get(n).getM_lASONLinkList().get(i).getM_sName());
//				     if(i != LinkRGroup.s_lRGroupList.get(n).getM_lASONLinkList().size()-1)
//					     buffer.append(" ; ");
//			   }
//			   srlgData[n][2] = buffer;			
//			   break;
			}
		}
		
		srlgModel = new DefaultTableModel(srlgData,ringNames);
		
		
		if(srlgModel.getRowCount() < 100){
			Vector data = new Vector();
			int num = srlgModel.getRowCount();
			for(int x = 0;x < 100- num; x++)
				srlgModel.addRow(data);          
		}
		
		srlgLayer.setSelectedIndex(0);
		firstrefresh();
		
		linkLayer.addItemListener(new ItemListener(){
		      public void itemStateChanged(ItemEvent e){
		    	  refreshlinkTable();
		      }
		    });
		srlgLayer.addItemListener(new ItemListener(){
		      public void itemStateChanged(ItemEvent e){
		    	  refreshsrlgTable();
		    	  if(srlgLayer.getSelectedIndex()==0){
		    		  refreshsrlgTable();
		    		  refreshsrlgTable();
		    		  refreshsrlgTable();
		    	  }
		      }
		    });
 		
	
				
		linkTable = new JTable(linkModel){
			   public boolean isCellEditable(int row, int column) { 
				    return false;
				    }
			   };//设置表内容不可编辑
			   
	    teamTable = new JTable(teamModel) {
			   public boolean isCellEditable(int row, int column) { 
					 return false;
					 }
			   };//设置表内容不可编辑			   
		srlgTable = new JTable(srlgModel){
			   public boolean isCellEditable(int row, int column) { 
					  return false;
					  }
				};//设置表内容不可编辑			
	    linkTable.getTableHeader().setReorderingAllowed(false);//列不可动
	    teamTable.getTableHeader().setReorderingAllowed(false);//列不可动
	    srlgTable.getTableHeader().setReorderingAllowed(false);//列不可动	
	    

	   for(int x=0;x<30;x++) {
	    System.out.println(LinkRGroup.getLinkRGroup(x));}
	 
	    
	    JScrollPane pane1 = new JScrollPane (linkTable);
	    JScrollPane pane2 = new JScrollPane (teamTable);
	    JScrollPane pane3 = new JScrollPane (srlgTable);
//	    pane1.getViewport().add(linkTable, null); 
//	    pane2.getViewport().add(teamTable, null); 
//	    pane3.getViewport().add(srlgTable, null); 
	    
	    linkTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  //水平滚动条
	    teamTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    srlgTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    
	    final JButton in = new JButton("=>");
	    final JButton out = new JButton("<=");
	    final JButton confirm = new JButton("确定");
	    in.setEnabled(false);
	    out.setEnabled(false);
	    confirm.setEnabled(false);
	    

	    final JButton set = new JButton("新建");
	    final JButton editor = new JButton("编辑");
	    final JButton delete = new JButton("删除");
	    editor.setEnabled(false);
	    delete.setEnabled(false);
	    
	    
	    if(linkTable.getValueAt(0, 0)==null)
	         set.setEnabled(false);
	    final JButton close = new JButton("关闭");
	    close.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		setVisible(false);
	    		dispose();
	    	}
	    });
	    in.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){	
//	    		点"=>"键的操作  完成
	    		Object[] data = new Object[4];
	    		for(int n = 0;n < linkTable.getSelectedRows().length; n++){
    				if(linkTable.getValueAt(linkTable.getSelectedRows()[n], 0)!=null){
    				      data[0] = linkTable.getValueAt(linkTable.getSelectedRows()[n], 0);
    				      data[1] = linkTable.getValueAt(linkTable.getSelectedRows()[n], 1);
    				      data[2] = linkTable.getValueAt(linkTable.getSelectedRows()[n], 2);
    				      data[3] = linkTable.getValueAt(linkTable.getSelectedRows()[n], 3);
    				      if(teamModel.getValueAt(29,0)==null)
    		    				 teamModel.removeRow(29);
    		    			teamModel.insertRow(getRow(teamModel), data);
    				}
    				
    			}
	    		
	    		Vector data1 = new Vector();
	    		for(int j=0;j< columnNames.length;j++){
	    			for(int n = 0;n < linkTable.getSelectedRows().length; n++){
	    				linkModel.removeRow(linkTable.getSelectedRows()[n]);
	    			}
//	    			保持有空白表
	    			if(linkModel.getRowCount() < 30){
	    				int num = linkModel.getRowCount();
	    				for(int x = 0;x < 30- num; x++)
	    					linkModel.addRow(data1);
	    			} 
	    		}
	    		
	    	}
	    });
	    
	    out.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){	
//	    		点"<="键的操作   完成
	    
	    		Object[] data = new Object[4];
	    		for(int n = 0;n < teamTable.getSelectedRows().length; n++){
    				if(teamTable.getValueAt(teamTable.getSelectedRows()[n], 0)!=null){
    				      data[0] = teamTable.getValueAt(teamTable.getSelectedRows()[n], 0);
    				      data[1] = teamTable.getValueAt(teamTable.getSelectedRows()[n], 1);
    				      data[2] = teamTable.getValueAt(teamTable.getSelectedRows()[n], 2);
    				      data[3] = teamTable.getValueAt(teamTable.getSelectedRows()[n], 3);
    				      if(linkModel.getValueAt(29,0)==null)
    		    				 linkModel.removeRow(29);
    		    			linkModel.insertRow(getRow(linkModel), data);
    				}
    				
    			}
	    		
	    		Vector data1 = new Vector();
	    		for(int j=0;j< columnNames.length;j++){
	    			for(int n = 0;n < teamTable.getSelectedRows().length; n++){
	    				teamModel.removeRow(teamTable.getSelectedRows()[n]);
	    			}
//	    			保持有空白表
	    			if(teamModel.getRowCount() < 30){
	    				int num = teamModel.getRowCount();
	    				for(int x = 0;x < 30- num; x++)
	    					teamModel.addRow(data1);
	    			} 
	    		}
	    	}
	    });
	    set.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){	
	    		status = 1; 
	    		new NewSRLG(Dlg_SRLGSet.this);//完成？
	    		if(NewSRLG.isReady()){
	    		    title1.setText(NewSRLG.getsrlgName()+"链路组成员链路");
		    		in.setEnabled(true);
		    		out.setEnabled(true);
		    		confirm.setEnabled(true);
		       		linkLayer.setEnabled(false);
		    		srlgLayer.setEnabled(false);
		    	    editor.setEnabled(false);
		     	    delete.setEnabled(false); 
		     	    set.setEnabled(false);
	    		}	    		
	    	}
	    });
	    
	    editor.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){	
//	    		点编辑键的操作 ,用id获得SRLG组？？？？？？？？？
	    		linkLayer.setEnabled(false);
	    		srlgLayer.setEnabled(false);
	    	    editor.setEnabled(false);
	     	    delete.setEnabled(false); 
	     	    set.setEnabled(false);
                
	    		in.setEnabled(true);
	    		out.setEnabled(true);
	    		confirm.setEnabled(true);
	    		
	    		status = 0;
	    		editId = Integer.parseInt(String.valueOf(srlgModel.getValueAt(srlgTable.getSelectedRow(), 0)));
		        title1.setText(LinkRGroup.getLinkRGroup(editId).getName()+"链路组成员链路");
	    		//SRG srg=new SRG();
	    		LinkRGroup.getLinkRGroup(editId);
	    		Object data[] = new Object[4];
	    		if(LinkRGroup.getLinkRGroup(editId).getBelongLayer() == Layer.Fiber){
	    			linkLayer.setSelectedIndex(0);
	    			removeTable(linkModel);
	    			for(int i = 0;i < LinkRGroup.getLinkRGroup(editId).getSRLGFiberLinkList().size();i++){
		    			data[0] = LinkRGroup.getLinkRGroup(editId).getSRLGFiberLinkList().get(i).getId();
		    			data[1] = LinkRGroup.getLinkRGroup(editId).getSRLGFiberLinkList().get(i).getName();
		    			data[2] = LinkRGroup.getLinkRGroup(editId).getSRLGFiberLinkList().get(i).getFromNode().getM_sName();
		    			data[3] = LinkRGroup.getLinkRGroup(editId).getSRLGFiberLinkList().get(i).getToNode().getM_sName();
		    			if(teamModel.getValueAt(29,0)==null)
		    				 teamModel.removeRow(29);
		    			teamModel.insertRow(0, data);
		    			
		    		}	
	    			int index = 0;
		    		for(int i = 0 ;i < FiberLink.fiberLinkList.size();i++){
		    			int j;
		    			for(j = 0;j<LinkRGroup.getLinkRGroup(editId).getSRLGFiberLinkList().size();j++){
		    				if(FiberLink.fiberLinkList.get(i).getId() == LinkRGroup.getLinkRGroup(editId).getSRLGFiberLinkList().get(j).getId()){
		    					
		   	    			    break;
		    				}
		    			}
		    			if(j==LinkRGroup.getLinkRGroup(editId).getSRLGFiberLinkList().size()){
		    				data[0] = FiberLink.fiberLinkList.get(i).getId();
		        			data[1] = FiberLink.fiberLinkList.get(i).getName();
		        			data[2] = FiberLink.fiberLinkList.get(i).getFromNode().getM_sName();
		        			data[3] = FiberLink.fiberLinkList.get(i).getToNode().getM_sName();
		        			if(linkModel.getValueAt(29,0)==null)
		        				linkModel.removeRow(29);
		        			linkModel.insertRow(index++, data);
		    			}
		    		}
		    		refreshlinkTable();
	    		}
	    	else if(LinkRGroup.getLinkRGroup(editId).getBelongLayer() == Layer.WDM){
    			linkLayer.setSelectedIndex(1);
    			removeTable(linkModel);
			for(int i = 0;i < LinkRGroup.getLinkRGroup(editId).getSRLGFiberLinkList().size();i++){
    			data[0] = LinkRGroup.getLinkRGroup(editId).getSRLGFiberLinkList().get(i).getId();
    			data[1] = LinkRGroup.getLinkRGroup(editId).getSRLGFiberLinkList().get(i).getName();
    			data[2] = LinkRGroup.getLinkRGroup(editId).getSRLGFiberLinkList().get(i).getFromNode().getM_sName();
    			data[3] = LinkRGroup.getLinkRGroup(editId).getSRLGFiberLinkList().get(i).getToNode().getM_sName();
    			if(teamModel.getValueAt(29,0)==null)
    				 teamModel.removeRow(29);
    			teamModel.insertRow(0, data);
    			
    		}	
			int index = 0;
    		for(int i = 0 ;i < WDMLink.WDMLinkList.size();i++){
    			int j;
    			for(j = 0;j<LinkRGroup.getLinkRGroup(editId).getSRLGFiberLinkList().size();j++){
    				if(WDMLink.WDMLinkList.get(i).getId() == LinkRGroup.getLinkRGroup(editId).getSRLGFiberLinkList().get(j).getId()){
    					
   	    			    break;
    				}
    			}
    			if(j==LinkRGroup.getLinkRGroup(editId).getSRLGFiberLinkList().size()){
    				data[0] = WDMLink.WDMLinkList.get(i).getId();
        			data[1] = WDMLink.WDMLinkList.get(i).getName();
        			data[2] = WDMLink.WDMLinkList.get(i).getFromNode().getM_sName();
        			data[3] = WDMLink.WDMLinkList.get(i).getToNode().getM_sName();
        			if(linkModel.getValueAt(29,0)==null)
        				linkModel.removeRow(29);
        			linkModel.insertRow(index++, data);
    			}
    		}
		}
//	    	else if(LinkRGroup.getLinkRGroup(editId).getM_eLayer() == Layer.OTN){
//    			linkLayer.setSelectedIndex(2);
//    			removeTable(linkModel);
//				for(int i = 0;i < LinkRGroup.getLinkRGroup(editId).getM_lOTNLinkList().size();i++){
//	    			data[0] = LinkRGroup.getLinkRGroup(editId).getM_lOTNLinkList().get(i).getM_nID();
//	    			data[1] = LinkRGroup.getLinkRGroup(editId).getM_lOTNLinkList().get(i).getM_sName();
//	    			data[2] = LinkRGroup.getLinkRGroup(editId).getM_lOTNLinkList().get(i).getM_cFromNode().getM_sName();
//	    			data[3] = LinkRGroup.getLinkRGroup(editId).getM_lOTNLinkList().get(i).getM_cToNode().getM_sName();
//	    			if(teamModel.getValueAt(29,0)==null)
//	    				 teamModel.removeRow(29);
//	    			teamModel.insertRow(0, data);
//	    			
//	    		}
//				int index = 0;
//	    		for(int i = 0 ;i < WDMLink.s_lOTNLinkList.size();i++){
//	    			int j;
//	    			for(j = 0;j<LinkRGroup.getLinkRGroup(editId).getM_lOTNLinkList().size();j++){
//	    				if(WDMLink.s_lOTNLinkList.get(i).getM_nID() == LinkRGroup.getLinkRGroup(editId).getM_lOTNLinkList().get(j).getM_nID()){
//	    					
//	   	    			    break;
//	    				}
//	    			}
//	    			if(j==LinkRGroup.getLinkRGroup(editId).getM_lOTNLinkList().size()){
//	    				data[0] = WDMLink.s_lOTNLinkList.get(i).getM_nID();
//	        			data[1] = WDMLink.s_lOTNLinkList.get(i).getM_sName();
//	        			data[2] = WDMLink.s_lOTNLinkList.get(i).getM_cFromNode().getM_sName();
//	        			data[3] = WDMLink.s_lOTNLinkList.get(i).getM_cToNode().getM_sName();
//	        			if(linkModel.getValueAt(29,0)==null)
//	        				linkModel.removeRow(29);
//	        			linkModel.insertRow(index++, data);
//	    			}
//	    		}
//			}
//	    	else if(LinkRGroup.getLinkRGroup(editId).getM_eLayer() == Layer.ASON){
//    			linkLayer.setSelectedIndex(3);
//    			removeTable(linkModel);
//				for(int i = 0;i < LinkRGroup.getLinkRGroup(editId).getM_lASONLinkList().size();i++){
//	    			data[0] = LinkRGroup.getLinkRGroup(editId).getM_lASONLinkList().get(i).getM_nID();
//	    			data[1] = LinkRGroup.getLinkRGroup(editId).getM_lASONLinkList().get(i).getM_sName();
//	    			data[2] = LinkRGroup.getLinkRGroup(editId).getM_lASONLinkList().get(i).getM_cFromNode().getM_sName();
//	    			data[3] = LinkRGroup.getLinkRGroup(editId).getM_lASONLinkList().get(i).getM_cToNode().getM_sName();
//	    			if(teamModel.getValueAt(29,0)==null)
//	    				 teamModel.removeRow(29);
//	    			teamModel.insertRow(0, data);
//	    			
//	    		}
//				int index = 0;
//	    		for(int i = 0 ;i < SDHLink.s_lASONLinkList.size();i++){
//	    			int j;
//	    			for(j = 0;j<LinkRGroup.getLinkRGroup(editId).getM_lASONLinkList().size();j++){
//	    				if(SDHLink.s_lASONLinkList.get(i).getM_nID() == LinkRGroup.getLinkRGroup(editId).getM_lASONLinkList().get(j).getM_nID()){
//	    					
//	   	    			    break;
//	    				}
//	    			}
//	    			if(j==LinkRGroup.getLinkRGroup(editId).getM_lASONLinkList().size()){
//	    				data[0] = SDHLink.s_lASONLinkList.get(i).getM_nID();
//	        			data[1] = SDHLink.s_lASONLinkList.get(i).getM_sName();
//	        			data[2] = SDHLink.s_lASONLinkList.get(i).getM_cFromNode().getM_sName();
//	        			data[3] = SDHLink.s_lASONLinkList.get(i).getM_cToNode().getM_sName();
//	        			if(linkModel.getValueAt(29,0)==null)
//	        				linkModel.removeRow(29);
//	        			linkModel.insertRow(index++, data);
//	    			}
//	    		}
//			}
//	    	else if(LinkRGroup.getLinkRGroup(editId).getM_eLayer() == Layer.SDH){
//    			linkLayer.setSelectedIndex(4);
//    			removeTable(linkModel);
//				for(int i = 0;i < LinkRGroup.getLinkRGroup(editId).getM_lSDHLinkList().size();i++){
//	    			data[0] = LinkRGroup.getLinkRGroup(editId).getM_lSDHLinkList().get(i).getM_nID();
//	    			data[1] = LinkRGroup.getLinkRGroup(editId).getM_lSDHLinkList().get(i).getM_sName();
//	    			data[2] = LinkRGroup.getLinkRGroup(editId).getM_lSDHLinkList().get(i).getM_cFromNode().getM_sName();
//	    			data[3] = LinkRGroup.getLinkRGroup(editId).getM_lSDHLinkList().get(i).getM_cToNode().getM_sName();
//	    			if(teamModel.getValueAt(29,0)==null)
//	    				 teamModel.removeRow(29);
//	    			teamModel.insertRow(0, data);
//	    			
//	    		}
//				int index = 0;
//	    		for(int i = 0 ;i < SDHLink.s_lSDHLinkList.size();i++){
//	    			int j;
//	    			for(j = 0;j<LinkRGroup.getLinkRGroup(editId).getM_lSDHLinkList().size();j++){
//	    				if(SDHLink.s_lSDHLinkList.get(i).getM_nID() == LinkRGroup.getLinkRGroup(editId).getM_lSDHLinkList().get(j).getM_nID()){
//	    					
//	   	    			    break;
//	    				}
//	    			}
//	    			if(j==LinkRGroup.getLinkRGroup(editId).getM_lSDHLinkList().size()){
//	    				data[0] = SDHLink.s_lSDHLinkList.get(i).getM_nID();
//	        			data[1] = SDHLink.s_lSDHLinkList.get(i).getM_sName();
//	        			data[2] = SDHLink.s_lSDHLinkList.get(i).getM_cFromNode().getM_sName();
//	        			data[3] = SDHLink.s_lSDHLinkList.get(i).getM_cToNode().getM_sName();
//	        			if(linkModel.getValueAt(29,0)==null)
//	        				linkModel.removeRow(29);
//	        			linkModel.insertRow(index++, data);
//	    			}
//	    		}
//			}
//	    		
	    		  
//	    		srlgModel.removeRow(srlgTable.getSelectedRow());
//	    		Vector data2 = new Vector();
//	    		if(srlgModel.getRowCount() < 30){
//    				int num = srlgModel.getRowCount();
//    				for(int x = 0;x < 30- num; x++)
//    					srlgModel.addRow(data2);
//    			} 
	    		
	    		
	    		
	    		

	    	}
	    });
	    
	    confirm.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){	
	    //		点确定键的操作,表和链都有操作  完成？
	    		if(teamModel.getValueAt(0, 0)!=null&&teamModel.getValueAt(1, 0)!=null){
	    			
	    			if(status == 1){//新建
	    	    		if(linkLayer.getSelectedIndex() == 0){
	    	    			srlgLayer.setSelectedIndex(1);
	    	    			SRG srg=new SRG();
		 	    			srg.newSRG(NewSRLG.getsrlgId(), "Fiber",NewSRLG.getsrlgName());
		    	    		for(int i = 0;i < getRow(teamModel);i++ ){
		    	    			srg.getTemplrg().addlFiberLinkList(FiberLink.getFiberLink((Integer.parseInt(String.valueOf(teamModel.getValueAt(i, 0))))));
		    	    		}
//		    	    		JOptionPane.showMessageDialog(null,"如有需要请手动添加相应SDH层SRLG组！");
	    	    		}
	    	    		else if(linkLayer.getSelectedIndex() == 1){
	    	    			srlgLayer.setSelectedIndex(2);
	    	    			SRG srg=new SRG();
		 	    			srg.newSRG(NewSRLG.getsrlgId(), "WDM",NewSRLG.getsrlgName());
	    	    			for(int i = 0;i < getRow(teamModel);i++ ){
		    	    			srg.getTemplrg().addlWDMLinkList(WDMLink.getWDMLink((String.valueOf(teamModel.getValueAt(i, 0)))));
		    	    		}
//	    	    			JOptionPane.showMessageDialog(null,"如有需要请手动添加相应SDH层SRLG组！");
	    	    		}
//	    	    		else if(linkLayer.getSelectedIndex()==2){
//	    	    			srlgLayer.setSelectedIndex(3);
//	    	    			SRG srg=new SRG();
//		 	    			srg.newSRG(NewSRLG.getsrlgId(), "OTN",NewSRLG.getsrlgName());
//	    	    			for(int i = 0;i < getRow(teamModel);i++ ){
//		    	    			srg.getTemplrg().addlWDMLinkList(WDMLink.getOTNLink((Integer.parseInt(String.valueOf(teamModel.getValueAt(i, 0))))));
//		    	    		}
////	    	    			JOptionPane.showMessageDialog(null,"如有需要请手动添加相应SDH层SRLG组！");
//	    	    		}
//	    	    		else if(linkLayer.getSelectedIndex()==3){
//	    	    			srlgLayer.setSelectedIndex(4);
//	    	    			SRG srg=new SRG();
//		 	    			srg.newSRG(NewSRLG.getsrlgId(), "ASON",NewSRLG.getsrlgName());
//	    	    			for(int i = 0;i < getRow(teamModel);i++ ){
//		    	    			srg.getTemplrg().addlSDHLinkList(SDHLink.getASONLink((Integer.parseInt(String.valueOf(teamModel.getValueAt(i, 0))))));
//		    	    		}
////	    	    			JOptionPane.showMessageDialog(null,"如有需要请手动添加相应SDH层SRLG组！");
//	    	    		}
//	    	    		else if(linkLayer.getSelectedIndex()==4){
//	    	    			srlgLayer.setSelectedIndex(5);
//	    	    			SRG srg=new SRG();
//		 	    			srg.newSRG(NewSRLG.getsrlgId(), "SDH",NewSRLG.getsrlgName());
//	    	    			for(int i = 0;i < getRow(teamModel);i++ ){
//		    	    			srg.getTemplrg().addlSDHLinkList(SDHLink.getSDHLink((Integer.parseInt(String.valueOf(teamModel.getValueAt(i, 0))))));
//
//		    	    		}
////	    	    			JOptionPane.showMessageDialog(null,"如有需要请手动添加相应SDH层SRLG组！");
//	    	    		}
	    	    		
	    	    		
	    			}
	    			else if(status == 0){//编辑"Fiber层","WDM层","OTN层","ASON层","SDH层"
	    	    		if(LinkRGroup.getLinkRGroup(editId).getBelongLayer()==Layer.Fiber){
	    	    			srlgLayer.setSelectedIndex(1);	
	    	    			for(int m = 0;m < LinkRGroup.getLinkRGroup(editId).getSRLGFiberLinkList().size();){
	    	    				LinkRGroup.getLinkRGroup(editId).dellFiberLinkList(LinkRGroup.getLinkRGroup(editId).getSRLGFiberLinkList().get(m));
	    	    				if(LinkRGroup.getLinkRGroup(editId).getSRLGFiberLinkList().size()==0)
	    	    						break;    	    				
	    	    			}
	    	    			//删除不了最后一个???????????新建不认层,在新建情况下，上下不对应怎么刷新
//	    	    			JOptionPane.showMessageDialog(null, LinkRGroup.getLinkRGroup(editId).getM_lFiberLinkList().size());
		    	    		for(int i = 0;i < getRow(teamModel);i++ ){
		    	    			LinkRGroup.getLinkRGroup(editId).addlFiberLinkList(FiberLink.getFiberLink((Integer.parseInt(String.valueOf(teamModel.getValueAt(i, 0))))));
		    	    		}
	    	    		}
	    	    		else if(LinkRGroup.getLinkRGroup(editId).getBelongLayer()==Layer.WDM){
	    	    			srlgLayer.setSelectedIndex(2);
	    	    			for(int m = 0;m < LinkRGroup.getLinkRGroup(editId).getSRLGWDMLinkList().size();){
	    	    				LinkRGroup.getLinkRGroup(editId).delWDMLinkList(LinkRGroup.getLinkRGroup(editId).getSRLGWDMLinkList().get(m));
	    	    				if(LinkRGroup.getLinkRGroup(editId).getSRLGWDMLinkList().size()==0)
    	    						break;    
	    	    			}
	    	    			for(int i = 0;i < getRow(teamModel);i++ ){
	    	    				LinkRGroup.getLinkRGroup(editId).addlWDMLinkList(WDMLink.getWDMLink((String.valueOf(teamModel.getValueAt(i, 0)))));
		    	    		}
	    	    		}
//	    	    		else if(LinkRGroup.getLinkRGroup(editId).getM_eLayer()==Layer.OTN){
//	    	    			srlgLayer.setSelectedIndex(3);
//	    	    			for(int m = 0;m < LinkRGroup.getLinkRGroup(editId).getM_lOTNLinkList().size();){
//	    	    				LinkRGroup.getLinkRGroup(editId).delWDMLinkList(LinkRGroup.getLinkRGroup(editId).getM_lOTNLinkList().get(m));
//	    	    				if(LinkRGroup.getLinkRGroup(editId).getM_lOTNLinkList().size()==0)
//    	    						break;    
//	    	    			}
//	    	    			for(int i = 0;i < getRow(teamModel);i++ ){
//	    	    				LinkRGroup.getLinkRGroup(editId).addlWDMLinkList(WDMLink.getOTNLink((Integer.parseInt(String.valueOf(teamModel.getValueAt(i, 0))))));
//		    	    		}
//	    	    		}
//	    	    		else if(LinkRGroup.getLinkRGroup(editId).getM_eLayer()==Layer.ASON){
//	    	    			srlgLayer.setSelectedIndex(4);
//	    	    			for(int m = 0;m < LinkRGroup.getLinkRGroup(editId).getM_lASONLinkList().size();){
//	    	    				LinkRGroup.getLinkRGroup(editId).dellSDHLinkList(LinkRGroup.getLinkRGroup(editId).getM_lASONLinkList().get(m));
//	    	    				if(LinkRGroup.getLinkRGroup(editId).getM_lASONLinkList().size()==0)
//    	    						break; 
//	    	    			}
//	    	    			for(int i = 0;i < getRow(teamModel);i++ ){
//	    	    				LinkRGroup.getLinkRGroup(editId).addlSDHLinkList(SDHLink.getASONLink((Integer.parseInt(String.valueOf(teamModel.getValueAt(i, 0))))));
//		    	    		}
//	    	    		}
//	    	    		else if(LinkRGroup.getLinkRGroup(editId).getM_eLayer()==Layer.SDH){
//	    	    			srlgLayer.setSelectedIndex(5);
//	    	    			for(int m = 0;m < LinkRGroup.getLinkRGroup(editId).getM_lSDHLinkList().size();){
//	    	    				LinkRGroup.getLinkRGroup(editId).dellSDHLinkList(LinkRGroup.getLinkRGroup(editId).getM_lSDHLinkList().get(m));
//	    	    				if(LinkRGroup.getLinkRGroup(editId).getM_lSDHLinkList().size()==0)
//    	    						break; 
//	    	    			}
//	    	    			for(int i = 0;i < getRow(teamModel);i++ ){
//	    	    				LinkRGroup.getLinkRGroup(editId).addlSDHLinkList(SDHLink.getSDHLink((Integer.parseInt(String.valueOf(teamModel.getValueAt(i, 0))))));
//		    	    		}
//	    	    		}
	    			}
	    			
//	    			else if(linkLayer.getSelectedIndex() == 0){
//	    				LinkRGroup.clear();//测试用
//	    		    	LinkRGroup.automation();//测试用
//	    			}
	    			
	    			refreshlinkTable();
	    			removeTable(teamModel);
	    			refreshsrlgTable();
	    			title1.setText("链路组成员链路");
		    		linkLayer.setEnabled(true);
		    		srlgLayer.setEnabled(true);	 
		    		in.setEnabled(false);
		    		out.setEnabled(false);
		    		confirm.setEnabled(false);
		    		 set.setEnabled(true);
		    		 srg.setEnabled(true);
	    		}
	    		else if(teamModel.getValueAt(0, 0) == null)
	    			JOptionPane.showMessageDialog(null, "请选择链路！");
	    		else//单个链路怎么处理
	    			JOptionPane.showMessageDialog(null, "没有设置SRG组必要！");
	    	
	    	}
	    }); 
	    
	    delete.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    	    editor.setEnabled(false);
	     	    delete.setEnabled(false);
	    		Vector data = new Vector();
	    		int[] id = new int[getidNum(srlgModel,srlgTable.getSelectedRows())];
	    		for(int i = 0;i<getId(srlgModel,srlgTable.getSelectedRows()).length;i++){
	    			LinkRGroup it  = LinkRGroup.getLinkRGroup(getId(srlgModel,srlgTable.getSelectedRows())[i]);
	    			if(linkLayer.getSelectedIndex()==0){
	    				for(int j = 0; j < it.getSRLGFiberLinkList().size();){
	    					FiberLink a = it.getSRLGFiberLinkList().get(j);
	    					it.dellFiberLinkList(a);
	    					Object[] objectdata =new Object[4];
	    					objectdata[0]=a.getId();
	    					objectdata[1]=a.getName();
	    					objectdata[2]=a.getFromNode().getM_sName();
	    					objectdata[3]=a.getToNode().getM_sName();
	    					if(linkModel.getValueAt(29, 0)==null)
	    						linkModel.removeRow(29);
	    						linkModel.insertRow(getRow(linkModel), objectdata);
	    				}
	    			}
	    			else if(linkLayer.getSelectedIndex()==1){
	    				for(int j = 0; j < it.getSRLGFiberLinkList().size();){
	    					WDMLink a = it.getSRLGWDMLinkList().get(j);
	    					it.delWDMLinkList(a);
	    					Object[] objectdata =new Object[4];
	    					objectdata[0]=a.getId();
	    					objectdata[1]=a.getName();
	    					objectdata[2]=a.getFromNode().getM_sName();
	    					objectdata[3]=a.getToNode().getM_sName();
	    					if(linkModel.getValueAt(29, 0)==null)
	    						linkModel.removeRow(29);
	    						linkModel.insertRow(getRow(linkModel), objectdata);
	    				}
	    			}
	    			/*else if(linkLayer.getSelectedIndex()==2){
	    				for(int j = 0; j < it.getM_lOTNLinkList().size();){
	    					WDMLink a = it.getM_lOTNLinkList().get(j);
	    					it.delWDMLinkList(a);
	    					Object[] objectdata =new Object[4];
	    					objectdata[0]=a.getId();
	    					objectdata[1]=a.getName();
	    					objectdata[2]=a.getFromNode().getM_sName();
	    					objectdata[3]=a.getToNode().getM_sName();
	    					if(linkModel.getValueAt(29, 0)==null)
	    						linkModel.removeRow(29);
	    						linkModel.insertRow(getRow(linkModel), objectdata);
	    				}
	    			}*/
//	    			else if(linkLayer.getSelectedIndex()==3){
//	    				for(int j = 0; j < it.getM_lASONLinkList().size();){
//	    					SDHLink a = it.getM_lASONLinkList().get(j);
//	    					it.dellSDHLinkList(a);
//	    					Object[] objectdata =new Object[4];
//	    					objectdata[0]=a.getM_nID();
//	    					objectdata[1]=a.getM_sName();
//	    					objectdata[2]=a.getM_cFromNode().getM_sName();
//	    					objectdata[3]=a.getM_cToNode().getM_sName();
//	    					if(linkModel.getValueAt(29, 0)==null)
//	    						linkModel.removeRow(29);
//	    						linkModel.insertRow(getRow(linkModel), objectdata);
//	    				}
//	    			}
//	    			else if(linkLayer.getSelectedIndex()==4){
//	    				for(int j = 0; j < it.getM_lSDHLinkList().size();){
//	    					SDHLink a = it.getM_lSDHLinkList().get(j);
//	    					it.dellSDHLinkList(a);
//	    					Object[] objectdata =new Object[4];
//	    					objectdata[0]=a.getM_nID();
//	    					objectdata[1]=a.getM_sName();
//	    					objectdata[2]=a.getM_cFromNode().getM_sName();
//	    					objectdata[3]=a.getM_cToNode().getM_sName();
//	    					if(linkModel.getValueAt(29, 0)==null)
//	    						linkModel.removeRow(29);
//	    						linkModel.insertRow(getRow(linkModel), objectdata);
//	    				}
//	    			}
	    			LinkRGroup.SRLGroupList.remove(LinkRGroup.getLinkRGroup(getId(srlgModel,srlgTable.getSelectedRows())[i]));
	    		}
	    		for(int j=0;j< ringNames.length;j++){
	    			for(int n = 0;n < srlgTable.getSelectedRows().length; n++){
	    				srlgModel.removeRow(srlgTable.getSelectedRows()[n]);
	    			}
//	    			保持有空白表
	    			if(srlgModel.getRowCount() < 100){
	    				int num = srlgModel.getRowCount();
	    				for(int x = 0;x < 100- num; x++)
	    					srlgModel.addRow(data);
	    			} 
	    		}
	    	}
	    });
	    
	   srlgTable.addMouseListener(new MouseAdapter() {
	          @Override
	          public void mouseClicked(MouseEvent e) {
	          if(teamTable.getValueAt(0, 0)==null&&srlgTable.getValueAt(srlgTable.getSelectedRow(),0)!=null&&!LinkRGroup.getLinkRGroup(Integer.parseInt(String.valueOf(srlgModel.getValueAt(srlgTable.getSelectedRow(), 0)))).isNatrue()&&!in.isEnabled()){//用ID获得SRG判断getNature为false才可
	        	  editor.setEnabled(true);
	       	      delete.setEnabled(true);
	          }
	          else{
	         	  editor.setEnabled(false);
		   	      delete.setEnabled(false);
	          }	   
	          }
	         });
	   srg.addMouseListener(new MouseAdapter() {
	          @Override
	          public void mouseClicked(MouseEvent e) {
//	        	  LinkRGroup.clear();//测试用
	        if(srg.isEnabled()){
	          	LinkRGroup.automation();//测试用
//	          	refreshsrlgTable();
	          	srg.setEnabled(false);
	        	  }
	          }
	         });
	    
	    JPanel p = new JPanel();
	    p.setLayout (new GridLayout (0, 1, 10, 40));
	    p.add(new JLabel(""));
	    p.add(in);
	    p.add(out);
	    p.add(confirm);//p上有竖直的三个按钮
	    
	    JPanel p3 = new JPanel();
	    p3.setLayout (new GridLayout (0, 1, 5, 10));//p3放上下两个部分
	   
	    JPanel p2 = new JPanel();
	    p2.setBorder (BorderFactory.createTitledBorder ("共享风险链路组列表"));
	    
	    

	    
	    TableColumnModel tcm0 =  srlgTable.getColumnModel();   
	    tcm0.getColumn(0).setPreferredWidth(130);
	    tcm0.getColumn(1).setPreferredWidth(130);
	    tcm0.getColumn(2).setPreferredWidth(590);
	    TableColumnModel tcm1 = linkTable.getColumnModel();   
	    tcm1.getColumn(0).setPreferredWidth(50);
	    tcm1.getColumn(1).setPreferredWidth(100);
	    tcm1.getColumn(2).setPreferredWidth(100);
	    tcm1.getColumn(3).setPreferredWidth(100);
	    TableColumnModel tcm2 = teamTable.getColumnModel();   
	    tcm2.getColumn(0).setPreferredWidth(50);
	    tcm2.getColumn(1).setPreferredWidth(100);
	    tcm2.getColumn(2).setPreferredWidth(84);
	    tcm2.getColumn(3).setPreferredWidth(84);
	    
	    
	    Insets insert1;
	    GridBagLayout mg1 = new GridBagLayout();
		GridBagConstraints mgc1 = new GridBagConstraints();
		p2.setLayout(mg1);
	    
		
		mgc1.gridwidth = GridBagConstraints.REMAINDER;
		mgc1.weightx = 1.0;
		mgc1.gridheight = 1;
		mgc1.weighty = 1.0;
		mgc1.anchor = GridBagConstraints.WEST;
		mgc1.fill = GridBagConstraints.HORIZONTAL;
		insert1 = new Insets(5,5,5,765);
		mgc1.insets = insert1;
		p2.add(srlgLayer,mgc1);
		
		mgc1.gridwidth = GridBagConstraints.REMAINDER;
		mgc1.weightx = 1.0;
		mgc1.fill = GridBagConstraints.BOTH;
		mgc1.gridheight = 20;
		mgc1.weighty =20.0;
		mgc1.anchor = GridBagConstraints.CENTER;
		insert1 = new Insets(0,5,5,5);
		mgc1.insets = insert1;
		p2.add(pane3,mgc1);
		
		mgc1.weighty = 1.0;
		mgc1.gridheight = 1;
		mgc1.gridwidth = 1;
		mgc1.fill = GridBagConstraints.NONE;
		p2.add(set,mgc1);
		mgc1.gridwidth = 2;
		p2.add(editor,mgc1);
		mgc1.gridwidth = 3;
		p2.add(delete,mgc1);
		mgc1.gridwidth = 4;
		p2.add(srg,mgc1);
		mgc1.gridwidth = GridBagConstraints.REMAINDER;
		p2.add(close,mgc1);
		 
		 
	    JPanel p1 = new JPanel();
	    p1.setBorder (BorderFactory.createTitledBorder ("链路链表"));

		
	    Insets insert;	
	    GridBagLayout mg = new GridBagLayout();
		GridBagConstraints mgc = new GridBagConstraints();
		p1.setLayout(mg);
		
	
		mgc.gridwidth = 1;
		mgc.weightx = 1.0;
		mgc.gridheight = 1;
		mgc.weighty = 1.0;
		mgc.anchor = GridBagConstraints.WEST;
		mgc.fill = GridBagConstraints.HORIZONTAL;
		insert = new Insets(0,5,10,320);
		mgc.insets = insert;
		p1.add(linkLayer,mgc);
		
		

		mgc.gridwidth = 2;
		mgc.weightx = 1.0;
		insert = new Insets(0,0,0,0);
		mgc.insets = insert;
		mgc.fill = GridBagConstraints.NONE;
		p1.add(new JLabel(""),mgc);
		mgc.gridwidth = 3;
		mgc.weightx = 1.0;
		insert = new Insets(0,0,-5,0);
		mgc.insets = insert;
		mgc.anchor = GridBagConstraints.CENTER;
		p1.add(title1,mgc);
		mgc.fill = GridBagConstraints.BOTH;
		
		mgc.gridwidth =  GridBagConstraints.REMAINDER;
		p1.add(new JLabel(""),mgc);
		
		mgc.gridwidth = 1;
		mgc.gridheight = 14;
		mgc.weighty = 14.0;
		insert = new Insets(0,5,0,48);
		mgc.insets = insert;
		p1.add(pane1,mgc);
		
		mgc.gridwidth = 2;
        p1.add(p);
		
		mgc.gridwidth =  GridBagConstraints.REMAINDER;
		mgc.weightx = 11.0;
		mgc.gridheight =7;
		mgc.weighty = 7.0;
		insert = new Insets(0,48,0,5);
		mgc.insets = insert;
		p1.add(pane2,mgc);

		
		p3.add(p1);
		p3.add(p2);
		this.add(p3);
        setContentPane(p3);
     	this.setVisible(true);
		
		
    }
    public void firstrefresh(){
    	removeTable(srlgModel);
	      Object data[] = new Object[3];
	      int index = 0;
	      for(int n = 0;n <LinkRGroup.SRLGroupList.size();n++){
	    	  if(LinkRGroup.SRLGroupList.get(n).getBelongLayer().equals(Layer.Fiber)){
				data[0] = LinkRGroup.SRLGroupList.get(n).getID();
				data[1] = LinkRGroup.SRLGroupList.get(n).getName();
				StringBuffer buffer=new StringBuffer();	
					for(int i = 0; i < LinkRGroup.SRLGroupList.get(n).getSRLGFiberLinkList().size();i++){
						 buffer.append(LinkRGroup.SRLGroupList.get(n).getSRLGFiberLinkList().get(i).getName());
						 if(i != LinkRGroup.SRLGroupList.get(n).getSRLGFiberLinkList().size()-1)
							 buffer.append(" ; ");
					}
					data[2] = buffer;
				}
				if(srlgModel.getValueAt(99,0)==null)
    				srlgModel.removeRow(99);
				srlgModel.insertRow(index++, data);
				int count=getRow(srlgModel);
		    	  for(int i=0;i<count-1;i++){
		    		  for(int j=i+1;j<count;j++){
		    			  if(String.valueOf(srlgModel.getValueAt(i, 0))!=null&&String.valueOf(srlgModel.getValueAt(j, 0))!=null){
		    			  if(String.valueOf(srlgModel.getValueAt(i, 0)).equals((String.valueOf(srlgModel.getValueAt(j, 0))))){
		    				  Vector vector=new Vector();
		    				  srlgModel.removeRow(--index);
	  		    				  srlgModel.insertRow(99, vector);
		    			  }
		    			  }
		    		  }
		    	  }
	      }
	      
    }
    public void refreshsrlgTable(){
    	  removeTable(srlgModel);
			      Object data[] = new Object[3];
			      int index = 0;
		    	  if(srlgLayer.getSelectedIndex()==0){
		    		  for(int n = 0;n <LinkRGroup.SRLGroupList.size();n++){
		    				data[0] = LinkRGroup.SRLGroupList.get(n).getID();
		    				data[1] = LinkRGroup.SRLGroupList.get(n).getName();
		    				StringBuffer buffer=new StringBuffer();		
		    				switch(LinkRGroup.SRLGroupList.get(n).getBelongLayer()){
		    				case Fiber:
		    					for(int i = 0; i < LinkRGroup.SRLGroupList.get(n).getSRLGFiberLinkList().size();i++){
		    						 buffer.append(LinkRGroup.SRLGroupList.get(n).getSRLGFiberLinkList().get(i).getName());
		    						 if(i != LinkRGroup.SRLGroupList.get(n).getSRLGFiberLinkList().size()-1)
		    							 buffer.append(" ; ");
		    					}
		    					data[2] = buffer;
		    					if(srlgModel.getValueAt(99,0)==null)
		            				srlgModel.removeRow(99);
				    			srlgModel.insertRow(index++, data);
				    			int count4=getRow(srlgModel);
				  		    	  for(int i=0;i<count4-1;i++){
				  		    		  for(int j=i+1;j<count4;j++){
				  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0))!=null&&String.valueOf(srlgModel.getValueAt(j, 0))!=null){
				  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0)).equals((String.valueOf(srlgModel.getValueAt(j, 0))))){
				  		    				  Vector vector=new Vector();
				  		    				  srlgModel.removeRow(--index);
				  		    				if(srlgModel.getValueAt(98, 0)== null){
					  		    				  srlgModel.insertRow(99, vector);
					  		    				  }
				  		    			  }
				  		    		  }
				  		    		  }
				    			}
		    					break;
		    				case WDM:
		    					//System.out.println(" LinkRGroup.s_lRGroup.get(n).getM_lWDMLinkList().size()"+ LinkRGroup.s_lRGroup.get(n).getM_lWDMLinkList().size());
		    				    for(int i = 0; i < LinkRGroup.SRLGroupList.get(n).getSRLGWDMLinkList().size();i++){
		    					     buffer.append(LinkRGroup.SRLGroupList.get(n).getSRLGWDMLinkList().get(i).getName());
		    					     if(i != LinkRGroup.SRLGroupList.get(n).getSRLGWDMLinkList().size()-1)
		    						      buffer.append(" ; ");
		    				    }
		    				    data[2] = buffer;
		    					if(srlgModel.getValueAt(99,0)==null)
		            				srlgModel.removeRow(99);
				    			srlgModel.insertRow(index++, data);
				    			int count=getRow(srlgModel);
				  		    	  for(int i=0;i<count-1;i++){
				  		    		  for(int j=i+1;j<count;j++){
				  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0))!=null&&String.valueOf(srlgModel.getValueAt(j, 0))!=null){
				  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0)).equals((String.valueOf(srlgModel.getValueAt(j, 0))))){
				  		    				  Vector vector=new Vector();
				  		    				  srlgModel.removeRow(--index);
				  		    				if(srlgModel.getValueAt(98, 0)== null){
					  		    				  srlgModel.insertRow(99, vector);
					  		    				  }
				  		    			  }
				  		    		  }
				  		    		  }
				    			}
		    				    break;
//		    				case OTN:
//		    					//System.out.println(" LinkRGroup.s_lRGroupList.get(n).getM_lWDMLinkList().size()"+ LinkRGroup.s_lRGroupList.get(n).getM_lWDMLinkList().size());
//		    				    for(int i = 0; i < LinkRGroup.SRLGroupList.get(n).getM_lOTNLinkList().size();i++){
//		    					     buffer.append(LinkRGroup.SRLGroupList.get(n).getM_lOTNLinkList().get(i).getName());
//		    					     if(i != LinkRGroup.SRLGroupList.get(n).getM_lOTNLinkList().size()-1)
//		    						      buffer.append(" ; ");
//		    				    }
//		    				    data[2] = buffer;
//		    					if(srlgModel.getValueAt(99,0)==null)
//		            				srlgModel.removeRow(99);
//				    			srlgModel.insertRow(index++, data);
//				    			int count1=getRow(srlgModel);
//				  		    	  for(int i=0;i<count1-1;i++){
//				  		    		  for(int j=i+1;j<count1;j++){
//				  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0))!=null&&String.valueOf(srlgModel.getValueAt(j, 0))!=null){
//				  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0)).equals((String.valueOf(srlgModel.getValueAt(j, 0))))){
//				  		    				  Vector vector=new Vector();
//				  		    				  srlgModel.removeRow(--index);
//				  		    				if(srlgModel.getValueAt(98, 0)== null){
//					  		    				  srlgModel.insertRow(99, vector);
//					  		    				  }
//				  		    			  }
//				  		    		  }
//				  		    		  }
//				    			}
//		    				    break;
//		    				case SDH:
//		    					
//		    				     for(int i = 0; i < LinkRGroup.s_lRGroupList.get(n).getM_lSDHLinkList().size();i++){
//		    					     buffer.append(LinkRGroup.s_lRGroupList.get(n).getM_lSDHLinkList().get(i).getM_sName());		
//		    					     if(i != LinkRGroup.s_lRGroupList.get(n).getM_lSDHLinkList().size()-1)
//		    						     buffer.append(" ; ");				 
//		    				   }
//		    				   data[2] = buffer;
//		    					if(srlgModel.getValueAt(99,0)==null)
//		            				srlgModel.removeRow(99);
//				    			srlgModel.insertRow(index++, data);
//				    			int count2=getRow(srlgModel);
//				  		    	  for(int i=0;i<count2-1;i++){
//				  		    		  for(int j=i+1;j<count2;j++){
//				  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0))!=null&&String.valueOf(srlgModel.getValueAt(j, 0))!=null){
//				  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0)).equals((String.valueOf(srlgModel.getValueAt(j, 0))))){
//				  		    				  Vector vector=new Vector();
//				  		    				  srlgModel.removeRow(--index);
//				  		    				if(srlgModel.getValueAt(98, 0)== null){
//					  		    				  srlgModel.insertRow(99, vector);
//					  		    				  }
//				  		    			  }
//				  		    		  }
//				  		    		  }
//				    			}
//		    				break;
//		    				case ASON:
//		    					//System.out.println(" LinkRGroup.s_lRGroupList.get(n).getM_lSDHLinkList().size()"+ LinkRGroup.s_lRGroupList.get(n).getM_lWDMLinkList().size());
//		    				     for(int i = 0; i < LinkRGroup.s_lRGroupList.get(n).getM_lASONLinkList().size();i++){
//		    					     buffer.append(LinkRGroup.s_lRGroupList.get(n).getM_lASONLinkList().get(i).getM_sName());
//		    					     if(i != LinkRGroup.s_lRGroupList.get(n).getM_lASONLinkList().size()-1)
//		    						     buffer.append(" ; ");
//		    				   }
//		    				   data[2] = buffer;	
//		    					if(srlgModel.getValueAt(99,0)==null)
//		            				srlgModel.removeRow(99);
//				    			srlgModel.insertRow(index++, data);
//				    			int count3=getRow(srlgModel);
//				  		    	  for(int i=0;i<count3-1;i++){
//				  		    		  for(int j=i+1;j<count3;j++){
//				  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0))!=null&&String.valueOf(srlgModel.getValueAt(j, 0))!=null){
//				  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0)).equals((String.valueOf(srlgModel.getValueAt(j, 0))))){
//				  		    				  Vector vector=new Vector();
//				  		    				  srlgModel.removeRow(--index);
//				  		    				if(srlgModel.getValueAt(98, 0)== null){
//					  		    				  srlgModel.insertRow(99, vector);
//					  		    				  }
//				  		    			  }
//				  		    		  }
//				  		    		  }
//				    			}
//		    				   break;
		    				}
//		    				if(srlgModel.getValueAt(29,0)==null)
//	            				srlgModel.removeRow(29);
//		    				srlgModel.insertRow(index++, data);
//		    				int count=getRow(srlgModel);
//		  		    	  for(int i=0;i<count-1;i++){
//		  		    		  for(int j=i+1;j<count;j++){
//		  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0))!=null&&String.valueOf(srlgModel.getValueAt(j, 0))!=null){
//		  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0)).equals((String.valueOf(srlgModel.getValueAt(j, 0))))){
//		  		    				  Vector vector=new Vector();
//		  		    				  srlgModel.removeRow(--index);
//		  		    				if(srlgModel.getValueAt(29, 0)== null){
//			  		    				  srlgModel.insertRow(29, vector);
//			  		    				  }
//		  		    			  }
//		  		    		  }
//		  		    		  }
//		  		    	  }
		    			}
		    	  }
		    	  else if(srlgLayer.getSelectedIndex()==1){
		    		  for(int n = 0;n <LinkRGroup.SRLGroupList.size();n++){		    				
		    				StringBuffer buffer=new StringBuffer();		
		    				if(LinkRGroup.SRLGroupList.get(n).getBelongLayer().equals(Layer.Fiber)){
		    					data[0] = LinkRGroup.SRLGroupList.get(n).getID();
			    				data[1] = LinkRGroup.SRLGroupList.get(n).getName();
		    					for(int i = 0; i < LinkRGroup.SRLGroupList.get(n).getSRLGFiberLinkList().size();i++){
		    						 buffer.append(LinkRGroup.SRLGroupList.get(n).getSRLGFiberLinkList().get(i).getName());
		    						 if(i != LinkRGroup.SRLGroupList.get(n).getSRLGFiberLinkList().size()-1)
		    							 buffer.append(" ; ");
		    					}
		    					data[2] = buffer;	
		    					if(srlgModel.getValueAt(99,0)==null)
		            				srlgModel.removeRow(99);
				    			srlgModel.insertRow(index++, data);
//				    			int count=getRow(srlgModel);
//				  		    	  for(int i=0;i<count;i++){
//				  		    		  for(int j=i+1;j<count;j++){
//				  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0))!=null&&String.valueOf(srlgModel.getValueAt(j, 0))!=null){
//				  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0)).equals((String.valueOf(srlgModel.getValueAt(j, 0))))){
//				  		    				  Vector vector=new Vector();
//				  		    				  srlgModel.removeRow(--index);
//				  		    				if(srlgModel.getValueAt(29, 0)== null){
//					  		    				  srlgModel.insertRow(29, vector);
//					  		    				  }
//				  		    			  }
//				  		    		  }
//				  		    		  }
//				    			}
		    				}    		  
		    	  }//
		    	  }
		    	  else if(srlgLayer.getSelectedIndex()==2){
		    		  for(int n = 0;n <LinkRGroup.SRLGroupList.size();n++){		    				
		    				StringBuffer buffer=new StringBuffer();		
		    				if(LinkRGroup.SRLGroupList.get(n).getBelongLayer().equals(Layer.WDM)){
		    					data[0] = LinkRGroup.SRLGroupList.get(n).getID();
			    				data[1] = LinkRGroup.SRLGroupList.get(n).getName();
		    					for(int i = 0; i < LinkRGroup.SRLGroupList.get(n).getSRLGWDMLinkList().size();i++){
		    						 buffer.append(LinkRGroup.SRLGroupList.get(n).getSRLGWDMLinkList().get(i).getName());
		    						 if(i != LinkRGroup.SRLGroupList.get(n).getSRLGWDMLinkList().size()-1)
		    							 buffer.append(" ; ");
		    					}
		    					data[2] = buffer;	
		    					if(srlgModel.getValueAt(29,0)==null)
		            				srlgModel.removeRow(99);
				    			srlgModel.insertRow(index++, data);
				    			int count=getRow(srlgModel);
				  		    	  for(int i=0;i<count-1;i++){
				  		    		  for(int j=i+1;j<count;j++){
				  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0))!=null&&String.valueOf(srlgModel.getValueAt(j, 0))!=null){
				  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0)).equals((String.valueOf(srlgModel.getValueAt(j, 0))))){
				  		    				  Vector vector=new Vector();
				  		    				  srlgModel.removeRow(--index);
				  		    				if(srlgModel.getValueAt(98, 0)== null){
					  		    				  srlgModel.insertRow(99, vector);
					  		    				  }
				  		    			  }
				  		    		  }
				  		    		  }
				    			}
		    				}    		  
		    	  }//
//		    		  for(int n = 0;n <LinkRGroup.SRLGroupList.size();n++){		    				
//		    				StringBuffer buffer=new StringBuffer();		
//		    				if(LinkRGroup.SRLGroupList.get(n).getBelongLayer().equals(Layer.OTN)){
//		    					data[0] = LinkRGroup.SRLGroupList.get(n).getID();
//			    				data[1] = LinkRGroup.SRLGroupList.get(n).getName();
//		    					for(int i = 0; i < LinkRGroup.SRLGroupList.get(n).getM_lOTNLinkList().size();i++){
//		    						 buffer.append(LinkRGroup.SRLGroupList.get(n).getM_lOTNLinkList().get(i).getName());
//		    						 if(i != LinkRGroup.SRLGroupList.get(n).getM_lOTNLinkList().size()-1)
//		    							 buffer.append(" ; ");
//		    					}
//		    					data[2] = buffer;	
//		    					if(srlgModel.getValueAt(99,0)==null)
//		            				srlgModel.removeRow(99);
//				    			srlgModel.insertRow(index++, data);
//				    			int count=getRow(srlgModel);
//				  		    	  for(int i=0;i<count-1;i++){
//				  		    		  for(int j=i+1;j<count;j++){
//				  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0))!=null&&String.valueOf(srlgModel.getValueAt(j, 0))!=null){
//				  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0)).equals((String.valueOf(srlgModel.getValueAt(j, 0))))){
//				  		    				  Vector vector=new Vector();
//				  		    				  srlgModel.removeRow(--index);
//				  		    				if(srlgModel.getValueAt(98, 0)== null){
//					  		    				  srlgModel.insertRow(99, vector);
//					  		    				  }
//				  		    			  }
//				  		    		  }
//				  		    		  }
//				    			}
//		    				}    		  
//		    	  }  
		    	  }
//		    	  else if(srlgLayer.getSelectedIndex()==3){
//		    		  for(int n = 0;n <LinkRGroup.s_lRGroupList.size();n++){		    				
//		    				StringBuffer buffer=new StringBuffer();		
//		    				if(LinkRGroup.s_lRGroupList.get(n).getM_eLayer().equals(Layer.OTN)){
//		    					data[0] = LinkRGroup.s_lRGroupList.get(n).getM_nID();
//			    				data[1] = LinkRGroup.s_lRGroupList.get(n).getM_sName();
//		    					for(int i = 0; i < LinkRGroup.s_lRGroupList.get(n).getM_lOTNLinkList().size();i++){
//		    						 buffer.append(LinkRGroup.s_lRGroupList.get(n).getM_lOTNLinkList().get(i).getM_sName());
//		    						 if(i != LinkRGroup.s_lRGroupList.get(n).getM_lOTNLinkList().size()-1)
//		    							 buffer.append(" ; ");
//		    					}
//		    					data[2] = buffer;	
//		    					if(srlgModel.getValueAt(99,0)==null)
//		            				srlgModel.removeRow(99);
//				    			srlgModel.insertRow(index++, data);
//				    			int count=getRow(srlgModel);
//				  		    	  for(int i=0;i<count-1;i++){
//				  		    		  for(int j=i+1;j<count;j++){
//				  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0))!=null&&String.valueOf(srlgModel.getValueAt(j, 0))!=null){
//				  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0)).equals((String.valueOf(srlgModel.getValueAt(j, 0))))){
//				  		    				  Vector vector=new Vector();
//				  		    				  srlgModel.removeRow(--index);
//				  		    				if(srlgModel.getValueAt(98, 0)== null){
//					  		    				  srlgModel.insertRow(99, vector);
//					  		    				  }
//				  		    			  }
//				  		    		  }
//				  		    		  }
//				    			}
//		    				}    		  
//		    	  }//
//		    	  }
//		    	  else if(srlgLayer.getSelectedIndex()==3){
//		    		  for(int n = 0;n <LinkRGroup.s_lRGroupList.size();n++){		    				
//		    				StringBuffer buffer=new StringBuffer();		
//		    				if(LinkRGroup.s_lRGroupList.get(n).getM_eLayer().equals(Layer.ASON)){
//		    					data[0] = LinkRGroup.s_lRGroupList.get(n).getM_nID();
//			    				data[1] = LinkRGroup.s_lRGroupList.get(n).getM_sName();
//		    					for(int i = 0; i < LinkRGroup.s_lRGroupList.get(n).getM_lASONLinkList().size();i++){
//		    						 buffer.append(LinkRGroup.s_lRGroupList.get(n).getM_lASONLinkList().get(i).getM_sName());
//		    						 if(i != LinkRGroup.s_lRGroupList.get(n).getM_lASONLinkList().size()-1)
//		    							 buffer.append(" ; ");
//		    					}
//		    					data[2] = buffer;	
//		    					if(srlgModel.getValueAt(99,0)==null)
//		            				srlgModel.removeRow(99);
//				    			srlgModel.insertRow(index++, data);
//				    			int count=getRow(srlgModel);
//				  		    	  for(int i=0;i<count-1;i++){
//				  		    		  for(int j=i+1;j<count;j++){
//				  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0))!=null&&String.valueOf(srlgModel.getValueAt(j, 0))!=null){
//				  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0)).equals((String.valueOf(srlgModel.getValueAt(j, 0))))){
//				  		    				  Vector vector=new Vector();
//				  		    				  srlgModel.removeRow(--index);
//				  		    				if(srlgModel.getValueAt(98, 0)== null){
//					  		    				  srlgModel.insertRow(99, vector);
//					  		    				  }
//				  		    			  }
//				  		    		  }
//				  		    		  }
//		    				}    		  
//		    	  }//
//		    		  }
//		    	  }
//		    	  else if(srlgLayer.getSelectedIndex()==4){
//		    		  for(int n=0;n <LinkRGroup.s_lRGroupList.size();n++){		    				
//		    				StringBuffer buffer=new StringBuffer();		
//		    				if(LinkRGroup.s_lRGroupList.get(n).getM_eLayer().equals(Layer.SDH)){
//		    					data[0] = LinkRGroup.s_lRGroupList.get(n).getM_nID();
//			    				data[1] = LinkRGroup.s_lRGroupList.get(n).getM_sName();
//		    					for(int i = 0; i < LinkRGroup.s_lRGroupList.get(n).getM_lSDHLinkList().size();i++){
//		    						 buffer.append(LinkRGroup.s_lRGroupList.get(n).getM_lSDHLinkList().get(i).getM_sName());
//		    						 if(i != LinkRGroup.s_lRGroupList.get(n).getM_lSDHLinkList().size()-1)
//		    							 buffer.append(" ; ");
//		    					}
//		    					data[2] = buffer;	
//		    					if(srlgModel.getValueAt(99,0)==null)
//		            				srlgModel.removeRow(99);
//				    			srlgModel.insertRow(index++, data);
//				    			int count=getRow(srlgModel);
//				  		    	  for(int i=0;i<count-1;i++){
//				  		    		  for(int j=i+1;j<count;j++){
//				  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0))!=null&&String.valueOf(srlgModel.getValueAt(j, 0))!=null){
//				  		    			  if(String.valueOf(srlgModel.getValueAt(i, 0)).equals((String.valueOf(srlgModel.getValueAt(j, 0))))){
//				  		    				  Vector vector=new Vector();
//				  		    				  srlgModel.removeRow(--index);
//				  		    				  if(srlgModel.getValueAt(98, 0)== null){
//				  		    				  srlgModel.insertRow(99, vector);
//				  		    				  }
//				  		    			  }
//				  		    		  }
//				  		    		  }
//				    			}
//		    				}
//		    		  }
//		    	  }
    }
		    	  
    public void refreshlinkTable(){
    	removeTable(linkModel);
    	int index=0;
    	Object data[] = new Object[4];
    	if(linkLayer.getSelectedIndex()==0){		    		
    		for(int n = 0;n <FiberLink.fiberLinkList.size();n++){
    			data[0] = FiberLink.fiberLinkList.get(n).getId();
    			data[1] = FiberLink.fiberLinkList.get(n).getName();
    			data[2] = FiberLink.fiberLinkList.get(n).getFromNode().getM_sName();
    			data[3] = FiberLink.fiberLinkList.get(n).getToNode().getM_sName();
    			if(linkModel.getValueAt(29,0)==null)
    				linkModel.removeRow(29);
    			linkModel.insertRow(index, data);
    			index++;
    			int count=LinkRGroup.SRLGroupList.size();
    			int id=FiberLink.fiberLinkList.get(n).getId();
    			Vector vector=new Vector();
    			for(int i=0;i<count;i++){
    				Layer layer=LinkRGroup.SRLGroupList.get(i).getBelongLayer();
    				if(layer.equals(Layer.Fiber)){
    					for(int j=0;j<LinkRGroup.SRLGroupList.get(i).getSRLGFiberLinkList().size();j++){
    						if(id==LinkRGroup.SRLGroupList.get(i).getSRLGFiberLinkList().get(j).getId()){
    							--index;
    							linkModel.removeRow(index);
    							linkModel.insertRow(29, vector);
    						}
    					}
    		}		    		
    	}
    		}
    	}
    	else if(linkLayer.getSelectedIndex()==1){
    		for(int n = 0;n <WDMLink.WDMLinkList.size();n++){
    			data[0] = WDMLink.WDMLinkList.get(n).getId();
    			data[1] = WDMLink.WDMLinkList.get(n).getName();
    			data[2] = WDMLink.WDMLinkList.get(n).getFromNode().getM_sName();
    			data[3] = WDMLink.WDMLinkList.get(n).getToNode().getM_sName();
    			if(linkModel.getValueAt(29,0)==null)
    				linkModel.removeRow(29);
    			linkModel.insertRow(index, data);
    			index++;
    			int count=LinkRGroup.SRLGroupList.size();
    			int id=WDMLink.WDMLinkList.get(n).getId();
    			Vector vector=new Vector();
    			for(int i=0;i<count;i++){
    				Layer layer=LinkRGroup.SRLGroupList.get(i).getBelongLayer();
    				if(layer.equals(Layer.WDM)){
    					for(int j=0;j<LinkRGroup.SRLGroupList.get(i).getSRLGWDMLinkList().size();j++){
    						if(id==LinkRGroup.SRLGroupList.get(i).getSRLGWDMLinkList().get(j).getId()){
    							--index;
    							linkModel.removeRow(index);
    							linkModel.insertRow(29, vector);
    							break;
    						}
    					}
    		}		    		
    	}
    		}
    	  
    	}
//    	else if(linkLayer.getSelectedIndex()==2){
//    		for(int n = 0;n <WDMLink.s_lOTNLinkList.size();n++){
//    			data[0] = WDMLink.s_lOTNLinkList.get(n).getM_nID();
//    			data[1] = WDMLink.s_lOTNLinkList.get(n).getM_sName();
//    			data[2] = WDMLink.s_lOTNLinkList.get(n).getM_cFromNode().getM_sName();
//    			data[3] = WDMLink.s_lOTNLinkList.get(n).getM_cToNode().getM_sName();
//    			if(linkModel.getValueAt(29,0)==null)
//    				linkModel.removeRow(29);
//    			linkModel.insertRow(index, data);
//    			index++;
//    			int count=LinkRGroup.s_lRGroupList.size();
//    			int id=WDMLink.s_lOTNLinkList.get(n).getM_nID();
//    			Vector vector=new Vector();
//    			for(int i=0;i<count;i++){
//    				Layer layer=LinkRGroup.s_lRGroupList.get(i).getM_eLayer();
//    				if(layer.equals(Layer.OTN)){
//    					for(int j=0;j<LinkRGroup.s_lRGroupList.get(i).getM_lOTNLinkList().size();j++){
//    						if(id==LinkRGroup.s_lRGroupList.get(i).getM_lOTNLinkList().get(j).getM_nID()){
//    							--index;
//    							linkModel.removeRow(index);
//    							linkModel.insertRow(29, vector);
//    							break;
//    						}
//    					}
//    		}		    		
//    	}
//    	}
//    	}
//    	else if(linkLayer.getSelectedIndex()==3){
//    		for(int n = 0;n <SDHLink.s_lASONLinkList.size();n++){
//    			data[0] = SDHLink.s_lASONLinkList.get(n).getM_nID();
//    			data[1] = SDHLink.s_lASONLinkList.get(n).getM_sName();
//    			data[2] = SDHLink.s_lASONLinkList.get(n).getM_cFromNode().getM_sName();
//    			data[3] = SDHLink.s_lASONLinkList.get(n).getM_cToNode().getM_sName();
//    			if(linkModel.getValueAt(29,0)==null)
//    				linkModel.removeRow(29);
//    			linkModel.insertRow(index, data);
//    			index++;
//    			int count=LinkRGroup.s_lRGroupList.size();
//    			int id=SDHLink.s_lASONLinkList.get(n).getM_nID();
//    			Vector vector=new Vector();
//    			for(int i=0;i<count;i++){
//    				Layer layer=LinkRGroup.s_lRGroupList.get(i).getM_eLayer();
//    				if(layer.equals(Layer.ASON)){
//    					for(int j=0;j<LinkRGroup.s_lRGroupList.get(i).getM_lASONLinkList().size();j++){
//    						if(id==LinkRGroup.s_lRGroupList.get(i).getM_lASONLinkList().get(j).getM_nID()){
//    							--index;
//    							linkModel.removeRow(index);
//    							linkModel.insertRow(29, vector);
//    							break;
//    						}
//    					}
//    		}		    		
//    	}
//    	}
//    	}
//    	else if(linkLayer.getSelectedIndex()==4){
//    		for(int n = 0;n <SDHLink.s_lSDHLinkList.size();n++){
//    			data[0] = SDHLink.s_lSDHLinkList.get(n).getM_nID();
//    			data[1] = SDHLink.s_lSDHLinkList.get(n).getM_sName();
//    			data[2] = SDHLink.s_lSDHLinkList.get(n).getM_cFromNode().getM_sName();
//    			data[3] = SDHLink.s_lSDHLinkList.get(n).getM_cToNode().getM_sName();
//    			if(linkModel.getValueAt(29,0)==null)
//    				linkModel.removeRow(29);
//    			linkModel.insertRow(index, data);
//    			index++;
//    			int count=LinkRGroup.s_lRGroupList.size();
//    			int id=SDHLink.s_lSDHLinkList.get(n).getM_nID();
//    			Vector vector=new Vector();
//    			for(int i=0;i<count;i++){
//    				Layer layer=LinkRGroup.s_lRGroupList.get(i).getM_eLayer();
//    				if(layer.equals(Layer.SDH)){
//    					for(int j=0;j<LinkRGroup.s_lRGroupList.get(i).getM_lSDHLinkList().size();j++){
//    						if(id==LinkRGroup.s_lRGroupList.get(i).getM_lSDHLinkList().get(j).getM_nID()){
//    							--index;
//    							linkModel.removeRow(index);
//    							linkModel.insertRow(29, vector);
//    							break;
//    						}
//    					}
//    		}		    		
//    	}
//    	}
//    	}
    }
    private void removeTable(DefaultTableModel model){
		Vector data = new Vector();
		for(int i = 0 ;i < model.getRowCount();i++){
			    if(model.getValueAt(i, 0)!=null){
			    	model.removeRow(i);
			    	model.insertRow(0, data);
			    }   	
		 }
    }
    private int  getRow(DefaultTableModel model){//判断一个表已填内容的行数
    	int i;
    	for( i = 0;i < model.getRowCount() ; i++){
    		if(model.getValueAt(i, 0) == null)
    			return i;
    	}
    	return model.getRowCount();
    }
    private int  getidNum(DefaultTableModel model,int[] selectRow){//判断所选行中非空行个数
    	int n = 0;
    	for(int i = 0;i < selectRow.length ; i++){
    		if(model.getValueAt(selectRow[i], 0) != null)
    			n++;
    	}
    	return n;
    }
    private int[] getId(DefaultTableModel model,int[] selectRow){
    	int n = 0;
    	for(int i = 0;i < selectRow.length ; i++){
    		if(model.getValueAt(selectRow[i], 0) != null)
    			n++;
    	}
    	int[] id = new int[n];
    	int j = 0;
    	for(int i = 0;i < selectRow.length ; i++){
    		if(model.getValueAt(selectRow[i], 0) != null)
    			id[j++] = Integer.parseInt(String.valueOf(model.getValueAt(selectRow[i], 0)));
    	}
    	return id;
    }
//    public static void main(String[] args) {
//		 Dlg_SRLGSet a=new Dlg_SRLGSet();
//	 }
}
class NewSRLG extends JDialog{
	private JTextField srlgId = new JTextField(10);
	private JTextField srlgName = new JTextField(10);
	private static int id;
	private static boolean ready;
	private static String name;
	public NewSRLG(Dlg_SRLGSet owner){
		super(owner,"新建",true);
		this.setModal(true);
		ready=false;
		JButton confirm = new JButton("确定");
//		ringId.setDocument(new NumberLenghtLimitedDmt(20));//ringId只能输入数字

		
	    confirm.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){	
	    		if(srlgId.getText().isEmpty())
	    			JOptionPane.showMessageDialog(null, "请填写ID！");
	    		else if(srlgName.getText().isEmpty())
	    			JOptionPane.showMessageDialog(null, "请填写名称！");
	    		else if(!isInteger(String.valueOf(srlgId.getText())))
	    			JOptionPane.showMessageDialog(null, "ID为整数！");
	    		else if(isReapeated(Integer.parseInt(String.valueOf(srlgId.getText()))))
	    			JOptionPane.showMessageDialog(null, "该ID已存在");
	    		else{
		    		id = Integer.parseInt(String.valueOf(srlgId.getText()));
		    		name = srlgName.getText();
		    		ready=true;
		    		setVisible(false);	
	    		}
	    	}
	    });	
	    
	    JLabel title1 = new JLabel("SRLG ID");
	    JLabel title2 = new JLabel("SRLG 名称");
		title1.setFont(new   java.awt.Font("Dialog",  Font.BOLD,   15));
		title2.setFont(new   java.awt.Font("Dialog",  Font.BOLD,   15));
	    
	    JPanel pane = new JPanel();
	    pane.setBorder (BorderFactory.createRaisedBevelBorder());
	    pane.setBorder (BorderFactory.createEmptyBorder (5,5,5,5));
		Insets insert;
    	GridBagLayout mg = new GridBagLayout();
		GridBagConstraints mgc = new GridBagConstraints();			
		pane.setLayout(mg);
		mgc.fill = GridBagConstraints.NONE;
		
		mgc.gridwidth = 1;
		mgc.weightx = 1.0;
		mgc.weighty = 1.0;	
		mgc.gridheight = 1;
		pane.add(new JLabel(""),mgc);
		mgc.gridwidth = 2;
		pane.add(title1,mgc);
		mgc.gridwidth = 3;
		pane.add(srlgId,mgc);
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		pane.add(new JLabel(""),mgc);
		
		mgc.gridwidth = 1;
		pane.add(new JLabel(""),mgc);
		mgc.gridwidth = 2;
		pane.add(title2,mgc);
		mgc.gridwidth = 3;
		pane.add(srlgName,mgc);
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		pane.add(new JLabel(""),mgc);
		
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		pane.add(confirm,mgc);
		this.add(pane);
        setContentPane(pane);
        this.setSize(350,170);
     	this.setVisible(true);
		
		
	}
	
   public static boolean isInteger(String value) {
		  try {
		   Integer.parseInt(value);
		   return true;
		  } catch (NumberFormatException e) {
		   return false;
		  }
		 }
   public static boolean isReady(){
	   return ready;
   }
   public static int getsrlgId(){
		return id;
	}
	public static String getsrlgName(){
		return name;
	}
	public Boolean isReapeated(int id){
		for(int i = 0;i <  LinkRGroup.SRLGroupList.size();i++){
			if( LinkRGroup.SRLGroupList.get(i).getID() == id)
				return true;
		}
		return false;
	}
	 

}


