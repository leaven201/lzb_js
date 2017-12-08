package NSUI;

import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

public class NSProgressBarUI extends BasicProgressBarUI {
	public static ComponentUI createUI( JComponent c) {
	    return new NSProgressBarUI();
	  }

	public void paintIndeterminate( Graphics g, JComponent c) {
		  Graphics2D g2D = (Graphics2D)g;
		  
		  Rectangle rec = new Rectangle();
	    rec = getBox( rec);
	    
	    Insets b = progressBar.getInsets();
	    int xi = b.left;
	    int yi = b.top;
	    int xf = c.getWidth() - b.right;
	    int yf = c.getHeight() - b.bottom;
	    
	    g2D.setColor( progressBar.getForeground());
	    g2D.fillRect( rec.x, rec.y, rec.width, rec.height);
	    
	    if ( progressBar.getOrientation() == JProgressBar.HORIZONTAL ) {
	      GradientPaint grad = new GradientPaint( rec.x,rec.y, new Color(179,211,123), 
	    		  rec.x,rec.height, new Color(236,255,190));
	      g2D.setPaint( grad);
	      g2D.fill( rec);
	      
	      grad = new GradientPaint( xi,yi, new Color(214,231,160), xi,yf, Color.white);
	  		g2D.setPaint( grad);
	      g2D.fillRect( xi,yi, rec.x,yf);
	      g2D.fillRect( rec.x + rec.width,yi, xf,yf);
	    }
	  }
}
