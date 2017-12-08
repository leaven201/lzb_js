package dialog;

import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import data.DataSave;
import twaver.TDataBox;
import twaver.TWaverConst;
import twaver.network.background.ImageBackground;
//import database.Database;
import datastructure.UIFiberLink;
import datastructure.UINode;
import database.*;
//import design.Colorutil;
import design.NetDesign_zs;
import enums.PortRate;

public class Dlg_NewProject extends JDialog implements ActionListener{
	private JPanel jp1 = new JPanel();
	private JPanel jp2 = new JPanel();
	private JPanel jp3 = new JPanel();
	private JPanel jp4 = new JPanel();
	private JPanel jp5 = new JPanel();
	private JPanel jp6 = new JPanel();
	private JPanel jp7 = new JPanel();
	private JPanel jp8 = new JPanel();
	private JPanel jp9 = new JPanel();
	private JPanel jp10 = new JPanel();
	private JRadioButton shangeyes = new JRadioButton("有");
	private JRadioButton shangeno = new JRadioButton("无");
	ButtonGroup slgroup=new ButtonGroup();
//	private JTextField sulv = new JTextField(20);
	String []sl={"100"};
	JComboBox slcombobox=new JComboBox(sl);
	private JTextField bodao = new JTextField(20);
	private JTextField path = new JTextField(20);
    private JTextField name = new JTextField(20);
//	private JTextArea information = new JTextArea(6,21);
//	private JTextField yeear = new JTextField(4);
//	private JTextField month = new JTextField(3);
//	private JTextField day = new JTextField(3);
	private String nowDate = "";
	private JButton  btnSelect;
	private String filelist = "nothing";
	private String project = "nothing";
	private NewFileChooser chooser = new NewFileChooser( );
	private ImageIcon title = new ImageIcon(getClass().getResource("/resource/光网络智能规划平台.jpg"));
	private TDataBox box;
	public Dlg_NewProject(TDataBox box1){
		setTitle("工程创建");		
		box = box1;
		box.setName("数据库");
		//title.setImage(title.getImage().getScaledInstance(500,60,Image.SCALE_DEFAULT)); 
		StringBuffer result = new StringBuffer();
		String resultlong = new String();
		resultlong = ReadData.ReadData(result).toString();
		int index1 = resultlong.indexOf("\n");
		NetDesign_zs.openfilelist = resultlong.substring(0, index1);
		NetDesign_zs.openproject = resultlong.substring(index1+1, resultlong.length());
		project = NetDesign_zs.openproject;
		this.setModal(true);
		setSize(500, 410);
		this.setLocationRelativeTo(null);
	    JPanel pane = new JPanel();
	    JPanel buttonpane = new JPanel();
	    
	    JLabel titlepane = new JLabel(title);
	    
	    
	    JLabel name1 = new JLabel("网络名称",JLabel.CENTER);
	    name1.setFont(new Font("微软雅黑",1,15));
	    JLabel path1 = new JLabel("存储路径",JLabel.CENTER);
	    path1.setFont(new Font("微软雅黑",1,15));
	    JLabel sulv1 =new JLabel("系统速率",JLabel.CENTER);
	    sulv1.setFont(new Font("微软雅黑",1,15));
	    JLabel sulv2 =new JLabel("Gbit/s",JLabel.CENTER);
	    sulv2.setFont(new Font("微软雅黑",1,15));
	    JLabel bodao1 =new JLabel("波道数量",JLabel.CENTER);
	    bodao1.setFont(new Font("微软雅黑",1,15));
	    JLabel shange1 =new JLabel("灵活栅格",JLabel.CENTER);
	    shange1.setFont(new Font("微软雅黑",1,15));
	    JLabel confirm1 =new JLabel("  ");
	    JLabel cancel1 =new JLabel("  ");
	    JLabel name2 =new JLabel("  ");
	    JLabel path2 =new JLabel("  ");
	    JLabel btnSelect1 =new JLabel("  ");
	    shangeyes.setFont(new Font("微软雅黑",1,15));
	    shangeno.setFont(new Font("微软雅黑",1,15));
//	    title1.setFont(new   java.awt.Font("Dialog",  Font.BOLD,   18));
//	    title2.setFont(new   java.awt.Font("Dialog",  Font.BOLD,   18));
//	    title3.setFont(new   java.awt.Font("Dialog",  Font.BOLD,   18));
//	    title4.setFont(new   java.awt.Font("Dialog",  Font.BOLD,   18));
//	    year1.setFont(new   java.awt.Font("Dialog",  Font.BOLD,   18));
//	    month1.setFont(new   java.awt.Font("Dialog",  Font.BOLD,   18));
//	    day1.setFont(new   java.awt.Font("Dialog",  Font.BOLD,   18));
	    
//	    GregorianCalendar calendar=new GregorianCalendar(); 
//	    int nowyear = calendar.get(calendar.YEAR);
//	    int nowmonth = calendar.get(calendar.MONTH)+1; 
//	    int today = calendar.get(calendar.DAY_OF_MONTH);
//	    year.setText(String.valueOf(nowyear));
//	    month.setText(String.valueOf(nowmonth));
//	    day.setText(String.valueOf(today));
	    
	    
	    btnSelect =new JButton("浏览"); 
	    btnSelect.setFont(new Font("微软雅黑",1,12));
	    JButton  confirm =new JButton("确定");
	    confirm.setFont(new Font("微软雅黑",1,15));
	    JButton cancel = new JButton("取消");
	    cancel.setFont(new Font("微软雅黑",1,15));
	    btnSelect.addActionListener(this); 
	    confirm.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){

	    		if(name.getText().isEmpty()){
	    			JOptionPane.showMessageDialog(null, "请输入工程名！ ");
	    		}
	    		else if(path.getText().isEmpty()){
	    			JOptionPane.showMessageDialog(null, "请输入存储路径！ ");
	    		}else if(bodao.getText().isEmpty()){
	    			JOptionPane.showMessageDialog(null, "请输入波道数量！");
//	    		}else if(sulv.getText().isEmpty()){
//	    			JOptionPane.showMessageDialog(null, "请输入系统速率！");
	    		}else if(!shangeyes.isSelected()&&!shangeno.isSelected()){
	    			JOptionPane.showMessageDialog(null, "请选择栅格特性！");
	    		}
	    		else if(!bodao.getText().isEmpty()&&!path.getText().isEmpty()&&!name.getText().isEmpty()&&!project.equals("nothing")){
	    			//@TY宇少爷  2017/9/22
	    			//调取波道数值
	    			DataSave.waveNum=Integer.parseInt(bodao.getText());
	    			//调取系统速率
	    			DataSave.judge=PortRate.stringToRate((String) slcombobox.getSelectedItem());
					DataSave.systemRate=PortRate.Rate2Num(DataSave.judge);
					//调取灵活栅格
					DataSave.flexibleRaster=shangeyes.isSelected();
					/*调试用  System.out.println("DataSave.waveNum:"+DataSave.waveNum);
					System.out.println("DataSave.systemrate:"+DataSave.systemrate);
					System.out.println("DataSave.flexibleraster:"+DataSave.flexibleraster);*/
	    			//2017/9/22
	    			NetDesign_zs.filename=name.getText()+".dat";          //
	    			filelist = path.getText()+"\\"+name.getText();
	    			File newFile = new File(filelist);
	    			if(newFile.exists()){
	    				JOptionPane.showMessageDialog(null,"工程"+name.getText()+"已经存在，请重新命名");
	    			}
	    			else{
	    				newFile.mkdir();
	    				NetDesign_zs.filepath=filelist;
	    				NetDesign_zs.filenameall = filelist+"\\"+name.getText()+".dat";
	    				NetDesign_zs.openproject = project;
	    				try {
	    					filelist = filelist + ".dat";
	    					Database.serialize(NetDesign_zs.filenameall);
	    				//	NetDesign_zs.printout("新建工程于"+NetDesign_zs.filenameall);
	    				//	NetDesign_zs.printout("新建工程信息:"+NetDesign_zs.fileinfo);
	    					} catch (Exception e2) {
	    						// TODO: handle exception
//	    						JOptionPane.showMessageDialog(null, "找不到指定路径！");
//	    						NetDesign_zs.printout("找不到指定路径！");
	    					}
	    				Database.setFileProperty(NetDesign_zs.fileinfo);
	    				Database.serialize(NetDesign_zs.filenameall);
	    				PrintWriter pw;
	    		        try {
	    		        	File txtfile = new File(System.getProperty("user.dir")+"\\文件读取位置.txt");
	    		            txtfile.delete(); 
	    		            txtfile.createNewFile(); 
	    		            String sets = "attrib +H \"" + txtfile.getAbsolutePath() + "\""; 
	    		            // 输出命令串 
	    		            System.out.println(sets); 
	    		            // 运行命令串 
	    		            Runtime.getRuntime().exec(sets); 
	    		  			pw = new PrintWriter( new FileWriter( "文件读取位置.txt" ));
	    		  	        pw.println(NetDesign_zs.openfilelist+"\r\n"+NetDesign_zs.openproject);
	    		  	        pw.close();
	    		  		} catch (IOException e2) {
	    		  			// TODO Auto-generated catch block
	    		  			e2.printStackTrace();
	    		  		}
	    				 int endIndex = NetDesign_zs.filename.length()-4;
	    				 box.setName(NetDesign_zs.filename.substring(0, endIndex));
	    				setVisible(false);
	    				dispose();//释放窗体所占内存资源?
	    			}
//	    			NetDesign_zs.setNodeLevelSelect(0, true);
//	    			NetDesign_zs.setNodeLevelSelect(1, false);
//	    			NetDesign_zs.setNodeLevelSelect(2, false);
	    		}
	    	}
	    });
	   
	    cancel.addActionListener(new ActionListener(){
	    	public void actionPerformed(ActionEvent e){
	    		setVisible(false);
				dispose();
	    	}
	    });
	    addWindowListener(new WindowAdapter(){
	    	public void windowClosing(WindowEvent e){
	            setVisible(false);
	            dispose();
	    	}
	    });
//	    pane.setBorder (BorderFactory.createRaisedBevelBorder());
	    Insets insert;
	   // jp10.add(titlepane);
	    GridBagLayout gbl=new GridBagLayout();
	    GridBagConstraints gbc1= new GridBagConstraints();
	    jp10.setLayout(gbl);
	    gbc1.gridy=0;
	    gbc1.gridx=0;
	    gbc1.insets = new Insets(0, 0, 0, 0);
	    gbl.setConstraints(titlepane, gbc1);
        jp10.add(titlepane);
	    //jp1.setLayout(new GridLayout(1,3,0,5));
	    //jp1.add(name1);
	    //jp1.add(name);
	    //jp1.add(name2);
	    //GridBagLayout gbl=new GridBagLayout();
	    //GridBagConstraints gbc1= new GridBagConstraints();
	    jp1.setLayout(gbl);
	    gbc1.weightx=1;
	    gbc1.weighty=1;
	    gbc1.gridy=0;
	    gbc1.gridx=0;
	    gbc1.anchor =  GridBagConstraints.CENTER;
	    gbc1.insets = new Insets(8, 5, 8, 5);
	    gbl.setConstraints(name1, gbc1);
        jp1.add(name1);
        gbc1.gridx=1;
        gbc1.anchor =  GridBagConstraints.CENTER;
        gbc1.insets = new Insets(8, 5, 8, 5);
        gbc1.fill = gbc1.VERTICAL;        
        gbl.setConstraints(name, gbc1);
        jp1.add(name);
        gbc1.gridx=2;
        gbc1.insets = new Insets(0, 5, 0, 5);        
        gbl.setConstraints(name2, gbc1);
        jp1.add(name2);
	    //jp2.setLayout(new GridLayout(1,3,0,5));
	    //jp2.add(bodao1);
	    //jp2.add(bodao);
	    //jp2.add(path2);
        //GridBagLayout gbl=new GridBagLayout();
	    //GridBagConstraints gbc1= new GridBagConstraints();
	    jp2.setLayout(gbl);
	    gbc1.weightx=1;
	    gbc1.weighty=1;
	    gbc1.gridy=0;
	    gbc1.gridx=0;
	    gbc1.anchor =  GridBagConstraints.CENTER;
	    gbc1.insets = new Insets(8, 5, 8, 5);
	    gbl.setConstraints(bodao1, gbc1);
        jp2.add(bodao1);
        gbc1.gridx=1;
        gbc1.anchor =  GridBagConstraints.CENTER;
        gbc1.insets = new Insets(8, 5, 8, 5);
        gbc1.fill = gbc1.VERTICAL;        
        gbl.setConstraints(bodao, gbc1);
        jp2.add(bodao);
        gbc1.gridx=2;
        gbc1.insets = new Insets(0, 5, 0, 5);        
        gbl.setConstraints(path2, gbc1);
        jp2.add(path2);
	    //jp3.setLayout(new GridLayout(1,3,0,5));
	    //jp3.add(path1);
	    //jp3.add(path);
	    //jp3.add(jp8);
        //GridBagLayout gbl=new GridBagLayout();
	    //GridBagConstraints gbc1= new GridBagConstraints();
	    jp3.setLayout(gbl);
	    gbc1.weightx=1;
	    gbc1.weighty=1;    
	    gbc1.gridy=0;
	    gbc1.gridx=0;
	    //gbc1.anchor =  GridBagConstraints.CENTER;
	    gbc1.insets = new Insets(8, 22, 8, 0);
	    gbl.setConstraints(path1, gbc1);
        jp3.add(path1);
        gbc1.gridx=1;
        //gbc1.anchor =  GridBagConstraints.EAST;    
        gbc1.insets = new Insets(8, 42, 8, 0);     
        gbc1.fill = gbc1.VERTICAL;        
        gbl.setConstraints(path, gbc1);
        jp3.add(path);      
        gbc1.gridx=2;        
        gbc1.insets = new Insets(8, 0, 0, 0);     
       // gbc1.anchor =  GridBagConstraints.WEST;
        gbl.setConstraints(jp8, gbc1);
        jp3.add(jp8);
//	    jp8.setLayout(new GridLayout(1,2,0,5));
	    jp8.add(btnSelect);
//	    jp8.add(btnSelect1);
	    
	    //jp4.setLayout(new GridLayout(1,3,0,5));
	    //jp4.add(sulv1);
	    //jp4.add(slcombobox);
	    //jp4.add(sulv2);o
	    jp4.setLayout(gbl);
	    gbc1.weightx=1;
	    gbc1.weighty=1;    
	    gbc1.gridy=0;
	    gbc1.gridx=0;	    
	    gbc1.insets = new Insets(8, 3, 8, 30);
	    gbl.setConstraints(sulv1, gbc1);
        jp4.add(sulv1);
        gbc1.gridx=1;   
        //gbc1.gridwidth = 2;
        gbc1.insets = new Insets(8, 5, 8, 0);
        gbc1.fill = gbc1.HORIZONTAL;        
        gbl.setConstraints(slcombobox, gbc1);
        jp4.add(slcombobox);      
        gbc1.gridx=2;  
        gbc1.insets = new Insets(8, 0, 8, 110);
        gbl.setConstraints(sulv2, gbc1);
        jp4.add(sulv2);
//	    jp8.setLayout(new GridLayout(1,2,0,5));
	    jp8.add(btnSelect);
	    //jp5.setLayout(new GridLayout(1,3,0,5));
	    //jp5.add(shange1);
	    //jp5.add(shangeyes);
	    //jp5.add(shangeno);
	    //slgroup.add(shangeyes);
	    //slgroup.add(shangeno);
	    //shangeno.setSelected(true);
	    jp5.setLayout(gbl);
	    gbc1.weightx=1;
	    gbc1.weighty=1;    
	    gbc1.gridy=0;
	    gbc1.gridx=0;	    
	    gbc1.insets = new Insets(8, 20, 8, 60);
	    gbl.setConstraints(shange1, gbc1);
        jp5.add(shange1);
        gbc1.gridx=1;      
        gbc1.fill = gbc1.VERTICAL;    
        gbc1.insets = new Insets(8, 0, 8, 50); 
        gbl.setConstraints(shangeyes, gbc1);
        jp5.add(shangeyes);      
        gbc1.gridx=2;        
        //gbc1.insets = new Insets(0, 0, 0, 5);   
        gbc1.insets = new Insets(8, 0, 8, 120); 
        gbl.setConstraints(shangeno, gbc1);
        jp5.add(shangeno);
        slgroup.add(shangeyes);
	    slgroup.add(shangeno);
	    shangeno.setSelected(true);
	    //jp7.setLayout(new GridLayout(1,2,0,0));
	    //jp7.add(jp6);
	    //jp6.add(confirm);
	    //jp7.add(jp9);
	    //jp9.add(cancel);
	    jp7.setLayout(gbl);
	    gbc1.weightx=1;
	    gbc1.weighty=1;
	    gbc1.gridy=0;
	    gbc1.gridx=0;
	    gbc1.anchor =  GridBagConstraints.CENTER;
	    gbc1.insets = new Insets(-10, 50, 10, -20);
	    gbl.setConstraints(jp6, gbc1);
        jp7.add(jp6);
        gbc1.gridx=1;
        gbc1.anchor =  GridBagConstraints.CENTER;
        gbc1.insets = new Insets(-10, -10, 10, 30);        
        gbl.setConstraints(jp9, gbc1);
        jp7.add(jp9);
        jp6.add(confirm);
        jp9.add(cancel);
	    this.setLayout(new GridLayout(7,1,0,0));
	    this.add(jp10);
	    this.add(jp1);
	    this.add(jp2);
	    this.add(jp3);
	    this.add(jp4);
	    this.add(jp5);
	    this.add(jp7);
     	this.setVisible(true);
     	ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/resource/ddd1111.png"));
    	this.setIconImage(imageIcon.getImage());
     	//ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/resource/ddd1111.png"));
    	//this.setIconImage(imageIcon.getImage());
    	this.setIconImage((new ImageIcon("src/resource/小图标.png")).getImage());
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		 if(e.getSource()==btnSelect){
			 JFileChooser chooser = new JFileChooser( );
		        if(project.equals("nothing")){
		        	chooser = new JFileChooser( );
		        }
		        else{
		        	chooser = new JFileChooser(project );
		        }
				chooser.setDialogTitle("新建");
				chooser.setAcceptAllFileFilterUsed(false);
				FileFilter filter = new FileNameExtensionFilter("dat", "dat");
				chooser.setFileFilter(filter);
				chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int option = chooser.showOpenDialog(this);
				if (option == JFileChooser.APPROVE_OPTION) {
		    	File file = chooser.getSelectedFile();
		    	System.out.println(file.getPath().toString());
//		        filelist = file.getPath()+".dat";
		    	project=file.getPath();
		        path.setText(project); 
		       
				}
			}
		
	}
	
	public static boolean deleteAllFile(String fileFolder){   
        boolean ret = false;   
        File file = new File(fileFolder);
        if(file.exists()){
        	if(file.isDirectory()){
        		File[] fileList = file.listFiles();
        		for (int i = 0; i < fileList.length; i++) {
        			String filePath = fileList[i].getPath();
        			deleteAllFile(filePath);
        			}
        		}
        	if(file.isFile()){
        		file.delete();
        		}
        	}
        file.delete();  
        return ret;
    } 
	
}

class ReadData {
	 /**
	  */
	public static StringBuffer ReadData(StringBuffer result){
		File file = new File(System.getProperty("user.dir")+"\\文件读取位置.txt");
		if(!file.exists()){
			result.append("nothing\nnothing");
		}
		else{
			try {
				FileReader read = new FileReader(System.getProperty("user.dir")+"\\文件读取位置.txt");
				BufferedReader br = new BufferedReader(read);
				String row;
				int i = 0;
				while((row = br.readLine())!=null){
					i++;
					result.append(row);
					if(1 == i)
						result.append("\n");
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e){
				e.printStackTrace();
			}
		}
		return result;
	}
}