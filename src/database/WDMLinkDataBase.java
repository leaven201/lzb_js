/*package database;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedList;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import data.BasicLink;
import data.CommonNode;
import data.FiberLink;
import data.OTNLink;
import data.WDMLink;
import enums.Layer;
import enums.NodeType;
import enums.PortRate;

*//**
 * WDM链路数据的导入导出，从excel导入程序，从程序导出excel 以及新建链路完成后对相应节点的端口添加
 * 
 * @author 豹读诗书
 *
 *//*
public class WDMLinkDataBase {
	public String msgf = ""; // 导入WDMlink信息提示变量
	public String msgerr = ""; // 导入导出错误提示
	public static boolean flagf = true; // fiber层导入标志
	public static boolean flagw = true;
	public static boolean flagsa = true;
	public static boolean flagfnull = true;// 该标志位对是空白工程还是进来就有内容进行标示，flase标示进行导入之前就有内容
	public static boolean flagwnull = true;

	*//**
	 * 函数名：inputWDMLink 功能：导入fiber层链路资源 输入：xls表格名称
	 * 
	 * @param str
	 *//*
	public void inputWDMLink(String str) {

		flagf = true;
		msgerr = "";
		FileInputStream fins = null;
		HSSFWorkbook wb = null;
		POIFSFileSystem fs = null;
		HSSFSheet sheet = null;
		if (BasicLink.allLinkList.size() != 0)
			flagfnull = false;
		LinkedList<CommonNode> NodeList = new LinkedList<CommonNode>();
		if (NodeDataBase.flag == false && NodeDataBase.flagnodenull) {
			CommonNode.allNodeList = NodeList;
		}
		if (CommonNode.allNodeList.size() == 0) {
			msgerr = "未导入节点端口信息" + "\n";
			flagf = false;
		} else {
			try {
				fins = new FileInputStream(str);
				fs = new POIFSFileSystem(fins);
				wb = new HSSFWorkbook(fs);
				sheet = wb.getSheet("WDMLink");
			} catch (Exception e) {
				e.printStackTrace();
			}

			int f = 0;
			if (sheet != null) {
				outter: for (Row row : sheet) {
					if (row.getRowNum() != 0) {
						// 格式控制
						if (((row.getCell(0) == null)
								|| (row.getCell(0) != null && row.getCell(0).getCellType() == Cell.CELL_TYPE_STRING
										&& row.getCell(0).getStringCellValue().trim().equals(""))
								|| row.getCell(0).getCellType() == Cell.CELL_TYPE_BLANK)
								&& ((row.getCell(1) == null)
										|| (row.getCell(1) != null
												&& row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING
												&& row.getCell(1).getStringCellValue().trim().equals(""))
										|| row.getCell(1).getCellType() == Cell.CELL_TYPE_BLANK)
								&& ((row.getCell(2) == null)
										|| (row.getCell(2) != null
												&& row.getCell(2).getCellType() == Cell.CELL_TYPE_STRING
												&& row.getCell(2).getStringCellValue().trim().equals(""))
										|| row.getCell(2).getCellType() == Cell.CELL_TYPE_BLANK)
								&& ((row.getCell(3) == null)
										|| (row.getCell(3) != null
												&& row.getCell(3).getCellType() == Cell.CELL_TYPE_STRING
												&& row.getCell(3).getStringCellValue().trim().equals(""))
										|| row.getCell(3).getCellType() == Cell.CELL_TYPE_BLANK)
								&& ((row.getCell(4) == null)
										|| (row.getCell(4) != null
												&& row.getCell(4).getCellType() == Cell.CELL_TYPE_STRING
												&& row.getCell(4).getStringCellValue().trim().equals(""))
										|| row.getCell(4).getCellType() == Cell.CELL_TYPE_BLANK)
								&& ((row.getCell(5) == null)
										|| (row.getCell(5) != null
												&& row.getCell(5).getCellType() == Cell.CELL_TYPE_STRING
												&& row.getCell(5).getStringCellValue().trim().equals(""))
										|| row.getCell(5).getCellType() == Cell.CELL_TYPE_BLANK)
								&& ((row.getCell(6) == null)
										|| (row.getCell(6) != null
												&& row.getCell(6).getCellType() == Cell.CELL_TYPE_STRING
												&& row.getCell(6).getStringCellValue().trim().equals(""))
										|| row.getCell(6).getCellType() == Cell.CELL_TYPE_BLANK)
								&& ((row.getCell(7) == null)
										|| (row.getCell(7) != null
												&& row.getCell(7).getCellType() == Cell.CELL_TYPE_STRING
												&& row.getCell(7).getStringCellValue().trim().equals(""))
										|| row.getCell(7).getCellType() == Cell.CELL_TYPE_BLANK)
								&& ((row.getCell(8) == null)
										|| (row.getCell(8) != null
												&& row.getCell(8).getCellType() == Cell.CELL_TYPE_STRING
												&& row.getCell(8).getStringCellValue().trim().equals(""))
										|| row.getCell(8).getCellType() == Cell.CELL_TYPE_BLANK)
								&& ((row.getCell(9) == null)
										|| (row.getCell(9) != null
												&& row.getCell(9).getCellType() == Cell.CELL_TYPE_STRING
												&& row.getCell(9).getStringCellValue().trim().equals(""))
										|| row.getCell(9).getCellType() == Cell.CELL_TYPE_BLANK)
								&& ((row.getCell(10) == null)
										|| (row.getCell(10) != null
												&& row.getCell(10).getCellType() == Cell.CELL_TYPE_STRING
												&& row.getCell(10).getStringCellValue().trim().equals(""))
										|| row.getCell(10).getCellType() == Cell.CELL_TYPE_BLANK)
								&& ((row.getCell(11) == null)
										|| (row.getCell(11) != null
												&& row.getCell(11).getCellType() == Cell.CELL_TYPE_STRING
												&& row.getCell(11).getStringCellValue().trim().equals(""))
										|| row.getCell(11).getCellType() == Cell.CELL_TYPE_BLANK)
								&& ((row.getCell(12) == null)
										|| (row.getCell(12) != null
												&& row.getCell(12).getCellType() == Cell.CELL_TYPE_STRING
												&& row.getCell(12).getStringCellValue().trim().equals(""))
										|| row.getCell(12).getCellType() == Cell.CELL_TYPE_BLANK)
								&& ((row.getCell(13) == null)
										|| (row.getCell(13) != null
												&& row.getCell(13).getCellType() == Cell.CELL_TYPE_STRING
												&& row.getCell(13).getStringCellValue().trim().equals(""))
										|| row.getCell(13).getCellType() == Cell.CELL_TYPE_BLANK)
								&& ((row.getCell(14) == null)
										|| (row.getCell(14) != null
												&& row.getCell(14).getCellType() == Cell.CELL_TYPE_STRING
												&& row.getCell(14).getStringCellValue().trim().equals(""))
										|| row.getCell(14).getCellType() == Cell.CELL_TYPE_BLANK)
								&& ((row.getCell(15) == null)
										|| (row.getCell(15) != null
												&& row.getCell(15).getCellType() == Cell.CELL_TYPE_STRING
												&& row.getCell(15).getStringCellValue().trim().equals(""))
										|| row.getCell(15).getCellType() == Cell.CELL_TYPE_BLANK)) {
							break outter;
						} else {
							int id = Integer.MAX_VALUE;
							String name = "";
							Layer linkLayer = null;
							CommonNode fromNode = null;// 链路源节点
							CommonNode toNode = null;// 链路宿节点
							double length = Double.MAX_VALUE;
							PortRate rate = null;
							boolean isActive = true;// 是否激活
							int size = Integer.MAX_VALUE;// 链路含有下层低粒度的数量,波长数量
							String SRLG = "";

							Cell cell = null;
							try {// 1.id
								cell = row.getCell(0);
								id = (int) cell.getNumericCellValue();
							} catch (Exception e) {
								e.printStackTrace();
							}

							try { // 2.name
								cell = row.getCell(1);
								name = cell.getStringCellValue().trim();
							} catch (Exception e) {
								e.printStackTrace();
							}

							try { // 3.源节点
								cell = row.getCell(2);
								fromNode = CommonNode.getNode(cell.getStringCellValue().trim());
							} catch (Exception e) {
								e.printStackTrace();
							}

							try { // 4.宿节点
								cell = row.getCell(3);
								toNode = CommonNode.getNode(cell.getStringCellValue().trim());
							} catch (Exception e) {
								e.printStackTrace();
							}

							try { // 5.链路长度
								cell = row.getCell(4);
								length = cell.getNumericCellValue();
							} catch (Exception e) {
								e.printStackTrace();
							}

							try { // 6.链路速率,size应该也在这个里面初始化，具体怎么弄还没想好
								cell = row.getCell(5);
								rate = PortRate.stringToRate(cell.getStringCellValue().trim());
								size = 4;
							} catch (Exception e) {
								e.printStackTrace();
							}

							try { // 7.isActive ，缺省为激活
								cell = row.getCell(6);
								if (cell.getStringCellValue().trim().equals("否"))
									isActive = false;
								else
									isActive = true;
							} catch (Exception e) {
								e.printStackTrace();
							}

							try { // 8.SRLG
								cell = row.getCell(7);
								if (cell == null)
									SRLG = "无";
								else
									SRLG = cell.getStringCellValue();

							} catch (Exception e) {
								e.printStackTrace();
							}

							try { // 9.链路所属层
								cell = row.getCell(8);
								linkLayer = Layer.stringToLinkType(cell.getStringCellValue());
							} catch (Exception e) {
								e.printStackTrace();
							}

							// 提示信息
							int a = row.getRowNum() + 1;
							boolean isIDRepeat = BasicLink.isLinkIDRepeat(id);
							if (isIDRepeat || id == Integer.MAX_VALUE) {
								msgerr += "链路表格第" + a + "行，链路ID属性有错误(如：重复)，请确认！" + "\n";
							} else if (name.equals("") || BasicLink.isLinkNameRepeat(name)) {
								msgerr += "链路表格第" + a + "行，链路名称属性有错误（如名称重复），请确认！" + "\n";
							} else if (fromNode == null) {
								msgerr += "链路表格第" + a + "行，链路首节点名称属性有错误（如此节点不存在），请确认！" + "\n";
							} else if (toNode == null) {
								msgerr += "链路表格第" + a + "行，链路末节点名称属性有错误（如此节点不存在），请确认！" + "\n";
							} else if (length == Double.MAX_VALUE) {
								msgerr += "链路表格第" + a + "行，链路长度属性有错误（如此节点不存在），请确认！" + "\n";
							} else if (rate == null) {
								msgerr += "链路表格第" + a + "行，链路速率属性有错误，请确认！" + "\n";
							} else if (name.equals("")) {
								msgerr += "链路表格第" + a + "行，链路SRLG属性有错误（如此节点不存在），请确认！" + "\n";
							} else if (linkLayer == null) {
								msgerr += "链路表格第" + a + "行，链路所属层属性有错误，请确认！" + "\n";
							} else if (linkLayer.equals(Layer.Fiber)) {
								if (fromNode.getNodeType().equals(NodeType.OLA)
										&& toNode.getNodeType().equals(NodeType.OLA)) {
									BasicLink link = null;
									if (length < 0.0001)
										length = BasicLink.getDistance(fromNode.getLatitude(), fromNode.getLongitude(),
												toNode.getLatitude(), toNode.getLongitude());// 如果链路长度小于10cm，则根据经纬度计算长度
									link = new FiberLink(id, name, fromNode, toNode, length, rate, size, isActive, SRLG,
											linkLayer);
									BasicLink.addFiberLinkPort((FiberLink) link);
									f++;
								} else {
									msgerr += "链路表格第" + a + "行，链路首末节点不支持光纤类型链路，请确认！" + "\n";
								}
							} else if (linkLayer.equals(Layer.WDM)) {
								if (fromNode.getNodeType().equals(NodeType.ROADM)
										&& toNode.getNodeType().equals(NodeType.ROADM)) {
									BasicLink link = null;
									if (length < 0.0001)
										length = BasicLink.getDistance(fromNode.getLatitude(), fromNode.getLongitude(),
												toNode.getLatitude(), toNode.getLongitude());// 如果链路长度小于10cm，则根据经纬度计算长度
									link = new WDMLink(id, name, fromNode, toNode, length, rate, size, isActive, SRLG,
											linkLayer);
									BasicLink.addOptLinkPort((WDMLink) link);
									f++;
								} else {
									msgerr += "链路表格第" + a + "行，链路首末节点不支持光类型链路，请确认！" + "\n";
								}
							} else if (linkLayer.equals(Layer.OTN)) {
								if (fromNode.getNodeType().equals(NodeType.OTN)
										&& toNode.getNodeType().equals(NodeType.OTN)) {
									BasicLink link = null;
									if (length < 0.0001)
										length = BasicLink.getDistance(fromNode.getLatitude(), fromNode.getLongitude(),
												toNode.getLatitude(), toNode.getLongitude());// 如果链路长度小于10cm，则根据经纬度计算长度
									link = new OTNLink(id, name, fromNode, toNode, length, rate, size, isActive, SRLG,
											linkLayer);
									BasicLink.addEleLinkPort((OTNLink) link);
									f++;
								} else {
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
}
*/