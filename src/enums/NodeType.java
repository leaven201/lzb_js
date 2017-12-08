package enums;

public enum NodeType {//好像可以用NodeType.valueOf(String)代替

    ROADM,OTN,OLA;
    
    //把字符串类型转换为枚举类型
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
    
    //直接使用toString()就可以完成此功能
//  //把枚举类型转换为字符串类型
//    public static String nodeTypeToString(NodeType nodeType){
//	String s="";
//	switch (nodeType) {
//	case 光:
//	    s="光";
//	    break;
//	case 电:
//	    s="电";
//	    break;
//	case 光纤:
//	    s="光纤";
//	    break;
//	default:
//	    break;
//	}
//	return s;
//    }
    
}
