package com.sshtools.ui;

import javax.swing.Icon;

public class Option {

	public static final Option CHOICE_YES = new Option(
			Messages.getString("OptionDialog.yes")); //$NON-NLS-1$

	public static final Option CHOICE_SHOW = new Option(
			Messages.getString("OptionDialog.show")); //$NON-NLS-1$

	public static final Option CHOICE_HIDE = new Option(
			Messages.getString("OptionDialog.hide")); //$NON-NLS-1$

	public static final Option CHOICE_NO = new Option(
			Messages.getString("OptionDialog.no")); //$NON-NLS-1$

	public static final Option CHOICE_CLOSE = new Option(
			Messages.getString("OptionDialog.close")); //$NON-NLS-1$

	public static final Option CHOICE_OK = new Option(
			Messages.getString("OptionDialog.ok")); //$NON-NLS-1$

	public static final Option CHOICE_YES_TO_ALL = new Option(
			Messages.getString("OptionDialog.yesToAll")); //$NON-NLS-1$

	public static final Option CHOICE_CANCEL = new Option(
			Messages.getString("OptionDialog.cancel")); //$NON-NLS-1$

	public static final Option CHOICES_YES_NO[] = { CHOICE_YES, CHOICE_NO };

	public static final Option CHOICES_OK_CANCEL[] = { CHOICE_OK, CHOICE_CANCEL };

	public static final Option CHOICES_OK[] = { CHOICE_OK };

	public static final Option CHOICES_CLOSE[] = { CHOICE_CLOSE };

	private String text;
	private String toolTipText;
	private int mnemonic;
	private Icon icon;

	/**
	 * Creates a new Option object. The first character of the text will be the
	 * mnemonic and the tool tip will be identical to the text.
	 * 
	 * @param text
	 */
	public Option(String text) {
		this(text, text, text.length() == 0 ? -1 : text.charAt(0));
	}

	/**
	 * Creates a new Option object.
	 * 
	 * @param text
	 * @param toolTipText
	 * @param mnemonic
	 */
	public Option(String text, String toolTipText, int mnemonic) {
		this(text, toolTipText, mnemonic, null);
	}

	/**
	 * Creates a new Option object.
	 * 
	 * @param text
	 * @param toolTipText
	 * @param mnemonic
	 * @param icon
	 *            icon
	 */
	public Option(String text, String toolTipText, int mnemonic, Icon icon) {
		this.text = text;
		this.toolTipText = toolTipText;
		this.mnemonic = mnemonic;
		this.icon = icon;
	}

	public String getText() {
		return text;
	}

	public int getMnemonic() {
		return mnemonic;
	}

	public String getToolTipText() {
		return toolTipText;
	}

	public Icon getIcon() {
		return icon;
	}
}