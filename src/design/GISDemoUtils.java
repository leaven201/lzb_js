/*
 * This source code is part of TWaver GIS 3.5
 *
 * Serva Software PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * Copyright 2010 Serva Software. All rights reserved.
 */

package design;

import java.awt.Component;
import javax.swing.JOptionPane;
import twaver.ElementAttribute;
import twaver.TWaverUtil;

public class GISDemoUtils {
	public static final  ElementAttribute createElementAttribute(String key, String dispalyName) {
		ElementAttribute attribute = new ElementAttribute();
		attribute.setUserPropertyKey(key);
		attribute.setDisplayName(dispalyName);
		attribute.setEditable(false);
		return attribute;
	}
	public static final void showTWaverInfo(Component refer){
		JOptionPane.showMessageDialog(TWaverUtil.getWindowForComponent(refer),
                "<html><font face='Dialog'><br><b>"+"TWaver GIS " + TWaverUtil.getVersionString() + " Demos" + "</b>" +
                "<br><br>" +
                "SERVA Software LLC.<br>" +
                "http://www.servasoftware.com<br>" +
                "Copyright(c)2010 Serva Software. All Rights Reserved.<br>" +
                "</font></html>",
                "About",
                JOptionPane.INFORMATION_MESSAGE,
                TWaverUtil.getIcon("/db/logo.png"));
	}
}