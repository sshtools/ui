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
import java.awt.Insets;
import java.util.Vector;

/**
 * A list layout puts elements in a vertical list, based on their vertical
 * preferred size. The width is expanded automatically.
 *
 * @version 1.0
 * @author Claude Duguay
 */
public class ListLayout extends AbstractLayout {
	public static final int CENTER = 0;
	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int BOTH = 3;
	protected Vector tabs = new Vector();
	protected Vector panels = new Vector();
	protected Component center;
	protected int index = 1;
	protected int alignment = BOTH;

	/**
	 * Constructs a ContextLayout with no gaps between components.
	 **/
	public ListLayout() {
		super();
	}

	/**
	 * Constructs a ContextLayout with no gaps between components and the
	 * specified alignment.
	 * 
	 * @param alignment
	 *            The LEFT, RIGHT, CENTER or BOTH alignment
	 **/
	public ListLayout(int alignment) {
		super();
		this.alignment = alignment;
	}

	/**
	 * Constructs a ContextLayout with the specified gaps.
	 * 
	 * @param hgap
	 *            The horizontal gap
	 * @param vgap
	 *            The vertical gap
	 **/
	public ListLayout(int hgap, int vgap) {
		super(hgap, vgap);
	}

	/**
	 * Constructs a ContextLayout with the specified gaps.
	 * 
	 * @param hgap
	 *            The horizontal gap
	 * @param vgap
	 *            The vertical gap
	 * @param alignment
	 *            The LEFT, RIGHT, CENTER or BOTH alignment
	 **/
	public ListLayout(int hgap, int vgap, int alignment) {
		super(hgap, vgap);
		this.alignment = alignment;
	}

	/**
	 * Returns the minimum dimensions needed to layout the components contained
	 * in the specified target container.
	 * 
	 * @param target
	 *            The Container on which to do the layout
	 **/
	public Dimension minimumLayoutSize(Container target) {
		Insets insets = target.getInsets();
		int w = 0;
		int h = insets.top;
		Dimension size;
		int ncomponents = target.getComponentCount();
		for (int i = 0; i < ncomponents; i++) {
			size = target.getComponent(i).getMinimumSize();
			if (size.width > w) {
				w = size.width;
			}
			h += size.height + vgap;
		}
		h += insets.bottom;
		return new Dimension(w + (hgap * 2), h);
	}

	/**
	 * Returns the preferred dimensions for this layout given the components in
	 * the specified target container.
	 * 
	 * @param target
	 *            The component which needs to be laid out
	 **/
	public Dimension preferredLayoutSize(Container target) {
		Insets insets = target.getInsets();
		int w = 0;
		int h = insets.top;
		Dimension size;
		int ncomponents = target.getComponentCount();
		for (int i = 0; i < ncomponents; i++) {
			size = target.getComponent(i).getPreferredSize();
			if (size.width > w) {
				w = size.width;
			}
			h += size.height + vgap;
		}
		h += insets.bottom;
		Dimension dimension = new Dimension(w + (hgap * 2), h);
		return dimension;
	}

	/**
	 * Lays out the specified container. This method will actually reshape the
	 * components in the specified target container in order to satisfy the
	 * constraints of the layout object.
	 * 
	 * @param target
	 *            The component being laid out
	 **/
	public void layoutContainer(Container parent) {
		Insets insets = parent.getInsets();
		int w = parent.getSize().width;
		Component comp;
		Dimension size;
		int position = insets.top;
		int ncomponents = parent.getComponentCount();

		for (int i = 0; i < ncomponents; i++) {
			comp = parent.getComponent(i);
			size = comp.getPreferredSize();

			int h = size.height - insets.top - insets.bottom;

			switch (alignment) {
			case CENTER: {
				int l = (w - size.width) / 2;
				comp.setBounds(insets.left + hgap + l, position, size.width
						- insets.left - insets.right - (hgap * 2), h);

				break;
			}
			case LEFT: {
				comp.setBounds(insets.left + hgap, position, size.width
						- insets.left - insets.right - (hgap * 2), h);

				break;
			}
			case RIGHT: {
				int l = w - size.width;
				comp.setBounds(insets.left + hgap + l, position, size.width
						- insets.left - insets.right - (hgap * 2), h);

				break;
			}
			default: {
				comp.setBounds(insets.left + hgap, position, w - insets.left
						- insets.right - (hgap * 2), h);

				break;
			}
			}

			position += (h + vgap);
		}
	}
}
