package dialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;


/** 
 * ������ʾRowHeader��JTable��ֻ��Ҫ�������JScrollPane��RowHeaderView����ΪJTable�����б��� 
 */  

public class RowHeaderTable extends JTable  
{  
    private JTable refTable;//��Ҫ���rowHeader��JTable  
    /** 
     * ΪJTable���RowHeader�� 
     * @param refTable ��Ҫ���rowHeader��JTable   
     * @param columnWideth rowHeader�Ŀ�� 
     */  
    public RowHeaderTable(JTable refTable,int columnWidth){  
        super(new RowHeaderTableModel(refTable.getRowCount()));  
        this.refTable=refTable;  
        this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//�����Ե����п�  
        this.getColumnModel().getColumn(0).setPreferredWidth(columnWidth);  
        this.setRowHeight(refTable.getRowHeight());  
        this.setDefaultRenderer(Object.class,new RowHeaderRenderer(refTable,this));//������Ⱦ��  
        this.setPreferredScrollableViewportSize (new Dimension(columnWidth,0));  
    }  
}  
/** 
 * ������ʾRowHeader��JTable����Ⱦ��������ʵ�ֶ�̬���ӣ�ɾ���У���Table�����ӡ�ɾ����ʱRowHeader 
 * һ��仯����ѡ��ĳ��ʱ��������ɫ�ᷢ���仯 
 */  
class RowHeaderRenderer extends JLabel implements TableCellRenderer,ListSelectionListener  
{  
    JTable reftable;//��Ҫ���rowHeader��JTable  
    JTable tableShow;//������ʾrowHeader��JTable  
    public RowHeaderRenderer(JTable reftable,JTable tableShow)  
    {  
        this.reftable = reftable;  
        this.tableShow=tableShow;  
        //���Ӽ�������ʵ�ֵ���reftable��ѡ����ʱ��RowHeader�ᷢ����ɫ�仯  
        ListSelectionModel listModel=reftable.getSelectionModel();  
        listModel.addListSelectionListener(this);   
    }  
    public Component getTableCellRendererComponent(JTable table,Object obj,  
        boolean isSelected,boolean hasFocus,int row, int col)  
    {  
        ((RowHeaderTableModel)table.getModel()).setRowCount(reftable.getRowCount());  
        JTableHeader header = reftable.getTableHeader();  
        this.setOpaque(true);  
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));//����ΪTableHeader�ı߿�����  
        setHorizontalAlignment(CENTER);//��text������ʾ  
        setBackground(header.getBackground());//���ñ���ɫΪTableHeader�ı���ɫ    
        if ( isSelect(row) )    //��ѡȡ��Ԫ��ʱ,��row header�����ó�ѡȡ��ɫ   
        {  
            setForeground(Color.white);  
            setBackground(Color.lightGray);  
        }  
        else  
        {  
            setForeground(header.getForeground());     
        }  
        setFont(header.getFont());  
        setText(String.valueOf(row+1));  
        return this;  
    }  
    public void valueChanged(ListSelectionEvent e){  
        this.tableShow.repaint();  
    }  
    private boolean isSelect(int row)  
    {  
        int[] sel = reftable.getSelectedRows();  
        for ( int i=0; i<sel.length; i++ )  
            if (sel[i] == row )   
                return true;  
        return false;  
    }  
}  
  
/** 
 * ������ʾ��ͷRowHeader��JTable��TableModel����ʵ�ʴ洢���� 
 */  
class RowHeaderTableModel extends AbstractTableModel  
{  
    private int rowCount;//��ǰJTable������������Ҫ��RowHeader��TableModelͬ��  
    public RowHeaderTableModel(int rowCount){  
        this.rowCount=rowCount;  
    }  
    public void setRowCount(int rowCount){  
        this.rowCount=rowCount;  
    }  
    public int getRowCount(){  
        return rowCount;  
    }  
    public int getColumnCount(){  
        return 1;  
    }  
    public Object getValueAt(int row, int column){  
        return row;  
    }  
}

