/**
 * SSHTOOLS Limited licenses this file to you under the Apache
 * License, Version 2.0 (the "License"); you may not use this
 * file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.sshtools.ui.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * 
 * 
 * @author $author$
 */
public class ColorIcon implements Icon {
	// Private instance variables
	private Dimension size;
	private Color color;
	private Color borderColor;

	/**
	 * Creates a new ColorIcon object.
	 */
	public ColorIcon() {
		this(null);
	}

	/**
	 * Creates a new ColorIcon object.
	 * 
	 * @param color
	 */
	public ColorIcon(Color color) {
		this(color, null);
	}

	/**
	 * Creates a new ColorIcon object.
	 * 
	 * @param color
	 * @param borderColor
	 */
	public ColorIcon(Color color, Color borderColor) {
		this(color, null, borderColor);
	}

	/**
	 * Creates a new ColorIcon object.
	 * 
	 * @param color
	 * @param size
	 * @param borderColor
	 */
	public ColorIcon(Color color, Dimension size, Color borderColor) {
		setColor(color);
		setSize(size);
		setBorderColor(borderColor);
	}

	/**
	 * 
	 * 
	 * @param c
	 * @param g
	 * @param x
	 * @param y
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {
		g.setColor((color == null) ? Color.white : color);
		g.fillRect(x, y, getIconWidth(), getIconHeight());

		if (borderColor != null) {
			g.setColor(borderColor);
			g.drawRect(x, y, getIconWidth(), getIconHeight());
		}

		if (color == null) {
			g.setColor(Color.black);
			g.drawLine(x, y, x + getIconWidth(), y + getIconHeight());
		}
	}

	/**
	 * 
	 * 
	 * @param size
	 */
	public void setSize(Dimension size) {
		this.size = size;
	}

	/**
	 * 
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * 
	 * 
	 * @param borderColor
	 */
	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public int getIconWidth() {
		return (size == null) ? 16 : size.width;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public int getIconHeight() {
		return (size == null) ? 16 : size.height;
	}

	public Color getColor() {
		return color;
	}
}