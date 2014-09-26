package de.juanah.database.databaseData;

import de.juanah.database.T_UserHelper;

public class T_User implements TableInterface {

	//Hauptklasse
	private T_UserHelper helper = new T_UserHelper();
	
	
	private String ID;
	private String Username;
	private String Email;
	private String Password;
	private String LoginUUID;
	private String UserdataUUID;
	
	public T_User(String id,String username,String email,String password,String loginUUID,String userdataUUID)
	{
		this.ID = id;
		this.Username = username;
		this.Email = email;
		this.Password = password;
		this.LoginUUID = loginUUID;
		this.UserdataUUID = userdataUUID;
	}
	
	//Getter Setter
	
	public String getID() {
		return ID;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPassword() {
		return Password;
	}

	public void setPassword(String password) {
		Password = password;
	}

	public String getLoginUUID() {
		return LoginUUID;
	}

	public void setLoginUUID(String loginUUID) {
		LoginUUID = loginUUID;
	}

	public String getUserdataUUID() {
		return UserdataUUID;
	}

	public void setUserdataUUID(String userdataUUID) {
		UserdataUUID = userdataUUID;
	}

	public boolean SaveChanges() {
		return helper.UpdateUser(this);
	}

	public boolean Delete() {
		return helper.DeleteUser(this);
	}

}
