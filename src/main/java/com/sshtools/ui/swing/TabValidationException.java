package com.sshtools.ui.swing;

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class TabValidationException extends RuntimeException {
	private Tab tab;
	private JComponent component;

	public TabValidationException(Tab tab, JComponent component) {
		super();
		this.component = component;
		this.tab = tab;
	}

	public TabValidationException(Tab tab, JComponent component, String message, Throwable cause) {
		super(message, cause);
		this.component = component;
		this.tab = tab;
	}

	public TabValidationException(Tab tab, JComponent component, String message) {
		super(message);
		this.component = component;
		this.tab = tab;
	}

	public TabValidationException(Tab tab, JComponent component, Throwable cause) {
		super(null, cause);
		this.component = component;
		this.tab = tab;
	}

	public Tab getTab() {
		return tab;
	}

	public JComponent getComponent() {
		return component;
	}
}
