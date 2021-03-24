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
/* HEADER*/
package com.sshtools.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.miginfocom.swing.MigLayout;

/**
 * Simple Swing component that just shows an icon and some boldened text on a
 * darkened background. The icon and text can also be derived from an Action.
 * 
 * @author $Author: brett $
 */

@SuppressWarnings("serial")
public class FolderBar extends JPanel {

	// Private instance variables

	private JLabel textLabel;
	private JLabel iconLabel;
	private Action action;

	/**
	 * Construct a new FolderBar.
	 */

	public FolderBar() {
		this(null, null);

	}

	/**
	 * Construct a new FolderBar with some text.
	 * 
	 * @param text
	 *            text
	 */

	public FolderBar(String text) {
		this(text, null);

	}

	/**
	 * Construct a new FolderBar with some text and an icon.
	 * 
	 * @param text
	 *            text
	 * @param icon
	 *            icon
	 */

	public FolderBar(String text, Icon icon) {
		super(new BorderLayout());
		setOpaque(true);
		setBackground(getBackground().darker());
		add(textLabel = new JLabel(), BorderLayout.CENTER);
		JPanel p = new JPanel(new MigLayout());
		p.setOpaque(false);
		p.add(iconLabel = new JLabel());
		add(p, BorderLayout.WEST);
		textLabel.setVerticalAlignment(JLabel.CENTER);
		textLabel.setVerticalTextPosition(JLabel.BOTTOM);
		textLabel.setForeground(Color.lightGray);
		iconLabel.setVerticalAlignment(JLabel.CENTER);
		setFont(UIUtil.getFontOrDefault("Label.font").deriveFont(Font.BOLD));
		setIcon(icon);
		setText(text);
	}

	/**
	 * Set the font
	 * 
	 * @param font
	 *            font
	 */
	public void setFont(Font font) {
		super.setFont(font);
		if (textLabel != null) {
			textLabel.setFont(font);
		}
	}

	/**
	 * Get the action that built this folder bar.
	 * 
	 * @return action
	 */

	public Action getAction() {
		return action;
	}

	/**
	 * Set the icon and text from an action. The {@link Action.NAME} values is
	 * used to derive the text and {@link Action.ICON} for the icon.
	 * 
	 * @param action
	 */

	public void setAction(Action action) {
		this.action = action;
		setIcon(action == null ? null : (Icon) action
				.getValue(Action.SMALL_ICON));
		setText(action == null ? null : (String) action
				.getValue(Action.LONG_DESCRIPTION));
	}

	/**
	 * Set the text of this folder bar.
	 * 
	 * @param text
	 *            text
	 */

	public void setText(String text) {
		textLabel.setText(text);
	}

	/**
	 * Set the icon on this folder bar.
	 * 
	 * @param icon
	 *            icon
	 */

	public void setIcon(Icon icon) {
		iconLabel.setIcon(icon);
	}

}
