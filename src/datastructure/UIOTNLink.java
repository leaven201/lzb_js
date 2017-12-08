package datastructure;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import twaver.Link;
import twaver.TWaverConst;
import data.*;
import data.BasicLink;

public class UIOTNLink extends Link {//CC£ºSDHÌæ»»Îª¶Ì²¨
       
	private OTNLink eleLink; 
	
	public static List<UIOTNLink> s_lUIEleLinkList=new LinkedList<UIOTNLink>();
	public UIOTNLink( OTNLink link){
		super(""+"EleLink"+link.getLinkLayer().toString()+link.getId());
		setName(link.getName());
		setDisplayName("");
		putLinkColor(Color.orange);
		int fromid =link.getFromNode().getId();
        UINode from = UINode.getThisNode(fromid);
		int toid = link.getToNode().getId();
		UINode to = UINode.getThisNode(toid);
		setFrom(from);
		setTo( to);
		eleLink = link;
		
		putLinkWidth(3);
		putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
	}
	public static UIOTNLink getUIEleLink(String Name){
		for(int i = 0;i < UIOTNLink.s_lUIEleLinkList.size();i++ ){
			if(UIOTNLink.s_lUIEleLinkList.get(i).getName().equals(Name))
				return UIOTNLink.s_lUIEleLinkList.get(i);
		}
	
		return null;
	}
	public static void setColor(Color color){
		ListIterator<UIOTNLink> it = s_lUIEleLinkList.listIterator(); 
		while(it.hasNext())
		{
			it.next().putLinkColor(color);		
		}
	
	}
	public static UIOTNLink getThisEleLink(int id){
		ListIterator<UIOTNLink> it = s_lUIEleLinkList.listIterator(); 
		while(it.hasNext())
		{
			if(it.next().getEleLink().getId()==id)
				break;
		}
		return it.previous();
	}
	public static Color getColor(){

		 if(s_lUIEleLinkList.isEmpty()){
			 return null;
		}
		 else if(!(s_lUIEleLinkList.isEmpty())){
			 return s_lUIEleLinkList.get(0).getLinkColor();
		 }
		 else  return null;
	}
	public void setthisColor(Color color){
		this.putLinkColor(color);
	}
	public Color getthisColor(){
		return this.getLinkColor();
	}
	public static void clearAll(){
		s_lUIEleLinkList.clear();
	}
	public OTNLink getEleLink()
	{
		return eleLink;
	}
	
	public void setEleLink(OTNLink link)
	{
	    eleLink=link;
	}

}
