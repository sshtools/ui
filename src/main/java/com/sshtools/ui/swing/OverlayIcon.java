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
package com.sshtools.ui.swing;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.SwingConstants;

/**
 * Create a {@link Icon} from an existing {@link Icon} by overlaying another
 * {@link Icon}.
 * <p>
 * The overlaid icon may be placed at any compass point.
 * 
 * @author Brett Smith <a href="mailto: brett@3sp.com">&lt;brett@3sp.com&gt;</a>
 * @see SwingConstants
 */
public class OverlayIcon implements Icon {

	// Private instance variables

	private Icon icon;
	private Icon overlayIcon;
	private int position;

	/**
	 * Constructor for the OverlayIcon object
	 * 
	 * @param overlayIcon
	 *            Description of the Parameter
	 * @param icon
	 *            Description of the Parameter
	 * @param position
	 *            Description of the Parameter
	 */
	public OverlayIcon(Icon overlayIcon, Icon icon, int position) {
		this.icon = icon;
		this.overlayIcon = overlayIcon;
		this.position = position;
	}

	/**
	 * Get the overlay icon
	 * 
	 * @return overlay icon
	 */
	public Icon getOverlayIcon() {
		return overlayIcon;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#getIconHeight()
	 */
	public int getIconHeight() {
		return (icon == null) ? 16 : icon.getIconHeight();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#getIconWidth()
	 */
	public int getIconWidth() {
		return (icon == null) ? 16 : icon.getIconWidth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.Icon#paintIcon(java.awt.Component, java.awt.Graphics,
	 * int, int)
	 */
	public void paintIcon(Component c, Graphics g, int x, int y) {
		if (icon != null) {
			icon.paintIcon(c, g, x, y);
			if(overlayIcon != null) {
	
				switch (position) {
				// TODO complete compass points and center
				case SwingConstants.NORTH:
					overlayIcon.paintIcon(
							c,
							g,
							x
									+ ((icon.getIconWidth() - overlayIcon
											.getIconWidth()) / 2), y);
					break;
				case SwingConstants.CENTER:
					overlayIcon.paintIcon(
							c,
							g,
							x
									+ ((icon.getIconWidth() - overlayIcon
											.getIconWidth()) / 2),
							y
									+ ((icon.getIconHeight() - overlayIcon
											.getIconHeight()) / 2));
					break;
				case SwingConstants.EAST:
					overlayIcon.paintIcon(
							c,
							g,
							(x + icon.getIconWidth()) - overlayIcon.getIconWidth(),
							y
									+ ((icon.getIconHeight() - overlayIcon
											.getIconHeight()) / 2));
					break;
				case SwingConstants.WEST:
					overlayIcon.paintIcon(
							c,
							g,
							x,
							y
									+ ((icon.getIconHeight() - overlayIcon
											.getIconHeight()) / 2));
					break;
				case SwingConstants.NORTH_WEST:
					overlayIcon.paintIcon(c, g, x, y);
					break;
				case SwingConstants.SOUTH_WEST:
					overlayIcon.paintIcon(c, g, x, (y + icon.getIconHeight())
							- overlayIcon.getIconHeight());
					break;
				case SwingConstants.NORTH_EAST:
					overlayIcon.paintIcon(c, g, (x + icon.getIconWidth())
							- overlayIcon.getIconWidth(), y);
					break;
				case SwingConstants.SOUTH_EAST:
					overlayIcon.paintIcon(
							c,
							g,
							(x + icon.getIconWidth()) - overlayIcon.getIconWidth(),
							(y + icon.getIconHeight())
									- overlayIcon.getIconHeight());
					break;
				case SwingConstants.SOUTH:
					overlayIcon.paintIcon(
							c,
							g,
							x
									+ ((icon.getIconWidth() - overlayIcon
											.getIconWidth()) / 2),
							(y + icon.getIconHeight())
									- overlayIcon.getIconHeight());
					break;
				default:
					overlayIcon.paintIcon(
							c,
							g,
							(x + icon.getIconWidth()) - overlayIcon.getIconWidth(),
							(y + icon.getIconHeight())
									- overlayIcon.getIconHeight());
				}
			}
		}
	}
}
