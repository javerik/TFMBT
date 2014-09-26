package de.juanah.trackingformybullentime.helper.data;

/**
 * Enthält die login informationen vom Server
 * @author jonas ahlf 25.09.2014
 *
 */
public class LoginResult {
	
	private boolean Success;
	
	private String LoginMessage;
	
	private String LoginUUID;
	
	public LoginResult(boolean success,String loginMessage,String loginUUID)
	{
		setSuccess(success);
		setLoginMessage(loginMessage);
		setLoginUUID(loginUUID);
	}
	
	public LoginResult(boolean success,String loginMessage)
	{
		setSuccess(success);
		setLoginMessage(loginMessage);
	}

	public boolean isSuccess() {
		return Success;
	}

	public void setSuccess(boolean success) {
		Success = success;
	}

	public String getLoginMessage() {
		return LoginMessage;
	}

	public void setLoginMessage(String loginMessage) {
		LoginMessage = loginMessage;
	}

	public String getLoginUUID() {
		return LoginUUID;
	}

	public void setLoginUUID(String loginUUID) {
		LoginUUID = loginUUID;
	}
	
}
