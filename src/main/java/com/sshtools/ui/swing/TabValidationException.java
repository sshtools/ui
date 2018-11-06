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

import javax.swing.JComponent;

@SuppressWarnings("serial")
public class TabValidationException extends RuntimeException {
	private Tab tab;
	private JComponent component;

	public TabValidationException(Tab tab, JComponent component) {
		super();
		this.component = component;
		this.tab = tab;
	}

	public TabValidationException(Tab tab, JComponent component, String message, Throwable cause) {
		super(message, cause);
		this.component = component;
		this.tab = tab;
	}

	public TabValidationException(Tab tab, JComponent component, String message) {
		super(message);
		this.component = component;
		this.tab = tab;
	}

	public TabValidationException(Tab tab, JComponent component, Throwable cause) {
		super(null, cause);
		this.component = component;
		this.tab = tab;
	}

	public Tab getTab() {
		return tab;
	}

	public JComponent getComponent() {
		return component;
	}
}
