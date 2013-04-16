


import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import javax.swing.JLabel;
import javax.swing.JDialog;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.Box;

import bean.IMAttrTableModel;
import bean.IMTable;

import java.io.IOException;

import java.sql.SQLException;

import java.awt.AWTEvent;
import java.awt.event.KeyEvent;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Rectangle;

import oracle.ord.im.OrdImage;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.sql.BFILE;

/**
 * The IMImagePanel class displays the product photo and its attributes.
 */
class IMImagePanel extends IMMediaPanel implements IMConstants
{
  OrdImage m_img = null;
  OrdImage m_imgThumb = null;

  JLabel m_jIcon = null;

  JPanel   m_jMetaPanel = null;

  Container m_loadContainer = null;

  static final String s_sNotExist = "Image does not exist!";
  static final String s_sNotSupported = "Unsupported format!";

  /**
   * Constructs the image panel.
   * @param container    the parent container
   * @param img          the image object
   * @param imgThumb     the thumbnail object
   * @param iProdId      the product id
   * @param colorFieldBg the background color for text fields
   */
  IMImagePanel(Container container, OrdImage img, OrdImage imgThumb, 
      int iProdId, Color colorFieldBg)
  {
    super(container, iProdId, colorFieldBg, PHOTO);
    m_img = img;
    m_imgThumb = imgThumb;
    m_loadContainer = this;
  }

  /**
   * Displays the image panel.
   */
  void display() throws IOException, SQLException
  {
    try
    {
      addControlPane();

      if (notExist(m_img))
      {
        // Image does not exist.
        m_hasMedia = false; 
        layoutEmpty(s_sNotExist);
      }
      else
      {
        m_hasMedia = true; 
        // If image exists, try to show the attributes.
        if (insertProperty())
        {
          // Shows the thumbnail image.
          // If the thumbnail image does not exist, it is generated.
          if (m_imgThumb != null)
          {
            String sFormat = m_imgThumb.getFormat();

            if (notExist(m_imgThumb) || 
                ( !("JFIF".equalsIgnoreCase(sFormat)) &&
                  !("GIFF".equalsIgnoreCase(sFormat))
                ))
            {
              m_imgThumb = IMUtil.generateThumbnail(m_iProdId, m_img, m_imgThumb);
            }

            byte[] thumbnail = getDataInByteArray(m_imgThumb);
            addThumbnail(thumbnail);
          }
          else
          {
            m_imgThumb = IMUtil.generateThumbnail(m_iProdId, m_img, m_imgThumb);
            byte[] thumbnail = getDataInByteArray(m_imgThumb);
            addThumbnail(thumbnail);
          }
        }
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

    setControlPaneBorder("Photo");

    m_jCheckBoxLoad.addActionListener(new ActionListener()
        {
        public void actionPerformed(ActionEvent e)
        {
          if (m_jCheckBoxLoad.isSelected())
          {
            new IMLoadFile(m_loadContainer, "Load from:", 
              m_img, m_imgThumb, m_iProdId, PHOTO);
          }
        }
        });

    m_jCheckBoxLoad.setToolTipText(IMMessage.getString("IMG_LOAD_DESC"));
    m_jCheckBoxDelete.setToolTipText(IMMessage.getString("IMG_DELETE_DESC"));
    m_jCheckBoxPlay.setToolTipText(IMMessage.getString("IMG_PLAY_DESC"));
    m_jCheckBoxSave.setToolTipText(IMMessage.getString("IMG_Save_DESC"));

    m_jCheckBoxLoad.getAccessibleContext().setAccessibleName(
        IMMessage.getString("IMG_LOAD_NAME"));
    m_jCheckBoxDelete.getAccessibleContext().setAccessibleName(
        IMMessage.getString("IMG_DELETE_NAME"));
    m_jCheckBoxPlay.getAccessibleContext().setAccessibleName(
        IMMessage.getString("IMG_PLAY_NAME"));
    m_jCheckBoxSave.getAccessibleContext().setAccessibleName(
        IMMessage.getString("IMG_Save_NAME"));
  }

  /**
   * Lays out the icon and attribute panel when image does not exist.
   */
  void layoutEmpty(String sInfo)
  {
    m_jIcon = new JLabel();
    m_jIcon.setLabelFor(m_jAttrPane);

    m_jIconPane.add(m_jIcon, BorderLayout.CENTER);

    super.layoutEmptyAttrPane(sInfo);
  }

  /**
   * Shows the attribute table using Oracle Multimedia accessor methods 
   * to get image properties.
   */
  boolean insertProperty() throws SQLException
  {
    boolean isFormatSupported = false;
    String sMimeType = m_img.getMimeType();

    if (sMimeType == null)
      isFormatSupported = IMUtil.setProperties(m_img);
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
        {"MIME Type",  m_img.getMimeType()},
        {"Height", new Integer(m_img.getHeight()).toString()},
        {"Width",  new Integer(m_img.getWidth()).toString()},
        {"Content Length", new Integer(m_img.getContentLength()).toString()}
      };

      IMAttrTableModel tm = new IMAttrTableModel(data, m_attrColNames);

      m_jAttrTbl = new IMTable(tm);
      JScrollPane jAttrScrollPane = new JScrollPane(m_jAttrTbl);
      jAttrScrollPane.setPreferredSize(new Dimension(300, 78));

      m_jMetaPanel = new JPanel();
      m_jMetaPanel.setLayout(new GridLayout(1, 2));
      m_jMetaPanel.setFont(new Font("Default", 0, 11));
      m_jMetaPanel.setBorder(BorderFactory.createTitledBorder(
            null, "Metadata", TitledBorder.LEADING,
            TitledBorder.TOP, new Font("Default", 0, 12)));

      JPanel   gmPanel = new JPanel();
      gmPanel.setLayout(new BorderLayout());

      JLabel   gmLabel = new JLabel("Read:");
      gmLabel.setFont(new Font("Default", 0, 11));

      String[] gmStrings = { "EXIF", "IPTC-IIM", "XMP"};
      JComboBox gmList = new JComboBox(gmStrings);
      gmList.setFont(new Font("Default", 0, 11));
      gmList.setSelectedIndex(0);

      gmLabel.setLabelFor(gmList);
      gmList.setToolTipText(IMMessage.getString("GMCOMBOBOX_DESC"));
      gmList.getAccessibleContext().setAccessibleName(
          IMMessage.getString("GMCOMBOBOX_NAME"));

      gmList.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              showMetadata(e);
            }
          });
      gmList.addKeyListener(new java.awt.event.KeyAdapter()
          {
            public void keyPressed(KeyEvent e)
            {
              showMetadata(e);
            }
          });
      
      gmPanel.add(gmLabel, BorderLayout.WEST);
      gmPanel.add(gmList, BorderLayout.CENTER);
      gmPanel.add(new JLabel("    "), BorderLayout.EAST);

      JPanel   pmPanel = new JPanel();
      pmPanel.setLayout(new BorderLayout());

      JLabel   pmLabel = new JLabel("Write:");
      pmLabel.setFont(new Font("Default", 0, 11));

      String[] pmStrings = { "XMP"};
      JComboBox pmList = new JComboBox(pmStrings);
      pmList.setFont(new Font("Default", 0, 11));
      pmList.setSelectedIndex(0);

      pmLabel.setLabelFor(pmList);
      pmList.setToolTipText(IMMessage.getString("PMCOMBOBOX_DESC"));
      pmList.getAccessibleContext().setAccessibleName(
          IMMessage.getString("PMCOMBOBOX_NAME"));

      pmList.addActionListener(new ActionListener()
          {
            public void actionPerformed(ActionEvent e)
            {
              writeMetadata(e);
            }
          });
      pmList.addKeyListener(new java.awt.event.KeyAdapter()
          {
            public void keyPressed(KeyEvent e)
            {
              writeMetadata(e);
            }
          });
      
      pmPanel.add(pmLabel, BorderLayout.WEST);
      pmPanel.add(pmList, BorderLayout.CENTER);
      pmPanel.add(new JLabel("    "), BorderLayout.EAST);

      m_jMetaPanel.add(gmPanel);
      m_jMetaPanel.add(pmPanel);

      m_jAttrPaneBox = Box.createVerticalBox();
      m_jAttrPaneBox.add(jAttrScrollPane);
      m_jAttrPaneBox.add(Box.createVerticalGlue());
      m_jAttrPaneBox.add(m_jMetaPanel);
      m_jAttrPane.add(m_jAttrPaneBox);
    }

    return isFormatSupported;
  }

  /**
   * Displays a window for showing metadata when
   * a mouse button is clicked, or the
   * ENTER or the SPACE key is pressed 
   * on a combobox metadata type.
   */
  void showMetadata(AWTEvent ae)
  {
    JComboBox cb = (JComboBox)ae.getSource();
    String metaType = (String)cb.getSelectedItem();

    if (ae instanceof KeyEvent 
        && ((KeyEvent)ae).getKeyCode() == KeyEvent.VK_ESCAPE)
    {
      cb.setPopupVisible(false);
      return;
    }

    if (ae instanceof KeyEvent 
        && ((KeyEvent)ae).getKeyCode() == KeyEvent.VK_SHIFT)
    {
      return;
    }

    if ( (ae instanceof ActionEvent &&
         (((ActionEvent)ae).getModifiers() == AWTEvent.MOUSE_EVENT_MASK)) ||
         (ae instanceof KeyEvent && 
          !(((KeyEvent)ae).getKeyCode() == KeyEvent.VK_UP ||
              ((KeyEvent)ae).getKeyCode() == KeyEvent.VK_DOWN))
       )
    {
      cb.setPopupVisible(false);
      IMGetMetadataDialog gmDialog =
        new IMGetMetadataDialog((JDialog)m_container, m_img, metaType);
      gmDialog.show();
    }
  }

  /**
   * Displays the writing metadata dialog when
   * a mouse button is clicked, or 
   * ENTER or SPACE key is pressed 
   * on a combobox metadata type.
   */
  void writeMetadata(AWTEvent ae)
  {
    JComboBox cb = (JComboBox)ae.getSource();
    String metaType = (String)cb.getSelectedItem();

    if (ae instanceof KeyEvent 
        && ((KeyEvent)ae).getKeyCode() == KeyEvent.VK_ESCAPE)
    {
      cb.setPopupVisible(false);
      return;
    }

    if (ae instanceof KeyEvent 
        && ((KeyEvent)ae).getKeyCode() == KeyEvent.VK_SHIFT)
    {
      return;
    }

    if ( (ae instanceof ActionEvent &&
         (((ActionEvent)ae).getModifiers() == AWTEvent.MOUSE_EVENT_MASK)) ||
         (ae instanceof KeyEvent && 
          !(((KeyEvent)ae).getKeyCode() == KeyEvent.VK_UP ||
              ((KeyEvent)ae).getKeyCode() == KeyEvent.VK_DOWN))
       )
    {
      cb.setPopupVisible(false);
      IMPutMetadataDialog pmDialog =
        new IMPutMetadataDialog((JDialog)m_container, m_img, metaType);
    }
  }

  /**
   * Shows the thumbnail image.
   */
  void addThumbnail(byte[] bThumbNail)
  {
    m_jIcon = new JLabel("Thumbnail", new ImageIcon(bThumbNail), 
        JLabel.CENTER);
    m_jIcon.setLabelFor(m_jAttrPane);
    m_jIcon.setVerticalTextPosition(JLabel.BOTTOM);
    m_jIcon.setHorizontalTextPosition(JLabel.CENTER);
    m_jIcon.setToolTipText("Thumbnail");

    m_jIconPane.add(m_jIcon, BorderLayout.CENTER);

  }

  /**
   * Changes the thumbnail image.
   */
  void changThumbnail(byte[] bThumbNail)
  {
    m_jIcon.setIcon(new ImageIcon(bThumbNail));
  }

  /**
   * Saves the image to a file.
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
        byte[] data = getDataInByteArray(m_img);
        saveMedia(data, m_img.getMimeType());
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
   * Deletes the image from the database by
   * setting it and its thumbnail image to empty
   * using the ORDImage.init method.
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
            "update pm.online_media set product_photo = ORDSYS.ORDImage.init(), " + 
            "product_thumbnail = ORDSYS.ORDImage.init() where product_id = ?");

        conn = IMMain.getDBConnection();
        pstmt = (OraclePreparedStatement) conn.prepareCall(sQuery);
        pstmt.setInt(1, m_iProdId);
        pstmt.execute();
        pstmt.close();

        sQuery = new String("select product_photo, product_thumbnail " +
            "from pm.online_media where product_id = ? for update");
        pstmt = (OraclePreparedStatement) conn.prepareStatement(sQuery);
        pstmt.setInt(1, m_iProdId);
        rs = (OracleResultSet)pstmt.executeQuery();
        if (rs.next() == false)
          throw new SQLException();
        else
        {
          m_img = (OrdImage)rs.getORAData(1, OrdImage.getORADataFactory());
          m_imgThumb = (OrdImage)rs.getORAData(2, OrdImage.getORADataFactory());
        }
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
   * Shows full image using a media player.
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
        // Gets media into a byte array and retrieves mime type
        // so we can play the media.
        byte[] data = getDataInByteArray(m_img);
        String sMIMEType = m_img.getMimeType();

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
   * Sets the photo and thumbnail object.
   * @param img the image object
   * @param imgThumb the thumbnail object
   */
  void setMedia(OrdImage img, OrdImage imgThumb)
  {
    m_img = img;
    m_imgThumb = imgThumb;
  }

  /**
   * Checks whether the image does exist.
   * @return true if BLOB is empty or not associated with a BFILE;
   *         false otherwise
   */
  static boolean notExist(OrdImage img) throws SQLException, IOException
  {
    if (img == null)
      return true;
    else
    {
      if (img.isLocal() && (img.getDataInByteArray() == null))
        return true;
      else if (!img.isLocal() && (":///".equals(img.getSource())))
        return true;
      else
      {
        if (!img.isLocal())
        {
          BFILE bfile = img.getBFILE();
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
   * Retrieves image data in a byte array.
   * @param img the image object
   * @return the byte array contains the image data
   */
  static byte[] getDataInByteArray(OrdImage img) throws SQLException, IOException
  {
    if (notExist(img))
      return null;
    else
    {
      if (!img.isLocal())
      {
        byte[] ctx[] = new byte[1][4000];
        try
        {
          img.importData(ctx);
        }
        catch (SQLException e)
        {
          new IMMessage(IMConstants.ERROR, "MEDIA_SOURCE_ERR", e);
          return null;
        }
      }
      return img.getDataInByteArray();
    }
  }

  /**
   * Refreshes the display when updating photo image.
   */
  void refreshPanel(boolean isFormatSupported) throws SQLException, IOException
  {
    m_hasMedia = true;
    if (isFormatSupported)
    {
      if (m_jAttrTbl == null)
      {
        m_jAttrPane.remove(m_jEmpty);
        m_jIconPane.remove(m_jIcon);

        byte[] thumbnail = getDataInByteArray(m_imgThumb);
        addThumbnail(thumbnail);

        insertProperty();
      }
      else
      {
        byte[] thumbnail = getDataInByteArray(m_imgThumb);
        changThumbnail(thumbnail);

        m_jAttrTbl.setValueAt(m_img.getMimeType(), 0, 1);
        m_jAttrTbl.setValueAt(new Integer(m_img.getHeight()).toString(), 1, 1);
        m_jAttrTbl.setValueAt(new Integer(m_img.getWidth()).toString(), 2, 1);
        m_jAttrTbl.setValueAt(new Integer(m_img.getContentLength()).toString(), 3, 1);
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
        m_jIconPane.remove(m_jIcon);
        layoutEmpty(s_sNotSupported);
      }
    }

    m_jCheckBoxLoad.setSelected(false);
    m_jCheckBoxSave.setSelected(false);
    m_jCheckBoxDelete.setSelected(false);
    m_jCheckBoxPlay.setSelected(false);

    m_jAttrPane.validate();
    m_jIconPane.validate();
    m_jControlPane.validate();
    validate();
  }

  /**
   * Clears the icon and attribute panel.
   */
  void emptyPanel(String emptyString)
  {
    if (m_jAttrTbl != null)
    {
      m_jAttrTbl = null;
      m_jAttrPane.remove(m_jAttrPaneBox);
      m_jAttrPane.setLayout(new BorderLayout());

      m_jIconPane.remove(m_jIcon);

      layoutEmpty(emptyString);
    }
    else
    {
      m_jEmpty.setText(emptyString);
    }

    m_jCheckBoxLoad.setSelected(false);
    m_jCheckBoxSave.setSelected(false);
    m_jCheckBoxDelete.setSelected(false);
    m_jCheckBoxPlay.setSelected(false);

    m_jAttrPane.validate();
    m_jIconPane.validate();
    m_jControlPane.validate();

    validate();
  }

}
