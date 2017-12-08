package NSUI;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicMenuUI;
import javax.swing.text.View;

public class NSMenuUI extends BasicMenuUI {
	protected static final Rectangle zeroRect = new Rectangle(0,0,0,0);
	protected static Rectangle iconRect = new Rectangle();
	protected static Rectangle textRect = new Rectangle();
	protected static Rectangle arrowIconRect = new Rectangle();
	protected static Rectangle viewRect = new Rectangle(Short.MAX_VALUE, Short.MAX_VALUE);
	static Rectangle r = new Rectangle();

	public static ComponentUI createUI( JComponent x) {
		  x.setFont(new java.awt.Font("微软雅黑", Font.PLAIN, 13));
		  x.setForeground(new Color(52,55,56));
		return new NSMenuUI();
		}
}
//
//	protected Dimension getPreferredMenuItemSize(JComponent c,Icon checkIcon,
//			Icon arrowIcon,	int defaultTextIconGap) {
//		JMenu menu = (JMenu)c;
//		Dimension d = null;
//		if (menu.isTopLevelMenu()) {
//			d = super.getPreferredMenuItemSize(menu, checkIcon,	arrowIcon, defaultTextIconGap);
////			d.width += 4;	// Allows for the "shadow" to be drawn.
//			d.height = 22;
//			}
//		else { // Menu that's a menu item inside another menu.
//			d = getPreferredNonTopLevelMenuSize(menu, checkIcon, arrowIcon,	defaultTextIconGap);
//			}
//		return d;
//		
//	}
//
//	protected Dimension getPreferredNonTopLevelMenuSize(JMenu menu,Icon checkIcon,
//			Icon arrowIcon,int defaultTextIconGap) {
//		Icon icon = (Icon)menu.getIcon();
//		String text = menu.getText();
//		Font font = menu.getFont();
//		FontMetrics fm = menu.getFontMetrics(font);
//		resetRects();
//
//		layoutMenuItem(fm, text, icon, arrowIcon,viewRect, iconRect, textRect, arrowIconRect,
//				text == null ? 0 : defaultTextIconGap,defaultTextIconGap);
//
//// Find the union of the icon and text rects.
//		r.setBounds(textRect);
//		r = SwingUtilities.computeUnion(iconRect.x, iconRect.y, iconRect.width, iconRect.height, r);
//
//// If the width of this menu's text is longer than the parent menu's
//// current longest text, update it.  This is so that other menu
//// items in the parent menu can have their accelerators align.
//
//// Get the parent, which stores the information.
//		Container parent = menu.getParent();
//		if (parent!=null && (parent instanceof JComponent)) {
//			//Get widest text so far from parent, if no one exists null is returned.
//			JComponent p = (JComponent) parent;
//			Integer maxTextWidth = (Integer) p.getClientProperty(NSMenuItemUI.MAX_TEXT_WIDTH);
//			int maxTextValue = maxTextWidth!=null ?	maxTextWidth.intValue() : 0;
//
////Compare the text widths, and adjust the r.width to the widest.
//			if (r.width < maxTextValue)
//				r.width = maxTextValue;
//			else {
//				p.putClientProperty(NSMenuItemUI.MAX_TEXT_WIDTH,new Integer(r.width));
//				}
//			
//			r.width += defaultTextIconGap;
//		}
//
//// Add in the checkIcon
////		r.width += 20;//checkIconRect.width;
//		r.width += defaultTextIconGap;
//		
//		// Add in the arrowIcon
//		r.width += defaultTextIconGap;
//		r.width += 12;//arrowIconRect.width;
//	
//// Add in the "padding" on either side of the menu item.
//		r.width += 2*defaultTextIconGap;
//		Insets insets = menu.getInsets();
//		if(insets != null) {
//			r.width += insets.left + insets.right;
//			r.height += insets.top + insets.bottom;
//			}
//		
//// if the width is even, bump it up one. This is critical for the focus dash line to draw properly
//		if(r.width%2 == 0)
//			r.width++;
//		// if the height is even, bump it up one. This is critical for the text to center properly
//		if(r.height%2 == 0)
//			r.height++;
//		return new Dimension((int)r.getWidth(),NSMenuItemUI.MENU_ITEM_HEIGHT);
//		}
//	
//	
//    protected void installDefaults() {
//	    super.installDefaults();
//	    
//	    menuItem.setBorderPainted( false);
//	    menuItem.setOpaque( false);
//	    
//	    defaultTextIconGap = 3;
//	  }
//	  
//	  protected void uninstallDefaults() {
//	    super.uninstallDefaults();
//	    
//	    menuItem.setOpaque( true);
//	  }
// 
//    protected String layoutMenuItem(FontMetrics fm, String text, Icon icon,
//			Icon arrowIcon, Rectangle viewRect, Rectangle iconRect,
//			Rectangle textRect, Rectangle arrowIconRect,int textIconGap, int menuItemGap) {
//    	NSMenuItemUI.layoutCompoundLabel(menuItem, fm, text, viewRect,iconRect, textRect);
//
//// Initialize the arrowIcon bounds rectangle width & height.
//    	if (arrowIcon != null) {
//    		arrowIconRect.width = arrowIcon.getIconWidth();
//    		arrowIconRect.height = arrowIcon.getIconHeight();
//    		}
//    	else {
//    		arrowIconRect.width = arrowIconRect.height = 0;
//    		}
//
////Rectangle labelRect = iconRect.union(textRect);
//
//// Position the Arrow Icon.
//    	int temp = viewRect.x;// + 6;
//    	textRect.x += temp;
//    	iconRect.x += temp;
//    	if (menuItem.getComponentOrientation().isLeftToRight()) {
//    		arrowIconRect.x = viewRect.x + viewRect.width - menuItemGap - arrowIconRect.width;
//    		}
//    	else {
//    		arrowIconRect.x = viewRect.x + menuItemGap;
//    		}
//    	
//    	arrowIconRect.y = 8;//labelRect.y + (labelRect.height/2) - arrowIconRect.height/2;
//    	
//    	return text;
//    	
//    }
//
//	protected void paintIcon(Graphics g, JMenuItem menuItem) {
//		NSUtils.paintMenuItemIcon(g, menuItem, iconRect);
//	}
//	
//  protected void paintMenuItem(Graphics g, JComponent c, Icon checkIcon, Icon arrowIcon,
//            Color background, Color foreground, int defaultTextIconGap) {
//    	JMenu menu = (JMenu)c;
//// For a top-level menu item, paint regularly (could be optimized...).
//    	if (menu.isTopLevelMenu())
//    		super.paintMenuItem(g, c, checkIcon, arrowIcon, background,	foreground, defaultTextIconGap);
//
//// Otherwise, it must be painted like an OfficeXPMenuItem
//// (but optimized a tad).
//    	else {
//    		JMenuItem b = (JMenuItem) c;
//    		//ButtonModel model = b.getModel();
//    		//   Dimension size = b.getSize();
//    		int menuWidth = b.getWidth();
//    		int menuHeight = b.getHeight();
//    		resetRects();
//
//    		viewRect.setBounds( 0, 0, menuWidth, menuHeight );
//    		
//    		Font holdf = g.getFont();
//    		Font f = c.getFont();
//    		g.setFont( f );
//    		FontMetrics fm = c.getFontMetrics( f );
//// layout the text and icon
//    		String text = layoutMenuItem(fm, b.getText(), b.getIcon(),
//    				arrowIcon, viewRect, iconRect, textRect, arrowIconRect,
//    				b.getText() == null ? 0 : defaultTextIconGap,defaultTextIconGap);
//
//// Paint background
//    		paintSubmenuBackground(g, menu);
//    		paintBackground(g, b, background);
//    		Color holdc = g.getColor();
//    		// Paint the Icon
//    		paintIcon(g, menuItem);
//
//// Draw the Text
//    		if(text != null) {
//    			View v = (View) c.getClientProperty(BasicHTML.propertyKey);
//    			if (v != null)
//    				v.paint(g, textRect);
//    			else
//    				paintText(g, b, textRect, text);
//    			}
//    		
//    		// Paint the Arrow
//    		if (arrowIcon != null) {
//    			arrowIcon.paintIcon(c, g, arrowIconRect.x, arrowIconRect.y);
//    			}
//    		g.setColor(holdc);
//    		g.setFont(holdf);
//    		}
//    	}
// 
//	protected void paintUnarmedBackground(Graphics g, JMenuItem menuItem) {
//		NSUtils.paintMenuItemBackground(g, menuItem);
//	}
//	
//    protected void resetRects() {
//        iconRect.setBounds(zeroRect);
//        textRect.setBounds(zeroRect);
//        arrowIconRect.setBounds(zeroRect);
//        viewRect.setBounds(0,0,Short.MAX_VALUE, Short.MAX_VALUE);
//    }
//    
//	protected void paintSubmenuBackground(Graphics g, JMenu menu) {
//
//		ButtonModel model = menu.getModel();
//		Color oldColor = g.getColor();
//		int menuWidth = menu.getWidth();
//		int menuHeight = menu.getHeight();
//
//		paintUnarmedBackground(g, menu);
//
//		Component parent = menu.getParent();
//		if (parent instanceof JPopupMenu) {
//			JPopupMenu popupMenu = (JPopupMenu)parent;
//			if (popupMenu.getComponentIndex(menu) == popupMenu.getComponentCount()-1) {
//				g.setColor(Color.white);
////				g.setColor(menu.getBackground());
//				int y = menuHeight - 1;
//				// Do whole line to cover both LTR and RTL.
//				g.drawLine(0,y, menuWidth-1,y);
//			}
//		}
//
//		g.setColor(oldColor);
//
//	}
//  protected void paintBackground( Graphics g, JMenuItem menuItem, Color bgColor) {
//	  JMenu menu = (JMenu)menuItem;
//	  if(!menu.isTopLevelMenu())
//		  NSUtils.pintaBarraMenu( g, menuItem, bgColor);
//	  else
//		  paintTopLevelMenuBackground(g, menu);
//  }
//  
//  protected void paintTopLevelMenuBackground(Graphics g, JMenu menu){
//	  
//		ButtonModel model = menu.getModel();
//		int menuWidth = menu.getWidth();
//		int menuHeight = menu.getHeight();
//	      RoundRectangle2D.Float area = new RoundRectangle2D.Float(); 
//	      area.x = 1;
//	      area.y = 1;
//	      area.width = menuWidth - 3;
//	      area.height = menuHeight + 2;
//	      area.arcwidth = 6;
//	      area.archeight = 6;
//
//		if (model.isArmed() || model.isSelected()) {
//			// 当该菜单被选中时填充菜单名称
//		      GradientPaint grad = new GradientPaint( 0,0,Color.WHITE,0, menuHeight, new Color(179,211,123));
//		      Graphics2D g2D = (Graphics2D)g;
//		      g2D.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		      //边框的颜色
//		     // g.setColor(new Color(73,135,146));
//		      g.setColor(new Color(0,0,0));
//		      g2D.setPaint( grad);
//		      g2D.fill( area);
//		      
//		      g2D.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
//
//			// 对被选菜单menu添加边框
//			JPopupMenu popupMenu = menu.getPopupMenu();
//			JMenuBar menuBar = (JMenuBar)menu.getParent();
//			Point menuLocation = menu.getLocation();
//			Point popupMenuLocation = SwingUtilities.convertPoint(popupMenu,
//									popupMenu.getLocation(), menuBar);
//			//int newX = menuLocation.x - popupMenuLocation.x + 1;
//			//g.setColor(new Color(73,135,146));
//			g.setColor(new Color(73,135,146));
//			// 如果下拉菜单低于菜单栏或下拉菜单不可见（用户使用ALT激活菜单）
//			if (menuLocation.y<popupMenuLocation.y || !menu.isPopupMenuVisible()) {
//			      g2D.draw( area);
//			}
//			// 如果下拉菜单高于菜单栏
//			else {
//			      g2D.draw( area);
//			}
//		}
//  }
//}
