package bean;

public class Category {
	private int m_nCategoryId;
	private String m_szName;
	
	public Category() {
		super();
	}
	public Category(int m_nCategoryId, String m_szName) {
		super();
		this.m_nCategoryId = m_nCategoryId;
		this.m_szName = m_szName;
	}
	public int getCategoryId() {
		return m_nCategoryId;
	}
	public void setCategoryId(int m_nCategoryId) {
		this.m_nCategoryId = m_nCategoryId;
	}
	public String getName() {
		return m_szName;
	}
	public void setName(String m_szName) {
		this.m_szName = m_szName;
	}
	@Override
	public String toString() {
		return m_szName;
	}
	
	
	
}
