


import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * The IMStreamAbsorber class runs as a separate thread
 * to consume an inputstream. This is useful when a plug-in
 * application is loaded and it writes something out to, e.g., 
 * standard error, without consuming the application's output, 
 * the application may be unwilling to continue.
 */
class IMStreamAbsorber extends Thread implements IMConstants
{
  InputStream m_inputStream = null;

  /**
   * Constructs the stream absorber.
   * @param is the input stream, can be null
   */
  IMStreamAbsorber(InputStream is)
  {
    m_inputStream = is;
  }

  /**
   * Consumes the input stream.
   */
  public void run()
  {
    BufferedReader br = null;
    try
    {
      if (m_inputStream != null)
      {
        br = new BufferedReader(new InputStreamReader(m_inputStream, CHAR_ENCODING));
        String line = null;
        while ((line = br.readLine()) != null)
        {
          // Does nothing
        }
        br.close();
        m_inputStream.close();
      }
    }
    catch (IOException e)
    {
      new IMMessage(IMConstants.WARNING, "PLAYER_IO_FAILED", e);
    }
    finally
    {
      try
      {
        IMUtil.cleanup(m_inputStream, br);
      }
      catch (IOException e)
      {
        new IMMessage(IMConstants.WARNING, "PLAYER_ERR", e);
      }
    }
  }
}
