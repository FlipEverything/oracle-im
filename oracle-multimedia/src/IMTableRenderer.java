


import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import java.awt.Component;

/**
 * The IMTableRenderer class renders the product_id, product_name, 
 * product_description columns. We need to render these ourselves
 * because we need to add accessibility information and change
 * the display also.
 */
class IMTableRenderer extends DefaultTableCellRenderer implements IMConstants
{
  protected IMResultSetTableModel m_tableModel = null;

  public IMTableRenderer(IMResultSetTableModel tm)
  {
    m_tableModel = tm;
  }

  /**
   * Overwrites getTableCellRendererComponent in the DefaultTableCellRenderer
   * to give required display.
   */
  public Component getTableCellRendererComponent(JTable table, Object value, 
      boolean isSelected, boolean hasFocus, int row, int column)
  {
    if (m_tableModel != null)
    {
      String sColName = m_tableModel.getRealColumnName(column);
      sColName = sColName.toUpperCase();
      
      // If product_id column, let it be center-aligned
      // and use the system label color. Also, set tool tip
      // accessibility context.
      if (ID.equals(sColName))
      {
        JLabel label = (JLabel)super.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, column);

        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(UIManager.getFont("Label.font"));
        label.setForeground(UIManager.getColor("Label.foreground"));

        String s = new String(IMMessage.getString("TBL_ID_DESC"));
        if (value != null)
        {
          label.setToolTipText(s + value.toString());
          label.getAccessibleContext().setAccessibleDescription
            (s + value.toString());
          label.getAccessibleContext().setAccessibleName
            (s + value.toString());
        }

        return label;
      }
      else
      {
        // Needs to set tool tip and accessibility context for 
        // product name and description. The tool tip needs
        // to be displayed in a multi-line fashion for 
        // readability.
        JLabel label = (JLabel)super.getTableCellRendererComponent(
            table, value, isSelected, hasFocus, row, column);

        String s = null;
        int id = m_tableModel.getID(row);
        if (column == 2)
          s = new String(IMMessage.getString("TBL_NAME_DESC") + id + " ");
        if (column == 3)
          s = new String(IMMessage.getString("TBL_DESC_DESC") + id + " ");

        if (value != null)
        {
          // Creates a 40-character per line multi-line tool tip.
          label.setToolTipText(IMUIUtil.createMultiLineToolTip(s+value.toString(), 40));
          label.getAccessibleContext().setAccessibleDescription(s+value.toString());
          label.getAccessibleContext().setAccessibleName(s);
        }
        return label;
      }
    }
    else
    {
      return super.getTableCellRendererComponent(
          table, value, isSelected, hasFocus, row, column);
    }
  }
}
