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

import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.KeyStroke;
import javax.swing.UIManager;

public class ToolBarToggleButton extends JToggleButton {
	// Private statics
	private final static Insets INSETS = new Insets(0, 0, 0, 0);
	// Private instance variables
	private boolean hideText;
	private boolean enablePlasticWorkaround;

	public ToolBarToggleButton(AbstractToggleableAction action) {
		this(action, true);
	}

	public ToolBarToggleButton(AbstractToggleableAction action,
			boolean useLargeIcon) {
		this(action, useLargeIcon, true);
	}

	public ToolBarToggleButton(AbstractToggleableAction action,
			boolean useLargeIcon, boolean showSelectiveText) {
		super();
		init(action,
				useLargeIcon ? AppAction.LARGE_ICON : AppAction.SMALL_ICON,
				showSelectiveText);
	}

	public ToolBarToggleButton(AbstractToggleableAction action, String iconKey,
			boolean showSelectiveText) {
		super();
		init(action, iconKey, showSelectiveText);
	}

	public void setSelected(boolean b) {
		System.out.println("Setting " + getAction().getValue(Action.NAME)
				+ " to " + b);
		super.setSelected(b);
		if (!isSelected()) {
			setBorderPainted(false);
			setContentAreaFilled(enablePlasticWorkaround);
		}
	}

	private void init(AbstractToggleableAction a, String iconKey,
			boolean showText) {
		enablePlasticWorkaround = UIManager.getLookAndFeel().getClass()
				.getName().startsWith("com.jgoodies.looks.plastic.");
		setAction(a);
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				if (isEnabled() || isSelected()) {
					setBorderPainted(true);
					if (!enablePlasticWorkaround) {
						setContentAreaFilled(true);
					}
				}
			}

			public void mouseExited(MouseEvent e) {
				if (!isSelected()) {
					setBorderPainted(false);
					setContentAreaFilled(enablePlasticWorkaround);
				}
			}
		});
		setBorderPainted(false);
		setContentAreaFilled(enablePlasticWorkaround);
		if (a != null && a.getValue(Action.ACCELERATOR_KEY) != null) {
			setMnemonic(0);
			registerKeyboardAction(a,
					(KeyStroke) a.getValue(Action.ACCELERATOR_KEY),
					JButton.WHEN_IN_FOCUSED_WINDOW);
		}
		setIcon((Icon) a.getValue(iconKey));
		if (Boolean.FALSE.equals(a.getValue(AppAction.TEXT_ON_TOOLBAR))
				|| !showText) {
			setHideText(true);
		} else {
			setHideText(false);
		}
		setVerticalTextPosition(JButton.BOTTOM);
		setHorizontalTextPosition(JButton.CENTER);
		a.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("selected")) {
					boolean sel = ((Boolean) evt.getNewValue()).booleanValue();
					setSelected(sel);
				}
			}
		});
		setSelected(a.isSelected());
	}

	public Insets getMargin() {
		return INSETS;
	}

	public boolean isRequestFocusEnabled() {
		return false;
	}

	public boolean isFocusTraversable() {
		return false;
	}

	public void setHideText(boolean hideText) {
		if (this.hideText != hideText) {
			firePropertyChange("hideText", this.hideText, hideText);
		}
		this.hideText = hideText;
		this.setHorizontalTextPosition(ToolBarToggleButton.RIGHT);
		repaint();
	}

	public String getText() {
		return hideText ? null : super.getText();
	}
}