package dialog;

import java.awt.Dimension;
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

public class Dlg_AddOLANode extends JDialog implements ItemListener{
//    private JTextField nodename = new JTextField("�½��ڵ�" + Integer.toString(CommonNode.getNode(ID) + 1), 6);
//    private JTextField nodeid = new JTextField(Integer.toString(CommonNode.getBiggestID() + 1), 6);
	private JFrame frame;
	private JTextField nodename = new JTextField("�ڵ�����");
	private JTextField nodeid = new JTextField("�ڵ�ID");
	private JTextField dlongitude = new JTextField(6);
    private JTextField dlatitude = new JTextField(6);
    private JTextField jtupDown = new JTextField("20");
    private JTextField jtWSS = new JTextField("9");
    private JTextField jtothername = new JTextField("�ڵ����");
    ///private JLabel jl1=new JLabel("");
    ///private JLabel jl2=new JLabel("");
    ///private JLabel jl3=new JLabel("");
    ///private JLabel jl4=new JLabel("");
   
    private JLabel jl6=new JLabel("");
    private JButton confirm, cancel;
    private String nodeName, nodeId;
    private String dLongitude, dLatitude;
    private double num_x, num_y, bitErrorRate, crossCapacity, equipmentLife, usingYears, nodeFaultRate;
    private TDataBox box1;
    private Dummy nodedummy1;
    private int id, year, layer, childnum;
    private int subnetNum;
    private boolean Roadm, OLA,zhongji,feizhongji;

    public Dlg_AddOLANode(Frame owner, TDataBox box, Dummy nodeDummy, Double x, Double y) {
	///super(owner, "���OLA�ڵ�", true);
	box1 = box;
	nodedummy1 = nodeDummy;

	
	JLabel nodenameLabel ;
	JLabel nodeidLabel;
	JLabel LongitudeLabel ;
	JLabel LatitudeLabel ;
	JLabel nullLabel ;
	JLabel othername ;
	frame = new JFrame();
	frame.getContentPane().setLayout(null);
	
	nodename = new JTextField();
	nodename.setText("\u8282\u70B9\u540D\u79F0");
	nodename.setBounds(125, 90, 66, 21);
	frame.getContentPane().add(nodename);
	nodename.setColumns(10);
	
	nodeid = new JTextField();
	nodeid.setText("\u8282\u70B9ID");
	nodeid.setBounds(125, 37, 66, 21);
	frame.getContentPane().add(nodeid);
	nodeid.setColumns(10);
	
	jtothername = new JTextField();
	jtothername.setText("\u8282\u70B9\u522B\u540D");
	jtothername.setBounds(125, 147, 66, 21);
	frame.getContentPane().add(jtothername);
	jtothername.setColumns(10);
	
	//dlongitude = new JTextField();
	//dlongitude.setBounds(310, 37, 66, 21);
	//frame.getContentPane().add(dlongitude);
	//dlongitude.setColumns(10);
	
	//dlatitude = new JTextField();
	//dlatitude.setBounds(310, 90, 66, 21);
	//frame.getContentPane().add(dlatitude);
	//dlatitude.setColumns(10);
	
	
	double longitude = x;
	double latitude = y;
	if (!(x == 0 && y == 0)) {
	    DecimalFormat df = new DecimalFormat("#.0000");
	    dlongitude = new JTextField(df.format(longitude), 6);
	    dlongitude.setBounds(310, 37, 66, 21);
		frame.getContentPane().add(dlongitude);
		dlongitude.setColumns(10);
	    dlatitude = new JTextField(df.format(latitude), 6);
	    dlatitude.setBounds(310, 90, 66, 21);
		frame.getContentPane().add(dlatitude);
		dlatitude.setColumns(10);
	}
	
	
	confirm = new JButton("\u786E\u5B9A");
	confirm.setBounds(85, 202, 93, 23);
	frame.getContentPane().add(confirm);
	
	cancel = new JButton("\u53D6\u6D88");
	cancel.setBounds(240, 202, 93, 23);
	frame.getContentPane().add(cancel);
	
	 nodenameLabel = new JLabel("\u8282\u70B9\u540D\u79F0");
	nodenameLabel.setBounds(46, 93, 54, 15);
	frame.getContentPane().add(nodenameLabel);
	
	 nodeidLabel = new JLabel("\u8282\u70B9ID");
	nodeidLabel.setBounds(46, 40, 54, 15);
	frame.getContentPane().add(nodeidLabel);
	
	 othername = new JLabel("\u8282\u70B9\u522B\u540D");
	othername.setBounds(46, 147, 54, 15);
	frame.getContentPane().add(othername);
	
 LongitudeLabel = new JLabel("\u7ECF\u5EA6");
	LongitudeLabel.setBounds(233, 40, 54, 15);
	frame.getContentPane().add(LongitudeLabel);
	
	 LatitudeLabel = new JLabel("\u7EAC\u5EA6");
	LatitudeLabel.setBounds(233, 93, 54, 15);
	frame.getContentPane().add(LatitudeLabel);
	
	
	
	///confirm = new JButton("ȷ��");
	///cancel = new JButton("ȡ��");
	Font f = new Font("΢���ź�", Font.PLAIN, 12);
	///confirm.setFont(f);
	///cancel.setFont(f);
	Dimension buttonsize = new Dimension(50, 28);

	nodenameLabel.setFont(f);
	nodeidLabel.setFont(f);
	LongitudeLabel.setFont(f);
	LatitudeLabel.setFont(f);
//	jltype.setFont(f);
//	upDown.setFont(f);
//	wss.setFont(f);
	othername.setFont(f);
	


	

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
		    JOptionPane.showMessageDialog(null, "�ڵ�IDӦΪ�ǿ����֣�");
		else if (isIDExisted(nodeId))
		    JOptionPane.showMessageDialog(null, "����ӽڵ�ID�Ѵ��ڣ�");
		// else if(ifExisted(nodeId))
		// JOptionPane.showMessageDialog(null,"����ӽڵ�ID�Ѵ��ڣ�");
		else if (nodeName.isEmpty())
		    JOptionPane.showMessageDialog(null, "����ӽڵ����ƣ�");
		else if (isNameRepeat(nodeName))
		    JOptionPane.showMessageDialog(null, "�ڵ������ظ���");
		else if (!(isNumber(dLongitude) & isNumber(dLatitude)))
		    JOptionPane.showMessageDialog(null, "����ȷ����ڵ㾭γ�ȣ�");
		else {
		    Integer data = new Integer(nodeId);
		    id = data.intValue();
		    num_x = Double.valueOf(dLongitude);
		    num_y = Double.valueOf(dLatitude);

		    NodeData node1 = new NodeData();
		    node1.addOLANode(Integer.parseInt(nodeId), nodeName, aname, Double.parseDouble(dLongitude), Double.parseDouble(dLatitude));
		    addNode(CommonNode.allNodeList.get(CommonNode.allNodeList.size()-1),id);
		    frame.dispose();// �ͷŴ�����ռ�ڴ���Դ?

//		     addNode(CommonNode.m_lCommonNode.get(CommonNode.m_lCommonNode.size()-1),id);
		    // CommonNode.m_lCommonNode.get(CommonNode.m_lCommonNode.size()-1).setM_nProvince(ChooseMap.whichmap);

		}
	    }
	});

	cancel.addActionListener(new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		frame.setVisible(false);
		frame.dispose();// �ͷŴ�����ռ�ڴ���Դ?
	    }
	});


	frame.setTitle("���OLA�ڵ�");
	frame.setSize(450, 300);
	frame.setLocation(360, 190);
	frame.setVisible(true);
	ImageIcon imageIcon = new ImageIcon(frame.getClass().getResource("/resource/ddd1111.png"));
	frame.setIconImage(imageIcon.getImage());
	

    }

    /**
     * �ж��ַ����Ƿ�������
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
     * �ж��ַ����Ƿ��Ǹ�����
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
     * �ж��ַ����Ƿ�������
     */
    public static boolean isNumber(String value) {
	return isInteger(value) || isDouble(value);
    }

    /*
     * �ж�������ID�Ƿ��Ѵ��� ,��fiber���ж�
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
	// ����Ƿ����ظ�ID������з���true

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
	// ����Ƿ����ظ�ID������з���true

    }

    // ��ȷ����ɲ���
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
