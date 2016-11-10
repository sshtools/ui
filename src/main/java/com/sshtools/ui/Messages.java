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
/* HEADER */
package com.sshtools.ui;

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
			.getBundle("com.sshtools.ui.ApplicationResources"); //$NON-NLS-1$

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
