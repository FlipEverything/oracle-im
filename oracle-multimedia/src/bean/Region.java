package bean;

public class Region {
	private int m_nRegionId;
	private String m_szName;
	private int m_nCountryId;
	
	
	public Region(int m_nRegionId, String m_szName, int m_nCountryId) {
		super();
		this.m_nRegionId = m_nRegionId;
		this.m_szName = m_szName;
		this.m_nCountryId = m_nCountryId;
	}
	
	public Region() {
		super();
	}

	@Override
	public String toString() {
		return m_szName;
	}

	public int getRegionId() {
		return m_nRegionId;
	}

	public void setRegionId(int m_nRegionId) {
		this.m_nRegionId = m_nRegionId;
	}

	public String getName() {
		return m_szName;
	}

	public void setName(String m_szName) {
		this.m_szName = m_szName;
	}

	public int getCountryId() {
		return m_nCountryId;
	}

	public void setCountryId(int m_nCountryId) {
		this.m_nCountryId = m_nCountryId;
	}
	
	
	
	
	
	
}
