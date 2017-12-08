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
		//��֤�ļ����Ƿ�Ϸ�
		if(validateFileName(file.getPath().toString()) == ILLEGAL_CHARACTER){
			JOptionPane.showMessageDialog(getParent(),"�ļ������ܰ��������ַ���\n*?\"<>|");
			return;
			}
		else if(validateFileName(file.getPath().toString()) == WRONG_DIRECTORY){
			int ifNewFolder = JOptionPane.showConfirmDialog(null, "��ǰ�ļ��в����ڣ��Ƿ��½���");
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
*��֤�����ַ��������Ƿ�Ϊ��Ч�ļ�����
*@param name ����֤���ļ����ַ�����
*@return ͨ����֤����Ч����false����Ч����true
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
