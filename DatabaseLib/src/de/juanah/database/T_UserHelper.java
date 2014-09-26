package de.juanah.database;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;



import de.juanah.database.databaseData.T_User;

public class T_UserHelper extends BaseDatabaseHelper {

		
	
	
	//SchreibMethoden
	
	/**
	 * Schreibt einen neuen User in die Datenbank
	 * @param user
	 * @return
	 */
	public  boolean WriteUser(T_User user)
	{
		try {
			
			//Variablen
			
			Connection connect = super.GetConnection();
			
			PreparedStatement preparedStatement;
			
		     preparedStatement = connect
		             .prepareStatement("insert into  tfmbt.User values ( ?, ?, ?, ? , ?, ?)");
		         // "myuser, webpage, datum, summary, COMMENTS from FEEDBACK.COMMENTS");
		         // parameters start with 1
		     //UserUUID
		     preparedStatement.setString(1, user.getID());
		     //Username
		     preparedStatement.setString(2, user.getUsername());
		     //Email
		     preparedStatement.setString(3, user.getEmail());
		     //Password
		     preparedStatement.setString(4,user.getPassword());
		     //LoginUUID
		     preparedStatement.setString(5, user.getLoginUUID());
		     //UserdataUUID
		     preparedStatement.setString(6, user.getUserdataUUID());
		     preparedStatement.executeUpdate();
		     connect.close();
		     
			return true;
		     
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	/**
	 * Ändernt die Daten eines vorhanden Users in der Datenbank
	 * @param user Objekt
	 * @return
	 */
	public boolean UpdateUser(T_User user)
	{
		try {
			
			//Variablen
			
			Connection connect = super.GetConnection();
			
			PreparedStatement preparedStatement;
			
			
			String query2 = "update User set Username = ? , Email = ? , Password = ?, Login = ? , Userdata = ? where ID = ?";
		     preparedStatement = connect
		             .prepareStatement(query2);
		     preparedStatement.setString(1, user.getUsername());
		     preparedStatement.setString(2, user.getEmail());
		     preparedStatement.setString(3, user.getPassword());
		     preparedStatement.setString(4, user.getLoginUUID());
		     preparedStatement.setString(5, user.getUserdataUUID());
		     preparedStatement.setString(6, user.getID());

		     preparedStatement.executeUpdate();
		     connect.close();
		     
			return true;
		     
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	//Read Methoden
	
	/**
	 * Gibt alle User zurück
	 * @return Arrayliste mit alles Usern
	 */
	public  ArrayList<T_User> GetAllUser()
	{
		try {
			
			//Variablen
			
			Connection connect = super.GetConnection();
			
			PreparedStatement preparedStatement;
			ResultSet resultSet;
			
			
			//Liste die zurück gegeben wird
			ArrayList<T_User> t_UserList = new ArrayList<>();
			
			//Prüfen ob connect null ist
			if (connect ==null) {
				if (!isConnected()) {
					return null;
				} //Versuche zu verbinden wenn es nicht geht gebe null zurück
			}
			
			//Datenbank abfrage
			preparedStatement = connect.prepareStatement("select * from tfmbt.User");
			
			resultSet = preparedStatement.executeQuery();
			
			//Daten paken
			while(resultSet.next())
			{
				String tempUserUUID = resultSet.getString("ID");
				String tempUsername = resultSet.getString("Username");
				String tempEmail = resultSet.getString("Email");
				String tempPassword = resultSet.getString("Password");
				String tempLoginUUID = resultSet.getString("Login");
				String tempUserdata = resultSet.getString("Userdata");
				
				
				t_UserList.add(new T_User(tempUserUUID, tempUsername,tempEmail, tempPassword, tempLoginUUID, tempUserdata));
			}
			connect.close();
		     
			return t_UserList;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Where abfrage
	 * @param query
	 * @return gibt alle Datensätze zurück, die der abfrage entsprechen
	 */
	public ArrayList<T_User> Where(String query)
	{
		try {
			
			//Variablen
			
			Connection connect = super.GetConnection();
			
			PreparedStatement preparedStatement;
			ResultSet resultSet;
			
			
			//Liste die zurück gegeben wird
			ArrayList<T_User> t_UserList = new ArrayList<>();
			
			//Prüfen ob connect null ist
			if (connect ==null) {
				if (!isConnected()) {
					return null;
				} //Versuche zu verbinden wenn es nicht geht gebe null zurück
			}
			
			//Datenbank abfrage
			preparedStatement = connect.prepareStatement("select * from tfmbt.User where " + query);
			
			resultSet = preparedStatement.executeQuery();
			
			//Daten paken
			while(resultSet.next())
			{
				String tempUserUUID = resultSet.getString("ID");
				String tempUsername = resultSet.getString("Username");
				String tempEmail = resultSet.getString("Email");
				String tempPassword = resultSet.getString("Password");
				String tempLoginUUID = resultSet.getString("Login");
				String tempUserdata = resultSet.getString("Userdata");
				
				
				t_UserList.add(new T_User(tempUserUUID, tempUsername,tempEmail, tempPassword, tempLoginUUID, tempUserdata));
			}
			connect.close();
		     
			return t_UserList;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	public T_User GetUserByID(String userUUID)
	{
		try {
			
			//Variablen
			
			Connection connect = super.GetConnection();
			
			PreparedStatement preparedStatement;
			ResultSet resultSet;
			
			
			//User der zurück gegeben wird
			T_User t_User = null;
			
			//Prüfen ob connect null ist
			if (connect ==null) {
				if (!isConnected()) {
					return null;
				} //Versuche zu verbinden wenn es nicht geht gebe null zurück
			}
			
			//Datenbank abfrage
			preparedStatement = connect.prepareStatement("select * from tfmbt.User where ID = ?");
			preparedStatement.setString(1, userUUID);
			resultSet = preparedStatement.executeQuery();
			
			//Zähler um herrauszufinden ob es nicht 2 Usergibt mit diesem Namen (Kann eigentlich nicht sein)
			
			int counter = 0;
			
			//Daten paken
			while(resultSet.next())
			{
				counter++;
				
				String tempUserUUID = resultSet.getString("ID");
				String tempUsername = resultSet.getString("Username");
				String tempEmail = resultSet.getString("Email");
				String tempPassword = resultSet.getString("Password");
				String tempLoginUUID = resultSet.getString("Login");
				String tempUserdata = resultSet.getString("Userdata");
				
				
				t_User = new T_User(tempUserUUID, tempUsername,tempEmail, tempPassword, tempLoginUUID, tempUserdata);
			}
			
			if (counter > 1) {
				return null;
			}
			connect.close();
		     
			return t_User;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
		
	}
	
	public T_User GetUserByName(String username)
	{
		try {
			
			//Variablen
			
			Connection connect = super.GetConnection();
			
			PreparedStatement preparedStatement;
			ResultSet resultSet;
			
			
			//User der zurück gegeben wird
			T_User t_User = null;
			
			//Prüfen ob connect null ist
			if (connect ==null) {
				if (!isConnected()) {
					return null;
				} //Versuche zu verbinden wenn es nicht geht gebe null zurück
			}
			
			//Datenbank abfrage
			preparedStatement = connect.prepareStatement("select * from tfmbt.User where Username = ?");
			preparedStatement.setString(1, username);
			resultSet = preparedStatement.executeQuery();
			
			//Zähler um herrauszufinden ob es nicht 2 Usergibt mit diesem Namen (Kann eigentlich nicht sein)
			
			int counter = 0;
			
			//Daten paken
			while(resultSet.next())
			{
				counter++;
				
				String tempUserUUID = resultSet.getString("ID");
				String tempUsername = resultSet.getString("Username");
				String tempEmail = resultSet.getString("Email");
				String tempPassword = resultSet.getString("Password");
				String tempLoginUUID = resultSet.getString("Login");
				String tempUserdata = resultSet.getString("Userdata");
				
				
				t_User = new T_User(tempUserUUID, tempUsername,tempEmail, tempPassword, tempLoginUUID, tempUserdata);
			}
			
			if (counter > 1) {
				return null;
			}
			connect.close();
		     
			return t_User;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}		
	}
	
	public ArrayList<T_User> Query(String query)
	{
		try {
			
			//Variablen
			
			Connection connect = super.GetConnection();
			
			PreparedStatement preparedStatement;
			ResultSet resultSet;
			
			
			//Liste die zurück gegeben wird
			ArrayList<T_User> t_UserList = new ArrayList<>();
			
			//Prüfen ob connect null ist
			if (connect ==null) {
				if (!isConnected()) {
					return null;
				} //Versuche zu verbinden wenn es nicht geht gebe null zurück
			}
			
			//Datenbank abfrage
			preparedStatement = connect.prepareStatement("select * from tfmbt.User " + query);
			
			resultSet = preparedStatement.executeQuery();
			
			//Daten paken
			while(resultSet.next())
			{
				String tempUserUUID = resultSet.getString("ID");
				String tempUsername = resultSet.getString("Username");
				String tempEmail = resultSet.getString("Email");
				String tempPassword = resultSet.getString("Password");
				String tempLoginUUID = resultSet.getString("Login");
				String tempUserdata = resultSet.getString("Userdata");
				
				
				t_UserList.add(new T_User(tempUserUUID, tempUsername,tempEmail, tempPassword, tempLoginUUID, tempUserdata));
			}
			connect.close();
		     
			return t_UserList;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Löscht ein User Eintrag aus der Datenbank
	 * @param user der gelöscht werden soll
	 * @return
	 */
	public boolean DeleteUser(T_User user)
	{
		try {
			//Variablen 
			Connection connect = super.GetConnection();
			
			PreparedStatement preparedStatement;
			
			preparedStatement = connect
		             .prepareStatement("delete from  tfmbt.User where ID = ?");
		     //UserUUID
		     preparedStatement.setString(1, user.getID());
		     preparedStatement.executeUpdate();
		     connect.close();
		     
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	
}
