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
package com.sshtools.ui.swing;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import com.sshtools.ui.awt.UIUtil;

public class DebugConsole {

	private JFrame frame;
	private JTextPane textArea;
	private Method deleteMethod;
	private OutputStream oldSysOut;
	private boolean userScrolled;

	private static DebugConsole console;

	private DebugConsole() {
		textArea = new JTextPane();
	}

	/**
	 * Show the console.
	 */
	void doShow() {
		if (frame == null) {

			try {
				deleteMethod = StringBuffer.class.getMethod(
						"delete", new Class[] { int.class, int.class }); //$NON-NLS-1$
			} catch (Throwable t) {
			}
			textArea.setEditable(false);
			JPanel panel = new JPanel(new BorderLayout());
			panel.setBackground(Color.gray);
			panel.setForeground(Color.black);
			panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
			JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
			buttonPanel.setBackground(Color.gray);
			buttonPanel.setForeground(Color.black);
			JButton clear = new JButton("Clear"); //$NON-NLS-1$
			clear.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					clear();
				}
			});
			buttonPanel.add(clear);
			JButton close = new JButton("Close"); //$NON-NLS-1$
			close.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent evt) {
					frame.setVisible(false);
				}
			});
			buttonPanel.add(close);
			panel.add(buttonPanel, BorderLayout.SOUTH);
			frame = new JFrame("Console"); //$NON-NLS-1$
			frame.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent evt) {
					frame.setVisible(false);

				}
			});
			frame.setIconImage(UIUtil.loadImage(getClass(),
					"/images/idle-16x16.gif")); //$NON-NLS-1$
			frame.add(panel);
			frame.pack();
			frame.setLocation(100, 100);
			frame.setSize(300, 400);
		}
		frame.setVisible(true);
		frame.toFront();
		textArea.setCaretPosition(textArea.getDocument().getLength());
		userScrolled = false;
	}

	void clear() {
		synchronized (textArea) {
			try {
				textArea.getStyledDocument().remove(0,
						textArea.getDocument().getLength());
				if (frame.isVisible()) {
					textArea.setCaretPosition(0);
				}
			} catch (BadLocationException e) {
			}
		}
	}

	public void append(String text, Color color) {
		synchronized (textArea) {
			SimpleAttributeSet attr1 = new SimpleAttributeSet();
			StyleConstants.setForeground(attr1, color);
			try {
				textArea.getStyledDocument().insertString(
						textArea.getStyledDocument().getLength(), text, attr1);
				if (textArea.getStyledDocument().getLength() > 65535) {
					textArea.getStyledDocument().remove(65536,
							textArea.getStyledDocument().getLength() - 65535);
				}
				if (frame != null && frame.isVisible()) {
					if (!userScrolled) {
						textArea.setCaretPosition(textArea.getStyledDocument()
								.getLength());
					}
				}
			} catch (BadLocationException ble) {

			}
		}
	}

	public static boolean isStarted() {
		return console != null;
	}

	public static void start() {
		OutputStream oldSysOut = System.out;
		OutputStream oldSysErr = System.err;
		console = new DebugConsole();
		System.setOut(new PrintStream(new ConsoleOutputStream(oldSysOut,
				Color.black, console), true));
		System.setErr(new PrintStream(new ConsoleOutputStream(oldSysErr,
				Color.red, console), true));
	}

	public static void show() {
		console.doShow();
	}

}
