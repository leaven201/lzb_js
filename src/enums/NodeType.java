package enums;

public enum NodeType {//���������NodeType.valueOf(String)����

    ROADM,OTN,OLA;
    
    //���ַ�������ת��Ϊö������
    public static NodeType stringToNodeType(String s){
	NodeType nt=null;
	switch (s) {
	case "ROADM":
	    nt = NodeType.ROADM;
	    break;
//	case "OTN":
//	    nt=NodeType.OTN;
//	    break;
	case "OLA":
	    nt=NodeType.OLA;
	    break;
	default:
	    break;
	}
	return nt;
    }
    
    //ֱ��ʹ��toString()�Ϳ�����ɴ˹���
//  //��ö������ת��Ϊ�ַ�������
//    public static String nodeTypeToString(NodeType nodeType){
//	String s="";
//	switch (nodeType) {
//	case ��:
//	    s="��";
//	    break;
//	case ��:
//	    s="��";
//	    break;
//	case ����:
//	    s="����";
//	    break;
//	default:
//	    break;
//	}
//	return s;
//    }
    
}
