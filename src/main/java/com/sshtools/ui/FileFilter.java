/* HEADER */
package com.sshtools.ui;

import java.io.File;


public interface FileFilter {
    public String getDescription();
    public boolean accept(File f);
}