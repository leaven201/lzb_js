package dialog;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.sound.sampled.LineUnavailableException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import algorithm.OSNR;
import data.DataSave;
import data.FiberLink;

public class OSNRSetting extends JFrame{
	double temp1,temp2,temp3;
	JLabel label;
	JTextField jt2,jt3;
	JPanel jp;
	JFrame frame;
	JButton confirm,cancel;
	public  static void main(String args[]) {
		OSNRSetting o = new OSNRSetting();	
	}
	public OSNRSetting() {
		frame = new JFrame("OSNR参数设置");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel jp = new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		frame.add(jp);
		frame.setSize(600,400);
		frame.setLocationRelativeTo(null);
		Font f1 = new Font("Dialog", Font.BOLD,30);
		Font f2 = new Font("Dialog", Font.BOLD,20);
		
//		label = new JLabel("放大器噪声系数");
//		label.setFont(f1);
//		c.weightx = 0;
//		c.weighty = 0;
//		//存在于第零行，第零列的位置
//		c.gridx = 0;
//		c.gridy = 0;
//		c.anchor = GridBagConstraints.CENTER;
//		c.insets = new Insets(30, 30, 30, 30);
//		jp.add(label,c);
//		
//		
//		jt1 = new JTextField(8);
//		jt1.setFont(f1);
//		c.fill = GridBagConstraints.VERTICAL;
//		c.gridx = 1;
//		c.gridy = 0;
//		c.anchor = GridBagConstraints.CENTER;
//		jp.add(jt1,c);
		
		label = new JLabel("OSC合/分波单元模块插损(dB)");
		label.setFont(f1);
		c.gridx = 0;
		c.gridy = 1;
		c.insets = new Insets(30,30,30,30);
		c.anchor = GridBagConstraints.CENTER;
		jp.add(label,c);
		
		jt2 = new JTextField(8);
		jt2.setFont(f1);
		c.fill = GridBagConstraints.VERTICAL;
		c.weightx = 0;
		c.gridx = 1;
		c.gridy = 1;
		c.anchor = GridBagConstraints.CENTER;
		jp.add(jt2,c);
		
		label = new JLabel("线路侧可调衰耗器插损(dB)");
		label.setFont(f1);
		c.weightx = 0;
		c.gridx = 0;
		c.gridy = 2;
		c.insets = new Insets(30, 30, 30, 30);
		jp.add(label,c);
		
		jt3 = new JTextField(8);
        jt3.setFont(f1);
        c.fill = GridBagConstraints.VERTICAL;
        c.weightx =0;
        c.gridx = 1;
        c.gridy = 2;
        c.anchor = GridBagConstraints.CENTER;
        jp.add(jt3,c);
		
        confirm = new JButton("确定");
        confirm.setFont(f2);
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(30, 0, 30, 0);
        jp.add(confirm,c);
        
        cancel = new JButton("取消");
        cancel.setFont(f2);
        c.weightx = 0;
        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(30, 0, 30, 150);
        jp.add(cancel,c);
        
		frame.pack();
		frame.setVisible(true);
		
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				
				
				// TODO Auto-generated method stub
				if (jt2.getText().equals("") || jt3.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "请输入参数","错误",JOptionPane.PLAIN_MESSAGE);
				}else {
					//DataSave.AF = Double.valueOf(jt1.getText());
					DataSave.OSC = Double.valueOf(jt2.getText());
					DataSave.road = Double.valueOf(jt3.getText());
					OSNR.resetOSNR();
					frame.setVisible(false);
					dispose();
				}
			}
		});
		
		cancel.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				frame.setVisible(false);
				dispose();
			}
		});
	}
}
