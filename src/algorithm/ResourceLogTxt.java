package algorithm;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


/*
 * 将资源分配情况（资源限制或者分配资源失败），导出到相应的txt里面
 */
public class ResourceLogTxt {

	public static StringBuffer buffer;//用于暂存资源情况
	public static void ResourceLogTxt(){
        PrintWriter pw;
		try {
			buffer=new StringBuffer();
			buffer.append(CommonAlloc.fallBuffer);
			buffer.append(PortAlloc.portFallBuffer);
			pw = new PrintWriter( new FileWriter( "资源分配日志.txt" ));
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
