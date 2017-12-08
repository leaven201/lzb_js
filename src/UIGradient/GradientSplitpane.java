package UIGradient;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class GradientSplitpane extends JSplitPane{
    private Color paneColor;
 

	public GradientSplitpane(int i ,JPanel a,JPanel b,Color c) { 
        super(i,a,b); 
        this.paneColor = c;
    }
	
   
	public void paintComponent(Graphics g) { 
        super.paintComponent(g); 
        if (!isOpaque()) { 
            return; 
        } 
    	
        Rectangle bounds = this.getBounds();
        float x = (float)bounds.getX();
        float y = (float)bounds.getY();
        int width = getWidth(); 
        int height = getHeight(); 
        Color c = Color.black;
        Graphics2D g2 = (Graphics2D) g; 
//        Paint storedPaint = g2.getPaint(); 
        g2.setPaint(new GradientPaint(x, y, paneColor, x, y+height,paneColor)); 
        g2.fillRect((int)x,(int)y, width, height);  
//        g2.setPaint(storedPaint); 
    } 
	
	public static void main(String[] args) {
//        try {
////在此处必须先指定样式
//            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Color splitColor = new Color(106,182,234);
//        UIDefaults uidefs = UIManager.getLookAndFeelDefaults();
//
////这句就是设置分割条的颜色了，此处设置为绿色，大家可以根据自己的需要设置。
//        uidefs.put("SplitPane.background", new ColorUIResource(splitColor));
 
        JFrame frame = new JFrame("test");
        frame.setContentPane(new GradientSplitpane(JSplitPane.HORIZONTAL_SPLIT, new JPanel(), new JPanel(),Color.blue));
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(3);
        frame.setVisible(true);
    }
}

