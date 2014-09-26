package de.juanah.communicationPakages.data;

/**
 * Enthält daten, die für die verschiedenen Pakages relevant sind
 * 
 * @author jonas ahlf 14.09.2014
 *
 */
public class BaseData {

	/**
	 * Typ ob gesendet oder empfangen wird
	 * @author jonas ahlf 14.09.2014
	 *
	 */
	public enum MessageType{
		send,
		recive
	}
	
	/**
	 * Definiert die Errortypen
	 * @author jonas ahlf 14.09.2014
	 *
	 */
	public enum ErrorType{
		wrongPakage("wrongPakage"),
		connectionLost("connectionLost");
		
		private final String Type;
		
		private ErrorType(String type)
		{
			Type = type;
		}

		public String getType() {
			return Type;
		}
		
	}
	
}
