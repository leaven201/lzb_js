package survivance;

import java.util.LinkedList;
import java.util.List;

import data.CommonNode;
import data.Port;

public class PickedItem {
	
    private CommonNode FromNode;//记录节点故障部分的首节点
	private CommonNode ToNode;//记录节点故障部分的末节点
	private int FromNum;//记录节点故障链路的位置
	private int ToNum;//记录节点故障链路的位置
    public List<Integer>WaveLengthIDList=new LinkedList<>();//存储所有故障链路的波长
    private boolean FromChanged=false;//头位置的节点是否是波长转换节点
    private boolean ToChanged=false;//末位置的节点是否是波长转换节点
    private int wavei;//记录前一根的波长
    private int wavej;//记录后一根的波长
	
	public PickedItem() {		
	}

	public CommonNode getFromNode() {
		return FromNode;
	}

	public void setFromNode(CommonNode fromNode) {
		FromNode = fromNode;
	}

	public CommonNode getToNode() {
		return ToNode;
	}

	public void setToNode(CommonNode toNode) {
		ToNode = toNode;
	}

	public List<Integer> getWaveLengthIDList() {
		return WaveLengthIDList;
	}

	public void setWaveLengthIDList(List<Integer> waveLengthIDList) {
		WaveLengthIDList = waveLengthIDList;
	}

	public boolean isFromChanged() {
		return FromChanged;
	}

	public void setFromChanged(boolean fromChanged) {
		FromChanged = fromChanged;
	}

	public boolean isToChanged() {
		return ToChanged;
	}

	public void setToChanged(boolean toChanged) {
		ToChanged = toChanged;
	}

	public int getFromNum() {
		return FromNum;
	}

	public void setFromNum(int fromNum) {
		FromNum = fromNum;
	}

	public int getToNum() {
		return ToNum;
	}

	public void setToNum(int toNum) {
		ToNum = toNum;
	}

	public int getWavei() {
		return wavei;
	}

	public void setWavei(int wavei) {
		this.wavei = wavei;
	}

	public int getWavej() {
		return wavej;
	}

	public void setWavej(int wavej) {
		this.wavej = wavej;
	}
}
