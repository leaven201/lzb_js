package dialog;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ListIterator;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import data.BasicLink;
import data.CommonNode;
import data.FiberLink;
import data.OTNLink;
import data.WDMLink;
import dataControl.NodeData;
import datastructure.UIFiberLink;
import datastructure.UINode;
import datastructure.UIOTNLink;
import datastructure.UIWDMLink;
import design.NetDesign_zs;
import enums.Layer;
import twaver.Dummy;
import twaver.TDataBox;

//完成数据导入 filelist 为导入文件的路径文件名
public class Dlg_InputNodes extends JFrame {
	private static String filelist = NetDesign_zs.openfilelist;
	private static TDataBox box;
	private static Dummy nodeDummy;
	private static Dummy FIBERlinkDummy;
	private static Dummy OPTLinkDummy;
	private static Dummy ELELinkDummy;
	private static Dummy TrafficDummy;

	public Dlg_InputNodes(TDataBox box1, Dummy nodeDummy1, Dummy FIBERlinkDummy1, Dummy optLinkDummy1,
			Dummy eleLinkDummy1, Dummy TrafficDummy1) {
		setSize(350, 200);
		box = box1;
		nodeDummy = nodeDummy1;
		FIBERlinkDummy = FIBERlinkDummy1;
		OPTLinkDummy = optLinkDummy1;
		ELELinkDummy = eleLinkDummy1;
		TrafficDummy = TrafficDummy1;

		new Thread() {
			public void run() {
//				if (CommonNode.allNodeList.size() !=0 && CommonNode.allNodeList!=null) {// 若路由不为空
//					int test = JOptionPane.showConfirmDialog(null, "节点已导入，是否清空所有数据？");
//					switch (test) {
//					case 0:// 清空路由
//						CommonNode.allNodeList.clear();;
//						BasicLink.allLinkList.clear();
//						Traffic.trafficList.clear();
//						break;
//					case 1:// 不清空
//
//						break;
//					case 2:
//						break;
//					}
//				} else {// 路由为空
					try {
						JFileChooser chooser = new JFileChooser();
						if (filelist.equals("nothing")) {
							chooser = new JFileChooser();
						} else {
							chooser = new JFileChooser(filelist);
						}
						chooser.setDialogTitle("网元导入");
						chooser.setAcceptAllFileFilterUsed(false);
						FileFilter filter = new FileNameExtensionFilter("Excel文件", "xls");
						chooser.setFileFilter(filter);
						int option = chooser.showOpenDialog(Dlg_InputNodes.this);
						if (option == JFileChooser.APPROVE_OPTION) {
							File file = chooser.getSelectedFile();
							if (file.exists()) {
								filelist = file.getPath();
							} else {
							}
							File txtfile = new File(System.getProperty("user.dir") + "\\文件读取位置.txt");
							txtfile.delete();
							txtfile.createNewFile();
							String sets = "attrib +H \"" + txtfile.getAbsolutePath() + "\"";
							// 输出命令串
							System.out.println(sets);
							// 运行命令串
							Runtime.getRuntime().exec(sets);
							NetDesign_zs.openfilelist = file.getParent().toString();
							PrintWriter pw;
							try {
								pw = new PrintWriter(new FileWriter("文件读取位置.txt"));
								pw.println(NetDesign_zs.openfilelist + "\r\n" + NetDesign_zs.openproject);
								pw.close();
							} catch (IOException e2) {
								// TODO Auto-generated catch block
								e2.printStackTrace();
							}

							// 初始化进度条
							final Dlg_ProgressBar pro = new Dlg_ProgressBar();
							pro.setIndeterminate();
							pro.setVal(0);
							pro.setVisible(true);

							System.out.println("filelistfilelist       " + filelist);
							NodeData node = new NodeData(); // 后台导入节点资源
							node.inputNode(filelist);
							// PortData port =new PortData(); //后台导入端口资源
							// port.inputPort(filelist);
							// LinkData link =new LinkData(); //后台导入链路资源
							// link.inputLink(filelist);
							// TrafficData traffic=new TrafficData(); //后台导入业务
							// traffic.inputTraffic(filelist);
							// inputUITrafficData(box,TrafficDummy);

							// 关闭进度条窗口
							pro.setVal(100);

							Dlg_InputInfo input = new Dlg_InputInfo(box, nodeDummy, FIBERlinkDummy, OPTLinkDummy,
									ELELinkDummy, TrafficDummy, 0, 1);// 弹出消息窗
							// NetDesign.printout(LinkData.s_sSatelliteMsg);
							// NetDesign.printout(LinkData.s_sShortWaveMsg);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
//				} // else
			}

		}.start();

	}

	// fiber
	public static void inputUINodeData(TDataBox box, Dummy nodeDummy) {
		// 后台节点数据导入前台
		ListIterator<CommonNode> it = CommonNode.allNodeList.listIterator();

		if (UINode.s_lUINodeList.isEmpty()) {
			while (it.hasNext()) {
				UINode.s_lUINodeList.add(new UINode(it.next()));
			}
			ListIterator<UINode> ituinode = UINode.s_lUINodeList.listIterator();
			while (ituinode.hasNext()) {

				UINode thenode = ituinode.next();
				box.addElement(thenode);
				nodeDummy.addChild(thenode);
			}
		} else {
			System.out.println("节点数据已导入");
		}
		return;
	}

	/**
	 * 不同级别节点之间连接的链路颜色设置（单条链路）
	 */
	public static void setthisTypeColor(BasicLink link) {// 给传递过来的link设置颜色
		Layer layer = link.getLinkLayer();
		switch (layer) {
		case Fiber:
			UIFiberLink thefiber = UIFiberLink.getUIFiberLink(link.getName());
			thefiber.setthisColor(Color.BLUE);
			break;
		case WDM:
			UIWDMLink optllite = UIWDMLink.getUIOptLink(link.getName());
			optllite.setthisColor(Color.GREEN);
			break;
		case OTN:
			UIOTNLink theShortWave = UIOTNLink.getUIEleLink(link.getName());
			theShortWave.setthisColor(Color.ORANGE);
		}
	}

	/**
	 * 不同级别节点之间连接的链路颜色设置
	 */
	public static void setTypeColor() {// 给不同类型的link设置颜色
		for (int i = 0; i < UIFiberLink.s_lUIFiberLinkList.size(); i++) {
			UIFiberLink thelink = UIFiberLink.s_lUIFiberLinkList.get(i);
			FiberLink thefiber = thelink.getFiberLink();
			thelink.setthisColor(Color.BLUE);
		}

		for (int i = 0; i < UIWDMLink.s_lUIWDMLinkList.size(); i++) {
			UIWDMLink thelink = UIWDMLink.s_lUIWDMLinkList.get(i);
			WDMLink theOptLink = thelink.getOptLink();
			thelink.setthisColor(Color.GREEN);
		}

		for (int i = 0; i < UIOTNLink.s_lUIEleLinkList.size(); i++) {
			UIOTNLink thelink = UIOTNLink.s_lUIEleLinkList.get(i);
			OTNLink theShortWave = thelink.getEleLink();
			thelink.setthisColor(Color.ORANGE);
		}
	}
}

