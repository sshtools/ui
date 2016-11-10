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
/* HEDAER */

package com.sshtools.ui.swing;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.StringTokenizer;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Swing component that takes a string, splits it up into lines based on the
 * newline character and displays each line.
 * 
 * @author $Author: brett $
 */

public class MultilineLabel extends JPanel {

	// Private instance variables

	private GridBagConstraints constraints;

	private String text;

	/**
	 * Creates a new MultilineLabel object.
	 */

	public MultilineLabel() {

		this(""); //$NON-NLS-1$

	}

	/**
	 * Creates a new MultilineLabel object.
	 * 
	 * @param text
	 */

	public MultilineLabel(String text) {

		super(new GridBagLayout());

		constraints = new GridBagConstraints();

		constraints.anchor = GridBagConstraints.NORTHWEST;

		constraints.fill = GridBagConstraints.NONE;

		setText(text);

	}

	/**
	 * Set the font
	 * 
	 * @param f
	 *            font
	 */

	public void setFont(Font f) {

		super.setFont(f);

		for (int i = 0; i < getComponentCount(); i++) {

			getComponent(i).setFont(f);

		}

	}

	/**
	 * Set the font
	 * 
	 * @param text
	 */

	public void setText(String text) {

		this.text = text;

		removeAll();

		StringTokenizer tok = new StringTokenizer(text, "\n"); //$NON-NLS-1$

		constraints.weighty = 0.0;

		constraints.weightx = 1.0;

		while (tok.hasMoreTokens()) {

			String t = tok.nextToken();

			if (!tok.hasMoreTokens()) {

				constraints.weighty = 1.0;

			}

			UIUtil.jGridBagAdd(this, new JLabel(t), constraints,

			GridBagConstraints.REMAINDER);

		}

		revalidate();

		repaint();

	}

	/**
	 * Get the text
	 * 
	 * @return text
	 */

	public String getText() {

		return text;

	}

}
