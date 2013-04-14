

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import bean.DatabaseCreditental;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.KeyEvent;

import oracle.jdbc.OracleConnection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Displays the login dialog and creates the connection
 * to the database.
 */
@SuppressWarnings("serial")
public class IMLoginListDialog extends JDialog implements IMConstants
{
  
  IMFrame m_jFrameOwner = null;

  JButton m_jButtonLogin = new JButton();
  JButton m_jButtonCancel = new JButton();
  JButton m_jButtonNew = new JButton();
  JButton m_jButtonDelete = new JButton();
  
  int m_nWidth = 500;
  int m_nHeight = 250;
  int m_nButtonWidth = 115;
  int m_nButtonHeight = 35;
  int m_nOffset = 40;
  int m_nTableOffset = 20;
  int m_nLabelHeight = 40;
  int m_nBorder = 8;
  int m_nButtonOffset = 6;
  
  AppDataStore m_dbStore = null;
  ArrayList<DatabaseCreditental> m_aCreditentals = new ArrayList<DatabaseCreditental>(); 
  int m_nActiveServerID = 0;
  JTable m_jTableCreditentals = null;
  

  public IMLoginListDialog(IMFrame jFrameOwner)
  {
    super(jFrameOwner, true);
    m_jFrameOwner = jFrameOwner;

    try
    {
      loadCreditentals();
      setupDisplay();
      setupButton();
      setupContentPane();
      addListener();

      setVisible(true);
    }
    catch (Exception e)
    {
      new IMMessage(IMConstants.ERROR, "APP_ERR", e);
    }
  }

  private void loadCreditentals() {
	m_dbStore = new AppDataStore();
	  
	
	try {
		m_aCreditentals = m_dbStore.init();
	} catch (SQLException e) {
		
		new IMMessage(IMConstants.ERROR, "APP_ERR", e);
	}
	
	m_jTableCreditentals = new JTable(new IMLoginListTableModel(m_aCreditentals, this));
	m_jTableCreditentals.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	m_jTableCreditentals.getColumnModel().getColumn(0).setPreferredWidth((int) ((m_nWidth - m_nTableOffset / 2) *0.26));
	m_jTableCreditentals.getColumnModel().getColumn(1).setPreferredWidth((int) ((m_nWidth - m_nTableOffset / 2) *0.32));
	m_jTableCreditentals.getColumnModel().getColumn(2).setPreferredWidth((int) ((m_nWidth - m_nTableOffset / 2) *0.11));
	m_jTableCreditentals.getColumnModel().getColumn(3).setPreferredWidth((int) ((m_nWidth - m_nTableOffset / 2) *0.11));
	m_jTableCreditentals.getColumnModel().getColumn(4).setPreferredWidth((int) ((m_nWidth - m_nTableOffset / 2) *0.20));
	m_jTableCreditentals.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	
  }

/**
   * Draws buttons.
   */
  private void setupButton()
  {
    int buttonPosition = m_nButtonOffset;
    
    m_jButtonLogin.setBounds(new Rectangle(buttonPosition, m_nHeight-m_nButtonHeight-m_nOffset-m_nLabelHeight, m_nButtonWidth, m_nButtonHeight));
    m_jButtonNew.setBounds(new Rectangle(buttonPosition+=(m_nButtonWidth+m_nButtonOffset), m_nHeight-m_nButtonHeight-m_nOffset-m_nLabelHeight, m_nButtonWidth, m_nButtonHeight));
    m_jButtonDelete.setBounds(new Rectangle(buttonPosition+=(m_nButtonWidth+m_nButtonOffset), m_nHeight-m_nButtonHeight-m_nOffset-m_nLabelHeight, m_nButtonWidth, m_nButtonHeight));
    m_jButtonCancel.setBounds(new Rectangle(buttonPosition+=(m_nButtonWidth+m_nButtonOffset), m_nHeight-m_nButtonHeight-m_nOffset-m_nLabelHeight, m_nButtonWidth, m_nButtonHeight));
    
    m_jButtonLogin.setText(IMMessage.getString("LOGINDIAG_LOGIN_BUTTON"));
    m_jButtonLogin.setMnemonic('K');
    m_jButtonLogin.setIcon(new ImageIcon(IMFrame.class.getResource("icons/connect.png")));
    m_jButtonLogin.setToolTipText(IMMessage.getString("LOGINDIAG_LOGIN_BUTTON_DESC"));
    m_jButtonLogin.addKeyListener(new java.awt.event.KeyAdapter()
      {
        public void keyPressed(KeyEvent e)
        {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) 
            login();
        }
      });
    m_jButtonLogin.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          login();
        }
      });
    
    m_jButtonNew.setText(IMMessage.getString("LOGINDIAG_NEW_BUTTON"));
    m_jButtonNew.setMnemonic('Ú');
    m_jButtonNew.setIcon(new ImageIcon(IMFrame.class.getResource("icons/add.png")));
    m_jButtonNew.setToolTipText(IMMessage.getString("LOGINDIAG_NEW_BUTTON_DESC"));
    m_jButtonNew.addKeyListener(new java.awt.event.KeyAdapter()
      {
        public void keyPressed(KeyEvent e)
        {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) 
        	  loginWindow();
        }
      });
    m_jButtonNew.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
        	loginWindow();
        }
      });
    
    m_jButtonDelete.setText(IMMessage.getString("LOGINDIAG_DELETE_BUTTON"));
    m_jButtonDelete.setMnemonic('T');
    m_jButtonDelete.setIcon(new ImageIcon(IMFrame.class.getResource("icons/delete.png")));
    m_jButtonDelete.setToolTipText(IMMessage.getString("LOGINDIAG_DELETE_BUTTON_DESC"));
    m_jButtonDelete.addKeyListener(new java.awt.event.KeyAdapter()
      {
        public void keyPressed(KeyEvent e)
        {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) 
        	  delete();
        }
      });
    m_jButtonDelete.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
        	delete();
        }
      });

    m_jButtonCancel.setText(IMMessage.getString("LOGINDIAG_CANCEL_BUTTON"));
    m_jButtonCancel.setMnemonic('M');
    m_jButtonCancel.setIcon(new ImageIcon(IMFrame.class.getResource("icons/cancel.png")));
    m_jButtonCancel.setToolTipText(
        IMMessage.getString("LOGINDIAG_CANCEL_BUTTON_DESC"));

    m_jButtonCancel.addKeyListener(new java.awt.event.KeyAdapter()
      {
        public void keyPressed(KeyEvent e)
        {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) 
            cancelLogin();
        }
      });
    m_jButtonCancel.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          cancelLogin();
        }
      });
  }

  protected void delete() {
	  try
	  {
		  int row = m_jTableCreditentals.getSelectedRow();
		  DatabaseCreditental db = m_aCreditentals.get(row);
		  m_dbStore.delete(db);
	      m_aCreditentals.remove(db);
		  refresh();
	  } 
	  catch (IndexOutOfBoundsException e) 
	  {
		  new IMMessage(IMConstants.ERROR, "NO_RECORD_SELECTED", e);
	  } catch (SQLException e) {
		
		new IMMessage(IMConstants.ERROR, "NO_RECORD_SELECTED", e);
	}
  }
  
  public void update(DatabaseCreditental db) {
	  try {
		m_dbStore.update(db);
	} catch (SQLException e) {
		
		new IMMessage(IMConstants.ERROR, "APP_ERR", e);
	}
  }
	
  protected void loginWindow() {
	  new IMLoginDialog(this);
  }
  
  public void refresh(){
	  SwingUtilities.updateComponentTreeUI(this);
  }

  /**
   * Draws the content pane.
   */
  private void setupContentPane() 
  {
	int iWidth = m_nWidth - m_nTableOffset / 2;
	int iHeight = m_nHeight - m_nButtonHeight - m_nOffset - m_nTableOffset - m_nLabelHeight;
	
	
    JScrollPane tablePane = new JScrollPane(m_jTableCreditentals);
    tablePane.setSize(new Dimension(iWidth, iHeight));
   
    EmptyBorder labelBorder = new EmptyBorder(m_nBorder, m_nBorder, m_nBorder, m_nBorder);
    JLabel label = new JLabel(IMMessage.getString("LOGINDIAG_INFO"));
    label.setSize(new Dimension(m_nWidth, m_nLabelHeight));
    label.setPreferredSize(new Dimension(m_nWidth, m_nLabelHeight));
    label.setBorder(labelBorder);
    
    
    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(null);
    contentPanel.add(tablePane, null);

    contentPanel.add(m_jButtonCancel, null);
    contentPanel.add(m_jButtonNew, null);
    contentPanel.add(m_jButtonDelete, null);
    contentPanel.add(m_jButtonLogin, null);

    this.getContentPane().add(label, BorderLayout.NORTH);
    this.getContentPane().add(contentPanel, BorderLayout.CENTER);
  }

  /**
   * Logs in to the database and creates the connection.
   * Sets auto commit to false. Shows the product information
   * table.
   */
  void login()
  {
	  int row = m_jTableCreditentals.getSelectedRow();
	  
    try
    {
      String connectString;
      DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
      connectString = 
        "jdbc:oracle:oci:@(description=(address=(host=" +
        m_aCreditentals.get(row).getHostname() + ")(protocol=tcp)(port=" +
        m_aCreditentals.get(row).getPort() + "))(connect_data=(sid=" +
        m_aCreditentals.get(row).getSid() + ")))";

      IMRunnableMain.setDBConnection(
          (OracleConnection) DriverManager.getConnection(connectString, 
        		  m_aCreditentals.get(row).getUsername(), 
        		  m_aCreditentals.get(row).getPassword())
          );

      IMRunnableMain.setAutoCommit(false);
      m_jFrameOwner.enableDisplay();

      this.setVisible(false);
      this.dispose();

      m_jFrameOwner.showLoginPanel();
      m_nActiveServerID = row;
      DatabaseCreditental db = m_aCreditentals.get(m_nActiveServerID);
      m_jFrameOwner.setStatusBar(IMMessage.getString("CONNECTED_TO")+" "+db.getUsername()+"@//"+db.getHostname()+":"+db.getPort()+"/"+db.getSid());
    }
    catch (SQLException e)
    {
      new IMMessage(IMConstants.ERROR, "CANNOT_CONNECT", e);
    } 
    catch (IndexOutOfBoundsException e) 
    {
	  new IMMessage(IMConstants.ERROR, "NO_RECORD_SELECTED", e);
    }
    catch (Exception e)
    {
      new IMMessage(IMConstants.ERROR, "APP_ERR", e);
    }
  }

  /**
   * Cancels the login.
   */
  void cancelLogin()
  {
    this.setVisible(false);
    this.dispose();
  }

  /**
   * Initializes the dialog display.
   */
  private void setupDisplay()
  {
    this.setTitle(IMMessage.getString("LOGINLISTDIAG_TITLE"));
    this.setSize(new Dimension(m_nWidth, m_nHeight));
    this.getAccessibleContext().setAccessibleDescription(
        IMMessage.getString("LOGINDIAG_DESC"));
    this.getContentPane().setLayout(new BorderLayout());
    this.setResizable(false);

    IMUIUtil.initJDialogHelper(this);
  }
  
  /**
   * Adds the WindowEvent listener.
   */
  private void addListener()
  {
    this.addWindowListener(new WindowAdapter()
        {
          
        });
  }

  public void addCreditentals(DatabaseCreditental db) {
	try {
		if (m_dbStore.insert(db)){
			m_aCreditentals.add(db);	
		}
	} catch (SQLException e) {
		new IMMessage(IMConstants.ERROR, "APP_ERR", e);
	}
	
  }

  
  
}
