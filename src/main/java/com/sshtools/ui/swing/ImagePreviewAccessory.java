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
/* HEADER */
package com.sshtools.ui.swing;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFileChooser;

public class ImagePreviewAccessory extends JComponent implements
		PropertyChangeListener {
	/**
     * 
     */
	ImageIcon thumbnail = null;
	File file = null;

	public ImagePreviewAccessory(JFileChooser fc) {
		setBorder(BorderFactory.createTitledBorder("Image preview"));
		setPreferredSize(new Dimension(100, 50));
		fc.addPropertyChangeListener(this);
	}

	public void loadImage() {
		if (file == null) {
			return;
		}
		ImageIcon tmpIcon = new ImageIcon(file.getPath());
		if (tmpIcon.getIconWidth() > 90) {
			thumbnail = new ImageIcon(tmpIcon.getImage().getScaledInstance(90,
					-1, Image.SCALE_DEFAULT));
		} else {
			thumbnail = tmpIcon;
		}
	}

	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();
		if (prop.equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)) {
			file = (File) e.getNewValue();
			loadImage();
			repaint();
		}
	}

	public void paintComponent(Graphics g) {
		if (thumbnail == null) {
			loadImage();
		}
		if (thumbnail != null) {
			int x = getWidth() / 2 - thumbnail.getIconWidth() / 2;
			int y = getHeight() / 2 - thumbnail.getIconHeight() / 2;
			if (y < 0) {
				y = 0;
			}
			if (x < 5) {
				x = 5;
			}
			thumbnail.paintIcon(this, g, x, y);
		}
	}
}