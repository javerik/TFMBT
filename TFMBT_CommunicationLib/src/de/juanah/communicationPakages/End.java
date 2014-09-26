package de.juanah.communicationPakages;
/**
 * Definiert das end Pakage
 * @author jonas ahlf 14.09.2014
 *
 */
public class End {

	private final String EndKey = "end";

	private String MessageUUID;
	
	public End(String messageUUID)
	{
		this.MessageUUID = messageUUID;
	}
	
	
	public String getEndKey() {
		return EndKey;
	}


	public String getMessageUUID() {
		return MessageUUID;
	}

	
}
