


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.util.concurrent.Callable;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class JPanelLogin extends JPanel implements IMConstants{
	
	JButton m_jButtonLogin = new JButton();
	JButton m_jButtonCancel = new JButton();
	  
	protected IMFrame m_jFrameOwner = null;
	
	protected JLabel m_jLabelUsername = new JLabel();
	protected JTextField m_jLoginField = new JTextField();
	
	protected JLabel m_jLabelPasswd = new JLabel();
	protected JPasswordField m_jPasswordField = new JPasswordField();
	
	protected JPanel contentPanel;
	protected String m_szButtonConfirmTitle = "MAIN_MENU_LOGIN";
	
	protected String m_szTitle;	
	protected int m_nWidth = 400;
	protected int m_nHeight = 180;
	
	protected int m_iFieldHeight = 30;
	protected int m_iDeltaY = 10;
	protected int m_iLabelStartY = 20;
	protected int m_iFieldStartY = m_iLabelStartY + 5;
	protected int m_iLabelStartX = 25;
	protected int m_iFieldStartX = 145;	
    protected int m_nButtonWidth = 120;
	protected int m_nButtonHeight = 35;
	protected int m_nButtonStart = 70;
	protected int m_nLastButtonStart = m_nButtonStart;
	protected int m_iLabelWidth = 100;
	protected int m_iFieldWidth = 185;

	
	public JPanelLogin(IMFrame frame){
		this.m_jFrameOwner = frame;
		this.m_szTitle = IMMessage.getString("MAIN_MENU_LOGIN");
	}
	
	public void init(){
		try
	    {
	      setupDisplay();
	      setupButton();
	      setupContentPane();
	      add(contentPanel, BorderLayout.CENTER);

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
	    IMFrame.createButton(m_jButtonLogin, m_szButtonConfirmTitle, 'L', "confirm", "LOGINDIAG_LOGIN_BUTTON_DESC", 
	    		new Rectangle(m_nLastButtonStart, m_nHeight-m_nButtonHeight-30, m_nButtonWidth  , m_nButtonHeight), new Callable<Void>() {
			   public Void call() {
			        confirm();
					return null;
			   }
			});
	    
	    IMFrame.createButton(m_jButtonCancel, "LOGINDIAG_CANCEL_BUTTON", 'N', "cancel", "LOGINDIAG_CANCEL_BUTTON_DESC", 
	    		new Rectangle(m_nLastButtonStart+=m_nButtonWidth, m_nHeight-m_nButtonHeight-30, m_nButtonWidth, m_nButtonHeight), new Callable<Void>() {
			   public Void call() {
			        cancel();
					return null;
			   }
			});
	  }

	  /**
	   * Draws the content pane.
	   */
	  protected void setupContentPane() 
	  {
		m_jLabelUsername = IMFrame.createJLabel(m_jLabelUsername, new Rectangle(m_iLabelStartX, m_iLabelStartY, m_iLabelWidth , m_iFieldHeight), 
				"LOGINDIAG_USERNAME", 'U', m_jLoginField);
		
		m_jLabelPasswd = IMFrame.createJLabel(m_jLabelPasswd, new Rectangle(m_iLabelStartX, m_iLabelStartY+=(m_iFieldHeight+m_iDeltaY), m_iLabelWidth, m_iFieldHeight), 
				"LOGINDIAG_PASSWD", 'P', m_jPasswordField);

	    m_jLoginField.setBounds(
		        new Rectangle(m_iFieldStartX, m_iFieldStartY, m_iFieldWidth , m_iFieldHeight));
	    m_jLoginField.setToolTipText(IMMessage.getString("LOGINDIAG_USERNAME_FIELD_DESC"));


	    m_jPasswordField.setBounds(
		        new Rectangle(m_iFieldStartX, m_iFieldStartY+=(m_iFieldHeight+m_iDeltaY), m_iFieldWidth, m_iFieldHeight));
	    m_jPasswordField.setToolTipText(IMMessage.getString("LOGINDIAG_PASSWD_FIELD_DESC"));

	    
	    
	    contentPanel = new JPanel();
	    contentPanel.setLayout(null);

	    contentPanel.add(m_jLoginField, null);
	    contentPanel.add(m_jPasswordField, null);
	    contentPanel.add(m_jLabelPasswd, null);
	    contentPanel.add(m_jLabelUsername, null);

	    contentPanel.add(m_jButtonCancel, null);
	    contentPanel.add(m_jButtonLogin, null);
	  }

	  /**
	   * Logs in to the database and creates the connection.
	   * Sets auto commit to false. Shows the product information
	   * table.
	   */
	  void confirm()
	  {
		  if (
			m_jLoginField.getText().equals("") ||
			m_jPasswordField.getPassword().equals("")
			 ){
			  new IMMessage(IMConstants.ERROR, "EMPTY_FIELD", new Exception());
		  } else {
			  
	      }
	  }

	  /**
	   * Cancels the login.
	   */
	  void cancel()
	  {
	    m_jPasswordField.setText(null);
	    m_jLoginField.setText(null);
	  }

	  /**
	   * Initializes the dialog display.
	   */
	  private void setupDisplay()
	  {
		m_jFrameOwner.setLabel(m_szTitle);
	    this.setSize(new Dimension(m_nWidth, m_nHeight));
	    this.getAccessibleContext().setAccessibleDescription(
	        IMMessage.getString("LOGINDIAG_DESC"));
	    this.setLayout(new BorderLayout());
	  }
	  
	  
	
	
	
}
