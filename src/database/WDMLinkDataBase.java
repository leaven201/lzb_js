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
 * WDM��·���ݵĵ��뵼������excel������򣬴ӳ��򵼳�excel �Լ��½���·��ɺ����Ӧ�ڵ�Ķ˿����
 * 
 * @author ����ʫ��
 *
 *//*
public class WDMLinkDataBase {
	public String msgf = ""; // ����WDMlink��Ϣ��ʾ����
	public String msgerr = ""; // ���뵼��������ʾ
	public static boolean flagf = true; // fiber�㵼���־
	public static boolean flagw = true;
	public static boolean flagsa = true;
	public static boolean flagfnull = true;// �ñ�־λ���ǿհ׹��̻��ǽ����������ݽ��б�ʾ��flase��ʾ���е���֮ǰ��������
	public static boolean flagwnull = true;

	*//**
	 * ��������inputWDMLink ���ܣ�����fiber����·��Դ ���룺xls�������
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
			msgerr = "δ����ڵ�˿���Ϣ" + "\n";
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
						// ��ʽ����
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
							CommonNode fromNode = null;// ��·Դ�ڵ�
							CommonNode toNode = null;// ��·�޽ڵ�
							double length = Double.MAX_VALUE;
							PortRate rate = null;
							boolean isActive = true;// �Ƿ񼤻�
							int size = Integer.MAX_VALUE;// ��·�����²�����ȵ�����,��������
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

							try { // 3.Դ�ڵ�
								cell = row.getCell(2);
								fromNode = CommonNode.getNode(cell.getStringCellValue().trim());
							} catch (Exception e) {
								e.printStackTrace();
							}

							try { // 4.�޽ڵ�
								cell = row.getCell(3);
								toNode = CommonNode.getNode(cell.getStringCellValue().trim());
							} catch (Exception e) {
								e.printStackTrace();
							}

							try { // 5.��·����
								cell = row.getCell(4);
								length = cell.getNumericCellValue();
							} catch (Exception e) {
								e.printStackTrace();
							}

							try { // 6.��·����,sizeӦ��Ҳ����������ʼ����������ôŪ��û���
								cell = row.getCell(5);
								rate = PortRate.stringToRate(cell.getStringCellValue().trim());
								size = 4;
							} catch (Exception e) {
								e.printStackTrace();
							}

							try { // 7.isActive ��ȱʡΪ����
								cell = row.getCell(6);
								if (cell.getStringCellValue().trim().equals("��"))
									isActive = false;
								else
									isActive = true;
							} catch (Exception e) {
								e.printStackTrace();
							}

							try { // 8.SRLG
								cell = row.getCell(7);
								if (cell == null)
									SRLG = "��";
								else
									SRLG = cell.getStringCellValue();

							} catch (Exception e) {
								e.printStackTrace();
							}

							try { // 9.��·������
								cell = row.getCell(8);
								linkLayer = Layer.stringToLinkType(cell.getStringCellValue());
							} catch (Exception e) {
								e.printStackTrace();
							}

							// ��ʾ��Ϣ
							int a = row.getRowNum() + 1;
							boolean isIDRepeat = BasicLink.isLinkIDRepeat(id);
							if (isIDRepeat || id == Integer.MAX_VALUE) {
								msgerr += "��·����" + a + "�У���·ID�����д���(�磺�ظ�)����ȷ�ϣ�" + "\n";
							} else if (name.equals("") || BasicLink.isLinkNameRepeat(name)) {
								msgerr += "��·����" + a + "�У���·���������д����������ظ�������ȷ�ϣ�" + "\n";
							} else if (fromNode == null) {
								msgerr += "��·����" + a + "�У���·�׽ڵ����������д�����˽ڵ㲻���ڣ�����ȷ�ϣ�" + "\n";
							} else if (toNode == null) {
								msgerr += "��·����" + a + "�У���·ĩ�ڵ����������д�����˽ڵ㲻���ڣ�����ȷ�ϣ�" + "\n";
							} else if (length == Double.MAX_VALUE) {
								msgerr += "��·����" + a + "�У���·���������д�����˽ڵ㲻���ڣ�����ȷ�ϣ�" + "\n";
							} else if (rate == null) {
								msgerr += "��·����" + a + "�У���·���������д�����ȷ�ϣ�" + "\n";
							} else if (name.equals("")) {
								msgerr += "��·����" + a + "�У���·SRLG�����д�����˽ڵ㲻���ڣ�����ȷ�ϣ�" + "\n";
							} else if (linkLayer == null) {
								msgerr += "��·����" + a + "�У���·�����������д�����ȷ�ϣ�" + "\n";
							} else if (linkLayer.equals(Layer.Fiber)) {
								if (fromNode.getNodeType().equals(NodeType.OLA)
										&& toNode.getNodeType().equals(NodeType.OLA)) {
									BasicLink link = null;
									if (length < 0.0001)
										length = BasicLink.getDistance(fromNode.getLatitude(), fromNode.getLongitude(),
												toNode.getLatitude(), toNode.getLongitude());// �����·����С��10cm������ݾ�γ�ȼ��㳤��
									link = new FiberLink(id, name, fromNode, toNode, length, rate, size, isActive, SRLG,
											linkLayer);
									BasicLink.addFiberLinkPort((FiberLink) link);
									f++;
								} else {
									msgerr += "��·����" + a + "�У���·��ĩ�ڵ㲻֧�ֹ���������·����ȷ�ϣ�" + "\n";
								}
							} else if (linkLayer.equals(Layer.WDM)) {
								if (fromNode.getNodeType().equals(NodeType.ROADM)
										&& toNode.getNodeType().equals(NodeType.ROADM)) {
									BasicLink link = null;
									if (length < 0.0001)
										length = BasicLink.getDistance(fromNode.getLatitude(), fromNode.getLongitude(),
												toNode.getLatitude(), toNode.getLongitude());// �����·����С��10cm������ݾ�γ�ȼ��㳤��
									link = new WDMLink(id, name, fromNode, toNode, length, rate, size, isActive, SRLG,
											linkLayer);
									BasicLink.addOptLinkPort((WDMLink) link);
									f++;
								} else {
									msgerr += "��·����" + a + "�У���·��ĩ�ڵ㲻֧�ֹ�������·����ȷ�ϣ�" + "\n";
								}
							} else if (linkLayer.equals(Layer.OTN)) {
								if (fromNode.getNodeType().equals(NodeType.OTN)
										&& toNode.getNodeType().equals(NodeType.OTN)) {
									BasicLink link = null;
									if (length < 0.0001)
										length = BasicLink.getDistance(fromNode.getLatitude(), fromNode.getLongitude(),
												toNode.getLatitude(), toNode.getLongitude());// �����·����С��10cm������ݾ�γ�ȼ��㳤��
									link = new OTNLink(id, name, fromNode, toNode, length, rate, size, isActive, SRLG,
											linkLayer);
									BasicLink.addEleLinkPort((OTNLink) link);
									f++;
								} else {
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
}
*/