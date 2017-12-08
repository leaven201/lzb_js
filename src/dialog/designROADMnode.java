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

import data.CommonNode;
import design.NetDesign_zs;
import enums.NodeType;

public class designROADMnode extends JFrame implements ActionListener{

	JLabel id,name,type,jin,wei,zj,j1,j2,j3,j4,shangxiazu,wss,aname;
	public JFrame frame;
	public static JTextField jtid;
//	public static JTextField jtzj;
	public static JTextField jttype;
	public static JTextField jtjin;
	public static JTextField jtwei;
	public static JTextField jtname;
	public static JTextField jtaname;
	public static JTextField jtshangxiazu;
	public static JTextField jtwss;
	JButton jb1,jb2;
	JPanel jp1,jp2,jp3,jp4,jp5;
	public static JRadioButton jc3 ;
	public static JRadioButton jc4 ;
    ButtonGroup roadmgroup=new ButtonGroup();
    
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	//	designROADMnode d1 =new designROADMnode();
	}

	public designROADMnode(int mark)
	{
		Font f=new Font("微软雅黑", Font.PLAIN, 12);
		frame = new JFrame();
		frame.getContentPane().setLayout(null);
		
		jtid = new JTextField("");
		jtid.setBounds(105, 23, 66, 21);
		frame.getContentPane().add(jtid);
		jtid.setColumns(10);
		jtid.setEditable(false);
		
		jtname = new JTextField("");
		jtname.setBounds(298, 23, 66, 21);
		frame.getContentPane().add(jtname);
		jtname.setColumns(10);
		jtname.setEditable(false);
		
		jttype = new JTextField("");
		jttype.setBounds(105, 79, 66, 21);
		frame.getContentPane().add(jttype);
		jttype.setColumns(10);
		jttype.setEditable(false);
		
		jtshangxiazu = new JTextField("");
		jtshangxiazu.setBounds(183, 135, 66, 21);
		frame.getContentPane().add(jtshangxiazu);
		jtshangxiazu.setColumns(10);
		
		jtwss = new JTextField("");
		jtwss.setBounds(298, 79, 66, 21);
		frame.getContentPane().add(jtwss);
		jtwss.setColumns(10);
		
		 id = new JLabel("ID");
		id.setBounds(44, 26, 54, 15);
		frame.getContentPane().add(id);
		
		 name = new JLabel("\u540D\u79F0");
		name.setBounds(229, 26, 54, 15);
		frame.getContentPane().add(name);
		
		type = new JLabel("\u7AD9\u70B9\u7C7B\u578B");
		type.setBounds(22, 82, 54, 15);
		frame.getContentPane().add(type);
		
		zj = new JLabel("\u4E2D\u7EE7");
		zj.setBounds(44, 187, 54, 15);
		frame.getContentPane().add(zj);
		
		shangxiazu = new JLabel("\u672C\u5730\u4E0A\u4E0B\u7EC4\u5206\u7EC4\u6570");
		shangxiazu.setBounds(22, 138, 129, 15);
		frame.getContentPane().add(shangxiazu);
		
		 wss = new JLabel("WSS\u7EF4\u6570");
		wss.setBounds(218, 82, 54, 15);
		frame.getContentPane().add(wss);
		
		 jb1 = new JButton("\u786E\u5B9A");
		jb1.setBounds(101, 229, 93, 23);
		frame.getContentPane().add(jb1);
		
		jb2 = new JButton("\u53D6\u6D88");
		jb2.setBounds(234, 229, 93, 23);
		frame.getContentPane().add(jb2);
		
		jc3 = new JRadioButton("\u662F");
		jc3.setBounds(128, 183, 93, 23);
		frame.getContentPane().add(jc3);
		
		jc4 = new JRadioButton("\u5426");
		jc4.setBounds(268, 183, 73, 23);
		frame.getContentPane().add(jc4);
		
		roadmgroup.add(jc3);
		roadmgroup.add(jc4);
		///jp4.add(jc3);
		///jp4.add(jc4);
		if(jttype.getText()=="OLA"){
			jc3.setSelected(false);;
			jc4.setSelected(false);
		}
		jc4.setSelected(true);
		
		frame.setSize(450,300);
		frame.setLocation(360, 190);
		frame.setTitle("编辑ROADM节点");
		ImageIcon imageIcon = new ImageIcon(frame.getClass().getResource("/resource/ddd1111.png"));
		frame.setIconImage(imageIcon.getImage());
		//frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		id.setFont(f);
		name.setFont(f);
		type.setFont(f);
		///jin.setFont(f);
		///wei.setFont(f);
		zj.setFont(f);
		jb1.setFont(f);
		jb2.setFont(f);
		jc3.setFont(f);
		jc4.setFont(f);
		///aname.setFont(f);
		shangxiazu.setFont(f);
		wss.setFont(f);
		
		jb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CommonNode commonnode=CommonNode.getROADMNode(mark);
	//			commonnode.setId(Integer.parseInt(jtid.getText()));
	//			commonnode.setName( jtname.getText());
	//			commonnode.setOtherName(jtaname.getText());
	//			commonnode.setLongitude(Double.parseDouble(jtjin.getText())); 
	//			commonnode.setLatitude(Double.parseDouble(jtwei.getText())); 
	//			commonnode.setType(NodeType.stringToNodeType(jttype.getText())); 
				commonnode.setWSS(Integer.parseInt(jtwss.getText()));
				commonnode.setUpDown(Integer.parseInt(jtshangxiazu.getText()));
				commonnode.setiszhongji(jc3.isSelected());
				frame.dispose();// 释放窗口资源
			}
		});
		///jb2 = new JButton("取消");
		jb2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);// 消失窗口
				frame.dispose();// 释放窗口资源
			}
		});
		// -2017/9/22
		
		
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		frame.dispose();
	}
}
