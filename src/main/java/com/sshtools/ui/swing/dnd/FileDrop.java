/* HEADER */
package com.sshtools.ui.swing.dnd;

import java.awt.Component;
import java.awt.dnd.DropTarget;

public class FileDrop {

    public final static void addFileDrop(FileDropListener listener, Component component) {
        DropTarget dropTarget  = new DropTarget(component, listener);
        component.setDropTarget(dropTarget);
    }
    
    public final static void removeFileDrop(Component component) {
        component.setDropTarget(null);
    }
}
