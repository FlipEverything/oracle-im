


import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JDialog;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
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
import java.awt.Rectangle;

import oracle.ord.im.OrdImage;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OraclePreparedStatement;
import oracle.jdbc.OracleResultSet;
import oracle.sql.BFILE;

/**
 * The IMImagePanel class displays the product photo and its attributes.
 */
@SuppressWarnings("serial")
class IMImage extends IMMediaPanel implements IMConstants
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
  IMImage(Container container, OrdImage img, OrdImage imgThumb, 
      int iProdId, Color colorFieldBg)
  {
    super(container, iProdId, colorFieldBg, PHOTO);
    m_img = img;
    m_imgThumb = imgThumb;
    m_loadContainer = this;
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


  public static JLabel showPicture(OrdImage image, Rectangle r, String text){
	  Icon profilePicture = null;
		
		if (image==null){
			profilePicture = new ImageIcon(IMFrame.class.getResource("icons/no_profile_picture.png"));
		} else {
			try {
				byte[] thumbnail = IMImage.getDataInByteArray(image);
				profilePicture = new ImageIcon(thumbnail);
			} catch (SQLException e) {
				new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
			} catch (IOException e) {
				new IMMessage(IMConstants.ERROR, "APP_ERR", e);
			}
		}
		
		JLabel userPictureLabel = new JLabel(null, profilePicture, JLabel.CENTER);
		userPictureLabel.setBounds(r);
		userPictureLabel.setToolTipText(text);
		
		return userPictureLabel;
  }

}
