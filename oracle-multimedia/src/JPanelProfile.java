


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.Callable;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;



@SuppressWarnings("serial")
public class JPanelProfile extends JPanel implements IMConstants{

	  
	protected IMFrame m_jFrameOwner = null;
	protected User m_userDisplayed = null;
	protected JPanel contentPanel;
	
	protected String m_szTitle;	
	protected int m_nWidth = 600;
	protected int m_nHeight = 300;
	protected int m_nProfilePictureWidth = 150;
	protected int m_nOffset = 30;
	
	
	public JPanelProfile(IMFrame frame, User user){
		this.m_jFrameOwner = frame;
		this.m_szTitle = IMMessage.getString("MAIN_MENU_PROFILE");
		this.m_userDisplayed = user;
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

	  }

	  /**
	   * Draws the content pane.
	   */
	  protected void setupContentPane() 
	  {
		Icon profilePicture = null;
		
		if (m_userDisplayed.getProfilePicture()==null){
			profilePicture = new ImageIcon(IMFrame.class.getResource("icons/no_profile_picture.png"));
		} else {
			try {
				byte[] thumbnail = IMImagePanel.getDataInByteArray(m_userDisplayed.getProfilePicture());
				profilePicture = new ImageIcon(thumbnail);
			} catch (SQLException e) {
				new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
			} catch (IOException e) {
				new IMMessage(IMConstants.ERROR, "APP_ERR", e);
			}
		}
		
		JLabel userPictureLabel = new JLabel(null, profilePicture, JLabel.CENTER);
		userPictureLabel.setBounds(new Rectangle(m_nOffset, 0, m_nProfilePictureWidth, m_nHeight));
		
		JLabel userDataLabel = new JLabel();
	    userDataLabel.setBounds(new Rectangle(m_nProfilePictureWidth+m_nOffset,0,m_nWidth-m_nProfilePictureWidth, m_nHeight));
		
	    
		userDataLabel.setText(
				"<html>" +
					"<h1>"+m_userDisplayed.getLastname()+" "+m_userDisplayed.getFirstName()+" profilja</h1>" +
					"<h2>id: #"+m_userDisplayed.getUserId()+", Felhasználónév: "+m_userDisplayed.getUsername()+"</h2>" +
					"<p>E-mail cím: " +m_userDisplayed.getEmail() + "</p>" +
					"<p>Regisztráció ideje: "+m_userDisplayed.getRegistered()+"</p>" +
					"<p>Feltöltött képek száma: "+m_userDisplayed.getPictureSum()+"</p>" +
					"<p><strong>Kommentek:</strong><br/>-</p>"+
					"<p><strong>Értékelések:</strong><br/>-</p>"+
				"</html>"
		);
	    
	    contentPanel = new JPanel();
	    contentPanel.setLayout(null);
	    
	    contentPanel.add(userDataLabel, null);
	    contentPanel.add(userPictureLabel, null);

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
