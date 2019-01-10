/*package dialog;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import design.NetDesign;

public class Dlg_ProjectInfo extends JFrame {
	private String information = new String();// ������Ϣ
	private String temp = new String();
	private ImageIcon title = new ImageIcon(getClass().getResource("/resource/titlenew.jpg"));
	private JPanel mainpane = new JPanel(); // �����
	private JPanel infopane = new JPanel();// ���ù�����Ϣ�����
	private JPanel pane = new JPanel();// ���ù������ƺ�λ�õ����

	public Dlg_ProjectInfo() {
		this.setTitle("������Ϣ");
		// JLabel labelNothing = new JLabel("");
		JLabel labelTitle = new JLabel(title);// ��ӱ���logo
		JLabel labelName = new JLabel("�������� " + ":    " + NetDesign.filename);
		labelName.setFont(new java.awt.Font("����", Font.PLAIN, 18));
		JLabel labelfilename = new JLabel("����λ�� " + ":    " + NetDesign.filepath);
		labelfilename.setFont(new java.awt.Font("����", Font.PLAIN, 18));
		JLabel labelfileinfo = new JLabel("������Ϣ " + ":    ");
		labelfileinfo.setFont(new java.awt.Font("����", Font.PLAIN, 18));
		JButton confirm = new JButton("ȷ��");
		information = NetDesign.fileinfo;
		if (information == null)
			information = "��";// ��������ϢΪ��ʱ��ʾ�յ�label������ʾ��null��
		// temp = information;
		// information = "<html> <body>".concat(temp).concat("</body> </html>");
		// temp = information;
		// information = temp.replaceAll("\n", "<br>");
		// int beginInd, endInd;
		// beginInd = information.indexOf("<br>");
		// for (int i = 0; i < information.length();){
		// if(beginInd != -1){//���ַ����д��ڻ��з�
		// endInd = information.indexOf("<br>", beginInd+4);//����+4���ʼĩ������ͬ
		// if(endInd != -1){//���л�����һ�����з�
		// String substr = information.substring(beginInd+4, endInd);//�������з��м䲿��
		// byte[] bytes = substr.getBytes();
		// if(bytes.length > 50){
		// int result = 0;
		// int cutLength = 0;
		// //ͳ�Ƴ���Ҫ��ȡ���ֽ����м����ֽ��Ǹ���
		// for (int j = 0; j < 50; j++) {
		// if (bytes[j] < 0) {
		// cutLength++;
		// }
		// }
		//// �������ֽ�����ż������Ҫ��ȡ���ֽ����������պ������������ĺ��֣����ȡ�ֽ�������
		//// �������ֽ�������������ʾ�����Ĳ����������֣���ȡ�ֽ����� 1��
		// if (cutLength % 2 == 0) result = 50;
		// else result = 49;
		// String subtmp = new String(bytes, 0, result);//ȡ���ﵽһ����ʾ��ǰһ��
		// temp = subtmp.concat("<br>");//��ӻ��з�
		// subtmp = new String(bytes, result,
		// bytes.length-result);//ȡ��substrʣ�²���
		// substr = temp.concat(subtmp);//��ɳ����ֽ�������ӻ��з�
		// temp = information.substring(endInd);//�Ե�ǰ�ַ�����һ�����з����Ժ�Ĳ���
		// subtmp = information.substring(0, beginInd+4);//������ǰ�ַ���ǰһ�����з���֮ǰ����
		// information = subtmp.concat(substr).concat(temp);
		// beginInd = information.indexOf("<br>", beginInd+4);
		// }
		// else
		// beginInd = endInd;
		// }
		// else{//�����ַ���֮��Ĳ�����һ��
		// String substr =
		// information.substring(beginInd+4,information.length()-15);
		// byte[] bytes = substr.getBytes();
		// if(bytes.length > 50){
		// int result = 0;
		// int cutLength = 0;
		// //ͳ�Ƴ���Ҫ��ȡ���ֽ����м����ֽ��Ǹ���
		// for (int j = 0; j < 50; j++) {
		// if (bytes[j] < 0) {
		// cutLength++;
		// }
		// }
		//// �������ֽ�����ż������Ҫ��ȡ���ֽ����������պ������������ĺ��֣����ȡ�ֽ�������
		//// �������ֽ�������������ʾ�����Ĳ����������֣���ȡ�ֽ����� 1��
		// if (cutLength % 2 == 0) result = 50;
		// else result = 49;
		// String subtmp = new String(bytes, 0, result);//ȡ���ﵽһ����ʾ��ǰһ��
		// temp = subtmp.concat("<br>");//��ӻ��з�
		// subtmp = new String(bytes, result,
		// bytes.length-result);//ȡ��substrʣ�²���
		// substr = temp.concat(subtmp);//��ɳ����ֽ�������ӻ��з�
		// temp = information.substring(information.length()-15);
		// subtmp = information.substring(0, beginInd+4);//������ǰ�ַ���ǰһ�����з���֮ǰ����
		// information = subtmp.concat(substr).concat(temp);
		// beginInd = information.indexOf("<br>", beginInd+4);
		// }
		// else
		// break;
		// }
		// }
		// else{//�ַ����в����ڻ��з�
		// break;
		// }
		// }
		// labelName.setHorizontalAlignment(JLabel.LEFT);
		// labelfileinfo.setHorizontalAlignment(JLabel.LEFT);
		// labelfilename.setHorizontalAlignment(JLabel.LEFT);
		// labelinfo.setHorizontalAlignment(JLabel.CENTER);
		// JLabel labelinfo = new JLabel(information);
		JTextArea labelinfo = new JTextArea(information, 5, 30);
		JScrollPane jsp = new JScrollPane(labelinfo);
		jsp.setBorder(BorderFactory.createEmptyBorder());
		labelinfo.setEditable(false);
		labelinfo.setBorder(BorderFactory.createEmptyBorder());
		labelinfo.setLineWrap(true);// �Զ����й���
		labelinfo.setWrapStyleWord(true);// ���в����ֹ���
		labelinfo.setFont(new java.awt.Font("����", Font.PLAIN, 18));
		labelinfo.setForeground(Color.BLACK);
		this.setContentPane(mainpane);
		pane.setLayout(new GridLayout(4, 1));
		pane.add(labelName);
		pane.add(labelfilename);
		pane.add(new JLabel(""));
		pane.add(labelfileinfo);
		pane.setBackground(Color.WHITE);
		// ���ù�����Ϣ���ֲ���
		infopane.setBorder(BorderFactory.createEtchedBorder());
		Insets insert;
		GridBagLayout mg = new GridBagLayout();
		GridBagConstraints mgc = new GridBagConstraints();
		infopane.setLayout(mg);
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		mgc.gridheight = 1;
		mgc.weighty = 1.0;
		mgc.weightx = 1.0;
		insert = new Insets(0, 10, 0, 10);
		mgc.insets = insert;
		mgc.fill = GridBagConstraints.HORIZONTAL;
		infopane.add(pane, mgc);
		mgc.fill = GridBagConstraints.BOTH;
		insert = new Insets(0, 19, 9, 19);
		mgc.insets = insert;
		infopane.add(jsp, mgc);
		infopane.setBackground(Color.WHITE);

		// ��Panel��������
		mg = new GridBagLayout();
		mgc = new GridBagConstraints();
		mainpane.setLayout(mg);
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		mgc.gridheight = 1;
		mgc.weighty = 1.0;
		mgc.weightx = 1.0;
		insert = new Insets(-50, 0, 0, 0);
		mgc.insets = insert;
		mainpane.add(labelTitle, mgc);
		mgc.fill = GridBagConstraints.BOTH;
		insert = new Insets(-35, 10, -104, 10);
		mgc.insets = insert;
		mainpane.add(infopane, mgc);
		mgc.fill = GridBagConstraints.NONE;
		insert = new Insets(70, 0, -35, 0);
		mgc.insets = insert;
		mainpane.add(confirm, mgc);
		int c = labelinfo.getColumns();
		int r = labelinfo.getRows();
		System.out.println("column = " + c + "rows = " + r);

		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();// �ͷŴ�����ռ�ڴ���Դ
			}
		});

		this.setSize(500, 375);
		this.setLocationRelativeTo(null);
		mainpane.setBackground(Color.white);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	// public static void main(String[] args){
	// new Dlg_ProjectInfo();
	// }
}
*/