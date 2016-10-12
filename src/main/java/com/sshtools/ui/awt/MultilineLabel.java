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
package com.sshtools.ui.awt;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.Panel;
import java.util.StringTokenizer;

/**
 * Swing component that takes a string, splits it up into lines based on the
 * newline character and displays each line.
 * 
 * @author $Author: brett $
 */
public class MultilineLabel extends Panel {
	// Private instance variables
	private GridBagConstraints constraints;

	private String text;
	private int alignment;

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
		invalidate();
		removeAll();
		StringTokenizer tok = new StringTokenizer(text, "\n"); //$NON-NLS-1$
		constraints.weighty = 0.0;
		constraints.weightx = 1.0;
		while (tok.hasMoreTokens()) {
			String t = tok.nextToken();
			// if (!tok.hasMoreTokens()) {
			// constraints.weighty = 1.0;
			// }
			Label l = new Label(t);
			UIUtil.gridBagAdd(this, l, constraints,
					GridBagConstraints.REMAINDER);
		}
		validate();
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

	/**
	 * Set the alignment. Uses <code>GridBagConstraints.anchor</code>
	 * 
	 * @param alignment
	 *            alignment
	 */
	public void setAlignment(int alignment) {
		constraints.anchor = alignment;
		setText(text);
	}
}