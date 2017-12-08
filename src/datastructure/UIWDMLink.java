package datastructure;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import twaver.Link;
import twaver.TWaverConst;
import data.*;

public class UIWDMLink extends Link {//ÓÃOptLink´úÌæSatellite
       
	private WDMLink WDMLink; 
	public static List<UIWDMLink> s_lUIWDMLinkList=new LinkedList<UIWDMLink>();
	public UIWDMLink( WDMLink link){
	//	super(""+"WDMLink"+link.getLinkLayer().toString()+link.getId());
		setName(link.getName());
		setDisplayName("");
		int fromid =link.getFromNode().getId();
        UINode from = UINode.getThisNode(fromid);
		int toid = link.getToNode().getId();
		UINode to = UINode.getThisNode(toid);
		setFrom(from);
		setTo( to);
		WDMLink = link;
		
		putLinkColor(Color.green);
		
		putLinkWidth(3);
		putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
	}
	public static UIWDMLink getUIOptLink(String Name){
		for(int i = 0;i < UIWDMLink.s_lUIWDMLinkList.size();i++ ){
			if(UIWDMLink.s_lUIWDMLinkList.get(i).getName().equals(Name))
				return UIWDMLink.s_lUIWDMLinkList.get(i);
		}
		return null;
	}
	public static void setColor(Color color){
		ListIterator<UIWDMLink> it = s_lUIWDMLinkList.listIterator(); 
		while(it.hasNext())
		{
			it.next().putLinkColor(color);		
		}
	}
	public static UIWDMLink getThisOptLink(int id){
		ListIterator<UIWDMLink> it = s_lUIWDMLinkList.listIterator(); 
		while(it.hasNext())
		{
			if(it.next().getOptLink().getId()==id)
				break;
		}
		return it.previous();
	}
	public static Color getColor(){

		 if(s_lUIWDMLinkList.isEmpty()){
			 return null;
		}
		 else if(!(s_lUIWDMLinkList.isEmpty())){
			 return s_lUIWDMLinkList.get(0).getLinkColor();
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
		s_lUIWDMLinkList.clear();
	}
	public WDMLink getOptLink()
	{
		return WDMLink;
	}
	public void setOptLink(WDMLink link)
	{
	    WDMLink=link;
	}
	
}