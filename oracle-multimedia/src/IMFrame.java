


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
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
import javax.swing.border.TitledBorder;

import bean.Category;
import bean.City;
import bean.Country;
import bean.Keyword;
import bean.Picture;
import bean.Region;
import bean.User;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Container;

import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;

/**
 * The IMExampleFrame class displays the main frame.
 */
@SuppressWarnings("serial")
public class IMFrame extends JFrame implements IMConstants
{
  private int m_nWidth = 946;
  private int m_nHeight = 601;
	
  JMenuBar m_menuBar = new JMenuBar();
  JMenu m_menuConnection = new JMenu();
  JMenu m_menuUser = new JMenu();
  JMenu m_menuGallery = new JMenu();
  JMenu m_menuSystem = new JMenu();

  JMenuItem m_menuConnectionOpen = new JMenuItem();
  JMenuItem m_menuConnectionClose = new JMenuItem();
  JMenuItem m_menuConnectionExit = new JMenuItem();
  
  JMenuItem m_menuUserLogin = new JMenuItem();
  JMenuItem m_menuUserLogout = new JMenuItem();
  JMenuItem m_menuUserRegister = new JMenuItem();
  JMenuItem m_menuUserProfile = new JMenuItem();
  JMenuItem m_menuUserSettings = new JMenuItem();
  JMenuItem m_menuUserAlbums = new JMenuItem();
  JMenuItem m_menuUserUpload = new JMenuItem();
  JMenuItem m_menuUserAlbumNew = new JMenuItem();
  
  JMenuItem m_menuGalleryHome = new JMenuItem();
  JMenuItem m_menuGalleryUsers = new JMenuItem();
  JMenuItem m_menuGallerySearch = new JMenuItem();
  
  JMenuItem m_menuSystemApplication = new JMenuItem();
  JMenuItem m_menuSystemBlank = new JMenuItem();
  
  JScrollPane m_jQueryResultPanel = new JScrollPane();
  JTable m_jResultSetTable = null;
  JLabel m_labelTable = new JLabel();

  Container m_jContentPane = null;
  private JLabel status;
  
  private User m_userActive = null;
  private ArrayList<Country> m_countryAll = null;
  private ArrayList<Region> m_regionAll = null;
  private ArrayList<City> m_cityAll = null;
  private ArrayList<Category> m_categoryAll = null;
  private ArrayList<Keyword> m_keywordAll = null;
  
  double m_nScreenWidth = 0;
  double m_nScreenHeight = 0;

  public IMFrame()
  {
	  Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	  m_nScreenWidth = screenSize.getWidth();
	  m_nScreenHeight = screenSize.getHeight();
    try
    { 
      m_jContentPane = getContentPane();

      setupDisplay();
      setupMenu();
      setupContentPane();

      setVisible(true);
      setResizable(false);

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
    contentPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

    // Sets the label for the table.
    m_labelTable.setFont(new Font("SansSerif", Font.BOLD, 15));
    m_labelTable.setEnabled(false);
    m_labelTable.setLabelFor(m_jQueryResultPanel);

    // Lays out the scroll pane for the table.
    m_jQueryResultPanel.setPreferredSize(new Dimension(846, 340));
    m_jQueryResultPanel.setBorder(BorderFactory.createEtchedBorder());
    m_jQueryResultPanel.setAutoscrolls(true);
    m_jQueryResultPanel.setEnabled(true);
    m_jQueryResultPanel.getVerticalScrollBar().setUnitIncrement(16);

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
   * Shows the login dialog (server list)
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
    
    setAllMenu(m_menuUser, false);
    m_menuUserLogin.setEnabled(true);
    m_menuUserRegister.setEnabled(true);
    
	downloadAll();
    
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
      
      setAllMenu(m_menuUser, false);
      setAllMenu(m_menuGallery, false);

      
      m_jQueryResultPanel.setViewportView(null);
      m_jQueryResultPanel.setEnabled(false);
      m_labelTable.setEnabled(false);

      IMMain.closeDBConnection();
      m_labelTable.setText(IMMessage.getString("CHOOSE_SERVER"));
      setStatusBar(IMMessage.getString("CHOOSE_SERVER"));
      m_userActive = null;
    }
    catch (Exception e)
    {
      new IMMessage(IMConstants.ERROR, "CONNECT_CLOSE_FAIL", e);
    }
  }

  
  /**
   * Initiates the frame.
   */
  private void setupDisplay()
  {
    m_jContentPane.setLayout(new BorderLayout());
    setSize(new Dimension(m_nWidth, m_nHeight));
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
          exitActionPerformed();
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
    
    
    m_menuUserProfile = createJMenuItem(m_menuUserProfile, "MAIN_MENU_PROFILE", 'P', false, "profile", "MAIN_MENU_PROFILE_DESC", new Callable<Void>() {
		   public Void call() {
			     showProfilePanel(m_userActive);
				return null;
		   }
		});
    
    m_menuUserSettings = createJMenuItem(m_menuUserSettings, "MAIN_MENU_SETTINGS", 'B', false, "settings", "MAIN_MENU_SETTINGS_DESC", new Callable<Void>() {
		   public Void call() {
			   	showSettingsPanel();
				return null;
		   }
		});

    
    m_menuUserAlbums = createJMenuItem(m_menuUserAlbums, "MAIN_MENU_ALBUMS", 'A', false, "album", "MAIN_MENU_ALBUMS_DESC", new Callable<Void>() {
		   public Void call() {
			   	showAlbumPanel(m_userActive);
				return null;
		   }
		});
    
    
    m_menuUserUpload = createJMenuItem(m_menuUserUpload, "MAIN_MENU_UPLOAD", 'F', false, "upload", "MAIN_MENU_UPLOAD_DESC", new Callable<Void>() {
			   public Void call() {
				   showNewPicturePanel(m_userActive);
					return null;
			   }
			});
    
    m_menuUserAlbumNew = createJMenuItem(m_menuUserAlbumNew, "MAIN_MENU_ALBUM_NEW", 'L', false, "album_new", "MAIN_MENU_ALBUM_NEW_DESC", new Callable<Void>() {
		   public Void call() {
			   	showNewAlbumDialog();
				return null;
		   }
		});
    
    m_menuGalleryHome = createJMenuItem(m_menuGalleryHome, "MAIN_MENU_HOME", 'K', false, "gallery", "MAIN_MENU_HOME_DESC", new Callable<Void>() {
		   public Void call() {
			    showGalleryPanel();
				return null;
		   }
		});
    
    m_menuGalleryUsers = createJMenuItem(m_menuGalleryUsers, "MAIN_MENU_USERS", 'F', false, "users", "MAIN_MENU_USERS_DESC", new Callable<Void>() {
		   public Void call() {
			    showUsersPanel();
				return null;
		   }
		});
    

    
    m_menuGallerySearch = createJMenuItem(m_menuGallerySearch, "MAIN_MENU_SEARCH", 'S', false, "search", "MAIN_MENU_SEARCH_DESC", new Callable<Void>() {
		   public Void call() {
			     showSearchPanel(m_userActive);
				return null;
		   }
		});

    
    m_menuSystemApplication = createJMenuItem(m_menuSystemApplication, "MAIN_MENU_ABOUT", 'N', true, "about", "MAIN_MENU_ABOUT_DESC", new Callable<Void>() {
		   public Void call() {
			    
				return null;
		   }
		});
    
    m_menuSystemBlank = createJMenuItem(m_menuSystemBlank, "MAIN_MENU_BLANK", 'N', true, null, "MAIN_MENU_ABOUT_DESC", new Callable<Void>() {
		   public Void call() {
			   m_jQueryResultPanel.setViewportView(null);
			   m_labelTable.setText("");
				return null;
		   }
		});
    



    m_menuConnection.setText(IMMessage.getString("MAIN_MENU_FILE"));
    m_menuConnection.setMnemonic('K');
    m_menuConnection.add(m_menuConnectionOpen);
    m_menuConnection.add(m_menuConnectionClose);
    m_menuConnection.addSeparator();
    m_menuConnection.add(m_menuConnectionExit);
    
    m_menuUser.setText(IMMessage.getString("MAIN_MENU_USER"));
    m_menuUser.setMnemonic('F');
    m_menuUser.add(m_menuUserLogin);
    m_menuUser.add(m_menuUserLogout);
    m_menuUser.add(m_menuUserRegister);
    m_menuUser.addSeparator();
    m_menuUser.add(m_menuUserProfile);
    m_menuUser.add(m_menuUserSettings);
    m_menuUser.add(m_menuUserAlbums);
    m_menuUser.addSeparator();
    m_menuUser.add(m_menuUserUpload);
    m_menuUser.add(m_menuUserAlbumNew);
    
    
    m_menuGallery.setText(IMMessage.getString("MAIN_MENU_GALLERY"));
    m_menuGallery.setMnemonic('G');
    m_menuGallery.add(m_menuGalleryHome);
    m_menuGallery.add(m_menuGalleryUsers);
    m_menuGallery.add(m_menuGallerySearch);
    
    m_menuSystem.setText(IMMessage.getString("MAIN_MENU_SYSTEM"));
    m_menuSystem.setMnemonic('G');
    m_menuSystem.add(m_menuSystemApplication);
    m_menuSystem.add(m_menuSystemBlank);
    
    m_menuBar.add(m_menuConnection);
    m_menuBar.add(m_menuUser);
    m_menuBar.add(m_menuGallery);
    m_menuBar.add(m_menuSystem);

    this.setJMenuBar(m_menuBar);
  }
  


public void downloadAll(){
	  downloadCities();
	  downloadCountries();
	  downloadRegions();
	  downloadCategories();
	  downloadKeywords();
  }

  public void downloadCountries(){
	  IMQuery q = new IMQuery();
	  m_countryAll = q.selectAllCountries();
  }
  
  public void downloadCities() {
	  IMQuery q = new IMQuery();
	  m_cityAll = q.selectAllCities();
  }
  
  public void downloadRegions(){
	  IMQuery q = new IMQuery();
	  m_regionAll = q.selectAllRegion();
  }
  
  public void downloadKeywords(){
	  IMQuery q = new IMQuery();
	  m_keywordAll = q.selectAllKeywords();
  }
  
  public void downloadCategories(){
	  IMQuery q = new IMQuery();
	  m_categoryAll = q.selectAllCategories();
  }
  
  
  /**
   * Wipe the user session, disable menuitems and then show the login panel
   * */
  public void userLogout() {  
	m_userActive = null;
	
	setAllMenu(m_menuUser, false);
	setAllMenu(m_menuGallery, false);
	m_menuUserRegister.setEnabled(true);
	m_menuUserLogin.setEnabled(true);
	
	showLoginPanel();
  }
  
  /**
   * Create a user session, enable menuitems, initiate the variables and then show the profile panel
   * @param user User object, contains the user data
   */
  public void userLogin(User user) {
	  setAllMenu(m_menuUser, true);
	  setAllMenu(m_menuGallery, true);
	  m_menuUserRegister.setEnabled(false);
	  m_menuUserLogin.setEnabled(false);

	  
	  m_userActive = user;
	  showProfilePanel(m_userActive);
  }
  
  /**
   * Show the login panel
   */
  void showLoginPanel(){
	  JPanelLogin panel = new JPanelLogin(this);
	  panel.init();
	  m_jQueryResultPanel.setViewportView(panel);
  }
  
  void showUsersPanel(){
	  JPanelUsers panel = new JPanelUsers(this);
	  panel.init();
	  m_jQueryResultPanel.setViewportView(panel);
  }
  
  /**
   * Show the signup panel
   */
  void showSignupPanel(){
	  JPanelSignup panel = new JPanelSignup(this);
	  panel.init();
	  m_jQueryResultPanel.setViewportView(panel);
  }
  
  void showGalleryPanel(){
	  JPanelGallery panel = new JPanelGallery(this);
	  panel.init();
	  m_jQueryResultPanel.setViewportView(panel);
  }
  
  /**
   * Show the profile panel
   * @param user Object of the user will be displayed
   */
  public void showProfilePanel(User user){
	  JPanelProfile panel = new JPanelProfile(this, user);
	  panel.init();
	  m_jQueryResultPanel.setViewportView(panel);
  }
  
  public void showSearchPanel(User user){
	  JPanelSearch panel = new JPanelSearch(this, user);
	  panel.init();
	  m_jQueryResultPanel.setViewportView(panel);
  }
  
  /**
   * Show the album panel
   * @param user Object of the user will be displayed
   */
  public void showAlbumPanel(User user){
	  JPanelAlbums panel = new JPanelAlbums(this, user);
	  panel.init();
	  m_jQueryResultPanel.setViewportView(panel);
  }
  
  /**
   * Show the settings panel
   */
  public void showSettingsPanel(){
	  JPanelSettings panel = new JPanelSettings(this, m_userActive);
	  panel.init();
	  m_jQueryResultPanel.setViewportView(panel);
  }
  
  protected void showNewAlbumDialog() {
	  JDialogNewAlbum newAlbumDialog = new JDialogNewAlbum(this);
	  newAlbumDialog.setVisible(true);
  }
  
  protected void showNewPicturePanel(User user) {
	  JPanelNewPicture panel = new JPanelNewPicture(this, user);
	  panel.init();
	  m_jQueryResultPanel.setViewportView(panel);
  }
  
  protected void showEditPicturePanel(User m_userDisplayed, Picture m_picture) {
	  JPanelEditPicture panel = new JPanelEditPicture(this, m_userDisplayed, m_picture);
	  panel.init();
	  m_jQueryResultPanel.setViewportView(panel);
		
	}
  
  /**
   * Set all menuitem in a specific menu to state (false or true)
   * @param menu The menu object
   * @param state The preferred state (true/false)
   */
  public void setAllMenu(JMenu menu, boolean state){
	  Component[] comp = menu.getMenuComponents();
	  for (int i=0; i<comp.length; i++){
		  if (comp[i] instanceof JMenuItem){
			  comp[i].setEnabled(state);
		  }
	  }
  }
  
  
  /**
   * Modify the JLabel object passed in the first parameter, and than return with it 
   * @param label The JLabel object
   * @param r Position of the JLabel
   * @param text Labeltext of the JLabel
   * @param mnemonic Mnemonic of the JLabel
   * @param component Set the component this is labelling
   * @return The modified JLabel object
   */
  public static JLabel createJLabel(JLabel label, Rectangle r, String text, int mnemonic, Component component){
	  label.setBounds(r);
	  label.setText(IMMessage.getString(text));
	  label.setHorizontalAlignment(JLabel.TRAILING);
	  label.setDisplayedMnemonic(mnemonic);
	  label.setLabelFor(component);
	  
	  return label;
  }
  
  /**
   * 
   * @param box
   * @param r
   * @param array
   * @param func
   * @return
   */
  public static JComboBox createComboBox(JComboBox box, boolean enabled, int selected, Rectangle r, ArrayList<?> array, final Callable<Void> func){
	  box.setBounds(r);
	  box.setEnabled(enabled);
	  box = setComboboxItems(box, array, selected);
	  	  
	  box.addActionListener(new ActionListener()
      {
	        public void actionPerformed(ActionEvent e)
	        {
	        	try {
	        		func.call();
				} catch (Exception e1) {
					new IMMessage(IMConstants.ERROR, "APP_ERR", e1);
				}
	        }
	      });
	  
	  return box;
  }
  
  public static JComboBox setComboboxItems(JComboBox box, ArrayList<?> array, int selected){
	  box.removeAllItems();
	  Iterator<?> it = array.iterator();
	  box.addItem(IMMessage.getString("SELECT"));
	  while (it.hasNext()){
		  Object o = it.next();
		  box.addItem(o);
	  }
	  box.setSelectedIndex(selected);
	  return box;
  }
  
  
  public IMTable createPictureResultTable(ArrayList<Picture> array, String text, int width, int height, int x, int y, JPanel contentPanel){
	  IMTableModelPicture m_model = new IMTableModelPicture(array, this); 
	  IMTable m_jTableData = new IMTable(m_model);		
	  m_jTableData.setRowSelectionAllowed(false);
	  
	  JScrollPane scroll = new JScrollPane(m_jTableData);
	  scroll.setSize(new Dimension(width, height));
	  scroll.setPreferredSize(new Dimension(width, height));
	  scroll.setBounds(new Rectangle(x, y, width, height));
	  
	  TitledBorder title;
	  title = BorderFactory.createTitledBorder(IMMessage.getString(text));
	  scroll.setBorder(title);
	  
	  contentPanel.add(scroll, null);
	  
	  return m_jTableData;
  }
  
  /**
   * 
   * @param menuItem
   * @param text
   * @param mnemonik
   * @param enabled
   * @param iconName
   * @param description
   * @param func
   * @return
   */
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
					new IMMessage(IMConstants.ERROR, "APP_ERR", e1);
				}
	        }
	      });
    
    return menuItem;
  }
  
  /**
   * 
   * @param button
   * @param text
   * @param mnemonic
   * @param iconName
   * @param description
   * @param r
   * @param func
   * @return
   */
  public static JButton createButton(JButton button, String text, int mnemonic, String iconName, String description, Rectangle r, final Callable<Void> func){
	  button.setBounds(r);
	  button.setText(IMMessage.getString(text));
	  button.setMnemonic(mnemonic);
	  
	  if (iconName!=null)
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
					new IMMessage(IMConstants.ERROR, "APP_ERR", e1);
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
					new IMMessage(IMConstants.ERROR, "APP_ERR", e1);
				}
	        }
	      });
	  
	  return button;
  }
  
  public void refresh(){
	  SwingUtilities.updateComponentTreeUI(this);
  }
  
  public City selectCurrentCity(int cityId){
	City c = null;
	Iterator<City> it = (getcityAll()).iterator();
	while (it.hasNext()){
		c = it.next();
		if (cityId==c.getCityId()){
			break;
		}
	}  
	
	return c;
  }
  
	
  public Region selectCurrentRegion(int regionId){
	Region r = null;
	Iterator<Region> it1 = (getregionAll()).iterator();
	while (it1.hasNext()){
		r = it1.next();
		if (regionId==r.getRegionId()){
			break;
		}
	}
	return r;
  }
  
  public Country selectCurrentCountry(int countryId){
	Country cou = null;
	Iterator<Country> it2 = (getcountryAll()).iterator();
	while (it2.hasNext()){
		cou = it2.next();
		if (countryId==cou.getCountryId()){
			break;
		}
	}
	return cou;
  }
  
  
  

  	public ArrayList<Country> getcountryAll() {
  		return m_countryAll;
	}
	
	public void setcountryAll(ArrayList<Country> m_countryAll) {
		this.m_countryAll = m_countryAll;
	}
	
	public ArrayList<Region> getregionAll() {
		return m_regionAll;
	}
	
	public void setregionAll(ArrayList<Region> m_regionAll) {
		this.m_regionAll = m_regionAll;
	}
	
	public ArrayList<City> getcityAll() {
		return m_cityAll;
	}
	
	public void setcityAll(ArrayList<City> m_cityAll) {
		this.m_cityAll = m_cityAll;
	}
	

	public User getUserActive() {
		return m_userActive;
	}

	public void setUserActive(User m_userActive) {
		this.m_userActive = m_userActive;
	}
	
	
	
	

  public ArrayList<Category> getcategoryAll() {
		return m_categoryAll;
	}

	public void setcategoryAll(ArrayList<Category> m_categoryAll) {
		this.m_categoryAll = m_categoryAll;
	}

	public ArrayList<Keyword> getkeywordAll() {
		return m_keywordAll;
	}

	public void setkeywordAll(ArrayList<Keyword> m_keywordAll) {
		this.m_keywordAll = m_keywordAll;
	}
	

  public int getWidth() {
		return m_nWidth;
	}

	public void setWidth(int m_nWidth) {
		this.m_nWidth = m_nWidth;
	}

	public int getHeight() {
		return m_nHeight;
	}

	public void setHeight(int m_nHeight) {
		this.m_nHeight = m_nHeight;
	}
	
	

	public double getScreenWidth() {
		return m_nScreenWidth;
	}

	public void setScreenWidth(double m_nScreenWidth) {
		this.m_nScreenWidth = m_nScreenWidth;
	}

	public double getScreenHeight() {
		return m_nScreenHeight;
	}

	public void setScreenHeight(double m_nScreenHeight) {
		this.m_nScreenHeight = m_nScreenHeight;
	}

/**
   * Exits the application
   */
  void exitActionPerformed()
  {
    disconnectActionPerformed();
    System.exit(0);
  }

  public void setPlaceCombos(JComboBox m_jComboCountry, JComboBox m_jComboRegion,
		JComboBox m_jComboCity, User m_userDisplayed) {
		setPlaceCombos(m_jComboCountry, m_jComboRegion, m_jComboCity, m_userDisplayed.getCityId());
	
  }
  
  public void setPlaceCombos(JComboBox m_jComboCountry, JComboBox m_jComboRegion,
			JComboBox m_jComboCity, Picture m_picture) {
	  setPlaceCombos(m_jComboCountry, m_jComboRegion, m_jComboCity, m_picture.getCityId());	
		
  }

  protected void setPlaceCombos(JComboBox m_jComboCountry, JComboBox m_jComboRegion,
			JComboBox m_jComboCity, int cityId){
	  int regionId = 0;
		City c = null;
		Iterator<City> it = (getcityAll()).iterator();
		while (it.hasNext()){
			c = it.next();
			if (cityId==c.getCityId()){
				m_jComboCity.setSelectedItem(c);
				regionId = c.getRegionId();
				break;
			}
		}
		
		it = (getcityAll()).iterator();
		while (it.hasNext()){
			City city = it.next();
			if (city.getRegionId()!=regionId){
				m_jComboCity.removeItem(city);
			}
		}
	
		Region r = null;
		Iterator<Region> it1 = (getregionAll()).iterator();
		while (it1.hasNext()){
			r = it1.next();
			if (c.getRegionId()==r.getRegionId()){
				m_jComboRegion.setSelectedItem(r);
				break;
			}
		}
		
		Country cou = null;
		Iterator<Country> it2 = (getcountryAll()).iterator();
		while (it2.hasNext()){
			cou = it2.next();
			if (r.getCountryId()==cou.getCountryId()){
				m_jComboCountry.setSelectedItem(cou);
				break;
			}
		}
		
		m_jComboCity.setEnabled(true);
		m_jComboCountry.setEnabled(false);
		m_jComboRegion.setEnabled(false);
  }

	public void scrollTo(int x, int y) {
		m_jQueryResultPanel.getViewport().setViewPosition(new Point(x, y));
		
	}

}
