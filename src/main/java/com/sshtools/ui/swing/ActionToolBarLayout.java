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
/*
 */
package com.sshtools.ui.swing;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;

public class ActionToolBarLayout implements LayoutManager {

	private boolean wrap;
	private int overunIndex;
	private int height;
	private Component expandComponent;
	private Map<Component, String> constraints = new HashMap<Component, String>();
	private int gap = 2;

	public ActionToolBarLayout(Component expandComponent) {
		overunIndex = -1;
		setExpandComponent(expandComponent);
	}

	public ActionToolBarLayout() {
		this(null);
	}

	public int getGap() {
		return gap;
	}

	public void setGap(int gap) {
		this.gap = gap;
	}

	public void setExpandComponent(Component expandComponent) {
		this.expandComponent = expandComponent;
	}

	public Component getExpandComponent() {
		return expandComponent;
	}

	public void addLayoutComponent(String name, Component comp) {
		constraints.put(comp, name);
	}

	public void removeLayoutComponent(Component c) {
		constraints.remove(c);
	}

	public void layoutContainer(Container target) {
		synchronized (target.getTreeLock()) {
			Insets insets = target.getInsets();
			Dimension s = target.getSize();
			int x = insets.left;
			int y = insets.top;
			int count = target.getComponentCount();
			Component c = null;
			Dimension z = null;
			overunIndex = -1;
			Dimension e = expandComponent != null ? expandComponent
					.getPreferredSize() : null;
			int expanderWidth = e != null ? e.width : 0;

			// Work out how many growing components there are
			int grower = 0;
			int taken = 0;
			int h = 0;
			for (int i = 0; i < count; i++) {
				c = target.getComponent(i);
				z = c.getPreferredSize();
				h = Math.max(z.height, h);
				String con = constraints.get(c);
				if ("grow".equals(con)) {
					grower++;
				} else {
					taken += z.width;
				}
				if (i != count - 1) {
					taken += gap;
				}
			}
			int available = s.width - taken - insets.left - insets.right - 1;
			int eachGrower = grower == 0 ? 0 : available / grower;

			for (int i = 0; i < count && overunIndex == -1; i++) {
				c = target.getComponent(i);
				if (c != expandComponent) {
					z = c.getPreferredSize();
					String con = constraints.get(c);
					if ("grow".equals(con)) {
						z.width = eachGrower;
					}
					z.width = Math.max(c.getMinimumSize().width, z.width);
					// int off = (h - z.height) / 2;
					int off = 0;
					if (z.width + x >= ((s.width - insets.left) - expanderWidth)) {
						if (wrap) {
							y += h;
							x = insets.left;
							c.setBounds(x, y + off, z.width, h);
						} else {
							overunIndex = i;
						}
					} else {
						c.setBounds(x, y + off, z.width, h);
					}
					x += z.width + gap;
				}
			}
			if (overunIndex != -1) {
				for (int i = overunIndex; i < count; i++) {
					c = target.getComponent(i);
					if (c != expandComponent) {
						c.setBounds(0, 0, 0, 0);
					}
				}
			}
			if (e != null) {
				if (overunIndex != -1) {
					Rectangle r = new Rectangle(s.width - insets.right
							- e.width, insets.top, e.width, h);
					expandComponent.setBounds(r);
				} else {
					expandComponent.setBounds(0, 0, 0, 0);
				}
			}
		}
	}

	public Dimension minimumLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			Insets insets = target.getInsets();
			int count = target.getComponentCount();
			Dimension d = new Dimension(insets.left, 0);
			Component c = null;
			Dimension s = null;
			for (int i = 0; i < count; i++) {
				c = target.getComponent(i);
				s = c.getMinimumSize();
				d.width += s.width + ((i == count - 1) ? 0 : gap);
				d.height = Math.max(d.height, insets.top + insets.bottom
						+ s.height);
			}
			return d;
		}
	}

	public Dimension preferredLayoutSize(Container target) {
		synchronized (target.getTreeLock()) {
			Insets insets = target.getInsets();
			int count = target.getComponentCount();
			Dimension s = target.getSize();
			Component c = null;
			Dimension z = null;
			Dimension e = expandComponent != null ? expandComponent
					.getPreferredSize() : null;
			int rowHeight = -1;
			int x = insets.left;
			int y = insets.top;
			int width = insets.left;
			int height = insets.top + insets.bottom;
			for (int i = 0; i < count; i++) {
				c = target.getComponent(i);
				if (c != expandComponent) {
					z = c.getPreferredSize();
					rowHeight = Math.max(rowHeight, z.height);
					if (z.width + x >= ((s.width - insets.left) - (e != null ? e.width
							: 0))) {
						overunIndex = i;
						if (wrap) {
							y += rowHeight;
							x = insets.left;
							height = Math.max(height, y + rowHeight
									+ insets.bottom);
							rowHeight = 0;
						} else {
							height = Math.max(height, y + rowHeight
									+ insets.bottom);
						}
					} else {
						height = Math
								.max(height, y + rowHeight + insets.bottom);
					}
					x += z.width + ((i == count - 1) ? 0 : gap);
					width = Math.max(width, x);
				}
			}
			width += insets.right;
			return new Dimension(width, height);
		}
	}

	public void setWrap(boolean wrap) {
		if (!this.wrap == wrap) {
			this.wrap = wrap;
			if (wrap) {
				overunIndex = -1;
			}
		}
	}

	public boolean isWrap() {
		return wrap;
	}

	public int getOverunIndex() {
		return overunIndex;
	}
}