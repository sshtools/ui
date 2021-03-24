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
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class ActionToolBar extends JToolBar {
	private ExpandToolBarToggleButton button;

	private int buttonIndex;

	/**
	 * 
	 */
	public ActionToolBar() {
		super();
		init();
	}

	/**
	 * @param orientation
	 */
	public ActionToolBar(int orientation) {
		super(orientation);
		init();
	}

	/**
	 * @param name
	 */
	public ActionToolBar(String name) {
		super(name);
		init();
	}

	/**
	 * @param name
	 * @param orientation
	 */
	public ActionToolBar(String name, int orientation) {
		super(name, orientation);
		init();
	}

	public void setWrap(boolean wrap) {
		boolean oldWrap = isWrap();
		if (wrap != oldWrap) {
			setLayout(wrap);
			checkButton();
		}
	}

	public boolean isWrap() {
		return getLayout() instanceof ActionToolBarLayout
				&& ((ActionToolBarLayout) getLayout()).isWrap();
	}

	@Override
	public void updateUI() {
		super.updateUI();
		setLayout(isWrap());
        invalidate();
	}

	private void setLayout(boolean wrap) {
		if (!(getLayout() instanceof ActionToolBarLayout)) {
			setLayout(new ActionToolBarLayout());
		}
		((ActionToolBarLayout) getLayout()).setWrap(wrap);
	}

	private void init() {
		ActionToolBarLayout layout = new ActionToolBarLayout();
		setLayout(layout);
		checkButton();
		setWrap(false);
	}

	private void checkButton() {
		invalidate();
		if (getLayout() instanceof ActionToolBarLayout) { // Hack to cope with
															// L&F changes
			if (button != null) {
				remove(button);
				((ActionToolBarLayout) getLayout()).setExpandComponent(null);
				buttonIndex = -1;
			}
			if (!isWrap()) {
				button = new ExpandToolBarToggleButton();
				button.setFocusPainted(false);
				((ActionToolBarLayout) getLayout()).setExpandComponent(button);
				add(button);
				buttonIndex = getComponentIndex(button);
			}
		}
		validate();
		repaint();
	}

	class ExpandToolBarToggleButton extends JButton implements ActionListener {
		JPopupMenu popup;

		ExpandToolBarToggleButton() {
			super(new ArrowIcon(SwingConstants.SOUTH,
					UIManager.getColor("controlShadow"),
					UIManager.getColor("Button.foreground"),
					UIManager.getColor("controlLtHighlight")));
			popup = new JPopupMenu();
			addActionListener(this);
			addMouseListener(new MouseAdapter() {

				public void mouseEntered(MouseEvent e) {
					if (isEnabled()) {
						setBorderPainted(true);
						setContentAreaFilled(true);
					}
				}

				public void mouseExited(MouseEvent e) {
					setBorderPainted(false);
					setContentAreaFilled(false);
				}
			});
			setBorderPainted(false);
			setContentAreaFilled(false);
		}

		public Insets getMargin() {
			return new Insets(0, 0, 0, 0);
		}

		public boolean isRequestFocusEnabled() {
			return false;
		}

		public boolean isFocusTraversable() {
			return false;
		}

		public void actionPerformed(ActionEvent evt) {
			int overun = ((ActionToolBarLayout) ActionToolBar.this.getLayout())
					.getOverunIndex();
			if (overun != -1) {
				popup.invalidate();
				popup.removeAll();
				int count = ActionToolBar.this.getComponentCount();
				Component c;
				for (int i = overun; i < count; i++) {
					c = ActionToolBar.this.getComponent(i);
					if (c != this) {
						if (c instanceof ToolBarSeparator) {
							popup.addSeparator();
						} else if (c instanceof AbstractButton) {
							AbstractButton button = (AbstractButton) c;
							Action appAction = button.getAction();
							int menus = 0;
							if (appAction != null) {
								JComponent menu = (JComponent) appAction
										.getValue(MenuAction.MENU);
								if (menu != null && menu instanceof JPopupMenu) {
									JPopupMenu pm = (JPopupMenu) menu;
									menus = 0;
									for (int j = 0; j < pm.getComponentCount(); j++) {
										JComponent jc = (JComponent) pm
												.getComponent(j);
										if (jc instanceof JMenuItem) {
											if (menus == 0
													&& popup.getComponentCount() > 0) {
												popup.addSeparator();
											}
											popup.add(((JMenuItem) jc)
													.getAction());
											menus++;
										}
									}
								} else {
									if (menus > 0) {
										popup.addSeparator();
									}
									popup.add(appAction);
								}
							}
						} else {
							Action appAction = (Action) ((JComponent) c)
									.getClientProperty("action");
							if (appAction != null) {
								popup.add(appAction);
							}
						}
					}
				}
				popup.show(this, getSize().width
						- popup.getPreferredSize().width, getSize().height);
			}
		}
	}

	@Override
	public void setPreferredSize(Dimension preferredSize) {
		// TODO Auto-generated method stub
		super.setPreferredSize(preferredSize);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Container#remove(java.awt.Component)
	 */
	public void remove(Component comp) {
		if (comp != button) {
			super.remove(comp);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Container#remove(int)
	 */
	public void remove(int index) {
		if (index != buttonIndex) {
			super.remove(index);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.awt.Container#removeAll()
	 */
	public void removeAll() {
		super.removeAll();
		checkButton();
	}
}