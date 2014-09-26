package de.juanah.database.constante;

/**
 * Definiert im wesendlichen die Columnnames, damit es nicht zu schreibfehlern kommen kann
 * @author jonas ahlf 16.09.2014
 *
 */
public class T_UserConstante {

	private final String ID = "ID";
	private final String Username = "Username";
	private final String Email = "Email";
	private final String Password = "Password";
	private final String LoginUUID = "Login";
	private final String UserdataUUID = "Userdata";
	
	
	public String getUsername() {
		return Username;
	}
	public String getEmail() {
		return Email;
	}
	public String getPassword() {
		return Password;
	}
	public String getLoginUUID() {
		return LoginUUID;
	}
	public String getUserdataUUID() {
		return UserdataUUID;
	}
	public String getID() {
		return ID;
	}
	
	
	
}
