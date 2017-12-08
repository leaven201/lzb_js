package dialog;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.FiberLink;
import dataControl.LinkData;
import datastructure.UIFiberLink;
import design.NetDesign_zs;

public class designWDMlink extends JFrame {

	JLabel shuaijian, linktype, linkpmd, stage,jlname,id,layer,len,aname,firstnode,lastnode,j1, j2, j3, j4,j5,j6;
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

	// @TY����ү +2017/9/22
	// public static double fiberLoss;//����˥��
	// public static String linkType;//��������
	// public static double linkPmd;//����PMD
	// public static int fiberstage;// ���˽׶�
	// -2017/9/22
	public designWDMlink(String name) {
		Font f = new Font("΢���ź�", Font.PLAIN, 12);
		shuaijian = new JLabel("����˥��", JLabel.CENTER);
		stage = new JLabel("���˽׶�", JLabel.CENTER);
		linktype = new JLabel("��������", JLabel.CENTER);
		linkpmd = new JLabel("����PMD", JLabel.CENTER);
		jlname = new JLabel("��·����", JLabel.CENTER);
		id = new JLabel("��·ID", JLabel.CENTER);
		layer = new JLabel("��·����", JLabel.CENTER);
		aname = new JLabel("������", JLabel.CENTER);
		len = new JLabel("����/Km", JLabel.CENTER);
		firstnode = new JLabel("�׽ڵ�", JLabel.CENTER);
		lastnode = new JLabel("ĩ�ڵ�", JLabel.CENTER);
		j1 = new JLabel("  ");
		j2 = new JLabel("  ");
		j3 = new JLabel("  ");
		j4 = new JLabel("  ");
		j5 = new JLabel("  ");
		j6 = new JLabel("  ");
		jtshuaijian = new JTextField(" ");
		jtlinktype = new JTextField(" ");
		jtlinkpmd = new JTextField(" ");
		jtstage = new JTextField(" ");
		jtname= new JTextField(" ");
		jtaname= new JTextField(" ");
		jtlayer= new JTextField(" ");
		jtid= new JTextField(" ");
		jtfirstnode= new JTextField(" ");
		jtlastnode= new JTextField(" ");
		jtlen= new JTextField(" ");
		jtname.setEnabled(false);
		jtaname.setEnabled(false);
		jtid.setEnabled(false);
		jtfirstnode.setEnabled(false);
		jtlastnode.setEnabled(false);
		jtlayer.setEnabled(false);
		jb1 = new JButton("ȷ��");
		// jb1.addActionListener(this);
		// @TY�� +2017/9/22
		jb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
//				FiberLink fiberlink=FiberLink.getLink(name);
//				// ����˥���ı༭
//				fiberlink.setAttenuation(Double.parseDouble(jtshuaijian.getText())); 
//				// �������͵ı༭
//				fiberlink.setLinkType( jtlinktype.getText());
//				// ����PDM�ı༭
//				fiberlink.setPMD(Double.parseDouble(jtlinkpmd.getText())); 
//				// ���˽׶εı༭
//				fiberlink.setFiberStage(Integer.parseInt(jtstage.getText()));
				setVisible(false);// ��ʧ����
				dispose();// �ͷŴ�����ռ��Դ
			}

		});
		jb2 = new JButton("ȡ��");
		jb2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);// ��ʧ����
				dispose();// �ͷŴ�����ռ��Դ
			}
		});
		// - 2017/9/22
		shuaijian.setFont(f);
		linkpmd.setFont(f);
		linktype.setFont(f);
		stage.setFont(f);
		jlname.setFont(f);
		layer.setFont(f);
		aname.setFont(f);
		firstnode.setFont(f);
		lastnode.setFont(f);
		id.setFont(f);
		len.setFont(f);
		jb1.setFont(f);
		jb2.setFont(f);
		jp1 = new JPanel();
		jp2 = new JPanel();
		jp3 = new JPanel();

		// this.setLayout(new GridLayout(3, 6));
		jp1.setLayout(new GridLayout(5, 6, 0, 15));
		this.add(jp1);
		jp1.add(jlname);
		jp1.add(jtname);
		jp1.add(aname);
		jp1.add(jtaname);
		jp1.add(id);
		jp1.add(jtid);
		
		jp1.add(firstnode);
		jp1.add(jtfirstnode);
		jp1.add(lastnode);
		jp1.add(jtlastnode);
		jp1.add(layer);
		jp1.add(jtlayer);
		
		jp1.add(linktype);
		jp1.add(jtlinktype);
		jp1.add(shuaijian);
		jp1.add(jtshuaijian);
		jp1.add(linkpmd);
		jp1.add(jtlinkpmd);
		
		jp1.add(stage);
		jp1.add(jtstage);
		jp1.add(len);
		jp1.add(jtlen);
		jp1.add(j3);
		jp1.add(j4);
		
		
		jp1.add(j1);
		jp1.add(jb1);
		jp1.add(j5);
		jp1.add(j2);
		jp1.add(jb2);
		jp1.add(j6);
		
		this.setSize(600, 350);
		this.setVisible(true);
		this.setTitle("WDM����·�༭");
		ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/resource/ddd1111.png"));
		this.setIconImage(imageIcon.getImage());
		this.setLocationRelativeTo(null);
	}

	// @Override
	// public void actionPerformed(ActionEvent arg0) {
	// // TODO Auto-generated method stub
	// this.dispose();
	// }
	//
}
