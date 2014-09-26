package de.juanah.database.databaseData;

import de.juanah.database.T_UserdataHelper;

public class T_Userdata implements TableInterface {

	//Class Variables
	private T_UserdataHelper helper = new T_UserdataHelper();
	private String UserUUID;
	private double CurrentLocationLatitude;
	private double CurrentLocationLongitude;
	
	//Constucors
	public T_Userdata(String userUUID)
	{
		this.UserUUID = userUUID;
		this.CurrentLocationLatitude = 0;
		this.CurrentLocationLongitude = 0;
	}
	
	public T_Userdata(String userUUID,double currentLocationLatitude,double currentLocationLongitude)
	{
		this.UserUUID = userUUID;
		this.CurrentLocationLatitude = currentLocationLatitude;
		this.CurrentLocationLongitude = currentLocationLongitude;
	}
	
	
	//Getter Setter
	
	public String getUserUUID() {
		return UserUUID;
	}


	public double getCurrentLocationLatitude() {
		return CurrentLocationLatitude;
	}


	public void setCurrentLocationLatitude(double currtentLoacationLatitude) {
		CurrentLocationLatitude = currtentLoacationLatitude;
	}


	public double getCurrentLocationLongitude() {
		return CurrentLocationLongitude;
	}


	public void setCurrentLocationLongitude(double currentLocationLongitude) {
		CurrentLocationLongitude = currentLocationLongitude;
	}


	public boolean SaveChanges() {
		return helper.UpdateUserdata(this);
	}

	public boolean Delete() {
		return helper.DeleteUserdata(this);
	}

}
