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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.Icon;

/**
 * 
 * 
 * @author $author$
 */
public class StrokeIcon implements Icon {
	// Private instance variables
	private Dimension size;
	private Stroke stroke;

	/**
	 * Creates a new ColorIcon object.
	 */
	public StrokeIcon() {
		this(null);
	}

	/**
	 * Creates a new ColorIcon object.
	 * 
	 * @param color
	 */
	public StrokeIcon(Stroke stroke) {
		this(stroke, null);
	}

	/**
	 * Creates a new ColorIcon object.
	 * 
	 * @param color
	 * @param size
	 * @param borderColor
	 */
	public StrokeIcon(Stroke stroke, Dimension size) {
		setStroke(stroke);
		setSize(size);
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
		Graphics2D g2 = (Graphics2D) g;
		g2.setStroke(stroke);
		g2.drawLine(x, y, x + getIconWidth(), y);
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
	public void setStroke(Stroke stroke) {
		this.stroke = stroke;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public int getIconWidth() {
		return (size == null) ? 48 : size.width;
	}

	/**
	 * 
	 * 
	 * @return
	 */
	public int getIconHeight() {
		return (size == null) ? 16 : size.height;
	}
}