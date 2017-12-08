package database;
import java.awt.Component;
import java.awt.Container;
import java.io.File;
/**
 * 节点数据的导入导出，从excel导入程序，从程序导出excel
 * @author 豹读诗书
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

    public String msg; // 导入数据成功提示信息
    public static boolean flag; // 该标志位判断在导入过程中是否存在数据格式错误，false表示存在表格内数据格式错误
    public static boolean flagnodenull;// 该标志位对是空白工程还是进来就有内容进行标示，flase标示进行导入之前就有内容
    public String msgr; // 导入数据错误提示信息
    private String filelist = "wrong";
  	public static int index = 0;
  	public static String Sfilelist = "";

    /**
     * 函数名：inputNode  
     * 功能：导入节点资源    
     * 输入：xls表格名称
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
	    sheet = wb.getSheet("站点");
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
		    int id=Integer.MAX_VALUE;// 节点ID，唯一
		    String name="";// 节点名称，唯一
		    String OtherName="";//站点别名
		    double longitude=Integer.MAX_VALUE;// 经度
		    double latitude=Integer.MAX_VALUE;// 纬度
		    boolean isActive=true;// 是否激活
		    boolean isOA=true;// 是否为光放节点
		    boolean isROADM=true;// 是否支持ROADM
		    boolean isOTN=true;//是否具有OTN功能
		    int maxPortNum=Integer.MAX_VALUE;// 节点最多包含的端口数
		    double switchCapacity=Double.MAX_VALUE;//交叉容量
		    NodeType nodeType=null;//节点类型（光、电、光纤）
		    int numOB=Integer.MAX_VALUE;
		    int numOP=Integer.MAX_VALUE;
		    int numOA=Integer.MAX_VALUE;
		    boolean isEleRegeneration=true;//电再生
		    boolean isWaveConversion=true;//波长转换
		    
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

		    try{//7.上下路分组数
			cell=row.getCell(6);
			if(cell!=null)
				upDown=(int) cell.getNumericCellValue();		
		    }catch(Exception e){
			e.printStackTrace();
		    }
		    
//		    try { // 5.isActive ，缺省为激活
//			cell = row.getCell(4);
//			if (cell.getStringCellValue().trim().equals("否"))
//			    isActive = false;
//			else
//			    isActive = true;
//		    } catch (Exception e) {
//			e.printStackTrace();
//		    }
//		    
//		    try { // 6.isOA ，缺省为激活
//			cell = row.getCell(5);
//			if (cell.getStringCellValue().trim().equals("否"))
//			    isOA = false;
//			else{
//			    isOA = true;
//			    nodeType=NodeType.OA;
//			}
//		    } catch (Exception e) {
//			e.printStackTrace();
//		    }
//		    
//		    try { // 7.isROADM ，缺省为激活
//			cell = row.getCell(6);
//			if (cell.getStringCellValue().trim().equals("否"))
//			    isROADM = false;
//			else
//			    isROADM = true;
//		    } catch (Exception e) {
//			e.printStackTrace();
//		    }
//		    
//		    try { // 8.isOTN ，缺省为激活
//			cell = row.getCell(7);
//			if (cell.getStringCellValue().trim().equals("否"))
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
//		    try{//11.OB数量
//			cell=row.getCell(10);
//			numOB=(int) cell.getNumericCellValue();
//		    }catch(Exception e){
//			e.printStackTrace();
//		    }
//		    
//		    try{//12.OP数量
//			cell=row.getCell(11);
//			numOP=(int) cell.getNumericCellValue();
//		    }catch(Exception e){
//			e.printStackTrace();
//		    }
//		    
//		    try{//13.OA数量
//			cell=row.getCell(12);
//			numOA=(int) cell.getNumericCellValue();
//		    }catch(Exception e){
//			e.printStackTrace();
//		    }
//		    
//		    try { // 14.isEleRegeneration ，缺省为激活
//			cell = row.getCell(13);
//			if (cell.getStringCellValue().trim().equals("否"))
//			    isEleRegeneration = false;
//			else{
//			    isEleRegeneration = true;
//			}
//		    } catch (Exception e) {
//			e.printStackTrace();
//		    }
//		    
//		    try { // 15.isWaveConversion ，缺省为激活
//			cell = row.getCell(14);
//			if (cell.getStringCellValue().trim().equals("否"))
//			    isWaveConversion = false;
//			else{
//			    isWaveConversion = true;
//			}
//		    } catch (Exception e) {
//			e.printStackTrace();
//		    }
		    
		    
		    
		    //提示信息
		    int a = row.getRowNum() + 1;
		    boolean isIDRepeat = CommonNode.isIdRepeat(id);
		    if(isIDRepeat||id==Integer.MAX_VALUE){
			msgr += "节点表格第" + a + "行，节点ID属性有错误(如：重复)，请确认！" + "\n";
		    }else if (name.equals("")) {
			msgr += "节点表格第" + a + "行，节点名称属性有错误，请确认！" + "\n";
		    } else if (longitude == Integer.MAX_VALUE) {
			msgr += "节点表格第" + a + "行，节点经度属性有错误，请确认！" + "\n";
		    } else if (latitude == Integer.MAX_VALUE) {
			msgr += "节点表格第" + a + "行，节点纬度属性有错误，请确认！" + "\n";
		    }/*else if (maxPortNum == Integer.MAX_VALUE) {
			msgr += "节点表格第" + a + "行，最大端口数属性有错误，请确认！" + "\n";
		    }else if (switchCapacity == Double.MAX_VALUE) {
			msgr += "节点表格第" + a + "行，交叉容量属性有错误，请确认！" + "\n";
		    }*/else{//调用构造器新建节点,并分配到所属链表中
				CommonNode node=new CommonNode(id, name,OtherName, longitude, latitude, 
					    nodeType,iszhongji,WSS,upDown,true);  //lzb9.22
			f++;//统计节点数目
		    
		    }
		}
	    }
	}
	try {//关闭流
		fins.close();
	    } catch (IOException e) {
		// TODO 自动生成 catch 块
		e.printStackTrace();
	    } finally {
		msg = "成功导入节点" + f + "个" + "\n";
		System.out.println(msg);
		System.out.println(msgr);
	    }
	}
	
    }
    
    /**
     * 函数名：outputNode 
     * 功能： 导出节点资源 
     * 输出：一张已存数据的xls表
     * @param str
     */
    public void outputNode(String str) {

	HSSFWorkbook wb = null;
	POIFSFileSystem fs = null;
	HSSFSheet sheet = null;
	try {
	    fs = new POIFSFileSystem(new FileInputStream(str));
	    wb = new HSSFWorkbook(fs);
	    sheet = wb.createSheet("节点");
	} catch (Exception e) {
	    e.printStackTrace();
	}
//	for (int i = 6; i < 11; i++) {//设置7到11列的列宽
//	    sheet.setColumnWidth(i, 4000); 
//	}
	HSSFRow row = sheet.createRow(0);

	HSSFCell cell = row.createCell(0);
	cell.setCellValue("ID");
	//cell.setCellStyle(style1);
	cell = row.createCell(1);
	cell.setCellValue("名称");
	cell = row.createCell(2);
	cell.setCellValue("经度");
	cell = row.createCell(3);
	cell.setCellValue("纬度");
	cell = row.createCell(4);
	cell.setCellValue("是否激活");
	cell = row.createCell(5);
	cell.setCellValue("是否为光放");
	cell = row.createCell(6);
	cell.setCellValue("是否为ROADM");
	cell = row.createCell(7);
	cell.setCellValue("是否支持OTN");
	cell = row.createCell(8);
	cell.setCellValue("最大端口数量");
	cell = row.createCell(9);
	cell.setCellValue("交叉容量(Gbit/s)");
	cell = row.createCell(10);
	cell.setCellValue("节点类型");
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
		cell.setCellValue("否");// 默认是激活状态CC
	    else
		cell.setCellValue("是");
	    cell = row.createCell(5);
	    if (!node.isOA())
		cell.setCellValue("否");// 默认是激活状态CC
	    else
		cell.setCellValue("是");
	    cell = row.createCell(6);
	    if (!node.isROADM())
		cell.setCellValue("否");// 默认是激活状态CC
	    else
		cell.setCellValue("是");
	    cell = row.createCell(7);
	    if (!node.isOTN())
		cell.setCellValue("否");// 默认是激活状态CC
	    else
		cell.setCellValue("是");
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
	msg="成功导出节点"+(i-1)+"个";
	System.out.println("msg");
    }
    public void OutPutNodeDan( CommonNode node) {

  		if (index == 0) {
  			JFileChooser chooser = new JFileChooser();
  			if (null == NetDesign_zs.filepath) {// 如果当前不存在正在操作的工程
  				chooser = new JFileChooser();
  			} else {
  				filelist = NetDesign_zs.filepath;
  				chooser = new JFileChooser(filelist);// 打开默认路径为工程的路径
  			}
  			chooser.setDialogTitle("节点单断信息表导出路径");
  			chooser.setAcceptAllFileFilterUsed(false);
  			// 前一个xls为窗口下拉选择文件类型显示，后一个是文件过滤器的文件类型
  			FileNameExtensionFilter filter = new FileNameExtensionFilter("xls", "xls");
  			chooser.setFileFilter(filter);
  			// 获取当前时间
  			Calendar cal = Calendar.getInstance();
  			SimpleDateFormat dateformat = new SimpleDateFormat("MM-dd");
  			String date = dateformat.format(cal.getTime());
  			JTextField text = getTextField(chooser);// 获取输入文件名部分
  			text.setText("节点单断信息表" + date);// 设置默认文件名
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
  						NodeDanOutPut(Sfilelist, node);
  						JOptionPane.showMessageDialog(null, "表格已覆盖");
  					} catch (Exception e1) {
  						JOptionPane.showMessageDialog(null, "请关闭原Excel文件，否则无法完成覆盖");
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
  				// JOptionPane.showMessageDialog(null, "数据已导出");
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
  			sheet1 = wb.createSheet(node.getName() + "节点单断");
  			
  		} catch (Exception e) {
  			e.printStackTrace();
  		}
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
  		cell1.setCellValue("电中继段名称");
  		cell1 = Row1.createCell(5);
  		cell1.setCellValue("波道号");
  		cell1 = Row1.createCell(6);
  		cell1.setCellValue("长度（km）");
  		cell1 = Row1.createCell(7);
  		cell1.setCellValue("等效跨段数量");
  		cell1 = Row1.createCell(8);
  		cell1.setCellValue("正向OSNR");
  		cell1 = Row1.createCell(9);
  		cell1.setCellValue("反向ODNR");
  		cell1 = Row1.createCell(10);
  		cell1.setCellValue("OSNR容限");
  		cell1 = Row1.createCell(11);
  		cell1.setCellValue("DGD值");
  		cell1 = Row1.createCell(12);
  		cell1.setCellValue("DGD容限");
  		cell1 = Row1.createCell(13);
  		cell1.setCellValue("Pre-FEC BER值");
  		cell1 = Row1.createCell(14);
  		cell1.setCellValue("Q值");

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
