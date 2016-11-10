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
package com.sshtools.ui.swing.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Vector;

public class FileSelection extends Vector implements Transferable {
	public static DataFlavor FILE_SELECTION_FLAVOR = new DataFlavor(
			DataFlavor.javaJVMLocalObjectMimeType, "FileSelection");

	public FileSelection() {
		super();
	}

	/* Returns the array of flavors in which it can provide the data. */
	public synchronized DataFlavor[] getTransferDataFlavors() {
		return new DataFlavor[] { FILE_SELECTION_FLAVOR };
	}

	/* Returns whether the requested flavor is supported by this object. */
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return FILE_SELECTION_FLAVOR == flavor;
	}

	/**
	 * If the data was requested in the "java.lang.String" flavor, return the
	 * String representing the selection.
	 */
	public synchronized Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		if (flavor.equals(FILE_SELECTION_FLAVOR)) {
			return this;
		}
		throw new UnsupportedFlavorException(flavor);
	}

	public String toString() {
		StringBuffer buf = new StringBuffer("File selection of");
		buf.append(size());
		for (Iterator i = iterator(); i.hasNext();) {
			buf.append("\n");
			File file = (File) i.next();
			buf.append("    ");
			buf.append(file.getAbsolutePath());
			buf.append(" (");
			try {
				buf.append(file.length());
			} catch (Exception ioe0) {
			}
			buf.append(")");
		}
		return buf.toString();
	}
}