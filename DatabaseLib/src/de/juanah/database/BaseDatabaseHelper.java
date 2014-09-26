package de.juanah.database;


import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Basis Datenbank helper
 * @author jonas ahlf 15.09.2014
 *
 */
public class BaseDatabaseHelper {

	/**
	 * Class Variables
	 */
	
	private static final String Username = "server";
	private static final String Password = "040123";
	
	 private static Connection connect = null;
	 
	 /**
	  * Prüft die Verbindung zur Datenbank
	  * @return
	  */
	 public static boolean isConnected()
	 {
		 try {
			Class.forName("com.mysql.jdbc.Driver");
			
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/tfmbt?" +
							"user=" + Username + "&password=" + Password);
			
			if (connect != null) {
				return true;
			}else
			{
				return false;
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
		 
		 
	 }
	 
	 /**
	  * Gibt ein Connection Objekt zurück
	  * @return Connection Object
	  */
	 public static Connection GetConnection()
	 {
		 try {
			Class.forName("com.mysql.jdbc.Driver");
			
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/tfmbt?" +
							"user=" + Username + "&password=" + Password);
			
			if (connect != null) {
				return connect;
			}else
			{
				return null;
			}
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	 }
	 

	
}
