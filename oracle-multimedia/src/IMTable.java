/* Copyright (c) 2003, 2008, Oracle. All rights reserved.  */

import javax.swing.JTable;
import javax.swing.table.TableModel;

/** 
 * The IMTable class subclasses JTable and overwrites 
 * isManagingFocus to avoid letting the table
 * absorb TAB.
 */
class IMTable extends JTable
{
  IMTable(TableModel dm)
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
