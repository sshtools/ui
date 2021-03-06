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
 * Created on 01-Aug-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package com.sshtools.ui.swing;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

public class ToggledPopupMenu extends JPopupMenu {

	private ToolBarToggleButton toggle;
	private ToggledPopupAction action;

	public ToggledPopupMenu(ToggledPopupAction action, String iconKey) {
		super((String) action.getValue(Action.NAME));
		this.action = action;

		toggle = new ToolBarToggleButton(action, iconKey, false);

		action.setPopup(this);
		action.setToggle(toggle);

		setLightWeightPopupEnabled(true);
		addPopupMenuListener(new PopupMenuListener() {

			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				if (!ToggledPopupMenu.this.toggle.isSelected()) {
					ToggledPopupMenu.this.toggle.setSelected(true);
				}
			}

			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				ToggledPopupMenu.this.toggle.setSelected(false);

			}

			public void popupMenuCanceled(PopupMenuEvent e) {
				ToggledPopupMenu.this.toggle.setSelected(false);
			}

		});
	}

	public JButton addButtonAction(AppAction action) {
		ActionButton item = new ActionButton(action, AppAction.SMALL_ICON,
				false);
		item.setOpaque(false);
		item.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		add(item);
		return item;
	}

	public ToggledPopupAction getAction() {
		return action;
	}

	public JToggleButton getToggleButton() {
		return toggle;
	}
}