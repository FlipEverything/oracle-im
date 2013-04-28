

/**
 * Oracle Multimedia example
 */


import java.io.IOException;
import java.net.ServerSocket;
import java.sql.SQLException;
import javax.swing.UIManager;



import oracle.jdbc.OracleConnection;

/**
 * The IMExample class creates the demo frame and maintains
 * the only connection to the database.
 */
public class IMMain implements IMConstants
{
  private static OracleConnection s_dbConn = null;
  private static ServerSocket SERVER_SOCKET;
  private IMFrame frame;
  
  /**
   * Constructs the main demo frame.
   */
  public IMMain()
  {
    try
    {
      

      setFrame(new IMFrame());
    } catch (Exception e)
    {
      new IMMessage(IMConstants.ERROR, "APP_ERR", e);
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
      new IMMessage(IMConstants.ERROR, "NOT_CONNECTED");
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
      s_dbConn.commit();
      s_dbConn.close();
    }

    s_dbConn = null;
  }

  public static void main(String[] args)
  {
	try {
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	   	  System.setProperty("apple.laf.useScreenMenuBar", "true");
	      System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Test");
		setSERVER_SOCKET(new ServerSocket(1339));
	} catch (IOException e1) {
	  	  new IMMessage(IMConstants.ERROR, "ALREADY_RUNNING", e1);
	  	  System.exit(0);
	} catch (Exception e2){
		new IMMessage(IMConstants.ERROR, "APP_ERR", e2);
	}
    new IMMain();
  }

public IMFrame getFrame() {
	return frame;
}

public void setFrame(IMFrame frame) {
	this.frame = frame;
}

public static ServerSocket getSERVER_SOCKET() {
	return SERVER_SOCKET;
}

public static void setSERVER_SOCKET(ServerSocket sERVER_SOCKET) {
	SERVER_SOCKET = sERVER_SOCKET;
}
}
