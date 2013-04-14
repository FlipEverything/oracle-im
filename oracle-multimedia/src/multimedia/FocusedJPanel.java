package multimedia;
/* Copyright (c) 2003, 2008, Oracle. All rights reserved.  */

import javax.swing.JPanel;

/** 
 * The FocusedJPanel class subclasses JPanel and overwrites 
 * isFocusTraversable to let it be able to gain focus.
 */
@SuppressWarnings("serial")
class FocusedJPanel extends JPanel
{
  /**
   * Constructs the JPanel.
   */
  FocusedJPanel()
  {
    super();
  }

  /** 
   * Returns true always. Let the JPanel be 
   * able to become focus owner. Default JPanel cannot
   * gain focus.
   * @return true
   */
  public boolean isFocusTraversable()
  {
    return true;
  }
}
