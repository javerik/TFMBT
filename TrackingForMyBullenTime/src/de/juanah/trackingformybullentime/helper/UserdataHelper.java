package de.juanah.trackingformybullentime.helper;

import java.util.ArrayList;

import de.juanah.communicationPakages.MDP;
import de.juanah.trackingformybullentime.track.TrackData;
import de.juanah.trackingformybullentime.userdata.UserData;

/**
 * Handelt alles ab, was mit Userdata zutun hat
 * @author jonas ahlf 24.09.2014
 *
 */
public class UserdataHelper {

	/**
	 * Holt die Userdata vom server
	 * @param userUUID vom aktuellen Client
	 * @return UserData Objekt
	 */
	public static UserData GetUserData(String userUUID,String loginUUID)
	{
		try {
			de.juanah.trackingformybullentime.network.BaseHelper.SendPackage("getuserdata",
					new MDP((userUUID + "<" + loginUUID).getBytes()));
			
			MDP recivedMdp = de.juanah.trackingformybullentime.network.BaseHelper.RecivePacket();
			
			if (recivedMdp == null) {
				return null;
			}
			return BuildUserData(recivedMdp);
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Baut aus den Empfangenen Daten UserData zusammen
	 * @param mdp
	 * @return Userdata Objekt
	 */
	private static UserData BuildUserData(MDP mdp)
	{
		String dataString = new String(mdp.getData());
		
		if (dataString.equals("ERROR")) {
			return null;
		}
		try {
			String[] attributes = dataString.split("<");
			
			String username = attributes[0];
			
			String email = attributes[1];
			
			String lastTrackString = attributes[2];
			
			String allTracks = attributes[3];
			
			String[] lastTrackAttributes = lastTrackString.split(";");
			
			double lastTrackLat = Double.parseDouble(lastTrackAttributes[0]);
			double lastTrackLong = Double.parseDouble(lastTrackAttributes[1]);
			
			TrackData lastTrack = new TrackData(lastTrackLat, lastTrackLong, username);
			
			ArrayList<TrackData> tracks = new ArrayList<TrackData>();
			
			if (!allTracks.equals("")) {
				
				String[] trackStrings = allTracks.split("§");
				
				for (String trackString : trackStrings) {
					
					String[] tempAttributes = trackString.split(";");
					
					double tempLat = Double.parseDouble(tempAttributes[0]);
					double tempLong = Double.parseDouble(tempAttributes[1]);
					tracks.add(new TrackData(tempLat, tempLong, username));
				}
			}
			
			return new UserData(username, email, lastTrack, tracks);
		} catch (Exception e) {
			return null;
		}
		
	}
	
}
