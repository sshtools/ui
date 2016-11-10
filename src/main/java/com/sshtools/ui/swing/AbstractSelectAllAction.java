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

import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.KeyStroke;

public abstract class AbstractSelectAllAction extends AbstractAction {
	public AbstractSelectAllAction() {
		putValue(Action.SMALL_ICON, new EmptyIcon(16, 16));
		putValue(Action.NAME, "Select All");
		putValue(Action.SHORT_DESCRIPTION, "Select All");
		putValue(Action.LONG_DESCRIPTION, "Select all items in the context");
		putValue(Action.MNEMONIC_KEY, new Integer('a'));
		putValue(Action.ACCELERATOR_KEY,
				KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_MASK));
	}
}