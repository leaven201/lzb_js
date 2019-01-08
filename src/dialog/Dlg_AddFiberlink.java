package dialog;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import twaver.Dummy;
import twaver.TDataBox;
import data.CommonNode;
import data.FiberLink;
import data.OTNLink;
//import data.SDHLink;
import data.WDMLink;
import datastructure.UIFiberLink;
import datastructure.UINode;
import datastructure.UIWDMLink;
import database.LinkDataBase;
//import enums.Area;
import enums.Layer;
//import enums.MaintainUnit;
//import enums.PerfunctoryWay;
import greenDesign.greenDesign;

public class Dlg_AddFiberlink {
	private Dummy linkDummy;
	public Dlg_AddFiberlink() {
		super();
	}
	 public void Dlg_AddFiberlink(TDataBox box1,Dummy linkDummy1){
	    	final JFrame frame=new JFrame();
	    	frame.setName("添加链路");
			frame.setSize(520, 350);
			frame.setLocation(350, 200);
			frame.setVisible(true);
			frame.setTitle("添加链路");
			ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/resource/ddd1111.png"));
			frame.setIconImage(imageIcon.getImage());
			linkDummy=linkDummy1;
			int maxid=0;
			for(int i=0;i<FiberLink.fiberLinkList.size();i++){
				if(maxid<FiberLink.fiberLinkList.get(i).getId()){
					maxid=FiberLink.fiberLinkList.get(i).getId();
				}
			}
			final TDataBox box=box1;
			final JTextField  linkid = new JTextField(10);
	    	final JTextField  linkname = new JTextField(10);
	    	final JTextField  linklength = new JTextField(10);
	    	final JTextField  shuaijian = new JTextField(10);
	    	final JTextField  pmd = new JTextField(10);
	    	final JTextField  jieduan = new JTextField(10);
	    	final JTextField  anothername = new JTextField(10);
	    	linkid.setText(Integer.toString(maxid+1));
	    	linkname.setText("新建链路"+Integer.toString(maxid+1));
	    	anothername.setText("首节点-末节点");
	    	Font f=new Font("微软雅黑", Font.PLAIN, 12);
	    	linklength.setText("30");
	    	shuaijian.setText("10.86");
	    	pmd.setText("0");
	    	jieduan.setText("1");
	    	anothername.setFont(f);
	    	linklength.setFont(f);
	    	shuaijian.setFont(f);
	    	pmd.setFont(f);
	    	jieduan.setFont(f);
//	    	linksize.setText("10");
//	    	blocktime.setText("1");
//	    	averlose.setText("0.2");
//	    	startyear.setText("2011");
//	    	endyear.setText("2013");
	    	
	    	JRadioButton layerfiber = new JRadioButton("Fiber层",true);
	    	JRadioButton layerwdm = new JRadioButton("WDM层",true);
	    	layerfiber.setFont(f);
	    	layerwdm.setFont(f);
	    	ButtonGroup layergroup=new ButtonGroup();
	    	layergroup.add(layerfiber);
	    	layergroup.add(layerwdm);
	    	layerfiber.setSelected(true);
	    	
	    	
	    	JPanel pane = new JPanel();
			final JComboBox startCombo = new JComboBox();
			startCombo.setFont(f);
			for(int i = 0;i < CommonNode.getM_lsCommonNode().size();i++){
				startCombo.addItem(CommonNode.getM_lsCommonNode().get(i).getM_sName());
			}
			startCombo.setSelectedIndex(0);
			
			final JComboBox endCombo = new JComboBox();
			for(int i = 0;i < CommonNode.getM_lsCommonNode().size();i++){
				endCombo.addItem(CommonNode.getM_lsCommonNode().get(i).getM_sName());
			}
			endCombo.setSelectedIndex(0);
			endCombo.setFont(f);
			
			final JComboBox typecombo = new JComboBox();
			typecombo.addItem("G655");
			typecombo.addItem("G652");
			typecombo.setSelectedIndex(0);
			typecombo.setFont(f);
			
			JButton confirmButton=new JButton("确定");
			confirmButton.setFont(f);
			confirmButton.addActionListener(new ActionListener(){
		    	public void actionPerformed(ActionEvent e){
		    		String linkName,linkId,aname,linkshuaijian,linkpmd,linkjieduan;
		    		linkName = linkname.getText();
		    		aname = anothername.getText();
		    		linkId = linkid.getText();
		    		linkshuaijian = shuaijian.getText();
		    		linkpmd = pmd.getText();
		    		linkjieduan = jieduan.getText();
		    	
		    	    if(linkName.isEmpty())
		    			JOptionPane.showMessageDialog(null,"请添加链路名称！");
		    	    else if(!isInteger(linkId))
		 	    		JOptionPane.showMessageDialog(null,"链路ID应为非空数字！");	
		    	    else if(linkNameSame(linkname.getText(),Layer.Fiber))
		    			JOptionPane.showMessageDialog(null,"该链路名称已存在！");	     	   
		    	    else if(isRepeat(String.valueOf(linkId),Layer.Fiber))
		    	    	JOptionPane.showMessageDialog(null,"该ID已存在！");
		    	    else if(aname.isEmpty())
			    			JOptionPane.showMessageDialog(null,"请正确添加链路别名！");
		    	    else if(!isInteger(linklength.getText()))
		    			JOptionPane.showMessageDialog(null,"请正确添加链路长度！");
		    	    else if(!isDouble(shuaijian.getText()))
		    			JOptionPane.showMessageDialog(null,"请正确添加光纤衰减！");
		    	    else if(!isInteger(pmd.getText()))
		    			JOptionPane.showMessageDialog(null,"请正确添加光纤PMD！");
		    	    else if(!isInteger(jieduan.getText()))
		    			JOptionPane.showMessageDialog(null,"请正确添加光纤阶段！");
//		    	    else if(!isInteger(endyear.getText()))
//		    			JOptionPane.showMessageDialog(null,"请正确添加竣工年份！");
		    	    else if(startCombo.getSelectedItem().equals(endCombo.getSelectedItem()))
		    	    	JOptionPane.showMessageDialog(null,"起始节点与终止节点重合！");
		    		else{
		    			if(layerfiber.isSelected()) {
		    			//开始添加Fiber链路工作
		    			int choiceindexstart=0;
		    			int choiceindexend=0;
		    			for(int i=0;i<CommonNode.getM_lsCommonNode().size();i++)
		    			{
		    			if(startCombo.getSelectedItem().toString()==CommonNode.getM_lsCommonNode().get(i).getM_sName()){
		    				
		    				
		    				choiceindexstart=i;
		    				
		    			}
		    			if(endCombo.getSelectedItem().toString()==CommonNode.getM_lsCommonNode().get(i).getM_sName()){
		    				
		    				
		    				choiceindexend=i;
		    				
		    			}
		    			
		    			}
		    		//	int childnumber=LinkDataBase.returnnetid(CommonNode.getM_lsCommonNode().get(choiceindexstart).getM_sName(), CommonNode.getM_lsCommonNode().get(choiceindexend).getM_sName());
		    			FiberLink fl=new FiberLink(Integer.parseInt(String.valueOf(linkId)),linkname.getText(),CommonNode.getM_lsCommonNode().get(choiceindexstart),
		    					CommonNode.getM_lsCommonNode().get(choiceindexend), Double.parseDouble(String.valueOf(linklength.getText())),
		    					Layer.Fiber,true,Double.parseDouble(String.valueOf(linkshuaijian)),Double.parseDouble(String.valueOf(linkpmd)),
		    					typecombo.getSelectedItem().toString(),Integer.parseInt(String.valueOf(linkjieduan)));
//		    				FiberLink.fiberLinkList.add(fl);
		    				UIFiberLink addnew = new UIFiberLink(FiberLink.getFiberLink(Integer.parseInt(String.valueOf(linkId))));
			    			UIFiberLink.s_lUIFiberLinkList.add(addnew);
			    			linkDummy.addChild(addnew);
			    			box.addElement(addnew);
			    			//Dlg_InputAll.setthisTypeColor(fl);
		    		//	}
		    		//	else{
		    		//		JOptionPane.showMessageDialog(null, "链路不合法！");
		    		//	}
		    			
		    		    frame.dispose();
		    	    	}
		    	if(layerwdm.isSelected()) {

	    			//开始添加WDM链路工作
	    			int choiceindexstart=0;
	    			int choiceindexend=0;
	    			for(int i=0;i<CommonNode.getM_lsCommonNode().size();i++)
	    			{
	    			if(startCombo.getSelectedItem().toString()==CommonNode.getM_lsCommonNode().get(i).getM_sName()){
	    				
	    				
	    				choiceindexstart=i;
	    				
	    			}
	    			if(endCombo.getSelectedItem().toString()==CommonNode.getM_lsCommonNode().get(i).getM_sName()){
	    				
	    				
	    				choiceindexend=i;
	    				
	    			}
	    			
	    			}
	    		//	int childnumber=LinkDataBase.returnnetid(CommonNode.getM_lsCommonNode().get(choiceindexstart).getM_sName(), CommonNode.getM_lsCommonNode().get(choiceindexend).getM_sName());
	    			WDMLink fl=new WDMLink(Integer.parseInt(String.valueOf(linkId)),linkname.getText(),CommonNode.getM_lsCommonNode().get(choiceindexstart),
	    					CommonNode.getM_lsCommonNode().get(choiceindexend), Double.parseDouble(String.valueOf(linklength.getText())),
	    					Layer.Fiber,true,Double.parseDouble(String.valueOf(linkshuaijian)),Double.parseDouble(String.valueOf(linkpmd)),
	    					typecombo.getSelectedItem().toString(),Integer.parseInt(String.valueOf(linkjieduan)));
	    			WDMLink.WDMLinkList.add(fl);
	    				UIWDMLink addnew = new UIWDMLink(WDMLink.getLinkByID(Integer.parseInt(String.valueOf(linkId))));
		    			UIWDMLink.s_lUIWDMLinkList.add(addnew);
		    			linkDummy.addChild(addnew);
		    			box.addElement(addnew);
		    			//Dlg_InputAll.setthisTypeColor(fl);
	    		//	}
	    		//	else{
	    		//		JOptionPane.showMessageDialog(null, "链路不合法！");
	    		//	}
	    			
	    		    frame.dispose();
	    	    	
		    	}		
		    	}	
		    	}
		    });	
			JButton cancel = new JButton("取消");
			cancel.setFont(f);
	        cancel.addActionListener(new ActionListener(){
		    	public void actionPerformed(ActionEvent e){
		    		frame.dispose();
		    	}
		    });
	        Dimension buttonsize = new Dimension(60,30);
	        confirmButton.setPreferredSize(buttonsize);
	        cancel.setPreferredSize(buttonsize);
	        
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
			JLabel jl1=new JLabel("链路名称" );
			jl1.setFont(f);
			pane.add (jl1,mgc);
			
			mgc.gridwidth =2;
			insert = new Insets(0,23,0,0);
			mgc.insets = insert;
			mgc.anchor = GridBagConstraints.WEST;
			pane.add(linkname,mgc);
			
			mgc.gridwidth = 3;
			insert = new Insets(0,0,0,0);
			mgc.insets = insert;
			mgc.anchor = GridBagConstraints.CENTER;
			JLabel jl2=new JLabel("链路ID" );
			jl2.setFont(f);
			pane.add (jl2,mgc);
			mgc.gridwidth =GridBagConstraints.REMAINDER;
			pane.add(linkid,mgc);
			mgc.gridwidth = 1;
//			pane.add (new JLabel("光纤数  "),mgc);		
//			mgc.gridwidth = 2;
//			mgc.weightx = 1.0;
//			pane.add(linksize,mgc);
//			

//			
//			mgc.gridwidth = 1;
//			pane.add (new JLabel("开通年份" ),mgc);		
//			mgc.gridwidth =2;
//			pane.add(startyear,mgc);		
//			mgc.gridwidth = 3;
//			pane.add (new JLabel("竣工年份"),mgc);	
//			mgc.gridwidth = GridBagConstraints.REMAINDER;
//			pane.add(endyear,mgc);
			
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
			

			
//			mgc.gridwidth = 3;
//			JLabel jj1=new JLabel("长度/km" );
//			jj1.setFont(f);
//			pane.add (jj1,mgc);	
//			mgc.gridwidth = GridBagConstraints.REMAINDER;
//			pane.add(linklength,mgc);
			
			mgc.gridwidth = 1;
			JLabel length=new JLabel("长度/km");
			length.setFont(f);
			pane.add (length,mgc);
			mgc.gridwidth = 2;
			pane.add(linklength,mgc);
			mgc.gridwidth =  3;
			JLabel shuai=new JLabel("光纤衰减");
			shuai.setFont(f);
			pane.add(shuai,mgc);
			mgc.gridwidth=GridBagConstraints.REMAINDER;
			pane.add(shuaijian,mgc);
			

			mgc.gridwidth = 1;
			JLabel p=new JLabel("光纤PMD");
			p.setFont(f);
			pane.add(p,mgc);		
			mgc.gridwidth = 2;
			pane.add (pmd,mgc);
			mgc.gridwidth=3;
			JLabel jie=new JLabel("光纤阶段");
			jie.setFont(f);
			pane.add(jie,mgc);
			mgc.gridwidth = GridBagConstraints.REMAINDER;
			pane.add ( jieduan,mgc);
			
			mgc.gridwidth = 1;
			JLabel aname=new JLabel("链路别名");
			aname.setFont(f);
			pane.add(aname,mgc);		
			mgc.gridwidth = 2;
			pane.add (anothername,mgc);
			mgc.gridwidth=3;
			JLabel gtype=new JLabel("光纤类型");
			gtype.setFont(f);
			pane.add(gtype,mgc);
			mgc.gridwidth = GridBagConstraints.REMAINDER;
			pane.add ( typecombo,mgc);
			
			mgc.gridwidth = 1;
			JLabel jl5=new JLabel("所属层");
			jl5.setFont(f);
			pane.add(jl5,mgc);
			mgc.gridwidth = 2;
			pane.add (layerfiber ,mgc);
//			pane.add (layerwdm ,mgc);
//			mgc.gridwidth = 3;
//			pane.add(new JLabel("维护单位"),mgc);	
			mgc.gridwidth = GridBagConstraints.REMAINDER;
			pane.add(layerwdm,mgc);


			mgc.gridwidth = 1;
			pane.add(new JLabel("  "),mgc);	
			mgc.gridwidth = 2;
			insert = new Insets(0,-70,0,0);
			mgc.insets = insert;
			pane.add (confirmButton ,mgc);
			insert = new Insets(0,-30,0,0);
			mgc.insets = insert;
			mgc.gridwidth = GridBagConstraints.REMAINDER;
			pane.add(cancel,mgc);
			
			pane.setVisible(true);
			frame.setContentPane(pane);
			frame.setVisible(true);
			
	    }
	
	 public void Dlg_AddFiberlink1(TDataBox box1,Dummy linkDummy1,UINode startNode,UINode endNode){
	    	final JFrame frame=new JFrame();
	    	frame.setName("添加链路");
			frame.setSize(520, 350);
			frame.setLocation(350, 200);
			frame.setVisible(true);
			frame.setTitle("添加链路");
			ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/resource/ddd1111.png"));
			frame.setIconImage(imageIcon.getImage());
			linkDummy=linkDummy1;
			int maxid=0;
			for(int i=0;i<FiberLink.fiberLinkList.size();i++){
				if(maxid<FiberLink.fiberLinkList.get(i).getId()){
					maxid=FiberLink.fiberLinkList.get(i).getId();
				}
			}
			final TDataBox box=box1;
			final JTextField  linkid = new JTextField(10);
	    	final JTextField  linkname = new JTextField(10);
	    	final JTextField  linklength = new JTextField(10);
	    	final JTextField  shuaijian = new JTextField(10);
	    	final JTextField  pmd = new JTextField(10);
	    	final JTextField  jieduan = new JTextField(10);
	    	final JTextField  anothername = new JTextField(10);
	    	linkid.setText(Integer.toString(maxid+1));
	    	linkname.setText("新建链路"+Integer.toString(maxid+1));
	    	anothername.setText("首节点-末节点");
	    	Font f=new Font("微软雅黑", Font.PLAIN, 12);
	    	linklength.setText("30");
	    	shuaijian.setText("10.86");
	    	pmd.setText("0");
	    	jieduan.setText("1");
	    	anothername.setFont(f);
	    	linklength.setFont(f);
	    	shuaijian.setFont(f);
	    	pmd.setFont(f);
	    	jieduan.setFont(f);
	    	
	    	
	    	JRadioButton layerfiber = new JRadioButton("Fiber层",true);
	    	JRadioButton layerwdm = new JRadioButton("WDM层",true);
	    	layerfiber.setFont(f);
	    	layerwdm.setFont(f);
	    	ButtonGroup layergroup=new ButtonGroup();
	    	layergroup.add(layerfiber);
	    	layergroup.add(layerwdm);
	    	layerfiber.setSelected(true);
	    	
	    	JPanel pane = new JPanel();
	    	final JComboBox startCombo = new JComboBox();
			final JComboBox endCombo = new JComboBox();
			startCombo.addItem(startNode.getCommonNode().getM_sName());
			endCombo.addItem(endNode.getCommonNode().getM_sName());
			
			startCombo.setFont(f);
			for(int i = 0;i < CommonNode.getM_lsCommonNode().size();i++){
				startCombo.addItem(CommonNode.getM_lsCommonNode().get(i).getM_sName());
			}
//			startCombo.setSelectedIndex(0);
			
			for(int i = 0;i < CommonNode.getM_lsCommonNode().size();i++){
				endCombo.addItem(CommonNode.getM_lsCommonNode().get(i).getM_sName());
			}
//			endCombo.setSelectedIndex(0);
			endCombo.setFont(f);
			
			final JComboBox typecombo = new JComboBox();
			typecombo.addItem("G655");
			typecombo.addItem("G652");
			typecombo.setSelectedIndex(0);
			typecombo.setFont(f);
			
			
			JButton confirmButton=new JButton("确定");
			confirmButton.setFont(f);
			confirmButton.addActionListener(new ActionListener(){
		    	public void actionPerformed(ActionEvent e){
		    		String linkName,linkId,aname,linkshuaijian,linkpmd,linkjieduan;
		    		linkName = linkname.getText();
		    		aname = anothername.getText();
		    		linkId = linkid.getText();
		    		linkshuaijian = shuaijian.getText();
		    		linkpmd = pmd.getText();
		    		linkjieduan = jieduan.getText();
		    	
		    		if(linkName.isEmpty())
		    			JOptionPane.showMessageDialog(null,"请添加链路名称！");
		    	    else if(!isInteger(linkId))
		 	    		JOptionPane.showMessageDialog(null,"链路ID应为非空数字！");	
		    	    else if(linkNameSame(linkname.getText(),Layer.Fiber))
		    			JOptionPane.showMessageDialog(null,"该链路名称已存在！");	     	   
		    	    else if(isRepeat(String.valueOf(linkId),Layer.Fiber))
		    	    	JOptionPane.showMessageDialog(null,"该ID已存在！");
		    	    else if(aname.isEmpty())
			    			JOptionPane.showMessageDialog(null,"请正确添加链路别名！");
		    	    else if(!isInteger(linklength.getText()))
		    			JOptionPane.showMessageDialog(null,"请正确添加链路长度！");
		    	    else if(!isDouble(shuaijian.getText()))
		    			JOptionPane.showMessageDialog(null,"请正确添加光纤衰减！");
		    	    else if(!isInteger(pmd.getText()))
		    			JOptionPane.showMessageDialog(null,"请正确添加光纤PMD！");
		    	    else if(!isInteger(jieduan.getText()))
		    			JOptionPane.showMessageDialog(null,"请正确添加光纤阶段！");
//		    	    else if(!isInteger(endyear.getText()))
//		    			JOptionPane.showMessageDialog(null,"请正确添加竣工年份！");
		    	    else if(startCombo.getSelectedItem().equals(endCombo.getSelectedItem()))
		    	    	JOptionPane.showMessageDialog(null,"起始节点与终止节点重合！");
		    		else{
		    			
		    			//开始添加链路工作
		    			if(layerfiber.isSelected()) {
		    			int choiceindexstart=0;
		    			int choiceindexend=0;
		    			for(int i=0;i<CommonNode.getM_lsCommonNode().size();i++)
		    			{
		    			if(startCombo.getSelectedItem().toString()==CommonNode.getM_lsCommonNode().get(i).getM_sName()){
		    				
		    				
		    				choiceindexstart=i;
		    				
		    			}
		    			if(endCombo.getSelectedItem().toString()==CommonNode.getM_lsCommonNode().get(i).getM_sName()){
		    				
		    				
		    				choiceindexend=i;
		    				
		    			}
		    			
		    			}
		    			
		    		//	int childnumber=LinkDataBase.returnnetid(CommonNode.getM_lsCommonNode().get(choiceindexstart).getM_sName(), CommonNode.getM_lsCommonNode().get(choiceindexend).getM_sName());
		    			FiberLink fl=new FiberLink(Integer.parseInt(String.valueOf(linkId)),linkname.getText(),CommonNode.getM_lsCommonNode().get(choiceindexstart),
		    					CommonNode.getM_lsCommonNode().get(choiceindexend), Double.parseDouble(String.valueOf(linklength.getText())),
		    					Layer.Fiber,true,Double.parseDouble(String.valueOf(linkshuaijian)),Double.parseDouble(String.valueOf(linkpmd)),
		    					typecombo.getSelectedItem().toString(),Integer.parseInt(String.valueOf(linkjieduan)));
//		    				FiberLink.fiberLinkList.add(fl);
		    				UIFiberLink addnew = new UIFiberLink(FiberLink.getFiberLink(Integer.parseInt(String.valueOf(linkId))));
			    			UIFiberLink.s_lUIFiberLinkList.add(addnew);
			    			linkDummy.addChild(addnew);
			    			box.addElement(addnew);
			    			
			    			//Dlg_InputAll.setthisTypeColor(fl);
		    		//	}
		    		//	else{
		    		//		JOptionPane.showMessageDialog(null, "链路不合法！");
		    		//	}
		    			
		    		    frame.dispose();
		    	    	}
		    			if(layerwdm.isSelected()) {

			    			//开始添加WDM链路工作
			    			int choiceindexstart=0;
			    			int choiceindexend=0;
			    			for(int i=0;i<CommonNode.getM_lsCommonNode().size();i++)
			    			{
			    			if(startCombo.getSelectedItem().toString()==CommonNode.getM_lsCommonNode().get(i).getM_sName()){
			    				
			    				
			    				choiceindexstart=i;
			    				
			    			}
			    			if(endCombo.getSelectedItem().toString()==CommonNode.getM_lsCommonNode().get(i).getM_sName()){
			    				
			    				
			    				choiceindexend=i;
			    				
			    			}
			    			
			    			}
			    		//	int childnumber=LinkDataBase.returnnetid(CommonNode.getM_lsCommonNode().get(choiceindexstart).getM_sName(), CommonNode.getM_lsCommonNode().get(choiceindexend).getM_sName());
			    			WDMLink fl=new WDMLink(Integer.parseInt(String.valueOf(linkId)),linkname.getText(),CommonNode.getM_lsCommonNode().get(choiceindexstart),
			    					CommonNode.getM_lsCommonNode().get(choiceindexend), Double.parseDouble(String.valueOf(linklength.getText())),
			    					Layer.WDM,true,Double.parseDouble(String.valueOf(linkshuaijian)),Double.parseDouble(String.valueOf(linkpmd)),
			    					typecombo.getSelectedItem().toString(),Integer.parseInt(String.valueOf(linkjieduan)));
			    			WDMLink.WDMLinkList.add(fl);
			    				UIWDMLink addnew = new UIWDMLink(WDMLink.getLinkByID(Integer.parseInt(String.valueOf(linkId))));
				    			UIWDMLink.s_lUIWDMLinkList.add(addnew);
				    			linkDummy.addChild(addnew);
				    			box.addElement(addnew);
				    			//Dlg_InputAll.setthisTypeColor(fl);
			    		//	}
			    		//	else{
			    		//		JOptionPane.showMessageDialog(null, "链路不合法！");
			    		//	}
			    			
			    		    frame.dispose();
			    	    	
				    	}
		    	}	
		    	}
		    });	
			JButton cancel = new JButton("取消");
			cancel.setFont(f);
	        cancel.addActionListener(new ActionListener(){
		    	public void actionPerformed(ActionEvent e){
		    		frame.dispose();
		    	}
		    });
	        Dimension buttonsize = new Dimension(60,30);
	        confirmButton.setPreferredSize(buttonsize);
	        cancel.setPreferredSize(buttonsize);
	        
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
			JLabel jl1=new JLabel("链路名称" );
			jl1.setFont(f);
			pane.add (jl1,mgc);
			
			mgc.gridwidth =2;
			insert = new Insets(0,23,0,0);
			mgc.insets = insert;
			mgc.anchor = GridBagConstraints.WEST;
			pane.add(linkname,mgc);
			
			mgc.gridwidth = 3;
			insert = new Insets(0,0,0,0);
			mgc.insets = insert;
			mgc.anchor = GridBagConstraints.CENTER;
			JLabel jl2=new JLabel("链路ID" );
			jl2.setFont(f);
			pane.add (jl2,mgc);
			mgc.gridwidth =GridBagConstraints.REMAINDER;
			pane.add(linkid,mgc);
			mgc.gridwidth = 1;
//			pane.add (new JLabel("光纤数  "),mgc);		
//			mgc.gridwidth = 2;
//			mgc.weightx = 1.0;
//			pane.add(linksize,mgc);
//			

//			
//			mgc.gridwidth = 1;
//			pane.add (new JLabel("开通年份" ),mgc);		
//			mgc.gridwidth =2;
//			pane.add(startyear,mgc);		
//			mgc.gridwidth = 3;
//			pane.add (new JLabel("竣工年份"),mgc);	
//			mgc.gridwidth = GridBagConstraints.REMAINDER;
//			pane.add(endyear,mgc);
			
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
			

			
//			mgc.gridwidth = 3;
//			JLabel jj1=new JLabel("长度/km" );
//			jj1.setFont(f);
//			pane.add (jj1,mgc);	
//			mgc.gridwidth = GridBagConstraints.REMAINDER;
//			pane.add(linklength,mgc);
			
			mgc.gridwidth = 1;
			JLabel length=new JLabel("长度/km");
			length.setFont(f);
			pane.add (length,mgc);
			mgc.gridwidth = 2;
			pane.add(linklength,mgc);
			mgc.gridwidth =  3;
			JLabel shuai=new JLabel("光纤衰减");
			shuai.setFont(f);
			pane.add(shuai,mgc);
			mgc.gridwidth=GridBagConstraints.REMAINDER;
			pane.add(shuaijian,mgc);
			

			mgc.gridwidth = 1;
			JLabel p=new JLabel("光纤PMD");
			p.setFont(f);
			pane.add(p,mgc);		
			mgc.gridwidth = 2;
			pane.add (pmd,mgc);
			mgc.gridwidth=3;
			JLabel jie=new JLabel("光纤阶段");
			jie.setFont(f);
			pane.add(jie,mgc);
			mgc.gridwidth = GridBagConstraints.REMAINDER;
			pane.add ( jieduan,mgc);
			
			mgc.gridwidth = 1;
			JLabel aname=new JLabel("链路别名");
			aname.setFont(f);
			pane.add(aname,mgc);		
			mgc.gridwidth = 2;
			pane.add (anothername,mgc);
			mgc.gridwidth=3;
			JLabel gtype=new JLabel("光纤类型");
			gtype.setFont(f);
			pane.add(gtype,mgc);
			mgc.gridwidth = GridBagConstraints.REMAINDER;
			pane.add ( typecombo,mgc);
			
			mgc.gridwidth = 1;
			JLabel jl5=new JLabel("所属层");
			jl5.setFont(f);
			pane.add(jl5,mgc);
			mgc.gridwidth = 2;
			pane.add (layerfiber ,mgc);
//			pane.add (layerwdm ,mgc);
//			mgc.gridwidth = 3;
//			pane.add(new JLabel("维护单位"),mgc);	
			mgc.gridwidth = GridBagConstraints.REMAINDER;
			pane.add(layerwdm,mgc);


			mgc.gridwidth = 1;
			pane.add(new JLabel("  "),mgc);	
			mgc.gridwidth = 2;
			insert = new Insets(0,-70,0,0);
			mgc.insets = insert;
			pane.add (confirmButton ,mgc);
			insert = new Insets(0,-30,0,0);
			mgc.insets = insert;
			mgc.gridwidth = GridBagConstraints.REMAINDER;
			pane.add(cancel,mgc);
			
			pane.setVisible(true);
			frame.setContentPane(pane);
			frame.setVisible(true);
			
	    }
	 private static double EARTH_RADIUS = 6378.137;
	 public static double GetDistance(double lat1, double lng1, double lat2, double lng2)
		{
		   double radLat1 = rad(lat1);
		   double radLat2 = rad(lat2);
		   double a = radLat1 - radLat2;
		   double b = rad(lng1) - rad(lng2);
		   double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
		    Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
		   s = s * EARTH_RADIUS;
		   s = Math.round(s * 10000) / 10000;
		   return s;
		}
	 private static double rad(double d)
		{
		   return d * Math.PI / 180.0;
		}
	 
	 private boolean linkNameSame(String name,Layer layer){
							boolean isSame=false;
							switch(layer){
							case Fiber:
								for(int i=0;i<FiberLink.fiberLinkList.size();++i){
									if(FiberLink.fiberLinkList.get(i).getName().equals(name))
									{
										isSame= true;
										break;
									}
								}
								break;
							case WDM:
								for(int i=0;i<WDMLink.WDMLinkList.size();++i){
									if(WDMLink.WDMLinkList.get(i).getName().equals(name))
									{
										isSame=  true;
										break;
									}
								}
								break;
							default:
								break;
							}
							return isSame;
						}
					    
					    public boolean isRepeat(String id,Layer layer){
					    	boolean isRepeat=false;
							switch(layer){
							case Fiber:
								for(int i=0;i<FiberLink.fiberLinkList.size();++i){
									if(FiberLink.fiberLinkList.get(i).getId()==(Integer.parseInt(id)))
									{
										isRepeat= true;
										break;
									}
								}
								break;
							case WDM:
								for(int i=0;i<WDMLink.WDMLinkList.size();++i){
									if(WDMLink.WDMLinkList.get(i).getId()==(Integer.parseInt(id)))
									{
										isRepeat=  true;
										break;
									}
								}
								break;
							default:
								break;
							}
							return isRepeat;
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

						
}
