package bean;

public class Country {
	private int m_nCountryId;
	private String m_szName;
	
	
	public Country(int m_nCountryId, String m_szName) {
		super();
		this.m_nCountryId = m_nCountryId;
		this.m_szName = m_szName;
	}
	public Country() {
		super();
	}
	public int getCountryId() {
		return m_nCountryId;
	}
	public void setCountryId(int m_nCountryId) {
		this.m_nCountryId = m_nCountryId;
	}
	public String getName() {
		return m_szName;
	}
	public void setName(String m_szName) {
		this.m_szName = m_szName;
	}
	
	@Override
	public String toString(){
		return this.m_szName;
	}
	
	
	
}
