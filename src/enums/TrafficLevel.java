package enums;

public enum TrafficLevel {
	/*
	 * 业务等级
	 */
    
	 PERMANENT11 ,	//永久1+1
	 NORMAL11    ,	//普通1+1
	 RESTORATION ,	//恢复,无保护路由，只能恢复
	 PROTECTandRESTORATION,//1+1+重路由
	 DynamicRESTORATION, //动态恢复
	 PresetRESTORATION,//专享预置恢复
	 NONPROTECT;	//无保护
    
    
    public static TrafficLevel trans(String chType){
	
	TrafficLevel enType=null;
	
	switch(chType){
	
	case "永久1+1":enType = TrafficLevel.PERMANENT11;
		break;
	case "1+1":enType = TrafficLevel.NORMAL11;
		break;
//	case "保护+恢复":enType = TrafficLevel.PROTECTandRESTORATION;
//		break;
	case "重路由"	:enType = TrafficLevel.RESTORATION;
	    break;	
	case "1+1+重路由":enType = TrafficLevel.PROTECTandRESTORATION;
		break;
//	case "专享预置恢复":enType = TrafficLevel.PresetRESTORATION;
//		break;
	case "无保护":enType = TrafficLevel.NONPROTECT;
	    break;
	default:
		break;
	}
	return enType;
    }

}
