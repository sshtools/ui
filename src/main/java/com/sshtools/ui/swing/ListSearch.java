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

/**
 * Description of the Class
 * 
 * @author magicthize
 * @created 26 May 2002
 */
public class ListSearch implements KeyListener {

	//
	private ListSearchListener listener;

	private JComponent parent;

	// private JWindow searchWindow;
	private JFrame searchWindow;

	private JTextField searchText;

	/**
	 * Creates a new ListSearch object.
	 * 
	 * @param parent
	 *            DOCUMENT ME!
	 * @param listener
	 *            DOCUMENT ME!
	 */
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

	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 */
	public void keyTyped(KeyEvent e) {
		char ch = e.getKeyChar();

		if (!e.isControlDown() && !e.isAltDown() && Character.isDefined(ch)
				&& e.getKeyChar() != KeyEvent.VK_ENTER
				&& e.getKeyChar() != KeyEvent.VK_BACK_SPACE
				&& e.getKeyChar() != KeyEvent.VK_ESCAPE
				&& (searchWindow == null || !searchWindow.isVisible())) {
			showSearchWindow(ch, e);
			e.consume();
		}
	}

	private void showSearchWindow(char ch, KeyEvent evt) {
		if (searchWindow == null) {
			// Window w = (Window)
			// SwingUtilities.getAncestorOfClass(Window.class,
			// parent);
			//
			// if (w != null)
			// searchWindow = new JWindow(w);
			// else
			// searchWindow = new JWindow();
			searchWindow = new JFrame("Find file");
			try {
				Method m = searchWindow.getClass().getMethod("setUndecorated",
						new Class[] { boolean.class });
				m.invoke(searchWindow, new Object[] { Boolean.TRUE });
			} catch (Throwable t) {
				// Probably not 1.4
			}

			searchWindow
					.setBackground(UIManager.getColor("ToolTip.background"));
			searchWindow
					.setForeground(UIManager.getColor("ToolTip.foreground"));

			// searchWindow.setLocationRelativeTo(w);
			searchText = new JTextField();
			searchText.setFont(UIManager.getFont("Label.font").deriveFont(12f));
			searchText.setOpaque(false);
			searchText.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			searchText.getDocument().addDocumentListener(
					new DocumentListener() {

						public void update(DocumentEvent evt) {
							searchText.revalidate();
							searchWindow.pack();
							searchText.grabFocus();
							listener.searchUpdated(searchText.getText());
						}

						public void insertUpdate(DocumentEvent de) {
							update(de);
						}

						public void changedUpdate(DocumentEvent de) {
							update(de);
						}

						public void removeUpdate(DocumentEvent de) {
							update(de);
						}
					});
			searchText.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent evt) {
					listener.searchComplete(searchText.getText());
					removeSearchWindow();
				}
			});
			searchText.addFocusListener(new FocusAdapter() {

				public void focusLost(FocusEvent evt) {
					listener.searchCancelled();
					removeSearchWindow();
				}
			});
			searchText.getInputMap().put(
					KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape");
			searchText.getActionMap().put("escape", new EscapeAction());

			JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0,
					0));
			searchPanel.add(new JLabel("Find: "));
			searchPanel.add(searchText);
			searchPanel.setForeground(UIManager.getColor("ToolTip.foreground"));
			searchPanel.setBackground(UIManager.getColor("ToolTip.background"));
			searchPanel.setBorder(BorderFactory.createCompoundBorder(
					UIManager.getBorder("ToolTip.border"),
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

	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 */
	public void keyPressed(KeyEvent e) {
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 */
	public void keyReleased(KeyEvent e) {
	}

	class EscapeAction extends AbstractAction {
		EscapeAction() {
			super("Escape");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent
		 * )
		 */
		public void actionPerformed(ActionEvent e) {
			listener.searchCancelled();
			removeSearchWindow();
		}
	}
}