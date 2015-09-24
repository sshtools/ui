package com.sshtools.ui.swing;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class TestJSwitch extends JFrame {

	public TestJSwitch() {
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
//		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				new TestJSwitch().setVisible(true);
			}
		});
	}
}