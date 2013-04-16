import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;

import bean.Album;
import bean.City;
import bean.Country;
import bean.Picture;
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
  
  
  private static final String USER_LOGIN = "SELECT * FROM USERS WHERE username = ? AND password = ?";
  private static final String USER_SIGNUP = "INSERT INTO USERS (username, password, email, first_name, last_name, registered, city_id) VALUES (?,?,?,?,?,?,?)";
  private static final String USER_SIGNUP_GET_USER_ID = "SELECT USERS_INC.CURRVAL FROM DUAL";
  private static final String USER_UPDATE = "UPDATE USERS SET first_name = ?, last_name = ?, password = ?, email = ?, city_id = ? WHERE user_id = ?";
  
  private static final String ALL_COUNTRIES = "SELECT * FROM COUNTRIES ORDER BY name";
  private static final String ALL_REGIONS = "SELECT * FROM REGIONS ORDER BY name";
  private static final String ALL_CITIES = "SELECT * FROM CITIES ORDER BY name";
  
  private static final String CREATE_ALBUM = "INSERT INTO ALBUMS (user_id, name, is_public) VALUES (?,?,?)";
  private static final String GET_ALBUMS_BY_USER = "SELECT * FROM ALBUMS WHERE USER_ID = ?";
  
  private static final String INIT_USER_PROFILE_PIC = "update users set profile_picture = ORDSYS.ORDImage.init() where user_id = ?";
  private static final String SELECT_USER_PROFILE_PIC = "select profile_picture from users where user_id = ? for update";
  private static final String UPDATE_USER_PROFILE_PIC = "update users set profile_picture = ? where user_id = ?";
  
  private static final String INIT_USER_PROFILE_PIC_THUMB = "update users set profile_picture_thumb = ORDSYS.ORDImage.init() where user_id = ?";
  private static final String SELECT_USER_PROFILE_PIC_THUMB = "select profile_picture_thumb from users where user_id = ? for update";
  private static final String UPDATE_USER_PROFILE_PIC_THUMB = "update users set profile_picture_thumb = ? where user_id = ?";
  
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
  
  /**
   * 
   * @param user
   * @return
   */
  public ArrayList<Album> getAlbumsByUser(User user){
	  connect();
	  ArrayList<Album> array = new ArrayList<Album>();
	  
	  try {
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(GET_ALBUMS_BY_USER);
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
  
  public boolean initOrdImage(Object o, boolean thumb){
	connect();
	try {
		if (o instanceof User){
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement(thumb?INIT_USER_PROFILE_PIC_THUMB:INIT_USER_PROFILE_PIC);
			int index=1;
			stmt.setInt(index++, ((User)o).getUserId());
		} else if (o instanceof Picture){
			//TODO
			//FIXME
			
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
				//TODO
				//FIXME
				
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
				//TODO
				//FIXME
				
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
