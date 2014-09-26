package de.juanah.functions.register;

/**
 * Stellt den erfolg einer Registrirung dar mit der UserUUID
 * @author jonas ahlf 19.09.2014
 *
 */
public class SuccessfulRegister {

	private boolean Success;
	
	private String UserUUID;
	
	public SuccessfulRegister()
	{
		Success = false;
	}
	
	public SuccessfulRegister(String userUUID)
	{
		Success = true;
		UserUUID = userUUID;
	}

	public boolean isSuccess() {
		return Success;
	}

	public String getUserUUID() {
		return UserUUID;
	}


	
	
}
