package de.juanah.functions.userdata;

import java.util.ArrayList;
import java.util.UUID;

import de.juanah.communicationPakages.Init;
import de.juanah.communicationPakages.MDP;
import de.juanah.communicationPakages.MIP;
import de.juanah.communicationPakages.data.DataTypes;
import de.juanah.database.T_TracksHelper;
import de.juanah.database.T_UserHelper;
import de.juanah.database.T_UserdataHelper;
import de.juanah.database.databaseData.T_Tracks;
import de.juanah.database.databaseData.T_User;
import de.juanah.database.databaseData.T_Userdata;
import de.juanah.service.connection.MessageAnswer;

/**
 * Behandelt alles was mit User daten zutun hat
 * @author jonas ahlf 24.09.2014
 *
 */
public class UserdataHelper {
	
	//Class Variables
	private static T_UserHelper _userHelper = new T_UserHelper();
	private static T_UserdataHelper _userDataHelper = new T_UserdataHelper();
	private static T_TracksHelper _tracksHelper = new T_TracksHelper();
	
	/**
	 * Gibt die Userdaten, eines bestimmten users wieder
	 * 
	 * StringAufbau
	 * Recive
	 * userUUID<loginUUID 
	 * Return
	 * username<email<lastTrackLat;lastTrackLong<tracksLat;tracksLong§tracksLat;tracksLong
	 * 
	 * @param mdp userUUID,LoginUUID vom Client, wenn leer dann muss neu eingeloggt werden
	 * @return Antwort mit den User daten oder mit Fehlermeldung
	 */
	public static MessageAnswer GetUserData(MDP mdp)
	{
		String dataString = new String(mdp.getData());
		
		try {
			
			String[] attributes = dataString.split("<"); 
			
			T_User user = _userHelper.GetUserByID(attributes[0]);
			
			if (user == null) {
				return GetErrorMessage();
			}
			
			//Abfrage der loginUUID
			if (user.getLoginUUID().equals(attributes[1])) {
				return GetNotLoggedInMessage();
			}
			
			T_Userdata userData = _userDataHelper.GetUserdataByUserUUID(user.getID());
			
			ArrayList<T_Tracks> tracks = _tracksHelper.GetAllTracksByUserID(user.getID());
			
			//Packen der Daten
			String returnString = user.getUsername() + 
					"<" + user.getEmail() +
					"<" + userData.getCurrentLocationLatitude() + ";" + userData.getCurrentLocationLongitude() + "<";
					
			for (T_Tracks t_Tracks : tracks) {
				String tempTrackString = t_Tracks.getLatitude() + ";" + t_Tracks.getLongitude()  + "§";
				returnString += tempTrackString;
			}
			
			return GetDataMessage(returnString);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return GetErrorMessage();
		}
		
	}
	
	
	//MessageAnswer Methoden
	
	private static MessageAnswer GetErrorMessage()
	{
		String messageUUID = UUID.randomUUID().toString();
		
		Init tempInit = new Init(messageUUID);
		
		MIP tempMIP = new MIP("",0,true,messageUUID,"userdata",DataTypes.Datatype.string,100);
		
		MDP  tempMDP = new MDP("ERROR".getBytes());
		
		return new MessageAnswer(tempInit, tempMIP, tempMDP);
	}
	
	private static MessageAnswer GetNotLoggedInMessage()
	{
		String messageUUID = UUID.randomUUID().toString();
		
		Init tempInit = new Init(messageUUID);
		
		MIP tempMIP = new MIP("",0,true,messageUUID,"userdata",DataTypes.Datatype.string,100);
		
		MDP  tempMDP = new MDP("notloggedin".getBytes());
		
		return new MessageAnswer(tempInit, tempMIP, tempMDP);
	}
	
	private static MessageAnswer GetDataMessage(String data)
	{
		String messageUUID = UUID.randomUUID().toString();
		
		Init tempInit = new Init(messageUUID);
		
		MIP tempMIP = new MIP("",0,true,messageUUID,"userdata",DataTypes.Datatype.string,100);
		
		MDP  tempMDP = new MDP(data.getBytes());
		
		return new MessageAnswer(tempInit, tempMIP, tempMDP);
	}
}
