
/**
 * Az adatbázishoz kapcsolódáshoz tárolja a szükséges adatokat
 * @author Dobó László
 *
 */
public class DatabaseCreditental {
		
	private int m_nId;
	private String m_szHostname;
	private int m_nPort;
	private String m_szSid;
	private String m_szUsername;
	private String m_szPassword;
	private String m_szDescription;
	
	public DatabaseCreditental() {
		super();
		this.m_nId = 0;
		this.m_szHostname = null;
		this.m_nPort = 0;
		this.m_szSid = null;
		this.m_szUsername = null;
		this.m_szPassword = null;
		this.m_szDescription = null;
		
	}

	public DatabaseCreditental(int m_nId, String m_szHostname, int m_nPort,
			String m_szSid, String m_szUsername, String m_szPassword, String m_szDescription) {
		super();
		this.m_nId = m_nId;
		this.m_szHostname = m_szHostname;
		this.m_nPort = m_nPort;
		this.m_szSid = m_szSid;
		this.m_szUsername = m_szUsername;
		this.m_szPassword = m_szPassword;
		this.m_szDescription = m_szDescription;
	}
	
	public DatabaseCreditental(String m_szHostname, int m_nPort,
			String m_szSid, String m_szUsername, String m_szPassword, String m_szDescription) {
		super();
		this.m_szHostname = m_szHostname;
		this.m_nPort = m_nPort;
		this.m_szSid = m_szSid;
		this.m_szUsername = m_szUsername;
		this.m_szPassword = m_szPassword;
		this.m_szDescription = m_szDescription;
	}
	
	public String getHostname() {
		return m_szHostname;
	}
	public void setHostname(String m_szHostname) {
		this.m_szHostname = m_szHostname;
	}
	public int getPort() {
		return m_nPort;
	}
	public void setPort(int m_nPort) {
		this.m_nPort = m_nPort;
	}
	public String getSid() {
		return m_szSid;
	}
	public void setSid(String m_szSid) {
		this.m_szSid = m_szSid;
	}
	public String getUsername() {
		return m_szUsername;
	}
	public void setUsername(String m_szUsername) {
		this.m_szUsername = m_szUsername;
	}
	public String getPassword() {
		return m_szPassword;
	}
	public void setPassword(String m_szPassword) {
		this.m_szPassword = m_szPassword;
	}

	public String getDescription() {
		return m_szDescription;
	}

	public void setDescription(String m_szDescription) {
		this.m_szDescription = m_szDescription;
	}

	public int getId() {
		return m_nId;
	}

	public void setId(int m_nId) {
		this.m_nId = m_nId;
	}
	
	
	
	
	
}
