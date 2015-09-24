
package com.sshtools.ui.swing;

/**
 *  Description of the Class
 *
 *@author     magicthize
 *@created    26 May 2002
 */
public interface ListSearchListener {
    /**
     * DOCUMENT ME!
     *
     * @param searchText DOCUMENT ME!
     */
    public void searchUpdated(String searchText);

    /**
     * DOCUMENT ME!
     */
    public void searchCancelled();

    /**
     * DOCUMENT ME!
     */
    public void searchComplete(String searchText);
}
