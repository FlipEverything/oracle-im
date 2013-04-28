package bean;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;


import oracle.ord.im.OrdImage;


public class Picture {
	private int m_nPictureId;
	private String m_szPictureName;
	private Date m_dateUploadTime;
	private OrdImage m_imagePicture;
	private OrdImage m_imagePictureThumbnail;
	private int m_nCityId;
	private int m_nAlbumId;
	private ArrayList<Category> m_arrayCategories;
	private ArrayList<Keyword> m_arrayKeywords;
	private ArrayList<Comment> m_arrayComments;
	private ArrayList<Rating> m_arrayRating;
	
	public Picture(int m_nPictureId, String m_szPictureName,
			Date m_dateUploadTime,
			OrdImage m_imagePicture, OrdImage m_imagePictureThumbnail,
			int m_nCityId, int m_nAlbumId,
			ArrayList<Category> m_arrayCategories,
			ArrayList<Keyword> m_arrayKeywords,
			ArrayList<Comment> m_arrayComments, 
			ArrayList<Rating> m_arrayRating) {
		super();
		this.m_nPictureId = m_nPictureId;
		this.m_szPictureName = m_szPictureName;
		this.m_dateUploadTime = m_dateUploadTime;
		this.m_imagePicture = m_imagePicture;
		this.m_imagePictureThumbnail = m_imagePictureThumbnail;
		this.m_nCityId = m_nCityId;
		this.m_nAlbumId = m_nAlbumId;
		this.m_arrayCategories = m_arrayCategories;
		this.m_arrayKeywords = m_arrayKeywords;
		this.m_arrayComments = m_arrayComments;
		this.m_arrayRating = m_arrayRating;
	}

	public Picture() {
		super();

	}

	public int getPictureId() {
		return m_nPictureId;
	}

	public void setPictureId(int m_nPictureId) {
		this.m_nPictureId = m_nPictureId;
	}

	public String getPictureName() {
		return m_szPictureName;
	}

	public void setPictureName(String m_szPictureName) {
		this.m_szPictureName = m_szPictureName;
	}

	public Date getUploadTime() {
		return m_dateUploadTime;
	}

	public void setUploadTime(Date m_dateUploadTime) {
		this.m_dateUploadTime = m_dateUploadTime;
	}

	public OrdImage getPicture() {
		return m_imagePicture;
	}

	public void setPicture(OrdImage m_imagePicture) {
		this.m_imagePicture = m_imagePicture;
	}

	public OrdImage getPictureThumbnail() {
		return m_imagePictureThumbnail;
	}

	public void setPictureThumbnail(OrdImage m_imagePictureThumbnail) {
		this.m_imagePictureThumbnail = m_imagePictureThumbnail;
	}

	public int getCityId() {
		return m_nCityId;
	}

	public void setCityId(int m_nCityId) {
		this.m_nCityId = m_nCityId;
	}

	public int getAlbumId() {
		return m_nAlbumId;
	}

	public void setAlbumId(int m_nAlbumId) {
		this.m_nAlbumId = m_nAlbumId;
	}

	public ArrayList<Category> getCategories() {
		return m_arrayCategories;
	}

	public void setCategories(ArrayList<Category> m_arrayCategories) {
		this.m_arrayCategories = m_arrayCategories;
	}

	public ArrayList<Keyword> getKeywords() {
		return m_arrayKeywords;
	}

	public void setKeywords(ArrayList<Keyword> m_arrayKeywords) {
		this.m_arrayKeywords = m_arrayKeywords;
	}

	public ArrayList<Comment> getComments() {
		return m_arrayComments;
	}

	public void setComments(ArrayList<Comment> m_arrayComments) {
		this.m_arrayComments = m_arrayComments;
	}


	public ArrayList<Rating> getRating() {
		return m_arrayRating;
	}

	public void setRating(ArrayList<Rating> m_arrayRating) {
		this.m_arrayRating = m_arrayRating;
	}

	@Override
	public String toString() {
		return "Picture [m_nPictureId=" + m_nPictureId + ", m_szPictureName="
				+ m_szPictureName + ", m_dateUploadTime=" + m_dateUploadTime
				+ ", m_imagePicture=" + m_imagePicture
				+ ", m_imagePictureThumbnail=" + m_imagePictureThumbnail
				+ ", m_nCityId=" + m_nCityId + ", m_nAlbumId=" + m_nAlbumId
				+ ", m_arrayCategories=" + m_arrayCategories
				+ ", m_arrayKeywords=" + m_arrayKeywords + ", m_arrayComments="
				+ m_arrayComments
				+ ", m_nRating=" + m_arrayRating + "]";
	}
	
	public double getRatingValue(){
		double value = 0.0;
		
		Iterator<Rating> it = getRating().iterator();
		while (it.hasNext()){
			value += ((Rating)it.next()).getValue();
		}
		
		value /= getRating().size();
		
		return value;
	}
	
	
	
	
}
