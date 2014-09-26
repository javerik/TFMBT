package de.juanah.communicationPakages;

/**
 * Definiert das init Pakage
 * @author jonas ahlf 14.09.2014
 *
 */
public class Init {

	private final String InitKey = "init";

	private String MessageUUID;
	
	public Init(String messageUUID)
	{
		this.MessageUUID = messageUUID;
	}
	
	public String getInitKey() {
		return InitKey;
	}



	public String getMessageUUID() {
		return MessageUUID;
	}

	
}
