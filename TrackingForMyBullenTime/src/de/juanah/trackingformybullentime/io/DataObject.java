package de.juanah.trackingformybullentime.io;

/**
 * Representiert ein DatenSatz, der auf das Telefon geschrieben werden kann oder gelesen
 * @author jonas ahlf 22.09.2014
 *
 */
public class DataObject {

	private String Key;
	
	private String Value;
	
	private String Filename;

	public DataObject(String key,String value,String filename)
	{
		Key = key;
		Value = value;
		setFilename(filename);
	}
	
	
	public String getKey() {
		return Key;
	}

	public void setKey(String key) {
		Key = key;
	}

	public String getValue() {
		return Value;
	}

	public void setValue(String value) {
		Value = value;
	}


	public String getFilename() {
		return Filename;
	}


	public void setFilename(String filename) {
		Filename = filename;
	}
	
}
