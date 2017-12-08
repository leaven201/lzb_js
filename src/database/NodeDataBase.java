package database;
import java.awt.Component;
import java.awt.Container;
import java.io.File;
/**
 * �ڵ����ݵĵ��뵼������excel������򣬴ӳ��򵼳�excel
 * @author ����ʫ��
 */
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import data.CommonNode;
import data.Traffic;
import design.NetDesign_zs;
import enums.NodeType;
import survivance.Evaluation;

public class NodeDataBase {

    public String msg; // �������ݳɹ���ʾ��Ϣ
    public static boolean flag; // �ñ�־λ�ж��ڵ���������Ƿ�������ݸ�ʽ����false��ʾ���ڱ�������ݸ�ʽ����
    public static boolean flagnodenull;// �ñ�־λ���ǿհ׹��̻��ǽ����������ݽ��б�ʾ��flase��ʾ���е���֮ǰ��������
    public String msgr; // �������ݴ�����ʾ��Ϣ
    private String filelist = "wrong";
  	public static int index = 0;
  	public static String Sfilelist = "";

    /**
     * ��������inputNode  
     * ���ܣ�����ڵ���Դ    
     * ���룺xls�������
     * @param str
     */
    public void inputNode(String str){
	msg = "";
	flag = true;
	flagnodenull = true;
	msgr = "";
	
	
	if (CommonNode.allNodeList.size() != 0)
	    flagnodenull = false;
	
	FileInputStream fins=null;
	POIFSFileSystem pfs=null;
	HSSFWorkbook wb=null;
	HSSFSheet sheet=null;
	try{
	    fins=new FileInputStream(str);
	    pfs = new POIFSFileSystem(fins);
	    wb = new HSSFWorkbook(pfs);
	    sheet = wb.getSheet("վ��");
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
		    int id=Integer.MAX_VALUE;// �ڵ�ID��Ψһ
		    String name="";// �ڵ����ƣ�Ψһ
		    String OtherName="";//վ�����
		    double longitude=Integer.MAX_VALUE;// ����
		    double latitude=Integer.MAX_VALUE;// γ��
		    boolean isActive=true;// �Ƿ񼤻�
		    boolean isOA=true;// �Ƿ�Ϊ��Žڵ�
		    boolean isROADM=true;// �Ƿ�֧��ROADM
		    boolean isOTN=true;//�Ƿ����OTN����
		    int maxPortNum=Integer.MAX_VALUE;// �ڵ��������Ķ˿���
		    double switchCapacity=Double.MAX_VALUE;//��������
		    NodeType nodeType=null;//�ڵ����ͣ��⡢�硢���ˣ�
		    int numOB=Integer.MAX_VALUE;
		    int numOP=Integer.MAX_VALUE;
		    int numOA=Integer.MAX_VALUE;
		    boolean isEleRegeneration=true;//������
		    boolean isWaveConversion=true;//����ת��
		    
		    boolean iszhongji=true;//lzb+9.21
		    int WSS=9;
		    int upDown=0;//lzb-9.21
		    
		    Cell cell=null;
		    try{//1.id
			cell=row.getCell(0);
			id=(int) cell.getNumericCellValue();
		    }catch(Exception e){
			e.printStackTrace();
		    }
		    
		    try { // 2.name
			cell = row.getCell(1);
			name = cell.getStringCellValue().trim();
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		    
		    try{//3.nodeType
				cell=row.getCell(2);
				nodeType=NodeType.stringToNodeType(cell.getStringCellValue().trim());
				nodeType=NodeType.valueOf(cell.getStringCellValue().trim());
			    }catch(Exception e){
				e.printStackTrace();
			    }
		    

		    try { // 4.longitude
			cell = row.getCell(3);
			longitude = cell.getNumericCellValue()/30+75;
	/*		if((0<longitude&&longitude<1)||(-1<longitude&&longitude<0)){
				longitude=longitude*10;
			}
			else if((1<=longitude&&longitude<=10)||(-10<=longitude&&longitude<=-1)){
				longitude=longitude*5;}
//			}else if((10<longitude&&longitude<=145)||(-145<=longitude&&longitude<-10)) {
//				longitude=longitude;
//			}else if((145<longitude)||(longitude<-145)) {
//				longitude=longitude/5;
//			}
			if ((90 <= longitude) || (longitude <= -70)) {
			    longitude = (longitude / 20) + 30;
			}*/
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		    
		    try { // 5.latitude
			cell = row.getCell(4);
			latitude = cell.getNumericCellValue()/30+30;
	/*		if ((0 < latitude && latitude < 1) || (-1 < latitude && latitude < 0)) {
			    latitude = latitude * 10;
			} else if ((1 <= latitude && latitude <= 10) || (-10 <= latitude && latitude <= -1)) {
			    latitude = latitude * 5;
			}
			if ((90 <= latitude) || (latitude <= -70)) {
			    latitude = (latitude / 20) + 30;
			}*/
		    } catch (Exception e) {
			e.printStackTrace();
		    }
		    
		    try { // 6.OtherName
				cell = row.getCell(5);
				OtherName = cell.getStringCellValue().trim();
			    } catch (Exception e) {
				e.printStackTrace();
			    }

		    try{//7.����·������
			cell=row.getCell(6);
			if(cell!=null)
				upDown=(int) cell.getNumericCellValue();		
		    }catch(Exception e){
			e.printStackTrace();
		    }
		    
//		    try { // 5.isActive ��ȱʡΪ����
//			cell = row.getCell(4);
//			if (cell.getStringCellValue().trim().equals("��"))
//			    isActive = false;
//			else
//			    isActive = true;
//		    } catch (Exception e) {
//			e.printStackTrace();
//		    }
//		    
//		    try { // 6.isOA ��ȱʡΪ����
//			cell = row.getCell(5);
//			if (cell.getStringCellValue().trim().equals("��"))
//			    isOA = false;
//			else{
//			    isOA = true;
//			    nodeType=NodeType.OA;
//			}
//		    } catch (Exception e) {
//			e.printStackTrace();
//		    }
//		    
//		    try { // 7.isROADM ��ȱʡΪ����
//			cell = row.getCell(6);
//			if (cell.getStringCellValue().trim().equals("��"))
//			    isROADM = false;
//			else
//			    isROADM = true;
//		    } catch (Exception e) {
//			e.printStackTrace();
//		    }
//		    
//		    try { // 8.isOTN ��ȱʡΪ����
//			cell = row.getCell(7);
//			if (cell.getStringCellValue().trim().equals("��"))
//			    isOTN = false;
//			else
//			    isOTN = true;
//		    } catch (Exception e) {
//			e.printStackTrace();
//		    }
//		    
//		    try{//9.maxPortNum
//			cell=row.getCell(8);
//			maxPortNum=(int) cell.getNumericCellValue();
//		    }catch(Exception e){
//			e.printStackTrace();
//		    }
//		    
//		    try{//10.switchCapacity
//			cell=row.getCell(9);
//			switchCapacity=cell.getNumericCellValue();
//		    }catch(Exception e){
//			e.printStackTrace();
//		    }
//		    
//		    try{//11.OB����
//			cell=row.getCell(10);
//			numOB=(int) cell.getNumericCellValue();
//		    }catch(Exception e){
//			e.printStackTrace();
//		    }
//		    
//		    try{//12.OP����
//			cell=row.getCell(11);
//			numOP=(int) cell.getNumericCellValue();
//		    }catch(Exception e){
//			e.printStackTrace();
//		    }
//		    
//		    try{//13.OA����
//			cell=row.getCell(12);
//			numOA=(int) cell.getNumericCellValue();
//		    }catch(Exception e){
//			e.printStackTrace();
//		    }
//		    
//		    try { // 14.isEleRegeneration ��ȱʡΪ����
//			cell = row.getCell(13);
//			if (cell.getStringCellValue().trim().equals("��"))
//			    isEleRegeneration = false;
//			else{
//			    isEleRegeneration = true;
//			}
//		    } catch (Exception e) {
//			e.printStackTrace();
//		    }
//		    
//		    try { // 15.isWaveConversion ��ȱʡΪ����
//			cell = row.getCell(14);
//			if (cell.getStringCellValue().trim().equals("��"))
//			    isWaveConversion = false;
//			else{
//			    isWaveConversion = true;
//			}
//		    } catch (Exception e) {
//			e.printStackTrace();
//		    }
		    
		    
		    
		    //��ʾ��Ϣ
		    int a = row.getRowNum() + 1;
		    boolean isIDRepeat = CommonNode.isIdRepeat(id);
		    if(isIDRepeat||id==Integer.MAX_VALUE){
			msgr += "�ڵ����" + a + "�У��ڵ�ID�����д���(�磺�ظ�)����ȷ�ϣ�" + "\n";
		    }else if (name.equals("")) {
			msgr += "�ڵ����" + a + "�У��ڵ����������д�����ȷ�ϣ�" + "\n";
		    } else if (longitude == Integer.MAX_VALUE) {
			msgr += "�ڵ����" + a + "�У��ڵ㾭�������д�����ȷ�ϣ�" + "\n";
		    } else if (latitude == Integer.MAX_VALUE) {
			msgr += "�ڵ����" + a + "�У��ڵ�γ�������д�����ȷ�ϣ�" + "\n";
		    }/*else if (maxPortNum == Integer.MAX_VALUE) {
			msgr += "�ڵ����" + a + "�У����˿��������д�����ȷ�ϣ�" + "\n";
		    }else if (switchCapacity == Double.MAX_VALUE) {
			msgr += "�ڵ����" + a + "�У��������������д�����ȷ�ϣ�" + "\n";
		    }*/else{//���ù������½��ڵ�,�����䵽����������
				CommonNode node=new CommonNode(id, name,OtherName, longitude, latitude, 
					    nodeType,iszhongji,WSS,upDown,true);  //lzb9.22
			f++;//ͳ�ƽڵ���Ŀ
		    
		    }
		}
	    }
	}
	try {//�ر���
		fins.close();
	    } catch (IOException e) {
		// TODO �Զ����� catch ��
		e.printStackTrace();
	    } finally {
		msg = "�ɹ�����ڵ�" + f + "��" + "\n";
		System.out.println(msg);
		System.out.println(msgr);
	    }
	}
	
    }
    
    /**
     * ��������outputNode 
     * ���ܣ� �����ڵ���Դ 
     * �����һ���Ѵ����ݵ�xls��
     * @param str
     */
    public void outputNode(String str) {

	HSSFWorkbook wb = null;
	POIFSFileSystem fs = null;
	HSSFSheet sheet = null;
	try {
	    fs = new POIFSFileSystem(new FileInputStream(str));
	    wb = new HSSFWorkbook(fs);
	    sheet = wb.createSheet("�ڵ�");
	} catch (Exception e) {
	    e.printStackTrace();
	}
//	for (int i = 6; i < 11; i++) {//����7��11�е��п�
//	    sheet.setColumnWidth(i, 4000); 
//	}
	HSSFRow row = sheet.createRow(0);

	HSSFCell cell = row.createCell(0);
	cell.setCellValue("ID");
	//cell.setCellStyle(style1);
	cell = row.createCell(1);
	cell.setCellValue("����");
	cell = row.createCell(2);
	cell.setCellValue("����");
	cell = row.createCell(3);
	cell.setCellValue("γ��");
	cell = row.createCell(4);
	cell.setCellValue("�Ƿ񼤻�");
	cell = row.createCell(5);
	cell.setCellValue("�Ƿ�Ϊ���");
	cell = row.createCell(6);
	cell.setCellValue("�Ƿ�ΪROADM");
	cell = row.createCell(7);
	cell.setCellValue("�Ƿ�֧��OTN");
	cell = row.createCell(8);
	cell.setCellValue("���˿�����");
	cell = row.createCell(9);
	cell.setCellValue("��������(Gbit/s)");
	cell = row.createCell(10);
	cell.setCellValue("�ڵ�����");
	int i=1;
	for (i = 1; i < CommonNode.allNodeList.size() + 1; i++) {
	    CommonNode node = CommonNode.allNodeList.get(i - 1);
	    row = sheet.createRow(i);
	    cell = row.createCell(0);
	    cell.setCellValue(node.getId());
	    cell = row.createCell(1);
	    cell.setCellValue(node.getName());
	    cell = row.createCell(2);
	    cell.setCellValue(node.getLongitude());
	    cell = row.createCell(3);
	    cell.setCellValue(node.getLatitude());
	    cell = row.createCell(4);
	    if (!node.isActive())
		cell.setCellValue("��");// Ĭ���Ǽ���״̬CC
	    else
		cell.setCellValue("��");
	    cell = row.createCell(5);
	    if (!node.isOA())
		cell.setCellValue("��");// Ĭ���Ǽ���״̬CC
	    else
		cell.setCellValue("��");
	    cell = row.createCell(6);
	    if (!node.isROADM())
		cell.setCellValue("��");// Ĭ���Ǽ���״̬CC
	    else
		cell.setCellValue("��");
	    cell = row.createCell(7);
	    if (!node.isOTN())
		cell.setCellValue("��");// Ĭ���Ǽ���״̬CC
	    else
		cell.setCellValue("��");
	    cell = row.createCell(8);
	    cell.setCellValue(node.getMaxPortNum());
	    cell = row.createCell(9);
	    cell.setCellValue(node.getSwitchCapacity());
	    cell = row.createCell(10);
	    //cell.setCellValue(NodeType.nodeTypeToString(node.getNodeType()));
	    cell.setCellValue(node.getNodeType().toString());
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
	msg="�ɹ������ڵ�"+(i-1)+"��";
	System.out.println("msg");
    }
    public void OutPutNodeDan( CommonNode node) {

  		if (index == 0) {
  			JFileChooser chooser = new JFileChooser();
  			if (null == NetDesign_zs.filepath) {// �����ǰ���������ڲ����Ĺ���
  				chooser = new JFileChooser();
  			} else {
  				filelist = NetDesign_zs.filepath;
  				chooser = new JFileChooser(filelist);// ��Ĭ��·��Ϊ���̵�·��
  			}
  			chooser.setDialogTitle("�ڵ㵥����Ϣ����·��");
  			chooser.setAcceptAllFileFilterUsed(false);
  			// ǰһ��xlsΪ��������ѡ���ļ�������ʾ����һ�����ļ����������ļ�����
  			FileNameExtensionFilter filter = new FileNameExtensionFilter("xls", "xls");
  			chooser.setFileFilter(filter);
  			// ��ȡ��ǰʱ��
  			Calendar cal = Calendar.getInstance();
  			SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd");
  			String date = dateformat.format(cal.getTime());
  			JTextField text = getTextField(chooser);// ��ȡ�����ļ�������
  			text.setText("�ڵ㵥����Ϣ��" + date);// ����Ĭ���ļ���
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
  						NodeDanOutPut(Sfilelist, node);
  						JOptionPane.showMessageDialog(null, "����Ѹ���");
  					} catch (Exception e1) {
  						JOptionPane.showMessageDialog(null, "��ر�ԭExcel�ļ��������޷���ɸ���");
  					}
  					// setVisible(false);
  					// dispose();
  					break;
  				case 1:
  					return;
  				case 2:
  					return;
  				}
  			} else {

  				NodeDanOutPut(Sfilelist, node);
  				// JOptionPane.showMessageDialog(null, "�����ѵ���");
  			}
  		}
  		else
  			NodeDanOutPut(Sfilelist,node);
  		index = 1;

  	}

  	public void NodeDanOutPut(String str, CommonNode node) {
  		
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
  			} else
  				wb = new HSSFWorkbook();
  			sheet1 = wb.createSheet(node.getName() + "�ڵ㵥��");
  			
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
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
  		cell1.setCellValue("���м̶�����");
  		cell1 = Row1.createCell(5);
  		cell1.setCellValue("������");
  		cell1 = Row1.createCell(6);
  		cell1.setCellValue("���ȣ�km��");
  		cell1 = Row1.createCell(7);
  		cell1.setCellValue("��Ч�������");
  		cell1 = Row1.createCell(8);
  		cell1.setCellValue("����OSNR");
  		cell1 = Row1.createCell(9);
  		cell1.setCellValue("����ODNR");
  		cell1 = Row1.createCell(10);
  		cell1.setCellValue("OSNR����");
  		cell1 = Row1.createCell(11);
  		cell1.setCellValue("DGDֵ");
  		cell1 = Row1.createCell(12);
  		cell1.setCellValue("DGD����");
  		cell1 = Row1.createCell(13);
  		cell1.setCellValue("Pre-FEC BERֵ");
  		cell1 = Row1.createCell(14);
  		cell1.setCellValue("Qֵ");

  		for (int i = 0; i < Evaluation.PutOutTraffic.size(); i++) {
  			Traffic tra = Evaluation.PutOutTraffic.get(i);
  			Row1 = sheet1.createRow(i + 1);
  			cell1 = Row1.createCell(0);
  			cell1.setCellValue(tra.toString());
  			cell1 = Row1.createCell(1);
  			cell1.setCellValue(tra.getFromNode().getName());
  			cell1 = Row1.createCell(2);
  			cell1.setCellValue(tra.getToNode().getName());
  			cell1 = Row1.createCell(3);
			cell1.setCellValue(tra.waveChangedNode());			
			cell1 = Row1.createCell(4);
			cell1.setCellValue(tra.workedRoute().toString());
			cell1 = Row1.createCell(5);
			if(tra.getWorkRoute()!=null)
			{
				cell1.setCellValue(tra.getWorkRoute().toStingwave());
			}
			// cell2.setCellValue(tra.getResumeRoute().getWDMLinkList().get().getName());
			cell1 = Row1.createCell(6);
			if(tra.getWorkRoute()!=null)
			{cell1.setCellValue(tra.getWorkRoute().routelength());}
			// cell2.setCellValue(tra.getLength());
  			
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
