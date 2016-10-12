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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.Icon;
import javax.swing.JCheckBoxMenuItem;

public class ActionCheckBoxMenuItem extends JCheckBoxMenuItem {
	public ActionCheckBoxMenuItem(ToggleableAction action) {
		super(action);
		Icon i = (Icon) action.getValue(AppAction.SMALL_ICON);
		if (i != null) {
			setIcon(i);
		}
		action.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("selected")) {
					boolean sel = ((Boolean) evt.getNewValue()).booleanValue();
					setSelected(sel);
					setIcon((Icon) getAction().getValue(AppAction.SMALL_ICON));
				}
			}
		});
		setSelected(action.isSelected());
	}
}