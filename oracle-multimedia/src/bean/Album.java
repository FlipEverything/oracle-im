package bean;

public class Album {
	private int m_nAlbumId;
	private int m_nUserId;
	private String m_szName;
	private boolean m_bPublic;
	
	
	public Album() {
		super();
	}
	
	public Album(int m_nAlbumId, int m_nUserId, String m_szName,
			boolean m_bPublic) {
		super();
		this.m_nAlbumId = m_nAlbumId;
		this.m_nUserId = m_nUserId;
		this.m_szName = m_szName;
		this.m_bPublic = m_bPublic;
	}
	
	public int getAlbumId() {
		return m_nAlbumId;
	}
	public void setAlbumId(int m_nAlbumId) {
		this.m_nAlbumId = m_nAlbumId;
	}
	public int getUserId() {
		return m_nUserId;
	}
	public void setUserId(int m_nUserId) {
		this.m_nUserId = m_nUserId;
	}
	public String getName() {
		return m_szName;
	}
	public void setName(String m_szName) {
		this.m_szName = m_szName;
	}
	public boolean isPublic() {
		return m_bPublic;
	}
	public void setPublic(boolean m_bPublic) {
		this.m_bPublic = m_bPublic;
	}

	@Override
	public String toString() {
		return m_szName;
	}
	
	
}
