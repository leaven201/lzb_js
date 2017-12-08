package NSUI;

import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.*;
import java.util.Properties;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

public class NSUtils {
	  protected static Color rollColor;
	  
	  static final int THIN = 0;
	  static final int FAT = 1;
	  
	  static final int MATRIX_FAT = 5;
	  static Kernel kernelFat;
		public static final AlphaComposite ICON_COMPOSITE =
			AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f);
	  
	  static final int MATRIX_THIN = 3;
	  static Kernel kernelThin;
	  static void pintaBarraMenu( Graphics g, JMenuItem menuItem, Color bgColor) {
		    ButtonModel model = menuItem.getModel();
		    Color oldColor = Color.BLACK;
		  
		    int menuWidth = menuItem.getWidth();
		    int menuHeight = menuItem.getHeight();
		  
		    if ( menuItem.isOpaque() ) {
		      g.setColor( menuItem.getBackground());
		      g.fillRect( 0,0, menuWidth, menuHeight);
		    }
		    
		    if ( (menuItem instanceof JMenu && !(((JMenu)menuItem).isTopLevelMenu()) && model.isSelected())
		         || model.isArmed() ) {
		      RoundRectangle2D.Float boton = new RoundRectangle2D.Float(); 
		      boton.x = 1;
		      boton.y = 0;
		      boton.width = menuWidth - 3;
		      boton.height = menuHeight - 1;
		      boton.arcwidth = 8;
		      boton.archeight = 8;
		      //鼠标移动位置的菜单项的颜色
		      GradientPaint grad = new GradientPaint( 0,0,new Color(84,212,53),0,(float) (menuHeight*0.7), new Color(236,255,190));
		      
		      Graphics2D g2D = (Graphics2D)g;
		      g2D.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		      //边框的颜色
		      g.setColor(new Color(73,135,146));
		      g2D.draw( boton);
		      
		      g2D.setPaint( grad);
		      g2D.fill( boton);
		      
		      g2D.setRenderingHint( RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
		    }
		  
		    g.setColor( oldColor);//菜单文字的颜色
		  }

		public static void paintDisabledButtonIcon(Graphics g, AbstractButton b,Rectangle iconRect) {
			Icon icon = (Icon)b.getDisabledIcon();
			// Necessary as some icons (like JInternalFrame's) don't have disabled icons.
			if (icon!=null) {
				Graphics2D g2d = (Graphics2D)g;
				g2d.setComposite(NSUtils.ICON_COMPOSITE);
				icon.paintIcon(b, g2d, iconRect.x, iconRect.y);
				g2d = null;
				}
			}

		public static void paintMenuItemIcon(Graphics g, JMenuItem menuItem,Rectangle iconRect) {
			Icon icon;
			ButtonModel model = menuItem.getModel();
			
			if(!model.isEnabled()) {
				NSUtils.paintDisabledButtonIcon(g, menuItem, iconRect);
				}
			
			else if(model.isPressed() && model.isArmed()) {
				icon = (Icon) menuItem.getPressedIcon();
				if(icon == null) {
					// 图标默认
					icon = (Icon) menuItem.getIcon();
					}
				if (icon!=null)
					icon.paintIcon(menuItem,g, iconRect.x,iconRect.y);
				}
			else {
				icon = (Icon) menuItem.getIcon();
				if (icon!=null) {
					if (model.isArmed()|| (menuItem instanceof JMenu && model.isSelected())) {
						Icon disabledIcon = (Icon)menuItem.getDisabledIcon();
// Sometimes this is null.  This will result in no shadow under the "raised up" icon, but oh well...
						if (disabledIcon!=null)
							disabledIcon.paintIcon(menuItem,g, iconRect.x,iconRect.y);
						icon.paintIcon(menuItem,g, iconRect.x-2,iconRect.y-2);
						}
					else
						icon.paintIcon(menuItem,g, iconRect.x,iconRect.y);
					}
				}
			
		}

		public static void paintMenuItemBackground(Graphics g, Component c) {
			Graphics2D g2d = (Graphics2D) g;
			Paint menuItemBGPaint = new GradientPaint(0, 0, new Color(236,255,190), (float) (((float)25)*0.7), 0, new Color(179,211,123));
			g2d.setPaint(menuItemBGPaint);
			int width = c.getWidth();
			int mainWidth = width - 25;
			int height = c.getHeight();
			if (c.getComponentOrientation().isLeftToRight()) {
				g.fillRect(0,0,  25,height);
				g.setColor(Color.WHITE);
				g.fillRect(25,0, mainWidth,height);
			}
			else {
				// Hack - translate so cached GradientPaints paint correctly.
				g2d.translate(width, height);
				g2d.rotate(Math.PI);
				g2d.fillRect(0,0,  25,height);
				g2d.rotate(Math.PI);//-Math.PI);
				g2d.translate(-width, -height);
				g2d.setColor(c.getBackground());
				g2d.fillRect(0,0, mainWidth,height);
			}
		}

}
