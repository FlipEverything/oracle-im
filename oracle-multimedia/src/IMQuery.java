import java.io.BufferedReader;
import java.io.FileReader;
import java.net.URL;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
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
import bean.Tag;
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
  
  private static final URL DB_SCRIPT_FILE = IMFrame.class.getResource("sql/db_script.sql");
  
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
  
  private static final String SELECT_PICTURES_FROM_ALBUM = "select * from pictures where album_id = ?";
  private static final String SELECT_PICTURE_KEYWORDS = "select distinct keywords.keyword_id, keywords.name from keywords, picture_to_keyword  where keywords.keyword_id = picture_to_keyword.keyword_id AND picture_to_keyword.picture_id = ?";
  private static final String SELECT_PICTURE_CATEGORIES = "select distinct categories.category_id, categories.name from categories, picture_to_category  where categories.category_id = picture_to_category.category_id AND picture_to_category.picture_id = ?";
  private static final String SELECT_PICTURE_COMMENTS = "select * from comments where picture_id = ?";
  private static final String SELECT_PICTURE_TAGS = "select * from tags where picture_id = ?";
  private static final String SELECT_PICTURE_RATINGS = "select * from ratings where picture_id = ?";
  
  private static final String ALL_KEYWORDS = "select * from keywords";
  private static final String ALL_CATEGORIES = "select * from categories";
  private static final String ALL_USERS = "select * from users";
  
  private static final String INSERT_KEYWORD = "insert into keywords (name) VALUES (?)";
  private static final String INSERT_KEYWORD_GET_ID = "select keywords_inc.currval from dual";
  private static final String INSERT_CATEGORY = "insert into categories (name) VALUES (?)";
  private static final String INSERT_CATEGORY_GET_ID = "select categories_inc.currval from dual";
  
  private static final String INSERT_PICTURE_TO_CATEGORY = "insert into picture_to_category (picture_id, category_id) VALUES (?,?)";
  private static final String INSERT_PICTURE_TO_KEYWORD = "insert into picture_to_keyword (picture_id, keyword_id) VALUES (?,?)";
  
  private static final String DELETE_PICTURE = "delete from pictures where picture_id = ?";
  private static final String DELETE_ALBUM = "delete from albums where album_id = ?";
  private static final String DELETE_PICTURES_FROM_ALBUM = "delete from pictures where album_id = ?";
  private static final String SELECT_PICTURES_COUNT_IN_ALBUM = "select count(*) from pictures where album_id = ?";
  
  
  /**
   * Get the OracleConnection object (create connection)
   */
  public void connect(){
	if (m_dbConn==null){
		  m_dbConn = IMMain.getDBConnection();    
	}
  }
  

  public boolean executeDBScripts() {
	  connect();

		boolean isScriptExecuted = false;
		try {
			
			BufferedReader in = new BufferedReader(new FileReader(DB_SCRIPT_FILE.toURI().toString().replace("file:/", "")));
			String str;
			StringBuffer sb = new StringBuffer();
				while ((str = in.readLine()) != null) {
						sb.append(str + "\n");
				}
				in.close();
				System.out.println(sb.toString());
				Statement stmt = m_dbConn.createStatement();
				stmt.executeUpdate(sb.toString());
				isScriptExecuted = true;
		} catch (Exception e) {
			new IMMessage(IMConstants.ERROR, "APP_ERR", e);
		} 
	return isScriptExecuted;
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
				Comment c = new Comment(rs.getInt("comment_id"), rs.getString("comment_text"), rs.getDate("comment_time"), rs.getInt("user_id"));
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
  
  public ArrayList<Tag> selectTagsForPicture(Picture p){
	  ArrayList<Tag> array = new ArrayList<Tag>();
	  connect();
	  try {
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(SELECT_PICTURE_TAGS);
			int index = 1;
			stmt.setInt(index++, p.getPictureId());
			
			rs = (OracleResultSet)stmt.executeQuery();
			
			while (rs.next()){
				Tag t = new Tag(rs.getInt("user_id"), rs.getFloat("pixel_x"), rs.getFloat("pixel_y"));
				array.add(t);
			}
			
			IMUtil.cleanup(rs, stmt);
		} catch (SQLException e) {
			new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
			return null;
		}
	  
	  return array;
  }
  
  public ArrayList<Rating> selectRatingForPicture(Picture p){
	  ArrayList<Rating> array = new ArrayList<Rating>();
	  connect();
	  try {
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(SELECT_PICTURE_RATINGS);
			int index = 1;
			stmt.setInt(index++, p.getPictureId());
			
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
	  ArrayList<Picture> array = new ArrayList<Picture>();
	  connect();
	  try {
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(SELECT_PICTURES_FROM_ALBUM);
			int index = 1;
			stmt.setInt(index++, a.getAlbumId());
			
			rs = (OracleResultSet)stmt.executeQuery();
			
			while (rs.next()){
				Picture p = new Picture(rs.getInt("picture_id"), rs.getString("picture_name"), rs.getDate("upload_time"), 
						(OrdImage)rs.getORAData("picture", OrdImage.getORADataFactory()), (OrdImage)rs.getORAData("picture_thumbnail", OrdImage.getORADataFactory()), 
						rs.getInt("city_id"), rs.getInt("album_id"), null, null, null, null, null);
				p.setCategories(new IMQuery().selectCategoriesForPicture(p));
				p.setRating(new IMQuery().selectRatingForPicture(p));
				p.setComments(new IMQuery().selectCommentForPicture(p));
				p.setTags(new IMQuery().selectTagsForPicture(p));
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
