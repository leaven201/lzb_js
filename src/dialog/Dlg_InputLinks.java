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
import data.FiberLink;
import data.OTNLink;
import data.WDMLink;
import dataControl.LinkData;
import datastructure.UIFiberLink;
import datastructure.UIOTNLink;
import datastructure.UIWDMLink;
import design.NetDesign_zs;
import enums.Layer;
import twaver.Dummy;
import twaver.TDataBox;

//������ݵ��� filelist Ϊ�����ļ���·���ļ���
public class Dlg_InputLinks extends JFrame {
	private static String filelist = NetDesign_zs.openfilelist;
	private static TDataBox box;
	private static Dummy nodeDummy;
	private static Dummy FIBERlinkDummy;
	private static Dummy EleLinkDummy;
	private static Dummy OptLinkDummy;
	private static Dummy TrafficDummy;

	public Dlg_InputLinks(TDataBox box1, Dummy nodeDummy1, Dummy FIBERlinkDummy1, Dummy ShortWaveLinkDummy1,
			Dummy SatelliteLinkDummy1, Dummy TrafficDummy1) {
		setSize(350, 200);
		box = box1;
		nodeDummy = nodeDummy1;
		FIBERlinkDummy = FIBERlinkDummy1;
		EleLinkDummy = ShortWaveLinkDummy1;
		OptLinkDummy = SatelliteLinkDummy1;
		TrafficDummy = TrafficDummy1;

		new Thread() {
			public void run() {
				try {
					JFileChooser chooser = new JFileChooser();
					if (filelist.equals("nothing")) {
						chooser = new JFileChooser();
					} else {
						chooser = new JFileChooser(filelist);
					}
					chooser.setDialogTitle("��Ԫ����");
					chooser.setAcceptAllFileFilterUsed(false);
					FileFilter filter = new FileNameExtensionFilter("Excel�ļ�", "xls");
					chooser.setFileFilter(filter);
					int option = chooser.showOpenDialog(Dlg_InputLinks.this);
					if (option == JFileChooser.APPROVE_OPTION) {
						File file = chooser.getSelectedFile();
						if (file.exists()) {
							filelist = file.getPath();
						} else {
						}
						File txtfile = new File(System.getProperty("user.dir") + "\\�ļ���ȡλ��.txt");
						txtfile.delete();
						txtfile.createNewFile();
						String sets = "attrib +H \"" + txtfile.getAbsolutePath() + "\"";
						// ������
						System.out.println(sets);
						// �������
						Runtime.getRuntime().exec(sets);
						NetDesign_zs.openfilelist = file.getParent().toString();
						PrintWriter pw;
						try {
							pw = new PrintWriter(new FileWriter("�ļ���ȡλ��.txt"));
							pw.println(NetDesign_zs.openfilelist + "\r\n" + NetDesign_zs.openproject);
							pw.close();
						} catch (IOException e2) {
							// TODO Auto-generated catch block
							e2.printStackTrace();
						}

						// ��ʼ��������
						final Dlg_ProgressBar pro = new Dlg_ProgressBar();
						pro.setIndeterminate();
						pro.setVal(0);
						pro.setVisible(true);

						System.out.println("filelistfilelist       " + filelist);
						// NodeData node = new NodeData(); //��̨����ڵ���Դ
						// node.inputNode(filelist);
						// PortData port =new PortData(); //��̨����˿���Դ
						// port.inputPort(filelist);
						LinkData link = new LinkData(); // ��̨������·��Դ
						link.inputLink(filelist);
						// TrafficData traffic=new TrafficData(); //��̨����ҵ��
						// traffic.inputTraffic(filelist);
						// inputUITrafficData(box,TrafficDummy);

						// �رս���������
						pro.setVal(100);

						Dlg_InputInfo input = new Dlg_InputInfo(box, nodeDummy, FIBERlinkDummy, EleLinkDummy,
								OptLinkDummy, TrafficDummy, 0, 0);// ������Ϣ��
						// NetDesign.printout(LinkData.s_sShortWaveMsg);
						// NetDesign.printout(LinkData.s_sSatelliteMsg);
						// Netdesign.printout(TrafficData.s_sTrafficMsg);
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

		}.start();

	}
	// fiber

	public static void inputUILinkData(TDataBox box, Dummy linkDummy, Dummy WDMLinkDummy,
			Dummy OTNLinkDummy) {
		// fiber
		ListIterator<FiberLink> itFiber = FiberLink.fiberLinkList.listIterator();
		if (UIFiberLink.s_lUIFiberLinkList.isEmpty()) {
			while (itFiber.hasNext()) {
				UIFiberLink.s_lUIFiberLinkList.add(new UIFiberLink(itFiber.next()));
			}
			ListIterator<UIFiberLink> itUIFiberLink = UIFiberLink.s_lUIFiberLinkList.listIterator();
			while (itUIFiberLink.hasNext()) {
				UIFiberLink thelink = itUIFiberLink.next();
				box.addElement(thelink);
				linkDummy.addChild(thelink);
			}
		} else {
			System.out.println("FIBER�����ѵ���");
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
				box.addElement(thelink);
				WDMLinkDummy.addChild(thelink);
			}
		} else {
			System.out.println("WDM �����ѵ���");
		}
		
		// OTN
		ListIterator<OTNLink> itEle = OTNLink.OTNLinkList.listIterator();
		if (UIOTNLink.s_lUIEleLinkList.isEmpty()) {
			while (itEle.hasNext()) {
				UIOTNLink.s_lUIEleLinkList.add(new UIOTNLink(itEle.next()));
			}
			ListIterator<UIOTNLink> itUIEleLink = UIOTNLink.s_lUIEleLinkList.listIterator();
			while (itUIEleLink.hasNext()) {
			    UIOTNLink thelink = itUIEleLink.next();
				box.addElement(thelink);
				OTNLinkDummy.addChild(thelink);
			}
		} else {
			System.out.println("OTN�����ѵ���");
		}


	}

	/**
	 * ��ͬ��·��ɫ���ã�������·�� ������·��ɫ
	 */
	public static void setthisTypeColor(BasicLink link) {
		Layer type = link.getLinkLayer();
		switch (type) {
		case Fiber:
			UIFiberLink thefiber = UIFiberLink.getUIFiberLink(link.getName());
			thefiber.setthisColor(Color.BLUE);
			// else if(2 == from && 2 == to){
			// thefiber.setthisColor(Color.BLUE);
			// }
			// else if(3 == from && 3 == to){
			// thefiber.setthisColor(Color.YELLOW);
			// }
			// else if((1 == from && 2 == to)||(2 == from && 1 == to)){
			// thefiber.setthisColor(Color.CYAN);
			// }
			// else if((3 == from && 2 == to)||(2 == from && 3 == to)){
			// thefiber.setthisColor(Color.GRAY);
			// }
			break;
		case OTN:
			UIOTNLink theShortWave = UIOTNLink.getUIEleLink(link.getName());
			// theShortWave.setthisColor(Color.GREEN);
			// else if(2 == from && 2 == to){
			theShortWave.setthisColor(Color.GREEN);
			// }
			// else if(3 == from && 3 == to){
			// theShortWave.setthisColor(Color.YELLOW);
			// }
			// else if((1 == from && 2 == to)||(2 == from && 1 == to)){
			// theShortWave.setthisColor(Color.CYAN);
			// }
			// else if((3 == from && 2 == to)||(2 == from && 3 == to)){
			// theShortWave.setthisColor(Color.GRAY);
			// }
			break;

		case WDM:
			UIWDMLink theSatellite = UIWDMLink.getUIOptLink(link.getName());
			// theSatellite.setthisColor(Color.GREEN);
			// else if(2 == from && 2 == to){
			// theSatellite.setthisColor(Color.BLUE);
			// }
			// else if(3 == from && 3 == to){
			theSatellite.setthisColor(Color.ORANGE);
			// }
			// else if((1 == from && 2 == to)||(2 == from && 1 == to)){
			// theSatellite.setthisColor(Color.CYAN);
			// }
			// else if((3 == from && 2 == to)||(2 == from && 3 == to)){
			// theSatellite.setthisColor(Color.GRAY);
			// }
			// break;
		}
	}

	/**
	 * ��ͬ����ڵ�֮�����ӵ���·��ɫ����
	 */
	public static void setTypeColor() {
		for (int i = 0; i < UIFiberLink.s_lUIFiberLinkList.size(); i++) {// ����fiber��·��ɫ
			UIFiberLink thelink = UIFiberLink.s_lUIFiberLinkList.get(i);
			FiberLink thefiber = thelink.getFiberLink();
			thelink.setthisColor(Color.BLUE);
			// else if(2 == from && 2 == to){
			// thelink.setthisColor(Color.BLUE);
			// }
			// else if(3 == from && 3 == to){
			// thelink.setthisColor(Color.YELLOW);
			// }
			// else if((1 == from && 2 == to)||(2 == from && 1 == to)){
			// thelink.setthisColor(Color.CYAN);
			// }
			// else if((3 == from && 2 == to)||(2 == from && 3 == to)){
			// thelink.setthisColor(Color.GRAY);
			// }
		}
		for (int i = 0; i < UIOTNLink.s_lUIEleLinkList.size(); i++) {// ���ö̲���·��ɫ
			UIOTNLink thelink = UIOTNLink.s_lUIEleLinkList.get(i);
			OTNLink theShortWave = thelink.getEleLink();
			// thelink.setthisColor(Color.GREEN);
			thelink.setthisColor(Color.GREEN);
			// else if(3 == from && 3 == to){
			// thelink.setthisColor(Color.YELLOW);
			// }
			// else if((1 == from && 2 == to)||(2 == from && 1 == to)){
			// thelink.setthisColor(Color.CYAN);
			// }
			// else if((3 == from && 2 == to)||(2 == from && 3 == to)){
			// thelink.setthisColor(Color.GRAY);
			// }
		}

		for (int i = 0; i < UIWDMLink.s_lUIWDMLinkList.size(); i++) {// ����������·��ɫ
			UIWDMLink thelink = UIWDMLink.s_lUIWDMLinkList.get(i);
			WDMLink theSatellite = thelink.getOptLink();
			// if(1 == from && 1 == to){
			// thelink.setthisColor(Color.GREEN);
			// }
			// else if(2 == from && 2 == to){
			// thelink.setthisColor(Color.BLUE);
			// }
			// else if(3 == from && 3 == to){
			thelink.setthisColor(Color.ORANGE);
			// }
			// else if((1 == from && 2 == to)||(2 == from && 1 == to)){
			// thelink.setthisColor(Color.CYAN);
			// }
			// else if((3 == from && 2 == to)||(2 == from && 3 == to)){
			// thelink.setthisColor(Color.GRAY);
			// }
		}
	}
}
