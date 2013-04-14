

@SuppressWarnings("serial")
public class JPanelSettings extends JPanelSignup implements IMConstants{

	public JPanelSettings(IMFrame frame) {
		super(frame);
		m_szTitle = IMMessage.getString("MAIN_MENU_SETTINGS");
		m_szButtonConfirmTitle = "MAIN_MENU_SIGNUP";
		m_nHeight = 2000;
	}
	
	
	
}
