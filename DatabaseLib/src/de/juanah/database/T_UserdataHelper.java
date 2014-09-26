package de.juanah.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import de.juanah.database.databaseData.T_Userdata;

/**
 * Helper für den Table Userdata
 * @author jonas ahlf 17.09.2014
 *
 */
public class T_UserdataHelper extends BaseDatabaseHelper {

	//Schreib Methoden
	
	/**
	 * Schreibt ein neuen Userdata Eintrag in die Datenbank
	 * @param userData
	 * @return erfolg oder nicht
	 */
	public boolean WriteUserdata(T_Userdata userData)
	{
		try {
			//Variablen 
			Connection connect = super.GetConnection();
			
			PreparedStatement preparedStatement;
			
			preparedStatement = connect
		             .prepareStatement("insert into  tfmbt.UserData values ( ?, ?, ?)");
		         // "myuser, webpage, datum, summary, COMMENTS from FEEDBACK.COMMENTS");
		         // parameters start with 1
		     //UserUUID
		     preparedStatement.setString(1, userData.getUserUUID());
		     //Latitude
		     preparedStatement.setDouble(2, userData.getCurrentLocationLatitude());
		     //Logitude
		     preparedStatement.setDouble(3, userData.getCurrentLocationLongitude());
		     preparedStatement.executeUpdate();
		     connect.close();
		     
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
	/**
	 * Ändert die daten des übergebenden Objekts in der Datenbank
	 * @param userData objekt welches in der Datenbank geupdated wird
	 * @return erfolg oder nicht
	 */
	public boolean UpdateUserdata(T_Userdata userData)
	{
		try {
			//Variablen
			
			Connection connect = super.GetConnection();
			
			PreparedStatement preparedStatement;
			
			
			String query2 = "update UserData set  CurrentLocationLatitude = ?, CurrentLocationLongitude = ?  where UserUUID = ?";
		     preparedStatement = connect
		             .prepareStatement(query2);
		     //Latitude
		     preparedStatement.setDouble(1, userData.getCurrentLocationLatitude());
		     //Longitude
		     preparedStatement.setDouble(2, userData.getCurrentLocationLongitude());
		     //UserUUID
		     preparedStatement.setString(3, userData.getUserUUID());
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
	 * 
	 * @return gibt alle Userdata Einträge aus der Datenbank zurück
	 */
	public ArrayList<T_Userdata> GetAllUserdata()
	{
		try {
			
			//Variablen
			
			Connection connect = super.GetConnection();
			
			PreparedStatement preparedStatement;
			ResultSet resultSet;
			
			
			//Liste die zurück gegeben wird
			ArrayList<T_Userdata> t_UserdataList = new ArrayList<>();
			
			//Prüfen ob connect null ist
			if (connect ==null) {
				if (!isConnected()) {
					return null;
				} //Versuche zu verbinden wenn es nicht geht gebe null zurück
			}
			
			//Datenbank abfrage
			preparedStatement = connect.prepareStatement("select * from tfmbt.UserData");
			
			resultSet = preparedStatement.executeQuery();
			
			//Daten paken
			while(resultSet.next())
			{
				String tempUserUUID = resultSet.getString("UserUUID");
				double tempLatitude = resultSet.getDouble("CurrentLocationLatitude");
				double tempLongitude = resultSet.getDouble("CurrentLocationLongitude");
				
				
				t_UserdataList.add(new T_Userdata(tempUserUUID, tempLatitude, tempLongitude));
			}
			connect.close();
		     
			return t_UserdataList;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	/**
	 * Gibt den Userdata Eintrag mit der passenden UUID zurück
	 * @param userUUID 
	 * @return Userdata Eintrag mit der passenden UUID zurück
	 */
	public T_Userdata GetUserdataByUserUUID(String userUUID)
	{
		try {
			
			//Variablen
			
			Connection connect = super.GetConnection();
			
			T_Userdata userData = null;
			
			PreparedStatement preparedStatement;
			ResultSet resultSet;
			
			//Prüfen ob connect null ist
			if (connect ==null) {
				if (!isConnected()) {
					return null;
				} //Versuche zu verbinden wenn es nicht geht gebe null zurück
			}
			
			//Datenbank abfrage
			preparedStatement = connect.prepareStatement("select * from tfmbt.UserData where UserUUID = ?");
			preparedStatement.setString(1, userUUID);
			
			resultSet = preparedStatement.executeQuery();
			
			//Daten paken
			while(resultSet.next())
			{
				String tempUserUUID = resultSet.getString("UserUUID");
				double tempLatitude = resultSet.getDouble("CurrentLocationLatitude");
				double tempLongitude = resultSet.getDouble("CurrentLocationLongitude");
				
				
				userData = new T_Userdata(tempUserUUID, tempLatitude, tempLongitude);
			}
			connect.close();
		     
			return userData;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Löscht ein Userdata Eintrag aus der Datenbank
	 * @param userData welches gelöscht werden soll
	 * @return erfolg oder nicht
	 */
	public boolean DeleteUserdata(T_Userdata userData)
	{
		try {
			//Variablen 
			Connection connect = super.GetConnection();
			
			PreparedStatement preparedStatement;
			
			preparedStatement = connect
		             .prepareStatement("delete from  tfmbt.UserData where UserUUID = ?");
		     //idTracks
		     preparedStatement.setString(1, userData.getUserUUID());		
		     preparedStatement.executeUpdate();
		     connect.close();
		     
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return false;
		}
	}
	
}
