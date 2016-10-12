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
/* HEADER */
package com.sshtools.ui.awt;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Panel;

public class BorderPanel extends Panel {
	Insets insets;
	Color borderColor;

	public BorderPanel() {
		super();
		init();
	}

	public BorderPanel(LayoutManager layout) {
		super(layout);
		init();
	}

	void init() {
		insets = new Insets(1, 1, 1, 1);
	}

	public void setBorderColor(Color c) {
		borderColor = c;
		repaint();
	}

	public void paint(Graphics g) {
		if (borderColor != null) {
			g.setColor(borderColor);
			g.drawRect(0, 0, getSize().width - 1, getSize().height - 1);
		}
		super.paint(g);
	}

	public Insets getInsets() {
		return borderColor == null ? super.getInsets() : insets;
	}
}