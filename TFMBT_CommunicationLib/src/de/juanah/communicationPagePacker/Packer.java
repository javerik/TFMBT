package de.juanah.communicationPagePacker;

import de.juanah.communicationPakages.*;

/**
 * Diese Klasse packt CommunicationPakete zu bytearrays
 * @author jonas ahlf 14.09.2014
 *
 */
public class Packer {

	//Pakage format {$TAG}|$Property
	

	//Class Variables
	private static final String FirstTag = "{";
	private static final String SecondTag = "}";
	private static final String PropertyTag = "§";
	
	
	/**
	 * Packt ein Init Pakage in ein byte array
	 * @param data Init Pakage Objekt
	 * @return byte array
	 */
	public static byte[] GetInitPakage(Init data)
	{
		try {
			String dataString = FirstTag + data.getInitKey() + SecondTag;
			dataString += PropertyTag + data.getMessageUUID();
			return dataString.getBytes();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Packt ein Initk Pakage in ein byte array
	 * @param data Initk Objekt
	 * @return
	 */
	public static byte[] GetInitkPakage(Initk data)
	{
		try {
			String dataString = FirstTag + data.getInitkKey() + SecondTag;
			dataString += PropertyTag + data.getMessageUUID();
			return dataString.getBytes();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Packt ein End Pakage in ein byte array
	 * @param data
	 * @return
	 */
	public static byte[] GetEnd(End data) {
		try {
			String dataString = FirstTag + data.getEndKey() + SecondTag;
			dataString += PropertyTag + data.getMessageUUID();
			return dataString.getBytes();			
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Packt ein Endk Pakage in ein byte array
	 * @param data
	 * @return
	 */
	public static byte[] GetEndkPakage(Endk data) {
		try {
			String dataString = FirstTag + data.getEndkKey() + SecondTag;
			dataString += PropertyTag + data.getMessageUUID();
			return dataString.getBytes();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Packt ein MIP Pakage in ein Byte array
	 * @param data
	 * @return
	 */
	public static byte[] GetMIPPakage(MIP data)
	{
		try {
			String dataString = FirstTag + "MIP" + SecondTag;
			
			//Verbindungsdaten werden nicht mit übermittelt, der Client sollte schon selbst wissen wer er ist ;)
			
			//MessageUUID
			dataString += PropertyTag + data.getMessageUUID();
			
			//MessageType
			dataString += PropertyTag + data.getMType().toString();
			
			//Datatype
			dataString += PropertyTag + data.getDType().toString();
			
			//DataSize 
			dataString += PropertyTag + data.getDataSize();
			
			return dataString.getBytes();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Packt ein MRP Pakage in ein byte array
	 * @param data
	 * @return
	 */
	public static byte[] GetMRPPakage(MRP data)
	{
		try {
			String dataString = FirstTag + data.getMrpKey() + SecondTag;
			dataString += PropertyTag + data.getMessageUUID();
			return dataString.getBytes();
		} catch (Exception e) {
			return null;
		}
	}
	
}
