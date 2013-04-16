


import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

import oracle.ord.im.OrdImage;
import oracle.ord.im.OrdAudio;
import oracle.ord.im.OrdVideo;
import oracle.ord.im.OrdDoc;

import java.io.FileOutputStream;

/** 
 * The IMUtil class includes common utilities.
 */
class IMUtil implements IMConstants
{
  static int counter = 0;


  /**
   * This method is a wrapper for OrdImage.setProperties.
   * It is used to separate the exceptions
   * caused by unrecognizable formats.
   * @param img the input OrdImage object
   * @return true if OrdImage.setProperties finishes successfully;
   *         false otherwise
   */
  static boolean setProperties(OrdImage img)
  {
    try
    {
      img.setProperties();
      return true;
    }
    catch (SQLException e)
    {
      return false;
    }
  }

  /**
   * This method is a wrapper for OrdAudio.setProperties.
   * It is used to separate the exceptions
   * caused by unrecognizable formats.
   * @param aud the input OrdAudio object
   * @return true if OrdAudio.setProperties finishes successfully;
   *         false otherwise
   */
  static boolean setProperties(OrdAudio aud)
  {
    byte[] ctx[] = new byte[1][64];
    try
    {
      aud.setProperties(ctx);
      return true;
    }
    catch (SQLException e)
    {
      return false;
    }
  }

  /**
   * This method is a wrapper for OrdVideo.setProperties.
   * It is used to separate the exceptions
   * caused by unrecognizable formats.
   * @param vid the input OrdVideo object
   * @return true if OrdVideo.setProperties finishes successfully;
   *         false otherwise
   */
  static boolean setProperties(OrdVideo vid)
  {
    byte[] ctx[] = new byte[1][64];
    try
    {
      vid.setProperties(ctx);
      return true;
    }
    catch (SQLException e)
    {
      return false;
    }
  }

  /**
   * This method is a wrapper for OrdDoc.setProperties.
   * It is used to separate the exceptions
   * caused by unrecognizable formats.
   * @param doc the input OrdDoc object
   * @return true if OrdDoc.setProperties finishes successfully;
   *         false otherwise
   */
  static boolean setProperties(OrdDoc doc)
  {
    byte[] ctx[] = new byte[1][64];
    try
    {
      doc.setProperties(ctx, true);
      return true;
    }
    catch (SQLException e)
    {
      return false;
    }
  }


  /** 
   * Cleans up the ResultSet and the Statement. This is called from the
   * <code>finally</code> block.
   * @param rs the ResultSet needs to be closed
   * @param stmt the Statement needs to be closed
   */
  public static void cleanup(ResultSet rs, Statement stmt)
    throws SQLException
  {
    SQLException sqle = null;

    try 
    {
      if (rs != null) 
      {
        rs.close();
      }
    }
    catch (SQLException e) 
    {
      sqle = e;
    } 

    try 
    {
      if (stmt != null)
        stmt.close();
    }
    catch (SQLException e) 
    {
      if (sqle == null)
        sqle = e;
    } 
  }

  /** 
   * Cleans up the Statement. This is called from the
   * <code>finally</code> block.
   * @param stmt the Statement needs to be closed
   */
  public static void cleanup(Statement stmt)
    throws SQLException
  {
    if (stmt != null)
      stmt.close();
  }

  /** 
   * Cleans up an input stream and its reader. 
   * This is called from the <code>finally</code> block.
   * @param is     the input stream
   * @param reader the reader
   */
  public static void cleanup(InputStream is, Reader reader)
    throws IOException
  {
    IOException ioe = null;

    try 
    {
      if (reader != null) 
      {
        reader.close();
      }
    }
    catch (IOException e) 
    {
      ioe = e;
    } 

    try 
    {
      if (is != null)
        is.close();
    }
    catch (IOException e) 
    {
      if (ioe == null)
        ioe = e;
    } 
  }

  /** 
   * Cleans up an output stream and its writer. 
   * This is called from the <code>finally</code> block.
   * @param os     the output stream
   * @param writer the writer
   */
  public static void cleanup(OutputStream os, Writer writer)
    throws IOException
  {
    IOException ioe = null;

    try 
    {
      if (writer != null) 
      {
        writer.close();
      }
    }
    catch (IOException e) 
    {
      ioe = e;
    } 

    try 
    {
      if (os != null)
        os.close();
    }
    catch (IOException e) 
    {
      if (ioe == null)
        ioe = e;
    } 
  }
}
