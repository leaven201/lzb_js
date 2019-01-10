
package dialog;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import data.CommonNode;
import data.FiberLink;
//import data.SDHLink;
import data.WDMLink;
//import enums.Area;
import enums.Layer;
import twaver.Dummy;
import twaver.TDataBox;

public class Dlg_AddWDMlink {
	private Dummy linkDummy;
	 public void Dlg_AddWDMlink(TDataBox box1,Dummy linkDummy1){
	    	final JFrame frame=new JFrame();
	    	frame.setName("�����·");
			frame.setSize(520, 350);
			frame.setLocation(350, 200);
			frame.setVisible(true);
			frame.setTitle("�����·");
			ImageIcon imageIcon = new ImageIcon(this.getClass().getResource("/resource/ddd1111.png"));
			frame.setIconImage(imageIcon.getImage());
			linkDummy=linkDummy1;
			int maxid=0;
//			for(int i=0;i<FiberLink.fiberLinkList.size();i++){
//				if(maxid<FiberLink.fiberLinkList.get(i).getId()){
//					maxid=FiberLink.fiberLinkList.get(i).getId();
//				}
//			}
			final TDataBox box=box1;
			final JTextField  linkid = new JTextField(10);
	    	final JTextField  linkname = new JTextField(10);
	    	final JTextField  linklength = new JTextField(10);
	    	final JTextField  linksize = new JTextField(10);
	    	final JTextField  blocktime = new JTextField(10);
	    	final JTextField  averlose = new JTextField(10);
	    	final JTextField  startyear = new JTextField(10);
	    	final JTextField  endyear = new JTextField(10);
	    	linkid.setText(Integer.toString(maxid+1));
	    	linkname.setText("�½���·"+Integer.toString(maxid+1));
	    	Font f=new Font("΢���ź�", Font.PLAIN, 12);
	    	JRadioButton layer = new JRadioButton("WDM��",true);
	    	layer.setFont(f);
	    	JPanel pane = new JPanel();
			final JComboBox startCombo = new JComboBox();
			startCombo.setFont(f);
			for(int i = 0;i < CommonNode.getM_lsCommonNode().size();i++){
				startCombo.addItem(CommonNode.getM_lsCommonNode().get(i).getM_sName());
			}
			startCombo.setSelectedIndex(0);
			
			final JComboBox endCombo = new JComboBox();
			for(int i = 0;i < CommonNode.getM_lsCommonNode().size();i++){
				endCombo.addItem(CommonNode.getM_lsCommonNode().get(i).getM_sName());
			}
			endCombo.setSelectedIndex(0);
			endCombo.setFont(f);
			
			JButton confirmButton=new JButton("ȷ��");
			confirmButton.setFont(f);
			confirmButton.addActionListener(new ActionListener(){
		    	public void actionPerformed(ActionEvent e){
		    		String linkName,linkId;
		    		linkName = linkname.getText();
		    		linkId = linkid.getText();
		    	    if(linkName.isEmpty())
		    			JOptionPane.showMessageDialog(null,"�������·���ƣ�");
		    	    else if(!isInteger(linkId))
		 	    		JOptionPane.showMessageDialog(null,"��·IDӦΪ�ǿ����֣�");	
//		    	    else if(linkNameSame(linkname.getText(),Layer.Fiber))
//		    			JOptionPane.showMessageDialog(null,"����·�����Ѵ��ڣ�");	     	   
//		    	    else if(isRepeat(String.valueOf(linkId),Layer.Fiber))
//		    	    	JOptionPane.showMessageDialog(null,"��ID�Ѵ��ڣ�");
		    	    else if(startCombo.getSelectedItem().equals(endCombo.getSelectedItem()))
		    	    	JOptionPane.showMessageDialog(null,"��ʼ�ڵ�����ֹ�ڵ��غϣ�");
		    		else{
		    			
		    			//��ʼ�����·����
		    			
		    			int choiceindexstart=0;
		    			int choiceindexend=0;
		    			for(int i=0;i<CommonNode.getM_lsCommonNode().size();i++)
		    			{
		    			if(startCombo.getSelectedItem().toString()==CommonNode.getM_lsCommonNode().get(i).getM_sName()){
		    				
		    				
		    				choiceindexstart=i;
		    				
		    			}
		    			if(endCombo.getSelectedItem().toString()==CommonNode.getM_lsCommonNode().get(i).getM_sName()){
		    				
		    				
		    				choiceindexend=i;
		    				
		    			}
		    			
		    			}
//		    			
//		    			FiberLink fl=new FiberLink(Integer.parseInt(String.valueOf(linkId)),linkname.getText(),CommonNode.getM_lsCommonNode().get(choiceindexstart),
//		    					CommonNode.getM_lsCommonNode().get(choiceindexend),
//		    					Layer.Fiber,true);
//		    				FiberLink.fiberLinkList.add(fl);
//		    				UIFiberLink addnew = new UIFiberLink(FiberLink.getFiberLink(Integer.parseInt(String.valueOf(linkId))));
//			    			UIFiberLink.s_lUIFiberLinkList.add(addnew);
//			    			linkDummy.addChild(addnew);
//			    			box.addElement(addnew);
		    			
		    		    frame.dispose();
		    	    	}
		    			
		    	}
		    });	
			JButton cancel = new JButton("ȡ��");
			cancel.setFont(f);
	        cancel.addActionListener(new ActionListener(){
		    	public void actionPerformed(ActionEvent e){
		    		frame.dispose();
		    	}
		    });
	        Dimension buttonsize = new Dimension(60,30);
	        confirmButton.setPreferredSize(buttonsize);
	        cancel.setPreferredSize(buttonsize);
	        
	        Insets insert ;

	        GridBagLayout mg = new GridBagLayout();
			GridBagConstraints mgc = new GridBagConstraints();		
			pane.setLayout(mg);
			mgc.fill = GridBagConstraints.NONE;
			
			mgc.gridwidth = 1;
			mgc.weightx = 1.0;
			mgc.weighty = 1.0;
			mgc.gridheight = 1;
			mgc.anchor = GridBagConstraints.CENTER;
			JLabel jl1=new JLabel("��·����" );
			jl1.setFont(f);
			pane.add (jl1,mgc);
			
			mgc.gridwidth =2;
			insert = new Insets(0,23,0,0);
			mgc.insets = insert;
			mgc.anchor = GridBagConstraints.WEST;
			pane.add(linkname,mgc);
			
			mgc.gridwidth = 3;
			insert = new Insets(0,0,0,0);
			mgc.insets = insert;
			mgc.anchor = GridBagConstraints.CENTER;
			JLabel jl2=new JLabel("��·ID" );
			jl2.setFont(f);
			pane.add (jl2,mgc);
			mgc.gridwidth =GridBagConstraints.REMAINDER;
			pane.add(linkid,mgc);
			mgc.gridwidth = 1;
//			pane.add (new JLabel("������  "),mgc);		
//			mgc.gridwidth = 2;
//			mgc.weightx = 1.0;
//			pane.add(linksize,mgc);
//			
//			mgc.gridwidth = 3;
//			pane.add (new JLabel("����/km"),mgc);	
//			mgc.gridwidth = GridBagConstraints.REMAINDER;
//			pane.add(linklength,mgc);
//			
//			mgc.gridwidth = 1;
//			pane.add (new JLabel("��ͨ���" ),mgc);		
//			mgc.gridwidth =2;
//			pane.add(startyear,mgc);		
//			mgc.gridwidth = 3;
//			pane.add (new JLabel("�������"),mgc);	
//			mgc.gridwidth = GridBagConstraints.REMAINDER;
//			pane.add(endyear,mgc);
			
			mgc.gridwidth = 1;
			JLabel jl3=new JLabel("�׽ڵ�");
			jl3.setFont(f);
			pane.add (jl3,mgc);	
			mgc.gridwidth =2;
			pane.add(startCombo,mgc);		
			mgc.gridwidth = 3;
			JLabel jl4=new JLabel("ĩ�ڵ�");
			jl4.setFont(f);
			pane.add (jl4,mgc);		
			mgc.gridwidth = GridBagConstraints.REMAINDER;
			pane.add(endCombo,mgc);
			
//			mgc.gridwidth = 1;
//			pane.add (new JLabel("���ʱ��/��"),mgc);
//			mgc.gridwidth = 2;
//			pane.add(blocktime,mgc);
//			mgc.gridwidth =  3;
//			pane.add(new JLabel("ƽ��˥��/����"),mgc);
//			mgc.gridwidth=GridBagConstraints.REMAINDER;
//			pane.add(averlose,mgc);
//			
//
//			mgc.gridwidth = 1;
//			pane.add(new JLabel("����"),mgc);		
//			mgc.gridwidth = 2;
//			pane.add (areaCombo,mgc);
//			mgc.gridwidth=3;
//			pane.add(new JLabel("���跽ʽ"),mgc);
//			mgc.gridwidth = GridBagConstraints.REMAINDER;
//			pane.add ( setstyleCombo,mgc);
//			
			mgc.gridwidth = 1;
			JLabel jl5=new JLabel("������");
			jl5.setFont(f);
			pane.add(jl5,mgc);
			mgc.gridwidth = 2;
			pane.add (layer ,mgc);
//			mgc.gridwidth = 3;
//			pane.add(new JLabel("ά����λ"),mgc);	
//			mgc.gridwidth = GridBagConstraints.REMAINDER;
//			pane.add(protectCombo,mgc);


			mgc.gridwidth = 1;
			pane.add(new JLabel("  "),mgc);	
			mgc.gridwidth = 2;
			insert = new Insets(0,-70,0,0);
			mgc.insets = insert;
			pane.add (confirmButton ,mgc);
			insert = new Insets(0,-30,0,0);
			mgc.insets = insert;
			mgc.gridwidth = GridBagConstraints.REMAINDER;
			pane.add(cancel,mgc);
			
			pane.setVisible(true);
			frame.setContentPane(pane);
			frame.setVisible(true);
			
	    }
	 private boolean linkNameSame(String name,Layer layer){
							boolean isSame=false;
							switch(layer){
							case Fiber:
								for(int i=0;i<FiberLink.fiberLinkList.size();++i){
									if(FiberLink.fiberLinkList.get(i).getName().equals(name))
									{
										isSame= true;
										break;
									}
								}
								break;
							case WDM:
								for(int i=0;i<WDMLink.WDMLinkList.size();++i){
									if(WDMLink.WDMLinkList.get(i).getName().equals(name))
									{
										isSame=  true;
										break;
									}
								}
								break;
							default:
								break;
							}
							return isSame;
						}
					    
					    public boolean isRepeat(String id,Layer layer){
					    	boolean isRepeat=false;
							switch(layer){
							case Fiber:
								for(int i=0;i<FiberLink.fiberLinkList.size();++i){
									if(FiberLink.fiberLinkList.get(i).getId()==(Integer.parseInt(id)))
									{
										isRepeat= true;
										break;
									}
								}
								break;
							case WDM:
								for(int i=0;i<WDMLink.WDMLinkList.size();++i){
									if(WDMLink.WDMLinkList.get(i).getId()==(Integer.parseInt(id)))
									{
										isRepeat=  true;
										break;
									}
								}
								break;
							default:
								break;
							}
							return isRepeat;
						}
						/**
						  * �ж��ַ����Ƿ�������
						  */
						 public static boolean isInteger(String value) {
						  try {
						   Integer.parseInt(value);
						   return true;
						  } catch (NumberFormatException e) {
						   return false;
						  }
						 }
						 /**
						  * �ж��ַ����Ƿ��Ǹ�����
						  */
						 public static boolean isDouble(String value) {
						  try {
						   Double.parseDouble(value);
						   if (value.contains("."))
						    return true;
						   return false;
						  } catch (NumberFormatException e) {
						   return false;
						  }
						 }

						 /**
						  * �ж��ַ����Ƿ�������
						  */
						 public static boolean isNumber(String value) {
						  return isInteger(value) || isDouble(value);
						 }
}
