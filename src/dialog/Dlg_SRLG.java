package dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import data.LinkRGroup;
import data.FiberLink;
import data.WDMLink;
import enums.Layer;

public class Dlg_SRLG extends JFrame {

	JButton jb1, jb2;
	private DefaultTableModel srlg;
	private JTable srlgtable;
	private final String[] columnNames = { "SRLG ID", "SRLG 名称", "所含链路" };
	private Object[][] content = null;
	private static boolean clicked;

	public Dlg_SRLG() {
		LinkRGroup.clicked=true;
		JScrollPane pane1 = new JScrollPane(srlgtable);
		JPanel p = new JPanel();
		JPanel buttonPanel = new JPanel();
		// jb1 = new JButton("自动映射");
		// 监听自动映射按钮
		// jb1.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// //处理srlg
		// LinkRGroup.automation();
		// JOptionPane.showConfirmDialog(null, "自动映射成功");
		// System.out.println("自动映射成功");
		// Dlg_SRLG.clicked=false;
		// }
		// });

		// jb1.setEnabled(true);
		// jb1.addMouseListener(new MouseAdapter() {
		// @Override
		// public void mouseClicked(MouseEvent e) {
		// // LinkRGroup.clear();//测试用
		//
		// if (jb1.isEnabled()) {
		// if (Dlg_SRLG.clicked == false) {
		// Dlg_SRLG.clicked = true;// 设置为已经点击
		// LinkRGroup.automation();
		// JOptionPane.showMessageDialog(null, "自动映射成功");
		// System.out.println("自动映射成功");
		// jb1.setEnabled(false);// 将自动映射按钮暗化
		// } else {
		// JOptionPane.showMessageDialog(null, "自动映射已经完成");
		// jb1.setEnabled(false);// 将自动映射按钮暗化
		// }
		//
		// }
		//
		// }
		// });

		jb2 = new JButton("确定");
		jb2.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setVisible(false);
				dispose();
			}
		});
		int index = 0;
		if (LinkRGroup.SRLGroupList != null && LinkRGroup.SRLGroupList.size() != 0) {// 统计wdmlink的linkgroup数
			for (int j = 0; j < LinkRGroup.SRLGroupList.size(); j++) {
				if (LinkRGroup.SRLGroupList.get(j).getBelongLayer().equals(Layer.WDM))
					index++;
				System.out.println("srlg"+index);
			}
		}
		content = new Object[index][3];

		Font f = new Font("微软雅黑", Font.PLAIN, 12);
		// jb1.setFont(f);
		jb2.setFont(f);
		// 读取srlg相关属性内容
		if (LinkRGroup.SRLGroupList != null && LinkRGroup.SRLGroupList.size() != 0) {
			for (int j = 0,k=0; j < LinkRGroup.SRLGroupList.size(); j++) {
				if (LinkRGroup.SRLGroupList.get(j).getBelongLayer().equals(Layer.WDM)) {// 只显示wdmLInk的linkgroup
					LinkRGroup lrg = LinkRGroup.SRLGroupList.get(j);
					content[k][0] = lrg.getID();
					content[k][1] = lrg.getName();
					String str = "";
					for (int i = 0; i < lrg.getSRLGWDMLinkList().size(); i++) {
						WDMLink fl = lrg.getSRLGWDMLinkList().get(i);
						if (i == lrg.getSRLGWDMLinkList().size() - 1)
							str += fl.getName();
						else
							str += fl.getName() + ";";
					}
					content[k][2] = str;
					k++;
				}
			}
		}
		srlg = new DefaultTableModel(content, columnNames);
		srlgtable = new JTable(srlg);
		srlgtable.getTableHeader().setReorderingAllowed(false);// 列不可动
		sortManager(srlgtable);
		srlgtable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		srlgtable.setPreferredScrollableViewportSize(new Dimension(600, 800));
		// buttonPanel.add(jb1);
		buttonPanel.add(jb2);
		pane1.setViewportView(srlgtable);
		this.add(pane1);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.setColumnWidth(srlgtable, 0, 100);
		this.setColumnWidth(srlgtable, 1, 100);
		this.setColumnWidth(srlgtable, 2, 500);
		this.setSize(600, 400);
		this.setVisible(true);
		this.setTitle("共享风险链路组列表");
		ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/resource/ddd1111.png"));
		this.setIconImage(imageIcon.getImage());
		this.setLocationRelativeTo(null);

	}

	private void sortManager(JTable table) {
		RowSorter sorter = new TableRowSorter(table.getModel());
		table.setRowSorter(sorter);
	}

	private void setColumnWidth(JTable trafTable, int columnIndex, int preferredWidth) {
		TableColumnModel tcm = trafTable.getColumnModel();
		TableColumn tc = tcm.getColumn(columnIndex);
		tc.setPreferredWidth(preferredWidth);
	}

	// 测试
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Dlg_SRLG ss = new Dlg_SRLG();
	}
}
