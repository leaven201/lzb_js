/*package database;

*//*******
 * ���๦��Ϊ����ҵ��EXCEL
 * 
 *//*
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import data.*;
import enums.PortRate;
import enums.PortStatus;
import enums.PortType;


  �˿����ݵĵ��뵼������excel������򣬴ӳ��򵼳�excel
  @firesky 
  2011.4.22
 
public class PortDataBase {

    public String msg = ""; // ���뵼����Ϣ��ʾ
    public String msgErro = ""; // ������ʾ
    public static boolean flag = true;
    public static boolean flagnportnull = true;// �ñ�־λ���ǿհ׹��̻��ǽ����������ݽ��б�ʾ��flase��ʾ���е���֮ǰ��������

    
      �������ƣ�inputPort ���ܣ�����˿� ���룺Ŀ¼��ַ str �����void
     
    
      public void inputPort(String str){ flag=true; msgErro="";
      List<CommonNode> NodeList=new LinkedList<CommonNode>();
      if(nodeDateBase.flag==false||CommonNode.m_lCommonNode.size()==0) {
      msgErro="δ����ڵ�˿���Ϣ"; flag=false; } else{ FileInputStream fins=null; try {
      fins = new FileInputStream(str); } catch (FileNotFoundException e1) { //
      TODO �Զ����� catch �� e1.printStackTrace(); } HSSFWorkbook wb = null;
      POIFSFileSystem fs = null; try { fs = new POIFSFileSystem(fins); wb = new
      HSSFWorkbook(fs); } catch (IOException e) { e.printStackTrace(); }
      HSSFSheet sheet=null; try { sheet =wb.getSheet("�˿�"); } catch (Exception
      e) { // TODO: handle exception }
      
      int f=0; //��ǳɹ����������� if(sheet!=null){ outter: for(Row row : sheet) {
      if(row.getRowNum()!=0) { int a=row.getRowNum()+1; //��ʽ���ƣ��ж�
      if(((row.getCell(0)==null)||(row.getCell(0)!=null&&row.getCell(0).
      getCellType()==Cell.CELL_TYPE_STRING&&row.getCell(0).getStringCellValue()
      .trim().equals(""))||row.getCell(0).getCellType()==Cell.CELL_TYPE_BLANK)
      &&((row.getCell(1)==null)||(row.getCell(1)!=null&&row.getCell(1).
      getCellType()==Cell.CELL_TYPE_STRING&&row.getCell(1).getStringCellValue()
      .trim().equals(""))||row.getCell(1).getCellType()==Cell.CELL_TYPE_BLANK)
      &&((row.getCell(2)==null)||(row.getCell(2)!=null&&row.getCell(2).
      getCellType()==Cell.CELL_TYPE_STRING&&row.getCell(2).getStringCellValue()
      .trim().equals(""))||row.getCell(2).getCellType()==Cell.CELL_TYPE_BLANK)
      ) { break outter; } else { int id=0; //�˿�id CommonNode belongsNode=null;
      //�˿������ڵ� Layer layer=null; //�˿������� PortRate portRate=null;//�˿����� int
      portNumber=0; //�˿����� PortType portType=null; //�˿����� PortStatus
      portStatus=null;//�˿�״̬
      
      try{ String portRateString=row.getCell(3).getStringCellValue().trim();
      if(portRateString.equals("40G")){portRate=PortRate.G40;} else
      if(portRateString.equals("10G")){portRate=PortRate.G10;} else
      if(portRateString.equals("2.5G")){portRate=PortRate.G2point5;} else
      if(portRateString.equals("GE")){portRate=PortRate.GE;} else
      if(portRateString.equals("622M")){portRate=PortRate.M622;} else
      if(portRateString.equals("155M")){portRate=PortRate.M155;} else
      if(portRateString.equals("FE")){portRate=PortRate.FE;} else
      if(portRateString.equals("2M")){portRate=PortRate.M2;} }catch(Exception
      e){ e.printStackTrace(); }
      
      try{ String portTypeString=row.getCell(4).getStringCellValue().trim();
      if(portTypeString.equals("ҵ��")||portTypeString.equals("ҵ��˿�")){portType=
      PortType.ҵ��˿�;} else
      if(portTypeString.equals("��·")||portTypeString.equals("��·�˿�")){portType=
      PortType.��·�˿�;} }catch(Exception e){ e.printStackTrace(); }
      
      try{ String portStatusString=row.getCell(5).getStringCellValue().trim();
      if(portStatusString.equals("����")){portStatus=PortStatus.����;} else
      if(portStatusString.equals("ͨ��")){portStatus=PortStatus.ͨ��;} else
      if(portStatusString.equals("��·")){portStatus=PortStatus.��·;} else
      if(portStatusString.equals("��·")){portStatus=PortStatus.��·;} else
      if(portStatusString.equals("�˹�ռ��")){portStatus=PortStatus.�˹�ռ��;}
      }catch(Exception e){ e.printStackTrace(); }
      
      
      try{ String layerString=row.getCell(2).getStringCellValue().trim();
      if(layerString.equals(Layer.FIBER.toString())){layer=Layer.FIBER;} else
      if(layerString.equals(Layer.Satellite.toString())){layer=Layer.Satellite;
      } else if(layerString.equals(Layer.OTN.toString())){layer=Layer.OTN;}
      else if(layerString.equals(Layer.ASON.toString())){layer=Layer.ASON;}
      else
      if(layerString.equals(Layer.ShortWave.toString())){layer=Layer.ShortWave;
      } }catch(Exception e){ e.printStackTrace(); }
      
      try{ String belongsNodeString=row.getCell(1).getStringCellValue().trim();
      Iterator<CommonNode> it=CommonNode.m_lCommonNode.iterator();
      while(it.hasNext()){ CommonNode node=it.next();
      if(node.getM_sName().equals(belongsNodeString)) { belongsNode=node;
      break; } } }catch(Exception e){ e.printStackTrace(); }
      
      try{ if(row.getCell(0).getCellType()==Cell.CELL_TYPE_NUMERIC) {
      id=(int)row.getCell(0).getNumericCellValue(); portNumber=1; } else
      if(row.getCell(0).getCellType()==Cell.CELL_TYPE_STRING) { String
      idString=row.getCell(0).getStringCellValue().trim(); String[]
      idStringList; idStringList=idString.split("-");
      if(idStringList.length==1){ id=Integer.parseInt(idStringList[0]);
      portNumber=1; } else if(idStringList.length==2){
      id=Integer.parseInt(idStringList[0]);
      portNumber=Integer.parseInt(idStringList[1])-Integer.parseInt(
      idStringList[0])+1; } } }catch(Exception e){
      
      e.printStackTrace(); }
      
      //�쳣���� if(layer==null) { msgErro+="�˿ڱ����ڴ���: "+a+"�� ���������д�����ȷ�ϣ�"+"\n";
      // clear(); // break outter; } else if(belongsNode==null) { msgErro+=
      "�˿ڱ����ڴ���: "+a+"�� ���޸������ڵ㣬��ȷ�ϣ�"+"\n"; // clear(); // break outter; } else
      if(portRate==null) { msgErro+="�˿ڱ����ڴ���: "+a+"�� �����������д�����ȷ�ϣ�"+"\n"; //
      clear(); // break outter; } else if(portType==null) { msgErro+=
      "�˿ڱ����ڴ���: "+a+"�� ���˿����������д�����ȷ�ϣ�"+"\n"; // clear(); // break outter; }
      else if(portStatus==null) { msgErro+="�˿ڱ����ڴ���: "+a+"�� ���˿�״̬�����д�����ȷ�ϣ�"
      +"\n"; // clear(); // break outter; } else if(portNumber==0) { msgErro+=
      "�˿ڱ����ڴ���: "+a+"�� ���˿���������Ϊ0���밴�涨��ʽ���룬��1-10��"+"\n"; // clear(); // break
      outter; } else{ //�½��¶˿� for(;portNumber>0;--portNumber){ new
      Port(id++,belongsNode,layer,portRate,portType.toString(),portStatus.
      toString()); } f++; } } } } } try { fins.close(); } catch (IOException e)
      { // TODO �Զ����� catch �� e.printStackTrace(); } msg="�ɹ�����˿�"+f+"��"; }
      
      }
     
    
      �������ƣ�outputPort 
      �������ܣ����Žڵ��������ε��������ڵ��ϵĶ˿� 
      ���룺����·��str 
      �����void
      
      @firesky 2011.4.23
     
    public void outputPort(String str) {
	HSSFWorkbook wb = new HSSFWorkbook();
	POIFSFileSystem fs = null;
	HSSFSheet sheet = null;
	try {
	    fs = new POIFSFileSystem(new FileInputStream(str));
	    wb = new HSSFWorkbook(fs);
	} catch (Exception e) {
	}
	try {
	    sheet = wb.createSheet("�˿�");
	} catch (Exception e) {
	    sheet = wb.getSheet("�˿�");
	}

	sheet.setColumnWidth(3, 3900);
	// ����������ʽ
	HSSFRow row = sheet.createRow(0);
	HSSFCell cell = row.createCell(0);
	cell.setCellValue("ID");
	cell = row.createCell(1);
	cell.setCellValue("�����ڵ�����");
	cell = row.createCell(2);
	cell.setCellValue("�˿�����");
	cell = row.createCell(3);
	cell.setCellValue("�˿�����(Gbit/s)");
	// cell = row.createCell(4);
	// cell.setCellValue("�˿�����");
	cell = row.createCell(4);
	cell.setCellValue("�˿ڹ���");
	cell = row.createCell(5);
	cell.setCellValue("״̬");
	cell = row.createCell(6);
	cell.setCellValue("����ҵ��");

	int i = 1;
	Iterator<CommonNode> it = CommonNode.allNodeList.iterator();
	while (it.hasNext()) {
	    CommonNode node = it.next();
	    Iterator<Port> optPort = node.getOptPortList().iterator();
	    while (optPort.hasNext()) {// �����˿�����
		Port port = optPort.next();

		row = sheet.createRow(i);
		cell = row.createCell(0);
		cell.setCellValue(port.getId());

		cell = row.createCell(1);
		cell.setCellValue(node.getName());

		cell = row.createCell(2);
		cell.setCellValue("��");

		cell = row.createCell(3);
		String b = String.valueOf(PortRate.Rate2Num(port.getRate()));
		cell.setCellValue(b);
		// System.out.println(PortRate.Rate2Num(port.getRate()));

		// cell = row.createCell(4);
		// cell.setCellValue(1);//�˿�����
		cell = row.createCell(4);
		cell.setCellValue(port.getKind().toString());

		cell = row.createCell(5);
		cell.setCellValue(port.getStatus().toString());

		cell = row.createCell(6);
		cell.setCellValue(returnPorttraffic(port));
		++i;
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setWrapText(true);// �Զ�����
	    }

	    Iterator<Port> elePort = node.getElePortList().iterator();
	    while (elePort.hasNext()) {// ����̲��˿�����
		Port port = elePort.next();

		row = sheet.createRow(i);
		cell = row.createCell(0);
		cell.setCellValue(port.getId());

		cell = row.createCell(1);
		cell.setCellValue(node.getName());

		cell = row.createCell(2);
		cell.setCellValue("��");

		cell = row.createCell(3);
		String b = String.valueOf(PortRate.Rate2Num(port.getRate()));
		cell.setCellValue(b);
		// System.out.println(PortRate.Rate2Num(port.getRate()));

		// cell = row.createCell(4);
		// cell.setCellValue(1);//�˿�����
		cell = row.createCell(4);
		cell.setCellValue(port.getKind().toString());

		cell = row.createCell(5);
		cell.setCellValue(port.getStatus().toString());

		cell = row.createCell(6);
		cell.setCellValue(returnPorttraffic(port));
		++i;
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setWrapText(true);// �Զ�����
	    }

	    Iterator<Port> fiberPort = node.getFiberPortList().iterator();
	    while (fiberPort.hasNext()) {// ������˶˿�����
		Port port = fiberPort.next();

		row = sheet.createRow(i);
		cell = row.createCell(0);
		cell.setCellValue(port.getId());

		cell = row.createCell(1);
		cell.setCellValue(node.getName());

		cell = row.createCell(2);
		cell.setCellValue("����");

		cell = row.createCell(3);
		String b = String.valueOf(PortRate.Rate2Num(port.getRate()));
		cell.setCellValue(b);
		// System.out.println(PortRate.Rate2Num(port.getRate()));

		// cell = row.createCell(4);
		// cell.setCellValue(1);//�˿�����
		cell = row.createCell(4);
		cell.setCellValue(port.getKind().toString());

		cell = row.createCell(5);
		cell.setCellValue(port.getStatus().toString());

		cell = row.createCell(6);
		cell.setCellValue(returnPorttraffic(port));
		++i;
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setWrapText(true);// �Զ�����
	    }
	}

	// ����Excel��sheet��һ��
	// ����һ��Excel�ĵ�Ԫ��
	// ���õ�Ԫ�����ݸ�ʽ

	FileOutputStream os = null;
	try {
	    os = new FileOutputStream(str);
	} catch (FileNotFoundException e) {
	    // TODO �Զ����� catch ��
	    e.printStackTrace();
	}
	try {
	    wb.write(os);
	} catch (IOException e) {
	    // TODO �Զ����� catch ��
	    e.printStackTrace();
	}
	try {
	    os.close();
	} catch (IOException e) {
	    // TODO �Զ����� catch ��
	    e.printStackTrace();
	}
	msg = "�ɹ������ڵ�" + (i - 1) + "��";
    }

    public static void clear() {
	CommonNode.allNodeList.clear();
	FiberLink.fiberLinkList.clear();
	OTNLink.eleLinkList.clear();

    }

*//**
 * ���ܣ�����ĳ���ڵ�Ķ˿�
 * @param str
 * @param Node
 *//*
    
    public void outputPort(String str, CommonNode Node) {
	String name = Node.getName();
	HSSFWorkbook wb = new HSSFWorkbook();
	POIFSFileSystem fs = null;
	HSSFSheet sheet = null;
	try {
	    fs = new POIFSFileSystem(new FileInputStream(str));
	    wb = new HSSFWorkbook(fs);
	} catch (Exception e) {
	}
	try {
	    sheet = wb.createSheet(name + "�ڵ�˿�");
	} catch (Exception e) {
	    sheet = wb.getSheet(name + "�ڵ�˿�");
	}
	// ����������ʽ
	HSSFRow row = sheet.createRow(0);
	HSSFCell cell = row.createCell(0);
	cell.setCellValue("ID");
	cell = row.createCell(1);
	cell.setCellValue("�˿�����");
	cell = row.createCell(2);
	cell.setCellValue("�˿�����");
	// cell = row.createCell(4);
	// cell.setCellValue("�˿�����");
	cell = row.createCell(3);
	cell.setCellValue("�˿ڹ���");
	cell = row.createCell(4);
	cell.setCellValue("״̬");
	cell = row.createCell(5);
	cell.setCellValue("��������");

	int i = 1;
	// Iterator<CommonNode> it=CommonNode.m_lCommonNode.iterator();
	// while(it.hasNext()){
	CommonNode node = Node;
	Iterator<Port> optPort = node.getOptPortList().iterator();
	while (optPort.hasNext()) {
	    Port port = optPort.next();

	    row = sheet.createRow(i);
	    cell = row.createCell(0);
	    cell.setCellValue(port.getId());

	    // cell = row.createCell(1);
	    // cell.setCellValue(node.getM_sName());
	    cell = row.createCell(1);
	    cell.setCellValue("��");

	    cell = row.createCell(2);
	    cell.setCellValue(PortRate.RateToString(port.getRate()));
	    // cell = row.createCell(3);
	    // cell.setCellValue(1);//�˿�����

	    cell = row.createCell(3);
	    cell.setCellValue(port.getKind().toString());

	    cell = row.createCell(4);
	    cell.setCellValue(port.getStatus().toString());

	    cell = row.createCell(5);
	    if (port.getCarriedTraffic().size() != 0) {
		cell.setCellValue(returnPorttraffic(port));
	    } else if (port.getStatus().equals(PortStatus.��·) || port.getStatus().equals(PortStatus.��·)) {
		StringBuffer buffer = new StringBuffer();
		for (int j = 0; j < port.getCarriedLinkList().size(); j++) {
		    buffer.append(port.getCarriedLinkList().get(j).getName() + "("
			    + port.getCarriedLinkList().get(j).getLinkLayer().toString() + ")");
		    if (i != port.getCarriedLinkList().size() - 1) {
			buffer.append(";");
		    }
		}
		cell.setCellValue(buffer.toString());
	    } else {
		cell.setCellValue("");
	    }
	    ++i;
	    HSSFCellStyle style1 = wb.createCellStyle();
	    style1.setWrapText(true);// �Զ�����
	}

	Iterator<Port> elePort = node.getElePortList().iterator();
	while (elePort.hasNext()) {
	    Port port = elePort.next();

	    row = sheet.createRow(i);
	    cell = row.createCell(0);
	    cell.setCellValue(port.getId());

	    // cell = row.createCell(1);
	    // cell.setCellValue(node.getM_sName());
	    cell = row.createCell(1);
	    cell.setCellValue("��");

	    cell = row.createCell(2);
	    cell.setCellValue(PortRate.RateToString(port.getRate()));
	    // cell = row.createCell(3);
	    // cell.setCellValue(1);//�˿�����

	    cell = row.createCell(3);
	    cell.setCellValue(port.getKind().toString());

	    cell = row.createCell(4);
	    cell.setCellValue(port.getStatus().toString());

	    cell = row.createCell(5);
	    if (port.getCarriedTraffic().size() != 0) {
		cell.setCellValue(returnPorttraffic(port));
	    } else if (port.getStatus().equals(PortStatus.��·) || port.getStatus().equals(PortStatus.��·)) {
		StringBuffer buffer = new StringBuffer();
		for (int j = 0; j < port.getCarriedLinkList().size(); j++) {
		    buffer.append(port.getCarriedLinkList().get(j).getName() + "("
			    + port.getCarriedLinkList().get(j).getLinkLayer().toString() + ")");
		    if (i != port.getCarriedLinkList().size() - 1) {
			buffer.append(";");
		    }
		}
		cell.setCellValue(buffer.toString());
	    } else {
		cell.setCellValue("");
	    }
	    ++i;
	    HSSFCellStyle style1 = wb.createCellStyle();
	    style1.setWrapText(true);// �Զ�����
	}

	Iterator<Port> fiberPort = node.getFiberPortList().iterator();
	while (fiberPort.hasNext()) {
	    Port port = fiberPort.next();

	    row = sheet.createRow(i);
	    cell = row.createCell(0);
	    cell.setCellValue(port.getId());

	    // cell = row.createCell(1);
	    // cell.setCellValue(node.getM_sName());
	    cell = row.createCell(1);
	    cell.setCellValue("����");

	    cell = row.createCell(2);
	    cell.setCellValue(PortRate.RateToString(port.getRate()));
	    // cell = row.createCell(3);
	    // cell.setCellValue(1);//�˿�����

	    cell = row.createCell(3);
	    cell.setCellValue(port.getKind().toString());

	    cell = row.createCell(4);
	    cell.setCellValue(port.getStatus().toString());

	    cell = row.createCell(5);
	    if (port.getCarriedTraffic().size() != 0) {
		cell.setCellValue(returnPorttraffic(port));
	    } else if (port.getStatus().equals(PortStatus.��·) || port.getStatus().equals(PortStatus.��·)) {
		StringBuffer buffer = new StringBuffer();
		for (int j = 0; j < port.getCarriedLinkList().size(); j++) {
		    buffer.append(port.getCarriedLinkList().get(j).getName() + "("
			    + port.getCarriedLinkList().get(j).getLinkLayer().toString() + ")");
		    if (i != port.getCarriedLinkList().size() - 1) {
			buffer.append(";");
		    }
		}
		cell.setCellValue(buffer.toString());
	    } else {
		cell.setCellValue("");
	    }
	    ++i;
	    HSSFCellStyle style1 = wb.createCellStyle();
	    style1.setWrapText(true);// �Զ�����
	}

	// }

	// ����Excel��sheet��һ��
	// ����һ��Excel�ĵ�Ԫ��
	// ���õ�Ԫ�����ݸ�ʽ

	FileOutputStream os = null;
	try {
	    os = new FileOutputStream(str);
	} catch (FileNotFoundException e) {
	    // TODO �Զ����� catch ��
	    e.printStackTrace();
	}
	try {
	    wb.write(os);
	} catch (IOException e) {
	    // TODO �Զ����� catch ��
	    e.printStackTrace();
	}
	try {
	    os.close();
	} catch (IOException e) {
	    // TODO �Զ����� catch ��
	    e.printStackTrace();
	}
	msg = "�ɹ������ڵ�" + (i - 1) + "��";

    }

    public String returnPorttraffic(Port port) {
	String string = "";
	List<Traffic> tralist = port.getCarriedTraffic();
	for (int i = 0; i < tralist.size(); ++i) {
	    Traffic tra = tralist.get(i);
	    string = string + tra.getName() + "(" + tra.getId() + ")" + ";";
	}
	return string;
    }

}
*/