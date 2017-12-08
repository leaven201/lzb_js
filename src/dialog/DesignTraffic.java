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
		Font f=new Font("微软雅黑", Font.PLAIN, 12);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(DesignTraffic.class.getResource("/resource/titlexiao.png")));
		frame.setTitle("设置业务路由");
		frame.setBounds(100, 100, 450, 300);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		luyou = new JLabel("路由（以“-”间隔）");
		luyou.setBounds(56, 42, 114, 23);
		luyou.setFont(f);
		frame.getContentPane().add(luyou);
		
		
		jtluyou = new JTextField("输入路由");
		jtluyou.setBounds(208, 43, 125, 21);
		frame.getContentPane().add(jtluyou);
		jtluyou.setColumns(10);
		
		yewu = new JLabel("业务");
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
		
		JButton button = new JButton("确定");
		button.setBounds(77, 205, 93, 23);
		button.setFont(f);
		frame.getContentPane().add(button);
		
		JButton button_1 = new JButton("取消");
		button_1.setBounds(240, 205, 93, 23);
		button_1.setFont(f);
		frame.getContentPane().add(button_1);
		
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
												
				if(!Route.isRouteTrue(String.valueOf(jtluyou.getText())))
					{JOptionPane.showMessageDialog(null,"输入路由有误");}
				
		
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
				
				if (CommonAlloc.allocateWaveLength(r1) && PortAlloc.allocatePort(r1)) {// 对光纤链路分配时隙成功，并且分配端口成功
					System.out.println("分配成功");
				//	CommonAlloc.fallBuffer.append("业务:" + traffic + "工作路由及资源分配成功！\r\n");
					Traffic.trafficList.get(choice).setWorkRoute(r1);// 如果路由不为空则设置为工作路由
					Traffic.trafficList.get(choice).setStatus(TrafficStatus.工作已分配);
				}
				else
					JOptionPane.showMessageDialog(null,"该路由无可用资源");
				
				
				frame.dispose();// 释放窗口资源
			}
		});
		///jb2 = new JButton("取消");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setVisible(false);// 消失窗口
				frame.dispose();// 释放窗口资源
			}
		});
		frame.setVisible(true);
	}
}
