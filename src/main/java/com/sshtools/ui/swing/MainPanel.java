package com.sshtools.ui.swing;

//-*- mode:java; encoding:utf8n; coding:utf-8 -*-
// vim:set fileencoding=utf-8:
//http://terai.xrea.jp/Swing/DnDTabbedPane.html
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.dnd.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.swing.*;
import javax.swing.table.*;

public class MainPanel extends JPanel {
	private final ClosableTabbedPane tab = new ClosableTabbedPane();
	private final JCheckBox gcheck = new JCheckBox("Tab Ghost", true);
	private final JCheckBox tcheck = new JCheckBox("Top", true);
	private final JCheckBox scheck = new JCheckBox("SCROLL_TAB_LAYOUT", true);
	private final JCheckBox debugp = new JCheckBox("Debug Paint", true);

	public MainPanel() {
		super(new BorderLayout());
		ClosableTabbedPane sub = new ClosableTabbedPane();
		// sub.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		sub.addTab("Title aa", new JLabel("aaa"));
		sub.addTab("Title bb", new JScrollPane(new JTree()));
		sub.addTab("Title cc", new JScrollPane(new JTextArea("123412341234\n46746745\n245342\n")));

		tab.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tab.addTab("JTree 00", new JScrollPane(new JTree()));
		tab.addTab("JLabel 01", new JLabel("Test"));
		tab.addTab("JTable 02", new JScrollPane(makeJTable()));
		tab.addTab("JTextArea 03", new JScrollPane(new JTextArea("asfasdfasfasdfas\nafasfasdfaf\n")));
		tab.addTab("JLabel 04", new JLabel("<html>asfasfdasdfasdfsa<br>asfdd13412341234123446745fgh"));
		tab.addTab("null 05", null);
		tab.addTab("JTabbedPane 06", sub);
		tab.addTab("Title 000000000000000006", new JScrollPane(new JTree()));
		tab.setTabComponentAt(2, new JLabel("XXXXXXX"));
		
		JPanel xp = new JPanel(new BorderLayout());
		xp.add(new JLabel("YYYYY"), BorderLayout.CENTER);
		tab.setTabComponentAt(3, xp);
		

		add(makeCheckBoxes(), BorderLayout.NORTH);
		add(tab);
		// setPreferredSize(new Dimension(320, 240));
	}

	private JPanel makeCheckBoxes() {
		gcheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tab.setPaintGhost(gcheck.isSelected());
			}
		});
		tcheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tab.setTabPlacement(tcheck.isSelected() ? JTabbedPane.TOP : JTabbedPane.RIGHT);
			}
		});
		scheck.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tab.setTabLayoutPolicy(scheck.isSelected() ? JTabbedPane.SCROLL_TAB_LAYOUT : JTabbedPane.WRAP_TAB_LAYOUT);
			}
		});
		debugp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tab.setPaintScrollArea(debugp.isSelected());
			}
		});
		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p1.add(gcheck);
		p1.add(tcheck);
		JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		p2.add(scheck);
		p2.add(debugp);
		JPanel p = new JPanel(new BorderLayout());
		p.add(p1, BorderLayout.NORTH);
		p.add(p2, BorderLayout.SOUTH);
		return p;
	}

	private JTable makeJTable() {
		String[] columnNames = { "String", "Integer", "Boolean" };
		Object[][] data = { { "AAA", 1, true }, { "BBB", 2, false }, };
		DefaultTableModel model = new DefaultTableModel(data, columnNames) {
			public Class<?> getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}
		};
		JTable table = new JTable(model);
		return table;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	public static void createAndShowGUI() {
//		try {
//			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		JFrame frame = new JFrame("DnDTabbedPane");
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(new MainPanel());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}