package bean;

public class City {
	private int m_nCityId;
	private String m_szName;
	private int m_nRegionId;

	public City() {
		super();
	}
	
	public City(int m_nCityId, String m_szName, int m_nRegionId) {
		super();
		this.m_nCityId = m_nCityId;
		this.m_szName = m_szName;
		this.m_nRegionId = m_nRegionId;
	}
	
	public int getCityId() {
		return m_nCityId;
	}
	public void setCityId(int m_nCityId) {
		this.m_nCityId = m_nCityId;
	}
	public String getName() {
		return m_szName;
	}
	public void setName(String m_szName) {
		this.m_szName = m_szName;
	}
	public int getRegionId() {
		return m_nRegionId;
	}
	public void setRegionId(int m_nRegionId) {
		this.m_nRegionId = m_nRegionId;
	}
	
	@Override
	public String toString() {
		return m_szName;
	}
	
	
	
	
}
