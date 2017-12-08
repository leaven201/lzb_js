package dataControl;

import data.FiberLink;
import data.LinkRGroup;
import enums.Layer;

public class SRG {
	/**
	 * 此函数对前台进行过链路或节点删除情况进行响应
	 * @return
	 */
	private LinkRGroup templrg=null;
	public void del(){
		LinkRGroup.reflash();
	}
	/**
	 * 此函数对层间路由修改，新增链路进行响应，根据fiber层链路进行设置，关联的fiber层需要用户填写
	 *
	 */
	public LinkRGroup  changen(FiberLink fl,Layer layer){
		return LinkRGroup.find(fl, layer);
		
	}
	/**
	 * 此函数对人为添加组作出处理
	 */
	public void  newSRG(int nID,String layer,String name){
		LinkRGroup lrg= new	LinkRGroup(nID,layer,name);
		lrg.setNatrue(false);
		LinkRGroup.SRLGroupList.add(lrg);
		templrg=lrg;
	}
	/**
	 * 对删除人为添加组进行处理
	 */
	public void  delSRG(LinkRGroup lrg){
		if(!lrg.isNatrue())
		LinkRGroup.SRLGroupList.remove(lrg);
	}
	/**
	 * 对人为添加的组进行的删除,这个前台直接调用比较轻松，不写中间模块了……
	 */
	/**
	 * 删除全部自动生成的组
	 */
   /**
    * 自动生成以上三个都直接调用，恩……
    */
	/**
	 * 判断是否含有WDM组,true 有
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
	 * 判断是否含有SDH组，true有
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
//	 * 判断是否含有ASON组，true有
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

