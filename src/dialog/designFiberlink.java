package dialog;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Iterator;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import data.FiberLink;
import dataControl.LinkData;
import datastructure.UIFiberLink;
import design.NetDesign_zs;

public class designFiberlink extends JFrame {

	JLabel shuaijian, linktype, linkpmd, stage,jlname,id,layer,len,aname,firstnode,lastnode,j1, j2, j3, j4,j5,j6,j7,j8;
	public JFrame frame;
	public static JTextField jtshuaijian;
	public static JTextField jtlinktype;
	public static JTextField jtlinkpmd;
	public static JTextField jtstage;
	public static JTextField jtname;
	public static JTextField jtid;
	public static JTextField jtaname;
	public static JTextField jtlayer;
	public static JTextField jtlen;
	public static JTextField jtfirstnode;
	public static JTextField jtlastnode;
	
	JButton jb1, jb2;
	JPanel jp1, jp2, jp3;

	// @TY宇少爷 +2017/9/22
	// public static double fiberLoss;//光纤衰减
	// public static String linkType;//光纤类型
	// public static double linkPmd;//光纤PMD
	// public static int fiberstage;// 光纤阶段
	// -2017/9/22
	public designFiberlink(String name) {
		Font f = new Font("微软雅黑", Font.PLAIN, 12);
		Font f1 = new Font("微软雅黑", Font.BOLD, 12);
		
		
		frame = new JFrame();
		frame.getContentPane().setLayout(null);
		
		jlname = new JLabel("\u94FE\u8DEF\u540D\u79F0");
		jlname.setBounds(26, 26, 54, 15);
		frame.getContentPane().add(jlname);
		
		firstnode = new JLabel("\u9996\u8282\u70B9");
		firstnode.setBounds(26, 78, 54, 15);
		frame.getContentPane().add(firstnode);
		
		linktype = new JLabel("\u5149\u7EA4\u7C7B\u578B");
		linktype.setBounds(26, 128, 54, 15);
		frame.getContentPane().add(linktype);
		
		stage = new JLabel("\u5149\u7EA4\u9636\u6BB5");
		stage.setBounds(26, 177, 54, 15);
		frame.getContentPane().add(stage);
		
		layer = new JLabel("\u6240\u5C5E\u5C42");
		layer.setBounds(200, 26, 54, 15);
		frame.getContentPane().add(layer);
		
		lastnode = new JLabel("\u672B\u8282\u70B9");
		lastnode.setBounds(200, 78, 54, 15);
		frame.getContentPane().add(lastnode);
		
		shuaijian = new JLabel("\u5149\u7EA4\u8870\u51CF");
		shuaijian.setBounds(200, 128, 54, 15);
		frame.getContentPane().add(shuaijian);
		
		len = new JLabel("\u957F\u5EA6/km");
		len.setBounds(200, 177, 54, 15);
		frame.getContentPane().add(len);
		
		id = new JLabel("\u94FE\u8DEFID");
		id.setBounds(334, 26, 54, 15);
		frame.getContentPane().add(id);
		
		linkpmd = new JLabel("\u5149\u7EA4PMD");
		linkpmd.setBounds(334, 128, 54, 15);
		frame.getContentPane().add(linkpmd);
		
		jb1 = new JButton("\u786E\u5B9A");
		jb1.setBounds(128, 229, 93, 23);
		frame.getContentPane().add(jb1);
		
		jb2 = new JButton("\u53D6\u6D88");
		jb2.setBounds(258, 229, 93, 23);
		frame.getContentPane().add(jb2);
		
		jtname = new JTextField("");
		jtname.setBounds(95, 23, 66, 21);
		frame.getContentPane().add(jtname);
		jtname.setColumns(10);
		jtname.setEnabled(false);
		
		jtfirstnode = new JTextField("");
		jtfirstnode.setBounds(95, 75, 66, 21);
		frame.getContentPane().add(jtfirstnode);
		jtfirstnode.setColumns(10);
		jtfirstnode.setEnabled(false);
		
		jtlinktype = new JTextField("");
		jtlinktype.setBounds(95, 125, 66, 21);
		frame.getContentPane().add(jtlinktype);
		jtlinktype.setColumns(10);
		
		jtstage = new JTextField("");
		jtstage.setBounds(95, 174, 66, 21);
		frame.getContentPane().add(jtstage);
		jtstage.setColumns(10);
		
		jtlayer = new JTextField("");
		jtlayer.setBounds(258, 23, 66, 21);
		frame.getContentPane().add(jtlayer);
		jtlayer.setColumns(10);
		jtlayer.setEnabled(false);
		
		jtlastnode = new JTextField("");
		jtlastnode.setBounds(258, 75, 66, 21);
		frame.getContentPane().add(jtlastnode);
		jtlastnode.setColumns(10);
		jtlastnode.setEnabled(false);
		
		jtshuaijian = new JTextField("");
		jtshuaijian.setBounds(258, 125, 66, 21);
		frame.getContentPane().add(jtshuaijian);
		jtshuaijian.setColumns(10);
		
		jtlen = new JTextField("");
		jtlen.setBounds(258, 174, 66, 21);
		frame.getContentPane().add(jtlen);
		jtlen.setColumns(10);
		
		jtid = new JTextField("");
		jtid.setBounds(398, 23, 66, 21);
		frame.getContentPane().add(jtid);
		jtid.setColumns(10);
		jtid.setFont(f1);
		jtid.setEnabled(false);
		
		jtlinkpmd = new JTextField("");
		jtlinkpmd.setBounds(398, 125, 66, 21);
		frame.getContentPane().add(jtlinkpmd);
		jtlinkpmd.setColumns(10);
		
		//jtname.setFont(f1);
		//jtaname.setFont(f1);
		//jtid.setFont(f1);
		//jtfirstnode.setFont(f1);
		//jtlastnode.setFont(f1);
		//jtlayer.setFont(f1);
		///jb1 = new JButton("确定");
		// jb1.addActionListener(this);
		// @TY宇 +2017/9/22
		frame.setSize(500,320);
		frame.setLocation(360, 190);
		frame.setTitle("Fiber层链路编辑");
		ImageIcon imageIcon = new ImageIcon(frame.getClass().getResource("/resource/ddd1111.png"));
		frame.setIconImage(imageIcon.getImage());
		//frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		
		jb1.addActionListener(new ActionListener() {//改后数据
			public void actionPerformed(ActionEvent e) {
				FiberLink fiberlink=FiberLink.getLink(name);
				// 光纤衰减的编辑
				fiberlink.setAttenuation(Double.parseDouble(jtshuaijian.getText())); //衰减
				// 光纤类型的编辑
				fiberlink.setLinkType( jtlinktype.getText());//光纤类型
				// 光纤PDM的编辑
				fiberlink.setPMD(Double.parseDouble(jtlinkpmd.getText())); //pmd
				// 光纤阶段的编辑
				fiberlink.setFiberStage(Integer.parseInt(jtstage.getText()));//光纤阶段
				fiberlink.setLength(Double.parseDouble(jtlen.getText()));   //长度

				frame.setVisible(false);// 消失窗口
				frame.dispose();// 释放窗口所占资源
			}

		});
		///jb2 = new JButton("取消");
		jb2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);// 消失窗口
				frame.dispose();// 释放窗口所占资源
			}
		});
		// - 2017/9/22
		
		
	}

	// @Override
	// public void actionPerformed(ActionEvent arg0) {
	// // TODO Auto-generated method stub
	// this.dispose();
	// }
	//
}
