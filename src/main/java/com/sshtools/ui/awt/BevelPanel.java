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
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.LayoutManager;

/**
 * <p>
 * <code>Panel</code> that draws a 3d border around the edge. The edge, or
 * bevel, can be one of :-
 * </p>
 * 
 * <ul>
 * <li><code>BevelPanel.NONE</code></li>
 * <li><code>BevelPanel.LOWERED</code></li>
 * <li><code>BevelPanel.RAISED</code></li>
 * 
 * <p>
 * </p>
 * 
 * @author $Author: brett $
 */
public class BevelPanel extends java.awt.Panel {
	// Public static

	/**
	 * No border
	 */
	public static final int NONE = 0;

	/**
	 * Lowered bevel
	 */
	public static final int LOWERED = 1;

	/**
	 * Raised bevel
	 */
	public static final int RAISED = 2;

	// Private instance variables

	private int type;

	/**
	 * Create a panel with the specified type of bevel.
	 * 
	 * @param layout
	 *            layout manager
	 */
	public BevelPanel(int bevel) {
		this(bevel, null);
	}

	/**
	 * Create a panel with the specified type of bevel.
	 * 
	 * @param type
	 *            bevel type
	 * @param layout
	 *            layout manager
	 */
	public BevelPanel(int type, LayoutManager layout) {
		super();
		if (layout != null) {
			setLayout(layout);
		}
		setType(type);
	}

	/**
	 * Set the bevel type. Can be one of :- </p>
	 * 
	 * <ul>
	 * <li><code>BevelPanel.NONE</code></li>
	 * <li><code>BevelPanel.LOWERED</code></li>
	 * <li><code>BevelPanel.RAISED</code></li>
	 * 
	 * @param type
	 *            bevel type
	 */
	public void setType(int type) {
		this.type = type;
		repaint();
	}

	/**
	 * Return insets sufficient for bevel and label drawing space.
	 */
	public Insets getInsets() {
		return new Insets(2, 2, 2, 2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Component#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		super.paint(g);
		Dimension d = getSize();
		Color bg = getBackground();
		if (bg == null) {
			bg = Color.gray;
		}
		Color si = bg.darker();
		Color so = si.darker();
		Color hi = bg.brighter();
		Color ho = hi.brighter();
		if (type == LOWERED) {
			g.setColor(si);
			g.drawLine(0, 0, 0, d.height - 1);
			g.drawLine(1, 0, d.width - 1, 0);
			g.setColor(so);
			g.drawLine(1, 1, 1, d.height - 2);
			g.drawLine(2, 1, d.width - 2, 1);
			g.setColor(ho);
			g.drawLine(1, d.height - 1, d.width - 1, d.height - 1);
			g.drawLine(d.width - 1, 1, d.width - 1, d.height - 2);
			g.setColor(hi);
			g.drawLine(2, d.height - 2, d.width - 2, d.height - 2);
			g.drawLine(d.width - 2, 2, d.width - 2, d.height - 3);
		} else {
			if (type == RAISED) {
				g.setColor(ho);
				g.drawLine(0, 0, 0, d.height - 2);
				g.drawLine(1, 0, d.width - 2, 0);
				g.setColor(hi);
				g.drawLine(1, 1, 1, d.height - 3);
				g.drawLine(2, 1, d.width - 3, 1);
				g.setColor(so);
				g.drawLine(0, d.height - 1, d.width - 1, d.height - 1);
				g.drawLine(d.width - 1, 0, d.width - 1, d.height - 2);
				g.setColor(si);
				g.drawLine(1, d.height - 2, d.width - 2, d.height - 2);
				g.drawLine(d.width - 2, 1, d.width - 2, d.height - 3);
			}
		}
	}
}
