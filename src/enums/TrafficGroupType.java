package enums;

public enum TrafficGroupType {
	��,
	ͬԴͬ��,
	ͬԴ��ͬ��,
	ͬ�޲�ͬԴ,
	��ͬ�޲�ͬԴ;
	
	
    public static TrafficGroupType StringToType(String ntype) {
    	TrafficGroupType type = null;
	switch (ntype) {
	case"��":
		type=TrafficGroupType.��;
		break;
	case "ͬԴͬ��":
	    type = TrafficGroupType.ͬԴͬ��;
	    break;
	case "ͬԴ��ͬ��":
		type = TrafficGroupType.ͬԴ��ͬ��;
	    break;
	case "ͬ�޲�ͬԴ":
		type = TrafficGroupType.ͬ�޲�ͬԴ;
	    break;
	case "��ͬ�޲�ͬԴ":
		type = TrafficGroupType.��ͬ�޲�ͬԴ;
	    break;
	default:
	    break;
	}
	return type;
    }
	


}
