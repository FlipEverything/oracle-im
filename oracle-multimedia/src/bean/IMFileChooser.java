package bean;


import javax.swing.JFileChooser;

/**
 * The IMFileChooser class inherits from JFileChooser in order
 * to add mnemonic and the full file name.
 */
@SuppressWarnings("serial")
public class IMFileChooser extends JFileChooser
{
  /**
   * Constructs the file chooser and sets the mnemonic.
   */
  public IMFileChooser()
  {
    super();
    setApproveButtonMnemonic('K');
    getAccessibleContext().setAccessibleDescription("File Chooser");
  }

  /**
   * Returns the full file name including path of the
   * selected file.
   */
  public String getText()
  {
    return getSelectedFile().toString();
  }
}
