


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JLabel;
import javax.swing.JPanel;
import bean.City;
import bean.Comment;
import bean.Country;
import bean.Rating;
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
		this.m_szTitle = IMMessage.getString("MAIN_MENU_PROFILE")+": "+ user.getUsername() + " "+ (user.getUserId()==frame.getUserActive().getUserId() ? IMMessage.getString("OWN") : "");
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
				if (m_userDisplayed.getProfilePicture()!=null){
					JDialogProfilePic p = new JDialogProfilePic(m_jFrameOwner, m_userDisplayed.getProfilePicture());
					p.setVisible(true);
				}
			}
		});
		
		JLabel userDataLabel = new JLabel();
	    
	    City c = m_jFrameOwner.selectCurrentCity(m_userDisplayed.getCityId());
	    Region r = m_jFrameOwner.selectCurrentRegion(c.getRegionId());
	    Country cou = m_jFrameOwner.selectCurrentCountry(r.getCountryId());

		userDataLabel.setText(
				"<html>" +
					"<h1>"+m_userDisplayed.getLastname()+" "+m_userDisplayed.getFirstName()+" profilja</h1>" +
					"<h2>id: #"+m_userDisplayed.getUserId()+", Felhasználónév: "+m_userDisplayed.getUsername()+"</h2>" +
					"<p>"+cou+", "+r+", "+c+"</p>"+
					"<p>E-mail cím: " +m_userDisplayed.getEmail() + "</p>" +
					"<p>Regisztráció ideje: "+m_userDisplayed.getRegistered()+"</p>" +
					"<p>Feltöltött képek száma: "+m_userDisplayed.getPictureSum()+"</p>" +
					"<br/><br/>"+
					"<p><strong>Kommentek:</strong><br/>"
		);
		
		IMQuery q = new IMQuery();
		ArrayList<Comment> commentArray = q.selectCommentForUser(m_userDisplayed);
		Iterator<Comment> it11 = commentArray.iterator();
		
		if (commentArray.size()==0){
			userDataLabel.setText(userDataLabel.getText()+IMMessage.getString("NO_COMMENT")+"<br/>");
		}
		while (it11.hasNext()){
			Comment com = it11.next();
			userDataLabel.setText(userDataLabel.getText()+com+"<br/>");
		}
		
		userDataLabel.setText(userDataLabel.getText()
				+"<br/><br/><p><strong>Értékelések:</strong><br/>");
		
		ArrayList<Rating> ratingArray = q.selectRatingForUsers(m_userDisplayed);
		Iterator<Rating> it111 = ratingArray.iterator();
		
		if (ratingArray.size()==0){
			userDataLabel.setText(userDataLabel.getText()+IMMessage.getString("NO_RATING")+"<br/>");
		}
		while (it111.hasNext()){
			Rating rat = it111.next();
			userDataLabel.setText(userDataLabel.getText()+rat+"<br/>");
		}
		
		userDataLabel.setText(userDataLabel.getText()+"</p>"+
					"<br/><br/>"+
				"</html>");
		
		
		userDataLabel.setBounds(new Rectangle(THUMB_WIDTH+m_nOffset*2,0,m_nWidth-THUMB_WIDTH-m_nOffset*2, (int) userDataLabel.getPreferredSize().getHeight()));
	    userDataLabel.validate();
	    
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
