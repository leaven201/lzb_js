package netdesigntitle;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.Date;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import design.NetDesign_zs;


public class NSTitle extends JPanel {

	public static String TITLEORIGINALSIZE = "title_original_size";

	private String title;
	private Image icon;
	private Window window;
	private boolean textInCenter = false;
	//private Color titleColor = Color.red;
	private Color textColor = Color.BLACK;
	private Font textFontABC = new Font("Berlin Sans FB Demi", Font.PLAIN | Font.BOLD, 14);
	private Font textFontHanzi = new Font("微软雅黑", Font.PLAIN | Font.PLAIN , 15);
	private Font textFont = new Font("Berlin Sans FB Demi", Font.PLAIN | Font.BOLD, 14);
	
	private int xGap = 5;
	private int yGap = 5;

	private JLabel titleLabel = new JLabel();
	private JLabel iconLabel = new JLabel();
	private int titleHeight = 23;

	private Point pressPoint;

	private JButton closeButton;
	private JButton resizeButton;
	private JButton minButton;
//	private Icon restoreIcon = new ImageIcon(this.getClass().getResource("reset.png"));
	private ImageIcon restoreIcon = new ImageIcon(getClass().getResource("/resource/reset.png"));
	private Icon maxIcon = new ImageIcon(getClass().getResource("/resource/max.png"));
	private Icon minIcon = new ImageIcon(getClass().getResource("/resource/min.png"));
	private Icon closeIcon = new ImageIcon(getClass().getResource("/resource/close.png"));

	private boolean resizable = true;

	private boolean maxed;

	public NSTitle(Window window) {
		this(window, null);
	}

	public NSTitle(final Window window, String title) {
		this.window = window;
		this.setTitle(title);
		installListener();

	
			
		titleLabel.setFont(textFont);
		titleLabel.setForeground(textColor);
		
		this.closeButton = createButton(closeIcon);
		this.resizeButton = createButton(maxIcon);
		this.minButton = createButton(minIcon);
		ActionListener buttonListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == closeButton) {

					//					long x = System.currentTimeMillis()/1000;
						System.exit(1); 
				}
				else if (e.getSource() == resizeButton) {
					if (isMaxed()) {
						resetWindow();
					} else {
						setWindowMax();
					}
				} else if (e.getSource() == minButton) {
					((JFrame) window).setExtendedState(JFrame.ICONIFIED);
				}
			}
		};
		this.closeButton.addActionListener(buttonListener);
		this.resizeButton.addActionListener(buttonListener);
		this.minButton.addActionListener(buttonListener);

		this.setLayout(new FlowLayout(FlowLayout.RIGHT, 0, 1));
		if (window instanceof JFrame) {
			add(this.minButton);
		}
		add(this.resizeButton);
		add(this.closeButton);

	}

	public JButton createButton(Icon icon) {
		final JButton button = new JButton(icon);
		button.setMargin(new Insets(0, 0, 0, 0));
		button.setContentAreaFilled(false);
		button.setBorderPainted(true);
		button.setFocusable(false);
		return button;
	}

	public boolean isMaxed() {
		return maxed;
	}

	public void setWindowMax() {
		this.maxed = true;
		resizeButton.setIcon(restoreIcon);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.height -= 30;
		this.putClientProperty(TITLEORIGINALSIZE, window.getBounds());
		setWindowBounds(new Rectangle(screenSize));
	}

	public void resetWindow() {
		this.maxed = false;
		resizeButton.setIcon(maxIcon);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle rect = getOriginalBounds();
		if (rect == null) {
			rect = new Rectangle(screenSize.width / 4, screenSize.height / 4, screenSize.width / 2, screenSize.height / 2);
		}
		setWindowBounds(rect);
	}

	private void setWindowBounds(Rectangle rect) {
		this.putClientProperty(TITLEORIGINALSIZE, window.getBounds());
		window.setBounds(new Rectangle(rect));
	}

	private Rectangle getOriginalBounds() {
		Object obj = getClientProperty(TITLEORIGINALSIZE);
		if (obj != null) {
			return (Rectangle) obj;
		} else {
			return null;
		}
	}

	protected JPopupMenu getPopupMenu() {
		NSPopupMenu menu = new NSPopupMenu(getTitle());
		menu.setTitleSize(20);
		menu.setTitleFillColor(Color.DARK_GRAY);
		//menu.setTitleFillColor(new Color(0, 0, 0));
		menu.setTitleGradientColor(Color.BLACK);
		menu.setTitleColor(Color.GREEN);
		menu.setTitlePosition(NSPopupMenu.POSITION_TOP);
		JMenuItem item = new JMenuItem("还原");
		item.setEnabled(isMaxed() && isResizable());
		item.setIcon(restoreIcon);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetWindow();
			}
		});
		menu.add(item);
		item = new JMenuItem("最大化");
		item.setEnabled(!isMaxed() && isResizable());
		item.setIcon(maxIcon);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setWindowMax();
			}
		});
		menu.add(item);
		item = new JMenuItem("最小化");
		item.setEnabled(window instanceof JFrame);
		item.setIcon(minIcon);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				((JFrame) window).setExtendedState(JFrame.ICONIFIED);
			}
		});
		menu.add(item);
		menu.addSeparator();
		item = new JMenuItem("关闭");
		item.setIcon(closeIcon);
		item.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				window.setVisible(false);
				window.dispose();
			}
		});
		menu.add(item);

		if (window instanceof JFrame || window instanceof JDialog) {
			final JCheckBoxMenuItem cItem = new JCheckBoxMenuItem("可修改大小");
			cItem.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					if (window instanceof JFrame) {
						((JFrame) window).setResizable(cItem.isSelected());
					} else {
						((JDialog) window).setResizable(cItem.isSelected());
					}
				}
			});
			cItem.setSelected(isResizable());
			menu.add(cItem);
		}

		return menu;
	}

	private void installListener() {
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				pressPoint = e.getPoint();
			}

			public void mouseReleased(MouseEvent e) {
				pressPoint = null;
				if (SwingUtilities.isRightMouseButton(e)) {
					JPopupMenu menu = getPopupMenu();
					menu.show(NSTitle.this, e.getX(), e.getY());
				}
			}

			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2 && isResizable()) {
					if (isMaxed()) {
						resetWindow();
					} else {
						setWindowMax();
					}
				}
			}

			public void mouseEntered(MouseEvent e) {
				setCursor(Cursor.getDefaultCursor());
			}
		});
		this.addMouseMotionListener(new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {
			}

			public void mouseDragged(MouseEvent e) {
				if (pressPoint != null && !isMaxed()) {
					Point point = window.getLocation();
					double xoffset = e.getX() - pressPoint.getX();
					double yoffset = e.getY() - pressPoint.getY();
					window.setLocation((int) (point.getX() + xoffset), (int) (point.getY() + yoffset));
				}
			}
		});
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
		if(title!=null){
			 if(title.getBytes().length == title.length()){  				 
				 setTextFont(textFontABC);  
	       }else{  
	       	setTextFont(textFontHanzi); 
	       }
		}
		titleLabel.setText(title);
		this.repaint();
	}

	public Image getIcon() {
		return icon;
	}

	public void setIcon(Image iconImage) {
		this.icon = iconImage;
		if (iconImage != null) {
			ImageIcon icon = new ImageIcon(iconImage);
			iconLabel.setIcon(icon);
			this.repaint();
		}
	}

	public Dimension getPreferredSize() {
		Dimension size = super.getPreferredSize();
		int h = titleHeight;
		int labelHeight = titleLabel.getPreferredSize().height;
		if (labelHeight > h) {
			h = labelHeight;
		}
		int iconHeight = iconLabel.getPreferredSize().height + yGap;
		if (h < iconHeight) {
			h = iconHeight;
		}
		return new Dimension(size.width, titleHeight);
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		
		Color a = new Color(208,222,235);
		Color b = new Color(208,222,235);
				//(247,252,255);
				//(181,206,231);
				//(212, 219, 238);
		
	//	Color c = new Color(178, 202, 229);	
		
		
		g2d.setColor(a);
		Rectangle bounds = this.getBounds();
		g2d.fill(new Rectangle2D.Double(bounds.getX(), bounds.getY() + bounds.getHeight() *1/ 5, bounds.getWidth(), bounds.getHeight() *2/ 3));
		g2d.setColor(b);
		g2d.fill(new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight() *1/ 1));
//		g2d.setColor(c);
//		g2d.fill(new Rectangle2D.Double(bounds.getX(), bounds.getY(), bounds.getWidth()*1/ 3, bounds.getHeight()*1/ 3 ));
		
		
		
		int xOffset = xGap;
		if (this.icon != null) {
			Dimension imageSize = iconLabel.getPreferredSize();
			double y = bounds.height / 2 - imageSize.height / 2;
			SwingUtilities.paintComponent(g2d, iconLabel, this, xGap, (int) y, imageSize.width, imageSize.height);
			xOffset += imageSize.width;
		}
		if (getTitle() != null) {
			Dimension labelSize = titleLabel.getPreferredSize();
			double x = bounds.getX() + xOffset;
			if (textInCenter) {
				x = bounds.getWidth() / 2 - labelSize.width / 2.0;
			}
			double y = bounds.height / 2 - labelSize.height / 2;
			SwingUtilities.paintComponent(g2d, titleLabel, this, (int) x, (int) y, labelSize.width, labelSize.height);
		}
		g2d.dispose();

	}

	public Font getTextFont() {
		return textFont;
	}

	public void setTextFont(Font textFont) {
		this.textFont = textFont;
		titleLabel.setFont(this.textFont);
		this.repaint();
	}

	public Color getTextColor() {
		return textColor;
	}

	public void setTextColor(Color textColor) {
		this.textColor = textColor;
		titleLabel.setForeground(textColor);
		this.repaint();
	}

	public boolean isTextInCenter() {
		return textInCenter;
	}

	public void setTextInCenter(boolean textInCenter) {
		this.textInCenter = textInCenter;
		this.repaint();
	}

	public boolean isResizable() {
		return resizable;
	}

	public void setResizable(boolean resizable) {
		this.resizable = resizable;
		resizeButton.setEnabled(resizable);
	}
	
	

	
	
}