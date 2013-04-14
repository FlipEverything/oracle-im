import java.sql.Date;
import java.sql.SQLException;

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

  
  public void connect(){
	if (m_dbConn==null){
		  m_dbConn = IMRunnableMain.getDBConnection();    
	}
  }

  public User checkLogin(User user){
	  connect();
	  try {
		stmt = (OraclePreparedStatement) m_dbConn.prepareStatement("SELECT * FROM USERS WHERE username = ? AND password = ?");
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
  
  public User userSignup(User user){
	  connect();
	  try {
		OraclePreparedStatement stmt = (OraclePreparedStatement) m_dbConn.prepareStatement("INSERT INTO USERS (username, password, email, first_name, last_name, registered) VALUES (?,?,?,?,?,?)");
		int index = 1;
		stmt.setString(index++, user.getUsername());
		stmt.setString(index++, user.getPassword());
		stmt.setString(index++, user.getEmail());
		stmt.setString(index++, user.getFirstName());
		stmt.setString(index++, user.getLastname());
		stmt.setDate(index++, (Date) new Date(System.currentTimeMillis()));
		
		OracleResultSet rs = (OracleResultSet) stmt.executeQuery();
		
		if (rs!=null){
			IMUtil.cleanup(rs, stmt);
			stmt = (OraclePreparedStatement) m_dbConn.prepareStatement("SELECT USERS_INC.CURRVAL FROM DUAL");
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
		m_dbConn.commit();
	} catch (SQLException e) {
		new IMMessage(IMConstants.ERROR, "SQL_FAIL", e);
	} 
	  
	  return user;
  }
  
  
  /**
   * Displays the table.
   */
  /*JTable showTable() throws SQLException
  {
    // Sets the display model.
    IMResultSetTableModel tm = new IMResultSetTableModel(m_resultSet);
    JTable jt = new JTable(tm);

    jt.getAccessibleContext().setAccessibleName(
        IMMessage.getString("MAIN_TBL_NAME"));
    jt.setToolTipText(IMMessage.getString("MAIN_TBL_DESC"));

    // Modifies the default display parameters.
    jt.setRowHeight(jt.getRowHeight()+10);
    jt.setRowMargin(jt.getRowMargin()+10);

    // Sets the rendering. 
    jt.setDefaultRenderer(Object.class, new IMTableRenderer(tm));
    jt.setDefaultRenderer(BigDecimal.class, new IMTableRenderer(tm));
    jt.setDefaultRenderer(Boolean.class, new BooleanRenderer(tm));

    // Modifies the column width.
    int iColCount = tm.getColumnCount();
    for (int i=0; i<iColCount; i++)
    {
      String colname = tm.getRealColumnName(i);
      TableColumn tblColumn = jt.getColumnModel().getColumn(i);

      if (ID.equals(colname))
      {
        tblColumn.setPreferredWidth(tblColumn.getPreferredWidth()-150);
      }
      else if (CHECK.equals(colname))
      {
        tblColumn.setPreferredWidth(tblColumn.getPreferredWidth()-90);
      }
      else if (NAME.equals(colname))
      {
        tblColumn.setPreferredWidth(tblColumn.getPreferredWidth()-50);
      }
      else if (DESC.equals(colname))
      {
        tblColumn.setPreferredWidth(tblColumn.getPreferredWidth()+290);
      }
      else
      {
        new IMMessage(IMConstants.ERROR, "COLNAME_ERR");
      }
    }

    return jt;
  }*/
}
