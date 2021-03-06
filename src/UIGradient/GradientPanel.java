package UIGradient;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JPanel;
import javax.swing.UIManager;

public class GradientPanel extends JPanel { 
    private Color fromColor;
    private Color toColor;
	public GradientPanel(LayoutManager lm) { 
        super(lm); 
    } 
	public GradientPanel(Color from,Color to) { 
        super(); 
        this.fromColor = from;
        this.toColor = to;
    }
	
    public void setFromColor(Color fromColor) {
		this.fromColor = fromColor;
	}
	public void setToColor(Color toColor) {
		this.toColor = toColor;
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
        g2.setPaint(new GradientPaint(x, y, fromColor, x, y+height,toColor)); 
        g2.fillRect((int)x,(int)y, width, height); 
//        g2.setPaint(storedPaint); 
    } 
    
}