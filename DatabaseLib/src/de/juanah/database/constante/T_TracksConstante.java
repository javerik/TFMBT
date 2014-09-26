package de.juanah.database.constante;

/**
 * Definiert die Colums vom Table Tracks
 * @author jonas ahlf 16.09.2014
 *
 */
public class T_TracksConstante {

	private final String Id = "idTracks";
	private final String Latitude = "Latitude";
	private final String Longitude = "Longitude";
	private final String UserUUID = "UserUUID";
	private final String Time = "Time";
	
	
	public String getId() {
		return Id;
	}
	public String getLatitude() {
		return Latitude;
	}
	public String getLongitude() {
		return Longitude;
	}
	public String getUserUUID() {
		return UserUUID;
	}
	public String getTime() {
		return Time;
	}
	
}
