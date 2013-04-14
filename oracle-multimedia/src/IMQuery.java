


import java.sql.SQLException;
import java.sql.Statement;

import java.math.BigDecimal;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

/**
 * The IMExampleQuery class retrieves the oe.product_information table,
 * and creates the display of the table.
 */
class IMQuery implements IMConstants
{

  private OracleConnection m_dbConn = null;
  private static Statement m_statement = null;
  private static OracleResultSet m_resultSet = null;

  /**
   * Constructs the object.
   */
  public IMQuery()
  {
    m_dbConn = IMRunnableMain.getDBConnection();
  }

  /**
   * Displays the table.
   */
  /*JTable showTable() throws SQLException
  {
    // Sets the display model.
    IMResultSetTableModel tm = new IMResultSetTableModel(m_resultSet);
    JTable jt = new JTable(tm);

    jt.getAccessibleContext().setAccessibleName(
        IMMessage.getString("MAIN_TBL_NAME"));
    jt.setToolTipText(IMMessage.getString("MAIN_TBL_DESC"));

    // Modifies the default display parameters.
    jt.setRowHeight(jt.getRowHeight()+10);
    jt.setRowMargin(jt.getRowMargin()+10);

    // Sets the rendering. 
    jt.setDefaultRenderer(Object.class, new IMTableRenderer(tm));
    jt.setDefaultRenderer(BigDecimal.class, new IMTableRenderer(tm));
    jt.setDefaultRenderer(Boolean.class, new BooleanRenderer(tm));

    // Modifies the column width.
    int iColCount = tm.getColumnCount();
    for (int i=0; i<iColCount; i++)
    {
      String colname = tm.getRealColumnName(i);
      TableColumn tblColumn = jt.getColumnModel().getColumn(i);

      if (ID.equals(colname))
      {
        tblColumn.setPreferredWidth(tblColumn.getPreferredWidth()-150);
      }
      else if (CHECK.equals(colname))
      {
        tblColumn.setPreferredWidth(tblColumn.getPreferredWidth()-90);
      }
      else if (NAME.equals(colname))
      {
        tblColumn.setPreferredWidth(tblColumn.getPreferredWidth()-50);
      }
      else if (DESC.equals(colname))
      {
        tblColumn.setPreferredWidth(tblColumn.getPreferredWidth()+290);
      }
      else
      {
        new IMMessage(IMConstants.ERROR, "COLNAME_ERR");
      }
    }

    return jt;
  }*/

  /**
   * Retrieves the table from the database.
   */
  JTable execQuery(String s_query)
  {
    if (m_dbConn == null)
    {
      new IMMessage(IMConstants.ERROR, "CANNOT_CONNECT");
      return null;
    }

    try 
    {
      m_statement = m_dbConn.createStatement();
      m_resultSet = (OracleResultSet)m_statement.executeQuery(s_query);

      IMUtil.cleanup(m_resultSet, m_statement);

      return jt;
    }
    catch(SQLException e) 
    {
      new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
      try
      {
        IMUtil.cleanup(m_resultSet, m_statement);
      }
      catch (SQLException sqle)
      {
        new IMMessage(IMConstants.ERROR, "CONNECT_CLOSE_FAIL", sqle);
      }
      return null;
    }
  }
}
