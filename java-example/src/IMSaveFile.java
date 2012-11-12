/* Copyright (c) 2003, 2005, Oracle. All rights reserved.  */

import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;

import java.awt.event.ActionEvent;
import java.awt.Component;

import javax.swing.JFileChooser;

/** 
 * The IMSaveFile class saves an input media stream
 * to a target file.
 */
public class IMSaveFile implements IMConstants
{
  byte[] m_bContent = null;

  IMFileChooser m_jFileChooser = null;

  /**
   * Constructs IMSaveFile. First, pops up a IMFileChooser to let
   * the user select the target file.
   * @param parent the parent component of the save file dialog
   * @param sLabel the dialog title
   * @param bContent the byte array which is to be saved. This should
   *               never be null when passed in.
   */
  public IMSaveFile(Component parent, String sLabel, byte[] bContent)
  {
    this(parent, sLabel, bContent, null);
  }

  /**
   * Constructs IMSaveFile. First, pops up a IMFileChooser to let
   * the user select the target file.
   * @param parent the parent component of the save file dialog
   * @param sLabel the dialog title
   * @param bContent the byte array which is to be saved. This should
   *               never be null when passed in.
   */
  public IMSaveFile(Component parent, String sLabel, byte[] bContent, 
      String sDefaultFileName)
  {
    m_bContent = bContent;
    m_jFileChooser = new IMFileChooser();
    m_jFileChooser.setDialogTitle(sLabel);
    m_jFileChooser.getAccessibleContext().setAccessibleDescription(
        IMMessage.getString("SAVE_DIAG_DESC"));
    
    if (sDefaultFileName != null)
    {
      m_jFileChooser.setSelectedFile(
          new File(m_jFileChooser.getCurrentDirectory(), sDefaultFileName));
    }

    int returnVal = m_jFileChooser.showDialog(parent, "OK");

    if (returnVal == JFileChooser.APPROVE_OPTION)
    {
      save();
    }
  }

  private void save()
  {
    FileOutputStream fileOut = null;
    try
    {
      fileOut = new FileOutputStream(m_jFileChooser.getText());
      fileOut.write(m_bContent);
      fileOut.close();
    }
    catch (FileNotFoundException e)
    {
      new IMMessage(IMConstants.ERROR, "FILE_NOTFOUND", e);
    }
    catch (SecurityException e)
    {
      new IMMessage(IMConstants.ERROR, "FILE_NORIGHT", e);
    }
    catch (IOException e)
    {
      new IMMessage(IMConstants.ERROR, "IO_FAILED", e);
    }
    finally
    {
      try
      {
        IMUtil.cleanup(fileOut, null);
      }
      catch (IOException e)
      {
        new IMMessage(IMConstants.ERROR, "IO_FAILED", e);
      }
    }
  }
}
