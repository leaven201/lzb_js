package dialog;
import java.util.LinkedList;
import java.util.List;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import algorithm.ResourceAlloc;
import algorithm.RouteAlloc;
import algorithm.algorithm;
import data.CommonNode;
import data.DataSave;
import data.FiberLink;
import data.Network;
import data.Traffic;
import data.TrafficGroup;
import data.WDMLink;
import database.TrafficDatabase;
import data.Traffic;
import dialog.Dlg_PolicySetting;

public class Dlg_BussinessPlan {
	public Dlg_BussinessPlan() {
		List<Traffic> traList = new LinkedList<Traffic>();
		traList.addAll(Traffic.trafficList);
		if (!RouteAlloc.isRouteEmpty(traList)) {// 若路由不为空
			int test = JOptionPane.showConfirmDialog(null, "规划已进行，是否清空路由？");
			switch (test) {
			case 0:// 清空路由
				RouteAlloc.clearAllTrafficRoute(traList);

				DataSave.separate = Dlg_PolicySetting.a;
				DataSave.separate1 = Dlg_PolicySetting.b;
				DataSave.OSNR = Dlg_PolicySetting.c;
				DataSave.locknum = Dlg_PolicySetting.d;
				if (Dlg_PolicySetting.f) {
					
					Thread thread = new Thread() {
			            public void run() {
			            	ResourceAlloc.index = 1;
			            	ResourceAlloc.allocateResource1(traList, Dlg_PolicySetting.h);
			                while ( ResourceAlloc.index < traList.size() ) {		                   
			                }
			            }
			        };
			        TestProgressBar.show((Frame) null, thread, "路由规划中，请稍后！", null, "Cancel");

					
				} else {
					
					Thread thread = new Thread() {
			            public void run() {
			            	ResourceAlloc.index = 1;
			            	ResourceAlloc.allocateResource(traList, Dlg_PolicySetting.h);
			                while ( ResourceAlloc.index < traList.size() ) {		                   
			                }
			            }
			        };
			        TestProgressBar.show((Frame) null, thread, "路由规划中，请稍后！", null, "Cancel");
					
				}
				new Dlg_DesignResult();
				break;
			case 1:
				DataSave.separate = Dlg_PolicySetting.a;
				DataSave.separate1 = Dlg_PolicySetting.b;
				DataSave.OSNR = Dlg_PolicySetting.c;
				DataSave.locknum = Dlg_PolicySetting.d;
				if (Dlg_PolicySetting.f) {
					
					Thread thread = new Thread() {
			            public void run() {
			            	ResourceAlloc.index = 1;
			            	ResourceAlloc.allocateResource1(traList, Dlg_PolicySetting.h);
			                while ( ResourceAlloc.index < traList.size() ) {		                   
			                }
			            }
			        };
			        TestProgressBar.show((Frame) null, thread, "路由规划中，请稍后！", null, "Cancel");
					
				} else {
					Thread thread = new Thread() {
			            public void run() {
			            	ResourceAlloc.index = 1;
			            	ResourceAlloc.allocateResource(traList, Dlg_PolicySetting.h);
			                while ( ResourceAlloc.index < traList.size() ) {		                   
			                }
			            }
			        };
			        TestProgressBar.show((Frame) null, thread, "路由规划中，请稍后！", null, "Cancel");
					
				}
				new Dlg_DesignResult();
				break;
			case 2:
				break;
			}

		} else {
			DataSave.separate = Dlg_PolicySetting.a;
			DataSave.separate1 = Dlg_PolicySetting.b;
			DataSave.OSNR = Dlg_PolicySetting.c;
			DataSave.locknum = Dlg_PolicySetting.d;
			if (Dlg_PolicySetting.f) {
				Thread thread = new Thread() {
		            public void run() {
		            	ResourceAlloc.index = 1;
		            	ResourceAlloc.allocateResource1(traList, Dlg_PolicySetting.h);
		                while ( ResourceAlloc.index < traList.size() ) {	
		                	System.out.println("hello");
		                }
		            }
		        };
		        TestProgressBar.show((Frame) null, thread, "路由规划中，请稍后！", null, "Cancel");
			} else {
				
				Thread thread = new Thread() {
		            public void run() {
		            	ResourceAlloc.index = 1;
		            	ResourceAlloc.allocateResource(traList, Dlg_PolicySetting.h);
		                while ( ResourceAlloc.index < traList.size() ) {	
		                	System.out.println("hello");
		                }
		            }
		        };
		        TestProgressBar.show((Frame) null, thread, "路由规划中，请稍后！", null, "Cancel");
			}
			new Dlg_DesignResult();

		}
	}
}
