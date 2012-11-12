/* Copyright (c) 2003, 2008, Oracle. All rights reserved.  */

import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.Box;

import java.io.IOException;

import java.sql.SQLException;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import oracle.ord.im.OrdAudio;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.sql.BFILE;

/**
 * The IMAudioPanel class displays the product audio and its attributes.
 */
class IMAudioPanel extends IMMediaPanel implements IMConstants
{
  OrdAudio m_aud = null;

  JLabel m_jIcon = null;

  Container m_loadContainer = null;

  static final String s_sNotExist = "Audio does not exist!";
  static final String s_sNotSupported = "Unsupported format!";

  /**
   * Constructs the audio panel.
   * @param container    the parent container
   * @param aud          the audio object
   * @param iProdId      the product id
   * @param colorFieldBg the background color for text fields
   */
  IMAudioPanel(Container container, OrdAudio aud, int iProdId, Color colorFieldBg)
  {
    super(container, iProdId, colorFieldBg, AUDIO);
    m_aud = aud;
    m_loadContainer = this;
  }

  /**
   * Displays the audio panel.
   */
  void display() throws IOException, SQLException
  {
    try
    {
      addControlPane();

      // Sets the audio icon.
      m_jIcon = new JLabel(new ImageIcon(IMExampleFrame.class.getResource("icons/OrdAudio.gif")));
      m_jIcon.setLabelFor(m_jAttrPane);

      m_jIconPane.add(m_jIcon, BorderLayout.CENTER);

      if (notExist())
      {
        // Audio does not exist.
        m_hasMedia = false; 
        layoutEmpty(s_sNotExist);
      }
      else
      {
        m_hasMedia = true;

        // If audio exists, try to show the attributes.
        insertProperty();
      }
    }
    catch (IOException e)
    {
      new IMMessage(IMConstants.ERROR, "RETRIEVAL_FAILED", e);
    }
    catch (SQLException e)
    {
      new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
    }
  }

  /**
   * Lays out the control panel for load, save, delete and play.
   */
  void addControlPane()
  {
    initControlPane();

    m_jCheckBoxPlay.setFont(new Font("Default", 0, 11));
    m_jControlPane.add(m_jCheckBoxPlay);

    setControlPaneBorder("Audio");

    m_jCheckBoxLoad.addActionListener(new ActionListener()
        {
        public void actionPerformed(ActionEvent e)
        {
          if (m_jCheckBoxLoad.isSelected())
          {
            new IMLoadFile(m_loadContainer, "Load from:", 
              m_aud, m_iProdId, AUDIO);
          }
        }
        });

    m_jCheckBoxLoad.setToolTipText(IMMessage.getString("AUD_LOAD_DESC"));
    m_jCheckBoxDelete.setToolTipText(IMMessage.getString("AUD_DELETE_DESC"));
    m_jCheckBoxPlay.setToolTipText(IMMessage.getString("AUD_PLAY_DESC"));
    m_jCheckBoxSave.setToolTipText(IMMessage.getString("AUD_Save_DESC"));

    m_jCheckBoxLoad.getAccessibleContext().setAccessibleName(
        IMMessage.getString("AUD_LOAD_NAME"));
    m_jCheckBoxDelete.getAccessibleContext().setAccessibleName(
        IMMessage.getString("AUD_DELETE_NAME"));
    m_jCheckBoxPlay.getAccessibleContext().setAccessibleName(
        IMMessage.getString("AUD_PLAY_NAME"));
    m_jCheckBoxSave.getAccessibleContext().setAccessibleName(
        IMMessage.getString("AUD_Save_NAME"));
  }

  /**
   * Lays out the attribute panel when audio does not exist.
   */
  void layoutEmpty(String sInfo)
  {
    super.layoutEmptyAttrPane(sInfo);
  }

  /**
   * Shows the attribute table using Oracle Multimedia accessor methods 
   * to get audio properties.
   */
  boolean insertProperty() throws SQLException
  {
    boolean isFormatSupported = false;
    String sMimeType = m_aud.getMimeType();

    if (sMimeType == null)
      isFormatSupported = IMUtil.setProperties(m_aud);
    else
      isFormatSupported = true;

    if (!isFormatSupported)
    {
      layoutEmpty(s_sNotSupported);
    }
    else
    {
      Object[][] data = 
      {
        {"MIME Type",  m_aud.getMimeType()},
        {"Duration", new Integer(m_aud.getAudioDuration()).toString()},
        {"Content Length", new Integer(m_aud.getContentLength()).toString()}
      };

      IMAttrTableModel tm = new IMAttrTableModel(data, m_attrColNames);

      m_jAttrTbl = new IMTable(tm);
      JScrollPane jAttrScrollPane = new JScrollPane(m_jAttrTbl);
      jAttrScrollPane.setPreferredSize(new Dimension(300, 30));

      m_jAttrPaneBox = Box.createVerticalBox();
      m_jAttrPaneBox.add(Box.createVerticalGlue());
      m_jAttrPaneBox.add(jAttrScrollPane);
      m_jAttrPaneBox.add(Box.createVerticalGlue());
      m_jAttrPane.add(m_jAttrPaneBox);
    }

    return isFormatSupported;
  }

  /**
   * Saves the audio to a file.
   */
  void saveToFile()
  {
    try
    {
      if (!m_hasMedia)
      {
        new IMMessage(IMConstants.WARNING, "NO_MEDIA");
      }
      else
      {
        byte[] data = getDataInByteArray(m_aud);
        saveMedia(data, m_aud.getMimeType());
      }
    }
    catch (SQLException e)
    {
      new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
    }
    catch (IOException e)
    {
      new IMMessage(IMConstants.ERROR, "SAVE_FAIL", e);
    }
  }

  /**
   * Deletes the audio from the database by
   * setting it to empty using the ORDAudio.init method.
   */
  void deleteMedia()
  {
    OracleConnection conn = null;
    OraclePreparedStatement pstmt = null;
    OracleResultSet rs = null;

    try
    {
      if (!m_hasMedia)
      {
        new IMMessage(IMConstants.WARNING, "NO_MEDIA");
      }
      else
      {
        String sQuery = new String(
            "update pm.online_media set product_audio = ORDSYS.ORDAudio.init() " +
            "where product_id = ?");

        conn = IMExample.getDBConnection();
        pstmt = (OraclePreparedStatement) conn.prepareCall(sQuery);
        pstmt.setInt(1, m_iProdId);
        pstmt.execute();
        pstmt.close();

        sQuery = new String("select product_audio from pm.online_media " +
            "where product_id = ? for update");
        pstmt = (OraclePreparedStatement) conn.prepareStatement(sQuery);
        pstmt.setInt(1, m_iProdId);
        rs = (OracleResultSet) pstmt.executeQuery();
        if (rs.next() == false)
          throw new SQLException();
        else
          m_aud = (OrdAudio)rs.getORAData(1, OrdAudio.getORADataFactory());
        rs.close();
        pstmt.close();

        // Clears the display.
        emptyPanel(s_sNotExist);

        m_hasMedia = false;
      }
    }
    catch (SQLException e)
    {
      new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
    }
    finally
    {
      try
      {
        IMUtil.cleanup(rs, pstmt);
      }
      catch (SQLException e)
      {
        new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
      }
    }
  }

  /**
   * Plays the audio stream using a media player
   * which supports this mime type.
   */
  void play()
  {
    if (!m_hasMedia)
    {
      new IMMessage(IMConstants.WARNING, "NO_MEDIA");
    }
    else 
    {
      try
      {
        // Gets the media into a byte array and retrieves the mime type
        // so we can play the media
        byte[] data = getDataInByteArray(m_aud);
        String sMIMEType = m_aud.getMimeType();

        if (data == null)
        {
          new IMMessage(IMConstants.WARNING, "NO_MEDIA");
        }
        else 
        {
          // Actually playing 
          play(data, sMIMEType);
        }
      }
      catch (SQLException e)
      {
        new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
      }
      catch (IOException e)
      {
        new IMMessage(IMConstants.ERROR, "RETRIEVAL_FAILED", e);
      }
    }
  }

  /**
   * Sets the audio object.
   * @param aud the audio object
   */
  void setMedia(OrdAudio aud)
  {
    m_aud = aud;
  }

  /**
   * Checks whether the audio does exist.
   * @return true if BLOB is empty or not associated with a BFILE;
   *         false otherwise
   */
  boolean notExist() throws SQLException, IOException
  {
    if (m_aud == null)
      return true;
    else
    {
      if (m_aud.isLocal() && (m_aud.getDataInByteArray() == null))
        return true;
      else if (!m_aud.isLocal() && (":///".equals(m_aud.getSource())))
        return true;
      else
      {
        if (!m_aud.isLocal())
        {
          BFILE bfile = m_aud.getBFILE();
          if (!bfile.fileExists())
            return true;
          else 
            return false;
        }
        else
          return false;
      }
    }
  }

  /**
   * Retrieves the audio data in a byte array.
   * @param aud the audio object
   * @return the byte array contains the audio data
   */
  byte[] getDataInByteArray(OrdAudio aud) throws SQLException, IOException
  {
    if (!m_hasMedia)
      return null;
    else
    {
      if (!aud.isLocal())
      {
        byte[] ctx[] = new byte[1][4000];
        try
        {
          aud.importData(ctx);
        }
        catch (SQLException e)
        {
          new IMMessage(IMConstants.ERROR, "MEDIA_SOURCE_ERR", e);
          return null;
        }
      }
      return aud.getDataInByteArray();
    }
  }

  /**
   * Refreshes the display when updating the product audio. 
   */
  void refreshPanel(boolean isFormatSupported) throws SQLException, IOException
  {
    m_hasMedia = true;
    if (isFormatSupported)
    {
      if (m_jAttrTbl == null)
      {
        m_jAttrPane.remove(m_jEmpty);
        insertProperty();
      }
      else
      {
        m_jAttrTbl.setValueAt(m_aud.getMimeType(), 0, 1);
        m_jAttrTbl.setValueAt(new Integer(m_aud.getAudioDuration()).toString(), 1, 1);
        m_jAttrTbl.setValueAt(new Integer(m_aud.getContentLength()).toString(), 2, 1);
      }
    }
    else
    {
      if (m_jAttrTbl == null)
      {
        m_jEmpty.setText(s_sNotSupported);
      }
      else
      {
        m_jAttrTbl = null;
        m_jAttrPane.remove(m_jAttrPaneBox);
        m_jAttrPane.setLayout(new BorderLayout());

        layoutEmpty(s_sNotSupported);
      }
    }

    m_jCheckBoxLoad.setSelected(false);
    m_jCheckBoxSave.setSelected(false);
    m_jCheckBoxDelete.setSelected(false);
    m_jCheckBoxPlay.setSelected(false);

    m_jAttrPane.validate();
    m_jControlPane.validate();
    validate();
  }
}
