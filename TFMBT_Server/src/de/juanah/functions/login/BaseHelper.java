package de.juanah.functions.login;

import java.util.UUID;

import de.juanah.communicationPakages.MDP;
import de.juanah.communicationPakages.Init;
import de.juanah.communicationPakages.MIP;
import de.juanah.communicationPakages.data.DataTypes;
import de.juanah.database.T_LoginHelper;
import de.juanah.database.T_UserHelper;
import de.juanah.database.databaseData.T_Login;
import de.juanah.database.databaseData.T_User;
import de.juanah.service.connection.MessageAnswer;

/**
 * Helper um Logins zuschreiben und abzuhandeln
 * @author jonas ahlf 19.09.2014
 *
 */
public class BaseHelper {

	private static T_UserHelper _userHelper = new T_UserHelper();
	private static T_LoginHelper _loginHelper = new T_LoginHelper();
	
	/**
	 * Prüft den Login
	 * 
	 * Stringaufbau
	 * @param mdp 
	 * @return gibt ein SuccessfulLogin Objekt zurück
	 */
	private static SuccessfulLogin CheckLogin(MDP mdp)
	{
		try {
			
			String dataString = new String(mdp.getData());
			
			String username = dataString.split("<")[0];
			
			String password = dataString.split("<")[1];
			
			T_User user = _userHelper.GetUserByName(username);
			
			
			if (!user.getPassword().equals(password)) {
				return new SuccessfulLogin();
			}
			
			
			//Login erstellen
			
			java.util.Date dt = new java.util.Date();

			java.text.SimpleDateFormat sdf = 
			     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			String currentTime = sdf.format(dt);
			
			String newLoginUUID = UUID.randomUUID().toString();
			
			user.setLoginUUID(newLoginUUID);
			
			
			if (!_loginHelper.WriteLogin(new T_Login(newLoginUUID, currentTime)) && _userHelper.UpdateUser(user)) {
				Write("could not write Login");
			}
			
			return new SuccessfulLogin(true,newLoginUUID);
			
		} catch (Exception e) {
			Write(e.getMessage());
			return new SuccessfulLogin();
		}
	}

	
	public static MessageAnswer GetAnswer(MDP mdp)
	{
		SuccessfulLogin tempLogin = CheckLogin(mdp);
		
		String messageUUID = UUID.randomUUID().toString();
		
		if (tempLogin.isSuccess()) {
			
			
			Init tempInit = new Init(messageUUID);
			
			MIP tempMIP = new MIP("",0,true,messageUUID,"login",DataTypes.Datatype.string,100);
			
			MDP  tempMDP = new MDP(tempLogin.getUserUUID().getBytes());
			
			return new MessageAnswer(tempInit, tempMIP, tempMDP);
						
		}else
		{
			Init tempInit = new Init(messageUUID);
			
			MIP tempMIP = new MIP("",0,true,messageUUID,"login",DataTypes.Datatype.string,100);
			
			MDP  tempMDP = new MDP("false".getBytes());
			
			return new MessageAnswer(tempInit, tempMIP, tempMDP);
		}
		
	}
	
	
	public static void Write(String text)
	{
		System.out.println(text);
	}
	
}
