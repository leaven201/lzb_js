package dialog;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import algorithm.OSNR;
import data.DataSave;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.JButton;

public class AFSetting extends JFrame{

	double  temp1,temp2,temp3;
	public JFrame frmAf;
	JTextField jt1;
	JTextField jt2;
	JTextField jt3;
	JLabel lblNewLabel_1;
	JLabel lblgidbafgidbaf;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					AFSetting window = new AFSetting();
					window.frmAf.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public AFSetting() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAf = new JFrame();
		frmAf.setVisible(true);
		frmAf.setTitle("AF\u7CFB\u6570\u8BBE\u7F6E");
		frmAf.setResizable(false);
		frmAf.setBounds(100, 100, 587, 404);
		frmAf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAf.getContentPane().setLayout(null);
		frmAf.setLocationRelativeTo(null);
		
		JLabel lblNewLabel = new JLabel("\u653E\u5927\u5668\u589E\u76CA\u4E34\u754C\u503CG");
		lblNewLabel.setFont(new Font("宋体", Font.PLAIN, 22));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(96, 41, 242, 46);
		frmAf.getContentPane().add(lblNewLabel);
		
		jt1 = new JTextField();
		jt1.setBounds(348, 50, 94, 35);
		frmAf.getContentPane().add(jt1);
		jt1.setColumns(10);
		
		JLabel lblIfGig = new JLabel("\u5982\u679C Gi \u2264 G\uFF0CAF = ");
		lblIfGig.setHorizontalAlignment(SwingConstants.CENTER);
		lblIfGig.setFont(new Font("宋体", Font.PLAIN, 22));
		lblIfGig.setBounds(106, 97, 242, 46);
		frmAf.getContentPane().add(lblIfGig);
		
		JLabel lblIfGi = new JLabel("\u5982\u679C Gi \uFF1E G\uFF0CAF = ");
		lblIfGi.setHorizontalAlignment(SwingConstants.CENTER);
		lblIfGi.setFont(new Font("宋体", Font.PLAIN, 22));
		lblIfGi.setBounds(106, 151, 242, 46);
		frmAf.getContentPane().add(lblIfGi);
		
		jt2 = new JTextField();
		jt2.setColumns(10);
		jt2.setBounds(348, 102, 94, 35);
		frmAf.getContentPane().add(jt2);
		
		jt3 = new JTextField();
		jt3.setColumns(10);
		jt3.setBounds(348, 157, 94, 35);
		frmAf.getContentPane().add(jt3);
		
		lblNewLabel_1 = new JLabel("\u6CE8\uFF1A\u5982\u672A\u505A\u8BBE\u7F6E\uFF0C\u5219\u9ED8\u8BA4\u503C\u4E3AG=22dB");
		lblNewLabel_1.setFont(new Font("宋体", Font.PLAIN, 16));
		lblNewLabel_1.setBounds(124, 264, 495, 38);
		frmAf.getContentPane().add(lblNewLabel_1);
		
		lblgidbafgidbaf = new JLabel("\u5F53Gi\u226422dB\u65F6\uFF0CAF=5\uFF0C\u5F53Gi\uFF1E22dB\u65F6\uFF0CAF=4.5");
		lblgidbafgidbaf.setFont(new Font("宋体", Font.PLAIN, 16));
		lblgidbafgidbaf.setBounds(124, 298, 495, 35);
		frmAf.getContentPane().add(lblgidbafgidbaf);
		
		JButton confirm = new JButton("\u786E\u5B9A");
		confirm.setBounds(176, 223, 93, 23);
		frmAf.getContentPane().add(confirm);
		
		JButton cancel = new JButton("\u53D6\u6D88");
		cancel.setBounds(312, 223, 93, 23);
		frmAf.getContentPane().add(cancel);
		
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
				frmAf.setVisible(false);
				dispose();
			}
		});
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frmAf.setVisible(false);
				dispose();
			}
		});
		
		
	}
}
