package test;

import java.awt.Frame;
import java.util.List;

import data.FiberLink;
import data.Traffic;
import database.TrafficDatabase;
import dialog.AFSetting;
import dialog.TestProgressBar;
import survivance.Evaluation;

public class test2 {
	public static void main(String[] args) {
		
		Thread thread = new Thread() {
            public void run() {
                int i = 0;	
                while( i < 200000) {
                	i++;
                	System.out.println(i);
                }
                while (i < 2000) {		                   
						
                	System.out.println("hello");
						
                }
            }
        };
        
        TestProgressBar.show((Frame) null, thread, "数据导出中...", "数据已导出!", "Cancel");
		
	}
}
