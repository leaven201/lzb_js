package dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import algorithm.ResourceAlloc;
import algorithm.RouteAlloc;
import algorithm.algorithm;
import data.CommonNode;
import data.DataSave;
import data.FiberLink;
import data.Network;
//import data.SpanLink;
import data.Traffic;
import data.TrafficGroup;
import data.WDMLink;

public class Dlg_PolicySetting extends JFrame {
	private JPanel p = null; // 放按钮的JPanel
	private JButton cancel = null; // 取消
	private JButton confirm = null; // 完成
	private JPanel pane1;
	private JComboBox trafficTeam = new JComboBox();// 业务组
	private JComboBox trafficLayer = new JComboBox();// 业务所属层
	// private JCheckBox[] ifCrossdomain = new JCheckBox[2];//是否允许跨域算路
	private JCheckBox[] ifSRLG = new JCheckBox[2];
	private JTextField limit = new JTextField(5);// 次优路门限
	private JTextField percent;// 资源占用门限
	private static JRadioButton[] arithmetic = new JRadioButton[4];// 选择路由算法
	private static JRadioButton[] ifOSNR = new JRadioButton[2];
	private static JRadioButton[] arithmetic_new = new JRadioButton[2];// 选择重路由策略
	private static JRadioButton[] separate = new JRadioButton[2];// 选择关联业务组中分离链路还是分离节点和链路
	private static JRadioButton[] separate1 = new JRadioButton[2];// 选择关联业务组中，工作及保护均分离还是仅工作分离
	private static JRadioButton[] ifLimit = new JRadioButton[2];// 是否限制跳数
	private static JLabel jl1 = new JLabel("锁定数:   ");// 波道锁定
	private static JTextField jtf1 = new JTextField(10);

	private ImageIcon title = new ImageIcon(getClass().getResource("/resource/titlePolicy1.jpg"));
	public static String trafficLayerchoice;// 记录所选业务层
	public static String trafficTeamchoice;// 记录所选业务组
	public static int flag = 1;
	public static boolean isDesign = false;
	public static int a =1;
	public static int b =1;
	public static boolean c = true;
	public static int d =0;
	public static boolean f =true;
	public static int h =1;

	public Dlg_PolicySetting() {
		// super(owner,"规划向导",true);Frame owner
		this.setTitle("规划向导");
		// Dlg_SmartPlan.smartflag=2;
		cancel = new JButton(" 取消 ");
		confirm = new JButton(" 确定 ");
		trafficLayer.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				trafficLayerchoice = trafficLayer.getSelectedItem().toString();
			}
		});// 监听存储所选业务层
		trafficTeam.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (trafficTeam.getSelectedIndex() != 0) {
					trafficLayer.setEnabled(false);
				} else {
					trafficLayer.setEnabled(true);
				}
				trafficTeamchoice = trafficTeam.getSelectedItem().toString();
			}
		});// 监听并记录所选业务层，当选择非全部业务组时业务层按钮设为不可编辑 2013.12.4 Qu
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();// 释放窗体所占内存资源
			}
		});
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (CommonNode.allNodeList.size() == 0)
					JOptionPane.showMessageDialog(null, "请导入节点资源！", "提示", JOptionPane.INFORMATION_MESSAGE);
				if (FiberLink.fiberLinkList.size() == 0 || WDMLink.WDMLinkList.size() == 0)
					JOptionPane.showMessageDialog(null, "请导入链路资源！", "提示", JOptionPane.INFORMATION_MESSAGE);
				if (Traffic.trafficList.size() == 0)
					JOptionPane.showMessageDialog(null, "请导入业务资源！", "提示", JOptionPane.INFORMATION_MESSAGE);
				if (!jtf1.getText().equals("")
						&& (Integer.parseInt(jtf1.getText()) < 0 || Integer.parseInt(jtf1.getText()) > 80))
					JOptionPane.showMessageDialog(null, "请输入0~80锁波数!", "提示", JOptionPane.INFORMATION_MESSAGE);
				// if (limit.getText() == null || limit.getText().trim().equals(""))
				// JOptionPane.showMessageDialog(null, "请输入OSNR阈值！", "提示",
				// JOptionPane.INFORMATION_MESSAGE);
				// if(ifSRLG[0].isSelected())
				else {
					if (separate[0].isSelected()) {
						   a=1;
					}else {
						   a=2;
					}
					
					if (separate1[0].isSelected()) {
						b = 1;
					} else {
						b = 2;
					}
					if(ifOSNR[0].isSelected()) {
						c = true;
					}else {
						c = false;
					}
					if (!jtf1.getText().equals("") ) {
						d = Integer.parseInt(jtf1.getText());
					}
					if (arithmetic_new[1].isSelected()) {
						DataSave.reRoutePolicy = 2;
						f = true;
					}else {
						DataSave.reRoutePolicy = 1;
						f =false;
					}
					if (arithmetic[1].isSelected()) {
						h = 1;
					}
					if (arithmetic[2].isSelected()) {
						h = 0;
					}
					if (arithmetic[3].isSelected()) {
						h = 2;
					}
					if (arithmetic[0].isSelected()) {
						h = 3;
					}
					setVisible(false);
					
					
//					isDesign = true;
//					List<Traffic> traList = new LinkedList<Traffic>();
//					traList.addAll(Traffic.trafficList);
//					if (!RouteAlloc.isRouteEmpty(traList)) {// 若路由不为空
//						int test = JOptionPane.showConfirmDialog(null, "规划已进行，是否清空路由？");
//						switch (test) {
//						case 0:// 清空路由
//							RouteAlloc.clearAllTrafficRoute(traList);
//							// 对关联业务组分离策略进行赋值
//							if (separate[0].isSelected()) {
//								DataSave.separate = 1;
//							} else {
//								DataSave.separate = 2;
//							}
//							// 对关联业务组工作及保护分离策略进行赋值
//							if (separate1[0].isSelected()) {
//								DataSave.separate1 = 1;
//							} else {
//								DataSave.separate1 = 2;
//							}
//							//对使用加入OSNR判决进行赋值
//							if(ifOSNR[0].isSelected()) {
//								DataSave.OSNR = true;
//							}else {
//								DataSave.OSNR = false;
//							}
//							// 对波道锁定数进行赋值
//							if (!jtf1.getText().equals("") ) {
//								DataSave.locknum = Integer.parseInt(jtf1.getText());
//							}
//							if (arithmetic_new[1].isSelected()) {
//								if (arithmetic[1].isSelected()) {// 最小跳
//									ResourceAlloc.allocateResource1(traList, 1);
//								}
//								if (arithmetic[2].isSelected()) {// 最短路径
//									ResourceAlloc.allocateResource1(traList, 0);
//								}
//								if (arithmetic[3].isSelected()) {// 负载均衡
//									ResourceAlloc.allocateResource1(traList, 2);
//								}
//								if (arithmetic[0].isSelected()) {// 自定义
//									ResourceAlloc.allocateResource1(traList, 3);
//								}
//							} else {
//								if (arithmetic[1].isSelected()) {// 最小跳
//									ResourceAlloc.allocateResource(traList, 1);
//								}
//								if (arithmetic[2].isSelected()) {// 最短路径
//									ResourceAlloc.allocateResource(traList, 0);
//								}
//								if (arithmetic[3].isSelected()) {// 负载均衡
//									ResourceAlloc.allocateResource(traList, 2);
//								}
//								if (arithmetic[0].isSelected()) {// 自定义
//									ResourceAlloc.allocateResource(traList, 3);
//								}
//							}
//							
//							Dlg_PolicySetting.this.setVisible(false);
//							dispose();// 释放窗体所占内存资源
// 							new Dlg_DesignResult();
//							break;
//						case 1:// 不清空
//								// 对关联业务组分离策略进行赋值
//							if (separate[0].isSelected()) {
//								DataSave.separate = 1;
//							} else {
//								DataSave.separate = 2;
//							}
//							// 对关联业务组工作及保护分离策略进行赋值
//							if (separate1[0].isSelected()) {
//								DataSave.separate1 = 1;
//							} else {
//								DataSave.separate1 = 2;
//							}
//							//对使用加入OSNR判决进行赋值
//							if(ifOSNR[0].isSelected()) {
//								DataSave.OSNR = true;
//							}else {
//								DataSave.OSNR = false;
//							}
//							// 对波道锁定数进行赋值
//							if (!jtf1.getText().equals("") ) {
//								DataSave.locknum = Integer.parseInt(jtf1.getText());
//							}
//							if (arithmetic_new[1].isSelected()) {
//								if (arithmetic[1].isSelected()) {// 最小跳
//									ResourceAlloc.allocateResource1(traList, 1);
//								}
//								if (arithmetic[2].isSelected()) {// 最短路径
//									ResourceAlloc.allocateResource1(traList, 0);
//								}
//								if (arithmetic[3].isSelected()) {// 负载均衡
//									ResourceAlloc.allocateResource1(traList, 2);
//								}
//								if (arithmetic[0].isSelected()) {// 自定义
//									ResourceAlloc.allocateResource1(traList, 3);
//								}
//							} else {
//								if (arithmetic[1].isSelected()) {// 最小跳
//									ResourceAlloc.allocateResource(traList, 1);
//								}
//								if (arithmetic[2].isSelected()) {// 最短路径
//									ResourceAlloc.allocateResource(traList, 0);
//								}
//								if (arithmetic[3].isSelected()) {// 负载均衡
//									ResourceAlloc.allocateResource(traList, 2);
//								}
//								if (arithmetic[0].isSelected()) {// 自定义
//									ResourceAlloc.allocateResource(traList, 3);
//								}
//							}
//							
//							Dlg_PolicySetting.this.setVisible(false);
//							Dlg_PolicySetting.this.dispose();
//							new Dlg_DesignResult();
//							break;
//						case 2:
//							break;
//						}
//					} else {// 路由为空
//						// 对关联业务组分离策略进行赋值
//						if (separate[0].isSelected()) {
//							DataSave.separate = 1;
//						} else {
//							DataSave.separate = 2;
//						}
//						// 对关联业务组工作及保护分离策略进行赋值
//						if (separate1[0].isSelected()) {
//							DataSave.separate1 = 1;
//						} else {
//							DataSave.separate1 = 2;
//						}
//						//对使用加入OSNR判决进行赋值
//						if(ifOSNR[0].isSelected()) {
//							DataSave.OSNR = true;
//						}else {
//							DataSave.OSNR = false;
//						}
//						// 对波道锁定数进行赋值
//						if (!jtf1.getText().equals("") ) {
//							DataSave.locknum = Integer.parseInt(jtf1.getText());
//						}
//						if (arithmetic_new[1].isSelected()) {
//							if (arithmetic[1].isSelected()) {// 最小跳
//								ResourceAlloc.allocateResource1(traList, 1);
//							}
//							if (arithmetic[2].isSelected()) {// 最短路径
//								ResourceAlloc.allocateResource1(traList, 0);
//							}
//							if (arithmetic[3].isSelected()) {// 负载均衡
//								ResourceAlloc.allocateResource1(traList, 2);
//							}
//							if (arithmetic[0].isSelected()) {// 自定义
//								ResourceAlloc.allocateResource1(traList, 3);
//							}
//						} else {
//							if (arithmetic[1].isSelected()) {// 最小跳
//								ResourceAlloc.allocateResource(traList, 1);
//							}
//							if (arithmetic[2].isSelected()) {// 最短路径
//								ResourceAlloc.allocateResource(traList, 0);
//							}
//							if (arithmetic[3].isSelected()) {// 负载均衡
//								ResourceAlloc.allocateResource(traList, 2);
//							}
//							if (arithmetic[0].isSelected()) {// 自定义
//								ResourceAlloc.allocateResource(traList, 3);
//							}
//						}
//						
//						setVisible(false);
//						dispose();// 释放窗体所占内存资源
//						new Dlg_DesignResult();
//					}
//				}
//
//				// if(arithmetic_new[1].isSelected())
//				// {
//				// if(arithmetic[1].isSelected()) {//最小跳
//				// ResourceAlloc.allocateResource1(traList, 1);
//				// }
//				// if(arithmetic[2].isSelected()) {//最短路径
//				// ResourceAlloc.allocateResource1(traList, 0);
//				// }
//				// }
//				// else
//				// {
//				// if(arithmetic[1].isSelected()) {//最小跳
//				// ResourceAlloc.allocateResource(traList, 1);
//				// }
//				// if(arithmetic[2].isSelected()) {//最短路径
//				// ResourceAlloc.allocateResource(traList, 0);
//				// }
//				// }
//				// setVisible(false);
//				// dispose();//释放窗体所占内存资源
//				// new Dlg_DesignResult();
				 }
			}
		});

		// trafficTeam.addItem("");
		trafficTeam.addItem("全部");
		// for(int i = 0;i < TrafficGroupTrafficGroupList.size();i++){
		// trafficTeam.addItem(TrafficGroup.TrafficGroupList.get(i).getM_sName());
		// }
		trafficTeam.setSelectedIndex(0);
		String[] s1 = { "全部", "FIBER", "WDM", "OTN", "SDH", "ASON" };
		for (int i = 0; i < s1.length; i++) {
			trafficLayer.addItem(s1[i]);
		}
		trafficLayer.setSelectedIndex(0);
		p = new JPanel();
		GridBagLayout mg = new GridBagLayout();
		GridBagConstraints mgc = new GridBagConstraints();
		Insets insert;
		p.setLayout(mg);
		p.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 5));
		mgc.gridwidth = 1;
		mgc.weighty = 1.0;
		insert = new Insets(5, 0, 5, 20);
		mgc.insets = insert;
		mgc.fill = GridBagConstraints.NONE;
		p.add(confirm);
		mgc.gridwidth = 2;
		p.add(new JLabel("           "));
		insert = new Insets(5, 20, 5, 0);
		mgc.insets = insert;
		mgc.fill = GridBagConstraints.NONE;
		mgc.insets = insert;
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		p.add(cancel);
		pane1 = new JPanel();
		initpane1();
		// pane.add(pane1);
		this.getContentPane().setBackground(Color.WHITE);
		this.getContentPane().add(pane1);
		this.getContentPane().add(p, BorderLayout.SOUTH);
		this.setSize(560, 630);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);

	}

	public void initpane1() {
		JLabel titlepane = new JLabel(title);
		// JPanel p1 = new JPanel();
		// p1.setBorder (BorderFactory.createEtchedBorder ());
		// JPanel p2 = new JPanel();
		// p2.setBorder (BorderFactory.createEtchedBorder ());
		JPanel p3 = new JPanel();
		p3.setBorder(BorderFactory.createTitledBorder("选择路由算法"));

		JPanel p4 = new JPanel();
		p4.setBorder(BorderFactory.createTitledBorder("重路由策略"));

		JPanel p5 = new JPanel();
		p5.setBorder(BorderFactory.createTitledBorder("是否考虑OSNR"));
		ifSRLG[0] = new JCheckBox("是", true);
		ifSRLG[1] = new JCheckBox("否");
		ButtonGroup group4 = new ButtonGroup();
		group4.add(ifSRLG[0]);
		group4.add(ifSRLG[1]);

		JPanel p6 = new JPanel();
		p6.setBorder(BorderFactory.createTitledBorder("关联业务组分离策略"));

		JPanel p7 = new JPanel();
		p7.setBorder(BorderFactory.createTitledBorder("波道锁定策略"));

		// limit.setHorizontalAlignment(JTextField.RIGHT);
		limit = new JTextField("200", 6);
		limit.setDocument(new NumberLenghtLimitedDmt(8));
		limit.setText("200");
		percent = new JTextField("100", 6);
		percent.setDocument(new NumberLenghtLimitedDmt(7));
		percent.setText("100");

		final JButton set = new JButton("算法设置");
		final JButton setT = new JButton("跳数设置");
		arithmetic[0] = new JRadioButton("自定义权值法", true);
		arithmetic[1] = new JRadioButton("最小跳数法");
		arithmetic[2] = new JRadioButton("最短路径法");
		arithmetic[3] = new JRadioButton("负载均衡法");
		//arithmetic[4] = new JRadioButton("最短时延法");

		arithmetic_new[0] = new JRadioButton("预置重路由", true);
		arithmetic_new[1] = new JRadioButton("动态重路由");

		separate[0] = new JRadioButton("链路分离", true);
		separate[1] = new JRadioButton("节点分离");
		
		separate1[0] = new JRadioButton("工作保护路由均分离");
		separate1[1] = new JRadioButton("仅工作路由分离", true);

		ifOSNR[0] = new JRadioButton("是", true);
		ifOSNR[1] = new JRadioButton("否");

		ButtonGroup group1 = new ButtonGroup();
		group1.add(arithmetic[0]);
		group1.add(arithmetic[1]);
		group1.add(arithmetic[2]);
		group1.add(arithmetic[3]);
		//group1.add(arithmetic[4]);

		ifLimit[0] = new JRadioButton("是");
		ifLimit[1] = new JRadioButton("否", true);
		ButtonGroup group3 = new ButtonGroup();
		group3.add(ifLimit[0]);
		group3.add(ifLimit[1]);

		ButtonGroup group6 = new ButtonGroup();
		group6.add(arithmetic_new[0]);
		group6.add(arithmetic_new[1]);

		ButtonGroup group7 = new ButtonGroup();
		group7.add(ifOSNR[0]);
		group7.add(ifOSNR[1]);

		ButtonGroup group8 = new ButtonGroup();
		group8.add(separate[0]);
		group8.add(separate[1]);
		ButtonGroup group9 = new ButtonGroup();
		group9.add(separate1[0]);
		group9.add(separate1[1]);

		arithmetic[0].addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (arithmetic[0].isSelected())
					set.setEnabled(true);
				else
					set.setEnabled(false);
			}
		});
		arithmetic[1].addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				set.setEnabled(false);
			}
		});
		arithmetic[2].addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				set.setEnabled(false);
			}
		});
		arithmetic[3].addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				set.setEnabled(false);
			}
		});
//		arithmetic[4].addItemListener(new ItemListener() {
//			public void itemStateChanged(ItemEvent e) {
//				set.setEnabled(false);
//			}
//		});
		setT.setEnabled(false);
		// ifLimit[0].addItemListener(new ItemListener() {
		// public void itemStateChanged(ItemEvent e) {
		// if(ifLimit[0].isSelected())
		// setT.setEnabled(true);
		// else
		// setT.setEnabled(false);
		// }
		// });
		// setT.addActionListener(new ActionListener(){
		// public void actionPerformed(ActionEvent e){
		// new SetT();
		// }
		// });
		set.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (arithmetic[0].isSelected())
					new SetZ(Dlg_PolicySetting.this);
			}
		});
		Insets insert;
		GridBagLayout mg = new GridBagLayout();
		GridBagConstraints mgc = new GridBagConstraints();

		// p3：选择路由算法
		mg = new GridBagLayout();
		mgc = new GridBagConstraints();
		p3.setLayout(mg);
		insert = new Insets(0, 0, 0, 0);
		mgc.insets = insert;
		mgc.gridwidth = 1;
		mgc.weightx = 1.0;
		mgc.weighty = 1.0;
		mgc.gridheight = 1;
		mgc.anchor = GridBagConstraints.WEST;
		mgc.gridwidth = 1;
		insert = new Insets(5, 0, 0, 0);
		mgc.insets = insert;
		mgc.fill = GridBagConstraints.NONE;
		mgc.anchor = GridBagConstraints.CENTER;
		p3.add(arithmetic[0], mgc);
		
		mgc.gridwidth = 2;
		insert = new Insets(5, -15, 0, 10);
		mgc.insets = insert;
		p3.add(arithmetic[1], mgc);
		mgc.gridwidth = 3;
		insert = new Insets(5, -35, 0, 20);
		mgc.insets = insert;
		p3.add(arithmetic[2], mgc);
		insert = new Insets(5, -35, 0, 0);
		mgc.insets = insert;
		p3.add(arithmetic[3], mgc);
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		insert = new Insets(5, -35, 0, 0);
		mgc.insets = insert;
//		p3.add(arithmetic[4], mgc);
//		mgc.anchor = GridBagConstraints.EAST;
//		insert = new Insets(0, 0, 0, 30);
//		mgc.insets = insert;
		p3.add(set, mgc);

		// p4：重路由选择
		mg = new GridBagLayout();
		mgc = new GridBagConstraints();
		p4.setLayout(mg);
		insert = new Insets(0, 0, 0, 0);
		mgc.insets = insert;
		mgc.gridwidth = 1;
		mgc.weightx = 1.0;
		mgc.weighty = 1.0;
		mgc.gridheight = 1;
		mgc.anchor = GridBagConstraints.WEST;
		mgc.gridwidth = 1;
		insert = new Insets(5, 0, 0, 0);
		mgc.insets = insert;
		mgc.fill = GridBagConstraints.NONE;
		mgc.anchor = GridBagConstraints.CENTER;
		p4.add(arithmetic_new[0], mgc);
		mgc.gridwidth = 2;
		insert = new Insets(5, -15, 0, 10);
		mgc.insets = insert;
		p4.add(arithmetic_new[1], mgc);
		// mgc.gridwidth = 3;
		// insert = new Insets(5, -35, 0, 20);
		// mgc.insets = insert;

		// 是否osnr
		mg = new GridBagLayout();
		mgc = new GridBagConstraints();
		p5.setLayout(mg);
		insert = new Insets(0, 0, 0, 0);
		mgc.insets = insert;
		mgc.gridwidth = 1;
		mgc.weightx = 1.0;
		mgc.weighty = 1.0;
		mgc.gridheight = 1;
		mgc.anchor = GridBagConstraints.WEST;
		mgc.gridwidth = 1;
		insert = new Insets(0, 0, 0, 0);
		mgc.insets = insert;
		mgc.fill = GridBagConstraints.NONE;
		mgc.anchor = GridBagConstraints.CENTER;
		p5.add(ifOSNR[0], mgc);
		mgc.gridwidth = 2;
		insert = new Insets(5, -15, 0, 10);
		mgc.insets = insert;
		p5.add(ifOSNR[1], mgc);
		// mgc.gridwidth = 3;
		// insert = new Insets(5, -35, 0, 20);
		// mgc.insets = insert;

		// 关联业务组
//		mg = new GridBagLayout();
//		mgc = new GridBagConstraints();
//		p6.setLayout(mg);
//		insert = new Insets(0, 0, 0, 0);
//		mgc.insets = insert;
//		mgc.gridwidth = 1;
//		mgc.weightx = 1.0;
//		mgc.weighty = 1.0;
//		mgc.gridheight = 1;
//		mgc.anchor = GridBagConstraints.WEST;
//		mgc.gridwidth = 1;
//		insert = new Insets(0, 0, 0, 0);
//		mgc.insets = insert;
//		mgc.fill = GridBagConstraints.NONE;
//		mgc.anchor = GridBagConstraints.CENTER;
		GridBagLayout layout = new GridBagLayout();
		p6.setLayout(layout);
		p6.add(separate[0]);
		p6.add(separate[1]);
		p6.add(separate1[1]);
		p6.add(separate1[0]);
		GridBagConstraints s= new GridBagConstraints();//定义一个GridBagConstraints，是用来控制添加进的组件的显示位置
		s.fill = GridBagConstraints.BOTH; //该方法是为了设置如果组件所在的区域比组件本身要大时的显示情况
		s.anchor = GridBagConstraints.WEST;
		s.gridwidth=1;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        s.weightx = 0;//该方法设置组件水平的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        s.weighty=0;//该方法设置组件垂直的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        insert = new Insets(0, 50, 0, 0);
        s.insets = insert;
        layout.setConstraints(separate[0], s);//设置组件
        s.gridwidth=0;
        s.weightx = 0;
        s.weighty=0;
        layout.setConstraints(separate[1], s);
        s.gridwidth=1;
        s.weightx = 0;
        s.weighty=0;
        layout.setConstraints(separate1[0], s);
        layout.setConstraints(separate1[1], s);
        s.gridwidth=0;
        s.weightx = 0;
        s.weighty=0;

//		p6.add(separate[0], mgc);
//		mgc.gridwidth = 2;
//		insert = new Insets(5, -15, 0, 10);
//		mgc.insets = insert;
//		p6.add(separate[1], mgc);
//		mgc.gridwidth = 14;
//		insert = new Insets(5, -35, 0, 20);
//		mgc.insets = insert;
		
//		mg = new GridBagLayout();
//		mgc = new GridBagConstraints();
//		p6.setLayout(mg);
//		insert = new Insets(0, 0, 0, 0);
//		mgc.insets = insert;
//		mgc.gridwidth = 1;
//		mgc.weightx = 1.0;
//		mgc.weighty = 1.0;
//		mgc.gridheight = 2;
//		mgc.anchor = GridBagConstraints.WEST;
//		mgc.gridwidth = 1;
//		insert = new Insets(0, 0, 0, 0);
//		mgc.insets = insert;
//		mgc.fill = GridBagConstraints.NONE;
//		mgc.anchor = GridBagConstraints.CENTER;
//		p6.add(separate1[0], mgc);
//		mgc.gridwidth = 2;
//		insert = new Insets(5, -15, 0, 10);
//		mgc.insets = insert;
//		p6.add(separate1[1], mgc);
//		mgc.gridwidth = 3;
//		insert = new Insets(5, -35, 0, 20);
//		mgc.insets = insert;

		// 波道锁定
//		mg = new GridBagLayout();
//		mgc = new GridBagConstraints();
//		p7.setLayout(mg);
//		insert = new Insets(0, 0, 0, 0);
//		mgc.insets = insert;
//		// mgc.gridwidth = 1;
//		mgc.weightx = 1.0;
//		mgc.weighty = 1.0;
//		mgc.gridheight = 1;
//		// mgc.anchor = GridBagConstraints.WEST;
//		mgc.gridwidth = 1;
//		insert = new Insets(0, 0, 0, 0);
//		mgc.insets = insert;
//		mgc.fill = GridBagConstraints.NONE;
//		mgc.anchor = GridBagConstraints.CENTER;
//		p7.add(jl1, mgc);
//		mgc.gridwidth = 2;
//		insert = new Insets(0, 0, 0, 0);
//		mgc.insets = insert;
//		mgc.anchor = GridBagConstraints.CENTER;
//		p7.add(jtf1, mgc);
        
        
        layout = new GridBagLayout();
		p7.setLayout(layout);
		p7.add(jl1);
		p7.add(jtf1);

		s= new GridBagConstraints();//定义一个GridBagConstraints，是用来控制添加进的组件的显示位置
		s.fill = GridBagConstraints.BOTH; //该方法是为了设置如果组件所在的区域比组件本身要大时的显示情况
		s.anchor = GridBagConstraints.WEST;
		s.gridwidth=3;//该方法是设置组件水平所占用的格子数，如果为0，就说明该组件是该行的最后一个
        s.weightx = 0;//该方法设置组件水平的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        s.weighty=0;//该方法设置组件垂直的拉伸幅度，如果为0就说明不拉伸，不为0就随着窗口增大进行拉伸，0到1之间
        insert = new Insets(0, 10, 0, 0);
        s.insets = insert;
        
        layout.setConstraints(jl1, s);//设置组件

        s.gridwidth=0;
        s.weightx = 0;
        s.weighty=0;
        layout.setConstraints(jtf1, s);
        
        
        
		// mgc.gridwidth = 3;
		// insert = new Insets(5, -35, 0, 20);
		// mgc.insets = insert;
		// p3.add(arithmetic[2],mgc);
		// insert = new Insets(5,-35,0,0);
		// mgc.insets = insert;
		// p3.add(arithmetic[3],mgc);
		// mgc.gridwidth = GridBagConstraints.REMAINDER;
		// insert = new Insets(5,-35,0,0);
		// mgc.insets = insert;
		// p3.add(arithmetic[4],mgc);
		// mgc.anchor = GridBagConstraints.EAST;
		// insert = new Insets(0,0,0,30);
		// mgc.insets = insert;
		// p4.add(set,mgc);

		mg = new GridBagLayout();
		mgc = new GridBagConstraints();
		pane1.setLayout(mg);
		mgc.fill = GridBagConstraints.BOTH;

		mgc.gridwidth = GridBagConstraints.REMAINDER;
		mgc.gridheight = 1;
		mgc.weighty = 1.0;
		mgc.weightx = 11.0;
		insert = new Insets(-5, 0, 5, 0);
		mgc.insets = insert;
		pane1.add(titlepane, mgc);

		mgc.gridwidth = GridBagConstraints.REMAINDER;
		mgc.gridheight = 2;
		mgc.weighty = 1.0;
		mgc.weightx = 11.0;
		insert = new Insets(0, 2, 2, 2);
		mgc.insets = insert;
		pane1.add(p3, mgc);
		pane1.add(p4, mgc);
		pane1.add(p5, mgc);
		pane1.add(p6, mgc);
		pane1.add(p7, mgc);
	}

	public static void main(String[] args) {
		Dlg_PolicySetting a = new Dlg_PolicySetting();
	}

	public static JRadioButton[] getArithmetic_new() {
		return arithmetic_new;
	}

	public static void setArithmetic_new(JRadioButton[] arithmetic_new) {
		Dlg_PolicySetting.arithmetic_new = arithmetic_new;
	}

}

class NumberLenghtLimitedDmt extends PlainDocument {

	private int limit;

	public NumberLenghtLimitedDmt(int limit) {
		super();
		this.limit = limit;
	}

	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		if (str == null) {
			return;
		}
		if ((getLength() + str.length()) <= limit) {

			char[] upper = str.toCharArray();
			int length = 0;
			for (int i = 0; i < upper.length; i++) {
				if (upper[i] >= '0' && upper[i] <= '9') {
					upper[length++] = upper[i];
				}
			}
			super.insertString(offset, new String(upper, 0, length), attr);
		}
	}

}

class SetZ extends JDialog {
	private JTextField aValue;
	private JTextField bValue;
	private JTextField cValue;
	private JTextField dValue;
	// private JTextField eValue;
	// private JTextField fValue;
	private JLabel A = new JLabel(" A +");
	private JLabel B = new JLabel(" B +");
	private JLabel C = new JLabel(" C ");
	private JLabel D = new JLabel(" D ");
	// private JLabel E = new JLabel(" E +");
	// private JLabel F = new JLabel(" F");
	private JLabel explication1 = new JLabel("1、A：距离，B：跳数，C：负载");
	private JLabel explication2 = new JLabel("2、各系数值处于0到1之间（包含0、1），且各系数之和为1。");
	private JLabel explication3 = new JLabel("3、数值越大，因素对结果影响越大。");
	private double[] safeCoefficient = { 0.3, 0.3, 0.4, 0.3 };// 安全性各项相应的系数
	// private int[] areaCoefficient = new int[6]; //地域安全系数
	// private int[] fuctoryCoefficient = new int[3]; //敷设方式安全系数
	// private int[] MaintainComCoefficient = new int[5]; //维护单位安全系数

	private DefaultTableModel regionModel;
	private DefaultTableModel perfunctoryModel;
	private DefaultTableModel timeModel;

	private JTable region;
	private JTable perfunctory;
	private JTable time;

	private final Object[] columnNames1 = { "地域", "安全系数" };
	private final Object[] columnNames2 = { "敷设方式", "安全系数" };
	private final Object[] columnNames3 = { "维护单位", "安全系数" };

	private Object[][] content1 = null;
	private Object[][] content2 = null;
	private Object[][] content3 = null;

	private JButton confirm = new JButton("确定");
	private JButton cancel = new JButton("取消");
	private JPanel mainpane = new JPanel();
	private JPanel pane1 = new JPanel();
	private JPanel pane2 = new JPanel();

	public SetZ(Frame owner) {
		super(owner, "自定义权重设置");
		// this.setTitle("综合代价最小");
		this.setModal(true);
		// safeCoefficient = FiberLink.safeCoefficient;

		// areaCoefficient = FiberLink.areaCoefficient;
		// fuctoryCoefficient = FiberLink.fuctoryCoefficient;
		// MaintainComCoefficient = FiberLink.MaintainComCoefficient;
		aValue = new JTextField(String.valueOf(safeCoefficient[0]), 5);
		bValue = new JTextField(String.valueOf(safeCoefficient[1]), 5);
		cValue = new JTextField(String.valueOf(safeCoefficient[2]), 5);
		dValue = new JTextField(String.valueOf(safeCoefficient[3]), 5);
		dValue.setVisible(false);
		// 创建地域表的Model
		regionModel = new DefaultTableModel(content1, columnNames1);
		region = new JTable(regionModel) {
			public boolean isCellEditable(int row, int col) {
				if (col == 1) {
					return true;
				} else
					return false;
			}

			public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
				if (columnIndex == 1) {
					Integer num = null;
					try {
						num = Integer.parseInt((String) aValue);
						if (!(1 <= num && num <= 9)) {
							JOptionPane.showMessageDialog(null, "只能输入1~9之间的整数!");
							return;
						}
					} catch (Exception ex) {
						javax.swing.JOptionPane.showMessageDialog(null, "只能输入整数!");
						return;

					}
				}
				super.setValueAt(aValue, rowIndex, columnIndex);
			}
		};
		region.getTableHeader().setReorderingAllowed(false);
		sortManager(region);
		MyUtil.makeFace(region, new Color(228, 255, 224));
		JScrollPane scrollPane1 = new JScrollPane(region);
		this.setColumnWidth(region, 0, 100);
		this.setColumnWidth(region, 1, 80);

		// 创建敷设方式表的Model
		perfunctoryModel = new DefaultTableModel(content2, columnNames2);
		perfunctory = new JTable(perfunctoryModel) {
			public boolean isCellEditable(int row, int col) {
				if (col == 1) {
					return true;
				} else
					return false;
			}
		};
		perfunctory.getTableHeader().setReorderingAllowed(false);// 列不可动
		sortManager(perfunctory);
		MyUtil.makeFace(perfunctory, new Color(255, 255, 224));
		JScrollPane scrollPane2 = new JScrollPane(perfunctory);
		this.setColumnWidth(perfunctory, 0, 100);
		this.setColumnWidth(perfunctory, 1, 80);

		// 创建维护单位表的Model
		timeModel = new DefaultTableModel(content3, columnNames3);
		time = new JTable(timeModel) {
			public boolean isCellEditable(int row, int col) {
				if (col == 1) {
					return true;
				} else
					return false;
			}

			public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
				if (columnIndex == 1) {
					Integer num = null;
					try {
						num = Integer.parseInt((String) aValue);
						if (!(1 <= num && num <= 9)) {
							JOptionPane.showMessageDialog(null, "只能输入1~9之间的整数!");
							return;
						}
					} catch (Exception ex) {
						javax.swing.JOptionPane.showMessageDialog(null, "只能输入整数!");
						return;

					}
				}
				super.setValueAt(aValue, rowIndex, columnIndex);
			}
		};
		time.getTableHeader().setReorderingAllowed(false);
		sortManager(time);
		MyUtil.makeFace(time, new Color(233, 244, 255));
		JScrollPane scrollPane3 = new JScrollPane(time);
		this.setColumnWidth(time, 0, 100);
		this.setColumnWidth(time, 1, 80);

		Insets insert;
		GridBagLayout mg = new GridBagLayout();
		GridBagConstraints mgc = new GridBagConstraints();
		// 设置计算公式部分布局
		A.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 15));
		B.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 15));
		C.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 15));
		D.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 15));
		// E.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 15));
		// F.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 15));
		pane1.setBorder(BorderFactory.createTitledBorder("计算公式"));
		pane1.setLayout(mg);
		mgc.gridwidth = 1;
		mgc.gridheight = 1;
		mgc.weighty = 1.0;
		mgc.weightx = 1.0;
		insert = new Insets(0, 10, 0, 0);
		mgc.insets = insert;
		pane1.add(aValue, mgc);
		mgc.gridwidth = 2;
		insert = new Insets(0, 0, 0, 0);
		mgc.insets = insert;
		pane1.add(A, mgc);
		mgc.gridwidth = 3;
		pane1.add(bValue, mgc);
		mgc.gridwidth = 4;
		pane1.add(B, mgc);
		mgc.gridwidth = 5;
		pane1.add(cValue, mgc);
		mgc.gridwidth =6;
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		mgc.anchor = GridBagConstraints.WEST;
		pane1.add(C, mgc);
//		pane1.add(dValue, mgc);
//		insert = new Insets(0, 0, 0, 10);
//		mgc.insets = insert;
//		mgc.gridwidth = GridBagConstraints.REMAINDER;
//		 pane1.add(dValue,mgc);
//		mgc.gridwidth = 8;
//		pane1.add(D, mgc);
//		D.setVisible(false);

		// mgc.gridwidth = 9;
		// pane1.add(eValue,mgc);
		// mgc.gridwidth = 10;
		// pane1.add(E,mgc);
		// mgc.gridwidth = 11;
		// pane1.add(fValue,mgc);
		// insert = new Insets(0,0,0,10);
		// mgc.insets = insert;
		// mgc.gridwidth = GridBagConstraints.REMAINDER;
		// pane1.add(F,mgc);
		// mgc.gridwidth= GridBagConstraints.REMAINDER;
		// mgc.anchor = GridBagConstraints.WEST;
		pane1.add(explication1, mgc); // 加上说明1
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		pane1.add(explication2, mgc);// 加上说明2
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		pane1.add(explication3, mgc);// 加上说明3

		// 主面板布局设置
		mg = new GridBagLayout();
		mgc = new GridBagConstraints();
		mainpane.setLayout(mg);
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		mgc.gridheight = 1;
		mgc.weighty = 1.0;
		mgc.weightx = 1.0;
		insert = new Insets(5, 6, 0, 6);
		mgc.insets = insert;
		mgc.fill = GridBagConstraints.BOTH;
		mainpane.add(pane1, mgc);
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		insert = new Insets(5, 0, 5, 20);
		mgc.insets = insert;
		mainpane.add(confirm);
		mgc.gridwidth = 2;
		insert = new Insets(5, 20, 5, 0);
		mgc.insets = insert;
		mainpane.add(cancel);
		this.setContentPane(mainpane);

		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 若存在系数未填写情况则弹出提示
				if (aValue.getText().isEmpty() || bValue.getText().isEmpty() || cValue.getText().isEmpty()
						) {
					JOptionPane.showMessageDialog(null, "请填写系数！系数为0-1，包含0、1");
				} else {
					safeCoefficient[0] = Double.valueOf(aValue.getText());
					safeCoefficient[1] = Double.valueOf(bValue.getText());
					safeCoefficient[2] = Double.valueOf(cValue.getText());

//					safeCoefficient[3] = Double.valueOf(dValue.getText());
					// safeCoefficient[4] = Double.valueOf(eValue.getText());
					// safeCoefficient[5] = Double.valueOf(fValue.getText());
					double safe = 0;
					for (int i = 0; i < 3; i++) {
						safe += safeCoefficient[i];// 计算系数和
					}
					if (safe != 1.0) {
						JOptionPane.showMessageDialog(null, "各系数之和必须为1!");
					} else {
						DataSave.indexA = Double.valueOf(aValue.getText());
						DataSave.indexB = Double.valueOf(bValue.getText());
						DataSave.indexC = Double.valueOf(cValue.getText());
						// for(int i = 0; i < 6; i++){
						// areaCoefficient[i] = Integer.valueOf(region.getValueAt(i, 1).toString());
						// }
						// for(int i = 0; i < 3; i++){
						// fuctoryCoefficient[i] = Integer.valueOf(perfunctory.getValueAt(i,
						// 1).toString());
						// }
						// for(int i = 0; i < 5; i++){
						// MaintainComCoefficient[i] = Integer.valueOf(time.getValueAt(i,
						// 1).toString());
						// }
						// FiberLink.safeCoefficient = safeCoefficient;
						// FiberLink.areaCoefficient = areaCoefficient;
						// FiberLink.fuctoryCoefficient =fuctoryCoefficient;
						// FiberLink.MaintainComCoefficient = MaintainComCoefficient;
						for (int i = 0; i < 3; i++) {
							System.out.println("critics" + (i + 1) + " = " + safeCoefficient[i]);
						}
						setVisible(false);
						dispose();// 释放窗体所占内存资源
					}
				}
			}
		});
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();// 释放窗体所占内存资源
			}
		});
		this.setSize(560, 230);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);

	}

	/**
	 * 表格排序管理器
	 */
	private void sortManager(JTable table) {
		RowSorter sorter = new TableRowSorter(table.getModel());
		table.setRowSorter(sorter);
	}

	public void setColumnWidth(javax.swing.JTable myTable, int columnIndex, int preferredWidth) {
		javax.swing.table.TableColumnModel tcm = myTable.getColumnModel();
		javax.swing.table.TableColumn tc = tcm.getColumn(columnIndex);
		tc.setPreferredWidth(preferredWidth);

	}

//	 public static void main(String[] args){
//	 new SetZ(null);
//	 }
	public static boolean isInteger(String value) {
		try {
			Integer.parseInt(value);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

}