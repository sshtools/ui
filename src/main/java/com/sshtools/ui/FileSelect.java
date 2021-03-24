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
package com.sshtools.ui;

import java.awt.Component;
import java.io.File;

import com.sshtools.ui.awt.AWTFileSelector;

/**
 *  
 */
public class FileSelect {
	public final static int FILES_AND_DIRECTORIES = 0;
	public final static int DIRECTORIES_ONLY = 1;
	private FileSelector selector;

	public FileSelect(int type, File cwd) {
		this(type, cwd, true, true);
	}

	public FileSelect(int type, File cwd, boolean showButtons, boolean showHiddenFilesSwitch) {
		this(type, cwd, showButtons, showHiddenFilesSwitch, true, false);
	}

	public FileSelect(int type, File cwd, boolean showButtons, boolean showHiddenFilesSwitch, boolean showButtonImages,
			boolean showButtonText) {
		try {
			selector = (FileSelector) Class.forName("com.sshtools.ui.swing.SwingFileSelector").newInstance(); //$NON-NLS-1$
		} catch (Throwable t) {
			selector = new AWTFileSelector();
		}
		selector.init(type, cwd, showButtons, showHiddenFilesSwitch, showButtonImages, showButtonText);
	}

	public void setUseAcceptAllFilter(boolean useAcceptAllFilter) {
		selector.setUseAcceptAllFilter(useAcceptAllFilter);
	}

	public void addFileFilter(FileFilter filter) {
		selector.addFileFilter(filter);
	}

	public File[] getSelectedFiles() {
		return selector.getSelectedFiles();
	}

	public File getSelectedFile() {
		return selector.getSelectedFile();
	}

	public void refresh() {
		selector.refresh();
	}

	public void setAllowMultipleSelection(boolean allowMultipleSelection) {
		selector.setAllowMultipleSelection(allowMultipleSelection);
	}

	public Option showDialog(Component parent, String title) {
		return selector.showDialog(parent, title);
	}

	public File getWorkingDirectory() {
		return selector.getWorkingDirectory();
	}

	public void setSelectedFileFilter(FileFilter filter) {
		selector.setSelectedFileFilter(filter);
	}

	public void setWorkingDirectory(File cwd) {
		selector.setWorkingDirectory(cwd);
	}
}