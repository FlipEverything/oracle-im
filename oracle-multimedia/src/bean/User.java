package bean;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
	private OrdImage m_imProfilePictureThumb;
	private String m_szUsername;
	private int m_nCityId;
	
	public User(){
		super();
		this.m_nUserId = 0;
		this.m_szFirstName = null;
		this.m_szLastname = null;
		this.m_szPassword = null;
		this.m_szEmail = null;
		this.m_dRegistered = null;
		this.m_nPictureSum = 0;
		this.m_imProfilePicture = null;
		this.m_imProfilePictureThumb = null;
		this.m_szUsername = null;
		this.m_nCityId = 0;
	}
	
	public User(int m_nUserId, String m_szFirstName, String m_szLastname,
			String m_szPassword, String m_szEmail, Date m_dRegistered,
			int m_nPictureSum, OrdImage m_imProfilePicture, OrdImage m_imProfilePictureThumb,
			String m_szUsername, int m_nCityId) {
		super();
		this.m_nUserId = m_nUserId;
		this.m_szFirstName = m_szFirstName;
		this.m_szLastname = m_szLastname;
		this.m_szPassword = hashPassword(m_szPassword);
		this.m_szEmail = m_szEmail;
		this.m_dRegistered = m_dRegistered;
		this.m_nPictureSum = m_nPictureSum;
		this.m_imProfilePicture = m_imProfilePicture;
		this.m_imProfilePictureThumb = m_imProfilePictureThumb;
		this.m_szUsername = m_szUsername;
		this.m_nCityId = m_nCityId;
	}
	
	

	public User(String m_szPassword, String m_szUsername) {
		this();
		this.m_szPassword = hashPassword(m_szPassword);
		this.m_szUsername = m_szUsername;
	}
	
	public String hashPassword(String password){
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(password.getBytes());
			
			byte byteData[] = md.digest();
			
			StringBuffer hexString = new StringBuffer();
	    	for (int i=0;i<byteData.length;i++) {
	    		String hex=Integer.toHexString(0xff & byteData[i]);
	   	     	if(hex.length()==1) hexString.append('0');
	   	     	hexString.append(hex);
	    	}
	    	password = hexString.toString();
		} catch (NoSuchAlgorithmException e) {
			
		}
		
		return password;
	}



	public int getUserId() {
		return m_nUserId;
	}

	public void setUserId(int m_nUserId) {
		this.m_nUserId = m_nUserId;
	}

	public String getFirstName() {
		return m_szFirstName;
	}

	public void setFirstName(String m_szFirstName) {
		this.m_szFirstName = m_szFirstName;
	}

	public String getLastname() {
		return m_szLastname;
	}

	public void setLastname(String m_szLastname) {
		this.m_szLastname = m_szLastname;
	}

	public String getPassword() {
		return m_szPassword;
	}

	public void setPassword(String m_szPassword) {
		this.m_szPassword = hashPassword(m_szPassword);
	}

	public String getEmail() {
		return m_szEmail;
	}

	public void setEmail(String m_szEmail) {
		this.m_szEmail = m_szEmail;
	}

	public Date getRegistered() {
		return m_dRegistered;
	}

	public void setRegistered(Date m_dRegistered) {
		this.m_dRegistered = m_dRegistered;
	}

	public int getPictureSum() {
		return m_nPictureSum;
	}

	public void setPictureSum(int m_nPictureSum) {
		this.m_nPictureSum = m_nPictureSum;
	}

	public OrdImage getProfilePicture() {
		return m_imProfilePicture;
	}

	public void setProfilePicture(OrdImage m_imProfilePicture) {
		this.m_imProfilePicture = m_imProfilePicture;
	}
	
	public OrdImage getProfilePictureThumb() {
		return m_imProfilePictureThumb;
	}

	public void setProfilePictureThumb(OrdImage m_imProfilePictureThumb) {
		this.m_imProfilePictureThumb = m_imProfilePictureThumb;
	}

	public String getUsername() {
		return m_szUsername;
	}

	public void setUsername(String m_szUsername) {
		this.m_szUsername = m_szUsername;
	}

	public int getCityId() {
		return m_nCityId;
	}

	public void setCityId(int m_nCityId) {
		this.m_nCityId = m_nCityId;
	}

	@Override
	public String toString() {
		return m_szFirstName + " " + m_szLastname;
	}
	
	
	
}
