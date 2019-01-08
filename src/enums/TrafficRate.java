package enums;

/*
 * 
 * 
SDH基本帧结构大小为155M，你可以把它看做一节火车车皮，
里面可以装几种速率级别的货物，比如VC12（2M）、VC3(45M)、VC4(155M),
即可装63个2M或者3个45M，也可以装一个VC4（155M），
VC12、VC3、VC4可以理解为封装货物的纸箱或者容器。传输速率为1秒8000帧，
也就是1秒有8000节火车皮经过，换算成每节车皮经过的时间就是1秒除以8000帧等于125us
 */
public enum TrafficRate {//
    G100, G40, G10, G2Dot5, GE, // 1000m
    M622, M155, FE, // 100m
    M2;
    public static float Rate2Num(TrafficRate rate) {
	float nrate = 0;
	switch (rate) {
	case FE:
	    nrate = (float) 0.1;
	    break;
	case G10:
	    nrate = 10;
	    break;
	case G100:
	    nrate = 100;
	    break;
	case G2Dot5:
	    nrate = (float) 2.5;
	    break;
	case G40:
	    nrate = 40;
	    break;
	case GE:
	    nrate = 1;
	    break;
	case M155:
	    nrate = (float) 0.155;
	    break;
	case M2:
	    nrate = (float) 0.002;
	    break;
	case M622:
	    nrate = (float) 0.622;
	    break;
	default:
	    break;
	}
	return nrate;
    }
   
    public static TrafficRate NumToRate(String nrate) {
	TrafficRate rate = null;
	switch (nrate) {
	case "100GE":
	    rate = TrafficRate.G100;
	    break;
	case "40GE":
	    rate = TrafficRate.G40;
	    break;
	case "10GE":
	    rate = TrafficRate.G10;
	    break;
	case "2.5GE":
	    rate = TrafficRate.G2Dot5;
	    break;
	case "1GE":
	    rate = TrafficRate.GE;
	    break;    
	default:
	    break;
	}
	return rate;
    }
    // public static PortRate T2PRate(int trarate) {// 把业务速率转化为端口速率
    // // TODO Auto-generated method stub
    // PortRate rate = null;
    // switch (trarate) {
    //// case 256:
    //// rate = PortRate.G40;
    //// break;
    // case 64:
    // rate = PortRate.G10;
    // break;
    //// case 16: // 2.5G
    //// rate = PortRate.G2Dot5;
    //// break;
    //// case 6:
    //// rate = PortRate.GE;
    //// break;
    //// case 4:
    //// rate = PortRate.M622;
    //// break;
    //// case 1:
    //// rate = PortRate.M155;
    //// break;
    //// case 2:
    //// rate = PortRate.M2;
    //// break;
    // }
    // return rate;
    // }

    public static PortRate T2PRate(TrafficRate trarate) {// 把业务速率转化为端口速率
	// TODO Auto-generated method stub
	PortRate rate = null;
	switch (trarate) {
	case G100:
	    rate = PortRate.G100;
	    break;
	 case G40:
	 rate = PortRate.G40;
	 break;
	case G10:
	    rate = PortRate.G10;
	    break;
	 case G2Dot5: // 2.5G
	 rate = PortRate.G2Dot5;
	 break;
	 case GE:
	 rate = PortRate.GE;
	 break;
	// case M622:
	// rate = PortRate.M622;
	// break;
	// case M155:
	// rate = PortRate.M155;
	// break;
	// case M2:
	// rate = PortRate.M2;
	// break;
	// case FE:
	// break;
	default:
	    break;
	}
	return rate;
    }
    // if(ratestring.equals("40G")){rate=40;size=256;}
    // else if(ratestring.equals("10G")){rate=10;size=64;}
    // else if(ratestring.equals("2.5G")){rate=2.5;size=16;}
    // else if(ratestring.equals("622M")){rate=622;size=4;}
    // else if(ratestring.equals("155M")){rate=155;size=1;}
}
