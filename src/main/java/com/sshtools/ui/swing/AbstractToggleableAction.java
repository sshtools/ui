/* HEADER */
package com.sshtools.ui.swing;

import java.awt.event.ActionEvent;

public class AbstractToggleableAction extends ToggleableAction {
    boolean selected;

    public AbstractToggleableAction() {
        super();
    }

    public void setSelected(boolean selected) {
        boolean was = this.selected;
        this.selected = selected;
        this.firePropertyChange("selected", new Boolean(was), new Boolean(selected));
    }

    public boolean isSelected() {
        return selected;
    }

    public void actionPerformed(ActionEvent evt) {
        setSelected(!isSelected());
    }
}