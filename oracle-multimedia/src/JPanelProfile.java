


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.concurrent.Callable;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import bean.City;
import bean.Country;
import bean.Region;
import bean.User;



@SuppressWarnings("serial")
public class JPanelProfile extends JPanel implements IMConstants{

	  
	protected IMFrame m_jFrameOwner = null;
	protected User m_userDisplayed = null;
	protected JPanel contentPanel;
	
	protected String m_szTitle;	
	protected int m_nWidth = 600;
	protected int m_nHeight = 300;
	protected int m_nOffset = 30;
	
	
	public JPanelProfile(IMFrame frame, User user){
		this.m_jFrameOwner = frame;
		this.m_szTitle = IMMessage.getString("MAIN_MENU_PROFILE")+": "+user.getUsername();
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
	  protected void setupButton()
	  {

	  }

	  /**
	   * Draws the content pane.
	   */
	  protected void setupContentPane() 
	  {
		  
		JLabel userPictureLabel = IMImage.showPicture(m_userDisplayed.getProfilePictureThumb(), new Rectangle(m_nOffset, 0, THUMB_WIDTH, m_nHeight), m_userDisplayed.getUsername());
		userPictureLabel.addMouseListener(new MouseListener() {			
			@Override
			public void mouseReleased(MouseEvent e) {
			}		
			@Override
			public void mousePressed(MouseEvent e) {
			}		
			@Override
			public void mouseExited(MouseEvent e) {
			}		
			@Override
			public void mouseEntered(MouseEvent e) {	
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				new JDialogPicturePopup(m_jFrameOwner, m_userDisplayed.getProfilePicture());
			}
		});
		
		JLabel userDataLabel = new JLabel();
	    userDataLabel.setBounds(new Rectangle(THUMB_WIDTH+m_nOffset*2,0,m_nWidth-THUMB_WIDTH-m_nOffset*2, m_nHeight));
		
	    
	    City c = null;
		Iterator<City> it = (m_jFrameOwner.getcityAll()).iterator();
		while (it.hasNext()){
			c = it.next();
			if (m_userDisplayed.getCityId()==c.getCityId()){
				break;
			}
		}
		
		Region r = null;
		Iterator<Region> it1 = (m_jFrameOwner.getregionAll()).iterator();
		while (it.hasNext()){
			r = it1.next();
			if (c.getRegionId()==r.getRegionId()){
				break;
			}
		}
		
		Country cou = null;
		Iterator<Country> it2 = (m_jFrameOwner.getcountryAll()).iterator();
		while (it.hasNext()){
			cou = it2.next();
			if (r.getCountryId()==cou.getCountryId()){
				break;
			}
		}
	    
		userDataLabel.setText(
				"<html>" +
					"<h1>"+m_userDisplayed.getLastname()+" "+m_userDisplayed.getFirstName()+" profilja</h1>" +
					"<h2>id: #"+m_userDisplayed.getUserId()+", Felhasználónév: "+m_userDisplayed.getUsername()+"</h2>" +
					"<p>"+cou.getName()+", "+r.getName()+", "+c.getName()+"</p>"+
					"<p>E-mail cím: " +m_userDisplayed.getEmail() + "</p>" +
					"<p>Regisztráció ideje: "+m_userDisplayed.getRegistered()+"</p>" +
					"<p>Feltöltött képek száma: "+m_userDisplayed.getPictureSum()+"</p>" +
					"<br/><br/>"+
					"<p><strong>Kommentek:</strong><br/>-</p>"+
					"<br/><br/>"+
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
