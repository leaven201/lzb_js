package dialog;

import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JFrame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import algorithm.CommonAlloc;
import algorithm.PortAlloc;
import data.CommonNode;
import data.Route;
import data.Traffic;
import data.WDMLink;

import javax.swing.JComboBox;
import javax.swing.JButton;


import data.Traffic;
import design.NetDesign_zs;
import enums.NodeType;
import enums.TrafficStatus;
import twaver.Dummy;
import twaver.TDataBox;

public class DesignTraffic {

	private JFrame frame;
	private static JTextField jtluyou;
	private static JLabel yewu,luyou;

	
	public static void main(String[] args) {
		
	}

	
	public DesignTraffic() {
		frame = new JFrame();
		Font f=new Font("΢���ź�", Font.PLAIN, 12);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(DesignTraffic.class.getResource("/resource/titlexiao.png")));
		frame.setTitle("����ҵ��·��");
		frame.setBounds(100, 100, 450, 300);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		luyou = new JLabel("·�ɣ��ԡ�-�������");
		luyou.setBounds(56, 42, 114, 23);
		luyou.setFont(f);
		frame.getContentPane().add(luyou);
		
		
		jtluyou = new JTextField("����·��");
		jtluyou.setBounds(208, 43, 125, 21);
		frame.getContentPane().add(jtluyou);
		jtluyou.setColumns(10);
		
		yewu = new JLabel("ҵ��");
		yewu.setBounds(87, 109, 37, 15);
		yewu.setFont(f);
		frame.getContentPane().add(yewu);
		
		
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(208, 106, 125, 21);
		frame.getContentPane().add(comboBox);
		for(int i = 0;i < Traffic.trafficList.size();i++){
			comboBox.addItem(Traffic.trafficList.get(i).getFromNode().getName()+"-"+Traffic.trafficList.get(i).getToNode().getName()+" "+i);
			
		}
		comboBox.setSelectedIndex(0);
		//comboBox.setFont(f);
		
		JButton button = new JButton("ȷ��");
		button.setBounds(77, 205, 93, 23);
		button.setFont(f);
		frame.getContentPane().add(button);
		
		JButton button_1 = new JButton("ȡ��");
		button_1.setBounds(240, 205, 93, 23);
		button_1.setFont(f);
		frame.getContentPane().add(button_1);
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
												
				if(!Route.isRouteTrue(String.valueOf(jtluyou.getText())))
					{JOptionPane.showMessageDialog(null,"����·������");}
				
		
				int choice=0;
				for(int i=0;i<data.Traffic.trafficList.size();i++)
				{
					if(comboBox.getSelectedItem().toString().equals(Traffic.trafficList.get(i).getFromNode().getName()+"-"+Traffic.trafficList.get(i).getToNode().getName()+" "+i)){
						choice=i;
					}
				}
				
//				List<CommonNode> nodelist=Route.rgnodelist(String.valueOf(jtluyou.getText()));
//				List<WDMLink> wdmlist=Route.rgwdmlinklist(String.valueOf(jtluyou.getText()));
				Route r1=Route.rgRoute(String.valueOf(jtluyou.getText()));
				r1.setBelongsTraffic(Traffic.trafficList.get(choice));
				
				if (CommonAlloc.allocateWaveLength(r1) && PortAlloc.allocatePort(r1)) {// �Թ�����·����ʱ϶�ɹ������ҷ���˿ڳɹ�
					System.out.println("����ɹ�");
				//	CommonAlloc.fallBuffer.append("ҵ��:" + traffic + "����·�ɼ���Դ����ɹ���\r\n");
					Traffic.trafficList.get(choice).setWorkRoute(r1);// ���·�ɲ�Ϊ��������Ϊ����·��
					Traffic.trafficList.get(choice).setStatus(TrafficStatus.�����ѷ���);
				}
				else
					JOptionPane.showMessageDialog(null,"��·���޿�����Դ");
				
				
				frame.dispose();// �ͷŴ�����Դ
			}
		});
		///jb2 = new JButton("ȡ��");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);// ��ʧ����
				frame.dispose();// �ͷŴ�����Դ
			}
		});
		frame.setVisible(true);
	}
}
