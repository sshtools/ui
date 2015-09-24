/* HEADER */

package com.sshtools.ui.swing.dynamenu;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * An extension of a {@link JMenu} that is built using a model of Most Recently
 * Used items from a {@link MRUModel}. When the model changes, the menu will
 * automatically update itself.
 * 
 * @author $Auth$
 */

public class DynamicMenu

extends JMenu

implements ListDataListener, ActionListener {

	private DynamicMenuListModel model;
	private String actionCommand;
	private boolean considerStateOnUpdate = false;

	/**
	 * Creates a new MRUMenu menu.
	 * 
	 * @param action
	 * @param model
	 */

	protected DynamicMenu(Action action, DynamicMenuListModel model, String actionCommand, boolean considerStateOnUpdate) {
		super(action);
		init(model);
		this.actionCommand = actionCommand;
		this.considerStateOnUpdate = considerStateOnUpdate;

	}

	/**
	 * Creates a new MRUMenu object.
	 * 
	 * @param text
	 * @param model
	 */

	protected DynamicMenu(String text, DynamicMenuListModel model) {
		super(text);
		init(model);
	}

	public void addNotify() {
		super.addNotify();
		rebuildMenu();
		model.addListDataListener(this);
	}

	/**
	 * 
	 */

	public void cleanUp() {
		if (model != null) {
			model.removeListDataListener(this);
		}

	}

	public void removeNotify() {
		super.removeNotify();
		cleanUp();
	}

	private void init(DynamicMenuListModel model) {
		this.model = model;

	}

	/**
	 * 
	 * 
	 * @param e
	 */

	public void intervalAdded(ListDataEvent e) {
		rebuildMenu();

	}

	/**
	 * 
	 * 
	 * @param e
	 */

	public void intervalRemoved(ListDataEvent e) {
		rebuildMenu();

	}

	/**
	 * 
	 * 
	 * @param e
	 */

	public void contentsChanged(ListDataEvent e) {
		rebuildMenu();

	}

	/**
	 * 
	 * 
	 * @param evt
	 */

	public void actionPerformed(ActionEvent evt) {
		fireActionPerformed(evt);
	}

	private void rebuildMenu() {

		boolean state = isEnabled();

		Component[] c = getMenuComponents();
		for (int i = 0; (c != null) && (i < c.length); i++) {
			((JMenuItem) c[i]).removeActionListener(this);
			remove(c[i]);
		}
		for (int i = 0; i < model.getSize(); i++) {
			DynamicMenuItem f = (DynamicMenuItem) model.getElementAt(i);
			String name = f.getName();
			JMenuItem m = new JMenuItem(name);
			m.setActionCommand(f.getUniqueId());
			m.setToolTipText(f.getToolTipText());
			m.setIcon(f.getIcon());
			m.addActionListener(this);
			add(m);
		}
		setEnabled(!considerStateOnUpdate ? model.getSize() > 0 : model.getSize() > 0 && state);
		validate();

	}

}