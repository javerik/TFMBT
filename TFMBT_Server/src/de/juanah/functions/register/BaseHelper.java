package de.juanah.functions.register;

import java.util.ArrayList;
import java.util.UUID;

import de.juanah.communicationPakages.Init;
import de.juanah.communicationPakages.MDP;
import de.juanah.communicationPakages.MIP;
import de.juanah.communicationPakages.data.DataTypes;
import de.juanah.database.T_UserHelper;
import de.juanah.database.T_UserdataHelper;
import de.juanah.database.databaseData.T_User;
import de.juanah.database.databaseData.T_Userdata;
import de.juanah.service.connection.MessageAnswer;

/**
 * Handelt Registrierungs Aufgaben
 * @author jonas ahlf 19.09.2014
 *Changelog
 * Bei registrierung abfragen, ob es den Nutzer schon gibt 24.09.2014
 */
public class BaseHelper {
	
	/**
	 * Class Variablen
	 */
	private static T_UserHelper _userHelper = new T_UserHelper();
	private static T_UserdataHelper _userDataHelper = new T_UserdataHelper();
	
	/**
	 * Prüft ob es möglich ist einen Account mit diesem Username anzulegen
	 * @param username
	 * @return true möglich, false nicht möglich
	 */
	private static boolean CheckUsername(String username)
	{
		ArrayList<T_User> users = _userHelper.Where("Username = '" + username +"'");
		
		if (users.size() == 0) {
			return true;
		}else
		{
			return false;
		}
	}
	
	/**
	 * Gibt die Antwort auf die Anfrage zurück
	 * 
	 * Stringaufbau
	 * userUUID<username<password<email
	 * @param mdp vom Client
	 * @return null wenn fehler
	 */
	public static MessageAnswer GetAnswer(MDP mdp)
	{
		try {
			String dataString = new String(mdp.getData());
			
			String[] attributes = dataString.split("<");
			
			String userUUID = attributes[0];
			
			String username = attributes[1];
			
			String password = attributes[2];
			
			String email = "";
			
			if (attributes.length > 3) {
				email = attributes[3];
			}
			
			
			if (!CheckUsername(username)) {
				return GetFalseAnswer();
			}
			
			SuccessfulRegister register = Register(userUUID,username, password, email);
			
			if (register.isSuccess()) {
				return GetSuccessAnswer(register.getUserUUID());
			}else
			{
				return GetFalseAnswer();
			}
			
			
		} catch (Exception e) {
			Write(e.getMessage());
			return null;
		}
	}
	
	
	private static SuccessfulRegister Register(String userUUID,String username,String password,String email)
	{
		
		T_User user = _userHelper.GetUserByName(username);
		
		if (user != null) {
			return new SuccessfulRegister();
		}
		
		
		T_User newUser = new T_User(userUUID, username, email, password, userUUID, userUUID);
		
		T_Userdata userData = new T_Userdata(userUUID);
		
		if (_userHelper.WriteUser(newUser) && _userDataHelper.WriteUserdata(userData)) {
			return new SuccessfulRegister(userUUID);
		}else
		{
			return new SuccessfulRegister();
		}
		
	}
	
	private static MessageAnswer GetFalseAnswer()
	{
		String messageUUID = UUID.randomUUID().toString();
		
		Init tempInit = new Init(messageUUID);
		
		MIP tempMIP = new MIP("",0,true,messageUUID,"register",DataTypes.Datatype.string,100);
		
		MDP tempMDP = new MDP("false".getBytes());
		
		return new MessageAnswer(tempInit, tempMIP, tempMDP);
		
	}
	
	private static MessageAnswer GetSuccessAnswer(String userUUID)
	{
		String messageUUID = UUID.randomUUID().toString();
		
		Init tempInit = new Init(messageUUID);
		
		MIP tempMIP = new MIP("",0,true,messageUUID,"register",DataTypes.Datatype.string,100);
		
		MDP tempMDP = new MDP(("success§" + userUUID).getBytes());
		
		return new MessageAnswer(tempInit, tempMIP, tempMDP);
		
	}
	
	private static void Write(String text)
	{
		System.out.println(text);
	}
	
}
