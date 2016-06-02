/* HEADER */
package com.sshtools.ui.swing;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 
 * Accesor class for message resources.
 * 
 * @author Brett Smith <a href="mailto: brett@3sp.com">&lt;brett@3sp.com&gt;</a>
 */
public class Messages {

	// Private statics
	private static ResourceBundle resourceBundle = ResourceBundle
			.getBundle("com.sshtools.ui.swing.ApplicationResources"); //$NON-NLS-1$

	private Messages() {
	}

	/**
	 * Set the resource bundle.
	 * 
	 * @param resourceBundle
	 *            bundle
	 */
	public static void setBundle(ResourceBundle resourceBundle) {
		Messages.resourceBundle = resourceBundle;
	}

	/**
	 * Get the localised string given its key.
	 * 
	 * @param key
	 *            key
	 * @return localised string
	 */
	public static String getString(String key) {
		try {
			return resourceBundle == null ? "*No resource bundle* !" + key + "!" : resourceBundle.getString(key); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
