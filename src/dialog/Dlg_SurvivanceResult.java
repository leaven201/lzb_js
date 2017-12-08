package dialog;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
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
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


import twaver.Element;
import twaver.Node;
import twaver.TDataBox;
import twaver.TWaverConst;
import twaver.chart.BarChart;
import data.BasicLink;
import data.CommonNode;
import data.FiberLink;
import data.LinkRGroup;
import data.Traffic;
import enums.TrafficLevel;
import survivance.Evaluation;

public class Dlg_SurvivanceResult extends JDialog{
	/**
	 * 
	 */
	private static final long serialVersionUID = 4866749180582173460L;
    private static List<Traffic> traList1;
    private TDataBox dataBox=new TDataBox();
	private BarChart bar=new BarChart(dataBox);
	public Dlg_SurvivanceResult(JDialog owner,int type){
		super(owner,"�˿�������",true);
		if(type == 1){
			this.setTitle("��·����ѭ��");
			Evaluation.listEvaluation(FiberLink.fiberLinkList);
		}
		else if(type == 2){
			this.setTitle("�ڵ㵥��ѭ��");
			Evaluation.nodelistEvaluation(CommonNode.allNodeList);
		}

    	this.setSize(1100,600);
    	Toolkit   tl=Toolkit.getDefaultToolkit();   
		Dimension   screenSize=tl.getScreenSize();   
	    this.setLocation((int)screenSize.getWidth()/2-(int)this.getSize().getWidth()/2, (int)screenSize.getHeight()/2-(int)this.getSize().getHeight()/2);
	    
		DefaultTableModel model;
		JTable table;
		final Object[] columnNames = {"ҵ������","ҵ�����","ҵ���յ� ","ҵ��ȼ�","��Ӱ�����","δ�ָ�����"};
	

		List<Traffic>  traList = new LinkedList<Traffic>();
			traList.addAll(Traffic.trafficList);
		Object data[][] = new Object[traList.size()][7];
		for(int n = 0;n <traList.size();n++){
			data[n][0] = traList.get(n).getFromNode().getName()+"-"+traList.get(n).getToNode().getName();
			data[n][1] = traList.get(n).getFromNode().getName();
			data[n][2] = traList.get(n).getToNode().getName();
			if(traList.get(n).getProtectLevel().equals(TrafficLevel.PERMANENT11))
				data[n][3] = "����1+1";
			else if(traList.get(n).getProtectLevel().equals(TrafficLevel.NORMAL11))
				data[n][3] = "��ͨ1+1";
			else if(traList.get(n).getProtectLevel().equals(TrafficLevel.RESTORATION))
				data[n][3] = "�ָ�";	
			else if(traList.get(n).getProtectLevel().equals(TrafficLevel.NONPROTECT))
				data[n][3] = "�ޱ���";	
			data[n][4] = traList.get(n).getEffectNum();						   						 
			data[n][5] = traList.get(n).getFailNum();
		}
		
		traList1 =traList;
		
		model = new DefaultTableModel(data,columnNames);
		model.setRowCount(traList.size());
		if(model.getRowCount() <traList.size()){
			Vector data1 = new Vector();
			int num = model.getRowCount();
			for(int x = 0;x < 20- num; x++)
				model.addRow(data1);
           
		}
		
		table = new JTable(model){
			   public boolean isCellEditable(int row, int column) { 
				    return false;
				    }
			   };//���ñ����ݲ��ɱ༭
	    table.getTableHeader().setReorderingAllowed(false); 
	    JPanel pane = new JPanel();
	    JScrollPane pane1 = new JScrollPane (table);
	    pane1.setRowHeaderView(new RowHeaderTable(table,40));  
//	    pane.add(pane1);
//	    pane.setVisible(true);
//        setContentPane(pane);
//     	this.setVisible(true);
     	

		JButton excel = new JButton("�ر�");
		Font f2=new Font("΢���ź�", Font.PLAIN, 12);
		excel.setFont(f2);
		excel.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		setVisible(false);
	    		dispose();
//	    		new Dlg_SurvivanceExport(Dlg_SurvivanceResult.this,traList1);
//	    		JOptionPane.showMessageDialog(null,"EXCEL���ѵ�����");
	
	    	}
	    });
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new   WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				
//				ExcelOut.over("���.xls");
	    		setVisible(false);
	    		dispose();//�ͷŴ�����ռ�ڴ���Դ?
				}	
			
       });
//		MyUtil.makeFace(table);//����JTable ��ɫ
		
     	GridBagLayout mg = new GridBagLayout();
		GridBagConstraints mgc = new GridBagConstraints();
		this.setLayout(mg);
		
		
		JScrollPane barpane=new JScrollPane(bar);
		pane.add(barpane);
		pane.setVisible(true);
		mgc.fill = GridBagConstraints.BOTH;
		mgc.gridwidth =  GridBagConstraints.REMAINDER;
		mgc.weightx = 1.0;
		mgc.gridheight = 9;
		mgc.weighty = 9.0;
		mgc.anchor = GridBagConstraints.CENTER;
		this.add(pane1,mgc);
		
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(pane,mgc);

		mgc.fill = GridBagConstraints.NONE;
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		this.add(excel,mgc);
		
		this.setSize(930,530);
		this.setVisible(true);
		ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/resource/ddd1111.png"));
		this.setIconImage(imageIcon.getImage());
     	System.out.println("ȫ�������ҵ����"+Traffic.trafficList.size());
	}
	public Dlg_SurvivanceResult(JFrame owner,int type){
		super(owner,"�˿�������",true);
		if(type == 1){
			this.setTitle("��·����ѭ��");
			Evaluation.listEvaluation(FiberLink.fiberLinkList);
		}
//		else if(type == 2){
//			this.setTitle("��·���ѭ��");
//			Evaluation.dLinkListEvaluation(BasicLink.allLinkList);
//		}
		else if(type == 3){
			this.setTitle("�ڵ㵥ѭ��");
			Evaluation.nodelistEvaluation(CommonNode.allNodeList);
		}
		else if(type == 4){
			this.setTitle("SRLG����ѭ��");
			Evaluation.SRLGListEvaluation(LinkRGroup.SRLGroupList);
		}

//		else if(type == 4){
//			this.setTitle("��·˫��ѭ��");
//			Evaluation.linkPollingII(FiberLink.FiberLinkList, false);
//		}
//		else if(type == 4){
//			this.setTitle("�ڵ���ѭ��");
//			Evaluation.multiNodeEva(CommonNode.allNodeList);
//		}
//		else if(type == 6){
//			this.setTitle("SRLG��˫��ѭ��");
//			Evaluation.srlgPollingII(LinkRGroup.s_lRGroupList, true);
//		}
    	this.setSize(1100,600);
    	Toolkit   tl=Toolkit.getDefaultToolkit();   
		Dimension   screenSize=tl.getScreenSize();   
	    this.setLocation((int)screenSize.getWidth()/2-(int)this.getSize().getWidth()/2, (int)screenSize.getHeight()/2-(int)this.getSize().getHeight()/2);
	    
		DefaultTableModel model;
		JTable table;
		final Object[] columnNames = {"ҵ������","ҵ�����","ҵ���յ� ","ҵ��ȼ�","��Ӱ�����","�жϴ���"};
	

		List<Traffic>  traList = new LinkedList<Traffic>();

		for(int i=0;i<Traffic.trafficList.size();i++)
				traList.add(Traffic.trafficList.get(i));
		Object data[][] = new Object[traList.size()][7];
		for(int n = 0;n <traList.size();n++){
			data[n][0] = traList.get(n).getFromNode().getName()+"-"+traList.get(n).getToNode().getName();
			data[n][1] = traList.get(n).getFromNode().getName();
			data[n][2] = traList.get(n).getToNode().getName();
			if(traList.get(n).getProtectLevel().equals(TrafficLevel.PERMANENT11))
				data[n][3] = "����1+1";
			else if(traList.get(n).getProtectLevel().equals(TrafficLevel.NORMAL11))
				data[n][3] = "��ͨ1+1";
			else if(traList.get(n).getProtectLevel().equals(TrafficLevel.RESTORATION))
				data[n][3] = "�ָ�";
			else if(traList.get(n).getProtectLevel().equals(TrafficLevel.NONPROTECT))
				data[n][3] = "�ޱ���";
			data[n][4] = traList.get(n).getEffectNum();						   						 
			data[n][5] = traList.get(n).getFailNum();

		}
		
		
		
		traList1 =traList;
	//��״ͼ
		Element A=new Node();
		A.setName("��Ӱ�����");
		A.putChartColor(Color.red);
		Element B=new Node();
		B.setName("δ�ָ�����");
		B.putChartColor(Color.blue);
		for(int i=0;i<traList.size();i++){
			bar.addXScaleText(traList.get(i).getName());
			bar.setXScaleTextColor(Color.black);
//			bar.setXScaleTextOrientation(TWaverConst.LABEL_ORIENTATION_VERTICAL);
			A.addChartValue(traList.get(i).getEffectNum());
			B.addChartValue(traList.get(i).getFailNum());
			bar.setBarType(TWaverConst.BAR_TYPE_GROUP);
		}
		dataBox.addElement(A);
		dataBox.addElement(B);
		
		
		model = new DefaultTableModel(data,columnNames);
		model.setRowCount(traList.size());
		if(model.getRowCount() <traList.size()){
			Vector data1 = new Vector();
			int num = model.getRowCount();
			for(int x = 0;x < 20- num; x++)
				model.addRow(data1);
           
		}
		
		table = new JTable(model){
			   public boolean isCellEditable(int row, int column) { 
				    return false;
				    }
			   };//���ñ����ݲ��ɱ༭
	    table.getTableHeader().setReorderingAllowed(false); 
//	    pane1.setRowHeaderView(new RowHeaderTable(table,40));  
//	    pane.add(pane1);
//	    pane.setVisible(true);
//        setContentPane(pane);
//     	this.setVisible(true);
     	

		JButton excel = new JButton("�ر�");
		Font f1=new Font("΢���ź�", Font.PLAIN, 12);
		excel.setFont(f1);
		excel.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		setVisible(false);
	    		dispose();
//	    		new Dlg_SurvivanceExport(Dlg_SurvivanceResult.this,traList1);
//	    		JOptionPane.showMessageDialog(null,"EXCEL���ѵ�����");
	
	    	}
	    });
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new   WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				
//				ExcelOut.over("���.xls");
	    		setVisible(false);
	    		dispose();//�ͷŴ�����ռ�ڴ���Դ?
				}	
			
       });
//		MyUtil.makeFace(table);//����JTable ��ɫ
		Insets insert;
     	GridBagLayout mg = new GridBagLayout();
		GridBagConstraints mgc = new GridBagConstraints();
		this.setLayout(mg);
		
		mgc.fill = GridBagConstraints.BOTH;
		mgc.gridwidth =  GridBagConstraints.REMAINDER;
		mgc.weightx = 1.0;
		mgc.gridheight = 1;
		mgc.weighty = 3.0;
		this.add(new JScrollPane (table),mgc);
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		insert = new Insets(20,0,20,0);
		mgc.insets=insert;
		mgc.weightx = 1.0;
		mgc.gridheight = 3;
		mgc.weighty = 3.0;
		mgc.fill = GridBagConstraints.BOTH; 
		this.add(new JScrollPane(bar),mgc);
		
		mgc.fill = GridBagConstraints.NONE;
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		mgc.gridheight=1;
		mgc.weighty=1;
		insert=new Insets(10, 0, 0, 0);
		mgc.insets=insert;
		this.add(excel,mgc);
		
		this.setSize(930,530);
		this.setVisible(true);
     	System.out.println("ȫ�������ҵ����"+Traffic.trafficList.size());
	}

}
