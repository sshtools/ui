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
package com.sshtools.ui.awt.tooltips;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.IllegalComponentStateException;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Window;

import com.sshtools.ui.awt.ImageTextLabel;

class TipWindow extends Window {
	private ImageTextLabel textLabel;
	private long lastShow;
	private boolean dismissed;
	private Component component;
	private String text;

	TipWindow(Frame owner) {
		super(owner);
		textLabel = new ImageTextLabel() {
			public void paint(Graphics g) {
				super.paint(g);
				g.setColor(getForeground());
				Dimension s = getSize();
				g.drawRect(0, 0, s.width - 1, s.height - 1);
			}
		};
		textLabel.setMargin(new Insets(2, 2, 2, 2));
		setLayout(new GridLayout(1, 1));
		add(textLabel);
	}

	boolean isDismissed() {
		return dismissed;
	}

	boolean isOutOfDate() {
		return (System.currentTimeMillis() > (lastShow + 5000));
	}

	synchronized void dismiss() {
		dismissed = true;
		hide();
	}

	synchronized void popup(int x, int y, Component component, String text) {

		invalidate();
		textLabel.setText(text);
		textLabel.setForeground(ToolTipManager.getInstance().foreground);
		textLabel.setBackground(ToolTipManager.getInstance().background);
		validate();
		pack();
		try {
			if (x != -1 && y != -1) {
				setLocation(x + 8, y + 8);
			} else {
				Point p = component.getLocationOnScreen();
				Dimension s = component.getSize();
				setLocation(p.x + 8, p.y + s.height + 8);
			}
			setVisible(true);
			toFront();
			lastShow = System.currentTimeMillis();
			dismissed = false;
		} catch (IllegalComponentStateException icse) {

		}
	}
}