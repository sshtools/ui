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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * 
 * @author $author$
 */
public class IconWrapperPanel extends JPanel {
	private JLabel iconLabel;
	private JPanel westPanel;
	private Component component;

	/**
	 * Creates a new IconWrapperPanel object.
	 * 
	 * @param icon
	 * @param component
	 */
	public IconWrapperPanel(Icon icon, Component component) {
		super(new BorderLayout());
		// Create the west panel with the icon in it
		westPanel = new JPanel(new BorderLayout());
		westPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 4));
		westPanel.add(iconLabel = new JLabel(icon), BorderLayout.NORTH);
		// Build this panel
		setOpaque(false);
		add(westPanel, BorderLayout.WEST);
		if (component != null) {
			this.component = component;
			add(component, BorderLayout.CENTER);
		}
	}

	public void setComponent(Component component) {
		invalidate();
		if (this.component != null) {
			remove(this.component);
		}
		add(component, BorderLayout.CENTER);
		this.component = component;
		validate();
		repaint();
	}

	public void setBackground(Color background) {
		super.setBackground(background);
		if (westPanel != null) {
			westPanel.setBackground(background);
		}
	}

	public void setForeground(Color foreground) {
		super.setForeground(foreground);
		if (westPanel != null) {
			westPanel.setForeground(foreground);
		}
	}

	public void setOpaque(boolean opaque) {
		super.setOpaque(opaque);
		if (westPanel != null)
			westPanel.setOpaque(opaque);
	}

	public void setIcon(Icon icon) {
		iconLabel.setIcon(icon);
	}

	public void setIconPosition(String position) {
		invalidate();
		remove(westPanel);
		add(westPanel, position);
		validate();
	}
}