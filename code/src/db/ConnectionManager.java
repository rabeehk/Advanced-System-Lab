package db;

import java.sql.Connection;
import java.util.Properties;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
	private static String URL;
	// set the URL of the database
	public static void setURL(String URL){
		ConnectionManager.URL = URL;
		//System.out.println(ConnectionManager.URL);
	}
	// get the database URL
	public static String getURL(){
		return ConnectionManager.URL;
	}
	// get connection to the database
	public static Connection getConnection() {
		//ConnectionManager.URL="jdbc:postgresql://localhost:5432/db?user=rabeehk&password=AllahoSamad@))%";
		//String url = new String("jdbc:postgresql://localhost:5432/db");
		//String url = new String("jdbc:postgresql://localhost:1084/db");
		/*Properties props = new Properties();
		props.setProperty("user",  "postgres");//new String(System.getenv("USER")));//"postgres");
		props.setProperty("password", "AllahoSamad@))%"); //new String(System.getenv("PASS")));//"AllahoSamad@))%");*/
		
		try {
			return DriverManager.getConnection(ConnectionManager.URL); 
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
