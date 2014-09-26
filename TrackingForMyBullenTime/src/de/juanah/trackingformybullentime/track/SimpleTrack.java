package de.juanah.trackingformybullentime.track;

import de.juanah.communicationPakages.MDP;

/**
 * Enthält die Funktionen um einen einfachen Track an den Server zu senden
 * @author jonas ahlf 21.09.2014
 *
 */
public class SimpleTrack  {
	
	public static SimpleTrackResult SendTrack(String userUUID,String time,double lat,double lon)
	{
		try {
			String dataString = userUUID + "<" + time + "<" + String.valueOf(lat) + ";" + String.valueOf(lon);
			
			de.juanah.trackingformybullentime.network.BaseHelper.SendPackage("track", new MDP(dataString.getBytes()));
			
			MDP revicedMDP = de.juanah.trackingformybullentime.network.BaseHelper.RecivePacket();
			
			if (revicedMDP == null) {
				return new SimpleTrackResult(false,"Error");
			}
			
			String test = new String(revicedMDP.getData());
			
			if (test.equals("success")) {
				return new SimpleTrackResult(true,test);
			}else
			{
				return new SimpleTrackResult(false,test);
			}
		} catch (Exception e) {
			return new SimpleTrackResult(false, "ERROR! SendTrack");
		}
	}
	
	public static GetSimpleTracksResult GetSimpleTracks(double lat,double lon)
	{
		try {
			String dataString = String.valueOf(lat) + ";" + String.valueOf(lon);
			
			de.juanah.trackingformybullentime.network.BaseHelper.SendPackage("getcoords", new MDP(dataString.getBytes()));
			
			MDP revicedMDP = de.juanah.trackingformybullentime.network.BaseHelper.RecivePacket();
			
			String mdpString = new String(revicedMDP.getData());
			
			GetSimpleTracksResult result = new GetSimpleTracksResult();
			
			result.setDataString(mdpString);
			
			return result;
		} catch (Exception e) {
			return null;
		}
	}
}

