package multimedia;
/* Copyright (c) 2003, 2008, Oracle. All rights reserved.  */
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import bean.DatabaseCreditental;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.KeyEvent;

/**
 * Displays the login dialog and creates the connection
 * to the database.
 */
@SuppressWarnings("serial")
public class IMLoginDialog extends JDialog implements IMConstants
{
  
  IMLoginListDialog m_jFrameOwner = null;

  JButton m_jButtonLogin = new JButton();
  JButton m_jButtonCancel = new JButton();

  JLabel m_jLabelUsername = new JLabel();
  JTextField m_jLoginField = new JTextField();

  JLabel m_jLabelPasswd = new JLabel();
  JPasswordField m_jPasswordField = new JPasswordField();

  JLabel m_jLabelHostname = new JLabel();
  JTextField m_jHostField = new JTextField();

  JLabel m_jLabelSID = new JLabel();
  JTextField m_jSIDField = new JTextField();

  JLabel m_jLabelPort = new JLabel();
  JTextField m_jPortField = new JTextField();
  
  JLabel m_jLabelDescription = new JLabel();
  JTextField m_jDescriptionField = new JTextField();

  public IMLoginDialog(IMLoginListDialog jFrameOwner)
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
      new IMMessage(IMConstants.ERROR, "APP_ERR", e);
    }
  }

  /**
   * Draws buttons.
   */
  private void setupButton()
  {
    m_jButtonLogin.setBounds(new Rectangle(70, 275, 100, 35));
    m_jButtonCancel.setBounds(new Rectangle(175, 275, 100, 35));

    m_jButtonLogin.setText(IMMessage.getString("LOGINDIAG_CREATE_BUTTON"));
    m_jButtonLogin.setMnemonic('L');
    m_jButtonLogin.setIcon(new ImageIcon(IMFrame.class.getResource("icons/confirm.png")));
    m_jButtonLogin.setToolTipText(IMMessage.getString("LOGINDIAG_LOGIN_BUTTON_DESC"));
    m_jButtonLogin.addKeyListener(new java.awt.event.KeyAdapter()
      {
        public void keyPressed(KeyEvent e)
        {
          if (e.getKeyCode() == KeyEvent.VK_ENTER) 
            createLogin();
        }
      });
    m_jButtonLogin.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          createLogin();
        }
      });

    m_jButtonCancel.setText(IMMessage.getString("LOGINDIAG_CANCEL_BUTTON"));
    m_jButtonCancel.setMnemonic('N');
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
    
    m_jLabelDescription.setBounds(
        new Rectangle(iLabelStartX, iLabelStartY+5*(iHeight+iDeltaY), 100, iHeight));
    m_jDescriptionField.setBounds(
        new Rectangle(iFieldStartX, iFieldStartY+5*(iHeight+iDeltaY), 185, iHeight));

    m_jLabelUsername.setText(IMMessage.getString("LOGINDIAG_USERNAME"));
    m_jLabelUsername.setHorizontalAlignment(JLabel.TRAILING);
    m_jLabelUsername.setDisplayedMnemonic('U');
    m_jLabelUsername.setLabelFor(m_jLoginField);

    m_jLoginField.setToolTipText(IMMessage.getString("LOGINDIAG_USERNAME_FIELD_DESC"));

    m_jLabelPasswd.setText(IMMessage.getString("LOGINDIAG_PASSWD"));
    m_jLabelPasswd.setHorizontalAlignment(JLabel.TRAILING);
    m_jLabelPasswd.setDisplayedMnemonic('P');
    m_jLabelPasswd.setLabelFor(m_jPasswordField);

    m_jPasswordField.setToolTipText(IMMessage.getString("LOGINDIAG_PASSWD_FIELD_DESC"));

    m_jLabelHostname.setText(IMMessage.getString("LOGINDIAG_HOSTNAME"));
    m_jLabelHostname.setHorizontalAlignment(JLabel.TRAILING);
    m_jLabelHostname.setDisplayedMnemonic('H');
    m_jLabelHostname.setLabelFor(m_jHostField);
    
    m_jHostField.setToolTipText(IMMessage.getString("LOGINDIAG_HOSTNAME_FIELD_DESC"));

    m_jLabelPort.setText(IMMessage.getString("LOGINDIAG_PORT"));
    m_jLabelPort.setHorizontalAlignment(JLabel.TRAILING);
    m_jLabelPort.setDisplayedMnemonic('T');
    m_jLabelPort.setLabelFor(m_jPortField);

    m_jPortField.setToolTipText(IMMessage.getString("LOGINDIAG_PORT_FIELD_DESC"));

    m_jLabelSID.setText(IMMessage.getString("LOGINDIAG_SID"));
    m_jLabelSID.setHorizontalAlignment(JLabel.TRAILING);
    m_jLabelSID.setDisplayedMnemonic('S');
    m_jLabelSID.setLabelFor(m_jSIDField);

    m_jSIDField.setToolTipText(IMMessage.getString("LOGINDIAG_DESCRIPTION_FIELD_DESC"));
    
    m_jLabelDescription.setText(IMMessage.getString("LOGINDIAG_DESCRIPTION"));
    m_jLabelDescription.setHorizontalAlignment(JLabel.TRAILING);
    m_jLabelDescription.setDisplayedMnemonic('M');
    m_jLabelDescription.setLabelFor(m_jDescriptionField);

    m_jDescriptionField.setToolTipText(IMMessage.getString("LOGINDIAG_SID_FIELD_DESC"));

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
    contentPanel.add(m_jLabelDescription, null);
    contentPanel.add(m_jDescriptionField, null);

    contentPanel.add(m_jButtonCancel, null);
    contentPanel.add(m_jButtonLogin, null);

    this.getContentPane().add(contentPanel, BorderLayout.CENTER);
  }

  /**
   * Logs in to the database and creates the connection.
   * Sets auto commit to false. Shows the product information
   * table.
   */
  void createLogin()
  {
	  if (
		m_jPortField.getText().equals("") ||
		m_jSIDField.getText().equals("") ||
		m_jHostField.getText().equals("") ||
		m_jLoginField.getText().equals("") ||
		m_jPasswordField.getPassword().equals("")
		 ){
		  new IMMessage(IMConstants.ERROR, "EMPTY_FIELD", new Exception());
	  } else {
		  try
		  {
			  m_jFrameOwner.addCreditentals(new DatabaseCreditental(
					  m_jHostField.getText(), 
					  Integer.parseInt(m_jPortField.getText()), 
					  m_jSIDField.getText(),
					  m_jLoginField.getText(),
					  new String(m_jPasswordField.getPassword()), 
					  m_jDescriptionField.getText() 
					  )
			  );
			  this.setVisible(false);
			  m_jFrameOwner.refresh();
		   } 
		   catch (NumberFormatException e)
		   {
			   new IMMessage(IMConstants.ERROR, "PORT_MUST_BE_NUMBER", e);
		   }
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
    this.setSize(new Dimension(400, 350));
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
