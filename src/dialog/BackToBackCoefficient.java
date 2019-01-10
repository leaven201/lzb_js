package dialog;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.DataSave;

public class BackToBackCoefficient extends JFrame{
	double  temp1,temp2,temp3,temp4;
	JLabel label;
	JTextField jt1,jt2,jt3,jt4;
	JFrame frame;
	JPanel panel;
	JPanel panel1;//用来存放背靠背指标的面板
	JPanel panel2;//用来存放a b c 的面板
	JPanel panel3;//用来存放确定和取消按钮的面板
	JButton confirm,cancel;
	public static void main(String args[]) {
		BackToBackCoefficient b = new BackToBackCoefficient();	
	}
	public BackToBackCoefficient() {
		frame = new JFrame("背靠背指标及过系统OSNR阈量设定");
		frame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		panel = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		frame.add(panel);
		frame.setSize(1000, 700);
		frame.setLocationRelativeTo(null);
		Font f1 = new Font("Dialog",Font.BOLD,60);	
		Font f2 = new Font("Dialog",Font.BOLD,50);
		Font f3 = new Font("Dialog",Font.BOLD,40);
		Font f4 = new Font("Dialog",Font.BOLD,20);
		
		//jlabel可以识别html，利用html进行换行
		String str1 = "背靠背指标";
		String str2 = "          (dB)";
		String str = "<html><body>"+str1+"<br>&nbsp&nbsp&nbsp&nbsp&nbsp"+str2+"</body></html>";
		label = new JLabel(str,JLabel.CENTER);
		label.setFont(f1);
		c.fill = GridBagConstraints.BOTH;
		//不需要设置初始值
//      c.gridx = 0;
//		c.gridy = 0;
		c.gridheight =3;
		label.setBorder(BorderFactory.createLineBorder(Color.black,1));
		c.anchor = GridBagConstraints.CENTER;
		c.insets = new Insets(100, 50, 0, 0);
		panel.add(label,c);
		
		//让Jlabel中的文字居中要在声明中
		label = new JLabel("余量（dB）",JLabel.CENTER);
		label.setFont(f2);
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.CENTER;
		c.gridheight = 2;
		c.gridwidth = GridBagConstraints.REMAINDER;
		label.setBorder(BorderFactory.createLineBorder(Color.black,1));
		c.insets = new Insets(100, 0, 0, 50);
		panel.add(label,c);
		
		label = new JLabel("  N<=a  ",JLabel.CENTER);
		label.setFont(f3);
		c.gridwidth = GridBagConstraints.RELATIVE;
		c.gridwidth = 1;
		c.gridheight = 1;
		label.setBorder(BorderFactory.createLineBorder(Color.black,1));
		c.insets = new Insets(0, 0, 0, 0);
		panel.add(label,c);
		
		label = new JLabel(" a<N<=b ",JLabel.CENTER);
		label.setFont(f3);
		c.gridwidth = 1;
		c.gridheight = 1;
		label.setBorder(BorderFactory.createLineBorder(Color.black,1));
		panel.add(label,c);
		
		label = new JLabel(" b<N<=c ",JLabel.CENTER);
		label.setFont(f3);
		c.gridwidth = 1;
		c.gridheight = 1;
		label.setBorder(BorderFactory.createLineBorder(Color.black,1));
		panel.add(label,c);
		
		label = new JLabel("   N>c  ",JLabel.CENTER);
		label.setFont(f3);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 1;
		label.setBorder(BorderFactory.createLineBorder(Color.black,1));
		c.insets = new Insets(0, 0, 0, 50);
		panel.add(label,c);
		
		panel1 = new JPanel();
		jt1 = new JTextField(5);
		jt1.setFont(f2);
		panel1.add(jt1);
		panel1.setBorder(BorderFactory.createLineBorder(Color.black,1));
		//在这里吧gridwidth设为1，就可以保证与第一行的第一个格子对齐
	    c.gridwidth = 1;
	    c.gridheight = 3;
	    c.insets = new Insets(0,50, 0, 0);
		panel.add(panel1,c);
		
		label = new JLabel("    4.5   ",JLabel.CENTER);
		label.setFont(f3);
		c.gridwidth = 1;
		c.gridheight = 3;
		label.setBorder(BorderFactory.createLineBorder(Color.black,1));
		c.insets = new Insets(0, 0, 0, 0);
		panel.add(label,c);
		
		label = new JLabel("     5     ",JLabel.CENTER);
		label.setFont(f3);
		c.gridwidth = 1;
		c.gridheight = 3;
		label.setBorder(BorderFactory.createLineBorder(Color.black,1));
		panel.add(label,c);
		
		label = new JLabel("    5.5   ",JLabel.CENTER);
		label.setFont(f3);
		c.gridwidth = 1;
		c.gridheight = 3;
		label.setBorder(BorderFactory.createLineBorder(Color.black,1));
		panel.add(label,c);
		
		label = new JLabel("     6    ",JLabel.CENTER);
		label.setFont(f3);
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.gridheight = 3;
		label.setBorder(BorderFactory.createLineBorder(Color.black,1));
		c.insets = new Insets(0, 0, 0, 50);
		panel.add(label,c);
		
		//面板2编辑
		panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
		panel2.setBorder(BorderFactory.createLineBorder(Color.black, 1));
		label = new JLabel("a= ");
		label.setFont(f3);
		panel2.add(label);
		
		jt2 = new JTextField(5);
		jt2.setFont(f3);
		panel2.add(jt2);
		
		label = new JLabel("b= ");
		label.setFont(f3);
		panel2.add(label);
		
		jt3 = new JTextField(5);
		jt3.setFont(f3);
		panel2.add(jt3);
		
		label = new JLabel("c= ");
		label.setFont(f3);
		panel2.add(label);
		
		jt4 = new JTextField(5);
		jt4.setFont(f3);
		panel2.add(jt4);
		
		c.gridwidth=GridBagConstraints.REMAINDER;
		c.insets = new Insets(0,50, 0, 50);
		panel.add(panel2,c);
		
		//面板3编辑
		panel3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 250, 0));
		
		confirm = new JButton("确定");
		confirm.setFont(f4);
		cancel = new JButton("取消");
		cancel.setFont(f4);
		
		panel3.add(confirm);
		panel3.add(cancel);
		
		c.gridwidth = GridBagConstraints.REMAINDER;
		c.insets = new Insets(50, 0, 50, 0);
		panel.add(panel3,c);
		
		frame.pack();
		frame.setVisible(true);
		
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			
				
				if (jt1.getText().equals("")||jt2.getText().equals("")||jt3.getText().equals("")||jt4.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "请输入参数","错误",JOptionPane.PLAIN_MESSAGE);
				}else {
					temp1=Double.valueOf(jt1.getText());
					temp2=Double.valueOf(jt2.getText());
					temp3=Double.valueOf(jt3.getText());
					temp4=Double.valueOf(jt4.getText());
				}
			    
			
				if (temp1-((int)temp1)<0.0000001&&temp1-((int)temp1)>=0) {
					DataSave.BackToBack=(int)temp1;
				}
				else {
					JOptionPane.showMessageDialog(null, "请输入正整数", "错误",JOptionPane.PLAIN_MESSAGE);
				}
				if (temp2-((int)temp2)<0.0000001&&temp2-((int)temp2)>=0) {
					DataSave.BackToBackA=(int)temp2;
				}
				else {
					JOptionPane.showMessageDialog(null, "请输入正整数", "错误",JOptionPane.PLAIN_MESSAGE);
				}
				if (temp3-((int)temp3)<0.0000001&&temp3-((int)temp3)>=0) {
					DataSave.BackToBackB=(int)temp3;
				}
				else {
					JOptionPane.showMessageDialog(null, "请输入正整数", "错误",JOptionPane.PLAIN_MESSAGE);
				}
				if (temp4-((int)temp4)<0.0000001&&temp4-((int)temp4)>=0) {
					DataSave.BackToBackC=(int)temp4;
				}
				else {
					JOptionPane.showMessageDialog(null, "请输入正整数", "错误",JOptionPane.PLAIN_MESSAGE);
				}
				System.out.println(DataSave.BackToBack);
				System.out.println(DataSave.BackToBackA);
				System.out.println(DataSave.BackToBackB);
				System.out.println(DataSave.BackToBackC);

				frame.setVisible(false);
				dispose();
			
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);
				dispose();
			}
		});
 	}

}
