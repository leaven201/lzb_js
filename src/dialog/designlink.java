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

public class designlink extends JFrame {

	JLabel shuaijian, linktype, linkpmd, stage, j1, j2, j3, j4;
	public static JTextField jtshuaijian;
	public static JTextField jtlinktype;
	public static JTextField jtlinkpmd;
	public static JTextField jtstage;
	JButton jb1, jb2;
	JPanel jp1, jp2, jp3;

	// @TY����ү +2017/9/22
	// public static double fiberLoss;//����˥��
	// public static String linkType;//��������
	// public static double linkPmd;//����PMD
	// public static int fiberstage;// ���˽׶�
	// -2017/9/22
	public designlink(String name) {
		Font f = new Font("΢���ź�", Font.PLAIN, 12);
		shuaijian = new JLabel("����˥��", JLabel.CENTER);
		linktype = new JLabel("��������", JLabel.CENTER);
		linkpmd = new JLabel("����PMD", JLabel.CENTER);
		stage = new JLabel("���˽׶�", JLabel.CENTER);
		j1 = new JLabel("  ");
		j2 = new JLabel("  ");
		j3 = new JLabel("  ");
		j4 = new JLabel("  ");
		jtshuaijian = new JTextField(" ");
		jtlinktype = new JTextField(" ");
		jtlinkpmd = new JTextField(" ");
		jtstage = new JTextField(" ");
		jb1 = new JButton("ȷ��");
		// jb1.addActionListener(this);
		// @TY�� +2017/9/22
		jb1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FiberLink fiberlink=FiberLink.getLink(name);
				// ����˥���ı༭
				fiberlink.setAttenuation(Double.parseDouble(jtshuaijian.getText())); 
				// �������͵ı༭
				fiberlink.setLinkType( jtlinktype.getText());
				// ����PDM�ı༭
				fiberlink.setPMD(Double.parseDouble(jtlinkpmd.getText())); 
				// ���˽׶εı༭
				fiberlink.setFiberStage(Integer.parseInt(jtstage.getText()));
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
		jb1.setFont(f);
		jb2.setFont(f);
		jp1 = new JPanel();
		jp2 = new JPanel();
		jp3 = new JPanel();

		// this.setLayout(new GridLayout(3, 6));
		jp1.setLayout(new GridLayout(3, 4, 0, 15));
		this.add(jp1);
		jp1.add(linktype);
		jp1.add(jtlinktype);
		jp1.add(shuaijian);
		jp1.add(jtshuaijian);
		jp1.add(linkpmd);
		jp1.add(jtlinkpmd);
		jp1.add(stage);
		jp1.add(jtstage);
		jp1.add(j1);
		jp1.add(jb1);
		jp1.add(j2);
		jp1.add(jb2);
		this.setSize(400, 250);
		this.setVisible(true);
		this.setTitle("���˲����༭");
		ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/resource/ddd1111.png"));
		this.setIconImage(imageIcon.getImage());
		this.setLocationRelativeTo(null);
		// UIFiberLink uilink = (UIFiberLink)
		// NetDesign_zs.network.getDataBox().getLastSelectedElement();
		// jtlinktype.setText(String.valueOf(uilink.fiberLink.getLinkType()));
		// jtshuaijian.setText(String.valueOf(uilink.fiberLink.getAttenuation()));
		// jtlinkpmd.setText(String.valueOf(uilink.fiberLink.getPMD()));
		// jtstage.setText(String.valueOf(uilink.fiberLink.getFiberStage()));
	}

	// @Override
	// public void actionPerformed(ActionEvent arg0) {
	// // TODO Auto-generated method stub
	// this.dispose();
	// }
	//
}
