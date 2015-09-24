package com.sshtools.ui.swing;

import java.awt.Dimension;

import javax.swing.JSeparator;
import javax.swing.JToolBar;

@SuppressWarnings("serial")
public class ToolBarSeparator extends JSeparator {

	public ToolBarSeparator() {
		super(JSeparator.VERTICAL);

	}

	public ToolBarSeparator(int orientation) {
		super(orientation);

	}

	public Dimension getMaximumSize() {
		return (((JToolBar) getParent()).getOrientation() == JToolBar.HORIZONTAL) ? new Dimension(
				8, super.getMaximumSize().height) : new Dimension(
				super.getMaximumSize().width, 8);

	}

	public Dimension getPreferredSize() {
		return (((JToolBar) getParent()).getOrientation() == JToolBar.HORIZONTAL) ? new Dimension(
				4, super.getMinimumSize().height) : new Dimension(
				super.getMinimumSize().width, 4);

	}

	public Dimension getMinimumSize() {
		return (((JToolBar) getParent()).getOrientation() == JToolBar.HORIZONTAL) ? new Dimension(
				2, super.getMinimumSize().height) : new Dimension(
				super.getMinimumSize().width, 2);

	}

	public void doLayout() {
		boolean horizontal = ((JToolBar) getParent()).getOrientation() == JToolBar.HORIZONTAL;
		setOrientation(horizontal ? JSeparator.VERTICAL : JSeparator.HORIZONTAL);

	}

}
