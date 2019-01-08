package dialog;

/**
 * 功能：展示路由规划结果，由Dlg_PolicySetting调用，生成路由的名称，格式为节点--<链路>--节点
 * @author CC
 * @since 2016.7.26
 * 
 */
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

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

import algorithm.OSNR;
import data.Traffic;

public class Dlg_DesignResult extends JFrame {
	private final String[] columnNames = { "序号", "首节点", "末节点", "工作路由", "保护路由","预置路由", "业务速率", "保护级别" };
	private Object[][] content = null;
	private DefaultTableModel trafficInfoModel;
	private JTable trafficInfoTable;
	private ImageIcon title = new ImageIcon(getClass().getResource("/resource/titleresult.jpg"));
	private JTextArea resultArea;
	private JScrollPane jspane;
	private JButton close = new JButton("确定");
	private StringBuffer result;

	public Dlg_DesignResult() {
		setTitle("业务路由");
		this.setSize(1220, 700);
		Font f = new Font("微软雅黑", Font.PLAIN, 12);
		close.setFont(f);
		// refreshTable(LinkID, LinkLayer);
		// 初始空表
		int traSize;
		traSize = Traffic.trafficList.size();
		if (traSize > 0) {
			content = new Object[traSize][columnNames.length];
		} else {
			content = new Object[0][columnNames.length];
		}
		// 分层向业务表中写入数据
		insertFiberTraffic(content);
		for (int i = 0; i < traSize; i++) {
			if (content[i][7].toString().equals("PERMANENT11"))
				content[i][7] = "  永久1+1";
			else if (content[i][7].toString().equals("NORMAL11"))
				content[i][7] = "  1+1";
			else if (content[i][7].toString().equals("RESTORATION"))
				content[i][7] = "  重路由";
			else if (content[i][7].toString().equals("NONPROTECT"))
				content[i][7] = "  无保护";
			else if (content[i][7].toString().equals("PROTECTandRESTORATION"))
				content[i][7] = "  1+1+重路由";
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
		this.setColumnWidth(trafficInfoTable, 0, 50);
		this.setColumnWidth(trafficInfoTable, 1, 100);
		this.setColumnWidth(trafficInfoTable, 2, 100);
		this.setColumnWidth(trafficInfoTable, 3, 220);
		this.setColumnWidth(trafficInfoTable, 4, 220);
		this.setColumnWidth(trafficInfoTable, 5, 220);
		this.setColumnWidth(trafficInfoTable, 6, 120);
		this.setColumnWidth(trafficInfoTable, 7, 120);

		resultArea = new JTextArea();
		resultArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		jspane = new JScrollPane(resultArea);
		result = new StringBuffer("规划结果信息：\n");
		result = ReadPhone.ReadData(result);
		int height = 2;
		double weight = 2.0;
		// if(!result.toString().contains("业务")&&
		// !result.toString().contains("链路")&&
		// !result.toString().contains("端口")){//如果资源分配日志里不包含这几个词说明资源分配成功，CC
		if (!result.toString().contains("失败")) {
			this.setSize(800, 600);
			height = 20;
			weight = 20.0;
			resultArea.setFont(new Font("微软雅黑", Font.PLAIN, 16));
			try {
				result = new StringBuffer(' ' + "业务资源分配均已成功！");
				resultArea.setRows(1);
			} catch (Exception e) {
			}
			;
		} // 如果包含那result里就是失败的信息
		resultArea.setText(result.toString());
		resultArea.setEditable(false);
		resultArea.setBackground(pane1.getBackground());

		pane1.setViewportView(trafficInfoTable);
		pane1.setRowHeaderView(new RowHeaderTable(trafficInfoTable, 40));
		Insets insert;
		GridBagLayout mg = new GridBagLayout();
		GridBagConstraints mgc = new GridBagConstraints();
		p.setLayout(mg);

		mgc.gridwidth = GridBagConstraints.REMAINDER;
		mgc.weightx = 1.0;
		mgc.weighty = weight;
		mgc.gridheight = height;
		mgc.fill = GridBagConstraints.BOTH;
		mgc.anchor = GridBagConstraints.CENTER;
		insert = new Insets(0, 1, 5, 10);
		mgc.insets = insert;
		p.add(pane1, mgc);
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		mgc.weightx = 1.0;
		mgc.gridheight = 1;
		mgc.weighty = 1.0;
		mgc.anchor = GridBagConstraints.CENTER;
		mgc.fill = GridBagConstraints.BOTH;
		insert = new Insets(15, 15, 10, 15);
		mgc.insets = insert;
		p.add(jspane, mgc);

		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 5));
		buttonPanel.add(close);

		this.setContentPane(mainPanel);
		BorderLayout mainPaneLayout = new BorderLayout();
		setLayout(mainPaneLayout);
		mainPanel.add(titlepane, BorderLayout.NORTH);
		mainPanel.add(p, BorderLayout.CENTER);
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
	
	private void insertFiberTraffic(Object[][] content) {
		for (int i = 0; i < Traffic.trafficList.size(); i++) {
			Traffic theTraffic = Traffic.trafficList.get(i);
			OSNR osnr = new OSNR();
			content[i][0] = "    "+theTraffic.getRankId();
//			content[i][1] = theTraffic.getName();// 业务名称
			content[i][1] = "       "+theTraffic.getFromNode().getName();// 头结点，node的唯一编号标示
			content[i][2] = "       "+theTraffic.getToNode().getName();// 尾结点，node的唯一编号标示
			DecimalFormat df = new DecimalFormat("0.0000 ");

//			if (theTraffic.getWorkRoute() == null)
//				content[i][5] = null;
//			else
//				content[i][5] = df.format(osnr.calculateOSNR(theTraffic.getWorkRoute()));// 工作路由的osnr
//			if (theTraffic.getProtectRoute() == null)
//				content[i][] = null;
//			else
//				content[i][7] = df.format(osnr.calculateOSNR(theTraffic.getProtectRoute()));// 保护路由的osnr

			content[i][5] = theTraffic.getNrate();// 业务速率
			content[i][7] = theTraffic.getProtectLevel();// 业务保护等级

			StringBuffer bufferw = new StringBuffer();// 工作路由
			if (theTraffic.getWorkRoute() != null && null != theTraffic.getWorkRoute().getNodeList()) {
				for (int n = 0; n < theTraffic.getWorkRoute().getNodeList().size(); n++) {
					bufferw.append(theTraffic.getWorkRoute().getNodeList().get(n).getName());
					if (n != theTraffic.getWorkRoute().getNodeList().size() - 1) {
						// switch
						// (theTraffic.getWorkRoute().getLinkList().get(n).getType())
						// {// 2017.3.5
						// // CC
						// // 添加链路类型提示
						// case Fiber:
						// bufferw.append("--<" +
						// theTraffic.getWorkRoute().getLinkList().get(n).getName()
						// + ">--");
						// break;
						// case Satellite:
						// bufferw.append(
						// "--<" +
						// theTraffic.getWorkRoute().getLinkList().get(n).getName()
						// + "(卫)" + ">--");
						// break;
						// case ShortWave:
						// bufferw.append(
						// "--<" +
						// theTraffic.getWorkRoute().getLinkList().get(n).getName()
						// + "(短)" + ">--");
						// break;
						// default:
						// break;
						// }
						bufferw.append("--");
						bufferw.append("--<" + theTraffic.getWorkRoute().getWDMLinkList().get(n).getEnglishname() + ">--");
					}
				}
				bufferw.append("   波道："+theTraffic.getWorkRoute().toStingwave());
			}
			content[i][3] = "   "+bufferw;

			StringBuffer bufferp = new StringBuffer();// 保护路由
			if (null != theTraffic.getProtectRoute() && null != theTraffic.getProtectRoute().getNodeList()) {
				for (int n = 0; n < theTraffic.getProtectRoute().getNodeList().size(); n++) {
					bufferp.append(theTraffic.getProtectRoute().getNodeList().get(n).getName());
					if (n != theTraffic.getProtectRoute().getNodeList().size() - 1) {
						// switch
						// (theTraffic.getProtectRoute().getWDMLinkList().get(n).getType())
						// {// 2017.3.5
						// // CC
						// // 添加链路类型提示
						// case Fiber:
						// bufferp.append("--<" +
						// theTraffic.getProtectRoute().getWDMLinkList().get(n).getName()
						// + ">--");
						// break;
						// case Satellite:
						// bufferp.append("--<" +
						// theTraffic.getProtectRoute().getWDMLinkList().get(n).getName()
						// + "(卫)"
						// + ">--");
						// break;
						// case ShortWave:
						// bufferp.append("--<" +
						// theTraffic.getProtectRoute().getWDMLinkList().get(n).getName()
						// + "(短)"
						// + ">--");
						// break;
						// default:
						// break;
						// }
//						bufferp.append("--<" + theTraffic.getProtectRoute().getWDMLinkList().get(n).getName() + ">--");
						bufferp.append("--");
					}
				}
			}
			content[i][4] = "   "+bufferp;
			
			StringBuffer bufferpre = new StringBuffer();// 预置路由
			if (theTraffic.getPreRoute() != null && null != theTraffic.getPreRoute().getNodeList()) {
				for (int n = 0; n < theTraffic.getPreRoute().getNodeList().size(); n++) {
					bufferpre.append(theTraffic.getPreRoute().getNodeList().get(n).getName());
					if (n != theTraffic.getPreRoute().getNodeList().size() - 1) {
						bufferpre.append("--");
						bufferpre.append("--<" + theTraffic.getPreRoute().getWDMLinkList().get(n).getEnglishname() + ">--");

					}
				}
				bufferpre.append("   波道："+theTraffic.getPreRoute().toStingwave());
			}
			content[i][5] = "   "+bufferpre;
		}
	}

//	private void insertFiberTraffic(Object[][] content) {
//		for (int i = 0; i < Traffic.trafficList.size(); i++) {
//			Traffic theTraffic = Traffic.trafficList.get(i);
//			content[i][0] = theTraffic.getId();
//			content[i][1] = theTraffic.getName();// 业务名称
//			content[i][2] = theTraffic.getFromNode().getName();// 头结点，node的唯一编号标示
//			content[i][3] = theTraffic.getToNode().getName();// 尾结点，node的唯一编号标示
//			content[i][6] = theTraffic.getNrate();// 业务速率
//			content[i][7] = theTraffic.getProtectLevel();// 业务保护等级
//
//			StringBuffer bufferw = new StringBuffer();// 工作路由
//			if (theTraffic.getWorkRoute() != null && null != theTraffic.getWorkRoute().getNodeList()) {
//				for (int n = 0; n < theTraffic.getWorkRoute().getNodeList().size(); n++) {
//					bufferw.append(theTraffic.getWorkRoute().getNodeList().get(n).getName());
//					if (n != theTraffic.getWorkRoute().getNodeList().size() - 1) {
//						switch (theTraffic.getWorkRoute().getLinkList().get(n).getType()) {// 2017.3.5
//						// CC
//						// 添加链路类型提示
//						case Fiber:
//							bufferw.append("--<" + theTraffic.getWorkRoute().getLinkList().get(n).getName() + ">--");
//							break;
//						case Satellite:
//							bufferw.append(
//									"--<" + theTraffic.getWorkRoute().getLinkList().get(n).getName() + "(卫)" + ">--");
//							break;
//						case ShortWave:
//							bufferw.append(
//									"--<" + theTraffic.getWorkRoute().getLinkList().get(n).getName() + "(短)" + ">--");
//							break;
//						default:
//							break;
//						}
//					}
//				}
//			}
//			content[i][4] = bufferw;
//
//			StringBuffer bufferp = new StringBuffer();// 保护路由
//			if (null != theTraffic.getProtectRoute() && null != theTraffic.getProtectRoute().getNodeList()) {
//				for (int n = 0; n < theTraffic.getProtectRoute().getNodeList().size(); n++) {
//					bufferp.append(theTraffic.getProtectRoute().getNodeList().get(n).getName());
//					if (n != theTraffic.getProtectRoute().getNodeList().size() - 1) {
//						switch (theTraffic.getProtectRoute().getLinkList().get(n).getType()) {// 2017.3.5
//						// CC
//						// 添加链路类型提示
//						case Fiber:
//							bufferp.append("--<" + theTraffic.getProtectRoute().getLinkList().get(n).getName() + ">--");
//							break;
//						case Satellite:
//							bufferp.append("--<" + theTraffic.getProtectRoute().getLinkList().get(n).getName() + "(卫)"
//									+ ">--");
//							break;
//						case ShortWave:
//							bufferp.append("--<" + theTraffic.getProtectRoute().getLinkList().get(n).getName() + "(短)"
//									+ ">--");
//							break;
//						default:
//							break;
//						}
//					}
//				}
//			}
//			content[i][5] = bufferp;
//		}
//	}

	private void sortManager(JTable table) {
		RowSorter sorter = new TableRowSorter(table.getModel());
		table.setRowSorter(sorter);
	}
	// public static void main(String[] args){
	// new Dlg_DesignResult();
	// }
}

class ReadPhone {
	/**
	 * 读取数据
	 */
	public static StringBuffer ReadData(StringBuffer result) {
		try {
			FileReader read = new FileReader(System.getProperty("user.dir") + "\\资源分配日志.txt");
			BufferedReader br = new BufferedReader(read);
			String row;
			while ((row = br.readLine()) != null) {
				result.append(row);
				result.append("\n");
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}