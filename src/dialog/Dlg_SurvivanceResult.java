package dialog;

import java.awt.Color;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
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
		super(owner,"端口利用率",true);
		if(type == 1){
			this.setTitle("链路单断循环");
			Evaluation.listEvaluation(FiberLink.fiberLinkList);
		}
		else if(type == 2){
			this.setTitle("节点单断循环");
			Evaluation.nodelistEvaluation(CommonNode.allNodeList);
		}

    	this.setSize(1100,600);
    	Toolkit   tl=Toolkit.getDefaultToolkit();   
		Dimension   screenSize=tl.getScreenSize();   
	    this.setLocation((int)screenSize.getWidth()/2-(int)this.getSize().getWidth()/2, (int)screenSize.getHeight()/2-(int)this.getSize().getHeight()/2);
	    
		DefaultTableModel model;
		JTable table;
		final Object[] columnNames = {"业务名称","业务起点","业务终点 ","业务等级","受影响次数","未恢复次数"};
	

		List<Traffic>  traList = new LinkedList<Traffic>();
			traList.addAll(Traffic.trafficList);
		Object data[][] = new Object[traList.size()][7];
		for(int n = 0;n <traList.size();n++){
			data[n][0] = traList.get(n).getFromNode().getName()+"-"+traList.get(n).getToNode().getName();
			data[n][1] = traList.get(n).getFromNode().getName();
			data[n][2] = traList.get(n).getToNode().getName();
			if(traList.get(n).getProtectLevel().equals(TrafficLevel.PERMANENT11))
				data[n][3] = "永久1+1";
			else if(traList.get(n).getProtectLevel().equals(TrafficLevel.NORMAL11))
				data[n][3] = "普通1+1";
			else if(traList.get(n).getProtectLevel().equals(TrafficLevel.RESTORATION))
				data[n][3] = "恢复";	
			else if(traList.get(n).getProtectLevel().equals(TrafficLevel.PROTECTandRESTORATION))
				data[n][3] ="保护+恢复";
			else if(traList.get(n).getProtectLevel().equals(TrafficLevel.NONPROTECT))
				data[n][3] = "无保护";	
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
			   };//设置表内容不可编辑
	    table.getTableHeader().setReorderingAllowed(false); 
	    JPanel pane = new JPanel();
	    JScrollPane pane1 = new JScrollPane (table);
	    pane1.setRowHeaderView(new RowHeaderTable(table,40));  
//	    pane.add(pane1);
//	    pane.setVisible(true);
//        setContentPane(pane);
//     	this.setVisible(true);
     	

		JButton excel = new JButton("关闭");
		Font f2=new Font("微软雅黑", Font.PLAIN, 12);
		excel.setFont(f2);
		excel.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		setVisible(false);
	    		dispose();
//	    		new Dlg_SurvivanceExport(Dlg_SurvivanceResult.this,traList1);
//	    		JOptionPane.showMessageDialog(null,"EXCEL表已导出！");
	
	    	}
	    });
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new   WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				
//				ExcelOut.over("输出.xls");
	    		setVisible(false);
	    		dispose();//释放窗体所占内存资源?
				}	
			
       });
//		MyUtil.makeFace(table);//设置JTable 颜色
		
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
     	System.out.println("全网仿真后业务量"+Traffic.trafficList.size());
	}
	
	
	public Dlg_SurvivanceResult(JFrame owner,int type){
		super(owner,"端口利用率",true);
		if(type == 1){
			this.setTitle("链路单断循环");
			Evaluation.listEvaluation(FiberLink.fiberLinkList);
		}
//		else if(type == 2){
//			this.setTitle("链路多断循环");
//			Evaluation.dLinkListEvaluation(BasicLink.allLinkList);
//		}
		else if(type == 3){
			this.setTitle("节点单循环");
			Evaluation.nodelistEvaluation(CommonNode.allNodeList);
		}
		else if(type == 4){
			this.setTitle("SRLG单断循环");
			Evaluation.SRLGListEvaluation(LinkRGroup.SRLGroupList);
		}

//		else if(type == 4){
//			this.setTitle("链路双断循环");
//			Evaluation.linkPollingII(FiberLink.FiberLinkList, false);
//		}
//		else if(type == 4){
//			this.setTitle("节点多断循环");
//			Evaluation.multiNodeEva(CommonNode.allNodeList);
//		}
//		else if(type == 6){
//			this.setTitle("SRLG组双断循环");
//			Evaluation.srlgPollingII(LinkRGroup.s_lRGroupList, true);
//		}
    	this.setSize(1100,600);
    	Toolkit   tl=Toolkit.getDefaultToolkit();   
		Dimension   screenSize=tl.getScreenSize();   
	    this.setLocation((int)screenSize.getWidth()/2-(int)this.getSize().getWidth()/2, (int)screenSize.getHeight()/2-(int)this.getSize().getHeight()/2);
	    
		DefaultTableModel model;
		JTable table;
		final Object[] columnNames = {"业务名称","业务起点","业务终点 ","业务等级","受影响次数","中断次数"};
	

		List<Traffic>  traList = new LinkedList<Traffic>();

		for(int i=0;i<Traffic.trafficList.size();i++)
				traList.add(Traffic.trafficList.get(i));
		Object data[][] = new Object[traList.size()][7];
		for(int n = 0;n <traList.size();n++){
			data[n][0] = traList.get(n).getFromNode().getName()+"-"+traList.get(n).getToNode().getName();
			data[n][1] = traList.get(n).getFromNode().getName();
			data[n][2] = traList.get(n).getToNode().getName();
			if(traList.get(n).getProtectLevel().equals(TrafficLevel.PERMANENT11))
				data[n][3] = "永久1+1";
			else if(traList.get(n).getProtectLevel().equals(TrafficLevel.NORMAL11))
				data[n][3] = "普通1+1";
			else if(traList.get(n).getProtectLevel().equals(TrafficLevel.RESTORATION))
				data[n][3] = "恢复";
			else if(traList.get(n).getProtectLevel().equals(TrafficLevel.PROTECTandRESTORATION))
				data[n][3] ="保护+恢复";
			else if(traList.get(n).getProtectLevel().equals(TrafficLevel.NONPROTECT))
				data[n][3] = "无保护";
			data[n][4] = traList.get(n).getEffectNum();						   						 
			data[n][5] = traList.get(n).getFailNum();

		}
		
		
		
		traList1 =traList;
	//柱状图
//		Element A=new Node();
//		A.setName("受影响次数");
//		A.putChartColor(Color.red);
//		Element B=new Node();
//		B.setName("未恢复次数");
//		B.putChartColor(Color.blue);
//		for(int i=0;i<traList.size();i++){
//			bar.addXScaleText(traList.get(i).getName());
//			bar.setXScaleTextColor(Color.black);
////			bar.setXScaleTextOrientation(TWaverConst.LABEL_ORIENTATION_VERTICAL);
//			A.addChartValue(traList.get(i).getEffectNum());
//			B.addChartValue(traList.get(i).getFailNum());
//			bar.setBarType(TWaverConst.BAR_TYPE_GROUP);
//		}
//		dataBox.addElement(A);
//		dataBox.addElement(B);
		
		
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
			   };//设置表内容不可编辑
	    table.getTableHeader().setReorderingAllowed(false); 
//	    JLabel zhongduan=new JLabel("中断的业务总数  :");
//	    JLabel jiangji=new JLabel("降级的业务总数  :");
//	    JLabel baochi=new JLabel("保持的业务总数  :");
//	    JLabel num_zhongduan=new JLabel("");
//	    JLabel num_jiangji=new JLabel("");
//	    JLabel num_baochi=new JLabel("");
//	    if(type==1) {
//	    	num_zhongduan=new JLabel(String.valueOf(Evaluation.linkCutoff));
//		    num_jiangji=new JLabel(String.valueOf(Evaluation.linkKeep));
//		    num_baochi=new JLabel(String.valueOf(Evaluation.linkDown));
//	    }
//	    else if(type==3)
//	    {
//	    	 num_zhongduan=new JLabel(String.valueOf(Evaluation.nodeCutoff));
//		     num_jiangji=new JLabel(String.valueOf(Evaluation.nodeKeep));
//		     num_baochi=new JLabel(String.valueOf(Evaluation.nodeDown));
//	    }
//	    else if(type==4)
//	    {
//	    	num_zhongduan=new JLabel(String.valueOf(Evaluation.scutoff));
//		    num_jiangji=new JLabel(String.valueOf(Evaluation.skeep));
//		    num_baochi=new JLabel(String.valueOf(Evaluation.sdown));
//	    }
//	    JLabel kong1=new JLabel();
//	    JLabel kong2=new JLabel();
//	    JLabel kong3=new JLabel();
//	    JPanel jp1=new JPanel();
//	    jp1.setLayout(new GridLayout(3, 3));
//	    jp1.add(zhongduan);
//	    jp1.add(kong1);
//	    jp1.add(num_zhongduan);
//	    jp1.add(jiangji);
//	    jp1.add(kong2);
//	    jp1.add(num_jiangji);
//	    jp1.add(baochi);
//	    jp1.add(kong3);
//	    jp1.add(num_baochi);
//	    Font f = new Font("微软雅黑",1, 15);
//	    Font ff = new Font("微软雅黑",0, 15);
//	    zhongduan.setFont(f);
//	    num_zhongduan.setFont(ff);
//	    jiangji.setFont(f);
//	    num_jiangji.setFont(ff);
//	    baochi.setFont(f);
//	    num_baochi.setFont(ff);
	    JLabel kong1=new JLabel();
	    JLabel kong2=new JLabel();
	    JLabel kong3=new JLabel();
	    JLabel kong4=new JLabel();
	    JLabel zhongduan=new JLabel("中断的业务总数 ");
	    JLabel jiangji=new JLabel("降级的业务总数 ");
	    JLabel baochi=new JLabel("保持的业务总数 ");
	    JButton num_zhongduan = new JButton();
	    JButton num_jiangji= new JButton();
	    JButton num_baochi= new JButton();
	    JPanel jp1=new JPanel();
	    jp1.setLayout(new GridLayout(2, 5));
	    ImageIcon icon =new ImageIcon(this.getClass().getResource("/resource/qiu.png"));
	    if(type==1) {
	    	num_zhongduan=new JButton(String.valueOf(Evaluation.linkCutoff),icon);
		    num_jiangji=new JButton(String.valueOf(Evaluation.linkDown),icon);
		    num_baochi=new JButton(String.valueOf(Evaluation.linkKeep),icon);
		    num_zhongduan.setHorizontalTextPosition(SwingConstants.CENTER);  
		    num_zhongduan.setOpaque(false);//设置控件是否透明，true为不透明，false为透明  
		    num_zhongduan.setContentAreaFilled(false);//设置图片填满按钮所在的区域  
		    num_zhongduan.setMargin(new Insets(0, 0, 0, 0));//设置按钮边框和标签文字之间的距离  
		    num_zhongduan.setFocusPainted(false);//设置这个按钮是不是获得焦点  
		    num_zhongduan.setBorderPainted(false);//设置是否绘制边框  
		    num_zhongduan.setBorder(null);//设置边框  
		    
		    num_jiangji.setHorizontalTextPosition(SwingConstants.CENTER);  
		    num_jiangji.setOpaque(false);//设置控件是否透明，true为不透明，false为透明  
		    num_jiangji.setContentAreaFilled(false);//设置图片填满按钮所在的区域  
		    num_jiangji.setMargin(new Insets(0, 0, 0, 0));//设置按钮边框和标签文字之间的距离  
		    num_jiangji.setFocusPainted(false);//设置这个按钮是不是获得焦点  
		    num_jiangji.setBorderPainted(false);//设置是否绘制边框  
		    num_jiangji.setBorder(null);//设置边框  
		    
		    num_baochi.setHorizontalTextPosition(SwingConstants.CENTER);  
		    num_baochi.setOpaque(false);//设置控件是否透明，true为不透明，false为透明  
		    num_baochi.setContentAreaFilled(false);//设置图片填满按钮所在的区域  
		    num_baochi.setMargin(new Insets(0, 0, 0, 0));//设置按钮边框和标签文字之间的距离  
		    num_baochi.setFocusPainted(false);//设置这个按钮是不是获得焦点  
		    num_baochi.setBorderPainted(false);//设置是否绘制边框  
		    num_baochi.setBorder(null);//设置边框  
	    }
	    else if(type==3)
	    {
	    	 num_zhongduan=new JButton(String.valueOf(Evaluation.nodeCutoff),icon);
		     num_jiangji=new JButton(String.valueOf(Evaluation.nodeDown),icon);
		     num_baochi=new JButton(String.valueOf(Evaluation.nodeKeep),icon);
		     num_zhongduan.setHorizontalTextPosition(SwingConstants.CENTER);  
			    num_zhongduan.setOpaque(false);//设置控件是否透明，true为不透明，false为透明  
			    num_zhongduan.setContentAreaFilled(false);//设置图片填满按钮所在的区域  
			    num_zhongduan.setMargin(new Insets(0, 0, 0, 0));//设置按钮边框和标签文字之间的距离  
			    num_zhongduan.setFocusPainted(false);//设置这个按钮是不是获得焦点  
			    num_zhongduan.setBorderPainted(false);//设置是否绘制边框  
			    num_zhongduan.setBorder(null);//设置边框  
			    
			    num_jiangji.setHorizontalTextPosition(SwingConstants.CENTER);  
			    num_jiangji.setOpaque(false);//设置控件是否透明，true为不透明，false为透明  
			    num_jiangji.setContentAreaFilled(false);//设置图片填满按钮所在的区域  
			    num_jiangji.setMargin(new Insets(0, 0, 0, 0));//设置按钮边框和标签文字之间的距离  
			    num_jiangji.setFocusPainted(false);//设置这个按钮是不是获得焦点  
			    num_jiangji.setBorderPainted(false);//设置是否绘制边框  
			    num_jiangji.setBorder(null);//设置边框  
			    
			    num_baochi.setHorizontalTextPosition(SwingConstants.CENTER);  
			    num_baochi.setOpaque(false);//设置控件是否透明，true为不透明，false为透明  
			    num_baochi.setContentAreaFilled(false);//设置图片填满按钮所在的区域  
			    num_baochi.setMargin(new Insets(0, 0, 0, 0));//设置按钮边框和标签文字之间的距离  
			    num_baochi.setFocusPainted(false);//设置这个按钮是不是获得焦点  
			    num_baochi.setBorderPainted(false);//设置是否绘制边框  
			    num_baochi.setBorder(null);//设置边框  
	    }
	    else if(type==4)
	    {
	    	num_zhongduan=new JButton(String.valueOf(Evaluation.scutoff),icon);
		    num_jiangji=new JButton(String.valueOf(Evaluation.sdown),icon);
		    num_baochi=new JButton(String.valueOf(Evaluation.skeep),icon);
		    num_zhongduan.setHorizontalTextPosition(SwingConstants.CENTER);  
		    num_zhongduan.setOpaque(false);//设置控件是否透明，true为不透明，false为透明  
		    num_zhongduan.setContentAreaFilled(false);//设置图片填满按钮所在的区域  
		    num_zhongduan.setMargin(new Insets(0, 0, 0, 0));//设置按钮边框和标签文字之间的距离  
		    num_zhongduan.setFocusPainted(false);//设置这个按钮是不是获得焦点  
		    num_zhongduan.setBorderPainted(false);//设置是否绘制边框  
		    num_zhongduan.setBorder(null);//设置边框  
		    
		    num_jiangji.setHorizontalTextPosition(SwingConstants.CENTER);  
		    num_jiangji.setOpaque(false);//设置控件是否透明，true为不透明，false为透明  
		    num_jiangji.setContentAreaFilled(false);//设置图片填满按钮所在的区域  
		    num_jiangji.setMargin(new Insets(0, 0, 0, 0));//设置按钮边框和标签文字之间的距离  
		    num_jiangji.setFocusPainted(false);//设置这个按钮是不是获得焦点  
		    num_jiangji.setBorderPainted(false);//设置是否绘制边框  
		    num_jiangji.setBorder(null);//设置边框  
		    
		    num_baochi.setHorizontalTextPosition(SwingConstants.CENTER);  
		    num_baochi.setOpaque(false);//设置控件是否透明，true为不透明，false为透明  
		    num_baochi.setContentAreaFilled(false);//设置图片填满按钮所在的区域  
		    num_baochi.setMargin(new Insets(0, 0, 0, 0));//设置按钮边框和标签文字之间的距离  
		    num_baochi.setFocusPainted(false);//设置这个按钮是不是获得焦点  
		    num_baochi.setBorderPainted(false);//设置是否绘制边框  
		    num_baochi.setBorder(null);//设置边框  
	    }
	    jp1.add(zhongduan);
	    jp1.add(kong1);
	    jp1.add(jiangji);
	    jp1.add(kong2);
	    jp1.add(baochi);
	    jp1.add(num_zhongduan);
	    jp1.add(kong3);
	    jp1.add(num_jiangji);
	    jp1.add(kong4);
	    jp1.add(num_baochi);
	    Font ff = new Font("微软雅黑",0, 12);
	    zhongduan.setFont(ff);
	    num_zhongduan.setFont(ff);
	    jiangji.setFont(ff);
	    num_jiangji.setFont(ff);
	    baochi.setFont(ff);
	    num_baochi.setFont(ff);
	    
		JButton excel = new JButton("关闭");
		Font f1=new Font("微软雅黑", Font.PLAIN, 12);
		excel.setFont(f1);
		excel.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		setVisible(false);
	    		dispose();
//	    		new Dlg_SurvivanceExport(Dlg_SurvivanceResult.this,traList1);
//	    		JOptionPane.showMessageDialog(null,"EXCEL表已导出！");
	
	    	}
	    });
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		addWindowListener(new   WindowAdapter(){
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				
//				ExcelOut.over("输出.xls");
	    		setVisible(false);
	    		dispose();//释放窗体所占内存资源?
				}	
			
       });
//		MyUtil.makeFace(table);//设置JTable 颜色
		JScrollPane j1=new JScrollPane (table);
		j1.setSize(new Dimension(800, 350));
		Insets insert;
     	GridBagLayout mg = new GridBagLayout();
		GridBagConstraints mgc = new GridBagConstraints();
		this.setLayout(mg);
		mgc.fill = GridBagConstraints.BOTH;
		mgc.gridwidth =  GridBagConstraints.REMAINDER;
		insert = new Insets(10,0,10,0);
		mgc.insets=insert;
		mgc.weightx = 1.0;
		mgc.gridheight = 1;
		mgc.weighty = 30.0;
		this.add(j1,mgc);
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		insert = new Insets(10,0,10,0);
		mgc.insets=insert;
		mgc.weightx = 1.0;
		mgc.gridheight = 1;
		mgc.weighty = 3.0;
		mgc.fill = GridBagConstraints.NONE; 
		this.add(jp1,mgc);
		

		
		mgc.fill = GridBagConstraints.NONE;
		mgc.gridwidth = GridBagConstraints.REMAINDER;
		mgc.gridheight=1;
		mgc.weighty=1;
		insert=new Insets(10, 0, 10, 0);
		mgc.insets=insert;
		this.add(excel,mgc);
		
		this.setSize(830,500);
		this.setVisible(true);
     	System.out.println("全网仿真后业务量"+Traffic.trafficList.size());
	}

}
