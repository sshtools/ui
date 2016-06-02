package com.sshtools.ui.swing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;

/**
 * A password together with a key (<code>PasswordKey</code>) that makes it
 * unique makes up a <code>PasswordPair</code>. This object holds a list of
 * these pairs and also is responsible for saving the list to disk, possibly
 * encrypted if an <code>Encrypter</code> instance has been registered.
 * 
 * @author magicthize
 */
public class PasswordPairList extends ArrayList {
	// Statics
	protected final static String ENC = "1x45cvs32zp29aapttk22u8";
	protected final static String FORMAT_KEY = "Format: ";
	protected final static String ENCRYPTED_FORMAT = "encrypted";
	protected final static String PLAIN_FORMAT = "plain";
	protected final static PasswordKey PWMGR = new PasswordKey("pwmgr",
			"pwmgr", "pwmgr", "pwmgr", 0);

	// Private instance variables
	private File passwordFile;
	private char[] masterPassword;
	private PasswordPair pwmgr;

	/**
	 * Creates a new PasswordPairList object.
	 * 
	 * @param passwordFile
	 *            file to store passwords in
	 * @throws IOException
	 */
	public PasswordPairList(File passwordFile) throws IOException {
		super();
		this.passwordFile = passwordFile;

		File dir = passwordFile.getParentFile();
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				throw new IOException(
						"Failed to create directory for password file.");
			}
		}

		if (this.passwordFile.exists()) {
			loadPasswordFile();
		}
		if (System.getProperty("converse.masterPassword") != null) {
			masterPassword = System.getProperty("converse.masterPassword")
					.toCharArray();
		}
		try {
			String pwd = System.getenv("PASSWORD_SAFE_PASSWORD");
			if (pwd != null && !pwd.equals("")) {
				masterPassword = pwd.toCharArray();
			}
		} catch (Throwable t) {
		}

		// The system property / environment variable may be wrong
		if (masterPassword != null) {
			if (!checkMasterPassword(masterPassword)) {
				masterPassword = null;
			}
		}
	}

	/**
	 * Test if the password file can be decrypted given the master password.
	 * 
	 * @param p
	 *            master password
	 * @return valid password
	 * @throws IOException
	 *             if password file cannot be read
	 */
	public boolean checkMasterPassword(char[] p) {
		try {
			return StringEncrypter.decryptString(pwmgr.getUserPassword(), p)
					.equals(ENC);
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Change the master password and rewrite the password list.
	 * 
	 * @param newMasterPassword
	 *            new master password
	 * @throws IOException
	 *             if the password file cannot be written
	 */
	public void changeMasterPassword(char[] newMasterPassword,
			char[] oldMasterPassword) throws Exception {
		for (Iterator i = this.iterator(); i.hasNext();) {
			PasswordPair pair = (PasswordPair) i.next();
			decryptPair(pair, oldMasterPassword);
		}
		decryptPair(pwmgr, oldMasterPassword);
		setMasterPassword(newMasterPassword);
		savePasswordFile();
	}

	/**
	 * Set the supplied <code>PasswordPair</code>. If the pairs key already
	 * exists it will be overwritten, if not it will be created. The list will
	 * <strong>not</strong> be saved.
	 * 
	 * @param pair
	 *            password pair
	 */
	public void setPair(PasswordPair pair) {
		removeKey(pair.getKey());
		addPair(pair);
	}

	/**
	 * Set the supplied <code>PasswordPair</code> and update the password. If
	 * the pairs key already exists it will be overwritten, if not it will be
	 * created. The list will <strong>not</strong> be saved.
	 * 
	 * @param key
	 *            key
	 * @param userPassword
	 *            new password
	 */
	public void setKey(PasswordKey key, String userPassword,
			boolean persistant, boolean encrypted) {
		removeKey(key);
		addKey(key, userPassword, persistant, encrypted);
	}

	/**
	 * Add a new password key with a new password. A <code>PasswordPair</code>
	 * will be created and added to the list.
	 * 
	 * @param key
	 *            key
	 * @param userPassword
	 *            key
	 * @param persistant
	 *            persistant
	 * @param encrypted
	 *            encrypted
	 */
	public void addKey(PasswordKey key, String userPassword,
			boolean persistant, boolean encrypted) {
		addPair(new PasswordPair(key, userPassword, persistant, encrypted));
	}

	/**
	 * Add a new password pair to the list. The list will <strong>not</strong>
	 * be saved.
	 * 
	 * @param pair
	 *            pair
	 */
	public void addPair(PasswordPair pair) {
		add(pair);
	}

	/**
	 * Get a <code>PasswordPair</code> at a given index in the list
	 * 
	 * @param r
	 *            index in list
	 * @return password pair
	 */
	public PasswordPair getPasswordPairAt(int r) {
		return (PasswordPair) get(r);
	}

	/**
	 * Remove a password pair from the list given its key. The list will
	 * <strong>not</strong> be saved.
	 * 
	 * @param key
	 *            key to remove
	 */
	public void removeKey(PasswordKey key) {
		for (int i = size() - 1; i >= 0; i--) {
			if (getPasswordPairAt(i).getKey().equals(key)) {
				remove(i);
			}
		}
	}

	/**
	 * Get a password pair given its key. <code>null</code> will be returned if
	 * no password with the supplied key can be found
	 * 
	 * @param key
	 *            key
	 * @return password pair
	 */
	public PasswordPair getPair(PasswordKey key) {
		for (int i = 0; i < size(); i++) {
			PasswordPair pair = (PasswordPair) get(i);
			if (pair.getKey().equals(key)) {
				return pair;
			}
		}
		return null;
	}

	/**
	 * Get the master password
	 * 
	 * @return master password
	 */
	public char[] getMasterPassword() {
		return masterPassword;
	}

	/**
	 * Set the master password.
	 * 
	 * @param masterPassword
	 *            master password
	 */
	public void setMasterPassword(char[] masterPassword) {
		this.masterPassword = masterPassword;
	}

	/**
	 * Save all of the passwords to disk.
	 * 
	 * @throws IOException
	 *             if the password file cannot be written
	 */
	public void savePasswordFile() throws Exception {
		if (getMasterPassword() == null) {
			throw new IOException("Master password not set");
		}
		OutputStream out = new FileOutputStream(passwordFile);
		try {
			Properties p = new Properties();
			p.put(pwmgr.getKey().toString(),
					pwmgr.isEncrypted() ? pwmgr.getUserPassword()
							: StringEncrypter.encryptString(
									pwmgr.getUserPassword(),
									getMasterPassword()));
			for (int i = 0; i < size(); i++) {
				PasswordPair pair = (PasswordPair) get(i);
				if (pair.isPersistant()) {
					p.put(pair.getKey().toString(),
							pair.isEncrypted() ? pair.getUserPassword()
									: StringEncrypter.encryptString(
											pair.getUserPassword(),
											getMasterPassword()));
				}
			}
			p.store(out, null);
			out.flush();
		} finally {
			out.close();
		}
	}

	/**
	 * Get if the list is loaded. It will not have been loaded if never used
	 * before (the password file doesn't exist)
	 */
	public boolean isLoaded() {
		return pwmgr != null;
	}

	/**
	 * New password list.
	 * 
	 * @param password
	 * @throws Exception
	 *             if password file cannot be read
	 */
	public void newPasswordFile(char[] password) throws Exception {
		if (isLoaded()) {
			throw new IOException("Alread loaded.");
		}
		pwmgr = new PasswordPair(PWMGR, StringEncrypter.encryptString(ENC,
				password), true, true);
		setMasterPassword(password);
		savePasswordFile();
	}

	/**
	 * Load the password list from disk.
	 * 
	 * @throws IOException
	 *             if password file cannot be read
	 */
	public void loadPasswordFile() throws IOException {
		clear();
		pwmgr = null;
		Properties p = new Properties();
		InputStream in = new FileInputStream(passwordFile);
		try {
			p.load(in);
		} finally {
			in.close();
		}

		//
		try {
			for (Enumeration e = p.keys(); e.hasMoreElements();) {
				String key = (String) e.nextElement();
				String[] sa = key.split("@");
				String protocol = URLDecoder.decode(sa[0], "UTF-8");
				String scheme = URLDecoder.decode(sa[1], "UTF-8");
				String host = URLDecoder.decode(sa[2], "UTF-8");
				String username = URLDecoder.decode(sa[3], "UTF-8");
				int port = Integer.parseInt(sa[4]);
				PasswordKey k = new PasswordKey(protocol, scheme, host,
						username, port);
				String val = p.getProperty(key);
				PasswordPair pair = new PasswordPair(k, val, true, true);
				pair.setPersistant(true);
				if (!k.equals(PWMGR)) {
					add(pair);
				} else {
					pwmgr = pair;
				}
			}
		} catch (Exception e) {
			throw new IOException("Could not parse password file. "
					+ e.getMessage());
		}

		//
		Collections.sort(this);
	}

	/**
	 * Return the password list file
	 * 
	 * @return password list file
	 */
	public File getPasswordFile() {
		return passwordFile;
	}

	void decryptPair(PasswordPair pair, char[] oldPassword) throws Exception {
		if (pair.isEncrypted()) {
			pair.setEncrypted(false);
			pair.setUserPassword(StringEncrypter.decryptString(
					pair.getUserPassword(), oldPassword));
		}
	}

	public void reset() {
		passwordFile.delete();
		try {
			loadPasswordFile();
		} catch (IOException e) {
		}
	}

}
