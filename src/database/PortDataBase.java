/*package database;

*//*******
 * 此类功能为导出业务到EXCEL
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


  端口数据的导入导出，从excel导入程序，从程序导出excel
  @firesky 
  2011.4.22
 
public class PortDataBase {

    public String msg = ""; // 导入导出信息提示
    public String msgErro = ""; // 错误提示
    public static boolean flag = true;
    public static boolean flagnportnull = true;// 该标志位对是空白工程还是进来就有内容进行标示，flase标示进行导入之前就有内容

    
      函数名称：inputPort 功能：导入端口 输入：目录地址 str 输出：void
     
    
      public void inputPort(String str){ flag=true; msgErro="";
      List<CommonNode> NodeList=new LinkedList<CommonNode>();
      if(nodeDateBase.flag==false||CommonNode.m_lCommonNode.size()==0) {
      msgErro="未导入节点端口信息"; flag=false; } else{ FileInputStream fins=null; try {
      fins = new FileInputStream(str); } catch (FileNotFoundException e1) { //
      TODO 自动生成 catch 块 e1.printStackTrace(); } HSSFWorkbook wb = null;
      POIFSFileSystem fs = null; try { fs = new POIFSFileSystem(fins); wb = new
      HSSFWorkbook(fs); } catch (IOException e) { e.printStackTrace(); }
      HSSFSheet sheet=null; try { sheet =wb.getSheet("端口"); } catch (Exception
      e) { // TODO: handle exception }
      
      int f=0; //标记成功导入总行数 if(sheet!=null){ outter: for(Row row : sheet) {
      if(row.getRowNum()!=0) { int a=row.getRowNum()+1; //格式控制，判断
      if(((row.getCell(0)==null)||(row.getCell(0)!=null&&row.getCell(0).
      getCellType()==Cell.CELL_TYPE_STRING&&row.getCell(0).getStringCellValue()
      .trim().equals(""))||row.getCell(0).getCellType()==Cell.CELL_TYPE_BLANK)
      &&((row.getCell(1)==null)||(row.getCell(1)!=null&&row.getCell(1).
      getCellType()==Cell.CELL_TYPE_STRING&&row.getCell(1).getStringCellValue()
      .trim().equals(""))||row.getCell(1).getCellType()==Cell.CELL_TYPE_BLANK)
      &&((row.getCell(2)==null)||(row.getCell(2)!=null&&row.getCell(2).
      getCellType()==Cell.CELL_TYPE_STRING&&row.getCell(2).getStringCellValue()
      .trim().equals(""))||row.getCell(2).getCellType()==Cell.CELL_TYPE_BLANK)
      ) { break outter; } else { int id=0; //端口id CommonNode belongsNode=null;
      //端口所属节点 Layer layer=null; //端口所属层 PortRate portRate=null;//端口速率 int
      portNumber=0; //端口数量 PortType portType=null; //端口类型 PortStatus
      portStatus=null;//端口状态
      
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
      if(portTypeString.equals("业务")||portTypeString.equals("业务端口")){portType=
      PortType.业务端口;} else
      if(portTypeString.equals("线路")||portTypeString.equals("线路端口")){portType=
      PortType.线路端口;} }catch(Exception e){ e.printStackTrace(); }
      
      try{ String portStatusString=row.getCell(5).getStringCellValue().trim();
      if(portStatusString.equals("空闲")){portStatus=PortStatus.空闲;} else
      if(portStatusString.equals("通过")){portStatus=PortStatus.通过;} else
      if(portStatusString.equals("上路")){portStatus=PortStatus.上路;} else
      if(portStatusString.equals("下路")){portStatus=PortStatus.下路;} else
      if(portStatusString.equals("人工占用")){portStatus=PortStatus.人工占用;}
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
      
      //异常处理 if(layer==null) { msgErro+="端口表格存在错误: "+a+"行 ，层属性有错误，请确认！"+"\n";
      // clear(); // break outter; } else if(belongsNode==null) { msgErro+=
      "端口表格存在错误: "+a+"行 ，无该所属节点，请确认！"+"\n"; // clear(); // break outter; } else
      if(portRate==null) { msgErro+="端口表格存在错误: "+a+"行 ，速率属性有错误，请确认！"+"\n"; //
      clear(); // break outter; } else if(portType==null) { msgErro+=
      "端口表格存在错误: "+a+"行 ，端口类型属性有错误，请确认！"+"\n"; // clear(); // break outter; }
      else if(portStatus==null) { msgErro+="端口表格存在错误: "+a+"行 ，端口状态属性有错误，请确认！"
      +"\n"; // clear(); // break outter; } else if(portNumber==0) { msgErro+=
      "端口表格存在错误: "+a+"行 ，端口数量属性为0，请按规定格式输入，如1-10！"+"\n"; // clear(); // break
      outter; } else{ //新建新端口 for(;portNumber>0;--portNumber){ new
      Port(id++,belongsNode,layer,portRate,portType.toString(),portStatus.
      toString()); } f++; } } } } } try { fins.close(); } catch (IOException e)
      { // TODO 自动生成 catch 块 e.printStackTrace(); } msg="成功导入端口"+f+"个"; }
      
      }
     
    
      函数名称：outputPort 
      函数功能：沿着节点链，依次导出各个节点上的端口 
      输入：导出路径str 
      输出：void
      
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
	    sheet = wb.createSheet("端口");
	} catch (Exception e) {
	    sheet = wb.getSheet("端口");
	}

	sheet.setColumnWidth(3, 3900);
	// 创建字体样式
	HSSFRow row = sheet.createRow(0);
	HSSFCell cell = row.createCell(0);
	cell.setCellValue("ID");
	cell = row.createCell(1);
	cell.setCellValue("所属节点名称");
	cell = row.createCell(2);
	cell.setCellValue("端口类型");
	cell = row.createCell(3);
	cell.setCellValue("端口速率(Gbit/s)");
	// cell = row.createCell(4);
	// cell.setCellValue("端口数量");
	cell = row.createCell(4);
	cell.setCellValue("端口功能");
	cell = row.createCell(5);
	cell.setCellValue("状态");
	cell = row.createCell(6);
	cell.setCellValue("承载业务");

	int i = 1;
	Iterator<CommonNode> it = CommonNode.allNodeList.iterator();
	while (it.hasNext()) {
	    CommonNode node = it.next();
	    Iterator<Port> optPort = node.getOptPortList().iterator();
	    while (optPort.hasNext()) {// 输出光端口数据
		Port port = optPort.next();

		row = sheet.createRow(i);
		cell = row.createCell(0);
		cell.setCellValue(port.getId());

		cell = row.createCell(1);
		cell.setCellValue(node.getName());

		cell = row.createCell(2);
		cell.setCellValue("光");

		cell = row.createCell(3);
		String b = String.valueOf(PortRate.Rate2Num(port.getRate()));
		cell.setCellValue(b);
		// System.out.println(PortRate.Rate2Num(port.getRate()));

		// cell = row.createCell(4);
		// cell.setCellValue(1);//端口数量
		cell = row.createCell(4);
		cell.setCellValue(port.getKind().toString());

		cell = row.createCell(5);
		cell.setCellValue(port.getStatus().toString());

		cell = row.createCell(6);
		cell.setCellValue(returnPorttraffic(port));
		++i;
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setWrapText(true);// 自动换行
	    }

	    Iterator<Port> elePort = node.getElePortList().iterator();
	    while (elePort.hasNext()) {// 输出短波端口数据
		Port port = elePort.next();

		row = sheet.createRow(i);
		cell = row.createCell(0);
		cell.setCellValue(port.getId());

		cell = row.createCell(1);
		cell.setCellValue(node.getName());

		cell = row.createCell(2);
		cell.setCellValue("电");

		cell = row.createCell(3);
		String b = String.valueOf(PortRate.Rate2Num(port.getRate()));
		cell.setCellValue(b);
		// System.out.println(PortRate.Rate2Num(port.getRate()));

		// cell = row.createCell(4);
		// cell.setCellValue(1);//端口数量
		cell = row.createCell(4);
		cell.setCellValue(port.getKind().toString());

		cell = row.createCell(5);
		cell.setCellValue(port.getStatus().toString());

		cell = row.createCell(6);
		cell.setCellValue(returnPorttraffic(port));
		++i;
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setWrapText(true);// 自动换行
	    }

	    Iterator<Port> fiberPort = node.getFiberPortList().iterator();
	    while (fiberPort.hasNext()) {// 输出光纤端口数据
		Port port = fiberPort.next();

		row = sheet.createRow(i);
		cell = row.createCell(0);
		cell.setCellValue(port.getId());

		cell = row.createCell(1);
		cell.setCellValue(node.getName());

		cell = row.createCell(2);
		cell.setCellValue("光纤");

		cell = row.createCell(3);
		String b = String.valueOf(PortRate.Rate2Num(port.getRate()));
		cell.setCellValue(b);
		// System.out.println(PortRate.Rate2Num(port.getRate()));

		// cell = row.createCell(4);
		// cell.setCellValue(1);//端口数量
		cell = row.createCell(4);
		cell.setCellValue(port.getKind().toString());

		cell = row.createCell(5);
		cell.setCellValue(port.getStatus().toString());

		cell = row.createCell(6);
		cell.setCellValue(returnPorttraffic(port));
		++i;
		HSSFCellStyle style1 = wb.createCellStyle();
		style1.setWrapText(true);// 自动换行
	    }
	}

	// 创建Excel的sheet的一行
	// 创建一个Excel的单元格
	// 设置单元格内容格式

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
	msg = "成功导出节点" + (i - 1) + "行";
    }

    public static void clear() {
	CommonNode.allNodeList.clear();
	FiberLink.fiberLinkList.clear();
	OTNLink.eleLinkList.clear();

    }

*//**
 * 功能：导出某个节点的端口
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
	    sheet = wb.createSheet(name + "节点端口");
	} catch (Exception e) {
	    sheet = wb.getSheet(name + "节点端口");
	}
	// 创建字体样式
	HSSFRow row = sheet.createRow(0);
	HSSFCell cell = row.createCell(0);
	cell.setCellValue("ID");
	cell = row.createCell(1);
	cell.setCellValue("端口类型");
	cell = row.createCell(2);
	cell.setCellValue("端口速率");
	// cell = row.createCell(4);
	// cell.setCellValue("端口数量");
	cell = row.createCell(3);
	cell.setCellValue("端口功能");
	cell = row.createCell(4);
	cell.setCellValue("状态");
	cell = row.createCell(5);
	cell.setCellValue("承载内容");

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
	    cell.setCellValue("光");

	    cell = row.createCell(2);
	    cell.setCellValue(PortRate.RateToString(port.getRate()));
	    // cell = row.createCell(3);
	    // cell.setCellValue(1);//端口数量

	    cell = row.createCell(3);
	    cell.setCellValue(port.getKind().toString());

	    cell = row.createCell(4);
	    cell.setCellValue(port.getStatus().toString());

	    cell = row.createCell(5);
	    if (port.getCarriedTraffic().size() != 0) {
		cell.setCellValue(returnPorttraffic(port));
	    } else if (port.getStatus().equals(PortStatus.上路) || port.getStatus().equals(PortStatus.下路)) {
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
	    style1.setWrapText(true);// 自动换行
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
	    cell.setCellValue("电");

	    cell = row.createCell(2);
	    cell.setCellValue(PortRate.RateToString(port.getRate()));
	    // cell = row.createCell(3);
	    // cell.setCellValue(1);//端口数量

	    cell = row.createCell(3);
	    cell.setCellValue(port.getKind().toString());

	    cell = row.createCell(4);
	    cell.setCellValue(port.getStatus().toString());

	    cell = row.createCell(5);
	    if (port.getCarriedTraffic().size() != 0) {
		cell.setCellValue(returnPorttraffic(port));
	    } else if (port.getStatus().equals(PortStatus.上路) || port.getStatus().equals(PortStatus.下路)) {
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
	    style1.setWrapText(true);// 自动换行
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
	    cell.setCellValue("光纤");

	    cell = row.createCell(2);
	    cell.setCellValue(PortRate.RateToString(port.getRate()));
	    // cell = row.createCell(3);
	    // cell.setCellValue(1);//端口数量

	    cell = row.createCell(3);
	    cell.setCellValue(port.getKind().toString());

	    cell = row.createCell(4);
	    cell.setCellValue(port.getStatus().toString());

	    cell = row.createCell(5);
	    if (port.getCarriedTraffic().size() != 0) {
		cell.setCellValue(returnPorttraffic(port));
	    } else if (port.getStatus().equals(PortStatus.上路) || port.getStatus().equals(PortStatus.下路)) {
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
	    style1.setWrapText(true);// 自动换行
	}

	// }

	// 创建Excel的sheet的一行
	// 创建一个Excel的单元格
	// 设置单元格内容格式

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
	msg = "成功导出节点" + (i - 1) + "行";

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