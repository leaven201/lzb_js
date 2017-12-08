package dialog;

import java.io.File;
import java.util.ListIterator;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import design.NetDesign_zs;
import database.Database;

public class Dlg_ProjectSaveAs extends JFrame {

    public Dlg_ProjectSaveAs() {
	setSize(350, 200);
	MyChooser chooser = new MyChooser();
	chooser.setDialogTitle("另存为");
	chooser.setAcceptAllFileFilterUsed(false);
	FileFilter filter = new FileNameExtensionFilter("dat", "dat");
	chooser.setFileFilter(filter);
	chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	int option = chooser.showSaveDialog(Dlg_ProjectSaveAs.this);
	if (option == JFileChooser.APPROVE_OPTION) {
	    File file = chooser.getSelectedFile();
	    String name = file.getName();
	    String flist = file.getPath() + "\\" + name;
	    File fl = new File(flist);
	    if (fl.exists())
		JOptionPane.showMessageDialog(this, "工程" + name + "已经存在，请重新命名");
	    
	    else {
		try {
		    fl.mkdir();// 在file的路径新建文件夹
		    String filelist = "wrong";
		    filelist = file.getPath() + "\\" + name + "\\" + name + ".dat";
		    Database.setFileProperty(NetDesign_zs.fileinfo);
		    Database.serialize(filelist);

		    NetDesign_zs.filenameall = filelist;
		    NetDesign_zs.filepath = file.getParent();
		    NetDesign_zs.filename = file.getName() + ".dat";
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	}
    }
}
