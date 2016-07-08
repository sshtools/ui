package com.sshtools.ui.swing;

import java.awt.Font;

import javax.swing.UIManager;

public class FontUtil {

	public static Font getUIManagerTableFontOrDefault(String key) {
		Font f = UIManager.getFont(key);
		if(f == null) {
			f = new Font("Sans", Font.PLAIN, 12);
		}
		return f;
	}
	
	public static Font getUIManagerListFontOrDefault(String key) {
		Font f = UIManager.getFont(key);
		if(f == null) {
			f = new Font("Sans", Font.PLAIN, 12);
		}
		return f;
	}

	public static Font getUIManagerTextFieldFontOrDefault(String key) {
		Font f = UIManager.getFont(key);
		if(f == null) {
			f = new Font("Sans", Font.PLAIN, 12);
		}
		return f;
	}

	public static Font getUIManagerLabelFontOrDefault(String key) {
		Font f = UIManager.getFont(key);
		if(f == null) {
			f = new Font("Sans", Font.PLAIN, 12);
		}
		return f;
	}

	public static Font getUIManagerButtonFontOrDefault(String key) {
		Font f = UIManager.getFont(key);
		if(f == null) {
			f = new Font("Sans", Font.PLAIN, 12);
		}
		return f;
	}
}
