package de.juanah.trackingformybullentime.helper;

import de.juanah.communicationPakages.MDP;
import de.juanah.trackingformybullentime.helper.data.LoginResult;

/**
 * Behandelt alles was mit login zutun hat
 * @author jonas ahlf 25.09.2014
 *
 */
public class LoginHelper {

	public static LoginResult GetLoginResult(String username,String password)
	{
		try {
			
			de.juanah.trackingformybullentime.network.BaseHelper.SendPackage("login",
					new MDP((username + "<" + password).getBytes()));
			
			MDP mdp = de.juanah.trackingformybullentime.network.BaseHelper.RecivePacket();
			
			return GetResult(mdp);
			
		} catch (Exception e) {
			return new LoginResult(false, "connection error");
		}
	}
	
	private static LoginResult GetResult(MDP mdp)
	{
		String dataString = new String(mdp.getData());
		
		if (dataString.equals("false")) {
			return new LoginResult(false, "Falsche login daten!");
		}
		
		return new LoginResult(true, "Erfolgreich eingelogged",dataString);
		
	}
	
	
}
