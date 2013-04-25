package bean;


import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

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
    FileFilter filter = new FileNameExtensionFilter("Képfájlok (*.jpg, *.png, *.jpeg, *.bmp)", "jpg", "jpeg", "png", "bmp");

    FileFilter ff[] = this.getChoosableFileFilters();
    for (int i=0; i<ff.length; i++){
    	removeChoosableFileFilter(ff[i]);
    }
    
    addChoosableFileFilter(filter);
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
