package SQLdatabase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import dao.Common;
public class SQLDatabase {



	public static Connection getConnection() throws SQLException, ClassNotFoundException{
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");	
		Connection conn = DriverManager.getConnection(Common.SQL_Connection);


		/***
		 * This is Only for Testing purpose
		 * 
		 */
		/*try{
			System.out.println("test");
			java.sql.Statement sta = conn.createStatement();
			String Sql = "select * from dbo.Category";
			ResultSet rs = sta.executeQuery(Sql);
			while (rs.next()) {
				System.out.println(rs.getString("Category_Name"));
			}
		}catch(Exception e){
			e.printStackTrace();
		}*/

		return conn;
	}
}
