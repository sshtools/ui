/* HEADER */
package com.sshtools.ui.swing;

import javax.swing.Icon;
import javax.swing.JMenuItem;

public class ActionMenuItem extends JMenuItem {
    public ActionMenuItem(AppAction action) {
        super(action);
        Icon i = (Icon) action.getValue(AppAction.SMALL_ICON);
        if (i != null) {
            setIcon(i);
        }
    }
}