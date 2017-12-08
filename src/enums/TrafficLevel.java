package enums;

public enum TrafficLevel {
	/*
	 * ҵ��ȼ�
	 */
    
	 PERMANENT11 ,	//����1+1
	 NORMAL11    ,	//��ͨ1+1
	 RESTORATION ,	//�ָ�,�ޱ���·�ɣ�ֻ�ָܻ�
	 PROTECTandRESTORATION,//1+1+��·��
	 DynamicRESTORATION, //��̬�ָ�
	 PresetRESTORATION,//ר��Ԥ�ûָ�
	 NONPROTECT;	//�ޱ���
    
    
    public static TrafficLevel trans(String chType){
	
	TrafficLevel enType=null;
	
	switch(chType){
	
	case "����1+1":enType = TrafficLevel.PERMANENT11;
		break;
	case "1+1":enType = TrafficLevel.NORMAL11;
		break;
//	case "����+�ָ�":enType = TrafficLevel.PROTECTandRESTORATION;
//		break;
	case "��·��"	:enType = TrafficLevel.RESTORATION;
	    break;	
	case "1+1+��·��":enType = TrafficLevel.PROTECTandRESTORATION;
		break;
//	case "ר��Ԥ�ûָ�":enType = TrafficLevel.PresetRESTORATION;
//		break;
	case "�ޱ���":enType = TrafficLevel.NONPROTECT;
	    break;
	default:
		break;
	}
	return enType;
    }

}
