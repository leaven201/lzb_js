package dialog;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import data.FiberLink;

public class Dlg_fiberresult extends JFrame {
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		Dlg_fiberresult d1 = new Dlg_fiberresult();
	}

	private final String[] columnNames = { "ID", "名称", "首节点", "末节点", "光纤类型", "是否激活", "输入光功率Pin(dBm)",
			"链路OSNR(dB)", "共享风险链路", "跨段损耗(dB)", "光纤阶段" };
	private Object[][] content = null;
	private DefaultTableModel trafficInfoModel;
	private JTable trafficInfoTable;
	private ImageIcon title = new ImageIcon(getClass().getResource("/resource/titleresult.jpg"));
	private JTextArea resultArea;
	private JScrollPane jspane;
	private JButton close = new JButton("确定");
	private StringBuffer result;

	public Dlg_fiberresult() {
		setTitle("Fiber管理");
		this.setSize(800, 500);
		Font f = new Font("微软雅黑", Font.PLAIN, 12);
		close.setFont(f);
		// refreshTable(LinkID, LinkLayer);
		// 初始空表
		int size;
		size = FiberLink.fiberLinkList.size();
		if (size > 0) {
			content = new Object[size][columnNames.length];
		} else {
			content = new Object[0][columnNames.length];
		}
		// 向表中写入数据
		Iterator<FiberLink> itor = FiberLink.fiberLinkList.iterator();
		int i = 0;
		while (itor.hasNext()) {
			FiberLink wlink = itor.next();
			content[i][0] = wlink.getId();
			content[i][1] = wlink.getName();
			content[i][2] = wlink.getFromNode().getName();
			content[i][3] = wlink.getToNode().getName();
			content[i][4] = wlink.getLinkType();
//			content[i][5] = wlink.getWaveNum();
			if (wlink.isActive() == false)
				content[i][5] = "否";
			else
				content[i][5] = "是";
			content[i][6] = wlink.getInPower();
			content[i][7] = wlink.getOSNRCount();
			content[i][8] = wlink.getSRLG();
			content[i][9] = wlink.getFiberStage();
			i++;
		}

		trafficInfoModel = new DefaultTableModel(content, columnNames);
		trafficInfoTable = new JTable(trafficInfoModel) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};// 设置表内容不可编辑
		trafficInfoTable.getTableHeader().setReorderingAllowed(false);// 列不可动
		sortManager(trafficInfoTable);

		JScrollPane pane1 = new JScrollPane(trafficInfoTable);
		JLabel titlepane = new JLabel(title);
		JPanel p = new JPanel();
		JPanel mainPanel = new JPanel();
		JPanel buttonPanel = new JPanel();

		trafficInfoTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 水平滚动条

		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		// 设置列宽
		this.setColumnWidth(trafficInfoTable, 0, 100);
		this.setColumnWidth(trafficInfoTable, 1, 100);
		this.setColumnWidth(trafficInfoTable, 2, 100);
		this.setColumnWidth(trafficInfoTable, 3, 100);
		this.setColumnWidth(trafficInfoTable, 4, 150);
		this.setColumnWidth(trafficInfoTable, 5, 100);
		this.setColumnWidth(trafficInfoTable, 6, 100);

		pane1.setViewportView(trafficInfoTable);
		pane1.setRowHeaderView(new RowHeaderTable(trafficInfoTable, 40));
		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 5));
		buttonPanel.add(close);

		this.setContentPane(mainPanel);
		BorderLayout mainPaneLayout = new BorderLayout();
		setLayout(mainPaneLayout);
		mainPanel.add(titlepane, BorderLayout.NORTH);
		mainPanel.add(pane1, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
		ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/resource/ddd1111.png"));
		this.setIconImage(imageIcon.getImage());
	}

	private void setColumnWidth(JTable trafTable, int columnIndex, int preferredWidth) {
		TableColumnModel tcm = trafTable.getColumnModel();
		TableColumn tc = tcm.getColumn(columnIndex);
		tc.setPreferredWidth(preferredWidth);
	}

	private void sortManager(JTable table) {
		RowSorter sorter = new TableRowSorter(table.getModel());
		table.setRowSorter(sorter);
	}
}
