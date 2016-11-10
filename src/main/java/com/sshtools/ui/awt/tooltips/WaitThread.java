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
/*
 */
package com.sshtools.ui.awt.tooltips;

import java.awt.Component;
import java.awt.Frame;

import com.sshtools.ui.awt.UIUtil;

class WaitThread extends Thread {
	TipWindow tipWindow;
	Component component;
	int tx = -1;
	int ty = -1;
	String text;
	Frame lastSharedFrame;

	WaitThread() {
		super("ToolTip"); //$NON-NLS-1$

	}

	synchronized void requestToolTip(Component component, String text) {
		requestToolTip(component, -1, -1, text);
	}

	synchronized void requestToolTip(Component component, int x, int y,
			String text) {
		// if(!isAlive()) {
		// start();
		// }
		this.component = component;
		this.tx = x;
		this.ty = y;
		this.text = text;
		if (component == null || text == null) {
			dismissToolTip();
		}
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(500);
				synchronized (this) {
					if (component != null) {
						if (tipWindow == null
								|| lastSharedFrame == null
								|| lastSharedFrame != ToolTipManager
										.getInstance().getSharedFrame()) {
							lastSharedFrame = ToolTipManager.getInstance()
									.getSharedFrame();
							Frame f = UIUtil.getFrameAncestor(component);
							if (tipWindow != null) {
								tipWindow.dispose();
							}
							f = f == null ? ToolTipManager.getInstance()
									.getSharedFrame() : f;
							tipWindow = new TipWindow(f);
						}
						tipWindow.popup(tx, ty, component, text);
						component = null;
						text = null;
					} else {
						if (tipWindow != null && tipWindow.isOutOfDate()) {
							dismissToolTip();
						}
					}
				}
			} catch (InterruptedException ie) {

			}
		}
	}

	/**
     *  
     */
	public void dismissToolTip() {
		if (tipWindow != null) {
			tipWindow.dismiss();
		}
	}
}