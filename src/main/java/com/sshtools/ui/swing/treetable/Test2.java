package com.sshtools.ui.swing.treetable;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public class Test2 {
	static class EmptyTreeTableModel extends AbstractTreeTableModel {

		public EmptyTreeTableModel() {
			super(null);
		}

		public Class getColumnClass(int column) {
			return String.class;
		}

		@Override
		public int getColumnCount() {
			return 1;
		}

		@Override
		public String getColumnName(int column) {
			return "";
		}

		@Override
		public Object getValueAt(Object node, int column) {
			return null;
		}

		@Override
		public Object getChild(Object parent, int index) {
			return null;
		}

		@Override
		public int getChildCount(Object parent) {
			return 0;
		}
	}
	static class MyTreeTableModel extends AbstractTreeTableModel {

		public MyTreeTableModel() {
			super(new File(System.getProperty("user.home")));
		}

		public Class getColumnClass(int column) {
			switch(column) {
			case 0:
				return TreeTableModel.class;
			default:
				return String.class;
			}
		}

		@Override
		public int getColumnCount() {
			return 2;
		}

		@Override
		public String getColumnName(int column) {
			switch(column) {
			case 0:
				return "File";
			default:
				return "Size";
			}
		}

		@Override
		public Object getValueAt(Object node, int column) {
			File f = (File)node;
			switch(column) {
			case 0:
				return f;
			default:
				return String.valueOf(f.length());
			}
		}

		@Override
		public Object getChild(Object parent, int index) {
			File f = (File)parent;
			return f.listFiles()[index];
		}

		@Override
		public int getChildCount(Object parent) {
			File f = (File)parent;
			File[] listFiles = f.listFiles();
			if(listFiles == null)
				return 0;
			return listFiles.length;
		}
	}


	public static void main(String[] args) {
		final int WIDTH = 600;
		final int HEIGHT = 400;
		final int LOCX = (Toolkit.getDefaultToolkit().getScreenSize().width - WIDTH) / 2;
		final int LOCY = (Toolkit.getDefaultToolkit().getScreenSize().height - HEIGHT) / 2;
		JFrame retFrame = new JFrame("TreeTable Example");
		retFrame.setBounds(LOCX, LOCY, WIDTH, HEIGHT);
		retFrame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				System.exit(0);
			}
		});
		retFrame.getContentPane().setLayout(new BorderLayout());
		JTreeTable treeTable = new JTreeTable(new EmptyTreeTableModel());
//		JTreeTable treeTable = new JTreeTable(new MyTreeTableModel());
		retFrame.getContentPane().add(new JScrollPane(treeTable), BorderLayout.CENTER);
		treeTable.setModel(new MyTreeTableModel());
		retFrame.setVisible(true);
	}
}
