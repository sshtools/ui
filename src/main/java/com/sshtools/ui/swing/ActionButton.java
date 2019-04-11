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

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class ActionButton extends JButton {
	// Private statics
	private final static Insets INSETS = new Insets(4, 4, 4, 4);
	// Private instance variables
	private boolean hideText;
	private boolean enablePlasticWorkaround;
	private Color hoverForeground, oldForeground, oldBackground;

	/**
	 * Creates a new button component from an AppAction. Action text will always
	 * be show.
	 * 
	 * @param action action
	 */
	public ActionButton(AppAction action) {
		this(action, Action.SMALL_ICON);
	}

	/**
	 * Creates a new button component from an AppAction. If
	 * <code>showSelectiveText</code> is <code>true</code> and the action has a
	 * property with a name of {@link AppAction.TEXT_ON_TOOLBAR}and a value of
	 * <code>Boolean.TRUE</code> then text text will be shown on the
	 * 
	 * @param action action
	 * @param showSelectiveText show 'selective' text.
	 */
	public ActionButton(AppAction action, boolean showSelectiveText) {
		this(action, Action.SMALL_ICON, showSelectiveText);
	}

	/**
	 * Creates a new button component from an AppAction. Action text will always
	 * be shown.
	 * 
	 * @param action action
	 * @param iconKey key for icon
	 */
	public ActionButton(AppAction action, String iconKey) {
		init(action, iconKey, false, true);
	}

	/**
	 * Creates a new button component from an AppAction. If
	 * <code>showSelectiveText</code> is <code>true</code> and the action has a
	 * property with a name of {@link AppAction.TEXT_ON_TOOLBAR}and a value of
	 * <code>Boolean.TRUE</code> then text text will be shown on the
	 * 
	 * @param action action
	 * @param iconKey key for icon
	 * @param showSelectiveText show 'selective' text.
	 */
	public ActionButton(AppAction action, String iconKey, boolean showSelectiveText) {
		init(action, iconKey, showSelectiveText, false);
	}

	private void init(AppAction a, final String iconKey, boolean showSelectiveText, boolean alwaysShowText) {
		enablePlasticWorkaround = UIManager.getLookAndFeel().getClass().getName().startsWith("com.jgoodies.looks.plastic.");
		setAction(a);
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				if (isEnabled()) {
					if (hoverForeground != null) {
						oldForeground = getForeground();
						setForeground(hoverForeground);
					}
					setBorderPainted(true);
					oldBackground = getBackground();
					setBackground(new Color(0, true));
					if (!enablePlasticWorkaround) {
						setContentAreaFilled(true);
					}
				}
			}

			@Override
			public void mouseExited(MouseEvent e) {
				setBorderPainted(false);
				setContentAreaFilled(enablePlasticWorkaround);
				setBackground(Color.GREEN);
				if (oldForeground != null) {
					setForeground(oldForeground);
					oldForeground = null;
				}
				if (oldBackground != null) {
					setForeground(oldForeground);
					oldForeground = null;
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				setBackground(new Color(0, true));
			}
		});
		a.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(iconKey)) {
					Icon icon = (Icon) evt.getNewValue();
					ActionButton.this.setIcon(icon);
					ActionButton.this.invalidate();
					ActionButton.this.repaint();
				}
			}
		});
		setBorderPainted(false);
		setContentAreaFilled(enablePlasticWorkaround);
		if (a != null && a.getValue(Action.ACCELERATOR_KEY) != null) {
			setMnemonic(0);
			registerKeyboardAction(a, (KeyStroke) a.getValue(Action.ACCELERATOR_KEY), JComponent.WHEN_IN_FOCUSED_WINDOW);
		}
		setIcon((Icon) a.getValue(iconKey));
		if ((Boolean.TRUE.equals(a.getValue(AppAction.TEXT_ON_TOOLBAR)) && showSelectiveText) || alwaysShowText) {
			setHideText(false);
		} else {
			setHideText(true);
		}
	}

	@Override
	public Insets getMargin() {
		Insets insets = UIManager.getInsets("Button.margin");
//		if (insets == null)
//			return INSETS;
		return insets;
	}

	@Override
	public boolean isRequestFocusEnabled() {
		return false;
	}

	@Override
	public boolean isFocusTraversable() {
		return false;
	}

	/**
	 * Set whether the button text for this component should be shown
	 * 
	 * @param hideText hide button text
	 */
	public void setHideText(boolean hideText) {
		if (this.hideText != hideText) {
			firePropertyChange("hideText", this.hideText, hideText);
		}
		this.hideText = hideText;
		this.setHorizontalTextPosition(SwingConstants.RIGHT);
		repaint();
	}

	@Override
	public String getText() {
		return hideText ? null : super.getText();
	}

	public void setHoverForeground(Color hoverForeground) {
		this.hoverForeground = hoverForeground;
	}
}