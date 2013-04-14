

import javax.swing.JOptionPane;
import java.util.ResourceBundle;

/**
 * IMMessage displays various messages for the demo.
 */
public class IMMessage implements IMConstants
{
  private static final String MSG_FILE =
    "IMMessageResource";
  private static final ResourceBundle msgs = 
    ResourceBundle.getBundle(MSG_FILE);

  boolean m_isConfirmed = false;

  /**
   * Constructs the message.
   * @param iMsgType the message type, defined in IMConstants
   * @param sMsg     the string key
   */
  IMMessage(int iMsgType, String sMsg)
  {
    this(iMsgType, sMsg, null);
  }
  
  /**
   * Constructs the message.
   * @param iMsgType the message type, defined in IMConstants
   * @param sMsg     the string key
   * @param t        the Throwable object
   */
  public IMMessage(int iMsgType, String sMsg, Throwable e)
  {
    switch (iMsgType)
    {
      case IMConstants.ERROR:
        IMJOptionPane.showMessageDialog(
            null, msgs.getString(sMsg)+( ((e!=null) && (e.getMessage()!=null)) ?  (((IMConstants.ENABLE_STACK_GUI) && (!e.getMessage().equals("-1"))) ?": "+e.getMessage()+" ("+e.getClass():"")+")" : "" ), ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
        break;
      case IMConstants.WARNING:
        IMJOptionPane.showMessageDialog(
            null, msgs.getString(sMsg), WARNING_TITLE, JOptionPane.WARNING_MESSAGE);
        break;
      case IMConstants.SUGGEST:
        int opt = IMJOptionPane.showConfirmDialog(
            null, msgs.getString(sMsg), CONFIRM_TITLE, JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION)
          m_isConfirmed= true;
        else if (opt == JOptionPane.NO_OPTION)
          m_isConfirmed = false;
        else
        {
          System.err.println("Should not be here");
        }
        break;
      default:
        break;
    }

    if (IMConstants.ENABLE_STACK_TRACE)
    {
      if (e != null)
      {
        e.printStackTrace();
      }
    }
  }

  /**
   * Constructs the message.
   * @param iMsgType  the message type, defined in IMConstants
   * @param sMsg1     the first string key
   * @param parameter the string sandwiched between two message strings
   * @param sMsg2     the second string key
   */
  IMMessage(int iMsgType, String sMsg1, String parameter, String sMsg2)
  {
    switch (iMsgType)
    {
      case IMConstants.SUGGEST:
        String sMsg = (msgs.getString(sMsg1) + parameter + msgs.getString(sMsg2));
        int opt = IMJOptionPane.showConfirmDialog(
            null, sMsg, CONFIRM_TITLE, JOptionPane.YES_NO_OPTION);
        if (opt == JOptionPane.YES_OPTION)
          m_isConfirmed= true;
        else if (opt == JOptionPane.NO_OPTION)
          m_isConfirmed = false;
        else
        {
          System.err.println("Should not be here");
        }
        break;
      default:
        break;
    }
  }

  /** 
   * Returns whether yes is clicked for the confirm dialog.
   * @return true if confirmed
   */
  boolean getConfirmOption()
  {
    return m_isConfirmed;
  }

  /**
   * Returns the message corresponding to the string key.
   * @param sMsg the string key
   * @return the message
   */
  public static String getString(String sMsg)
  {
    return msgs.getString(sMsg);
  }
}
