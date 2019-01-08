package dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import algorithm.ResourceAlloc;
import twaver.Element;
import twaver.Node;
import twaver.TDataBox;
import twaver.TWaverConst;
import twaver.chart.PieChart;
import data.CommonNode;
import data.FiberLink;
import data.Traffic;
//import dataBase.NodeDataBase;
//import dataBase.TrafficDataBase;
import datastructure.UIFiberLink;
import enums.TrafficLevel;
import survivance.Evaluation;


public class Dlg_SetLinkPoll extends JFrame {
	private DefaultTableModel linkModel;
	private DefaultTableModel trafficModel;
	private JTable linkTable;
	private JTable trafficTable;
	private TDataBox box = new TDataBox();
	private PieChart pie = new PieChart(box);
	private final Object[] linkHeader = { "", "ID", "链路名称", "首节点", "末节点" };
	private final String[] trafficHeader = { "ID", "受影响业务名称", "首节点 ", "末节点", "保护等级", "受影响程度", "原工作路由", "原保护路由",
			"故障后工作路由", "故障后保护路由" };// 列名最好用final修饰
	private List<Traffic> tra = new LinkedList<Traffic>();
	private List<FiberLink> thelink = new LinkedList<FiberLink>();
	private Element aa = new Node();
	private Element bb = new Node();
	private Element cc = new Node();
	private Element dd = new Node();

	public Dlg_SetLinkPoll(Frame owner) {
		setTitle("链路故障设置");
		ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/resource/titlexiao.png"));
		this.setIconImage(imageIcon.getImage());
		// super(owner,"链路双断循环",true);
		this.setSize(1000, 600);
		Toolkit tl = Toolkit.getDefaultToolkit();
		Dimension screenSize = tl.getScreenSize();
		this.setLocation((int) screenSize.getWidth() / 2 - (int) this.getSize().getWidth() / 2,
				(int) screenSize.getHeight() / 2 - (int) this.getSize().getHeight() / 2);

		trafficModel = new DefaultTableModel(null, trafficHeader);

		linkModel = new DefaultTableModel(linkHeader, 0);
		// linkModel.addRow(new Object[] {new
		// Boolean(false),"fox","12","050101","男"});
		Object data[] = new Object[5];
		for (int n = 0; n < FiberLink.fiberLinkList.size(); n++) {
			data[0] = new Boolean(false);
			data[1] = FiberLink.fiberLinkList.get(n).getId();
			data[2] = FiberLink.fiberLinkList.get(n).getName();
			data[3] = FiberLink.fiberLinkList.get(n).getFromNode().getName();
			data[4] = FiberLink.fiberLinkList.get(n).getToNode().getName();
			linkModel.addRow(data);
		}
		linkTable = new JTable(linkModel) {
			public boolean isCellEditable(int row, int column) {
				if (column == 0) {
					return true;
				} else {
					return false;
				}

			}
		};

		trafficTable = new JTable(trafficModel) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		pie.setSize(40, 80);

		trafficTable.getTableHeader().setReorderingAllowed(false);
		linkTable.getTableHeader().setReorderingAllowed(false); // 列不可动
		JScrollPane pane1 = new JScrollPane(linkTable);
		JScrollPane pane2 = new JScrollPane(trafficTable);
		pane1.setRowHeaderView(new RowHeaderTable(linkTable, 40));

		linkTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 水平滚动条
		trafficTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 水平滚动条

		TableColumnModel tcm = trafficTable.getColumnModel();
		tcm.getColumn(7).setPreferredWidth(200);
		tcm.getColumn(8).setPreferredWidth(200);
		tcm.getColumn(9).setPreferredWidth(200);
		tcm.getColumn(6).setPreferredWidth(200);

		tcm = linkTable.getColumnModel();

		tcm.getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));
		tcm.getColumn(0).setCellRenderer(new MyTableRenderer());

		tcm.getColumn(0).setPreferredWidth(20);
		tcm.getColumn(0).setWidth(20);
		tcm.getColumn(0).setMaxWidth(20);
		tcm.getColumn(1).setPreferredWidth(50);
		tcm.getColumn(2).setPreferredWidth(90);

		JButton close = new JButton(" 关  闭  ");
//		JButton Browse = new JButton("统计结果");
		JButton simulation = new JButton(" 仿  真  ");
		Font f = new Font("微软雅黑", Font.PLAIN, 12);
		close.setFont(f);
//		Browse.setFont(f);
		simulation.setFont(f);
		simulation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int num = 0;

				recoveryLinkListcol(thelink);

				thelink = new LinkedList<FiberLink>();
				for (int i = 0; i < linkTable.getRowCount(); i++) {

					if (linkTable.getValueAt(i, 0).toString().equals("true")) {
						++num;
						thelink.add(FiberLink.getFiberLink(Integer.parseInt(String.valueOf(linkTable.getValueAt(i, 1)))));
						if (num == 3)
							break;
					}

				}
//				if (num == 2) {MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
//					// 设置故障链路 thelink
//
					tra = Evaluation.multiLinkEva(thelink, 0);

					setLinkListcol(thelink);

					aa.putChartValue(Evaluation.multiLinkKeepPer);
					bb.putChartValue(Evaluation.multiLinkDownPer);
					cc.putChartValue(Evaluation.multiLinkCutoffPer);
					dd.putChartValue(Evaluation.multiLinkUneffPer);

					showEffectTraffic(tra);
					Evaluation.clearRsmRoute(tra);
//
//				} else {
//					thelink = new LinkedList<FiberLink>();
//					JOptionPane.showMessageDialog(null, "请设置两条故障链路！");
//				}MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
			}
		});
		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				recoveryLinkListcol(thelink);
				setVisible(false);
				dispose();// 释放窗体所占内存资源?
			}
		});
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				recoveryLinkListcol(thelink);
				setVisible(false);
				dispose();
			}
		});
//		Browse.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				/*
//				 * 统计结果窗口
//				 */
//				Evaluation.dLinkListEvaluation(FiberLink.allFiberLinkList, 0);
//				new Dlg_SurvivanceResult_ls();
//			}
//		});

		aa.setName("保持");
		bb.setName("降级");
		cc.setName("中断");
		dd.setName("无影响");

		// 设置背景色加深
		// barChart.setBackgroundVisible(true);
		pie.setBackgroundVisible(true);

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

		JPanel piepanel = new JPanel(new BorderLayout());
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
		p4.add(simulation, mgc);
		mgc.gridwidth = GridBagConstraints.REMAINDER;
//		p4.add(Browse, mgc);
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
		insert = new Insets(5, 30, 5, -15);
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
		ImageIcon imageIcon1 = new ImageIcon(this.getClass().getResource("/resource/ddd1111.png"));
		this.setIconImage(imageIcon1.getImage());
	}

	public void showEffectTraffic(List<Traffic> tra) {

		for (int i = 0; i < trafficModel.getRowCount();) {
			trafficModel.removeRow(0);
		}
		if (tra == null || tra.isEmpty()) {// 2017.4.7
			aa.putChartValue(0);
			bb.putChartValue(0);
			cc.putChartValue(0);
			dd.putChartValue(1);
			return; // 11.11
		}
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
//			} else if (tra.get(i).getProtectLevel().equals(TrafficLevel.PresetRESTORATION)) {
//				data1[4] = "专享预置恢复";
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
					if (from.equals(tra.get(i).getProtectRoute().getWDMLinkList().get(j).getFromNode().getName()))
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
					if (from.equals(tra.get(i).getResumeRoute().getWDMLinkList().get(j).getFromNode().getName()))
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
					if (from.equals(tra.get(i).getResumeRoutePro().getWDMLinkList().get(j).getFromNode().getName()))
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
					data1[5] = "保持";
				else if ((tra1.getResumeRoute() != null
						&& tra1.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra1.getResumeRoutePro() != null
								&& tra1.getResumeRoutePro().getWDMLinkList().size() != 0))
					data1[5] = "保持";
				break;
			case RESTORATION:
				if (tra1.getResumeRoute() == null || tra1.getResumeRoute().getWDMLinkList().size() == 0) {
					data1[5] = "中断";

				} else  if ((tra1.getResumeRoute() != null && tra1.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra1.getPreRoute() == null || tra1.getPreRoute().getWDMLinkList().size() == 0)) {
					data1[5] = "保持";
				} else if ((tra1.getResumeRoute() != null && tra1.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra1.getPreRoute() != null && tra1.getPreRoute().getWDMLinkList().size() != 0)) {
					data1[5] = "保持";
				}
				break;
//			case PresetRESTORATION:
//				if (tra1.getResumeRoute() == null || tra1.getResumeRoute().getWDMLinkList().size() == 0) {
//					data1[5] = "中断";
//				} else if ((tra1.getResumeRoute() != null
//						&& tra1.getResumeRoute().getWDMLinkList().size() != 0)
//						&& (tra1.getPreRoute() == null
//								|| tra1.getPreRoute().getWDMLinkList().size() == 0))
//					data1[5] = "保持";
//				else if ((tra1.getResumeRoute() != null
//						&& tra1.getResumeRoute().getWDMLinkList().size() != 0)
//						&& (tra1.getPreRoute() != null
//								&& tra1.getPreRoute().getWDMLinkList().size() != 0))
//					data1[5] = "保持";
//				break;
			case PROTECTandRESTORATION:
				if (tra1.getResumeRoute() == null || tra1.getResumeRoute().getWDMLinkList().size() == 0) {
					data1[5] = "中断";
				} else if ((tra1.getResumeRoute() != null
						&& tra1.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra1.getPreRoute() == null
								|| tra1.getPreRoute().getWDMLinkList().size() == 0)) {
					data1[5] = "保持";
				}else if ((tra1.getResumeRoute() != null
						&& tra1.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra1.getPreRoute() != null
								&& tra1.getPreRoute().getWDMLinkList().size() != 0)) {
					data1[5] = "保持";
				}
				break;
			default:
				break;
			}

			trafficModel.addRow(data1);

		}
	}

	private void setLinkListcol(List<FiberLink> bsclinklist) {
		for (FiberLink bscLink : bsclinklist) {
			// assert (bsclink == null) : bsclink;
			UIFiberLink UIfiberlink = null;
			if (bscLink != null) {
				UIfiberlink = UIFiberLink.getUIFiberLink(bscLink.getName()); // wx
			}
			if (UIfiberlink != null) {
				UIfiberlink.putLinkColor(Color.GRAY.brighter());// 设置链路颜色灰色
				UIfiberlink.putLinkBlinking(true);// 设置闪烁
				UIfiberlink.putLinkBlinkingColor(Color.RED);// 红色闪烁
				UIfiberlink.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
				UIfiberlink.putLink3D(true);
			} // end for
		}

	}

	private void recoveryLinkListcol(List<FiberLink> bsclinklist) {
		for (FiberLink bscLink : bsclinklist) {
			if (bscLink != null) {// 10.25
				UIFiberLink UIfiberlink = UIFiberLink.getUIFiberLink(bscLink.getName());
				if (UIfiberlink != null) {
					UIfiberlink.setthisColor(Color.orange);
					UIfiberlink.putLinkBlinking(false);
					UIfiberlink.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
					UIfiberlink.putLink3D(false);
				}
			}
		} // end for

	}
	// public static void main(String[] args){
	// new Dlg_DoubleLinkPoll();
	// }

//	public static void main(String[] args) {
//		Frame owner1 = new Frame();
//		NodeDataBase dbs = new NodeDataBase();
//		dbs.inputNode("测试案例.xls");
//		System.out.println(CommonNode.allNodeList);
//
//		// LinkDataBase dbsl = new LinkDataBase();
//		// dbsl.inputFiberLink("测试案例.xls");
//		// System.out.println("wdm" + WDMLink.allWDMLinkList);
//		// System.out.println("fiber" + FiberLink.allFiberLinkList);
//
//		TrafficDataBase dbst = new TrafficDataBase();
//		dbst.inputTraffic("测试案例.xls");
//		System.out.println(Traffic.trafficList);
//		ResourceAlloc.allocateResource(Traffic.trafficList, 0);
//
//		for (Traffic tra : Traffic.trafficList) {
//			System.out.println(tra.getWorkRoute());
//			System.out.println(tra.getProtectRoute());
//		}
//		new Dlg_DoubleLinkPoll(owner1);
//	}
}

class MyTableRenderer extends JCheckBox implements TableCellRenderer {
	// 此方法可以查考JDK文档的说明
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column) {
		Boolean b = (Boolean) value;
		this.setSelected(b.booleanValue());
		return this;
	}

}