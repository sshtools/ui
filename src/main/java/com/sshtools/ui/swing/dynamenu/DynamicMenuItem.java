package com.sshtools.ui.swing.dynamenu;

import javax.swing.Icon;

public interface DynamicMenuItem {

	public String getToolTipText();

	public String getName();

	public Icon getIcon();

	public String getUniqueId();
}