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

import javax.swing.Icon;

public class EmptyIcon implements Icon {

	public final static Icon EMPTY_SMALL_HORIZONTAL_SLIVER = new EmptyIcon(1,
			16);
	public final static Icon EMPTY_SMALL_VERTICAL_SLIVER = new EmptyIcon(16, 1);

	private int w;
	private int h;

	public EmptyIcon(int w, int h) {
		this.w = w;
		this.h = h;
	}

	public void paintIcon(Component c, Graphics g, int x, int y) {
	}

	public int getIconWidth() {
		return w;
	}

	public int getIconHeight() {
		return h;
	}

}
