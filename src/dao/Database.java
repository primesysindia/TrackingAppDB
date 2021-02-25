package dao;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClients;

import dto.VehicalTrackingSMSCmdDTO;

public class Database {
	public Connection conn=null;
	public Connection connSunMssql=null;

	public static Mongo mongo_instance;
	static Database database_single_instance;
	
    private static Database single_instance = null;
    
    
    // static method to create instance of Singleton class
    public static Database getInstance()
    {
        if (single_instance == null)
            database_single_instance = new Database();
 
        return database_single_instance;
    }

    public Database() {
		// TODO Auto-generated constructor stub
 
    
			try {
				Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

				conn = DriverManager.getConnection(Common.SQL_Connection);
				connSunMssql= DriverManager.getConnection(Common.SQL_Connection_Sun_Mssql);
				
			
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	

		
		
		//////////////////////////////////////////////
		String mongouname="",mongopassword="";
		//for local mongo 
//		int mongoport=27017;
		//for production mongo
		int mongoport=19209;
		try
		{	ResultSet rs=null;
			java.sql.CallableStatement stmt=conn.prepareCall("{call GetMongoMetaData()}");
		
			  

			 rs = stmt.executeQuery();
				if(rs!=null )
				{
					while ( rs.next()) {
						
						
						mongouname=rs.getString("MongoUsername")+"";
						mongopassword=rs.getString("MongoPassword");
						mongoport=rs.getInt("MongoPort");
					}
				}
				
		}catch(Exception e)
		{		
			e.printStackTrace();
		}
	
		try {
			
			  mongo_instance = new Mongo(Common.mongo_Connection, mongoport);
//			  mongo_instance = new MongoClient(new MongoClientURI("mongodb://"+Common.mongo_Connection+":19209"));

//				System.err.println("checkMongoConnection------"+Common.mongo_Connection+mongouname+"  "+mongopassword);


			/*db=mongo_instance.getDB("tracking");
			 boolean auth = db.authenticate(mongouname.toString().trim(),mongopassword.trim().toCharArray());
			// boolean auth = db.authenticate("ram", "123".toCharArray());

			  if(auth){
			   System.out.println("checkMongoConnection------"+"TRUE");
			  }else{
			   System.out.println("checkMongoConnection------"+"FALSE");
			  }
			   */
//			  System.out.println("checkMongoConnection------"+"Done");
		} catch (MongoException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}finally{
			
		}
    }
		
   }
	
	
	/*public  Connection getConnection() throws SQLException, ClassNotFoundException{
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");	
		conn = DriverManager.getConnection(Common.SQL_Connection);

		return conn;
	}
	
	
	public  DB checkMongoConnection(){
		String mongouname="",mongopassword="";
		int mongoport=19209;

		try
		{	ResultSet rs=null;
			java.sql.CallableStatement stmt=getConnection().prepareCall("{call GetMongoMetaData()}");
		
			  

			 rs = stmt.executeQuery();
				if(rs!=null )
				{
					while ( rs.next()) {
						
						
						mongouname=rs.getString("MongoUsername")+"";
						mongopassword=rs.getString("MongoPassword");
						mongoport=rs.getInt("MongoPort");
					}
				}
				
		}catch(Exception e)
		{		
			e.printStackTrace();
		}
	
		try {

			 Mongo mongo_instance = new Mongo(Common.mongo_Connection, mongoport);
				System.err.println("checkMongoConnection------"+Common.mongo_Connection+mongouname+"  "+mongopassword);


			db=mongo_instance.getDB("tracking");
			 boolean auth = db.authenticate(mongouname.toString().trim(),mongopassword.trim().toCharArray());
			// boolean auth = db.authenticate("ram", "123".toCharArray());

			  if(auth){
			   System.out.println("checkMongoConnection------"+"TRUE");
			  }else{
			   System.out.println("checkMongoConnection------"+"FALSE");
			  }
			   
			  System.out.println("checkMongoConnection------"+"Done");

		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MongoException ex) {
			// TODO Auto-generated catch block
			ex.printStackTrace();
		}
		return db;
	}
	

public Connection Get_MysqlConnection() 
	{		Connection mysqlconnection = null;

	
		try
		{
		//String connectionURL = "jdbc:mysql://52.34.38.150:3306/handcraftdb";

        String connectionURL = "jdbc:mysql://192.168.1.101:3306/trackingappdb";
		Class.forName("com.mysql.jdbc.Driver").newInstance();
	//	connection = DriverManager.getConnection(connectionURL, "root", "gettoffer@2016");
		mysqlconnection = DriverManager.getConnection(connectionURL, "root", "pt001");

		}
		catch (SQLException e)
		{
		e.printStackTrace();
		}
		catch (Exception e)
		{
			e.printStackTrace();		
		}
	    return mysqlconnection;


	}

}
*/