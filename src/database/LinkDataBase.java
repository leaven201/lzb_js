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
 * 链路数据的导入导出，从excel导入程序，从程序导出excel
 * 以及新建链路完成后对相应节点的端口添加
 * @author 豹读诗书
 *
 */
public class LinkDataBase {

    public String msgf = ""; // 导入fiber信息提示变量
    public String msgw=""; //导入wdm信息提示变量
    public String msgo=""; //导入otn信息提示变量
    public String msgerr = ""; // 导入导出错误提示
    public static boolean flagf = true; // fiber层导入标志
    public static boolean flagw = true;
    public static boolean flago = true;
    public static boolean flagfnull = true;// 该标志位对是空白工程还是进来就有内容进行标示，flase标示进行导入之前就有内容
    public static boolean flagwnull = true;
    
    /**
     * 函数名：inputFiberLink
     * 功能：导入fiber层链路资源 
     * 输入：xls表格名称
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
	    msgerr = "未导入节点端口信息" + "\n";
	    flagf = false;
	}else{
	    try{
		fins = new FileInputStream(str);
		fs = new POIFSFileSystem(fins);
		wb = new HSSFWorkbook(fs);
		sheet = wb.getSheet("光纤");
	    }catch(Exception e){
		e.printStackTrace();
	    }
	
	    int f=0;
	    if(sheet!=null){
		outter:for(Row row:sheet){
		    if(row.getRowNum()!=0){
			//格式控制
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
			CommonNode fromNode = null;// 链路源节点
			CommonNode toNode = null;// 链路宿节点
			double length =Double.MAX_VALUE;
			int waveNum=Integer.MAX_VALUE;//波道数
			boolean isActive = true;// 是否激活
			int size = Integer.MAX_VALUE;// 链路含有下层低粒度的数量,波长数量
			String SRLG="";
			double inPower=Double.MAX_VALUE;//入纤功率
			double spanLoss=Double.MAX_VALUE;//跨段损耗
			double NF=Double.MAX_VALUE;//外部噪声系数
			int OSNRCount=0;//OSNR计算值
			LinkRGroup linkGroup = null;//2017.11.10

			Cell cell = null;
			try {// 1.id
			    cell = row.getCell(0);
			    id = (int) cell.getNumericCellValue();
			} catch (Exception e) {
			    e.printStackTrace();
			}

			try { // 2.源节点
			    cell = row.getCell(1);
			    fromNode = CommonNode.getNode(cell.getStringCellValue().trim());
			} catch (Exception e) {
			    e.printStackTrace();
			}

			try { // 3.宿节点
			    cell = row.getCell(2);
			    toNode = CommonNode.getNode(cell.getStringCellValue().trim());
			} catch (Exception e) {
			    e.printStackTrace();
			}

			try { // 4.链路长度
			    cell = row.getCell(3);
			    length = cell.getNumericCellValue();
			} catch (Exception e) {
			    e.printStackTrace();
			}
			
			try { // 5.光纤类型
			    cell = row.getCell(4);
			    LinkType = cell.getStringCellValue().trim();
			} catch (Exception e) {
			    e.printStackTrace();
			}
			
			try { // 6.光纤衰减
			    cell = row.getCell(5);
			    attenuation = cell.getNumericCellValue();
			} catch (Exception e) {
			    e.printStackTrace();
			}
			
			try { // 7.光纤PMD
			    cell = row.getCell(6);
			    PMD = cell.getNumericCellValue();
			} catch (Exception e) {
			    e.printStackTrace();
			}
			
			try {// 8.光纤阶段
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
//				SRLG="无";
//			    else SRLG=cell.getStringCellValue();    
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//			try { // 6.波道数
//			    cell = row.getCell(5);
//			    waveNum = (int)cell.getNumericCellValue();
//			    
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//
//			try { // 7.isActive ，缺省为激活
//			    cell = row.getCell(6);
//			    if (cell.getStringCellValue().trim().equals("否"))
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
//				SRLG="无";
//			    else SRLG=cell.getStringCellValue();
//			    
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//			
//			try { // 9.入纤功率
//			    cell = row.getCell(8);
//			    inPower = cell.getNumericCellValue();
//			    
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//			
//			try { // 10.跨段损耗
//			    cell = row.getCell(9);
//			    spanLoss = cell.getNumericCellValue();
//			    
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//			
//			try { // 11.外部噪声系数
//			    cell = row.getCell(10);
//			    NF = cell.getNumericCellValue();
//			    
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
			
			// 提示信息
			int a = row.getRowNum() + 1;
			boolean isIDRepeat = FiberLink.isLinkIDRepeat(id);
			if (isIDRepeat || id == Integer.MAX_VALUE) {
			    msgerr += "链路表格第" + a + "行，链路ID属性有错误(如：重复)，请确认！" + "\n";
			} else if (name.equals("") || FiberLink.isLinkNameRepeat(name)) {
			    msgerr += "链路表格第" + a + "行，链路名称属性有错误（如名称重复），请确认！" + "\n";
			} else if (fromNode == null) {
			    msgerr += "链路表格第" + a + "行，链路首节点名称属性有错误（如此节点不存在），请确认！" + "\n";
			} else if (toNode == null) {
			    msgerr += "链路表格第" + a + "行，链路末节点名称属性有错误（如此节点不存在），请确认！" + "\n";
			}  else if (length == Double.MAX_VALUE) {
			    msgerr += "链路表格第" + a + "行，链路长度属性有错误（如此节点不存在），请确认！" + "\n";
			}/*else if (waveNum == Integer.MAX_VALUE) {
			    msgerr += "链路表格第" + a + "行，波道数属性有错误，请确认！" + "\n";
			}else if (SRLG.equals("")) {
			    msgerr += "链路表格第" + a + "行，链路SRLG属性有错误（如此节点不存在），请确认！" + "\n";
			}else if (linkLayer == null) {
			    msgerr += "链路表格第" + a + "行，链路所属层属性有错误，请确认！" + "\n";
			}*/ else if (linkLayer.equals(Layer.Fiber)) {
				FiberLink link = null;
				if (length < 0.0001)
				    length = BasicLink.getDistance(fromNode.getLatitude(), fromNode.getLongitude(),
					    toNode.getLatitude(), toNode.getLongitude());// 如果链路长度小于10cm，则根据经纬度计算长度
				link = new FiberLink(id, name, fromNode, toNode, length, SRLG,attenuation,LinkType,PMD,FiberStage,true);
//				BasicLink.addFiberLinkPort((FiberLink) link);
				//2017.11.10
				// 处理srlg,这里只处理spanlink的linkgroup，自动映射处理wdmlink的linkgroup
				if (SRLG != null && !SRLG.equals("")) {
					String[] SRLGlist;
					SRLGlist = SRLG.split(";");
					// if (SRLGlist.length == 1)
					// SRLGlist = SRLG.split(";");
					for (int n = 0; n < SRLGlist.length; ++n) {
						SRLG = SRLGlist[n];

						boolean srlg = true;// 判断是否已经有该srlg
						for (int k = 0; k < LinkRGroup.SRLGroupList.size(); ++k) {
							if (LinkRGroup.SRLGroupList.get(k).getName().equals(SRLG)) {
								linkGroup = LinkRGroup.SRLGroupList.get(k);
								srlg = false;
								break;
							}
						}
						if (srlg) {
							linkGroup = new LinkRGroup(SRLG);// 若无则新建SRLG组
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
				} // 处理srlg完毕

				f++;
			} else if (linkLayer.equals(Layer.WDM)) {
			    if (fromNode.getNodeType().equals(NodeType.ROADM) && toNode.getNodeType().equals(NodeType.ROADM)) {
				BasicLink link = null;
				if (length < 0.0001)
				    length = BasicLink.getDistance(fromNode.getLatitude(), fromNode.getLongitude(),
					    toNode.getLatitude(), toNode.getLongitude());// 如果链路长度小于10cm，则根据经纬度计算长度
//				link = new WDMLink(id, name, fromNode, toNode, length, rate, size,isActive,SRLG,linkLayer);
//				BasicLink.addOptLinkPort((WDMLink) link);
				f++;
			    } else {
				msgerr += "链路表格第" + a + "行，链路首末节点不支持光类型链路，请确认！" + "\n";
			    }
			} else if (linkLayer.equals(Layer.OTN)) {
			    if (fromNode.getNodeType().equals(NodeType.OTN) && toNode.getNodeType().equals(NodeType.OTN)) {
				BasicLink link = null;
				if (length < 0.0001)
				    length = BasicLink.getDistance(fromNode.getLatitude(), fromNode.getLongitude(),
					    toNode.getLatitude(), toNode.getLongitude());// 如果链路长度小于10cm，则根据经纬度计算长度
//				link = new OTNLink(id, name, fromNode, toNode, length, rate, size,isActive,SRLG,linkLayer);
//				BasicLink.addEleLinkPort((OTNLink) link);
				f++;
				
			}else {
				msgerr += "链路表格第" + a + "行，链路首末节点不支持电类型链路，请确认！" + "\n";
			    }
			}
		    }
		}
	    }
	    try {
		fins.close();
	    } catch (IOException e) {
		// TODO 自动生成 catch 块
		e.printStackTrace();
		} finally {
		    msgf = "成功导入" + f + "条链路数据" + "\n";
		    System.out.println(msgf);
		    System.out.println(msgerr);
		}
	    }

	}
	
    }
//    /**
//     * 函数名：inputWDMLink
//     * 功能：导入WDM层链路资源 
//     * 输入：xls表格名称
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
//	    msgerr = "未导入节点端口信息" + "\n";
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
//			//格式控制
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
//			CommonNode fromNode = null;// 链路源节点
//			CommonNode toNode = null;// 链路宿节点
//			double length = Double.MAX_VALUE;
//			PortRate rate=null;
//			boolean isActive = true;// 是否激活
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
//			try { // 3.源节点
//			    cell = row.getCell(2);
//			    fromNode = CommonNode.getNode(cell.getStringCellValue().trim());
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//
//			try { // 4.宿节点
//			    cell = row.getCell(3);
//			    toNode = CommonNode.getNode(cell.getStringCellValue().trim());
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//
////			try { // 5.链路长度
////			    cell = row.getCell(4);
////			    length = cell.getNumericCellValue();
////			} catch (Exception e) {
////			    e.printStackTrace();
////			}
//
//			try { // 6.链路速率
//			    cell = row.getCell(4);
//			    rate = PortRate.stringToRate(cell.getStringCellValue());
//			    
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//			
//			try { // 7.isActive ，缺省为激活
//			    cell = row.getCell(5);
//			    if (cell.getStringCellValue().trim().equals("否"))
//				isActive = false;
//			    else
//				isActive = true;
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//			
//			
//			// 提示信息
//			int a = row.getRowNum() + 1;
//			boolean isIDRepeat = BasicLink.isLinkIDRepeat(id);
//			if (isIDRepeat || id == Integer.MAX_VALUE) {
//			    msgerr += "链路表格第" + a + "行，链路ID属性有错误(如：重复)，请确认！" + "\n";
//			} else if (name.equals("") || BasicLink.isLinkNameRepeat(name)) {
//			    msgerr += "链路表格第" + a + "行，链路名称属性有错误（如名称重复），请确认！" + "\n";
//			} else if (fromNode == null) {
//			    msgerr += "链路表格第" + a + "行，链路首节点名称属性有错误（如此节点不存在），请确认！" + "\n";
//			} else if (toNode == null) {
//			    msgerr += "链路表格第" + a + "行，链路末节点名称属性有错误（如此节点不存在），请确认！" + "\n";
//			} else if (rate == null) {
//			    msgerr += "链路表格第" + a + "行，链路速率属性有错误，请确认！" + "\n";
//			} else if (linkLayer.equals(Layer.WDM)) {
//				BasicLink link = null;
//				if (length < 0.0001)
//				    length = BasicLink.getDistance(fromNode.getLatitude(), fromNode.getLongitude(),
//					    toNode.getLatitude(), toNode.getLongitude());// 如果链路长度小于10cm，则根据经纬度计算长度
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
//		// TODO 自动生成 catch 块
//		e.printStackTrace();
//		} finally {
//		    msgw = "成功导入" + f + "条链路数据" + "\n";
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
//     * 函数名：inputOTNLink
//     * 功能：导入OTN层链路资源 
//     * 输入：xls表格名称
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
//	    msgerr = "未导入节点端口信息" + "\n";
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
//			//格式控制
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
//			CommonNode fromNode = null;// 链路源节点
//			CommonNode toNode = null;// 链路宿节点
//			double length = Double.MAX_VALUE;
//			PortRate rate=null;
//			boolean isActive = true;// 是否激活
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
//			try { // 3.源节点
//			    cell = row.getCell(2);
//			    fromNode = CommonNode.getNode(cell.getStringCellValue().trim());
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//
//			try { // 4.宿节点
//			    cell = row.getCell(3);
//			    toNode = CommonNode.getNode(cell.getStringCellValue().trim());
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//
//			try { // 5.链路长度
//			    cell = row.getCell(4);
//			    length = cell.getNumericCellValue();
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//
//			try { // 6.链路速率
//			    cell = row.getCell(5);
//			    rate = PortRate.stringToRate(cell.getStringCellValue());
//			    
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//			
//			try { // 7.isActive ，缺省为激活
//			    cell = row.getCell(6);
//			    if (cell.getStringCellValue().trim().equals("否"))
//				isActive = false;
//			    else
//				isActive = true;
//			} catch (Exception e) {
//			    e.printStackTrace();
//			}
//			
//			
//			// 提示信息
//			int a = row.getRowNum() + 1;
//			boolean isIDRepeat = BasicLink.isLinkIDRepeat(id);
//			if (isIDRepeat || id == Integer.MAX_VALUE) {
//			    msgerr += "链路表格第" + a + "行，链路ID属性有错误(如：重复)，请确认！" + "\n";
//			} else if (name.equals("") || BasicLink.isLinkNameRepeat(name)) {
//			    msgerr += "链路表格第" + a + "行，链路名称属性有错误（如名称重复），请确认！" + "\n";
//			} else if (fromNode == null) {
//			    msgerr += "链路表格第" + a + "行，链路首节点名称属性有错误（如此节点不存在），请确认！" + "\n";
//			} else if (toNode == null) {
//			    msgerr += "链路表格第" + a + "行，链路末节点名称属性有错误（如此节点不存在），请确认！" + "\n";
//			} else if (length == Double.MAX_VALUE) {
//			    msgerr += "链路表格第" + a + "行，链路长度属性有错误（如此节点不存在），请确认！" + "\n";
//			} else if (rate == null) {
//			    msgerr += "链路表格第" + a + "行，链路速率属性有错误，请确认！" + "\n";
//			} else if (linkLayer.equals(Layer.OTN)) {
//				BasicLink link = null;
//				if (length < 0.0001)
//				    length = BasicLink.getDistance(fromNode.getLatitude(), fromNode.getLongitude(),
//					    toNode.getLatitude(), toNode.getLongitude());// 如果链路长度小于10cm，则根据经纬度计算长度
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
//		// TODO 自动生成 catch 块
//		e.printStackTrace();
//		} finally {
//		    msgo = "成功导入" + f + "条链路数据" + "\n";
//		    System.out.println(msgo);
//		    System.out.println(msgerr);
//		}
//	    }
//	}
//    }
//    /**
//     * 函数名：outputLink 
//     * 功能： 导出链路资源 
//     * 输出：一张已存数据的xls表
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
//	    sheet = wb.createSheet("链路");
//	} catch (Exception e) {
//	    e.printStackTrace();
//	}
////	for (int i = 6; i < 11; i++) {//设置7到11列的列宽
////	    sheet.setColumnWidth(i, 4000); 
////	}
//	HSSFRow row = sheet.createRow(0);
//
//	HSSFCell cell = row.createCell(0);
//	cell.setCellValue("ID");
//	cell = row.createCell(1);
//	cell.setCellValue("名称");
//	cell = row.createCell(2);
//	cell.setCellValue("链路类型");
//	cell = row.createCell(3);
//	cell.setCellValue("起节点");
//	cell = row.createCell(4);
//	cell.setCellValue("末节点");
//	cell = row.createCell(5);
//	cell.setCellValue("链路长度(km)");
//	cell = row.createCell(6);
//	cell.setCellValue("链路速率(Gbit/s)");
//	cell = row.createCell(7);
//	cell.setCellValue("是否激活");
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
//		cell.setCellValue("否");// 缺省状态为激活 CC
//	    else
//		cell.setCellValue("是");
//	
//	}
//	try {
//	    wb.write(os);
//	} catch (IOException e) {
//	    // TODO 自动生成 catch 块
//	    e.printStackTrace();
//	}
//	try {
//	    os.close();
//	} catch (IOException e) {
//	    // TODO 自动生成 catch 块
//	    e.printStackTrace();
//	}
//	msgf = "成功导出链路" + (i - 1) + "行";
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
