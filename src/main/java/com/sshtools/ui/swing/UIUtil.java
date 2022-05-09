/**
 * SSHTOOLS Limited licenses this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
/* HEADER */
package com.sshtools.ui.swing;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.table.TableColumn;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * Useful UI utilities.
 */
public class UIUtil implements SwingConstants {
	/**
	 * Select an item in a {@link JList} given an items string value
	 * 
	 * @param string string to select in list
	 * @param list list
	 */
	public static void selectStringInList(String string, JList<String> list) {
		for (int i = 0; i < list.getModel().getSize(); i++) {
			if (String.valueOf(list.getModel().getElementAt(i)).equals(string)) {
				list.setSelectedIndex(i);
				return;
			}
		}
	}

	/**
	 * Select an item in a {@link JComboBox} given an items string value
	 * 
	 * @param string string to select in list
	 * @param list list
	 */
	public static void selectStringInList(String string, JComboBox<String> list) {
		for (int i = 0; i < list.getModel().getSize(); i++) {
			if (String.valueOf(list.getModel().getElementAt(i)).equals(string)) {
				list.setSelectedIndex(i);
				return;
			}
		}
	}

	/**
	 * Convert a string in the format of <code>x,y,width,height</code> in to a
	 * {@link Rectangle} object. Suitable for retrieving rectangles from
	 * property files, XML files etc. The supplied default value will be
	 * returned if the string is not in the correct format or is
	 * <code>null</code>.
	 * 
	 * @param string string in format <code>x,y,width,height</code>
	 * @param defaultValue default rectangle
	 * @return rectangle
	 */
	public static Rectangle stringToRectangle(String string, Rectangle defaultValue) {
		if (string == null) {
			return defaultValue;
		}
		StringTokenizer t = new StringTokenizer(string, ","); //$NON-NLS-1$
		try {
			return new Rectangle(Integer.parseInt(t.nextToken()), Integer.parseInt(t.nextToken()), Integer.parseInt(t.nextToken()),
					Integer.parseInt(t.nextToken()));
		} catch (Exception e) {
			return defaultValue;
		}
	}

	/**
	 * Convert a {@link Rectangle} object to a comma separated string in the
	 * format of <code>x,y,width,height</code>. Suitable for storing rectangles
	 * in property files, XML files etc.
	 * 
	 * @param rectangle rectangle
	 * @return comman separated string x,y,width,height
	 */
	public static String rectangleToString(Rectangle rectangle) {
		StringBuffer buf = new StringBuffer(String.valueOf(rectangle.x));
		buf.append(',');
		buf.append(String.valueOf(rectangle.y));
		buf.append(',');
		buf.append(String.valueOf(rectangle.width));
		buf.append(',');
		buf.append(String.valueOf(rectangle.height));
		return buf.toString();
	}

	/**
	 * Parse a string in the format of <code>[character]</code> to create an
	 * Integer that may be used for an action.
	 * 
	 * @param character mnemonic string
	 * @return mnemonic
	 */
	public static Integer parseMnemonicString(String string) {
		try {
			return Integer.valueOf(string);
		} catch (Throwable t) {
			return Integer.valueOf(-1);
		}
	}

	/**
	 * Parse a string in the format of [ALT+|CTRL+|SHIFT+] <keycode>to create a
	 * keystroke. This can be used to define accelerators from resource bundles
	 * 
	 * @param string accelerator string
	 * @return keystroke
	 */
	public static KeyStroke parseAcceleratorString(String string) {
		if (string == null || string.equals("")) { //$NON-NLS-1$
			return null;
		}
		StringTokenizer t = new StringTokenizer(string, "+"); //$NON-NLS-1$
		int mod = 0;
		int key = -1;
		while (t.hasMoreTokens()) {
			String x = t.nextToken();
			if (x.equalsIgnoreCase("ctrl")) { //$NON-NLS-1$
				mod += KeyEvent.CTRL_MASK;
			} else if (x.equalsIgnoreCase("shift")) { //$NON-NLS-1$
				mod += KeyEvent.SHIFT_MASK;
			} else if (x.equalsIgnoreCase("alt")) { //$NON-NLS-1$
				mod += KeyEvent.ALT_MASK;
			} else {
				try {
					java.lang.reflect.Field f = KeyEvent.class.getField(x);
					key = f.getInt(null);
				} catch (Throwable ex) {
					ex.printStackTrace();
				}
			}
		}
		if (key != -1) {
			KeyStroke ks = KeyStroke.getKeyStroke(key, mod);
			return ks;
		}
		return null;
	}

	/**
	 * Add a component to a container that is using a
	 * <code>GridBagLayout</code>, together with its constraints and the
	 * <code>GridBagConstraints.gridwidth</code> value.
	 * 
	 * @param parent parent container
	 * @param componentToAdd component to add
	 * @param constraints contraints
	 * @param pos grid width position
	 * 
	 * @throws IllegalArgumentException
	 */
	public static void jGridBagAdd(JComponent parent, Component componentToAdd, GridBagConstraints constraints, int pos) {
		if (!(parent.getLayout() instanceof GridBagLayout)) {
			throw new IllegalArgumentException(Messages.getString("UIUtil.parentMustHaveAGridBagLayout")); //$NON-NLS-1$
		}
		//
		GridBagLayout layout = (GridBagLayout) parent.getLayout();
		//
		constraints.gridwidth = pos;
		layout.setConstraints(componentToAdd, constraints);
		parent.add(componentToAdd);
	}

	/**
	 * Position a component on the screen (must be a
	 * <code>java.awt.Window</code> to be useful)
	 * 
	 * @param p postion from <code>SwingConstants</code>
	 * @param c component
	 */
	public static void positionComponent(int p, Component c) {
		positionComponent(p, c, c);
	}

	public static void positionComponent(int p, Component c, Component o) {
		Rectangle d = null;
		if(o == null) {
			GraphicsConfiguration gc = c.getGraphicsConfiguration();
			GraphicsDevice dev = gc.getDevice();
			GraphicsConfiguration def = dev.getDefaultConfiguration();
			d = def.getBounds();
		}
		else {
			d = o.getBounds();
		}
		positionComponent(p, c, d);
	}

	public static void positionComponent(int p, Component c, Rectangle d) {
		if (d == null) {
			Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
			d = new Rectangle(0, 0, s != null ? s.width : 800, s != null ? s.height : 600);
		}
		switch (p) {
		case NORTH_WEST:
			c.setLocation(d.x, d.y);
			break;
		case NORTH:
			c.setLocation(d.x + (d.width - c.getSize().width) / 2, d.y);
			break;
		case NORTH_EAST:
			c.setLocation(d.x + (d.width - c.getSize().width), d.y);
			break;
		case WEST:
			c.setLocation(d.x, d.y + (d.height - c.getSize().height) / 2);
			break;
		case SOUTH_WEST:
			c.setLocation(d.x, d.y + (d.height - c.getSize().height));
			break;
		case EAST:
			c.setLocation(d.x + d.width - c.getSize().width, d.y + (d.height - c.getSize().height) / 2);
			break;
		case SOUTH_EAST:
			c.setLocation(d.x + (d.width - c.getSize().width), d.y + (d.height - c.getSize().height) - 30);
			break;
		case CENTER:
			c.setLocation(d.x + (d.width - c.getSize().width) / 2, d.y + (d.height - c.getSize().height) / 2);
			break;
		}
	}

	/**
	 * Show an error message with detail
	 * 
	 * @param parent
	 * @param title
	 * @param exception
	 */
	public static void showErrorMessage(Component parent, String title, Throwable exception) {
		showErrorMessage(parent, null, title, exception);
	}

	/**
	 * Show an error message with toggable detail
	 * 
	 * @param parent
	 * @param mesg
	 * @param title
	 * @param exception
	 */
	public static void showErrorMessage(Component parent, String mesg, String title, Throwable exception) {
		showErrorMessage(parent, mesg, title, exception, true);
	}

	/**
	 * Show an error message with toggable detail
	 * 
	 * @param parent
	 * @param mesg
	 * @param title
	 * @param appenExceptionMessageToShortMessage
	 * @param exception
	 */
	public static void showErrorMessage(Component parent, String mesg, String title, Throwable exception,
			boolean appendExceptionMessageToShortMessage) {
		boolean details = false;
		while (true) {
			String[] opts = new String[] { details ? "Hide Details" : "Details", "Ok" };
			StringBuffer buf = new StringBuffer();
			if (mesg != null) {
				buf.append(mesg);
			}
			if (mesg == null || appendExceptionMessageToShortMessage || details) {
				appendException(exception, 0, buf, details);
			}
			MultilineLabel message = new MultilineLabel(buf.toString());
			int opt = JOptionPane.showOptionDialog(parent, message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.ERROR_MESSAGE,
					null, opts, opts[1]);
			if (opt == 0) {
				details = !details;
			} else {
				break;
			}
		}
	}

	private static void appendException(Throwable exception, int level, StringBuffer buf, boolean details) {
		try {
			if (((exception != null) && (exception.getMessage() != null)) && (exception.getMessage().length() > 0)) {
				if (details && (level > 0)) {
					buf.append("\n \nCaused by ...\n");
				}
				buf.append(exception.getMessage());
			}
			if (details) {
				if (exception != null) {
					if ((exception.getMessage() != null) && (exception.getMessage().length() == 0)) {
						buf.append("\n \nCaused by ...");
					} else {
						buf.append("\n \n");
					}
				}
				StringWriter sw = new StringWriter();
				if (exception != null) {
					exception.printStackTrace(new PrintWriter(sw));
				}
				buf.append(sw.toString());
			}
			try {
				java.lang.reflect.Method method = exception.getClass().getMethod("getCause", new Class[] {});
				Throwable cause = (Throwable) method.invoke(exception, (Object[]) null);
				if (cause != null) {
					appendException(cause, level + 1, buf, details);
				}
			} catch (Exception e) {
			}
		} catch (Throwable ex) {
		}
	}

	public static Image scaleWidth(int width, Image image, ImageObserver observer) {
		if (image == null) {
			return null;
		}
		double scale = (double) width / (double) image.getWidth(observer);
		return image.getScaledInstance(width, (int) (image.getHeight(observer) * scale), Image.SCALE_SMOOTH);
	}

	public static Image scaleHeight(int height, Image image, ImageObserver observer) {
		if (image == null) {
			return null;
		}
		double scale = (double) height / (double) image.getHeight(observer);
		return image.getScaledInstance((int) (image.getWidth(observer) * scale), height, Image.SCALE_SMOOTH);
	}

	public static SimpleAttributeSet getDefaultAttributeSet() {
		SimpleAttributeSet attrSet = new SimpleAttributeSet();
		// Font f = new Font("Arial", Font.PLAIN, 11);
		Font f = FontUtil.getUIManagerLabelFontOrDefault("Label.font");
		StyleConstants.setFontFamily(attrSet, f.getFamily());
		StyleConstants.setFontSize(attrSet, f.getSize());
		StyleConstants.setBold(attrSet, false);
		StyleConstants.setItalic(attrSet, (f.getStyle() & Font.ITALIC) != 0);
		return attrSet;
	}

	public static Image getImage(ImageIcon imageIcon) {
		return imageIcon != null ? imageIcon.getImage() : null;
	}

	public static Image getImage(Icon icon) {
		BufferedImage bim = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		icon.paintIcon(null, bim.getGraphics(), 0, 0);
		return bim;
	}

	public static Image getFrameImage(Icon icon) {
		if (icon == null) {
			return null;
		}
		BufferedImage bim = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		icon.paintIcon(null, bim.getGraphics(), 0, 0);
		return bim;
	}

	public static void setIconImage(Dialog d, Image image) {
		try {
			d.getClass().getMethod("setIconImage", new Class[] { Image.class }).invoke(d, new Object[] { image });
		} catch (Exception e) {
		}
	}

	public static Font getFontOrDefault(String key) {
		Font f = UIManager.getDefaults().getFont(key);
		if (f == null) {
			f = UIManager.getDefaults().getFont("defaultFont");
		}
		if (f == null) {
			f = new Font("Sans", Font.PLAIN, 12);
		}
		return f;
	}

	static int defaultDelay = ToolTipManager.sharedInstance().getInitialDelay();

	public static void showTooltip(Component component) {
		final ToolTipManager ttm = ToolTipManager.sharedInstance();
		if (ttm.getInitialDelay() == 0)
			ttm.setInitialDelay(defaultDelay);
		final int oldDelay = ttm.getInitialDelay();
		ttm.setInitialDelay(0);
		ttm.mouseMoved(new MouseEvent(component, 0, 0, 0, 0, 0, // X-Y of the
																// mouse for the
																// tool tip
				0, false));
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				ttm.setInitialDelay(oldDelay);
			}
		});
	}

	public static int getTotalPreferredWith(JTable table) {
		int w = 0;
		int sz = table.getColumnModel().getColumnCount();
		for (int i = 0; i < sz; i++) {
			w += table.getColumnModel().getColumn(i).getPreferredWidth();
		}
		return w;
	}

	public static void distributeColumns(JTable table, TableColumn... exceptions) {
		List<TableColumn> ex = Arrays.asList(exceptions);
		int w = getTotalPreferredWith(table);
		int sz = table.getColumnModel().getColumnCount();
		int vpw = getAvailableTableSpace(table);
		if(w != vpw) {
			int left = vpw - w;
			if(left > 0) {
				int each = left / ( sz - exceptions.length );
				for (int i = 0; i < sz; i++) {
					TableColumn col = table.getColumnModel().getColumn(i);
					if (!ex.contains(col)) {
						col.setPreferredWidth(
								col.getPreferredWidth() + (i == 0 ? each + (left - (each * (sz - exceptions.length))) : each));
					}
				}
			}
		}
	}

	public static int getAvailableTableSpace(JTable table) {
		int vpw = table.getParent() == null ? table.getPreferredScrollableViewportSize().width : table.getParent().getWidth();
		return vpw;
	}
}
