package de.juanah.communicationPagePacker;

import java.util.ArrayList;

import de.juanah.communicationPakages.*;
import de.juanah.communicationPakages.data.DataTypes.Datatype;

/**
 * Entpackt aus einem Byte array das entsprechende paket
 * @author jonas ahlf 14.09.2014
 *
 */
public class UnPacker {
	//Pakage format {$TAG}|$Property
	
	//Class Variables
	private static final String FirstTag = "{";
	private static final String SecondTag = "}";
	private static final String PropertyTag = "§";
	
	
	
	public static Object GetPakage(byte[] data)
	{
		try {
			
			//Bilde den DataString
			
			String dataString = new String(data,"UTF-8");
			
			
			//Herrausfinden um welchen type es sich hier handelt
			String[] typeAndData = 	GetType(dataString);
			switch(typeAndData[0])
			{
			case "init":
				return GetInit(typeAndData[1]);
			case "initk":
				return GetInitk(typeAndData[1]);
			case "end":
				return GetEnd(typeAndData[1]);
			case "endk":
				return GetEndk(typeAndData[1]);
			case "MIP":
				return GetMIP(typeAndData[1]);
			case "MRP":
				return GetMRP(typeAndData[1]);
				default:
					System.out.println("DEFAULTVALUE" + typeAndData[0]);
					return null;
			}
			
		} catch (Exception e) {
			return null;
		}
		
		
		
	}
	
	/**
	 * Konvertiert einen String in ein Init objekt 
	 * @return
	 * @param dataString daten vom Client
	 */
	private static Init GetInit(String dataString)
	{
		try {
			
			//Hole dir alle Attribute
			
			ArrayList<String> attributes = GetParams(dataString);
			
			//prüfe ob es sich um 1 attribute handelt
			
			if (attributes.size() != 1) {
				//Wir erwarten 1 Werte nichts anderes!
				//TODO Debug
				System.out.println("attributes too small"+ attributes.size());
				return null;
			}
			
			return new Init(attributes.get(0));
			
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Konvertiert einen String in ein Initk objekt 
	 * @return
	 * @param dataString daten vom Client
	 */
	private static Initk GetInitk(String dataString)
	{
		try {
			
			
			//Hole dir alle Attribute
			
			ArrayList<String> attributes = GetParams(dataString);
			
			//prüfe ob es sich um 1 attribute handelt
			
			if (attributes.size() != 1) {
				//Wir erwarten 1 Werte nichts anderes!
				//TODO Debug
				System.out.println("attributes too small"+ attributes.size());
				return null;
			}
			
			return new Initk(attributes.get(0));
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Konvertiert einen String in ein End objekt 
	 * @return
	 * @param dataString daten vom Client
	 */
	private static End GetEnd(String dataString)
	{
		try {
			
			//Hole dir alle Attribute
			
			ArrayList<String> attributes = GetParams(dataString);
			
			//prüfe ob es sich um 1 attribute handelt
			
			if (attributes.size() != 1) {
				//Wir erwarten 1 Werte nichts anderes!
				return null;
			}
			
			return new End(attributes.get(0));
			
			
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * Konvertiert einen String in ein End objekt 
	 * @return
	 * @param dataString daten vom Client
	 */
	private static Endk GetEndk(String dataString)
	{
		try {
			
			//Hole dir alle Attribute
			
			ArrayList<String> attributes = GetParams(dataString);
			
			//prüfe ob es sich um 1 attribute handelt
			
			if (attributes.size() != 1) {
				//Wir erwarten 1 Werte nichts anderes!
				return null;
			}
			
			return new Endk(attributes.get(0));
			
			
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Konvertiert einen String in ein MIP Objekt
	 * @param dataString
	 * @return null wenn ERROR
	 */
	private static MIP GetMIP(String dataString)
	{
		try {
			
			//Hole Attribute
			ArrayList<String> attributes = GetParams(dataString);
			
			//Prüfe ob attribute Legal sind
			if (attributes.size() != 4) {
				return null;
			}

			//MessageUUID
			String MessageUUID = attributes.get(0);
			
			//MessageType 
			String MessageType = attributes.get(1);
			
			//DataType 
			Datatype DataType = GetDataType(attributes.get(2));
			
			//DataSize 
			int DataSize = Integer.parseInt( attributes.get(3));
			
			return new MIP("", 0, true, MessageUUID, MessageType, DataType, DataSize);
			
			
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Konvertiert einen String in ein MRP Objekt
	 * @param dataString
	 * @return null wenn ERROR
	 */
	private static MRP GetMRP(String dataString)
	{
		try {
			
			//Hole dir alle Attribute
			
			ArrayList<String> attributes = GetParams(dataString);
			
			//prüfe ob es sich um 1 attribute handelt
			
			if (attributes.size() != 1) {
				//Wir erwarten 1 Werte nichts anderes!
				return null;
			}
			
			return new MRP(attributes.get(0));
		} catch (Exception e) {
			return null;
		}
	}
	
	

	
	/**
	 * Bestimmt den MessageType
	 * Wird erstmal nicht mehr gebraucht
	 * @param mData
	 * @return wenn nicht passend dann null
	 */
//	private static MessageType GetMessageType(String mData)
//	{
//		switch(mData)
//		{
//		case "send":
//			return MessageType.send;
//			
//		case "recive":
//			return MessageType.recive;
//		
//		default:
//			return null;
//		}
//	}
	/**
	 * Bestimmt den Datatype
	 * @param dData
	 * @return wenn nicht passend dann null
	 */
	private static Datatype GetDataType(String dData)
	{
		switch(dData)
		{
		case "integer":
			return Datatype.integer;
		case "string":
			return Datatype.string;
		default:
			return null;
		}
	}
	
	/**
	 * Selektiert die Parameter aus dem String und gibt diese als Liste zurück
	 * @param data dataString
	 * @return
	 */
	private static ArrayList<String> GetParams(String data)
	{
		try {
			
			ArrayList<String> params = new ArrayList<>();
			
			String[] split = data.split(PropertyTag);
			
			for (String param : split) {
				if (!param.equals("")) {
					params.add(param);
				}
				
			}
			return params;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	
	/**
	 * Sucht den Typ des Paketes herraus und gibt daten ohne type wieder zurück
	 * @param data
	 * @return [0] type, [1] dataString ohne type
	 */
	private static String[] GetType(String data)
	{
		try {
			
			int start = data.indexOf(FirstTag);
			
			int end = data.indexOf(SecondTag);
			
			String[] returnVal = new String[2];
			
			returnVal[0] = data.substring(start + 1, end);
			returnVal[1] = data.substring(end + 1,data.length());
			
			
			return returnVal ;
			
		} catch (Exception e) {
			return null;
		}
	}
	
}
