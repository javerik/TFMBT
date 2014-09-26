package de.juanah.trackingformybullentime.helper.data;

/**
 * Bündelt alle Registrierungsdaten
 * @author jonas ahlf 23.09.2014
 *
 */
public class RegisterData {

	private String UserUUID;
	private String Username;
	private String Password;
	private String Email;
	
	public RegisterData(String userUUID,String username,String password)
	{
		UserUUID = userUUID;
		Username = username;
		Password = password;
		Email = "";
	}
	
	public RegisterData(String userUUID,String username,String password,String email)
	{
		UserUUID = userUUID;
		Username = username;
		Password = password;
		Email = email;
	}
	
	public String getUserUUID() {
		return UserUUID;
	}

	public String getUsername() {
		return Username;
	}

	public String getPassword() {
		return Password;
	}

	public String getEmail() {
		return Email;
	}



	
}
