package bean;

import java.sql.Date;

public class Comment {
	private int m_nCommentId;
	private String m_szCommentText;
	private Date m_dateCommentTime;
	private int m_nUserId;
	
	public Comment(int m_nCommentId, String m_szCommentText,
			Date m_dateCommentTime, int m_nUserId) {
		super();
		this.m_nCommentId = m_nCommentId;
		this.m_szCommentText = m_szCommentText;
		this.m_dateCommentTime = m_dateCommentTime;
		this.m_nUserId = m_nUserId;
	}
	public Comment() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getCommentId() {
		return m_nCommentId;
	}
	public void setCommentId(int m_nCommentId) {
		this.m_nCommentId = m_nCommentId;
	}
	public String getCommentText() {
		return m_szCommentText;
	}
	public void setCommentText(String m_szCommentText) {
		this.m_szCommentText = m_szCommentText;
	}
	public Date getCommentTime() {
		return m_dateCommentTime;
	}
	public void setCommentTime(Date m_dateCommentTime) {
		this.m_dateCommentTime = m_dateCommentTime;
	}
	public int getUserId() {
		return m_nUserId;
	}
	public void setUserId(int m_nUserId) {
		this.m_nUserId = m_nUserId;
	}

	@Override
	public String toString() {
		return "Comment [m_szCommentText=" + m_szCommentText
				+ ", m_dateCommentTime=" + m_dateCommentTime + "]";
	}
	
	
}
