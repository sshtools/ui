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
package com.sshtools.ui.swing.dynamenu;

import com.sshtools.ui.swing.AppAction;
import com.sshtools.ui.swing.MenuAction;

/**
 * 
 * 
 * @author $author$
 */
public abstract class DynamicMenuAction extends AppAction implements MenuAction {
	private DynamicMenu menu;

	/**
	 * Creates a new MRUAction object.
	 * 
	 * @param model
	 */
	public DynamicMenuAction(DynamicMenuListModel model, String actionCommand,
			boolean considerStateOnUpdate) {
		menu = new DynamicMenu(this, model, actionCommand,
				considerStateOnUpdate);
		putValue(MenuAction.MENU, menu);
	}

	public void cleanUp() {
		menu.cleanUp();
	}
}
