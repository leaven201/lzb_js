package dialog;

import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import algorithm.OSNR;
import data.DataSave;
import data.FiberLink;

public class NFCoefficient extends JFrame{
	  double  temp1,temp2,temp3;
	  JLabel lable;
      JTextField jt1,jt2,jt3;
      JPanel jp;
      JFrame frame;
      JButton confirm,cancel;
//    public static void main (String args[]) {
//    	NFCoefficient co = new NFCoefficient();
//    }

	public NFCoefficient()  {
		frame = new JFrame("AF系数设置");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		JPanel jp=new JPanel(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		frame.add(jp);
		frame.setSize(600,400);
		frame.setLocationRelativeTo(null);
        Font  f1 = new Font("Dialog",Font.BOLD,30);
        Font  f2 = new Font("Dialog",Font.BOLD,20);
        
        
        lable = new JLabel("\t放大器增益临界值G");
        lable.setFont(f1);
//        c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx=0;
        c.weighty=0;
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(30,30,30,30);
        jp.add(lable,c);
        
        jt1 = new JTextField(8);
        jt1.setFont(f1);
        c.fill = GridBagConstraints.VERTICAL;
        c.weightx=0.5;
        c.gridx = 1;
        c.gridy = 0;
        c.anchor = GridBagConstraints.CENTER;
        jp.add(jt1,c);
        
        
        lable = new JLabel("\tif Gi≤G, AF=  ");
        lable.setFont(f2);
//        c.fill = GridBagConstraints.BOTH;
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(30,50,0,50);
        c.anchor = GridBagConstraints.CENTER;
        jp.add(lable,c);
        
        jt2 = new JTextField(10);
        jt2.setFont(f2);
        c.fill = GridBagConstraints.VERTICAL;
        c.weightx = 0;
        c.gridx = 1;
        c.gridy = 1;
        c.anchor = GridBagConstraints.CENTER;
        jp.add(jt2,c);
        
        
        lable = new JLabel("\tif Gi>G, AF=  ");
        lable.setFont(f2);
//      c.fill = GridBagConstraints.HORIZONTAL;
        c.weightx = 0;
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(0, 50, 30, 50);
        c.anchor = GridBagConstraints.CENTER;
        jp.add(lable,c);
   
        
        jt3 = new JTextField(10);
        jt3.setFont(f2);
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
        c.insets = new Insets(30,100,30,0);
        jp.add(confirm,c);
        
        cancel = new JButton("取消");
        cancel.setFont(f2);
        c.weightx = 0;
        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(30,0,30,100);
        jp.add(cancel,c);
		
        frame.pack();
		frame.setVisible(true);
	
		
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
				if (jt1.getText().equals("")||jt2.getText().equals("")||jt3.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "请输入参数","错误",JOptionPane.PLAIN_MESSAGE);
				}else {
					temp1=Double.valueOf(jt1.getText());
					temp2=Double.valueOf(jt2.getText());
					temp3=Double.valueOf(jt3.getText());
				}
				if (temp1-((int)temp1)>=0) {
					DataSave.Gain=temp1;
				}
				else {
					JOptionPane.showMessageDialog(null, "请输入正数", "错误",JOptionPane.PLAIN_MESSAGE);
				}
				if (temp2-((int)temp2)>=0) {
					DataSave.AboveNF=temp2;
				}
				else {
					JOptionPane.showMessageDialog(null, "请输入正数", "错误",JOptionPane.PLAIN_MESSAGE);
				}
				if (temp3-((int)temp3)>=0) {
					DataSave.BelowNF=temp3;
				}
				else {
					JOptionPane.showMessageDialog(null, "请输入正数", "错误",JOptionPane.PLAIN_MESSAGE);
				}
				System.out.println(DataSave.Gain);
				System.out.println(DataSave.AboveNF);
				System.out.println(DataSave.BelowNF);
				OSNR.resetOSNR();
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
