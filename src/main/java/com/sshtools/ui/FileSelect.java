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

	private FileFilter acceptAllFilter = new FileFilter() {

		public String getDescription() {
			return Messages.getString("FileSelect.allFiles"); //$NON-NLS-1$
		}

		public boolean accept(File f) {
			return true;
		}

	};

	public FileSelect(int type, File cwd) {
		this(type, cwd, true, true);
	}

	public FileSelect(int type, File cwd, boolean showButtons,
			boolean showHiddenFilesSwitch) {
		this(type, cwd, showButtons, showHiddenFilesSwitch, true, false);
	}

	public FileSelect(int type, File cwd, boolean showButtons,
			boolean showHiddenFilesSwitch, boolean showButtonImages,
			boolean showButtonText) {
		try {
			selector = (FileSelector) Class.forName(
					"com.sshtools.ui.swing.SwingFileSelector").newInstance(); //$NON-NLS-1$
		} catch (Throwable t) {
			selector = new AWTFileSelector();
		}
		selector.init(type, cwd, showButtons, showHiddenFilesSwitch,
				showButtonImages, showButtonText);
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