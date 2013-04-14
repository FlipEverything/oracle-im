

import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.BorderFactory;

import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;


/** 
 * The FocusedJLabel class subclasses JLabel and overwrites 
 * isFocusTraversable and adds focus listener to let 
 * it be able to gain focus.
 */
@SuppressWarnings("serial")
class FocusedJLabel extends JLabel
{
  /**
   * Constructs the JLabel and adds focus listener.
   */
  FocusedJLabel(String sLabel)
  {
    super(sLabel);
    addFocusListener(new FocusListener()
      {
        public void focusGained(FocusEvent e)
        {
          ((JLabel)e.getSource()).setBorder(
                   UIManager.getBorder("Table.focusCellHighlightBorder")
                   ); 
        }
        public void focusLost(FocusEvent e)
        {
          ((JLabel)e.getSource()).setBorder(
                   BorderFactory.createEmptyBorder(0, 10, 0, 10)
                   );
        }
      });
  }

  /** 
   * Returns true always. Let the JLabel be 
   * able to become focus owner. Default JLabel cannot
   * gain focus.
   * @return true
   */
  public boolean isFocusTraversable()
  {
    return true;
  }
}
