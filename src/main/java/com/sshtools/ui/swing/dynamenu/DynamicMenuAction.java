package com.sshtools.ui.swing.dynamenu;

import com.sshtools.ui.swing.AppAction;
import com.sshtools.ui.swing.MenuAction;

/**
 * 
 * 
 * @author $author$
 */
public abstract class DynamicMenuAction extends AppAction implements MenuAction {
	private DynamicMenu menu;

	/**
	 * Creates a new MRUAction object.
	 * 
	 * @param model
	 */
	public DynamicMenuAction(DynamicMenuListModel model, String actionCommand,
			boolean considerStateOnUpdate) {
		menu = new DynamicMenu(this, model, actionCommand,
				considerStateOnUpdate);
		putValue(MenuAction.MENU, menu);
	}

	public void cleanUp() {
		menu.cleanUp();
	}
}
