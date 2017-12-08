package dialog;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class NewFileChooser extends JFileChooser{
	public static int FILE_RIGHT = 0;
	public static int ILLEGAL_CHARACTER = 1;
	public static int WRONG_DIRECTORY = 2;
	public void approveSelection(){
		File file = getSelectedFile();
		//验证文件名是否合法
		if(validateFileName(file.getPath().toString()) == ILLEGAL_CHARACTER){
			JOptionPane.showMessageDialog(getParent(),"文件名不能包含下列字符：\n*?\"<>|");
			return;
			}
		else if(validateFileName(file.getPath().toString()) == WRONG_DIRECTORY){
			int ifNewFolder = JOptionPane.showConfirmDialog(null, "当前文件夹不存在，是否新建？");
			switch(ifNewFolder){
			case 0:
				file.mkdir();
				super.approveSelection();
			case 1:
				return;
			case 2:
				return;
			}
			}
		else
			super.approveSelection();
		}

/**
*验证输入字符串参数是否为有效文件名。
*@param name 待验证的文件名字符串。
*@return 通过验证，无效返回false，有效返回true
*/
	public static int validateFileName(String name){
		File file = new File(name);
		if(name.indexOf('/')!=-1||name.indexOf('*')!=-1||name.indexOf('?')!=-1||name.indexOf('"')!=-1||
				name.indexOf('<')!=-1||name.indexOf('>')!=-1||name.indexOf('|')!=-1){
			return ILLEGAL_CHARACTER;
			}
		else if(!file.exists()){
			return WRONG_DIRECTORY;
		}
		else{
			return FILE_RIGHT;
			}
		}
	
}
