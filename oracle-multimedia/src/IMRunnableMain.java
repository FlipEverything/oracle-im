/* Copyright (c) 2003, 2008, Oracle. All rights reserved.  */

/**
 * Oracle Multimedia example
 */

import java.sql.SQLException;

import oracle.jdbc.OracleConnection;

/**
 * The IMExample class creates the demo frame and maintains
 * the only connection to the database.
 */
public class IMRunnableMain implements IMConstants
{
  private static OracleConnection s_dbConn = null;
  
  /**
   * Constructs the main demo frame.
   */
  public IMRunnableMain()
  {
    try
    {
      IMFrame frame = new IMFrame();
    }
    catch (Exception e)
    {
      IMMessage msg = new IMMessage(IMConstants.ERROR, "APP_ERR", e);
    }
  }

  /**
   * Sets the database connection.
   * @param conn the established database connection. If null, 
   *             SQLException is thrown.
   *
   * @exception Exception If an exception occurs
   */
  protected static void setDBConnection(OracleConnection conn) throws Exception
  {
    if (s_dbConn == null)
    {
      if (conn == null)
        throw new SQLException();
      s_dbConn = conn;
    }
    else
    {
      new IMMessage(IMConstants.ERROR, "AlREADY_CONNECTED");
    }
  }

  /**
   * Sets whether wants to auto commit.
   * @param isAuto true if want auto commit, false otherwise
   *
   * @exception SQLException 
   */
  protected static void setAutoCommit(boolean isAuto) throws SQLException
  {
    s_dbConn.setAutoCommit(isAuto);
  }

  /**
   * Checks whether the connection to the database is set up.
   * @return true If the database connection is set up;
   *         false otherwise.
   */
  protected static boolean isDBConnected()
  {
    if (s_dbConn == null)
      return false;
    else
      return true;
  }

  /**
   * Gets the database connection.
   * @return the connection, may be null
   */
  protected static OracleConnection getDBConnection()
  {
    if (s_dbConn == null)
    {
      IMMessage msg = new IMMessage(IMConstants.ERROR, "NOT_CONNECTED");
      return null;
    }
    else
      return s_dbConn;
  }

  /**
   * Cancels changes.
   */
  protected static void rollback() throws SQLException
  {
    if (s_dbConn != null)
    {
      s_dbConn.rollback();
    }
  }

  /**
   * Commits changes.
   */
  protected static void commit() throws SQLException
  {
    if (s_dbConn != null)
    {
      s_dbConn.commit();
    }
  }

  /**
   * Closes the database connection after canceling all the
   * changes.
   */
  protected static void closeDBConnection() throws SQLException
  {
    if (s_dbConn != null)
    {
      s_dbConn.rollback();
      s_dbConn.close();
    }

    s_dbConn = null;
  }

  public static void main(String[] args)
  {
    new IMRunnableMain();
  }
}
