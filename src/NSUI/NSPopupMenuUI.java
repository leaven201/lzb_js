package NSUI;

import java.awt.*;
import java.awt.image.*;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.event.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

public class NSPopupMenuUI extends BasicPopupMenuUI {
	  private BufferedImage blurFondo = null;
	  
	  public static ComponentUI createUI( JComponent c) {
	    return new NSPopupMenuUI();
	  }
	  
		public void update( Graphics g, JComponent c) {
			  Component invoker = ((JPopupMenu)c).getInvoker();
			  paintBorder(c, g, c.getLocation().x, c.getLocation().y, c.getWidth(), c.getHeight());
		    
		    super.update( g, c);
		    }

		  public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
			  int right = x + width-1;
			  int bottom = y + height-1;

			  // Draw the main line border.
			  g.setColor(new Color(73,135,146));
			  //g.setColor(UIManager.getDefaults().getColor("OfficeLnF.MenuBorderColor"));
			  g.drawLine(x,y, right,y);
			  g.drawLine(right,y, right,bottom);
			  g.drawLine(right,bottom, x,bottom);
			  g.drawLine(x,bottom, x,y);

// Draw the menu-colored line where this popup attaches to the
// menu (if applicable).
			  Component invoker = ((JPopupMenu)c).getInvoker();
			  if (invoker!=null && invoker instanceof JMenu) {
				  JMenu menu = (JMenu)invoker;
				  Component parent = invoker.getParent();
				  if (parent instanceof JMenuBar) {
					  JMenuBar menuBar = (JMenuBar)parent;
					  Point menuLocation = menu.getLocation();
					  Point popupMenuLocation = SwingUtilities.convertPoint(c, c.getLocation(), menuBar);
					  int topLevelMenuWidth = menu.getWidth() - 7; // "-7" for the shadow.
					  g.setColor(Color.white);
					  //g.setColor(UIManager.getColor("OfficeXPLnF.ChosenMenuColor"));
					  int newX = menuLocation.x - popupMenuLocation.x + 1;
					  if (menuLocation.y < popupMenuLocation.y)
						  g.drawLine(newX,y, newX+topLevelMenuWidth,y);
					  else
						  g.drawLine(newX,bottom, newX+topLevelMenuWidth,bottom);
					  }
				  } // End of if (invoker!=null && invoker instanceof JMenu).

// Draw the menu-item colored line at the top of this popup menu.
//			  g.setColor(Color.BLUE);
			  g.setColor(c.getBackground());
			  g.drawLine(1,1, right-1,1);
			  }
		  public static class PopupBorder extends AbstractBorder
	                						implements UIResource {
		  private static final long serialVersionUID = 1L;
		  
		  public PopupBorder() {
			  super();
			  }

		  public Insets getBorderInsets(Component c) {
			  return new Insets(0,0,0,0);
			  }


//		  public void update( Graphics g, JComponent c) {
//		    if ( blurFondo != null ) {
//		      g.drawImage( blurFondo, 0, 0, null);
//		    }
//		    c.setFont(new java.awt.Font("宋体", Font.PLAIN, 13));
//
//		    	//弹出菜单右半部分的颜色
//		    Color cFondoR = Color.WHITE;
//		    Graphics2D g2d = (Graphics2D) g.create();
//		    GradientPaint paint;
//		    paint = new GradientPaint(0, 0, new Color(184,236,220), (float) (((float)25)*0.7), 0, new Color(140,199,240));
//		    g2d.setPaint(paint);
//		    g2d.fillRect(0, 0, 26, c.getHeight()-1);
//		    g.setColor( cFondoR);
//		    g.fillRect( 26,0, c.getWidth()-1,c.getHeight()-1);
		  }
}
