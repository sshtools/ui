/* HEADER */
package com.sshtools.ui.swing;

import java.awt.event.ActionEvent;

import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

public abstract class ToggledPopupAction extends AbstractToggleableAction {
	JToggleButton toggle;
	ToggledPopupMenu popup;
	int edge;

	public ToggledPopupAction() {
		this(SwingConstants.NORTH);
	}

	public ToggledPopupAction(int edge) {
		super();
		this.edge = edge;
	}

	public void setEdge(int edge) {
		this.edge = edge;
	}

	public void setPopup(ToggledPopupMenu popup) {
		this.popup = popup;
	}

	public void setToggle(JToggleButton toggle) {
		this.toggle = toggle;
	}

	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource() instanceof JComponent) {
			JComponent c = (JComponent) evt.getSource();
			// if (!popup.ignoreNextToggleAction) {
			if (!toggle.isSelected()) {
				popup.setVisible(false);
			} else {
				switch (edge) {
				case SwingConstants.NORTH:
					popup.show(c, 0, popup.getPreferredSize().height * -1);
					break;
				case SwingConstants.EAST:
					popup.show(c, toggle.getSize().width, 01);
					break;
				}
			}
			// } else {
			// toggle.setSelected(false);
			// popup.ignoreNextToggleAction = false;
			// }
		}
	}
}