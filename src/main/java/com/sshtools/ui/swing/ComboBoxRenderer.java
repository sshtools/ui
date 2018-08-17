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

import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public abstract class ComboBoxRenderer<T> implements ListCellRenderer<T> {
	private ListCellRenderer<Object> renderer;

	@SuppressWarnings("unchecked")
	public ComboBoxRenderer(JComboBox<T> combo) {
		renderer = (ListCellRenderer<Object>) combo.getRenderer();
		if (renderer == null)
			renderer = new DefaultListCellRenderer();
		combo.setRenderer(this);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends T> list, T value, int index, boolean isSelected,
			boolean cellHasFocus) {
		JComponent c = (JComponent) renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		if (c instanceof JLabel) {
			decorate((JLabel) c, list, value, index, isSelected, cellHasFocus);
		}
		return c;
	}

	protected abstract void decorate(JLabel label, JList<? extends T> list, T value, int index, boolean isSelected,
			boolean cellHasFocus);
}
