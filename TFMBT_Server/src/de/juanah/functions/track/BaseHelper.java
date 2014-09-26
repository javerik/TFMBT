package de.juanah.functions.track;

import java.util.ArrayList;
import java.util.UUID;

import de.juanah.communicationPakages.Init;
import de.juanah.communicationPakages.MDP;
import de.juanah.communicationPakages.MIP;
import de.juanah.communicationPakages.data.DataTypes;
import de.juanah.database.T_TracksHelper;
import de.juanah.database.T_UserHelper;
import de.juanah.database.databaseData.T_Tracks;
import de.juanah.database.databaseData.T_User;
import de.juanah.service.connection.MessageAnswer;

/**
 * Behandelt alle Track Anfragen
 * @author jonas ahlf 19.09.2014
 *
 *Change 22.09.2014
 *Track enthält nun userUUID und Zeit
 *
 */
public class BaseHelper {

	/**
	 * Class Variables
	 */
	private static T_TracksHelper _tracksHelper = new T_TracksHelper();
	
	private static T_UserHelper _userHelper = new T_UserHelper();
	
	
	private static final double midAreaLat = 0.021;
	private static final double midAreaLong = 0.051;
	
	/**
	 * Einkommende Tracks
	 */
	
	/**
	 * Schreibt einen Track in die Datenbank ohne besonderen Parametern
	 * enthält folgende Informationen "loingUUID<latitude;longitude<time"
	 * 
	 * Aufbau SimpleTrack String =
	 * "userUUID<Time<lat;long
	 * 
	 * @param mdp
	 * @return
	 */
	private static MessageAnswer SimpleTrack(MDP mdp)
	{
		
		String dataString = new String(mdp.getData());
		
		try {
				
				String[] userSplit = dataString.split("<");
				
				String userUUID = userSplit[0];
				String time = userSplit[1];
				String[] coords = userSplit[2].split(";");
				T_User user = _userHelper.GetUserByID(userUUID);
				
				//Casten der Coordinaten
				
				double lat = Double.parseDouble(coords[0]);
				double lon = Double.parseDouble(coords[1]);
				
				T_Tracks newTrack;
				
				//Falls kein User Eintrag vorhanden ist, wird die ID direkt abgespeichert
				if (user == null) {
					 newTrack = new T_Tracks(lat, lon, userUUID, time);
				}else
				{
					 newTrack = new T_Tracks(lat, lon, user.getID(), time);
				}
				
				if (!_tracksHelper.WriteTrack(newTrack)) {
					return GetFalseAnswer();
				} else
				{
					return GetSuccessAnswer();
				}
			
			
		} catch (Exception e) {
			Write(e.getMessage());
			return GetFalseAnswer();
		}
		
	}
	
	
	/**
	 * Gibt die Antwort auf ein Einkommenden Track
	 * @param mdp
	 * @return
	 */
	public static MessageAnswer GetTrackAnswer(MDP mdp)
	{
		return SimpleTrack(mdp);
		
	}
	
	/**
	 * Ausgehende Koordinaten
	 */
	
	private static MessageAnswer GetSimpleCoords(MDP mdp)
	{
		
		String dataString = new String(mdp.getData());
		
		try {
			
			if (dataString.equals("ALL")) {
				return GetAllCoords();
			}
			
			String[] coordStrings = dataString.split(";");
			
			double lat = Double.parseDouble(coordStrings[0]);
			double lon = Double.parseDouble(coordStrings[1]);
			
			//Hole alle Koordinaten, die sich in der nähe befinden
			
			ArrayList<T_Tracks> tracks = _tracksHelper.GetTracksByArea(new double[]{lat, lon}, midAreaLat,midAreaLong);
			
			if (tracks.size() == 0) {
				return GetNoDataAnswer();
			}
			
			return GetCoordMessageAnswer(tracks);
		} catch (Exception e) {
			Write(e.getMessage());
			return GetFalseAnswer();
		}
		
	}
	
	private static MessageAnswer GetAllCoords()
	{
		ArrayList<T_Tracks> tracks = _tracksHelper.GetAllTracks();
		
		if (tracks.size() == 0) {
			return GetNoDataAnswer();
		}
		return GetCoordMessageAnswer(tracks);
	}
	
	/**
	 * Gibt ein MessageAnswer Objekt zurück, welches die Coordinaten beinhaltet welche sich in der Nähe des nutzers befinden
	 * @param mdp
	 * @return
	 */
	public static MessageAnswer GetTracks(MDP mdp)
	{
		return GetSimpleCoords(mdp);
	}
	
	private static MessageAnswer GetCoordMessageAnswer(ArrayList<T_Tracks> coords)
	{
		//Stringaufbau "lat;lon#time#user
		
		String messageUUID = UUID.randomUUID().toString();
		
		Init tempInit = new Init(messageUUID);
		
		MIP tempMIP = new MIP("",0,true,messageUUID,"gettrack",DataTypes.Datatype.string,100);
		
		String dataString = "";
		
		for (T_Tracks ds : coords) {
			
			String tempLat = String.valueOf(ds.getLatitude());
			String tempLong = String.valueOf(ds.getLongitude());
			
			String tempdataString = "<" + tempLat + ";" + tempLong + "#" +ds.getTime();
			
			//Versuchen den User auszulesen
			
			T_User tempUser = _userHelper.GetUserByID(ds.getUserUUID());
			
			if (tempUser != null) {
				tempdataString += "#" + tempUser.getUsername();
			}
			

			dataString += tempdataString;
		}
		
		MDP tempMDP = new MDP(dataString.getBytes());
		
		return new MessageAnswer(tempInit, tempMIP, tempMDP);
	}
	
	private static MessageAnswer GetFalseAnswer()
	{
		
		String messageUUID = UUID.randomUUID().toString();
		
		Init tempInit = new Init(messageUUID);
		
		MIP tempMIP = new MIP("",0,true,messageUUID,"track",DataTypes.Datatype.string,100);
		
		MDP tempMDP = new MDP("false".getBytes());
		
		return new MessageAnswer(tempInit, tempMIP, tempMDP);
		
	}
	
	private static MessageAnswer GetNoDataAnswer()
	{
		String messageUUID = UUID.randomUUID().toString();
		
		Init tempInit = new Init(messageUUID);
		
		MIP tempMIP = new MIP("",0,true,messageUUID,"track",DataTypes.Datatype.string,100);
		
		MDP tempMDP = new MDP("nodata".getBytes());
		
		return new MessageAnswer(tempInit, tempMIP, tempMDP);
		
	}
	
	private static MessageAnswer GetSuccessAnswer()
	{
		String messageUUID = UUID.randomUUID().toString();
		
		Init tempInit = new Init(messageUUID);
		
		MIP tempMIP = new MIP("",0,true,messageUUID,"track",DataTypes.Datatype.string,100);
		
		MDP tempMDP = new MDP(("success").getBytes());
		
		return new MessageAnswer(tempInit, tempMIP, tempMDP);
		
	}
	
	
	/**
	 * Schreibt Text in die Konsole
	 * @param text
	 */
	private static void Write(String text)
	{
		System.out.println(text);
	}
	
}
