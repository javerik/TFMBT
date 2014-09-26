package de.juanah.trackingformybullentime.userdata;

import java.util.ArrayList;

import de.juanah.trackingformybullentime.track.TrackData;

/**
 * Alle Userdata werden hier gesammelt
 * @author jonas ahlf 24.09.2014
 *
 */
public class UserData {

	private String Username;
	private String Email;
	
	private TrackData LastTrack;
	
	private ArrayList<TrackData> AllTracks;
	
	public UserData(String username,String email)
	{
		setUsername(username);
		setEmail(email);
		setAllTracks(new ArrayList<TrackData>());
	}
	
	public UserData(String username,String email,TrackData lastTrack)
	{
		setUsername(username);
		setEmail(email);
		setLastTrack(lastTrack);
		setAllTracks(new ArrayList<TrackData>());
	}
	
	public UserData(String username,String email,TrackData lastTrack,ArrayList<TrackData> allTracks)
	{
		setUsername(username);
		setEmail(email);
		setLastTrack(lastTrack);
		setAllTracks(allTracks);
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public TrackData getLastTrack() {
		return LastTrack;
	}

	public void setLastTrack(TrackData lastTrack) {
		LastTrack = lastTrack;
	}

	public ArrayList<TrackData> getAllTracks() {
		return AllTracks;
	}

	public void setAllTracks(ArrayList<TrackData> allTracks) {
		AllTracks = allTracks;
	}
	

	
	
}
