package enums;

public enum PortRate {//与trafficrate内容一样
	G100, G10;
    
    public static int Rate2Num(PortRate rate) {
	int nrate = 0;
	switch (rate) {
	case G10:
	    nrate = 10;
	    break;
	case G100:
	    nrate = 100;
	    break;
	default:
	    break;
	}
	return nrate;
    }
    
    public static PortRate stringToRate(String s) {
	PortRate rate = null;
	switch (s) {
	case "10":
	    rate = PortRate.G10;
	    break;
	case "100":
	    rate = PortRate.G100;
	    break;
	default:
	    break;
	}
	return rate;
    }
    
    public static String RateToString(PortRate rate) {
	String nrate ="";
	switch (rate) {
//	case FE:
//	    nrate = "100M";
//	    break;
	case G10:
	    nrate = "10G";
	    break;
	case G100:
	    nrate = "100G";
	    break;
//	case G2Dot5:
//	    nrate = "2.5G";
//	    break;
//	case G40:
//	    nrate = "40G";
//	    break;
//	case GE:
//	    nrate = "1G";
//	    break;
//	case M155:
//	    nrate = "155M";
//	    break;
//	case M2:
//	    nrate = "2M"; 
//	    break;
//	case M622:
//	    nrate = "622M";
//	    break;
//	case M36:
//	    nrate = "36M";
//	    break;
	default:
	    break;
	}
	return nrate;
    }
	
}
