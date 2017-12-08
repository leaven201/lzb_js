
package dialog;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import data.CommonNode;
import data.FiberLink;
import data.Route;
import data.Traffic;
import data.Wavelength_2;
import datastructure.UINode;
import database.TrafficDatabase;
import enums.*;
//import enums.TrafficLevel;
//import enums.TrafficRate;
//import enums.TrafficStatus;


import twaver.Dummy;
import twaver.Link;
import twaver.Node;
import twaver.TDataBox;
import twaver.base.A.E.f;



public class Dlg_AddTraffic extends JDialog {
	private JTextField  trafficname = new JTextField(8);
	private JTextField  trafficnumber = new JTextField(9);
	private JTextField  rankid=new JTextField(9);
	private JButton confirm,cancel;
	private JComboBox trafficLevel = new JComboBox();
//	private JRadioButton[] trafficLayer= new JRadioButton[4];
	private String trafficName,linknumber;
	private TDataBox box1;
    private int n = 0;
    private Node startNode,endNode;
    private JComboBox startCombo = new JComboBox();
	private JComboBox endCombo = new JComboBox();
//	private JComboBox trafficlevel = new JComboBox();
	private JComboBox trafficrate = new JComboBox();
//	private JComboBox trafficGroupType = new JComboBox();
	private static TDataBox box;
    private static Dummy TrafficDummy;

//	public Dlg_AddTraffic(Frame owner,TDataBox box1, Dummy TrafficDummy1){
//		super(owner,"添加业务",true);
//		box = box1;
//	    TrafficDummy = TrafficDummy1;
//	    
//		this.setSize(630, 370);
//		this.setLocation(350, 200);
//        this.setTitle("业务添加");
//        Font f=new Font("微软雅黑", Font.PLAIN, 12);
//        int maxid=0;
//		for(int i=0;i<Traffic.trafficList.size();i++){
//			if(maxid<Traffic.trafficList.get(i).getId()){
//				maxid=Traffic.trafficList.get(i).getId();
//			}
//		}
//		rankid.setText(Integer.toString(maxid+1));
////		Date present = new Date();
////		int pYear = present.getYear()+1900;	
////		trafficyear 	= new JTextField(""+pYear,6);
//		
////		int m=Traffic.s_lFiberTrafficList.size()+Traffic.s_lWDMTrafficList.size()+Traffic.s_lOTNTrafficList.size()+
////				Traffic.s_lSDHTrafficList.size()+Traffic.s_lASONTrafficList.size()+1;
////		trafficname.setText("新建业务"+newtraid);
////		trafficnumber.setText("1");
//		
//		JPanel  pane = new JPanel();
////		JLabel  rankldLabel = new JLabel("业务ID");
////		JLabel  sLabel = new JLabel("起始节点");
////		JLabel  eLabel = new JLabel("终止节点");		
//		for(int i = 0;i < CommonNode.getM_lsCommonNode().size();i++){
//			startCombo.addItem(CommonNode.getM_lsCommonNode().get(i).getM_sName());
//		}
//		startCombo.setSelectedIndex(0);
//		
//		final JComboBox endCombo = new JComboBox();
//		for(int i = 0;i < CommonNode.getM_lsCommonNode().size();i++){
//			endCombo.addItem(CommonNode.getM_lsCommonNode().get(i).getM_sName());
//		}
//		endCombo.setSelectedIndex(0);
//		endCombo.setFont(f);
//		
//		String[] s1 = {"100G","40G"};
//		for(int i = 0;i < s1.length;i++){
//			trafficrate.addItem(s1[i]);
//		}
////		trafficrate.setSelectedIndex(2);
////		String[] s5 = {" 1 "," 2 "," 3 "};
////		for(int i = 0;i < s5.length;i++){
////			trafficlevel.addItem(s5[i]);
////		}
//		String[] s2= {"永久1+1","1+1","保护+恢复","动态恢复","专享预制恢复","无保护"};
//		for(int i=0;i<s2.length;i++) {
//			trafficLevel.addItem(s2[i]);
//		}
////		String[] s3= {"无","同源同宿","同源不同宿","同宿不同源","不同宿不同源"};
////			for(int i=0;i<s3.length;i++) {
////				trafficGroupType.addItem(s3[i]);
////			}
////		isShare[0]=new JRadioButton("是");
////		isShare[1]=new JRadioButton("否");
////		ButtonGroup group1 = new ButtonGroup();
////		for(int i = 0;i <isShare.length; i++){
////     		group1.add(isShare[i]);
////		}
////		isElectricalCrossConnection[0]=new JRadioButton("是");
////		isElectricalCrossConnection[1]=new JRadioButton("否");
////		ButtonGroup group2 = new ButtonGroup();
////		for(int i = 0;i <isElectricalCrossConnection.length; i++){
////     		group2.add(isElectricalCrossConnection[i]);
////		}
//
//		confirm = new JButton("确定");
//        confirm.addActionListener(new ActionListener(){
//	    	public void actionPerformed(ActionEvent e){
//	    		String rankId;
//	    		rankId = rankid.getText();
////	    		trafficName = trafficname.getText();
////	    		linknumber = trafficnumber.getText();
////	    		int number = Integer.parseInt(String.valueOf(linknumber));
//////	    	    if(trafficName.isEmpty())
//////	    			JOptionPane.showMessageDialog(null,"请添加业务名称！");
//////	    	    else if(number>9||number<1)
//////	    			JOptionPane.showMessageDialog(null,"请正确添加业务数量！(1~9)");	     	   
//////	    	    else if(isRepeat(Integer.parseInt(String.valueOf(linkId))))
//////	    	    	JOptionPane.showMessageDialog(null,"该ID已存在！");
//////	    	    else if(!isInteger(trafficyear.getText()))
//////	    			JOptionPane.showMessageDialog(null,"请正确添加业务年份！");
//	    	    if(startCombo.getSelectedItem().equals(endCombo.getSelectedItem()))
//    	    	JOptionPane.showMessageDialog(null,"起始节点与终止节点重合！");
//	    		else{
//	    			
////	    			//开始添加链路工作
//	    			String level=null ;
////	    			String layerString="";
////					String idString="";
////					String rateString="";
//////	    	
//////	    			
//	    			int rate=2;
//	    			TrafficRate  trate=TrafficRate.GE;
//	    			
//					if(trafficLevel.getSelectedIndex()==0)
//					{
//						level="PERMANENT11";
//					}
//					else if(trafficLevel.getSelectedIndex()==1)
//					{
//						level="NORMAL11";
//					}
//					else if(trafficLevel.getSelectedIndex()==2)
//					{
//						level = "PROTECTandRESTORATION";
//					}					
//					else if(trafficLevel.getSelectedIndex()==3)
//					{
//						level = "DynamicRESTORATION";
//					}
//					else if(trafficLevel.getSelectedIndex()==4)
//					{
//						level = "PresetRESTORATION";
//					}
//					else
//					{
//						level = "NONPROTECT";
//					}
//				
//					int choiceindexstart=0;
//	    			int choiceindexend=0;
//	    			for(int i=0;i<CommonNode.getM_lsCommonNode().size();i++)
//	    			{
//	    			if(startCombo.getSelectedItem().toString()==CommonNode.getM_lsCommonNode().get(i).getM_sName()){
//	    				
//	    				
//	    				choiceindexstart=i;
//	    				
//	    			}
//	    			if(endCombo.getSelectedItem().toString()==CommonNode.getM_lsCommonNode().get(i).getM_sName()){
//	    				
//	    				
//	    				choiceindexend=i;
//	    				
//	    			}
//	    			
//	    			}
//											
////					String status=TrafficStatus.未分配.toString();
//					
////					int tranum=Integer.parseInt(trafficnumber.getText());
////					CommonNode fNode=CommonNode.getM_lsCommonNode().get(startCombo.getSelectedIndex());
////					CommonNode tNode=CommonNode.getM_lsCommonNode().get(endCombo.getSelectedIndex());
////					String fromIDString	= String.valueOf(fNode.getId());
////		            String toIDString	= String.valueOf(tNode.getId());
////		            if(fNode.getId()<10){fromIDString="00"+fromIDString;}
////		            else if(tNode.getId()<100){fromIDString="0"+fromIDString;
////		            }
////		            if(tNode.getId()<10){toIDString="00"+toIDString;}
////		            else if(tNode.getId()<100){toIDString="0"+toIDString;
////		            }
//		            
//			         //id自动命名
////		            idString=layerString.trim()+fromIDString+toIDString+rateString.trim()+"1";
////			        String maxString=layerString.trim()+fromIDString+toIDString+rateString.trim()+"9";
////			        int maxid=Integer.parseInt(maxString);
////					for(int i=0;i<tranum;i++){
////					int id=Traffic.s_lFiberTrafficList.size()+Traffic.s_lWDMTrafficList.size()+Traffic.s_lOTNTrafficList.size()+
////								Traffic.s_lSDHTrafficList.size()+Traffic.s_lASONTrafficList.size()+1;
//					
//					Traffic t1= new Traffic(Integer.parseInt(String.valueOf(rankId)),CommonNode.getM_lsCommonNode().get(choiceindexstart),CommonNode.getM_lsCommonNode().get(choiceindexend),trate,TrafficLevel.valueOf(level)); 
//				
////					int traID=Integer.parseInt(idString);
//					
////					for(int k=0;traID<=maxid;k++){
////						if(isRepeat(traID))
////							traID++;
////						else {
////							break;
////						}
////					}
////					if(traID>maxid){
////						JOptionPane.showMessageDialog(null, "根据业务ID命名规则，构建业务达到上限！");
////						break;
////					}
////					t1.setId(traID);
//			        
// 					    t1.setNrate(rate);
////	    			    t1.setTrafficLevel(level);
//	    			    TrafficDatabase  a= new TrafficDatabase();
//////	    			a.addTrafficPort(t1);
//
//    			
//	    			
//	    		     Node thetraffic = new Node(t1.getId());
//	    	    	 thetraffic.setName(t1.getName());
//	    			 box.addElement(thetraffic);
//	    			 TrafficDummy.addChild(thetraffic);
//					
//////	    			Traffic tr=new Traffic(Integer.parseInt(String.valueOf(linkId)),rate,
//////	    					1,0,Integer.parseInt(trafficyear.getText()),CommonNode.m_lCommonNode.get(startCombo.getSelectedIndex()).getM_nID(),
//////	    					CommonNode.m_lCommonNode.get(endCombo.getSelectedIndex()).getM_nID(),"",level,"ASON_LAYER",null,trafficName);
//////	    			Traffic.s_lASONTrafficList.add(tr);
//////	    			JOptionPane.showMessageDialog(null, "成功添加业务"+tr.getM_sName());
//////	    			Netdesign.printout("成功添加业务"+tr.getM_sName());
//	    			setVisible(false);
//	    	    	dispose();
//	    	    	}
//	    	}
//	    	});	
//
//		cancel = new JButton("取消");
//        cancel.addActionListener(new ActionListener(){
//	    public void actionPerformed(ActionEvent e){
//	    		Dlg_AddTraffic.this.dispose();
//////	    		setVisible(false);
////	    		dispose();
//	    	}
//	    });
//        
////        Dimension buttonsize = new Dimension(50,28);
////        confirm.setPreferredSize(buttonsize);
////        cancel.setPreferredSize(buttonsize);
//        
//        Insets insert ;
//        
//        GridBagLayout mg = new GridBagLayout();
//		GridBagConstraints mgc = new GridBagConstraints();		
//		pane.setLayout(mg);
//		mgc.fill = GridBagConstraints.NONE;
//		
//		mgc.gridwidth = 1;
//		mgc.weightx = 1.0;
//		mgc.weighty = 1.0;
//		mgc.gridheight = 1;
//		mgc.anchor = GridBagConstraints.CENTER;
//		JLabel jl1=new JLabel("业务ID" );
//		jl1.setFont(f);
//		pane.add (jl1,mgc);
////		mgc.anchor = GridBagConstraints.WEST;
//		
//		mgc.gridwidth =2;
//		insert = new Insets(0,23,0,0);
//		mgc.insets = insert;
//		mgc.anchor = GridBagConstraints.WEST;
//		pane.add(rankid,mgc);
//		
//		mgc.gridwidth = 3;
//		insert = new Insets(0,0,0,0);
//		mgc.insets = insert;
//		mgc.anchor = GridBagConstraints.CENTER;
//		JLabel jl2=new JLabel("业务速率" );
//		jl2.setFont(f);
//		pane.add (jl2,mgc);
//		mgc.gridwidth =GridBagConstraints.REMAINDER;
//		pane.add(trafficrate,mgc);
//		
//		mgc.gridwidth = 1;
//		JLabel jl3=new JLabel("首节点");
//		jl3.setFont(f);
//		pane.add (jl3,mgc);	
//		mgc.gridwidth =2;
//		pane.add(startCombo,mgc);		
//		mgc.gridwidth = 3;
//		JLabel jl4=new JLabel("末节点");
//		jl4.setFont(f);
//		pane.add (jl4,mgc);		
//		mgc.gridwidth = GridBagConstraints.REMAINDER;
//		pane.add(endCombo,mgc);
//		
//		mgc.gridwidth = 1;
//		JLabel jl5=new JLabel("保护等级");
//		jl5.setFont(f);
//		pane.add(jl5,mgc);
//		mgc.gridwidth = 2;
//		pane.add (trafficLevel ,mgc);
//		
//		mgc.anchor = GridBagConstraints.CENTER;
////		Insets insert = new Insets(0,20,0,20);
//		mgc.insets = insert;
//		mgc.gridwidth = 1;
//		mgc.anchor = GridBagConstraints.CENTER;
//		pane.add(new JLabel("       "),mgc);	
//		mgc.gridwidth = 2;
////		mgc.fill = GridBagConstraints.HORIZONTAL;
//		pane.add (confirm ,mgc);
//		mgc.gridwidth = 3;
//		pane.add(new JLabel("      "),mgc);
//		mgc.gridwidth = 4;
//		pane.add(new JLabel("      "),mgc);
//		mgc.gridwidth = 5;
////		mgc.anchor = GridBagConstraints.WEST;
//		pane.add(cancel,mgc);
//		mgc.gridwidth = GridBagConstraints.REMAINDER;
//		pane.add(new JLabel("      "),mgc);
//		
//		pane.setVisible(true);
//		setContentPane(pane);
//		this.setVisible(true);
//		
//	}
	
//	public Dlg_AddTraffic() {
//		// TODO Auto-generated constructor stub
//	}

	public Dlg_AddTraffic() {
		// TODO Auto-generated constructor stub
	}

	public void addTraffic(TDataBox box1, Dummy TrafficDummy1,UINode startNode,UINode endNode){
		
		final UINode fNode=startNode;
		final UINode tNode=endNode;
		final TDataBox box=box1;
	    TrafficDummy = TrafficDummy1;
	    int maxid=0;
		maxid=Traffic.trafficList.size();
		rankid.setText(Integer.toString(maxid+1));
		this.setSize(560, 370);
		this.setLocation(350, 200);
        this.setTitle("业务添加");
		trafficnumber.setText("1");
		Font f=new Font("微软雅黑", Font.PLAIN, 12);
		trafficname.setText(startNode.getCommonNode().getM_sName()+"-"+endNode.getCommonNode().getM_sName()+""+maxid);
//		trafficName.setText(fNode.getCommonNode().getM_sName()+"-"+tNode.getCommonNode().getM_sName());
//		final int id=Traffic.trafficList.size();
		
		JPanel pane = new JPanel();
	
		final JComboBox startCombo = new JComboBox();
		final JComboBox endCombo = new JComboBox();
		startCombo.addItem(startNode.getCommonNode().getM_sName());
		endCombo.addItem(endNode.getCommonNode().getM_sName());
		
		startCombo.setFont(f);
		for(int i = 0;i < CommonNode.getM_lsCommonNode().size();i++){
			startCombo.addItem(CommonNode.getM_lsCommonNode().get(i).getM_sName());
		}
//		startCombo.setSelectedIndex(0);
		
		for(int i = 0;i < CommonNode.getM_lsCommonNode().size();i++){
			endCombo.addItem(CommonNode.getM_lsCommonNode().get(i).getM_sName());
		}
//		endCombo.setSelectedIndex(0);
		endCombo.setFont(f);

		
		String[] s1 = {"100G"};
		for(int i = 0;i < s1.length;i++){
			trafficrate.addItem(s1[i]);
		}

		String[] s2= {"永久1+1","1+1","保护+恢复","动态恢复","专享预制恢复","无保护"};
		for(int i=0;i<s2.length;i++) {
			trafficLevel.addItem(s2[i]);
		}
	


		



	

//		trafficLayer[0].addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				// TODO Auto-generated method stub
//				trafficrate.removeAllItems();
//				String[] s1 = {"100G","40G","10G","2.5G","GE"};
//				for(int i = 0;i < s1.length;i++){
//					trafficrate.addItem(s1[i]);
//				}
//				trafficrate.setSelectedIndex(2);
//			}
//		});
//		trafficLayer[1].addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				// TODO Auto-generated method stub
//				trafficrate.removeAllItems();
//				String[] s1 = {"100G","40G","10G","2.5G","GE","622M"};
//				for(int i = 0;i < s1.length;i++){
//					trafficrate.addItem(s1[i]);
//				}
//				trafficrate.setSelectedIndex(2);
//			}
//		});
//		trafficLayer[2].addActionListener(new ActionListener() {	//SDH
//	
//			@Override
//		public void actionPerformed(ActionEvent arg0) {
//		// TODO Auto-generated method stub
//				trafficrate.removeAllItems();
//			String[] s1 = {"40G","10G","2.5G","GE","622M","155M","FE","2M"};
//			for(int i = 0;i < s1.length;i++){
//				trafficrate.addItem(s1[i]);
//			}
//			trafficrate.setSelectedIndex(5);
//		}
//		});
//		trafficLayer[3].addActionListener(new ActionListener() {
//	
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				// TODO Auto-generated method stub
//				trafficrate.removeAllItems();
//				String[] s1 = {"40G","10G","2.5G","GE","622M","155M","FE","2M"};
//				for(int i = 0;i < s1.length;i++){
//					trafficrate.addItem(s1[i]);
//				}
//				trafficrate.setSelectedIndex(4);
//			}
//		});
//		
//		selection[0] = new JRadioButton("ASON 层",true);
//		selection[0].enable(false);
//		ButtonGroup group = new ButtonGroup();
//		group.add(selection[0]);
		confirm = new JButton("确定");
        confirm.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		String rankId;
	    		String trafficName;
	    		rankId = rankid.getText();
	    		trafficName=trafficname.getText();
////	    		trafficName = trafficName.getText();
	    		linknumber = trafficnumber.getText();
	    		int number = Integer.parseInt(String.valueOf(linknumber));
	    	    if(rankId.isEmpty())
	    			JOptionPane.showMessageDialog(null,"请添加业务ID！");
	    	    else if(number>9||number<1)
	    			JOptionPane.showMessageDialog(null,"请正确添加业务数量！");	     	   
////	    	    else if(isRepeat(Integer.parseInt(String.valueOf(linkId))))
////	    	    	JOptionPane.showMessageDialog(null,"该ID已存在！");
////	    	    else if(!isInteger(trafficyear.getText()))
////	    			JOptionPane.showMessageDialog(null,"请正确添加业务年份！");
	    	    else if(startCombo.getSelectedItem().equals(endCombo.getSelectedItem()))
	    	    	JOptionPane.showMessageDialog(null,"起始节点与终止节点重合！");
	    		else{
//	    			//开始添加链路工作
	    			String protectlevel=null ;
//	    			
//	    			String layerString="";
					String idString="";
					String rateString="";
//					
					if(trafficLevel.getSelectedIndex()==0)
					{
						protectlevel="PERMANENT11";
					}
					else if(trafficLevel.getSelectedIndex()==1)
					{
						protectlevel="NORMAL11";
					}
					else if(trafficLevel.getSelectedIndex()==2)
					{
						protectlevel = "PROTECTandRESTORATION";
					}					
					else if(trafficLevel.getSelectedIndex()==3)
					{
						protectlevel = "DynamicRESTORATION";
					}
					else if(trafficLevel.getSelectedIndex()==4)
					{
						protectlevel = "PresetRESTORATION";
					}
					else
					{
						protectlevel = "NONPROTECT";
					}
					String rate;
				    if(trafficrate.getSelectedIndex()==0)
				    { 
				    	rate="100";
				    }
				    else
				    {
				    	rate="";
				    }


	    			

	    			
//	    			int rate=2;
//	    			TrafficRate  trate=TrafficRate.GE;
				
					

					
						
//                    String status=TrafficStatus.未分配.toString();
//					
					int tranum=Integer.parseInt(trafficnumber.getText());
					CommonNode fNode=CommonNode.getM_lsCommonNode().get(startCombo.getSelectedIndex());
					CommonNode tNode=CommonNode.getM_lsCommonNode().get(endCombo.getSelectedIndex());
//					String fromIDString	= String.valueOf(fNode.getId());
//		            String toIDString	= String.valueOf(tNode.getId());
//		            if(fNode.getId()<10){fromIDString="00"+fromIDString;}
//		            else if(tNode.getId()<100){fromIDString="0"+fromIDString;
//		            }
//		            if(tNode.getId()<10){toIDString="00"+toIDString;}
//		            else if(tNode.getId()<100){toIDString="0"+toIDString;
//		            }
	    						         //id自动命名
//			        idString=layerString.trim()+fromIDString+toIDString+rateString.trim()+"1";
//			        String maxString=layerString.trim()+fromIDString+toIDString+rateString.trim()+"9";
//			        int maxid=Integer.parseInt(maxString);
					for(int i=0;i<tranum;i++){
					int id=Traffic.trafficList.size()+1;
//					Traffic t1= new Traffic(Integer.parseInt(String.valueOf(rankid.getText())),trafficName,fNode,tNode,TrafficRate.valueOf(rate),TrafficLevel.valueOf(protectlevel)); 
					Traffic t1= new Traffic(id,trafficName,fNode,tNode,TrafficRate.valueOf(rate),TrafficLevel.valueOf(protectlevel)); 
//	    			System.out.println(rate);
//					int traID=Integer.parseInt(idString);
					
//					for(int k=0;traID<=maxid;k++){
//						if(isRepeat(traID))
//							traID++;
//						else {
//							break;
//						}
//					}
//					if(traID>maxid){
//						JOptionPane.showMessageDialog(null, "根据业务ID命名规则，构建业务达到上限！");
////						break;
//					}
//					t1.setId(traID);
//			        
//					t1.setNrate(TrafficRate.Rate2Num(rate));
//					t1.setNrate(nrate);
	    			t1.setProtectLevel(TrafficLevel.trans(protectlevel));
	    			TrafficDatabase  a= new TrafficDatabase();
//	     			a.addTrafficPort(t1);
                    Traffic.trafficList.add(t1);
	    			
	    		     Node thetraffic = new Node(t1.getId());
//	  		        thetraffic.setName(Traffic.trafficList.get(i).getFromNode().getName()+"-"+Traffic.trafficList.get(i).getToNode().getName()+" "+i);
	    	    	 thetraffic.setName(t1.getName());
	    	    	 thetraffic.setLocation(99999999, 999999999);
	    			 box.addElement(thetraffic);
	    			 TrafficDummy.addChild(thetraffic);
					}
////	    			Traffic tr=new Traffic(Integer.parseInt(String.valueOf(linkId)),rate,
////	    					1,0,Integer.parseInt(trafficyear.getText()),CommonNode.m_lCommonNode.get(startCombo.getSelectedIndex()).getM_nID(),
////	    					CommonNode.m_lCommonNode.get(endCombo.getSelectedIndex()).getM_nID(),"",level,"ASON_LAYER",null,trafficName);
////	    			Traffic.s_lASONTrafficList.add(tr);
////	    			JOptionPane.showMessageDialog(null, "成功添加业务"+tr.getM_sName());
////	    			Netdesign.printout("成功添加业务"+tr.getM_sName());
	    			setVisible(false);
	    	    	dispose();
	    	    	}
//	    			
	    	}
	    });	
//
		cancel = new JButton("取消");
        cancel.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		Dlg_AddTraffic.this.dispose();
////	    		setVisible(false);
////	    		dispose();
	    	}
	    });
//        
//        Dimension buttonsize = new Dimension(50,28);
//        confirm.setPreferredSize(buttonsize);
//        cancel.setPreferredSize(buttonsize);
        
        Insets insert ;
        
        GridBagLayout mg = new GridBagLayout();
		GridBagConstraints mgc = new GridBagConstraints();		
		pane.setLayout(mg);
		mgc.fill = GridBagConstraints.NONE;
        
		mgc.gridwidth = 1;
		mgc.weightx = 1.0;
		mgc.weighty = 1.0;
		mgc.gridheight = 1;
		mgc.anchor = GridBagConstraints.CENTER;
		JLabel jl1=new JLabel("业务ID" );
		jl1.setFont(f);
		pane.add (jl1,mgc);

		
		mgc.gridwidth =2;
		insert = new Insets(0,23,0,0);
		mgc.insets = insert;
		mgc.anchor = GridBagConstraints.WEST;
		pane.add(rankid,mgc);
		
		mgc.gridwidth = 3;
		insert = new Insets(0,0,0,0);
		mgc.insets = insert;
		mgc.anchor = GridBagConstraints.CENTER;
		JLabel jl2=new JLabel("业务数量" );
		jl2.setFont(f);
		pane.add (jl2,mgc);
		mgc.gridwidth =GridBagConstraints.REMAINDER;
		pane.add(trafficnumber,mgc);
		
		mgc.gridwidth = 1;
		JLabel jl3=new JLabel("首节点");
		jl3.setFont(f);
		pane.add (jl3,mgc);	
		mgc.gridwidth =2;
		pane.add(startCombo,mgc);		
		mgc.gridwidth = 3;
		JLabel jl4=new JLabel("末节点");
		jl4.setFont(f);
		pane.add (jl4,mgc);		
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		pane.add(endCombo,mgc);
		
		mgc.gridwidth = 1;
		JLabel jl5=new JLabel("保护等级");
		jl5.setFont(f);
		pane.add(jl5,mgc);
		mgc.gridwidth = 2;
		pane.add (trafficLevel ,mgc);
		JLabel jl6=new JLabel("业务数率");
		jl6.setFont(f);
		pane.add(jl6,mgc);
		mgc.gridwidth=GridBagConstraints.REMAINDER;
		pane.add(trafficrate,mgc);
		
		mgc.gridwidth = 1;
		pane.add(new JLabel("  "),mgc);	
		mgc.gridwidth = 2;
		insert = new Insets(0,-70,0,0);
		mgc.insets = insert;
		pane.add (confirm ,mgc);
		insert = new Insets(0,-30,0,0);
		mgc.insets = insert;
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		pane.add(cancel,mgc);
        
		
		pane.setVisible(true);
		setContentPane(pane);
		this.setVisible(true);
		
	}
	
	public boolean isRepeat(int id){
		for(int t=0;t<Traffic.trafficList.size();t++){
			if(Traffic.trafficList.get(t).getId() == id )
				return true;
		}
//		for(int t=0;t<Traffic.s_lOTNTrafficList.size();t++){
//			if(Traffic.s_lOTNTrafficList.get(t).getM_nID() == id )
//				return true;
//		}
//		for(int t=0;t<Traffic.s_lSDHTrafficList.size();t++){
//			if(Traffic.s_lSDHTrafficList.get(t).getM_nID() == id )
//				return true;
//		}
//		for(int t=0;t<Traffic.s_lASONTrafficList.size();t++){
//			if(Traffic.s_lASONTrafficList.get(t).getM_nID() == id )
//				return true;
//		}
		return false;	
	}
	/**
	  * 判断字符串是否是整数
	  */
	 public static boolean isInteger(String value) {
	  try {
	   Integer.parseInt(value);
	   return true;
	  } catch (NumberFormatException e) {
	   return false;
	  }
	 }
	 /**
	  * 判断字符串是否是浮点数
	  */
	 public static boolean isDouble(String value) {
	  try {
	   Double.parseDouble(value);
	   if (value.contains("."))
	    return true;
	   return false;
	  } catch (NumberFormatException e) {
	   return false;
	  }
	 }

	 /**
	  * 判断字符串是否是数字
	  */
	 public static boolean isNumber(String value) {
	  return isInteger(value) || isDouble(value);
	 }
	 
//	 public static void main(String[] args){
//		 new Dlg_AddTraffic();
//		 
//	 }

	
	}


