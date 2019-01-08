package dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import twaver.Element;
import twaver.Node;
import twaver.TDataBox;
import twaver.TWaverConst;
import twaver.chart.PieChart;
import twaver.table.TTableColumn;
import data.CommonNode;
import data.FiberLink;
import data.Network;
import data.Traffic;
import datastructure.UIFiberLink;
import enums.Layer;
import enums.TrafficLevel;
import survivance.Evaluation;
import survivance.Survivance;

public class Dlg_SetFault extends JFrame{
	private DefaultTableModel nodeModel;
	private DefaultTableModel linkModel;
	private DefaultTableModel trafficModel;
	private JTable nodeTable;
	private JTable linkTable;
	private JTable trafficTable;
	private DefaultTableModel resultModel;
	private JTable resultTable;
	private TDataBox box = new TDataBox();
	private PieChart pie = new PieChart(box);
	private final Object[] nodeHeader = {"","ID","�ڵ�����","��������","�ڵ㼶��"};
	private final Object[] linkHeader = {"","ID","��·����","�׽ڵ�","ĩ�ڵ�"};
	private final String[] trafficHeader={"ID","��Ӱ��ҵ������","�׽ڵ� ","ĩ�ڵ�","�����ȼ�","��Ӱ��̶�","ԭ����·��","ԭ����·��","���Ϻ���·��","���Ϻ󱣻�·��"};//���������final����
	private List<Traffic> tra = new LinkedList<Traffic>();  
	private List<CommonNode> thenode = new LinkedList<CommonNode>();	//��Ź��Ͻڵ�
	private List<FiberLink> thelink = new LinkedList<FiberLink>();	//��Ź�����·
	private Element aa = new Node();   
	private Element bb = new Node();   
	private Element cc = new Node();
	private Element dd = new Node();
	private List<Integer> intlist = new LinkedList<Integer>();	
	public Dlg_SetFault(Frame  owner){
		setTitle("�˹��������÷���");
		ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/resource/titlexiao.png"));
		this.setIconImage(imageIcon.getImage());
//		super(owner,"�˹��������÷���",true);
    	this.setSize(1000,600);
    	Toolkit   tl=Toolkit.getDefaultToolkit();   
		Dimension   screenSize=tl.getScreenSize();   
	    this.setLocation((int)screenSize.getWidth()/2-(int)this.getSize().getWidth()/2, (int)screenSize.getHeight()/2-(int)this.getSize().getHeight()/2);
	    resultModel = new DefaultTableModel(6,2);
	    resultTable = new JTable(resultModel){
	    	public Component prepareRenderer(TableCellRenderer renderer, int row, int col) {
				Component component = super.prepareRenderer(renderer, row, col);
			
					setAlternateRowColor(component, row, col,5);
				
				
				return component;
			}
			public boolean isCellEditable(int row, int column) { 
				return false;
				
			}
		};
		
//		resultModel.setValueAt("     ҵ�񱣻���", 0, 0);
//		resultModel.setValueAt("     ҵ�񱣻�ǿ��", 1, 0);
//		resultModel.setValueAt("     ҵ��ȼ�Ч��(����1+1)", 2, 0);
//		resultModel.setValueAt("     ҵ��ȼ�Ч��(1+1)", 3, 0);
//		resultModel.setValueAt("     ҵ��ȼ�Ч��(�ָ�)", 4, 0);
//		resultModel.setValueAt("     ������Ч��", 5, 0);
//	
	    trafficModel = new DefaultTableModel(null,trafficHeader);
	    
	    
	    linkModel = new DefaultTableModel(linkHeader,0);   
		Object data[] = new Object[5];
		for(int n = 0;n <FiberLink.fiberLinkList.size();n++){
			data[0] = new Boolean(false);
			data[1] = FiberLink.fiberLinkList.get(n).getId();
			data[2] = FiberLink.fiberLinkList.get(n).getName();
			data[3] = FiberLink.fiberLinkList.get(n).getFromNode().getM_sName();
			data[4] = FiberLink.fiberLinkList.get(n).getToNode().getM_sName();	
	        linkModel.addRow(data);
		}
		linkTable = new JTable(linkModel){
			public boolean isCellEditable(int row, int column) { 
				if(column == 0){
					return true;
				}
				else{
					return false;
				}
				
			}
		};
	    
	    nodeModel = new DefaultTableModel(nodeHeader,0);    
		
		
	    Object data1[] = new Object[5];
		for (int n = 0; n < CommonNode.allNodeList.size(); n++) {
		    data1[0] = new Boolean(false);
		    data1[1] = CommonNode.allNodeList.get(n).getId();
		    data1[2] = CommonNode.allNodeList.get(n).getName();
		    switch (CommonNode.allNodeList.get(n).getNodeType()) {
		    case ROADM:
			data1[3] = "ROADM";
			break;
//		    case FOADM:
//			data[3] = "FOADM";
//			break;
		    case OLA:
			data1[3] = "OLA";
			break;
		    default:
			break;
		    }
		    nodeModel.addRow(data1);
		}
		nodeTable = new JTable(nodeModel){
			public boolean isCellEditable(int row, int column) { 
				if(column == 0){
					return true;
				}
				else{
					return false;
				}
				
			}
		};
		
	    trafficTable = new JTable( trafficModel){
			public boolean isCellEditable(int row, int column) { 
				return false;
			}
		};
		pie.setSize(40,80);
		
		trafficTable.getTableHeader().setReorderingAllowed(false); 
		nodeTable.getTableHeader().setReorderingAllowed(false); //�в��ɶ�
		linkTable.getTableHeader().setReorderingAllowed(false); //�в��ɶ�
		JScrollPane pane1 = new JScrollPane (nodeTable);
		JScrollPane pane2 = new JScrollPane (trafficTable);
		JScrollPane pane3 = new JScrollPane (linkTable);
		pane1.setRowHeaderView(new RowHeaderTable(nodeTable,40)); 
		pane3.setRowHeaderView(new RowHeaderTable(linkTable,40)); 
		
		nodeTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  //ˮƽ������
		trafficTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  //ˮƽ������
		linkTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);  //ˮƽ������
		
		TableColumnModel tcm = trafficTable.getColumnModel();   
	    tcm.getColumn(7).setPreferredWidth(200); 
	    tcm.getColumn(8).setPreferredWidth(200);
	    tcm.getColumn(9).setPreferredWidth(200); 
	    tcm.getColumn(6).setPreferredWidth(200);
	    
	    tcm = nodeTable.getColumnModel();   
	    tcm.getColumn(0).setPreferredWidth(80); 
	    tcm.getColumn(1).setPreferredWidth(80);

	    tcm = resultTable.getColumnModel();   
	    tcm.getColumn(0).setPreferredWidth(150); 
	    tcm.getColumn(1).setPreferredWidth(80);
		
        tcm = nodeTable.getColumnModel();   
        
        tcm.getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));   
        tcm.getColumn(0).setCellRenderer(new MyTableRenderer());   
  
        tcm.getColumn(0).setPreferredWidth(20);   
        tcm.getColumn(0).setWidth(20);   
        tcm.getColumn(0).setMaxWidth(20);  	
   
	    tcm = linkTable.getColumnModel();   
	    tcm.getColumn(0).setPreferredWidth(80); 
	    tcm.getColumn(1).setPreferredWidth(80);
        tcm.getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox()));   
        tcm.getColumn(0).setCellRenderer(new MyTableRenderer());   
        tcm.getColumn(0).setPreferredWidth(20);   
        tcm.getColumn(0).setWidth(20);   
        tcm.getColumn(0).setMaxWidth(20);  
		
	   
	    JButton close  = new JButton(" ��  ��  ");
//	    JButton Browse = new JButton("ͳ�ƽ��");
	    JButton simulation  = new JButton(" ��  ��  ");
	    
	    simulation.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){	  	
	    		int num = 0;
	    		double res=0;
	    	    double percent=0;
	    		
	    		recoveryLinkListcol(thelink,thenode);//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
	    		
	    		thenode = new LinkedList<CommonNode>();	
	    		for(int i = 0;i < nodeTable.getRowCount();i++){
	    			
	    			if(nodeTable.getValueAt(i,0).toString().equals("true")){
	    				++num;
	    				thenode.add(CommonNode.getNode(Integer.parseInt(String.valueOf(nodeTable.getValueAt(i,1)))));
	    			}
	    			
	    		}
	    		thelink = new LinkedList<FiberLink>();	
	    		for(int i = 0;i < linkTable.getRowCount();i++){	
	    			if(linkTable.getValueAt(i,0).toString().equals("true")){
	    				++num;
	    				thelink.add(FiberLink.getFiberLink(Integer.parseInt(String.valueOf(linkTable.getValueAt(i,1)))));	    			
	    			}
	    			
	    		}
	    		if(num == 0){
	    			JOptionPane.showMessageDialog(null, "�����ù��Ͻڵ����·��");
	    		}
	    		else{
	    		//	���ù�����· thenode 
	    			System.out.println("��û�н����˹����öϵ�");
	    			tra = Evaluation.multiEvaluation(thelink,thenode);//���ù�����·״̬��������Ӱ��ҵ����,thelink��thenode��Ź�����·�ͽڵ�
	    			System.out.println("�˹����öϵ�"+tra.size());
	    			setLinkListcol(thelink,thenode);//MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
	    		    aa.putChartValue(Evaluation.multiKeepPer);// ��ͼ��ֵ
	    		    bb.putChartValue(Evaluation.multiDownPer);
	    		    cc.putChartValue(Evaluation.multiCutoffPer);
	    		    dd.putChartValue(Evaluation.multiUneffPer);
	    		    
//		            
		            showEffectTraffic(tra);
		            Evaluation.clearRsmRoute(tra);
		           
	    			
	    		}
	    	}
	    });
	    close.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){	
	    		
	    		recoveryLinkListcol(thelink,thenode);
	    		setVisible(false);
	    		dispose();//�ͷŴ�����ռ�ڴ���Դ?
	    	}
	    });
	    setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new   WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				recoveryLinkListcol(thelink,thenode);
	    		setVisible(false);
	    		dispose();
			}	
        });	
	      
	    nodeTable.addMouseListener(new MouseAdapter() {
	          @Override
	          public void mouseClicked(MouseEvent e) {
	        	  for(int i = 0;i < nodeTable.getRowCount();i++){	    			
		    			if(nodeTable.getValueAt(i,0).toString().equals("true")){
		    				//һ��֮��������·��Ϊ����
		    				setFaultLink(CommonNode.allNodeList.get(i));
//		    				.getNode(Integer.parseInt(String.valueOf(nodeTable.getValueAt(i,1)))));
//		    				JOptionPane.showMessageDialog(null, "sssss");
		    			}
		    			
		    		}
	          }
	   });
        aa.setName("����");   
        bb.setName("����");   
        cc.setName("�ж�");
        dd.setName("��Ӱ��");
        
        //���ñ���ɫ����   
//        barChart.setBackgroundVisible(true);   
        pie.setBackgroundVisible(true); 
        
        //ͼʾ��ɫ   
        aa.putChartColor(Color.green);   
        bb.putChartColor(Color.blue);   
        cc.putChartColor(Color.RED);  
        dd.putChartColor(Color.yellow);  
        
//        //ͼʾ�ı���ֵ   
//        aa.putChartValue(0.25);   
//        bb.putChartValue(0.25);   
//        cc.putChartValue(0.25);   
           
        box.addElement(aa);   
        box.addElement(bb);   
        box.addElement(cc);  
        box.addElement(dd);
	    
	    JPanel piepanel = new JPanel(new BorderLayout());
	    piepanel.add(pie, BorderLayout.CENTER);
	    
	    JPanel p1 = new JPanel(new BorderLayout());
	    p1.setBorder (BorderFactory.createTitledBorder ("�ڵ��б�"));
	    p1.add(pane1, BorderLayout.CENTER);
	    
	    JPanel p5 = new JPanel(new BorderLayout());
	    p5.setBorder (BorderFactory.createTitledBorder ("��·�б�"));
	    p5.add(pane3, BorderLayout.CENTER);
	    
	    JPanel p6 = new JPanel(new BorderLayout());
	    p6.setBorder (BorderFactory.createTitledBorder ("��Ӱ��ҵ���б�"));
	    p6.add(pane2, BorderLayout.CENTER);
	    
	    JPanel p4 = new JPanel();
	    GridBagLayout mg = new GridBagLayout();
	    GridBagConstraints mgc = new GridBagConstraints();		
		p4.setLayout(mg);
		mgc.fill = GridBagConstraints.NONE;
		
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		mgc.weightx = 1.0;
		mgc.weighty = 1.0;	
		mgc.gridheight = 1;
		p4.add(simulation,mgc);
//		mgc.gridwidth = GridBagConstraints.REMAINDER;
//		p4.add(Browse,mgc);
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		p4.add(close,mgc);
	    
	    JPanel p3 = new JPanel();
	    
	    Insets insert ;
    	 mg = new GridBagLayout();
		 mgc = new GridBagConstraints();		
		p3.setLayout(mg);
		mgc.fill = GridBagConstraints.BOTH;
		
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		mgc.weightx = 1.0;
		mgc.weighty = 2.0;	
		mgc.gridheight = 2;
		p3.add(p1,mgc);
		p3.add(p5,mgc);
		
		resultTable.setRowHeight(30);
		JPanel px = new JPanel();
		px.setLayout (new GridLayout (1,1));
		px.setBorder (BorderFactory.createEtchedBorder ());
//		px.add (resultTable);
		
		 JPanel p2 = new JPanel();
		 mg = new GridBagLayout();
		 mgc = new GridBagConstraints();		
		p2.setLayout(mg);
		mgc.fill = GridBagConstraints.BOTH;
		mgc.gridwidth = 1;
		mgc.weighty = 1.0;
		mgc.gridheight = 1;
		insert = new Insets(5,-10,5,0);
		mgc.insets = insert;
		p2.add(p4,mgc);
		mgc.gridwidth = 2;
		insert = new Insets(-5,30,5,10);
		mgc.insets = insert;
		p2.add(piepanel,mgc);
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		mgc.fill = GridBagConstraints.NONE;
		insert = new Insets(5,30,5,10);
		mgc.insets = insert;
		p2.add(px,mgc);
		
		JSplitPane split1 = new JSplitPane(JSplitPane.VERTICAL_SPLIT,p6, p2);
        split1.setOneTouchExpandable(true);
        split1.setDividerLocation(350);
		
		
		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,p3, split1);
        split.setOneTouchExpandable(true);
        split.setDividerLocation(380);
        


		setContentPane(split);
		this.setVisible(true);
	}
	public void showEffectTraffic(List<Traffic> tra){
		
		for (int i = 0; i < trafficModel.getRowCount();) {
			trafficModel.removeRow(0);
		}
		if (tra == null || tra.isEmpty()) {// 2017.11.15
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
				data1[4] = "��ͨ1+1";
			} else if (tra.get(i).getProtectLevel().equals(TrafficLevel.PERMANENT11)) {
				data1[4] = "����1+1";
			} else if (tra.get(i).getProtectLevel().equals(TrafficLevel.RESTORATION)) {
				data1[4] = "�ָ�";
//			} else if (tra.get(i).getProtectLevel().equals(TrafficLevel.PresetRESTORATION)) {
//				data1[4] = "ר��Ԥ�ûָ�";
			} else if (tra.get(i).getProtectLevel().equals(TrafficLevel.PROTECTandRESTORATION)) {
				data1[4] = "����+�ָ�";
			} else if (tra.get(i).getProtectLevel().equals(TrafficLevel.NONPROTECT)) {
				data1[4] = "�ޱ���";
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
				// �����·������ʾ
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
					// �����·������ʾ
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
					// �����·������ʾ
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
					// �����·������ʾ
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
					data1[5] = "�ж�";

				} else if ((tra1.getResumeRoute() != null && tra1.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra1.getResumeRoutePro() == null || tra1.getResumeRoutePro().getWDMLinkList().size() == 0))
					data1[5] = "����";
				else if ((tra1.getResumeRoute() != null && tra1.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra1.getResumeRoutePro() != null && tra1.getResumeRoutePro().getWDMLinkList().size() != 0))
					data1[5] = "����";
				break;
			case NONPROTECT:
				if (tra1.getResumeRoute() == null || tra1.getResumeRoute().getWDMLinkList().size() == 0) {
					data1[5] = "�ж�";

				}
				break;
			case NORMAL11:
				if (tra1.getResumeRoute() == null || tra1.getResumeRoute().getWDMLinkList().size() == 0) {
					data1[5] = "�ж�";

				} else if ((tra1.getResumeRoute() != null && tra1.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra1.getResumeRoutePro() == null || tra1.getResumeRoutePro().getWDMLinkList().size() == 0))
					data1[5] = "����";
				else if ((tra1.getResumeRoute() != null && tra1.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra1.getResumeRoutePro() != null && tra1.getResumeRoutePro().getWDMLinkList().size() != 0))
					data1[5] = "����";
				break;
			case RESTORATION:
				if (tra1.getResumeRoute() == null || tra1.getResumeRoute().getWDMLinkList().size() == 0) {
					data1[5] = "�ж�";

				} else if (tra1.getResumeRoute() != null && tra1.getResumeRoute().getWDMLinkList().size() != 0)
					data1[5] = "����";
				break;
			case PresetRESTORATION:
				if (tra1.getResumeRoute() == null || tra1.getResumeRoute().getWDMLinkList().size() == 0) {
					data1[5] = "�ж�";
				} else if ((tra1.getResumeRoute() != null && tra1.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra1.getResumeRoutePro() == null || tra1.getResumeRoutePro().getWDMLinkList().size() == 0))
					data1[5] = "����";
				else if ((tra1.getResumeRoute() != null && tra1.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra1.getResumeRoutePro() != null && tra1.getResumeRoutePro().getWDMLinkList().size() != 0))
					data1[5] = "����";
				break;
			case PROTECTandRESTORATION:
				if (tra1.getResumeRoute() == null || tra1.getResumeRoute().getWDMLinkList().size() == 0) {
					data1[5] = "�ж�";
				} else if ((tra1.getResumeRoute() != null && tra1.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra1.getResumeRoutePro() == null || tra1.getResumeRoutePro().getWDMLinkList().size() == 0))
					data1[5] = "����";
				else if ((tra1.getResumeRoute() != null && tra1.getResumeRoute().getWDMLinkList().size() != 0)
						&& (tra1.getResumeRoutePro() != null && tra1.getResumeRoutePro().getWDMLinkList().size() != 0))
					data1[5] = "����";
				break;
			default:
				break;
			}

			trafficModel.addRow(data1);

		}
	}  
	  
	
	private void setFaultLink(CommonNode node){
/*//	    List<FiberLink> link = new LinkedList<FiberLink>();	
//		for(int i = 0;i < FiberLink.fiberLinkList.size();i++){
//			if(FiberLink.fiberLinkList.get(i).getM_cFromNode().equals(node)||FiberLink.fiberLinkList.get(i).getM_cToNode().equals(node)){
//				link.add(FiberLink.fiberLinkList.get(i));
//			}
//		}
//		System.out.println("fibersize    "+link.size());
//		Object data[] = new Object[5];
//		
//		
//		intlist = new LinkedList<Integer>();	
//		for(int j = 0;j < link.size();j++){
//			for(int i = 0;i < linkTable.getRowCount();i++){
//				
//				if(Integer.parseInt(String.valueOf(linkTable.getValueAt(i,1))) == (link.get(j).getId())){
//					linkTable.remove(i);
//					data[0] = new Boolean(true);
//					data[1] = linkTable.getValueAt(i,1);
//					data[2] = linkTable.getValueAt(i,2);
//					data[3] = linkTable.getValueAt(i,3);
//					data[4] = linkTable.getValueAt(i,4);	
//			        linkModel.insertRow(i, data);
//					linkTable.setValueAt(new Boolean(true), i, 0);
//					intlist.add(i);
//					
//				}
//				
//			}
//			System.out.println("intlistsize    "+intlist.size());
//		}
//		
//		
//		linkTable = new JTable(linkModel){
//			public boolean isCellEditable(int row, int column) { 
//				if(column == 0){
//					for(int i = 0;i < intlist.size();i++){
//						if(row == intlist.get(i))
//							return false;
//					}
//					return true;
//				}
//				else{
//					return false;
//				}
//				
//			}
//		};
*/	}
	private void setLinkListcol(List<FiberLink> fiberlinklist,List<CommonNode> nodelist){	
		FiberLink fiberlink ;
		UIFiberLink UIfiberlink = null;
		for(int i = 0;i < fiberlinklist.size();i++){
			fiberlink =  fiberlinklist.get(i);
			if (fiberlink != null) {
			 UIfiberlink =  UIFiberLink.getUIFiberLink(fiberlink.getName());	
			}
			 if(UIfiberlink != null){
				 UIfiberlink.putLinkColor(Color.GRAY.brighter());// ������·��ɫ��ɫ
				 UIfiberlink.putLinkBlinking(true);// ������˸
				 UIfiberlink.putLinkBlinkingColor(Color.RED);// ��ɫ��˸
				 UIfiberlink.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
				 UIfiberlink.putLink3D(true);
			 }
		}
		 
		 
		List<FiberLink> fiberlinklist1 = new LinkedList<FiberLink>();
		FiberLink fiberlink1 ;
		UIFiberLink UIfiberlink1;
		CommonNode node;
		for(int j = 0;j < nodelist.size();j++){
			node = nodelist.get(j);
			fiberlinklist1 = Survivance.getNodeEffectLink(node);
			
			for(int i = 0;i < fiberlinklist1.size();i++){
				fiberlink1 =  fiberlinklist1.get(i);
			    UIfiberlink1 =  UIFiberLink.getUIFiberLink(fiberlink1.getName());	
				 if(UIfiberlink1 != null){
					 UIfiberlink1.putLinkColor(Color.GRAY.brighter());
					 UIfiberlink1.putLinkBlinking(true);
					 UIfiberlink1.putLinkBlinkingColor(Color.RED);
					 UIfiberlink1.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
					 UIfiberlink1.putLink3D(true);
				 }
			}
		} 
	}
	 private void recoveryLinkListcol(List<FiberLink> fiberlinklist,List<CommonNode> nodelist){	
		 FiberLink fiberlink ;
		 UIFiberLink UIfiberlink;
		 for(int i = 0;i < fiberlinklist.size();i++){
			 fiberlink = fiberlinklist.get(i);
			 UIfiberlink =  UIFiberLink.getUIFiberLink(fiberlink.getName());	
			 if(UIfiberlink != null){
//				 if(Network.searchNetwork(fiberlink.getFromNode().getM_nSubnetNum())!=null){
//					    int from, to;
//						from = Network.searchNetwork(fiberlink.getFromNode().getM_nSubnetNum()).getM_nNetType();
//						to = Network.searchNetwork(fiberlink.getToNode().getM_nSubnetNum()).getM_nNetType();
//						if(1 == from && 1 == to){
							 UIfiberlink.setthisColor(Color.blue);
							 UIfiberlink.putLinkBlinking(false);
							 UIfiberlink.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
							 UIfiberlink.putLink3D(false);
//						}
//						else if(2 == from && 2 == to){
//							UIfiberlink.setthisColor(Color.BLUE);
//							 UIfiberlink.putLinkBlinking(false);
//							 UIfiberlink.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
//							 UIfiberlink.putLink3D(false);
//						}
//						else if(3 == from && 3 == to){
//							UIfiberlink.setthisColor(Color.YELLOW);
//							 UIfiberlink.putLinkBlinking(false);
//							 UIfiberlink.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
//							 UIfiberlink.putLink3D(false);
//						}
//						else if((1 == from && 2 == to)||(2 == from && 1 == to)){
//							UIfiberlink.setthisColor(Color.CYAN);
//							 UIfiberlink.putLinkBlinking(false);
//							 UIfiberlink.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
//							 UIfiberlink.putLink3D(false);
//						}
//						else if((3 == from && 2 == to)||(2 == from && 3 == to)){
//							UIfiberlink.setthisColor(Color.GRAY);
//							 UIfiberlink.putLinkBlinking(false);
//							 UIfiberlink.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
//							 UIfiberlink.putLink3D(false);
//						} 
//				 }
					
			 }
		 }
		 
		 
		 List<FiberLink> fiberlinklist1 = new LinkedList<FiberLink>();
			UIFiberLink UIfiberlink1;
			CommonNode node;
			FiberLink fiberlink1 ;
			for(int j = 0;j < nodelist.size();j++){
				node = nodelist.get(j);
				fiberlinklist1 = Survivance.getNodeEffectLink(node);				 
				 for(int i = 0;i < fiberlinklist1.size();i++){
					 fiberlink1 = fiberlinklist1.get(i);
					 UIfiberlink1 =  UIFiberLink.getUIFiberLink(fiberlink1.getName());	
					 if(UIfiberlink1 != null){
//						 if(Network.searchNetwork(fiberlink1.getFromNode().getM_nSubnetNum())!=null){
//							    int from, to;
//								from = Network.searchNetwork(fiberlink1.getFromNode().getM_nSubnetNum()).getM_nNetType();
//								to = Network.searchNetwork(fiberlink1.getToNode().getM_nSubnetNum()).getM_nNetType();
//								if(1 == from && 1 == to){
									 UIfiberlink1.setthisColor(Color.blue);
									 UIfiberlink1.putLinkBlinking(false);
									 UIfiberlink1.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
									 UIfiberlink1.putLink3D(false);
//								}
//								else if(2 == from && 2 == to){
//									UIfiberlink1.setthisColor(Color.BLUE);
//									 UIfiberlink1.putLinkBlinking(false);
//									 UIfiberlink1.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
//									 UIfiberlink1.putLink3D(false);
//								}
//								else if(3 == from && 3 == to){
//									UIfiberlink1.setthisColor(Color.YELLOW);
//									 UIfiberlink1.putLinkBlinking(false);
//									 UIfiberlink1.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
//									 UIfiberlink1.putLink3D(false);
//								}
//								else if((1 == from && 2 == to)||(2 == from && 1 == to)){
//									UIfiberlink1.setthisColor(Color.CYAN);
//									 UIfiberlink1.putLinkBlinking(false);
//									 UIfiberlink1.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
//									 UIfiberlink1.putLink3D(false);
//								}
//								else if((3 == from && 2 == to)||(2 == from && 3 == to)){
//									UIfiberlink1.setthisColor(Color.GRAY);
//									 UIfiberlink1.putLinkBlinking(false);
//									 UIfiberlink1.putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
//									 UIfiberlink1.putLink3D(false);
//								} 
//						 }
							
					 }
				 
			}
			
			
			}
		 		 
	 }
	 
	public void setAlternateRowColor(Component component, int row, int col,int type){

//			FiberLinkELement person = (FiberLinkELement) fiberlinkTable.getElementByRowIndex(row);
//			if(person == null){
//				return;
//			}
			Color alternateColor = new Color(238, 238, 238);
			 if (col == 0) {
	            	component.setBackground(alternateColor);
	            } else {
	            	component.setBackground(Color.WHITE);
	            }
		
		}
//	 public static void main(String[] args){
//		 new Dlg_SetFault();
//	 }

}
