package de.juanah.functions.login;

/**
 * Stellt einen erfolgreichen Login dar
 * @author jonas ahlf 19.09.2014
 *
 */
public class SuccessfulLogin {

	private String UserUUID;
	
	private boolean Success;

	public SuccessfulLogin()
	{
		Success = false;
	}
	
	public SuccessfulLogin(boolean success,String userUUID)
	{
		Success = success;
		UserUUID = userUUID;
	}
	

	
	public String getUserUUID() {
		return UserUUID;
	}

	public boolean isSuccess() {
		return Success;
	}


	
}
