/* HEADER */
package com.sshtools.ui.swing;

import java.awt.event.ActionEvent;

public class ToggledPopupMenuHiderAction extends AppAction {
	private ToggledPopupMenu menu;

	public ToggledPopupMenuHiderAction(ToggledPopupMenu menu) {
		this.menu = menu;
	}

	public ToggledPopupMenu getMenu() {
		return menu;
	}

	public void actionPerformed(ActionEvent evt) {
		menu.setVisible(false);
		menu.getAction().setSelected(false);
		// menu.ignoreNextToggleAction = false;
	}
}