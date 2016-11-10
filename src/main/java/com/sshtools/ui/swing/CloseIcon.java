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
