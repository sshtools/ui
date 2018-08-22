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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;

/**
 * Swing component that provides a horziontal separator that is preceeded by
 * some text.
 *
 * @author $Author: brett $
 */
public class TitledSeparator extends JPanel {
	// Private instance variables
	/**
	 * Construct a titled separtor with some text
	 *
	 * @param text text
	 */
	public TitledSeparator(String text) {
		super(new GridBagLayout());
		setOpaque(false);
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.fill = GridBagConstraints.HORIZONTAL;
		gbc2.anchor = GridBagConstraints.WEST;
		gbc2.weightx = 0.0;
		gbc2.insets = new Insets(4, 0, 2, 2);
		JLabel l = new JLabel(text);
		l.setFont(FontUtil.getUIManagerLabelFontOrDefault("ToolTip.font"));
		UIUtil.jGridBagAdd(this, l, gbc2, GridBagConstraints.RELATIVE);
		gbc2.weightx = 1.0;
		UIUtil.jGridBagAdd(this, new JSeparator(JSeparator.HORIZONTAL), gbc2, GridBagConstraints.REMAINDER);
	}
}