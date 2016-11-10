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

import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;

/**
 *  
 */
public class ToolTipManager {

	private static ToolTipManager instance;
	private WaitThread waitThread;
	private boolean enabled;

	Color foreground, background;
	private static Frame sharedFrame;

	static {
		Frame[] f = null;
		try {
			f = (Frame[]) Frame.class.getMethod("getFrames", new Class[] {})
					.invoke(null, new Object[] {});
		} catch (Exception e) {
		}
		sharedFrame = f != null && f.length > 0 ? f[0] : new Frame();
	}

	private ToolTipManager() {
		foreground = Color.black;
		background = new Color(0xfe, 0xff, 0xc6);
		enabled = true;
	}

	public Frame getSharedFrame() {
		return sharedFrame;
	}

	public void setSharedFrame(Frame frame) {
		if (frame == null) {
			Frame[] f = null;
			try {
				f = (Frame[]) Frame.class
						.getMethod("getFrames", new Class[] {}).invoke(null,
								new Object[] {});
			} catch (Exception e) {
			}
			sharedFrame = f != null && f.length > 0 ? f[0] : new Frame();
		} else {
			sharedFrame = frame;
		}

		// Clear the current popup
		if (waitThread != null) {
			waitThread.dismissToolTip();
		}
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public synchronized void requestToolTip(Component component, String text) {
		requestToolTip(component, -1, -1, text);
	}

	public synchronized void requestToolTip(Component component, int x, int y,
			String text) {
		if (enabled) {
			if (waitThread == null) {
				waitThread = new WaitThread();
				waitThread.start();
			}
			waitThread.requestToolTip(component, x, y, text);
		}
	}

	public void setToolTipBackground(Color background) {
		this.background = background;
	}

	public void setToolTipForeground(Color foreground) {
		this.foreground = foreground;
	}

	public static ToolTipManager getInstance() {
		if (instance == null) {
			instance = new ToolTipManager();
		}
		return instance;
	}

	/**
     *  
     */
	public void hide() {
		if (waitThread != null) {
			waitThread.dismissToolTip();
		}
	}
}