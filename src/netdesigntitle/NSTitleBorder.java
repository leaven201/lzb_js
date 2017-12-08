package netdesigntitle;


import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

public class NSTitleBorder implements Border {

	public Insets getBorderInsets(Component c) {
		return new Insets(2, 2, 4, 3);
	}

	public boolean isBorderOpaque() {
		return false;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		g.setColor(new Color(92,121,53));
		g.fill3DRect(0, 0, width, height, true);
	}

}