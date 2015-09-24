package com.sshtools.ui.swing;

/**
 * PasswordPair pairs a <code>PasswordKey</code> with the actual password
 */
public class PasswordPair implements Comparable {
	private PasswordKey key;
	private String userPassword;
	private boolean persistant;
    private boolean encrypted;

	/**
	 * Creates a new PasswordPair object.
	 * 
	 * @param key key
	 * @param userPassword password
	 * @param persistant save the password permanently
	 */
	public PasswordPair(PasswordKey key, String userPassword, boolean persistant, boolean encrypted) {
		setKey(key);
		setUserPassword(userPassword);
		setPersistant(persistant);
        setEncrypted(encrypted);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		PasswordPair p = (PasswordPair) o;

		if ((isPersistant() && p.isPersistant()) || (!isPersistant() && !p.isPersistant())) {
			return key.compareTo(((PasswordPair) o).getKey());
		}
		else
			if (isPersistant()) {
				return 1;
			}
			else
				if (p.isPersistant()) {
					return -1;
				}
				else {

					return 0;
				}
	}

	/**
	 * Set the key for this password pair
	 * 
	 * @param key key
	 */
	public void setKey(PasswordKey key) {
		this.key = key;
	}

	/**
	 * Get the key for the password pair
	 * 
	 * @return key
	 */
	public PasswordKey getKey() {
		return key;
	}

	/**
	 * Set the password for the password pair
	 * 
	 * @param userPassword password
	 */
	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	/**
	 * Get the password for the password pair
	 * 
	 * @return password
	 */
	public String getUserPassword() {
		return userPassword;
	}

	/**
	 * Set whether the password pair should be saved permanently. 
	 * 
	 * @param persistant save password permanently
	 */
	public void setPersistant(boolean persistant) {
		this.persistant = persistant;
	}

	/**
	 * Get whether the password pair should be saved permanently. 
	 * 
	 * @return save password permanently
	 */
	public boolean isPersistant() {
		return persistant;
	}

    /**
     * Set whether the password pair is encrypted
     * 
     * @param encrypted encrypted
     */
    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    /**
     * Get whether the password pair is encrypted
     * 
     * @return encrypted
     */
    public boolean isEncrypted() {
        return encrypted;
    }
}