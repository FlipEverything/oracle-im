


import javax.swing.ImageIcon;
import javax.swing.JButton;
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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Container;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.concurrent.Callable;

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
    m_menuConnectionExit.setEnabled(true);
    m_menuConnectionExport.setEnabled(true);
    m_menuConnectionImport.setEnabled(true);
    
    setAllMenu(m_menuUser, false);
    m_menuUserLogin.setEnabled(true);
    m_menuUserRegister.setEnabled(true);
    m_menuUserLostpassword.setEnabled(true);
    
  }

  /**
   * Drops the database connection and sets the proper display.
   */
  void disconnectActionPerformed()
  {
    try
    {
      m_menuConnectionOpen.setEnabled(true);
      m_menuConnectionClose.setEnabled(false);
      m_menuConnectionExit.setEnabled(true);
      m_menuConnectionExport.setEnabled(false);
      m_menuConnectionImport.setEnabled(false);
      
      setAllMenu(m_menuUser, false);

      
      m_jQueryResultPanel.setViewportView(null);
      m_jQueryResultPanel.setEnabled(false);
      m_labelTable.setEnabled(false);

      IMRunnableMain.closeDBConnection();
      m_labelTable.setText(IMMessage.getString("CHOOSE_SERVER"));
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
  /*void showDefaultTable()
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
  }*/
  
  void showLoginPanel(){
	  JPanelLogin panel = new JPanelLogin(this);
	  panel.init();
	  m_jQueryResultPanel.setViewportView(panel);
  }
  
  void showSignupPanel(){
	  JPanelSignup panel = new JPanelSignup(this);
	  panel.init();
	  m_jQueryResultPanel.setViewportView(panel);
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
    m_menuConnectionOpen = createJMenuItem(m_menuConnectionOpen, "MAIN_MENU_CONNECT", 'K', true, "connect", "MAIN_MENU_CONNECT_DESC", new Callable<Void>() {
    	   public Void call() {
    		    connectActionPerformed();
   				return null;
   		   }
   		});
    
    m_menuConnectionClose = createJMenuItem(m_menuConnectionClose, "MAIN_MENU_DISCONNECT", 'E', false, "disconnect", "MAIN_MENU_DISCONNECT_DESC", new Callable<Void>() {
   	   public Void call() {
   		   		disconnectActionPerformed();
  				return null;
  		   }
  		});
    
    m_menuConnectionImport = createJMenuItem(m_menuConnectionImport, "MAIN_MENU_IMPORT", 'I', false, "import", "MAIN_MENU_IMPORT_DESC", new Callable<Void>() {
  	   public Void call() {
 			    
 				return null;
 		   }
 		});
    
    m_menuConnectionExport = createJMenuItem(m_menuConnectionExport, "MAIN_MENU_EXPORT", 'E', false, "export", "MAIN_MENU_EXPORT_DESC", new Callable<Void>() {
 	   public Void call() {
			    
				return null;
		   }
		});
    
    m_menuConnectionExit = createJMenuItem(m_menuConnectionExit, "MAIN_MENU_EXIT", 'x', true, null, "MAIN_MENU_EXIT_DESC", new Callable<Void>() {
    	   public Void call() {
			    exitActionPerformed();
				return null;
		   }
		});
    
    m_menuUserLogin = createJMenuItem(m_menuUserLogin, "MAIN_MENU_LOGIN", 'B', false, "login", "MAIN_MENU_LOGIN_DESC", new Callable<Void>() {
		   public Void call() {
	        	showLoginPanel();
				return null;
		   }
		});
    
    m_menuUserLogout = createJMenuItem(m_menuUserLogout, "MAIN_MENU_LOGOUT", 'K', false, "logout", "MAIN_MENU_LOGOUT_DESC", new Callable<Void>() {
		   public Void call() {
			    userLogout();
	        	showLoginPanel();
				return null;
		   }
		});

    m_menuUserRegister = createJMenuItem(m_menuUserRegister, "MAIN_MENU_SIGNUP", 'R', false, "register", "MAIN_MENU_SIGNUP_DESC", new Callable<Void>() {
		   public Void call() {
			    showSignupPanel();
				return null;
		   }
		});
    
    m_menuUserLostpassword = createJMenuItem(m_menuUserLostpassword, "MAIN_MENU_LOSTPASS", 'E', false, "profile", "MAIN_MENU_LOSTPASS_DESC", new Callable<Void>() {
		   public Void call() {

				return null;
		   }
		});
    
    m_menuUserProfile = createJMenuItem(m_menuUserProfile, "MAIN_MENU_PROFILE", 'P', false, "profile", "MAIN_MENU_PROFILE_DESC", new Callable<Void>() {
		   public Void call() {

				return null;
		   }
		});
    
    m_menuUserSettings = createJMenuItem(m_menuUserSettings, "MAIN_MENU_SETTINGS", 'B', false, "settings", "MAIN_MENU_SETTINGS_DESC", new Callable<Void>() {
		   public Void call() {

				return null;
		   }
		});

    
    m_menuUserAlbums = createJMenuItem(m_menuUserAlbums, "MAIN_MENU_ALBUMS", 'A', false, "gallery", "MAIN_MENU_ALBUMS_DESC", new Callable<Void>() {
		   public Void call() {

				return null;
		   }
		});
    
    m_menuUserUpload = createJMenuItem(m_menuUserUpload, "MAIN_MENU_UPLOAD", 'F', false, "upload", "MAIN_MENU_UPLOAD_DESC", new Callable<Void>() {
			   public Void call() {

					return null;
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
    m_menuGallery.setMnemonic('G');
    
    m_menuBar.add(m_menuConnection);
    m_menuBar.add(m_menuUser);
    m_menuBar.add(m_menuGallery);

    this.setJMenuBar(m_menuBar);
  }

  public void userLogout() {  
	setAllMenu(m_menuUser, false);
	
	m_menuUserRegister.setEnabled(true);
	m_menuUserLogin.setEnabled(true);
	m_menuUserLostpassword.setEnabled(true);
	
  }
  
  public void userLogin() {
	  setAllMenu(m_menuUser, true);
		
	  m_menuUserRegister.setEnabled(false);
	  m_menuUserLogin.setEnabled(false);
	  m_menuUserLostpassword.setEnabled(false);
  }
  
  public void setAllMenu(JMenu menu, boolean state){
	  Component[] comp = menu.getMenuComponents();
	  for (int i=0; i<comp.length; i++){
		  if (comp[i] instanceof JMenuItem){
			  comp[i].setEnabled(state);
		  }
	  }
  }
  
  public static JLabel createJLabel(JLabel label, Rectangle r, String text, int mnemonic, Component component){
	  label.setBounds(r);
	  label.setText(IMMessage.getString(text));
	  label.setHorizontalAlignment(JLabel.TRAILING);
	  label.setDisplayedMnemonic(mnemonic);
	  label.setLabelFor(component);
	  
	  return label;
  }
  
  public static JMenuItem createJMenuItem(JMenuItem menuItem, String text, int mnemonik, boolean enabled, String iconName, String description, final Callable<Void> func){
	  menuItem.setText(IMMessage.getString(text));
	  menuItem.setMnemonic(mnemonik);
	  menuItem.setEnabled(enabled);
	  if (iconName!=null)
		  menuItem.setIcon(new ImageIcon(IMFrame.class.getResource("icons/"+iconName+".png")));
	  menuItem.setToolTipText(IMMessage.getString(description));
	  menuItem.addActionListener(new ActionListener()
      {
	        public void actionPerformed(ActionEvent e)
	        {
	        	try {
	        		func.call();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        }
	      });
    
    return menuItem;
  }
  
  public static JButton createButton(JButton button, String text, int mnemonic, String iconName, String description, Rectangle r, final Callable<Void> func){
	  button.setBounds(r);
	  button.setText(IMMessage.getString(text));
	  button.setMnemonic(mnemonic);
	  button.setIcon(new ImageIcon(IMFrame.class.getResource("icons/"+iconName+".png")));
	  button.setToolTipText(IMMessage.getString(description));
	  button.addKeyListener(new java.awt.event.KeyAdapter()
	      {
	        public void keyPressed(KeyEvent e)
	        {
	          if (e.getKeyCode() == KeyEvent.VK_ENTER)
				try {
					func.call();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        }
	      });
	  button.addActionListener(new ActionListener()
	      {
	        public void actionPerformed(ActionEvent e)
	        {
	        	try {
	        		func.call();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
	        }
	      });
	  
	  return button;
  }
  
  
  

/**
   * Exits the demo.
   */
  void exitActionPerformed()
  {
    disconnectActionPerformed();
    System.exit(0);
  }
}
