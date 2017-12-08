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

import twaver.Dummy;
import twaver.TDataBox;
import datastructure.UINode;
import data.CommonNode;
import dataControl.NodeData;
import design.NetDesign_zs;
import enums.NodeType;
import test.design;

public class Dlg_AddROADMNode extends JDialog implements ItemListener{
//    private JTextField nodename = new JTextField("新建节点" + Integer.toString(CommonNode.getNode(ID) + 1), 6);
//    private JTextField nodeid = new JTextField(Integer.toString(CommonNode.getBiggestID() + 1), 6);
	private JFrame frame;
	private JTextField nodename;
	private JTextField nodeid;
	private JTextField dlongitude;
    private JTextField dlatitude;
    private JTextField jtupDown;
    private JTextField jtWSS ;
    private JTextField jtothername;
    private JLabel jl1;
    private JLabel jl2;
    private JLabel jl3;
    private JLabel jl4;
    private JLabel jl6;
    private JButton confirm, cancel;
    private String nodeName, nodeId;
    private String dLongitude, dLatitude;
    private double num_x, num_y, bitErrorRate, crossCapacity, equipmentLife, usingYears, nodeFaultRate;
    private TDataBox box1;
    private Dummy nodedummy1;
    private int id, year, layer, childnum;
    private int subnetNum;
    private boolean Roadm, OLA,zhongji,feizhongji;
    
    public Dlg_AddROADMNode(Frame owner,TDataBox box, Dummy nodeDummy, Double x, Double y) {
    super(owner, "添加ROADM节点", true);
	box1 = box;
	nodedummy1 = nodeDummy;
	
	JLabel nodenameLabel ;
	JLabel nodeidLabel ;
	JLabel LongitudeLabel;
	JLabel LatitudeLabel;
	///JLabel nullLabel = new JLabel("    ");
	JLabel jltype ;
	JLabel upDown;
	JLabel wss ;
	JLabel othername;
	JRadioButton jc1,jc3,jc4;
	///JRadioButton jc3 = new JRadioButton();//中继
	///JRadioButton jc4 = new JRadioButton();//非中继
	
	frame = new JFrame();
	frame.getContentPane().setLayout(null);
	
	nodename = new JTextField("节点名称");
	nodename.setBounds(125, 66, 66, 21);
	frame.getContentPane().add(nodename);
	nodename.setColumns(10);
	
	nodeid = new JTextField("节点ID");
	nodeid.setBounds(125, 20, 66, 21);
	frame.getContentPane().add(nodeid);
	nodeid.setColumns(10);
	
	//dlongitude = new JTextField(6);
	//dlongitude.setBounds(327, 20, 66, 21);
	//frame.getContentPane().add(dlongitude);
	//dlongitude.setColumns(10);
	
	//dlatitude = new JTextField(6);
	//dlatitude.setBounds(327, 66, 66, 21);
	//frame.getContentPane().add(dlatitude);
	//dlatitude.setColumns(10);
	
	double longitude = x;
	double latitude = y;
	if (!(x == 0 && y == 0)) {
	    DecimalFormat df = new DecimalFormat("#.0000");
	    dlongitude = new JTextField(df.format(longitude), 6);
	    dlongitude.setBounds(327, 20, 66, 21);
		frame.getContentPane().add(dlongitude);
		dlongitude.setColumns(10);
	    dlatitude = new JTextField(df.format(latitude), 6);
	    dlatitude.setBounds(327, 66, 66, 21);
		frame.getContentPane().add(dlatitude);
		dlatitude.setColumns(10);
	}
	
	jtupDown = new JTextField("20");
	jtupDown.setBounds(221, 163, 66, 21);
	frame.getContentPane().add(jtupDown);
	jtupDown.setColumns(10);
	
	jtWSS = new JTextField("9");
	jtWSS.setBounds(327, 120, 66, 21);
	frame.getContentPane().add(jtWSS);
	jtWSS.setColumns(10);
	
	jtothername = new JTextField("节点别名");
	jtothername.setBounds(125, 120, 66, 21);
	frame.getContentPane().add(jtothername);
	jtothername.setColumns(10);
	
	confirm = new JButton("确定");
	confirm.setBounds(125, 227, 66, 23);
	frame.getContentPane().add(confirm);
	
	cancel = new JButton("取消");
	cancel.setBounds(250, 227, 66, 23);
	frame.getContentPane().add(cancel);
	
	 nodenameLabel = new JLabel("节点名称");
	nodenameLabel.setBounds(39, 69, 54, 15);
	frame.getContentPane().add(nodenameLabel);
	
	 nodeidLabel = new JLabel("\u8282\u70B9ID");
	nodeidLabel.setBounds(39, 23, 54, 15);
	frame.getContentPane().add(nodeidLabel);
	
	 LongitudeLabel = new JLabel("\u7ECF\u5EA6");
	LongitudeLabel.setBounds(250, 23, 54, 15);
	frame.getContentPane().add(LongitudeLabel);
	
	 LatitudeLabel = new JLabel("\u7EAC\u5EA6");
	LatitudeLabel.setBounds(250, 69, 54, 15);
	frame.getContentPane().add(LatitudeLabel);
	
	 jltype = new JLabel("是否有中继能力");
	jltype.setBounds(39, 202, 126, 15);
	frame.getContentPane().add(jltype);
	
	upDown = new JLabel("本地上下路分组数");
	upDown.setBounds(39, 166,126, 15);
	frame.getContentPane().add(upDown);
	
	 wss = new JLabel("WSS维数");
	wss.setBounds(250, 123, 54, 15);
	frame.getContentPane().add(wss);
	
	 othername = new JLabel("节点别名");
	othername.setBounds(39, 123, 54, 15);
	frame.getContentPane().add(othername);
	
	 jc3 = new JRadioButton("\u662F");
	 jc3.setBounds(190, 198, 54, 23);
	frame.getContentPane().add(jc3);
	
	jc4 = new JRadioButton("\u5426");
	jc4.setBounds(286, 198, 54, 23);
	frame.getContentPane().add(jc4);
	
	
	Font f = new Font("微软雅黑", 0, 12);
	confirm.setFont(f);
	cancel.setFont(f);
	Dimension buttonsize = new Dimension(50, 28);

	nodenameLabel.setFont(f);
	nodeidLabel.setFont(f);
	LongitudeLabel.setFont(f);
	LatitudeLabel.setFont(f);
	jltype.setFont(f);
	upDown.setFont(f);
	wss.setFont(f);
	othername.setFont(f);

	jc1 = new JRadioButton("ROADM节点");

	ButtonGroup nodegroup=new ButtonGroup();
	ButtonGroup roadmgroup=new ButtonGroup();
	jc1.setFont(f);

	jc3.setFont(f);
	jc4.setFont(f);

	

	confirm.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		nodeId = nodeid.getText();
		nodeName = nodename.getText();
		dLongitude = dlongitude.getText();
		dLatitude = dlatitude.getText();
		String updownnum = jtupDown.getText();
		String wssnum = jtWSS.getText();
		String aname = jtothername.getText();
		

		if (!isInteger(nodeId))
		    JOptionPane.showMessageDialog(null, "节点ID应为非空数字！");
		else if (isIDExisted(nodeId))
		    JOptionPane.showMessageDialog(null, "所添加节点ID已存在！");
		// else if(ifExisted(nodeId))
		// JOptionPane.showMessageDialog(null,"所添加节点ID已存在！");
		else if (nodeName.isEmpty())
		    JOptionPane.showMessageDialog(null, "请添加节点名称！");
		else if (isNameRepeat(nodeName))
		    JOptionPane.showMessageDialog(null, "节点名称重复！");
		else if (!(isNumber(dLongitude) & isNumber(dLatitude)))
		    JOptionPane.showMessageDialog(null, "请正确输入节点经纬度！");
		else {
		    Integer data = new Integer(nodeId);
		    id = data.intValue();
		    num_x = Double.valueOf(dLongitude);
		    num_y = Double.valueOf(dLatitude);

		    NodeData node1 = new NodeData();
			if(jc3.isSelected()){
				Roadm=true;
				zhongji=true;
				node1.addROADMNode(Integer.parseInt(nodeId), nodeName, aname, Double.parseDouble(dLongitude), Double.parseDouble(dLatitude), true, Integer.parseInt(wssnum),Integer.parseInt(updownnum));
			}
			if(jc4.isSelected()){
				OLA=true;
				feizhongji=true;
				node1.addROADMNode(Integer.parseInt(nodeId), nodeName, aname, Double.parseDouble(dLongitude), Double.parseDouble(dLatitude), false, Integer.parseInt(wssnum),Integer.parseInt(updownnum));
			}   
//			if(jc2.isSelected()){
//				node1.addOLANode(Integer.parseInt(nodeId), nodeName, aname, Double.parseDouble(dLongitude), Double.parseDouble(dLatitude));
//			}
			
		    addNode(CommonNode.allNodeList.get(CommonNode.allNodeList.size()-1),id);
		    frame.dispose();// 释放窗体所占内存资源?

//		     addNode(CommonNode.m_lCommonNode.get(CommonNode.m_lCommonNode.size()-1),id);
		    // CommonNode.m_lCommonNode.get(CommonNode.m_lCommonNode.size()-1).setM_nProvince(ChooseMap.whichmap);

		}
	    }
	});

	cancel.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		frame.setVisible(false);
		frame.dispose();// 释放窗体所占内存资源?
	    }
	});
	


	roadmgroup.add(jc3);
	roadmgroup.add(jc4);
	jc4.setSelected(true);
	
	frame.setTitle("添加ROADM节点");
	frame.setSize(470, 300);
	frame.setLocation(360, 190);
	ImageIcon imageIcon = new ImageIcon(frame.getClass().getResource("/resource/ddd1111.png"));
	frame.setIconImage(imageIcon.getImage());
	frame.setVisible(true);
	
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

    /*
     * 判断所输入ID是否已存在 ,在fiber层判断
     */
    public boolean isIDExisted(String ID) {
	Integer data3 = new Integer(nodeId);
	int id = data3.intValue();
	int i = 0;
	int n = CommonNode.allNodeList.size();
	for (i = 0; i < n; i++) {
	    if (CommonNode.allNodeList.get(i).getId() == id)
		break;
	}
	if (i == n)
	    return false;
	else
	    return true;
	// 检查是否有重复ID，如果有返回true

    }

    public boolean isNameRepeat(String name) {
	int i = 0;
	int n = CommonNode.allNodeList.size();
	for (i = 0; i < n; i++) {
	    if (CommonNode.allNodeList.get(i).getName().equals(name))
		break;
	}
	if (i == n)
	    return false;
	else
	    return true;
	// 检查是否有重复ID，如果有返回true

    }

    // 点确定完成操作
    public void addNode(CommonNode it, int id) {

	UINode addnew = new UINode(it);
	UINode.s_lUINodeList.add(addnew);
	nodedummy1.addChild(addnew);
	box1.addElement(addnew);
	dispose();
	setVisible(false);

    }

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stub
		
	}

    // public static void main(String args[]){
    // new Dlg_AddNode(null,null,null,null,0,0);
    // }
}
