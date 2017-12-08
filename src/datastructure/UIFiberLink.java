package datastructure;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import twaver.Link;
import twaver.TWaverConst;
import data.FiberLink;

public class UIFiberLink extends Link {
       
	public  FiberLink fiberLink;  
	public static List<UIFiberLink> s_lUIFiberLinkList=new LinkedList<UIFiberLink>(); 
	public UIFiberLink(FiberLink link){
		super(""+"Fiberlink"+link.getId());
		setName(link.getName());
		setDisplayName("");
		int fromid = link.getFromNode().getId();
        UINode from = UINode.getThisNode(fromid);
		int toid = link.getToNode().getId();
		UINode to = UINode.getThisNode(toid);
		setFrom(from);
		setTo( to);
		
		fiberLink = link;
	
		putLinkColor(Color.blue);
		putLinkWidth(3);
		putLinkStyle(TWaverConst.LINK_STYLE_SOLID);
	}

	public static UIFiberLink getUIFiberLink(String Name){
		for(int i = 0;i < UIFiberLink.s_lUIFiberLinkList.size();i++ ){
			if(UIFiberLink.s_lUIFiberLinkList.get(i).getName().equals(Name))
				return UIFiberLink.s_lUIFiberLinkList.get(i);
		}
		return null;
	}
	public static void setColor(Color color){
		ListIterator<UIFiberLink> it = s_lUIFiberLinkList.listIterator(); 
		while(it.hasNext())
		{
			it.next().putLinkColor(color);		
		}
	}
	public void setthisColor(Color color){
		this.putLinkColor(color);
	}
	public Color getthisColor(){
		return this.getLinkColor();
	}
	public static Color getColor(){

		 if(s_lUIFiberLinkList.isEmpty()){
			 return null;
		}
		 else if(!(s_lUIFiberLinkList.isEmpty())){
			 return s_lUIFiberLinkList.get(0).getLinkColor();
		 }
		 else  return null;
	}
	public static UIFiberLink getThisFiberlink(int id){
		ListIterator<UIFiberLink> it = s_lUIFiberLinkList.listIterator(); 
		while(it.hasNext())
		{
			if(it.next().getFiberLink().getId()==id)
				break;
		}
		return it.previous();
	}
	public static void clearAll(){
		s_lUIFiberLinkList.clear();
	}
	public FiberLink getFiberLink()
	{
		return fiberLink;
	}
	public void setFiberLink(FiberLink link)
	{
		 fiberLink=link;
	}

}
