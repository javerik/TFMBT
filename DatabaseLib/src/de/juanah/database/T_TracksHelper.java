package de.juanah.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import de.juanah.database.databaseData.T_Tracks;

/**
 * Helper für den Table Tracks
 * @author jonas ahlf 17.09.2014
 *
 */
public class T_TracksHelper extends BaseDatabaseHelper {

	//Schreib Methoden
	/**
	 * Schreibt einen neuen Track Eintrag in die Datenbank
	 * @param track
	 * @return erfolg oder nicht
	 */
	public boolean WriteTrack(T_Tracks track)
	{
		try {
			//Variablen 
			Connection connect = super.GetConnection();
			
			PreparedStatement preparedStatement;
			
			preparedStatement = connect
		             .prepareStatement("insert into  tfmbt.Tracks values (default, ?, ?, ?, ?)");
		         // "myuser, webpage, datum, summary, COMMENTS from FEEDBACK.COMMENTS");
		         // parameters start with 1
		     //Latitude
		     preparedStatement.setDouble(1, track.getLatitude());
		     //Longitude
		     preparedStatement.setDouble(2, track.getLongitude());
		     //UserUUID		     
		     preparedStatement.setString(3, track.getUserUUID());
		     //Time 
		     preparedStatement.setString(4, track.getTime());
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
	 * List alle Tracks Einträge aus der Datenbank aus
	 * @return Liste mit allen Tracks
	 */
	public ArrayList<T_Tracks> GetAllTracks()
	{
		try {
			//Variablen
			Connection connect = super.GetConnection();
			PreparedStatement preparedStatement;
			ResultSet resultSet;
			//Liste die zurück gegeben wird
			ArrayList<T_Tracks> t_TracksList = new ArrayList<>();
			
			//Prüfen ob connect null ist
			if (connect ==null) {
				if (!isConnected()) {
					return null;
				} //Versuche zu verbinden wenn es nicht geht gebe null zurück
			}
			
			//Datenbank abfrage
			preparedStatement = connect.prepareStatement("select * from tfmbt.Tracks");
			
			resultSet = preparedStatement.executeQuery();
			
			//Daten paken
			while(resultSet.next())
			{
				int tempidTracks = resultSet.getInt("idTracks");
				double tempLatitude = resultSet.getDouble("Latitude");
				double tempLogitude = resultSet.getDouble("Longitude");
				String tempUserUUID = resultSet.getString("UserUUID");
				String tempTime = resultSet.getString("Time");
				
				t_TracksList.add(new T_Tracks(tempidTracks, tempLatitude, tempLogitude, tempUserUUID,tempTime));
			}
			connect.close();
			return t_TracksList;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	
	public ArrayList<T_Tracks> GetTracksByArea(double[] coords,double factorLat,double factorLong)
	{
		try {
			
			//Variablen
			
			Connection connect = super.GetConnection();
			
			PreparedStatement preparedStatement;
			ResultSet resultSet;
			
			//Liste die zurück gegeben wird
			ArrayList<T_Tracks> t_TracksList = new ArrayList<>();
			
			//Prüfen ob connect null ist
			if (connect ==null) {
				if (!isConnected()) {
					return null;
				} //Versuche zu verbinden wenn es nicht geht gebe null zurück
			}
			
			//Datenbank abfrage
			String query = "select * from tfmbt.Tracks where (Latitude - ?) < ?"
					+" and (Latitude - ?) > -?  or (? - Latitude) < ? and "
					+ "(? - Latitude) > -? and "
					+ "(Longitude - ?) < ? and (Longitude - ?) > -? " 
					+" or (? - Longitude) < ? and (? - Longitude) > -?";
			preparedStatement = connect.prepareStatement(query);
			
			preparedStatement.setDouble(1, coords[0]);
			preparedStatement.setDouble(2, factorLat);
			preparedStatement.setDouble(3, coords[0]);
			preparedStatement.setDouble(4, factorLat);
			
			
			preparedStatement.setDouble(5, coords[0]);
			preparedStatement.setDouble(6, factorLat);
			preparedStatement.setDouble(7, coords[0]);
			preparedStatement.setDouble(8, factorLat);
			
			preparedStatement.setDouble(9, coords[1]);
			preparedStatement.setDouble(10, factorLong);
			preparedStatement.setDouble(11, coords[1]);
			preparedStatement.setDouble(12, factorLong);
			
			preparedStatement.setDouble(13, coords[1]);
			preparedStatement.setDouble(14, factorLong);
			preparedStatement.setDouble(15, coords[1]);
			preparedStatement.setDouble(16, factorLong);
			
			resultSet = preparedStatement.executeQuery();
			
			//Daten paken
			while(resultSet.next())
			{
				int tempidTracks = resultSet.getInt("idTracks");
				double tempLatitude = resultSet.getDouble("Latitude");
				double tempLongitude = resultSet.getDouble("Longitude");
				String tempUserUUID = resultSet.getString("UserUUID");
				String tempTime = resultSet.getString("Time");
				
				
				t_TracksList.add(new T_Tracks(tempidTracks, tempLatitude, tempLongitude, tempUserUUID, tempTime));
			}
			connect.close();
		     
			return t_TracksList;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Gibt alle Tracks Einträge zurück die der query entsprechen
	 * @param query
	 * @return
	 */
	public ArrayList<T_Tracks> GetAllTracksByQuery(String query)
	{
		try {
			
			//Variablen
			
			Connection connect = super.GetConnection();
			
			PreparedStatement preparedStatement;
			ResultSet resultSet;
			
			//Liste die zurück gegeben wird
			ArrayList<T_Tracks> t_TracksList = new ArrayList<>();
			
			//Prüfen ob connect null ist
			if (connect ==null) {
				if (!isConnected()) {
					return null;
				} //Versuche zu verbinden wenn es nicht geht gebe null zurück
			}
			
			//Datenbank abfrage
			preparedStatement = connect.prepareStatement("select * from tfmbt.Tracks where " + query);
			
			resultSet = preparedStatement.executeQuery();
			
			//Daten paken
			while(resultSet.next())
			{
				int tempidTracks = resultSet.getInt("idTracks");
				double tempLatitude = resultSet.getDouble("Latitude");
				double tempLongitude = resultSet.getDouble("Longitude");
				String tempUserUUID = resultSet.getString("UserUUID");
				Date tempTime = resultSet.getDate("Time");
				
				
				t_TracksList.add(new T_Tracks(tempidTracks, tempLatitude, tempLongitude, tempUserUUID, tempTime.toString()));
			}
			connect.close();
		     
			return t_TracksList;
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

	/**
	 * Gibt alle Tracks eines bestimmten Users zurück
	 * @param userUUID UUID des Nutzers
	 * @return Liste mit den Tracks
	 */
	public ArrayList<T_Tracks> GetAllTracksByUserID(String userUUID)
	{
		try {
			//Variablen
			Connection connect = super.GetConnection();
			PreparedStatement preparedStatement;
			ResultSet resultSet;
			//Liste die zurück gegeben wird
			ArrayList<T_Tracks> t_TracksList = new ArrayList<>();
			//Prüfen ob connect null ist
			if (connect ==null) {
				if (!isConnected()) {
					return null;
				} //Versuche zu verbinden wenn es nicht geht gebe null zurück
			}
			//Datenbank abfrage
			preparedStatement = connect.prepareStatement("select * from tfmbt.Tracks where UserUUID = ?");
			preparedStatement.setString(1, userUUID);
			resultSet = preparedStatement.executeQuery();
			//Daten paken
			while(resultSet.next())
			{
				int tempidTracks = resultSet.getInt("idTracks");
				double tempLatitude = resultSet.getDouble("Latitude");
				double tempLogitude = resultSet.getDouble("Longitude");
				String tempUserUUID = resultSet.getString("UserUUID");
				String tempTime = resultSet.getString("Time");
				
				t_TracksList.add(new T_Tracks(tempidTracks, tempLatitude, tempLogitude, tempUserUUID,tempTime));
			}
			connect.close();
			return t_TracksList;
		}catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Löscht ein Tracks Eintrag aus der Datenbank
	 * @param track
	 * @return erfolg oder nicht
	 */
	public boolean DeleteTrack(T_Tracks track)
	{
			try {
				//Variablen 
				Connection connect = super.GetConnection();
				
				PreparedStatement preparedStatement;
				
				preparedStatement = connect
			             .prepareStatement("delete from  tfmbt.Tracks where idTracks = ?");
			     //idTracks
			     preparedStatement.setInt(1, track.getIdTrack());		
			     preparedStatement.executeUpdate();
			     connect.close();
			     
				return true;
			} catch (Exception e) {
				System.out.println(e.getMessage());
				return false;
			}
	}
}
