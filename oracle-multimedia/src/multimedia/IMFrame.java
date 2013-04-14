package multimedia;
/* Copyright (c) 2003, 2008, Oracle. All rights reserved.  */

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Container;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * The IMExampleFrame class displays the main frame.
 */
@SuppressWarnings("serial")
public class IMFrame extends JFrame implements IMConstants
{
  JMenuBar m_menuBar = new JMenuBar();
  JMenu m_menuConnection = new JMenu();
  JMenu m_menuUser = new JMenu();
  JMenu m_menuGallery = new JMenu();

  JMenuItem m_menuConnectionOpen = new JMenuItem();
  JMenuItem m_menuConnectionClose = new JMenuItem();
  JMenuItem m_menuConnectionImport = new JMenuItem();
  JMenuItem m_menuConnectionExport = new JMenuItem();
  JMenuItem m_menuConnectionExit = new JMenuItem();
  
  JMenuItem m_menuUserLogin = new JMenuItem();
  JMenuItem m_menuUserLogout = new JMenuItem();
  JMenuItem m_menuUserRegister = new JMenuItem();
  JMenuItem m_menuUserLostpassword = new JMenuItem();
  JMenuItem m_menuUserProfile = new JMenuItem();
  JMenuItem m_menuUserSettings = new JMenuItem();
  JMenuItem m_menuUserAlbums = new JMenuItem();
  JMenuItem m_menuUserUpload = new JMenuItem();
  
  JScrollPane m_jQueryResultPanel = new JScrollPane();
  JTable m_jResultSetTable = null;
  JLabel m_labelTable = new JLabel();

  Container m_jContentPane = null;
  private JLabel status;

  public IMFrame()
  {
    try
    { 
      m_jContentPane = getContentPane();

      setupDisplay();
      setupMenu();
      setupContentPane();

      setVisible(true);

      clickConnect();
    }
    catch(Exception e)
    {
      new IMMessage(IMConstants.ERROR, "APP_ERR", e);
    }
  }

  /**
   * Lays out the content pane.
   */
  private void setupContentPane()
  {
    // Lays out the panel.
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(new BorderLayout(10,10));
    contentPanel.setBackground(UIManager.getColor("Panel.background"));
    contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

    // Sets the label for the table.
    m_labelTable.setEnabled(false);
    m_labelTable.setLabelFor(m_jQueryResultPanel);

    // Lays out the scroll pane for the table.
    m_jQueryResultPanel.setPreferredSize(new Dimension(846, 340));
    m_jQueryResultPanel.setBorder(BorderFactory.createEtchedBorder());
    m_jQueryResultPanel.setAutoscrolls(true);
    m_jQueryResultPanel.setEnabled(false);

    // Puts the label in the middle horizontally.
    Box hbox = Box.createHorizontalBox();
    hbox.add(m_labelTable);
    hbox.add(Box.createHorizontalGlue());

    // Leaves some space around the label.
    Box vbox = Box.createVerticalBox();
    vbox.add(Box.createVerticalStrut(5));
    vbox.add(hbox);
    vbox.add(Box.createVerticalStrut(5));

    // Adds the label and the scroll pane.
    contentPanel.add(vbox, BorderLayout.NORTH);
    contentPanel.add(m_jQueryResultPanel, BorderLayout.CENTER);

    this.getContentPane().add(contentPanel, BorderLayout.CENTER);
    
    status = new JLabel(IMMessage.getString("CHOOSE_SERVER"));
    JPanel statusBar = new JPanel();
    statusBar.setLayout(new BorderLayout(10,10));
    statusBar.setBackground(UIManager.getColor("Panel.background"));
    statusBar.setBorder(BorderFactory.createEmptyBorder(3, 8, 3, 3));
    statusBar.add(status);
    
    this.getContentPane().add(statusBar, BorderLayout.SOUTH);
  }
  
  public void setLabel(String text){
	  m_labelTable.setText(text);
  }
  
  public void setStatusBar(String text){
	  status.setText(text);
	  SwingUtilities.updateComponentTreeUI(this);
  }

  /**
   * Simulates the clicking of the connection open menu item.
   */
  void clickConnect()
  {
    m_menuConnectionOpen.doClick();
  }

  /**
   * Shows the login dialog.
   */
  void connectActionPerformed() 
  {
    new IMLoginListDialog(this);
  }

  /**
   * Enables the table and some menu items after the connection established.
   */
  void enableDisplay()
  {
    m_labelTable.setEnabled(true);
    m_menuConnectionClose.setEnabled(true);
    m_jQueryResultPanel.setEnabled(true);
    m_jQueryResultPanel.setVisible(true);

    m_menuConnectionOpen.setEnabled(false);
    m_menuConnectionExit.setEnabled(false);
    
    m_menuUserLogin.setEnabled(true);
    m_menuUserRegister.setEnabled(true);
    m_menuUserLostpassword.setEnabled(true);
    
    m_menuConnectionExport.setEnabled(true);
    m_menuConnectionImport.setEnabled(true);
  }

  /**
   * Drops the database connection and sets the proper display.
   */
  void disconnectActionPerformed(ActionEvent ae)
  {
    try
    {
      m_menuConnectionOpen.setEnabled(true);
      m_menuConnectionExit.setEnabled(true);

      m_menuConnectionClose.setEnabled(false);
      m_jQueryResultPanel.setViewportView(null);
      m_jQueryResultPanel.setEnabled(false);
      m_labelTable.setEnabled(false);

      IMRunnableMain.closeDBConnection();
      setStatusBar(IMMessage.getString("CHOOSE_SERVER"));
    }
    catch (Exception e)
    {
      new IMMessage(IMConstants.ERROR, "CONNECT_CLOSE_FAIL", e);
    }
  }

  /**
   * Displays the retrieved table from the database.
   */
  void showDefaultTable()
  {
    IMQuery imQuery = new IMQuery();

    m_jResultSetTable = imQuery.execQuery();

    if (m_jResultSetTable != null)
    {
      m_jQueryResultPanel.setViewportView(m_jResultSetTable);
      m_jResultSetTable.requestFocus();
      // select the first row
      m_jResultSetTable.changeSelection(0, 0, false, false);
    }
    else
    {
      new IMMessage(IMConstants.ERROR, "NO_TABLE");
    }
  }
  
  void showLoginPanel(){
	  
  }
  
  /**
   * Initiates the frame.
   */
  private void setupDisplay()
  {
    m_jContentPane.setLayout(new BorderLayout());
    setSize(new Dimension(946, 601));
    setTitle(IMMessage.getString("MAIN_TITLE"));
    getAccessibleContext().setAccessibleDescription
      (IMMessage.getString("MAIN_DESC"));

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = getSize();
    if (frameSize.height > screenSize.height)
    {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width)
    {
      frameSize.width = screenSize.width;
    }
    setLocation((screenSize.width - frameSize.width) / 2, 
        (screenSize.height - frameSize.height) / 2);

    addWindowListener(new WindowAdapter()
        {
          public void windowClosing(WindowEvent e)
          {
          System.exit(0);
          }
        });
  }

  /**
   * Adds menu items.
   */
  private void setupMenu()
  {
    m_menuConnectionOpen.setText(IMMessage.getString("MAIN_MENU_CONNECT"));
    m_menuConnectionOpen.setMnemonic('K');
    m_menuConnectionOpen.setIcon(new ImageIcon(IMFrame.class.getResource("icons/connect.png")));
    m_menuConnectionOpen.setToolTipText(IMMessage.getString("MAIN_MENU_CONNECT_DESC"));
    m_menuConnectionOpen.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          connectActionPerformed();
        }
      });

    m_menuConnectionClose.setText(IMMessage.getString("MAIN_MENU_DISCONNECT"));
    m_menuConnectionClose.setEnabled(false);
    m_menuConnectionClose.setIcon(new ImageIcon(IMFrame.class.getResource("icons/disconnect.png")));
    m_menuConnectionClose.setMnemonic('E');
    m_menuConnectionClose.setToolTipText(IMMessage.getString("MAIN_MENU_DISCONNECT_DESC"));
    m_menuConnectionClose.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          disconnectActionPerformed(e);
        }
      });
    
    m_menuConnectionImport.setText(IMMessage.getString("MAIN_MENU_IMPORT"));
    m_menuConnectionImport.setMnemonic('I');
    m_menuConnectionImport.setEnabled(false);
    m_menuConnectionImport.setIcon(new ImageIcon(IMFrame.class.getResource("icons/import.png")));
    m_menuConnectionImport.setToolTipText(IMMessage.getString("MAIN_MENU_IMPORT_DESC"));
    m_menuConnectionImport.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          
        }
      });
    
    m_menuConnectionExport.setText(IMMessage.getString("MAIN_MENU_EXPORT"));
    m_menuConnectionExport.setMnemonic('E');
    m_menuConnectionExport.setEnabled(false);
    m_menuConnectionExport.setIcon(new ImageIcon(IMFrame.class.getResource("icons/export.png")));
    m_menuConnectionExport.setToolTipText(IMMessage.getString("MAIN_MENU_EXPORT_DESC"));
    m_menuConnectionExport.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          
        }
      });

    m_menuConnectionExit.setText(IMMessage.getString("MAIN_MENU_EXIT"));
    m_menuConnectionExit.setMnemonic('x');
    m_menuConnectionExit.setToolTipText(IMMessage.getString("MAIN_MENU_EXIT_DESC"));
    m_menuConnectionExit.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent ae)
        {
          exitActionPerformed(ae);
        }
      });
    
    m_menuUserLogin.setText(IMMessage.getString("MAIN_MENU_LOGIN"));
    m_menuUserLogin.setMnemonic('B');
    m_menuUserLogin.setEnabled(false);
    m_menuUserLogin.setIcon(new ImageIcon(IMFrame.class.getResource("icons/login.png")));
    m_menuUserLogin.setToolTipText(IMMessage.getString("MAIN_MENU_LOGIN_DESC"));
    m_menuUserLogin.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent ae)
        {
          
        }
      });
    
    m_menuUserLogout.setText(IMMessage.getString("MAIN_MENU_LOGOUT"));
    m_menuUserLogout.setMnemonic('K');
    m_menuUserLogout.setEnabled(false);
    m_menuUserLogout.setIcon(new ImageIcon(IMFrame.class.getResource("icons/logout.png")));
    m_menuUserLogout.setToolTipText(IMMessage.getString("MAIN_MENU_LOGOUT_DESC"));
    m_menuUserLogout.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent ae)
        {
          
        }
      });
    
    
    m_menuUserRegister.setText(IMMessage.getString("MAIN_MENU_SIGNUP"));
    m_menuUserRegister.setMnemonic('R');
    m_menuUserRegister.setEnabled(false);
    m_menuUserRegister.setIcon(new ImageIcon(IMFrame.class.getResource("icons/register.png")));
    m_menuUserRegister.setToolTipText(IMMessage.getString("MAIN_MENU_SIGNUP_DESC"));
    m_menuUserRegister.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent ae)
        {
          
        }
      });
    
    m_menuUserLostpassword.setText(IMMessage.getString("MAIN_MENU_LOSTPASS"));
    m_menuUserLostpassword.setMnemonic('E');
    m_menuUserLostpassword.setEnabled(false);
    m_menuUserLostpassword.setToolTipText(IMMessage.getString("MAIN_MENU_LOSTPASS_DESC"));
    m_menuUserLostpassword.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent ae)
        {
          
        }
      });
    
    m_menuUserProfile.setText(IMMessage.getString("MAIN_MENU_PROFILE"));
    m_menuUserProfile.setMnemonic('P');
    m_menuUserProfile.setEnabled(false);
    m_menuUserProfile.setIcon(new ImageIcon(IMFrame.class.getResource("icons/profile.png")));
    m_menuUserProfile.setToolTipText(IMMessage.getString("MAIN_MENU_PROFILE_DESC"));
    m_menuUserProfile.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent ae)
        {
          
        }
      });
    
    m_menuUserSettings.setText(IMMessage.getString("MAIN_MENU_SETTINGS"));
    m_menuUserSettings.setMnemonic('B');
    m_menuUserSettings.setEnabled(false);
    m_menuUserSettings.setIcon(new ImageIcon(IMFrame.class.getResource("icons/settings.png")));
    m_menuUserSettings.setToolTipText(IMMessage.getString("MAIN_MENU_SETTINGS_DESC"));
    m_menuUserSettings.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent ae)
        {
          
        }
      });  
    
    m_menuUserAlbums.setText(IMMessage.getString("MAIN_MENU_ALBUMS"));
    m_menuUserAlbums.setMnemonic('A');
    m_menuUserAlbums.setEnabled(false);
    m_menuUserAlbums.setIcon(new ImageIcon(IMFrame.class.getResource("icons/gallery.png")));
    m_menuUserAlbums.setToolTipText(IMMessage.getString("MAIN_MENU_ALBUMS_DESC"));
    m_menuUserAlbums.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent ae)
        {
          
        }
      }); 
    
    m_menuUserUpload.setText(IMMessage.getString("MAIN_MENU_UPLOAD"));
    m_menuUserUpload.setMnemonic('F');
    m_menuUserUpload.setEnabled(false);
    m_menuUserUpload.setIcon(new ImageIcon(IMFrame.class.getResource("icons/upload.png")));
    m_menuUserUpload.setToolTipText(IMMessage.getString("MAIN_MENU_UPLOAD_DESC"));
    m_menuUserUpload.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent ae)
        {
          
        }
      }); 

    m_menuConnection.setText(IMMessage.getString("MAIN_MENU_FILE"));
    m_menuConnection.setMnemonic('K');
    m_menuConnection.add(m_menuConnectionOpen);
    m_menuConnection.add(m_menuConnectionClose);
    m_menuConnection.addSeparator();
    m_menuConnection.add(m_menuConnectionImport);
    m_menuConnection.add(m_menuConnectionExport);
    m_menuConnection.addSeparator();
    m_menuConnection.add(m_menuConnectionExit);
    
    m_menuUser.setText(IMMessage.getString("MAIN_MENU_USER"));
    m_menuUser.setMnemonic('F');
    m_menuUser.add(m_menuUserLogin);
    m_menuUser.add(m_menuUserLogout);
    m_menuUser.add(m_menuUserRegister);
    m_menuUser.add(m_menuUserLostpassword);
    m_menuUser.addSeparator();
    m_menuUser.add(m_menuUserProfile);
    m_menuUser.add(m_menuUserSettings);
    m_menuUser.add(m_menuUserAlbums);
    m_menuUser.addSeparator();
    m_menuUser.add(m_menuUserUpload);
    
    
    m_menuGallery.setText(IMMessage.getString("MAIN_MENU_GALLERY"));
    m_menuUser.setMnemonic('G');
    
    m_menuBar.add(m_menuConnection);
    m_menuBar.add(m_menuUser);
    m_menuBar.add(m_menuGallery);

    this.setJMenuBar(m_menuBar);
  }

  /**
   * Exits the demo.
   */
  void exitActionPerformed(ActionEvent e)
  {
    disconnectActionPerformed(e);
    System.exit(0);
  }
}
