/* Copyright (c) 2003, 2008, Oracle. All rights reserved.  */

import javax.swing.table.TableCellRenderer;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JCheckBox;
import javax.swing.UIManager;

import java.awt.Component;

/** 
 * The BooleanRenderer class is used to render Boolean objects as JCheckBox in a 
 * JTable. To support accessibility, it also sets the AccessibleName and 
 * AccessibleDescription (by setting tooltip).
 */
@SuppressWarnings("serial")
class BooleanRenderer extends JCheckBox 
  implements TableCellRenderer, IMConstants
{
  protected IMResultSetTableModel m_tableModel = null;

  /** 
   * Constructs the renderer from the table model.
   * @param tm  the <code>IMResultSetTableModel</code>
   */
  public BooleanRenderer(IMResultSetTableModel tm)
  {
    super();
    m_tableModel = tm;
    setHorizontalAlignment(JLabel.CENTER);
  }

  /** 
   * This is the only interface we need to implement for TableCellRenderer.
   * Returns the checkbox rendered.
   * @param table  the <code>JTable</code>
   * @param value  the value to assign to the cell at
   *			<code>[row, column]</code>
   * @param isSelected true if cell is selected
   * @param isFocus true if cell has focus
   * @param row  the row of the cell to render
   * @param column the column of the cell to render
   * @return the checkbox, boolean cell renderer 
   */
  public Component getTableCellRendererComponent(JTable table, Object value, 
      boolean isSelected, boolean hasFocus, int row, int column)
  {
    if (isSelected) 
    {
      setForeground(table.getSelectionForeground());
      super.setBackground(table.getSelectionBackground());
    }
    else 
    {
      setForeground(table.getForeground());
      setBackground(table.getBackground());
    }

    if (hasFocus)
    {
      setBackground(UIManager.getColor("Label.foreground"));
      setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
    }

    setSelected((value != null && ((Boolean)value).booleanValue()));
    getAccessibleContext().setAccessibleName(IMMessage.getString("CHECK_MEDIA_NAME"));
    setToolTipText(IMMessage.getString("CHECK_MEDIA_DESC") + m_tableModel.getID(row));

    return this;
  }
}
