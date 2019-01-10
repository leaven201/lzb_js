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
	private String information = new String();// 工程信息
	private String temp = new String();
	private ImageIcon title = new ImageIcon(getClass().getResource("/resource/titlenew.jpg"));
	private JPanel mainpane = new JPanel(); // 主面板
	private JPanel infopane = new JPanel();// 放置工程信息的面板
	private JPanel pane = new JPanel();// 放置工程名称和位置的面板

	public Dlg_ProjectInfo() {
		this.setTitle("工程信息");
		// JLabel labelNothing = new JLabel("");
		JLabel labelTitle = new JLabel(title);// 添加标题logo
		JLabel labelName = new JLabel("工程名称 " + ":    " + NetDesign.filename);
		labelName.setFont(new java.awt.Font("宋体", Font.PLAIN, 18));
		JLabel labelfilename = new JLabel("保存位置 " + ":    " + NetDesign.filepath);
		labelfilename.setFont(new java.awt.Font("宋体", Font.PLAIN, 18));
		JLabel labelfileinfo = new JLabel("工程信息 " + ":    ");
		labelfileinfo.setFont(new java.awt.Font("宋体", Font.PLAIN, 18));
		JButton confirm = new JButton("确定");
		information = NetDesign.fileinfo;
		if (information == null)
			information = "无";// 当工程信息为空时显示空的label，不显示“null”
		// temp = information;
		// information = "<html> <body>".concat(temp).concat("</body> </html>");
		// temp = information;
		// information = temp.replaceAll("\n", "<br>");
		// int beginInd, endInd;
		// beginInd = information.indexOf("<br>");
		// for (int i = 0; i < information.length();){
		// if(beginInd != -1){//若字符串中存在换行符
		// endInd = information.indexOf("<br>", beginInd+4);//若无+4则会始末索引相同
		// if(endInd != -1){//此行还存在一个换行符
		// String substr = information.substring(beginInd+4, endInd);//两个换行符中间部分
		// byte[] bytes = substr.getBytes();
		// if(bytes.length > 50){
		// int result = 0;
		// int cutLength = 0;
		// //统计出在要截取的字节中有几个字节是负数
		// for (int j = 0; j < 50; j++) {
		// if (bytes[j] < 0) {
		// cutLength++;
		// }
		// }
		//// 若负数字节数是偶数，即要截取的字节数中遇到刚好是若干完整的汉字，则截取字节数不变
		//// 否则负数字节数是奇数，表示遇到的不是完整汉字，截取字节数减 1。
		// if (cutLength % 2 == 0) result = 50;
		// else result = 49;
		// String subtmp = new String(bytes, 0, result);//取出达到一行显示的前一半
		// temp = subtmp.concat("<br>");//添加换行符
		// subtmp = new String(bytes, result,
		// bytes.length-result);//取出substr剩下部分
		// substr = temp.concat(subtmp);//完成超过字节数则添加换行符
		// temp = information.substring(endInd);//自当前字符串后一个换行符及以后的部分
		// subtmp = information.substring(0, beginInd+4);//包含当前字符串前一个换行符及之前所有
		// information = subtmp.concat(substr).concat(temp);
		// beginInd = information.indexOf("<br>", beginInd+4);
		// }
		// else
		// beginInd = endInd;
		// }
		// else{//整个字符串之后的部分是一行
		// String substr =
		// information.substring(beginInd+4,information.length()-15);
		// byte[] bytes = substr.getBytes();
		// if(bytes.length > 50){
		// int result = 0;
		// int cutLength = 0;
		// //统计出在要截取的字节中有几个字节是负数
		// for (int j = 0; j < 50; j++) {
		// if (bytes[j] < 0) {
		// cutLength++;
		// }
		// }
		//// 若负数字节数是偶数，即要截取的字节数中遇到刚好是若干完整的汉字，则截取字节数不变
		//// 否则负数字节数是奇数，表示遇到的不是完整汉字，截取字节数减 1。
		// if (cutLength % 2 == 0) result = 50;
		// else result = 49;
		// String subtmp = new String(bytes, 0, result);//取出达到一行显示的前一半
		// temp = subtmp.concat("<br>");//添加换行符
		// subtmp = new String(bytes, result,
		// bytes.length-result);//取出substr剩下部分
		// substr = temp.concat(subtmp);//完成超过字节数则添加换行符
		// temp = information.substring(information.length()-15);
		// subtmp = information.substring(0, beginInd+4);//包含当前字符串前一个换行符及之前所有
		// information = subtmp.concat(substr).concat(temp);
		// beginInd = information.indexOf("<br>", beginInd+4);
		// }
		// else
		// break;
		// }
		// }
		// else{//字符串中不存在换行符
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
		labelinfo.setLineWrap(true);// 自动换行功能
		labelinfo.setWrapStyleWord(true);// 断行不断字功能
		labelinfo.setFont(new java.awt.Font("宋体", Font.PLAIN, 18));
		labelinfo.setForeground(Color.BLACK);
		this.setContentPane(mainpane);
		pane.setLayout(new GridLayout(4, 1));
		pane.add(labelName);
		pane.add(labelfilename);
		pane.add(new JLabel(""));
		pane.add(labelfileinfo);
		pane.setBackground(Color.WHITE);
		// 设置工程信息部分布局
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

		// 主Panel布局设置
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
				dispose();// 释放窗体所占内存资源
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