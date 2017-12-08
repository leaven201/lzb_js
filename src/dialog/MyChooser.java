package dialog;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class MyChooser extends JFileChooser {
	public static int FILE_RIGHT = 0;
	public static int ILLEGAL_CHARACTER = 1;
	public static int WRONG_DIRECTORY = 2;
	public static int EXTENSION_NAME =3;
	public void approveSelection(){
		File file = getSelectedFile();
		//��֤�ļ����Ƿ�Ϸ�
		if(validateFileName(file.getName()) == ILLEGAL_CHARACTER){
			JOptionPane.showMessageDialog(getParent(),"�ļ������ܰ��������ַ���\n\\:*?\"<>|");
			return;
			}
		else if(validateFileName(file.getName()) == EXTENSION_NAME){
			JOptionPane.showMessageDialog(getParent(),"�����ļ��������������չ��");
		}
		else if(validateFileName(file) == WRONG_DIRECTORY){
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
		if(name.indexOf('\\')!=-1||name.indexOf(':')!=-1||name.indexOf('/')!=-1||
				name.indexOf('*')!=-1||name.indexOf('?')!=-1||name.indexOf('"')!=-1||
				name.indexOf('<')!=-1||name.indexOf('>')!=-1||name.indexOf('|')!=-1){
			return ILLEGAL_CHARACTER;
			}
		else if(name.indexOf(".dat")!=-1||name.indexOf(".DAT")!=-1){
			return EXTENSION_NAME;
		}
		else{
			return FILE_RIGHT;
			}
		}
	public static int validateFileName(File file){
		int namelen = file.getName().length();
		String path = file.getPath().toString().substring(0, file.getPath().toString().length()-namelen-1);
		File newfile = new File(path);
		if(!newfile.exists())
			return WRONG_DIRECTORY;
		else
			return FILE_RIGHT;
	}
}
