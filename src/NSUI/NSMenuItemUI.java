package NSUI;

import java.awt.*;
import java.awt.event.KeyEvent;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.*;

public class NSMenuItemUI  extends BasicMenuItemUI {
	static final int MENU_ITEM_HEIGHT	= 23;

	static final String MAX_TEXT_WIDTH =  "maxTextWidth";
	static final String MAX_ACC_WIDTH  =  "maxAccWidth";
	protected static final Rectangle zeroRect = new Rectangle(0,0,0,0);
	protected static Rectangle iconRect = new Rectangle();
	protected static Rectangle textRect = new Rectangle();
	protected static Rectangle acceleratorRect = new Rectangle();
	protected static Rectangle viewRect = new Rectangle(Short.MAX_VALUE, Short.MAX_VALUE);
	static Rectangle r = new Rectangle();

	public static ComponentUI createUI( JComponent x) {
		  x.setFont(new java.awt.Font("Î¢ÈíÑÅºÚ", Font.PLAIN, 13));
		  x.setForeground(new Color(52,55,56));
		  return new NSMenuItemUI();
		  }
}
		  
//    protected Dimension getPreferredMenuItemSize(JComponent c, Icon checkIcon,
//    		Icon arrowIcon, int defaultTextIconGap) {
//    	JMenuItem b = (JMenuItem) c;
//    	Icon icon = (Icon) b.getIcon();
//    	String text = b.getText();
//    	KeyStroke accelerator =  b.getAccelerator();
//    	String acceleratorText = "";
//    	
//    	if (accelerator != null) {
//    		int modifiers = accelerator.getModifiers();
//    		if (modifiers > 0) {
//    			acceleratorText = KeyEvent.getKeyModifiersText(modifiers);
//    			acceleratorText += "+";
//    			}
//    		int keyCode = accelerator.getKeyCode();
//    		if (keyCode != 0) {
//    			acceleratorText += KeyEvent.getKeyText(keyCode);
//    			}
//    		else {
//    			acceleratorText += accelerator.getKeyChar();
//    			}
//    		}
//    	
//    	Font font = b.getFont();
//    	FontMetrics fm = c.getFontMetrics(font);
//    	resetRects();
//    	layoutMenuItem(fm, text, acceleratorText, icon, checkIcon, arrowIcon,viewRect, iconRect, 
//    			textRect, acceleratorRect,text == null ? 0 : defaultTextIconGap,defaultTextIconGap);
//
//// Find the union of the icon and text rects.
//    	r.setBounds(textRect);
//    	r = SwingUtilities.computeUnion(iconRect.x, iconRect.y,iconRect.width, iconRect.height, r);
//    	Container parent = menuItem.getParent();
//    	if (parent != null && parent instanceof JComponent) {
//    		JComponent p = (JComponent) parent;
//
//// Get widest values so far from parent, or null if none yet.
//    		Integer maxTextWidth = (Integer) p.getClientProperty(MAX_TEXT_WIDTH);
//    		Integer maxAccWidth = (Integer) p.getClientProperty(MAX_ACC_WIDTH);
//    		int maxTextValue = maxTextWidth!=null ? maxTextWidth.intValue() : 0;
//    		int maxAccValue = maxAccWidth!=null ? maxAccWidth.intValue() : 0;
////Compare the text widths, and adjust the r.width to the widest.
//    		if (r.width < maxTextValue) {
//    			r.width = maxTextValue;
//    			}
//    		else {
//    			p.putClientProperty(MAX_TEXT_WIDTH, new Integer(r.width) );
//    			}
//
//// Compare the accelarator widths.
//    		if (acceleratorRect.width > maxAccValue) {
//    			maxAccValue = acceleratorRect.width;
//    			p.putClientProperty(MAX_ACC_WIDTH, new Integer(acceleratorRect.width) );
//    			}
//
//// Add on the widest accelerator.
//    		r.width += maxAccValue;
//    		r.width += defaultTextIconGap;
//    		}
//
//// Add in the checkIcon
//    	r.width += 20;//checkIconRect.width;
//    	r.width += defaultTextIconGap;
//
//// Add in the arrowIcon
//    	r.width += defaultTextIconGap;
//    	r.width += 12;//arrowIconRect.width;
//// Add in the "padding" on either side of the menu item.
//    	r.width += 2*defaultTextIconGap;
//    	Insets insets = b.getInsets();
//    	if(insets != null) {
//    		r.width += insets.left + insets.right;
//    		r.height += insets.top + insets.bottom;
//    		}
//
//    	if(r.width%2 == 0) {
//    		r.width++;
//    		}
//    	if(r.height%2 == 0) {
//    		r.height++;
//    		}
//
//    	return new Dimension((int)r.getWidth(), MENU_ITEM_HEIGHT);
//    	
//    }
//
////	protected void paintCheck(Graphics g, JMenuItem menuItem) {
////		ButtonModel model = menuItem.getModel();
////		if(menuItem.isSelected()) {
////			int x = iconRect.x-2;
////			if (!menuItem.getComponentOrientation().isLeftToRight()) {
////				x++; // ???
////			}
////			int y = 1;
////			int width = 19;//20;//checkIconRect.width;
////			int height = 19;//20;//checkIconRect.height;
////			Color oldColor = g.getColor();
////		//	g.setColor(UIManager.getColor("OfficeLnF.HighlightBorderColor"));
////			g.drawRect(x,y, width,height);
////			if (model.isArmed())
////		//		g.setColor(UIManager.getColor("OfficeXPLnF.PressedHighlightColor"));
////		//	else
////		//		g.setColor(UIManager.getColor("OfficeXPLnF.CheckBoxHighlightColor"));
////			g.fillRect(x+1,y+1, width-1,height-1);
////			g.setColor(oldColor);
////			checkIcon.paintIcon(menuItem, g, x+6,y+6);//8,7);//checkIconRect.x, checkIconRect.y);
////		}
////	}
//
//	protected void paintIcon(Graphics g, JMenuItem menuItem) {
//		NSUtils.paintMenuItemIcon(g, menuItem, iconRect);
//	}
//	
//	protected void paintMenuItem(Graphics g, JComponent c, Icon checkIcon, Icon arrowIcon,
//            Color background, Color foreground, int defaultTextIconGap) {
//		JMenuItem b = (JMenuItem) c;
//		ButtonModel model = b.getModel();
//		int menuWidth = b.getWidth();
//		int menuHeight = b.getHeight();
//		resetRects();
//		viewRect.setBounds( 0, 0, menuWidth, menuHeight );
//		Font holdf = g.getFont();
//		Font f = c.getFont();
//		g.setFont(f);
//		FontMetrics fm = c.getFontMetrics(f);
//
//// get Accelerator text
//		KeyStroke accelerator =  b.getAccelerator();
//		String acceleratorText = "";
//		if (accelerator != null) {
//			int modifiers = accelerator.getModifiers();
//			if (modifiers > 0) {
//				acceleratorText = KeyEvent.getKeyModifiersText(modifiers);
//				acceleratorText += "+";
//				}
//			
//			int keyCode = accelerator.getKeyCode();
//			if (keyCode != 0)
//				acceleratorText += KeyEvent.getKeyText(keyCode);
//			else
//				acceleratorText += accelerator.getKeyChar();
//			}
//		
//// layout the text and icon
//		String text = layoutMenuItem(
//				fm, b.getText(), acceleratorText, b.getIcon(),checkIcon, arrowIcon,
//				viewRect, iconRect, textRect, acceleratorRect,
//				b.getText() == null ? 0 : defaultTextIconGap,defaultTextIconGap);
//
//// Paint background
//		paintBackground(g, b, background);
//		Color holdc = g.getColor();
//
//// Paint the Check
//		boolean isCheckOrRadio = (c instanceof JCheckBoxMenuItem) ||(c instanceof JRadioButtonMenuItem);
//		if (checkIcon != null && isCheckOrRadio)
//	//		paintCheck(g, menuItem);
//
//// Paint the Icon
//		if(b.getIcon()!=null && !isCheckOrRadio)
//			paintIcon(g, menuItem);
//
//// Draw the Text
//		paintText(g, b, textRect, text);
//
//
//		g.setColor(holdc);
//		g.setFont(holdf);
//		
//	}
//    protected void installDefaults() {
//		    super.installDefaults();
//		    
//		    menuItem.setBorderPainted( false);
//		    menuItem.setOpaque( false);
//		    
//		    defaultTextIconGap = 3;
//		  }
//		  
//		  protected void uninstallDefaults() {
//		    super.uninstallDefaults();
//		    
//		    menuItem.setOpaque( true);
//		  }
//		  
//		    public static String layoutCompoundLabel(JComponent c, FontMetrics fm,
//					String text, Rectangle viewR,
//					Rectangle iconR, Rectangle textR) {
//		    	
//		    	boolean ltr = true;
//		    	if (c!=null) ltr = c.getComponentOrientation().isLeftToRight();
//		    	//int hAlign = ltr ? SwingConstants.RIGHT : SwingConstants.LEFT;
//		    	int hTextPos = ltr ? SwingConstants.LEFT : SwingConstants.RIGHT;
//		    	
//		    	// Note that iconR won't matter if an icon doesn't exist, so it
//		    	// doesn't matter that it's in the same (x,y) location as the text.
//		    	iconR.width  = 20;
//		    	iconR.height = 20;
//		    	if (hTextPos==SwingConstants.LEFT) {
//		    		iconR.x = 4;
//		    		}
//		    	else {
//		    		iconR.x = viewR.x+viewR.width-20-1;
//		    		}
//		    	iconR.y = viewR.y + (viewR.height/2) - 7; //(iconR.height/2);
//
//// Initialize the text bounds rectangle textR.  If a null
//// or and empty String was specified we substitute "" here
//// and use 0,0,0,0 for textR.
//		    	boolean textIsEmpty = (text == null) || text.equals("");
//		    	if (textIsEmpty) {
//		    		textR.width = textR.height = 0;
//		    		text = "";
//		    		}
//		    	else {
//		    		
//		    		textR.width = SwingUtilities.computeStringWidth(fm,text);
//		    		textR.height = fm.getHeight();
//
///* If the label text string is too wide to fit within the available
//* space, "..." and as many characters as will fit will be
//* displayed instead.
//*/
//		    		int availTextWidth = viewR.width - 32;
//		    		
//		    		if (textR.width > availTextWidth) {
//		    			String clipString = "...";
//		    			int totalWidth = SwingUtilities.computeStringWidth(fm,clipString);
//		    			int nChars;
//		    			for(nChars = 0; nChars < text.length(); nChars++) {
//		    				totalWidth += fm.charWidth(text.charAt(nChars));
//		    				if (totalWidth > availTextWidth)
//		    					break;
//		    				}
//		    			text = text.substring(0, nChars) + clipString;
//		    			textR.width = SwingUtilities.computeStringWidth(fm,text);
//		    			}
//		    		
//		    	}
//// If this is a top-level menu, there will be no icon.
//		    	if ((c instanceof JMenu) && ((JMenu)c).isTopLevelMenu()) {
//		    		int nonShadowWidth = viewR.width - 4;
//		    		textR.x = viewR.x + nonShadowWidth/2 - textR.width/2;	// Text will be centered.
//		    		}
//		    	else
//		    		// Text will be placed after the icon/check/whatever if it's not
//		    		// a top-level menu item.
//		    		if (hTextPos==SwingConstants.LEFT) {
//		    			textR.x = 32;
//		    			}
//		    		else { // SwingConstants.RIGHT
//		    			//Integer i = (Integer)c.getClientProperty(MAX_ACC_WIDTH);
////int maxAccWidth = i==null ? 0 : i.intValue();
//		    			textR.x = viewR.x+viewR.width -(32 + /*defaultTextIconGap*/5 + textR.width);
//		    			}
//		    	
//		    	// Must compute text's y-coordinate after textR.height has been computed.
//		    	textR.y = viewR.y + (viewR.height/2) - (textR.height/2);
//		    	
//		    	return text;
//		    	
//		    }
//		    
//			protected String layoutMenuItem(FontMetrics fm, String text,
//					String acceleratorText, Icon icon, Icon checkIcon,
//					Icon arrowIcon, Rectangle viewRect, Rectangle iconRect,
//					Rectangle textRect, Rectangle acceleratorRect,
//					int textIconGap, int menuItemGap) {
//
//			// Lay out the text and icon rectangles.
//			layoutCompoundLabel(menuItem, fm, text, viewRect, iconRect, textRect);
//
//			// Give dimensions to the accelerator text rectangle if the text is
//			// actually something.
//			if (!acceleratorText.equals("")) {
//				acceleratorRect.width = SwingUtilities.computeStringWidth( fm, acceleratorText );
//				acceleratorRect.height = fm.getHeight();
//				if (menuItem.getComponentOrientation().isLeftToRight()) {
//					acceleratorRect.x = viewRect.x + viewRect.width - 12/*arrowIconRect.width*/
//										- menuItemGap - acceleratorRect.width;
//				}
//				else {
//					acceleratorRect.x = viewRect.x + 20;
//				}
//				acceleratorRect.y = textRect.y;
//			}
//
//			//Rectangle labelRect = iconRect.union(textRect);
//
//	        return text;
//
//	    }
//
//			protected void resetRects() {
//		        iconRect.setBounds(zeroRect);
//		        textRect.setBounds(zeroRect);
//		        acceleratorRect.setBounds(zeroRect);
//		        viewRect.setBounds(0,0,Short.MAX_VALUE, Short.MAX_VALUE);
//		        r.setBounds(zeroRect);
//		    }
//		    
//			protected void paintUnarmedBackground(Graphics g, JMenuItem menuItem) {
//				NSUtils.paintMenuItemBackground(g, menuItem);
//			}
//		    
//			protected void paintBackground( Graphics g, JMenuItem menuItem, Color bgColor) {
//				ButtonModel model = menuItem.getModel();
//				Color oldColor = g.getColor();
//				int menuItemWidth = menuItem.getWidth();
//				int menuItemHeight = menuItem.getHeight();
//				paintUnarmedBackground(g, menuItem);
//		    NSUtils.pintaBarraMenu( g, menuItem, bgColor);
//			JPopupMenu popupMenu = (JPopupMenu)menuItem.getParent();
//			if (popupMenu.getComponentIndex(menuItem) == popupMenu.getComponentCount()-1) {
//				g.setColor(menuItem.getBackground());
//				int y = menuItemHeight - 1;
//				// Do whole line to cover both LTR and RTL.
//				g.drawLine(0,y, menuItemWidth-1,y);
//			}
//
//			g.setColor(oldColor);
//		  }
//}
