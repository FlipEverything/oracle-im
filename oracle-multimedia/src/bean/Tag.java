package bean;

public class Tag {
	private int m_nUserId;
	private float m_fpPixelX;
	private float m_fpPixelY;
	
	public Tag() {
		super();
	
	}

	public Tag(int m_nUserId, float m_fpPixelX, float m_fpPixelY) {
		super();
		this.m_nUserId = m_nUserId;
		this.m_fpPixelX = m_fpPixelX;
		this.m_fpPixelY = m_fpPixelY;
	}

	public int getUserId() {
		return m_nUserId;
	}

	public void setUserId(int m_nUserId) {
		this.m_nUserId = m_nUserId;
	}

	public float getPixelX() {
		return m_fpPixelX;
	}

	public void setPixelX(float m_fpPixelX) {
		this.m_fpPixelX = m_fpPixelX;
	}

	public float getPixelY() {
		return m_fpPixelY;
	}

	public void setPixelY(float m_fpPixelY) {
		this.m_fpPixelY = m_fpPixelY;
	}

	@Override
	public String toString() {
		return "Tag [m_nUserId=" + m_nUserId + ", m_fpPixelX=" + m_fpPixelX
				+ ", m_fpPixelY=" + m_fpPixelY + "]";
	}
	
	
}
