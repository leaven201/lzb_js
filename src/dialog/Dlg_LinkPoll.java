package dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import algorithm.ResourceAlloc;
import twaver.Element;
import twaver.Node;
import twaver.TDataBox;
import twaver.TWaverConst;
import twaver.chart.PieChart;
import data.BasicLink;
import data.CommonNode;
import data.FiberLink;
import data.Traffic;
import database.NodeDataBase;
import database.TrafficDatabase;
import datastructure.UIFiberLink;
import enums.TrafficLevel;
import survivance.Evaluation;

public class Dlg_LinkPoll extends JFrame {
	private DefaultTableModel nodeModel;
	private DefaultTableModel trafficModel;
	private JTable nodeTable;
	private JTable trafficTable;
	private TDataBox box = new TDataBox();
	private PieChart pie = new PieChart(box);
	private final Object[] nodeHeader = { "ID", "链路名称", "首节点", "末节点" };
	private final String[] trafficHeader = { "ID", "受影响业务名称", "首节点 ", "末节点", "保护等级", "受影响程度", "原工作路由", "原保护路由",
			"故障后工作路由", "故障后保护路由" };// 列名最好用final修饰
	private List<Traffic> tra = new LinkedList<Traffic>();
	private FiberLink thelink = new FiberLink();
	private Element aa = new Node();
	private Element bb = new Node();
	private Element cc = new Node();
	private Element dd = new Node();
	private Frame owner1;

	public Dlg_LinkPoll(Frame owner) {
		setTitle("链路单断");
		owner1 = owner;
		ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/resource/titlexiao.png"));
		this.setIconImage(imageIcon.getImage());
		// super(owner,"链路单断循环",true);
		this.setSize(1000, 600);
		ImageIcon imageIcon1 = new ImageIcon(this.getClass().getResource("/resource/ddd1111.png"));
		this.setIconImage(imageIcon1.getImage());
		Toolkit tl = Toolkit.getDefaultToolkit();
		Dimension screenSize = tl.getScreenSize();
		this.setLocation((int) screenSize.getWidth() / 2 - (int) this.getSize().getWidth() / 2,
				(int) screenSize.getHeight() / 2 - (int) this.getSize().getHeight() / 2);

		trafficModel = new DefaultTableModel(null, trafficHeader);
		Object data[][] = new Object[FiberLink.fiberLinkList.size()][4];
		for (int n = 0; n < FiberLink.fiberLinkList.size(); n++) {
			data[n][0] = FiberLink.fiberLinkList.get(n).getId();
			data[n][1] = FiberLink.fiberLinkList.get(n).getName();
			data[n][2] = FiberLink.fiberLinkList.get(n).getFromNode().getName();
			data[n][3] = FiberLink.fiberLinkList.get(n).getToNode().getName();

		}

		nodeModel = new DefaultTableModel(data, nodeHeader);
		nodeTable = new JTable(nodeModel) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		trafficTable = new JTable(trafficModel) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		pie.setSize(40, 80);

		trafficTable.getTableHeader().setReorderingAllowed(false);
		nodeTable.getTableHeader().setReorderingAllowed(false); // 列不可动
		JScrollPane pane1 = new JScrollPane(nodeTable);
		JScrollPane pane2 = new JScrollPane(trafficTable);
		pane1.setRowHeaderView(new RowHeaderTable(nodeTable, 40));

		nodeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 水平滚动条
		trafficTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 水平滚动条

		TableColumnModel tcm = trafficTable.getColumnModel();
		tcm.getColumn(6).setPreferredWidth(200);
		tcm.getColumn(7).setPreferredWidth(200);
		tcm.getColumn(8).setPreferredWidth(200);
		tcm.getColumn(9).setPreferredWidth(200);
		// tcm.getColumn(10).setPreferredWidth(200);

		tcm = nodeTable.getColumnModel();
		tcm.getColumn(0).setPreferredWidth(50);//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
		tcm.getColumn(1).setPreferredWidth(110);//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM

		JButton close = new JButton(" 关  闭  ");
		JButton Browse = new JButton("单断循环");
		Font f = new Font("微软雅黑", Font.PLAIN, 12);
		close.setFont(f);
		Browse.setFont(f);
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				recoveryLinkcol(thelink);
				setVisible(false);
				dispose();// 释放窗体所占内存资源?
			}
		});
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				recoveryLinkcol(thelink);
				setVisible(false);
				dispose();
			}
		});
		Browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				 * 统计结果窗口
				 */
			//	Evaluation.listEvaluation(FiberLink.fiberLinkList, 0);
				new Dlg_SurvivanceResult(Dlg_LinkPoll.this, 1);
			}
		});
		nodeTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// System.out.println(BasicLink.allLinkList.get(11).getCarriedTraffic());调试专用
				// for (Traffic tra : Traffic.trafficList) {
				// System.out.println(tra.getWorkRoute());
				// System.out.println(tra.getProtectRoute());
				// System.out.println(tra.getResumeRoute());
				// System.out.println(tra.getResumeRoutePro()+"***");
				//
				// }
				int id = Integer.parseInt(String.valueOf(nodeModel.getValueAt(nodeTable.getSelectedRow(), 0)));
//				 if (thelink.getType() != null)
				recoveryLinkcol(thelink);// 10.21

				thelink = FiberLink.getFiberLink(id);
				for (int i = 0; i < trafficModel.getRowCount();) {
					trafficModel.removeRow(0);
				}
				setLinkcol(thelink);// 把饼图数值都置为0，并设置故障链路灰红闪烁 10.21

				tra = survivance.Evaluation.linkEvaluation(thelink);// 链路单断入口 CC
				if (tra == null || tra.isEmpty()) // 2017.4.7
					dd.putChartValue(1);// 新增判断 10.20
				else {
					aa.putChartValue(survivance.Evaluation.keepPercent); // 饼图数值
					// CC
					bb.putChartValue(survivance.Evaluation.downPercent);
					cc.putChartValue(survivance.Evaluation.cutoffPercent);
					dd.putChartValue(survivance.Evaluation.uneffPercent);

					Object data1[] = new Object[11];

					String from, to, link;

					for (int i = 0; i < tra.size(); i++) {

						data1[0] = tra.get(i).getId();
						data1[1] = tra.get(i).getFromNode().getName()+"-"+tra.get(i).getToNode().getName();
						data1[2] = tra.get(i).getFromNode().getName();
						data1[3] = tra.get(i).getToNode().getName();
						data1[4] = tra.get(i).getProtectLevel().toString();
						if (tra.get(i).getProtectLevel().equals(TrafficLevel.NORMAL11)) {
							data1[4] = "普通1+1";
						} else if (tra.get(i).getProtectLevel().equals(TrafficLevel.PERMANENT11)) {
							data1[4] = "永久1+1";
						} else if (tra.get(i).getProtectLevel().equals(TrafficLevel.RESTORATION)) {
							data1[4] = "恢复";
						} else if (tra.get(i).getProtectLevel().equals(TrafficLevel.PresetRESTORATION)) {
							data1[4] = "专享预置恢复";
						} else if (tra.get(i).getProtectLevel().equals(TrafficLevel.PROTECTandRESTORATION)) {
							data1[4] = "保护+恢复";
						} else if (tra.get(i).getProtectLevel().equals(TrafficLevel.NONPROTECT)) {
							data1[4] = "无保护";
						}
						StringBuffer buffer = new StringBuffer();
						StringBuffer buffer1 = new StringBuffer();
						StringBuffer buffer2 = new StringBuffer();
						StringBuffer buffer3 = new StringBuffer();

						from = tra.get(i).getFromNode().getName();
						for (int j = 0; j < tra.get(i).getWorkRoute().getWDMLinkList().size(); j++) {
							if (j == 0) {
								buffer.append(from);
							}
							link = tra.get(i).getWorkRoute().getWDMLinkList().get(j).getName();
							if (from.equals(tra.get(i).getWorkRoute().getWDMLinkList().get(j).getFromNode().getName()))
								to = tra.get(i).getWorkRoute().getWDMLinkList().get(j).getToNode().getName();
							else
								to = tra.get(i).getWorkRoute().getWDMLinkList().get(j).getFromNode().getName();
							// 添加链路类型提示
							buffer.append("--<" + link + ">--");
							buffer.append(to);
							from = to;
						}
						data1[6] = buffer.toString();

						from = tra.get(i).getFromNode().getName();
						if (tra.get(i).getProtectRoute() == null)
							buffer1.append("");
						else {
							for (int j = 0; j < tra.get(i).getProtectRoute().getWDMLinkList().size(); j++) {
								if (j == 0)
									buffer1.append(from);
								link = tra.get(i).getProtectRoute().getWDMLinkList().get(j).getName();
								if (from.equals(
										tra.get(i).getProtectRoute().getWDMLinkList().get(j).getFromNode().getName()))
									to = tra.get(i).getProtectRoute().getWDMLinkList().get(j).getToNode().getName();
								else
									to = tra.get(i).getProtectRoute().getWDMLinkList().get(j).getFromNode().getName();
								// 添加链路类型提示
								buffer1.append("--<" + link + ">--");
								buffer1.append(to);
								from = to;
							}
						}

						data1[7] = buffer1.toString();

						from = tra.get(i).getFromNode().getName();
						if (tra.get(i).getResumeRoute() != null) {

							for (int j = 0; j < tra.get(i).getResumeRoute().getWDMLinkList().size(); j++) {
								if (j == 0)
									buffer2.append(from);
								link = tra.get(i).getResumeRoute().getWDMLinkList().get(j).getName();
								if (from.equals(
										tra.get(i).getResumeRoute().getWDMLinkList().get(j).getFromNode().getName()))
									to = tra.get(i).getResumeRoute().getWDMLinkList().get(j).getToNode().getName();
								else
									to = tra.get(i).getResumeRoute().getWDMLinkList().get(j).getFromNode().getName();
								// 添加链路类型提示
								buffer2.append("--<" + link + ">--");
								buffer2.append(to);
								from = to;
							}
						}

						data1[8] = buffer2.toString();

						from = tra.get(i).getFromNode().getName();
						if (tra.get(i).getResumeRoutePro() != null) {

							for (int j = 0; j < tra.get(i).getResumeRoutePro().getWDMLinkList().size(); j++) {
								if (j == 0)
									buffer3.append(from);
								link = tra.get(i).getResumeRoutePro().getWDMLinkList().get(j).getName();
								if (from.equals(
										tra.get(i).getResumeRoutePro().getWDMLinkList().get(j).getFromNode().getName()))
									to = tra.get(i).getResumeRoutePro().getWDMLinkList().get(j).getToNode().getName();
								else
									to = tra.get(i).getResumeRoutePro().getWDMLinkList().get(j).getFromNode().getName();
								// 添加链路类型提示
								buffer3.append("--<" + link + ">--");
								buffer3.append(to);
								from = to;
							}
						}

						data1[9] = buffer3.toString();

						Traffic tra1 = tra.get(i);
						switch (tra1.getProtectLevel()) {
						case PERMANENT11:
							if (tra1.getResumeRoute() == null || tra1.getResumeRoute().getWDMLinkList().size() == 0) {
								data1[5] = "中断";

							} else if ((tra1.getResumeRoute() != null
									&& tra1.getResumeRoute().getWDMLinkList().size() != 0)
									&& (tra1.getResumeRoutePro() == null
											|| tra1.getResumeRoutePro().getWDMLinkList().size() == 0))
								data1[5] = "降级";
							else if ((tra1.getResumeRoute() != null
									&& tra1.getResumeRoute().getWDMLinkList().size() != 0)
									&& (tra1.getResumeRoutePro() != null
											&& tra1.getResumeRoutePro().getWDMLinkList().size() != 0))
								data1[5] = "保持";
							break;
						case NONPROTECT:
							if (tra1.getResumeRoute() == null || tra1.getResumeRoute().getWDMLinkList().size() == 0) {
								data1[5] = "中断";

							}
							break;
						case NORMAL11:
							if (tra1.getResumeRoute() == null || tra1.getResumeRoute().getWDMLinkList().size() == 0) {
								data1[5] = "中断";

							} else if ((tra1.getResumeRoute() != null
									&& tra1.getResumeRoute().getWDMLinkList().size() != 0)
									&& (tra1.getResumeRoutePro() == null
											|| tra1.getResumeRoutePro().getWDMLinkList().size() == 0))
								data1[5] = "降级";
							else if ((tra1.getResumeRoute() != null
									&& tra1.getResumeRoute().getWDMLinkList().size() != 0)
									&& (tra1.getResumeRoutePro() != null
											&& tra1.getResumeRoutePro().getWDMLinkList().size() != 0))
								data1[5] = "保持";
							break;
						case RESTORATION:
							if (tra1.getResumeRoute() == null || tra1.getResumeRoute().getWDMLinkList().size() == 0) {
								data1[5] = "中断";

							} else if (tra1.getResumeRoute() != null
									&& tra1.getResumeRoute().getWDMLinkList().size() != 0)
								data1[5] = "保持";
							break;
						case PresetRESTORATION:
							if (tra1.getResumeRoute() == null || tra1.getResumeRoute().getWDMLinkList().size() == 0) {
								data1[5] = "中断";
							} else if ((tra1.getResumeRoute() != null
									&& tra1.getResumeRoute().getWDMLinkList().size() != 0)
									&& (tra1.getResumeRoutePro() == null
											|| tra1.getResumeRoutePro().getWDMLinkList().size() == 0))
								data1[5] = "降级";
							else if ((tra1.getResumeRoute() != null
									&& tra1.getResumeRoute().getWDMLinkList().size() != 0)
									&& (tra1.getResumeRoutePro() != null
											&& tra1.getResumeRoutePro().getWDMLinkList().size() != 0))
								data1[5] = "保持";
							break;
						case PROTECTandRESTORATION:
							if (tra1.getResumeRoute() == null || tra1.getResumeRoute().getWDMLinkList().size() == 0) {
								data1[5] = "中断";
							} else if ((tra1.getResumeRoute() != null
									&& tra1.getResumeRoute().getWDMLinkList().size() != 0)
									&& (tra1.getResumeRoutePro() == null
											|| tra1.getResumeRoutePro().getWDMLinkList().size() == 0))
								data1[5] = "降级";
							else if ((tra1.getResumeRoute() != null
									&& tra1.getResumeRoute().getWDMLinkList().size() != 0)
									&& (tra1.getResumeRoutePro() != null
											&& tra1.getResumeRoutePro().getWDMLinkList().size() != 0))
								data1[5] = "保持";
							break;
						default:
							break;
						}

						trafficModel.addRow(data1);
					}
//					Evaluation.clearRsmRoute(tra);
				} // 10.20新增
			}
		});

		aa.setName("保持");
		bb.setName("降级");
		cc.setName("中断");
		dd.setName("无影响");

		pie.setVisible(true);

		// 图示付色
		aa.putChartColor(Color.green);
		bb.putChartColor(Color.blue);
		cc.putChartColor(Color.RED);
		dd.putChartColor(Color.yellow);

		// //图示的比例值
		// aa.putChartValue(0.25);
		// bb.putChartValue(0.25);
		// cc.putChartValue(0.25);

		box.addElement(aa);
		box.addElement(bb);
		box.addElement(cc);
		box.addElement(dd);

		JPanel piepanel = new JPanel(new BorderLayout());// 饼图
		piepanel.add(pie, BorderLayout.CENTER);

		JPanel p1 = new JPanel(new BorderLayout());
		// p1.setBorder(BorderFactory.createTitledBorder("链路列表"));
		p1.setBorder(BorderFactory.createTitledBorder(null, "链路列表", TitledBorder.LEFT, TitledBorder.TOP,
				new java.awt.Font("微软雅黑", Font.BOLD, 12)));
		p1.add(pane1, BorderLayout.CENTER);

		JPanel p2 = new JPanel(new BorderLayout());
		// p2.setBorder(BorderFactory.createTitledBorder("受影响业务列表"));
		p2.setBorder(BorderFactory.createTitledBorder(null, "受影响业务列表", TitledBorder.LEFT, TitledBorder.TOP,
				new java.awt.Font("微软雅黑", Font.BOLD, 12)));
		p2.add(pane2, BorderLayout.CENTER);

		JPanel p4 = new JPanel();
		GridBagLayout mg = new GridBagLayout();
		GridBagConstraints mgc = new GridBagConstraints();
		p4.setLayout(mg);
		mgc.fill = GridBagConstraints.NONE;

		mgc.gridwidth = GridBagConstraints.REMAINDER;
		mgc.weightx = 1.0;
		mgc.weighty = 1.0;
		mgc.gridheight = 1;
		p4.add(Browse, mgc);
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		p4.add(close, mgc);

		JPanel p3 = new JPanel();

		Insets insert;
		mg = new GridBagLayout();
		mgc = new GridBagConstraints();
		p3.setLayout(mg);
		mgc.fill = GridBagConstraints.BOTH;

		mgc.gridwidth = GridBagConstraints.REMAINDER;
		mgc.weightx = 1.0;
		mgc.weighty = 2.0;
		mgc.gridheight = 2;
		p3.add(p1, mgc);
		mgc.gridwidth = 1;
		mgc.weighty = 1.0;
		mgc.gridheight = 1;
		insert = new Insets(5, 40, 5, -25);
		mgc.insets = insert;
		p3.add(piepanel, mgc);
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		insert = new Insets(0, 0, 0, -35);
		mgc.insets = insert;
		p3.add(p4, mgc);

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, p3, p2);
		split.setOneTouchExpandable(true);
		split.setDividerLocation(380);

		setContentPane(split);
		this.setVisible(true);
	}

	/**
	 * 把bsclink根据类型设置不同的UILink效果，同时初始化饼图数值
	 * 
	 * @param bsclink
	 */
	private void setLinkcol(BasicLink bsclink) {// 设置故障链路灰红闪烁
		 assert (bsclink == null) : bsclink;
		UIFiberLink UIFiberLink = null;
		if (bsclink != null) {
			UIFiberLink = UIFiberLink.getUIFiberLink(bsclink.getName()); // wx
		}
		if (UIFiberLink != null) {
			UIFiberLink.putLinkColor(Color.GRAY.brighter());// 设置链路颜色灰色
			UIFiberLink.putLinkBlinking(true);// 设置闪烁
			UIFiberLink.putLinkBlinkingColor(Color.RED);// 红色闪烁
			UIFiberLink.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
			UIFiberLink.putLink3D(true);
		}
		aa.putChartValue(0); // 饼图数值 CC 没有数据时饼图设置为1
		bb.putChartValue(0);
		cc.putChartValue(0);
		dd.putChartValue(0);
	}

	private void recoveryLinkcol(BasicLink bsclink) {
		UIFiberLink UIFiberLink = null;
		if (bsclink != null) {// 10.25
			 UIFiberLink = UIFiberLink.getUIFiberLink(bsclink.getName());
			if (UIFiberLink != null) {
				UIFiberLink.setthisColor(Color.blue);
				UIFiberLink.putLinkBlinking(false);
				UIFiberLink.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
				UIFiberLink.putLink3D(false);
			}
		}

	}

	// 测试函数功能
	public static void main(String arge[]) {
		Frame owner1 = new Frame();
		NodeDataBase dbs = new NodeDataBase();
		dbs.inputNode("测试案例.xls");
		System.out.println(CommonNode.allNodeList);

		// LinkDataBase dbsl = new LinkDataBase();
		// dbsl.inputFiberLink("测试案例.xls");
		// System.out.println("wdm" + WDMLink.allWDMLinkList);
		// System.out.println("fiber" + FiberLink.allFiberLinkList);

		TrafficDatabase dbst = new TrafficDatabase();
		dbst.inputTraffic("测试案例.xls");
		System.out.println(Traffic.trafficList);
		ResourceAlloc.allocateResource(Traffic.trafficList, 0);

		for (Traffic tra : Traffic.trafficList) {
			System.out.println(tra.getWorkRoute());
			System.out.println(tra.getProtectRoute());
		}
		// new evaluate.NodeUtilization(CommonNode.allNodeList);
		// System.out.println(NodeUtilization.nodeUtiList);
		new Dlg_LinkPoll(owner1);

	}
}
