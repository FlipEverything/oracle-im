package bean;

import java.sql.Date;

public class Comment {
	private int m_nCommentId;
	private String m_szCommentText;
	private Date m_dateCommentTime;
	private int m_nUserId;
	private int m_nPictureId;
	
	public Comment(int m_nCommentId, String m_szCommentText,
			Date m_dateCommentTime, int m_nUserId, int m_nPictureId) {
		super();
		this.m_nCommentId = m_nCommentId;
		this.m_szCommentText = m_szCommentText;
		this.m_dateCommentTime = m_dateCommentTime;
		this.m_nUserId = m_nUserId;
		this.m_nPictureId = m_nPictureId;
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

	
	public int getPictureId() {
		return m_nPictureId;
	}
	public void setPictureId(int m_nPictureId) {
		this.m_nPictureId = m_nPictureId;
	}
	@Override
	public String toString() {
		return  "[" + m_dateCommentTime + "] " + m_szCommentText + " (user#"+ m_nUserId +") (picture#"+m_nPictureId+")";
	}
	
	
}
