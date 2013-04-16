package bean;


import javax.swing.JTable;
import javax.swing.table.TableModel;

/** 
 * The IMTable class subclasses JTable and overwrites 
 * isManagingFocus to avoid letting the table
 * absorb TAB.
 */
@SuppressWarnings("serial")
public class IMTable extends JTable
{
  public IMTable(TableModel dm)
  {
    super(dm);
  }

  /** 
   * Returns false always. Do not let the table handle
   * TAB.
   * @return false
   */
  public boolean isManagingFocus() 
  {
    return false;
  }
}
