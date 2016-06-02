/* HEADER */
package com.sshtools.ui.swing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JRadioButtonMenuItem;

public class ActionRadioButtonMenuItem extends JRadioButtonMenuItem {
	public ActionRadioButtonMenuItem(ToggleableAction action) {
		super(action);
		Icon i = (Icon) action.getValue(AppAction.SMALL_ICON);
		if (i != null) {
			setIcon(i);
		}
		action.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("selected")) {
					boolean sel = ((Boolean) evt.getNewValue()).booleanValue();
					setSelected(sel);
					setIcon((Icon) getAction().getValue(AppAction.SMALL_ICON));
				}
			}
		});
		setSelected(action.isSelected());
	}
}