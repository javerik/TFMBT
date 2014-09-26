package de.juanah.trackingformybullentime.track;

/**
 * Representiert einen Track
 * @author jonas ahlf 24.09.2014
 *
 */
public class TrackData {

	private double Lat;
	private double Long;
	
	private String Time;
	private String User;
	
	
	public TrackData(double lat, double lonG,String user)
	{
		setLat(lat);
		setLong(lonG);
		setUser(user);
	}
	
	public TrackData(double lat, double lonG,String time,String user)
	{
		setLat(lat);
		setLong(lonG);
		setTime(time);
		setUser(user);
	}
	

	
	public double getLat() {
		return Lat;
	}
	public void setLat(double lat) {
		Lat = lat;
	}
	public double getLong() {
		return Long;
	}
	public void setLong(double _long) {
		Long = _long;
	}
	public String getTime() {
		return Time;
	}
	public void setTime(String time) {
		Time = time;
	}
	public String getUser() {
		return User;
	}
	public void setUser(String user) {
		User = user;
	}
	

	
	
}
