/* Copyright (c) 2003, 2008, Oracle. All rights reserved.  */

import java.io.IOException;
import java.io.FileNotFoundException;

import java.awt.event.ActionEvent;
import java.awt.Component;

import javax.swing.JFileChooser;

import java.sql.SQLException;
import java.sql.ResultSet;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;

import oracle.ord.im.OrdMediaUtil;
import oracle.ord.im.OrdImage;
import oracle.ord.im.OrdAudio;
import oracle.ord.im.OrdVideo;
import oracle.ord.im.OrdDoc;

/** 
 * The IMLoadFile class loads a media stream
 * from a file to the database.
 */
public class IMLoadFile implements IMConstants
{
  OrdImage m_img = null;
  OrdImage m_imgThumb = null;
  OrdAudio m_aud = null;
  OrdVideo m_vid = null;
  OrdDoc   m_doc = null;
  Object   m_obj = null;

  int      m_iTypeIdentifier = DEFAULT_TYPE; 

  int      m_iProdId = -1;
  String   m_sColName = null;

  Component m_parent = null;

  IMFileChooser m_jFileChooser = null;

  /**
   * Constructs IMLoadFile to load product photo.
   * Initialize the media pointers of the photo and the thumbnail image.
   * @param parent the parent component
   * @param sLabel the dialog title
   * @param img    the media pointer to the photo object, can be null
   * @param imgThumb the media pointer to the thumbnail object, can be null
   * @param prodId   this product's id
   * @param sColName the column name in the online_media table
   */
  public IMLoadFile(Component parent, String sLabel, OrdImage img, 
      OrdImage imgThumb, int prodId, String sColName)
  {
    m_img = img;
    m_imgThumb = imgThumb;
    m_obj = m_img;
    m_iTypeIdentifier = IMG_TYPE;

    m_iProdId = prodId;
    m_sColName = sColName;

    initFileChooser(parent, sLabel);
  }

  /**
   * Constructs IMLoadFile to load product audio.
   * @param parent the parent component
   * @param sLabel the dialog title
   * @param aud    the media pointer to the audio object, can be null
   * @param prodId   this product's id
   * @param sColName the column name in the online_media table
   */
  public IMLoadFile(Component parent, String sLabel, OrdAudio aud, int prodId, 
      String sColName)
  {
    m_aud = aud;
    m_obj = m_aud;
    m_iTypeIdentifier = AUD_TYPE;

    m_iProdId = prodId;
    m_sColName = sColName;

    initFileChooser(parent, sLabel);
  }

  /**
   * Constructs IMLoadFile to load product video.
   * @param parent the parent component
   * @param sLabel the dialog title
   * @param vid    the media pointer to the video object, can be null
   * @param prodId   this product's id
   * @param sColName the column name in the online_media table
   */
  public IMLoadFile(Component parent, String sLabel, OrdVideo vid, int prodId, 
      String sColName)
  {
    m_vid = vid;
    m_obj = m_vid;
    m_iTypeIdentifier = VID_TYPE;

    m_iProdId = prodId;
    m_sColName = sColName;

    initFileChooser(parent, sLabel);
  }

  /**
   * Constructs IMLoadFile to load product testimonials.
   * @param parent the parent component
   * @param sLabel the dialog title
   * @param doc    the media pointer to the testimonials object, can be null
   * @param prodId   this product's id
   * @param sColName the column name in the online_media table
   */
  public IMLoadFile(Component parent, String sLabel, OrdDoc doc, int prodId, 
      String sColName)
  {
    m_doc = doc;
    m_obj = m_doc;
    m_iTypeIdentifier = DOC_TYPE;

    m_iProdId = prodId;
    m_sColName = sColName;

    initFileChooser(parent, sLabel);
  }

  /**
   * Displays the file chooser dialog.
   */
  private void initFileChooser(Component parent, String sLabel)
  {
    m_jFileChooser = new IMFileChooser();
    m_jFileChooser.setDialogTitle(sLabel);
    m_jFileChooser.getAccessibleContext().setAccessibleDescription(
        IMMessage.getString("LOAD_DIAG_DESC"));
    m_parent = parent;

    int returnVal = m_jFileChooser.showDialog(parent, "OK");
    if (returnVal == JFileChooser.APPROVE_OPTION)
    {
      try
      {
        loadNewMedia();
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
      catch (SQLException e)
      {
        new IMMessage(IMConstants.ERROR, "UPDATE_FAILED", e);
      }
    }
  }

  /**
   * This method takes care of all the preparation work, including
   * inserting a row, initializing the media object if necessary, for 
   * loading/updating a media object. <code>updateMedia</code>
   * does the real update work.
   */
  private void loadNewMedia() 
    throws SQLException, FileNotFoundException, SecurityException, IOException
  {
    boolean isInsertNeeded = false;
    String sQuery = null;
    OracleConnection conn = null;
    OracleResultSet rs = null;
    OraclePreparedStatement pstmt = null;

    try
    {
      conn = IMExample.getDBConnection();

      if (m_obj == null)
      {
        // First, checks whether this product exists in the 
        // pm.online_media table.
        // If exists, isInsertNeeded is set to false;
        // else, isInsertNeeded is set to true.
        sQuery = new String(
            "select product_id from pm.online_media where product_id = ?");
        pstmt = (OraclePreparedStatement) conn.prepareStatement(sQuery);
        pstmt.setInt(1, m_iProdId);
        rs = (OracleResultSet)pstmt.executeQuery();
        if (rs.next() == false)
          isInsertNeeded = true;
        else
          isInsertNeeded = false;
        rs.close();
        pstmt.close();

        if (isInsertNeeded)
        {
          // If this product is not in pm.online_media table, 
          // inserts a row in pm.online_media for this product
          // and initializes the media object at the same time.
          sQuery = new String(
              "insert into pm.online_media (product_id, product_photo, " + 
              "product_thumbnail, product_video, " + 
              "product_audio, product_text, product_testimonials) values (" + 
              "?, ORDSYS.ORDImage.init(), " +
              "ORDSYS.ORDImage.init(),  ORDSYS.ORDVideo.init(), " +
              "ORDSYS.ORDAudio.init(), null, ORDSYS.ORDDoc.init())");

          pstmt = (OraclePreparedStatement) conn.prepareCall(sQuery);
          pstmt.setInt(1, m_iProdId);
          pstmt.execute();
          pstmt.close();
        }
      }

      if (!isInsertNeeded)
      {
        // Creates a new media object.
        switch (m_iTypeIdentifier)
        {
          case IMG_TYPE:
            sQuery = new String(
                "update pm.online_media set " + m_sColName + 
                " = ORDSYS.ORDImage.init() where product_id = ?");
            break;
          case AUD_TYPE:
            sQuery = new String(
                "update pm.online_media set " + m_sColName +
                " = ORDSYS.ORDAudio.init() where product_id = ?");
            break;
          case VID_TYPE:
            sQuery = new String(
                "update pm.online_media set " + m_sColName + 
                " = ORDSYS.ORDVideo.init() where product_id = ?");
            break;
          case DOC_TYPE:
            sQuery = new String(
                "update pm.online_media set " + m_sColName +  
                " = ORDSYS.ORDDoc.init() where product_id = ?"); 
            break;
          default:
            new IMMessage(IMConstants.ERROR, "UNKNOWN_TYPE");
            break;
        }

        pstmt = (OraclePreparedStatement) conn.prepareCall(sQuery);
        pstmt.setInt(1, m_iProdId);
        pstmt.execute();
        pstmt.close();
      }

      // At this point, there is a row in the online_media table
      // for this product and the desired media object is initialized.
      // In the following, we update the media object pointer and 
      // acquire the right to modify it by selecting again from the
      // database.
      //
      sQuery = new String(
          "select " + m_sColName + 
          " from pm.online_media where product_id = ? for update");
      pstmt = (OraclePreparedStatement) conn.prepareStatement(sQuery);
      pstmt.setInt(1, m_iProdId);
      rs = (OracleResultSet)pstmt.executeQuery();
      if (rs.next() == false)
        throw new SQLException();
      else
      {
        switch (m_iTypeIdentifier)
        {
          case IMG_TYPE:
            m_img = (OrdImage)rs.getORAData(1, OrdImage.getORADataFactory());
            break;
          case AUD_TYPE:
            m_aud = (OrdAudio)rs.getORAData(1, OrdAudio.getORADataFactory());
            break;
          case VID_TYPE:
            m_vid = (OrdVideo)rs.getORAData(1, OrdVideo.getORADataFactory());
            break;
          case DOC_TYPE:
            m_doc = (OrdDoc)rs.getORAData(1, OrdDoc.getORADataFactory());
            break;
          default:
            new IMMessage(IMConstants.ERROR, "UNKNOWN_TYPE");
            break;
        }

        // Updates the media object.
        updateMedia();
      }

      rs.close();
      pstmt.close();
    }
    finally
    {
      IMUtil.cleanup(rs, pstmt);
    }
  }

  /**
   * Updates the media and also sets the media properties.
   */
  private void updateMedia()
    throws SQLException, FileNotFoundException, SecurityException, IOException
  {
    String sQuery = null;
    OracleConnection conn = null;
    byte[] ctx[] = new byte[1][64];
    OraclePreparedStatement pstmt = null;

    boolean isFormatSupported = false;

    try
    {
      conn = IMExample.getDBConnection();
      sQuery = new String(
          "update pm.online_media set " + m_sColName + 
          " = ? where product_id = ?");
      pstmt = (OraclePreparedStatement) conn.prepareCall(sQuery);
      pstmt.setInt(2, m_iProdId);

      switch (m_iTypeIdentifier)
      {
        case IMG_TYPE:
          m_img.loadDataFromFile(m_jFileChooser.getText());
          isFormatSupported = IMUtil.setProperties(m_img);
          pstmt.setORAData(1, m_img);
          break;
        case AUD_TYPE:
          m_aud.loadDataFromFile(m_jFileChooser.getText());
          isFormatSupported = IMUtil.setProperties(m_aud);
          pstmt.setORAData(1, m_aud);

          // Needs to update the media pointer for display,
          // because the input media pointer may be null.
          ((IMAudioPanel)m_parent).setMedia(m_aud);
          ((IMAudioPanel)m_parent).refreshPanel(isFormatSupported);
          break;
        case VID_TYPE:
          m_vid.loadDataFromFile(m_jFileChooser.getText());
          isFormatSupported = IMUtil.setProperties(m_vid);
          pstmt.setORAData(1, m_vid);

          ((IMVideoPanel)m_parent).setMedia(m_vid);
          ((IMVideoPanel)m_parent).refreshPanel(isFormatSupported);
          break;
        case DOC_TYPE:
          m_doc.loadDataFromFile(m_jFileChooser.getText());
          isFormatSupported = IMUtil.setProperties(m_doc);
          pstmt.setORAData(1, m_doc);

          ((IMDocPanel)m_parent).setMedia(m_doc);
          ((IMDocPanel)m_parent).refreshPanel(isFormatSupported);
          break;
        default:
          new IMMessage(IMConstants.ERROR, "UNKNOWN_TYPE");
          break;
      }

      pstmt.execute();
      pstmt.close();

      // Updates the thumbnail image.
      if (m_iTypeIdentifier == IMG_TYPE)
      {
        if (isFormatSupported)
          m_imgThumb = IMUtil.generateThumbnail(m_iProdId, m_img, m_imgThumb);

        ((IMImagePanel)m_parent).setMedia(m_img, m_imgThumb);
        ((IMImagePanel)m_parent).refreshPanel(isFormatSupported);
      }
    }
    finally
    {
      IMUtil.cleanup(pstmt);
    }
  }
}
