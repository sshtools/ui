package com.sshtools.ui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class ScrollingPanel extends JPanel implements ActionListener {

	protected JButton north;
	protected JButton south;
	protected JViewport viewport;
	protected int incr = 48;

	public ScrollingPanel(JComponent component) {
		setLayout(new ScrollerLayout());
		addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				setAvailableActions();
			}
		});
		north = new JButton(new ArrowIcon(SwingConstants.NORTH,
				UIManager.getColor("controlShadow"),
				UIManager.getColor("Button.foreground"),
				UIManager.getColor("controlLtHighlight")));
		JPanel northPanel = new JPanel(new BorderLayout());
		northPanel.add(north, BorderLayout.CENTER);
		northPanel.setPreferredSize(new Dimension(20, 20));
		south = new JButton(new ArrowIcon(SwingConstants.SOUTH,
				UIManager.getColor("controlShadow"),
				UIManager.getColor("Button.foreground"),
				UIManager.getColor("controlLtHighlight")));
		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.add(south, BorderLayout.CENTER);
		southPanel.setPreferredSize(new Dimension(20, 20));

		viewport = new JViewport();
		viewport.setView(component);

		add(northPanel);
		add(viewport);
		add(southPanel);

		north.addActionListener(this);
		south.addActionListener(this);

		setSize(new Dimension(100, 500));
		setAvailableActions();
	}

	public void setIncrement(int incr) {
		this.incr = incr;
	}

	public void actionPerformed(ActionEvent event) {
		Dimension view = new Dimension(getSize().width, getSize().height
				- north.getPreferredSize().height
				- south.getPreferredSize().height);
		Dimension pane = viewport.getView().getPreferredSize();
		Point top = viewport.getViewPosition();
		if (event.getSource() == north) {
			if (top.y < incr) {
				viewport.setViewPosition(new Point(0, 0));
			} else {
				viewport.setViewPosition(new Point(0, top.y - incr));
			}
		}
		if (event.getSource() == south) {
			int max = pane.height - view.height;
			if (top.y > (max - incr)) {
				view = viewport.getExtentSize();
				max = Math.max(pane.height - view.height, 0);
				viewport.setViewPosition(new Point(0, max));
			} else {
				viewport.setViewPosition(new Point(0, top.y + incr));
			}
		}
		setAvailableActions();
	}

	public void setAvailableActions() {
		Dimension view = viewport.getView().getSize();
		Dimension pane = viewport.getSize();
		Point top = viewport.getViewPosition();
		north.setEnabled(top.y > 0);
		south.setEnabled(top.y + pane.height < view.height);
	}

	class ScrollerLayout implements LayoutManager {

		public void addLayoutComponent(String name, Component comp) {
		}

		public void layoutContainer(Container parent) {
			int h = 0;
			int components = parent.getComponentCount();

			// First get the preferred height of top and bottom components
			for (int i = 0; i < components; i++) {
				Component c = parent.getComponent(i);
				if (i == 0 || i == components - 1) {
					h += c.getPreferredSize().height;
				}
			}

			// The rest of the space is for all other components
			int oy = (parent.getSize().height - h) / (components - 2);

			// Now reshape the components
			int y = 0;
			for (int i = 0; i < components; i++) {
				Component c = parent.getComponent(i);
				if (i == 0 || i == components - 1) {
					c.setBounds(0, y, parent.getWidth(),
							c.getPreferredSize().height);
					y += c.getPreferredSize().height;
				} else {
					c.setBounds(0, y, parent.getWidth(), oy);
					y += oy;
				}
			}
		}

		public Dimension minimumLayoutSize(Container parent) {
			int h = 0;
			int components = parent.getComponentCount();
			for (int i = 0; i < components; i++) {
				Component c = parent.getComponent(i);
				if (i == 0 || i == components - 1) {
					h += c.getPreferredSize().height;
				} else {
					h += c.getMinimumSize().height;
				}
			}
			return new Dimension(parent.getSize().width, h);
		}

		public Dimension preferredLayoutSize(Container parent) {
			int h = 0;
			int components = parent.getComponentCount();
			for (int i = 0; i < components; i++) {
				Component c = parent.getComponent(i);
				h += c.getPreferredSize().height;
			}
			return new Dimension(parent.getSize().width, h);
		}

		public void removeLayoutComponent(Component comp) {
		}

	}
}
