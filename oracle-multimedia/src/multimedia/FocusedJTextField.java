package multimedia;
/* Copyright (c) 2003, 2005, Oracle. All rights reserved.  */

import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;

import javax.swing.JTextField;
import javax.swing.UIManager;

/** 
 * The FocusedJTextField class subclasses JTextField and overwrites 
 * isFocusTraversable to let it be able to gain
 * focus when it is set to be uneditable. 
 */
@SuppressWarnings("serial")
class FocusedJTextField extends JTextField
{
  /**
   * Constructs the JTextField with the focusListener
   * to update background if focus gained.
   * @param s  string displayed inside the textfield
   */
  public FocusedJTextField(String s)
  {
    super(s);

    addFocusListener(new FocusListener()
      {
        public void focusGained(FocusEvent e)
        {
          setBackground(UIManager.getColor("TextField.selectionBackground"));
        }
        public void focusLost(FocusEvent e)
        {
          setBackground(UIManager.getColor("TextArea.background"));
        }
      });

  }

  /** 
   * Returns true always. Let the JTextField be 
   * able to become focus owner. Default uneditable 
   * JTextField cannot gain focus.
   * @return true
   */
  public boolean isFocusTraversable()
  {
    return true;
  }

}
