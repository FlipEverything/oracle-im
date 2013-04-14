package bean;

import java.sql.Date;

import oracle.ord.im.OrdImage;

public class User {
	
	private int m_nUserId;
	private String m_szFirstName;
	private String m_szLastname;
	private String m_szPassword;
	private String m_szEmail;
	private Date m_dRegistered;
	private int m_nPictureSum;
	private OrdImage m_imProfilePicture;
	private String m_szUsername;
	private int m_nCityId;
	
	public User(int m_nUserId, String m_szFirstName, String m_szLastname,
			String m_szPassword, String m_szEmail, Date m_dRegistered,
			int m_nPictureSum, OrdImage m_imProfilePicture,
			String m_szUsername, int m_nCityId) {
		super();
		this.m_nUserId = m_nUserId;
		this.m_szFirstName = m_szFirstName;
		this.m_szLastname = m_szLastname;
		this.m_szPassword = m_szPassword;
		this.m_szEmail = m_szEmail;
		this.m_dRegistered = m_dRegistered;
		this.m_nPictureSum = m_nPictureSum;
		this.m_imProfilePicture = m_imProfilePicture;
		this.m_szUsername = m_szUsername;
		this.m_nCityId = m_nCityId;
	}

	public int getM_nUserId() {
		return m_nUserId;
	}

	public void setM_nUserId(int m_nUserId) {
		this.m_nUserId = m_nUserId;
	}

	public String getM_szFirstName() {
		return m_szFirstName;
	}

	public void setM_szFirstName(String m_szFirstName) {
		this.m_szFirstName = m_szFirstName;
	}

	public String getM_szLastname() {
		return m_szLastname;
	}

	public void setM_szLastname(String m_szLastname) {
		this.m_szLastname = m_szLastname;
	}

	public String getM_szPassword() {
		return m_szPassword;
	}

	public void setM_szPassword(String m_szPassword) {
		this.m_szPassword = m_szPassword;
	}

	public String getM_szEmail() {
		return m_szEmail;
	}

	public void setM_szEmail(String m_szEmail) {
		this.m_szEmail = m_szEmail;
	}

	public Date getM_dRegistered() {
		return m_dRegistered;
	}

	public void setM_dRegistered(Date m_dRegistered) {
		this.m_dRegistered = m_dRegistered;
	}

	public int getM_nPictureSum() {
		return m_nPictureSum;
	}

	public void setM_nPictureSum(int m_nPictureSum) {
		this.m_nPictureSum = m_nPictureSum;
	}

	public OrdImage getM_imProfilePicture() {
		return m_imProfilePicture;
	}

	public void setM_imProfilePicture(OrdImage m_imProfilePicture) {
		this.m_imProfilePicture = m_imProfilePicture;
	}

	public String getM_szUsername() {
		return m_szUsername;
	}

	public void setM_szUsername(String m_szUsername) {
		this.m_szUsername = m_szUsername;
	}

	public int getM_nCityId() {
		return m_nCityId;
	}

	public void setM_nCityId(int m_nCityId) {
		this.m_nCityId = m_nCityId;
	}
	
	
	
}
