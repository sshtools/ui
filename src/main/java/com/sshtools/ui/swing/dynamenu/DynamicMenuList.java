package com.sshtools.ui.swing.dynamenu;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

/**
 *
 *
 * @author $author$
 */

public abstract class DynamicMenuList

    extends java.util.Vector {


  /**
   * Creates a new MRUList object.
   */

  public DynamicMenuList() {
    super();
    reload();
  }


  /**
   *
   *
   * @param in
   *
   * @throws SAXException
   * @throws ParserConfigurationException
   * @throws IOException
   */

  public abstract void reload();



}
