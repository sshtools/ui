package com.sshtools.ui.swing.treetable;

import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JFrame;

public class Test {
	static class MyTreeTableModel extends AbstractTreeTableModel {
		private final String[] ACCOUNT_COLUMNS = { "Accounts", "User Name" };

		public MyTreeTableModel() {
			super(new Accounts());
		}

		private Class[] cTypes = { TreeTableModel.class, String.class };

		public Class getColumnClass(int column) {
			return cTypes[column];
		}

		public int getChildCount(Object node) {
			Object[] children = getChildren(node);
			return (children == null) ? 0 : children.length;
		}

		public Object getChild(Object node, int i) {
			return getChildren(node);
		}

		protected Object[] getChildren(Object node) {
			if (node instanceof Accounts) {
				Accounts accounts = (Accounts) node;
				return accounts.getAccounts().toArray();
			} else if (node instanceof Account) {
				Account account = (Account) node;
				return account.getEmails().toArray();
			} else {
				return null;
			}
		}

		public Object getValueAt(Object node, int column) {
			return getChildren(node)[column];
		}

		public int getColumnCount() {
			return ACCOUNT_COLUMNS.length;
		}

		public String getColumnName(int column) {
			return ACCOUNT_COLUMNS[column];
		}
	}

	static class Account {
		private String accountName = null;
		private String accountType = null;
		ArrayList emails = null;

		public Account(String accountName, String accountType) {
			this.accountName = accountName;
			this.accountType = accountType;
			emails = new ArrayList();
		}

		public String getAccountName() {
			return accountName;
		}

		public String getAccountType() {
			return accountType;
		}

		public String[] getEmailAddresses() {
			return ((String[]) emails.toArray(new String[0]));
		}

		public ArrayList getEmails() {
			return emails;
		}

		public void addEmailAddress(String emailAddress) {
			emails.add(emailAddress);
		}

		public String toString() {
			return getAccountName();
		}
	}

	static public class Accounts {
		private ArrayList accounts = new ArrayList();

		public Accounts() {
			Account account1 = new Account("Yahoo", "POP3/SMTP");
			account1.addEmailAddress("abe@yahoo.com");
			account1.addEmailAddress("sue@yahoo.com");
			Account account2 = new Account("EDS", "POP3/SMTP");
			account2.addEmailAddress("mark@eds.com");
			account2.addEmailAddress("jane@eds.com");
			Account account3 = new Account("NOFFCROFF", "POP3/SMTP");
			account3.addEmailAddress("mark@noffcroff.org");
			account3.addEmailAddress("manny@noffcroff.org");
			account3.addEmailAddress("abe@noffcroff.org");
			account3.addEmailAddress("whocares@noffcroff.org");
			accounts = new ArrayList();
			accounts.add(account1);
			accounts.add(account2);
			accounts.add(account3);
		}

		public ArrayList getAccounts() {
			return accounts;
		}

		public String toString() {
			return "Accounts";
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
		JTreeTable treeTable = new JTreeTable(new MyTreeTableModel());
		retFrame.getContentPane().add(treeTable, BorderLayout.CENTER);
		retFrame.setVisible(true);
	}
}
