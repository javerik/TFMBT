package de.juanah.helper;

/**
 * Helper für Primitive Datentypen
 * @author jonas ahlf 22.09.2014
 *
 */
public class PrimitivHelper {

	public static String GetTime()
	{
		//Zeit ermitteln
		java.util.Date dt = new java.util.Date();
		
		java.text.SimpleDateFormat sdf = 
		     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return sdf.format(dt);
	}
	
	
}
