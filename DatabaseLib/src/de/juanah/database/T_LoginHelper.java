package de.juanah.database;


import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import de.juanah.database.databaseData.T_Login;

/**
 * Helper für den Table T_Login
 * @author jonas ahlf 17.09.2014
 *Changelog
 *19.09.2014 ahlf
 *		Date zu String geändert
 */
public class T_LoginHelper extends BaseDatabaseHelper {
	
	
	
	//Schreib Methoden
	
	/**
	 * Schreibt einen neuen Login in die Datenbank
	 * @param login
	 * @return Erfolg = true, Misserfolg = false
	 */
	public boolean WriteLogin(T_Login login)
	{
		try {
			//Variablen 
			Connection connect = super.GetConnection();
			
			PreparedStatement preparedStatement;
			
			preparedStatement = connect
		             .prepareStatement("insert into  tfmbt.Login values ( ?, ?)");
		         // "myuser, webpage, datum, summary, COMMENTS from FEEDBACK.COMMENTS");
		         // parameters start with 1
		     //UserUUID
		     preparedStatement.setString(1, login.getUUID());
		     //Zeit
		     preparedStatement.setString(2, login.getDTime());
		     preparedStatement.executeUpdate();
			
		     connect.close();
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	/**
	 * Ändert ein Login Eintrag in der Datenbank
	 * @param login
	 * @return erfolg oder nicht
	 */
	public boolean UpdateLogin(T_Login login)
	{
		try {
			//Variablen
			
			Connection connect = super.GetConnection();
			
			PreparedStatement preparedStatement;
			
			
			String query2 = "update Login set  LoginDate = ?  where UUID = ?";
		     preparedStatement = connect
		             .prepareStatement(query2);
		     //Zeit
		     preparedStatement.setString(1, login.getDTime());
		     //UserUUID
		     preparedStatement.setString(2, login.getUUID());
		     preparedStatement.executeUpdate();
		     connect.close();
		     
			return true;
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	//Lese Methoden
	/**
	 * Gibt alle Login Einträge in der Datenbank zurück
	 * @return ArrayListe mit allen Datenbank Einträgen
	 */
	public ArrayList<T_Login> GetAllLogin()
	{
		try {
			
			//Variablen
			
			Connection connect = super.GetConnection();
			
			PreparedStatement preparedStatement;
			ResultSet resultSet;
			
			
			//Liste die zurück gegeben wird
			ArrayList<T_Login> t_LoginList = new ArrayList<>();
			
			//Prüfen ob connect null ist
			if (connect ==null) {
				if (!isConnected()) {
					return null;
				} //Versuche zu verbinden wenn es nicht geht gebe null zurück
			}
			
			//Datenbank abfrage
			preparedStatement = connect.prepareStatement("select * from tfmbt.Login");
			
			resultSet = preparedStatement.executeQuery();
			
			//Daten paken
			while(resultSet.next())
			{
				String tempUserUUID = resultSet.getString("UUID");
				String tempDate = resultSet.getString("LoginDate");
				
				
				t_LoginList.add(new T_Login(tempUserUUID, tempDate));
			}
			connect.close();
		     
			return t_LoginList;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Gibt Logins zurück mit die eine UserUUID Pflegen
	 * @param UUID
	 * @return
	 */
	public ArrayList<T_Login> GetAllLoginsByUUID(String UUID)
	{
		try {
			
			//Variablen
			
			Connection connect = super.GetConnection();
			
			PreparedStatement preparedStatement;
			ResultSet resultSet;
			
			
			//Liste die zurück gegeben wird
			ArrayList<T_Login> t_LoginList = new ArrayList<>();
			
			//Prüfen ob connect null ist
			if (connect ==null) {
				if (!isConnected()) {
					return null;
				} //Versuche zu verbinden wenn es nicht geht gebe null zurück
			}
			
			//Datenbank abfrage
			preparedStatement = connect.prepareStatement("select * from tfmbt.Login where UUID = ?");
			preparedStatement.setString(1, UUID);
			
			resultSet = preparedStatement.executeQuery();
			
			//Daten paken
			while(resultSet.next())
			{
				String tempUserUUID = resultSet.getString("UUID");
				String tempDate = resultSet.getString("LoginDate");
				
				
				t_LoginList.add(new T_Login(tempUserUUID, tempDate));
			}
			connect.close();
		     
			return t_LoginList;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Gibt alle Login Einträge zurück, die, die parameter einhalten
	 * @param UUID UserUUID
	 * @param date größergleich als die gesuchten
	 * @return
	 */
	public ArrayList<T_Login> GetAllLoginsByUUIDAndDate(String UUID,Date date)
	{
		try {
			
			//Variablen
			
			Connection connect = super.GetConnection();
			
			PreparedStatement preparedStatement;
			ResultSet resultSet;
			
			
			//Liste die zurück gegeben wird
			ArrayList<T_Login> t_LoginList = new ArrayList<>();
			
			//Prüfen ob connect null ist
			if (connect ==null) {
				if (!isConnected()) {
					return null;
				} //Versuche zu verbinden wenn es nicht geht gebe null zurück
			}
			
			//Datenbank abfrage
			preparedStatement = connect.prepareStatement("select * from tfmbt.Login where UUID = ? and Time >= ?");
			preparedStatement.setString(1, UUID);
			preparedStatement.setDate(2, date);
			resultSet = preparedStatement.executeQuery();
			
			//Daten paken
			while(resultSet.next())
			{
				String tempUserUUID = resultSet.getString("UUID");
				String tempDate = resultSet.getString("Time");
				t_LoginList.add(new T_Login(tempUserUUID, tempDate));
			}
			connect.close();
		     
			return t_LoginList;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Löscht einen Login Eintrag aus der Datenbank
	 * @param login welches gelöscht werden soll
	 * @return erfolg oder nicht 
	 */
	public boolean DeleteLogin(T_Login login)
	{
		try {
			//Variablen 
			Connection connect = super.GetConnection();
			
			PreparedStatement preparedStatement;
			
			preparedStatement = connect
		             .prepareStatement("delete from  tfmbt.Login where UUID = ?");
		     //idTracks
		     preparedStatement.setString(1, login.getUUID());		
		     preparedStatement.executeUpdate();
		     connect.close();
		     
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
}
