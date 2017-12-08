package dialog;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import twaver.Element;
import twaver.Node;
import twaver.TDataBox;
import twaver.TWaverConst;
import twaver.chart.PieChart;
import data.FiberLink;
import data.LinkRGroup;
import data.Network;
import data.Traffic;
import datastructure.UIFiberLink;
import enums.Layer;
import enums.TrafficLevel;
import survivance.Evaluation;
//import evaluation.Evaluation;

public class Dlg_SRLGPoll extends JFrame{
	private DefaultTableModel srgModel;
	private DefaultTableModel trafficModel;
	private JTable srgTable;
	private JTable trafficTable;
	private TDataBox box = new TDataBox();
	private PieChart pie = new PieChart(box);
	private final Object[] srgHeader = {"SRLG ID","SRLG名称","所含链路"};
	private final String[] trafficHeader={"ID","受影响业务名称","首节点 ","末节点","保护等级","受影响程度","原工作路由","原保护路由","故障后工作路由","故障后保护路由"};//列名最好用final修饰
	private List<Traffic> tra = new LinkedList<Traffic>();  
	private LinkRGroup thesrg ; 
	private Element aa = new Node();   
	private Element bb = new Node();   
	private Element cc = new Node();
	private Element dd = new Node();
	public Dlg_SRLGPoll(Frame owner){
		setTitle("SRLG组单断");
		ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/resource/titlexiao.png"));
		this.setIconImage(imageIcon.getImage());
//		super(owner,"SRLG组循环",true);
    	this.setSize(1100,600);
    	Toolkit   tl=Toolkit.getDefaultToolkit();   
		Dimension   screenSize=tl.getScreenSize();   
	    this.setLocation((int)screenSize.getWidth()/2-(int)this.getSize().getWidth()/2, (int)screenSize.getHeight()/2-(int)this.getSize().getHeight()/2);
	
//	    srgModel = new DefaultTableModel(null,srgHeader);
		trafficModel = new DefaultTableModel(null,trafficHeader);
		
		
		
		int num = 0;
	    for(int n = 0;n <LinkRGroup.SRLGroupList.size();n++){
	    	if(LinkRGroup.SRLGroupList.get(n).getBelongLayer().equals(Layer.Fiber)){
	    		++num;
			}
		}
	    int[] count=new int[num];
	    int m=0;
	    for(int n = 0;n <LinkRGroup.SRLGroupList.size();n++){
	    	if(LinkRGroup.SRLGroupList.get(n).getBelongLayer().equals(Layer.Fiber)){
	    		count[m]=n;
	    		++m;
			}
		}
		Object srlgData[][] = new Object[num][3];
		for(int n = 0;n <num;n++){
			srlgData[n][0] = LinkRGroup.SRLGroupList.get(count[n]).getID();
			srlgData[n][1] = LinkRGroup.SRLGroupList.get(count[n]).getName();
			StringBuffer buffer=new StringBuffer();		
			for(int i  = 0; i < LinkRGroup.SRLGroupList.get(count[n]).getSRLGFiberLinkList().size();i++){
				buffer.append(LinkRGroup.SRLGroupList.get(count[n]).getSRLGFiberLinkList().get(i).getName());
				 if(i != LinkRGroup.SRLGroupList.get(count[n]).getSRLGFiberLinkList().size()-1)
					 buffer.append(" ; ");
				}	
				srlgData[n][2] = buffer;
		}
		
		srgModel = new DefaultTableModel(srlgData,srgHeader);
		
		srgTable = new JTable(srgModel){
			public boolean isCellEditable(int row, int column) { 
				return false;
			}
		};
	    trafficTable = new JTable( trafficModel){
			public boolean isCellEditable(int row, int column) { 
				return false;
			}
		};
		
		trafficTable.getTableHeader().setReorderingAllowed(false); 
		srgTable.getTableHeader().setReorderingAllowed(false); //列不可动
		JScrollPane pane1 = new JScrollPane (srgTable);
		JScrollPane pane2 = new JScrollPane (trafficTable);
		pane1.setRowHeaderView(new RowHeaderTable(srgTable,40)); 


		
		srgTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  //水平滚动条
		trafficTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  //水平滚动条
		
		TableColumnModel tcm = trafficTable.getColumnModel();   
	    tcm.getColumn(7).setPreferredWidth(200); 
	    tcm.getColumn(8).setPreferredWidth(200);
	    tcm.getColumn(9).setPreferredWidth(200); 
	    tcm.getColumn(6).setPreferredWidth(200);
	    
	     tcm = srgTable.getColumnModel();   
	    tcm.getColumn(0).setPreferredWidth(80);
	    tcm.getColumn(1).setPreferredWidth(80); 
	    tcm.getColumn(2).setPreferredWidth(200);
	    
	    
//	    sortManager(srgTable);
//	    sortManager(trafficTable);
	    
	   
	    JButton close  = new JButton(" 关  闭  ");
	    JButton Browse = new JButton("单断循环");
	    close.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){	
	    		
//	    		recoveryLinkListcol(thesrg);
	    		setVisible(false);
	    		dispose();//释放窗体所占内存资源?
	    	}
	    });
	    setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new   WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
//				recoveryLinkListcol(thesrg);
	    		setVisible(false);
	    		dispose();
			}	
        });	
		
		
		
	    Browse.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){	  		
	    	/*
	    	 * 统计结果窗口
	    	 */
	    		new Dlg_SurvivanceResult(Dlg_SRLGPoll.this,4);
	    	}
	    });
	    
	    srgTable.addMouseListener(new MouseAdapter() {
	          @Override
	          public void mouseClicked(MouseEvent e) {
	        
	        	  int id = Integer.parseInt(String.valueOf(srgModel.getValueAt(srgTable.getSelectedRow(), 0)));
	        	  
//	        	  if(thesrg != null){
//	        		  recoveryLinkListcol(thesrg);
//	        	  }
	        	 
	        	  
	        	  thesrg = LinkRGroup.getLinkRGroup(id);
	        	  for(int i = 0 ;i < trafficModel.getRowCount();){
	        		  trafficModel.removeRow(0);   	
			       }	 
	        	  
//	        	  setLinkListcol(thesrg);
	        	  tra = Evaluation.SRLGEvaluation(thesrg);
	        	  System.out.println("chuanruyewu"+tra.size());
	        	  System.out.println("bingtu"+Evaluation.suneffpercent);
	        	  aa.putChartValue(Evaluation.skeeppercent);   
	              bb.putChartValue(Evaluation.sdownpercent);   
	              cc.putChartValue(Evaluation.scutoffpercent);
	              dd.putChartValue(Evaluation.suneffpercent);
	        	  
	        	 
	        	  Object data1[] = new Object[10];
	              
	        	  
				  String from , to,link;
	              
	        	  for(int i = 0;i < tra.size();i++){
	        		  
	        		  data1[0] = tra.get(i).getId();
	        		  data1[1] = tra.get(i).getFromNode().getName()+"-"+ tra.get(i).getToNode().getName();
	        		  data1[2] = tra.get(i).getFromNode().getName();
	        		  data1[3] = tra.get(i).getToNode().getName();
//	        		  data1[4] = "wdm";
	        		  data1[4] = tra.get(i).getProtectLevel().toString();
	        		  if(tra.get(i).getProtectLevel().equals(TrafficLevel.NORMAL11)){
	        			  data1[4] = "普通1+1";
	        		  }
	        		  else  if(tra.get(i).getProtectLevel().equals(TrafficLevel.PERMANENT11)){
	        			  data1[4] = "永久1+1";
	        		  }
	        		  else  if(tra.get(i).getProtectLevel().equals(TrafficLevel.RESTORATION)){
	        			  data1[4] = "恢复";
	        		  }
	        		  else  if(tra.get(i).getProtectLevel().equals(TrafficLevel.NONPROTECT)){
	        			  data1[4] = "无保护";
	        		  }
	        		  StringBuffer buffer=new StringBuffer();
					  StringBuffer buffer1=new StringBuffer();
					  StringBuffer buffer2=new StringBuffer();
					  StringBuffer buffer3=new StringBuffer();
	        		  
	        		  if(tra.get(i).getWorkRoute()!=null){

						    from = tra.get(i).getFromNode().getM_sName();						   
						    for(int j = 0;j <tra.get(i).getWorkRoute().getFiberLinkList().size();j++){
						    	if(j == 0)	 buffer.append(from);
						     link = tra.get(i).getWorkRoute().getFiberLinkList().get(j).getName();
						     if(from.equals(tra.get(i).getWorkRoute().getFiberLinkList().get(j).getFromNode().getName()))
						      to = tra.get(i).getWorkRoute().getFiberLinkList().get(j).getToNode().getName();
						     else
						      to = tra.get(i).getWorkRoute().getFiberLinkList().get(j).getFromNode().getName();
						     buffer.append("-<");
						     buffer.append(link);
						     buffer.append(">-");
						     buffer.append(to);
						     from = to;
						    }
						    data1[6] = buffer.toString();
						    
						    from = tra.get(i).getFromNode().getName();
						    if(tra.get(i).getProtectRoute()!=null) {
							    for(int j = 0;j <tra.get(i).getProtectRoute().getFiberLinkList().size();j++){
							    	if(j == 0)	buffer1.append(from);
							     link = tra.get(i).getProtectRoute().getFiberLinkList().get(j).getName();
							     if(from.equals(tra.get(i).getProtectRoute().getFiberLinkList().get(j).getFromNode().getName()))
							      to = tra.get(i).getProtectRoute().getFiberLinkList().get(j).getToNode().getName();
							     else
							      to = tra.get(i).getProtectRoute().getFiberLinkList().get(j).getFromNode().getName();
							     buffer1.append("-<");
							     buffer1.append(link);
							     buffer1.append(">-");
							     buffer1.append(to);
							     from = to;
							    }
						    }
						    data1[7] = buffer1.toString();
						    
						    from = tra.get(i).getFromNode().getName();
						    if(tra.get(i).getResumeRoute()!=null){
						    	
							    for(int j = 0;j <tra.get(i).getResumeRoute().getFiberLinkList().size();j++){
							    	if(j == 0)	buffer2.append(from);
							    	link = tra.get(i).getResumeRoute().getFiberLinkList().get(j).getName();
							     if(from.equals(tra.get(i).getResumeRoute().getFiberLinkList().get(j).getFromNode().getName()))
							      to = tra.get(i).getResumeRoute().getFiberLinkList().get(j).getFromNode().getName();
							     else
							      to = tra.get(i).getResumeRoute().getFiberLinkList().get(j).getFromNode().getName();
							     buffer2.append("-<");
							     buffer2.append(link);
							     buffer2.append(">-");
							     buffer2.append(to);
							     from = to;
							    }
						    }
						    
						    data1[8] = buffer2.toString();
						    
						    
						    from = tra.get(i).getFromNode().getName();
						    if(tra.get(i).getResumeRoutePro()!=null){
						    	
								    for(int j = 0;j <tra.get(i).getResumeRoutePro().getFiberLinkList().size();j++){
								    	if(j == 0)	 buffer3.append(from);
								    	link = tra.get(i).getResumeRoutePro().getFiberLinkList().get(j).getName();
								     if(from.equals(tra.get(i).getResumeRoutePro().getFiberLinkList().get(j).getFromNode().getName()))
								      to = tra.get(i).getResumeRoutePro().getFiberLinkList().get(j).getFromNode().getName();
								     else
								      to = tra.get(i).getResumeRoutePro().getFiberLinkList().get(j).getFromNode().getName();
								     buffer3.append("-<");
								     buffer3.append(link);
								     buffer3.append(">-");
								     buffer3.append(to);
								     from = to;
								    }
						    }
						   
						    data1[9] = buffer3.toString();
						   
	        		  }
	        		       		  
	        		  Traffic tra1= tra.get(i);
		              		switch(tra1.getProtectLevel()){
		                  	case PERMANENT11:
		                  		if(tra1.getResumeRoute()==null||tra1.getResumeRoute().getFiberLinkList().size()==0)
		                  		{
		                  			data1[5] = "中断";
		                  			
		                  		}  
		                  		else if((tra1.getResumeRoute()!=null&&tra1.getResumeRoute().getFiberLinkList().size()!=0)&&(tra1.getResumeRoutePro()==null||tra1.getResumeRoutePro().getFiberLinkList().size()==0))
		                  			data1[5] = "降级";
		                  		else if((tra1.getResumeRoute()!=null&&tra1.getResumeRoute().getFiberLinkList().size()!=0)&&(tra1.getResumeRoutePro()!=null&&tra1.getResumeRoutePro().getFiberLinkList().size()!=0))
		                  			data1[5] = "保持";
		                  		break;
		                 	case NONPROTECT:
		                 		if(tra1.getResumeRoute()==null||tra1.getResumeRoute().getFiberLinkList().size()==0)
		                 		{
		                 			data1[5] = "中断";
		                 			
		                 		}
		                 		break;
		                  	case NORMAL11 :
		                  		if(tra1.getResumeRoute()==null||tra1.getResumeRoute().getFiberLinkList().size()==0)
		                  		{
		                  			data1[5] = "中断";
		                  			
		                  		}
		                  		else if((tra1.getResumeRoute()!=null&&tra1.getResumeRoute().getFiberLinkList().size()!=0)&&(tra1.getResumeRoutePro()==null||tra1.getResumeRoutePro().getFiberLinkList().size()==0))
		                  			data1[5] = "降级";
		                  		else if((tra1.getResumeRoute()!=null&&tra1.getResumeRoute().getFiberLinkList().size()!=0)&&(tra1.getResumeRoutePro()!=null&&tra1.getResumeRoutePro().getFiberLinkList().size()!=0))
		                  			data1[5] = "保持";
		                  		break;
		                	case RESTORATION:
		                  		if(tra1.getResumeRoute()==null||tra1.getResumeRoute().getFiberLinkList().size()==0)
		                  		{
		                  			data1[5] = "中断";
		                  			
		                  		}
		                  		else if(tra1.getResumeRoute()!=null&&tra1.getResumeRoute().getFiberLinkList().size()!=0)
		                  			data1[5] = "保持";
		                  		break;
							default:
								break;
		                  	}
		              		
	        		trafficModel.addRow(data1);
		              
		              	}
	        	  	        		  
	        	  Evaluation.clearRsmRoute(tra);
	        	  }
	        		  
	          
	   });
	    
	    aa.setName("保持");   
        bb.setName("降级");   
        cc.setName("中断");
        dd.setName("无影响");
        
       
        pie.setVisible(true);
        
        //图示付色   
        aa.putChartColor(Color.green);   
        bb.putChartColor(Color.blue);   
        cc.putChartColor(Color.RED);   
        dd.putChartColor(Color.yellow); 
//        //图示的比例值   
//        aa.putChartValue(0.25);   
//        bb.putChartValue(0.25);   
//        cc.putChartValue(0.25);   
           
        box.addElement(aa);   
        box.addElement(bb);   
        box.addElement(cc); 
        box.addElement(dd); 
	    
	    JPanel piepanel = new JPanel(new BorderLayout());
	    piepanel.add(pie, BorderLayout.CENTER);
	    
	    JPanel p1 = new JPanel(new BorderLayout());
	    p1.setBorder (BorderFactory.createTitledBorder ("SRLG列表"));
	    p1.add(pane1, BorderLayout.CENTER);
	    
	    JPanel p2 = new JPanel(new BorderLayout());
	    p2.setBorder (BorderFactory.createTitledBorder ("受影响业务列表"));
	    p2.add(pane2, BorderLayout.CENTER);
	    
	    JPanel p4 = new JPanel();
	    GridBagLayout mg = new GridBagLayout();
	    GridBagConstraints mgc = new GridBagConstraints();		
		p4.setLayout(mg);
		mgc.fill = GridBagConstraints.NONE;
		
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		mgc.weightx = 1.0;
		mgc.weighty = 1.0;	
		mgc.gridheight = 1;
		p4.add(Browse,mgc);
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		p4.add(close,mgc);
	    
	    JPanel p3 = new JPanel();
	    
	    Insets insert ;
    	 mg = new GridBagLayout();
		 mgc = new GridBagConstraints();		
		p3.setLayout(mg);
		mgc.fill = GridBagConstraints.BOTH;
		
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		mgc.weightx = 1.0;
		mgc.weighty = 2.0;	
		mgc.gridheight = 2;
		p3.add(p1,mgc);
		mgc.gridwidth = 1;
		mgc.weighty = 1.0;
		mgc.gridheight = 1;
		insert = new Insets(5,40,5,-25);
		mgc.insets = insert;
		p3.add(piepanel,mgc);
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		insert = new Insets(0,0,0,-35);
		mgc.insets = insert;
		p3.add(p4,mgc);
		
		
		
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,p3, p2);
        split.setOneTouchExpandable(true);
        split.setDividerLocation(420);
        


		setContentPane(split);
		this.setVisible(true);	
	}
//	private void sortManager(JTable table){
//		RowSorter sorter = new TableRowSorter(table.getModel());
//		table.setRowSorter(sorter);
//	}
	
	
	private void setLinkListcol(LinkRGroup srg){	
		List<FiberLink> fiberlinklist = new LinkedList<FiberLink>();
		
		if(srg.getBelongLayer().equals(Layer.Fiber)){
			fiberlinklist = srg.getSRLGFiberLinkList();
		}
		
//		System.out.println("getM_lFiberLinkList          "   + fiberlinklist.size());
		FiberLink fiberlink ;
		for(int i = 0;i < fiberlinklist.size();i++){
			fiberlink =  fiberlinklist.get(i);
			UIFiberLink UIfiberlink =  UIFiberLink.getUIFiberLink(fiberlink.getName());	
			 if(UIfiberlink != null){
				 UIfiberlink.putLinkColor(Color.GRAY.brighter());
				 UIfiberlink.putLinkBlinking(true);
				 UIfiberlink.putLinkBlinkingColor(Color.RED);
				 UIfiberlink.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
				 UIfiberlink.putLink3D(true);
			 }
		}
		 
		 
		 
	}
	 private void recoveryLinkListcol(LinkRGroup srg){	
		 List<FiberLink> fiberlinklist = new LinkedList<FiberLink>();
			
		 if(srg.getBelongLayer().equals(Layer.Fiber)){
				fiberlinklist = srg.getSRLGFiberLinkList();
		 }
		 
		 FiberLink fiberlink ;
		 for(int i = 0;i < fiberlinklist.size();i++){
			 fiberlink = fiberlinklist.get(i);
			 UIFiberLink UIfiberlink =  UIFiberLink.getUIFiberLink(fiberlink.getName());	
			 if(UIfiberlink != null){
//				 if(Network.searchNetwork(fiberlink.getFromNode().getM_nSubnetNum())!=null){
//					    int from, to;
//						from = Network.searchNetwork(fiberlink.getFromNode().getM_nSubnetNum()).getM_nNetType();
//						to = Network.searchNetwork(fiberlink.getToNode().getM_nSubnetNum()).getM_nNetType();
//						if(1 == from && 1 == to){
							 UIfiberlink.setthisColor(Color.GREEN);
							 UIfiberlink.putLinkBlinking(false);
							 UIfiberlink.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
							 UIfiberlink.putLink3D(false);
//						}
//						else if(2 == from && 2 == to){
//							UIfiberlink.setthisColor(Color.BLUE);
//							 UIfiberlink.putLinkBlinking(false);
//							 UIfiberlink.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
//							 UIfiberlink.putLink3D(false);
//						}
//						else if(3 == from && 3 == to){
//							UIfiberlink.setthisColor(Color.YELLOW);
//							 UIfiberlink.putLinkBlinking(false);
//							 UIfiberlink.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
//							 UIfiberlink.putLink3D(false);
//						}
//						else if((1 == from && 2 == to)||(2 == from && 1 == to)){
//							UIfiberlink.setthisColor(Color.CYAN);
//							 UIfiberlink.putLinkBlinking(false);
//							 UIfiberlink.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
//							 UIfiberlink.putLink3D(false);
//						}
//						else if((3 == from && 2 == to)||(2 == from && 3 == to)){
//							UIfiberlink.setthisColor(Color.GRAY);
//							 UIfiberlink.putLinkBlinking(false);
//							 UIfiberlink.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
//							 UIfiberlink.putLink3D(false);
//						} 
//				 }
					
			 }
		 }
		 		 
	 }
	public static void main(String args[]){
		new Dlg_SRLGPoll(null);
	}

}
