package de.juanah.communicationPakages.data;

/**
 * Hier werden alle Datentypen definiert
 * @author jonas ahlf 14.09.2014
 *
 */
public class DataTypes {

	public enum Datatype{
		integer("ingeter"),
		string("string");
		
		private final String Type;
		
		private Datatype(String type)
		{
			Type = type;
		}

		public String getType() {
			return Type;
		}
		
		
		
	}
}
