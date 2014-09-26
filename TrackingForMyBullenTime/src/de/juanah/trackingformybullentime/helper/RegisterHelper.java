package de.juanah.trackingformybullentime.helper;

import de.juanah.communicationPakages.MDP;
import de.juanah.trackingformybullentime.helper.data.RegisterData;

/**
 * Führt Registrierungen aus
 * @author jonas ahlf 23.09.2014
 *
 */
public class RegisterHelper {

	public static boolean Register(RegisterData data)
	{
		try {
			
			String dataString ="";
			
			if (!data.getEmail().equals("")) {
				dataString = data.getUserUUID() + "<" + data.getUsername() + "<" + data.getPassword() + "<" + data.getEmail();
			}else
			{
				dataString = dataString = data.getUserUUID() + "<" + data.getUsername() + "<" + data.getPassword();
			}
			
			de.juanah.trackingformybullentime.network.BaseHelper.SendPackage("register", new MDP(dataString.getBytes()));
			
			MDP recivedMdp = de.juanah.trackingformybullentime.network.BaseHelper.RecivePacket();
			
			if (recivedMdp == null) {
				return false;
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	
}

