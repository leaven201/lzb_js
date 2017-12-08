package dialog;
/***
 * ��Ҫ��д��Ӧ���ݵĵ���ѡ���ļ��ĶԻ����Լ���ǰ̨���ݵ���ͨ
 * 
 */

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ListIterator;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import twaver.Dummy;
import twaver.Node;
import twaver.TDataBox;
import twaver.TUIManager;
import twaver.TWaverConst;
import data.*;
import data.Traffic;
import dataControl.TrafficData;
import design.NetDesign_zs;

//������ݵ��� filelist Ϊ�����ļ���·���ļ���
public class Dlg_TrafficInput extends JFrame {
	private static String filelist = NetDesign_zs.openfilelist;
	private static TDataBox box;
    private static Dummy TrafficDummy;
   public Dlg_TrafficInput(TDataBox box1, Dummy TrafficDummy1){
    setSize(350, 200);
    box = box1;
    TrafficDummy = TrafficDummy1;
	new Thread(){
		public void run(){
			try {
    JFileChooser chooser = new JFileChooser( );
        if(filelist.equals("nothing")){
 		   chooser = new JFileChooser();
 	   }
 	   else{
 		   chooser = new JFileChooser(filelist);
 	   }
        chooser.setDialogTitle("ҵ����Դ����");
        chooser.setAcceptAllFileFilterUsed(false);
        FileFilter filter = new FileNameExtensionFilter("Excel�ļ�", "xls");
        chooser.setFileFilter(filter);
        int option = chooser.showOpenDialog(Dlg_TrafficInput.this);
        if (option == JFileChooser.APPROVE_OPTION) {
          File file = chooser.getSelectedFile( );

          if (file.exists()) 
          {  
              //��ʼ��������	 	
            	final Dlg_ProgressBar pro = new Dlg_ProgressBar();
            	pro.setIndeterminate();
            	pro.setVal(0);
            	pro.setVisible(true);

            	filelist = file.getPath();
            	File txtfile = new File(System.getProperty("user.dir")+"\\�ļ���ȡλ��.txt");
                txtfile.delete(); 
                txtfile.createNewFile(); 
                String sets = "attrib +H \"" + txtfile.getAbsolutePath() + "\""; 
                // ������ 
                System.out.println(sets); 
                // ������� 
                Runtime.getRuntime().exec(sets); 
            	NetDesign_zs.openfilelist = file.getParent().toString();
                PrintWriter pw;
                try {
        			pw = new PrintWriter( new FileWriter( "�ļ���ȡλ��.txt" ));
        	        pw.println(NetDesign_zs.openfilelist+"\r\n"+NetDesign_zs.openproject);
        	        pw.close();
        		} catch (IOException e2) {
        			// TODO Auto-generated catch block
        			e2.printStackTrace();
        		}
             TrafficData traffic=new TrafficData(); //��̨����ҵ��
             traffic.inputTraffic(filelist); 
         	
             //�رս���������	
             	pro.setVal(100);

             	TDataBox b1 = null;
            	Dummy d1 = null;
            	Dummy d2 = null;
            	Dummy d3 = null;
            	Dummy d4 = null;
            	Dlg_InputInfo input = new Dlg_InputInfo(box,d1,d2,d3,d4,TrafficDummy,1,0);//������Ϣ��
            }
        else {
        	System.out.println("����ǲ����ļ�ѡ�����");//cc
        }
     }
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
				}
			}
	}.start();
        
         
   }
   
   public static void inputUITrafficData(TDataBox box,Dummy trafficDummy){
//     ��̨ҵ�����ݵ���ǰ̨ 
    
      for(int i = 0;i <Traffic.trafficList.size();i++){
    	    Node thetraffic = new Node(Traffic.trafficList.get(i).getId()){
    	    	public int getHeight() {
    	    		return 0;
    	    		}
    	    		public int getWidth() {
    	    		return 0;
    	    		}
    	    };
    	    thetraffic.setName(Traffic.trafficList.get(i).getFromNode().getName()+"-"+Traffic.trafficList.get(i).getToNode().getName()+" "+i);
//    	    thetraffic.setImage(TWaverConst.BLANK_IMAGE);
//    	    TUIManager.registerWithoutImage(thetraffic.getClass());
    	    thetraffic.setLocation(99999999, 999999999);

    	    box.addElement(thetraffic);
    	    trafficDummy.addChild(thetraffic);
//    	    thetraffic.setVisible(false);
      }
//      for(int i = 0;i <Traffic.s_lWDMTrafficList.size();i++){
//    	  Node thetraffic = new Node(Traffic.s_lWDMTrafficList.get(i).getM_nID());
//  	    thetraffic.setName(Traffic.s_lWDMTrafficList.get(i).getM_sName());
//  	    box.addElement(thetraffic);
//  	    trafficDummy.addChild(thetraffic);	
////  	    thetraffic.setVisible(false);
//    }
//      for(int i = 0;i <Traffic.s_lOTNTrafficList.size();i++){
//    	  Node thetraffic = new Node(Traffic.s_lOTNTrafficList.get(i).getM_nID());
//  	    thetraffic.setName(Traffic.s_lOTNTrafficList.get(i).getM_sName());
//  	    box.addElement(thetraffic);
//  	    trafficDummy.addChild(thetraffic);
////  	  thetraffic.setVisible(false);
//    }
//      for(int i = 0;i <Traffic.s_lSDHTrafficList.size();i++){
//    	  Node thetraffic = new Node(Traffic.s_lSDHTrafficList.get(i).getM_nID());
//  	    thetraffic.setName(Traffic.s_lSDHTrafficList.get(i).getM_sName());
//  	    box.addElement(thetraffic);
//  	    trafficDummy.addChild(thetraffic);	
////  	  thetraffic.setVisible(false);
//    }
//      for(int i = 0;i <Traffic.s_lASONTrafficList.size();i++){
//    	  Node thetraffic = new Node(Traffic.s_lASONTrafficList.get(i).getM_nID());
//  	    thetraffic.setName(Traffic.s_lASONTrafficList.get(i).getM_sName());
//  	    box.addElement(thetraffic);
//  	    trafficDummy.addChild(thetraffic);	
////  	  thetraffic.setVisible(false);
//    }
      System.out.println("ҵ�������ѵ���");
      return;
   }
   
   
}

   
 


