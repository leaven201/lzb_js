package algorithm;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


/*
 * ����Դ�����������Դ���ƻ��߷�����Դʧ�ܣ�����������Ӧ��txt����
 */
public class ResourceLogTxt {

	public static StringBuffer buffer;//�����ݴ���Դ���
	public static void ResourceLogTxt(){
        PrintWriter pw;
		try {
			buffer=new StringBuffer();
			buffer.append(CommonAlloc.fallBuffer);
			buffer.append(PortAlloc.portFallBuffer);
			pw = new PrintWriter( new FileWriter( "��Դ������־.txt" ));
	        pw.println(buffer);
//	        buffer=portAlloc.sPortFallBuffer;
//	        pw.println(buffer);
	        pw.close();
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}
	
}
