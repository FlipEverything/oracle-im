


import javax.swing.table.AbstractTableModel;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JCheckBox;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import java.util.ArrayList;

import java.math.BigDecimal;

import java.sql.Clob;
import java.sql.SQLException;

import java.io.IOException;

import oracle.ord.im.OrdMediaUtil;
import oracle.ord.im.OrdImage;
import oracle.ord.im.OrdAudio;
import oracle.ord.im.OrdVideo;
import oracle.ord.im.OrdDoc;

import oracle.jdbc.OracleResultSet;

import oracle.sql.STRUCT;

/**
 * The IMResultSetTableModel class controls the display of the product 
 * information table.
 */
class IMResultSetTableModel extends AbstractTableModel implements IMConstants
{
  private OracleResultSet m_resultSet = null;
  private ResultSetMetaData m_resultSetMetaData = null;
  private ArrayList m_cache = null;
  private String[] m_colNameCache = null;
  private String[] m_tblNameCache = null;
  private Class[] m_colClass = null;
  private int m_colCount = -1;

  public IMResultSetTableModel(ResultSet rs)
  {
    m_resultSet = (OracleResultSet)rs;
    m_cache = new ArrayList();

    try
    {
      m_resultSetMetaData = m_resultSet.getMetaData();
      //Adds a check media column.
      m_colCount = m_resultSetMetaData.getColumnCount() + 1; 
      m_colNameCache = new String[m_colCount];
      m_tblNameCache = new String[m_colCount];
      m_colClass = new Class[m_colCount];

      m_colNameCache[0] = CHECK;
      m_tblNameCache[0] = "";
      m_colClass[0] = Boolean.class;

      for (int j=1; j<m_colNameCache.length; j++)
      {
        m_colNameCache[j] = m_resultSetMetaData.getColumnName(j);
        m_colNameCache[j] = m_colNameCache[j].toUpperCase();

        m_tblNameCache[j] = m_resultSetMetaData.getTableName(j);
        m_tblNameCache[j] = m_tblNameCache[j].toUpperCase();

        String sColName = m_colNameCache[j];

        m_colClass[j] = Class.forName(m_resultSetMetaData.getColumnClassName(j));
      }

      while (m_resultSet.next())
      {
        Object[] row = new Object[m_colCount];

        row[0] = new Boolean(false);

        for (int j=1; j<row.length; j++)
        {
          row[j] = m_resultSet.getObject(j);
        }
        m_cache.add(row);
      }
    }
    catch (SQLException e)
    {
      new IMMessage(IMConstants.ERROR, "NO_TABLEINFO", e);
    }
    catch (ClassNotFoundException e)
    {
      new IMMessage(IMConstants.ERROR, "UNKNOWN_TYPE", e);
    }
  }

  /**
   * Gets the column name.
   * @param column the column number, starts from 0
   */
  public String getColumnName(int column)
  {
    if (column != 0)
      return m_colNameCache[column].substring(8);
    else
      return m_colNameCache[column];
  }

  public String getRealColumnName(int column)
  {
    return m_colNameCache[column];
  }

  /**
   * Gets the column class.
   * @param column the column number, starts from 0
   */
  public Class getColumnClass(int column)
  {
    return m_colClass[column];
  }

  public boolean isCellEditable(int row, int column)
  {
    String sColName = m_colNameCache[column];

    if (CHECK.equals(sColName))
    {
      return !((Boolean)getValueAt(row, 0)).booleanValue();
    }
    else if (ID.equals(sColName))
    {
      return false;
    }
    else
      return true;
  }

  public int getColumnCount()
  {
    return m_colCount;
  }

  public int getRowCount()
  {
    return m_cache.size();
  }

  public Object getValueAt(int row, int column)
  {
    if (row < m_cache.size())
    {
      return ((Object[])m_cache.get(row))[column];
    }
    else
      return null;
  }

  public void setValueAt(Object value, int row, int col) 
  {
    String sColName = m_colNameCache[col];

    if (CHECK.equals(sColName))
    {
      if (row < m_cache.size())
      {
        ((Object[])m_cache.get(row))[col] = value;
      }
      fireTableCellUpdated(row, col);
      if (((Boolean)value).booleanValue())
      {
        IMProductDialog dialog = 
          new IMProductDialog(((BigDecimal)getValueAt(row, 1)).intValue(), 
              (String)getValueAt(row,2), 
              (String)getValueAt(row,3), 
              this, row);
        dialog.show();
      }
    }
  }

  void unsetCheckMedia(int row)
  {
    ((Object[])m_cache.get(row))[0] = new Boolean(false);
    fireTableCellUpdated(row, 0);
  }

  int getID(int row)
  {
     return (((BigDecimal)getValueAt(row, 1)).intValue()); 
  }
}

