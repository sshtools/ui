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
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import com.sshtools.ui.swing.ScrollingPanel.ButtonMode;

import net.miginfocom.swing.MigLayout;

/**
 * A TabToolBar creates a vertical tool bar with actions that may be grouped
 * into contexts, in a style similar to navigation bar used on the left hand
 * side in Outlook. See the addIcon() method for details on which action
 * properties are required
 */
@SuppressWarnings("serial")
public class TabToolBar extends JPanel {
	// Private instance variables
	private List<ContextPanel> contextList;
	private ContextPanel selectedContext;
	private CardLayout cardLayout;
	private JPanel cardPanel;
	private FolderBar folderBar;
	private int fixedWidth = -1;
	private Color toolBarBackground;

	/**
	 * Construct a new TabToolBar
	 */
	public TabToolBar() {
		super(new MigLayout("ins 0, gap 0, wrap 1, fill"));
		toolBarBackground = UIManager.getColor("Panel.background").darker();
		// Intialise
		contextList = new ArrayList<>();
		cardLayout = new CardLayout();
		cardPanel = new JPanel(cardLayout);
		// Create the constraints for use later
		setBackground(toolBarBackground);
		setOpaque(true);
	}

	public void setFixedWidth(int fixedWidth) {
		this.fixedWidth = fixedWidth;
		doLayout();
	}

	public int getFixedWidth() {
		return fixedWidth;
	}

	/**
	 * Set the selected context
	 * 
	 * @param context context
	 */
	public void setSelectedContext(String context) {
		for (ContextPanel z : contextList) {
			if (context.equals(z.name)) {
				selectedContext = z;
				cardLayout.show(cardPanel, context);
				makeToolBar();
				return;
			}
		}
	}

	/**
	 * Sets a FolderBar associated with this tool bar. When actions are invoked,
	 * the FolderBar will change to show the action details for the selected
	 * action.
	 * 
	 * @param folder bar
	 */
	public void setFolderBar(FolderBar folderBar) {
		this.folderBar = folderBar;
	}

	/**
	 * Returns the FolderBar that is being changed upon actions..
	 * 
	 * @return folder bar
	 */
	public FolderBar getFolderBar() {
		return folderBar;
	}

	/**
	 * Add a new action to the toolbar, this must have a non null
	 * DefaultAction.CONTEXT property or an IllegalArgumentException will be
	 * thrown. The DefaultAction.LARGE_ICON property will be used for the icon,
	 * and the Action.NAME property will be used for the text.
	 * Action.LONG_DESCRIPTION will be used to any tool tip text.
	 * 
	 * @param action to build icon from
	 * @throws IllegalArgumentExcption if context not set
	 * @todo Make changes to the action reflect on the button correctly
	 */
	public void addAction(AppAction action) {
		// If there is a folder bar in use, and it has no action, then set it
		// to be this one
		if ((getFolderBar() != null) && (getFolderBar().getAction() == null)) {
			getFolderBar().setAction(action);
		}
		// Get the context and its panel (or create the panel if its new)
		String context = (String) action.getValue(AppAction.CATEGORY);
		if (context == null) {
			throw new IllegalArgumentException("AppAction.CONTEXT parameter of action must not be null");
		}
		ContextPanel contextPanel = null;
		for (ContextPanel p : contextList) {
			if (p.name.equals(context)) {
				contextPanel = p;
				break;
			}
		}
		if (contextPanel == null) {
			contextPanel = new ContextPanel(context);
			cardPanel.add(contextPanel.name, contextPanel);
			contextList.add(contextPanel);
		}
		if (selectedContext == null) {
			selectedContext = contextPanel;
		}
		// Add the action to the appropriate context panel and layout the bar
		TabActionButton button = contextPanel.addIcon(action);
		if (getParent() instanceof JViewport && ((JViewport) getParent()).getView() instanceof ScrollingPanel) {
			((ScrollingPanel) ((JViewport) getParent()).getView()).setIncrement(button.getPreferredSize().height);
		}
		makeToolBar();
	}

	/**
	 * 
	 */
	public void removeAllActions() {
		contextList.clear();
		cardPanel.removeAll();
		selectedContext = null;
		makeToolBar();
	}

	private void makeToolBar() {
		// Rebuild the panels
		invalidate();
		removeAll();
		for (ContextPanel p : contextList) {
			// First add the context button
			if (contextList.size() > 1) {
				add(new TabButton(p.getContextAction()) {
					@Override
					public Dimension getMaximumSize() {
						Dimension s = super.getMaximumSize();
						if (fixedWidth != -1)
							return new Dimension(fixedWidth, s.height);
						else
							return s;
					}
				}, "growx");
			}
			// If this is the selected action, the now add the panel
			if (p == selectedContext) {
				cardLayout.show(cardPanel, p.name);
				add(cardPanel, "dock center");
			}
		}
		validate();
		repaint();
		//
	}

	// Supporting classes
	public class ContextPanel extends JPanel {
		String name;
		AppAction action;
		GridBagConstraints gBC;
		ListPanel listPanel;
		ScrollingPanel scroller;

		public ContextPanel(String name) {
			super(new BorderLayout());
			listPanel = new ListPanel();
			listPanel.setOpaque(true);
			listPanel.setBackground(toolBarBackground);
			scroller = new ScrollingPanel(listPanel) {
				@Override
				public Dimension getMinimumSize() {
					if (fixedWidth != -1)
						return new Dimension(fixedWidth, super.getMinimumSize().height);
					else
						return super.getMinimumSize();
				}
			};
			scroller.setOpaque(false);
			add(scroller, BorderLayout.CENTER);
			this.name = name;
			setOpaque(false);
			setBackground(toolBarBackground);
			action = new ContextAction(name, this);
		}

		public AppAction getContextAction() {
			return action;
		}

		public TabActionButton addIcon(AppAction action) {
			TabActionButton b = new TabActionButton(action);
			listPanel.add(b);
			scroller.setAvailableActions();
			return b;
		}
	}

	public class ListPanel extends JPanel {
		public ListPanel() {
			super(new ListLayout());
			setOpaque(false);
			setBackground(toolBarBackground);
		}
	}

	public class ContextAction extends AppAction {
		ContextPanel context;

		ContextAction(String name, ContextPanel context) {
			super();
			putValue(AppAction.NAME, name);
			this.context = context;
		}

		public void actionPerformed(ActionEvent evt) {
			selectedContext = context;
			makeToolBar();
		}

		public boolean checkAvailable() {
			return true;
		}
	}

	public class TabButton extends JButton {
		public TabButton(AppAction a) {
			super(a);
			setFocusPainted(false);
			setDefaultCapable(false);
			setMargin(new Insets(1, 1, 1, 1));
		}
	}

	public class TabActionButton extends ActionButton {
		public TabActionButton(final AppAction a) {
			super(a, AppAction.LARGE_ICON);
			setHideText(false);
			setHorizontalTextPosition(SwingConstants.CENTER);
			setVerticalTextPosition(SwingConstants.BOTTOM);
		}

		public Dimension getMaximimumSize() {
			return fixedWidth == -1 ? super.getMaximumSize() : new Dimension(fixedWidth, super.getMaximumSize().height);
		}

		public Dimension getMinimumSize() {
			return fixedWidth == -1 ? super.getMinimumSize() : new Dimension(fixedWidth, super.getMinimumSize().height);
		}

		public Dimension getPreferredSize() {
			return fixedWidth == -1 ? super.getPreferredSize() : new Dimension(fixedWidth, super.getPreferredSize().height);
		}
	}
}
