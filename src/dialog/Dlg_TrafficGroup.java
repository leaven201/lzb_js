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
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import data.LinkRGroup;
import data.DaZu;
import data.Traffic;
import data.TrafficGroup;

public class Dlg_TrafficGroup extends JDialog{

	private final Object[] columnNames = {"业务组id","所含业务"," "};//所在层隐藏
	private final String[] ringNames={"关联业务组名称","所含业务组"};//列名最好用final修饰
	private DefaultTableModel trafficModel;
	private DefaultTableModel trafficteamModel;
	private DefaultTableModel trafficgroupModel;
	private JTable trafficTable;
	private JTable trafficteamTable;
    private JTable trafficgroupTable;   
    private JComboBox trafficLayer;
    private static JLabel title1 ;
    private static int status = 2;//state = 0 为编辑，State  = 1 为新建
    private static String editName;//记录被编辑的业务组名
    private static List<Traffic> GroupedTraffic=new LinkedList<Traffic>();//已被分组的业务（用于业务组表）
    public Dlg_TrafficGroup(Frame owner){
    	super(owner,"关联业务组",true);
    	this.setSize(900,650);
    	
    	trafficLayer = new JComboBox();
//    	
//    	String[] s = {"全部","Fiber层","WDM层","OTN层","ASON层","SDH层"};
//    	for(int i = 0;i <s.length;i++){
//    		trafficLayer.addItem(s[i]);
//    	}
    	title1 = new JLabel("关联业务组成员列表");
    	trafficModel = new DefaultTableModel(null,columnNames);//环链路
    	trafficteamModel = new DefaultTableModel(null,columnNames);
    	trafficgroupModel = new DefaultTableModel(null,ringNames);
		
    	trafficModel.setRowCount(30);
		trafficteamModel.setRowCount(30);
		trafficgroupModel.setRowCount(30);
    	
		trafficTable = new JTable(trafficModel){
			   public boolean isCellEditable(int row, int column) { 
				    return false;
				    }
			   };//设置表内容不可编辑
		trafficteamTable = new JTable(trafficteamModel) {
			   public boolean isCellEditable(int row, int column) { 
					 return false;
					 }
			   };//设置表内容不可编辑			   
			   
	    trafficgroupTable = new JTable(trafficgroupModel){
			   public boolean isCellEditable(int row, int column) { 
					  return false;
					  }
				};//设置表内容不可编辑			
		trafficTable.getTableHeader().setReorderingAllowed(false);//列不可动
	    trafficteamTable.getTableHeader().setReorderingAllowed(false);//列不可动
	    trafficgroupTable.getTableHeader().setReorderingAllowed(false);//列不可动
	    trafficgroupTable();
	    removeTable(trafficModel);
	    refreshtrafficTable();
	    
	   
	    JScrollPane pane1 = new JScrollPane (trafficTable);
	    JScrollPane pane2 = new JScrollPane (trafficteamTable);
	    JScrollPane pane3 = new JScrollPane (trafficgroupTable); 
	    
	    trafficTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  //水平滚动条
	    trafficteamTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    trafficgroupTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	    
	    TableColumnModel   tcm   =   trafficTable.getColumnModel(); 
        TableColumn   tc   =   tcm.getColumn(2); //111
        tc.setResizable(false); 
        tc.setWidth(0); 
        tc.setPreferredWidth(0); 
        tc.setMaxWidth(0); 
        tc.setMinWidth(0); 
        trafficTable.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(0); 
        trafficTable.getTableHeader().getColumnModel().getColumn(2). setMinWidth(0); 
        
        tcm   =   trafficteamTable.getColumnModel();
        tc   =   tcm.getColumn(2); 
        tc.setWidth(0); 
        tc.setPreferredWidth(0); 
        tc.setMaxWidth(0); 
        tc.setMinWidth(0); 
        trafficteamTable.getTableHeader().getColumnModel().getColumn(2).setMaxWidth(0); 
        trafficteamTable.getTableHeader().getColumnModel().getColumn(2). setMinWidth(0); 
        
	    
	    

	    
	    final JButton in = new JButton("=>");
	    final JButton out = new JButton("<=");
	    final JButton confirm = new JButton("确定");
	    in.setEnabled(false);
	    out.setEnabled(false);
	    confirm.setEnabled(false);
	    
	   
		title1.setFont(new   java.awt.Font("Dialog",  Font.BOLD,   15)); 

	    final JButton set = new JButton("新建");
	    final JButton editor = new JButton("编辑");
	    final JButton delete = new JButton("删除");
	    editor.setEnabled(false);
	    delete.setEnabled(false);
	    final JButton close = new JButton("关闭");
	    close.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		setVisible(false);
	    		dispose();
	    	}
	    });
	    in.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){	
//	    		点"=>"键的操作
	    		Object[] data = new Object[3];
	    		for(int n = 0;n < trafficTable.getSelectedRows().length; n++){
    				if(trafficTable.getValueAt(trafficTable.getSelectedRows()[n], 0)!=null){
    				      data[0] = trafficTable.getValueAt(trafficTable.getSelectedRows()[n], 0);
    				      data[1] = trafficTable.getValueAt(trafficTable.getSelectedRows()[n], 1);
//    				      data[2] = trafficTable.getValueAt(trafficTable.getSelectedRows()[n], 2);
    				      //data[3] = trafficTable.getValueAt(trafficTable.getSelectedRows()[n], 3);
    				   //   data[4] = trafficTable.getValueAt(trafficTable.getSelectedRows()[n], 4);
    				   //   data[5] = trafficTable.getValueAt(trafficTable.getSelectedRows()[n], 5);
                          if(trafficteamModel.getValueAt(29,0)==null)
                        	  trafficteamModel.removeRow(29);
                          trafficteamModel.insertRow(getRow(trafficteamModel), data);
                          GroupedTraffic.add(Traffic.getTraffic(Integer.parseInt(String.valueOf(data[0]))));
    				      }
    				
    			}
	    		Vector data1 = new Vector();
	    		for(int j=0;j< columnNames.length;j++){
	    			for(int n = 0;n < trafficTable.getSelectedRows().length; n++){
	    				trafficModel.removeRow(trafficTable.getSelectedRows()[n]);
	    			}
//	    			保持有空白表
	    			if(trafficTable.getRowCount() < 30){
	    				int num = trafficTable.getRowCount();
	    				for(int x = 0;x < 30- num; x++)
	    					trafficModel.addRow(data1);
	    			} 
	    		}
	    	}
	    }); 
	    
	    /////////////////////////////
	    editor.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){	
//	    		点编辑键的操作 ,完成？
	    		set.setEnabled(false);
	    	    editor.setEnabled(false);
	     	    delete.setEnabled(false);
	     	   DaZu.getDaZu(String.valueOf(trafficgroupModel.getValueAt(trafficgroupTable.getSelectedRow(), 0)));
	    		Object data[] = new Object[2];
	    		for(int i = 1;i < DaZu.getDaZu(String.valueOf(trafficgroupModel.getValueAt(trafficgroupTable.getSelectedRow(), 0))).getRelatedGroupList().size();i++){
	    			data[0] = DaZu.getDaZu(String.valueOf(trafficgroupModel.getValueAt(trafficgroupTable.getSelectedRow(), 0))).getRelatedGroupList().get(i).getGroupId();
	    			data[1] = DaZu.getDaZu(String.valueOf(trafficgroupModel.getValueAt(trafficgroupTable.getSelectedRow(), 0))).getRelatedGroupList().get(i).getTrafficList();
//	    			data[2] = TrafficGroup.getTrafficGroup(String.valueOf(trafficgroupModel.getValueAt(trafficgroupTable.getSelectedRow(), 0))).getM_lTrafficList().get(i).getM_nYear();
	    		//	data[2] = TrafficGroup.getTrafficGroup(String.valueOf(trafficgroupModel.getValueAt(trafficgroupTable.getSelectedRow(), 0))).getM_lTrafficList().get(i).getFromNode().getM_sName();
	    		//	data[3] = TrafficGroup.getTrafficGroup(String.valueOf(trafficgroupModel.getValueAt(trafficgroupTable.getSelectedRow(), 0))).getM_lTrafficList().get(i).getToNode().getM_sName();
	    		//	data[5] = TrafficGroup.getTrafficGroup(String.valueOf(trafficgroupModel.getValueAt(trafficgroupTable.getSelectedRow(), 0))).getM_lTrafficList().get(i).getM_eLayer();
	    			if(trafficteamModel.getValueAt(29,0)==null)
	    				trafficteamModel.removeRow(29);
	    			trafficteamModel.insertRow(0, data);
	    			
	    		}
	    		status = 0;  
	    		editName =String.valueOf(trafficgroupModel.getValueAt(trafficgroupTable.getSelectedRow(), 0));
//	    		trafficgroupModel.removeRow(trafficgroupTable.getSelectedRow());
//	    		Vector data1 = new Vector();
//	    		if(trafficgroupModel.getRowCount() < 30){
//    				int num = trafficgroupModel.getRowCount();
//    				for(int x = 0;x < 30- num; x++)
//    					trafficgroupModel.addRow(data1);
//    			}  "<html> 资 <br> 源 <br> 利 <br> 用 <br> 率<html> "
	    		trafficgroupTable();
    			refreshtrafficTable();
	    		title1.setText(editName+"业务组成员列表");
	    		in.setEnabled(true);
	    		out.setEnabled(true);
	    		confirm.setEnabled(true);
	    	}
	    });
	    
	    
	    
	    out.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){	
//	    		点"<="键的操作
	    
	    		Object[] data = new Object[2];
	    		for(int n = 0;n < trafficteamTable.getSelectedRows().length; n++){
    				if(trafficteamTable.getValueAt(trafficteamTable.getSelectedRows()[n], 0)!=null){
    				      data[0] = trafficteamTable.getValueAt(trafficteamTable.getSelectedRows()[n], 0);
    				      data[1] = trafficteamTable.getValueAt(trafficteamTable.getSelectedRows()[n], 1);
//    				      data[2] = trafficteamTable.getValueAt(trafficteamTable.getSelectedRows()[n], 2);
    				     // data[3] = trafficteamTable.getValueAt(trafficteamTable.getSelectedRows()[n], 3);
    				    //  data[4] = trafficteamTable.getValueAt(trafficteamTable.getSelectedRows()[n], 4);
    				    //  data[5] = trafficteamTable.getValueAt(trafficteamTable.getSelectedRows()[n], 5);
    				      if(trafficModel.getValueAt(29,0)==null)
    		    				 trafficModel.removeRow(29);
    		    		  trafficModel.insertRow(getRow(trafficModel), data);
    		    		  GroupedTraffic.remove((Traffic.getTraffic(Integer.parseInt(String.valueOf(data[0])))));
    				}
    				
    			}
	    		
	    		Vector data1 = new Vector();
	    		for(int j=0;j< columnNames.length;j++){
	    			for(int n = 0;n < trafficteamTable.getSelectedRows().length; n++){
	    				trafficteamModel.removeRow(trafficteamTable.getSelectedRows()[n]);
	    			}
//	    			保持有空白表
	    			if(trafficteamModel.getRowCount() < 30){
	    				int num = trafficteamModel.getRowCount();
	    				for(int x = 0;x < 30- num; x++)
	    					trafficteamModel.addRow(data1);
	    			} 
	    		}
	    	}
	    }); 
	    delete.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){	
//	    		点删除键的操作,同时在前台和链中删除  完成？
	    	    editor.setEnabled(false);
	     	    delete.setEnabled(false);
	    		Vector data = new Vector();
	    		
	    		for(int i = 0;i<getName(trafficgroupModel,trafficgroupTable.getSelectedRows()).length;i++){
	    			DaZu it = DaZu.getDaZu(getName(trafficgroupModel,trafficgroupTable.getSelectedRows())[i]);
	    			for(int j = 0;j <it.getRelatedGroupList().size(); ){
	    				TrafficGroup a =it.getRelatedGroupList().get(0);
	    				a.setRelatedId(Integer.MAX_VALUE-99);
	    				DaZu g=null;
//	    				a.setM_cGroup(g);
	    				it.delTrafficGroup(a);
	    			}
	    			DaZu.allGroupList.remove(it);
	    		}
	    		for(int j=0;j< ringNames.length;j++){
	    			for(int n = 0;n < trafficgroupTable.getSelectedRows().length; n++){
	    				trafficgroupModel.removeRow(trafficgroupTable.getSelectedRows()[n]);
	    			}
//	    			保持有空白表
	    			if(trafficgroupModel.getRowCount() < 30){
	    				int num = trafficgroupModel.getRowCount();
	    				for(int x = 0;x < 30- num; x++)
	    					trafficgroupModel.addRow(data);
	    			} 
	    		}
	    		trafficgroupTable();
    			refreshtrafficTable();
	    	}
	    });
	    confirm.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){	
//	    		点确定键的操作,表和链都有操作  完成？
	    		if(trafficteamModel.getValueAt(0, 0)!=null){	
	    			if(status == 1){//新建
	    				DaZu rg=new DaZu(NewTrafficGroup.getgroupName());
//	    				System.out.println(TrafficGroup.s_lTrafficGroup.size());
	 	    			for(int i = 0;i < getRow(trafficteamModel);i++ ){//将关联的业务组放到一个大组中
	 	    				TrafficGroup a=TrafficGroup.gettrafficGroupById(Integer.parseInt(String.valueOf(trafficteamModel.getValueAt(i, 0)))); 	    			    
	 	    			    rg.addGroup(a);
	 	    			  //  a.setM_cGroup(tg.getM_sName());
	 	    			}	
	 	    			//relatedId设置
	 	    			rg.setRelatedId();
	 	    			
	 	             }  
	    			else if(status == 0){//编辑
	    				DaZu it =DaZu.getDaZu(editName);
	    				for(int i = 0;i< it.getRelatedGroupList().size();){
	    					TrafficGroup  a = DaZu.getDaZu(editName).getRelatedGroupList().get(0);
	    					it.delTrafficGroup(a);
//	    					TrafficGroup g=null;
//		    				a.setM_cGroup(g);
//	    					if(TrafficGroup.getTrafficGroup(editName).getM_lTrafficList().size()==0)
//	    						break;
	    				}
	    				for(int i = 0;i < getRow(trafficteamModel);i++){
	    					it.addTrafficGroup(TrafficGroup.gettrafficGroupById(Integer.parseInt(String.valueOf(trafficteamModel.getValueAt(i, 0)))));
	    					//Traffic.getTraffic(Integer.parseInt(String.valueOf(trafficteamModel.getValueAt(i, 0)))).setM_cGroup(editName);
	    				}
	    				it.setRelatedId();
	    			}
	    			trafficgroupTable();
	    			refreshtrafficTable();
 	    			
 	    			removeTable(trafficteamModel);
 	    			
 	    			title1.setText("业务组成员列表");
 	    			in.setEnabled(false);
		    		out.setEnabled(false);
		    		confirm.setEnabled(false);
		    		set.setEnabled(true);
	 	    	 }
	    		else 
	    			JOptionPane.showMessageDialog(null, "请选择业务！");
	    	}
	    			
	    }); 
	    set.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){	
	    		status = 1; 
	    		new NewTrafficGroup(Dlg_TrafficGroup.this);
	    		if(NewTrafficGroup.isReady()){
		    		in.setEnabled(true);
		    		out.setEnabled(true);
		    		confirm.setEnabled(true);
		    		title1.setText(NewTrafficGroup.getgroupName()+"\n"+"业务组成员列表");
		    		set.setEnabled(false);
		    	    editor.setEnabled(false);
		     	    delete.setEnabled(false); 
	    		}	    		
	    	}
	    });
	    trafficgroupTable.addMouseListener(new MouseAdapter() {
	          @Override
	          public void mouseClicked(MouseEvent e) {
	          if(trafficteamTable.getValueAt(0, 0)==null&&trafficgroupTable.getValueAt(trafficgroupTable.getSelectedRow(),0)!=null&&!in.isEnabled()){//用ID获得SRG判断getNature为false才可
	        	  editor.setEnabled(true);
	       	      delete.setEnabled(true);
	          }
	          else{
	         	  editor.setEnabled(false);
		   	      delete.setEnabled(false);
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
	    p2.setBorder (BorderFactory.createTitledBorder ("业务组表"));
	    
	    TableColumnModel tcm0 =  trafficgroupTable.getColumnModel();   
	    tcm0.getColumn(0).setPreferredWidth(155);
	    tcm0.getColumn(1).setPreferredWidth(710);
	    TableColumnModel tcm1 = trafficTable.getColumnModel();   
	    tcm1.getColumn(0).setPreferredWidth(50);
	    tcm1.getColumn(1).setPreferredWidth(400);
	    tcm1.getColumn(2).setPreferredWidth(400);
//	    tcm1.getColumn(3).setPreferredWidth(92);
	   // tcm1.getColumn(4).setPreferredWidth(90);
	    
	    TableColumnModel tcm2 = trafficteamTable.getColumnModel();   
	    tcm2.getColumn(0).setPreferredWidth(50);
	    tcm2.getColumn(1).setPreferredWidth(400);
	    tcm2.getColumn(2).setPreferredWidth(400);
//	    tcm2.getColumn(3).setPreferredWidth(85);
	   // tcm2.getColumn(4).setPreferredWidth(80);
	    
	    Insets insert1;
	    GridBagLayout mg1 = new GridBagLayout();
		GridBagConstraints mgc1 = new GridBagConstraints();
		p2.setLayout(mg1);
	    

		
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
		mgc1.gridwidth = GridBagConstraints.REMAINDER;
		p2.add(close,mgc1);
		 
		
	    JPanel p1 = new JPanel();
	    p1.setBorder (BorderFactory.createTitledBorder ("业务列表"));

		
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
//		p1.add(trafficLayer,mgc);
		p1.add(new JLabel(""),mgc);
		
		

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
		insert = new Insets(0,5,0,40);
		mgc.insets = insert;
		p1.add(pane1,mgc);
		
		mgc.gridwidth = 2;
        p1.add(p);
		
		mgc.gridwidth =  GridBagConstraints.REMAINDER;
		mgc.weightx = 11.0;
		mgc.gridheight =7;
		mgc.weighty = 7.0;
		insert = new Insets(0,55,0,50);
		mgc.insets = insert;
		p1.add(pane2,mgc);

		
		p3.add(p1);
		p3.add(p2);
		this.add(p3);
        setContentPane(p3);
     	this.setVisible(true); 	
    	
    }
    private void refreshtrafficTable(){
    	removeTable(trafficModel);
        Object data[] = new Object[2];
        int index = 0;
     //   if(trafficLayer.getSelectedIndex()==0){
        	
        	for(int i = 1;i <TrafficGroup.grouptrafficGroupList.size();i++){
        		data[0] = TrafficGroup.grouptrafficGroupList.get(i).getGroupId();
        		data[1] = TrafficGroup.grouptrafficGroupList.get(i).toString1();
//        		data[2] = Traffic.trafficList.get(i).getM_nYear();
//			    data[2] = Traffic.trafficList.get(i).getFromNode().getM_sName();
//			    data[3] = Traffic.trafficList.get(i).getToNode().getM_sName();
			 //   data[5] = Traffic.trafficList.get(i).getM_eLayer();
			    if(!GroupedTraffic.contains(TrafficGroup.grouptrafficGroupList.get(i))){
			    	if(trafficModel.getValueAt(29,0)==null)
				    	trafficModel.removeRow(29);
				    trafficModel.insertRow(index++,data);
	//		    }
			    
        	}  
        
        }
//        else if(trafficLayer.getSelectedIndex()==1){
//        	for(int i = 0;i <Traffic.trafficList.size();i++){
//        		data[0] = Traffic.trafficList.get(i).getId();
//        		data[1] = Traffic.trafficList.get(i).getName();
//        		//data[2] = Traffic.trafficList.get(i).getM_nYear();
//			    data[2] = Traffic.trafficList.get(i).getFromNode().getM_sName();
//			    data[3] = Traffic.trafficList.get(i).getToNode().getM_sName();
//			//    data[5] = Traffic.trafficList.get(i).getM_eLayer();
//			    if(!GroupedTraffic.contains(Traffic.trafficList.get(i))){
//			    	if(trafficModel.getValueAt(29,0)==null)
//				    	trafficModel.removeRow(29);
//				    trafficModel.insertRow(index++,data);
//			    }
//        	}
//        }
//        else if(trafficLayer.getSelectedIndex()==2){
//        	for(int i = 0;i <Traffic.trafficList.size();i++){
//        		data[0] = Traffic.trafficList.get(i).getId();
//        		data[1] = Traffic.trafficList.get(i).getName();
//        //		data[2] = Traffic.trafficList.get(i).getM_nYear();
//			    data[2] = Traffic.trafficList.get(i).getFromNode().getM_sName();
//			    data[3] = Traffic.trafficList.get(i).getToNode().getM_sName();
//		//	    data[5] = Traffic.trafficList.get(i).getM_eLayer();
//			    if(!GroupedTraffic.contains(Traffic.trafficList.get(i))){
//			    	if(trafficModel.getValueAt(29,0)==null)
//				    	trafficModel.removeRow(29);
//				    trafficModel.insertRow(index++,data);
//			    }
//        	}
//        }
//        else if(trafficLayer.getSelectedIndex()==3){
//        	for(int i = 0;i <Traffic.s_lOTNTrafficList.size();i++){
//        		data[0] = Traffic.s_lOTNTrafficList.get(i).getM_nID();
//        		data[1] = Traffic.s_lOTNTrafficList.get(i).getM_sName();
//        		data[2] = Traffic.s_lOTNTrafficList.get(i).getM_nYear();
//			    data[3] = Traffic.s_lOTNTrafficList.get(i).getM_nFromNode().getM_sName();
//			    data[4] = Traffic.s_lOTNTrafficList.get(i).getM_nToNode().getM_sName();
//			    data[5] = Traffic.s_lOTNTrafficList.get(i).getM_eLayer();
//			    if(!GroupedTraffic.contains(Traffic.s_lOTNTrafficList.get(i))){
//			    	if(trafficModel.getValueAt(29,0)==null)
//				    	trafficModel.removeRow(29);
//				    trafficModel.insertRow(index++,data);
//			    }
//        	}
//        }
//        else if(trafficLayer.getSelectedIndex()==4){
//        	for(int i = 0;i <Traffic.s_lASONTrafficList.size();i++){
//        		data[0] = Traffic.s_lASONTrafficList.get(i).getM_nID();
//        		data[1] = Traffic.s_lASONTrafficList.get(i).getM_sName();
//        		data[2] = Traffic.s_lASONTrafficList.get(i).getM_nYear();
//			    data[3] = Traffic.s_lASONTrafficList.get(i).getM_nFromNode().getM_sName();
//			    data[4] = Traffic.s_lASONTrafficList.get(i).getM_nToNode().getM_sName();
//			    data[5] = Traffic.s_lASONTrafficList.get(i).getM_eLayer();
//			    if(!GroupedTraffic.contains(Traffic.s_lASONTrafficList.get(i))){
//			    	if(trafficModel.getValueAt(29,0)==null)
//				    	trafficModel.removeRow(29);
//				    trafficModel.insertRow(index++,data);
//			    }
//        	}
//        }
//        else if(trafficLayer.getSelectedIndex()==5){
//        	for(int i = 0;i <Traffic.s_lSDHTrafficList.size();i++){
//        		data[0] = Traffic.s_lSDHTrafficList.get(i).getM_nID();
//        		data[1] = Traffic.s_lSDHTrafficList.get(i).getM_sName();
//        		data[2] = Traffic.s_lSDHTrafficList.get(i).getM_nYear();
//			    data[3] = Traffic.s_lSDHTrafficList.get(i).getM_nFromNode().getM_sName();
//			    data[4] = Traffic.s_lSDHTrafficList.get(i).getM_nToNode().getM_sName();
//			    data[5] = Traffic.s_lSDHTrafficList.get(i).getM_eLayer();
//			    if(!GroupedTraffic.contains(Traffic.s_lSDHTrafficList.get(i))){
//			    	if(trafficModel.getValueAt(29,0)==null)
//				    	trafficModel.removeRow(29);
//				    trafficModel.insertRow(index++,data);
//			    }
//        	}
//        }
        
    	
    }
    private void trafficgroupTable(){
    	removeTable(trafficgroupModel);
    	GroupedTraffic.clear();
    	Object[] data = new Object[2];
		for(int n = 0;n <DaZu.allGroupList.size();n++){
			data[0] = DaZu.allGroupList.get(n).getName();
			StringBuffer buffer=new StringBuffer();
			buffer.append(DaZu.allGroupList.get(n).toString1());
			for(int i = 0; i < DaZu.allGroupList.get(n).relatedGroupList.size();i++){
				// buffer.append(DaZu.allGroupList.get(n).relatedGroupList.get(i).toString1());
				 
				 GroupedTraffic.addAll(DaZu.allGroupList.get(n).relatedGroupList.get(i).getTrafficList());//111
				 if(i != DaZu.allGroupList.get(n).relatedGroupList.size()-1)
					 buffer.append(" ; ");
			}
			data[1] = buffer;
			if(trafficgroupModel.getValueAt(29,0)==null)
				 trafficgroupModel.removeRow(29);
			 trafficgroupModel.insertRow(n,data);
			
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
    private String[] getName(DefaultTableModel model,int[] selectRow){
    	int n = 0;
    	for(int i = 0;i < selectRow.length ; i++){
    		if(model.getValueAt(selectRow[i], 0) != null)
    			n++;
    	}
    	String[] name = new String[n];
    	int j = 0;
    	for(int i = 0;i < selectRow.length ; i++){
    		if(model.getValueAt(selectRow[i], 0) != null)
    			name[j++] = String.valueOf(model.getValueAt(selectRow[i], 0));
    	}
    	return name;
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

}
class NewTrafficGroup extends JDialog{
	private JTextField groupId = new JTextField(6);
	private JTextField groupName = new JTextField(10);
	private static int id;
	private static boolean ready;
	private static String name;
	public NewTrafficGroup(Dlg_TrafficGroup owner){
	     super(owner,"新建",true);
		this.setModal(true);
		ready = false;
		
		JButton confirm = new JButton("确定");

		
	    confirm.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){	
	    		if(groupName.getText().isEmpty())
	    			JOptionPane.showMessageDialog(null, "请填写业务组名称！");
	    		else if(isReapeated())
	    			JOptionPane.showMessageDialog(null, "该业务组名称已存在！");
	    		else{
		    		name = groupName.getText();
		    		ready = true;
		    		setVisible(false);	
	    		}
	    	}
	    });	
	    JLabel title1 = new JLabel("业务组名称");
		title1.setFont(new   java.awt.Font("Dialog",  Font.BOLD,   15));
	    
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
		pane.add(groupName,mgc);
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		pane.add(new JLabel(""),mgc);
		
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		pane.add(confirm,mgc);
		this.add(pane);
        setContentPane(pane);
        this.setSize(350,170);
     	this.setVisible(true);
		
	}
    public static boolean isReady(){
 	   return ready;
    }
	public static String getgroupName(){
		return name;
	}
	public Boolean isReapeated(){
		int i ;
		for( i = 0;i < DaZu.allGroupList.size();i++){
			if(DaZu.allGroupList.get(i).getName().equals(groupName.getText()))
				return true;
		}
		return false;
	}
	

}

