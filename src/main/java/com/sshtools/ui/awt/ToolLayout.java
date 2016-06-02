/* HEADER */
package com.sshtools.ui.awt;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;

class ToolLayout implements LayoutManager {
	private Separator separator;

	ToolLayout(Separator separator) {
		this.separator = separator;
	}

	public void removeLayoutComponent(Component comp) {
	}

	public void layoutContainer(Container parent) {
		int c = parent.getComponentCount();
		Dimension s = new Dimension();
		Component comp;
		int x = 0;
		int w = 0;
		for (int i = 0; i < c; i++) {
			comp = parent.getComponent(i);
			if (comp == separator) {
				w = parent.getSize().width;
				comp.setBounds(0,
						parent.getSize().height
								- separator.getPreferredSize().height, w,
						separator.getPreferredSize().height);
			} else {
				w = comp.getPreferredSize().width;
				comp.setBounds(x, 0, w,
						parent.getSize().height
								- separator.getPreferredSize().height);
				x += w;
			}
		}
	}

	public void addLayoutComponent(String name, Component comp) {
	}

	public Dimension minimumLayoutSize(Container parent) {
		int c = parent.getComponentCount();
		Dimension s = new Dimension();
		Component comp;
		for (int i = 0; i < c; i++) {
			comp = parent.getComponent(i);
			s.width += comp.getMinimumSize().width;
			s.height = Math.max(s.height, comp.getMinimumSize().height);
		}
		s.height += separator.getMinimumSize().height;
		return s;
	}

	public Dimension preferredLayoutSize(Container parent) {
		int c = parent.getComponentCount();
		Dimension s = new Dimension();
		Component comp;
		for (int i = 0; i < c; i++) {
			comp = parent.getComponent(i);
			s.width += comp.getPreferredSize().width;
			s.height = Math.max(s.height, comp.getPreferredSize().height);
		}
		s.height += separator.getPreferredSize().height;
		return s;
	}

}