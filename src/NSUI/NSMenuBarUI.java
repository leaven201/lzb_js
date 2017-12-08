package NSUI;

import java.awt.*;

import javax.swing.*;
import javax.swing.plaf.*;
import javax.swing.plaf.basic.*;

public class NSMenuBarUI  extends BasicMenuBarUI {
	  public static ComponentUI createUI( JComponent x) {
		  return new NSMenuBarUI();
	  }

//	  public void paint( Graphics g, JComponent c) {
//	    super.paint( g, c);
//	  }
}
