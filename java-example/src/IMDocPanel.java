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

import oracle.ord.im.OrdDoc;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.sql.BFILE;

/**
 * The IMDocPanel class displays the product testimonials and its attributes.
 */
class IMDocPanel extends IMMediaPanel implements IMConstants
{
  OrdDoc m_doc = null;

  JLabel m_jIcon = null;

  Container m_loadContainer = null;

  static final String s_sNotExist = "Testimonials does not exist!";
  static final String s_sNotSupported = "Unsupported format!";

  /**
   * Constructs the doc panel.
   * @param container    the parent container
   * @param doc          the testimonials object
   * @param iProdId      the product id
   * @param colorFieldBg the background color for text fields
   */
  IMDocPanel(Container container, OrdDoc doc, int iProdId, Color colorFieldBg)
  {
    super(container, iProdId, colorFieldBg, DOC);
    m_doc = doc;
    m_loadContainer = this;
  }

  /**
   * Displays the doc panel
   */
  void display() throws IOException, SQLException
  {
    addControlPane();

    // Sets the icon.
    m_jIcon = new JLabel(new ImageIcon(
          IMExampleFrame.class.getResource("icons/OrdDoc.gif")
          ));
    m_jIcon.setLabelFor(m_jAttrPane);
    m_jIconPane.add(m_jIcon, BorderLayout.CENTER);

    if (notExist())
    {
      // Doc does not exist.
      m_hasMedia = false; 
      layoutEmpty(s_sNotExist);
    }
    else
    {
      // If doc exists, show the attribute table.
      m_hasMedia = true; 
      insertProperty();
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

    setControlPaneBorder("Testimonials");

    m_jCheckBoxLoad.addActionListener(new ActionListener()
        {
        public void actionPerformed(ActionEvent e)
        {
          if (m_jCheckBoxLoad.isSelected())
          {
            new IMLoadFile(m_loadContainer, "Load from:", m_doc, m_iProdId, DOC);
          }
        }
        });

    m_jCheckBoxLoad.setToolTipText(IMMessage.getString("DOC_LOAD_DESC"));
    m_jCheckBoxDelete.setToolTipText(IMMessage.getString("DOC_DELETE_DESC"));
    m_jCheckBoxPlay.setToolTipText(IMMessage.getString("DOC_PLAY_DESC"));
    m_jCheckBoxSave.setToolTipText(IMMessage.getString("DOC_Save_DESC"));

    m_jCheckBoxLoad.getAccessibleContext().setAccessibleName(
        IMMessage.getString("DOC_LOAD_NAME"));
    m_jCheckBoxDelete.getAccessibleContext().setAccessibleName(
        IMMessage.getString("DOC_DELETE_NAME"));
    m_jCheckBoxPlay.getAccessibleContext().setAccessibleName(
        IMMessage.getString("DOC_PLAY_NAME"));
    m_jCheckBoxSave.getAccessibleContext().setAccessibleName(
        IMMessage.getString("DOC_Save_NAME"));
  }

  /**
   * Lays out the attribute panel when doc does not exist.
   */
  void layoutEmpty(String sInfo)
  {
    super.layoutEmptyAttrPane(sInfo);
  }

  /**
   * Shows the attribute table using Oracle Multimedia accessor methods 
   * to get testimonial properties.
   */
  boolean insertProperty() throws SQLException
  {
    boolean isFormatSupported = false;
    String sMimeType = m_doc.getMimeType();

    if (sMimeType == null)
      isFormatSupported = IMUtil.setProperties(m_doc);
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
        {"MIME Type",  m_doc.getMimeType()},
        {"Content Length", new Integer(m_doc.getContentLength()).toString()}
      };

      IMAttrTableModel tm = new IMAttrTableModel(data, m_attrColNames);

      m_jAttrTbl = new IMTable(tm);
      JScrollPane jAttrScrollPane = new JScrollPane(m_jAttrTbl);
      jAttrScrollPane.setPreferredSize(new Dimension(300, 7));

      m_jAttrPaneBox = Box.createVerticalBox();
      m_jAttrPaneBox.add(Box.createVerticalGlue());
      m_jAttrPaneBox.add(jAttrScrollPane);
      m_jAttrPaneBox.add(Box.createVerticalGlue());
      m_jAttrPane.add(m_jAttrPaneBox);
    }

    return isFormatSupported;
  }

  /**
   * Saves to a file.
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
        byte[] data = getDataInByteArray(m_doc);
        saveMedia(data, m_doc.getMimeType());
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
   * Deletes the doc from the database by
   * setting it to empty using the ORDDoc.init method.
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
            "update pm.online_media set product_testimonials = ORDSYS.ORDDoc.init() " +
            "where product_id = ?");

        conn = IMExample.getDBConnection();
        pstmt = (OraclePreparedStatement) conn.prepareCall(sQuery);
        pstmt.setInt(1, m_iProdId);
        pstmt.execute();
        pstmt.close();

        sQuery = new String("select product_testimonials from pm.online_media " + 
            "where product_id = ? for update");
        pstmt = (OraclePreparedStatement) conn.prepareStatement(sQuery);
        pstmt.setInt(1, m_iProdId);
        rs = (OracleResultSet)pstmt.executeQuery();
        if (rs.next() == false)
          throw new SQLException();
        else
          m_doc = (OrdDoc)rs.getORAData(1, OrdDoc.getORADataFactory());
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
   * Plays the media using a media player
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
        // Gets the media into a byte array and retrieve the mime type
        // so we can play the media
        byte[] data = getDataInByteArray(m_doc);
        String sMIMEType = m_doc.getMimeType();

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
   * Sets the testimonials object.
   * @param doc the testimonials object
   */
  void setMedia(OrdDoc doc)
  {
    m_doc = doc;
  }

  /**
   * Checks whether the doc does exist.
   * @return true if BLOB is empty or not associated with a BFILE;
   *         false otherwise
   */
  boolean notExist() throws SQLException, IOException
  {
    if (m_doc == null)
      return true;
    else
    {
      if (m_doc.isLocal() && (m_doc.getDataInByteArray() == null))
        return true;
      else if (!m_doc.isLocal() && (":///".equals(m_doc.getSource())))
        return true;
      else
      {
        if (!m_doc.isLocal())
        {
          BFILE bfile = m_doc.getBFILE();
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
   * Retrieves the doc data in a byte array.
   * @param doc the doc object
   * @return the byte array contains the doc data
   */
  byte[] getDataInByteArray(OrdDoc doc) throws SQLException, IOException
  {
    if (!m_hasMedia)
      return null;
    else
    {
      if (!doc.isLocal())
      {
        byte[] ctx[] = new byte[1][4000];
        try
        {
          doc.importData(ctx, false);
        }
        catch (SQLException e)
        {
          new IMMessage(IMConstants.ERROR, "MEDIA_SOURCE_ERR", e);
          return null;
        }
      }
      return doc.getDataInByteArray();
    }
  }

  /**
   * Refreshes the display when updating the media.
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
        m_jAttrTbl.setValueAt(m_doc.getMimeType(), 0, 1);
        m_jAttrTbl.setValueAt(new Integer(m_doc.getContentLength()).toString(), 1, 1);
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
