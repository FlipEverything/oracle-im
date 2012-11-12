/* Copyright (c) 2003, 2008, Oracle. All rights reserved.  */
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Frame;
import java.awt.Toolkit;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleDriver;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Displays the login dialog and creates the connection
 * to the database.
 */
public class IMLoginDialog extends JDialog implements IMConstants
{
	
  private String defaultUserName = "";
  private String defaultHostName = "";
  private String defaultSID = "";
  private String defaultPort = "";
  
  IMExampleFrame m_jFrameOwner = null;

  JButton m_jButtonLogin = new JButton();
  JButton m_jButtonCancel = new JButton();

  JLabel m_jLabelUsername = new JLabel();
  JTextField m_jLoginField = new JTextField(defaultUserName);

  JLabel m_jLabelPasswd = new JLabel();
  JPasswordField m_jPasswordField = new JPasswordField();

  JLabel m_jLabelHostname = new JLabel();
  JTextField m_jHostField = new JTextField(defaultHostName);

  JLabel m_jLabelSID = new JLabel();
  JTextField m_jSIDField = new JTextField(defaultSID);

  JLabel m_jLabelPort = new JLabel();
  JTextField m_jPortField = new JTextField(defaultPort);

  public IMLoginDialog(IMExampleFrame jFrameOwner)
  {
    super(jFrameOwner, true);
    m_jFrameOwner = jFrameOwner;

    try
    {
      setupDisplay();
      setupButton();
      setupContentPane();
      addListener();

      setVisible(true);
    }
    catch (Exception e)
    {
      IMMessage msg = new IMMessage(IMConstants.ERROR, "APP_ERR", e);
    }
  }

  /**
   * Draws buttons.
   */
  private void setupButton()
  {
    m_jButtonLogin.setBounds(new Rectangle(70, 225, 100, 35));
    m_jButtonCancel.setBounds(new Rectangle(225, 225, 100, 35));

    m_jButtonLogin.setText(IMMessage.getString("LOGINDIAG_LOGIN_BUTTON"));
    m_jButtonLogin.setMnemonic('L');
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

    m_jButtonCancel.setText(IMMessage.getString("LOGINDIAG_CANCEL_BUTTON"));
    m_jButtonCancel.setMnemonic('N');
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

  /**
   * Draws the content pane.
   */
  private void setupContentPane() 
  {
    int iHeight = 30;
    int iDeltaY = 10;
    int iLabelStartY = 20;
    int iFieldStartY = iLabelStartY + 5;
    int iLabelStartX = 25;
    int iFieldStartX = 145;

    m_jLabelUsername.setBounds(
        new Rectangle(iLabelStartX, iLabelStartY, 100, iHeight));
    m_jLoginField.setBounds(
        new Rectangle(iFieldStartX, iFieldStartY, 185, iHeight));

    m_jLabelPasswd.setBounds(
        new Rectangle(iLabelStartX, iLabelStartY+iHeight+iDeltaY, 100, iHeight));
    m_jPasswordField.setBounds(
        new Rectangle(iFieldStartX, iFieldStartY+iHeight+iDeltaY, 185, iHeight));

    m_jLabelHostname.setBounds(
        new Rectangle(iLabelStartX, iLabelStartY+2*(iHeight+iDeltaY), 100, iHeight));
    m_jHostField.setBounds(
        new Rectangle(iFieldStartX, iFieldStartY+2*(iHeight+iDeltaY), 185, iHeight));

    m_jLabelPort.setBounds(
        new Rectangle(iLabelStartX, iLabelStartY+3*(iHeight+iDeltaY), 100, iHeight));
    m_jPortField.setBounds(
        new Rectangle(iFieldStartX, iFieldStartY+3*(iHeight+iDeltaY), 185, iHeight));

    m_jLabelSID.setBounds(
        new Rectangle(iLabelStartX, iLabelStartY+4*(iHeight+iDeltaY), 100, iHeight));
    m_jSIDField.setBounds(
        new Rectangle(iFieldStartX, iFieldStartY+4*(iHeight+iDeltaY), 185, iHeight));

    m_jLabelUsername.setText(IMMessage.getString("LOGINDIAG_USERNAME"));
    m_jLabelUsername.setHorizontalAlignment(JLabel.TRAILING);
    m_jLabelUsername.setDisplayedMnemonic('U');
    m_jLabelUsername.setLabelFor(m_jLoginField);

    //m_jLoginField.setText("scott");
    m_jLoginField.setToolTipText(IMMessage.getString("LOGINDIAG_USERNAME_FIELD_DESC"));

    m_jLabelPasswd.setText("Password:");
    m_jLabelPasswd.setHorizontalAlignment(JLabel.TRAILING);
    m_jLabelPasswd.setDisplayedMnemonic('P');
    m_jLabelPasswd.setLabelFor(m_jPasswordField);

    m_jPasswordField.setToolTipText(IMMessage.getString("LOGINDIAG_PASSWD_FIELD_DESC"));

    m_jLabelHostname.setText("Host name:");
    m_jLabelHostname.setHorizontalAlignment(JLabel.TRAILING);
    m_jLabelHostname.setDisplayedMnemonic('H');
    m_jLabelHostname.setLabelFor(m_jHostField);
    
    m_jHostField.setToolTipText(IMMessage.getString("LOGINDIAG_HOSTNAME_FIELD_DESC"));

    m_jLabelPort.setText("Port:");
    m_jLabelPort.setHorizontalAlignment(JLabel.TRAILING);
    m_jLabelPort.setDisplayedMnemonic('T');
    m_jLabelPort.setLabelFor(m_jPortField);

    m_jPortField.setToolTipText(IMMessage.getString("LOGINDIAG_PORT_FIELD_DESC"));

    m_jLabelSID.setText("Service name:");
    m_jLabelSID.setHorizontalAlignment(JLabel.TRAILING);
    m_jLabelSID.setDisplayedMnemonic('S');
    m_jLabelSID.setLabelFor(m_jSIDField);

    m_jSIDField.setToolTipText(IMMessage.getString("LOGINDIAG_SID_FIELD_DESC"));

    JPanel contentPanel = new JPanel();
    contentPanel.setLayout(null);
    contentPanel.add(m_jPortField, null);
    contentPanel.add(m_jLabelPort, null);
    contentPanel.add(m_jSIDField, null);
    contentPanel.add(m_jLabelSID, null);
    contentPanel.add(m_jHostField, null);
    contentPanel.add(m_jLabelHostname, null);
    contentPanel.add(m_jLoginField, null);
    contentPanel.add(m_jPasswordField, null);
    contentPanel.add(m_jLabelPasswd, null);
    contentPanel.add(m_jLabelUsername, null);

    contentPanel.add(m_jButtonCancel, null);
    contentPanel.add(m_jButtonLogin, null);

    this.getContentPane().add(contentPanel, BorderLayout.CENTER);
  }

  /**
   * Logs in to the database and creates the connection.
   * Sets auto commit to false. Shows the product information
   * table.
   */
  void login()
  {
    try
    {
      String connectString;
      DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
      connectString = 
        "jdbc:oracle:oci:@(description=(address=(host=" +
        m_jHostField.getText() + ")(protocol=tcp)(port=" +
        m_jPortField.getText() + "))(connect_data=(sid=" +
        m_jSIDField.getText() + ")))";

      IMExample.setDBConnection(
          (OracleConnection) DriverManager.getConnection(connectString, 
            m_jLoginField.getText(), 
            new String(m_jPasswordField.getPassword())));

      IMExample.setAutoCommit(false);
      m_jFrameOwner.enableDisplay();

      this.setVisible(false);
      this.dispose();

      m_jFrameOwner.showDefaultTable();
    }
    catch (SQLException e)
    {
      new IMMessage(IMConstants.ERROR, "CANNOT_CONNECT", e);
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
    this.setTitle(IMMessage.getString("LOGINDIAG_TITLE"));
    this.setSize(new Dimension(400, 300));
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
          boolean gotFocus = false;
          public void windowOpened(WindowEvent e)
          {
            if (!gotFocus)
            {
              m_jLoginField.requestFocus();
              gotFocus =true;
            }
          }
        });
  }
}
