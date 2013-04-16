import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.Callable;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import bean.City;
import bean.Country;
import bean.Region;
import bean.User;


@SuppressWarnings("serial")
public class JPanelSignup extends JPanelLogin {
	
	protected JLabel m_jLabelEmail = new JLabel();
	protected JTextField m_jEmailField = new JTextField();
	
	protected JLabel m_jLabelFirstname = new JLabel();
	protected JTextField m_jFirstnameField = new JTextField();
	
	protected JLabel m_jLabelLastname = new JLabel();
	protected JTextField m_jLastnameField = new JTextField();
	
	protected JComboBox m_jComboCountry = new JComboBox();
	protected JComboBox m_jComboRegion = new JComboBox();
	protected JComboBox m_jComboCity = new JComboBox();
	
	protected JLabel m_jLabelCountry = new JLabel();
	protected JLabel m_jLabelRegion = new JLabel();
	protected JLabel m_jLabelCity = new JLabel();
	
	public JPanelSignup(IMFrame frame) {
		super(frame);
		m_szTitle = IMMessage.getString("MAIN_MENU_SIGNUP");
		m_szButtonConfirmTitle = "MAIN_MENU_SIGNUP";
		m_nHeight = 420;
	}
	
	
	@Override 
	protected void setupContentPane(){
		super.setupContentPane();
		
		m_jLabelEmail = IMFrame.createJLabel(m_jLabelEmail, new Rectangle(m_iLabelStartX, m_iLabelStartY+=(m_iFieldHeight+m_iDeltaY), m_iLabelWidth , m_iFieldHeight), 
				"LOGINDIAG_EMAIL", 'U', m_jLoginField);
		
		m_jLabelFirstname = IMFrame.createJLabel(m_jLabelFirstname, new Rectangle(m_iLabelStartX, m_iLabelStartY+=(m_iFieldHeight+m_iDeltaY), m_iLabelWidth, m_iFieldHeight), 
				"LOGINDIAG_FIRSTNAME", 'P', m_jPasswordField);
		
		m_jLabelLastname = IMFrame.createJLabel(m_jLabelLastname, new Rectangle(m_iLabelStartX, m_iLabelStartY+=(m_iFieldHeight+m_iDeltaY), m_iLabelWidth, m_iFieldHeight), 
				"LOGINDIAG_LASTNAME", 'P', m_jPasswordField);
		
		m_jLabelCountry = IMFrame.createJLabel(m_jLabelCountry,  new Rectangle(m_iLabelStartX, m_iLabelStartY+=(m_iFieldHeight+m_iDeltaY), m_iLabelWidth, m_iFieldHeight), 
				"COUNTRY", 'O', m_jComboCountry);
		
		m_jLabelRegion = IMFrame.createJLabel(m_jLabelRegion,  new Rectangle(m_iLabelStartX, m_iLabelStartY+=(m_iFieldHeight+m_iDeltaY), m_iLabelWidth, m_iFieldHeight), 
				"REGION", 'O', m_jComboRegion);
		
		m_jLabelCity = IMFrame.createJLabel(m_jLabelCity,  new Rectangle(m_iLabelStartX, m_iLabelStartY+=(m_iFieldHeight+m_iDeltaY), m_iLabelWidth, m_iFieldHeight), 
				"CITY", 'O', m_jComboCity);

		m_jEmailField.setBounds(
		        new Rectangle(m_iFieldStartX, m_iFieldStartY+=(m_iFieldHeight+m_iDeltaY), m_iFieldWidth , m_iFieldHeight));
		m_jEmailField.setToolTipText(IMMessage.getString("LOGINDIAG_USERNAME_FIELD_DESC"));


		m_jFirstnameField.setBounds(
		        new Rectangle(m_iFieldStartX, m_iFieldStartY+=(m_iFieldHeight+m_iDeltaY), m_iFieldWidth, m_iFieldHeight));
		m_jFirstnameField.setToolTipText(IMMessage.getString("LOGINDIAG_PASSWD_FIELD_DESC"));
		
		m_jLastnameField.setBounds(
		        new Rectangle(m_iFieldStartX, m_iFieldStartY+=(m_iFieldHeight+m_iDeltaY), m_iFieldWidth, m_iFieldHeight));
		m_jLastnameField.setToolTipText(IMMessage.getString("LOGINDIAG_PASSWD_FIELD_DESC"));
		

		m_jComboCountry = IMFrame.createComboBox(m_jComboCountry, true, 0, new Rectangle(m_iFieldStartX, m_iFieldStartY+=(m_iFieldHeight+m_iDeltaY), m_iFieldWidth, m_iFieldHeight), 
				m_jFrameOwner.getcountryAll(), new Callable<Void>() {
			   public Void call() {
			        countryListener();
					return null;
			   }
		});
		
		m_jComboRegion = IMFrame.createComboBox(m_jComboRegion, false, 0, new Rectangle(m_iFieldStartX, m_iFieldStartY+=(m_iFieldHeight+m_iDeltaY), m_iFieldWidth, m_iFieldHeight), 
				m_jFrameOwner.getregionAll(), new Callable<Void>() {
			   public Void call() {
			        regionListener();
					return null;
			   }
		});
		
		m_jComboCity = IMFrame.createComboBox(m_jComboCity, false, 0, new Rectangle(m_iFieldStartX, m_iFieldStartY+=(m_iFieldHeight+m_iDeltaY), m_iFieldWidth, m_iFieldHeight), 
				m_jFrameOwner.getcityAll(), new Callable<Void>() {
			   public Void call() {
			        cityListener();
					return null;
			   }
		});
		
		
	

	    contentPanel.add(m_jEmailField, null);
	    contentPanel.add(m_jFirstnameField, null);
	    contentPanel.add(m_jLastnameField, null);
		contentPanel.add(m_jComboCountry, null);
		contentPanel.add(m_jComboRegion, null);
		contentPanel.add(m_jComboCity, null);
	    
	    contentPanel.add(m_jLabelFirstname, null);
	    contentPanel.add(m_jLabelLastname, null);
	    contentPanel.add(m_jLabelEmail, null);
	    contentPanel.add(m_jLabelCountry,null);
		contentPanel.add(m_jLabelCity,null);
		contentPanel.add(m_jLabelRegion,null);

	} 
	
	@Override
	protected void setupButton()
	{
	    super.setupButton();
	}
	
	protected void cityListener() {
		if ((m_jComboCity.getSelectedIndex()!=0) && (m_jComboCity.isEnabled()==true)){
			m_jComboRegion.setEnabled(false);
		}
		if ((m_jComboCity.getSelectedIndex()==0) && (m_jComboCity.isEnabled()==true)){
			m_jComboRegion.setEnabled(true);
		}
	}


	protected void regionListener() {
		checkCombo(m_jComboRegion, m_jComboCity, m_jFrameOwner.getcityAll());
		
		if (m_jComboRegion.getSelectedIndex()!=0){
			m_jComboCountry.setEnabled(false);
			m_jComboCity.setEnabled(true);
		} else {
			m_jComboCountry.setEnabled(true);
			m_jComboCity.setEnabled(false);
		}
	}


	protected void countryListener() {
		checkCombo(m_jComboCountry, m_jComboRegion, m_jFrameOwner.getregionAll());
		
		if (m_jComboCountry.getSelectedIndex()!=0){
			m_jComboRegion.setEnabled(true);
		} else {
			m_jComboRegion.setEnabled(false);
		}
	}
	
	private void checkCombo(JComboBox active, JComboBox children, ArrayList<?> all){
		int selected = active.getSelectedIndex();
		
		
		if ((selected!=0) && (active.isEnabled())){
			children.removeAllItems();
			Object o = active.getItemAt(selected);
			
			children.addItem(IMMessage.getString("SELECT"));
				
				if (o instanceof Country ){
					Iterator it = all.iterator();
					while (it.hasNext()){
						Region r = (Region) it.next();
						if (((Country) o).getCountryId()==r.getCountryId()){
							children.addItem(r);
						}	
					}
				} else if (o instanceof Region){
					Iterator it = all.iterator();
					while (it.hasNext()){
						City c = (City) it.next();
						if (((Region) o).getRegionId()==c.getRegionId()){
							children.addItem(c);
						}	
					}
				}	
				
			
			children.validate();
			SwingUtilities.updateComponentTreeUI(this);
		}
	}


	@Override
	public void confirm(){
		if (
				m_jLoginField.getText().equals("") ||
				m_jPasswordField.getPassword().equals("") ||
				m_jEmailField.getText().equals("") ||
				m_jLastnameField.getText().equals("") ||
				m_jFirstnameField.getText().equals("") || 
				m_jComboCity.getSelectedIndex()==0
				 ){
				  new IMMessage(IMConstants.ERROR, "EMPTY_FIELD", new Exception());
			  } else {
				  User user = new User(0, m_jFirstnameField.getText(), m_jLastnameField.getText(), new String(m_jPasswordField.getPassword()), 
						  m_jEmailField.getText(), null, 0, null, null, m_jLoginField.getText(), ((City)m_jComboCity.getSelectedItem()).getCityId());
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
