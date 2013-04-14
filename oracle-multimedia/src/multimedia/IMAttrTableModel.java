package multimedia;
/* Copyright (c) 2003, 2005, Oracle. All rights reserved.  */

import javax.swing.table.DefaultTableModel;

/** 
 * The IMAttrTableModel class subclasses DefaultTableModel 
 * to provide the table model for displaying media
 * attributes. It overwrites <code>isCellEditable</code> 
 * to make the cells uneditable.
 */
@SuppressWarnings("serial")
class IMAttrTableModel extends DefaultTableModel 
{
  public IMAttrTableModel(Object[][] data, Object[] columnNames)
  {
    super(data, columnNames);
  }

  /** 
   * Returns false always. Do not allow the cell
   * to be editable.
   * @param row  the row number in the table
   * @param column  the column number in the table
   * @return false
   */
  public boolean isCellEditable(int row, int column)
  {
    return false;
  }
}

