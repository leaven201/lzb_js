package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import data.DataSave;

public class Solution {
	public double count(double I, double Q, double J, double K, double O) {
		return Math.pow(10, (I+Q+J+K-O)/10);
	}
	public double OSNRcountHuaWei(double I,String type) {
		
		double K = 0.25;
		double J = 0.2;
		double zengyi = I + J + K;
		//根据增益设置放大器噪声系数
		double Q = 5;
		if(zengyi <= 22) {
			Q = 5;
		}else {
			Q = 4.5;
		}
		
		double	O = 0;
		//根据光纤类型，以及光纤衰减设置单波平均入纤光功率
		if(I <= 25) {
			if(type.equals("G.652")) {
				O = 1;
			}
			if(type.equals("G.655")) {
				O = 0;
			}
		}else if(I <= 32) {
			if(type.equals("G.652")) {
				O = 2;
			}
			if(type.equals("G.655")) {
				O = 1;
			}
		}else if(I <= 37) {
			if(type.equals("G.652")) {
				O = 4;
			}
			if(type.equals("G.655")) {
				O = 2;
			}
		}else {
			if(type.equals("G.652")) {
				O = 7;
			}
			if(type.equals("G.655")) {
				O = 4;
			}
		}		
		double OSNRCount = Math.pow(10, ((I + J + K + Q - O) / 10));
		return OSNRCount;
	}
	public double count(double[] OSNRCount) {
		double sum = 0;
		for (double d : OSNRCount) {
			sum += d;
		}
		double OSNR = 58 - 10 * Math.log10(sum);
		return OSNR;
	}
	
	
	public static void main(String[] args) {
		Solution s = new Solution();
		Fiber f1 = new Fiber(11.495);
		Fiber f2 = new Fiber(20.296,"G.652");
		Fiber f3 = new Fiber(10.34952,"G.652");
		Fiber f4 = new Fiber(22.462,"G.652");		
		Fiber f5 = new Fiber(20.482);
		Fiber f6 = new Fiber(18.177);
		Fiber f7 = new Fiber(26.955812);
		Fiber f8 = new Fiber(27.419052);
		//ES
		Fiber f9 = new Fiber(21.979);
		Fiber f10 = new Fiber(20.737);
		//QS
		Fiber f11 = new Fiber(19.816);
		Fiber f12 = new Fiber(17.505);
		Fiber f13 = new Fiber(23.53);
		Fiber f14 = new Fiber(20.2,"G.652");
		Fiber f15= new Fiber(22.5905);
		Fiber f16 = new Fiber(21.452);
		Fiber f17 = new Fiber(24.885968);
		//KQ
		Fiber f18 = new Fiber(16.525);
		Fiber f19 = new Fiber(27.684244);
		Fiber f20 = new Fiber(23.185536);
		Fiber f21 = new Fiber(17.337);
		Fiber f22 = new Fiber(29.709168);
		
		List<Fiber> list = new ArrayList<>();
		list.add(f1);list.add(f2);list.add(f3);
		list.add(f4);list.add(f5);list.add(f6);
		list.add(f7);list.add(f8);list.add(f9);
		list.add(f10);list.add(f11);list.add(f12);
		list.add(f13);list.add(f14);list.add(f15);
		list.add(f16);list.add(f17);list.add(f18);
		list.add(f19);list.add(f20);list.add(f21);
		list.add(f22);
		double[] countOSNR = new double[list.size()];
		for(int i=0; i<list.size();i++) {
			Fiber f = list.get(i);
			countOSNR[i] = s.OSNRcountHuaWei(f.I, f.type);
		}
		double osnr = s.count(countOSNR);
		System.out.println(osnr);
//		Fiber fF1 = new Fiber(14.543,"G.652");
//		Fiber fF2= new Fiber(22.349,"G.652");
//		Fiber fF3 = new Fiber(18.36,"G.652");
//		Fiber fF4 = new Fiber(22.179,"G.652");
//		List<Fiber> list2 = new ArrayList<>();
//		list2.add(fF1);list2.add(fF2);list2.add(fF3);list2.add(fF4);
//		double[] countOSNR2 = new double[list2.size()];
//		for(int i=0; i<list2.size();i++) {
//			Fiber f = list2.get(i);
//			countOSNR2[i] = s.OSNRcountHuaWei(f.I, f.type);
//		}
System.out.println("2222222222222222222222222222222222222");
		List<Fiber> list2 = new ArrayList<>();
		list2.add(f9);list2.add(f10);
		list2.add(f11);list2.add(f12);list2.add(f13);list2.add(f14);
		list2.add(f15);list2.add(f16);list2.add(f13);list2.add(f17);
		list2.add(f18);list2.add(f19);list2.add(f20);list2.add(f21);
		list2.add(f22);
		
		double[] countOSNR2 = new double[list2.size()];
		for(int i=0; i<list2.size();i++) {
			Fiber f = list2.get(i);
			countOSNR2[i] = s.OSNRcountHuaWei(f.I, f.type);
		}
		double osnr2 = s.count(countOSNR2);
		System.out.println(osnr2);
	}
}
class Fiber{
	double I;
	String type = "G.655";
	public Fiber(double i) {
		this.I = i;
	}
	public Fiber(double i, String type) {
		this.I = i;
		this.type = type;
	}
}