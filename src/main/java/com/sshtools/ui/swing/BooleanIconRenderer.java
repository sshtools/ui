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
/* HEADER */

package com.sshtools.ui.swing;

import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * Extension of Swing {@link DefaultTableCellRenderer}that renders
 * <code>Boolean</code> values as one of two icons.
 *
 * @author $Author: brett $
 */

public class BooleanIconRenderer extends DefaultTableCellRenderer {

	// Private instance variables

	private Icon trueIcon;

	private Icon falseIcon;

	/**
	 * Construct a new BooleanIconRenderer.
	 *
	 * @param trueIcon
	 *            icon for <code>Boolean.TRUE</code> values
	 * @param falseIcon
	 *            icons for <code>Boolean.FALSE</code> vlaues
	 */

	public BooleanIconRenderer(Icon trueIcon, Icon falseIcon) {

		this.trueIcon = trueIcon;

		this.falseIcon = falseIcon;

		setHorizontalAlignment(JLabel.CENTER);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax
	 * .swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */

	public Component getTableCellRendererComponent(JTable table, Object value,

	boolean isSelected, boolean hasFocus, int row, int column) {

		super.getTableCellRendererComponent(table, value, isSelected, hasFocus,

		row, column);

		setText(null);

		setIcon(((Boolean) value).booleanValue() ? trueIcon : falseIcon);

		return this;

	}

	public String getText() {

		return null;

	}

}
