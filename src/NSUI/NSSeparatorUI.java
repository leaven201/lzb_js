package NSUI;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSeparatorUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class NSSeparatorUI extends BasicSeparatorUI {
		public static ComponentUI createUI( JComponent c ) {
			return new NSSeparatorUI();
		}

		public Dimension getPreferredSize( JComponent c ) {
			return new Dimension(0,2);
		}

		public void paint(Graphics g, JComponent c) {
			if (c.getParent() instanceof JPopupMenu)
				paintMenuSeparator(g, c);
			else
				super.paint(g, c);
		}

		protected void paintMenuSeparator(Graphics g, JComponent c) {
			NSUtils.paintMenuItemBackground(g, c);
			g.setColor(c.getForeground());
			if (c.getComponentOrientation().isLeftToRight()) {
				g.setColor(new Color(73,135,146));
				g.drawLine(26,0, c.getWidth(),0);
			}
			else {
				g.drawLine(0,0, c.getWidth()-31,0);
			}
		}
}
