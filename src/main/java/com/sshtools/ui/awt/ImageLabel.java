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
package com.sshtools.ui.awt;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Label;

/**
 * Component that can display some text and an image
 *
 * @author $Author: brett $
 */
public class ImageLabel extends BevelPanel {

	private ImageCanvas imageCanvas;
	private Label textLabel;

	/**
	 * Construct a image label with no text or image
	 */
	public ImageLabel() {
		this(null, null);
	}

	/**
	 * Construct a new image label with an image
	 *
	 * @param image
	 *            image
	 */
	public ImageLabel(Image image) {
		this(null, image);
	}

	/**
	 * Construct a new image label with some text
	 *
	 * @param text
	 *            text
	 */
	public ImageLabel(String text) {
		this(text, null);
	}

	/**
	 * Construct a new image label with an image and some text
	 *
	 * @param text
	 *            text
	 * @param image
	 *            image
	 */
	public ImageLabel(String text, Image image) {
		super(NONE, new BorderLayout(2, 0));
		imageCanvas = new ImageCanvas();
		textLabel = new Label() {
			public Dimension getMinimumSize() {
				return getPreferredSize();
			}

			public void processEvent(AWTEvent evt) {
				ImageLabel.this.dispatchEvent(evt);
			}
		};
		add(imageCanvas, BorderLayout.WEST);
		add(textLabel, BorderLayout.CENTER);
		setText(text);
		setImage(image);
	}

	/**
	 * Set the image
	 *
	 * @param image
	 *            image
	 */
	public void setImage(Image image) {
		imageCanvas.setImage(image);
		imageCanvas.setVisible(image != null);
	}

	/**
	 * Get the image
	 *
	 * @retirm image
	 */
	public Image getImage() {
		return imageCanvas.getImage();
	}

	/**
	 * Set the text
	 *
	 * @param text
	 *            text
	 */
	public void setText(String text) {
		textLabel.setText(text);
		textLabel.setVisible(text != null);
	}

	/**
	 * Set the font
	 *
	 * @param font
	 *            font
	 */
	public void setFont(Font font) {
		super.setFont(font);
		textLabel.setFont(font);
	}

	/**
	 * Set foreground color of text
	 *
	 * @param color
	 *            foreground color
	 */
	public void setForeground(Color foreground) {
		super.setForeground(foreground);
		textLabel.setForeground(foreground);
	}
}
