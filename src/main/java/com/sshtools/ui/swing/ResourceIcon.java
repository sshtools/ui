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

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.HashMap;

import javax.swing.ImageIcon;

/**
 * Icon that can use a <code>Class</code> reference to determine a
 * <code>ClassLoader</code> to use to load an icon resource.
 * 
 * @author $Author: brett $
 */
public class ResourceIcon extends ImageIcon {
	// Private instance variables
	private Class cls;

	private static HashMap imageCache = new HashMap();

	public ResourceIcon(String image) {
		super();
		init(getClass(), image);
	}

	/**
	 * Creates a new ResourceIcon given a class to derive the class loader from
	 * and the image path.
	 * 
	 * @param cls
	 *            class to derive class loader for image from
	 * @param image
	 *            image path
	 */
	public ResourceIcon(Class cls, String image) {
		super();
		init(cls, image);
	}

	public ResourceIcon(URL iconResource) {
		super(iconResource);
	}

	public void init(Class cls, String image) {
		this.cls = cls;
		if (image.startsWith("/")) { //$NON-NLS-1$
			loadImage(image);
		} else {
			int idx = cls.getName().lastIndexOf('.');
			String packageName = idx == -1 ? null : cls.getName().substring(0,
					idx);
			String path = packageName == null ? "" : "/" + packageName; //$NON-NLS-1$ //$NON-NLS-2$
			path = path.replace('.', '/');
			path += ("/" + image); //$NON-NLS-1$
			loadImage(path);
		}
	}

	protected void loadImage(String imageName) {
		Image image = (Image) imageCache.get(imageName);
		if (image == null) {
			URL url = cls.getResource(imageName);
			if (url != null) {
				image = Toolkit.getDefaultToolkit().getImage(url);
			} else {
				url = cls.getClassLoader().getResource(imageName);
				if (url != null) {
					image = Toolkit.getDefaultToolkit().getImage(url);
				} else {
					image = Toolkit.getDefaultToolkit().getImage(imageName);
				}
			}
			imageCache.put(imageName, image);
		}
		if (image != null) {
			this.setImage(image);
		} else {
			System.err.println(Messages.getString("ResourceIcon.nullImage")); //$NON-NLS-1$
		}
	}
}
