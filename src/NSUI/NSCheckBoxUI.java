package NSUI;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;

public class NSCheckBoxUI extends BasicCheckBoxMenuItemUI {

    public static ComponentUI createUI(JComponent c) {
		  c.setFont(new java.awt.Font("ËÎÌו", Font.PLAIN, 13));
		  c.setForeground(new Color(52,55,56));
        return new NSCheckBoxUI();
    }

    protected void installDefaults() {
        super.installDefaults();
        
        menuItem.setBorderPainted( false);
        menuItem.setOpaque( false);
        
        defaultTextIconGap = 3;
      }
      
      protected void uninstallDefaults() {
        super.uninstallDefaults();
        
        menuItem.setOpaque( true);
      }
      
      protected void paintBackground( Graphics g, JMenuItem menuItem, Color bgColor) {
    	  menuItem.setFont(new java.awt.Font("ËÎÌו", Font.PLAIN, 13));
    	  g.setColor(Color.BLACK);
    	  NSUtils.paintMenuItemBackground(g, menuItem);
    	  NSUtils.pintaBarraMenu( g, menuItem, bgColor);
      }
}