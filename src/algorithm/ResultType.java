package algorithm;

import java.util.List;

import data.WDMLink;

public class ResultType {
	boolean succeed; 
	int start;
	List<WDMLink> list;
	
	public ResultType(boolean succeed, int start) {
		super();
		this.succeed = succeed;
		this.start = start;
	}
	public boolean isSucceed() {
		return succeed;
	}
	public void setSucceed(boolean succeed) {
		this.succeed = succeed;
	}
	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public List<WDMLink> getList() {
		return list;
	}
	public void setList(List<WDMLink> list) {
		this.list = list;
	}
}
