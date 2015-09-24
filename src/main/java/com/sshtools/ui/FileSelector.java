/* HEADER */
package com.sshtools.ui;

import java.awt.Component;
import java.io.File;


/**
 *  
 */
public interface FileSelector  {

    public void init(int type, File cwd, boolean showButtons, boolean showHiddenFilesSwitch, boolean showButtonImages, boolean showButtonText);    
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