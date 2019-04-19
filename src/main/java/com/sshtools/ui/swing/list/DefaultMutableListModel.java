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
package com.sshtools.ui.swing.list;

import javax.swing.DefaultListModel;

// @author Santhosh Kumar T - santhosh@in.fiorano.com 
@SuppressWarnings("serial")
public class DefaultMutableListModel<T> extends DefaultListModel<T> implements MutableListModel<T> {
	public boolean isCellEditable(int index) {
		return true;
	}

	@SuppressWarnings("unchecked")
	public void setValueAt(Object value, int index) {
		super.setElementAt((T) value, index);
	}
}