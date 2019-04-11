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
 */
package com.sshtools.ui.swing;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;

import com.sshtools.ui.swing.ScrollingPanel.ButtonMode;

@SuppressWarnings("serial")
public class SideBarTabber extends JPanel implements Tabber {
	private TabToolBar toolBar;
	private List<Tab> tabs;
	private List<TabAction> actions;
	private FolderBar folderBar;
	private JPanel viewPane;
	private CardLayout layout;

	public SideBarTabber() {
		this(true);
	}

	/**
	 * 
	 */
	public SideBarTabber(boolean showFolderBar) {
		super(new BorderLayout());
		tabs = new ArrayList<>();
		actions = new ArrayList<>();
		toolBar = new TabToolBar() {
			public int getFixedWidth() {
				return getFixedToolBarWidth();
			}
		};
		if (showFolderBar) {
			folderBar = new FolderBar(" ", new EmptyIcon(32, 32));
			folderBar.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(),
					BorderFactory.createEmptyBorder(0, 0, 4, 0)));
			toolBar.setFolderBar(folderBar);
		}
		JPanel centerPane = new JPanel(new BorderLayout());
		centerPane.setBorder(BorderFactory.createEmptyBorder(0, 4, 0, 0));
		viewPane = new JPanel(layout = new CardLayout());
		if (folderBar != null)
			centerPane.add(folderBar, BorderLayout.NORTH);
		centerPane.add(viewPane, BorderLayout.CENTER);
		add(toolBar, BorderLayout.WEST);
		add(centerPane, BorderLayout.CENTER);
	}
	
	public ButtonMode getButtonMode() {
		return toolBar.getButtonMode();
	}
	
	public void setButtonMode(ButtonMode buttonMode) {
		toolBar.setButtonMode(buttonMode);
	}

	public int getFixedToolBarWidth() {
		return toolBar.getFixedWidth();
	}

	public void setFixedToolBarWidth(int fixedWidth) {
		toolBar.setFixedWidth(fixedWidth);
	}

	public Tab getTabAt(int i) {
		return tabs.get(i);
	}

	public Component getComponent() {
		return this;
	}

	public void removeAllTabs() {
		tabs.clear();
		actions.clear();
		viewPane.invalidate();
		viewPane.removeAll();
		if (folderBar != null)
			folderBar.setAction(null);
		toolBar.removeAllActions();
		viewPane.validate();
	}

	public void applyTabs() {
		for (Tab t : tabs) {
			try {
				t.applyTab();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public boolean validateTabs() {
		for (Tab t : tabs) {
			try {
				if (!t.validateTab()) {
					setSelectedTabClass(t.getClass());
					return false;
				}
			} catch (TabValidationException tve) {
				setSelectedTabClass(tve.getTab().getClass());
				throw tve;
			}
		}
		return true;
	}

	public void addTab(Tab tab) {
		addTab(tab, false);
	}

	public void addTab(Tab tab, boolean sel) {
		String c = (tab.getTabCategory() == null) ? "Unknown" : tab.getTabCategory();
		TabAction action = new TabAction(tab.getTabIcon(), tab.getTabLargeIcon(), tab.getTabTitle(), tab.getTabToolTipText(),
				tab.getTabMnemonic(), layout, viewPane, c);
		tabs.add(tab);
		actions.add(action);
		String componentName = c + "/" + tab.getTabTitle();
		viewPane.add(tab.getTabComponent(), componentName);
		toolBar.addAction(action);
		if (sel || tabs.size() == 1) {
			layout.show(viewPane, componentName);
			if (folderBar != null)
				folderBar.setAction(action);
			toolBar.setSelectedContext(c);
		}
		// scrolling.setAvailableActions();
	}

	// Supporting classes
	class TabAction extends AppAction {
		CardLayout layout;
		JPanel viewPane;

		TabAction(Icon icon, Icon largeIcon, String name, String description, int mnemonic, CardLayout layout, JPanel viewPane,
				String category) {
			super(name);
			putValue(AppAction.LARGE_ICON, largeIcon);
			putValue(AppAction.SMALL_ICON, icon);
			putValue(AppAction.LONG_DESCRIPTION, description);
			putValue(AppAction.CATEGORY, category);
			this.layout = layout;
			this.viewPane = viewPane;
		}

		public boolean checkAvailable() {
			return true;
		}

		public void actionPerformed(ActionEvent evt) {
			if (folderBar != null)
				folderBar.setAction(this);
			layout.show(viewPane, (String) getValue(AppAction.CATEGORY) + "/" + (String) getValue(AppAction.NAME));
		}
	}

	/**
	 * @return
	 */
	public int getTabCount() {
		return tabs.size();
	}

	public void setSelectedTabClass(Class<?> selectedTabClass) {
		if (selectedTabClass != null) {
			for (Tab tab : tabs) {
				if (tab.getClass().equals(selectedTabClass)) {
					String c = (tab.getTabCategory() == null) ? "Unknown" : tab.getTabCategory();
					layout.show(viewPane, c + "/" + tab.getTabTitle());
					;
					if (folderBar != null)
						folderBar.setAction((Action) actions.get(tabs.indexOf(tab)));
					toolBar.setSelectedContext(c);
					return;
				}
			}
		}
	}
}