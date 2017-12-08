package dialog;

/**
 * ���ܣ�չʾ·�ɹ滮�������Dlg_PolicySetting���ã�����·�ɵ����ƣ���ʽΪ�ڵ�--<��·>--�ڵ�
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
import data.TrafficGroupNew;

public class Dlg_RelatedTraffic extends JFrame {
	private final String[] columnNames = { "�������ҵ����", "A", "����", "B", "����" };
	private Object[][] content = null;
	private DefaultTableModel trafficInfoModel;
	private JTable trafficInfoTable;
	private JButton close = new JButton("ȷ��");

	public Dlg_RelatedTraffic() {
		setTitle("�������ҵ����");
		this.setSize(640, 350);
		Font f = new Font("΢���ź�", Font.PLAIN, 12);
		close.setFont(f);
		// refreshTable(LinkID, LinkLayer);
		// ��ʼ�ձ�
		int traSize;
		traSize = Traffic.trafficList.size();
		if (traSize > 0) {
			content = new Object[traSize][columnNames.length];
		} else {
			content = new Object[50][columnNames.length];
		}
		// �ֲ���ҵ�����д������
		insertFiberTraffic(content);
		for (int i = 0; i < traSize; i++) {

		}
		trafficInfoModel = new DefaultTableModel(content, columnNames);
		trafficInfoTable = new JTable(trafficInfoModel) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};// ���ñ����ݲ��ɱ༭
		trafficInfoTable.getTableHeader().setReorderingAllowed(false);// �в��ɶ�
		sortManager(trafficInfoTable);

		JScrollPane pane1 = new JScrollPane(trafficInfoTable);
		JPanel p = new JPanel();
		JPanel mainPanel = new JPanel();
		JPanel buttonPanel = new JPanel();

		trafficInfoTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // ˮƽ������

		close.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		// �����п�
		this.setColumnWidth(trafficInfoTable, 0, 150);
		this.setColumnWidth(trafficInfoTable, 1, 70);
		this.setColumnWidth(trafficInfoTable, 2, 150);
		this.setColumnWidth(trafficInfoTable, 3, 70);
		this.setColumnWidth(trafficInfoTable, 4, 150);
		int height = 2;
		double weight = 2.0;

		pane1.setViewportView(trafficInfoTable);
	//	pane1.setRowHeaderView(new RowHeaderTable(trafficInfoTable, 40));
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
//		p.add(jspane, mgc);

		buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 100, 5));
		buttonPanel.add(close);

		this.setContentPane(mainPanel);
		BorderLayout mainPaneLayout = new BorderLayout();
		setLayout(mainPaneLayout);
//		mainPanel.add(titlepane, BorderLayout.NORTH);
		mainPanel.add(p, BorderLayout.CENTER);
		mainPanel.add(buttonPanel, BorderLayout.SOUTH);
		this.setVisible(true);
		this.setLocationRelativeTo(null);
//		ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/resource/ddd1111.png"));
//		this.setIconImage(imageIcon.getImage());
	}

	private void setColumnWidth(JTable trafTable, int columnIndex, int preferredWidth) {
		TableColumnModel tcm = trafTable.getColumnModel();
		TableColumn tc = tcm.getColumn(columnIndex);
		tc.setPreferredWidth(preferredWidth);
	}
	
	private void insertFiberTraffic(Object[][] content) {
		for (int i = 0; i < TrafficGroupNew.grouplist.size(); i++) {
			TrafficGroupNew thegroup =TrafficGroupNew.grouplist.get(i);
			if(thegroup.getBelongGroup().equals("A"))
			{content[thegroup.getId()-1][0] = "�������ҵ����"+thegroup.getId();
//			content[i][1] = theTraffic.getName();// �������ҵ����
			content[thegroup.getId()-1][1] = thegroup.trafficAndNum();// A������ҵ������
			content[thegroup.getId()-1][2] = thegroup.getType();}// Aҵ��������
			if(thegroup.getBelongGroup().equals("B"))
			{content[thegroup.getId()-1][3] = thegroup.trafficAndNum();//B������ҵ������
		    content[thegroup.getId()-1][4] =      thegroup.getType();}   //Bҵ��������
			DecimalFormat df = new DecimalFormat("0.0000 ");

//			if (theTraffic.getWorkRoute() == null)
//				content[i][5] = null;
//			else
//				content[i][5] = df.format(osnr.calculateOSNR(theTraffic.getWorkRoute()));// ����·�ɵ�osnr
//			if (theTraffic.getProtectRoute() == null)
//				content[i][] = null;
//			else
//				content[i][7] = df.format(osnr.calculateOSNR(theTraffic.getProtectRoute()));// ����·�ɵ�osnr

//			content[i][5] = theTraffic.getNrate();// ҵ������
//			content[i][6] = theTraffic.getProtectLevel();// ҵ�񱣻��ȼ�
//
//			StringBuffer bufferw = new StringBuffer();// ����·��
//			if (theTraffic.getWorkRoute() != null && null != theTraffic.getWorkRoute().getNodeList()) {
//				for (int n = 0; n < theTraffic.getWorkRoute().getNodeList().size(); n++) {
//					bufferw.append(theTraffic.getWorkRoute().getNodeList().get(n).getName());
//					if (n != theTraffic.getWorkRoute().getNodeList().size() - 1) {
						// switch
						// (theTraffic.getWorkRoute().getLinkList().get(n).getType())
						// {// 2017.3.5
						// // CC
						// // �����·������ʾ
						// case Fiber:
						// bufferw.append("--<" +
						// theTraffic.getWorkRoute().getLinkList().get(n).getName()
						// + ">--");
						// break;
						// case Satellite:
						// bufferw.append(
						// "--<" +
						// theTraffic.getWorkRoute().getLinkList().get(n).getName()
						// + "(��)" + ">--");
						// break;
						// case ShortWave:
						// bufferw.append(
						// "--<" +
						// theTraffic.getWorkRoute().getLinkList().get(n).getName()
						// + "(��)" + ">--");
						// break;
						// default:
						// break;
						// }
//						bufferw.append("--");
////						bufferw.append("--<" + theTraffic.getWorkRoute().getWDMLinkList().get(n).getName() + ">--");
//					}
//				}
//			}
//			content[i][3] = "   "+bufferw;

//			StringBuffer bufferp = new StringBuffer();// ����·��
//			if (null != theTraffic.getProtectRoute() && null != theTraffic.getProtectRoute().getNodeList()) {
//				for (int n = 0; n < theTraffic.getProtectRoute().getNodeList().size(); n++) {
//					bufferp.append(theTraffic.getProtectRoute().getNodeList().get(n).getName());
//					if (n != theTraffic.getProtectRoute().getNodeList().size() - 1) {
//						// switch
//						// (theTraffic.getProtectRoute().getWDMLinkList().get(n).getType())
//						// {// 2017.3.5
//						// // CC
//						// // �����·������ʾ
//						// case Fiber:
//						// bufferp.append("--<" +
//						// theTraffic.getProtectRoute().getWDMLinkList().get(n).getName()
//						// + ">--");
//						// break;
//						// case Satellite:
//						// bufferp.append("--<" +
//						// theTraffic.getProtectRoute().getWDMLinkList().get(n).getName()
//						// + "(��)"
//						// + ">--");
//						// break;
//						// case ShortWave:
//						// bufferp.append("--<" +
//						// theTraffic.getProtectRoute().getWDMLinkList().get(n).getName()
//						// + "(��)"
//						// + ">--");
//						// break;
//						// default:
//						// break;
//						// }
////						bufferp.append("--<" + theTraffic.getProtectRoute().getWDMLinkList().get(n).getName() + ">--");
//						bufferp.append("--");
//					}
//				}
//			}
//			content[i][4] = "   "+bufferp;
		}
	}
	


	private void sortManager(JTable table) {
		RowSorter sorter = new TableRowSorter(table.getModel());
		table.setRowSorter(sorter);
	}
}

