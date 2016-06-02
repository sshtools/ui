/* HEADER */
package com.sshtools.ui.swing;

public abstract class ToggleableAction extends AppAction {
	public abstract void setSelected(boolean sel);

	public abstract boolean isSelected();
}