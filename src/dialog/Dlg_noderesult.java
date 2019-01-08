package dialog;

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

import algorithm.OSNR;
import data.CommonNode;
import data.Traffic;

public class Dlg_noderesult extends JFrame {

	public static void main(String[] args) {
		// TODO �Զ����ɵķ������
		Dlg_noderesult d1=new Dlg_noderesult();
	}

	 private final String[] columnNames = { "ID", "����", "����", "γ��", "�Ƿ񼤻�", "�ڵ�����", "�Ƿ񲨳�����" , "�Ƿ��м�" };
	    private Object[][] content = null;
	    private DefaultTableModel trafficInfoModel;
	    private JTable trafficInfoTable;
	    private ImageIcon title = new ImageIcon(getClass().getResource("/resource/titleresult.jpg"));
	    private JTextArea resultArea;
	    private JScrollPane jspane;
	    private JButton close = new JButton("ȷ��");
	    private StringBuffer result;

	    public Dlg_noderesult() {
		setTitle("�ڵ�鿴");
		this.setSize(800, 500);
		Font f = new Font("΢���ź�", Font.PLAIN, 12);
		close.setFont(f);
		// refreshTable(LinkID, LinkLayer);
		// ��ʼ�ձ�
		int size;
		size = CommonNode.allNodeList.size();
		if (size > 0) {
		    content = new Object[size][columnNames.length];
		} else {
		    content = new Object[0][columnNames.length];
		}
		// �����д������
		Iterator<CommonNode> itor=CommonNode.allNodeList.iterator();
		int i=0;
		while(itor.hasNext()){
		    CommonNode node=itor.next();
		    content[i][0]=node.getId();
		    content[i][1]=node.getName();
		    content[i][2]=node.getLongitude();
		    content[i][3]=node.getLatitude();
		    if(node.isActive()==false)
		    	content[i][4]="��";
		    else 
		    	content[i][4]="��";
		    content[i][5]=node.getNodeType();
		    if(node.isWaveConversion()==false)
				content[i][6]="��";
			else 
				content[i][6]="��";
		    if(node.iszhongji()==false)
				content[i][7]="��";
			else 
				content[i][7]="��";
		    i++;
		}
		///////
		
		
		
		trafficInfoModel = new DefaultTableModel(content, columnNames);
		trafficInfoTable = new JTable(trafficInfoModel) {
		    public boolean isCellEditable(int row, int column) {
			return false;
		    }
		};// ���ñ����ݲ��ɱ༭
		trafficInfoTable.getTableHeader().setReorderingAllowed(false);// �в��ɶ�
		sortManager(trafficInfoTable);

		JScrollPane pane1 = new JScrollPane(trafficInfoTable);
		JLabel titlepane = new JLabel(title);
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
		this.setColumnWidth(trafficInfoTable, 0, 100);
		this.setColumnWidth(trafficInfoTable, 1, 150);
		this.setColumnWidth(trafficInfoTable, 2, 150);
		this.setColumnWidth(trafficInfoTable, 3, 100);
		this.setColumnWidth(trafficInfoTable, 4, 150);
//		this.setColumnWidth(trafficInfoTable, 5, 500);
		this.setColumnWidth(trafficInfoTable, 6, 150);
		this.setColumnWidth(trafficInfoTable, 7, 150);
//		this.setColumnWidth(trafficInfoTable, 8, 80);
//		this.setColumnWidth(trafficInfoTable, 9, 80);


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

	

