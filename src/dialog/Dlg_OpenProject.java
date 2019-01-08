package dialog;

import java.awt.Color;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ListIterator;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import twaver.Dummy;
import twaver.Node;
import twaver.TDataBox;
import twaver.TWaverConst;
import twaver.network.background.ImageBackground;
import data.*;
import database.Database;
import datastructure.*;
import datastructure.UINode;
import datastructure.UIWDMLink;
import design.NetDesign_zs;

//完成数据导入 filelist 为导入文件的路径文件名
public class Dlg_OpenProject extends JFrame {
	private static String filelist = "nothing";
	private String project = "nothing";
	private JFileChooser chooser = new JFileChooser();
	private String icon;

	public Dlg_OpenProject(TDataBox box1, Dummy nodeDummy, Dummy FiberLinkDummy, Dummy OptLinkDummy, Dummy EleLinkDummy,
			Dummy TrafficDummy) {
		setSize(350, 200);
		box1.setName("数据库");
		StringBuffer result = new StringBuffer();
		String resultlong = new String();
		resultlong = ReadData.ReadData(result).toString();
		int index1 = resultlong.indexOf("\n");
		NetDesign_zs.openfilelist = resultlong.substring(0, index1);
		NetDesign_zs.openproject = resultlong.substring(index1 + 1, resultlong.length());
		project = NetDesign_zs.openproject;
		if (project.equals("nothing")) {
			chooser = new JFileChooser();
		} else {
			chooser = new JFileChooser(project);
		}
		chooser.setDialogTitle("打开工程");
		chooser.setAcceptAllFileFilterUsed(false);
		FileFilter filter = new FileNameExtensionFilter("dat", "dat");
		chooser.setFileFilter(filter);
		int option = chooser.showOpenDialog(Dlg_OpenProject.this);
		if (option == JFileChooser.APPROVE_OPTION) {
			File file = chooser.getSelectedFile();
			if (file.exists()) {
				filelist = file.getPath();
				project = file.getParentFile().getParent();
				System.out.println("filelist：  " + filelist);
				try {
					Database.deserialize(filelist);
					NetDesign_zs.fileinfo = Database.getFileProperty();
					// NetDesign_zs.printout("打开 " +filelist);
				} catch (Exception e) {
					NetDesign_zs.printout("工程打开异常");
				}
				NetDesign_zs.filenameall = filelist;
				NetDesign_zs.filepath = file.getParent();
				NetDesign_zs.filename = file.getName();
				NetDesign_zs.openproject = project;
				int endIndex = NetDesign_zs.filename.length() - 4;
				box1.setName(NetDesign_zs.filename.substring(0, endIndex));
				PrintWriter pw;
				try {
					File txtfile = new File(System.getProperty("user.dir") + "\\文件读取位置.txt");
					txtfile.delete();
					txtfile.createNewFile();
					String sets = "attrib +H \"" + txtfile.getAbsolutePath() + "\"";
					// 输出命令串
					System.out.println(sets);
					// 运行命令串
					Runtime.getRuntime().exec(sets);
					pw = new PrintWriter(new FileWriter("文件读取位置.txt"));
					pw.println(NetDesign_zs.openfilelist + "\r\n" + NetDesign_zs.openproject);
					pw.close();
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			} else {
			}
			// NetDesign_zs.setNodeLevelSelect(0, true);
			// NetDesign_zs.setNodeLevelSelect(1, false);
			// NetDesign_zs.setNodeLevelSelect(2, false);

			// 后台节点数据导入前台
			ListIterator<CommonNode> it = CommonNode.allNodeList.listIterator();

			if (UINode.s_lUINodeList.isEmpty()) {
				while (it.hasNext()) {
					UINode.s_lUINodeList.add(new UINode(it.next()));
				}
				ListIterator<UINode> ituinode = UINode.s_lUINodeList.listIterator();
				while (ituinode.hasNext()) {

					UINode thenode = ituinode.next();
					box1.addElement(thenode);
					nodeDummy.addChild(thenode);

					// if(Network.searchNetwork(thenode.getCommonNode().getM_nSubnetNum())!=null){
					// if(Network.searchNetwork(thenode.getCommonNode().getM_nSubnetNum()).getM_nNetType()
					// == 1){
					// icon = "/resource/node-1.png";
					// thenode.setImage(icon);
					// }
					// else
					// if(Network.searchNetwork(thenode.getCommonNode().getM_nSubnetNum()).getM_nNetType()
					// == 2){
					// icon = "/resource/node-2.png";
					// thenode.setImage(icon);
					// }
				}
			}
		} else {
			System.out.println("节点数据已导入");
		}
		// 后台链路数据导入前台
		// fiber

		ListIterator<FiberLink> itFiber = FiberLink.fiberLinkList.listIterator();
		if (UIFiberLink.s_lUIFiberLinkList.isEmpty()) {
			while (itFiber.hasNext()) {
				UIFiberLink.s_lUIFiberLinkList.add(new UIFiberLink(itFiber.next()));
			}
			System.out.println(FiberLink.fiberLinkList);
			ListIterator<UIFiberLink> itUIFiberLink = UIFiberLink.s_lUIFiberLinkList.listIterator();
			while (itUIFiberLink.hasNext()) {
				UIFiberLink thelink = itUIFiberLink.next();
				System.out.println("link" + thelink.getName());
				box1.addElement(thelink);
				FiberLinkDummy.addChild(thelink);
			}
		} else {
			System.out.println("FIBER数据已导入");
		}
		// WDM
		ListIterator<WDMLink> itWDM = WDMLink.WDMLinkList.listIterator();
		if (UIWDMLink.s_lUIWDMLinkList.isEmpty()) {
			while (itWDM.hasNext()) {
				UIWDMLink.s_lUIWDMLinkList.add(new UIWDMLink(itWDM.next()));
			}
			ListIterator<UIWDMLink> itUIWDMLink = UIWDMLink.s_lUIWDMLinkList.listIterator();
			while (itUIWDMLink.hasNext()) {
				UIWDMLink thelink = itUIWDMLink.next();
				box1.addElement(thelink);
				OptLinkDummy.addChild(thelink);
			}
		} else {
		}
		// OTN
		ListIterator<OTNLink> itOTN = OTNLink.OTNLinkList.listIterator();
		if (OTNLink.OTNLinkList.isEmpty()) {
			while (itOTN.hasNext()) {
				OTNLink.OTNLinkList.add(new OTNLink(itOTN.next()));
			}
			ListIterator<OTNLink> itUIOTNLink = OTNLink.OTNLinkList.listIterator();
			while (itUIOTNLink.hasNext()) {
				UIWDMLink thelink = OTNLink.next();
				box1.addElement(thelink);
				EleLinkDummy.addChild(thelink);
			}
		} else {
		}

		// 业务导入前台

		if (Traffic.trafficList != null && Traffic.trafficList.size() != 0) {
			for(int i = 0;i <Traffic.trafficList.size();i++){
	    	    Node thetraffic = new Node(Traffic.trafficList.get(i).getId()){
	    	    	public int getHeight() {
	    	    		return 0;
	    	    		}
	    	    		public int getWidth() {
	    	    		return 0;
	    	    		}
	    	    };
	    	    thetraffic.setName(Traffic.trafficList.get(i).getFromNode().getName()+"-"+Traffic.trafficList.get(i).getToNode().getName()+" "+i);
//	    	    thetraffic.setImage(TWaverConst.BLANK_IMAGE);
//	    	    TUIManager.registerWithoutImage(thetraffic.getClass());
	    	    thetraffic.setLocation(99999999, 999999999);

	    	    box1.addElement(thetraffic);
	    	    TrafficDummy.addChild(thetraffic);
//	    	    thetraffic.setVisible(false);
	      }
		}
	}
}
