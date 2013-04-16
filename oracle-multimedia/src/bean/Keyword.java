package bean;

public class Keyword {
	private int m_nKeywordId;
	private int m_szName;
	
	public Keyword(int m_nKeywordId, int m_szName) {
		super();
		this.m_nKeywordId = m_nKeywordId;
		this.m_szName = m_szName;
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

	public int getName() {
		return m_szName;
	}

	public void setName(int m_szName) {
		this.m_szName = m_szName;
	}

	@Override
	public String toString() {
		return "Keyword [m_szName=" + m_szName + "]";
	}
	
	
	
}
