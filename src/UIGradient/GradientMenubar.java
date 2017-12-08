package UIGradient;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Rectangle;

import javax.swing.JMenuBar;

public class GradientMenubar extends JMenuBar{
	    private Color fromColor;
	    private Color toColor;
		private Color middleColor;
		public GradientMenubar(Color from,Color to,Color middle) { 
	        super(); 
	        this.fromColor = from;
	        this.toColor = to;
	        this.middleColor = middle;
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
//	        Paint storedPaint = g2.getPaint(); 
	        g2.setPaint(new GradientPaint(x, y, fromColor, x, y+height/2,middleColor)); 
	        g2.fillRect((int)x,(int)y, width, height/2); 
	        g2.setPaint(new GradientPaint(x, y+height/2, middleColor, x, y+height,toColor)); 
	        g2.fillRect((int)x,(int)y+height/2, width, height/2); 
//	        g2.setPaint(storedPaint); 
	    } 
}
