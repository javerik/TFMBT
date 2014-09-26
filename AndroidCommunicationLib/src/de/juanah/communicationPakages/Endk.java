package de.juanah.communicationPakages;
/**
 * Definiert das endk Pakage
 * @author jonas ahlf 14.09.2014
 *
 */
public class Endk {

	private final String EndkKey = "endk";
	
	private String MessageUUID;

	public Endk(String messageUUID)
	{
		this.MessageUUID = messageUUID;
	}
	
	public String getMessageUUID() {
		return MessageUUID;
	}

	public String getEndkKey() {
		return EndkKey;
	}


	
}
