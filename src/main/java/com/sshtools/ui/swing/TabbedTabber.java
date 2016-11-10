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

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * 
 * 
 * @author $author$
 * @version $Revision: 1.1.2.3 $
 */

public class TabbedTabber

extends ClosableTabbedPane implements Tabber {

	/**
	 * Creates a new TabbedTabber object.
	 */

	public TabbedTabber() {
		this(TOP);
	}

	/**
	 * Creates a new TabbedTabber object.
	 * 
	 * @param tabPlacement
	 */

	public TabbedTabber(int tabPlacement) {
		super(tabPlacement);
		addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (getSelectedIndex() != -1) {
					getTabAt(getSelectedIndex()).tabSelected();
				}
			}

		});

	}

	/**
	 * 
	 * 
	 * @param i
	 * 
	 * @return
	 */

	public Tab getTabAt(int i) {
		return ((TabPanel) getComponentAt(i)).getTab();

	}

	/**
	 * 
	 * 
	 * @return
	 */

	public boolean validateTabs() {
		for (int i = 0; i < getTabCount(); i++) {
			Tab tab = ((TabPanel) getComponentAt(i)).getTab();
			if (!tab.validateTab()) {
				setSelectedIndex(i);
				return false;
			}
		}
		return true;

	}

	/**
     * 
     */

	public void applyTabs() {
		for (int i = 0; i < getTabCount(); i++) {
			Tab tab = ((TabPanel) getComponentAt(i)).getTab();
			tab.applyTab();
		}

	}

	public synchronized Tab getSelectedTab() {
		int idx = getSelectedIndex();
		return idx == -1 ? null : getTabAt(idx);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sshtools.appframework.ui.Tabber#getComponent()
	 */
	public Component getComponent() {
		return this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.sshtools.appframework.ui.Tabber#removeAllTabs()
	 */
	public void removeAllTabs() {
		removeAll();
	}

	/**
	 * 
	 * 
	 * @param tab
	 */

	public void addTab(Tab tab) {
		addTab(tab.getTabTitle(), tab.getTabIcon(), new TabPanel(tab),
				tab.getTabToolTipText());

	}

	class TabPanel extends JPanel {

		private Tab tab;

		TabPanel(Tab tab) {
			super(new BorderLayout());
			this.tab = tab;
			setOpaque(false);
			add(tab.getTabComponent(), BorderLayout.CENTER);

		}

		public Tab getTab() {
			return tab;

		}

	}

}
