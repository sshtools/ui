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
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.sshtools.ui.Option;
import com.sshtools.ui.OptionCallback;
import com.sshtools.ui.OptionChooser;

public class PersistantAuthenticator extends Authenticator {

	private final static String DEFAULT_TEXT = "<html>Please enter the secret for your password safe.<br>"
			+ "The password safe is used to store all of your<br>"
			+ "passwords for various servers and proxy<br>"
			+ "servers so you do no have to enter them again.<br>"
			+ "These passwords will be encrypted using the secret<br>"
			+ "you provide as a key.</html>";

	private static File prefDir;

	private XTextField user;
	private JPasswordField password;
	private Component parent;
	private Box rememberPassword;
	private JPanel passwordPanel;
	private JLabel requestingSiteLabel, requestingSite;
	private JLabel text, userLabel;
	private boolean forceAskForPassword;
	private PasswordPairList pairs;
	private String userName;
	private Throwable exception;
	private boolean emphasisePrompt;
	private boolean triedSysProp;
	private String initialPassword;
	private boolean allowEmptyPassword;
	private int iconSize = 48;
	private boolean focusUser;
	private boolean focusPassword;
	private JRadioButton doNotRemember;

	private JRadioButton storeSession;

	private JRadioButton storePermanently;

	/**
	 * Creates a new GruntspudAuthenticator.
	 * 
	 * @param context
	 *            context
	 * @throws IOException
	 */
	public PersistantAuthenticator() {
		// Initialise

		// Create the password panel
		passwordPanel = new JPanel(new GridBagLayout());
		passwordPanel.setOpaque(false);

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 4, 4);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.weightx = 2.0;
		UIUtil.jGridBagAdd(passwordPanel, text = new JLabel(), gbc,
				GridBagConstraints.REMAINDER);

		gbc.weightx = 0.0;
		UIUtil.jGridBagAdd(passwordPanel, requestingSiteLabel = new JLabel(
				"Host"), gbc, GridBagConstraints.RELATIVE);
		gbc.weightx = 1.0;
		UIUtil.jGridBagAdd(passwordPanel, requestingSite = new JLabel(), gbc,
				GridBagConstraints.REMAINDER);

		gbc.weightx = 0.0;
		UIUtil.jGridBagAdd(passwordPanel, userLabel = new JLabel("User"), gbc,
				GridBagConstraints.RELATIVE);
		gbc.weightx = 1.0;
		UIUtil.jGridBagAdd(passwordPanel, user = new XTextField(10) {

			public void addNotify() {
				super.addNotify();
				if (focusUser) {
					requestFocusInWindow();
				}
			}

		}, gbc, GridBagConstraints.REMAINDER);

		gbc.weightx = 0.0;
		UIUtil.jGridBagAdd(passwordPanel, new JLabel("Password"), gbc,
				GridBagConstraints.RELATIVE);
		gbc.weightx = 1.0;
		UIUtil.jGridBagAdd(passwordPanel, password = new JPasswordField(10) {
			public void addNotify() {
				super.addNotify();
				if (focusPassword) {
					requestFocusInWindow();
				}
			}

		}, gbc, GridBagConstraints.REMAINDER);
		gbc.weightx = 0.0;

		gbc.weightx = 2.0;
		gbc.insets = new Insets(4, 16, 4, 16);

		ButtonGroup bg = new ButtonGroup();

		rememberPassword = new Box(BoxLayout.Y_AXIS);

		rememberPassword.add(doNotRemember = new JRadioButton(
				"Do not store this password"));
		bg.add(doNotRemember);
		rememberPassword.add(storeSession = new JRadioButton(
				"Store for the remainder of this session"));
		bg.add(storeSession);
		rememberPassword.add(storePermanently = new JRadioButton(
				"Store permanently"));
		bg.add(storePermanently);

		UIUtil.jGridBagAdd(passwordPanel, rememberPassword, gbc,
				GridBagConstraints.REMAINDER);

	}

	/**
	 * Set the initial password. This is used if no password has ever been
	 * stored. If this is set the password will not be prompted for unless force
	 * is <code><true</code> or a password has already been stored. The value
	 * must be <code>null</code> to unset. The value is reset once the
	 * authenticator has been used.
	 * 
	 * @param initialPassword
	 *            initial password
	 */
	public void setInitialPassword(String initialPassword) {
		this.initialPassword = initialPassword;
	}

	/**
	 * Get the current parent component
	 * 
	 * @return parent
	 */
	public Component getParent() {
		return parent;
	}

	/**
	 * Set the directory where the passwords will be store
	 * 
	 * @param dir
	 *            directory
	 */
	public static void setDirectory(File prefDir) {
		PersistantAuthenticator.prefDir = prefDir;
	}

	/**
	 * Return a list of all the password pairs
	 * 
	 * @return password pair list
	 */
	public PasswordPairList getPasswordPairList() {
		return pairs;
	}

	/**
	 * Remove a password pair from the list.
	 * 
	 * @param pair
	 * @throws Exception
	 */
	public void removePassword(PasswordPair pair) throws Exception {
		pairs.remove(pair);
		pairs.savePasswordFile();
	}

	/**
	 * Initialise the authenticator. Should be called just after instantiation
	 * 
	 * @param context
	 *            context
	 * @throws IOException
	 *             if password list cannot be loaded
	 */
	public void init() throws IOException {
		File passwordFile = new File(prefDir, "passwd");
		pairs = new PasswordPairList(passwordFile);
	}

	/**
	 * Show a dialog asking the use for a new password safe secret. The user
	 * must enter the same secret twice, then an 2 element array of the old
	 * password and new password as a char[] array is returned. If the user
	 * cancels <code>null</code> will be returned.
	 * 
	 * @param context
	 *            context
	 * @param parent
	 *            parent component
	 * @param text
	 *            help text
	 * @param title
	 *            dialog title
	 * @param checkAgainstOld
	 *            make sure the old secret matches first
	 * 
	 * @return 2 element array of char[], first element is old secret, second is
	 *         new
	 */
	public char[][] showMasterPasswordConfirmationDialog(JComponent parent,
			String text, String title, final boolean checkAgainstOld) {
		//
		JPanel t = new JPanel(new BorderLayout());
		if (text != null) {
			t.add(new JLabel(text), BorderLayout.NORTH);
		}

		JPanel p = new JPanel(new GridBagLayout());
		p.setBorder(BorderFactory.createEmptyBorder(4, 0, 0, 0));

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 4, 4);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 0.0;

		final JPasswordField oldPassword = checkAgainstOld ? new JPasswordField(
				15) : null;

		if (checkAgainstOld) {
			UIUtil.jGridBagAdd(p, new JLabel("Old Secret"), gbc,
					GridBagConstraints.RELATIVE);
			gbc.weightx = 1.0;
			UIUtil.jGridBagAdd(p, oldPassword, gbc,
					GridBagConstraints.REMAINDER);
			gbc.weightx = 0.0;
		}

		UIUtil.jGridBagAdd(p, new JLabel("New Secret"), gbc,
				GridBagConstraints.RELATIVE);
		gbc.weightx = 1.0;

		final JPasswordField newPassword = new JPasswordField(20);
		UIUtil.jGridBagAdd(p, newPassword, gbc, GridBagConstraints.REMAINDER);
		gbc.weightx = 0.0;
		UIUtil.jGridBagAdd(p, new JLabel("Confirm Secret"), gbc,
				GridBagConstraints.RELATIVE);
		gbc.weightx = 1.0;

		final JPasswordField confirmPassword = new JPasswordField(15);
		UIUtil.jGridBagAdd(p, confirmPassword, gbc,
				GridBagConstraints.REMAINDER);
		gbc.weightx = 0.0;
		t.add(p, BorderLayout.CENTER);

		//

		final Option opt = OptionDialog.prompt(parent, OptionChooser.QUESTION,
				title, t, Option.CHOICES_OK_CANCEL, Option.CHOICE_OK,
				new OptionCallback() {
					public boolean canClose(OptionChooser chooser, Option option) {
						if (!Option.CHOICE_OK.equals(option)) {
							return true;
						}

						char[] p1 = newPassword.getPassword();
						char[] p2 = confirmPassword.getPassword();

						if (p1.length == 0 && p2.length == 0) {
							Toolkit.getDefaultToolkit().beep();
							return false;
						}

						if (checkAgainstOld) {
							char[] p3 = oldPassword.getPassword();

							try {
								if (!getPasswordPairList().checkMasterPassword(
										p3)) {
									JOptionPane.showMessageDialog(
											chooser.getComponent(),
											"Old password safe secret incorrect",
											"Error", JOptionPane.ERROR_MESSAGE);

									return false;
								}
							} catch (Exception e) {
								JOptionPane.showMessageDialog(
										chooser.getComponent(), e.getMessage(),
										"Error", JOptionPane.ERROR_MESSAGE);
								return false;
							}
						}
						if (!(new String(p1).equals(new String(p2)))) {
							JOptionPane.showMessageDialog(
									chooser.getComponent(),
									"New secret and confirmed secret do not match",
									"Error", JOptionPane.ERROR_MESSAGE);
							return false;
						}
						return true;
					}
				}, getIcon("safe", iconSize));
		if (!Option.CHOICE_OK.equals(opt)) {
			return null;
		}
		return new char[][] {
				checkAgainstOld ? oldPassword.getPassword() : null,
				newPassword.getPassword() };
	}

	/**
	 * Show a dialog asking for the password safe secret
	 * 
	 * @param message
	 *            message to show
	 * @param text
	 *            text
	 * @param parent
	 *            parent component
	 * 
	 * @return password safe secret (or <code>null</code> if aborted)
	 */
	public char[] getPasswordSafeSecret(String message, String text,
			Component parent) {
		//
		JPanel t = new JPanel(new BorderLayout(0, 4));
		t.add(new JLabel(text), BorderLayout.NORTH);

		if (message != null) {
			JLabel mesg = new JLabel(message, JLabel.CENTER);
			mesg.setForeground(Color.red);
			t.add(mesg, BorderLayout.SOUTH);
		}

		JPanel p = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(0, 0, 4, 4);
		gbc.anchor = GridBagConstraints.WEST;
		gbc.fill = GridBagConstraints.HORIZONTAL;

		gbc.weightx = 0.0;
		UIUtil.jGridBagAdd(p, new JLabel("Password Safe Secret"), gbc,
				GridBagConstraints.RELATIVE);
		gbc.weightx = 1.0;
		JPasswordField secret = new JPasswordField(15);
		UIUtil.jGridBagAdd(p, secret, gbc, GridBagConstraints.REMAINDER);
		secret.addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				e.getComponent().requestFocusInWindow();
			}

		});
		secret.requestDefaultFocus();

		t.add(p, BorderLayout.CENTER);
		Option option = OptionDialog.prompt(parent, OptionDialog.QUESTION,
				"Enter master password", t, Option.CHOICES_OK_CANCEL,
				Option.CHOICE_OK, null, null, getIcon("safe", iconSize), false,
				null);
		if (!Option.CHOICE_OK.equals(option)) {
			return null;
		}
		return secret.getPassword();
	}

	/**
	 * Set whether the master password should be asked for if it is needed. This
	 * should be set just before any operation that might require the use of a
	 * password is run.
	 * 
	 * @param forceAskForPassword
	 *            always ask for the master password next time it is needed
	 */
	public void setForceAskForPassword(boolean forceAskForPassword) {
		this.forceAskForPassword = forceAskForPassword;
	}

	/**
	 * Set if the next request for a password should emphasise the 'prompt'
	 * field by colouring it red. This would normally be used if this is the
	 * second request for a password as the first one was incorrect in some way.
	 * 
	 * @param emphasisePrompt
	 *            emphasis prompt
	 */
	public void setEmphasisePrompt(boolean emphasisePrompt) {
		this.emphasisePrompt = emphasisePrompt;
	}

	/**
	 * Set the parent component to use as a parent for the modal authentication
	 * dialog (should it be displayed).
	 * 
	 * @param parent
	 *            parent component
	 */
	public void setParentComponent(Component parent) {
		this.parent = parent;
	}

	/**
	 * Set the user name to use next time a password is requested
	 * 
	 * @param userName
	 *            user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void updateUI() {
		SwingUtilities.updateComponentTreeUI(passwordPanel);
	}

	public void setAllowEmptyPassword(boolean allowEmptyPassword) {
		this.allowEmptyPassword = allowEmptyPassword;
	}

	protected PasswordAuthentication getPasswordAuthentication() {
		exception = null;
		try {
			String host = (getRequestingSite() == null) ? getRequestingHost()
					: getRequestingSite().getHostName();
			String scheme = getRequestingScheme();
			String prompt = getRequestingPrompt();
			String userKey = "";
			boolean hasUsername = isNotEmpty(userName);

			if (scheme.equals("proxy")) {
				requestingSiteLabel.setVisible(true);
				requestingSite.setVisible(true);
				requestingSite.setText(host);
				userKey = userName;
				userLabel.setVisible(!hasUsername);
				user.setVisible(!hasUsername);
				rememberPassword.setVisible(true);
			}
			// Dont show most of the fields for XMPP authentication
			else if (scheme.equals("xmpp") || scheme.equals("sslx")) {
				requestingSiteLabel.setVisible(false);
				requestingSite.setVisible(false);
				user.setVisible(false);
				userKey = getRequestingProtocol(); // eurgh!
				userLabel.setVisible(false);
				rememberPassword.setVisible(true);
			} else if (scheme.equals("transport")) {
				requestingSiteLabel.setVisible(false);
				requestingSite.setVisible(false);
				user.setVisible(true);
				user.setText("");
				userLabel.setVisible(true);
				rememberPassword.setVisible(false);
			} else {
				requestingSiteLabel.setVisible(true);
				requestingSite.setVisible(true);
				requestingSite.setText(host);
				user.setVisible(true);
				user.setText("");
				userLabel.setVisible(true);
				rememberPassword.setVisible(true);
			}

			text.setText(prompt);
			text.setForeground(emphasisePrompt ? Color.red : UIManager
					.getColor("Label.foreground"));

			PasswordKey key = new PasswordKey(getRequestingProtocol(),
					getRequestingScheme(), host, userKey, getRequestingPort());

			// First check the session passwords, then the persistant ones for a
			// match on the protocol / scheme / host name
			String userPassword = null;
			PasswordPair pair = null;
			if (!isNotEmpty(userKey)) {
				pair = pairs.getPair(key);
			}

			if (scheme.equals("transport")) {
				storePermanently.setSelected(true);
			} else if (pair == null) {
				storeSession.setSelected(true);
			} else {
				userPassword = pair.getUserPassword();

				if (pair.isPersistant()) {
					storePermanently.setSelected(true);
				} else {
					doNotRemember.setSelected(true);
				}
			}

			// Decrypt the password if necessary
			if (userPassword != null && pair.isEncrypted()) {
				String message = null;
				if (pairs.getMasterPassword() == null) {
					while (true) {
						if (pairs.getMasterPassword() == null) {
							pairs.setMasterPassword(getPasswordSafeSecret(
									message, DEFAULT_TEXT, parent));
						}
						if (pairs.getMasterPassword() == null) {
							return null;
						}
						if (pairs
								.checkMasterPassword(pairs.getMasterPassword())) {
							break;
						}
						message = "Incorrect password";
						pairs.setMasterPassword(null);
					}
				}
				try {
					userPassword = StringEncrypter.decryptString(userPassword,
							pairs.getMasterPassword());
				} catch (Exception e) {
					System.err
							.println("Failed to decrypt string. Cannot get password.");
					e.printStackTrace();
					userPassword = null;
				}
			}

			// Get the user and password
			char[] passwordChars = null;

			if ((userPassword != null) && (userPassword.length() != 0)) {
				String[] s = userPassword.split("@");

				if (userName == null) {
					userName = URLDecoder.decode(s[0], "UTF-8");

				}
				if (s.length > 1) {
					passwordChars = URLDecoder.decode(s[1], "UTF-8")
							.toCharArray();
				}
			}

			// Set the initial password
			if (userPassword == null && initialPassword != null
					&& !forceAskForPassword) {
				passwordChars = initialPassword.toCharArray();
			}

			boolean askForPassword = forceAskForPassword
					|| (passwordChars == null) || (userName == null);

			// Only ask for the password if we do not already know it
			if (askForPassword) {
				user.setEnabled(!isNotEmpty(userKey));
				user.setText((userName == null) ? "" : userName);
				password.setText((passwordChars == null) ? "" : new String(
						passwordChars));
				password.selectAll();
				// Set the focus
				focusPassword = false;
				focusUser = false;
				if (!userLabel.isVisible() || !userLabel.isEnabled()
						|| hasUsername) {
					focusPassword = true;
					if (passwordChars != null) {
						password.selectAll();
					} else {
						password.setCaretPosition(0);
					}
				} else {
					focusUser = true;
				}

				// Choose the icon
				Icon icon = null;
				icon = getIcon(scheme, iconSize);

				Option opt = OptionDialog.prompt(parent, OptionDialog.QUESTION,
						"Enter Password", passwordPanel,
						Option.CHOICES_OK_CANCEL, Option.CHOICE_OK,
						new OptionCallback() {
							public boolean canClose(OptionChooser dialog,
									Option option) {
								if (option == Option.CHOICE_OK
										&& !allowEmptyPassword
										&& password.getPassword().length == 0) {
									Toolkit.getDefaultToolkit().beep();
									return false;
								}
								return true;
							}
						}, null, icon, false, null);
				if (!Option.CHOICE_OK.equals(opt)) {
					return null;
				}

				userName = user.getText();
				passwordChars = password.getPassword();

				if (isNotEmpty(userKey)) {
					userKey = userName;
					key = new PasswordKey(getRequestingProtocol(),
							getRequestingScheme(), host, userKey,
							getRequestingPort());
				}

				// If we are to remember the password ..
				if (storePermanently.isSelected()) {
					if (!pairs.isLoaded()) {
						char[][] pw = showMasterPasswordConfirmationDialog(
								passwordPanel,
								"<html>The password safe is used to securely<br>"
										+ "store all passwords for instant messaging servers<br>"
										+ "and proxy servers. Please provide a 'secret' word or<br>"
										+ "phrase of at least 10 characters in length.",
								"New Password Safe", false);
						pairs.newPasswordFile(pw[1]);
					}

					if (pairs.getMasterPassword() == null) {
						String message = null;

						while (true) {
							if (pairs.getMasterPassword() == null) {
								pairs.setMasterPassword(getPasswordSafeSecret(
										message, DEFAULT_TEXT, parent));

							}
							if (pairs.getMasterPassword() == null) {
								throw new IOException(
										"Master password not supplied");
							}

							if (pairs.checkMasterPassword(pairs
									.getMasterPassword())) {
								break;
							}
							message = "Incorrect password";
							pairs.setMasterPassword(null);
						}
					}

					String upw = URLEncoder.encode(userName, "UTF-8")
							+ "@"
							+ URLEncoder.encode(new String(passwordChars),
									"UTF-8");
					String epw = StringEncrypter.encryptString(upw,
							pairs.getMasterPassword());
					pairs.setKey(key, epw, true, true);
					pairs.savePasswordFile();
				} else if (storeSession.isSelected()) {
					String upw = URLEncoder.encode(userName, "UTF-8")
							+ "@"
							+ URLEncoder.encode(new String(passwordChars),
									"UTF-8");
					pairs.setKey(key, upw, false, false);
				}
			}

			//
			return new PasswordAuthentication(userName, passwordChars);
		} catch (Throwable t) {
			exception = t;
			System.err.println("Failed to get password");
			t.printStackTrace();
			return null;
		} finally {
			allowEmptyPassword = false;
			userName = null;
			forceAskForPassword = false;
		}
	}

	public void setIconSize(int iconSize) {
		this.iconSize = iconSize;
	}

	public int getIconSize() {
		return iconSize;
	}

	public Icon getIcon(String scheme, int iconSize) {
		Icon icon;
		if ("proxy".equals(scheme)) {
			icon = new ResourceIcon(getClass(), "/images/proxy-" + iconSize
					+ "x" + iconSize + ".png");
		} else if ("safe".equals(scheme)) {
			icon = new ResourceIcon(getClass(), "/images/password-safe-"
					+ iconSize + "x" + iconSize + ".png");
		} else {
			icon = new ResourceIcon(getClass(), "/images/password-" + iconSize
					+ "x" + iconSize + ".png");
		}
		return icon;
	}

	private boolean isNotEmpty(String string) {
		return string != null && !string.trim().equals("");
	}

	/**
	 * Return the last exception
	 * 
	 * @return last exceptoion
	 */
	public Throwable getException() {
		return exception;
	}

	/**
	 * Get if the password will be asked for no matter what.
	 * 
	 * @return force ask for password
	 */
	public boolean isForceAskForPassword() {
		return forceAskForPassword;
	}
}