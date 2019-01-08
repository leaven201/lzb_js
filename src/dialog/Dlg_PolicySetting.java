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
	private JPanel p = null; // �Ű�ť��JPanel
	private JButton cancel = null; // ȡ��
	private JButton confirm = null; // ���
	private JPanel pane1;
	private JComboBox trafficTeam = new JComboBox();// ҵ����
	private JComboBox trafficLayer = new JComboBox();// ҵ��������
	// private JCheckBox[] ifCrossdomain = new JCheckBox[2];//�Ƿ����������·
	private JCheckBox[] ifSRLG = new JCheckBox[2];
	private JTextField limit = new JTextField(5);// ����·����
	private JTextField percent;// ��Դռ������
	private static JRadioButton[] arithmetic = new JRadioButton[4];// ѡ��·���㷨
	private static JRadioButton[] ifOSNR = new JRadioButton[2];
	private static JRadioButton[] arithmetic_new = new JRadioButton[2];// ѡ����·�ɲ���
	private static JRadioButton[] separate = new JRadioButton[2];// ѡ�����ҵ�����з�����·���Ƿ���ڵ����·
	private static JRadioButton[] separate1 = new JRadioButton[2];// ѡ�����ҵ�����У����������������뻹�ǽ���������
	private static JRadioButton[] ifLimit = new JRadioButton[2];// �Ƿ���������
	private static JLabel jl1 = new JLabel("������:   ");// ��������
	private static JTextField jtf1 = new JTextField(10);

	private ImageIcon title = new ImageIcon(getClass().getResource("/resource/titlePolicy1.jpg"));
	public static String trafficLayerchoice;// ��¼��ѡҵ���
	public static String trafficTeamchoice;// ��¼��ѡҵ����
	public static int flag = 1;
	public static boolean isDesign = false;
	public static int a =1;
	public static int b =1;
	public static boolean c = true;
	public static int d =0;
	public static boolean f =true;
	public static int h =1;

	public Dlg_PolicySetting() {
		// super(owner,"�滮��",true);Frame owner
		this.setTitle("�滮��");
		// Dlg_SmartPlan.smartflag=2;
		cancel = new JButton(" ȡ�� ");
		confirm = new JButton(" ȷ�� ");
		trafficLayer.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				trafficLayerchoice = trafficLayer.getSelectedItem().toString();
			}
		});// �����洢��ѡҵ���
		trafficTeam.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (trafficTeam.getSelectedIndex() != 0) {
					trafficLayer.setEnabled(false);
				} else {
					trafficLayer.setEnabled(true);
				}
				trafficTeamchoice = trafficTeam.getSelectedItem().toString();
			}
		});// ��������¼��ѡҵ��㣬��ѡ���ȫ��ҵ����ʱҵ��㰴ť��Ϊ���ɱ༭ 2013.12.4 Qu
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();// �ͷŴ�����ռ�ڴ���Դ
			}
		});
		confirm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (CommonNode.allNodeList.size() == 0)
					JOptionPane.showMessageDialog(null, "�뵼��ڵ���Դ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				if (FiberLink.fiberLinkList.size() == 0 || WDMLink.WDMLinkList.size() == 0)
					JOptionPane.showMessageDialog(null, "�뵼����·��Դ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				if (Traffic.trafficList.size() == 0)
					JOptionPane.showMessageDialog(null, "�뵼��ҵ����Դ��", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				if (!jtf1.getText().equals("")
						&& (Integer.parseInt(jtf1.getText()) < 0 || Integer.parseInt(jtf1.getText()) > 80))
					JOptionPane.showMessageDialog(null, "������0~80������!", "��ʾ", JOptionPane.INFORMATION_MESSAGE);
				// if (limit.getText() == null || limit.getText().trim().equals(""))
				// JOptionPane.showMessageDialog(null, "������OSNR��ֵ��", "��ʾ",
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
//					if (!RouteAlloc.isRouteEmpty(traList)) {// ��·�ɲ�Ϊ��
//						int test = JOptionPane.showConfirmDialog(null, "�滮�ѽ��У��Ƿ����·�ɣ�");
//						switch (test) {
//						case 0:// ���·��
//							RouteAlloc.clearAllTrafficRoute(traList);
//							// �Թ���ҵ���������Խ��и�ֵ
//							if (separate[0].isSelected()) {
//								DataSave.separate = 1;
//							} else {
//								DataSave.separate = 2;
//							}
//							// �Թ���ҵ���鹤��������������Խ��и�ֵ
//							if (separate1[0].isSelected()) {
//								DataSave.separate1 = 1;
//							} else {
//								DataSave.separate1 = 2;
//							}
//							//��ʹ�ü���OSNR�о����и�ֵ
//							if(ifOSNR[0].isSelected()) {
//								DataSave.OSNR = true;
//							}else {
//								DataSave.OSNR = false;
//							}
//							// �Բ������������и�ֵ
//							if (!jtf1.getText().equals("") ) {
//								DataSave.locknum = Integer.parseInt(jtf1.getText());
//							}
//							if (arithmetic_new[1].isSelected()) {
//								if (arithmetic[1].isSelected()) {// ��С��
//									ResourceAlloc.allocateResource1(traList, 1);
//								}
//								if (arithmetic[2].isSelected()) {// ���·��
//									ResourceAlloc.allocateResource1(traList, 0);
//								}
//								if (arithmetic[3].isSelected()) {// ���ؾ���
//									ResourceAlloc.allocateResource1(traList, 2);
//								}
//								if (arithmetic[0].isSelected()) {// �Զ���
//									ResourceAlloc.allocateResource1(traList, 3);
//								}
//							} else {
//								if (arithmetic[1].isSelected()) {// ��С��
//									ResourceAlloc.allocateResource(traList, 1);
//								}
//								if (arithmetic[2].isSelected()) {// ���·��
//									ResourceAlloc.allocateResource(traList, 0);
//								}
//								if (arithmetic[3].isSelected()) {// ���ؾ���
//									ResourceAlloc.allocateResource(traList, 2);
//								}
//								if (arithmetic[0].isSelected()) {// �Զ���
//									ResourceAlloc.allocateResource(traList, 3);
//								}
//							}
//							
//							Dlg_PolicySetting.this.setVisible(false);
//							dispose();// �ͷŴ�����ռ�ڴ���Դ
// 							new Dlg_DesignResult();
//							break;
//						case 1:// �����
//								// �Թ���ҵ���������Խ��и�ֵ
//							if (separate[0].isSelected()) {
//								DataSave.separate = 1;
//							} else {
//								DataSave.separate = 2;
//							}
//							// �Թ���ҵ���鹤��������������Խ��и�ֵ
//							if (separate1[0].isSelected()) {
//								DataSave.separate1 = 1;
//							} else {
//								DataSave.separate1 = 2;
//							}
//							//��ʹ�ü���OSNR�о����и�ֵ
//							if(ifOSNR[0].isSelected()) {
//								DataSave.OSNR = true;
//							}else {
//								DataSave.OSNR = false;
//							}
//							// �Բ������������и�ֵ
//							if (!jtf1.getText().equals("") ) {
//								DataSave.locknum = Integer.parseInt(jtf1.getText());
//							}
//							if (arithmetic_new[1].isSelected()) {
//								if (arithmetic[1].isSelected()) {// ��С��
//									ResourceAlloc.allocateResource1(traList, 1);
//								}
//								if (arithmetic[2].isSelected()) {// ���·��
//									ResourceAlloc.allocateResource1(traList, 0);
//								}
//								if (arithmetic[3].isSelected()) {// ���ؾ���
//									ResourceAlloc.allocateResource1(traList, 2);
//								}
//								if (arithmetic[0].isSelected()) {// �Զ���
//									ResourceAlloc.allocateResource1(traList, 3);
//								}
//							} else {
//								if (arithmetic[1].isSelected()) {// ��С��
//									ResourceAlloc.allocateResource(traList, 1);
//								}
//								if (arithmetic[2].isSelected()) {// ���·��
//									ResourceAlloc.allocateResource(traList, 0);
//								}
//								if (arithmetic[3].isSelected()) {// ���ؾ���
//									ResourceAlloc.allocateResource(traList, 2);
//								}
//								if (arithmetic[0].isSelected()) {// �Զ���
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
//					} else {// ·��Ϊ��
//						// �Թ���ҵ���������Խ��и�ֵ
//						if (separate[0].isSelected()) {
//							DataSave.separate = 1;
//						} else {
//							DataSave.separate = 2;
//						}
//						// �Թ���ҵ���鹤��������������Խ��и�ֵ
//						if (separate1[0].isSelected()) {
//							DataSave.separate1 = 1;
//						} else {
//							DataSave.separate1 = 2;
//						}
//						//��ʹ�ü���OSNR�о����и�ֵ
//						if(ifOSNR[0].isSelected()) {
//							DataSave.OSNR = true;
//						}else {
//							DataSave.OSNR = false;
//						}
//						// �Բ������������и�ֵ
//						if (!jtf1.getText().equals("") ) {
//							DataSave.locknum = Integer.parseInt(jtf1.getText());
//						}
//						if (arithmetic_new[1].isSelected()) {
//							if (arithmetic[1].isSelected()) {// ��С��
//								ResourceAlloc.allocateResource1(traList, 1);
//							}
//							if (arithmetic[2].isSelected()) {// ���·��
//								ResourceAlloc.allocateResource1(traList, 0);
//							}
//							if (arithmetic[3].isSelected()) {// ���ؾ���
//								ResourceAlloc.allocateResource1(traList, 2);
//							}
//							if (arithmetic[0].isSelected()) {// �Զ���
//								ResourceAlloc.allocateResource1(traList, 3);
//							}
//						} else {
//							if (arithmetic[1].isSelected()) {// ��С��
//								ResourceAlloc.allocateResource(traList, 1);
//							}
//							if (arithmetic[2].isSelected()) {// ���·��
//								ResourceAlloc.allocateResource(traList, 0);
//							}
//							if (arithmetic[3].isSelected()) {// ���ؾ���
//								ResourceAlloc.allocateResource(traList, 2);
//							}
//							if (arithmetic[0].isSelected()) {// �Զ���
//								ResourceAlloc.allocateResource(traList, 3);
//							}
//						}
//						
//						setVisible(false);
//						dispose();// �ͷŴ�����ռ�ڴ���Դ
//						new Dlg_DesignResult();
//					}
//				}
//
//				// if(arithmetic_new[1].isSelected())
//				// {
//				// if(arithmetic[1].isSelected()) {//��С��
//				// ResourceAlloc.allocateResource1(traList, 1);
//				// }
//				// if(arithmetic[2].isSelected()) {//���·��
//				// ResourceAlloc.allocateResource1(traList, 0);
//				// }
//				// }
//				// else
//				// {
//				// if(arithmetic[1].isSelected()) {//��С��
//				// ResourceAlloc.allocateResource(traList, 1);
//				// }
//				// if(arithmetic[2].isSelected()) {//���·��
//				// ResourceAlloc.allocateResource(traList, 0);
//				// }
//				// }
//				// setVisible(false);
//				// dispose();//�ͷŴ�����ռ�ڴ���Դ
//				// new Dlg_DesignResult();
				 }
			}
		});

		// trafficTeam.addItem("");
		trafficTeam.addItem("ȫ��");
		// for(int i = 0;i < TrafficGroupTrafficGroupList.size();i++){
		// trafficTeam.addItem(TrafficGroup.TrafficGroupList.get(i).getM_sName());
		// }
		trafficTeam.setSelectedIndex(0);
		String[] s1 = { "ȫ��", "FIBER", "WDM", "OTN", "SDH", "ASON" };
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
		p3.setBorder(BorderFactory.createTitledBorder("ѡ��·���㷨"));

		JPanel p4 = new JPanel();
		p4.setBorder(BorderFactory.createTitledBorder("��·�ɲ���"));

		JPanel p5 = new JPanel();
		p5.setBorder(BorderFactory.createTitledBorder("�Ƿ���OSNR"));
		ifSRLG[0] = new JCheckBox("��", true);
		ifSRLG[1] = new JCheckBox("��");
		ButtonGroup group4 = new ButtonGroup();
		group4.add(ifSRLG[0]);
		group4.add(ifSRLG[1]);

		JPanel p6 = new JPanel();
		p6.setBorder(BorderFactory.createTitledBorder("����ҵ����������"));

		JPanel p7 = new JPanel();
		p7.setBorder(BorderFactory.createTitledBorder("������������"));

		// limit.setHorizontalAlignment(JTextField.RIGHT);
		limit = new JTextField("200", 6);
		limit.setDocument(new NumberLenghtLimitedDmt(8));
		limit.setText("200");
		percent = new JTextField("100", 6);
		percent.setDocument(new NumberLenghtLimitedDmt(7));
		percent.setText("100");

		final JButton set = new JButton("�㷨����");
		final JButton setT = new JButton("��������");
		arithmetic[0] = new JRadioButton("�Զ���Ȩֵ��", true);
		arithmetic[1] = new JRadioButton("��С������");
		arithmetic[2] = new JRadioButton("���·����");
		arithmetic[3] = new JRadioButton("���ؾ��ⷨ");
		//arithmetic[4] = new JRadioButton("���ʱ�ӷ�");

		arithmetic_new[0] = new JRadioButton("Ԥ����·��", true);
		arithmetic_new[1] = new JRadioButton("��̬��·��");

		separate[0] = new JRadioButton("��·����", true);
		separate[1] = new JRadioButton("�ڵ����");
		
		separate1[0] = new JRadioButton("��������·�ɾ�����");
		separate1[1] = new JRadioButton("������·�ɷ���", true);

		ifOSNR[0] = new JRadioButton("��", true);
		ifOSNR[1] = new JRadioButton("��");

		ButtonGroup group1 = new ButtonGroup();
		group1.add(arithmetic[0]);
		group1.add(arithmetic[1]);
		group1.add(arithmetic[2]);
		group1.add(arithmetic[3]);
		//group1.add(arithmetic[4]);

		ifLimit[0] = new JRadioButton("��");
		ifLimit[1] = new JRadioButton("��", true);
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

		// p3��ѡ��·���㷨
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

		// p4����·��ѡ��
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

		// �Ƿ�osnr
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

		// ����ҵ����
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
		GridBagConstraints s= new GridBagConstraints();//����һ��GridBagConstraints��������������ӽ����������ʾλ��
		s.fill = GridBagConstraints.BOTH; //�÷�����Ϊ���������������ڵ�������������Ҫ��ʱ����ʾ���
		s.anchor = GridBagConstraints.WEST;
		s.gridwidth=1;//�÷������������ˮƽ��ռ�õĸ����������Ϊ0����˵��������Ǹ��е����һ��
        s.weightx = 0;//�÷����������ˮƽ��������ȣ����Ϊ0��˵�������죬��Ϊ0�����Ŵ�������������죬0��1֮��
        s.weighty=0;//�÷������������ֱ��������ȣ����Ϊ0��˵�������죬��Ϊ0�����Ŵ�������������죬0��1֮��
        insert = new Insets(0, 50, 0, 0);
        s.insets = insert;
        layout.setConstraints(separate[0], s);//�������
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

		// ��������
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

		s= new GridBagConstraints();//����һ��GridBagConstraints��������������ӽ����������ʾλ��
		s.fill = GridBagConstraints.BOTH; //�÷�����Ϊ���������������ڵ�������������Ҫ��ʱ����ʾ���
		s.anchor = GridBagConstraints.WEST;
		s.gridwidth=3;//�÷������������ˮƽ��ռ�õĸ����������Ϊ0����˵��������Ǹ��е����һ��
        s.weightx = 0;//�÷����������ˮƽ��������ȣ����Ϊ0��˵�������죬��Ϊ0�����Ŵ�������������죬0��1֮��
        s.weighty=0;//�÷������������ֱ��������ȣ����Ϊ0��˵�������죬��Ϊ0�����Ŵ�������������죬0��1֮��
        insert = new Insets(0, 10, 0, 0);
        s.insets = insert;
        
        layout.setConstraints(jl1, s);//�������

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
	private JLabel explication1 = new JLabel("1��A�����룬B��������C������");
	private JLabel explication2 = new JLabel("2����ϵ��ֵ����0��1֮�䣨����0��1�����Ҹ�ϵ��֮��Ϊ1��");
	private JLabel explication3 = new JLabel("3����ֵԽ�����ضԽ��Ӱ��Խ��");
	private double[] safeCoefficient = { 0.3, 0.3, 0.4, 0.3 };// ��ȫ�Ը�����Ӧ��ϵ��
	// private int[] areaCoefficient = new int[6]; //����ȫϵ��
	// private int[] fuctoryCoefficient = new int[3]; //���跽ʽ��ȫϵ��
	// private int[] MaintainComCoefficient = new int[5]; //ά����λ��ȫϵ��

	private DefaultTableModel regionModel;
	private DefaultTableModel perfunctoryModel;
	private DefaultTableModel timeModel;

	private JTable region;
	private JTable perfunctory;
	private JTable time;

	private final Object[] columnNames1 = { "����", "��ȫϵ��" };
	private final Object[] columnNames2 = { "���跽ʽ", "��ȫϵ��" };
	private final Object[] columnNames3 = { "ά����λ", "��ȫϵ��" };

	private Object[][] content1 = null;
	private Object[][] content2 = null;
	private Object[][] content3 = null;

	private JButton confirm = new JButton("ȷ��");
	private JButton cancel = new JButton("ȡ��");
	private JPanel mainpane = new JPanel();
	private JPanel pane1 = new JPanel();
	private JPanel pane2 = new JPanel();

	public SetZ(Frame owner) {
		super(owner, "�Զ���Ȩ������");
		// this.setTitle("�ۺϴ�����С");
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
		// ����������Model
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
							JOptionPane.showMessageDialog(null, "ֻ������1~9֮�������!");
							return;
						}
					} catch (Exception ex) {
						javax.swing.JOptionPane.showMessageDialog(null, "ֻ����������!");
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

		// �������跽ʽ���Model
		perfunctoryModel = new DefaultTableModel(content2, columnNames2);
		perfunctory = new JTable(perfunctoryModel) {
			public boolean isCellEditable(int row, int col) {
				if (col == 1) {
					return true;
				} else
					return false;
			}
		};
		perfunctory.getTableHeader().setReorderingAllowed(false);// �в��ɶ�
		sortManager(perfunctory);
		MyUtil.makeFace(perfunctory, new Color(255, 255, 224));
		JScrollPane scrollPane2 = new JScrollPane(perfunctory);
		this.setColumnWidth(perfunctory, 0, 100);
		this.setColumnWidth(perfunctory, 1, 80);

		// ����ά����λ���Model
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
							JOptionPane.showMessageDialog(null, "ֻ������1~9֮�������!");
							return;
						}
					} catch (Exception ex) {
						javax.swing.JOptionPane.showMessageDialog(null, "ֻ����������!");
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
		// ���ü��㹫ʽ���ֲ���
		A.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 15));
		B.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 15));
		C.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 15));
		D.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 15));
		// E.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 15));
		// F.setFont(new java.awt.Font("Times New Roman", Font.BOLD, 15));
		pane1.setBorder(BorderFactory.createTitledBorder("���㹫ʽ"));
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
		pane1.add(explication1, mgc); // ����˵��1
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		pane1.add(explication2, mgc);// ����˵��2
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		pane1.add(explication3, mgc);// ����˵��3

		// ����岼������
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
				// ������ϵ��δ��д����򵯳���ʾ
				if (aValue.getText().isEmpty() || bValue.getText().isEmpty() || cValue.getText().isEmpty()
						) {
					JOptionPane.showMessageDialog(null, "����дϵ����ϵ��Ϊ0-1������0��1");
				} else {
					safeCoefficient[0] = Double.valueOf(aValue.getText());
					safeCoefficient[1] = Double.valueOf(bValue.getText());
					safeCoefficient[2] = Double.valueOf(cValue.getText());

//					safeCoefficient[3] = Double.valueOf(dValue.getText());
					// safeCoefficient[4] = Double.valueOf(eValue.getText());
					// safeCoefficient[5] = Double.valueOf(fValue.getText());
					double safe = 0;
					for (int i = 0; i < 3; i++) {
						safe += safeCoefficient[i];// ����ϵ����
					}
					if (safe != 1.0) {
						JOptionPane.showMessageDialog(null, "��ϵ��֮�ͱ���Ϊ1!");
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
						dispose();// �ͷŴ�����ռ�ڴ���Դ
					}
				}
			}
		});
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();// �ͷŴ�����ռ�ڴ���Դ
			}
		});
		this.setSize(560, 230);
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setVisible(true);

	}

	/**
	 * ������������
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