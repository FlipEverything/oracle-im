/* Copyright (c) 2003, 2008, Oracle. All rights reserved.  */

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusListener;
import java.awt.event.FocusEvent;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Color;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.BorderFactory;
import javax.swing.border.TitledBorder;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import java.sql.SQLException;

import java.io.IOException;
import java.io.File;
import java.io.FileOutputStream;

/**
 * IMMediaPanel takes care of common layout tasks
 * for all four media types.
 */
@SuppressWarnings("serial")
abstract class IMMediaPanel extends JPanel implements IMConstants
{
  Color m_colorFieldBg = null;

  int m_iProdId = -1;
  Container m_container = null;

  boolean m_hasMedia = false;

  JLabel m_jEmpty = null;

  JPanel m_jIconPane = new JPanel();
  JPanel m_jAttrPane = new JPanel();
  JPanel m_jControlPane = new JPanel();
  JTabbedPane m_jControlPaneHolder = new JTabbedPane();

  JCheckBox m_jCheckBoxLoad = new JCheckBox(" Load ");
  JCheckBox m_jCheckBoxSave = new JCheckBox(" Save ");
  JCheckBox m_jCheckBoxDelete = new JCheckBox(" Delete ");
  JCheckBox m_jCheckBoxPlay = new JCheckBox(" Play ");

  Box      m_jAttrPaneBox = null;
  JTable   m_jAttrTbl = null;
  String[] m_attrColNames = {"Attribute Name", "Attribute Value"};

  private static IMMIME s_mime = null;
  private static boolean s_isMIMEInited = false;

  /**
   * Constructs the media panel.
   * @param container    the parent container
   * @param iProdId      the product id
   * @param colorFieldBg the background color for text fields
   * @param sTitle       the title of this media panel
   */
  public IMMediaPanel(Container container, int iProdId, Color colorFieldBg, 
      String sTitle)
  {
    m_iProdId = iProdId;
    m_container = container;
    m_colorFieldBg = colorFieldBg;

    initMediaBlock(sTitle);
  }

  /**
   * Lays out common components for the media panel.
   * @param sTitle the title of this media panel
   */
  void initMediaBlock(String sTitle)
  {
    setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), sTitle, 
            TitledBorder.LEADING, TitledBorder.TOP),
          BorderFactory.createEmptyBorder(0, 20, 3, 20)
          ));

    int width = getWidth();
    int height = getHeight();

    setLayout(new BorderLayout());
    m_jIconPane.setPreferredSize(new Dimension(100, height));
    m_jAttrPane.setPreferredSize(new Dimension(width-200, height));
    m_jControlPaneHolder.setPreferredSize(new Dimension(90, height));

    m_jIconPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    m_jAttrPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

    m_jIconPane.setLayout(new BorderLayout());
    m_jAttrPane.setLayout(new BorderLayout());

    add(m_jIconPane, BorderLayout.WEST);
    add(m_jAttrPane, BorderLayout.CENTER);
    add(m_jControlPane, BorderLayout.EAST);

    // This has effect only when m_jIconPane is of type FocusedJPanel.
    // Currently m_jIconPane is not focused.
    m_jIconPane.addFocusListener(new FocusListener()
        {
          public void focusGained(FocusEvent e)
          {
            m_jIconPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                UIManager.getBorder("Table.focusCellHighlightBorder")
                ));
          }
          public void focusLost(FocusEvent e)
          {
            m_jIconPane.setBorder(
              BorderFactory.createEmptyBorder(10, 10, 10, 10)
              );
          }
        });
  }

  /**
   * Initializes the control panel layout on this media panel.
   */
  void initControlPane()
  {
    m_jControlPane.setLayout(new BoxLayout(m_jControlPane, BoxLayout.Y_AXIS));

    m_jCheckBoxLoad.setFont(new Font("Default", 0, 11));
    m_jCheckBoxSave.setFont(new Font("Default", 0, 11));
    m_jCheckBoxDelete.setFont(new Font("Default", 0, 11));

    m_jControlPane.add(m_jCheckBoxLoad);
    m_jControlPane.add(m_jCheckBoxSave);
    m_jControlPane.add(m_jCheckBoxDelete);

    m_jCheckBoxSave.addActionListener(new ActionListener()
        {
        public void actionPerformed(ActionEvent e)
        {
          if (m_jCheckBoxSave.isSelected())
          {
            saveToFile();
          }
        }
        });

    m_jCheckBoxDelete.addActionListener(new ActionListener()
        {
        public void actionPerformed(ActionEvent e)
        {
          if (m_jCheckBoxDelete.isSelected())
            deleteMedia();
        }
        });

    m_jCheckBoxPlay.addActionListener(new ActionListener()
        {
        public void actionPerformed(ActionEvent e)
        {
          if (m_jCheckBoxPlay.isSelected())
            play();
        }
        });
  }

  /**
   * Sets the border of the control panel.
   * @param sTitle the title of the control panel
   */
  void setControlPaneBorder(String sTitle)
  {
    m_jControlPane.setBorder(BorderFactory.createCompoundBorder(
          BorderFactory.createEmptyBorder(10, 10, 10, 0),
          BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(""),
            BorderFactory.createEmptyBorder(10, 0, 0, 0)
            )
          ));
  }

  /**
   * Saves a byte array stream.
   * @param data the input byte array of data
   * @param mimeType the mime type of media
   */
  void saveMedia(byte[] data, String mimeType)
  {
    if (data == null)
    {
      new IMMessage(IMConstants.WARNING, "NO_MEDIA");
    }
    else
    {
      IMMIME mime = getMIME();
      String sExt = s_mime.getExtName(mimeType);
      if (sExt != null)
        new IMSaveFile(this, "Save to:", data, "media" + sExt);
      else
        new IMSaveFile(this, "Save to:", data);
    }
  }

  /**
   * Initializes the mime configuration for plug-in players of the current 
   * operation system. Currenty, we only have plug-in configuration 
   * for Windows (mime.Windows) and Sun OS (mime.SunOS).
   */
  IMMIME getMIME()
  {
    if (!s_isMIMEInited)
    {
      s_mime = new IMMIME("mime." + System.getProperty("os.name"));
      s_isMIMEInited = true;
    }

    return s_mime;
  }

  /**
   * Plays the data stream associated with the mime type.
   * @param data the binary data stream
   * @param sMIMEType the data's mime type
   */
  void play(byte[] data, String sMIMEType) throws SQLException, IOException
  {
    FileOutputStream fileOut = null;

    IMMIME mime = getMIME();

    // Gets the player name for this mime type.
    String sPlayerName = mime.getPlayerName(sMIMEType);
    // Gets the file extension required for the player for this mime type.
    String sExtName    = mime.getExtName(sMIMEType);

    boolean isPlay = false;

    // Decides whether there is a default player.
    if (sPlayerName == null)
    {
      // Pops up a dialog to ask whether the user wants to specify a player.
      boolean isConfirmed = false;
      IMMessage msg = new IMMessage(IMConstants.SUGGEST, "NO_MIME_PLAYER");
      isConfirmed = msg.getConfirmOption();

      if (isConfirmed)
      {
        // The user wants to specify a player.
        // Gets the player name.
        IMFileChooser fc = new IMFileChooser();
        fc.setDialogTitle("Load Player");
        fc.getAccessibleContext().setAccessibleDescription(
            "Load a player to play/view the media");
        int returnVal = fc.showDialog(this, "OK");
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
          sPlayerName = fc.getText();
          isPlay = true;
        }
        else
        {
          isPlay = false;
        }
      }
      else 
      {
        // The user does not want to specify a player. 
        // Skips the play.
        isPlay = false;
      }
    }
    else
    {
      // Has the default player in the configuration file.
      // Still checks whether user wants to change it.
      isPlay = true;
      boolean isConfirmed = false;
      IMMessage msg = new IMMessage(IMConstants.SUGGEST, "DEFAULT_PLAYER",  sPlayerName, 
          "CHANGE_PLAYER");
      isConfirmed = msg.getConfirmOption();

      if (!isConfirmed)
      {
        // The user wants another player.
        // Gets the player name.
        IMFileChooser fc = new IMFileChooser();
        fc.setDialogTitle("Load Player");
        fc.getAccessibleContext().setAccessibleDescription(
            "Load a player to play/view the media");
        int returnVal = fc.showDialog(this, "OK");
        if (returnVal == JFileChooser.APPROVE_OPTION)
        {
          sPlayerName = fc.getText();
        }
      }
    }

    try
    {
      // If there is a player, play.
      if (isPlay)
      {
        // Creates a temporary file.
        File f = File.createTempFile("IMDemo", sExtName);
        f.deleteOnExit();

        String szURL = f.toString();

        // Saves the data stream to the temporary file.
        fileOut = new FileOutputStream(szURL);
        fileOut.write(data);
        fileOut.close();

        try
        {
          // Starts the player.
          Process proc = Runtime.getRuntime().exec(sPlayerName + " " + szURL);

          // For some players, stdout and stderr need to be absorbed
          // for its proper operation.
          IMStreamAbsorber errAbsorb = new IMStreamAbsorber(proc.getErrorStream());
          IMStreamAbsorber outAbsorb = new IMStreamAbsorber(proc.getInputStream());
          errAbsorb.start();
          outAbsorb.start();

          // Waits for the player to stop.
          int exit = proc.waitFor();
        }
        catch (IOException e)
        {
          new IMMessage(IMConstants.WARNING, "NO_PLAYER", e);
        }
        catch (Throwable t)
        {
          new IMMessage(IMConstants.WARNING, "PLAYER_ERR", t);
        }
      }
    }
    finally
    {
      IMUtil.cleanup(fileOut, null);
    }
  }

  /**
   * Saves the media to a file.
   * The action should be performed by
   * the derived class.
   */
  void saveToFile()
  {
    new IMMessage(IMConstants.ERROR, "UNKNOWN_TYPE");
  }

  /**
   * Deletes the media.
   * The action should be performed by
   * the derived class.
   */
  void deleteMedia()
  {
    new IMMessage(IMConstants.ERROR, "UNKNOWN_TYPE");
  }

  /**
   * Plays the media.
   * The action should be performed by
   * the derived class.
   */
  void play()
  {
    new IMMessage(IMConstants.ERROR, "UNKNOWN_TYPE");
  }

  /**
   * Lays out an empty attribute panel.
   */
  void layoutEmptyAttrPane(String sInfo)
  {
    m_jEmpty = new FocusedJLabel(sInfo);
    m_jEmpty.setLabelFor(m_jAttrPane);
    m_jEmpty.setForeground(UIManager.getColor("TextArea.foreground"));

    m_jEmpty.setHorizontalAlignment(SwingConstants.CENTER);
    m_jEmpty.setHorizontalTextPosition(SwingConstants.CENTER);
    m_jEmpty.setVerticalAlignment(SwingConstants.CENTER);
    m_jEmpty.setVerticalTextPosition(SwingConstants.CENTER);
    m_jAttrPane.add(m_jEmpty, BorderLayout.CENTER);
  }

  /**
   * Clears the attribute panel.
   */
  void emptyPanel(String emptyString)
  {
    if (m_jAttrTbl != null)
    {
      m_jAttrTbl = null;
      m_jAttrPane.remove(m_jAttrPaneBox);
      m_jAttrPane.setLayout(new BorderLayout());

      layoutEmptyAttrPane(emptyString);
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
    m_jControlPane.validate();

    validate();
  }

  /**
   * Enforces the right focus order.
   */
  Component getFirstFocusComponent()
  {
    return m_jAttrPane;
  }
}
