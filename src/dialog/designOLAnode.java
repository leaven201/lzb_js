package dialog;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import enums.NodeType;
import data.CommonNode;

public class designOLAnode extends JFrame implements ActionListener{
	JLabel id,name,type,jin,wei,zj,j1,j2,j3,j4,j5,j6,shangxiazu,wss,aname;
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
	public static JRadioButton jc3 = new JRadioButton("是");
	public static JRadioButton jc4 = new JRadioButton("否");
    ButtonGroup roadmgroup=new ButtonGroup();
//  //@TY宇少爷 +2017/9/22
//    public static int fixId;
//    public static String fixName;
//    public static String fixOtherName;
//    public static double fixLongitude;
//    public static double fixLatitude;
//    public static NodeType fixNodeType;
//    public static int fixWss;
//    public static int fixUpDown;
//  //-2017/9/22  
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	//	designROADMnode d1 =new designROADMnode();
	}

	public designOLAnode (int mark)
	{
		Font f=new Font("微软雅黑", Font.PLAIN, 12);
		id=new JLabel("ID",JLabel.CENTER);
		name=new JLabel("名称",JLabel.CENTER);
		type=new JLabel("站点类型",JLabel.CENTER);
		jin=new JLabel("经度",JLabel.CENTER);
		wei=new JLabel("纬度",JLabel.CENTER);
		zj=new JLabel("中继",JLabel.CENTER);
		aname=new JLabel("别名",JLabel.CENTER);
		shangxiazu=new JLabel("本地上下组分组数",JLabel.CENTER);
		wss=new JLabel("WSS维数",JLabel.CENTER);
		j1=new JLabel("  ");
		j2=new JLabel("  ");
		j3=new JLabel("  ");
		j4=new JLabel("  ");
		j5=new JLabel("  ");
		j6=new JLabel("  ");
		jtid=new JTextField(" ");
		jtid.setEditable(false);
//		jtzj=new JTextField(" ");
		jttype=new JTextField(" ");
		jttype.setEditable(false);
		jtjin=new JTextField(" ");
		jtwei=new JTextField(" ");
		jtname=new JTextField(" ");
		jtname.setEditable(false);
		jtaname=new JTextField(" ");
		jtshangxiazu=new JTextField(" ");
		jtwss=new JTextField(" ");
		//@TY宇少爷  +2017/9/22
		jb1=new JButton("确定");
		jb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CommonNode commonnode=CommonNode.getNode(mark);
			//	commonnode.setId(Integer.parseInt(jtid.getText()));
				commonnode.setName(jtname.getText());
				commonnode.setOtherName(jtaname.getText());
				commonnode.setLongitude(Double.parseDouble(jtjin.getText()));
				commonnode.setLatitude(Double.parseDouble(jtwei.getText()));
				commonnode.setType(NodeType.stringToNodeType(jttype.getText()));
//				commonnode.setWSS(Integer.parseInt(jtwss.getText()));
//				commonnode.setUpDown(Integer.parseInt(jtshangxiazu.getText()));
		        dispose();// 释放窗口资源
			}
		});
		jb2=new JButton("取消");
		jb2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);//消失窗口
				dispose();//释放窗口资源
			}
		});
//-2017/9/22	
		id.setFont(f);
		name.setFont(f);
		type.setFont(f);
		jin.setFont(f);
		wei.setFont(f);
		zj.setFont(f);
		jb1.setFont(f);
		jb2.setFont(f);
		jc3.setFont(f);
		jc4.setFont(f);
		aname.setFont(f);
		shangxiazu.setFont(f);
		wss.setFont(f);
		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		jp4=new JPanel();
		
		
//		this.setLayout(new GridLayout(3, 6));
		jp1.setLayout(new GridLayout(3,6,0,15));
		jp4.setLayout(new GridLayout(2,1,0,0));
//		jp5.setLayout(new GridLayout(1,6,0,0));
		this.add(jp1);
		jp1.add(id);
		jp1.add(jtid);
		jp1.add(name);
		jp1.add(jtname);
		jp1.add(type);
		jp1.add(jttype);
		jp1.add(jin);
		jp1.add(jtjin);
		jp1.add(wei);
		jp1.add(jtwei);
//		jp1.add(zj);
//		jp1.add(j5);
		jp1.add(aname);
		jp1.add(jtaname);
//		jp1.add(shangxiazu);
//		jp1.add(jtshangxiazu);
//		jp1.add(wss);
//		jp1.add(jtwss);
//		jp1.add(j5);
//		jp1.add(j6);
		jp1.add(j1);
		jp1.add(jb1);
		jp1.add(j2);
		jp1.add(j3);
		jp1.add(jb2);
		jp1.add(j4);
		roadmgroup.add(jc3);
		roadmgroup.add(jc4);
		jp4.add(jc3);
		jp4.add(jc4);
		if(jttype.getText()=="OLA"){
			jc3.setSelected(false);;
			jc4.setSelected(false);
		}
		jc4.setSelected(true);
		
		this.setSize(600,300);
		this.setVisible(true);
		this.setTitle("站点编辑");
		ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/resource/ddd1111.png"));
		this.setIconImage(imageIcon.getImage());
		this.setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		this.dispose();
	}
}
