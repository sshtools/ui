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

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.Method;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class ListSearch implements KeyListener {
	private ListSearchListener listener;
	private JComponent parent;
	private JFrame searchWindow;
	private JTextField searchText;

	public ListSearch(JComponent parent, ListSearchListener listener) {
		this.parent = parent;
		this.listener = listener;
		KeyListener[] l = parent.getKeyListeners();
		for (int i = 0; i < l.length; i++) {
			parent.removeKeyListener(l[i]);
		}
		parent.addKeyListener(this);
	}

	public void removeSearch() {
		parent.removeKeyListener(this);
	}

	private void removeSearchWindow() {
		if (searchWindow != null) {
			searchWindow.setVisible(false);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		char ch = e.getKeyChar();
		if (ch != KeyEvent.CHAR_UNDEFINED && !e.isControlDown() && !e.isAltDown() && Character.isDefined(ch)
				&& e.getKeyChar() != KeyEvent.VK_ENTER && e.getKeyChar() != KeyEvent.VK_BACK_SPACE
				&& e.getKeyChar() != KeyEvent.VK_ESCAPE && e.getKeyChar() != KeyEvent.VK_DELETE
				&& (searchWindow == null || !searchWindow.isVisible())) {
			showSearchWindow(ch, e);
			e.consume();
		}
	}

	private void showSearchWindow(char ch, KeyEvent evt) {
		if (searchWindow == null) {
			searchWindow = new JFrame("Find file");
			try {
				Method m = searchWindow.getClass().getMethod("setUndecorated", new Class[] { boolean.class });
				m.invoke(searchWindow, new Object[] { Boolean.TRUE });
			} catch (Throwable t) {
				// Probably not 1.4
			}
			searchWindow.setBackground(UIManager.getColor("ToolTip.background"));
			searchWindow.setForeground(UIManager.getColor("ToolTip.foreground"));
			// searchWindow.setLocationRelativeTo(w);
			searchText = new JTextField();
			searchText.setFont(FontUtil.getUIManagerLabelFontOrDefault("Label.font").deriveFont(12f));
			searchText.setOpaque(false);
			searchText.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			searchText.getDocument().addDocumentListener(new DocumentListener() {
				public void update(DocumentEvent evt) {
					searchText.revalidate();
					searchWindow.pack();
					searchText.grabFocus();
					listener.searchUpdated(searchText.getText());
				}

				@Override
				public void insertUpdate(DocumentEvent de) {
					update(de);
				}

				@Override
				public void changedUpdate(DocumentEvent de) {
					update(de);
				}

				@Override
				public void removeUpdate(DocumentEvent de) {
					update(de);
				}
			});
			searchText.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent evt) {
					listener.searchComplete(searchText.getText());
					removeSearchWindow();
				}
			});
			searchText.addFocusListener(new FocusAdapter() {
				@Override
				public void focusLost(FocusEvent evt) {
					listener.searchCancelled();
					removeSearchWindow();
				}
			});
			searchText.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
			searchText.getActionMap().put("escape", new EscapeAction());
			JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
			searchPanel.add(new JLabel("Find: "));
			searchPanel.add(searchText);
			searchPanel.setForeground(UIManager.getColor("ToolTip.foreground"));
			searchPanel.setBackground(UIManager.getColor("ToolTip.background"));
			searchPanel.setBorder(BorderFactory.createCompoundBorder(UIManager.getBorder("ToolTip.border"),
					BorderFactory.createEmptyBorder(4, 4, 4, 4)));
			searchWindow.getContentPane().setLayout(new GridLayout(1, 1));
			searchWindow.getContentPane().add(searchPanel);
		}
		searchText.setText(String.valueOf(ch));
		searchWindow.pack();
		JComponent evtpar = null;
		Point z = null;
		if (evt != null && evt.getSource() instanceof JComponent) {
			evtpar = (JComponent) evt.getSource();
		}
		if (evtpar == null) {
			evtpar = parent;
		}
		if (evtpar != null) {
			z = evtpar.getLocationOnScreen();
		}
		if (z == null && evtpar != null && evtpar.getParent() != null) {
			z = evtpar.getParent().getLocationOnScreen();
		}
		if (z == null) {
			z = new Point(100, 100);
		} else {
			z = new Point(z.x + 20, z.y + 20);
		}
		searchWindow.setLocation(z);
		searchWindow.setVisible(true);
		searchText.grabFocus();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		char ch = e.getKeyChar();
		if (ch != KeyEvent.CHAR_UNDEFINED && !e.isControlDown() && !e.isAltDown() && Character.isDefined(ch)
				&& e.getKeyChar() != KeyEvent.VK_ENTER && e.getKeyChar() != KeyEvent.VK_BACK_SPACE
				&& e.getKeyChar() != KeyEvent.VK_ESCAPE && e.getKeyChar() != KeyEvent.VK_DELETE
				&& (searchWindow == null || !searchWindow.isVisible())) {
			e.consume();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@SuppressWarnings("serial")
	class EscapeAction extends AbstractAction {
		EscapeAction() {
			super("Escape");
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			listener.searchCancelled();
			removeSearchWindow();
		}
	}
}