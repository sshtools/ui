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
import javax.swing.KeyStroke;
import javax.swing.UIManager;

public class ActionButton extends JButton {
	// Private statics
	private final static Insets INSETS = new Insets(0, 0, 0, 0);
	// Private instance variables
	private boolean hideText;
	private boolean enablePlasticWorkaround;
	private Color hoverForeground, oldForeground;

	/**
	 * Creates a new button component from an AppAction. Action text will always
	 * be show.
	 * 
	 * @param action
	 *            action
	 */
	public ActionButton(AppAction action) {
		this(action, AppAction.SMALL_ICON);
	}

	/**
	 * Creates a new button component from an AppAction. If
	 * <code>showSelectiveText</code> is <code>true</code> and the action has a
	 * property with a name of {@link AppAction.TEXT_ON_TOOLBAR}and a value of
	 * <code>Boolean.TRUE</code> then text text will be shown on the
	 * 
	 * @param action
	 *            action
	 * @param showSelectiveText
	 *            show 'selective' text.
	 */
	public ActionButton(AppAction action, boolean showSelectiveText) {
		this(action, AppAction.SMALL_ICON, showSelectiveText);
	}

	/**
	 * Creates a new button component from an AppAction. Action text will always
	 * be shown.
	 * 
	 * @param action
	 *            action
	 * @param iconKey
	 *            key for icon
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
	 * @param action
	 *            action
	 * @param iconKey
	 *            key for icon
	 * @param showSelectiveText
	 *            show 'selective' text.
	 */
	public ActionButton(AppAction action, String iconKey,
			boolean showSelectiveText) {
		init(action, iconKey, showSelectiveText, false);
	}

	private void init(AppAction a, final String iconKey,
			boolean showSelectiveText, boolean alwaysShowText) {
		enablePlasticWorkaround = UIManager.getLookAndFeel().getClass()
				.getName().startsWith("com.jgoodies.looks.plastic.");
		setAction(a);
		addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				if (isEnabled()) {
					if (hoverForeground != null) {
						oldForeground = getForeground();
						setForeground(hoverForeground);
					}
					setBorderPainted(true);
					if (!enablePlasticWorkaround) {
						setContentAreaFilled(true);
					}
				}
			}

			public void mouseExited(MouseEvent e) {
				setBorderPainted(false);
				setContentAreaFilled(enablePlasticWorkaround);
				if (oldForeground != null) {
					setForeground(oldForeground);
					oldForeground = null;
				}
			}
		});
		a.addPropertyChangeListener(new PropertyChangeListener() {
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
			registerKeyboardAction(a,
					(KeyStroke) a.getValue(Action.ACCELERATOR_KEY),
					JButton.WHEN_IN_FOCUSED_WINDOW);
		}
		setIcon((Icon) a.getValue(iconKey));
		if ((Boolean.TRUE.equals(a.getValue(AppAction.TEXT_ON_TOOLBAR)) && showSelectiveText)
				|| alwaysShowText) {
			setHideText(false);
		} else {
			setHideText(true);
		}
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

	/**
	 * Set whether the button text for this component should be shown
	 * 
	 * @param hideText
	 *            hide button text
	 */
	public void setHideText(boolean hideText) {
		if (this.hideText != hideText) {
			firePropertyChange("hideText", this.hideText, hideText);
		}
		this.hideText = hideText;
		this.setHorizontalTextPosition(ActionButton.RIGHT);
		repaint();
	}

	public String getText() {
		return hideText ? null : super.getText();
	}

	public void setHoverForeground(Color hoverForeground) {
		this.hoverForeground = hoverForeground;
	}
}