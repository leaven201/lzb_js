package dialog;

import java.awt.*;  
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.*;  

import design.NetDesign_zs;
  
public class Dlg_login extends JFrame{  
        //�������  
    JPanel jp1, jp2, jp3;  
    JLabel jl1, jl2;  
    JTextField jtf;  
    JPasswordField jpf;  
    JButton jb1, jb2;  
  
    public static void main(String[] args) {  
    	Dlg_login test = new Dlg_login();  
    }  
        //���캯�� ��ʼ�����  
    public Dlg_login(){  
        jp1 = new JPanel();  
        jp2 = new JPanel();  
        jp3 = new JPanel();  
          
        jl1 = new JLabel("�û���");  
        jl2 = new JLabel("��      ��");  
          
        jtf = new JTextField(10);  
        jpf = new JPasswordField(10);  
          
        jb1 = new JButton("��¼");  
        jb2 = new JButton("ȡ��");  
          
        jp1.add(jl1);  
        jp1.add(jtf);  
        jp2.add(jl2);  
        jp2.add(jpf);  
        jp3.add(jb1);  
        jp3.add(jb2);  
          
        this.add(jp1);  
        this.add(jp2);  
        this.add(jp3);  
          
        this.setLayout(new GridLayout(3, 1));         
        this.setTitle("��½��֤");  
        ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/resource/ddd1111.png"));
		this.setIconImage(imageIcon.getImage());
        this.setSize(300,200);  
        this.setLocation(550,280);  
        this.setResizable(false);  
        this.setVisible(true);  
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
     
        
        jb1.addActionListener(new ActionListener(){  
            @Override  
            public void actionPerformed(ActionEvent e) {  
                String username=jtf.getText();  
                char[] password=jpf.getPassword();  
                System.out.println(username);  
                System.out.println(password);  
                char[] pw={'a','d','m','i','n'};  
                /*System.out.println(Arrays.equals(s2,pw)); 
                System.out.println(s1.equals("han"));*/  
                if (username.equals("admin") && Arrays.equals(password,pw)) {  
                	JOptionPane.showMessageDialog(null, "��¼�ɹ�");  
                   // setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//����ͬ�ڵ���رմ���ʱִ��frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE );��  
                   // System.exit(0);//�����˳�����ͬ�ڵ���رմ���ʱִ��frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );��
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
                	jtf.setText("");
                    jpf.setText("");
                }  
                Arrays.fill(password,'0'); //Zero out the possible password, for security.  
            }             
        });  
        
        jb2.addActionListener(new ActionListener(){  
            @Override  
            public void actionPerformed(ActionEvent e) {  
                
                    System.exit(0);//�����˳�����ͬ�ڵ���رմ���ʱִ��frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );��  
                 
            }             
        });  
    }  
   
    
}  