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

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TestJSwitch2 extends JFrame {

	public TestJSwitch2() {
		setBounds(100, 100, 300, 120);
		setDefaultCloseOperation(3);
		getContentPane().setLayout(new FlowLayout(FlowLayout.LEADING, 10, 10));
		// getContentPane().add( new JSwitchBox( "on", "off" ));
		// getContentPane().add( new JSwitchBox( "yes", "no" ));
		// getContentPane().add( new JSwitchBox( "true", "false" ));
		// getContentPane().add( new JSwitchBox( "on", "off" ));
		// getContentPane().add( new JSwitchBox( "yes", "no" ));
		// getContentPane().add( new JSwitchBox( "true", "false" ));
		getContentPane().add(new JSwitch("on", "off"));
		getContentPane().add(new JSwitch("yes", "no"));
		getContentPane().add(new JSwitch("true", "false"));
		getContentPane().add(new JSwitch("on", "off"));
		getContentPane().add(new JSwitch("yes", "no"));
		getContentPane().add(new JSwitch("true", "false"));
		getContentPane().add(new JButton("text"));
	}

	public static void main(String[] args) throws Exception {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new TestJSwitch2().setVisible(true);
			}
		});
	}
}