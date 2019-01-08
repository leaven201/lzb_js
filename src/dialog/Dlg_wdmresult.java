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

import data.WDMLink;
import enums.PortRate;

public class Dlg_wdmresult extends JFrame {
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		Dlg_wdmresult d1 = new Dlg_wdmresult();
	}

	private final String[] columnNames = { "ID", "名称", "首节点", "末节点", "波长带宽(bit/s)", "波道上限", "是否激活", "输入光功率Pin(dBm)",
			"中继数目", "WDM链路长度", "是否有灵活栅格", "剩余波道数", "WDM路由", "承载业务"};
	private Object[][] content = null;
	private DefaultTableModel trafficInfoModel;
	private JTable trafficInfoTable;
	private ImageIcon title = new ImageIcon(getClass().getResource("/resource/titleresult.jpg"));
	private JTextArea resultArea;
	private JScrollPane jspane;
	private JButton close = new JButton("确定");
	private StringBuffer result;

	public Dlg_wdmresult() {
		setTitle("OTN管理");
		this.setSize(800, 500);
		Font f = new Font("微软雅黑", Font.PLAIN, 12);
		close.setFont(f);
		// refreshTable(LinkID, LinkLayer);
		// 初始空表
		int size;
		size = WDMLink.WDMLinkList.size();
		if (size > 0) {
			content = new Object[size][columnNames.length];
		} else {
			content = new Object[0][columnNames.length];
		}
		// 向表中写入数据
		Iterator<WDMLink> itor = WDMLink.WDMLinkList.iterator();
		int i = 0;
		while (itor.hasNext()) {
			WDMLink wlink = itor.next();
			content[i][0] = wlink.getId();
			content[i][1] = wlink.getName();
			content[i][2] = wlink.getFromNode().getName();
			content[i][3] = wlink.getToNode().getName();
			content[i][4] = PortRate.Rate2Num(wlink.getRate());
			content[i][5] = wlink.getWaveNum();
			if (wlink.isActive() == false)
				content[i][6] = "否";
			else
				content[i][6] = "是";
			content[i][7] = wlink.getInPower();
			content[i][8] = wlink.getZhongjiNum();
			content[i][9] = wlink.getLength();
			if (wlink.isFlexibleRaster() == false)
				content[i][10] = "否";
			else
				content[i][10] = "是";
			content[i][11] = wlink.getRemainResource();
			content[i][12] = wlink.getFiberLinkList();
			content[i][13] = wlink.getCarriedTrafficList();
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
		this.setColumnWidth(trafficInfoTable, 12, 400);

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
