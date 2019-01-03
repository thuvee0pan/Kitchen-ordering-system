package db;
import java.sql.*;


public class DB_Connection {

	public static Connection getConnection() throws SQLException {
		Connection conn = null;
				try {
					Class.forName("com.mysql.jdbc.Driver");
					return conn = (Connection) DriverManager
							.getConnection("jdbc:mysql://localhost:3306/" + "kot" + "?useSSL=true", "root", "");
				}
		catch (Exception e ) {
			e.printStackTrace();
		}
		return conn;
	
	}
//	public static void main(String[] args)  throws SQLException {
//		Connection conn = null;
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//			 conn = (Connection) DriverManager
//					.getConnection("jdbc:mysql://localhost:3306/" + "kot" + "?useSSL=true", "root", "");
//		}
//		catch (Exception e ) {
//			e.printStackTrace();
//		}
//	}
	}
