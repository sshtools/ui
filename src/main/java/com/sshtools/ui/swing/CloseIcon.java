/*
 *  SSHTools - Java SSH2 API
 *
 *  Copyright (C) 2002 Lee David Painter.
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Library General Public License
 *  as published by the Free Software Foundation; either version 2 of
 *  the License, or (at your option) any later version.
 *
 *  You may also distribute it and/or modify it under the terms of the
 *  Apache style J2SSH Software License. A copy of which should have
 *  been provided with the distribution.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  License document supplied with your distribution for more details.
 *
 */

package com.sshtools.ui.swing;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;
import javax.swing.UIManager;

public class CloseIcon

implements Icon {

	private int w;
	private int h;

	public CloseIcon(int s) {
		this(s, s);
	}

	public CloseIcon(int w, int h) {
		this.w = w;
		this.h = h;
	}

	public void paintIcon(Component c, Graphics g1, int x, int y) {
		Graphics2D g = (Graphics2D) g1;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(UIManager.getColor("TabbedPane.foreground"));
		int thickness = 1;

		// Box
		for (int i = 0; i < thickness; i++) {
			g.drawRoundRect(x + i, y + i, w - (i * 2), h - (i * 2), 2, 2);
			g.drawLine(x + i + thickness + 1, y + thickness + 1, x + w
					- thickness - thickness + i, y + h - thickness - 1);
			g.drawLine(x + i + thickness + 1, y + h - thickness - 1, x + w
					- thickness - thickness + i, y + thickness + 1);
		}
	}

	public int getIconWidth() {
		return w;
	}

	public int getIconHeight() {
		return h;
	}

}
