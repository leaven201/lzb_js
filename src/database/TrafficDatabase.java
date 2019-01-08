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
 * ҵ�����ݵĵ��뵼������excel������򣬴ӳ��򵼳�excel�� �Լ���ҵ�����Ӧ�ڵ����֧·�˿���׼��������ҵ��
 * 
 * @author ����ʫ��
 */
public class TrafficDatabase {

	public String msgerr = "";
	public String msgt = "ҵ��δ����";
	public static int index = 0;
	private String filelist = "wrong";
	public static String Sfilelist = "";
	public String suoyin = null;
	public String Group = null;
	public static String filepath; // ����·��
	public static List<Sheet> str1 =new LinkedList<>();//���ڴ洢srlg�������������
	
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
			msgerr = "����δ�ɹ���������������Դ\n��δ�ɹ���������ҵ����Դ";
		}
		FileInputStream fins = null;
		POIFSFileSystem pfs = null;
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		try {
			fins = new FileInputStream(str);
			pfs = new POIFSFileSystem(fins);
			wb = new HSSFWorkbook(pfs);
			sheet = wb.getSheet("ҵ���б�");
		} catch (Exception e) {
			e.printStackTrace();
		}

		int f = 0;
		if (sheet != null) {
			outter: for (Row row : sheet) {
				if (row.getRowNum() != 0) {
					// ��ʽ����
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
						TrafficStatus status = TrafficStatus.δ����;
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
//						TrafficGroupType TraGroType = TrafficGroupType.��;
						String traGroType="��";
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
						try {// 2.Դ�ڵ�
							cell = row.getCell(1);
							fromNode = CommonNode.getNode(cell.getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {// 3.�޽ڵ�
							cell = row.getCell(2);
							toNode = CommonNode.getNode(cell.getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {// 4.ҵ�����ʣ�����
							cell = row.getCell(3);
							rate = TrafficRate.NumToRate(cell.getStringCellValue());
							nrate = TrafficRate.Rate2Num(rate);
							temp=nrate;
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {// 5.ҵ������
							cell = row.getCell(4);
							traNum = (int) cell.getNumericCellValue();
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {// 6.ҵ�񱣻��ȼ�
							cell = row.getCell(5);
							protectLevel = TrafficLevel.trans(cell.getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try { // 7.�罻�棬ȱʡΪ�㽻��
							cell = row.getCell(6);
							if (cell != null && cell.getStringCellValue().trim().equals("NO"))
								isElectricalCrossConnection = false;
							else
								isElectricalCrossConnection = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
						try { // 8.Ԥ�ù���ȱʡΪ����
							cell = row.getCell(7);
							if (cell != null && cell.getStringCellValue().trim().equals("EXCLUSIVE"))
								isShare = false;
							else
								isShare = true;
						} catch (Exception e) {
							e.printStackTrace();
						}
						
//						try {// 9.�������ҵ��������
//							cell = row.getCell(8);
//							if (cell == null) {
//								idOfTrafficGroup = 0;
//							} // û�й���ҵ���������ͳ�ʼΪ0
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
						
						
						try {// 9.�������ҵ��������
							cell = row.getCell(8);
							if (cell == null||cell.getStringCellValue().equals("����")) {
								trafficGroup = null;
							} // û�й���ҵ���������ͳ�ʼΪ0
							 else {
								 suoyin=cell.getStringCellValue();
								 int groupid=getGroupid(suoyin);
								 String belongGroup=getBelongGroup(suoyin);
								 trafficGroup=new TrafficGroupNew(groupid, belongGroup);							
						}
							} catch (Exception e) {
							e.printStackTrace();
						}
						try {// 10.�������ҵ��������
							cell = row.getCell(9);
							if (cell == null)
								{traGroType = "��";}
							else
								traGroType = cell.getStringCellValue();		
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {// 11.�ؾ�վ��
							cell = row.getCell(10);
							if (cell == null)
								MustPassNode = null;
							else
								MustPassNode = CommonNode.getNode(cell.getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {// 12.�ر�վ��
							cell = row.getCell(11);
							if (cell == null)
								MustAvoidNode = null;
							else
								MustAvoidNode = CommonNode.getNode(cell.getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {// 13.�ؾ�����
							cell = row.getCell(12);
							if (cell == null)
								MustPassLink = null;
							else
								MustPassLink = FiberLink.getLink(cell.getStringCellValue());
						} catch (Exception e) {

							e.printStackTrace();
						}
						try {// 14.�ؾ�����
							cell = row.getCell(13);
							if (cell == null)
								MustAvoidLink = null;
							else
								MustAvoidLink = FiberLink.getLink(cell.getStringCellValue());
						} catch (Exception e) {
							e.printStackTrace();
						}
						try { // 15.�Ƿ�����ҵ��ȱʡΪ��
							cell = row.getCell(14);
							if (cell.getStringCellValue().trim().equals("YES"))
								isHaveTraffic = true;
							else
								isHaveTraffic = false;
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {// 16.����ҵ�� ���ỹûд��
							cell = row.getCell(15);
							if (cell == null) {
								existRoute = null;
							} else {
								existRoute = Route.rgRoute(cell.getStringCellValue());
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
						try {// 17.����ҵ�񲨳� ���ỹûд��
							cell = row.getCell(16);
							if (cell == null) {
								existWaveLength = 0;
							} else {
								existWaveLength = (int) cell.getNumericCellValue();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}

						try {// 18.odukҪ��
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
						// try {// 1.ҵ������
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
						// try {// 3.Դ�ڵ�
						// cell = row.getCell(2);
						// fromNode = CommonNode.getNode(cell.getStringCellValue());
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
						//
						// try {// 4.�޽ڵ�
						// cell = row.getCell(3);
						// toNode = CommonNode.getNode(cell.getStringCellValue());
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
						//
						// try {// 5.ҵ������
						// cell = row.getCell(4);
						// rate = TrafficRate.NumToRate(cell.getStringCellValue());
						// nrate = TrafficRate.Rate2Num(rate);
						// } catch (Exception e) {
						// e.printStackTrace();
						// }
						//
						// try {// 6.ҵ�񱣻��ȼ�
						// cell = row.getCell(5);
						// protectLevel = TrafficLevel.trans(cell.getStringCellValue());
						// } catch (Exception e) {
						// e.printStackTrace();
						// }

						int a = row.getRowNum() + 1;
						// �쳣����
						/*
						 * if (name.equals("")) { msgerr += "ҵ�����" + a + "�У�ҵ����������Ϊ�գ���ȷ�ϣ�" + "\n"; }
						 * else
						 */if (traNum == Integer.MAX_VALUE) {
							msgerr += "ҵ�����" + a + "�У�ҵ�����������д�����ȷ�ϣ�" + "\n";// ҵ������Ϊ�β��ܳ���9��
						} else if (fromNode == null) {
							msgerr += "ҵ�����" + a + "�У�ҵ����ʼ�ڵ������д�����ȷ�ϣ�" + "\n";
						} else if (toNode == null) {
							msgerr += "ҵ�����" + a + "�У�ҵ��Ŀ�Ľڵ������д�����ȷ�ϣ�" + "\n";
						} else if (fromNode.equals(toNode)) {
							msgerr += "ҵ�����" + a + "�У�ҵ����ʼ�ڵ��Ŀ�Ľڵ���ͬ������������ȷ�ϣ�" + "\n";
						} else if (!protectLevel.equals(TrafficLevel.PERMANENT11)
								&& !protectLevel.equals(TrafficLevel.PROTECTandRESTORATION)
								&& !protectLevel.equals(TrafficLevel.RESTORATION)
								&& !protectLevel.equals(TrafficLevel.PresetRESTORATION)
								&& !protectLevel.equals(TrafficLevel.NORMAL11)
								&& !protectLevel.equals(TrafficLevel.NONPROTECT)) {
							msgerr += "ҵ�����" + a + "�У�ҵ�񱣻����������д�����ȷ�ϣ�" + "\n";
						} else if (rate == null) {
							msgerr += "ҵ�����" + a + "�У�ҵ�����������д�����ȷ�ϣ�" + "\n";
						} 
						else if(nrate<temp) { 
							msgerr="ҵ�����" + a + "�У�ҵ�������oduk�趨��ƥ�䣬��ȷ�ϣ�" + "\n";
						}

							else {

							// �ڵ�ID����(��Ϊ3λ��)
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

							// id�Զ����� cc:����°�������
//							idString = layerString.trim()+fromIDString + toIDString + "01";
//							String maxidString = layerString.trim() + fromIDString + toIDString + rateString.trim()
//									+ "9";
//							id = Integer.parseInt(idString);
//							int maxid = Integer.parseInt(maxidString);
						
							
							
							if(trafficGroup!=null) {
								trafficGroup.setType(traGroType);
							}

							// �½�ҵ��
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

								// �������ʱ��ҵ������·�ɼ�ʹ�ò�������Ը�ҵ�����·�ɼ���Դ����
								if (existRoute != null && existWaveLength != 0&&(a1.substring(a1.length()-1).equals("0"))) {
									existRoute.setBelongsTraffic(t1); // ���ø�·�ɵ�belongTraffic����

									List<WDMLink> wdmLinkList = existRoute.getWDMLinkList(); // �����·�ɵ�WDM��·
									for (int hop = 0; hop < wdmLinkList.size(); hop++) {// Ϊÿһ��wdmLink��·������������
										int startWL = existWaveLength - 1;// ����WaveLengthListʱ��0��ʼ�� ��0���������ǲ���idΪ1�ģ��Դ�����
										WDMLink wdmLink = wdmLinkList.get(hop);
										// ���ÿ��в���Ϊ������������·
										wdmLink.getWaveLengthList().get(startWL).setStatus(Status.����);// �ı乤��״̬
										wdmLink.getWaveLengthList().get(startWL).setCarriedTraffic(t1);// Ϊ������ӳ���ҵ��
										wdmLink.getCarriedTrafficList().add(t1);// Ϊ��·��ӳ���ҵ��
										wdmLink.setRemainResource(wdmLink.getRemainResource() - 1);// ������·ʣ����Դ
									}
									
									t1.setWorkRoute(existRoute);// ����·����Ϊ����·��
									for(int i=0;i<existRoute.getWDMLinkList().size();i++)
									{
										t1.getWorkRoute().getWaveLengthIdList().add(existWaveLength - 1);
									}
									t1.getWorkRoute().setUsedWaveList(t1.getWorkRoute().getWaveLengthIdList());    //���õĲ�ͬ�����������м̻��õ�

									// ���ж˿���Դ����
									PortAlloc.allocatePort(existRoute);
								}

								Traffic.trafficList.add(t1);
								//����乲�����ҵ�������Բ�Ϊ�գ��ͽ���ҵ��ӵ�GrouptrafficList
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
				// TODO �Զ����� catch ��
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
			msgt = "�ɹ�����" + f + "��ҵ������";}
			else
			{
				msgt = "�ɹ�����" + f + "��ҵ������,����"+sum+"��ҵ���ѷ���·�ɼ�����";
			}
		}

	}

	/**
	 * ��������outputTraffic ���ܣ� ���� traffic��·��Դ �����һ���Ѵ����ݵ�xls��
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
	// sheet = wb.createSheet("ҵ��");
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// HSSFRow row = sheet.createRow(0);
	//
	// HSSFCell cell = row.createCell(0);
	// cell.setCellValue("ID");
	// // cell.setCellStyle(style1);
	// cell = row.createCell(1);
	// cell.setCellValue("����");
	// cell = row.createCell(2);
	// cell.setCellValue("�׽ڵ�");
	// cell = row.createCell(3);
	// cell.setCellValue("ĩ�ڵ�");
	// cell = row.createCell(4);
	// cell.setCellValue("����·��");
	// cell = row.createCell(5);
	// cell.setCellValue("OSNR(dB)");
	// cell = row.createCell(6);
	// cell.setCellValue("����·��");
	// cell = row.createCell(7);
	// cell.setCellValue("OSNR(dB)");
	// cell = row.createCell(8);
	// cell.setCellValue("ҵ������(Gbit/s)");
	// cell = row.createCell(9);
	// cell.setCellValue("��������");
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
	// StringBuffer bufferw = new StringBuffer();// ����·��
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
	// StringBuffer bufferp = new StringBuffer();// ����·��
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
	// cell.setCellValue("����1+1");
	// else if (traffic.getProtectLevel().toString().equals("NORMAL11"))
	// cell.setCellValue("1+1");
	// else if (traffic.getProtectLevel().toString().equals("RESTORATION"))
	// cell.setCellValue("�ָ�");
	// else if
	// (traffic.getProtectLevel().toString().equals("PROTECTandRESTORATION"))
	// cell.setCellValue("����+�ָ�");
	// else if (traffic.getProtectLevel().toString().equals("PresetRESTORATION"))
	// cell.setCellValue("ר��Ԥ�ûָ�");
	// else if (traffic.getProtectLevel().toString().equals("NONPROTECT"))
	// cell.setCellValue("�ޱ���");
	// }
	// FileOutputStream os = null;
	// try {
	// os = new FileOutputStream(str);
	// } catch (FileNotFoundException e) {
	// // TODO �Զ����� catch ��
	// e.printStackTrace();
	// }
	// try {
	// wb.write(os);
	// } catch (IOException e) {
	// // TODO �Զ����� catch ��
	// e.printStackTrace();
	// }
	// try {
	// os.close();
	// } catch (IOException e) {
	// // TODO �Զ����� catch ��
	// e.printStackTrace();
	// }
	// msg = "�ɹ�����SpanLink��·" + (i - 1) + "��";
	// System.out.println(msg);
	// }
	//

	public void addTrafficPort(Traffic tra) {// ��Ҫ�����֧·�˿�,����״̬��Ϊ����
		PortRate pRate = TrafficRate.T2PRate(tra.getRate());// �˿���������
		CommonNode from = tra.getFromNode();
		CommonNode to = tra.getToNode();
		int portnum = 2;
		int fromportid = 0;
		int toportid = 0;

		fromportid = from.getPortList().size() + 1;// ��1��ʼ����ID
		toportid = to.getPortList().size() + 1;
		for (int a = 0; a < portnum; a++) {
			Port p = new Port(fromportid++, from, pRate, PortKind.֧·�˿�, PortStatus.����);
			from.getPortList().add(p);
		}
		for (int b = 0; b < portnum; b++) {
			Port p = new Port(toportid++, to, pRate, PortKind.֧·�˿�, PortStatus.����);
			to.getPortList().add(p);
		}
	}

	public void OutPutRoute(FiberLink fl) {

		if (index == 0) {
			JFileChooser chooser = new JFileChooser();
			if (null == filepath) {// �����ǰ���������ڲ����Ĺ���
				chooser = new JFileChooser();
			} else {
				filelist = filepath;
				chooser = new JFileChooser(filelist);// ��Ĭ��·��Ϊ���̵�·��
			}
			chooser.setDialogTitle("��·������Ϣ��");
			chooser.setAcceptAllFileFilterUsed(false);
			// ǰһ��xlsΪ��������ѡ���ļ�������ʾ����һ�����ļ����������ļ�����
			FileNameExtensionFilter filter = new FileNameExtensionFilter("xls", "xls");
			chooser.setFileFilter(filter);
			// ��ȡ��ǰʱ��
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd");
			String date = dateformat.format(cal.getTime());
			JTextField text = getTextField(chooser);// ��ȡ�����ļ�������
			text.setText("��·������Ϣ��" + date);// ����Ĭ���ļ���
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int option = chooser.showSaveDialog(chooser);
			if (option == JFileChooser.APPROVE_OPTION) {// ������ȷ��
				File file = chooser.getSelectedFile();
				filelist = file.getPath();
				if (!(filelist.endsWith(".xls"))) {// ������ڵ��ļ����ﲻ������չ�����͸������
					filelist = filelist + ".xls";
					Sfilelist = filelist;
					System.out.println(filelist);
				}
			}
			if (option == JFileChooser.CANCEL_OPTION) {// ������ȡ��
  				index=2;
  				return;
  			}


			File sfile = new File(Sfilelist);
			if (sfile.exists()) {// �������ļ��Ѿ�������
				// JOptionPane.showMessageDialog(null, "��ǰ�ļ��Ѵ���ڣ���ɾ�������������ٵ���");}
				int test = JOptionPane.showConfirmDialog(null, "��ǰ�ļ��Ѵ��ڣ��Ƿ񸲸ǣ�");
				switch (test) {
				case 0:// ����
						// PortDataBase portOut = new PortDataBase();
						// int id = Integer
						// .parseInt(String.valueOf(nodeModel.getValueAt(nodeTable.getSelectedRow(),
						// 0)));
						// theNode = CommonNode.getNode(id);
					try {
						TrafficOutPut(Sfilelist, fl);
						index=1;
						//JOptionPane.showMessageDialog(null, "����Ѹ���");
					} catch (Exception e1) {
						JOptionPane.showMessageDialog(null, "��ر�ԭExcel�ļ��������޷���ɸ���");
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
		// System.out.println("����д��");
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
						sheet1 = wb.createSheet(LinkRGroup.PutOutSRLG(temp)+"��·����");
						
					} else {
						sheet1 = wb.createSheet(fl.getName() + "��·����");
					}
				} catch (Exception e) {
					
				}

			} 
			else if(index == 0) {
				wb = new HSSFWorkbook();
				if (fl.getGroupSRLG().size() != 0) {
					List<FiberLink> temp = fl.getFiberRelatedList().get(0).getSRLGFiberLinkList();
					sheet1 = wb.createSheet(LinkRGroup.PutOutSRLG(temp)+"��·����");
				} else {
					sheet1 = wb.createSheet(fl.getName() + "��·����");
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			HSSFRow Row1 = sheet1.createRow(0);
			HSSFCell cell1 = Row1.createCell(0);
			cell1.setCellValue("Ӱ���ҵ����");
			cell1 = Row1.createCell(1);
			cell1.setCellValue("���");
			cell1 = Row1.createCell(2);
			cell1.setCellValue("�յ�");
			cell1 = Row1.createCell(3);
			cell1.setCellValue("���м̽ڵ�");
			cell1 = Row1.createCell(4);
			cell1.setCellValue("�ָ�·��");
			cell1 = Row1.createCell(5);
			cell1.setCellValue("������");
			cell1 = Row1.createCell(6);
			cell1.setCellValue("�׿�");
			cell1 = Row1.createCell(7);
			cell1.setCellValue("ODU2���");
			cell1 = Row1.createCell(8);
			cell1.setCellValue("ODU1���");
			cell1 = Row1.createCell(9);
			cell1.setCellValue("���ȣ�km��");
			cell1 = Row1.createCell(10);
			cell1.setCellValue("��Ч�������");
			cell1 = Row1.createCell(11);
			cell1.setCellValue("����OSNR");
			cell1 = Row1.createCell(12);
			cell1.setCellValue("����ODNR");
			cell1 = Row1.createCell(13);
			cell1.setCellValue("OSNR����");
			cell1 = Row1.createCell(14);
			cell1.setCellValue("DGDֵ");
			cell1 = Row1.createCell(15);
			cell1.setCellValue("DGD����");
			cell1 = Row1.createCell(16);
			cell1.setCellValue("Pre-FEC BERֵ");
			cell1 = Row1.createCell(17);
			cell1.setCellValue("Qֵ");

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
				
				//��Ч�����
				cell1 = Row1.createCell(10);
				if(tra.getResumeRoute()!= null) {
				cell1.setCellValue(OSNR.crossSum(tra.getResumeRoute()));
				}
				
				//����OSNR(OSNRģ��ֵ)
				cell1 = Row1.createCell(11);
				if(tra.getResumeRoute()!= null) {
				cell1.setCellValue(OSNR.calculateOSNR(tra.getResumeRoute()));
				}
				
				//OSNR����
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
