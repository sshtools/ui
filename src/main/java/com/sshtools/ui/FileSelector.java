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

/**
 *  
 */
public interface FileSelector {

	public void init(int type, File cwd, boolean showButtons,
			boolean showHiddenFilesSwitch, boolean showButtonImages,
			boolean showButtonText);

	public void setUseAcceptAllFilter(boolean useAcceptAllFilter);

	public void addFileFilter(FileFilter filter);

	public File[] getSelectedFiles();

	public File getSelectedFile();

	public void refresh();

	public void setAllowMultipleSelection(boolean allowMultipleSelection);

	public Option showDialog(Component parent, String title);

	public File getWorkingDirectory();

	public void setSelectedFileFilter(FileFilter filter);

	public void setWorkingDirectory(File cwd);
}