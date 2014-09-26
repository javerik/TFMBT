package de.juanah.service.connection;

import de.juanah.communicationPakages.MDP;
import de.juanah.communicationPakages.MIP;
import de.juanah.functions.userdata.UserdataHelper;

/**
 * Differenziert die Anfrage und führt das entsprechende Prozedere aus
 * @author jonas ahlf 19.09.2014
 *
 */
public class HandleClientQuestion implements ConnectionInterface {

	/**
	 * Differenziert die anfrage und gibt die Antwort darauf zurück, die an den Client geschickt wird
	 * @param init vom Client
	 * @param mip vom Client
	 * @param mdp vom Client
	 * @return Antwort auf die Client Anfrage
	 */
	public  MessageAnswer Handle(MIP mip, MDP mdp)
	{
		try {
			
			switch(mip.getMType())
			{
			case "login":
				return de.juanah.functions.login.BaseHelper.GetAnswer(mdp);
			case "register":
				return de.juanah.functions.register.BaseHelper.GetAnswer(mdp);
			case "track":
				return de.juanah.functions.track.BaseHelper.GetTrackAnswer(mdp);
			case "getcoords":
				return de.juanah.functions.track.BaseHelper.GetTracks(mdp);
			case "getuserdata":
				return UserdataHelper.GetUserData(mdp);
			default:
				return null;
			}
		} catch (Exception e) {
			//Wenn ein Fehler auftritt geben wir null zurück
			Write(e.getMessage());
			return null;
		}
	}
	
	
	
	public void Write(String text) {
		System.out.println(text);
	}

}
