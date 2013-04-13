/* Copyright (c) 2003, 2005, Oracle. All rights reserved.  */

import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

import javax.swing.JTextArea;
import javax.swing.UIManager;

/** 
 * The FocusedJTextArea class subclasses JTextArea and overwrites 
 * isFocusTraversable to let it be able to gain
 * focus when it is set to be uneditable. It also overrides 
 * isManagingFocus to force it to not handle TAB. 
 */
@SuppressWarnings("serial")
class FocusedJTextArea extends JTextArea
{
  /**
   * Constructs the JTextArea with specified
   * numbers of rows and columns and adds the
   * focusListener.
   * @param r  number of rows of displayed area
   * @param c  number of columns of displayed area
   */
  public FocusedJTextArea(int r, int c)
  {
    super(r, c);

    addFocusListener(new FocusListener()
      {
        public void focusGained(FocusEvent e)
        {
          setBackground(UIManager.getColor("TextArea.selectionBackground"));
        }
        public void focusLost(FocusEvent e)
        {
          setBackground(UIManager.getColor("TextArea.background"));
        }
      });

  }

  /** 
   * Returns true always. Let the JTextArea be 
   * able to become focus owner. Default uneditable 
   * JTextArea cannot gain focus.
   * @return true
   */
  public boolean isFocusTraversable()
  {
    return true;
  }

  /** 
   * Returns false always. Do not allow JTextArea to handle
   * TAB.
   * @return false
   */
  public boolean isManagingFocus() 
  {
    return false;
  }
}
