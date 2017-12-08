package enums;

public enum Layer {
	WDM,
	Fiber, 
	OTN;
	
	
	
	
	
	
	
	public static Layer stringToLinkType(String s){
	    Layer linkType=null;
	    switch(s){
	    case "WDM":
		linkType=Layer.WDM;
		break;
	    case "Fiber":
		linkType=Layer.Fiber;
		break;
	    default:
		break;
	    }
	    return linkType;
	}
}
