package dialog;
import javax.swing.*;

import design.NetDesign_zs;

import java.awt.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;
import java.util.Arrays;
public class UserLogin extends JFrame {
	//������Ҫ�����
	JLabel jl1,jl2,jl3;
	JTextField jName;
	JPasswordField jpassword;
	JButton jConfirm,jCancel;
    Font  f1=new Font("Dialog",Font.BOLD,16);
	public static void main (String [] args) {
		UserLogin ul=new UserLogin();
    }
     public UserLogin() {
    	 this.setLayout(null);
    	 Container ct=this.getContentPane();
    	 //�����������
    	 jl1=new JLabel("�û��� :");
    	 jl1.setFont(f1);
    	 jl1.setBounds(150,290,70,30);   
    	 //����
    	 ct.add(jl1);
  	 
    	 jName=new JTextField(20);
    	 jName.setFont(f1);
    	 jName.setBounds(250,290, 220, 30);
    	 jName.setBorder(BorderFactory.createLoweredSoftBevelBorder());
    	 ct.add(jName);
    	 
    	 jl2=new JLabel("��    �� :");
    	 jl2.setFont(f1);
         jl2.setBounds(150, 350, 70, 30); 
    	 ct.add(jl2);
    	 
    	 jpassword=new JPasswordField(20);
    	 jpassword.setFont(f1);
    	 jpassword.setBounds(250,350, 220, 30);
    	 jpassword.setBorder(BorderFactory.createLoweredSoftBevelBorder());
    	 ct.add(jpassword);
    	 
    	 //ȷ����ť
    	 jConfirm=new JButton("ȷ��");
    	 jConfirm.setFont(f1);
    	 jConfirm.setBounds(150,400,70,30);
    	 //���ð�ťΪ͸��
    	 jConfirm.setContentAreaFilled(true);
    	 //����ͼ��͹��
    	 jConfirm.setBorder(BorderFactory.createRaisedBevelBorder());
    	 //����
    	 ct.add(jConfirm);
    	 
    	 //ȡ����ť
    	 jCancel=new JButton("ȡ��");
    	 jCancel.setFont(f1);
         jCancel.setBounds(400,400,70,30);
    	 //���ð�ťΪ͸��
    	 jCancel.setContentAreaFilled(true);
    	 //����ͼ��͹��
    	 jCancel.setBorder(BorderFactory.createRaisedBevelBorder());
    	 ct.add(jCancel);
    
    	
    	//����һ��BackImage����
    	 BackImage bi=new BackImage();
    	 //ȷ��ͼƬλ��
    	 bi.setBounds(0,0,625,460);
    	 //��ʹ�����¿�
    	
    	 ct.add(bi);
    	 this.setUndecorated(true);
         this.setSize(625, 460); 
         int width=Toolkit.getDefaultToolkit().getScreenSize().width;
         int height=Toolkit.getDefaultToolkit().getScreenSize().height;
         this.setLocation(width/2-300,height/2-250);
         this.setVisible(true);
         
         
         jConfirm.addActionListener(new ActionListener(){  
             public void actionPerformed(ActionEvent e) {  
                 String username=jName.getText();  
                 char[] password=jpassword.getPassword();  
                 char[] pw={'a','d','m','i','n'};  
                 if (username.equals("admin") && Arrays.equals(password,pw)) {  
                 	JOptionPane.showMessageDialog(null, "��¼�ɹ�");  
                 	setVisible(false);
                     NetDesign_zs frame = new NetDesign_zs(1);
                  // frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
             		frame.setTitle(" ���������ܹ滮ƽ̨");
             		// frame.setIconImage((new ImageIcon("button/ddd1.png")).getImage());
             		ImageIcon imageIcon = new ImageIcon(frame.getClass().getResource("/resource/ddd1111.png"));
             		frame.setIconImage(imageIcon.getImage());
             		Toolkit tl = Toolkit.getDefaultToolkit();
             		Dimension screenSize = tl.getScreenSize();
             		frame.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight() - 40);
             		frame.setVisible(true);
             		
             		
             	
                 }  
                 else {                    
                 	JOptionPane.showMessageDialog(null, "��¼ʧ�ܣ��û������������", "��¼ʧ��",JOptionPane.PLAIN_MESSAGE);  
                 	jName.setText("");
                     jpassword.setText("");
                 }  
                 Arrays.fill(password,'0'); //Zero out the possible password, for security.  
             }             
         });  
         
         jCancel.addActionListener(new ActionListener(){  
             public void actionPerformed(ActionEvent e) {  
                     System.exit(0);   
             }             
         });  
    }
     //�ڲ���
     class BackImage extends JPanel {
    	 Image im;
    	 public BackImage() {
    		 try {
				im=ImageIO.read(new File("src/resource/login.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	 }
    	 public void paintComponent(Graphics g) {
    		 g.drawImage(im,0,0,626,469,this);
    	 }
     }
}
