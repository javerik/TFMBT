package de.juanah.communicationPakages;
/**
 * Diese Klasse definiert das MRP Paket
 * @author jonas ahlf 14.09.2014
 *
 */
public class MRP {

	private final String MrpKey = "MRP";

	private String MessageUUID;
	
	public MRP(String messageUUID)
	{
		this.MessageUUID = messageUUID;
	}
	
	
	public String getMrpKey() {
		return MrpKey;
	}

	public String getMessageUUID() {
		return MessageUUID;
	}


}
