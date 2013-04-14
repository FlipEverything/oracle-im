import java.awt.Rectangle;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


@SuppressWarnings("serial")
public class JPanelSignup extends JPanelLogin {
	
	protected JLabel m_jLabelEmail = new JLabel();
	protected JTextField m_jEmailField = new JTextField();
	
	protected JLabel m_jLabelFirstname = new JLabel();
	protected JTextField m_jFirstnameField = new JTextField();
	
	protected JLabel m_jLabelLastname = new JLabel();
	protected JTextField m_jLastnameField = new JTextField();
	
	public JPanelSignup(IMFrame frame) {
		super(frame);
		m_szTitle = IMMessage.getString("MAIN_MENU_SIGNUP");
		m_szButtonConfirmTitle = "MAIN_MENU_SIGNUP";
		m_nHeight = 300;
	}
	
	
	@Override
	public void setupContentPane(){
		super.setupContentPane();
		
		m_jLabelEmail = IMFrame.createJLabel(m_jLabelEmail, new Rectangle(m_iLabelStartX, m_iLabelStartY+=(m_iFieldHeight+m_iDeltaY), m_iLabelWidth , m_iFieldHeight), 
				"LOGINDIAG_EMAIL", 'U', m_jLoginField);
		
		m_jLabelFirstname = IMFrame.createJLabel(m_jLabelFirstname, new Rectangle(m_iLabelStartX, m_iLabelStartY+=(m_iFieldHeight+m_iDeltaY), m_iLabelWidth, m_iFieldHeight), 
				"LOGINDIAG_FIRSTNAME", 'P', m_jPasswordField);
		
		m_jLabelLastname = IMFrame.createJLabel(m_jLabelLastname, new Rectangle(m_iLabelStartX, m_iLabelStartY+=(m_iFieldHeight+m_iDeltaY), m_iLabelWidth, m_iFieldHeight), 
				"LOGINDIAG_LASTNAME", 'P', m_jPasswordField);

		m_jEmailField.setBounds(
		        new Rectangle(m_iFieldStartX, m_iFieldStartY+=(m_iFieldHeight+m_iDeltaY), m_iFieldWidth , m_iFieldHeight));
		m_jEmailField.setToolTipText(IMMessage.getString("LOGINDIAG_USERNAME_FIELD_DESC"));


		m_jFirstnameField.setBounds(
		        new Rectangle(m_iFieldStartX, m_iFieldStartY+=(m_iFieldHeight+m_iDeltaY), m_iFieldWidth, m_iFieldHeight));
		m_jFirstnameField.setToolTipText(IMMessage.getString("LOGINDIAG_PASSWD_FIELD_DESC"));
		
		m_jLastnameField.setBounds(
		        new Rectangle(m_iFieldStartX, m_iFieldStartY+=(m_iFieldHeight+m_iDeltaY), m_iFieldWidth, m_iFieldHeight));
		m_jLastnameField.setToolTipText(IMMessage.getString("LOGINDIAG_PASSWD_FIELD_DESC"));

	    contentPanel.add(m_jEmailField, null);
	    contentPanel.add(m_jFirstnameField, null);
	    contentPanel.add(m_jLastnameField, null);
	    
	    contentPanel.add(m_jLabelFirstname, null);
	    contentPanel.add(m_jLabelLastname, null);
	    contentPanel.add(m_jLabelEmail, null);

	} 
	
	@Override
	public void confirm(){
		if (
				m_jLoginField.getText().equals("") ||
				m_jPasswordField.getPassword().equals("") ||
				m_jEmailField.getText().equals("") ||
				m_jLastnameField.getText().equals("") ||
				m_jFirstnameField.getText().equals("")
				 ){
				  new IMMessage(IMConstants.ERROR, "EMPTY_FIELD", new Exception());
			  } else {
				  User user = new User(0, m_jFirstnameField.getText(), m_jLastnameField.getText(), new String(m_jPasswordField.getPassword()), 
						  m_jEmailField.getText(), null, 0, null, m_jLoginField.getText(), 0);
				  IMQuery q = new IMQuery();
				  user = q.userSignup(user);
				  
				  
				  if ((user!=null) && (user.getUserId()!=0)){
					  new IMMessage(IMConstants.WARNING, "SIGNUP_SUCCESS");
					  m_jFrameOwner.showLoginPanel();
				  } else {
					  new IMMessage(IMConstants.ERROR, "SIGNUP_ERROR", new Exception());
				  }
				  
		      }
	}
	
	@Override
	public void cancel(){
		m_jFrameOwner.showLoginPanel();
	}

}
