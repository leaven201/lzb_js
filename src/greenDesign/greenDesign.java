/*package greenDesign;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.text.StyledEditorKit.ForegroundAction;

import org.omg.CORBA.PUBLIC_MEMBER;
//
//import rcAllocate.NewAlloc;
//import rcAllocate.ResourceLogTxt;
//import rcAllocate.commonAlloc;
//import rcAllocate.portAlloc;
import twaver.Link;

//import algorithm.DijkstraAlgorithm;
import algorithm.algorithm;

import data.BasicLink;
import data.CommonNode;
//import data.Fiber;
import data.FiberLink;
import data.LinkRGroup;
import data.Network;
import data.Route;
//import data.SDHLink;
//import data.Timeslot;
import data.Traffic;
import data.WDMLink;
//import data.WDMSystem;
//import data.Wavelength;
import dataControl.LinkData;
import dataControl.TrafficData;
import dataControl.clearNetSource;
import design.NetDesign;
//import enums.Define;
import enums.Layer;
import enums.Status;
import enums.TrafficLevel;
import enums.TrafficRate;
import enums.TrafficStatus;

public class greenDesign {
	public static String msg=""; 
	public static int s=0;
	public static List linkList;//瀛樻斁鎵�鏈夐摼璺紝鍖呮嫭缃戝叧閾捐矾鍜岀綉鍐呴摼璺�
	private static List sdhlinkList;//鏋勫缓鍏ㄨ繛鎺ョ綉
	private static List linkcantdel=new LinkedList();
	private static List<Traffic> trfList;
	private static ArrayList<Double> utilList=new ArrayList<Double>();
	private static List<CommonNode> nodeList;
	private static Layer trfLayer;
	private static double e1=0;
	private static double e0=0;
	private static Object linktodel=null;
	private final static int infi=499;
	private static int mID=10000;
	public static boolean success= true;
	public static int s_setlimit;//wx
	public static int s_uselimit;//wx
	private static double linktodel_utl;//wx璁板綍褰撳墠寰呭垹闄ら摼璺殑鍒╃敤鐜�
//	public static PrintWriter pw;
	public static LinkedList<CommonNode> GatewayNodeList;//wx 瀛樻斁缁垮湴涓殑缃戝叧鑺傜偣
	public  static  LinkedList<CommonNode> 	InNetNodeList;//wx瀛樻斁 缃戝唴鑺傜偣
	//private static Network net=new Network("green",1,1);//鏍稿績缃�
	private static ArrayList<Integer> linkNumList=new ArrayList<Integer>();
	private static ArrayList<Integer> linkUsed=new ArrayList<Integer>();	
	public static boolean start(List<Traffic> trafficList,List<CommonNode> NodeList,Layer l,double target, double min,int setinglimit,int usinglimit,int flag){//wx 鍔犱簡涓や釜鍙傛暟
//		try {
////			pw = new PrintWriter( new FileWriter( "C:\\鎵撳嵃鏃ュ織.txt" ) );
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}f		
		s_setlimit=setinglimit;//wx
		s_uselimit=usinglimit;//wx
		
		trfList=trafficList;
		nodeList=NodeList;
		//Network.m_lNetworkList.add(net);
		trfLayer=l;
		e1=min;
		System.out.println(" 寰呰鍒掍笟鍔￠�熺巼 锛�");
		for(int i=0; i!=trfList.size(); i++){
			System.out.println(trfList.get(i).getName()+"--"+trfList.get(i).getRate());
		}
		switch(trfLayer){
		case WDM:
			s=1;
			linkList=new LinkedList<WDMLink>();
			linktodel=new WDMLink();
			Traffic.setS_lWDMTrafficList(trfList);
			//Traffic.getS_lASONTrafficList().clear();//wx 3.4
			//Traffic.getS_lSDHTrafficList().clear();
			for (int y=0;y!=trfList.size();y++){
				trfList.get(y).setM_eLayer(Layer.WDM);
			}
			break;
//		case ASON://wx 3.4
//			s=2;
//			linkList=new LinkedList<SDHLink>();
//			linktodel=new SDHLink();			
//			//Traffic.s_lSDHTrafficList=trfList;
//			Traffic.s_lASONTrafficList=trfList;//wx 3.4
//			Traffic.getS_lWDMTrafficList().clear();
//			for (int y=0;y!=trfList.size();y++){
//				trfList.get(y).setM_eLayer(Layer.ASON);
//			}
//			break;
		default:
			return false;
		}
		//createAllConNetwork();//寤虹珛鍏ㄨ繛鎺ユ嫇鎵�
		createAllConNetwork2();//寤虹珛鍏ㄨ繛鎺ユ嫇鎵�
		
		//wx 3.5灏嗚祫婧愬垎閰嶅崰鐢ㄨ祫婧愰噴鏀�
		switch (trfLayer) {
//		case ASON:
//			for (int  num= 0;  num< trafficList.size(); num++) {
//				Traffic trf=trafficList.get(num);
//				for (int i = 0; i < greenDesign.linkList.size(); i++) {
//					SDHLink linkin=(SDHLink)greenDesign.linkList.get(i);			
//						linkin.setM_nFree(setinglimit);
//						for (int j = 0; j != trf.getM_nRate(); j++) {
//							if(linkin.getTimeslot(j ).getM_eStatus().equals(Status.宸ヤ綔)||linkin.getTimeslot(j ).getM_eStatus().equals(Status.鎵胯浇)||linkin.getTimeslot(j ).getM_eStatus().equals(Status.淇濇姢)||linkin.getTimeslot(j ).getM_eStatus().equals(Status.淇濇姢鎭㈠)||linkin.getTimeslot(j ).getM_eStatus().equals(Status.淇濇姢鎭㈠))
//							linkin.getTimeslot(j ).setM_eStatus(Status.绌洪棽);
//							linkin.getTimeslot(j).setnFree(63);
//							if (linkin.getM_cCarriedTraffic().contains(trf)) {
//								linkin.delTraffic(trf); 
//							}
//							
//						}
//					
//				}
//			}
//			break;
			case WDM:
				for (int jj = 0; jj < linkList.size(); jj++) {
					WDMLink tmplink=(WDMLink)linkList.get(jj);
					tmplink.getCarriedTrafficList();
					for (int ii = 0; ii < tmplink.getCarriedTrafficList().size(); ii++) {
						tmplink.delTraffic(tmplink.getCarriedTrafficList().get(ii));
					}
				}
				for (int  num= 0;  num< trafficList.size(); num++) {
					Traffic trf=trafficList.get(num);
					for (int i = 0; i < greenDesign.linkList.size(); i++) {
						WDMLink linkin=(WDMLink)greenDesign.linkList.get(i);			
							linkin.setM_nFree(500);
							for (int j = 0; j != trf.getM_nRate(); j++) {
								if(linkin.getM_lWavelength(j).getM_eStatus().equals(Status.宸ヤ綔)||linkin.getM_lWavelength(j).getM_eStatus().equals(Status.鎵胯浇)||linkin.getM_lWavelength(j).getM_eStatus().equals(Status.淇濇姢)||linkin.getM_lWavelength(j).getM_eStatus().equals(Status.淇濇姢鎭㈠)||linkin.getM_lWavelength(j).getM_eStatus().equals(Status.淇濇姢鎭㈠))
								linkin.getM_lWavelength(j).setM_eStatus(Status.绌洪棽);
								linkin.getM_lWavelength(j).setM_nFree(400);
								if (linkin.getM_cCarriedTraffic().contains(trf)) {
									linkin.delTraffic(trf); 
								}
								
							}
						
					}
				}

		default:
			break;
		}				
				
		plan2();//璁＄畻姣忎釜鏂瑰悜鐨勮矾鐢卞苟涓旇祫婧愬垎閰�
//		for (int i = 0; i < linkList.size(); i++) {
//			WDMLink templink=(WDMLink)linkList.get(i);
//			for (int j = 0; j < templink.getM_nSize(); j++) {
//				Wavelength tmpWavelength=templink.getM_lWavelength(j);
//				if (tmpWavelength.getM_eStatus()==Status.宸ヤ綔) {
//					System.out.println("閾捐矾鍚嶇О锛�"+templink.getM_sName()+"娉㈤亾鍙凤細"+tmpWavelength.getM_nID());
//					
//				}
//			}
//		}
		findLinkNumber( setinglimit, usinglimit);//璁＄畻姣忎釜鏂瑰悜閾捐矾鏁帮紝璁剧疆绌洪棽娉㈤亾鏁扮洰 锛宻et s_lWDMlink 鍦╬lan2()涓祫婧愬垎閰嶉噷闈㈠皢瀵箂_lWDMlink杩涜杞 璁剧疆鍗曚釜閾捐矾鐨勫埄鐢ㄧ巼
		initialize();//閾捐矾鍒╃敤鐜噓tilList閾捐〃娓呯┖锛屽垵濮嬪寲
		//plan();//涓轰粈涔堣鍐峱lan涓�娆★紵 wx
		plan2();//姣忎釜鏂瑰悜鐨勯摼璺暟鐩彲鑳戒骇鐢熷彉鍖栵紝鎵�浠ヨ閲嶆柊璁＄畻璺敱鍜岃祫婧愬垎閰�


		if(!success){
			JOptionPane.showMessageDialog(null,"涓氬姟涓嶈兘瑙勫垝鎴愬姛銆�");
			return false;
		}
		e0=findNetUtil();
		System.out.println("=================");
		System.out.println("鍚勬潯閾捐矾鍒╃敤鐜囷細");
		for(int i=0;i!=linkList.size();i++){
			System.out.println(((BasicLink)(linkList.get(i))).getName() + " 鍒╃敤鐜囷細 " + utilList.get(i));
		}
		System.out.println("=================");
		int times=0;
		int timek=linkList.size();
		if (flag==1) {
			//鑺傜偣搴︽暟澶т簬绛変簬2锛屾甯哥豢鍦拌鍒�
			for(int i=0;i!=timek;i++){//杩欓噷鏈夐棶棰橈紝瑙勫垝娆℃暟闄愬埗鎴愪簡28娆★紝鍗抽摼璺殑鏁伴噺锛屾墍浠ラ渶瑕佽繘琛屼慨鏀癸紒
				e0=findNetUtil();//wx 3.5鏇存柊e0鍊�
				//System.out.println("鍏ㄧ綉骞冲潎鍒╃敤鐜噀0锛�"+e0);
				if (((!(getMinUtil()<e1))&&(!(e0<target)))||linkList.size()==linkcantdel.size())
					break;
				findLink();//瀵绘壘寰呭垹闄ょ殑閾捐矾锛屾斁鍏inktodel涓�
				if((getNodeDegree(((BasicLink) linktodel).getM_cFromNode())>2)&&(getNodeDegree(( ((BasicLink) linktodel).getM_cToNode()))>2)){
					times++;
					initialize();//娓呯┖姣忔潯閾捐矾鐨勫伐浣滆矾鐢卞拰淇濇姢璺敱
					if (linktodel_utl==0) {
						linkList.remove(linktodel);//鍏堝垹闄ら摼璺埄鐢ㄧ巼涓�0锛屾弧瓒崇淮搴﹁姹傜殑閾捐矾锛岀洿鍒板埄鐢ㄧ巼涓�0鐨勯摼璺笉鍙垹闄�
						System.out.println("鍒犻櫎閾捐矾锛�"+((BasicLink)(linktodel)).getName());
					}
					else if (linktodel_utl>0&&e0<target) {//寰呭垹闄ら摼璺埄鐢ㄧ巼澶т簬0锛屼絾鏄綉缁滃钩鍧囧埄鐢ㄧ巼涓嶆弧瓒筹紝鎵�浠ラ渶瑕佹帴鐫�鍒犻櫎
						linkList.remove(linktodel);
						System.out.println("鍒犻櫎閾捐矾锛�"+((BasicLink)(linktodel)).getName());
					}
					else {
						System.out.println("姝ゆ涓嶅垹闄ら摼璺�");
					}
					//linkList.remove(linktodel);
					changeNet();			
				//	plan();
					plan2();
					if(!success){
						success=true;																	
						linkList.add(linktodel);
						changeNet();
						System.out.println("娣诲姞閾捐矾锛�"+((BasicLink)(linktodel)).getName());
						//plan();
						plan2();
						e0=findNetUtil();
						System.out.println("=================");
						System.out.println("plan澶辫触锛屾妸寰呭垹闄ら摼璺噸鏂板姞鍏inklist锛屽啀娆℃墦鍗板悇鏉￠摼璺埄鐢ㄧ巼锛�");
						for(int g=0;g!=linkList.size();g++){
							System.out.println(((BasicLink)(linkList.get(g))).getName() + " 鍒╃敤鐜囷細 " + utilList.get(g));
						}
						System.out.println("=================");
						linkcantdel.add(linktodel);
						System.out.println("times"+times);
						continue;
					}
					e0=findNetUtil();//璁＄畻骞朵笖鎵撳嵃鍏ㄧ綉骞冲潎鍒╃敤鐜�
					System.out.println("=================");
					System.out.println("plan鎴愬姛锛屾墦鍗板悇鏉￠摼璺埄鐢ㄧ巼锛�");
					for(int g=0;g!=linkList.size();g++){
						System.out.println(((BasicLink)(linkList.get(g))).getName() + " 鍒╃敤鐜囷細 " + utilList.get(g));
					}
					System.out.println("=================");
					System.out.println("times"+times);
				}
				else{
					times++;
					linkcantdel.add(linktodel);
					e0=findNetUtil();
					System.out.println("=================");
					System.out.println("閾捐矾鑺傜偣缁村害涓嶅ぇ浜�2锛岄噸鏂版墦鍗板悇鏉￠摼璺埄鐢ㄧ巼锛�");
					for(int g=0;g!=linkList.size();g++){
						System.out.println(((BasicLink)(linkList.get(g))).getName() + " 鍒╃敤鐜囷細 " + utilList.get(g));
					}
					System.out.println("=================");
					System.out.println("times"+times);
					continue;
				}
			}
		}
	else if(flag==0){
		//鑺傜偣搴︽暟澶т簬绛変簬0鍗冲彲锛屼笉鑰冭檻鑺傜偣搴︽暟鐨勭豢鍦拌鍒�.閾捐矾鍒╃敤鐜囩瓑浜�0鏃跺�欏彲浠ュ垹闄� wx 2014.1.4
		for (int j = 0; j < timek; j++) {
			//杩欓噷鏈夐棶棰橈紝瑙勫垝娆℃暟闄愬埗鎴愪簡28娆★紝鍗抽摼璺殑鏁伴噺锛屾墍浠ラ渶瑕佽繘琛屼慨鏀癸紒
			if (((!(getMinUtil()<e1))&&(!(e0<target)))||linkList.size()==linkcantdel.size())
				break;
			findLink();//瀵绘壘寰呭垹闄ょ殑閾捐矾锛屾斁鍏inktodel涓�
			if (getMinUtil()!=0) {//濡傛灉鍒犲畬浜嗗埄鐢ㄧ巼涓�0鐨勯摼璺紝鍒犻櫎鍒╃敤鐜囦笉涓�0鐨勯摼璺椂鍊欙紝鍙戠幇閾捐矾鐨勯鏈妭鐐瑰害鏁板皬浜庣瓑浜�2锛屽垯姝ら摼璺笉鑳藉垹闄わ紒 鍗� 濡傛灉鑺傜偣搴︽暟绛変簬2锛屽彧鑳藉垹闄ゅ埄鐢ㄧ巼涓�0鐨勯摼璺��
				if ((getNodeDegree(((BasicLink) linktodel).getM_cFromNode())<=2)||(getNodeDegree(((BasicLink) linktodel).getM_cToNode())<=2)) {
					times++;
					linkcantdel.add(linktodel);
					e0=findNetUtil();
					System.out.println("=================");
					System.out.println("鍚勬潯閾捐矾鍒╃敤鐜囷細");
					for(int g=0;g!=linkList.size();g++){
						System.out.println(((BasicLink)(linkList.get(g))).getName() + " 鍒╃敤鐜囷細 " + utilList.get(g));
					}
					System.out.println("=================");
					System.out.println("times"+times);
					continue;
				}
			}
			if((getNodeDegree(((BasicLink) linktodel).getM_cFromNode())>0)&&(getNodeDegree(( ((BasicLink) linktodel).getM_cToNode()))>0)){
				times++;
				initialize();//娓呯┖姣忔潯閾捐矾鐨勫伐浣滆矾鐢卞拰淇濇姢璺敱
				linkList.remove(linktodel);
				changeNet();
				System.out.println("鍒犻櫎閾捐矾锛�"+((BasicLink)(linktodel)).getName());
			//	plan();
				plan2();
				if(!success){
					success=true;
  				    linkList.add(linktodel);
					changeNet();
					System.out.println("娣诲姞閾捐矾锛�"+((BasicLink)(linktodel)).getName());
					//plan();
					plan2();
					e0=findNetUtil();
					System.out.println("=================");
					System.out.println("鍚勬潯閾捐矾鍒╃敤鐜囷細");
					for(int g=0;g!=linkList.size();g++){
						System.out.println(((BasicLink)(linkList.get(g))).getName() + " 鍒╃敤鐜囷細 " + utilList.get(g));
					}
					System.out.println("=================");
					linkcantdel.add(linktodel);
					System.out.println("times"+times);
					continue;
				}
				e0=findNetUtil();
				System.out.println("=================");
				System.out.println("鍚勬潯閾捐矾鍒╃敤鐜囷細");
				for(int g=0;g!=linkList.size();g++){
					System.out.println(((BasicLink)(linkList.get(g))).getName() + " 鍒╃敤鐜囷細 " + utilList.get(g));
				}
				System.out.println("=================");
				System.out.println("times"+times);
			}
			else{
				times++;
				linkcantdel.add(linktodel);
				e0=findNetUtil();
				System.out.println("=================");
				System.out.println("鍚勬潯閾捐矾鍒╃敤鐜囷細");
				for(int g=0;g!=linkList.size();g++){
					System.out.println(((BasicLink)(linkList.get(g))).getName() + " 鍒╃敤鐜囷細 " + utilList.get(g));
				}
				System.out.println("=================");
				System.out.println("times"+times);
				continue;
			}
		
		}

	}
	
		if (!(e0<target))
		{
			System.out.println("鏂板缓鎷撴墤瑙勫垝鎴愬姛");
			JOptionPane.showMessageDialog(null,"鏂板缓鎷撴墤瑙勫垝鎴愬姛");
			ArrayList mark = new ArrayList<Integer>();
			for(int i=0;i<linkList.size();i++)
				mark.add((Integer)0);
			
			if(trfLayer == Layer.WDM)
			    for(int i=0;i!=linkList.size();i++)
			    {
			    	WDMLink refWDMLink = (((WDMLink)linkList.get(i)));
			    	int nUsed = 0;				    	
			    	
			    	for(int j=i;j!=linkList.size();j++)
			    	{		    	
			    		WDMLink tempWDMLink = (((WDMLink)linkList.get(j)));		    		
			    		if(tempWDMLink.getName() == refWDMLink.getName())
			    		{
			    			FiberLink objectFiberLink = null;
			    			if(nUsed > 36 || ((Integer)mark.get(i)) == 0)         //濡傛灉澶т簬鏈�澶х氦鑺暟鎴栬�呮槸澶勭悊鐨勭涓�鏉￠摼璺�
			    			{
			    				int length=(int)GetDistance(tempWDMLink.getM_cFromNode().getLatitude(),tempWDMLink.getM_cFromNode().getLongitude(),tempWDMLink.getM_cToNode().getLatitude(),tempWDMLink.getM_cToNode().getLongitude());
			    				objectFiberLink = new FiberLink(tempWDMLink.getId(),tempWDMLink.getName(),tempWDMLink.getFromNode().getName(),tempWDMLink.getToNode().getName(),Layer.WDM,length,true,1,2012,36);
			    				FiberLink.fiberLinkList.add(objectFiberLink);          //娣诲姞鎷撴墤涓竴鏉iberLink		    				
			    				nUsed++;
			    			}
			    			else
			    			{
                                for(int k=0;k!=FiberLink.fiberLinkList.size();k++)
                                {
                                    if(sameFiberLink(FiberLink.fiberLinkList.get(k).getM_cFromNode().getId(),
                                        		FiberLink.fiberLinkList.get(k).getM_cToNode().getId()))
                                        	objectFiberLink = FiberLink.getFiberLink(k);                                 	
                                } 				                             
			    			}
			    								    	
//				    		WDMLink.getS_lWDMLinkList().add(((WDMLink)linkList.get(j)));       //鎷撴墤涓坊鍔燱DMLink
				    		tempWDMLink.getM_lFiberLinkList().add(objectFiberLink);       //鎶奧DM閾捐矾鎵胯浇鍦‵iber涓�
				    		mark.set(j, 1);
			    		}
			    		commonAlloc.dealWithInterLayerFiber(tempWDMLink, Layer.WDM);
			    		int size=linkList.size();
			    	}
			    }
			
			
			if(trfLayer == Layer.ASON)
			    for(int i=0;i!=linkList.size();i++)
			    {
			    	SDHLink refSDHLink = (((SDHLink)linkList.get(i)));
			    	int nUsed = 0;
			    	for(int j=i;j!=linkList.size();j++)
			    	{		    	
			    		SDHLink tempSDHLink = (((SDHLink)linkList.get(j)));		    		
			    		if(tempSDHLink.getM_sName() == refSDHLink.getM_sName())
			    		{
			    			FiberLink objectFiberLink = null;
			    			if(nUsed > 36 || ((Integer)mark.get(i)) == 0)
			    			{
			    				int length=(int)GetDistance(tempSDHLink.getM_cFromNode().getM_dLatitude(),tempSDHLink.getM_cFromNode().getM_dLongitude(),tempSDHLink.getM_cToNode().getM_dLatitude(),tempSDHLink.getM_cToNode().getM_dLongitude());
			    				objectFiberLink = new FiberLink(tempSDHLink.getM_nID(),tempSDHLink.getM_sName(),tempSDHLink.getM_cFromNode().getM_sName(),tempSDHLink.getM_cToNode().getM_sName(),Layer.ASON,length,true,1,2012,36);
			    				objectFiberLink.setM_sName(tempSDHLink.getM_sName());
			    				FiberLink.fiberLinkList.add(objectFiberLink);
			    				nUsed++;
			    			}
			    			else
			    			{			    				
			    				for(int k=0;k!=FiberLink.fiberLinkList.size();k++)
                                {
                                    if(sameFiberLink(FiberLink.fiberLinkList.get(k).getM_cFromNode().getId(),
                                    		FiberLink.fiberLinkList.get(k).getM_cToNode().getId()))
                                    	objectFiberLink = FiberLink.getFiberLink(k);                                	
                                }                               
			    			}
			    								    	
//			    			SDHLink.getS_lSDHLinkList().add(((SDHLink)linkList.get(j)));         //娣诲姞鎷撴墤
			    			tempSDHLink.getM_lFiberLinkList().add(objectFiberLink);           //鎵胯浇SDH鍒癋iber涓�
			    			tempSDHLink.setM_nType(1);
			    			mark.set(j, 1);
			    		}
			    		commonAlloc.dealWithInterLayerFiber(tempSDHLink, Layer.ASON);
			    	}
			    }
			System.out.println("缁忚繃(" + times + ")娆¤鍒掞紝鏂板缓鎷撴墤瑙勫垝鎴愬姛");
//			pw.close();
		    return true;
		}
		else
		{
			System.out.println("缁忚繃(" + times + ")娆¤鍒掞紝鏂板缓鎷撴墤瑙勫垝澶辫触");
			JOptionPane.showMessageDialog(null,"缁忚繃(" + times + ")娆¤鍒掞紝鏂板缓鎷撴墤瑙勫垝澶辫触");
//			pw.close();
			if(msg.equals("")){
				
				double a=findNetUtil()*100;				
				java.text.DecimalFormat   df1=new   java.text.DecimalFormat( "#.## "); 
				msg="缁忚繃(" + times + ")娆¤鍒掞紝鏂板缓鎷撴墤瑙勫垝鏈兘杈惧埌鏈�浣庡埄鐢ㄧ巼銆俓n鐩墠缃戠粶鐨勫钩鍧囧埄鐢ㄧ巼涓猴細"+df1.format(a)+"%";}
			return false;
		}
	}
	private static boolean sameFiberLink(int nFrom,int nTo)
	{
		for(int i=0;i!=FiberLink.fiberLinkList.size();i++)
		{
			FiberLink getLink = FiberLink.fiberLinkList.get(i);
			if ((getLink.getM_cFromNode().getId() == nFrom && getLink
					.getM_cToNode().getId() == nTo)|| 
				(getLink.getM_cFromNode().getId() == nTo && getLink
							.getM_cToNode().getId() == nFrom))
				return true;
		}
		
		return false;
	}
//	static void createAllConNetwork(){//寤虹珛鍏ㄨ繛鎺ョ綉缁�
//		for (int i=0;i!=nodeList.size()-1;i++){
//			int from_net_id=nodeList.get(i).getM_nSubnetNum();//棣栬妭鐐瑰瓙缃戝彿
//			int from_netType=Network.searchNetwork(from_net_id).getM_nNetType();//棣栬妭鐐规墍灞炲眰绫诲瀷
//			
//			for (int j=i+1;j!=nodeList.size();j++){
//				 int to_net_id=nodeList.get(j).getM_nSubnetNum();//鏈妭鐐瑰瓙缃戝彿
//				 int to_netType=Network.searchNetwork(to_net_id).getM_nNetType();//鏈妭鐐规墍灞炲眰绫诲瀷
//				if (trfLayer == Layer.WDM)									 				      
//					if (from_net_id==to_net_id||Network.searchNetwork(from_net_id).getM_nUpperNetwork().equals(Network.searchNetwork(to_net_id))||Network.searchNetwork(to_net_id).getM_nUpperNetwork().equals(Network.searchNetwork(from_net_id)))
//					{//濡傛灉棣栨湯鑺傜偣鍦ㄥ悓涓�瀛愮綉鎴栬�� 棣栬妭鐐逛笂灞傜綉缁滄槸鏈妭鐐� 鎴栬�呮湯鑺傜偣涓婂眰缃戠粶鏄鑺傜偣锛岃繖涓や釜鑺傜偣闂村彲浠ユ瀯寤洪摼璺�
//					 //濡傛灉涓や釜鑺傜偣鍦ㄥ悓涓�灞備腑涓嶅悓瀛愮綉涓紝杩欎袱鑺傜偣涓嶈兘鏋勫缓閾捐矾锛屾剰鎬濇槸涓嶈兘鐩磋揪
//						linkList.add(getNewWDMLink(nodeList.get(i),nodeList.get(j)));
//					}
//					
//				if (trfLayer == Layer.SDH)
//					if (from_net_id==to_net_id||Network.searchNetwork(from_net_id).getM_nUpperNetwork().equals(Network.searchNetwork(to_net_id))||Network.searchNetwork(to_net_id).getM_nUpperNetwork().equals(Network.searchNetwork(from_net_id))) {
//						linkList.add(getNewSDHLink(nodeList.get(i),nodeList.get(j)));
//					}
//					
//			}
//		}
//		if(trfLayer == Layer.WDM){
//			
//		    net.setM_lInNetWDMLinkList(linkList);
//		    net.setM_lInNetNodeList(nodeList);
//		}
//		if(trfLayer == Layer.SDH){
//			net.setM_lInNetSDHLinkList(linkList);
//			net.setM_lInNetNodeList(nodeList);
//		}
//	}
	
	 * 鍑芥暟鍔熻兘锛氬垎灞傚垎鍩熸瀯寤哄叏杩炴帴缃戠粶銆傚鏋滀袱涓妭鐐瑰睘浜庡悓涓�瀛愮綉锛屾瀯寤鸿繛鎺ラ摼璺紱濡傛灉涓や釜鑺傜偣涓嶅湪鍚屼竴瀛愮綉锛屼絾鏄睘浜庝笂涓嬩袱涓瓙缃戯紙浠庡睘鍏崇郴锛屼笉鑳借法灞傦級锛屼篃鍙互鏋勫缓杩炴帴锛涘叾浣欐儏鍐靛潎涓嶅彲銆傚悓涓�灞傚唴涓嶅悓瀛愮綉涓や釜鑺傜偣涓嶈兘鏋勫缓杩炴帴
	 * wx 2013.12.25
	 
	static void createAllConNetwork2(){
		//寤虹珛鍏ㄨ繛鎺ョ綉缁�
				for (int i=0;i!=nodeList.size()-1;i++){
					int from_net_id=nodeList.get(i).getM_nSubnetNum();//棣栬妭鐐瑰瓙缃戝彿
					int from_netType=Network.searchNetwork(from_net_id).getM_nNetType();//棣栬妭鐐规墍灞炲眰绫诲瀷
					
					for (int j=i+1;j!=nodeList.size();j++){
						 int to_net_id=nodeList.get(j).getM_nSubnetNum();//鏈妭鐐瑰瓙缃戝彿
						 int to_netType=Network.searchNetwork(to_net_id).getM_nNetType();//鏈妭鐐规墍灞炲眰绫诲瀷
						if (trfLayer == Layer.WDM)	{
							if (from_net_id==to_net_id)
							{//濡傛灉棣栨湯鑺傜偣鍦ㄥ悓涓�瀛愮綉杩欎袱涓妭鐐归棿鍙互鏋勫缓閾捐矾
							 //濡傛灉涓や釜鑺傜偣鍦ㄥ悓涓�灞備腑涓嶅悓瀛愮綉涓紝杩欎袱鑺傜偣涓嶈兘鏋勫缓閾捐矾锛屾剰鎬濇槸涓嶈兘鐩磋揪
								linkList.add(getNewWDMLink(nodeList.get(i),nodeList.get(j)));//瀛樻斁鎵�鏈夐摼璺�
								//Network.searchNetwork(from_net_id).setM_lInNetWDMLinkList(linkList);
								Network.searchNetwork(from_net_id).addInNetLink(getNewWDMLink(nodeList.get(i),nodeList.get(j)));//姣忎釜缃戠粶澧炲姞缃戝唴閾捐矾
								Network.searchNetwork(from_net_id).addNode(nodeList.get(i));//姣忎釜缃戠粶瀵瑰簲澧炲姞缃戝唴鑺傜偣
								Network.searchNetwork(to_net_id).addNode(nodeList.get(j));
							}
							else if (Network.searchNetwork(from_net_id).getM_nUpperNetwork().equals(Network.searchNetwork(to_net_id))||Network.searchNetwork(to_net_id).getM_nUpperNetwork().equals(Network.searchNetwork(from_net_id))) {
							//鎴栬�� 棣栬妭鐐逛笂灞傜綉缁滄槸鏈妭鐐� 鎴栬�呮湯鑺傜偣涓婂眰缃戠粶鏄鑺傜偣锛岃繖涓や釜鑺傜偣闂村彲浠ユ瀯寤洪摼璺�;鍗充粬浠槸涓婁笅灞傚叧绯�
							WDMLink tempLink=getNewWDMLink(nodeList.get(i),nodeList.get(j));
							linkList.add(tempLink);						
							Network.searchNetwork(from_net_id).addGatewayLinkList(tempLink);//姣忎釜缃戠粶澧炲姞缃戝叧閾捐矾
							Network.searchNetwork(to_net_id).addGatewayLinkList(tempLink);
							Network.searchNetwork(from_net_id).addGatewayNodeList(nodeList.get(i));//棣栬妭鐐规墍鍦ㄧ綉缁滃鍔犵綉鍏宠妭鐐�
							Network.searchNetwork(to_net_id).addGatewayNodeList(nodeList.get(j));//鏈妭鐐�.....
						}
							else continue;
						}								 				      
						
							
//						if (trfLayer == Layer.ASON)
//						{
//							if (from_net_id==to_net_id)
//						    {//濡傛灉棣栨湯鑺傜偣鍦ㄥ悓涓�瀛愮綉杩欎袱涓妭鐐归棿鍙互鏋勫缓閾捐矾
//							 //濡傛灉涓や釜鑺傜偣鍦ㄥ悓涓�灞備腑涓嶅悓瀛愮綉涓紝杩欎袱鑺傜偣涓嶈兘鏋勫缓閾捐矾锛屾剰鎬濇槸涓嶈兘鐩磋揪
//								linkList.add(getNewASONLink(nodeList.get(i),nodeList.get(j)));//瀛樻斁鎵�鏈夐摼璺�
//								//Network.searchNetwork(from_net_id).setM_lInNetWDMLinkList(linkList);
//								Network.searchNetwork(from_net_id).addInNetLink(getNewASONLink(nodeList.get(i),nodeList.get(j)));//姣忎釜缃戠粶澧炲姞缃戝唴閾捐矾
//								Network.searchNetwork(from_net_id).addNode(nodeList.get(i));//姣忎釜缃戠粶瀵瑰簲澧炲姞缃戝唴鑺傜偣
//								Network.searchNetwork(to_net_id).addNode(nodeList.get(j));
//							}
//						if (Network.searchNetwork(from_net_id).getM_nUpperNetwork().equals(Network.searchNetwork(to_net_id))||Network.searchNetwork(to_net_id).getM_nUpperNetwork().equals(Network.searchNetwork(from_net_id))) 
//						{
//							//鎴栬�� 棣栬妭鐐逛笂灞傜綉缁滄槸鏈妭鐐� 鎴栬�呮湯鑺傜偣涓婂眰缃戠粶鏄鑺傜偣锛岃繖涓や釜鑺傜偣闂村彲浠ユ瀯寤洪摼璺�;鍗充粬浠槸涓婁笅灞傚叧绯�
//							SDHLink tempSDHlink=getNewASONLink(nodeList.get(i),nodeList.get(j));
//							linkList.add(tempSDHlink);
//							Network.searchNetwork(from_net_id).addGatewayLinkList(tempSDHlink);//姣忎釜缃戠粶澧炲姞缃戝叧閾捐矾
//							Network.searchNetwork(to_net_id).addGatewayLinkList(tempSDHlink);
//							Network.searchNetwork(from_net_id).addGatewayNodeList(nodeList.get(i));//棣栬妭鐐规墍鍦ㄧ綉缁滃鍔犵綉鍏宠妭鐐�
//							Network.searchNetwork(to_net_id).addGatewayNodeList(nodeList.get(j));//鏈妭鐐�.....
//						}
//						else continue;
//					}												
				}
			}
	}
	private static double rad(double d)
	{
	   return d * Math.PI / 180.0;
	}
	private static double EARTH_RADIUS = 6378.137; //鍦扮悆鍗婂緞
	public static double GetDistance(double lat1, double lng1, double lat2, double lng2)
	{
	   double radLat1 = rad(lat1);
	   double radLat2 = rad(lat2);
	   double a = radLat1 - radLat2;
	   double b = rad(lng1) - rad(lng2);
	   double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
	    Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
	   s = s * EARTH_RADIUS;
	   s = Math.round(s * 10000) / 10000;
	   return s;
	}
	private static WDMLink getNewWDMLink(CommonNode From,CommonNode To){//鏍规嵁棣栨湯鑺傜偣鏂板缓閾捐矾
		int length=(int)GetDistance(From.getLatitude(),From.getLongitude(),To.getLatitude(),To.getLongitude());
		WDMLink tmp= new WDMLink(mID++,From.getName() + "-" + To.getName(),From.getName(),To.getName(),Layer.WDM,length,400,infi,true,1,2012);
		tmp.setFromNode(From);
		tmp.setToNode(To);
		tmp.setM_nFree(tmp.getSize());
		for(int i=0;i!=tmp.getSize();i++)
		{
			Wavelength wl = tmp.getM_lWavelength(i);
			wl.getM_cFrom().setNode(From);
			wl.getM_cTo().setNode(To);
		}
		return tmp;
	}
	private static WDMLink getNewWDMLink(CommonNode From,CommonNode To,int n){//鏂板缓WDM閾捐矾
		int length=(int)GetDistance(From.getLatitude(),From.getLongitude(),To.getLatitude(),To.getLongitude());
		WDMLink tmp= new WDMLink(mID++,From.getName() + "-" + To.getName()+n,From.getName(),To.getName(),Layer.WDM,length,400,infi,true,1,2012);
		tmp.setToNode(To);
		tmp.setM_nFree(tmp.getSize());
		for(int i=0;i!=tmp.getM_nSize();i++)
		{
			Wavelength wl = tmp.getM_lWavelength(i);
			wl.getM_cFrom().setNode(From);
			wl.getM_cTo().setNode(To);
		}
		return tmp;
	}
//	private static SDHLink getNewSDHLink(CommonNode From,CommonNode To){
//		int length=(int)GetDistance(From.getM_dLatitude(),From.getM_dLongitude(),To.getM_dLatitude(),To.getM_dLongitude());
//		SDHLink tmp= new SDHLink(mID++,From.getM_sName() + "-" + To.getM_sName(),From.getM_sName(),To.getM_sName(),Layer.SDH,length,infi,infi,true,1,2012,Layer.FIBER);
//		tmp.setM_cFromNode(From);
//		tmp.setM_cToNode(To);
//		tmp.setM_nFree(tmp.getM_nSize());
//		return tmp;
//	}
//	private static SDHLink getNewASONLink(CommonNode From,CommonNode To){
//		int length=(int)GetDistance(From.getLatitude(),From.getLongitude(),To.getLatitude(),To.getLongitude());
//		SDHLink tmp= new SDHLink(mID++,From.getM_sName() + "-" + To.getM_sName(),From.getM_sName(),To.getM_sName(),Layer.ASON,length,infi,infi,true,1,2012,Layer.FIBER);
//		tmp.setM_cFromNode(From);
//		tmp.setM_cToNode(To);
//		tmp.setM_nFree(tmp.getM_nSize());
//		return tmp;
//	}//WX 3.4
//	private static SDHLink getNewSDHLink(CommonNode From,CommonNode To,int n){
//		int length=(int)GetDistance(From.getLatitude(),From.getLongitude(),To.getLatitude(),To.getLongitude());
//		SDHLink tmp= new SDHLink(mID++,From.getM_sName() + "-" + To.getM_sName(),From.getM_sName(),To.getM_sName(),Layer.SDH,length,infi,infi,true,1,2012,Layer.FIBER);
//		tmp.setM_cFromNode(From);
//		tmp.setM_cToNode(To);
//		tmp.setM_nFree(tmp.getM_nSize());
//		return tmp;
//	}
//	private static SDHLink getNewASONLink(CommonNode From,CommonNode To,int n){
//		int length=(int)GetDistance(From.getM_dLatitude(),From.getM_dLongitude(),To.getM_dLatitude(),To.getM_dLongitude());
//		SDHLink tmp= new SDHLink(mID++,From.getM_sName() + "-" + To.getM_sName(),From.getM_sName(),To.getM_sName(),Layer.ASON,length,infi,infi,true,1,2012,Layer.FIBER);
//		tmp.setM_cFromNode(From);
//		tmp.setM_cToNode(To);
//		tmp.setM_nFree(tmp.getM_nSize());
//		return tmp;
//	}
//	static void plan(){//鏁翠綋瑙勫垝鍏ュ彛
//		algorithm.Initial_Link_Weight(linkList,Define.by_length);//鍒濆鍖栭摼璺潈閲�
//		if (trfLayer==Layer.WDM){
//			int tnt=0;//璁＄畻涓氬姟鏁伴噺
//			for (int i=0; i!=trfList.size(); i++){//涓烘瘡鏉′笟鍔¤绠楀伐浣滆矾鐢卞拰淇濇姢璺敱锛岃繘琛岃祫婧愬垎閰�
//				tnt++;
//				Traffic temp= trfList.get(i);//浠庝笟鍔￠摼涓彇鍑轰笟鍔★紝鍒ゆ柇閫熺巼鏄惁绗﹀悎鏉′欢
//				LinkedList<WDMLink> wdmLinkList= new LinkedList<WDMLink>();
//				if(temp.getM_nRate()!=10&&temp.getM_nRate()!=100&&temp.getM_nRate()!=400&&temp.getM_nRate()!=25) {
//					JOptionPane.showMessageDialog(null,"涓氬姟閫熺巼涓庢墍灞炲眰涓嶇鍚�","鎻愮ず",JOptionPane.INFORMATION_MESSAGE);
//					System.out.println(" 涓氬姟閫熺巼涓庢墍灞炲眰閫熺巼涓嶇  ");
//					success=false;
//					return;
//				}
//				algorithm.Initial_Link_Weight(linkList,Define.by_length);//鍒濆鍖栭摼璺潈閲�
// 				algorithm.SetNodeRankID(nodeList);
//				D(net,temp.getM_nFromNode(),temp.getM_nToNode(),wdmLinkList);//d绠楁硶璁＄畻璺敱锛岃繑鍥炵殑璺敱鏀惧湪wdmlinklist涓�
// 				temp.setM_cWorkRoute(wdmLinkList);//set宸ヤ綔璺敱
// 				if(wdmLinkList.size()==0) {//濡傛灉宸ヤ綔璺敱閾捐矾涓虹┖锛屽垯閲婃斁璧勬簮锛岃矾鐢辫绠楀け璐�
//					for(int n=0;n!=tnt-1;n++){
//						trfList.get(n).releaseTraffic(trfList.get(n),0);//閲婃斁宸ヤ綔璺敱鍗犵敤璧勬簮
//						trfList.get(n).getM_cWorkRoute().clearRoute();//娓呯┖宸ヤ綔璺敱
//						if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){
//							trfList.get(n).releaseTraffic(trfList.get(n),1);//閲婃斁淇濇姢璺敱鍗犵敤璧勬簮
//							trfList.get(n).getM_cProtectRoute().clearRoute();//娓呯┖淇濇姢璺敱
//							
//						}
//					}
//					System.out.println("璺敱璁＄畻澶辫触");
//					success=false;
//					return;
//				}
//				if(!commonAlloc.ResourceAssignment(temp, 0)){//宸ヤ綔璺敱璧勬簮鍒嗛厤澶辫触鐨勬儏鍐�
//					for(int n=0;n!=tnt-1;n++){
//						trfList.get(n).releaseTraffic(trfList.get(n),0);
//						trfList.get(n).getM_cWorkRoute().clearRoute();
//						if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){
//							trfList.get(n).releaseTraffic(trfList.get(n),1);
//							trfList.get(n).getM_cProtectRoute().clearRoute();
//							
//						}
//					}
//					temp.getM_cWorkRoute().clearRoute();
//					System.out.println("璧勬簮鍒嗛厤澶辫触");
//					System.out.println("===========");
//					success=false;
//					return;
//				}
//                if(temp.getM_eLevel().equals(TrafficLevel.PERMANENT11)||temp.getM_eLevel().equals(TrafficLevel.NORMAL11)){//濡傛灉涓氬姟鏈変繚鎶よ矾鐢憋紝鍒欓渶瑕佽�冭檻淇濇姢璺敱鐨勬儏鍐�
//                	DeleteSRLGList(wdmLinkList,false);//鍒犻櫎srlg锛岄�氳繃璁剧疆鏉冮噸涓烘棤闄愬ぇ锛岃〃绀轰笉鍙埌杈�
//                	for (int f=0;f<temp.getM_cWorkRoute().getM_lWDMLinkList().size();f++){//浠庡伐浣滆矾鐢遍摼璺腑鍙栧嚭閾捐矾锛岃缃湭婵�娲�
//                		WDMLink theLink=temp.getM_cWorkRoute().getM_lWDMLinkList().get(f);
//                		theLink.setM_bStatus(false);//鍏堝皢宸ヤ綔璺敱鐨勯摼璺缃负鏈縺娲伙紝鏂逛究杩涗竴姝ヤ负淇濇姢璺敱绠楄矾
//                	}
//                	LinkedList<WDMLink> prwdmLinkList = new LinkedList<WDMLink>();
//                	D(net,temp.getM_nFromNode(),temp.getM_nToNode(),prwdmLinkList);//d绠楁硶璁＄畻淇濇姢璺敱
//                	for (int f=0;f<temp.getM_cWorkRoute().getM_lWDMLinkList().size();f++){
//                		WDMLink theLink=temp.getM_cWorkRoute().getM_lWDMLinkList().get(f);
//                		theLink.setM_bStatus(true);//绠楄矾鍚庯紝浠庡伐浣滆矾鐢遍摼璺腑鍙栧嚭閾捐矾锛岃缃负婵�娲荤姸鎬�
//                	}
//                	temp.setM_cProtectRoute(prwdmLinkList);//璁剧疆淇濇姢璺敱
//                	if(prwdmLinkList.size()==0){//濡傛灉淇濇姢璺敱璁＄畻澶辫触
//                		for(int n=0;n!=tnt-1;n++){						
//    						trfList.get(n).releaseTraffic(trfList.get(n),0);
//    						trfList.get(n).getM_cWorkRoute().clearRoute();
//    						if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){
//    							trfList.get(n).releaseTraffic(trfList.get(n),1);
//    							trfList.get(n).getM_cProtectRoute().clearRoute();
//    							
//    						}
//    					}
//                		temp.releaseTraffic(temp,0);
//                		temp.getM_cWorkRoute().clearRoute();
//                		System.out.println("淇濇姢璺敱璁＄畻澶辫触");
//    					success=false;
//    					return;
//                	}
//                	if(!commonAlloc.ResourceAssignment(temp, 1)){//濡傛灉淇濇姢璺敱璁＄畻澶辫触
//                		for(int n=0;n!=tnt-1;n++){
//                			trfList.get(n).releaseTraffic(trfList.get(n),0);//閲婃斁宸ヤ綔璺敱銆佷繚鎶よ矾鐢便�佸苟娓呯┖閾捐矾
//							trfList.get(n).releaseTraffic(trfList.get(n),1);
//                			trfList.get(n).getM_cWorkRoute().clearRoute();
//							trfList.get(n).getM_cProtectRoute().clearRoute();
//							
//						}
//                		temp.releaseTraffic(temp,0);
//                		temp.getM_cWorkRoute().clearRoute();
//						temp.getM_cProtectRoute().clearRoute();
//						System.out.println("宸ヤ綔閾捐矾鎴愬姛锛屼繚鎶ら摼璺け璐�/n");
//						System.out.println("===========");
//						success=false;
//						return;
//                	}
//                }
//                
//                System.out.println(" 涓氬姟-"+tnt+"   瀹屾垚 ");
//			}
//		}
//		if (trfLayer==Layer.SDH) {//鍚屼笂
//			int tnt=0;
//			for (int i = 0; i != trfList.size(); ++i){
//				tnt++;
//				Traffic aTraffic = trfList.get(i);
//				LinkedList<SDHLink> sdhLinkList = new LinkedList<SDHLink>();
//				if(aTraffic.getM_nRate()!=64&&aTraffic.getM_nRate()!=16&&aTraffic.getM_nRate()!=100&&aTraffic.getM_nRate()!=4&&aTraffic.getM_nRate()!=1&&aTraffic.getM_nRate()!=50&&aTraffic.getM_nRate()!=2){
//					JOptionPane.showMessageDialog(null,"涓氬姟閫熺巼涓庢墍灞炲眰涓嶇鍚�","鎻愮ず",JOptionPane.INFORMATION_MESSAGE);
//					System.out.println(" 涓氬姟閫熺巼涓庢墍灞炲眰閫熺巼涓嶇  ");
//					success=false;
//					return;
//				}
//				algorithm.Initial_Link_Weight(linkList,Define.by_length);
//				algorithm.SetNodeRankID(nodeList);		
//				D(net,aTraffic.getM_nFromNode(),aTraffic.getM_nToNode(),sdhLinkList);
//				if(sdhLinkList.size() == 0){
//					for(int n=0;n!=tnt-1;n++){					
//						trfList.get(n).releaseTraffic(trfList.get(n),0);
//						trfList.get(n).getM_cWorkRoute().clearRoute();
//						if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){
//							trfList.get(n).releaseTraffic(trfList.get(n),1);
//							trfList.get(n).getM_cProtectRoute().clearRoute();
//						}
//					}
//					System.out.println("璺敱璁＄畻澶辫触");
//					success=false;
//					return;
//				}
//				else{
//					aTraffic.setM_cWorkRoute(sdhLinkList);
//					boolean signal = commonAlloc.ResourceAssignment(aTraffic, 0);
//					if (!signal){
//						for(int n=0;n!=tnt-1;n++){
//							
//							trfList.get(n).releaseTraffic(trfList.get(n),0);
//							trfList.get(n).getM_cWorkRoute().clearRoute();
//							if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){								
//								trfList.get(n).releaseTraffic(trfList.get(n),1);
//								trfList.get(n).getM_cProtectRoute().clearRoute();
//							}
//						}
//						aTraffic.getM_cWorkRoute().clearRoute();
//						System.out.println("璧勬簮鍒嗛厤澶辫触");
//						System.out.println("===========");
//						success=false;
//						return;
//					}
//					if (signal)
//						System.out.println("璧勬簮鍒嗛厤鎴愬姛");
//					if (aTraffic.getM_eLevel().equals(TrafficLevel.PERMANENT11)||aTraffic.getM_eLevel().equals(TrafficLevel.NORMAL11)){
//						DeleteSRLGList(sdhLinkList, false);
//						for (int f=0;f<aTraffic.getM_cWorkRoute().getM_lSDHLinkList().size();f++){
//	                		SDHLink theLink=aTraffic.getM_cWorkRoute().getM_lSDHLinkList().get(f);
//	                		theLink.setM_bStatus(false);
//	                	}
//						LinkedList<SDHLink> prsdhLinkList = new LinkedList<SDHLink>();
//						D(net,aTraffic.getM_nFromNode(),aTraffic.getM_nToNode(),prsdhLinkList);
//						for (int f=0;f<aTraffic.getM_cWorkRoute().getM_lSDHLinkList().size();f++){
//		            		SDHLink theLink=aTraffic.getM_cWorkRoute().getM_lSDHLinkList().get(f);
//		            		theLink.setM_bStatus(true);
//		            	}
//						aTraffic.setM_cProtectRoute(prsdhLinkList);
//						if(prsdhLinkList.size()==0){
//	                		for(int n=0;n!=tnt-1;n++){						
//	    						trfList.get(n).releaseTraffic(trfList.get(n),0);
//	    						trfList.get(n).getM_cWorkRoute().clearRoute();
//	    						if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){
//	    							trfList.get(n).releaseTraffic(trfList.get(n),1);
//	    							trfList.get(n).getM_cProtectRoute().clearRoute();
//	    							
//	    						}
//	    					}
//	                		aTraffic.releaseTraffic(aTraffic,0);
//	                		aTraffic.getM_cWorkRoute().clearRoute();
//	                		System.out.println("淇濇姢璺敱璁＄畻澶辫触");
//	    					success=false;
//	    					return;
//	                	}
//						if(!commonAlloc.ResourceAssignment(aTraffic, 1)){
//							for(int n=0;n!=tnt-1;n++){							
//								trfList.get(n).releaseTraffic(trfList.get(n),0);
//								trfList.get(n).releaseTraffic(trfList.get(n),1);
//								trfList.get(n).getM_cWorkRoute().clearRoute();
//								trfList.get(n).getM_cProtectRoute().clearRoute();
//							}
//							aTraffic.releaseTraffic(aTraffic,0);
//							aTraffic.getM_cProtectRoute().clearRoute();
//							aTraffic.getM_cWorkRoute().clearRoute();
//							System.out.println("宸ヤ綔閾捐矾鎴愬姛锛屼繚鎶ら摼璺け璐�/n");
//							System.out.println("===========");
//							success=false;
//							return;
//						}
//					}
//				}
//				
//			}
//		}
//	}
	
	public static void plan2(){//wx 鍒嗗眰鍒嗗煙
		//鏁翠綋瑙勫垝鍏ュ彛
//		commonAlloc.sFallbuffer=new StringBuffer();//鎯呭喌璧勬簮鍒嗛厤澶辫触鍒楄〃 wx 3.17
//		portAlloc.sPortFallBuffer=new StringBuffer();//鍚屼笂
				algorithm.Initial_Link_Weight(linkList,Define.by_length);//鍒濆鍖栭摼璺潈閲�
				if (trfLayer==Layer.WDM){
					int tnt=0;//璁＄畻涓氬姟鏁伴噺
					for (int i=0; i!=trfList.size(); i++){//涓烘瘡鏉′笟鍔¤绠楀伐浣滆矾鐢卞拰淇濇姢璺敱锛岃繘琛岃祫婧愬垎閰�
						tnt++;
						Traffic temp= trfList.get(i);//浠庝笟鍔￠摼涓彇鍑轰笟鍔★紝鍒ゆ柇閫熺巼鏄惁绗﹀悎鏉′欢
					//	LinkedList<WDMLink> wdmLinkList= new LinkedList<WDMLink>();
						if(temp.getM_nRate()!=10&&temp.getM_nRate()!=100&&temp.getM_nRate()!=400&&temp.getM_nRate()!=25) {
							JOptionPane.showMessageDialog(null,"涓氬姟閫熺巼涓庢墍灞炲眰涓嶇鍚�","鎻愮ず",JOptionPane.INFORMATION_MESSAGE);
							System.out.println(" 涓氬姟閫熺巼涓庢墍灞炲眰閫熺巼涓嶇  ");
							success=false;
							return;
						}
						
						//鍒ゆ柇鎵�灞炲眰銆佸煙 wx
						
						LinkedList returnList=new LinkedList();
						algorithm.SetNodeRankID(nodeList);
						Route route=algorithm.RWAMain(temp.getFromNode(),temp.getToNode(),temp.getM_eLayer(),2,returnList);//2涓烘寜闀垮害							 				
						if(route!=null)
							for (int j = 0; j < route.getM_lWDMLinkList().size(); j++) {
							System.out.println(route.getM_lWDMLinkList().get(j).getM_sName());	
							}//娴嬭瘯 wx
						temp.setM_cWorkRoute(route);//set宸ヤ綔璺敱
						algorithm.rmRepeat(temp,0);//firesky 2012 4.19
		 				
		 				if(route==null) {//濡傛灉宸ヤ綔璺敱閾捐矾涓虹┖锛屽垯閲婃斁璧勬簮锛岃矾鐢辫绠楀け璐�
							for(int n=0;n!=tnt-1;n++){
								trfList.get(n).releaseTraffic(trfList.get(n),0);//閲婃斁宸ヤ綔璺敱鍗犵敤璧勬簮
								trfList.get(n).getM_cWorkRoute().clearRoute();//娓呯┖宸ヤ綔璺敱
								if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){
									trfList.get(n).releaseTraffic(trfList.get(n),1);//閲婃斁淇濇姢璺敱鍗犵敤璧勬簮
									trfList.get(n).getM_cProtectRoute().clearRoute();//娓呯┖淇濇姢璺敱
									
								}
							}
							System.out.println("璺敱璁＄畻澶辫触");
							success=false;
							return;
						}
						if(!commonAlloc.ResourceAssignment(temp, 0)){//宸ヤ綔璺敱璧勬簮鍒嗛厤澶辫触鐨勬儏鍐�
							for(int n=0;n!=tnt-1;n++){
								trfList.get(n).releaseTraffic(trfList.get(n),0);
								trfList.get(n).getM_cWorkRoute().clearRoute();
								if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){
									trfList.get(n).releaseTraffic(trfList.get(n),1);
									trfList.get(n).getM_cProtectRoute().clearRoute();
									
								}
							}
							temp.getM_cWorkRoute().clearRoute();
							System.out.println("璧勬簮鍒嗛厤澶辫触");
							System.out.println("===========");
							success=false;
							return;
						}
		                if(temp.getM_eLevel().equals(TrafficLevel.PERMANENT11)||temp.getM_eLevel().equals(TrafficLevel.NORMAL11)){//濡傛灉涓氬姟鏈変繚鎶よ矾鐢憋紝鍒欓渶瑕佽�冭檻淇濇姢璺敱鐨勬儏鍐�
		                	DeleteSRLGList(route.getM_lWDMLinkList(),false);//鍒犻櫎srlg锛岄�氳繃璁剧疆鏉冮噸涓烘棤闄愬ぇ锛岃〃绀轰笉鍙埌杈�
		                	DeleteSRLGList(route.getM_lOTNLinkList(),false);
		                	for (int f=0;f<temp.getM_cWorkRoute().getM_lWDMLinkList().size();f++){//浠庡伐浣滆矾鐢遍摼璺腑鍙栧嚭閾捐矾锛岃缃湭婵�娲�
		                		WDMLink theLink=temp.getM_cWorkRoute().getM_lWDMLinkList().get(f);
		                		theLink.setM_bStatus(false);//鍏堝皢宸ヤ綔璺敱鐨勯摼璺缃负鏈縺娲伙紝鏂逛究杩涗竴姝ヤ负淇濇姢璺敱绠楄矾
		                	}
		                	Route routeP=algorithm.RWAMain(temp.getM_nFromNode(),temp.getM_nToNode(),temp.getM_eLayer(),2,returnList);
		    				if(routeP!=null)
		    					temp.setM_cProtectRoute(routeP);
		    				algorithm.rmRepeat(temp,1);//firesky 2012 4.19
//		                	
		                	for (int f=0;f<temp.getM_cWorkRoute().getM_lWDMLinkList().size();f++){
		                		WDMLink theLink=temp.getM_cWorkRoute().getM_lWDMLinkList().get(f);
		                		theLink.setM_bStatus(true);//绠楄矾鍚庯紝浠庡伐浣滆矾鐢遍摼璺腑鍙栧嚭閾捐矾锛岃缃负婵�娲荤姸鎬�
		                	}
		                	
		                	if(routeP==null){//濡傛灉淇濇姢璺敱璁＄畻澶辫触
		                		for(int n=0;n!=tnt-1;n++){						
		    						trfList.get(n).releaseTraffic(trfList.get(n),0);
		    						trfList.get(n).getM_cWorkRoute().clearRoute();
		    						if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){
		    							trfList.get(n).releaseTraffic(trfList.get(n),1);
		    							trfList.get(n).getM_cProtectRoute().clearRoute();
		    							
		    						}
		    					}
		                		temp.releaseTraffic(temp,0);
		                		temp.getM_cWorkRoute().clearRoute();
		                		System.out.println("淇濇姢璺敱璁＄畻澶辫触");
		    					success=false;
		    					return;
		                	}
		                	if(!commonAlloc.ResourceAssignment(temp, 1)){//濡傛灉淇濇姢璺敱璁＄畻澶辫触
		                		for(int n=0;n!=tnt-1;n++){
		                			trfList.get(n).releaseTraffic(trfList.get(n),0);//閲婃斁宸ヤ綔璺敱銆佷繚鎶よ矾鐢便�佸苟娓呯┖閾捐矾
									trfList.get(n).releaseTraffic(trfList.get(n),1);
		                			trfList.get(n).getM_cWorkRoute().clearRoute();
									trfList.get(n).getM_cProtectRoute().clearRoute();
									
								}
		                		temp.releaseTraffic(temp,0);
		                		temp.getM_cWorkRoute().clearRoute();
								temp.getM_cProtectRoute().clearRoute();
								System.out.println("宸ヤ綔閾捐矾鎴愬姛锛屼繚鎶ら摼璺け璐�/n");
								System.out.println("===========");
								success=false;
								return;
		                	}
		                }
		                
		                System.out.println(" 涓氬姟-"+tnt+"   瀹屾垚 ");
		                for (int ii = 0; ii< linkList.size(); ii++) {
		        			WDMLink templink=(WDMLink)linkList.get(ii);
		        			for (int j = 0; j < templink.getM_nSize(); j++) {
		        				Wavelength tmpWavelength=templink.getM_lWavelength(j);
		        				if (tmpWavelength.getM_eStatus()==Status.宸ヤ綔) {
		        					System.out.println("閾捐矾鍚嶇О锛�"+templink.getM_sName()+"娉㈤亾鍙凤細"+tmpWavelength.getM_nID());
		        					
		        				}
		        			}
		        		}
		    		
					}
				}
				
				
				
				else if (trfLayer==Layer.ASON) {
					int tnt=0;//璁＄畻涓氬姟鏁伴噺
					for (int i=0; i!=trfList.size(); i++){//涓烘瘡鏉′笟鍔¤绠楀伐浣滆矾鐢卞拰淇濇姢璺敱锛岃繘琛岃祫婧愬垎閰�
						tnt++;
						Traffic temp= trfList.get(i);//浠庝笟鍔￠摼涓彇鍑轰笟鍔★紝鍒ゆ柇閫熺巼鏄惁绗﹀悎鏉′欢
						//LinkedList<WDMLink> wdmLinkList= new LinkedList<WDMLink>();
						if(temp.getM_nRate()!=64&&temp.getM_nRate()!=16&&temp.getM_nRate()!=4&&temp.getM_nRate()!=1) {
							JOptionPane.showMessageDialog(null,"涓氬姟閫熺巼涓庢墍灞炲眰涓嶇鍚�","鎻愮ず",JOptionPane.INFORMATION_MESSAGE);
							System.out.println(" 涓氬姟閫熺巼涓庢墍灞炲眰閫熺巼涓嶇  ");
							success=false;
							return;
						}
						
						//鍒ゆ柇鎵�灞炲眰銆佸煙 wx
						
						LinkedList returnList=new LinkedList();
						algorithm.SetNodeRankID(nodeList);
						Route route=algorithm.RWAMain(temp.getM_nFromNode(),temp.getM_nToNode(),temp.getM_eLayer(),2,returnList);//2涓烘寜闀垮害							 				
						if(route!=null)
						temp.setM_cWorkRoute(route);//set宸ヤ綔璺敱
						algorithm.rmRepeat(temp,0);//firesky 2012 4.19
		 				
		 				if(route==null) {//濡傛灉宸ヤ綔璺敱閾捐矾涓虹┖锛屽垯閲婃斁璧勬簮锛岃矾鐢辫绠楀け璐�
							for(int n=0;n!=tnt-1;n++){
								trfList.get(n).releaseTraffic(trfList.get(n),0);//閲婃斁宸ヤ綔璺敱鍗犵敤璧勬簮
								trfList.get(n).getM_cWorkRoute().clearRoute();//娓呯┖宸ヤ綔璺敱
								if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){
									trfList.get(n).releaseTraffic(trfList.get(n),1);//閲婃斁淇濇姢璺敱鍗犵敤璧勬簮
									trfList.get(n).getM_cProtectRoute().clearRoute();//娓呯┖淇濇姢璺敱
									
								}
							}
							System.out.println("璺敱璁＄畻澶辫触");
							success=false;
							return;
						}
						if(!commonAlloc.ResourceAssignment(temp, 0)){//宸ヤ綔璺敱璧勬簮鍒嗛厤澶辫触鐨勬儏鍐�
							for(int n=0;n!=tnt-1;n++){
								trfList.get(n).releaseTraffic(trfList.get(n),0);
								trfList.get(n).getM_cWorkRoute().clearRoute();
								if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){
									trfList.get(n).releaseTraffic(trfList.get(n),1);
									trfList.get(n).getM_cProtectRoute().clearRoute();
									
								}
							}
							temp.getM_cWorkRoute().clearRoute();
							System.out.println("璧勬簮鍒嗛厤澶辫触");
							System.out.println("===========");
							success=false;
							return;
						}
		                if(temp.getM_eLevel().equals(TrafficLevel.PERMANENT11)||temp.getM_eLevel().equals(TrafficLevel.NORMAL11)){//濡傛灉涓氬姟鏈変繚鎶よ矾鐢憋紝鍒欓渶瑕佽�冭檻淇濇姢璺敱鐨勬儏鍐�
		                	DeleteSRLGList(route.getM_lSDHLinkList(),false);//鍒犻櫎srlg锛岄�氳繃璁剧疆鏉冮噸涓烘棤闄愬ぇ锛岃〃绀轰笉鍙埌杈�
		                	DeleteSRLGList(route.getM_lASONLinkList(),false);
		                	for (int f=0;f<temp.getM_cWorkRoute().getM_lASONLinkList().size();f++){//浠庡伐浣滆矾鐢遍摼璺腑鍙栧嚭閾捐矾锛岃缃湭婵�娲�
		                		SDHLink theLink = temp.getM_cWorkRoute().getM_lASONLinkList().get(f);
		                		theLink.setM_bStatus(false);//鍏堝皢宸ヤ綔璺敱鐨勯摼璺缃负鏈縺娲伙紝鏂逛究杩涗竴姝ヤ负淇濇姢璺敱绠楄矾
		                	}
		                	Route routeP=algorithm.RWAMain(temp.getM_nFromNode(),temp.getM_nToNode(),temp.getM_eLayer(),2,returnList);
		    				if(routeP!=null)
		    					temp.setM_cProtectRoute(routeP);
		    				algorithm.rmRepeat(temp,1);//firesky 2012 4.19
//		                	
		                	for (int f=0;f<temp.getM_cWorkRoute().getM_lASONLinkList().size();f++){
		                		SDHLink theLink= temp.getM_cWorkRoute().getM_lASONLinkList().get(f);
		                		theLink.setM_bStatus(true);//绠楄矾鍚庯紝浠庡伐浣滆矾鐢遍摼璺腑鍙栧嚭閾捐矾锛岃缃负婵�娲荤姸鎬�
		                	}
		                	
		                	if(routeP==null){//濡傛灉淇濇姢璺敱璁＄畻澶辫触
		                		for(int n=0;n!=tnt-1;n++){						
		    						trfList.get(n).releaseTraffic(trfList.get(n),0);
		    						trfList.get(n).getM_cWorkRoute().clearRoute();
		    						if(trfList.get(n).getM_eLevel().equals(TrafficLevel.PERMANENT11)||trfList.get(n).getM_eLevel().equals(TrafficLevel.NORMAL11)){
		    							trfList.get(n).releaseTraffic(trfList.get(n),1);
		    							trfList.get(n).getM_cProtectRoute().clearRoute();
		    							
		    						}
		    					}
		                		temp.releaseTraffic(temp,0);
		                		temp.getM_cWorkRoute().clearRoute();
		                		System.out.println("淇濇姢璺敱璁＄畻澶辫触");
		    					success=false;
		    					return;
		                	}
		                	if(!commonAlloc.ResourceAssignment(temp, 1)){//濡傛灉淇濇姢璺敱璁＄畻澶辫触
		                		for(int n=0;n!=tnt-1;n++){
		                			trfList.get(n).releaseTraffic(trfList.get(n),0);//閲婃斁宸ヤ綔璺敱銆佷繚鎶よ矾鐢便�佸苟娓呯┖閾捐矾
									trfList.get(n).releaseTraffic(trfList.get(n),1);
		                			trfList.get(n).getM_cWorkRoute().clearRoute();
									trfList.get(n).getM_cProtectRoute().clearRoute();
									
								}
		                		temp.releaseTraffic(temp,0);
		                		temp.getM_cWorkRoute().clearRoute();
								temp.getM_cProtectRoute().clearRoute();
								System.out.println("宸ヤ綔閾捐矾鎴愬姛锛屼繚鎶ら摼璺け璐�/n");
								System.out.println("===========");
								success=false;
								return;
		                	}
		                }
		                
		                System.out.println(" 涓氬姟-"+tnt+"   瀹屾垚 ");
					}
				}
				ResourceLogTxt.ResourceLogTxt();//灏嗚祫婧愬垎閰嶆棩蹇楀啓鍏xt涓� wx 3.17
			
	}
	public static boolean D(Network net,CommonNode from,CommonNode to,LinkedList returnlinklist){
		//鍒濆鍖�
		List<CommonNode> NodeList=new LinkedList<CommonNode>();
		List LinkList=new LinkedList();
		LinkedList<CommonNode> routeNodeList=new LinkedList<CommonNode>();//璁＄畻璺敱杩斿洖缁撴灉
		double[] NodeWeight=null;
		NodeList.clear();
		LinkList.clear();
		NodeList.addAll(net.getM_lInNetNodeList());
		if (trfLayer==Layer.WDM){
			LinkList.addAll(net.getM_lInNetWDMLinkList());
		}
//		if (trfLayer==Layer.ASON){
//			LinkList.addAll(net.getM_lInNetSDHLinkList());
//		}
		//鍘婚櫎鏈縺娲荤殑鑺傜偣
		Iterator<CommonNode> itNode=net.getM_lInNetNodeList().iterator();
		while(itNode.hasNext()){
			CommonNode theNode=itNode.next();
			if(!theNode.isM_bStatus())
				NodeList.remove(theNode);
		}

		//鍘婚櫎鏈縺娲荤殑閾捐矾
		Iterator<BasicLink>  itLink=LinkList.iterator();
		while(itLink.hasNext()){
			BasicLink theLink=itLink.next();
			double weight=theLink.getM_dLength();
			theLink.setM_fWeight((float)weight);
		}
		
		if(LinkList.isEmpty())
			return false;
		int nNodeSum = 0;
		int nNodeRankID=0;
		Iterator<CommonNode> it=NodeList.listIterator();
		 while(it.hasNext())
			{   
			 CommonNode theNode =it.next();
             theNode.setM_nRankID(nNodeRankID);//ID鍙蜂粠0寮�濮嬫寜椤哄簭鎺掑垪
             nNodeRankID++;
             nNodeSum++;
			}
		
		double[][] Adjacency = new double[nNodeSum][nNodeSum];
		for(int i=0; i<nNodeSum; i++)
    	{
    		for(int j=0; j<nNodeSum; j++)
    		{
    			if (i != j)
    			{
    				Adjacency[i][j] = algorithm.Infinity;
    			}
    			else
    			{
    				Adjacency[i][j] = 0;
    			}
    		}
    	}
		Iterator<BasicLink> that=LinkList.iterator();
		while(that.hasNext())
    	{
    		BasicLink thelink =that.next();
    		if (thelink.isM_bStatus() == true)
    		{
    			int fromID=thelink.getM_cFromNode().getM_nRankID();
    			int toID=thelink.getM_cToNode().getM_nRankID();
    			if (thelink.getM_fWeight()<Adjacency[fromID][toID]) 
    			{
    				Adjacency[fromID][toID]= thelink.getM_fWeight();
    			}
    			if (thelink.getM_fWeight()<Adjacency[toID][fromID]) 
    			{
    				Adjacency[toID][fromID]= thelink.getM_fWeight();
    			}
    		}
    	}
		//D绠楁硶锛屽緱鍒颁竴涓狿rev[]鏁扮粍琛ㄧず涓�鏉¤矾鐢�
    	double Weight_min = algorithm.Infinity;
    	int source = from.getM_nRankID();
    	int v=source;
    	boolean Found[] = new boolean[nNodeSum];//璁板綍鑺傜偣鏄惁琚爣璁�
    	double Distance[]=new double[nNodeSum];//璁板綍鑺傜偣鏉冮噸鍊硷紝鍗冲悇鑺傜偣鍒拌揪婧愯妭鐐圭殑鍙璺濈
    	int Prev[] = new int[nNodeSum];	//Prev[i]:i鐨勫墠椹辫妭鐐�
    	for(int i=0; i<nNodeSum; i++)
    	{
    		Found[i] = false;
    		Distance[i] = Adjacency[source][i];//鐩磋揪
    		Prev[i] = (Distance[i]<Weight_min) ? source : -1;	
    	}
    	Found[source] = true;	
    	Distance[source] = 0;
    	Prev[source] = -1;
    	
    	for(int i=0; i<nNodeSum; i++)
    	{
    		Weight_min = algorithm.Infinity;
    		for(int w=0; w<nNodeSum; w++)
    		{
    			if ((!Found[w])&&Distance[w]<Weight_min)   //閬嶅巻鍏ㄧ綉鏈爣璁扮殑鐐癸紙Found[w]==false锛�
    			{
    				v = w;
    				Weight_min = Distance[w];
    			}
    		}
    		Found[v]=true;
    		for(int w=0; w<nNodeSum; w++)                        //鏇存柊鑺傜偣鏉冮噸鍊�
    		{
    			if ((!Found[w])&&(Weight_min+Adjacency[v][w]<Distance[w]))
    			{
    				Distance[w] = Weight_min+Adjacency[v][w];
    				Prev[w] = v;
    			}
    		}
    	}
    	//D绠楁硶锛屽緱鍒颁竴涓狿rev[]鏁扮粍琛ㄧず涓�鏉¤矾鐢�
    	int dest = to.getM_nRankID();
    	routeNodeList.addFirst(to);
    	while (dest!=-1) 
    	{
    		int u = dest;
    		dest = Prev[dest];
    		if (dest!=-1)
    		{
    			//閬嶅巻閾捐矾锛屾牴鎹甈rev[]鍓嶉┍鑺傜偣鏁扮粍鎵惧埌绗﹀悎鐨勯摼璺紝鐢熸垚Path璺敱list
    			Iterator<BasicLink> itlink=LinkList.listIterator();
    			BasicLink pLink = new  BasicLink();
    			while(itlink.hasNext())
    	    	{    
    				BasicLink thelink =itlink.next();
    				if(thelink.isM_bStatus() == true) 
    				{
    					if(((thelink.getM_cFromNode().getM_nRankID() == u) && (thelink.getM_cToNode().getM_nRankID() == dest))
    						||((thelink.getM_cToNode().getM_nRankID() == u) && (thelink.getM_cFromNode().getM_nRankID() == dest)))
    					{
    						if(pLink.getM_cFromNode() == null)
    						{
    							pLink = thelink;
    						}
    						else 
    						{
    							if (thelink.getM_fWeight() < pLink.getM_fWeight())
    							{
    								pLink = thelink;
    							}
    						}
    					}
    				}
    			}
    			returnlinklist.addFirst(pLink);
    			if(routeNodeList.getFirst().equals(pLink.getM_cFromNode()))
    			{
    				routeNodeList.addFirst(pLink.getM_cToNode());
    			}
    			else if(routeNodeList.getFirst().equals(pLink.getM_cToNode()))
    			{
    				routeNodeList.addFirst(pLink.getM_cFromNode());
    			}
            }
    	}
    	if(!routeNodeList.getFirst().equals(from))
			return false;
		return true;
	}
	public static boolean DeleteSRLGList(List linkList,boolean ifSRLG)//????
	{
		if(linkList.isEmpty()){
			return false;
		}  
			
	    if(linkList.get(0).getClass().equals(WDMLink.class)){               //灏嗕笌宸ヤ綔璺敱鍚岀粍鐨勯摼璺潈閲嶈缃负infinity+1
				ListIterator<WDMLink> it=linkList.listIterator();
				while(it.hasNext())
				{   
					WDMLink theLink =it.next();		
					
					if(theLink.getId()==15){
//						璋冭瘯
						int a= 0;
					}
					if(theLink.getM_fWeight()!=Define.infinity){
						theLink.setM_fWeight(Define.infinity+1);//
					}
						if(ifSRLG){                                                   
							List<LinkRGroup> WDMLinkGroup = theLink.getM_lRelate();
							ListIterator<LinkRGroup> itGroup=WDMLinkGroup.listIterator();
							while(itGroup.hasNext()){
								LinkRGroup theGroup = itGroup.next();
								List<WDMLink> relateWDMlink =theGroup.getM_lWDMLinkList();
								ListIterator<WDMLink> itrelateWDMlink=relateWDMlink.listIterator();
								while(itrelateWDMlink.hasNext()){					
									WDMLink therelateWDMlink =itrelateWDMlink.next();
									if(therelateWDMlink.getM_fWeight()!=Define.infinity){
										therelateWDMlink.setM_fWeight(Define.infinity+1);
									}
								}
							}		
							}	
					
					else{}
				}
				return true;
			}  
			
			else if(linkList.get(0).getClass().equals(SDHLink.class)){               //灏嗕笌宸ヤ綔璺敱鍚岀粍鐨勯摼璺潈閲嶈缃负infinity+1
				ListIterator<SDHLink> it=linkList.listIterator();
				while(it.hasNext())
				{   
					SDHLink theLink =it.next();		
					if(theLink.getM_fWeight()!=Define.infinity){
						theLink.setM_fWeight(Define.infinity+1);
						if(ifSRLG){  //灏嗕笌宸ヤ綔璺敱鍚岀粍鐨勯摼璺潈閲嶈缃负infinity+1
							if(SDHLink.s_lSDHLinkList.contains(theLink)){
							List<LinkRGroup> SDHLinkGroup = theLink.getM_lRelate();
							ListIterator<LinkRGroup> itGroup=SDHLinkGroup.listIterator();
							while(itGroup.hasNext()){
								LinkRGroup theGroup = itGroup.next();
								List<SDHLink> relateSDHlink =theGroup.getM_lSDHLinkList();
								ListIterator<SDHLink> itrelateSDHlink=relateSDHlink.listIterator();
								while(itrelateSDHlink.hasNext()){					
									SDHLink therelateSDHlink =itrelateSDHlink.next();
									if(therelateSDHlink.getM_fWeight()!=Define.infinity){
										therelateSDHlink.setM_fWeight(Define.infinity+1);
									}
									}
								}
							}	
							else if(SDHLink.s_lASONLinkList.contains(theLink)){
								List<LinkRGroup> ASONLinkGroup = theLink.getM_lRelate();
								ListIterator<LinkRGroup> itGroup=ASONLinkGroup.listIterator();
								while(itGroup.hasNext()){
									LinkRGroup theGroup = itGroup.next();
									List<SDHLink> relateASONlink =theGroup.getM_lASONLinkList();
									ListIterator<SDHLink> itrelateASONlink=relateASONlink.listIterator();
									while(itrelateASONlink.hasNext()){					
										SDHLink therelateASONlink =itrelateASONlink.next();
										if(therelateASONlink.getM_fWeight()!=Define.infinity){
											therelateASONlink.setM_fWeight(Define.infinity+1);
										}
										}
									}
								}	
							
							}	
				}
					else{}
				}
				return true;
			}  
			else{
			return false;}
		}
	static void findLinkNumber(int setlimit,int uselimit){//鍒濆鍖栬绠楅摼璺暟鐩紝姣�72鏉″崰鐢ㄧ殑娉㈤亾鍒嗘垚涓�鏉￠摼璺紒锛佸垰寮�濮嬮粯璁ら摼璺尝閬撴暟鐩负500鎰忎负寰堝ぇ
//          for (int i1 = 0; i1 < linkList.size(); i1++) {
//        	      if (linkList.get(i1).getClass().equals(WDMLink.class)) {
//        	    	  WDMLink temp=(WDMLink) linkList.get(i1);
//      				int linkSum=0;
//      				int nUsed=0;
//      				for (int j = 0; j != temp.getM_nSize(); j++) {//閬嶅巻閾捐矾搴曞眰鐨勬尝閬� temp.getM_nsize=500				
//      					if(temp.getM_lWavelength(j).getM_eStatus() == Status.宸ヤ綔)
//      					    nUsed++;	//璁＄畻姣忔潯閾捐矾鐨勬尝閬撲娇鐢ㄦ暟鐩�				
//      				}
//      				linkUsed.add(nUsed);//姣忔潯閾捐矾浣跨敤鐨勬尝閬撴暟瀛樺叆linkused閾捐矾涓�
//      				//wx 寰呬慨鏀� 鍗曠嚎閰嶇疆涓婇檺鍜屼娇鐢ㄤ笂闄�.鐢ㄦ埛璁剧疆鍗曠氦浣跨敤涓婇檺銆倁seLimit 锛侊紒
//      				linkSum = (int) Math.ceil((double)nUsed / (double)uselimit);//鍚戜笂鍙栨暣銆備负浠�涔堥櫎浠�72锛燂紵锛燂紵濡傛灉涓�鏉￠摼璺崰鐢ㄦ尝閬撴暟瓒呰繃72锛屽垯姣�72鏉℃尝閬撳垎涓�鏉￠摼璺紒锛佽绠楅摼璺暟鐩紒
//      				if(nUsed==0){//杩欓噷linkNumlist涓瓨鐨勬暟鏄摼璺暟鐩紝1琛ㄧず1鏉￠摼璺紝2琛ㄧず2鏉￠摼璺紝鏍规嵁鍗犵敤娉㈤亾鏁扮洰鏉ュ垎
//      					linkNumList.add(1);//濡傛灉鍗犵敤娉㈤亾鏁扮洰涓�0锛屽垯閾捐矾鏁扮洰涓�1
//      				}
//      				else linkNumList.add(linkSum);//瀛橀摼璺暟鐩�
//      			
//      			int linkSizeInitial = linkList.size();//28鏉￠摼璺�
//      			      			
//      			for (int i=0; i!=linkSizeInitial; i++){//寮�濮嬮粯璁や负鍏ㄨ繛鎺ョ綉缁滐紝鏍规嵁鍗犵敤娉㈤亾鏄惁鍗犳弧锛岃绠楁槸鍚﹂渶瑕佹柊鍔犻摼璺�
//      				WDMLink tempLink = ((WDMLink)(linkList.get(i)));
//      				//int wlSize = 72;//姣忔潯閾捐矾72涓尝閬�
//      				int wlSize = uselimit;//姣忔潯閾捐矾娉㈤亾鏁扮洰涓哄崟绾や娇鐢ㄤ笂闄愭暟鐩�				
//      				tempLink.resizeWaveLengthList(setlimit);//娓呯┖鍓╀綑娉㈤亾
//      				
//                      tempLink.setM_nSize(setlimit);//璁剧疆姣忔潯閾捐矾搴曞眰浣庣矑搴︿负72  璁剧疆閰嶇疆涓婇檺 80
//                      if (linkUsed.get(i)<wlSize)//濡傛灉閾捐矾鍗犵敤娉㈤亾鏁板皬浜�72锛岃〃绀烘湭鍗犳弧锛屽垎涓�鏉￠摼璺�傝缃摼璺墿浣欒祫婧愪负72-鎵�鐢ㄦ尝閬撴暟
//                          tempLink.setM_nFree(setlimit-linkUsed.get(i));//绌洪棽娉㈤亾鏁扮洰涓�80-used
//                      else//濡傛灉閾捐矾鍗犵敤娉㈤亾鏁扮洰澶т簬72锛屽垯绗竴鏉￠摼璺崰婊★紝鍒嗗墿浣欑殑閾捐矾
//                      	tempLink.setM_nFree(0);
//                      for(int j=0;j<linkNumList.get(i)-1;j++)//濡傛灉鍗犵敤娉㈤亾鏁扮洰澶т簬72锛屽垯绗竴鏉￠摼璺崰婊★紝鎺ョ潃鍒嗙浜屾潯銆佺涓夋潯閾捐矾
//                      {
//                      	WDMLink insertNewLink = getNewWDMLink(tempLink.getM_cFromNode(), tempLink.getM_cToNode(),j+1);
//                      	insertNewLink.resizeWaveLengthList(setlimit);
//                      	insertNewLink.setM_nSize(setlimit);//璁剧疆娉㈤亾涓�80涓� 閰嶇疆涓婇檺
//                      	if (j==linkNumList.get(i)-2)//濡傛灉鏄涓氬姟鍗犵敤鐨勬渶鍚庝竴涓尝閬擄紝鍒欒缃墿浣欒祫婧�
//                      	    insertNewLink.setM_nFree(linkNumList.get(i)*setlimit-linkUsed.get(i)-(setlimit-uselimit)*(linkNumList.get(i)-1));
//                      	else
//                      		insertNewLink.setM_nFree(setlimit-uselimit);
//                      	linkList.add(insertNewLink);
//                      }
//      			}
//      			//net.setM_lInNetWDMLinkList(linkList);
//      			WDMLink.setS_lWDMLinkList(linkList);
//      			break;
//				}
//        	  else if ((linkList.get(i1).getClass().equals(SDHLink.class))) {
//        		  SDHLink temp = (SDHLink) linkList.get(i1);
//  				int nUsed = 0;
//  				for (int j = 0; j != temp.getM_nSize(); j++) {				
//  					if(temp.getM_lTimeslotList().get(j).getM_eStatus() == Status.宸ヤ綔)
//  					    nUsed++;					
//  				}
//  				linkUsed.add(nUsed);
//  				int nLinkNum = (int) Math.ceil((double)nUsed / (double)uselimit);//wx 64鏀逛负uselimit
//  				
//  				if (nUsed==0){
//  					linkNumList.add(1);
//  				}
//  				else linkNumList.add(nLinkNum);
//  			
//  			int k = linkList.size();
//  			for(int i=0;i!=k;i++)
//  			{
//  				SDHLink tempLink = ((SDHLink)(linkList.get(i)));
//  				tempLink.setM_nRate(10);
//                  tempLink.setM_nSize(setlimit);//wx
//                  tempLink.getM_lTimeslotList().clear();
//                  int h=0;
//          		while(h<tempLink.getM_nSize()){
//          			Timeslot tl=new Timeslot(h,tempLink);
//          			tempLink.getM_lTimeslotList().add(tl);
//          			++h;
//          		}
//                  if(linkUsed.get(i)<uselimit)
//                      tempLink.setM_nFree(setlimit-linkUsed.get(i)); 
//                  else
//                  	tempLink.setM_nFree(0);
//          		
//                  for(int j=0;j<linkNumList.get(i)-1;j++)
//                  {
//                  	SDHLink insertNewLink = getNewSDHLink(tempLink.getM_cFromNode(), tempLink.getM_cToNode(),j+1);
//                  	insertNewLink.setM_nSize(setlimit);
//                  	insertNewLink.getM_lTimeslotList().clear();
//                  	int e=0;
//              		while(e<insertNewLink.getM_nSize()){
//              			Timeslot tl=new Timeslot(e,insertNewLink);
//              			insertNewLink.getM_lTimeslotList().add(tl);
//              			++e;
//              		}
//                  	insertNewLink.setM_nRate(10);
//                  	
//                  	if (j==linkNumList.get(i)-2)
//                  	    insertNewLink.setM_nFree(linkNumList.get(i)*setlimit-linkUsed.get(i)-(setlimit-uselimit)*(linkNumList.get(i)-1));
//                  	else
//                  		insertNewLink.setM_nFree(setlimit-uselimit);
//                      linkList.add(insertNewLink);
//                  }
//  			}
//  			//net.setM_lInNetSDHLinkList(linkList); wx 2014.1.1
//  			SDHLink.setS_lSDHLinkList(linkList);
//  			break;
//  			
//				}	
//            }
	
		switch(trfLayer){
		case WDM:
			for (int i=0;i!=linkList.size();i++){
				WDMLink temp=(WDMLink) linkList.get(i);
				int linkSum=0;
				int nUsed=0;
				for (int j = 0; j != temp.getSize(); j++) {//閬嶅巻閾捐矾搴曞眰鐨勬尝閬� temp.getM_nsize=500				
					if(temp.getM_lWavelength(j).getM_eStatus() == Status.宸ヤ綔)
					    nUsed++;	//璁＄畻姣忔潯閾捐矾鐨勬尝閬撲娇鐢ㄦ暟鐩�				
				}
				linkUsed.add(nUsed);//姣忔潯閾捐矾浣跨敤鐨勬尝閬撴暟瀛樺叆linkused閾捐矾涓�
				//wx 寰呬慨鏀� 鍗曠嚎閰嶇疆涓婇檺鍜屼娇鐢ㄤ笂闄�.鐢ㄦ埛璁剧疆鍗曠氦浣跨敤涓婇檺銆倁seLimit 锛侊紒
				linkSum = (int) Math.ceil((double)nUsed / (double)uselimit);//鍚戜笂鍙栨暣銆備负浠�涔堥櫎浠�72锛燂紵锛燂紵濡傛灉涓�鏉￠摼璺崰鐢ㄦ尝閬撴暟瓒呰繃72锛屽垯姣�72鏉℃尝閬撳垎涓�鏉￠摼璺紒锛佽绠楅摼璺暟鐩紒
				if(nUsed==0){//杩欓噷linkNumlist涓瓨鐨勬暟鏄摼璺暟鐩紝1琛ㄧず1鏉￠摼璺紝2琛ㄧず2鏉￠摼璺紝鏍规嵁鍗犵敤娉㈤亾鏁扮洰鏉ュ垎
					linkNumList.add(1);//濡傛灉鍗犵敤娉㈤亾鏁扮洰涓�0锛屽垯閾捐矾鏁扮洰涓�1
				}
				else linkNumList.add(linkSum);//瀛橀摼璺暟鐩�
			}
			int linkSizeInitial = linkList.size();//28鏉￠摼璺�
			
			
			for (int i=0; i!=linkSizeInitial; i++){//寮�濮嬮粯璁や负鍏ㄨ繛鎺ョ綉缁滐紝鏍规嵁鍗犵敤娉㈤亾鏄惁鍗犳弧锛岃绠楁槸鍚﹂渶瑕佹柊鍔犻摼璺�
				WDMLink tempLink = ((WDMLink)(linkList.get(i)));
				//int wlSize = 72;//姣忔潯閾捐矾72涓尝閬�
				int wlSize = uselimit;//姣忔潯閾捐矾娉㈤亾鏁扮洰涓哄崟绾や娇鐢ㄤ笂闄愭暟鐩�				
				tempLink.resizeWaveLengthList(setlimit);//娓呯┖鍓╀綑娉㈤亾
				
                tempLink.setM_nSize(setlimit);//璁剧疆姣忔潯閾捐矾搴曞眰浣庣矑搴︿负72  璁剧疆閰嶇疆涓婇檺 80
                if (linkUsed.get(i)<wlSize)//濡傛灉閾捐矾鍗犵敤娉㈤亾鏁板皬浜�72锛岃〃绀烘湭鍗犳弧锛屽垎涓�鏉￠摼璺�傝缃摼璺墿浣欒祫婧愪负72-鎵�鐢ㄦ尝閬撴暟
                    tempLink.setM_nFree(setlimit-linkUsed.get(i));//绌洪棽娉㈤亾鏁扮洰涓�80-used
                else//濡傛灉閾捐矾鍗犵敤娉㈤亾鏁扮洰澶т簬72锛屽垯绗竴鏉￠摼璺崰婊★紝鍒嗗墿浣欑殑閾捐矾
                	tempLink.setM_nFree(0);
                for(int j=0;j<linkNumList.get(i)-1;j++)//濡傛灉鍗犵敤娉㈤亾鏁扮洰澶т簬72锛屽垯绗竴鏉￠摼璺崰婊★紝鎺ョ潃鍒嗙浜屾潯銆佺涓夋潯閾捐矾
                {
                	WDMLink insertNewLink = getNewWDMLink(tempLink.getM_cFromNode(), tempLink.getM_cToNode(),j+1);
                	insertNewLink.resizeWaveLengthList(setlimit);
                	insertNewLink.setM_nSize(setlimit);//璁剧疆娉㈤亾涓�80涓� 閰嶇疆涓婇檺
                	if (j==linkNumList.get(i)-2)//濡傛灉鏄涓氬姟鍗犵敤鐨勬渶鍚庝竴涓尝閬擄紝鍒欒缃墿浣欒祫婧�
                	    insertNewLink.setM_nFree(linkNumList.get(i)*setlimit-linkUsed.get(i)-(setlimit-uselimit)*(linkNumList.get(i)-1));
                	else
                		insertNewLink.setM_nFree(setlimit-uselimit);
                	linkList.add(insertNewLink);
                }
			}
			//net.setM_lInNetWDMLinkList(linkList);
			WDMLink.setS_lWDMLinkList(linkList);
			break;
		case ASON:
			for (int i=0;i!=linkList.size();i++){
				SDHLink temp = (SDHLink) linkList.get(i);
				int nUsed = 0;
				for (int j = 0; j != temp.getM_nSize(); j++) {				
					if(temp.getM_lTimeslotList().get(j).getM_eStatus() == Status.宸ヤ綔)
					    nUsed++;					
				}
				linkUsed.add(nUsed);
				int nLinkNum = (int) Math.ceil((double)nUsed / (double)uselimit);//wx 64鏀逛负uselimit
				
				if (nUsed==0){
					linkNumList.add(1);
				}
				else linkNumList.add(nLinkNum);
			}
			int k = linkList.size();
			for(int i=0;i!=k;i++)
			{
				SDHLink tempLink = ((SDHLink)(linkList.get(i)));
				tempLink.setM_nRate(10);
                tempLink.setM_nSize(setlimit);//wx
                tempLink.getM_lTimeslotList().clear();
                int h=0;
        		while(h<tempLink.getM_nSize()){
        			Timeslot tl=new Timeslot(h,tempLink);
        			tempLink.getM_lTimeslotList().add(tl);
        			++h;
        		}
                if(linkUsed.get(i)<uselimit)
                    tempLink.setM_nFree(setlimit-linkUsed.get(i)); 
                else
                	tempLink.setM_nFree(0);
        		
                for(int j=0;j<linkNumList.get(i)-1;j++)
                {
                	SDHLink insertNewLink = getNewASONLink(tempLink.getM_cFromNode(), tempLink.getM_cToNode(),j+1);
                	insertNewLink.setM_nSize(setlimit);
                	insertNewLink.getM_lTimeslotList().clear();
                	int e=0;
            		while(e<insertNewLink.getM_nSize()){
            			Timeslot tl=new Timeslot(e,insertNewLink);
            			insertNewLink.getM_lTimeslotList().add(tl);
            			++e;
            		}
                	insertNewLink.setM_nRate(10);
                	
                	if (j==linkNumList.get(i)-2)
                	    insertNewLink.setM_nFree(linkNumList.get(i)*setlimit-linkUsed.get(i)-(setlimit-uselimit)*(linkNumList.get(i)-1));
                	else
                		insertNewLink.setM_nFree(setlimit-uselimit);
                    linkList.add(insertNewLink);
                }
			}
			//net.setM_lInNetSDHLinkList(linkList); wx 2014.1.1
			//SDHLink.setS_lSDHLinkList(linkList); wx 3.4
			SDHLink.setS_lASONLinkList(linkList);
			break;
		default:
			return;
		}
	}
	private static int getNodeDegree(CommonNode NodeIn)
	{ 
		int degree = 0;
		for (int j = 0; j != linkList.size(); ++j) {
			BasicLink aLink = (BasicLink) linkList.get(j);
			if ((aLink.getM_cFromNode().getId() == NodeIn.getId())
					|| aLink.getM_cToNode().getId() == NodeIn.getId()) {
				degree++;
			}
		}
		return degree;
	}
	private static double findNetUtil(){//璁＄畻閾捐矾鍒╃敤鐜�
		double avgNet = 0.0f;
		double dUtil = 0.0f;
		SDHLink.reflashUtilization();
		WDMLink.reflashUtilization();
		if (trfLayer==Layer.WDM){
			int dLamdaSum = 0;
			for (int i = 0; i != linkList.size(); i++){
				WDMLink aLink = (WDMLink) linkList.get(i);
				double temp=0;
				for (int j = 0; j != aLink.getM_nSize(); j++){//閬嶅巻姣忔潯閾捐矾鐨勬墍鏈夋尝閬擄紝璁＄畻姣忔潯閾捐矾鐨勬尝閬撳崰鐢ㄦ儏鍐�
					temp = (double)(aLink.getM_nRate() - aLink.getM_lWavelength(j).getM_nFree());
					dUtil += temp;
				}
				dLamdaSum+=aLink.getM_nSize();//鎵�鏈夐摼璺殑娉㈤亾涔嬪拰
				utilList.add((double)aLink.getM_dUtilization());
			}
			avgNet = dUtil/(100*dLamdaSum);//鍏ㄧ綉骞冲潎鍒╃敤鐜�		
		}
		if (trfLayer==Layer.ASON){
			for (int i = 0; i != linkList.size(); i++) {
				SDHLink link = (SDHLink) linkList.get(i);
				utilList.add((double)link.getM_dUtilization());			
				avgNet += (double)link.getM_dUtilization();
			}
			avgNet = avgNet/(double)linkList.size();
		}
		System.out.println("====================");
		System.out.println("====================");
		System.out.println("鍏ㄧ綉骞冲潎鍒╃敤鐜囷細"+avgNet);
		System.out.println("====================");
		System.out.println("====================");
		return avgNet;
	}
	private static void initialize(){
	    utilList.clear();//閾捐矾鍒╃敤鐜囨竻绌�
	    for(int i=0;i<Traffic.s_lWDMTrafficList.size();i++){//閲婃斁宸ヤ綔璺敱鍜屼繚鎶よ矾鐢�
			Traffic.s_lWDMTrafficList.get(i).releaseTraffic(Traffic.s_lWDMTrafficList.get(i), 0);
			Traffic.s_lWDMTrafficList.get(i).releaseTraffic(Traffic.s_lWDMTrafficList.get(i), 1);
		}	
	    for(int i=0;i<Traffic.s_lASONTrafficList.size();i++){//wx 3.4
			Traffic.s_lASONTrafficList.get(i).releaseTraffic(Traffic.s_lASONTrafficList.get(i), 0);
			Traffic.s_lASONTrafficList.get(i).releaseTraffic(Traffic.s_lASONTrafficList.get(i), 1);
		}
	    for(int i=0; i!=trfList.size(); i++){
	    	Traffic temp= trfList.get(i);
	    	temp.getM_cWorkRoute().clearRoute();
			temp.getM_cProtectRoute().clearRoute();
	    }
    }
	private static double getMinUtil(){//瀵绘壘鍏ㄧ綉鍒╃敤鐜囨渶浣庣殑閾捐矾
		double minRate = 1.0;
		for (int i = 0; i != utilList.size(); i++) {
		if(minRate>(Double) utilList.get(i)){
			minRate = (Double) utilList.get(i);
		}
		}
		return minRate;
	}
	static void findLink(){//瀵绘壘寰呭垹闄ょ殑閾捐矾锛岄摼璺埄鐢ㄧ巼鏈�浣庝腑鏈�闀跨殑閭ｆ潯
		utilList.clear();//娓呯┖鍒╃敤鐜囬摼璺�
		findNetUtil();//璁＄畻鍏ㄧ綉骞冲潎鍒╃敤鐜� 鍜屾瘡鏉￠摼璺殑鍒╃敤鐜� utilList閾捐矾閲岄潰閲嶆柊瀛樺叆鍚勬潯閾捐矾鐨勫埄鐢ㄧ巼
		double min=1.0;
		int longest=0;
		double length=1;
		boolean there=false;
		List temp = new LinkedList();
		for (int i=0;i!=utilList.size();i++){//瀵绘壘鏈�浣庡埄鐢ㄧ巼锛屾斁鍏in涓�
			if (utilList.get(i)<min){
				for (int j=0;j!=linkcantdel.size();j++){
					if (linkList.get(i)==linkcantdel.get(j)){
						there=true;
						break;
					}
					else
						continue;
				}
				if (!there)
					min=utilList.get(i);
				there=false;
			}
		}
		for (int i=0;i!=utilList.size();i++){
			if (utilList.get(i)==min){//鎵惧埌鍒╃敤鐜囨渶灏忕殑閾捐矾锛屾斁鍏emp閾捐矾涓�
				for (int j=0;j!=linkcantdel.size();j++){
					if (linkList.get(i)==linkcantdel.get(j)){
						there=true;
						break;
					}
					else
						continue;
				}
				if (!there)
					temp.add( linkList.get(i));
				there=false;
			}
		}
		for (int i=0;i!=temp.size();i++){//瀵绘壘鍒╃敤鐜囨渶浣庣殑閾捐矾涓殑鏈�闀块摼璺�
			if (( (BasicLink) temp.get(i)).getM_dLength()>length){
				length=((BasicLink) temp.get(i)).getM_dLength();
				longest=i;//璁板綍搴忓彿
			}
		}
		linktodel= temp.get(longest);
		linktodel_utl=min;//wx璁板綍寰呭垹闄ら摼璺埄鐢ㄧ巼
	}		
	
	static void changeNet(){
		switch(trfLayer){
		case WDM:
			//net.setM_lInNetWDMLinkList(linkList);
			WDMLink.setS_lWDMLinkList(linkList);
			break;
			
//		case ASON:
//			//net.setM_lInNetSDHLinkList(linkList);
//			//SDHLink.setS_lSDHLinkList(linkList);wx 3.4
//			SDHLink.setS_lASONLinkList(linkList);
//			break;
			default:
				return;
		}
	}
	
	
}
*/