package de.juanah.communicationPakages;

/**
 * Definiert das initk Pakage
 * @author jonas ahlf 14.09.2014
 *
 */
public class Initk {

	private final String InitkKey = "initk";

	private String MessageUUID;
	
	public Initk(String messageUUID)
	{
		this.MessageUUID = messageUUID;
	}
	
	
	public String getInitkKey() {
		return InitkKey;
	}




	public String getMessageUUID() {
		return MessageUUID;
	}


	
	
}
