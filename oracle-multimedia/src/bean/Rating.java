package bean;

public class Rating {
	private int m_nPictureId;
	private int m_nUserId;
	private int m_nValue;
	
	public Rating() {
		super();
	}
	public Rating(int m_nPictureId, int m_nUserId, int m_nValue) {
		super();
		this.m_nPictureId = m_nPictureId;
		this.m_nUserId = m_nUserId;
		this.m_nValue = m_nValue;
	}
	public int getPictureId() {
		return m_nPictureId;
	}
	public void setPictureId(int m_nPictureId) {
		this.m_nPictureId = m_nPictureId;
	}
	public int getUserId() {
		return m_nUserId;
	}
	public void setUserId(int m_nUserId) {
		this.m_nUserId = m_nUserId;
	}
	public int getValue() {
		return m_nValue;
	}
	public void setValue(int m_nValue) {
		this.m_nValue = m_nValue;
	}
	
	@Override
	public String toString() {
		return "Rating [m_nPictureId=" + m_nPictureId + ", m_nUserId="
				+ m_nUserId + ", m_nValue=" + m_nValue + "]";
	}
	
	
	
	
}
