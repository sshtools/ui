package com.sshtools.ui.swing;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class PasswordsTab extends JPanel implements Tab, ListSelectionListener {

	private static final long serialVersionUID = 1L;

	protected JList passwords;
	protected RemoveAction removeAction;
	protected PasswordModel model;
	protected PersistantAuthenticator authenticator;

	public PasswordsTab(PersistantAuthenticator authenticator) {
		super();
		this.authenticator = authenticator;

		JPanel s = new JPanel(new BorderLayout());
		s.setOpaque(false);

		// Tool bar
		JToolBar toolBar = new JToolBar();
		toolBar.setFloatable(false);
		toolBar.setOpaque(true);
		removeAction = new RemoveAction();
		toolBar.add(new JButton(removeAction));

		passwords = new JList(model = new PasswordModel());
		passwords.setVisibleRowCount(4);
		passwords.addListSelectionListener(this);
		setRenderer(new PasswordRenderer());
		JScrollPane scroller = new JScrollPane(passwords);
		s.add(scroller, BorderLayout.CENTER);
		setLayout(new BorderLayout());

		JPanel changePasswordPanel = new JPanel();
		changePasswordPanel.setBorder(BorderFactory
				.createTitledBorder("Change Master Password"));
		changePasswordPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.insets = new Insets(0, 4, 0, 4);

		JButton changePassword = new JButton("Change");
		changePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				changeSecret();
			}
		});
		UIUtil.jGridBagAdd(changePasswordPanel, changePassword, gbc,
				GridBagConstraints.RELATIVE);
		changePasswordPanel.add(changePassword);
		gbc.weightx = 1.0;
		UIUtil.jGridBagAdd(changePasswordPanel, new JLabel(
				"<html>You may change or remove the master password used<br>"
						+ "to encrypt all saved passwords.</html>"), gbc,
				GridBagConstraints.REMAINDER);

		JButton resetSafe = new JButton("Reset");
		resetSafe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				resetSafe();
			}
		});
		gbc.weightx = 0.0;
		UIUtil.jGridBagAdd(changePasswordPanel, resetSafe, gbc,
				GridBagConstraints.RELATIVE);
		gbc.weightx = 1.0;
		UIUtil.jGridBagAdd(
				changePasswordPanel,
				new JLabel(
						"<html>If you have forgotten your master password,<br>"
								+ "you may reset it. Note, all of your saved passwords<br>"
								+ "will be lost if you do this.</html>"), gbc,
				GridBagConstraints.REMAINDER);

		add(s, BorderLayout.CENTER);
		add(changePasswordPanel, BorderLayout.SOUTH);
		add(toolBar, BorderLayout.NORTH);
		setOpaque(false);
		setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
		reset();
	}

	public void reset() {
		setAvailableActions();
	}

	public void applyTab() {
	}

	public String getTabCategory() {
		return "Advanced";
	}

	public Component getTabComponent() {
		return this;
	}

	public Icon getTabIcon() {
		return new ResourceIcon(PasswordsTab.class,
				"/images/password-safe-16x16.png");
	}

	public Icon getTabLargeIcon() {
		return new ResourceIcon(PasswordsTab.class,
				"/images/password-safe-48x48.png");
	}

	public int getTabMnemonic() {
		return 'p';
	}

	public String getTabTitle() {
		return "Passwords";
	}

	public String getTabToolTipText() {
		return "Configure the password safe";
	}

	public void tabSelected() {
	}

	public boolean validateTab() {
		return true;
	}

	public void valueChanged(ListSelectionEvent e) {
		setAvailableActions();
	}

	public void setRenderer(ListCellRenderer renderer) {
		passwords.setCellRenderer(renderer);
	}

	protected AppAction getRemoveAction() {
		return removeAction;
	}

	void resetSafe() {
		if (JOptionPane
				.showConfirmDialog(
						PasswordsTab.this,
						"Are you sure you wish to reset your password safe. All saved passwords will be lost",
						"Reset Password Safe", JOptionPane.YES_NO_OPTION,
						JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
			authenticator.getPasswordPairList().reset();
			model.reload();
		}
	}

	void changeSecret() {
		try {
			char[][] pw = authenticator
					.showMasterPasswordConfirmationDialog(
							this,
							"Please enter your old master password, then enter and confirm your new master password",
							"Change Master Password", true);
			if (pw != null) {
				authenticator.getPasswordPairList().changeMasterPassword(pw[1],
						pw[0]);
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this,
					"Failed to change secret. " + e.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	void setAvailableActions() {
		removeAction.setEnabled(passwords.getSelectedValues().length > 0);
	}

	class PasswordModel extends AbstractListModel {

		private static final long serialVersionUID = -2460489260989922868L;
		private PasswordPairList passwords;

		PasswordModel() {
			reload();
		}

		public void reload() {
			passwords = authenticator.getPasswordPairList();
			fireContentsChanged(this, 0, getSize() - 1);
		}

		public Object getElementAt(int index) {
			return passwords.get(index);
		}

		public int getSize() {
			return passwords.size();
		}

		public void removePassword(int idx) {
			try {
				if (authenticator.getPasswordPairList().getMasterPassword() == null) {
					char[] pw = authenticator
							.getPasswordSafeSecret(
									null,
									"<html>To be able to remove passwords from the safe<br/>"
											+ "you must enter the master password. If you do<br/>"
											+ "not know the master password you will have to<br/>"
											+ "reset it, which will remove all passwords.",
									PasswordsTab.this);
					if (pw == null) {
						return;
					}
					authenticator.getPasswordPairList().setMasterPassword(pw);
				}
				authenticator.removePassword(passwords.getPasswordPairAt(idx));
			} catch (Exception e) {
				JOptionPane.showMessageDialog(PasswordsTab.this,
						"Failed to set master password. " + e.getMessage(),
						"Error", JOptionPane.ERROR_MESSAGE);
			}
			fireIntervalRemoved(this, idx, idx);
		}

	}

	class RemoveAction extends AppAction {
		private static final long serialVersionUID = 1L;

		public RemoveAction() {
			super();
			putValue(NAME, "Remove");
			putValue(SMALL_ICON, new ResourceIcon(RemoveAction.class,
					"/images/delete-16x16.png"));
			putValue(SHORT_DESCRIPTION, "Remove Selected Password");
			putValue(LONG_DESCRIPTION, "Removethe selected password");
			putValue(ACCELERATOR_KEY,
					KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		}

		public void actionPerformed(ActionEvent evt) {
			if (JOptionPane.showConfirmDialog(
					PasswordsTab.this,
					"Are you sure you wish to remove these "
							+ passwords.getSelectedIndices().length
							+ " passwords(s)?", "Remove Passwords",
					JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION) {
				int[] sel = passwords.getSelectedIndices();
				for (int i = sel.length - 1; i >= 0; i--) {
					model.removePassword(sel[i]);
				}
			}
		}
	}

	public class PasswordRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = 1L;

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected,
					cellHasFocus);
			PasswordPair pair = (PasswordPair) value;
			String text = pair.getKey().getScheme() + "://"
					+ pair.getKey().getHost();
			if (pair.getKey().getPort() > -1) {
				text += ":" + pair.getKey().getPort();
			}
			setText(text);
			setIcon(authenticator.getIcon(pair.getKey().getScheme(), 16));
			return this;
		}

	}
}