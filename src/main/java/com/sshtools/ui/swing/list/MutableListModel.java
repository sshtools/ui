package com.sshtools.ui.swing.list;

import javax.swing.ListModel;

// @author Santhosh Kumar T - santhosh@in.fiorano.com 
public interface MutableListModel extends ListModel {
	public boolean isCellEditable(int index);

	public void setValueAt(Object value, int index);
}