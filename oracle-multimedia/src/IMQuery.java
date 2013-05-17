import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import bean.Album;
import bean.Category;
import bean.City;
import bean.Comment;
import bean.Country;
import bean.Keyword;
import bean.Picture;
import bean.Rating;
import bean.Region;
import bean.User;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.OracleResultSet;
import oracle.jdbc.internal.OraclePreparedStatement;
import oracle.ord.im.OrdImage;



/**
 * The IMExampleQuery class retrieves the oe.product_information table,
 * and creates the display of the table.
 */
class IMQuery implements IMConstants
{

  private OracleConnection m_dbConn = null;
  private OraclePreparedStatement stmt = null;
  private OracleResultSet rs = null;
  
  public static final int TOP = 5;
  
  private static final String USER_LOGIN = "SELECT * FROM USERS WHERE username = ? AND password = ?";
  private static final String USER_SIGNUP = "INSERT INTO USERS (username, password, email, first_name, last_name, registered, city_id) VALUES (?,?,?,?,?,?,?)";
  private static final String USER_SIGNUP_GET_USER_ID = "SELECT USERS_INC.CURRVAL FROM DUAL";
  private static final String USER_UPDATE = "UPDATE USERS SET first_name = ?, last_name = ?, password = ?, email = ?, city_id = ? WHERE user_id = ?";
  
  private static final String ALL_COUNTRIES = "SELECT * FROM COUNTRIES ORDER BY name";
  private static final String ALL_REGIONS = "SELECT * FROM REGIONS ORDER BY name";
  private static final String ALL_CITIES = "SELECT * FROM CITIES ORDER BY name";
  
  private static final String CREATE_ALBUM = "INSERT INTO ALBUMS (user_id, name, is_public) VALUES (?,?,?)";
  private static final String EDIT_ALBUM = "update albums set name = ?, is_public = ? where album_id = ?";
  private static final String GET_ALBUMS_BY_USER = "SELECT * FROM ALBUMS WHERE USER_ID = ?";
  private static final String GET_PUBLIC_ALBUMS_BY_USER = "SELECT * FROM ALBUMS WHERE USER_ID = ? and is_public = 1";
  
  private static final String INIT_USER_PROFILE_PIC = "update users set profile_picture = ORDSYS.ORDImage.init() where user_id = ?";
  private static final String SELECT_USER_PROFILE_PIC = "select profile_picture from users where user_id = ? for update";
  private static final String UPDATE_USER_PROFILE_PIC = "update users set profile_picture = ? where user_id = ?";
  
  private static final String INIT_USER_PROFILE_PIC_THUMB = "update users set profile_picture_thumb = ORDSYS.ORDImage.init() where user_id = ?";
  private static final String SELECT_USER_PROFILE_PIC_THUMB = "select profile_picture_thumb from users where user_id = ? for update";
  private static final String UPDATE_USER_PROFILE_PIC_THUMB = "update users set profile_picture_thumb = ? where user_id = ?";
  
  private static final String INIT_PIC = "update pictures set picture = ORDSYS.ORDImage.init() where picture_id = ?";
  private static final String SELECT_PIC = "select picture from pictures where picture_id = ? for update";
  private static final String UPDATE_PIC = "update pictures set picture = ? where picture_id = ?";
  
  private static final String INIT_PIC_THUMB = "update pictures set picture_thumbnail = ORDSYS.ORDImage.init() where picture_id = ?";
  private static final String SELECT_PIC_THUMB = "select picture_thumbnail from pictures where picture_id = ? for update";
  private static final String UPDATE_PIC_THUMB = "update pictures set picture_thumbnail = ? where picture_id = ?";
  
  private static final String INSERT_PICTURE = "insert into pictures (picture_name, upload_time, city_id, album_id) VALUES (?,?,?,?)";
  private static final String INSERT_PICTURE_GET_PICTURE_ID = "select pictures_inc.currval from dual";
  private static final String UPDATE_PICTURE = "update pictures set picture_name=?, city_id=?, album_id=? where picture_id=?";
  
  private static final String SELECT_PICTURES_FROM_ALBUM = "select * from pictures where album_id = ?";
  private static final String SELECT_PICTURE_KEYWORDS = "select distinct keywords.keyword_id, keywords.name from keywords, picture_to_keyword  where keywords.keyword_id = picture_to_keyword.keyword_id AND picture_to_keyword.picture_id = ?";
  private static final String SELECT_PICTURE_CATEGORIES = "select distinct categories.category_id, categories.name from categories, picture_to_category  where categories.category_id = picture_to_category.category_id AND picture_to_category.picture_id = ?";
  private static final String SELECT_PICTURE_COMMENTS = "select * from comments where picture_id = ?";
  private static final String SELECT_PICTURE_RATINGS = "select * from ratings where picture_id = ?";
  private static final String SELECT_USER_RATINGS = "select * from ratings where user_id = ?";
  
  private static final String SELECT_TOP_RATED_PICTURES = "select * from "+
		  	"(select (sum(ratings.value)/count(ratings.value)) as rating_value, pictures.picture_id as group_picture_id "+
		  	"from pictures, ratings where ratings.picture_id = pictures.picture_id "+ 
		  	"group by pictures.picture_id order by rating_value DESC), pictures, albums where albums.album_id = pictures.album_id AND " +
		  	"albums.is_public = 1 AND pictures.picture_id = group_picture_id AND rownum <= "+TOP;
  
  private static final String SELECT_TOP_RATE_COUNT_PICTURES = "select * from "+
		  	"(select count(ratings.value) as rating_value, pictures.picture_id as group_picture_id "+
		  	"from pictures, ratings where ratings.picture_id = pictures.picture_id "+ 
		  	"group by pictures.picture_id order by rating_value DESC), pictures, albums where albums.album_id = pictures.album_id AND " +
		  	"albums.is_public = 1 AND pictures.picture_id = group_picture_id AND rownum <= "+TOP;
  
  private static final String SELECT_TOP_COMMENT_COUNT_PICTURES = "select * from "+
		  	"(select count(comments.comment_id) as rating_value, pictures.picture_id as group_picture_id "+
		  	"from pictures, comments where comments.picture_id = pictures.picture_id "+ 
		  	"group by pictures.picture_id order by rating_value DESC), pictures, albums where albums.album_id = pictures.album_id AND " +
		  	"albums.is_public = 1 AND pictures.picture_id = group_picture_id AND rownum <= "+TOP;
  
  private static final String SELECT_NEW_PICTURES = "select * from "+
		  	"pictures, albums where albums.album_id = pictures.album_id AND " +
		  	"albums.is_public = 1 AND rownum <= "+TOP+" ORDER BY pictures.picture_id DESC";
  
  private static final String ALL_KEYWORDS = "select * from keywords";
  private static final String ALL_CATEGORIES = "select * from categories";
  private static final String ALL_USERS = "select * from users";
  
  private static final String INSERT_RATING = "insert into ratings (picture_id, user_id, value) VALUES (?,?,?)";
  
  private static final String INSERT_KEYWORD = "insert into keywords (name) VALUES (?)";
  private static final String INSERT_KEYWORD_GET_ID = "select keywords_inc.currval from dual";
  private static final String INSERT_CATEGORY = "insert into categories (name) VALUES (?)";
  private static final String INSERT_CATEGORY_GET_ID = "select categories_inc.currval from dual";
  
  private static final String INSERT_COMMENT = "insert into comments (comment_text, comment_time, user_id, picture_id) VALUES (?,?,?,?)";
  private static final String INSERT_COMMENT_GET_ID = "select comments_inc.currval from dual";
  
  private static final String INSERT_PICTURE_TO_CATEGORY = "insert into picture_to_category (picture_id, category_id) VALUES (?,?)";
  private static final String INSERT_PICTURE_TO_KEYWORD = "insert into picture_to_keyword (picture_id, keyword_id) VALUES (?,?)";
  
  private static final String DELETE_PICTURE = "delete from pictures where picture_id = ?";
  private static final String DELETE_ALBUM = "delete from albums where album_id = ?";
  private static final String DELETE_PICTURES_FROM_ALBUM = "delete from pictures where album_id = ?";
  private static final String SELECT_PICTURES_COUNT_IN_ALBUM = "select count(*) from pictures where album_id = ?";
  
  private static final String SELECT_USER_COMMENTS = "select * from comments where user_id = ?";
  
  private static final String DELETE_PICTURE_KEYWORDS = "delete from picture_to_keyword where picture_id = ?";
  private static final String DELETE_PICTURE_CATEGORIES = "delete from picture_to_category where picture_id = ?";
  
  /*DELETE ORDIMAGE // NO NEED
   * String sQuery = new String(
          "update pm.online_media set product_photo = ORDSYS.ORDImage.init(), " + 
          "product_thumbnail = ORDSYS.ORDImage.init() where product_id = ?");*/
  /**
   * Get the OracleConnection object (create connection)
   */
  public void connect(){
	if (m_dbConn==null){
		  m_dbConn = IMMain.getDBConnection();    
	}
  }
  


  /**
   * Check the login parameters, and if the user exist return the user object. If not return null
   * @param user Userdata object
   * @return All of the userdata (downloaded from the database), if the user not exist return null
   */
  public User checkLogin(User user){
	    connect();
	    try {
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(USER_LOGIN);
			int index = 1;
			stmt.setString(index++, user.getUsername());
			stmt.setString(index++, user.getPassword());
			rs = (OracleResultSet)stmt.executeQuery();
			
			if (rs.next()){
				user.setCityId(rs.getInt("city_id"));
				user.setEmail(rs.getString("email"));
				user.setFirstName(rs.getString("first_name"));
				user.setLastname(rs.getString("last_name"));
				user.setRegistered(rs.getDate("registered"));
				user.setPictureSum(rs.getInt("picture_sum"));
				user.setProfilePicture((OrdImage)rs.getORAData("profile_picture", OrdImage.getORADataFactory()));
				user.setProfilePictureThumb((OrdImage)rs.getORAData("profile_picture_thumb", OrdImage.getORADataFactory()));
				user.setUserId(rs.getInt("user_id"));
			} else {
				return null;
			}
			
			IMUtil.cleanup(rs, stmt);
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
		}
	  
	  return user;
  }
  
  /**
   * Insert a user to the database
   * @param user The user data collected from the signup panel
   * @return Return the user data if the operation was successfal, return null if not
   */
  public User userSignup(User user){
	  connect();
	  try {
		OraclePreparedStatement stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(USER_SIGNUP);
		int index = 1;
		stmt.setString(index++, user.getUsername());
		stmt.setString(index++, user.getPassword());
		stmt.setString(index++, user.getEmail());
		stmt.setString(index++, user.getFirstName());
		stmt.setString(index++, user.getLastname());
		stmt.setDate(index++, (Date) new Date(System.currentTimeMillis()));
		stmt.setInt(index++, user.getCityId());
		
		OracleResultSet rs = (OracleResultSet) stmt.executeQuery();
		
		if (rs!=null){
			IMUtil.cleanup(rs, stmt);
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(USER_SIGNUP_GET_USER_ID);
			rs =  (OracleResultSet) stmt.executeQuery();
			if (rs.next()){
				user.setUserId(rs.getInt(1));
			} else {
				return null;
			}
			
		} else {
			return null;
		}
		
		IMUtil.cleanup(rs, stmt);
	} catch (SQLException e) {
		new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
	} 
	  
	  return user;
  }
  
  /**
   * 
   */
  public Picture insertPicture(Picture pic){
	  connect();
	  try {
		OraclePreparedStatement stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(INSERT_PICTURE);
		int index = 1;
		stmt.setString(index++, pic.getPictureName());
		stmt.setDate(index++, (Date) new Date(System.currentTimeMillis()));
		stmt.setInt(index++, pic.getCityId());
		stmt.setInt(index++, pic.getAlbumId());
		
		OracleResultSet rs = (OracleResultSet) stmt.executeQuery();
		
		if (rs!=null){
			IMUtil.cleanup(rs, stmt);
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(INSERT_PICTURE_GET_PICTURE_ID);
			rs =  (OracleResultSet) stmt.executeQuery();
			if (rs.next()){
				pic.setPictureId(rs.getInt(1));
			} else {
				return null;
			}
			
		} else {
			return null;
		}
		
		IMUtil.cleanup(rs, stmt);
	} catch (SQLException e) {
		new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
	} 
	  
	  return pic;
  }
  
  
	public Picture updatePicture(Picture pic) {
		connect();
		  try {
			OraclePreparedStatement stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(UPDATE_PICTURE);
			int index = 1;
			stmt.setString(index++, pic.getPictureName());
			stmt.setInt(index++, pic.getCityId());
			stmt.setInt(index++, pic.getAlbumId());
			stmt.setInt(index++, pic.getPictureId());
			
			stmt.executeQuery();
			
			
			IMUtil.cleanup(rs, stmt);
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
			return null;
		} 
		  
	  return pic;
	}
  
  
  /**
   * Insert a user to the database
   * @param user The user data collected from the signup panel
   * @return Return the user data if the operation was successfal, return null if not
   */
  public boolean userUpdate(User user){
	  connect();
	  
	  boolean success = true;
	  try {
		OraclePreparedStatement stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(USER_UPDATE);
		int index = 1;
		stmt.setString(index++, user.getFirstName());
		stmt.setString(index++, user.getLastname());
		stmt.setString(index++, user.getPassword());
		stmt.setString(index++, user.getEmail());
		stmt.setInt(index++, user.getCityId());
		stmt.setInt(index++, user.getUserId());
		
		
		stmt.execute();
		
		IMUtil.cleanup(rs, stmt);
	} catch (SQLException e) {
		new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
		success = false;
	} 
	  
	  return success;
  }
  
  public ArrayList<Comment> selectCommentForPicture(Picture p){
	  ArrayList<Comment> array = new ArrayList<Comment>();
	  connect();
	  try {
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(SELECT_PICTURE_COMMENTS);
			int index = 1;
			stmt.setInt(index++, p.getPictureId());
			
			rs = (OracleResultSet)stmt.executeQuery();
			
			while (rs.next()){
				Comment c = new Comment(rs.getInt("comment_id"), rs.getString("comment_text"), rs.getDate("comment_time"), rs.getInt("user_id"), rs.getInt("picture_id"));
				array.add(c);
			}
			
			IMUtil.cleanup(rs, stmt);
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
			return null;
		}
	  
	  return array;
  }
  
  public ArrayList<Category> selectCategoriesForPicture(Picture p){
	  ArrayList<Category> array = new ArrayList<Category>();
	  connect();
	  try {
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(SELECT_PICTURE_CATEGORIES);
			int index = 1;
			stmt.setInt(index++, p.getPictureId());
			
			rs = (OracleResultSet)stmt.executeQuery();
			
			while (rs.next()){
				Category c = new Category(rs.getInt("category_id"), rs.getString("name"));
				array.add(c);
			}
			
			IMUtil.cleanup(rs, stmt);
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
			return null;
		}
	  
	  return array;
  }
  
  public ArrayList<Keyword> selectKeywordsForPicture(Picture p){
	  ArrayList<Keyword> array = new ArrayList<Keyword>();
	  connect();
	  try {
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(SELECT_PICTURE_KEYWORDS);
			int index = 1;
			stmt.setInt(index++, p.getPictureId());
			
			rs = (OracleResultSet)stmt.executeQuery();
			
			while (rs.next()){
				Keyword k = new Keyword(rs.getInt("keyword_id"), rs.getString("name"));
				array.add(k);
			}
			
			IMUtil.cleanup(rs, stmt);
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
			return null;
		}
	  
	  return array;
  }
  
  
  public ArrayList<Rating> selectRatingForPicture(Picture p){
	  return selectRating(SELECT_PICTURE_RATINGS, p.getPictureId());
  }
  
  public ArrayList<Rating> selectRatingForUsers(User u){
	  return selectRating(SELECT_USER_RATINGS, u.getUserId());
  }
  
  protected ArrayList<Rating> selectRating(final String sql, int id){
	  ArrayList<Rating> array = new ArrayList<Rating>();
	  connect();
	  try {
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(sql);
			int index = 1;
			stmt.setInt(index++, id);
			
			rs = (OracleResultSet)stmt.executeQuery();
			
			while (rs.next()){
				Rating r = new Rating(rs.getInt("picture_id"), rs.getInt("user_id"), rs.getInt("value"));
				array.add(r);
			}
			
			IMUtil.cleanup(rs, stmt);
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
			return null;
		}
	  
	  return array;
  }
  
  public ArrayList<Picture> selectPicturesFromAlbum(Album a){
	  return selectPictures(SELECT_PICTURES_FROM_ALBUM, a.getAlbumId());	  
  }
  
  public ArrayList<Picture> selectPicturesTopValue(){
	  return selectPictures(SELECT_TOP_RATED_PICTURES, 0);	
  }
  
  public ArrayList<Picture> selectPicturesTopRateCount(){
	  return selectPictures(SELECT_TOP_RATE_COUNT_PICTURES, 0);	
  }
  
  public ArrayList<Picture> selectPicturesTopCommentCount(){
	  return selectPictures(SELECT_TOP_COMMENT_COUNT_PICTURES, 0);	
  }
  
  public ArrayList<Picture> selectPicturesNew(){
	  return selectPictures(SELECT_NEW_PICTURES, 0);	
  }
  
  public ArrayList<Picture> selectPictures(String sql, int id){
	  ArrayList<Picture> array = new ArrayList<Picture>();
	  connect();
	  try {
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(sql);
			int index = 1;
			if (id!=0){
				stmt.setInt(index++, id);
			}
			
			rs = (OracleResultSet)stmt.executeQuery();
			
			while (rs.next()){
				Picture p = new Picture(rs.getInt("picture_id"), rs.getString("picture_name"), rs.getDate("upload_time"), 
						(OrdImage)rs.getORAData("picture", OrdImage.getORADataFactory()), (OrdImage)rs.getORAData("picture_thumbnail", OrdImage.getORADataFactory()), 
						rs.getInt("city_id"), rs.getInt("album_id"), null, null, null, null);
				p.setCategories(new IMQuery().selectCategoriesForPicture(p));
				p.setRating(new IMQuery().selectRatingForPicture(p));
				p.setComments(new IMQuery().selectCommentForPicture(p));
				p.setKeywords(new IMQuery().selectKeywordsForPicture(p));
				array.add(p);
			}
			
			IMUtil.cleanup(rs, stmt);
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
			return null;
		}
	  
	  return array;
	  
  }
  
  /**
   * 
   */
  public Album createAlbum(Album a){
	  connect();
	  try {
		OraclePreparedStatement stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(CREATE_ALBUM);
		int index = 1;
		stmt.setInt(index++, a.getUserId());
		stmt.setString(index++, a.getName());
		stmt.setInt(index++, a.isPublic()? 1 : 0 );
		
		stmt.executeQuery();
		
		IMUtil.cleanup(rs, stmt);
	} catch (SQLException e) {
		new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
		return null;
	} 
	  
	  return a;
  }
  
  public Album editAlbum(Album a){
	  connect();
	  try {
		OraclePreparedStatement stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(EDIT_ALBUM);
		int index = 1;
		stmt.setString(index++, a.getName());
		stmt.setInt(index++, a.isPublic()? 1 : 0 );
		stmt.setInt(index++, a.getAlbumId());
		
		stmt.executeUpdate();
		
		IMUtil.cleanup(rs, stmt);
	} catch (SQLException e) {
		new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
		return null;
	} 
	  
	  return a;
  }
  
  /**
   * 
   * @param user
   * @return
   */
  public ArrayList<Album> getAlbums(User user, String sql){
	  connect();
	  ArrayList<Album> array = new ArrayList<Album>();
	  
	  try {
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(sql);
			int index = 1;
			stmt.setInt(index++, user.getUserId());
			
			rs = (OracleResultSet)stmt.executeQuery();
			
			while (rs.next()){
				Album a = new Album(rs.getInt("album_id"), rs.getInt("user_id"), rs.getString("name"), rs.getInt("is_public")==1?true:false);
				array.add(a);
			}
			
			IMUtil.cleanup(rs, stmt);
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
			return null;
		}
	  

	  
	  return array; 
  }
  
  public ArrayList<Album> getAlbumsByUser(User user){
	  return getAlbums(user, GET_ALBUMS_BY_USER);
  }
  
  public ArrayList<Album> getPublicAlbumsByUser(User user){
	  return getAlbums(user, GET_PUBLIC_ALBUMS_BY_USER);
  }
  
  
  
  /**
   * Select all from countries table
   * @return an arraylist of the country objects
   */
  public ArrayList<Country> selectAllCountries(){
	    connect();
	    ArrayList<Country> array = new ArrayList<Country>();
	    
	    try {
 			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(ALL_COUNTRIES);
			rs = (OracleResultSet)stmt.executeQuery();
			
			while (rs.next()){
				Country c = new Country(rs.getInt("country_id"), rs.getString("name"));
				array.add(c);
			}
			
			IMUtil.cleanup(rs, stmt);
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
		}
	  
	  return array;	  
  }
  
  /**
   * Select all from regions table
   * @return an arraylist of the region objects
   */
  public ArrayList<Region> selectAllRegion(){
	    connect();
	    ArrayList<Region> array = new ArrayList<Region>();
	    
	    try {
 			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(ALL_REGIONS);
			rs = (OracleResultSet)stmt.executeQuery();
			
			while (rs.next()){
				Region c = new Region(rs.getInt("region_id"), rs.getString("name"), rs.getInt("country_id"));
				array.add(c);
			}
			
			IMUtil.cleanup(rs, stmt);
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
		}
	  
	  return array;	  
  }
  
  /**
   * Select all from cities table
   * @return an arraylist of the city objects
   */
  public ArrayList<City> selectAllCities(){
	    connect();
	    ArrayList<City> array = new ArrayList<City>();
	    
	    try {
 			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(ALL_CITIES);
			rs = (OracleResultSet)stmt.executeQuery();
			
			while (rs.next()){
				City c = new City(rs.getInt("city_id"), rs.getString("name"), rs.getInt("region_id"));
				array.add(c);
			}
			
			IMUtil.cleanup(rs, stmt);
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
		}
	  
	  return array;	  
  }
  
  public ArrayList<Keyword> selectAllKeywords(){
	    connect();
	    ArrayList<Keyword> array = new ArrayList<Keyword>();
	    
	    try {
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(ALL_KEYWORDS);
			rs = (OracleResultSet)stmt.executeQuery();
			
			while (rs.next()){
				Keyword k = new Keyword(rs.getInt("keyword_id"), rs.getString("name"));
				array.add(k);
			}
			
			IMUtil.cleanup(rs, stmt);
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
		}
	  
	  return array;	  
  }
  
  public ArrayList<User> selectAllUsers() {
	    connect();
	    ArrayList<User> array = new ArrayList<User>();
	    
	    try {
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(ALL_USERS);
			rs = (OracleResultSet)stmt.executeQuery();
			
			while (rs.next()){
				User user = new User();
				user.setCityId(rs.getInt("city_id"));
				user.setUsername(rs.getString("username"));
				user.setEmail(rs.getString("email"));
				user.setFirstName(rs.getString("first_name"));
				user.setLastname(rs.getString("last_name"));
				user.setRegistered(rs.getDate("registered"));
				user.setPictureSum(rs.getInt("picture_sum"));
				user.setProfilePicture((OrdImage)rs.getORAData("profile_picture", OrdImage.getORADataFactory()));
				user.setProfilePictureThumb((OrdImage)rs.getORAData("profile_picture_thumb", OrdImage.getORADataFactory()));
				user.setUserId(rs.getInt("user_id"));
				
				array.add(user);
			}
			
			IMUtil.cleanup(rs, stmt);
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
		}
	  
	  return array;	  
	}
  
  
  public ArrayList<Category> selectAllCategories(){
	    connect();
	    ArrayList<Category> array = new ArrayList<Category>();
	    
	    try {
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(ALL_CATEGORIES);
			rs = (OracleResultSet)stmt.executeQuery();
			
			while (rs.next()){
				Category c = new Category(rs.getInt("category_id"), rs.getString("name"));
				array.add(c);
			}
			
			IMUtil.cleanup(rs, stmt);
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
		}
	  
	  return array;	  
  }
  
  public Keyword insertKeyword(Keyword k){
	  connect();
	  try {
		OraclePreparedStatement stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(INSERT_KEYWORD);
		int index = 1;
		stmt.setString(index++, k.getName());
		
		
		OracleResultSet rs = (OracleResultSet) stmt.executeQuery();
		
		if (rs!=null){
			IMUtil.cleanup(rs, stmt);
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(INSERT_KEYWORD_GET_ID);
			rs =  (OracleResultSet) stmt.executeQuery();
			if (rs.next()){
				k.setKeywordId(rs.getInt(1));
			} else {
				return null;
			}
			
		} else {
			return null;
		}
		
		IMUtil.cleanup(rs, stmt);
	} catch (SQLException e) {
		new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
	} 
	  
	  return k;
  }
  
  public boolean insertRating(Rating r) {
	  connect();
	  try {
		OraclePreparedStatement stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(INSERT_RATING);
		int index = 1;
		stmt.setInt(index++, r.getPictureId());
		stmt.setInt(index++, r.getUserId());
		stmt.setInt(index++, r.getValue());
		
		stmt.executeQuery();

		IMUtil.cleanup(rs, stmt);
	} catch (SQLException e) {
		new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
		return false;
	} 
	  
	  return true;
		
  }
  
  
  public void insertKeywordToPicture(Picture p, ArrayList<Keyword> array){
	  connect();
	  
	  Iterator<Keyword> it = array.iterator();
	  
	  while (it.hasNext()){
		  Keyword keyword = it.next();
	  
	  
		  try {
			OraclePreparedStatement stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(INSERT_PICTURE_TO_KEYWORD);
			int index = 1;
			stmt.setInt(index++, p.getPictureId());
			stmt.setInt(index++, keyword.getKeywordId());
						
			stmt.executeQuery();

			IMUtil.cleanup(rs, stmt);
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
		}
		  
	  }

  }
  
  public void insertCategoryToPicture(Picture p, ArrayList<Category> array){
	  connect();
	  
	  Iterator<Category> it = array.iterator();
	  
	  while (it.hasNext()){
		  Category category = it.next();
	  
	  
		  try {
			OraclePreparedStatement stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(INSERT_PICTURE_TO_CATEGORY);
			int index = 1;
			stmt.setInt(index++, p.getPictureId());
			stmt.setInt(index++, category.getCategoryId());
						
			stmt.executeQuery();

			IMUtil.cleanup(rs, stmt);
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
		}
		  
	  }

  }
  
  public Category insertCategory(Category c){
	  connect();
	  try {
		OraclePreparedStatement stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(INSERT_CATEGORY);
		int index = 1;
		stmt.setString(index++, c.getName());
		
		
		OracleResultSet rs = (OracleResultSet) stmt.executeQuery();
		
		if (rs!=null){
			IMUtil.cleanup(rs, stmt);
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(INSERT_CATEGORY_GET_ID);
			rs =  (OracleResultSet) stmt.executeQuery();
			if (rs.next()){
				c.setCategoryId(rs.getInt(1));
			} else {
				return null;
			}
			
		} else {
			return null;
		}
		
		IMUtil.cleanup(rs, stmt);
	} catch (SQLException e) {
		new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
	} 
	  
	  return c;
  }
  
  public boolean deletePicture(Picture p){
	  connect();
	  try {
		OraclePreparedStatement stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(DELETE_PICTURE);
		int index = 1;
		stmt.setInt(index++, p.getPictureId());
		
		stmt.executeQuery();
	
		IMUtil.cleanup(rs, stmt);
		
		
	} catch (SQLException e) {
		new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
		
		return false;
	} 
	  
	return true;
  }
  
  public int deletePicture(Album a){
	  int count = 0;
	  connect();
	  try {
		OraclePreparedStatement stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(SELECT_PICTURES_COUNT_IN_ALBUM);
		int index = 1;
		stmt.setInt(index++, a.getAlbumId());
			
		OracleResultSet rs = (OracleResultSet) stmt.executeQuery();
		if (rs.next()){
			count = rs.getInt(1);
		}
	
		IMUtil.cleanup(rs, stmt);
		  		  
		stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(DELETE_PICTURES_FROM_ALBUM);
		index = 1;
		stmt.setInt(index++, a.getAlbumId());
		
		stmt.executeQuery();
	
		IMUtil.cleanup(rs, stmt);
		
	} catch (SQLException e) {
		new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
		
		return 0;
	} 
	  
	return count;
  }
  
  public boolean deleteAlbum(Album a){
	  connect();
	  try {
		OraclePreparedStatement stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(DELETE_ALBUM);
		int index = 1;
		stmt.setInt(index++, a.getAlbumId());
		
		stmt.executeQuery();
	
		IMUtil.cleanup(rs, stmt);
		
		
	} catch (SQLException e) {
		new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
		
		return false;
	} 
	  
	return true;
  }
  
  public Comment createComment(Comment c){
	  connect();
	  try {
		OraclePreparedStatement stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(INSERT_COMMENT);
		int index = 1;
		stmt.setString(index++, c.getCommentText());
		stmt.setDate(index++, c.getCommentTime());
		stmt.setInt(index++, c.getUserId());
		stmt.setInt(index++, c.getPictureId());
		
		
		OracleResultSet rs = (OracleResultSet) stmt.executeQuery();
		
		if (rs!=null){
			IMUtil.cleanup(rs, stmt);
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(INSERT_COMMENT_GET_ID);
			rs =  (OracleResultSet) stmt.executeQuery();
			if (rs.next()){
				c.setCommentId(rs.getInt(1));
			} else {
				return null;
			}
			
		} else {
			return null;
		}
		
		IMUtil.cleanup(rs, stmt);
	} catch (SQLException e) {
		new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
		return null;
	} 
	  
	  return c;
  }
  
  public ArrayList<Comment> selectCommentForUser(User u){
	  ArrayList<Comment> array = new ArrayList<Comment>();
	  connect();
	  try {
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(SELECT_USER_COMMENTS);
			int index = 1;
			stmt.setInt(index++, u.getUserId());
			
			rs = (OracleResultSet)stmt.executeQuery();
			
			while (rs.next()){
				Comment c = new Comment(rs.getInt("comment_id"), rs.getString("comment_text"), rs.getDate("comment_time"), rs.getInt("user_id"), rs.getInt("picture_id"));
				array.add(c);
			}
			
			IMUtil.cleanup(rs, stmt);
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
			return null;
		}
	  
	  return array;
  }
  
  public boolean removeAllCategoryToPicture(Picture m_picture) {
	  connect();
	  try {
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(DELETE_PICTURE_CATEGORIES);
			int index = 1;
			stmt.setInt(index++, m_picture.getPictureId());
			
			stmt.executeQuery();
			
			IMUtil.cleanup(rs, stmt);
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
			return false;
		}
	  
	  return true;
	}


	public boolean removeAllKeywordToPicture(Picture m_picture) {
		connect();
		  try {
				stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(DELETE_PICTURE_KEYWORDS);
				int index = 1;
				stmt.setInt(index++, m_picture.getPictureId());
				
				stmt.executeQuery();
				
				IMUtil.cleanup(rs, stmt);
			} catch (SQLException e) {
				new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
				return false;
			}
		  
		  return true;
		
	}
	
	
	public ArrayList<Picture> search(String name, int minHeight, int maxHeight,
			int minWidth, int maxWidth, int countryId, int regionId, int cityId,
			ArrayList<Category> selectedCategories,
			ArrayList<Keyword> selectedKeywords) {
	
		  ArrayList<Picture> array = new ArrayList<Picture>();
		  connect();
		  try {
			    String sql = "SELECT * FROM pictures, albums ";
				String where = "";
				if (!name.equals("") && !name.equals(null)){
					where = ((where.equals("")) ? "WHERE " : where + " AND") + " pictures.picture_name LIKE ?";
				}
				if (cityId!=0){
					where = ((where.equals("")) ? "WHERE " : where + " AND") + " pictures.city_id = ?";
				} else if (regionId!=0){
					sql += ", regions, cities ";
					where = ((where.equals("")) ? "WHERE " : where + " AND") + " pictures.city_id = cities.city_id AND regions.region_id = cities.region_id AND regions.region_id = ?";
				} else if (countryId!=0){
					sql += ", countries, regions, cities ";
					where = ((where.equals("")) ? "WHERE " : where + " AND") + " pictures.city_id = cities.city_id AND regions.region_id = cities.region_id AND " +
							"regions.country_id = countries.country_id AND countries.country_id = ?";
				}
				
				if (selectedCategories.size()>0){
					String sub = " (pictures.picture_id IN (SELECT DISTINCT picture_id FROM picture_to_category WHERE ";
					for (int i=0; i<selectedCategories.size(); i++){
						sub += " category_id = ? OR ";
					}	
					sub = sub.substring(0, sub.length()-3);
					sub +=")) ";
					where = ((where.equals("")) ? "WHERE " : where + " AND") + sub;
				}
				
				if (selectedKeywords.size()>0){
					String sub = " (pictures.picture_id IN (SELECT DISTINCT picture_id FROM picture_to_keyword WHERE ";
					for (int i=0; i<selectedKeywords.size(); i++){
						sub += " keyword_id = ? OR ";
					}	
					sub = sub.substring(0, sub.length()-3);
					sub +=")) ";
					where = ((where.equals("")) ? "WHERE " : where + " AND") + sub;
				}
				
				where = ((where.equals("")) ? "WHERE " : where + " AND") +" pictures.album_id = albums.album_id AND albums.is_public = 1 ";
				sql += where;
				
				if (IMMain.isDEBUG()){
					System.out.println(sql);
				}
				stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(sql);
				
				int index = 1;
				
				if (!name.equals("") && !name.equals(null)){
					stmt.setString(index++, "%"+name+"%");
					if (IMMain.isDEBUG()){
						System.out.println("bind name: "+name);
					}
				}
				if (cityId!=0){
					stmt.setInt(index++, cityId);
					if (IMMain.isDEBUG()){
						System.out.println("bind city: "+cityId);
					}
				} else if (regionId!=0){
					stmt.setInt(index++, regionId);
					if (IMMain.isDEBUG()){
						System.out.println("bind region: "+regionId);
					}
				} else if (countryId!=0){
					stmt.setInt(index++, countryId);
					if (IMMain.isDEBUG()){
						System.out.println("bind country: "+countryId);
					}
				}
				
				
				Iterator<Category> itCategory = selectedCategories.iterator();
					while (itCategory.hasNext()){
						Category c = itCategory.next();
						stmt.setInt(index++, c.getCategoryId());
						if (IMMain.isDEBUG()){
							System.out.println("bind category: "+c.getCategoryId());
						}
					}	
				
				Iterator<Keyword> itKeyword = selectedKeywords.iterator();
					while (itKeyword.hasNext()){
						Keyword k = itKeyword.next();
						stmt.setInt(index++, k.getKeywordId());
						if (IMMain.isDEBUG()){
							System.out.println("bind keyword: "+k.getKeywordId());
						}
					}	
				
				
				rs = (OracleResultSet) stmt.executeQuery();
				while (rs.next()){
					Picture p = new Picture(rs.getInt("picture_id"), rs.getString("picture_name"), rs.getDate("upload_time"), 
							(OrdImage)rs.getORAData("picture", OrdImage.getORADataFactory()), (OrdImage)rs.getORAData("picture_thumbnail", OrdImage.getORADataFactory()), 
							rs.getInt("city_id"), rs.getInt("album_id"), null, null, null, null);
					p.setCategories(new IMQuery().selectCategoriesForPicture(p));
					p.setRating(new IMQuery().selectRatingForPicture(p));
					p.setComments(new IMQuery().selectCommentForPicture(p));
					p.setKeywords(new IMQuery().selectKeywordsForPicture(p));
					if (
							(p.getPicture().getHeight()>=minHeight) &&
							(p.getPicture().getHeight()<=maxHeight) &&
							(p.getPicture().getWidth()>=minWidth) &&
							(p.getPicture().getWidth()<=maxWidth)
					){
								array.add(p);
					}
				}
				
				IMUtil.cleanup(rs, stmt);
			} catch (SQLException e) {
				new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
				return null;
			}
		  
		  return array;
	}




  
  public boolean initOrdImage(Object o, boolean thumb){
	connect();
	try {
		if (o instanceof User){
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(thumb?INIT_USER_PROFILE_PIC_THUMB:INIT_USER_PROFILE_PIC);
			int index=1;
			stmt.setInt(index++, ((User)o).getUserId());
		} else if (o instanceof Picture){
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(thumb?INIT_PIC_THUMB:INIT_PIC);
			int index=1;
			stmt.setInt(index++, ((Picture)o).getPictureId());
			
		}
		stmt.execute();
		IMUtil.cleanup(rs, stmt);
	} catch (SQLException e) {
		new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
		return false;
	}

	return true;

  }
  
  public OrdImage selectOrdImage(Object o, boolean thumb){
	  connect();
	  OrdImage m_img = null;
	  try {
			if (o instanceof User){
				stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(thumb?SELECT_USER_PROFILE_PIC_THUMB:SELECT_USER_PROFILE_PIC);
				int index=1;
				stmt.setInt(index++, ((User)o).getUserId());
			} else if (o instanceof Picture){
				stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(thumb?SELECT_PIC_THUMB:SELECT_PIC);
				int index=1;
				stmt.setInt(index++, ((Picture)o).getPictureId());
				
			}
			
			rs = (OracleResultSet)stmt.executeQuery();
			if (rs.next() == false){
		        throw new SQLException();
			} else {
		        m_img = (OrdImage)rs.getORAData(1, OrdImage.getORADataFactory());
			}
			IMUtil.cleanup(rs, stmt);
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
			return null;
		}

		return m_img;
  }
  
  public boolean updateOrdImage(Object o, OrdImage m_img, boolean thumb){
	  connect();
	  try {
			if (o instanceof User){
				stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(thumb?UPDATE_USER_PROFILE_PIC_THUMB:UPDATE_USER_PROFILE_PIC);
				int index=1;
				stmt.setORAData(index++, m_img);
				stmt.setInt(index++, ((User)o).getUserId());
			} else if (o instanceof Picture){
				stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(thumb?UPDATE_PIC_THUMB:UPDATE_PIC);
				int index=1;
				stmt.setORAData(index++, m_img);
				stmt.setInt(index++, ((Picture)o).getPictureId());
			}
			
			stmt.execute();
			IMUtil.cleanup(rs, stmt);
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
			return false;
		}
      
	return true;
	  
  }

}
