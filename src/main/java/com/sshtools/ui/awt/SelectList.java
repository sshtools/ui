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
package com.sshtools.ui.awt;

import java.awt.List;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public abstract class SelectList extends List implements MouseListener {

	public SelectList() {
		init(750);
	}

	public SelectList(int items) {
		this(items, 750);
	}

	public SelectList(int items, int waitInterval) {
		super(items);
		init(waitInterval);
	}

	private void init(int waitInterval) {
		this.waitInterval = waitInterval;
		addMouseListener(this);
	}

	public abstract void selected();

	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2)
			selected();
	}

	public void mousePressed(MouseEvent e) {
		if (timerThread == null) {
			released = false;
			timerThread = new Thread() {

				public void run() {
					try {
						Thread.sleep(waitInterval);
					} catch (InterruptedException interruptedexception) {
					}
					if (!released)
						selected();
					timerThread = null;
				}

			};
			timerThread.start();
		}
	}

	public void mouseReleased(MouseEvent e) {
		released = true;
	}

	public void mouseEntered(MouseEvent mouseevent) {
	}

	public void mouseExited(MouseEvent mouseevent) {
	}

	private Thread timerThread;
	private int waitInterval;
	private boolean released;

}