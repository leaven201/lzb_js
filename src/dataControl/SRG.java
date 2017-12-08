package dataControl;

import data.FiberLink;
import data.LinkRGroup;
import enums.Layer;

public class SRG {
	/**
	 * �˺�����ǰ̨���й���·��ڵ�ɾ�����������Ӧ
	 * @return
	 */
	private LinkRGroup templrg=null;
	public void del(){
		LinkRGroup.reflash();
	}
	/**
	 * �˺����Բ��·���޸ģ�������·������Ӧ������fiber����·�������ã�������fiber����Ҫ�û���д
	 *
	 */
	public LinkRGroup  changen(FiberLink fl,Layer layer){
		return LinkRGroup.find(fl, layer);
		
	}
	/**
	 * �˺�������Ϊ�������������
	 */
	public void  newSRG(int nID,String layer,String name){
		LinkRGroup lrg= new	LinkRGroup(nID,layer,name);
		lrg.setNatrue(false);
		LinkRGroup.SRLGroupList.add(lrg);
		templrg=lrg;
	}
	/**
	 * ��ɾ����Ϊ�������д���
	 */
	public void  delSRG(LinkRGroup lrg){
		if(!lrg.isNatrue())
		LinkRGroup.SRLGroupList.remove(lrg);
	}
	/**
	 * ����Ϊ��ӵ�����е�ɾ��,���ǰֱ̨�ӵ��ñȽ����ɣ���д�м�ģ���ˡ���
	 */
	/**
	 * ɾ��ȫ���Զ����ɵ���
	 */
   /**
    * �Զ���������������ֱ�ӵ��ã�������
    */
	/**
	 * �ж��Ƿ���WDM��,true ��
	 */
	public boolean isWDM()
	{
		for(int i=0;i<LinkRGroup.SRLGroupList.size();i++){
			if(LinkRGroup.SRLGroupList.get(i).isNatrue())
				if(LinkRGroup.SRLGroupList.get(i).getBelongLayer().equals(Layer.WDM))
					return true;
		}
		return false;
	}
	/**
	 * �ж��Ƿ���SDH�飬true��
	 */
//	public boolean isSDH()
//	{
//		for(int i=0;i<LinkRGroup.SRLGroupList.size();i++){
//			if(LinkRGroup.SRLGroupList.get(i).isNatrue())
//				if(LinkRGroup.SRLGroupList.get(i).getBelongLayer().equals(Layer.SDH))
//					return true;
//		}
//		return false;
//	}
//	/**
//	 * �ж��Ƿ���ASON�飬true��
//	 */
//	public boolean isASON()
//	{
//		for(int i=0;i<LinkRGroup.SRLGroupList.size();i++){
//			if(LinkRGroup.SRLGroupList.get(i).isNatrue())
//				if(LinkRGroup.SRLGroupList.get(i).getBelongLayer().equals(Layer.ASON))
//					return true;
//		}
//		return false;
//	}
	public LinkRGroup getTemplrg() {
		return templrg;
	}
	public void setTemplrg(LinkRGroup templrg) {
		this.templrg = templrg;
	}
}

