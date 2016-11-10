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
package com.sshtools.ui.swing.wizard;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class AbstractWizardModel implements WizardModel {

	private List listeners = new ArrayList();

	public void addWizardModelListener(WizardModelListener listener) {
		listeners.add(listener);
	}

	public void removeWizardModelListener(WizardModelListener listener) {
		listeners.remove(listener);
	}

	protected void firePageAdded(WizardPage page) {
		for (Iterator i = listeners.iterator(); i.hasNext();) {
			((WizardModelListener) i.next()).pageAdd(page);
		}
	}

	protected void firePageRemoved(WizardPage page) {
		for (Iterator i = listeners.iterator(); i.hasNext();) {
			((WizardModelListener) i.next()).pageRemoved(page);
		}
	}

	protected void firePageChanged(WizardPage oldPage, WizardPage newPage) {
		for (Iterator i = listeners.iterator(); i.hasNext();) {
			((WizardModelListener) i.next()).pageChanged(oldPage, newPage);
		}
	}

}
