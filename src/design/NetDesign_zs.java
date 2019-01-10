package design;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.plaf.MenuItemUI;
import javax.swing.plaf.PopupMenuUI;
import javax.swing.plaf.basic.BasicMenuUI;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import NSUI.NSMenuItemUI;
import NSUI.NSMenuUI;
import NSUI.NSPopupMenuUI;
import UIGradient.GradientMenubar;
import UIGradient.GradientPanel;
import UIGradient.GradientToolbar;
import UIGradient.MySplitPaneUI;
import algorithm.OSNR;
import data.BasicLink;
import data.CommonNode;
import data.DataSave;
import data.FiberLink;
import data.LinkRGroup;
import data.Network;
import data.Route;
import data.Traffic;
import data.WDMLink;
import data.WaveLength;
import dataControl.LinkData;
import dataControl.NodeData;
import dataControl.TrafficData;
import database.Database;
import database.NodeDataBase;
import database.TrafficDatabase;
import datastructure.UIFiberLink;
import datastructure.UINode;
import datastructure.UIOTNLink;
import datastructure.UIWDMLink;
import dialog.AFSetting;
import dialog.BackToBackCoefficient;
import dialog.Dlg_AddFiberlink;
import dialog.Dlg_AddOLANode;
import dialog.Dlg_AddROADMNode;
import dialog.Dlg_AddTraffic;
import dialog.Dlg_BussinessPlan;
import dialog.Dlg_DesignResult;
import dialog.Dlg_InputAll;
import dialog.Dlg_InputLinks;
import dialog.Dlg_InputNodes;
import dialog.Dlg_LinkPoll;
import dialog.Dlg_NewProject;
import dialog.Dlg_NodePoll;
import dialog.Dlg_OpenProject;
import dialog.Dlg_PolicySetting;
import dialog.Dlg_ProjectSaveAs;
import dialog.Dlg_RelatedTraffic;
import dialog.Dlg_SRLG;
import dialog.Dlg_SRLGSet;
import dialog.Dlg_SetFault;
import dialog.Dlg_TrafficInput;
import dialog.Dlg_fiberresult;
import dialog.Dlg_noderesult;
import dialog.Dlg_wdmresult;
import dialog.PropertyDisplay;
import dialog.TestProgressBar;
import dialog.designFiberlink;
import dialog.designROADMnode;
import enums.NodeType;
import enums.Status;
import netdesigntitle.NSTitleFrame;
import survivance.Evaluation;
import twaver.DataBoxAdapter;
import twaver.DataBoxEvent;
import twaver.Dummy;
import twaver.Element;
import twaver.GeoCoordinate;
import twaver.PopupMenuGenerator;
import twaver.ResizableNode;
import twaver.TDataBox;
import twaver.TView;
import twaver.VisibleFilter;
import twaver.gis.GeographyMap;
import twaver.gis.GisManager;
import twaver.gis.GisNetworkAdapter;
import twaver.gis.TWaverGisConst;
import twaver.gis.gadget.Navigator;
import twaver.gis.gadget.StatusBar;
import twaver.network.TNetwork;
import twaver.tree.TTree;

public class NetDesign_zs extends NSTitleFrame {
	
	public static int processValue;
	public static List<Integer> process;
	
	public static String filenameall; // 全名
	public static String filename; // 工程名
	public static String filepath; // 工程路径
	public static String fileinfo; // 工程信息
	public static String openfilelist = "nothing"; // 打开文件位置
	public static String openproject = "nothing"; // 打开工程位置
	private JPanel panel = new JPanel(new BorderLayout());
	private static TDataBox box1 = new TDataBox();
	public static TNetwork network;
	private TTree tree;
	private String filelist = "wrong";
	static public JTextArea ta;
	JLabel jbc1=new JLabel("                             ");
	JLabel jbc2=new JLabel("  ");
	JLabel jbc3=new JLabel("  ");
	JLabel jbc4=new JLabel("  ");
	public static JRadioButton buttonCreateLink=new JRadioButton("");
    public static JRadioButton buttonCreateTraffic=new JRadioButton("");
    public static ButtonGroup buttonGroup= new ButtonGroup();
  
	StatusBar status;
	Color backColor = new Color(98, 147, 199);
	private GradientToolbar toolbar = new GradientToolbar("Still draggable", backColor, backColor); // 工具栏
	public static GeographyMap map;
	protected Navigator navigator;
	public static JCheckBox[] nodedisplay = new JCheckBox[3];
	private static JCheckBox[] linklayer = new JCheckBox[2];
	private List<UINode> theUINodelist = new LinkedList<UINode>();

	public GradientToolbar getToolbar() {
		return toolbar;
	}

	public void setToolbar(GradientToolbar toolbar) {
		this.toolbar = toolbar;
	}

	private static Dummy nodeDummy;
//	private static Dummy zjnodeDummy;
//	private static Dummy fzjnodeDummy;
	private static Dummy FiberLinkDummy;
	private static Dummy WDMLinkDummy;// wdm
	private static Dummy EleLinkDummy;
	private static Dummy TrafficDummy;

	public Dummy getTrafficDummy() {
		return TrafficDummy;
	}

	public void setTrafficDummy(Dummy trafficDummy) {
		TrafficDummy = trafficDummy;
	}

	public Dummy getLinkDummy() {
		return FiberLinkDummy;
	}

	public void setLinkDummy(Dummy linkDummy) {
		this.FiberLinkDummy = linkDummy;
	}

	public Dummy getNodeDummy() {
		return nodeDummy;
	}

	public void setNodeDummy(Dummy nodeDummy) {
		this.nodeDummy = nodeDummy;
	}

//	public Dummy getZjnodeDummy() {
//		return zjnodeDummy;
//	}
//
//	public void setZjnodeDummy(Dummy zjnodeDummy) {
//		this.zjnodeDummy = zjnodeDummy;//	}
//
//	public Dummy getFzjnodeDummy() {
//		return fzjnodeDummy;
//	}
//
//	public void setFzjnodeDummy(Dummy fzjnodeDummy) {
//		this.fzjnodeDummy = fzjnodeDummy;
//	}

	public Dummy getOptLinkDummy() {
		return WDMLinkDummy;
	}

	public void setOptLinkDummy(Dummy OptLinkDummy) {
		this.WDMLinkDummy = OptLinkDummy;
	}

	public Dummy getEleLinkDummy() {
		return EleLinkDummy;
	}

	public void setEleLinkDummy(Dummy EleLinkDummy) {
		this.EleLinkDummy = EleLinkDummy;
	}

	public TNetwork getNetwork() {
		return network;
	}

	public void setNetwork(TNetwork network) {
		this.network = network;
	}

	public TDataBox getBox1() {
		return box1;
	}

	public void setBox1(TDataBox box1) {
		this.box1 = box1;
	}

	private long targetTimestamp = Long.MAX_VALUE;
	private long hoverTimestamp;
	private Element hoverElement;
	private static final long POPUP_DELAY = 500;
	protected static final Traffic Traffic = null;
	private boolean popupVisible;
	private  ImageIcon creatLink = new ImageIcon(getClass().getResource("/resource/link_icon.png"));
	private  ImageIcon creatTraffic = new ImageIcon(getClass().getResource("/resource/newtraffic.png"));
	private boolean CreatLinkcounter = true;
	private boolean CreatBusinesscounter = true;

	public boolean isCreatBusinesscounter() {
		return CreatBusinesscounter;
	}

	public void setCreatBusinesscounter(boolean creatBusinesscounter) {
		CreatBusinesscounter = creatBusinesscounter;
	}

	public boolean isCreatBusinesscounter1() {
		return CreatBusinesscounter;
	}

	public void setCreatBusinesscounter1(boolean creatBusinesscounter) {
		CreatBusinesscounter = creatBusinesscounter;
	}

	public boolean isCreatLinkcounter() {
		return CreatLinkcounter;
	}

	public void setCreatLinkcounter(boolean creatLinkcounter) {
		CreatLinkcounter = creatLinkcounter;
	}
	//// 添加创建链路和创建业务的按钮在工具栏上
	//////////////////////////////////////////////////// start

	int sourcexx, sourceyy;// 源节点的纵横坐标

	public int getSourcexx() {
		return sourcexx;
	}

	public void setSourcexx(int sourcexx) {
		this.sourcexx = sourcexx;
	}

	public int getSourceyy() {
		return sourceyy;
	}

	public void setSourceyy(int sourceyy) {
		this.sourceyy = sourceyy;
	}

	MouseListener ListenerLink;
	MouseListener ListenerBusiness;

	////////////////////////////////////////////////////////////// end
	static JComboBox layer;
	NetDesign_zs dt = this; // 主窗口对象的一个别名 用来在一些函数中实现

	public static long nowtime;
	Color menuColor1 = new Color(246, 248, 252);
	Color menuColor2 = new Color(212, 219, 238);
	Color menuColor3 = new Color(222, 227, 243);
	GradientMenubar menubar = new GradientMenubar(menuColor1, menuColor2, menuColor3);

	// 主菜单
	// 工程创建
	JMenu projectDesign = new JMenu("工程创建 ");
	JMenuItem menuItemNew = new JMenuItem(" 新建  ");
	JMenuItem menuItemOpen = new JMenuItem(" 打开  ");
	JMenuItem menuItemSave = new JMenuItem(" 保存  ");
	JMenuItem menuItemSaveas = new JMenuItem(" 另存为  ");
	JMenuItem menuItemproperty = new JMenuItem(" 工程信息  ");
	JMenuItem menuItemExit = new JMenuItem(" 退出  ");
	// 物理拓扑及业务创建
//	JMenu businessDesign = new JMenu("物理拓扑及业务创建 ");
	JMenu networkModelCreat = new JMenu("网络模型创建");
	JMenu businessCreat = new JMenu("业务创建");
	JMenuItem siteimport = new JMenuItem(" 站点站型导入  ");
	JMenuItem fiberimport = new JMenuItem(" 光纤参数导入  ");
	JMenuItem AllInput = new JMenuItem(" 导入节点和链路  ");
//	JMenuItem WDMimport = new JMenuItem(" 波分导入  ");
//	JMenuItem OTNimport = new JMenuItem(" OTN导入  ");
	JMenuItem businessimport = new JMenuItem(" 业务导入  ");
	JMenuItem resourceclear = new JMenuItem(" 资源清空  ");
	// 参数设置
	JMenu parameterSetting = new JMenu("参数设置 ");
	JMenuItem NFsetting = new JMenuItem(" 放大噪声系数AF设置  ");
	JMenuItem OSNRSetting = new JMenuItem("OSNR参数设置");
	//JMenuItem OSNRsetting = new JMenuItem(" OSNR容量设置  ");
	JMenuItem overOSNRsetting = new JMenuItem(" 背靠背指标及过系统OSNR阈量设定  ");
//	JMenuItem amplifiersetting = new JMenuItem(" 放大器入纤光功率设置  ");
//	JMenuItem attenuatorsetting = new JMenuItem(" 线路侧可调衰减器插损设置  ");
//	JMenuItem OSCsetting = new JMenuItem(" OSC合分波器插损设置  ");
	//JMenuItem capacitysetting = new JMenuItem(" 系统容量设置  ");
	//JMenuItem Wavelengthsetting = new JMenuItem(" 可用波长设置  ");
	// 网络管理
//	JMenu networkManagement = new JMenu("网络管理 ");
	JMenu resourcemanagement = new JMenu(" 资源管理  ");//有二级菜单
	JMenuItem nodemanagement = new JMenuItem(" 节点管理  ");
	JMenuItem fibermanagement = new JMenuItem(" Fiber管理  ");
	JMenuItem wdmmanagement = new JMenuItem(" WDM管理  ");
	JMenuItem businessmanagement = new JMenuItem(" 业务路由管理  ");
	JMenuItem SRLGmanagement = new JMenuItem(" SRLG管理  ");
	JMenuItem trafficgroup = new JMenuItem(" 共享风险业务组管理  ");
	// 网络规划
	JMenu networkDesign = new JMenu("网络规划 ");
	JMenuItem networkDesignguide = new JMenuItem(" 路由策略  ");
	JMenuItem bussinessPlan = new JMenuItem(" 业务规划  ");
//	JMenuItem Algorithmused = new JMenuItem(" 规划算法调用 ");
	// 故障模拟
	JMenu faultSimulation = new JMenu("网络仿真 ");
	JMenuItem Nodesingle = new JMenuItem(" 节点单断仿真  ");
	JMenuItem Linksingle = new JMenuItem(" 链路单断仿真  ");
//	JMenuItem SRLGsingle = new JMenuItem(" SRLG组单断仿真  ");
	JMenuItem Artificialsetting = new JMenuItem(" 人工故障设置仿真  ");
//	JMenuItem nodeArtificialsetting = new JMenuItem(" 节点故障设置仿真  ");
//	JMenuItem linkArtificialsetting = new JMenuItem(" 链路故障设置仿真  ");
	// 报表输出
	JMenu reportOutput = new JMenu("报表输出 ");
	JMenuItem nodeoutput = new JMenuItem(" 节点配置表  ");
	JMenuItem traficoutput = new JMenuItem(" 工作路由表  ");
	JMenu guzhangout=new JMenu("故障模拟报表输出");
	JMenuItem nodeout = new JMenuItem(" 节点单断循环 ");
	JMenuItem linkout = new JMenuItem(" 链路单断循环  ");
	
	// 链路资源利用率
	//JMenu LinkUtilization = new JMenu("链路资源利用率 ");
	// SRLG自动映射
	JMenu SRLGyingshe = new JMenu("SRLG自动映射");
	JMenuItem SRLGzidongyingshe = new JMenuItem(" SRLG自动映射  ");

	// 分割窗口
	private JPanel networkPane = new JPanel(new BorderLayout());
	private JPanel treePane = new JPanel(new BorderLayout());
	private JPanel menuPane = new JPanel(new BorderLayout());
	private JPanel fencengPane = new JPanel(new BorderLayout());
	private JPanel TtreePane = new JPanel(new BorderLayout());
//	private JPanel xsPane2 = new JPanel(new BorderLayout());
//	private JPanel xsPane = new JPanel(new BorderLayout());
	private JSplitPane splitWhole = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treePane, networkPane);

	// 分割窗口
	public NetDesign_zs(int type) {
		super();
		toolbar();// 工具栏
		accessMap();// 拓扑平面
		installHoverTimer();
		menubar();// 菜单栏
		myGUI();// 事件监听
		popup();// 右键响应函数

		nodeDummy = new Dummy();
//		zjnodeDummy = new Dummy();
//		fzjnodeDummy = new Dummy();
		nodeDummy.setName(" 节点");
//		zjnodeDummy.setName("ROADM节点");
//		fzjnodeDummy.setName("OLA节点");
		FiberLinkDummy = new Dummy();
		FiberLinkDummy.setName("FIBER");
		WDMLinkDummy = new Dummy();
		WDMLinkDummy.setName("WDM");
		EleLinkDummy = new Dummy();
		EleLinkDummy.setName("OTN");
		TrafficDummy = new Dummy();
		TrafficDummy.setName("业务");
		box1.addElement(nodeDummy);
		// box1.addElement(zjnodeDummy);
		// box1.addElement(fzjnodeDummy);
		box1.addElement(FiberLinkDummy);
		 box1.addElement(WDMLinkDummy);
		// box1.addElement(EleLinkDummy);
		box1.addElement(TrafficDummy);
		box1.setName("数据库");
		// treePane
		tree = new TTree();
		tree.setDataBox(box1);
		tree.setBackground(new Color(247, 252, 255));
		JScrollPane jsp = new JScrollPane(tree);
		jsp.setBackground(backColor);
		TtreePane.add(jsp);
		treePane.add(TtreePane, BorderLayout.CENTER);
		fencengPane.setLayout(new GridLayout(1, 3));
		fencengPane.add(linklayer[0]);
		fencengPane.add(linklayer[1]);
		//fencengPane.add(linklayer[2]);
		fencengPane.setBackground(new Color(247, 252, 255));
		treePane.add(fencengPane, BorderLayout.SOUTH);

//		// JtabbedPane
//		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
//		tabbedPane.addTab("树图显示", new ImageIcon(ClassLoader.getSystemResource("resource/SRLG.png")), treePane);
//		tabbedPane.addTab("显示3", new ImageIcon(ClassLoader.getSystemResource("resource/SRLG.png")), xsPane2);
//		xsPane.add(tabbedPane);

		Color top = new Color(255, 255, 255);
		Color bottom = new Color(255, 255, 255);
		new GradientPanel(top, bottom);

		networkPane.add(network, BorderLayout.CENTER);

		status = new StatusBar(map, network.getCanvas()); // 地图显示下面的状态栏
		Toolkit tl = Toolkit.getDefaultToolkit();
		Dimension screenSize = tl.getScreenSize();

		splitWhole.setDividerSize(1);
		splitWhole.setDividerLocation(230);
		splitWhole.setUI(new MySplitPaneUI());
		// networkPane.add(toolbar, BorderLayout.SOUTH);
		panel.add(menubar, BorderLayout.NORTH);// 加menu
		this.setContentPane(panel);
		panel.add(splitWhole);
		network.addVisibleFilter(new VisibleFilter() {
			public boolean isVisible(Element e) {
				// TODO Auto-generated method stub
				if (e.getClass().equals(UIFiberLink.class)) {
					return true;
//				} else if (e.getClass().equals(UIOTNLink.class)) {
//					return true;
				} else if (e.getClass().equals(UIWDMLink.class)) {
					return true;
				} else if (e.getClass().equals(UINode.class)) {
					return true;
				} else {
					return false;
				}
			}
		});

	}

	public JPanel getnetworkpane() {
		return networkPane;
	}

	// 菜单设置
	public void menubar() // 菜单栏
	{

		Color menufore = new Color(0, 0, 0);// 是字体的颜色
		menubar.add(projectDesign);// 工程管理
//		menubar.add(businessDesign);// 物理拓扑及业务创建
		menubar.add(networkModelCreat);
		menubar.add(businessCreat);
//		menubar.add(parameterSetting);// 参数设置
//		menubar.add(networkManagement);// 网络管理
		menubar.add(networkDesign);// 网络规划
		menubar.add(faultSimulation);// 故障模拟
		menubar.add(reportOutput);// 报表输出
//		menubar.add(LinkUtilization);// 链路资源利用率
//		menubar.add(SRLGyingshe);// SRLG自动映射
		// 工程管理
		projectDesign.setUI((BasicMenuUI) NSMenuUI.createUI(projectDesign));
		projectDesign.getPopupMenu().setUI((PopupMenuUI) NSPopupMenuUI.createUI(projectDesign.getPopupMenu()));
		projectDesign.setFont(new java.awt.Font("微软雅黑", Font.PLAIN, 13));
		projectDesign.setForeground(menufore);
		projectDesign.add(menuItemNew);
		menuItemNew.setUI((MenuItemUI) NSMenuItemUI.createUI(menuItemNew));
		projectDesign.add(menuItemOpen);
		menuItemOpen.setUI((MenuItemUI) NSMenuItemUI.createUI(menuItemOpen));
		projectDesign.add(menuItemSave);
		menuItemSave.setUI((MenuItemUI) NSMenuItemUI.createUI(menuItemSave));
		projectDesign.add(menuItemSaveas);
		menuItemSaveas.setUI((MenuItemUI) NSMenuItemUI.createUI(menuItemSaveas));
		// projectDesign.add(menuItemproperty);
		menuItemproperty.setUI((MenuItemUI) NSMenuItemUI.createUI(menuItemproperty));
		projectDesign.add(menuItemExit);
		menuItemExit.setUI((MenuItemUI) NSMenuItemUI.createUI(menuItemExit));
		// 网络模型创建
		networkModelCreat.setUI((BasicMenuUI) NSMenuUI.createUI(networkModelCreat));
		networkModelCreat.getPopupMenu().setUI((PopupMenuUI) NSPopupMenuUI.createUI(networkModelCreat.getPopupMenu()));
		networkModelCreat.setFont(new java.awt.Font("微软雅黑", Font.PLAIN, 13));
		networkModelCreat.setForeground(menufore);
		networkModelCreat.add(siteimport);
		siteimport.setUI((MenuItemUI) NSMenuItemUI.createUI(siteimport));
		networkModelCreat.add(fiberimport);
		fiberimport.setUI((MenuItemUI) NSMenuItemUI.createUI(fiberimport));
		networkModelCreat.add(AllInput);
	    AllInput.setUI((MenuItemUI)NSMenuItemUI.createUI(AllInput));
	    networkModelCreat.add(resourcemanagement);
		resourcemanagement.setUI((BasicMenuUI) NSMenuUI.createUI(resourcemanagement));
		resourcemanagement.getPopupMenu().setUI(
				(PopupMenuUI) NSPopupMenuUI.createUI(resourcemanagement.getPopupMenu()));
		nodemanagement.setUI((MenuItemUI) NSMenuItemUI.createUI(nodemanagement));
		resourcemanagement.add(nodemanagement);
		fibermanagement.setUI((MenuItemUI) NSMenuItemUI.createUI(fibermanagement));
		resourcemanagement.add(fibermanagement);
		wdmmanagement.setUI((MenuItemUI) NSMenuItemUI.createUI(wdmmanagement));
		resourcemanagement.add(wdmmanagement);
		networkModelCreat.add(SRLGmanagement);
		SRLGmanagement.setUI((MenuItemUI) NSMenuItemUI.createUI(SRLGmanagement));
        //业务创建
	    businessCreat.setUI((BasicMenuUI) NSMenuUI.createUI(businessCreat));
	    businessCreat.getPopupMenu().setUI((PopupMenuUI) NSPopupMenuUI.createUI(businessCreat.getPopupMenu()));
	    businessCreat.setFont(new java.awt.Font("微软雅黑", Font.PLAIN, 13));
	    businessCreat.setForeground(menufore);
	    businessCreat.add(businessimport);
		businessimport.setUI((MenuItemUI) NSMenuItemUI.createUI(businessimport));
		businessCreat.add(resourceclear);
		resourceclear.setUI((MenuItemUI) NSMenuItemUI.createUI(resourceclear));
		businessCreat.add(trafficgroup);
		trafficgroup.setUI((MenuItemUI) NSMenuItemUI.createUI(trafficgroup));
//		businessDesign.setUI((BasicMenuUI) NSMenuUI.createUI(businessDesign));
//		businessDesign.getPopupMenu().setUI((PopupMenuUI) NSPopupMenuUI.createUI(businessDesign.getPopupMenu()));
//		businessDesign.setFont(new java.awt.Font("微软雅黑", Font.PLAIN, 13));
//		businessDesign.setForeground(menufore);
//		businessDesign.add(siteimport);
//		siteimport.setUI((MenuItemUI) NSMenuItemUI.createUI(siteimport));
//		businessDesign.add(fiberimport);
//		fiberimport.setUI((MenuItemUI) NSMenuItemUI.createUI(fiberimport));
//		businessDesign.add(AllInput);
//	    AllInput.setUI((MenuItemUI)NSMenuItemUI.createUI(AllInput));
//		businessDesign.add(WDMimport);
//		WDMimport.setUI((MenuItemUI) NSMenuItemUI.createUI(WDMimport));
//		businessDesign.add(OTNimport);
//		OTNimport.setUI((MenuItemUI) NSMenuItemUI.createUI(OTNimport));
//		businessDesign.add(businessimport);
//		businessimport.setUI((MenuItemUI) NSMenuItemUI.createUI(businessimport));
//		businessDesign.add(resourceclear);
//		resourceclear.setUI((MenuItemUI) NSMenuItemUI.createUI(resourceclear));
		// 参数设置
//		parameterSetting.setUI((BasicMenuUI) NSMenuUI.createUI(parameterSetting));
//		parameterSetting.getPopupMenu().setUI((PopupMenuUI) NSPopupMenuUI.createUI(parameterSetting.getPopupMenu()));
//		parameterSetting.setFont(new java.awt.Font("微软雅黑", Font.PLAIN, 13));
//		parameterSetting.setForeground(menufore);
//		parameterSetting.add(NFsetting);
//		NFsetting.setUI((MenuItemUI) NSMenuItemUI.createUI(NFsetting));
//		parameterSetting.add(OSNRSetting);
//		OSNRSetting.setUI((MenuItemUI) NSMenuItemUI.createUI(OSNRSetting));
//		parameterSetting.add(OSNRsetting);
//		OSNRsetting.setUI((MenuItemUI) NSMenuItemUI.createUI(OSNRsetting));
//		parameterSetting.add(overOSNRsetting);
//		overOSNRsetting.setUI((MenuItemUI) NSMenuItemUI.createUI(overOSNRsetting));
//		parameterSetting.add(amplifiersetting);
//		amplifiersetting.setUI((MenuItemUI) NSMenuItemUI.createUI(amplifiersetting));
//		parameterSetting.add(attenuatorsetting);
//		attenuatorsetting.setUI((MenuItemUI) NSMenuItemUI.createUI(attenuatorsetting));
//		parameterSetting.add(OSCsetting);
//		OSCsetting.setUI((MenuItemUI) NSMenuItemUI.createUI(OSCsetting));
//		parameterSetting.add(capacitysetting);
//		capacitysetting.setUI((MenuItemUI) NSMenuItemUI.createUI(capacitysetting));
//		parameterSetting.add(Wavelengthsetting);
//		Wavelengthsetting.setUI((MenuItemUI) NSMenuItemUI.createUI(Wavelengthsetting));
		// 网络管理
//		networkManagement.setUI((BasicMenuUI) NSMenuUI.createUI(networkManagement));
//		networkManagement.getPopupMenu().setUI((PopupMenuUI) NSPopupMenuUI.createUI(networkManagement.getPopupMenu()));
//		networkManagement.setFont(new java.awt.Font("微软雅黑", Font.PLAIN, 13));
//		networkManagement.setForeground(menufore);
//		networkManagement.add(resourcemanagement);
//		resourcemanagement.setUI((BasicMenuUI) NSMenuUI.createUI(resourcemanagement));
//		resourcemanagement.getPopupMenu().setUI(
//				(PopupMenuUI) NSPopupMenuUI.createUI(resourcemanagement.getPopupMenu()));
//		networkManagement.add(businessmanagement);
//		nodemanagement.setUI((MenuItemUI) NSMenuItemUI.createUI(nodemanagement));
//		resourcemanagement.add(nodemanagement);
//		fibermanagement.setUI((MenuItemUI) NSMenuItemUI.createUI(fibermanagement));
//		resourcemanagement.add(fibermanagement);
//		wdmmanagement.setUI((MenuItemUI) NSMenuItemUI.createUI(wdmmanagement));
//		resourcemanagement.add(wdmmanagement);
//		
//		networkManagement.add(businessmanagement);
//		businessmanagement.setUI((MenuItemUI) NSMenuItemUI.createUI(businessmanagement));
//		networkManagement.add(SRLGmanagement);
//		SRLGmanagement.setUI((MenuItemUI) NSMenuItemUI.createUI(SRLGmanagement));
//		networkManagement.add(trafficgroup);
//		trafficgroup.setUI((MenuItemUI) NSMenuItemUI.createUI(trafficgroup));
		// 网络规划
		networkDesign.setUI((BasicMenuUI) NSMenuUI.createUI(networkDesign));
		networkDesign.getPopupMenu().setUI((PopupMenuUI) NSPopupMenuUI.createUI(networkDesign.getPopupMenu()));
		networkDesign.setFont(new java.awt.Font("微软雅黑", Font.PLAIN, 13));
		networkDesign.setForeground(menufore);
		
		networkDesign.add(parameterSetting);
		parameterSetting.setUI((BasicMenuUI) NSMenuUI.createUI(parameterSetting));
		parameterSetting.getPopupMenu().setUI(
				(PopupMenuUI) NSPopupMenuUI.createUI(parameterSetting.getPopupMenu()));
		parameterSetting.add(NFsetting);
		NFsetting.setUI((MenuItemUI) NSMenuItemUI.createUI(NFsetting));
		parameterSetting.add(OSNRSetting);
		OSNRSetting.setUI((MenuItemUI) NSMenuItemUI.createUI(OSNRSetting));
		parameterSetting.add(overOSNRsetting);
		overOSNRsetting.setUI((MenuItemUI) NSMenuItemUI.createUI(overOSNRsetting));
		
		 
			
		networkDesign.add(networkDesignguide);
		networkDesignguide.setUI((MenuItemUI) NSMenuItemUI.createUI(networkDesignguide));
//		networkDesignguide.getPopupMenu().setUI(
//				(PopupMenuUI) NSPopupMenuUI.createUI(networkDesignguide.getPopupMenu()));
//		networkDesignguide.add(Algorithmused);
//		Algorithmused.setUI((MenuItemUI) NSMenuItemUI.createUI(Algorithmused));
		networkDesign.add(businessmanagement);
		businessmanagement.setUI((MenuItemUI) NSMenuItemUI.createUI(businessmanagement));
		networkDesign.add(bussinessPlan);
		bussinessPlan.setUI((MenuItemUI) NSMenuItemUI.createUI(bussinessPlan));
		// 网络仿真
		faultSimulation.setUI((BasicMenuUI) NSMenuUI.createUI(faultSimulation));
		faultSimulation.getPopupMenu().setUI((PopupMenuUI) NSPopupMenuUI.createUI(faultSimulation.getPopupMenu()));
		faultSimulation.setFont(new java.awt.Font("微软雅黑", Font.PLAIN, 13));
		faultSimulation.setForeground(menufore);
		faultSimulation.add(Nodesingle);
		Nodesingle.setUI((MenuItemUI) NSMenuItemUI.createUI(Nodesingle));
		faultSimulation.add(Linksingle);
		Linksingle.setUI((MenuItemUI) NSMenuItemUI.createUI(Linksingle));
//		faultSimulation.add(SRLGsingle);
//		SRLGsingle.setUI((MenuItemUI) NSMenuItemUI.createUI(SRLGsingle));
		faultSimulation.add(Artificialsetting);
		Artificialsetting.setUI((MenuItemUI) NSMenuItemUI.createUI(Artificialsetting));
//		Artificialsetting.add(nodeArtificialsetting);
//		nodeArtificialsetting.setUI((MenuItemUI) NSMenuItemUI.createUI(nodeArtificialsetting));
//		Artificialsetting.add(linkArtificialsetting);
//		linkArtificialsetting.setUI((MenuItemUI) NSMenuItemUI.createUI(linkArtificialsetting));
		// 报表输出
		reportOutput.setUI((BasicMenuUI) NSMenuUI.createUI(reportOutput));
		reportOutput.getPopupMenu().setUI((PopupMenuUI) NSPopupMenuUI.createUI(reportOutput.getPopupMenu()));
		reportOutput.setFont(new java.awt.Font("微软雅黑", Font.PLAIN, 13));
		reportOutput.setForeground(menufore);
		reportOutput.add(nodeoutput);
		nodeoutput.setUI((MenuItemUI) NSMenuItemUI.createUI(nodeoutput));
		reportOutput.add(traficoutput);
		traficoutput.setUI((MenuItemUI) NSMenuItemUI.createUI(traficoutput));
		reportOutput.add(guzhangout);
		guzhangout.setUI((BasicMenuUI) NSMenuUI.createUI(guzhangout));
		guzhangout.getPopupMenu().setUI((PopupMenuUI) NSPopupMenuUI.createUI(guzhangout.getPopupMenu()));
		
		guzhangout.add(nodeout);
		nodeout.setUI((MenuItemUI) NSMenuItemUI.createUI(nodeout));
		
		guzhangout.add(linkout);
		linkout.setUI((MenuItemUI) NSMenuItemUI.createUI(linkout));
		
		
//		// 链路资源利用率
//		LinkUtilization.setUI((BasicMenuUI) NSMenuUI.createUI(LinkUtilization));
//		LinkUtilization.getPopupMenu().setUI((PopupMenuUI) NSPopupMenuUI.createUI(LinkUtilization.getPopupMenu()));
//		LinkUtilization.setFont(new java.awt.Font("微软雅黑", Font.PLAIN, 13));
//		LinkUtilization.setForeground(menufore);
		
		// SRLG自动映射
		SRLGyingshe.removeAll();
		SRLGyingshe.setUI((BasicMenuUI) NSMenuUI.createUI(SRLGyingshe));
		SRLGyingshe.getPopupMenu().setUI((PopupMenuUI) NSPopupMenuUI.createUI(SRLGyingshe.getPopupMenu()));
		SRLGyingshe.setFont(new java.awt.Font("微软雅黑", Font.PLAIN, 13));
		SRLGyingshe.setForeground(menufore);
		SRLGyingshe.add(SRLGzidongyingshe);
		SRLGzidongyingshe.setUI((MenuItemUI) NSMenuItemUI.createUI(SRLGzidongyingshe));
	}

	private void accessMap() {
		network = new TNetwork();
		network.setDataBox(box1);

		network.getDataBox().addDataBoxListener(new DataBoxAdapter() {
			public void elementAdded(DataBoxEvent dataBoxEvent) {

				if (dataBoxEvent.getElement().getClass() == UINode.class) {
					nodeDummy.setName(" 节点(" + UINode.s_lUINodeList.size() + ")");
				} else if (dataBoxEvent.getElement().getClass() == UIFiberLink.class) {
					FiberLinkDummy.setName("FIBER(" + FiberLink.fiberLinkList.size() + ")");
					 } else if (dataBoxEvent.getElement().getClass() == UIWDMLink.class) {
					 WDMLinkDummy.setName("WDM(" + (WDMLink.WDMLinkList.size() + ")"));
					//
					// } else if (dataBoxEvent.getElement().getClass() == UIOTNLink.class) {
					// EleLinkDummy.setName("OTN(" + OTNLink.OTNLinkList.size() + ")");
				} else {
					if (TrafficDummy.getChildren().size() != 0) {// 11.21 业务数量问题
						TrafficDummy.setName("业务(" + Traffic.trafficList.size() + ")");
					} else {
						TrafficDummy.setName("业务");// 11.18
					}
				} // 11.21
			}

			public void elementRemoved(DataBoxEvent dataBoxEvent) {
				if (dataBoxEvent.getElement().getClass() == UINode.class) {
					nodeDummy.setName(" 节点(" + UINode.s_lUINodeList.size() + ")");
				} else if (dataBoxEvent.getElement().getClass() == UIFiberLink.class) {
					FiberLinkDummy.setName("光纤(" + FiberLink.fiberLinkList.size() + ")");
					 } else if (dataBoxEvent.getElement().getClass() == UIWDMLink.class) {
					 WDMLinkDummy.setName("光(" + (WDMLink.WDMLinkList.size() + ")"));
					//
					// } else if (dataBoxEvent.getElement().getClass() == UIOTNLink.class) {
					//
					// EleLinkDummy.setName("电(" + OTNLink.OTNLinkList.size() + ")");
				} else {
					if (TrafficDummy.getChildren().size() != 0) {

						TrafficDummy.setName("业务(" + Traffic.trafficList.size() + ")");
					} else {
						TrafficDummy.setName("业务");
					}
				}
			}

			public void elementsCleared(DataBoxEvent dataBoxEvent) {

				nodeDummy.setName(" 节点");
//				zjnodeDummy.setName("ROADM节点");
//				fzjnodeDummy.setName("OLA节点");
				FiberLinkDummy.setName("光纤");
				 WDMLinkDummy.setName("WDM");
				// EleLinkDummy.setName("短波");
				TrafficDummy.setName("业务");

			}
		});

		// 加地图

		GisNetworkAdapter adapter = new GisNetworkAdapter(network);
		adapter.installAdapter();
		adapter.getMap().setZoomLevels(new int[] { 7,8,9 });
		map = adapter.getMap();
		String b = "file:/" + System.getProperty("user.dir") + "/map/localecache";
		GisManager.registerDefaultSetting(TWaverGisConst.MAPDRIVER_GOOGLE_MAP, b);

		map.addLayer("Cached Map", TWaverGisConst.EXECUTOR_TYPE_GOOGLEMAP);

		double weidu = 35.2;
		double jingdu = 101.5;
		// map.setCenterPoint(new GeoCoordinate(101.5, 35.2));
		map.setCenterPoint(new GeoCoordinate(jingdu, weidu));
		map.setZoom(4);

		navigator = new Navigator(network); // 加入导航器
		navigator.showout(true);
		navigator.setLocation(10, 120);
		network.getToolbar().remove(10);
		network.getToolbar().remove(6);
		buttonCreateLink.setToolTipText("添加链路");
        jbc2.setIcon(creatLink);
        network.getToolbar().add(jbc1);
        network.getToolbar().add(buttonCreateLink);
        network.getToolbar().add(jbc2);
      //拖拽添加链路
        
		final MouseListener ListenerLink;
		ListenerLink =(new MouseListener(){
			UINode a;
			UINode b;
            @Override
			public void mouseClicked(MouseEvent arg0) {
				
				// TODO Auto-generated method stub
				System.out.println(arg0.getPoint().x);
				System.out.println(arg0.getPoint().y);
				
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println(arg0.getPoint().x);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void mousePressed(MouseEvent arg0) {
				if((network.getElementPhysicalAt(arg0.getPoint().x, arg0.getPoint().y)==null)||(!network.getElementPhysicalAt(arg0.getPoint().x, arg0.getPoint().y).getClass().equals(UINode.class)))
				{
					return;
				}
			  a=(UINode)network.getElementPhysicalAt(arg0.getPoint().x, arg0.getPoint().y);
				a.setSelected(false);
				if(a!=null)
				{
					setSourcexx(arg0.getPoint().x);
					setSourceyy(arg0.getPoint().y);
					
				}
				// TODO Auto-generated method stub
			
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				if((network.getElementPhysicalAt(arg0.getPoint().x, arg0.getPoint().y)==null)||(!network.getElementPhysicalAt(arg0.getPoint().x, arg0.getPoint().y).getClass().equals(UINode.class)))
				{
					return;
				}
				 b=(UINode)network.getElementPhysicalAt(arg0.getPoint().x, arg0.getPoint().y);
				 if(a!=null){
				a.setSelected(false);}
				if(a!=null&&b!=null&&a.getName()!=b.getName()){
					new Dlg_AddFiberlink().Dlg_AddFiberlink1(network.getDataBox(), FiberLinkDummy,a,b);
			}
			}
		});

		buttonCreateLink.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(buttonCreateLink.isSelected()){
					System.out.println("hahahahaha666666666666666");
					network.getCanvas().addMouseListener(ListenerLink);
				}
				else {
					network.getCanvas().removeMouseListener(ListenerLink);
		       }
		}
   });
		buttonGroup.add(buttonCreateLink);
	    buttonGroup.add(buttonCreateTraffic);
	    
		buttonCreateTraffic.setToolTipText("添加业务");//
	    jbc4.setIcon(creatTraffic);
	    network.getToolbar().add(jbc3);
	    network.getToolbar().add(buttonCreateTraffic);
	    network.getToolbar().add(jbc4);
	    final MouseListener ListenerTraffic;      //拖拽创建业务时对鼠标操作的控制	
	    ListenerTraffic =(new MouseListener(){
		UINode a;
		UINode b;
        @Override
		public void mouseClicked(MouseEvent arg0) {
			
			// TODO Auto-generated method stub
			System.out.println(arg0.getPoint().x);
			System.out.println(arg0.getPoint().y);//
//			System.out.println(arg0.getX());
//			System.out.println(arg0.getY());
			
		}
		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub
			System.out.println(arg0.getPoint().x);
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		@Override
		public void mousePressed(MouseEvent arg0) {
			if((network.getElementPhysicalAt(arg0.getPoint().x, arg0.getPoint().y)==null)||(!network.getElementPhysicalAt(arg0.getPoint().x, arg0.getPoint().y).getClass().equals(UINode.class)))
			{
				return;
			}
		  a=(UINode)network.getElementPhysicalAt(arg0.getPoint().x, arg0.getPoint().y);
			a.setSelected(false);
			if(a!=null)
			{
				setSourcexx(arg0.getPoint().x);
				setSourceyy(arg0.getPoint().y);
				
			}
			// TODO Auto-generated method stub
		
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			if((network.getElementPhysicalAt(arg0.getPoint().x, arg0.getPoint().y)==null)||(!network.getElementPhysicalAt(arg0.getPoint().x, arg0.getPoint().y).getClass().equals(UINode.class)))
			{
				return;
			}
			 b=(UINode)network.getElementPhysicalAt(arg0.getPoint().x, arg0.getPoint().y);
			 if(a!=null){
			a.setSelected(false);}
			if(a!=null&&b!=null&&a.getName()!=b.getName()){
				new Dlg_AddTraffic().addTraffic(network.getDataBox(), TrafficDummy, a, b);
				
		}
		}
	});
	buttonCreateTraffic.addActionListener(new ActionListener(){
		public void actionPerformed(ActionEvent e) {   //新建业务
			if(CreatBusinesscounter){
				CreatBusinesscounter =false;
				network.getCanvas().addMouseListener(ListenerTraffic);
				
			}
			else if(!CreatBusinesscounter){
				network.getCanvas().removeMouseListener(ListenerTraffic);
				buttonCreateTraffic.setBackground( new Color(212,226,194));
				CreatBusinesscounter =true;
	}
		}
		});
		
		
		
		
	}

	// 设置中心位置
	public static void setLocation(double jingdu, double weidu) {
		map.setCenterPoint(new GeoCoordinate(jingdu, weidu));
	}

	/**
	 * 加入节点信息悬浮窗显示
	 */
	private void installHoverTimer() {

		Timer hoverTimer = new Timer(10, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hoverTimestamp = System.currentTimeMillis();
				if (hoverTimestamp - targetTimestamp > POPUP_DELAY) {
					if (!popupVisible && (hoverElement != null)) {
						showPopup(hoverElement);
					}

				} else {
					if (popupVisible && (hoverElement != null)) {
						hidePopup(hoverElement);
					}
				}
			}
		});
		preparePhysicNetwork();
		hoverTimer.start();
		// TUIManager.registerAttachment("INFO", InfoAttachment.class);
	}

	private void preparePhysicNetwork() {
		network.getCanvas().addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {
				Element element = network.getElementLogicalAt(e.getPoint());
				if (element instanceof ResizableNode) {
					targetTimestamp = System.currentTimeMillis();
					hoverElement = element;
				} else {
					hoverElement = null;
				}
			}

			public void mouseDragged(MouseEvent e) {
			}
		});
	}

	private void hidePopup(Element element) {
		element.removeAttachment("INFO");
		popupVisible = false;
	}

	private void showPopup(Element element) {
		element.addAttachment("INFO");
		popupVisible = true;
	}

	public static void printout(String text) {
		String s1 = ta.getText();
		if (s1.isEmpty())
			ta.setText(text);
		else
			ta.setText(s1 + "\r\n" + text);
	}

	public void toolbar() { // 工具栏
		toolbar.removeAll();
		toolbar.setFloatable(false);
		toolbar.setRollover(true);

		
		
		linklayer[0] = new JCheckBox();
		linklayer[0].setSelected(true);
		linklayer[0].setIcon(new ImageIcon(ClassLoader.getSystemResource("button/WDM层2.jpg")));
		// linklayer[0].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

		linklayer[1] = new JCheckBox();
		linklayer[1].setSelected(true);
		linklayer[1].setIcon(new ImageIcon(ClassLoader.getSystemResource("button/FIBER层2.jpg")));
		// linklayer[1].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

//		linklayer[2] = new JCheckBox();
//		linklayer[2].setSelected(true);
//		linklayer[2].setIcon(new ImageIcon(ClassLoader.getSystemResource("button/new.png")));
		// linklayer[2].setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		// new ImageIcon(getClass().getResource("/resource/node-1.png"));
		// new ImageIcon(getClass().getResource("/resource/node-2.png"));
		// new ImageIcon(getClass().getResource("/resource/node2.png"));

		nodedisplay[0] = new JCheckBox("", true);
		nodedisplay[0].setBackground(backColor);
		nodedisplay[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				displayLinkandNode();
			}
		});

		 linklayer[0].addActionListener(new ActionListener() {
		 public void actionPerformed(ActionEvent e) {
		 if (linklayer[0].isSelected()) {
		 linklayer[0].setIcon(new
		 ImageIcon(ClassLoader.getSystemResource("button/WDM层2.jpg")));
		
		 } else {
		 linklayer[0].setIcon(new
		 ImageIcon(ClassLoader.getSystemResource("button/WDM层1.jpg")));
		 }
		 displayLinkandNode();
		
		 }
		 });
		 linklayer[1].addActionListener(new ActionListener() {
		 public void actionPerformed(ActionEvent e) {
		 if (linklayer[1].isSelected()) {
		 linklayer[1].setIcon(new
		 ImageIcon(ClassLoader.getSystemResource("button/FIBER层2.jpg")));
		
		 } else {
		 linklayer[1].setIcon(new
		 ImageIcon(ClassLoader.getSystemResource("button/FIBER层1.jpg")));
		 }
		 displayLinkandNode();
		 }
		 });
		// linklayer[2].addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent e) {
		// if (linklayer[2].isSelected()) {
		// linklayer[2].setIcon(new
		// ImageIcon(ClassLoader.getSystemResource("button/new.png")));
		//
		// } else {
		// linklayer[2].setIcon(new
		// ImageIcon(ClassLoader.getSystemResource("button/new1.png")));
		// }
		// displayLinkandNode();
		// }
		// });
	}

	private static void displayLinkandNode() {
		for (int i = 0; i < network.getVisibleFilters().size(); i++) {
			VisibleFilter theone = (VisibleFilter) network.getVisibleFilters().get(i);
			network.removeVisibleFilter(theone);
		}
		 network.addVisibleFilter(new VisibleFilter() {
		 public boolean isVisible(Element e) {
		 // TODO Auto-generated method stub
		 if (e.getClass().equals(UIFiberLink.class)) {
		 UIFiberLink thelink = (UIFiberLink) e;
		 if (linklayer[0].isSelected() && isNodeVisible(thelink.getFiberLink()))
		 return true;
		 } else if (e.getClass().equals(UIWDMLink.class)) {
		 UIWDMLink thelink = (UIWDMLink) e;
		 if (linklayer[1].isSelected() && isNodeVisible(thelink.getOptLink()))
		 return true;
		 }else if (e.getClass().equals(UINode.class)) {
				UINode theuinode = (UINode) e;
				if (theuinode != null) {
					if (theuinode.getCommonNode().getNodeType() != null) {
						if (linklayer[0].isSelected()) {
							return true;
						} else if (theuinode.getCommonNode().getNodeType() != NodeType.OLA) {
							if (linklayer[1].isSelected())
								return true;
						}
					}

				}
			}
				 return false;
				
				 }
				 });
				// PropertyDisplay.cleartable();

		 //else if (e.getClass().equals(UIOTNLink.class)) {
		// UIOTNLink thelink = (UIOTNLink) e;
		// if (linklayer[2].isSelected() && isNodeVisible(thelink.getEleLink()))
		// return true;
		// } else if (e.getClass().equals(UIOTNLink.class)) {
		// // if(linklayer[3].isSelected() &&
		// // isNodeVisible(thetraffic.getTrafficNode()))
		// return true;
		// } else if (e.getClass().equals(UINode.class)) {
		// UINode theuinode = (UINode) e;
		// if (theuinode != null) {
		// //if (nodedisplay[0].isSelected())
		// return true;
		//
		// }
		// }
		//
		// // }
		// // }
		// return false;
		//
		// }
		// });
		// PropertyDisplay.cleartable();2017.10.27被注释掉了

	}

	private static boolean isNodeVisible(BasicLink thelink) {
		int n = 0;
		// if(Network.searchNetwork(thelink.getM_cFromNode().getM_nSubnetNum()).getM_nNetType()
		// == 1){
		if (nodedisplay[0].isSelected()) {
			++n;
		}

		if (nodedisplay[0].isSelected())
			++n;

		if (n == 2) {
			return true;
		} else
			return false;
	}

	// 右键菜单设置
	public void popup() {

		PopupMenuGenerator popupMenuGenerator = new PopupMenuGenerator() {
			/**
			 * Add the identifier of each of the selected objects to the menu. In this
			 * example, the items added to the menu do nothing. In a real application, you
			 * would probably associate an implementation of the Swing Action interface with
			 * each menu item.
			 */

			public JPopupMenu generate(TView tview, final MouseEvent mouseEvent) {
				// Create an empty pop-up menu.
				JPopupMenu popMenu = new JPopupMenu();

				// JMenuItem checkport = new JMenuItem("查看端口");
				// JMenuItem checklowernodes = new JMenuItem("查看节点");
				JMenuItem addlink = new JMenuItem("添加链路");
				JMenuItem addROADMnode = new JMenuItem("添加ROADM节点");
				JMenuItem addOLAnode = new JMenuItem("添加OLA节点");
				JMenuItem designnode = new JMenuItem("编辑节点");
				JMenuItem designlink = new JMenuItem("编辑链路");
				JMenuItem designlink1 = new JMenuItem("编辑链路");
				JMenuItem designlink2 = new JMenuItem("编辑链路");

				JMenuItem dellink = new JMenuItem("删除链路");
				JMenuItem dellink1 = new JMenuItem("删除链路");
				JMenuItem dellink2 = new JMenuItem("删除链路");
				JMenuItem delnode = new JMenuItem("删除节点");

				designnode.setFont(new java.awt.Font("微软雅黑", Font.PLAIN, 13));
				designlink.setFont(new java.awt.Font("微软雅黑", Font.PLAIN, 13));
				designlink1.setFont(new java.awt.Font("微软雅黑", Font.PLAIN, 13));
				designlink2.setFont(new java.awt.Font("微软雅黑", Font.PLAIN, 13));
				addlink.setUI((MenuItemUI) NSMenuItemUI.createUI(addlink));
				addROADMnode.setUI((MenuItemUI) NSMenuItemUI.createUI(addROADMnode));
				addOLAnode.setUI((MenuItemUI) NSMenuItemUI.createUI(addOLAnode));
				dellink.setUI((MenuItemUI) NSMenuItemUI.createUI(dellink));
				dellink.setUI((MenuItemUI) NSMenuItemUI.createUI(dellink1));
				dellink.setUI((MenuItemUI) NSMenuItemUI.createUI(dellink2));
				delnode.setUI((MenuItemUI) NSMenuItemUI.createUI(delnode));
				designlink.setUI((MenuItemUI) NSMenuItemUI.createUI(dellink));
				designlink1.setUI((MenuItemUI) NSMenuItemUI.createUI(dellink1));
				designlink2.setUI((MenuItemUI) NSMenuItemUI.createUI(dellink2));
				designnode.setUI((MenuItemUI) NSMenuItemUI.createUI(delnode));
				new JMenuItem("设置链路故障");
				// JMenuItem linkbreakdown1= new JMenuItem("设置链路故障");
				// JMenuItem linkbreakdown2 = new JMenuItem("设置链路故障");
				JMenuItem recoverylinkbreakdown = new JMenuItem("链路故障恢复");

				// If the selectedObjects collection is empty, no objects are
				// selected.
				if (tview.getDataBox().getSelectionModel().isEmpty()) {
					popMenu.add(addROADMnode);
					popMenu.add(addOLAnode);
					popMenu.add(addlink);
				} else if (network.getDataBox().getLastSelectedElement().getClass() == UINode.class) {
					// popMenu.add(checkport);
					// popMenu.add(checklowernodes);
//					popMenu.add(delnode);
					popMenu.add(designnode);
				} else if (network.getDataBox().getLastSelectedElement().getClass() == UIFiberLink.class) {
//					popMenu.add(dellink);
					popMenu.add(designlink);
					// popMenu.add(linkbreakdown); // 2014.12.1 su 电路应急调度
					// popMenu.add(recoverylinkbreakdown);// 2014.12.1 su 电路应急调度
				} else if (network.getDataBox().getLastSelectedElement().getClass() == UIWDMLink.class) {
//					popMenu.add(dellink1);
//					popMenu.add(designlink1);
					// popMenu.add(linkbreakdown); // 2014.12.1 su 电路应急调度
					// popMenu.add(recoverylinkbreakdown);// 2014.12.1 su 电路应急调度
				} else if (network.getDataBox().getLastSelectedElement().getClass() == UIOTNLink.class) {
//					popMenu.add(dellink2);
					popMenu.add(designlink2);
					// popMenu.add(linkbreakdown); // 2014.12.1 su 电路应急调度
					// popMenu.add(recoverylinkbreakdown);// 2014.12.1 su 电路应急调度

				}

				addROADMnode.addActionListener(new ActionListener() {

					public boolean isInteger(String value) {
						try {
							Integer.parseInt(value);
							return true;
						} catch (NumberFormatException e) {
							return false;
						}
					}

					/**
					 * 判断字符串是否是浮点数
					 */
					public boolean isDouble(String value) {
						try {
							Double.parseDouble(value);
							if (value.contains("."))
								return true;
							return false;
						} catch (NumberFormatException e) {
							return false;
						}
					}

					/**
					 * 判断字符串是否是数字
					 */
					public boolean isNumber(String value) {
						return isInteger(value) || isDouble(value);
					}

					public List<String> tiqu(String string) { // 从状态栏中的string提取数字
						Pattern p = Pattern.compile("[^0-9]");
						Matcher m = p.matcher(string);
						String result = m.replaceAll(" ");
						System.out.println(result);
						String num[] = result.split(" ");
						java.util.List<String> list = new LinkedList<String>();
						for (String temp : num) {
							if (isNumber(temp))
								list.add(temp);
						}
						return list;
					}

					public void actionPerformed(ActionEvent e) {
						JLabel longitudeLabel = (JLabel) status.getComponent(2); // 获取状态栏中string
						JLabel latitudeLabel = (JLabel) status.getComponent(4);
						String jingdu = longitudeLabel.getText();
						String weidu = latitudeLabel.getText();
						List<String> list = tiqu(jingdu);
						String m = list.get(0);
						String n = list.get(1);
						String o = list.get(2);
						double longitude = Double.parseDouble(m) + Double.parseDouble(n) / 60.0
								+ Double.parseDouble(o) / 3600.0;
						List<String> list1 = tiqu(weidu);
						String x = list1.get(0);
						String y = list1.get(1);
						String z = list1.get(2);
						double latitude = Double.parseDouble(x) + Double.parseDouble(y) / 60.0
								+ Double.parseDouble(z) / 3600.0;
						new Dlg_AddROADMNode(dt, network.getDataBox(), nodeDummy, longitude, latitude);
						// UIdatarefresh();
						displayLinkandNode();

					}
				});

				addOLAnode.addActionListener(new ActionListener() {

					public boolean isInteger(String value) {
						try {
							Integer.parseInt(value);
							return true;
						} catch (NumberFormatException e) {
							return false;
						}
					}

					/**
					 * 判断字符串是否是浮点数
					 */
					public boolean isDouble(String value) {
						try {
							Double.parseDouble(value);
							if (value.contains("."))
								return true;
							return false;
						} catch (NumberFormatException e) {
							return false;
						}
					}

					/**
					 * 判断字符串是否是数字
					 */
					public boolean isNumber(String value) {
						return isInteger(value) || isDouble(value);
					}

					public List<String> tiqu(String string) { // 从状态栏中的string提取数字
						Pattern p = Pattern.compile("[^0-9]");
						Matcher m = p.matcher(string);
						String result = m.replaceAll(" ");
						System.out.println(result);
						String num[] = result.split(" ");
						java.util.List<String> list = new LinkedList<String>();
						for (String temp : num) {
							if (isNumber(temp))
								list.add(temp);
						}
						return list;
					}

					public void actionPerformed(ActionEvent e) {
						JLabel longitudeLabel = (JLabel) status.getComponent(2); // 获取状态栏中string
						JLabel latitudeLabel = (JLabel) status.getComponent(4);
						String jingdu = longitudeLabel.getText();
						String weidu = latitudeLabel.getText();
						List<String> list = tiqu(jingdu);
						String m = list.get(0);
						String n = list.get(1);
						String o = list.get(2);
						double longitude = Double.parseDouble(m) + Double.parseDouble(n) / 60.0
								+ Double.parseDouble(o) / 3600.0;
						List<String> list1 = tiqu(weidu);
						String x = list1.get(0);
						String y = list1.get(1);
						String z = list1.get(2);
						double latitude = Double.parseDouble(x) + Double.parseDouble(y) / 60.0
								+ Double.parseDouble(z) / 3600.0;
						new Dlg_AddOLANode(dt, network.getDataBox(), nodeDummy, longitude, latitude);
						// UIdatarefresh();
						displayLinkandNode();

					}
				});
				
				designnode.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						UINode uinode = (UINode) network.getDataBox().getLastSelectedElement();
                        int mark=Integer.parseInt(uinode.getID().toString());
						CommonNode commonNode = uinode.getCommonNode();
						
						if (String.valueOf(commonNode.getNodeType()) == "ROADM") {
							designROADMnode dnode = new designROADMnode(mark);
							dnode.jtid.setText(String.valueOf(commonNode.getId()));
							dnode.jtname.setText(String.valueOf(commonNode.getName()));
							dnode.jttype.setText(String.valueOf(commonNode.getNodeType()));
//							dnode.jtjin.setText(String.valueOf(commonNode.getLongitude()));
//							dnode.jtwei.setText(String.valueOf(commonNode.getLatitude()));
//							dnode.jtaname.setText(String.valueOf(commonNode.getOtherName()));
							dnode.jtshangxiazu.setText(String.valueOf(commonNode.getUpDown()));
							dnode.jtwss.setText(String.valueOf(commonNode.getWSS()));
							dnode.jc3.setSelected(commonNode.iszhongji());
						} else {
							JOptionPane.showMessageDialog(null, "OLA节点不可编辑", "提示", JOptionPane.INFORMATION_MESSAGE);
//							designOLAnode dnode1 = new designOLAnode(mark);
//							dnode1.jtid.setText(String.valueOf(commonNode.getId()));
//							dnode1.jtname.setText(String.valueOf(commonNode.getName()));
//							dnode1.jttype.setText(String.valueOf(commonNode.getNodeType()));
//							dnode1.jtjin.setText(String.valueOf(commonNode.getLongitude()));
//							dnode1.jtwei.setText(String.valueOf(commonNode.getLatitude()));
//							dnode1.jtaname.setText(String.valueOf(commonNode.getOtherName()));
//							dnode1.jtshangxiazu.setText(String.valueOf(commonNode.getUpDown()));
//							dnode1.jtwss.setText(String.valueOf(commonNode.getWSS()));
						}
						
						// dnode.jtaname.setText(String.valueOf(commonNode.getOtherName()));
						// 修改后传参
					}
				});

				designlink.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
//						LinkData ld = new LinkData();
						
						UIFiberLink uilink = (UIFiberLink) network.getDataBox().getLastSelectedElement();
						String name= uilink.fiberLink.getName();
						designFiberlink dlink = new designFiberlink(name);
						// 把链路的属性显示到对话框上
						dlink.jtlinktype.setText(String.valueOf(uilink.fiberLink.getLinkType()));   //光纤类型
						dlink.jtshuaijian.setText(String.valueOf(uilink.fiberLink.getAttenuation()));//光纤衰减
						dlink.jtlinkpmd.setText(String.valueOf(uilink.fiberLink.getPMD()));     //PMD
						dlink.jtstage.setText(String.valueOf(uilink.fiberLink.getFiberStage()));//光纤阶段
						dlink.jtname.setText(String.valueOf(uilink.fiberLink.getName()));     //名字
						dlink.jtid.setText(String.valueOf(uilink.fiberLink.getId()));        //id
						dlink.jtlen.setText(String.valueOf(uilink.fiberLink.getLength()));   //长度
						dlink.jtfirstnode.setText(String.valueOf(uilink.fiberLink.getFromNode().getName()));
						dlink.jtlastnode.setText(String.valueOf(uilink.fiberLink.getToNode().getName()));
						dlink.jtlayer.setText("Fiber");
					}
				});//fiber
				
//				designlink1.addActionListener(new ActionListener() {
//					public void actionPerformed(ActionEvent e) {
//						
//						UIWDMLink uilink1 = (UIWDMLink) network.getDataBox().getLastSelectedElement();
//						String name1= uilink1.WDMLink.getName();
//						designWDMlink dlink1 = new designWDMlink(name1);
//						// 把链路的属性显示到对话框上
//						dlink1.jtfirstnode.setText(String.valueOf(uilink1.WDMLink.getFromNode()));
////						dlink1.jtshuaijian.setText(String.valueOf(uilink1.WDMLink.getAttenuation()));
////						dlink1.jtlinkpmd.setText(String.valueOf(uilink1.WDMLink.getPMD()));
////						dlink1.jtstage.setText(String.valueOf(uilink1.WDMLink.getFiberStage()));
//						
//					}
//				});//wdm

				/*delnode.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						UINode uinode = (UINode) network.getDataBox().getLastSelectedElement();
						NodeData nd = new NodeData();
						System.out.println("uinodelist size  " + UINode.s_lUINodeList.size());
						if (nd.isCarryTraffic(uinode.getCommonNode())) {// 判断该节点是否承载业务
							// ，
							// 分两种情况进行提示
							int response = JOptionPane.showConfirmDialog(null, "节点“" + uinode.getCommonNode().getName()
									+ "”" + "已承载业务，若删除，此节点及其相关链路将会被删除,而且有可能导致路由失败！！！\n" + "删除后，是否需要立刻进行新一轮的网络规划？");
							if (response == 0) {
								nd.delNode(uinode.getCommonNode());
								UIdatarefresh();
								new Dlg_PolicySetting();
							} else if (response == 1) {
								nd.delNode(uinode.getCommonNode());
								UIdatarefresh();
							}
						} else {
							int response1 = JOptionPane.showConfirmDialog(null,
									"节点“" + uinode.getCommonNode().getName() + "”" + "暂没有承载业务\n" + "确认是否删除此节点及其相关链路？",
									"删除节点", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
							if (response1 == 0) {
								nd.delNode(uinode.getCommonNode());
								UIdatarefresh();
							}
						}
					}
				});*/
//				dellink.addActionListener(new ActionListener() {
//					public void actionPerformed(ActionEvent e) {// 删除光纤Fiber
//						// link
//						LinkData ld = new LinkData();
//						UIFiberLink uilink = (UIFiberLink) network.getDataBox().getLastSelectedElement();
//
//						int response = 0;
//						if (uilink.getFiberLink().getCarriedTraffic().size() != 0) {
//							response = JOptionPane.showConfirmDialog(null,
//									"链路" + "“" + uilink.getFiberLink().getName() + "”" + "已承载业务," + "是否删除该链路？", "删除链路",
//									JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//						} else {
//							response = JOptionPane.showConfirmDialog(null,
//									"链路" + "“" + uilink.getFiberLink().getName() + "”" + "没有承载业务，" + "是否删除该链路？", "删除链路",
//									JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//						}
//						if (response == 0) {
//							ld.delLink(uilink.getFiberLink());
//							UIdatarefresh();
//						}
//					}
//				});

//				dellink1.addActionListener(new ActionListener() {
//					public void actionPerformed(ActionEvent e) {// 删除卫星 删除Sate
//						LinkData ld = new LinkData();
//						UIWDMLink uilink = (UIWDMLink) network.getDataBox().getLastSelectedElement();
//
//						int response = 0;
//						if (uilink.getOptLink().getCarriedTraffic().size() != 0) {
//							response = JOptionPane.showConfirmDialog(null,
//									"链路" + "“" + uilink.getOptLink().getName() + "”" + "已承载业务," + "是否删除该链路？", "删除链路",
//									JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//						} else {
//							response = JOptionPane.showConfirmDialog(null,
//									"链路" + "“" + uilink.getOptLink().getName() + "”" + "没有承载业务，" + "是否删除该链路？", "删除链路",
//									JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//						}
//
//						if (response == 0) {
//							ld.delLink(uilink.getOptLink());
//							UIdatarefresh();
//						}
//					}
//				});

//				dellink2.addActionListener(new ActionListener() {// 删除短波 删除SHORT
//					public void actionPerformed(ActionEvent e) {
//						LinkData ld = new LinkData();
//						UIOTNLink uilink = (UIOTNLink) network.getDataBox().getLastSelectedElement();
//
//						if (uilink.getEleLink().getCarriedTraffic().size() != 0) {
//							JOptionPane.showConfirmDialog(null,
//									"链路" + "“" + uilink.getEleLink().getName() + "”" + "已承载业务," + "是否删除该链路？", "删除链路",
//									JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//						} else {
//							JOptionPane.showConfirmDialog(null,
//									"链路" + "“" + uilink.getEleLink().getName() + "”" + "没有承载业务，" + "是否删除该链路？", "删除链路",
//									JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
//
//						}
//						ld.delLink(uilink.getEleLink());
//						UIdatarefresh();
//					}
//				});
				addlink.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						// new Dlg_Addlink(dt, network, FiberLinkDummy, WDMLinkDummy, linklayer);
						new Dlg_AddFiberlink().Dlg_AddFiberlink(network.getDataBox(), FiberLinkDummy);
					}
				});
				
				
					
				
				
				// If menu is empty, return null.
				if (popMenu.getComponentCount() == 0) {
					return null;
				} else {
					return popMenu;
				}
			}
		};
		// Set the pop-up menu generator for network components
		network.setPopupMenuGenerator(popupMenuGenerator);
	}

	public void onlydataclear() {
		nodeDummy.clearChildren();
		FiberLinkDummy.clearChildren();
		 WDMLinkDummy.clearChildren();
		// EleLinkDummy.clearChildren();
		FiberLinkDummy.clearChildren();
		TrafficDummy.clearChildren();

		UIFiberLink.s_lUIFiberLinkList.clear();
		UIWDMLink.s_lUIWDMLinkList.clear();
//		UIOTNLink.s_lUIEleLinkList.clear();
		UINode.s_lUINodeList.clear();

		box1.clear();
		box1.addElement(nodeDummy);
		box1.addElement(FiberLinkDummy);
		 box1.addElement(WDMLinkDummy);
		// box1.addElement(EleLinkDummy);
		box1.addElement(TrafficDummy);

		// PropertyDisplay.cleartable();
		LinkData.clearLink();// 11.22
		NodeData.clearNode();
		TrafficData.clearTraffic();
	}

	public void onlydataclearforTraffic() {
		box1.removeElement(TrafficDummy);
		TrafficDummy.clearChildren();
		box1.addElement(TrafficDummy);
		//PropertyDisplay.cleartable();M11.14
	}

	public static void oneofTrafficDel(int id) {
		box1.removeElement(TrafficDummy);
		TrafficDummy.clearChildren();

		Dlg_TrafficInput.inputUITrafficData(network.getDataBox(), TrafficDummy);
		box1.addElement(TrafficDummy);
		PropertyDisplay.cleartable();
	}

	public static void UIdatarefresh() {

		CommonNode it = new CommonNode();
		for (int i = 0; i < UINode.s_lUINodeList.size(); ++i) {
			UINode.s_lUINodeList.get(i).setCommonNode(it);
		}
		UINode.clearAll();
		UIFiberLink.clearAll();
		UIWDMLink.clearAll();
//		UIOTNLink.clearAll();

		nodeDummy.clearChildren();
		FiberLinkDummy.clearChildren();
		 WDMLinkDummy.clearChildren();
		// EleLinkDummy.clearChildren();
		TrafficDummy.clearChildren();// 2017.3.28

		box1.clear();
		box1.addElement(nodeDummy);
		box1.addElement(FiberLinkDummy);
		 box1.addElement(WDMLinkDummy);
		// box1.addElement(EleLinkDummy);
		box1.addElement(TrafficDummy);// 2017.3.28

		// PropertyDisplay.cleartable();
	    Dlg_InputNodes.inputUINodeData(network.getDataBox(), nodeDummy);
		Dlg_InputLinks.inputUILinkData(network.getDataBox(), FiberLinkDummy, WDMLinkDummy, EleLinkDummy);
		Dlg_TrafficInput.inputUITrafficData(network.getDataBox(), TrafficDummy);

		displayLinkandNode();
		PropertyDisplay.cleartable();

	}

	/**
	 * 设置右上角节点某一级别（number）是否显示 number：0--一级节点，1--二级节点，2--三级节点
	 */
	// public static void setNodeLevelSelect(int number, boolean select) {
	// nodedisplay[number].setSelected(select);
	// }

	/**
	 * 判断某一级别的节点是否被显示 节点级别nodeLevel--1，2，3级
	 * 
	 */
	public static boolean ifNodeLevelSelect(int nodeLevel) {
		boolean result = false;
		int index = nodeLevel - 1;
		result = nodedisplay[index].isSelected();
		return result;
	}

	public static int getLayer() {
		return layer.getSelectedIndex();
	}

	// 为菜单栏和工具栏的按钮添加事件监听
	public void myGUI() {

		// 文件管理菜单
		menuItemNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (filenameall != null) {
					int test = JOptionPane.showConfirmDialog(null, "是否保存当前工程？");
					switch (test) { // 存储该操作的系统时间以判断是否在关闭时提醒保存
					case 0:
						// nowtime = System.currentTimeMillis()/1000;
						Database.setFileProperty(NetDesign_zs.fileinfo);
						Database.serialize(filenameall);
						// NetDesign_zs.printout("You saved " + filenameall);
						onlydataclear();
						filenameall = filename = filepath = fileinfo = null;
						new Dlg_NewProject(network.getDataBox());
						break;
					case 1:
						onlydataclear();
						filenameall = filename = filepath = fileinfo = null;
						new Dlg_NewProject(network.getDataBox());
						break;
					case 2:
						break;
					}
				}

				else {
					new Dlg_NewProject(network.getDataBox());
				}
			}
		});// 新建工程

		menuItemOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (filenameall != null) {
					int test = JOptionPane.showConfirmDialog(null, "是否保存当前工程？");
					switch (test) { // 存储该操作的系统时间以判断是否在关闭时提醒保存
					case 0:
						// nowtime = System.currentTimeMillis() / 1000;
						Database.setFileProperty(NetDesign_zs.fileinfo);
						Database.serialize(filenameall);
						// NetDesign_zs.printout("You saved " +filenameall);
						onlydataclear();
						filenameall = filename = filepath = fileinfo = null;
						displayLinkandNode();
						new Dlg_OpenProject(network.getDataBox(), nodeDummy, FiberLinkDummy, WDMLinkDummy, EleLinkDummy,
								TrafficDummy);
						break;
					case 1:
						onlydataclear();
						filenameall = filename = filepath = fileinfo = null;
						new Dlg_OpenProject(network.getDataBox(), nodeDummy, FiberLinkDummy, WDMLinkDummy, EleLinkDummy,
								TrafficDummy);
						displayLinkandNode();
						break;
					case 2:
						break;
					}
				}

				else {
					new Dlg_OpenProject(network.getDataBox(), nodeDummy, FiberLinkDummy, WDMLinkDummy, EleLinkDummy,
							TrafficDummy);
					displayLinkandNode();
				}
			}
		});// 打开工程

		menuItemSave.addActionListener(new ActionListener() { // 保存工程文件
			public void actionPerformed(ActionEvent e) {
				if (filenameall == null) {
					JOptionPane.showMessageDialog(null, "请先建立一个工程！", "提示", JOptionPane.INFORMATION_MESSAGE);
					return;
				} else {
					nowtime = System.currentTimeMillis() / 1000; // 存储该操作的系统时间以判断是否在关闭时提醒保存
					Database.setFileProperty(NetDesign_zs.fileinfo);
					Database.serialize(filenameall);
					// NetDesign_zs.printout("You saved " +filenameall);
					// //存储该操作的系统时间以判断是否在关闭时提醒保存
				}
			}
		});

		menuItemSaveas.addActionListener(new ActionListener() { // 另保存工程文件
			public void actionPerformed(ActionEvent e) {
				if (filenameall == null) {
					JOptionPane.showMessageDialog(null, "请先建立一个工程！", "提示", JOptionPane.INFORMATION_MESSAGE);
					return;
				} else
					new Dlg_ProjectSaveAs();
			}
		});

//		menuItemproperty.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {// 工程信息
//				if (filenameall == null) {
//					JOptionPane.showMessageDialog(null, "请先建立一个工程！", "提示", JOptionPane.INFORMATION_MESSAGE);
//					return;
//				} else
//					new Dlg_ProjectInfo();
//			}
//		});

		menuItemExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { // 退出

				long x = System.currentTimeMillis() / 1000;
				if ((x - nowtime) < 10 || filename == null) {
					dispose();
					System.exit(1);
				} else {
					int test = JOptionPane.showConfirmDialog(null, "是否保存工程文件？");
					switch (test) { // 存储该操作的系统时间以判断是否在关闭时提醒保存
					case 0:
						nowtime = System.currentTimeMillis() / 1000;
						Database.setFileProperty(NetDesign_zs.fileinfo);
						// Database.setNodenametype(UINode.UINameType);
						// Database.setLinknametype(UIFiberLink.UINameType);
						Database.serialize(filenameall);
						// Database.setLinkStyle0(NetDesign.LinkType0);
						// Database.setLinkStyle2(Netdesign.LinkType2);
						// Database.setLinkStyle3(Netdesign.LinkType3);
						// Database.setLinkStyle1(Netdesign.LinkType1);
						// NetDesign_zs.printout("You saved " + filenameall);
						dispose();
						System.exit(1);

						break;
					case 1:
						dispose();
						System.exit(1);
						break;
					case 2:
						dispose();
						System.exit(1);
						break;
					}
				}
			}
		});// 退出

		// 物理拓扑创建
		siteimport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (CommonNode.allNodeList.size() > 0 || FiberLink.fiberLinkList.size() > 0) {
					int test = JOptionPane.showConfirmDialog(null, "网元信息已存在，是否清空并导入？");
					switch (test) { // 存储该操作的系统时间以判断是否在关闭时提醒保存
					case 0:
						onlydataclear();// 清空资源
						new Dlg_InputNodes(network.getDataBox(), nodeDummy, FiberLinkDummy, WDMLinkDummy, EleLinkDummy,
								TrafficDummy);
						displayLinkandNode();

						break;
					case 1:
						break;
					case 2:
						break;
					}
				} else {
					new Dlg_InputNodes(network.getDataBox(), nodeDummy, FiberLinkDummy, WDMLinkDummy, EleLinkDummy,
							TrafficDummy);
					displayLinkandNode();
				}

			}
		});
		// 导入链路 链路导入
		fiberimport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (CommonNode.allNodeList.size() == 0) {
					JOptionPane.showMessageDialog(null, "请先导入节点信息！", "错误", JOptionPane.INFORMATION_MESSAGE);

				}

				else if (!FiberLink.fiberLinkList.isEmpty() || !WDMLink.WDMLinkList.isEmpty()) {
					int test = JOptionPane.showConfirmDialog(null, "网元信息已存在，是否清空并导入？");
					switch (test) { // 存储该操作的系统时间以判断是否在关闭时提醒保存
					case 0:
						onlydataclear();// 清空资源
						new Dlg_InputLinks(network.getDataBox(), nodeDummy, FiberLinkDummy, WDMLinkDummy, EleLinkDummy,
								TrafficDummy);
						displayLinkandNode();

						break;
					case 1:
						break;
					case 2:
						break;
					}
				} else {
					new Dlg_InputLinks(network.getDataBox(), nodeDummy, FiberLinkDummy, WDMLinkDummy, EleLinkDummy,
							TrafficDummy);
					displayLinkandNode();
				}

			}
		});
		AllInput.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {	
				if(filenameall==null){
	        		JOptionPane.showMessageDialog(null,"请先建立一个工程！","提示",JOptionPane.INFORMATION_MESSAGE);
	        		return;
	        	}
				else if(Network.m_lNetworkList.size() != 0){
					int test = JOptionPane.showConfirmDialog(null, "网元信息已存在，是否清空并导入？");
					switch(test){ //存储该操作的系统时间以判断是否在关闭时提醒保存
					case 0:        
						onlydataclear();//清空资源
						new Dlg_InputAll(network.getDataBox(), nodeDummy, FiberLinkDummy, WDMLinkDummy, EleLinkDummy,
								TrafficDummy);
						displayLinkandNode();
			            break;
					case 1:
						break;
					case 2:
						break;
					}
				}
				else{
					new Dlg_InputAll(network.getDataBox(), nodeDummy, FiberLinkDummy, WDMLinkDummy, EleLinkDummy,
							TrafficDummy);
				displayLinkandNode();}
				
			}});	
		// 导入业务
		businessimport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (FiberLink.fiberLinkList.isEmpty() || WDMLink.WDMLinkList.isEmpty()) {
					JOptionPane.showMessageDialog(null, "请先导入网元资源！");
				} else if (!data.Traffic.trafficList.isEmpty()) {
					int test = JOptionPane.showConfirmDialog(null, "业务信息已存在，是否清空并导入？");
					switch (test) { // 存储该操作的系统时间以判断是否在关闭时提醒保存
					case 0:
						TrafficData.clearTraffic();
						onlydataclearforTraffic();
						Dlg_TrafficInput sfc = new Dlg_TrafficInput(network.getDataBox(), TrafficDummy);
						sfc.setVisible(false);
						break;
					case 1:
						break;
					case 2:
						break;
					}
				} else {
					Dlg_TrafficInput sfc = new Dlg_TrafficInput(network.getDataBox(), TrafficDummy);
					sfc.setVisible(false);
				}
				

			}
		});
		
		// 资源清空
				resourceclear.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
							int test = JOptionPane.showConfirmDialog(null, "是否确定要清空全部网元及业务资源？");
							switch(test)
							{
								case 0:
								onlydataclear();                         //清空网元资源
								TrafficData traffic=new TrafficData();   //清空业务资源
							    traffic.clearTraffic();
							    LinkRGroup.clear();                      //清空共享风险链路组
							       
							    JOptionPane.showMessageDialog(null, "资源已清空");
							    
							    break;
								case 1:
								case 2:	
									break;
								}	
							}
						});
//				Algorithmused.addActionListener(new ActionListener() {
//					public void actionPerformed(ActionEvent e) {
//					
//					}
				
//				});
       //NF系数设置
				NFsetting.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
//						new NFCoefficient();	
						new AFSetting();
						//window.frmAf.setVisible(true);
						for(FiberLink link : FiberLink.fiberLinkList) {
							System.out.println(link.getName()+": "+link.getOSNRCount());
						}
					}
				});
      //背靠背指标
				overOSNRsetting.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						// TODO Auto-generated method stub
						new BackToBackCoefficient();
					}
				});
	  //OSNR参数设置
				OSNRSetting.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent arg0) {
						// TODO Auto-generated method stub
						new dialog.OSNRSetting();
						for(FiberLink link : FiberLink.fiberLinkList) {
							System.out.println(link.getOSNRCount());
						}
					}
				});
				
				
		Nodesingle.addActionListener(new ActionListener() {// 节点单断
			public void actionPerformed(ActionEvent e) {
				new Dlg_NodePoll(dt);
			}
		});
		
		Linksingle.addActionListener(new ActionListener() {// 链路单断
			public void actionPerformed(ActionEvent e) {
				new Dlg_LinkPoll(dt);
			}
		});
		
//		SRLGsingle.addActionListener(new ActionListener() {// 链路单断
//			public void actionPerformed(ActionEvent e) {
//				if ((FiberLink.fiberLinkList.isEmpty()) && (WDMLink.WDMLinkList.isEmpty())) {
//					JOptionPane.showMessageDialog(null, "请先导入网元资源！");
//					return;
//				} else {
//				new Dlg_SRLGPoll(dt);
//				}
//			}
//		});
		//人工设置故障
//		 nodeArtificialsetting.addActionListener(new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					new Dlg_SetNodePoll(dt);
//				}
//		 });
//		 
//		linkArtificialsetting.addActionListener(new ActionListener() {
//				@Override
//				public void actionPerformed(ActionEvent e) {
//					new Dlg_SetLinkPoll(dt);
//				}
//		 });
		//资源管理
		nodemanagement.addActionListener(new ActionListener() {// 节点管理
			public void actionPerformed(ActionEvent e) {
				new Dlg_noderesult();
			}
		});
		fibermanagement.addActionListener(new ActionListener() {//fiber管理
			public void actionPerformed(ActionEvent e) {
				new Dlg_fiberresult();
			}
		});
		wdmmanagement.addActionListener(new ActionListener() {//wdm管理
			public void actionPerformed(ActionEvent e) {
				new Dlg_wdmresult();
			}
		});
		businessmanagement.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {    //业务管理
				new Dlg_DesignResult();
			}});
		
		Artificialsetting.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {    //人工故障设置
				new Dlg_SetFault(dt);
			}});
		
		networkDesignguide.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {    //路由分配
				new Dlg_PolicySetting();
			}});
		
        bussinessPlan.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {    //路由分配
				new Dlg_BussinessPlan();
			}});
		
		SRLGmanagement.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if ((FiberLink.fiberLinkList.isEmpty()) && (WDMLink.WDMLinkList.isEmpty())) {
					JOptionPane.showMessageDialog(null, "请先导入网元资源！");
					return;
				} else {
					new Dlg_SRLGSet(dt);
				}
			}
			});
		
//		trafficgroup.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//
//				new Dlg_TrafficGroup(dt);
//			}});
		
		trafficgroup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				new Dlg_RelatedTraffic();
			}});
			
		// SRLG自动映射
				SRLGzidongyingshe.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if ((FiberLink.fiberLinkList.isEmpty()) && (WDMLink.WDMLinkList.isEmpty())) {
							JOptionPane.showMessageDialog(null, "请先导入网元资源！");
							return;
						} else {
							if (LinkRGroup.clicked == false) {// 如果还没有自动映射
								LinkRGroup.automation();
								JOptionPane.showMessageDialog(null, "自动映射成功");
								System.out.println("自动映射成功");
							}
							new Dlg_SRLG();
						}
					}

				});
	

		// 报表导出
		nodeoutput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (e.getSource() == nodeoutput) {
					JFileChooser chooser = new JFileChooser();
					if (null == filepath) {// 如果当前不存在正在操作的工程
						chooser = new JFileChooser();
					} else {
						filelist = filepath;
						chooser = new JFileChooser(filelist);// 打开默认路径为工程的路径
					}
					chooser.setDialogTitle("导出节点配置表");
					chooser.setAcceptAllFileFilterUsed(false);
					// 前一个xls为窗口下拉选择文件类型显示，后一个是文件过滤器的文件类型
					FileNameExtensionFilter filter = new FileNameExtensionFilter("xls", "xls");
					chooser.setFileFilter(filter);
					// 获取当前时间
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd");
					String date = dateformat.format(cal.getTime());
					JTextField text = getTextField(chooser);// 获取输入文件名部分
					text.setText("节点配置表" + date);// 设置默认文件名
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					int option = chooser.showSaveDialog(chooser);
					if (option == JFileChooser.APPROVE_OPTION) {// 如果点击确定
						File file = chooser.getSelectedFile();
						filelist = file.getPath();
						if (!(filelist.endsWith(".xls")))// 如果现在的文件名里不包含扩展名，就给其加上
							filelist = filelist + ".xls";
						file = new File(filelist);
						if (file.exists()) {// 如果这个文件已经存在了
							// JOptionPane.showMessageDialog(null, "当前文件已存存在，请删除或重命名后再导出");}
							int test = JOptionPane.showConfirmDialog(null, "当前文件已存在，是否覆盖？");
							switch (test) {
							case 0:// 覆盖
									// PortDataBase portOut = new PortDataBase();
									// int id = Integer
									// .parseInt(String.valueOf(nodeModel.getValueAt(nodeTable.getSelectedRow(),
									// 0)));
									// theNode = CommonNode.getNode(id);
								try {
									NodeOutput(filelist);
									JOptionPane.showMessageDialog(null, "表格已覆盖");
								} catch (Exception e1) {
									JOptionPane.showMessageDialog(null, "请关闭原Excel文件，否则无法完成覆盖");
								}
//								setVisible(false);
//								dispose();
								break;
							case 1:
								return;
							case 2:
								return;
							}
						} else {
							NodeOutput(filelist);
							JOptionPane.showMessageDialog(null, "数据已导出");
						}

					}
				}
			}

			private void NodeOutput(String str) {
				HSSFWorkbook wb = null;
				HSSFSheet sheet1 = null;
				HSSFSheet sheet2 = null;
				try {
					// fs = new POIFSFileSystem(new FileInputStream(str));
					wb = new HSSFWorkbook();
					sheet1 = wb.createSheet("链路利用率");
					sheet2 = wb.createSheet("节点");
				} catch (Exception e) {
					e.printStackTrace();
				}

				HSSFRow row1 = sheet1.createRow(0);
				HSSFCell cell1 = row1.createCell(0);
				cell1.setCellValue("序号");
				cell1 = row1.createCell(1);
				cell1.setCellValue("起点");
				cell1 = row1.createCell(2);
				cell1.setCellValue("终点");
				cell1 = row1.createCell(3);
				cell1.setCellValue("数量");
				cell1 = row1.createCell(4);
				cell1.setCellValue("用于工作路由的波长波道编号");
				cell1 = row1.createCell(5);
				cell1.setCellValue("数量");
				cell1 = row1.createCell(6);
				cell1.setCellValue("用于恢复路由的波长波道编号");

				for (int i = 0; i < WDMLink.WDMLinkList.size(); i++) {
					WDMLink link = WDMLink.WDMLinkList.get(i);
					row1 = sheet1.createRow(i + 1);
					cell1 = row1.createCell(0);
					cell1.setCellValue(link.getId());
					cell1 = row1.createCell(1);
					cell1.setCellValue(link.getFromNode().getName());
					cell1 = row1.createCell(2);
					cell1.setCellValue(link.getToNode().getName());
					cell1 = row1.createCell(3);
					List<WaveLength> wavelist = link.getWaveLengthList();
					int workUsedWaveNum = 0;
					for (int k = 0; k < wavelist.size(); k++) {
						if (wavelist.get(k).getStatus() == Status.工作)
							workUsedWaveNum++;

					}
					cell1.setCellValue(workUsedWaveNum);
					cell1 = row1.createCell(4);
					cell1.setCellValue((link.workedUseWave()));
					
					//选择的是动态重路由策略
					if(DataSave.reRoutePolicy == 2) {
						System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++6555555555555555555555");
						cell1 = row1.createCell(5);
						int otherUsedWaveNum = 0;
						String res = "";
						int[] dynUsedLink = link.getDynUsedLink();
						for(int j=1; j<dynUsedLink.length;j++) {
							if(dynUsedLink[j] == 1) {
								otherUsedWaveNum++;
								res = res + j +",";
							}
						}
						if(!res.equals("")) {
							res = res.substring(0, res.length() - 1);
						}
						cell1.setCellValue(otherUsedWaveNum);
						cell1 = row1.createCell(6);
						cell1.setCellValue(res);
					}else {
						cell1 = row1.createCell(5);
						int otherUsedWaveNum = 0;
						List<WaveLength> wavelist2 = link.getWaveLengthList();
						for (int j = 0; j < wavelist2.size(); j++) {
							if (wavelist2.get(j).getStatus() == Status.保护 || wavelist.get(j).getStatus() == Status.恢复)
								otherUsedWaveNum++;
						}
						cell1.setCellValue(otherUsedWaveNum);
						cell1 = row1.createCell(6);
						cell1.setCellValue(link.otherUseWave());
					}
					
					
				}

				HSSFRow row2 = sheet2.createRow(0);
				HSSFCell cell2 = row2.createCell(0);
				cell2.setCellValue("序号");
				cell2 = row2.createCell(1);
				cell2.setCellValue("节点");
				cell2 = row2.createCell(2);
				cell2.setCellValue("上下路模块组数量");
				cell2 = row2.createCell(3);
				cell2.setCellValue("用于工作的中继OTU数量");
				cell2 = row2.createCell(4);
				cell2.setCellValue("用于恢复的中继OTU数量");

				System.out.println(CommonNode.ROADM_NodeList.size());
				for (int k = 0; k < CommonNode.ROADM_NodeList.size(); k++) {

					CommonNode node = CommonNode.ROADM_NodeList.get(k);
					row2 = sheet2.createRow(k + 1);
					cell2 = row2.createCell(0);
					cell2.setCellValue(node.getId());
					cell2 = row2.createCell(1);
					cell2.setCellValue(node.getName());
					if(DataSave.reRoutePolicy == 2) {
						System.out.println("哎！！！！！！！！！！！！！！！！！！！！！！");
						cell2 = row2.createCell(2);
						cell2.setCellValue(Math.max(node.getDynUsedUpdown(), node.countUpdown(node)));
						//cell2.setCellValue(node.getDynUsedUpdown());
					} else {
						cell2 = row2.createCell(2);
						cell2.setCellValue(node.countUpdown(node));}
					cell2 = row2.createCell(3);
					cell2.setCellValue(node.getWorkOTUNum());
					
					//选择的是动态重路由策略
					if(DataSave.reRoutePolicy == 2) {
						cell2 = row2.createCell(4);
						int[] dynUsedOTU = node.getDynUsedOTU();
						cell2.setCellValue(dynUsedOTU[0]);
					}else {
						cell2 = row2.createCell(4);
						cell2.setCellValue(node.getRestoreOTUNum());
					}
					
					
					// cell2 = row2.createCell(5);
					// cell2.setCellValue();
				}

				FileOutputStream os = null;
				try {
					os = new FileOutputStream(str);
				} catch (FileNotFoundException e) {
					// TODO 自动生成 catch 块
					e.printStackTrace();
				}
				try {
					wb.write(os);
				} catch (IOException e) {
					// TODO 自动生成 catch 块
					e.printStackTrace();
				}
				try {
					os.close();
				} catch (IOException e) {
					// TODO 自动生成 catch 块
					e.printStackTrace();
				}
			}

		});
		traficoutput.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == traficoutput) {
					JFileChooser chooser = new JFileChooser();
					if (null == filepath) {// 如果当前不存在正在操作的工程
						chooser = new JFileChooser();
					} else {
						filelist = filepath;
						chooser = new JFileChooser(filelist);// 打开默认路径为工程的路径
					}
					chooser.setDialogTitle("导出工作路由表");
					chooser.setAcceptAllFileFilterUsed(false);
					// 前一个xls为窗口下拉选择文件类型显示，后一个是文件过滤器的文件类型
					FileNameExtensionFilter filter = new FileNameExtensionFilter("xls", "xls");
					chooser.setFileFilter(filter);
					// 获取当前时间
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd");
					String date = dateformat.format(cal.getTime());
					JTextField text = getTextField(chooser);// 获取输入文件名部分
					text.setText("工作路由表" + date);// 设置默认文件名
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					int option = chooser.showSaveDialog(chooser);
					if (option == JFileChooser.APPROVE_OPTION) {// 如果点击确定
						
						
					    
					    File file = chooser.getSelectedFile();
						filelist = file.getPath();
						if (!(filelist.endsWith(".xls")))// 如果现在的文件名里不包含扩展名，就给其加上
							filelist = filelist + ".xls";
						file = new File(filelist);
						if (file.exists()) {// 如果这个文件已经存在了
							// JOptionPane.showMessageDialog(null, "当前文件已存存在，请删除或重命名后再导出");}
							int test = JOptionPane.showConfirmDialog(null, "当前文件已存在，是否覆盖？");
							switch (test) {
							case 0:// 覆盖
									// PortDataBase portOut = new PortDataBase();
									// int id = Integer
									// .parseInt(String.valueOf(nodeModel.getValueAt(nodeTable.getSelectedRow(),
									// 0)));
									// theNode = CommonNode.getNode(id);
								try {
									TrafficOutPut(filelist);
									JOptionPane.showMessageDialog(null, "表格已覆盖");
								} catch (Exception e1) {
									JOptionPane.showMessageDialog(null, "请关闭原Excel文件，否则无法完成覆盖");
								}
//								setVisible(false);
//								dispose();
								break;
							case 1:
								return;
							case 2:
								return;
							}
						} else {
							TrafficOutPut(filelist);
							JOptionPane.showMessageDialog(null, "数据已导出");
						}

					}
				}
			}
		});
		
//		linkout.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				if (e.getSource() == traficoutput) {
//					JFileChooser chooser = new JFileChooser();
//					if (null == filepath) {// 如果当前不存在正在操作的工程
//						chooser = new JFileChooser();
//					} else {
//						filelist = filepath;
//						chooser = new JFileChooser(filelist);// 打开默认路径为工程的路径
//					}
//					chooser.setDialogTitle("导出链路单断循环表");
//					chooser.setAcceptAllFileFilterUsed(false);
//					// 前一个xls为窗口下拉选择文件类型显示，后一个是文件过滤器的文件类型
//					FileNameExtensionFilter filter = new FileNameExtensionFilter("xls", "xls");
//					chooser.setFileFilter(filter);
//					// 获取当前时间
//					Calendar cal = Calendar.getInstance();
//					SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd");
//					String date = dateformat.format(cal.getTime());
//					JTextField text = getTextField(chooser);// 获取输入文件名部分
//					text.setText("链路单断循环表" + date);// 设置默认文件名
//					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//					int option = chooser.showSaveDialog(chooser);
//					if (option == JFileChooser.APPROVE_OPTION) {// 如果点击确定
//						File file = chooser.getSelectedFile();
//						filelist = file.getPath();
//						if (!(filelist.endsWith(".xls")))// 如果现在的文件名里不包含扩展名，就给其加上
//							filelist = filelist + ".xls";
//						file = new File(filelist);
//						if (file.exists()) {// 如果这个文件已经存在了
//							// JOptionPane.showMessageDialog(null, "当前文件已存存在，请删除或重命名后再导出");}
//							int test = JOptionPane.showConfirmDialog(null, "当前文件已存在，是否覆盖？");
//							switch (test) {
//							case 0:// 覆盖
//								try {
//									TrafficOutPut(filelist);
//									JOptionPane.showMessageDialog(null, "表格已覆盖");
//								} catch (Exception e1) {
//									JOptionPane.showMessageDialog(null, "请关闭原Excel文件，否则无法完成覆盖");
//								}
////								setVisible(false);
////								dispose();
//								break;
//							case 1:
//								return;
//							case 2:
//								return;
//							}
//						} else {
//							TrafficOutPut(filelist);
//							JOptionPane.showMessageDialog(null, "数据已导出");
//						}
//
//					}
//				}
//			}
//		});
		//45456
		linkout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == linkout) {
					JFileChooser chooser = new JFileChooser();
					if (null == filepath) {// 如果当前不存在正在操作的工程
						chooser = new JFileChooser();
					} else {
						filelist = filepath;
						chooser = new JFileChooser(filelist);// 打开默认路径为工程的路径
					}
					chooser.setDialogTitle("导出链路单断信息表");
					chooser.setAcceptAllFileFilterUsed(false);
					// 前一个xls为窗口下拉选择文件类型显示，后一个是文件过滤器的文件类型
					FileNameExtensionFilter filter = new FileNameExtensionFilter("xls", "xls");
					chooser.setFileFilter(filter);
					// 获取当前时间
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd");
					String date = dateformat.format(cal.getTime());
					JTextField text = getTextField(chooser);// 获取输入文件名部分
					text.setText("链路单断信息表" + date);// 设置默认文件名
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					int option = chooser.showSaveDialog(chooser);
					if (option == JFileChooser.APPROVE_OPTION) {// 如果点击确定
						File file = chooser.getSelectedFile();
						filelist = file.getPath();
						if (!(filelist.endsWith(".xls")))// 如果现在的文件名里不包含扩展名，就给其加上
							filelist = filelist + ".xls";
						file = new File(filelist);
						if (file.exists()) {// 如果这个文件已经存在了
							// JOptionPane.showMessageDialog(null, "当前文件已存存在，请删除或重命名后再导出");}
							int test = JOptionPane.showConfirmDialog(null, "当前文件已存在，是否覆盖？");
							switch (test) {
							case 0:// 覆盖
									// PortDataBase portOut = new PortDataBase();
									// int id = Integer
									// .parseInt(String.valueOf(nodeModel.getValueAt(nodeTable.getSelectedRow(),
									// 0)));
									// theNode = CommonNode.getNode(id);
								try {
									
									//去重
									LinkedList<FiberLink> unrepeat = new LinkedList<>();
					                unrepeat.addAll(FiberLink.fiberLinkList);
					                for  ( int  i  =   0 ; i  <  unrepeat.size()  -   1 ; i ++ )  {       
					                    for  ( int  j  =  unrepeat.size()  -   1 ; j  >  i; j -- )  {       
					                         if  (unrepeat.get(j).getName().equals(unrepeat.get(i).getName()))  {       
					                        	 unrepeat.remove(j);       
					                          }        
					                      }        
					                    } 
									
									Thread thread = new Thread() {
							            public void run() {
							                int i = 0;
 						                
							              //每次单断循环的时候都初始化节点dynUsedOTU[]和链路的dynUsedLink[]属性
							        		for(CommonNode node : CommonNode.allNodeList) {
							        			int[] dynUsedOTU = node.getDynUsedOTU();
							        			dynUsedOTU[0]=dynUsedOTU[1]=0;
							        		}
							        		for(WDMLink link : WDMLink.WDMLinkList) {
							        			int[] dynUsedLink = link.getDynUsedLink();
							        			for(int j=0;j<dynUsedLink.length;j++) {
							        				dynUsedLink[j]=0;
							        			}
							        		}
							                
							                while (i < unrepeat.size()) {		                   
													
													FiberLink fl = unrepeat.get(i);
													// this.linkEvaluation(fl,isSRLG);
													System.out.println(fl.getName() + " "+i+"/"+ unrepeat.size());
													List<Traffic> tralist = Evaluation.linkEvaluation(fl);
													if (tralist != null && tralist.size() != 0 ) {
														for (Traffic tra : tralist) {
															if (tra.getResumeRoute() != null) {
																Route.setDynNodeAndLinkParams(tra.getResumeRoute());
															}
														}
													}
													// 判断此次仿真用了多少个OTU，如果大于当前使用的OTU数dynOTU[0]，则更新
													for (CommonNode node : CommonNode.allNodeList) {
														node.countUpdown1(node);//更新此次仿真用的上下路模块
														int[] dynUsedOTU = node.getDynUsedOTU();
														if (dynUsedOTU[1] > dynUsedOTU[0]) {
															dynUsedOTU[0] = dynUsedOTU[1];
														}
													}
													if(tralist!=null&&tralist.size()!=0) {
														for(Traffic tra:tralist) {
//															if (tra.getFaultType()!=0)
															Evaluation.PutOutTraffic.add(tra);
														}
													}
													TrafficDatabase lldd=new TrafficDatabase();
													lldd.TrafficOutPut(filelist,fl);
													TrafficDatabase.index = 1;
													Evaluation.PutOutTraffic.clear();
													
													Evaluation.clearRsmRoute(tralist);
													i++;    
							                }
							            }
							        };
							        TestProgressBar.show((Frame) null, thread, "数据导出中...", "数据已导出!", "Cancel");
									TrafficDatabase.index = 0;
									JOptionPane.showMessageDialog(null, "表格已覆盖");
								} catch (Exception e1) {
									JOptionPane.showMessageDialog(null, "请关闭原Excel文件，否则无法完成覆盖");
								}
//								setVisible(false);
//								dispose();
								break;
							case 1:
								return;
							case 2:
								return;
							}
						} else {
							
							//去重
							LinkedList<FiberLink> unrepeat = new LinkedList<>();
			                unrepeat.addAll(FiberLink.fiberLinkList);
			                for  ( int  i  =   0 ; i  <  unrepeat.size()  -   1 ; i ++ )  {       
			                    for  ( int  j  =  unrepeat.size()  -   1 ; j  >  i; j -- )  {       
			                         if  (unrepeat.get(j).getName().equals(unrepeat.get(i).getName()))  {       
			                        	 unrepeat.remove(j);       
			                          }        
			                      }        
			                    } 

							Thread thread = new Thread() {
					            public void run() {
					                int i = 0;					 
					                while (i < unrepeat.size()) {		                   
											
											FiberLink fl = unrepeat.get(i);
											// this.linkEvaluation(fl,isSRLG);
											System.out.println(fl.getName() + " "+i+"/"+ unrepeat.size());
											List<Traffic> tralist = Evaluation.linkEvaluation(fl);
											if (tralist != null && tralist.size() != 0 ) {
												for (Traffic tra : tralist) {
													if (tra.getResumeRoute() != null) {
														Route.setDynNodeAndLinkParams(tra.getResumeRoute());
													}
												}
											}
											// 判断此次仿真用了多少个OTU，如果大于当前使用的OTU数dynOTU[0]，则更新
											for (CommonNode node : CommonNode.allNodeList) {
												node.countUpdown1(node);//更新此次仿真用的上下路模块
												int[] dynUsedOTU = node.getDynUsedOTU();
												if (dynUsedOTU[1] > dynUsedOTU[0]) {
													dynUsedOTU[0] = dynUsedOTU[1];
												}
											}
											if(tralist!=null&&tralist.size()!=0) {
												for(Traffic tra:tralist) {
//													if (tra.getFaultType()!=0)
													Evaluation.PutOutTraffic.add(tra);
												}
											}
											TrafficDatabase lldd=new TrafficDatabase();
											lldd.TrafficOutPut(filelist,fl);
											TrafficDatabase.index = 1;
											Evaluation.PutOutTraffic.clear();
											Evaluation.clearRsmRoute(tralist);
											i++;    
					                }
					            }
					        };
					        						    
//							for (int i = 0; i < FiberLink.fiberLinkList.size(); ++i) {
//								int size = FiberLink.fiberLinkList.size();
//								NetDesign_zs.processValue = 100*(i+1)/size;
//								System.out.println("++++++++++++++++++++++++++++++++++"+processValue);
//								FiberLink fl = FiberLink.fiberLinkList.get(i);
//								// this.linkEvaluation(fl,isSRLG);
//								List<Traffic> tralist = Evaluation.linkEvaluation(fl);
//								if(tralist!=null&&tralist.size()!=0) {
//									for(Traffic tra:tralist) {
////										if (tra.getFaultType()!=0)
//										Evaluation.PutOutTraffic.add(tra);
//									}
//								}
//								TrafficDatabase lldd=new TrafficDatabase();
//								lldd.TrafficOutPut(filelist,fl);
//								TrafficDatabase.index = 1;
//								Evaluation.PutOutTraffic.clear();
//								Evaluation.clearRsmRoute(tralist);
//							}
							TestProgressBar.show((Frame) null, thread, "数据导出中...", "数据已导出!", "Cancel");
							TrafficDatabase.index = 0;
							//JOptionPane.showMessageDialog(null, "数据已导出");
						}

					}
				}
			}
		});

//		linkout.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				NetDesign_zs.listEvaluation(FiberLink.fiberLinkList);
//				// 显示链路单断的窗口
//			}
//		});
		

//		nodeout.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				NetDesign_zs.nodelistEvaluation(CommonNode.allNodeList);
//				// 显示节点单断的窗口
//			}
//		});
		nodeout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == linkout) {
					JFileChooser chooser = new JFileChooser();
					if (null == filepath) {// 如果当前不存在正在操作的工程
						chooser = new JFileChooser();
					} else {
						filelist = filepath;
						chooser = new JFileChooser(filelist);// 打开默认路径为工程的路径
					}
					chooser.setDialogTitle("导出节点节点信息表");
					chooser.setAcceptAllFileFilterUsed(false);
					// 前一个xls为窗口下拉选择文件类型显示，后一个是文件过滤器的文件类型
					FileNameExtensionFilter filter = new FileNameExtensionFilter("xls", "xls");
					chooser.setFileFilter(filter);
					// 获取当前时间
					Calendar cal = Calendar.getInstance();
					SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd");
					String date = dateformat.format(cal.getTime());
					JTextField text = getTextField(chooser);// 获取输入文件名部分
					text.setText("节点单断信息表" + date);// 设置默认文件名
					chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					int option = chooser.showSaveDialog(chooser);
					if (option == JFileChooser.APPROVE_OPTION) {// 如果点击确定
						File file = chooser.getSelectedFile();
						filelist = file.getPath();
						if (!(filelist.endsWith(".xls")))// 如果现在的文件名里不包含扩展名，就给其加上
							filelist = filelist + ".xls";
						file = new File(filelist);
						if (file.exists()) {// 如果这个文件已经存在了
							// JOptionPane.showMessageDialog(null, "当前文件已存存在，请删除或重命名后再导出");}
							int test = JOptionPane.showConfirmDialog(null, "当前文件已存在，是否覆盖？");
							switch (test) {
							case 0:// 覆盖
									// PortDataBase portOut = new PortDataBase();
									// int id = Integer
									// .parseInt(String.valueOf(nodeModel.getValueAt(nodeTable.getSelectedRow(),
									// 0)));
									// theNode = CommonNode.getNode(id);
								try {
									for (int i = 0; i < CommonNode.allNodeList.size(); ++i) {
										CommonNode node = CommonNode.allNodeList.get(i);
										List<Traffic> tralist = Evaluation.nodeEvaluation(node);
										if(tralist!=null&&tralist.size()!=0) {
											for(Traffic tra:tralist) {
												if (tra.getFaultType()!=0)
												{Evaluation.PutOutTraffic.add(tra);}
											}
										}
										NodeDataBase jd=new NodeDataBase();
										jd.NodeDanOutPut(filelist, node);
										NodeDataBase.index = 1;
										Evaluation.PutOutTraffic.clear();
										Evaluation.clearRsmRoute(tralist);
									}
									
									JOptionPane.showMessageDialog(null, "表格已覆盖");
								} catch (Exception e1) {
									JOptionPane.showMessageDialog(null, "请关闭原Excel文件，否则无法完成覆盖");
								}
//								setVisible(false);
//								dispose();
								break;
							case 1:
								return;
							case 2:
								return;
							}
						} else {
							for (int i = 0; i < CommonNode.allNodeList.size(); ++i) {
								CommonNode node = CommonNode.allNodeList.get(i);
								List<Traffic> tralist = Evaluation.nodeEvaluation(node);
								if(tralist!=null&&tralist.size()!=0) {
									for(Traffic tra:tralist) {
										if (tra.getFaultType()!=0)
										{Evaluation.PutOutTraffic.add(tra);}
									}
								}
								NodeDataBase jd=new NodeDataBase();
								jd.NodeDanOutPut(filelist, node);
								NodeDataBase.index = 1;
								Evaluation.PutOutTraffic.clear();
								Evaluation.clearRsmRoute(tralist);
							}
							JOptionPane.showMessageDialog(null, "数据已导出");
						}

					}
				}
			}
		});
	}
	
	public static void nodelistEvaluation(List<CommonNode> nodelist) {
		Evaluation.nodeCutoff = 0;
		Evaluation.nodeKeep = 0;
		Evaluation.nodeDown = 0;
		Evaluation.refresh();
		for (int i = 0; i < nodelist.size(); ++i) {
			CommonNode node = nodelist.get(i);
			List<Traffic> tralist = Evaluation.nodeEvaluation(node);
			if(tralist!=null&&tralist.size()!=0) {
				for(Traffic tra:tralist) {
					if (tra.getFaultType()!=0)
					{Evaluation.PutOutTraffic.add(tra);}
				}
			}
			NodeDataBase jd=new NodeDataBase();
			jd.OutPutNodeDan(node);
			Evaluation.PutOutTraffic.clear();
			Evaluation.clearRsmRoute(tralist);
		}
		if(NodeDataBase.index!=2) {
			JOptionPane.showMessageDialog(null, "数据已导出");
			}
			NodeDataBase.index=0;
        for(Traffic tra:Traffic.getTrafficList()) {
        	tra.setFaultType(0);
        }
	}
	
	
	public static void listEvaluation(LinkedList<FiberLink> allLinkList) {
		Evaluation.linkCutoff = 0;
		Evaluation.linkKeep = 0;
		Evaluation.linkDown = 0;
		Evaluation.refresh();
		for (int i = 0; i < allLinkList.size(); ++i) {
			FiberLink fl = allLinkList.get(i);
			// this.linkEvaluation(fl,isSRLG);
			List<Traffic> tralist = Evaluation.linkEvaluation(fl);
			if(tralist!=null&&tralist.size()!=0) {
				for(Traffic tra:tralist) {
//					if (tra.getFaultType()!=0)
					Evaluation.PutOutTraffic.add(tra);
				}
			}
			TrafficDatabase lldd=new TrafficDatabase();
			lldd.OutPutRoute(fl);
			Evaluation.PutOutTraffic.clear();
			Evaluation.clearRsmRoute(tralist);
		}
		if(TrafficDatabase.index!=2) {
			JOptionPane.showMessageDialog(null, "数据已导出");
			}
			TrafficDatabase.index=0;
		for(Traffic tra:Traffic.getTrafficList()) {
			tra.setFaultType(0);
		}
	}

	private void TrafficOutPut(String str) {
		HSSFWorkbook wb = null;
		HSSFSheet sheet1 = null;
		HSSFSheet sheet2 = null;
		JRadioButton[] temp = new JRadioButton[2];//用于暂时存储 选择重路由策略
		temp=Dlg_PolicySetting.getArithmetic_new();
		try {
			// fs = new POIFSFileSystem(new FileInputStream(str));
			wb = new HSSFWorkbook();
			sheet1 = wb.createSheet("工作路由");
			if (temp[0].isSelected()) {// 选择了预置重路由的规划方法
				sheet2 = wb.createSheet("预置恢复路由");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		HSSFRow row1 = sheet1.createRow(0);
		HSSFCell cell1 = row1.createCell(0);
		cell1.setCellValue("业务编号");
		cell1 = row1.createCell(1);
		cell1.setCellValue("起点");
		cell1 = row1.createCell(2);
		cell1.setCellValue("终点");
		cell1 = row1.createCell(3);
		cell1.setCellValue("电中继节点");
		cell1 = row1.createCell(4);
		cell1.setCellValue("名称");
		cell1 = row1.createCell(5);
		cell1.setCellValue("中心频率");
		cell1 = row1.createCell(6);
		cell1.setCellValue("谱宽");
		// cell1 = row1.createCell(7);
		// cell1.setCellValue("业务终点");
		cell1 = row1.createCell(7);
		cell1.setCellValue("ODU2序号");
		cell1 = row1.createCell(8);
		cell1.setCellValue("ODU1序号");
		cell1 = row1.createCell(9);
		cell1.setCellValue("长度（km）");
		cell1 = row1.createCell(10);
		cell1.setCellValue("等效跨段数量");
		cell1 = row1.createCell(11);
		cell1.setCellValue("正向OSNR");
		cell1 = row1.createCell(12);
		cell1.setCellValue("反向ODNR");
		cell1 = row1.createCell(13);
		cell1.setCellValue("OSNR容限");
		cell1 = row1.createCell(14);
		cell1.setCellValue("DGD值");
		cell1 = row1.createCell(15);
		cell1.setCellValue("DGD容限");
		cell1 = row1.createCell(16);
		cell1.setCellValue("Pre-FEC BER值");
		cell1 = row1.createCell(17);
		cell1.setCellValue("Q值");

		System.out.println(Traffic.trafficList);

		for (int i = 0; i < data.Traffic.trafficList.size(); i++) {
			Traffic tra = data.Traffic.trafficList.get(i);
			row1 = sheet1.createRow(i + 1);
			cell1 = row1.createCell(0);
//			if(tra.getTrafficgroup()==null) {
//				cell1.setCellValue("常规-"+(i+1));
//			}else {
//				cell1.setCellValue(tra.getTrafficgroup().getTheName()+tra.getNumRank());
//			}
			cell1.setCellValue(tra.getTrafficId());
			cell1 = row1.createCell(1);
			cell1.setCellValue(tra.getFromNode().getName());
			cell1 = row1.createCell(2);
			cell1.setCellValue(tra.getToNode().getName());
			//电中继
			cell1 = row1.createCell(3);
			if (tra.getWorkRoute() != null) {
			cell1.setCellValue(tra.getWorkRoute().waveChangedNode());}
			//路由
			cell1 = row1.createCell(4);
			cell1.setCellValue(tra.getWorkRoute().toString());
			//使用波道
			cell1 = row1.createCell(5);
			if (tra.getWorkRoute() != null) {
				//cell1.setCellValue(tra.getWorkRoute().getWaveLengthIdList().toString());
				cell1.setCellValue(tra.getWorkRoute().toStingwave());
			}
			//长度
			cell1 = row1.createCell(9);
			if (tra.getWorkRoute() != null) {
				cell1.setCellValue(tra.getWorkRoute().routelength());
			}
			//等效跨段数
			cell1 = row1.createCell(10);
			if (tra.getWorkRoute() != null) {
				cell1.setCellValue(OSNR.crossSum(tra.getWorkRoute()));
			}
			//正向OSNR(OSNR模拟值)
			cell1 = row1.createCell(11);
			if (tra.getWorkRoute() != null) {
				cell1.setCellValue(OSNR.calculateOSNR(tra.getWorkRoute()));
			}
			//OSNR容限
			cell1 = row1.createCell(13);
			if (tra.getWorkRoute() != null) {
				cell1.setCellValue(OSNR.crossOSNR(tra.getWorkRoute()));
			}
			
			
		}
		try {
			HSSFRow row2 = sheet2.createRow(0);
			HSSFCell cell2 = row2.createCell(0);
			cell2.setCellValue("业务编号");
			cell2 = row2.createCell(1);
			cell2.setCellValue("起点");
			cell2 = row2.createCell(2);
			cell2.setCellValue("终点");
			cell2 = row2.createCell(3);
			cell2.setCellValue("电中继节点");
			cell2 = row2.createCell(4);
			cell2.setCellValue("名称");
			cell2 = row2.createCell(5);
			cell2.setCellValue("中心频率");
			cell2 = row2.createCell(6);
			cell2.setCellValue("谱宽");
			cell2 = row2.createCell(7);
			cell2.setCellValue("ODU2序号");
			cell2 = row2.createCell(8);
			cell2.setCellValue("ODU1序号");
			cell2 = row2.createCell(9);
			cell2.setCellValue("长度（km）");
			cell2 = row2.createCell(10);
			cell2.setCellValue("等效跨段数量");
			cell2 = row2.createCell(11);
			cell2.setCellValue("正向OSNR");
			cell2 = row2.createCell(12);
			cell2.setCellValue("反向ODNR");
			cell2 = row2.createCell(13);
			cell2.setCellValue("OSNR容限");
			cell2 = row2.createCell(14);
			cell2.setCellValue("DGD值");
			cell2 = row2.createCell(15);
			cell2.setCellValue("DGD容限");
			cell2 = row2.createCell(16);
			cell2.setCellValue("Pre-FEC BER值");
			cell2 = row2.createCell(17);
			cell2.setCellValue("Q值");

			System.out.println(Traffic.trafficList);

			for (int i = 0; i < data.Traffic.trafficList.size(); i++) {
				Traffic tra = data.Traffic.trafficList.get(i);
				row2 = sheet2.createRow(i + 1);
				cell2 = row2.createCell(0);
//				if(tra.getTrafficgroup()==null) {
//					cell2.setCellValue("常规-"+(i+1));
//				}else {
//					cell2.setCellValue(tra.getTrafficgroup().getTheName()+tra.getNumRank());
//				}
				cell2.setCellValue(tra.getTrafficId());
				cell2 = row2.createCell(1);
				cell2.setCellValue(tra.getFromNode().getName());
				cell2 = row2.createCell(2);
				cell2.setCellValue(tra.getToNode().getName());
				//电中继
				cell2 = row2.createCell(3);
				if (tra.getPreRoute() != null) {
				cell2.setCellValue(tra.getPreRoute().waveChangedNode());}
				//路由
				cell2 = row2.createCell(4);
				if (tra.getPreRoute() != null) {
					cell2.setCellValue(tra.getPreRoute().toString());
				}
				//使用波道
				cell2 = row2.createCell(5);
				if (tra.getPreRoute() != null) {
					//cell2.setCellValue(tra.getPreRoute().getWaveLengthIdList().toString());
					cell2.setCellValue(tra.getPreRoute().toStingwave());
				}
				//长度
				cell2 = row2.createCell(9);
				if (tra.getPreRoute()!= null) {
					cell2.setCellValue(tra.getPreRoute().routelength());
				}
				
				//等效跨段数
				cell2 = row2.createCell(10);
				if (tra.getPreRoute()!= null) {
					cell2.setCellValue(OSNR.crossSum(tra.getPreRoute()));
				}
				//正向OSNR(OSNR模拟值)
				cell2 = row2.createCell(11);
				if (tra.getPreRoute()!= null) {
					cell2.setCellValue(OSNR.calculateOSNR(tra.getPreRoute()));
				}
				//OSNR容限
				cell2 = row2.createCell(13);
				if (tra.getPreRoute()!= null) {
					cell2.setCellValue(OSNR.crossOSNR(tra.getPreRoute()));
				}
				
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
		}

		FileOutputStream os = null;
		try {
			os = new FileOutputStream(str);
		} catch (FileNotFoundException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		try {
			wb.write(os);
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
		try {
			os.close();
		} catch (IOException e) {
			// TODO 自动生成 catch 块
			e.printStackTrace();
		}
	}
		
	
	protected JTextField getTextField(Container c) {
		// TODO Auto-generated method stub
		JTextField text = null;
		for (int i = 0; i < c.getComponentCount(); i++) {
			Component cnt = c.getComponent(i);
			if (cnt instanceof JTextField) {
				return (JTextField) cnt;
			}
			if (cnt instanceof Container) {
				text = getTextField((Container) cnt);
				if (text != null) {
					return text;
				}
			}
		}
		return text;
	}

	public static void main(String[] args) {
		NetDesign_zs frame = new NetDesign_zs(1);
		// frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setTitle(" 光网络智能规划平台");
		// frame.setIconImage((new ImageIcon("button/ddd1.png")).getImage());
		ImageIcon imageIcon = new ImageIcon(frame.getClass().getResource("/resource/ddd1111.png"));
		frame.setIconImage(imageIcon.getImage());
		Toolkit tl = Toolkit.getDefaultToolkit();
		Dimension screenSize = tl.getScreenSize();
		frame.setSize((int) screenSize.getWidth(), (int) screenSize.getHeight() - 40);
		frame.setVisible(true);

	}
	
	//Internal class to implement the progress bar
		class Progress extends Thread{
		    private final int[]   progressValue = new int[101];
		    private JProgressBar progressBar;
		    private JFrame frame;
		    public Progress(JProgressBar progressBar,JFrame j1)
		    {
		        this.progressBar = progressBar;
		        this.frame=j1;
		    }
		    public void run(){
		    	
		    	for(int j=0;j<101;j++) {
		    	   progressValue[j]=j;
		        }
		    	progressBar.setValue(progressValue[1]);
		    	while(true) {
		    		try
	                {
	                Thread.sleep(500);
	                }catch(Exception e)
	                {
	                e.printStackTrace();
	                }
		    		if(NetDesign_zs.processValue==0) progressBar.setValue(progressValue[1]);
		    		else {
		    		progressBar.setValue(progressValue[NetDesign_zs.processValue]);
		    		//System.out.println(UserInterface.Process);
		    		if(NetDesign_zs.processValue==100) {
		    			NetDesign_zs.processValue=0;
		    			break;
		    		 }
		    		}
		    	}
		        
		        progressBar.setIndeterminate(false);  
		       // progressBar.setIndeterminate(true);   //
		        progressBar.setString("表格已导出");  //鎻愮ず淇℃伅
		        try
	            {
	                Thread.sleep(1000);
	            }catch(Exception e)
	            {
	                e.printStackTrace();
	            }
		       
		        progressBar.setVisible(false);
		        //ResultsPresent.remove(progressBar);
		        frame.dispose();
		    }
		}

}
