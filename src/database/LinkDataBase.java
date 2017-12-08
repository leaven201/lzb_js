package database;

import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import data.*;
import enums.Layer;
import enums.NodeType;
import enums.PortRate;

import java.io.*;
/**
 * ��·���ݵĵ��뵼������excel������򣬴ӳ��򵼳�excel
 * �Լ��½���·��ɺ����Ӧ�ڵ�Ķ˿����
 * @author ����ʫ��
 *
 */
public class LinkDataBase {

    public String msgf = ""; // ����fiber��Ϣ��ʾ����
    public String msgw=""; //����wdm��Ϣ��ʾ����
    public String msgo=""; //����otn��Ϣ��ʾ����
    public String msgerr = ""; // ���뵼��������ʾ
    public static boolean flagf = true; // fiber�㵼���־
    public static boolean flagw = true;
    public static boolean flago = true;
    public static boolean flagfnull = true;// �ñ�־λ���ǿհ׹��̻��ǽ����������ݽ��б�ʾ��flase��ʾ���е���֮ǰ��������
    public static boolean flagwnull = true;
    
    /**
     * ��������inputFiberLink
     * ���ܣ�����fiber����·��Դ 
     * ���룺xls�������
     * @param str
     */
    public void inputFiberLink(String str) {

	flagf = true;
	msgerr = "";
	FileInputStream fins=null;
	HSSFWorkbook wb=null;
	POIFSFileSystem fs=null;
	HSSFSheet sheet = null;
	if (FiberLink.fiberLinkList.size() != 0)
	    flagfnull = false;
	LinkedList<CommonNode> NodeList = new LinkedList<CommonNode>();
	if (NodeDataBase.flag == false && NodeDataBase.flagnodenull) {
	    CommonNode.allNodeList = NodeList;
	}
	if (CommonNode.allNodeList.size() == 0) {
	    msgerr = "δ����ڵ�˿���Ϣ" + "\n";
	    flagf = false;
	}else{
	    try{
		fins = new FileInputStream(str);
		fs = new POIFSFileSystem(fins);
		wb = new HSSFWorkbook(fs);
		sheet = wb.getSheet("����");
	    }catch(Exception e){
		e.printStackTrace();
	    }
	
	    int f=0;
	    if(sheet!=null){
		outter:for(Row row:sheet){
		    if(row.getRowNum()!=0){
			//��ʽ����
			if ( ( (row.getCell(0) == null)|| (row.getCell(0) != null && row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING
	                     		&& row.getCell(0).getStringCellValue().trim().equals(""))|| row.getCell(0).getCellType() == Cell.CELL_TYPE_BLANK)
				&& ((row.getCell(1) == null)|| (row.getCell(1) != null&& row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING
			     		&& row.getCell(1).getStringCellValue().trim().equals(""))|| row.getCell(1).getCellType() == Cell.CELL_TYPE_BLANK)
				&& ((row.getCell(2) == null)|| (row.getCell(2) != null&& row.getCell(2).getCellType() == Cell.CELL_TYPE_STRING
			     		&& row.getCell(2).getStringCellValue().trim().equals(""))|| row.getCell(2).getCellType() == Cell.CELL_TYPE_BLANK)
				&& ((row.getCell(3) == null)|| (row.getCell(3) != null&& row.getCell(3).getCellType() == Cell.CELL_TYPE_STRING
			     		&& row.getCell(3).getStringCellValue().trim().equals(""))|| row.getCell(3).getCellType() == Cell.CELL_TYPE_BLANK)
				&& ((row.getCell(4) == null)|| (row.getCell(4) != null&& row.getCell(4).getCellType() == Cell.CELL_TYPE_STRING
			     		&& row.getCell(4).getStringCellValue().trim().equals(""))|| row.getCell(4).getCellType() == Cell.CELL_TYPE_BLANK)
				&& ((row.getCell(5) == null)|| (row.getCell(5) != null&& row.getCell(5).getCellType() == Cell.CELL_TYPE_STRING
					&& row.getCell(5).getStringCellValue().trim().equals(""))|| row.getCell(5).getCellType() == Cell.CELL_TYPE_BLANK)
				&& ((row.getCell(6) == null)|| (row.getCell(6) != null&& row.getCell(6).getCellType() == Cell.CELL_TYPE_STRING
					&& row.getCell(6).getStringCellValue().trim().equals(""))|| row.getCell(6).getCellType() == Cell.CELL_TYPE_BLANK)
				&& ((row.getCell(7) == null)|| (row.getCell(7) != null&& row.getCell(7).getCellType() == Cell.CELL_TYPE_STRING
					&& row.getCell(7).getStringCellValue().trim().equals(""))|| row.getCell(7).getCellType() == Cell.CELL_TYPE_BLANK)
				&& ((row.getCell(8) == null)|| (row.getCell(8) != null&& row.getCell(8).getCellType() == Cell.CELL_TYPE_STRING
					&& row.getCell(8).getStringCellValue().trim().equals(""))|| row.getCell(8).getCellType() == Cell.CELL_TYPE_BLANK)
				&& ((row.getCell(9) == null)|| (row.getCell(9) != null&& row.getCell(9).getCellType() == Cell.CELL_TYPE_STRING
					&& row.getCell(9).getStringCellValue().trim().equals(""))|| row.getCell(9).getCellType() == Cell.CELL_TYPE_BLANK)
				&& ((row.getCell(10) == null)|| (row.getCell(10) != null&& row.getCell(10).getCellType() == Cell.CELL_TYPE_STRING
					&& row.getCell(10).getStringCellValue().trim().equals(""))|| row.getCell(10).getCellType() == Cell.CELL_TYPE_BLANK)
				&& ((row.getCell(11) == null)|| (row.getCell(11) != null&& row.getCell(11).getCellType() == Cell.CELL_TYPE_STRING
					&& row.getCell(11).getStringCellValue().trim().equals(""))|| row.getCell(11).getCellType() == Cell.CELL_TYPE_BLANK)
				&& ((row.getCell(12) == null)|| (row.getCell(12) != null&& row.getCell(12).getCellType() == Cell.CELL_TYPE_STRING
					&& row.getCell(12).getStringCellValue().trim().equals(""))|| row.getCell(12).getCellType() == Cell.CELL_TYPE_BLANK)
				&& ((row.getCell(13) == null)|| (row.getCell(13) != null&& row.getCell(13).getCellType() == Cell.CELL_TYPE_STRING
					&& row.getCell(13).getStringCellValue().trim().equals(""))|| row.getCell(13).getCellType() == Cell.CELL_TYPE_BLANK)
				&& ((row.getCell(14) == null)|| (row.getCell(14) != null&& row.getCell(14).getCellType() == Cell.CELL_TYPE_STRING
					&& row.getCell(14).getStringCellValue().trim().equals(""))|| row.getCell(14).getCellType() == Cell.CELL_TYPE_BLANK)
				&& ((row.getCell(15) == null)|| (row.getCell(15) != null&& row.getCell(15).getCellType() == Cell.CELL_TYPE_STRING
					&& row.getCell(15).getStringCellValue().trim().equals(""))|| row.getCell(15).getCellType() == Cell.CELL_TYPE_BLANK)) {
			    break outter;  
			}else{
			int id = Integer.MAX_VALUE;
			String name = "";
			String LinkType = "";
			double attenuation = Double.MAX_VALUE;
			double PMD=Double.MAX_VALUE;
			int FiberStage=Integer.MAX_VALUE;
			Layer linkLayer =Layer.Fiber;
			CommonNode fromNode = null;// ��·Դ�ڵ�
			CommonNode toNode = null;// ��·�޽ڵ�
			double length =Double.MAX_VALUE;
			int waveNum=Integer.MAX_VALUE;//������
			boolean isActive = true;// �Ƿ񼤻�
			int size = Integer.MAX_VALUE;// ��·�����²�����ȵ�����,��������
			String SRLG="";
			double inPower=Double.MAX_VALUE;//���˹���
			double spanLoss=Double.MAX_VALUE;//������
			double NF=Double.MAX_VALUE;//�ⲿ����ϵ��
			int OSNRCount=0;//OSNR����ֵ
			LinkRGroup linkGroup = null;//2017.11.10

			Cell cell = null;
			try {// 1.id
			    cell = row.getCell(0);
			    id = (int) cell.getNumericCellValue();
			} catch (Exception e) {
			    e.printStackTrace();
			}

			try { // 2.Դ�ڵ�
			    cell = row.getCell(1);
			    fromNode = CommonNode.getNode(cell.getStringCellValue().trim());
			} catch (Exception e) {
			    e.printStackTrace();
			}

			try { // 3.�޽ڵ�
			    cell = row.getCell(2);
			    toNode = CommonNode.getNode(cell.getStringCellValue().trim());
			} catch (Exception e) {
			    e.printStackTrace();
			}

			try { // 4.��·����
			    cell = row.getCell(3);
			    length = cell.getNumericCellValue();
			} catch (Exception e) {
			    e.printStackTrace();
			}
			
			try { // 5.��������
			    cell = row.getCell(4);
			    LinkType = cell.getStringCellValue().trim();
			} catch (Exception e) {
			    e.printStackTrace();
			}
			
			try { // 6.����˥��
			    cell = row.getCell(5);
			    attenuation = cell.getNumericCellValue();
			} catch (Exception e) {
			    e.printStackTrace();
			}
			
			try { // 7.����PMD
			    cell = row.getCell(6);
			    PMD = cell.getNumericCellValue();
			} catch (Exception e) {
			    e.printStackTrace();
			}
			
			try {// 8.���˽׶�
				cell = row.getCell(7);
			    FiberStage = (int) cell.getNumericCellValue();
			} catch (Exception e) {
			    e.printStackTrace();
			}
			
			try { // 9.name
			    cell = row.getCell(8);
			    name = cell.getStringCellValue().trim();
			} catch (Exception e) {
			    e.printStackTrace();
			}
			
			try { // 9.SRLG
				cell = row.getCell(9);
				if (cell == null)
					SRLG = null;
				else if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
					SRLG = "" + (int) cell.getNumericCellValue();
				} else {
					SRLG = cell.getStringCellValue().trim();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

//			try { // 10.SRLG
//			    cell = row.getCell(9);
//			    if(cell==null)
//				SRLG="��";
//			    else SRLG=cell.getStringCellValue();    
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//			try { // 6.������
//			    cell = row.getCell(5);
//			    waveNum = (int)cell.getNumericCellValue();
//			    
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//
//			try { // 7.isActive ��ȱʡΪ����
//			    cell = row.getCell(6);
//			    if (cell.getStringCellValue().trim().equals("��"))
//				isActive = false;
//			    else
//				isActive = true;
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//			
//			try { // 8.SRLG
//			    cell = row.getCell(7);
//			    if(cell==null)
//				SRLG="��";
//			    else SRLG=cell.getStringCellValue();
//			    
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//			
//			try { // 9.���˹���
//			    cell = row.getCell(8);
//			    inPower = cell.getNumericCellValue();
//			    
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//			
//			try { // 10.������
//			    cell = row.getCell(9);
//			    spanLoss = cell.getNumericCellValue();
//			    
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//			
//			try { // 11.�ⲿ����ϵ��
//			    cell = row.getCell(10);
//			    NF = cell.getNumericCellValue();
//			    
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
			
			// ��ʾ��Ϣ
			int a = row.getRowNum() + 1;
			boolean isIDRepeat = FiberLink.isLinkIDRepeat(id);
			if (isIDRepeat || id == Integer.MAX_VALUE) {
			    msgerr += "��·����" + a + "�У���·ID�����д���(�磺�ظ�)����ȷ�ϣ�" + "\n";
			} else if (name.equals("") || FiberLink.isLinkNameRepeat(name)) {
			    msgerr += "��·����" + a + "�У���·���������д����������ظ�������ȷ�ϣ�" + "\n";
			} else if (fromNode == null) {
			    msgerr += "��·����" + a + "�У���·�׽ڵ����������д�����˽ڵ㲻���ڣ�����ȷ�ϣ�" + "\n";
			} else if (toNode == null) {
			    msgerr += "��·����" + a + "�У���·ĩ�ڵ����������д�����˽ڵ㲻���ڣ�����ȷ�ϣ�" + "\n";
			}  else if (length == Double.MAX_VALUE) {
			    msgerr += "��·����" + a + "�У���·���������д�����˽ڵ㲻���ڣ�����ȷ�ϣ�" + "\n";
			}/*else if (waveNum == Integer.MAX_VALUE) {
			    msgerr += "��·����" + a + "�У������������д�����ȷ�ϣ�" + "\n";
			}else if (SRLG.equals("")) {
			    msgerr += "��·����" + a + "�У���·SRLG�����д�����˽ڵ㲻���ڣ�����ȷ�ϣ�" + "\n";
			}else if (linkLayer == null) {
			    msgerr += "��·����" + a + "�У���·�����������д�����ȷ�ϣ�" + "\n";
			}*/ else if (linkLayer.equals(Layer.Fiber)) {
				FiberLink link = null;
				if (length < 0.0001)
				    length = BasicLink.getDistance(fromNode.getLatitude(), fromNode.getLongitude(),
					    toNode.getLatitude(), toNode.getLongitude());// �����·����С��10cm������ݾ�γ�ȼ��㳤��
				link = new FiberLink(id, name, fromNode, toNode, length, SRLG,attenuation,LinkType,PMD,FiberStage,true);
//				BasicLink.addFiberLinkPort((FiberLink) link);
				//2017.11.10
				// ����srlg,����ֻ����spanlink��linkgroup���Զ�ӳ�䴦��wdmlink��linkgroup
				if (SRLG != null && !SRLG.equals("")) {
					String[] SRLGlist;
					SRLGlist = SRLG.split(";");
					// if (SRLGlist.length == 1)
					// SRLGlist = SRLG.split(";");
					for (int n = 0; n < SRLGlist.length; ++n) {
						SRLG = SRLGlist[n];

						boolean srlg = true;// �ж��Ƿ��Ѿ��и�srlg
						for (int k = 0; k < LinkRGroup.SRLGroupList.size(); ++k) {
							if (LinkRGroup.SRLGroupList.get(k).getName().equals(SRLG)) {
								linkGroup = LinkRGroup.SRLGroupList.get(k);
								srlg = false;
								break;
							}
						}
						if (srlg) {
							linkGroup = new LinkRGroup(SRLG);// �������½�SRLG��
							linkGroup.setBelongLayer(Layer.Fiber);
							linkGroup.setID(LinkRGroup.SRLGroupList.size());
							LinkRGroup.SRLGroupList.add(linkGroup);
						}
						if (linkGroup != null) {
							linkGroup.addFiberLink(link);
							// for (WDMLink wdmLink : slink.getCarriedWDMLinkList()) {
							// linkGroup.addWDMLink(wdmLink);
							// }
						}

					}
				} // ����srlg���

				f++;
			} else if (linkLayer.equals(Layer.WDM)) {
			    if (fromNode.getNodeType().equals(NodeType.ROADM) && toNode.getNodeType().equals(NodeType.ROADM)) {
				BasicLink link = null;
				if (length < 0.0001)
				    length = BasicLink.getDistance(fromNode.getLatitude(), fromNode.getLongitude(),
					    toNode.getLatitude(), toNode.getLongitude());// �����·����С��10cm������ݾ�γ�ȼ��㳤��
//				link = new WDMLink(id, name, fromNode, toNode, length, rate, size,isActive,SRLG,linkLayer);
//				BasicLink.addOptLinkPort((WDMLink) link);
				f++;
			    } else {
				msgerr += "��·����" + a + "�У���·��ĩ�ڵ㲻֧�ֹ�������·����ȷ�ϣ�" + "\n";
			    }
			} else if (linkLayer.equals(Layer.OTN)) {
			    if (fromNode.getNodeType().equals(NodeType.OTN) && toNode.getNodeType().equals(NodeType.OTN)) {
				BasicLink link = null;
				if (length < 0.0001)
				    length = BasicLink.getDistance(fromNode.getLatitude(), fromNode.getLongitude(),
					    toNode.getLatitude(), toNode.getLongitude());// �����·����С��10cm������ݾ�γ�ȼ��㳤��
//				link = new OTNLink(id, name, fromNode, toNode, length, rate, size,isActive,SRLG,linkLayer);
//				BasicLink.addEleLinkPort((OTNLink) link);
				f++;
				
			}else {
				msgerr += "��·����" + a + "�У���·��ĩ�ڵ㲻֧�ֵ�������·����ȷ�ϣ�" + "\n";
			    }
			}
		    }
		}
	    }
	    try {
		fins.close();
	    } catch (IOException e) {
		// TODO �Զ����� catch ��
		e.printStackTrace();
		} finally {
		    msgf = "�ɹ�����" + f + "����·����" + "\n";
		    System.out.println(msgf);
		    System.out.println(msgerr);
		}
	    }

	}
	
    }
//    /**
//     * ��������inputWDMLink
//     * ���ܣ�����WDM����·��Դ 
//     * ���룺xls�������
//     * @param str
//     */
//    public void inputWDMLink(String str) {
//
//	flagw = true;
//	msgerr = "";
//	FileInputStream fins=null;
//	HSSFWorkbook wb=null;
//	POIFSFileSystem fs=null;
//	HSSFSheet sheet = null;
//	if (WDMLink.WDMLinkList.size() != 0)
//	    flagfnull = false;
//	LinkedList<CommonNode> NodeList = new LinkedList<CommonNode>();
//	if (NodeDataBase.flag == false && NodeDataBase.flagnodenull) {
//	    CommonNode.allNodeList = NodeList;
//	}
//	if (CommonNode.allNodeList.size() == 0) {
//	    msgerr = "δ����ڵ�˿���Ϣ" + "\n";
//	    flagw = false;
//	}else{
//	    try{
//		fins = new FileInputStream(str);
//		fs = new POIFSFileSystem(fins);
//		wb = new HSSFWorkbook(fs);
//		sheet = wb.getSheet("WDMLink");
//	    }catch(Exception e){
//		e.printStackTrace();
//	    }
//	
//	    int f=0;
//	    if(sheet!=null){
//		outter:for(Row row:sheet){
//		    if(row.getRowNum()!=0){
//			//��ʽ����
//			if ( ( (row.getCell(0) == null)|| (row.getCell(0) != null && row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING
//	                     		&& row.getCell(0).getStringCellValue().trim().equals(""))|| row.getCell(0).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(1) == null)|| (row.getCell(1) != null&& row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING
//			     		&& row.getCell(1).getStringCellValue().trim().equals(""))|| row.getCell(1).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(2) == null)|| (row.getCell(2) != null&& row.getCell(2).getCellType() == Cell.CELL_TYPE_STRING
//			     		&& row.getCell(2).getStringCellValue().trim().equals(""))|| row.getCell(2).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(3) == null)|| (row.getCell(3) != null&& row.getCell(3).getCellType() == Cell.CELL_TYPE_STRING
//			     		&& row.getCell(3).getStringCellValue().trim().equals(""))|| row.getCell(3).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(4) == null)|| (row.getCell(4) != null&& row.getCell(4).getCellType() == Cell.CELL_TYPE_STRING
//			     		&& row.getCell(4).getStringCellValue().trim().equals(""))|| row.getCell(4).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(5) == null)|| (row.getCell(5) != null&& row.getCell(5).getCellType() == Cell.CELL_TYPE_STRING
//					&& row.getCell(5).getStringCellValue().trim().equals(""))|| row.getCell(5).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(6) == null)|| (row.getCell(6) != null&& row.getCell(6).getCellType() == Cell.CELL_TYPE_STRING
//					&& row.getCell(6).getStringCellValue().trim().equals(""))|| row.getCell(6).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(7) == null)|| (row.getCell(7) != null&& row.getCell(7).getCellType() == Cell.CELL_TYPE_STRING
//					&& row.getCell(7).getStringCellValue().trim().equals(""))|| row.getCell(7).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(8) == null)|| (row.getCell(8) != null&& row.getCell(8).getCellType() == Cell.CELL_TYPE_STRING
//					&& row.getCell(8).getStringCellValue().trim().equals(""))|| row.getCell(8).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(9) == null)|| (row.getCell(9) != null&& row.getCell(9).getCellType() == Cell.CELL_TYPE_STRING
//					&& row.getCell(9).getStringCellValue().trim().equals(""))|| row.getCell(9).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(10) == null)|| (row.getCell(10) != null&& row.getCell(10).getCellType() == Cell.CELL_TYPE_STRING
//					&& row.getCell(10).getStringCellValue().trim().equals(""))|| row.getCell(10).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(11) == null)|| (row.getCell(11) != null&& row.getCell(11).getCellType() == Cell.CELL_TYPE_STRING
//					&& row.getCell(11).getStringCellValue().trim().equals(""))|| row.getCell(11).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(12) == null)|| (row.getCell(12) != null&& row.getCell(12).getCellType() == Cell.CELL_TYPE_STRING
//					&& row.getCell(12).getStringCellValue().trim().equals(""))|| row.getCell(12).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(13) == null)|| (row.getCell(13) != null&& row.getCell(13).getCellType() == Cell.CELL_TYPE_STRING
//					&& row.getCell(13).getStringCellValue().trim().equals(""))|| row.getCell(13).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(14) == null)|| (row.getCell(14) != null&& row.getCell(14).getCellType() == Cell.CELL_TYPE_STRING
//					&& row.getCell(14).getStringCellValue().trim().equals(""))|| row.getCell(14).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(15) == null)|| (row.getCell(15) != null&& row.getCell(15).getCellType() == Cell.CELL_TYPE_STRING
//					&& row.getCell(15).getStringCellValue().trim().equals(""))|| row.getCell(15).getCellType() == Cell.CELL_TYPE_BLANK)) {
//			    break outter;  
//			}else{
//			int id = Integer.MAX_VALUE;
//			String name = "";
//			Layer linkLayer =Layer.WDM;
//			CommonNode fromNode = null;// ��·Դ�ڵ�
//			CommonNode toNode = null;// ��·�޽ڵ�
//			double length = Double.MAX_VALUE;
//			PortRate rate=null;
//			boolean isActive = true;// �Ƿ񼤻�
//			
//
//			Cell cell = null;
//			try {// 1.id
//			    cell = row.getCell(0);
//			    id = (int) cell.getNumericCellValue();
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//
//			try { // 2.name
//			    cell = row.getCell(1);
//			    name = cell.getStringCellValue().trim();
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//
//
//			try { // 3.Դ�ڵ�
//			    cell = row.getCell(2);
//			    fromNode = CommonNode.getNode(cell.getStringCellValue().trim());
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//
//			try { // 4.�޽ڵ�
//			    cell = row.getCell(3);
//			    toNode = CommonNode.getNode(cell.getStringCellValue().trim());
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//
////			try { // 5.��·����
////			    cell = row.getCell(4);
////			    length = cell.getNumericCellValue();
////			} catch (Exception e) {
////			    e.printStackTrace();
////			}
//
//			try { // 6.��·����
//			    cell = row.getCell(4);
//			    rate = PortRate.stringToRate(cell.getStringCellValue());
//			    
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//			
//			try { // 7.isActive ��ȱʡΪ����
//			    cell = row.getCell(5);
//			    if (cell.getStringCellValue().trim().equals("��"))
//				isActive = false;
//			    else
//				isActive = true;
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//			
//			
//			// ��ʾ��Ϣ
//			int a = row.getRowNum() + 1;
//			boolean isIDRepeat = BasicLink.isLinkIDRepeat(id);
//			if (isIDRepeat || id == Integer.MAX_VALUE) {
//			    msgerr += "��·����" + a + "�У���·ID�����д���(�磺�ظ�)����ȷ�ϣ�" + "\n";
//			} else if (name.equals("") || BasicLink.isLinkNameRepeat(name)) {
//			    msgerr += "��·����" + a + "�У���·���������д����������ظ�������ȷ�ϣ�" + "\n";
//			} else if (fromNode == null) {
//			    msgerr += "��·����" + a + "�У���·�׽ڵ����������д�����˽ڵ㲻���ڣ�����ȷ�ϣ�" + "\n";
//			} else if (toNode == null) {
//			    msgerr += "��·����" + a + "�У���·ĩ�ڵ����������д�����˽ڵ㲻���ڣ�����ȷ�ϣ�" + "\n";
//			} else if (rate == null) {
//			    msgerr += "��·����" + a + "�У���·���������д�����ȷ�ϣ�" + "\n";
//			} else if (linkLayer.equals(Layer.WDM)) {
//				BasicLink link = null;
//				if (length < 0.0001)
//				    length = BasicLink.getDistance(fromNode.getLatitude(), fromNode.getLongitude(),
//					    toNode.getLatitude(), toNode.getLongitude());// �����·����С��10cm������ݾ�γ�ȼ��㳤��
//				link = new WDMLink(id, name, fromNode, toNode, length, rate,isActive,linkLayer);
//				//BasicLink.addOptLinkPort((WDMLink) link);
//				f++;
//			} 
//		    }
//		}
//	    }
//	    try {
//		fins.close();
//	    } catch (IOException e) {
//		// TODO �Զ����� catch ��
//		e.printStackTrace();
//		} finally {
//		    msgw = "�ɹ�����" + f + "����·����" + "\n";
//		    System.out.println(msgw);
//		    System.out.println(msgerr);
//		}
//	    }
//
//	}
//	
//    }
//    
//    /**
//     * ��������inputOTNLink
//     * ���ܣ�����OTN����·��Դ 
//     * ���룺xls�������
//     * @param str
//     */
//    public void inputOTNLink(String str) {
//
//	flago = true;
//	msgerr = "";
//	FileInputStream fins=null;
//	HSSFWorkbook wb=null;
//	POIFSFileSystem fs=null;
//	HSSFSheet sheet = null;
//	if (OTNLink.OTNLinkList.size() != 0)
//	    flagfnull = false;
//	LinkedList<CommonNode> NodeList = new LinkedList<CommonNode>();
//	if (NodeDataBase.flag == false && NodeDataBase.flagnodenull) {
//	    CommonNode.allNodeList = NodeList;
//	}
//	if (CommonNode.allNodeList.size() == 0) {
//	    msgerr = "δ����ڵ�˿���Ϣ" + "\n";
//	    flago = false;
//	}else{
//	    try{
//		fins = new FileInputStream(str);
//		fs = new POIFSFileSystem(fins);
//		wb = new HSSFWorkbook(fs);
//		sheet = wb.getSheet("OTNLink");
//	    }catch(Exception e){
//		e.printStackTrace();
//	    }
//	
//	    int f=0;
//	    if(sheet!=null){
//		outter:for(Row row:sheet){
//		    if(row.getRowNum()!=0){
//			//��ʽ����
//			if ( ( (row.getCell(0) == null)|| (row.getCell(0) != null && row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING
//	                     		&& row.getCell(0).getStringCellValue().trim().equals(""))|| row.getCell(0).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(1) == null)|| (row.getCell(1) != null&& row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING
//			     		&& row.getCell(1).getStringCellValue().trim().equals(""))|| row.getCell(1).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(2) == null)|| (row.getCell(2) != null&& row.getCell(2).getCellType() == Cell.CELL_TYPE_STRING
//			     		&& row.getCell(2).getStringCellValue().trim().equals(""))|| row.getCell(2).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(3) == null)|| (row.getCell(3) != null&& row.getCell(3).getCellType() == Cell.CELL_TYPE_STRING
//			     		&& row.getCell(3).getStringCellValue().trim().equals(""))|| row.getCell(3).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(4) == null)|| (row.getCell(4) != null&& row.getCell(4).getCellType() == Cell.CELL_TYPE_STRING
//			     		&& row.getCell(4).getStringCellValue().trim().equals(""))|| row.getCell(4).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(5) == null)|| (row.getCell(5) != null&& row.getCell(5).getCellType() == Cell.CELL_TYPE_STRING
//					&& row.getCell(5).getStringCellValue().trim().equals(""))|| row.getCell(5).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(6) == null)|| (row.getCell(6) != null&& row.getCell(6).getCellType() == Cell.CELL_TYPE_STRING
//					&& row.getCell(6).getStringCellValue().trim().equals(""))|| row.getCell(6).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(7) == null)|| (row.getCell(7) != null&& row.getCell(7).getCellType() == Cell.CELL_TYPE_STRING
//					&& row.getCell(7).getStringCellValue().trim().equals(""))|| row.getCell(7).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(8) == null)|| (row.getCell(8) != null&& row.getCell(8).getCellType() == Cell.CELL_TYPE_STRING
//					&& row.getCell(8).getStringCellValue().trim().equals(""))|| row.getCell(8).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(9) == null)|| (row.getCell(9) != null&& row.getCell(9).getCellType() == Cell.CELL_TYPE_STRING
//					&& row.getCell(9).getStringCellValue().trim().equals(""))|| row.getCell(9).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(10) == null)|| (row.getCell(10) != null&& row.getCell(10).getCellType() == Cell.CELL_TYPE_STRING
//					&& row.getCell(10).getStringCellValue().trim().equals(""))|| row.getCell(10).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(11) == null)|| (row.getCell(11) != null&& row.getCell(11).getCellType() == Cell.CELL_TYPE_STRING
//					&& row.getCell(11).getStringCellValue().trim().equals(""))|| row.getCell(11).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(12) == null)|| (row.getCell(12) != null&& row.getCell(12).getCellType() == Cell.CELL_TYPE_STRING
//					&& row.getCell(12).getStringCellValue().trim().equals(""))|| row.getCell(12).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(13) == null)|| (row.getCell(13) != null&& row.getCell(13).getCellType() == Cell.CELL_TYPE_STRING
//					&& row.getCell(13).getStringCellValue().trim().equals(""))|| row.getCell(13).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(14) == null)|| (row.getCell(14) != null&& row.getCell(14).getCellType() == Cell.CELL_TYPE_STRING
//					&& row.getCell(14).getStringCellValue().trim().equals(""))|| row.getCell(14).getCellType() == Cell.CELL_TYPE_BLANK)
//				&& ((row.getCell(15) == null)|| (row.getCell(15) != null&& row.getCell(15).getCellType() == Cell.CELL_TYPE_STRING
//					&& row.getCell(15).getStringCellValue().trim().equals(""))|| row.getCell(15).getCellType() == Cell.CELL_TYPE_BLANK)) {
//			    break outter;  
//			}else{
//			int id = Integer.MAX_VALUE;
//			String name = "";
//			Layer linkLayer =Layer.OTN;
//			CommonNode fromNode = null;// ��·Դ�ڵ�
//			CommonNode toNode = null;// ��·�޽ڵ�
//			double length = Double.MAX_VALUE;
//			PortRate rate=null;
//			boolean isActive = true;// �Ƿ񼤻�
//			
//
//			Cell cell = null;
//			try {// 1.id
//			    cell = row.getCell(0);
//			    id = (int) cell.getNumericCellValue();
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//
//			try { // 2.name
//			    cell = row.getCell(1);
//			    name = cell.getStringCellValue().trim();
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//
//
//			try { // 3.Դ�ڵ�
//			    cell = row.getCell(2);
//			    fromNode = CommonNode.getNode(cell.getStringCellValue().trim());
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//
//			try { // 4.�޽ڵ�
//			    cell = row.getCell(3);
//			    toNode = CommonNode.getNode(cell.getStringCellValue().trim());
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//
//			try { // 5.��·����
//			    cell = row.getCell(4);
//			    length = cell.getNumericCellValue();
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//
//			try { // 6.��·����
//			    cell = row.getCell(5);
//			    rate = PortRate.stringToRate(cell.getStringCellValue());
//			    
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//			
//			try { // 7.isActive ��ȱʡΪ����
//			    cell = row.getCell(6);
//			    if (cell.getStringCellValue().trim().equals("��"))
//				isActive = false;
//			    else
//				isActive = true;
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//			
//			
//			// ��ʾ��Ϣ
//			int a = row.getRowNum() + 1;
//			boolean isIDRepeat = BasicLink.isLinkIDRepeat(id);
//			if (isIDRepeat || id == Integer.MAX_VALUE) {
//			    msgerr += "��·����" + a + "�У���·ID�����д���(�磺�ظ�)����ȷ�ϣ�" + "\n";
//			} else if (name.equals("") || BasicLink.isLinkNameRepeat(name)) {
//			    msgerr += "��·����" + a + "�У���·���������д����������ظ�������ȷ�ϣ�" + "\n";
//			} else if (fromNode == null) {
//			    msgerr += "��·����" + a + "�У���·�׽ڵ����������д�����˽ڵ㲻���ڣ�����ȷ�ϣ�" + "\n";
//			} else if (toNode == null) {
//			    msgerr += "��·����" + a + "�У���·ĩ�ڵ����������д�����˽ڵ㲻���ڣ�����ȷ�ϣ�" + "\n";
//			} else if (length == Double.MAX_VALUE) {
//			    msgerr += "��·����" + a + "�У���·���������д�����˽ڵ㲻���ڣ�����ȷ�ϣ�" + "\n";
//			} else if (rate == null) {
//			    msgerr += "��·����" + a + "�У���·���������д�����ȷ�ϣ�" + "\n";
//			} else if (linkLayer.equals(Layer.OTN)) {
//				BasicLink link = null;
//				if (length < 0.0001)
//				    length = BasicLink.getDistance(fromNode.getLatitude(), fromNode.getLongitude(),
//					    toNode.getLatitude(), toNode.getLongitude());// �����·����С��10cm������ݾ�γ�ȼ��㳤��
//				link = new OTNLink(id, name, fromNode, toNode, length, rate,isActive,linkLayer);
//				//BasicLink.addOptLinkPort((WDMLink) link);
//				f++;
//			} 
//		    }
//		}
//	    }
//	    try {
//		fins.close();
//	    } catch (IOException e) {
//		// TODO �Զ����� catch ��
//		e.printStackTrace();
//		} finally {
//		    msgo = "�ɹ�����" + f + "����·����" + "\n";
//		    System.out.println(msgo);
//		    System.out.println(msgerr);
//		}
//	    }
//	}
//    }
//    /**
//     * ��������outputLink 
//     * ���ܣ� ������·��Դ 
//     * �����һ���Ѵ����ݵ�xls��
//     * @param str
//     */
//    public void outputLink(String str) {
//	HSSFWorkbook wb = null;
//	POIFSFileSystem fs = null;
//	HSSFSheet sheet = null;
//	FileOutputStream os = null;
//	try {
//	    fs = new POIFSFileSystem(new FileInputStream(str));
//	    wb = new HSSFWorkbook(fs);
//	    sheet = wb.createSheet("��·");
//	} catch (Exception e) {
//	    e.printStackTrace();
//	}
////	for (int i = 6; i < 11; i++) {//����7��11�е��п�
////	    sheet.setColumnWidth(i, 4000); 
////	}
//	HSSFRow row = sheet.createRow(0);
//
//	HSSFCell cell = row.createCell(0);
//	cell.setCellValue("ID");
//	cell = row.createCell(1);
//	cell.setCellValue("����");
//	cell = row.createCell(2);
//	cell.setCellValue("��·����");
//	cell = row.createCell(3);
//	cell.setCellValue("��ڵ�");
//	cell = row.createCell(4);
//	cell.setCellValue("ĩ�ڵ�");
//	cell = row.createCell(5);
//	cell.setCellValue("��·����(km)");
//	cell = row.createCell(6);
//	cell.setCellValue("��·����(Gbit/s)");
//	cell = row.createCell(7);
//	cell.setCellValue("�Ƿ񼤻�");
//	
//	int i = 1;
//	for (i = 1; i < BasicLink.allLinkList.size() + 1; i++) {
//	    BasicLink fl = BasicLink.allLinkList.get(i - 1);
//	    row = sheet.createRow(i);
//	    cell = row.createCell(0);
//	    cell.setCellValue(fl.getId());
//
//	    cell = row.createCell(1);
//	    cell.setCellValue(fl.getName());
//	    
//	    cell = row.createCell(2);
//	    cell.setCellValue(fl.getLinkLayer().toString());
//	    
//	    cell = row.createCell(3);
//	    cell.setCellValue(fl.getFromNode().getName());
//
//	    cell = row.createCell(4);
//	    cell.setCellValue(fl.getToNode().getName());
//
//	    cell = row.createCell(5);
//	    cell.setCellValue(fl.getLength());
//
//	    cell = row.createCell(6);
//	    cell.setCellValue(fl.getNrate());
//
//	    cell = row.createCell(7);
//	    if (!fl.isActive())
//		cell.setCellValue("��");// ȱʡ״̬Ϊ���� CC
//	    else
//		cell.setCellValue("��");
//	
//	}
//	try {
//	    wb.write(os);
//	} catch (IOException e) {
//	    // TODO �Զ����� catch ��
//	    e.printStackTrace();
//	}
//	try {
//	    os.close();
//	} catch (IOException e) {
//	    // TODO �Զ����� catch ��
//	    e.printStackTrace();
//	}
//	msgf = "�ɹ�������·" + (i - 1) + "��";
//	System.out.println(msgf);
//    }
    
// public static int returnnetid(String from,String to){
//    	
//    	int netnum=0;
//    	Network net1=Network.searchNetwork(CommonNode.getNode(from).getM_nSubnetNum());
//    	Network net2=Network.searchNetwork(CommonNode.getNode(to).getM_nSubnetNum());
//    	if(net1.getM_nId()==net2.getM_nId())
//			netnum=net1.getM_nId();
//		else{
//			if(net1.getM_nNetType()>net2.getM_nNetType())
//				netnum=net1.getM_nId();
//			if(net1.getM_nNetType()<net2.getM_nNetType())
//				netnum=net2.getM_nId();
//		}
//		return netnum;
//    }
}
