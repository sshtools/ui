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
package com.sshtools.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.sshtools.ui.swing.ArrowIcon;
import com.sshtools.ui.swing.ColorIcon;
import com.sshtools.ui.swing.ComboBoxRenderer;

public class ColorComboBox extends JComboBox<Color> {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new ColorComboBox object.
	 */
	public ColorComboBox() {
		this(null);
	}

	public ColorComboBox(Color color) {
		super();
		setModel(new ColorModel());
		setColor(color);
		setRenderer(new ColorRenderer(this));
		addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (getSelectedItem() == null) {
					chooseCustomColor();
				} else {
					fireChangeEvent();
				}
			}
		});
	}

	protected void fireChangeEvent() {
		ChangeEvent evt = new ChangeEvent(this);
		ChangeListener[] l = (ChangeListener[]) listenerList.getListeners(ChangeListener.class);
		for (int i = (l.length - 1); i >= 0; i--) {
			l[i].stateChanged(evt);
		}
	}

	public void addChangeListener(ChangeListener l) {
		listenerList.add(ChangeListener.class, l);
	}

	public void removeChangeListener(ChangeListener l) {
		listenerList.remove(ChangeListener.class, l);
	}

	private void chooseCustomColor() {
		Color c = JColorChooser.showDialog(this, "Custom Color", Color.black);
		if (c != null) {
			setColor(c);
			fireChangeEvent();
		}
	}

	public void setColor(Color c) {
		for (int i = 0; i < getModel().getSize() - 1; i++) {
			Color z = getModel().getElementAt(i);
			if (z != null && z.equals(c)) {
				setSelectedIndex(i);
				return;
			}
		}
		if (c != null) {
			((ColorModel) getModel()).addElement(c);
		}
	}

	public Color getColor() {
		return (Color) getSelectedItem();
	}

	class ColorModel extends DefaultListModel<Color> implements ComboBoxModel<Color> {
		private static final long serialVersionUID = 1L;
		private Object selected;

		ColorModel() {
			addElement(Color.black);
			addElement(Color.white);
			addElement(Color.red);
			addElement(Color.orange);
			addElement(Color.yellow);
			addElement(Color.green);
			addElement(Color.blue);
			addElement(Color.cyan);
			addElement(Color.magenta);
			addElement(Color.pink);
			addElement(Color.lightGray);
			addElement(Color.gray);
			addElement(Color.darkGray);
			addElement(null);
		}

		@Override
		public void setSelectedItem(Object selected) {
			this.selected = selected;
			fireContentsChanged(this, 0, getSize());
		}

		@Override
		public Object getSelectedItem() {
			return selected;
		}
	}

	class ColorRenderer extends ComboBoxRenderer<Color> {
		private ColorIcon icon;

		ColorRenderer(JComboBox<Color> combo) {
			super(combo);
			icon = new ColorIcon(Color.black, new Dimension(10, 10), Color.black);
		}

		@Override
		protected void decorate(JLabel label, JList<? extends Color> list, Color value, int index, boolean isSelected,
				boolean cellHasFocus) {
			Color c = (Color) value;
			// If the value is null. Then this signifies custom color
			if (c == null) {
				label.setIcon(new ArrowIcon(ArrowIcon.EAST));
				label.setText("Choose ....");
			} else {
				// Set up the icon
				icon.setColor(c);
				label.setIcon(icon);
				// Set the text. If the color is a well known one with a name,
				// render
				// the name. Otherwise use the RGB values
				String s = "#" + c.getRed() + "," + c.getGreen() + "," + c.getBlue();
				if (c.equals(Color.black)) {
					s = "Black";
				} else if (c.equals(Color.white)) {
					s = "White";
				} else if (c.equals(Color.red)) {
					s = "Red";
				} else if (c.equals(Color.orange)) {
					s = "Orange";
				} else if (c.equals(Color.yellow)) {
					s = "Yellow";
				} else if (c.equals(Color.green)) {
					s = "Green";
				} else if (c.equals(Color.blue)) {
					s = "Blue";
				} else if (c.equals(Color.cyan)) {
					s = "Cyan";
				} else if (c.equals(Color.magenta)) {
					s = "Magenta";
				} else if (c.equals(Color.pink)) {
					s = "Pink";
				} else if (c.equals(Color.lightGray)) {
					s = "Light Gray";
				} else if (c.equals(Color.gray)) {
					s = "Gray";
				} else if (c.equals(Color.darkGray)) {
					s = "Dark Gray";
				}
				label.setText(s);
			}
		}
	}
}