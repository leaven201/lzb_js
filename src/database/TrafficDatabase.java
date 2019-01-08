package database;

import java.awt.Component;
import java.awt.Container;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.hssf.model.Sheet;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import algorithm.OSNR;
import algorithm.PortAlloc;
import data.BasicLink;
import data.CommonNode;
import data.FiberLink;
import data.LinkRGroup;
import data.Port;
import data.Route;
import data.Traffic;
import data.TrafficGroup;
import data.TrafficGroupNew;
import data.WDMLink;
import data.WaveLength;
import data.Wavelength_2;
import design.NetDesign_zs;
import enums.PortKind;
import enums.PortRate;
import enums.PortStatus;
import enums.Status;
import enums.TrafficGroupType;
import enums.TrafficLevel;
import enums.TrafficRate;
import enums.TrafficStatus;
import survivance.Evaluation;

/**
 * 业务数据的导入导出，从excel导入程序，从程序导出excel， 以及对业务的相应节点添加支路端口来准备承载其业务
 * 
 * @author 豹读诗书
 */
public class TrafficDatabase {

	public String msgerr = "";
	public String msgt = "业务未导入";
	public static int index = 0;
	private String filelist = "wrong";
	public static String Sfilelist = "";
	public String suoyin = null;
	public String Group = null;
	public static String filepath; // 工程路径
	public static List<Sheet> str1 =new LinkedList<>();//用于存储srlg成组输出的问题
	
	public static int getGroupid(String str) {
		String strNew[]=str.split("-");
		int num = Integer.parseInt(strNew[1]);
		return num;
	}
	public static String getBelongGroup(String str) {
		String strNew[]=str.split("-");
		return strNew[2];
	}
	

	public void inputTraffic(String str) {
		if (NodeDataBase.flag == false || LinkDataBase.flagf == false) {
			msgerr = "由于未成功导入网络拓扑资源\n，未成功导入网络业务资源";
		}
		FileInputStream fins = null;
		POIFSFileSystem pfs = null;
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		try {
			fins = new FileInputStream(str);
			pfs = new POIFSFileSystem(fins);
			wb = new HSSFWorkbook(pfs);
			sheet = wb.getSheet("业务列表");
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
									|| (row.getCell(1) != null && row.getCell(1).getCellType() == Cell.CELL_TYPE_STRING
											&& row.getCell(1).getStringCellValue().trim().equals(""))
									|| row.getCell(1).getCellType() == Cell.CELL_TYPE_BLANK)
							&& ((row.getCell(2) == null)
									|| (row.getCell(2) != null && row.getCell(2).getCellType() == Cell.CELL_TYPE_STRING
											&& row.getCell(2).getStringCellValue().trim().equals(""))
									|| row.getCell(2).getCellType() == Cell.CELL_TYPE_BLANK)
							&& ((row.getCell(3) == null)
									|| (row.getCell(3) != null && row.getCell(3).getCellType() == Cell.CELL_TYPE_STRING
											&& row.getCell(3).getStringCellValue().trim().equals(""))
									|| row.getCell(3).getCellType() == Cell.CELL_TYPE_BLANK)
							&& ((row.getCell(4) == null)
									|| (row.getCell(4) != null && row.getCell(4).getCellType() == Cell.CELL_TYPE_STRING
											&& row.getCell(4).getStringCellValue().trim().equals(""))
									|| row.getCell(4).getCellType() == Cell.CELL_TYPE_BLANK)
							&& ((row.getCell(5) == null)
									|| (row.getCell(5) != null && row.getCell(5).getCellType() == Cell.CELL_TYPE_STRING
											&& row.getCell(5).getStringCellValue().trim().equals(""))
									|| row.getCell(5).getCellType() == Cell.CELL_TYPE_BLANK)
							&& ((row.getCell(6) == null)
									|| (row.getCell(6) != null && row.getCell(6).getCellType() == Cell.CELL_TYPE_STRING
											&& row.getCell(6).getStringCellValue().trim().equals(""))
									|| row.getCell(6).getCellType() == Cell.CELL_TYPE_BLANK)
							&& ((row.getCell(7) == null)
									|| (row.getCell(7) != null && row.getCell(7).getCellType() == Cell.CELL_TYPE_STRING
											&& row.getCell(7).getStringCellValue().trim().equals(""))
									|| row.getCell(7).getCellType() == Cell.CELL_TYPE_BLANK)
							&& ((row.getCell(8) == null)
									|| (row.getCell(8) != null && row.getCell(8).getCellType() == Cell.CELL_TYPE_STRING
											&& row.getCell(8).getStringCellValue().trim().equals(""))
									|| row.getCell(8).getCellType() == Cell.CELL_TYPE_BLANK)
							&& ((row.getCell(9) == null)
									|| (row.getCell(9) != null && row.getCell(9).getCellType() == Cell.CELL_TYPE_STRING
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
									|| row.getCell(15).getCellType() == Cell.CELL_TYPE_BLANK)
							&& ((row.getCell(16) == null)
									|| (row.getCell(16) != null
											&& row.getCell(16).getCellType() == Cell.CELL_TYPE_STRING
											&& row.getCell(16).getStringCellValue().trim().equals(""))
									|| row.getCell(16).getCellType() == Cell.CELL_TYPE_BLANK)) {
						break outter;
					} else {
						TrafficStatus status = TrafficStatus.未分配;
						int id = 0;
						int rankId = 0;
						int traNum = Integer.MAX_VALUE;
						// String name = "";
						CommonNode fromNode = null;
						CommonNode toNode = null;
						// boolean isFromOTN=false;
						TrafficRate rate = null;
						double nrate = Double.MAX_VALUE;
						TrafficLevel protectLevel = null;
						boolean isElectricalCrossConnection = true;
						boolean isShare = true;
						TrafficGroupNew trafficGroup=null;
//						int idOfTrafficGroup = 882388;
//						TrafficGroupType TraGroType = TrafficGroupType.无;
						String traGroType="无";
						CommonNode MustPassNode = null;
						CommonNode MustAvoidNode = null;
						FiberLink MustPassLink = null;
						FiberLink MustAvoidLink = null;
						boolean isHaveTraffic = false;
						Route existRoute = null;
						double temp = 0;
						int existWaveLength = 0;

						// for id auto assignment
						String idString = "";
						String layerString = "1";
						String rateString = "";

						Cell cell = null;

						try {// 1.rankId
							cell = row.getCell(0);
							rankId = (int) cell.getNumericCellValue();
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {// 2.源节点
							cell = row.getCell(1);
							fromNode = CommonNode.getNode(cell.getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {// 3.宿节点
							cell = row.getCell(2);
							toNode = CommonNode.getNode(cell.getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {// 4.业务速率（带宽）
							cell = row.getCell(3);
							rate = TrafficRate.NumToRate(cell.getStringCellValue());
							nrate = TrafficRate.Rate2Num(rate);
							temp=nrate;
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {// 5.业务数量
							cell = row.getCell(4);
							traNum = (int) cell.getNumericCellValue();
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {// 6.业务保护等级
							cell = row.getCell(5);
							protectLevel = TrafficLevel.trans(cell.getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try { // 7.电交叉，缺省为点交叉
							cell = row.getCell(6);
							if (cell != null && cell.getStringCellValue().trim().equals("NO"))
								isElectricalCrossConnection = false;
							else
								isElectricalCrossConnection = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
						try { // 8.预置共享，缺省为共享
							cell = row.getCell(7);
							if (cell != null && cell.getStringCellValue().trim().equals("EXCLUSIVE"))
								isShare = false;
							else
								isShare = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
						
//						try {// 9.共享风险业务组索引
//							cell = row.getCell(8);
//							if (cell == null) {
//								idOfTrafficGroup = 0;
//							} // 没有关联业务组索引就初始为0
//							 else {
//							 suoyin=cell.getStringCellValue();
//							 Group=suoyin.substring(suoyin.length()-2);
//							 idOfTrafficGroup=Integer.parseInt(Group);
//							 }
////							else
////								idOfTrafficGroup = (int) cell.getNumericCellValue();
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
						
						
						try {// 9.共享风险业务组索引
							cell = row.getCell(8);
							if (cell == null||cell.getStringCellValue().equals("常规")) {
								trafficGroup = null;
							} // 没有关联业务组索引就初始为0
							 else {
								 suoyin=cell.getStringCellValue();
								 int groupid=getGroupid(suoyin);
								 String belongGroup=getBelongGroup(suoyin);
								 trafficGroup=new TrafficGroupNew(groupid, belongGroup);							
						}
							} catch (Exception e) {
							e.printStackTrace();
						}
						try {// 10.共享风险业务组类型
							cell = row.getCell(9);
							if (cell == null)
								{traGroType = "无";}
							else
								traGroType = cell.getStringCellValue();		
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {// 11.必经站点
							cell = row.getCell(10);
							if (cell == null)
								MustPassNode = null;
							else
								MustPassNode = CommonNode.getNode(cell.getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {// 12.必避站点
							cell = row.getCell(11);
							if (cell == null)
								MustAvoidNode = null;
							else
								MustAvoidNode = CommonNode.getNode(cell.getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {// 13.必经光纤
							cell = row.getCell(12);
							if (cell == null)
								MustPassLink = null;
							else
								MustPassLink = FiberLink.getLink(cell.getStringCellValue());
						} catch (Exception e) {

							e.printStackTrace();
						}
						try {// 14.必经光纤
							cell = row.getCell(13);
							if (cell == null)
								MustAvoidLink = null;
							else
								MustAvoidLink = FiberLink.getLink(cell.getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try { // 15.是否已有业务，缺省为无
							cell = row.getCell(14);
							if (cell.getStringCellValue().trim().equals("YES"))
								isHaveTraffic = true;
							else
								isHaveTraffic = false;
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {// 16.已有业务 不会还没写呢
							cell = row.getCell(15);
							if (cell == null) {
								existRoute = null;
							} else {
								existRoute = Route.rgRoute(cell.getStringCellValue());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {// 17.已有业务波长 不会还没写呢
							cell = row.getCell(16);
							if (cell == null) {
								existWaveLength = 0;
							} else {
								existWaveLength = (int) cell.getNumericCellValue();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						try {// 18.oduk要求
							cell = row.getCell(17);
						if (cell == null) {
							return;
						} else {
						  {
							if(cell.getStringCellValue().trim().equals("odu0")) {
								//System.out.println("odu0");
								rate = TrafficRate.GE;
								nrate = TrafficRate.Rate2Num(rate);
							}
							if(cell.getStringCellValue().trim().equals("odu1")) {
							//	System.out.println("odu1");
								rate = TrafficRate.G2Dot5;
								nrate = TrafficRate.Rate2Num(rate);
								//rate = TrafficRate.NumToRate(cell.getStringCellValue());
							}
							if(cell.getStringCellValue().trim().equals("odu2")) {
								//System.out.println("odu2");
								rate = TrafficRate.G10;
								nrate = TrafficRate.Rate2Num(rate);
							}
							if(cell.getStringCellValue().trim().equals("odu3")) {
								rate = TrafficRate.G40;
								nrate = TrafficRate.Rate2Num(rate);
							}
							if(cell.getStringCellValue().trim().equals("odu4")) {
								//System.out.println("odu4");
								rate = TrafficRate.G100;
								nrate = TrafficRate.Rate2Num(rate);
						    	}
							}
						}
							
					} catch (Exception e) {
						e.printStackTrace();
					}
					
						//

						//
						//
						//
						// try {// 1.业务数量
						// cell = row.getCell(0);
						// traNum = (int) cell.getNumericCellValue();
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
						//
						// try { // 2.name
						// cell = row.getCell(1);
						// name = cell.getStringCellValue().trim();
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
						//
						// try {// 3.源节点
						// cell = row.getCell(2);
						// fromNode = CommonNode.getNode(cell.getStringCellValue());
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
						//
						// try {// 4.宿节点
						// cell = row.getCell(3);
						// toNode = CommonNode.getNode(cell.getStringCellValue());
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
						//
						// try {// 5.业务速率
						// cell = row.getCell(4);
						// rate = TrafficRate.NumToRate(cell.getStringCellValue());
						// nrate = TrafficRate.Rate2Num(rate);
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
						//
						// try {// 6.业务保护等级
						// cell = row.getCell(5);
						// protectLevel = TrafficLevel.trans(cell.getStringCellValue());
						// } catch (Exception e) {
						// e.printStackTrace();
						// }

						int a = row.getRowNum() + 1;
						// 异常处理
						/*
						 * if (name.equals("")) { msgerr += "业务表格第" + a + "行，业务名称属性为空，请确认！" + "\n"; }
						 * else
						 */if (traNum == Integer.MAX_VALUE) {
							msgerr += "业务表格第" + a + "行，业务数量属性有错误，请确认！" + "\n";// 业务数量为何不能超过9？
						} else if (fromNode == null) {
							msgerr += "业务表格第" + a + "行，业务起始节点属性有错误，请确认！" + "\n";
						} else if (toNode == null) {
							msgerr += "业务表格第" + a + "行，业务目的节点属性有错误，请确认！" + "\n";
						} else if (fromNode.equals(toNode)) {
							msgerr += "业务表格第" + a + "行，业务起始节点和目的节点相同，产生错误，请确认！" + "\n";
						} else if (!protectLevel.equals(TrafficLevel.PERMANENT11)
								&& !protectLevel.equals(TrafficLevel.PROTECTandRESTORATION)
								&& !protectLevel.equals(TrafficLevel.RESTORATION)
								&& !protectLevel.equals(TrafficLevel.PresetRESTORATION)
								&& !protectLevel.equals(TrafficLevel.NORMAL11)
								&& !protectLevel.equals(TrafficLevel.NONPROTECT)) {
							msgerr += "业务表格第" + a + "行，业务保护级别属性有错误，请确认！" + "\n";
						} else if (rate == null) {
							msgerr += "业务表格第" + a + "行，业务速率属性有错误，请确认！" + "\n";
						} 
						else if(nrate<temp) { 
							msgerr="业务表格第" + a + "行，业务带宽与oduk设定不匹配，请确认！" + "\n";
						}

							else {

							// 节点ID补零(补为3位数)
							String fromIDString = String.valueOf(fromNode.getId());
							String toIDString = String.valueOf(toNode.getId());
							if (fromNode.getId() < 10) {
								fromIDString = "00" + fromIDString;
							} else if (fromNode.getId() < 100) {
								fromIDString = "0" + fromIDString;
							} else {
							}
							if (toNode.getId() < 10) {
								toIDString = "00" + toIDString;
							} else if (toNode.getId() < 100) {
								toIDString = "0" + toIDString;
							} else {
							}

							// id自动命名 cc:如何新版命名捏？
//							idString = layerString.trim()+fromIDString + toIDString + "01";
//							String maxidString = layerString.trim() + fromIDString + toIDString + rateString.trim()
//									+ "9";
//							id = Integer.parseInt(idString);
//							int maxid = Integer.parseInt(maxidString);
						
							
							
							if(trafficGroup!=null) {
								trafficGroup.setType(traGroType);
							}

							// 新建业务
							for (int t = traNum; t > 0; --t) {
								int traid = Traffic.trafficList.size() + 1;
								Traffic t1 = new Traffic(traid, rankId, fromNode, toNode, rate, traNum, protectLevel,
										isElectricalCrossConnection, isShare, trafficGroup, 
										MustPassNode, MustAvoidNode, MustPassLink, MustAvoidLink, isHaveTraffic,
										existRoute, existWaveLength);
								t1.setNumRank(traNum+1-t);
								

								t1.setNrate(nrate);
								addTrafficPort(t1);
								String a1=String.valueOf(id);

								// 如果导入时该业务以有路由及使用波长，则对该业务进行路由及资源分配
								if (existRoute != null && existWaveLength != 0&&(a1.substring(a1.length()-1).equals("0"))) {
									existRoute.setBelongsTraffic(t1); // 设置该路由的belongTraffic属性

									List<WDMLink> wdmLinkList = existRoute.getWDMLinkList(); // 储存该路由的WDM链路
									for (int hop = 0; hop < wdmLinkList.size(); hop++) {// 为每一条wdmLink链路分配连续波长
										int startWL = existWaveLength - 1;// 由于WaveLengthList时从0开始的 第0个对象存的是波道id为1的，以此类推
										WDMLink wdmLink = wdmLinkList.get(hop);
										// 设置空闲波长为工作并承载链路
										wdmLink.getWaveLengthList().get(startWL).setStatus(Status.工作);// 改变工作状态
										wdmLink.getWaveLengthList().get(startWL).setCarriedTraffic(t1);// 为波长添加承载业务
										wdmLink.getCarriedTrafficList().add(t1);// 为链路添加承载业务
										wdmLink.setRemainResource(wdmLink.getRemainResource() - 1);// 更新链路剩余资源
									}
									
									t1.setWorkRoute(existRoute);// 将该路由设为工作路由
									for(int i=0;i<existRoute.getWDMLinkList().size();i++)
									{
										t1.getWorkRoute().getWaveLengthIdList().add(existWaveLength - 1);
									}
									t1.getWorkRoute().setUsedWaveList(t1.getWorkRoute().getWaveLengthIdList());    //存用的不同波长，计算中继会用到

									// 进行端口资源分配
									PortAlloc.allocatePort(existRoute);
								}

								Traffic.trafficList.add(t1);
								//如果其共享风险业务组属性不为空，就将该业务加到GrouptrafficList
								if(t1.getTrafficgroup()!=null) {
									t1.getTrafficgroup().getGrouptrafficList().add(t1);
								}
								id++;
								f++;
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
			}
			Traffic.setAllTrafficId();
			int sum=0;
			for(int i=0;i<Traffic.trafficList.size();i++) {
				Traffic tra=Traffic.trafficList.get(i);
				if(tra.getWorkRoute()!=null)
					sum++;
			}
			
			if(sum==0) {
			msgt = "成功导入" + f + "条业务数据";}
			else
			{
				msgt = "成功导入" + f + "条业务数据,其中"+sum+"条业务已分配路由及波长";
			}
		}

	}

	/**
	 * 函数名：outputTraffic 功能： 导出 traffic链路资源 输出：一张已存数据的xls表
	 * 
	 * @param str
	 */
	// public void outputTraffic(String str) {
	// String msg = "";
	// HSSFWorkbook wb = null;
	// // POIFSFileSystem fs = null;
	// HSSFSheet sheet = null;
	// try {
	// // fs = new POIFSFileSystem(new FileInputStream(str));
	// wb = new HSSFWorkbook();
	// sheet = wb.createSheet("业务");
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// HSSFRow row = sheet.createRow(0);
	//
	// HSSFCell cell = row.createCell(0);
	// cell.setCellValue("ID");
	// // cell.setCellStyle(style1);
	// cell = row.createCell(1);
	// cell.setCellValue("名称");
	// cell = row.createCell(2);
	// cell.setCellValue("首节点");
	// cell = row.createCell(3);
	// cell.setCellValue("末节点");
	// cell = row.createCell(4);
	// cell.setCellValue("工作路由");
	// cell = row.createCell(5);
	// cell.setCellValue("OSNR(dB)");
	// cell = row.createCell(6);
	// cell.setCellValue("保护路由");
	// cell = row.createCell(7);
	// cell.setCellValue("OSNR(dB)");
	// cell = row.createCell(8);
	// cell.setCellValue("业务速率(Gbit/s)");
	// cell = row.createCell(9);
	// cell.setCellValue("保护级别");
	//
	// int i = 1;
	// DecimalFormat df = new DecimalFormat("0.0000 ");
	// OSNR osnr = new OSNR();
	// for (i = 1; i < Traffic.trafficList.size() + 1; i++) {
	// Traffic traffic = Traffic.trafficList.get(i - 1);
	// row = sheet.createRow(i);
	// cell = row.createCell(0);
	// cell.setCellValue(traffic.getId());
	// cell = row.createCell(1);
	// cell.setCellValue(traffic.getName());
	// cell = row.createCell(2);
	// cell.setCellValue(traffic.getFromNode().getName());
	// cell = row.createCell(3);
	// cell.setCellValue(traffic.getToNode().getName());
	// cell = row.createCell(4);
	// // String workroute = "";
	// // if (traffic.getWorkRoute() != null) {
	// // for (CommonNode node : traffic.getWorkRoute().getNodeList()) {
	// // workroute = workroute + node.getName() + " ";
	// // }
	// // }
	// // cell.setCellValue(workroute);
	// StringBuffer bufferw = new StringBuffer();// 工作路由
	// if (traffic.getWorkRoute() != null && null !=
	// traffic.getWorkRoute().getNodeList()) {
	// for (int n = 0; n < traffic.getWorkRoute().getNodeList().size(); n++) {
	// bufferw.append(traffic.getWorkRoute().getNodeList().get(n).getName());
	// if (n != traffic.getWorkRoute().getNodeList().size() - 1) {
	//
	// bufferw.append("--<" +
	// traffic.getWorkRoute().getWDMLinkList().get(n).getName() + ">--");
	// }
	// }
	// }
	// cell.setCellValue(bufferw.toString());
	//
	// cell = row.createCell(5);
	// if (traffic.getWorkRoute() != null)
	// cell.setCellValue(df.format(osnr.calculateOSNR(traffic.getWorkRoute())));
	// else
	// cell.setCellValue("");
	// cell = row.createCell(6);
	// StringBuffer bufferp = new StringBuffer();// 保护路由
	// if (null != traffic.getProtectRoute() && null !=
	// traffic.getProtectRoute().getNodeList()) {
	// for (int n = 0; n < traffic.getProtectRoute().getNodeList().size(); n++) {
	// bufferp.append(traffic.getProtectRoute().getNodeList().get(n).getName());
	// if (n != traffic.getProtectRoute().getNodeList().size() - 1) {
	//
	// bufferp.append("--<" +
	// traffic.getProtectRoute().getWDMLinkList().get(n).getName() + ">--");
	// }
	// }
	// }
	// cell.setCellValue(bufferp.toString());
	// cell = row.createCell(7);
	//
	// if (traffic.getProtectRoute() != null)
	// cell.setCellValue(df.format(osnr.calculateOSNR(traffic.getProtectRoute())));
	// else
	// cell.setCellValue("");
	// cell = row.createCell(8);
	// cell.setCellValue(traffic.getNrate());
	// cell = row.createCell(9);
	// if (traffic.getProtectLevel().toString().equals("PERMANENT11"))
	// cell.setCellValue("永久1+1");
	// else if (traffic.getProtectLevel().toString().equals("NORMAL11"))
	// cell.setCellValue("1+1");
	// else if (traffic.getProtectLevel().toString().equals("RESTORATION"))
	// cell.setCellValue("恢复");
	// else if
	// (traffic.getProtectLevel().toString().equals("PROTECTandRESTORATION"))
	// cell.setCellValue("保护+恢复");
	// else if (traffic.getProtectLevel().toString().equals("PresetRESTORATION"))
	// cell.setCellValue("专享预置恢复");
	// else if (traffic.getProtectLevel().toString().equals("NONPROTECT"))
	// cell.setCellValue("无保护");
	// }
	// FileOutputStream os = null;
	// try {
	// os = new FileOutputStream(str);
	// } catch (FileNotFoundException e) {
	// // TODO 自动生成 catch 块
	// e.printStackTrace();
	// }
	// try {
	// wb.write(os);
	// } catch (IOException e) {
	// // TODO 自动生成 catch 块
	// e.printStackTrace();
	// }
	// try {
	// os.close();
	// } catch (IOException e) {
	// // TODO 自动生成 catch 块
	// e.printStackTrace();
	// }
	// msg = "成功导出SpanLink链路" + (i - 1) + "条";
	// System.out.println(msg);
	// }
	//

	public void addTrafficPort(Traffic tra) {// 主要是添加支路端口,并把状态设为空闲
		PortRate pRate = TrafficRate.T2PRate(tra.getRate());// 端口速率设置
		CommonNode from = tra.getFromNode();
		CommonNode to = tra.getToNode();
		int portnum = 2;
		int fromportid = 0;
		int toportid = 0;

		fromportid = from.getPortList().size() + 1;// 从1开始分配ID
		toportid = to.getPortList().size() + 1;
		for (int a = 0; a < portnum; a++) {
			Port p = new Port(fromportid++, from, pRate, PortKind.支路端口, PortStatus.空闲);
			from.getPortList().add(p);
		}
		for (int b = 0; b < portnum; b++) {
			Port p = new Port(toportid++, to, pRate, PortKind.支路端口, PortStatus.空闲);
			to.getPortList().add(p);
		}
	}

	public void OutPutRoute(FiberLink fl) {

		if (index == 0) {
			JFileChooser chooser = new JFileChooser();
			if (null == filepath) {// 如果当前不存在正在操作的工程
				chooser = new JFileChooser();
			} else {
				filelist = filepath;
				chooser = new JFileChooser(filelist);// 打开默认路径为工程的路径
			}
			chooser.setDialogTitle("链路单断信息表");
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
				if (!(filelist.endsWith(".xls"))) {// 如果现在的文件名里不包含扩展名，就给其加上
					filelist = filelist + ".xls";
					Sfilelist = filelist;
					System.out.println(filelist);
				}
			}
			if (option == JFileChooser.CANCEL_OPTION) {// 如果点击取消
  				index=2;
  				return;
  			}


			File sfile = new File(Sfilelist);
			if (sfile.exists()) {// 如果这个文件已经存在了
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
						TrafficOutPut(Sfilelist, fl);
						index=1;
						//JOptionPane.showMessageDialog(null, "表格已覆盖");
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "请关闭原Excel文件，否则无法完成覆盖");
						index=2;
					}
					// setVisible(false);
					// dispose();
					break;
				case 1:
  					index=2;
					return;
				case 2:
  					index=2;
					return;
				}
			} else {

				TrafficOutPut(Sfilelist, fl);
				index=1;
			}
		} 
		else if(index==1) {
		TrafficOutPut(Sfilelist, fl);
		index = 1;
		}

	}

	public void TrafficOutPut(String str, FiberLink fl) {
		// System.out.println("不想写了");
		HSSFWorkbook wb = null;
		HSSFSheet sheet1 = null;
		POIFSFileSystem fins = null;
		// HSSFSheet sheet2 = null;
		// HSSFSheet sheet3 = null;
		// HSSFSheet sheet4 = null;
		try {
			if (index == 1) {
				fins = new POIFSFileSystem(new FileInputStream(str));
				wb = new HSSFWorkbook(fins);
				try {
					if (fl.getFiberRelatedList().size() != 0) {
						List<FiberLink> temp = fl.getFiberRelatedList().get(0).getSRLGFiberLinkList();
						sheet1 = wb.createSheet(LinkRGroup.PutOutSRLG(temp)+"链路单断");
						
					} else {
						sheet1 = wb.createSheet(fl.getName() + "链路单断");
					}
				} catch (Exception e) {
					
				}

			} 
			else if(index == 0) {
				wb = new HSSFWorkbook();
				if (fl.getGroupSRLG().size() != 0) {
					List<FiberLink> temp = fl.getFiberRelatedList().get(0).getSRLGFiberLinkList();
					sheet1 = wb.createSheet(LinkRGroup.PutOutSRLG(temp)+"链路单断");
				} else {
					sheet1 = wb.createSheet(fl.getName() + "链路单断");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			HSSFRow Row1 = sheet1.createRow(0);
			HSSFCell cell1 = Row1.createCell(0);
			cell1.setCellValue("影响的业务编号");
			cell1 = Row1.createCell(1);
			cell1.setCellValue("起点");
			cell1 = Row1.createCell(2);
			cell1.setCellValue("终点");
			cell1 = Row1.createCell(3);
			cell1.setCellValue("电中继节点");
			cell1 = Row1.createCell(4);
			cell1.setCellValue("恢复路由");
			cell1 = Row1.createCell(5);
			cell1.setCellValue("波道号");
			cell1 = Row1.createCell(6);
			cell1.setCellValue("谱宽");
			cell1 = Row1.createCell(7);
			cell1.setCellValue("ODU2序号");
			cell1 = Row1.createCell(8);
			cell1.setCellValue("ODU1序号");
			cell1 = Row1.createCell(9);
			cell1.setCellValue("长度（km）");
			cell1 = Row1.createCell(10);
			cell1.setCellValue("等效跨段数量");
			cell1 = Row1.createCell(11);
			cell1.setCellValue("正向OSNR");
			cell1 = Row1.createCell(12);
			cell1.setCellValue("反向ODNR");
			cell1 = Row1.createCell(13);
			cell1.setCellValue("OSNR容限");
			cell1 = Row1.createCell(14);
			cell1.setCellValue("DGD值");
			cell1 = Row1.createCell(15);
			cell1.setCellValue("DGD容限");
			cell1 = Row1.createCell(16);
			cell1.setCellValue("Pre-FEC BER值");
			cell1 = Row1.createCell(17);
			cell1.setCellValue("Q值");

			for (int i = 0; i < Evaluation.PutOutTraffic.size(); i++) {
				Traffic tra = Evaluation.PutOutTraffic.get(i);
				Row1 = sheet1.createRow(i + 1);
				cell1 = Row1.createCell(0);
				cell1.setCellValue(tra.getTrafficId());
				cell1 = Row1.createCell(1);
				cell1.setCellValue(tra.getFromNode().getName());
				
				cell1 = Row1.createCell(2);
				cell1.setCellValue(tra.getToNode().getName());
				
				cell1 = Row1.createCell(3);
				if(tra.getResumeRoute() != null) {
				cell1.setCellValue(tra.getResumeRoute().waveChangedNode());
				}
				cell1 = Row1.createCell(4);
				if(tra.getResumeRoute() != null) {
				cell1.setCellValue(tra.getResumeRoute().toString());
				}
				
				cell1 = Row1.createCell(5);
				if (tra.getResumeRoute() != null) {
					cell1.setCellValue(tra.getResumeRoute().getWaveLengthIdListToString());
				}
				
				// cell2.setCellValue(tra.getResumeRoute().getWDMLinkList().get().getName());
				cell1 = Row1.createCell(9);
				if (tra.getResumeRoute() != null) {
					cell1.setCellValue(tra.getResumeRoute().routelength());
				}
				
				//等效跨段数
				cell1 = Row1.createCell(10);
				if(tra.getResumeRoute()!= null) {
				cell1.setCellValue(OSNR.crossSum(tra.getResumeRoute()));
				}
				
				//正向OSNR(OSNR模拟值)
				cell1 = Row1.createCell(11);
				if(tra.getResumeRoute()!= null) {
				cell1.setCellValue(OSNR.calculateOSNR(tra.getResumeRoute()));
				}
				
				//OSNR容限
				cell1 = Row1.createCell(13);
				if(tra.getResumeRoute()!= null) {					
				cell1.setCellValue(OSNR.crossOSNR(tra.getResumeRoute()));
				}
								
				
				// cell2.setCellValue(tra.getLength());
			}
		} catch (Exception e1) {

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
}

// public static boolean isTraIDRepeat(int id) {
// Iterator<Traffic> it = Traffic.trafficList.iterator();
// while (it.hasNext()) {
// Traffic tra = it.next();
// if (tra.getId() == id)
// return true;
// }
// return false;
//
// }
// }
