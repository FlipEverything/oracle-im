import java.awt.Rectangle;
import java.util.concurrent.Callable;

import javax.swing.JButton;
import bean.City;
import bean.User;



@SuppressWarnings("serial")
public class JPanelSettings extends JPanelSignup implements IMConstants{
	
	JButton m_jButtonUpload = new JButton();
	User m_userDisplayed = null; 
	
	
	
	public JPanelSettings(IMFrame frame, User user) {
		super(frame);
		m_szTitle = IMMessage.getString("MAIN_MENU_SETTINGS");
		m_szButtonConfirmTitle = "SAVE";
		m_userDisplayed = user;

		m_jLoginField.setEditable(false);
	}
	
	@Override
	public void init(){
		super.init();
		setFields();
	}
	
	@Override
	protected void setupButton()
	  {
		super.setupButton();
		
		m_jButtonUpload = IMFrame.createButton(m_jButtonUpload, "UPLOAD_PROFILE_PIC", 'L', "upload", "LOGINDIAG_LOGIN_BUTTON_DESC", 
				new Rectangle(m_nLastButtonStart+=m_nButtonWidth, m_nHeight-m_nButtonHeight-30, m_nButtonWidth, m_nButtonHeight), new Callable<Void>() {
			   public Void call() {
			        uploadProfilePic();
					return null;
			   }
			});
	  }
	
	
	protected void uploadProfilePic() {
		IMLoadFile load = new IMLoadFile(m_jFrameOwner, IMMessage.getString("UPLOAD_PROFILE_PIC"), m_userDisplayed.getProfilePicture(), m_userDisplayed.getProfilePictureThumb(), m_userDisplayed);
		load.startUpload();
	}

	public void setFields(){
		m_jFirstnameField.setText(m_userDisplayed.getFirstName());
		m_jLastnameField.setText(m_userDisplayed.getLastname());
		m_jLoginField.setText(m_userDisplayed.getUsername());
		m_jEmailField.setText(m_userDisplayed.getEmail());
		m_jPasswordField.setText(new String());
		
		m_jFrameOwner.setPlaceCombos(m_jComboCountry, m_jComboRegion, m_jComboCity, m_userDisplayed);
	}
	
	
	@Override
	protected void setupContentPane(){
		super.setupContentPane();
		
		contentPanel.add(m_jButtonUpload, null);
	}

	  @Override
	  public void confirm()
	  {
		  if (
			    m_jLoginField.getText().equals("") ||
				m_jEmailField.getText().equals("") ||
				m_jLastnameField.getText().equals("") ||
				m_jFirstnameField.getText().equals("") || 
				m_jComboCity.getSelectedIndex()==0
			 ){
			  new IMMessage(IMConstants.ERROR, "EMPTY_FIELD");
		  } else {
			  m_userDisplayed.setCityId( ((City)m_jComboCity.getSelectedItem()).getCityId()  );
			  m_userDisplayed.setEmail(m_jEmailField.getText());
			  m_userDisplayed.setFirstName(m_jFirstnameField.getText());
			  m_userDisplayed.setLastname(m_jLastnameField.getText());
			  if (!new String(m_jPasswordField.getPassword()).equals("")){
				  m_userDisplayed.setPassword(new String(m_jPasswordField.getPassword()));
			  }
			  
			  
			  IMQuery q = new IMQuery();
			  boolean success = q.userUpdate(m_userDisplayed);
			  
			  if (success){
				  new IMMessage(IMConstants.WARNING, "MODIFY_SUCCESS");
			  } else {
				  new IMMessage(IMConstants.ERROR, "APP_ERR");
			  }
			  
	      }
	  }


	  @Override
	  public void cancel()
	  {
	    setFields();
	  }
	
}
