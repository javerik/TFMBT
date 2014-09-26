package de.juanah.communicationPakages;
/**
 * Diese Klasse definiert das MDP Pakage
 * @author jonas ahlf 14.09.2014
 *
 */
public class MDP {
	
	private byte[] Data;

	public MDP(byte[] data)
	{
		this.Data = data;
	}
	
	public byte[] getData() {
		return Data;
	}


	
}
