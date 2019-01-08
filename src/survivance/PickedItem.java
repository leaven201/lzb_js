package survivance;

import java.util.LinkedList;
import java.util.List;

import data.CommonNode;
import data.Port;

public class PickedItem {
	
    private CommonNode FromNode;//��¼�ڵ���ϲ��ֵ��׽ڵ�
	private CommonNode ToNode;//��¼�ڵ���ϲ��ֵ�ĩ�ڵ�
	private int FromNum;//��¼�ڵ������·��λ��
	private int ToNum;//��¼�ڵ������·��λ��
    public List<Integer>WaveLengthIDList=new LinkedList<>();//�洢���й�����·�Ĳ���
    private boolean FromChanged=false;//ͷλ�õĽڵ��Ƿ��ǲ���ת���ڵ�
    private boolean ToChanged=false;//ĩλ�õĽڵ��Ƿ��ǲ���ת���ڵ�
    private int wavei;//��¼ǰһ���Ĳ���
    private int wavej;//��¼��һ���Ĳ���
	
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
