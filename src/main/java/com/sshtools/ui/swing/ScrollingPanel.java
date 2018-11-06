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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class ScrollingPanel extends JPanel implements ActionListener {
	public enum ButtonMode {
		VISIBILITY, VISIBILITY_AND_SIZE, ENABLEMENT
	}

	protected JButton north;
	protected JButton south;
	protected JViewport viewport;
	protected int incr = 48;
	protected int orientation;
	protected ButtonMode buttonMode = ButtonMode.ENABLEMENT;

	public ScrollingPanel(JComponent component) {
		this(component, SwingConstants.VERTICAL);
	}

	public ScrollingPanel(JComponent component, final int orientation) {
		this.orientation = orientation;
		setLayout(new ScrollerLayout(orientation));
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				setAvailableActions();
			}
		});
		north = new JButton(new ArrowIcon(orientation == SwingConstants.VERTICAL ? SwingConstants.NORTH : SwingConstants.WEST,
				UIManager.getColor("controlShadow"), UIManager.getColor("Button.foreground"),
				UIManager.getColor("controlLtHighlight")));
		south = new JButton(new ArrowIcon(orientation == SwingConstants.VERTICAL ? SwingConstants.SOUTH : SwingConstants.EAST,
				UIManager.getColor("controlShadow"), UIManager.getColor("Button.foreground"),
				UIManager.getColor("controlLtHighlight")));
		viewport = new JViewport();
		viewport.setView(component);
		add(north);
		add(viewport);
		add(south);
		north.addActionListener(this);
		south.addActionListener(this);
		setAvailableActions();
		addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (orientation == SwingConstants.VERTICAL) {
					if (e.getWheelRotation() < 0)
						incr(e.getWheelRotation() * -1 * incr);
					else
						decr(e.getWheelRotation() * incr);
				} else {
					if (e.getWheelRotation() < 0)
						incr(e.getWheelRotation() * -1 * incr);
					else
						decr(e.getWheelRotation() * incr);
				}
			}
		});
	}

	public void setButtonMode(ButtonMode buttonMode) {
		this.buttonMode = buttonMode;
		revalidate();
	}

	public ButtonMode getButtonMode() {
		return buttonMode;
	}

	public void setBordersPainted(boolean bordersPainted) {
		north.setBorderPainted(bordersPainted);
		south.setBorderPainted(bordersPainted);
	}

	public void setIncrement(int incr) {
		this.incr = incr;
	}

	public void decr(int incr) {
		Dimension view = orientation == SwingConstants.VERTICAL
				? new Dimension(getSize().width,
						getSize().height - north.getPreferredSize().height - south.getPreferredSize().height)
				: new Dimension(getSize().width - north.getPreferredSize().width - south.getPreferredSize().width,
						getSize().height);
		Dimension pane = viewport.getView().getPreferredSize();
		Point top = viewport.getViewPosition();
		if (orientation == SwingConstants.VERTICAL) {
			int max = pane.height - view.height;
			if (top.y > (max - incr)) {
				view = viewport.getExtentSize();
				max = Math.max(pane.height - view.height, 0);
				viewport.setViewPosition(new Point(0, max));
			} else {
				viewport.setViewPosition(new Point(0, top.y + incr));
			}
		} else {
			int max = pane.width - view.width;
			if (top.x > (max - incr)) {
				view = viewport.getExtentSize();
				max = Math.max(pane.width - view.width, 0);
				viewport.setViewPosition(new Point(max, 0));
			} else {
				viewport.setViewPosition(new Point(top.x + incr, 0));
			}
		}
	}

	public void incr(int incr) {
		Point top = viewport.getViewPosition();
		if (orientation == SwingConstants.VERTICAL) {
			if (top.y < incr) {
				viewport.setViewPosition(new Point(0, 0));
			} else {
				viewport.setViewPosition(new Point(0, top.y - incr));
			}
		} else {
			if (top.x < incr) {
				viewport.setViewPosition(new Point(0, 0));
			} else {
				viewport.setViewPosition(new Point(top.x - incr, 0));
			}
		}
	}

	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == north) {
			incr(incr);
		} else if (event.getSource() == south) {
			decr(incr);
		}
		setAvailableActions();
	}

	void setAvailableActions() {
		Dimension view = viewport.getView().getSize();
		Dimension pane = viewport.getSize();
		Point top = viewport.getViewPosition();
		if (orientation == SwingConstants.VERTICAL) {
			if (buttonMode == ButtonMode.ENABLEMENT) {
				north.setVisible(true);
				south.setVisible(true);
				north.setEnabled(top.y > 0);
				south.setEnabled(top.y + pane.height < view.height);
			} else {
				north.setEnabled(true);
				south.setEnabled(true);
				north.setVisible(top.y > 0);
				south.setVisible(top.y + pane.height < view.height);
			}
		} else {
			if (buttonMode == ButtonMode.ENABLEMENT) {
				north.setVisible(true);
				south.setVisible(true);
				north.setEnabled(top.x > 0);
				south.setEnabled(top.x + pane.width < view.width);
			} else {
				north.setEnabled(true);
				south.setEnabled(true);
				north.setVisible(top.x > 0);
				south.setVisible(top.x + pane.width < view.width);
			}
		}
	}

	public void doLayout() {
		super.doLayout();
		setAvailableActions();
	}

	class ScrollerLayout implements LayoutManager {
		private int orientation;

		ScrollerLayout(int orientation) {
			this.orientation = orientation;
		}

		public void addLayoutComponent(String name, Component comp) {
		}

		List<Component> getIncluded(Container parent) {
			List<Component> l = new ArrayList<Component>();
			for (Component c : parent.getComponents()) {
				if (buttonMode == ButtonMode.ENABLEMENT || buttonMode == ButtonMode.VISIBILITY
						|| (buttonMode == ButtonMode.VISIBILITY_AND_SIZE && c.isVisible()))
					l.add(c);
			}
			return l;
		}

		public void layoutContainer(Container parent) {
			List<Component> clist = getIncluded(parent);
			int components = clist.size();
			if (orientation == SwingConstants.VERTICAL) {
				int h = 0;
				// First get the preferred height of top and bottom components
				for (int i = 0; i < components; i++) {
					Component c = clist.get(i);
					if (i == 0 || i == components - 1) {
						h += c.getPreferredSize().height;
					}
				}
				// The rest of the space is for all other components
				int oy = (parent.getSize().height - h) / (parent.getComponentCount() - 2);
				// Now reshape the components
				int y = 0;
				for (int i = 0; i < components; i++) {
					Component c = clist.get(i);
					if (i == 0 || i == components - 1) {
						c.setBounds(0, y, parent.getWidth(), c.getPreferredSize().height);
						y += c.getPreferredSize().height;
					} else {
						c.setBounds(0, y, parent.getWidth(), oy);
						y += oy;
					}
				}
			} else {
				int w = 0;
				// First get the preferred height of top and bottom components
				for (int i = 0; i < components; i++) {
					Component c = clist.get(i);
					if (i == 0 || i == components - 1) {
						w += c.getPreferredSize().width;
					}
				}
				// The rest of the space is for all other components
				int ox = (parent.getSize().width - w) / (parent.getComponentCount() - 2);
				// Now reshape the components
				int x = 0;
				for (int i = 0; i < components; i++) {
					Component c = clist.get(i);
					if (i == 0 || i == components - 1) {
						c.setBounds(x, 0, c.getPreferredSize().width, parent.getHeight());
						x += c.getPreferredSize().width;
					} else {
						c.setBounds(x, 0, ox, parent.getHeight());
						x += ox;
					}
				}
			}
		}

		public Dimension minimumLayoutSize(Container parent) {
			int w = 0;
			int h = 0;
			List<Component> clist = getIncluded(parent);
			int components = clist.size();
			if (orientation == SwingConstants.VERTICAL) {
				for (int i = 0; i < components; i++) {
					Component c = clist.get(i);
					w = Math.max(w, c.getPreferredSize().width);
					if (i == 0 || i == components - 1) {
						h += c.getPreferredSize().height;
					} else {
						h += c.getMinimumSize().height;
					}
				}
			} else {
				for (int i = 0; i < components; i++) {
					Component c = clist.get(i);
					h = Math.max(h, c.getPreferredSize().height);
					if (i == 0 || i == components - 1) {
						w += c.getPreferredSize().width;
					} else {
						w += c.getMinimumSize().width;
					}
				}
			}
			return new Dimension(w, h);
		}

		public Dimension preferredLayoutSize(Container parent) {
			List<Component> clist = getIncluded(parent);
			int components = clist.size();
			if (orientation == SwingConstants.VERTICAL) {
				int h = 0;
				for (int i = 0; i < components; i++) {
					Component c = clist.get(i);
					h += c.getPreferredSize().height;
				}
				return new Dimension(parent.getSize().width, h);
			} else {
				int w = 0;
				for (int i = 0; i < components; i++) {
					Component c = clist.get(i);
					w += c.getPreferredSize().width;
				}
				return new Dimension(w, parent.getSize().height);
			}
		}

		public void removeLayoutComponent(Component comp) {
		}
	}
}
