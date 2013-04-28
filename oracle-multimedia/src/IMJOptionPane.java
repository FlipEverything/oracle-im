

import javax.swing.JOptionPane;
import javax.swing.Icon;
import javax.swing.JDialog;

import java.awt.Component;

/**
 * The IMJOptionPane class subclasses JOptionPane in order to add
 * AccessibleDescription to the displayed dialog
 */
@SuppressWarnings("serial")
public class IMJOptionPane extends JOptionPane
{
  // Needs the following calls here because otherwise the overwritten 
  // showOptionDialog will not be called. 
  public static void showMessageDialog(Component parentComponent, Object message,
      String title, int messageType)
  {
    showOptionDialog(parentComponent, message, title, DEFAULT_OPTION, messageType,
        null, null, null);
  }

  public static int showConfirmDialog(Component parentComponent, Object message,
      String title, int optionType)
  {
    return showOptionDialog(parentComponent, message, title, optionType, 
        QUESTION_MESSAGE, null, null, null);
  }

  public static int showOptionDialog(Component parentComponent, Object message,
      String title, int optionType,
      int messageType, Icon icon,
      Object[] options, Object initialValue) 
  {
    JOptionPane pane = new JOptionPane(message, messageType,
        optionType, icon,
        options, initialValue);

    pane.setInitialValue(initialValue);

    JDialog dialog = pane.createDialog(parentComponent, title);

    // The following is the only line added.
    dialog.getAccessibleContext().setAccessibleDescription((String)message);

    pane.selectInitialValue();
    dialog.setVisible(true);

    Object selectedValue = pane.getValue();

    if(selectedValue == null)
      return CLOSED_OPTION;
    if(options == null) 
    {
      if(selectedValue instanceof Integer)
        return ((Integer)selectedValue).intValue();
      return CLOSED_OPTION;
    }

    for(int counter = 0, maxCounter = options.length;
        counter < maxCounter; counter++) 
    {
      if(options[counter].equals(selectedValue))
        return counter;
    }

    return CLOSED_OPTION;
  }
}
