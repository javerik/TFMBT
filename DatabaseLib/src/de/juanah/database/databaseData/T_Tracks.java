package de.juanah.database.databaseData;

import java.sql.Date;

import de.juanah.database.T_TracksHelper;

public class T_Tracks implements TableInterface {

	//Class variables
	private T_TracksHelper helper = new T_TracksHelper();
	
	private int idTrack;
	private double Latitude;
	private double Longitude;
	private String UserUUID;
	private String Time;
	
	//Constructor
	
	public T_Tracks(int idTrack,double latitude,double longitude,String userUUID,String time)
	{
		this.idTrack = idTrack;
		this.Latitude = latitude;
		this.Longitude = longitude;
		this.UserUUID = userUUID;
		this.Time = time;
	}
	
	public T_Tracks(double latitude,double longitude,String userUUID,String time)
	{
		this.idTrack = -1;
		this.Latitude = latitude;
		this.Longitude = longitude;
		this.UserUUID = userUUID;
		this.Time = time;
	}
	
	public T_Tracks(double latitude,double longitude,String time)
	{
		this.idTrack = -1;
		this.Latitude = latitude;
		this.Longitude = longitude;
		this.UserUUID = "";
		this.Time = time;
	}
	
	
	//Getter Setter
	
	public int getIdTrack() {
		return idTrack;
	}


	public double getLatitude() {
		return Latitude;
	}

	public void setLatitude(double latitude) {
		Latitude = latitude;
	}

	public double getLongitude() {
		return Longitude;
	}

	public void setLongitude(double longitude) {
		Longitude = longitude;
	}

	public String getUserUUID() {
		return UserUUID;
	}

	public void setUserUUID(String userUUID) {
		UserUUID = userUUID;
	}

	public String getTime() {
		return Time;
	}

	public void setTime(String time) {
		Time = time;
	}
	
	public boolean SaveChanges() {
		
		return false;
	}

	public boolean Delete() {
		return helper.DeleteTrack(this);
	}



}
