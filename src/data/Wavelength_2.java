package data;

//import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import enums.Status;
import enums.WaveType;

public class Wavelength_2  {
	
    private int m_nID; // �������
    private Status m_eStatus = Status.����; // ����ʹ��״̬ ����,����,����,�ָ�
    private WaveType m_eType; // �������ͣ�ԭ�У����ݣ��½�
    private int m_nYear; // �ò����滮���
    private WDMLink m_cHolder = null; // ����������·
    private int m_nCaption; // ����������ҵ����
    private int m_nFree; // ����ʣ��ɳ���ҵ����
    private int m_nGran; // ���õȼ�
    private List<Traffic> m_lTraffic = new LinkedList<Traffic>(); // ����ҵ��
    //private List<SDHLink> m_lCarriedLink = new LinkedList<SDHLink>(); // ���ص�SDH��ASON
//    private WaveInfo m_cFrom = null; // ?????????????????
//    private WaveInfo m_cTo = null;
	

    //private static final long serialVersionUID = 4830295077379904964L;

    public Wavelength_2(WDMLink holder, int id, int rate) {
	m_nID = id;
	m_cHolder = holder;
	m_nCaption = m_nFree = rate;

//	m_cFrom = new WaveInfo(m_cHolder.getM_cFromNode());
//	m_cTo = new WaveInfo(m_cHolder.getM_cToNode());
    }

   
    // ???????????????
    public int getM_nID() {
	return m_nID;
    }

    public void setM_nID(int mNID) {
	m_nID = mNID;
    }

    public Status getM_eStatus() {
	return m_eStatus;
    }

    public void setM_eStatus(Status mEStatus) {
	m_eStatus = mEStatus;
    }

    public WaveType getM_eType() {
	return m_eType;
    }

    public void setM_eType(WaveType mEType) {
	m_eType = mEType;
    }

    public int getM_nYear() {
	return m_nYear;
    }

    public void setM_nYear(int mNYear) {
	m_nYear = mNYear;
    }

    public WDMLink getM_cHolder() {
	return m_cHolder;
    }

    public void setM_cHolder(WDMLink mCHolder) {
	m_cHolder = mCHolder;
    }

    public int getM_nCaption() {
	return m_nCaption;
    }

    public void setM_nCaption(int mNCaption) {
	m_nCaption = mNCaption;
    }

    public int getM_nFree() {
	// if(m_cHolder.getM_nRate()==100){
	// if((m_nCaption-m_nFree)/10.0>9){
	// return 0;
	// }
	// }
	return m_nFree;
    }

    public void setM_nFree(int mNFree) {
	m_nFree = mNFree;
    }

    public int getM_nGran() {
	return m_nGran;
    }

    public void setM_nGran(int mNGran) {
	m_nGran = mNGran;
    }

    public List<Traffic> getM_lTraffic() {
	return m_lTraffic;
    }

    public void setM_lTraffic(List<Traffic> mLTraffic) {
	m_lTraffic = mLTraffic;
    }

    public void delTraffic(Traffic tr) {
	m_lTraffic.remove(tr);

    }

    public void addTraffic(Traffic e) {
	m_lTraffic.add(e);
    }

}
