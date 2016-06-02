package com.sshtools.ui;

import java.awt.Component;

public interface OptionChooser {

	public static final int ERROR = 0;
	public static final int INFORMATION = 1;
	public static final int WARNING = 2;
	public static final int QUESTION = 3;
	public static final int UNCATEGORISED = -1;

	public Component getComponent();
}
