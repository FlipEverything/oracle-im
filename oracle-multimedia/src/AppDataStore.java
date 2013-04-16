
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;



public class AppDataStore {
	private static final String SELECT = "SELECT * FROM creditentals;";
	private static final String CREATE = "CREATE TABLE IF NOT EXISTS creditentals (" +
			  "id INTEGER GENERATED ALWAYS AS IDENTITY(START WITH 1), " +
			  "hostname varchar(30) NOT NULL, " +
			  "port INTEGER NOT NULL, " +
			  "sid varchar(20) NOT NULL, " +
			  "username varchar(30) NOT NULL, " +
			  "password varchar(50) NOT NULL, " +
			  "description varchar(50) NOT NULL, " +
			  "PRIMARY KEY  (id), " +
			  "unique(description) " +
			  ");";
	private static final String INSERT = "INSERT INTO creditentals (hostname, port, sid, username, password, description) VALUES (?,?,?,?,?,?)";
	private static final String UPDATE = "UPDATE creditentals SET hostname = ?, port = ?, sid = ?, username = ?, password = ?, description = ? WHERE id = ?";
	private static final String DELETE = "DELETE FROM creditentals WHERE id = ?";
	
	
	public static final String PROJECT_PATH = new File("").getAbsolutePath();
	public static final String key = "770A8A65DA156D24EE2A093277530142";
    String url = "jdbc:hsqldb:file://"+PROJECT_PATH+"//appdata/creditentals;crypt_key="+key+";crypt_type=AES";
    Connection c;
    
    public AppDataStore(){

    }
    
    private void connect() throws SQLException{
        if ((c==null) || (!c.isValid(0))){
            c = DriverManager.getConnection(url, "SA", "");
        }
        createTableIfNotExist();
    }
    
    public ArrayList<DatabaseCreditental> init() throws SQLException{
    	connect();
    	ArrayList<DatabaseCreditental> array = new ArrayList<DatabaseCreditental>();
    	
    	PreparedStatement stmt = c.prepareStatement(SELECT);
    	ResultSet rs = stmt.executeQuery();
    	if(rs.next()){
    		array.add(
    				new DatabaseCreditental(
    						rs.getInt("id"), 
    						rs.getString("hostname"), 
    						rs.getInt("port"), 
    						rs.getString("sid"), 
    						rs.getString("username"), 
    						rs.getString("password"), 
    						rs.getString("description"))
    				);
    	}
    	
    	
	    return array;
    }
    
    public boolean createTableIfNotExist() throws SQLException{
    	PreparedStatement stmt = c.prepareStatement(CREATE);
    	
    	if(stmt.executeUpdate()==1){
	        return true;
	    }
	    return false;
    }
    
    public boolean insert(DatabaseCreditental db) throws SQLException{
	    connect();
	    PreparedStatement stmt = c.prepareStatement(INSERT);
	    
	    int index = 1;
	    stmt.setString(index++, db.getHostname());
	    stmt.setInt(index++, db.getPort());
	    stmt.setString(index++, db.getSid());
	    stmt.setString(index++, db.getUsername());
	    stmt.setString(index++, db.getPassword());
	    stmt.setString(index++, db.getDescription());
	    
	    if(stmt.executeUpdate()==1){
	        return true;
	    }
	    return false;
    }

    public boolean update(DatabaseCreditental db) throws SQLException{
	    connect();
	    PreparedStatement stmt = c.prepareStatement(UPDATE);
	    
	    int index = 1;
	    stmt.setString(index++, db.getHostname());
	    stmt.setInt(index++, db.getPort());
	    stmt.setString(index++, db.getSid());
	    stmt.setString(index++, db.getUsername());
	    stmt.setString(index++, db.getPassword());
	    stmt.setString(index++, db.getDescription());
	    stmt.setInt(index++, db.getId());
	    
	    if(stmt.executeUpdate()==1){
	        return true;
	    }
	    return false;
    }
    
    public void delete(DatabaseCreditental db) throws SQLException{
	    connect();
	    PreparedStatement stmt = c.prepareStatement(DELETE);
	    
	    int index = 1;
	    stmt.setInt(index++, db.getId());
	    
	    stmt.execute();
    }
}
