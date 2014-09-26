package de.juanah.trackingformybullentime.helper;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.ParseException;

/**
 * Helper für alles was Zeit und Datum angeht
 * @author jonas
 *
 */


public class TimeHelper {

	public static String GetTime()
	{
		//Zeit ermitteln
		java.util.Date dt = new java.util.Date();
		
		java.text.SimpleDateFormat sdf = 
		     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return sdf.format(dt);
	}
}
