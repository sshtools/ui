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

import java.awt.Dimension;

import javax.swing.JSeparator;
import javax.swing.JToolBar;

@SuppressWarnings("serial")
public class ToolBarSeparator extends JSeparator {

	public ToolBarSeparator() {
		super(JSeparator.VERTICAL);

	}

	public ToolBarSeparator(int orientation) {
		super(orientation);

	}

	public Dimension getMaximumSize() {
		return (((JToolBar) getParent()).getOrientation() == JToolBar.HORIZONTAL) ? new Dimension(
				8, super.getMaximumSize().height) : new Dimension(
				super.getMaximumSize().width, 8);

	}

	public Dimension getPreferredSize() {
		return (((JToolBar) getParent()).getOrientation() == JToolBar.HORIZONTAL) ? new Dimension(
				4, super.getMinimumSize().height) : new Dimension(
				super.getMinimumSize().width, 4);

	}

	public Dimension getMinimumSize() {
		return (((JToolBar) getParent()).getOrientation() == JToolBar.HORIZONTAL) ? new Dimension(
				2, super.getMinimumSize().height) : new Dimension(
				super.getMinimumSize().width, 2);

	}

	public void doLayout() {
		boolean horizontal = ((JToolBar) getParent()).getOrientation() == JToolBar.HORIZONTAL;
		setOrientation(horizontal ? JSeparator.VERTICAL : JSeparator.HORIZONTAL);

	}

}
