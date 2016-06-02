/* HEADER */
package com.sshtools.ui.awt;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.SystemColor;

/**
 * <p>
 * Simple component to draw an etched separator line.
 * </p>
 * 
 * @author $Author: brett $
 */

public class Separator extends Canvas {

	/**
	 * Horizontal
	 */
	public final static int HORIZONTAL = 0;

	/**
	 * Vertical
	 */
	public final static int VERTICAL = 1;

	// Private instance variables
	private int orientation;
	private Color background;
	private Dimension preferredSize;

	/**
	 * <p>
	 * Construct a new Separator with a given orientation. Can be one of :-
	 * </p>
	 * 
	 * <ul>
	 * <li>{@link Seperator.HORIZONTAL}</li>
	 * <li>{@link Seperator.VERTICAL}</li>
	 * </ul>
	 * 
	 * @param orientation
	 *            orientation
	 */
	public Separator(int orientation) {
		super();
		setOrientation(orientation);
	}

	public void setBackground(Color background) {
		super.setForeground(background);
		this.background = background;
	}

	/**
	 * <p>
	 * Set the orientation of the separator. Can be one of :-
	 * </p>
	 * 
	 * <ul>
	 * <li>{@link Seperator.HORIZONTAL}</li>
	 * <li>{@link Seperator.VERTICAL}</li>
	 * </ul>
	 * 
	 * @param orientation
	 *            orientation
	 */
	public void setOrientation(int orientation) {
		this.orientation = orientation;
		repaint();
	}

	public void paint(Graphics g) {
		Dimension d = getSize();

		// First try basing color on background color of this component
		Color fg = background;

		// No background, traverse parents until a background color is found
		Component co = getParent();
		while (fg == null && co != null) {
			fg = co.getBackground();
			co = co.getParent();
		}

		// Use system color
		Color l1 = null;
		Color l2 = null;
		if (fg == null) {
			l1 = SystemColor.controlHighlight;
			l2 = SystemColor.controlShadow;
		} else {
			float[] hsbvals = new float[3];
			Color.RGBtoHSB(fg.getRed(), fg.getGreen(), fg.getBlue(), hsbvals);
			l1 = Color.getHSBColor(hsbvals[0], hsbvals[1], hsbvals[2] * 0.9f);
			l2 = Color.getHSBColor(hsbvals[0], hsbvals[1], hsbvals[2] * 1.1f);
		}

		g.setColor(l1);
		switch (orientation) {
		case HORIZONTAL:
			int c = (d.height - 1) / 2;
			g.drawLine(0, c, d.width, c);
			g.setColor(l2);
			g.drawLine(0, c - 1, d.width, c - 1);
			break;
		case VERTICAL:
			int m = (d.width - 1) / 2;
			g.drawLine(m, 0, m, d.height);
			g.setColor(l2);
			g.drawLine(m - 1, 0, m - 1, d.height - 1);
			break;
		}
	}

	public Dimension getPreferredSize() {
		if (preferredSize != null) {
			return preferredSize;
		}
		switch (orientation) {
		case HORIZONTAL:
			return new Dimension(0, 2);
		default:
			return new Dimension(2, 0);
		}
	}

	public void setPreferredSize(Dimension preferredSize) {
		this.preferredSize = preferredSize;
	}
}