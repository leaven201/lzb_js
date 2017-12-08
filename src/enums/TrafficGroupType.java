package enums;

public enum TrafficGroupType {
	无,
	同源同宿,
	同源不同宿,
	同宿不同源,
	不同宿不同源;
	
	
    public static TrafficGroupType StringToType(String ntype) {
    	TrafficGroupType type = null;
	switch (ntype) {
	case"无":
		type=TrafficGroupType.无;
		break;
	case "同源同宿":
	    type = TrafficGroupType.同源同宿;
	    break;
	case "同源不同宿":
		type = TrafficGroupType.同源不同宿;
	    break;
	case "同宿不同源":
		type = TrafficGroupType.同宿不同源;
	    break;
	case "不同宿不同源":
		type = TrafficGroupType.不同宿不同源;
	    break;
	default:
	    break;
	}
	return type;
    }
	


}
