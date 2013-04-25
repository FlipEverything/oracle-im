package bean;

public class Keyword {
	private int m_nKeywordId;
	private String m_szName;
	private boolean m_bSelected;
	
	public Keyword(int m_nKeywordId, String m_szName) {
		super();
		this.m_nKeywordId = m_nKeywordId;
		this.m_szName = m_szName;
		this.m_bSelected = false;
	}

	public Keyword() {
		super();

	}

	public int getKeywordId() {
		return m_nKeywordId;
	}

	public void setKeywordId(int m_nKeywordId) {
		this.m_nKeywordId = m_nKeywordId;
	}

	public String getName() {
		return m_szName;
	}

	public void setName(String m_szName) {
		this.m_szName = m_szName;
	}
	
	

	public boolean isSelected() {
		return m_bSelected;
	}

	public void setSelected(boolean m_bSelected) {
		this.m_bSelected = m_bSelected;
	}

	@Override
	public String toString() {
		return "Keyword [m_szName=" + m_szName + "]";
	}
	
	
	
}
