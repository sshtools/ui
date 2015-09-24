package com.sshtools.ui.swing.dynamenu;


import javax.swing.AbstractListModel;

/**
 *
 *
 * @author $author$
 */

public class DynamicMenuListModel

    extends AbstractListModel {

  private DynamicMenuList mru;
  /**
   * Creates a new MRUListModel object.
   */

  public DynamicMenuListModel() {
    this(null);
  }

  /**
   * Creates a new MRUListModel object.
   */

  public DynamicMenuListModel(DynamicMenuList list) {
    super();
    setMRUList(list);

  }

  /**
   *
   *
   * @param f
   */

  public void add(DynamicMenuItem f) {
    if(mru != null) {
      mru.insertElementAt(f, 0);
      for (int i = mru.size() - 1; i >= 1; i--) {
        if (((DynamicMenuItem) mru.elementAt(i)).equals(f)) {
          mru.removeElementAt(i);
        }
      }
  
      fireContentsChanged(this, 0, getSize() - 1);
    }
  }

  public void remove(DynamicMenuItem f) {
    mru.removeElement(f);
    fireContentsChanged(this, 0, getSize() - 1);
  }

  /**
   *
   *
   * @param i
   *
   * @return
   */

  public Object getElementAt(int i) {
    return mru.get(i);

  }

  /**
   *
   *
   * @return
   */

  public int getSize() {
    return (mru == null) ? 0 : mru.size();

  }

  /**
   *
   *
   * @param mru
   */

  public void setMRUList(DynamicMenuList mru) {
    this.mru = mru;
    fireContentsChanged(this, 0, getSize());
  }

  /**
   *
   *
   * @return
   */

  public DynamicMenuList getMRUList() {
    return mru;

  }

}
