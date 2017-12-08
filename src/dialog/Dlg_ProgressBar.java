package dialog;

import javax.swing.*;
import javax.swing.plaf.ProgressBarUI;

import netdesigntitle.NSTitleFrame;

import NSUI.NSProgressBarUI;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Observable;
import java.util.Observer;

public class Dlg_ProgressBar extends JFrame {
	private static final ActionListener ActionListener = null;
    private final JProgressBar PBar = new JProgressBar(0, 100);
//    private JButton button;
    private JLabel show_JL;
    private JLabel space;

	public Dlg_ProgressBar(){
		setTitle("运行中。。");
		ImageIcon imageIcon = new ImageIcon(getClass().getResource("/resource/titlexiao.png"));
		setIconImage(imageIcon.getImage());
		
//	    PBar.setForeground(new Color(98,183,253));
	    PBar.setUI((ProgressBarUI)NSProgressBarUI.createUI(PBar));
//	    PBar.setSize(100, 20);
        show_JL = new JLabel();
        space = new JLabel(" ");
        this.setBackground(new Color(234,247,208));
        this.setResizable(false);
        show_JL.setFont(new java.awt.Font("微软雅黑", Font.PLAIN, 13));
//	    button = new JButton("隐藏");

//	    button.addActionListener(new ActionListener() {
//		      public void actionPerformed(ActionEvent e) {		    	  
//		    		setVisible(false);
//		    		dispose();
//		      }
//	    });
        se.addObserver(new setObserver());
//        setModal(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	    addWindowListener(new WindowAdapter(){
	    	public void windowClosing(WindowEvent e){
	            setVisible(false);
//	            return;
	            dispose();
	    	}
	    });

//	    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    Container contentPane = getContentPane();
	    contentPane.setBackground(new Color(234,247,208));
	    contentPane.add(PBar, BorderLayout.CENTER);
	    contentPane.add(show_JL, BorderLayout.NORTH);
	    contentPane.add(space, BorderLayout.SOUTH);
	    setSize(300, 90);
        this.setLocationRelativeTo(null);
	}
	
	set se = new set();
	
    public void setVal(int i) {
        se.setPrice(i);
    }
    
    public void setIndeterminate(){
    	PBar.setIndeterminate(true);
    }
    
    class set extends Observable {
        private int price;

        public int getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
            setChanged();
            notifyObservers(new Integer(price));
        }
    }

    class setObserver implements Observer {
        public void update(Observable obj, Object arg) {
            if (arg instanceof Integer) {
                int i = ((Integer) arg).intValue();
                PBar.setValue(i);
                show_JL.setText("             处理中，请稍候");
                if(i>=100) {
                	setVisible(false);
                	dispose();
                }
            }
        }

    }
    
    public static void main(String[] args){
    	Dlg_ProgressBar pro = new Dlg_ProgressBar();
      	pro.setIndeterminate();
      	pro.setVal(0);
      	pro.setVisible(true);
    }
}
