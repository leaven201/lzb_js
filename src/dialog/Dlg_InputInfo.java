package dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import twaver.Dummy;
import twaver.TDataBox;
import dataControl.LinkData;
import dataControl.NodeData;
import dataControl.PortData;
import dataControl.TrafficData;

public class Dlg_InputInfo extends JFrame {

	public Dlg_InputInfo(final TDataBox box1,final Dummy nodeDummy,final Dummy fiberLinkDummy ,final Dummy satelliteLinkDummy,
    final Dummy shortWaveLinkDummy,final Dummy TrafficDummy,final int Resource,int status){
		//Resource表示当前导入的是什么资源，0为网元，1为业务
		//status表示导入全部资源时输出导入消息的状态，节点1，链路0，both2
	    this.setTitle("导入");
	    setSize(400, 200);
	    this.setLocationRelativeTo(null);
	    ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/resource/ddd1111.png"));
		this.setIconImage(imageIcon.getImage());
		Container contentPane = this.getContentPane();
		ImageIcon iconinfo = new ImageIcon(getClass().getResource("/resource/information.png"));
		JLabel iconPane = new JLabel(iconinfo);//信息图标
		JPanel infoPane = new JPanel();//信息部分
		Color backg = infoPane.getBackground();
		JPanel buttonPane = new JPanel();//按钮部分
		JTextArea pane2 = new JTextArea(5,29);
		pane2.setAlignmentX(CENTER_ALIGNMENT);//2017.1.3 居中
		pane2.setAlignmentY(CENTER_ALIGNMENT);
		pane2.setEditable(false);
	    pane2.setFont(new java.awt.Font("宋体", Font.BOLD, 12));
		pane2.setBackground(backg);
		JScrollPane scroll = new JScrollPane(pane2);
		scroll.setBorder(BorderFactory.createEmptyBorder());
		boolean traerr = TrafficData.s_sTrafficMsg.startsWith("业务表格第")||TrafficData.s_sTrafficMsg.startsWith("由于未成功");
//		boolean traerr = false;
		boolean nodeerr = NodeData.s_sNodeMsg.startsWith("节点表格第");
		boolean linkerr = LinkData.s_sFiberMsg.startsWith("链路表格第");
	    if(Resource == 1){
	    	if(traerr){
	    	    setSize(480, 200);
	    	    this.setLocationRelativeTo(null);
	    	    pane2.setRows(6);
	    	    pane2.setColumns(45);
	    	    pane2.setText(TrafficData.s_sTrafficMsg);
	    	}
	    	else
	    		pane2.setText("\n\n "+TrafficData.s_sTrafficMsg);//2017.1.3 居中
			BorderLayout mainPaneLayout = new BorderLayout();
		    contentPane.setLayout(mainPaneLayout);
	    	JPanel p = new JPanel();
		    new GridBagConstraints();	
		    infoPane.setLayout(new BorderLayout() );

			infoPane.add(scroll,BorderLayout.CENTER); //东、西、南、北中


	    	//设置上半部分布局
		    GridBagLayout mg = new GridBagLayout();
		    GridBagConstraints mgc = new GridBagConstraints();	
		    Insets insert;
		    p.setLayout(mg);
			mgc.gridwidth  = 1;
			mgc.weightx = 1.0;
			mgc.anchor = GridBagConstraints.WEST;
			insert = new Insets(0,15,0,5);
			mgc.insets = insert;
			p.add(iconPane,mgc);
			mgc.fill = GridBagConstraints.BOTH;
			mgc.weightx = 5.0;
			mgc.gridwidth = GridBagConstraints.REMAINDER;
			insert = new Insets(5,10,0,0);
			mgc.insets = insert;
			p.add(infoPane,mgc);
			//设置按键位置
		    JButton button = new JButton("确定");
		    buttonPane.setLayout(mg);
		    mgc.gridwidth = 2;
		    mgc.weighty = 1.0;
		    insert = new Insets(0,143,10,0);
			mgc.insets = insert;
			mgc.fill = GridBagConstraints.NONE;	
		    buttonPane.add(button,mgc);
		    
		    contentPane.add(p);
		    contentPane.add(buttonPane,BorderLayout.SOUTH);
		    setVisible(true);
		    this.addWindowListener(new WindowAdapter(){
		    	public void windowClosing(WindowEvent e){
		            setVisible(false);
		            dispose();
		            Dlg_TrafficInput.inputUITrafficData(box1,TrafficDummy);
		    	}
		    });
		    button.addActionListener(new ActionListener(){
		    	public void actionPerformed(ActionEvent e){	
		            setVisible(false);
		            dispose();
		            Dlg_TrafficInput.inputUITrafficData(box1,TrafficDummy);
		            }		    	
		    });
	    	
	    }
	    else{//导入网元信息
		    if(status == 1){//节点信息
		    	setSize(400, 190);
		    	if(nodeerr){
		    		setSize(520,260);
			    	pane2.setRows(9);
			    	pane2.setColumns(50);
		    	}
		    	this.setLocationRelativeTo(null);
		    	ImageIcon imageIcon2 = new ImageIcon(this.getClass().getResource("/resource/ddd1111.png"));
				this.setIconImage(imageIcon2.getImage());
				BorderLayout mainPaneLayout = new BorderLayout();
			    contentPane.setLayout(mainPaneLayout);
			    JPanel p = new JPanel();
			    pane2.setText("\n\n" +NodeData.s_sNodeMsg+"\n ");//2017.1.3 居中
			    new GridBagLayout();
			    new GridBagConstraints();	
			    infoPane.setLayout(new BorderLayout() );

				infoPane.add(scroll,BorderLayout.CENTER); //东、西、南、北中
		    	//设置上半部分布局
			    GridBagLayout mg = new GridBagLayout();
			    GridBagConstraints mgc = new GridBagConstraints();	
			    Insets insert;
			    p.setLayout(mg);
				mgc.gridwidth  = 1;
				mgc.anchor = GridBagConstraints.WEST;
				insert = new Insets(0,20,0,0);
				mgc.insets = insert;
				p.add(iconPane,mgc);
				mgc.gridwidth = GridBagConstraints.REMAINDER;
				insert = new Insets(0,10,0,0);
				mgc.insets = insert;
				mgc.fill = GridBagConstraints.BOTH;
				p.add(infoPane,mgc);
				//设置按键位置
			    JButton button = new JButton("继续");
			    buttonPane.setLayout(mg);
			    mgc.gridwidth = 1;
			    mgc.weighty = 1.0;
			    insert = new Insets(0,0,10,0);
				mgc.insets = insert;
				mgc.fill = GridBagConstraints.NONE;	
			    buttonPane.add(button,mgc);
			    
			    contentPane.add(p);
			    contentPane.add(buttonPane,BorderLayout.SOUTH);
			    setVisible(true);
			    this.addWindowListener(new WindowAdapter(){
			    	public void windowClosing(WindowEvent e){
			            setVisible(false);
			            dispose();
			            Dlg_InputNodes.inputUINodeData(box1, nodeDummy);
			            Dlg_InputLinks.inputUILinkData(box1,fiberLinkDummy, satelliteLinkDummy, shortWaveLinkDummy);
			    	}
			    });
			    button.addActionListener(new ActionListener(){
			    	public void actionPerformed(ActionEvent e){	
			            setVisible(false);
			            dispose();
			            Dlg_InputNodes.inputUINodeData(box1, nodeDummy);
			            }
			    	
			    });
		    }
		    else if(status == 0){//链路信息
		        setSize(400, 190);
		        if(linkerr){
			        setSize(520, 280);//2017.1.3
			        pane2.setRows(8);
			    	pane2.setColumns(52);
		        }
			    this.setLocationRelativeTo(null);
			    ImageIcon imageIcon1 = new ImageIcon(this.getClass().getResource("/resource/ddd1111.png"));
				this.setIconImage(imageIcon1.getImage());
				BorderLayout mainPaneLayout = new BorderLayout();
			    contentPane.setLayout(mainPaneLayout);
			    JPanel p = new JPanel();
			    pane2.setText("\n\n"+LinkData.s_sFiberMsg+"\n");//2017.1.3 居中
			    JButton button = new JButton("确定");
			    new GridBagLayout();
			    new GridBagConstraints();	
			    infoPane.setLayout(new BorderLayout() );

				infoPane.add(scroll,BorderLayout.CENTER); //东、西、南、北中
		    	
		    	//设置上半部分布局
			    GridBagLayout mg = new GridBagLayout();
			    GridBagConstraints mgc = new GridBagConstraints();	
			    Insets insert;
			    p.setLayout(mg);
				mgc.gridwidth  = 1;
				mgc.anchor = GridBagConstraints.WEST;
				insert = new Insets(0,20,0,0);
				mgc.insets = insert;
				p.add(iconPane,mgc);
				mgc.gridwidth = GridBagConstraints.REMAINDER;
				mgc.anchor = GridBagConstraints.CENTER;
				insert = new Insets(0,10,0,0);
				mgc.insets = insert;
				p.add(infoPane,mgc);
				//设置按键位置
			    buttonPane.setLayout(mg);
			    mgc.gridwidth = 1;
			    mgc.weighty = 1.0;
			    insert = new Insets(0,0,10,0);
				mgc.insets = insert;
				mgc.fill = GridBagConstraints.NONE;	
			    buttonPane.add(button,mgc);
			    
			    contentPane.add(p);
			    contentPane.add(buttonPane,BorderLayout.SOUTH);
			    
			    setVisible(true);
			    this.addWindowListener(new WindowAdapter(){
			    	public void windowClosing(WindowEvent e){
			            setVisible(false);
			            dispose();
			            Dlg_InputLinks.inputUILinkData(box1,fiberLinkDummy, satelliteLinkDummy, shortWaveLinkDummy);
			    	}
			    });
			    button.addActionListener(new ActionListener(){
		    	public void actionPerformed(ActionEvent e){	
		            setVisible(false);
		            dispose();
		            Dlg_InputLinks.inputUILinkData(box1,fiberLinkDummy, satelliteLinkDummy, shortWaveLinkDummy);
		            }		    	
		    });
		    }
		    else if(status==2){//节点+链路
		    	setSize(400, 190);
		    	if(nodeerr){
		    		setSize(520,260);
			    	pane2.setRows(9);
			    	pane2.setColumns(50);
		    	}
		    	this.setLocationRelativeTo(null);
		    	ImageIcon imageIcon3 = new ImageIcon(this.getClass().getResource("/resource/ddd1111.png"));
				this.setIconImage(imageIcon3.getImage());
				BorderLayout mainPaneLayout = new BorderLayout();
			    contentPane.setLayout(mainPaneLayout);
			    JPanel p = new JPanel();
			    pane2.setText("\n\n"+NodeData.s_sNodeMsg+"\n ");//2017.1.3 居中
			    new GridBagLayout();
			    new GridBagConstraints();	
			    infoPane.setLayout(new BorderLayout() );

				infoPane.add(scroll,BorderLayout.CENTER); //东、西、南、北中
		    	//设置上半部分布局
			    GridBagLayout mg = new GridBagLayout();
			    GridBagConstraints mgc = new GridBagConstraints();	
			    Insets insert;
			    p.setLayout(mg);
				mgc.gridwidth  = 1;
				mgc.anchor = GridBagConstraints.WEST;
				insert = new Insets(0,20,0,0);
				mgc.insets = insert;
				p.add(iconPane,mgc);
				mgc.gridwidth = GridBagConstraints.REMAINDER;
				insert = new Insets(0,10,0,0);
				mgc.insets = insert;
				mgc.fill = GridBagConstraints.BOTH;
				p.add(infoPane,mgc);
				//设置按键位置
			    JButton button = new JButton("继续");
			    buttonPane.setLayout(mg);
			    mgc.gridwidth = 1;
			    mgc.weighty = 1.0;
			    insert = new Insets(0,0,10,0);
				mgc.insets = insert;
				mgc.fill = GridBagConstraints.NONE;	
			    buttonPane.add(button,mgc);
			    
			    contentPane.add(p);
			    contentPane.add(buttonPane,BorderLayout.SOUTH);
			    setVisible(true);
			    this.addWindowListener(new WindowAdapter(){
			    	public void windowClosing(WindowEvent e){
			            setVisible(false);
			            dispose();
			            Dlg_InputNodes.inputUINodeData(box1, nodeDummy);
			            Dlg_InputLinks.inputUILinkData(box1,fiberLinkDummy, satelliteLinkDummy, shortWaveLinkDummy);
			    	}
			    });
			    button.addActionListener(new ActionListener(){
			    	public void actionPerformed(ActionEvent e){	
			            setVisible(false);
			            dispose();
			            Dlg_InputNodes.inputUINodeData(box1, nodeDummy);
			            new Dlg_InputInfo(box1,nodeDummy,fiberLinkDummy ,satelliteLinkDummy,shortWaveLinkDummy,TrafficDummy,Resource,0);
			            }
			    	
			    });
		    }
	    }
	}

}
