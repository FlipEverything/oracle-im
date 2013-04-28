


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * The IMMIME class loads and stores the mapping between 
 * plugin players and mime types. 
 */
class IMMIME implements IMConstants
{
  static String s_sMIMEFileName = null;

  // The hash tables hold the mappings.
  private static Hashtable<String, String> m_hashTable;
  private static Hashtable<String, String> m_hashTableExt;

  /**
   * Constructs a hashtable to store the mime types
   * and plugin players mapping.
   * @param sMIMEFileName the plugin configuration
   *        file name
   */
  IMMIME(String sMIMEFileName)
  {
    if (sMIMEFileName.startsWith("mime.Windows"))
      sMIMEFileName = "mime.Windows";

    try
    {
      m_hashTable = new Hashtable<String, String>();
      m_hashTableExt = new Hashtable<String, String>();

      BufferedReader in = 
        new BufferedReader(new InputStreamReader(
              IMMIME.class.getResourceAsStream(sMIMEFileName), CHAR_ENCODING));

      while (in.ready()) 
      {
        String szTemp = in.readLine();

        // Checks for the comment line.
        if((szTemp.trim().length() == 0) ||
            (szTemp.charAt(0) == '#'))
          continue;

        StringTokenizer st = new StringTokenizer(szTemp);

        String szMIMEType   = st.nextToken();
        String szPlayer     = null;
        String szExt        = null;

        // Gets the player if it exists.
        if (st.hasMoreElements()) 
        {
          szPlayer = st.nextToken();

          // Gets the filename extension if it exists.
          if (st.hasMoreElements()) 
            szExt = st.nextToken();
        }

        // Stores them into the hash table.
        if (szPlayer != null)
          m_hashTable.put(szMIMEType, szPlayer);
        if (szExt != null)
          m_hashTableExt.put(szMIMEType, "."+szExt);
      }

      in.close();
    }
    catch (Exception e)
    {
      new IMMessage(IMConstants.WARNING, "NO_MIME_CONF", e);
    }
  }

  /**
   * Retrieves the player for the mime type.
   * @param szMIMEType the mime type to be played
   * 
   * @return the player name
   */
  public String getPlayerName(String szMIMEType)
  { 
    if (szMIMEType == null)
      return null;

    Object player = m_hashTable.get(szMIMEType);
    if (player == null) 
      return null;
    return (String)player;
  }

  /**
   * Retrieves the file extension.
   * @param szMIMEType the mime type 
   * 
   * @return the file extension 
   */
  public String getExtName(String szMIMEType)
  { 
    if (szMIMEType == null)
      return null;

    Object extension = m_hashTableExt.get(szMIMEType);

    if (extension == null) 
      return null;
    return (String)extension;
  }
}
