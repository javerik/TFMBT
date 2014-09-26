package de.juanah.communicationPakages;

/**
 * Diese Klasse definiert ein MessagePakage
 * @author jonas ahlf 14.09.2014
 *
 */
public class MessagePakage {

	//Class Variables
	private MIP _mip;
	
	private MDP _mdp;

	public MessagePakage(){
		
	}
	
	public MessagePakage(MIP mip,MDP mdp)
	{
		this._mip = mip;
		this._mdp = mdp;
	}
	
	//Getter and Setter
	
	public MIP get_mip() {
		return _mip;
	}

	public void set_mip(MIP _mip) {
		this._mip = _mip;
	}

	public MDP get_mdp() {
		return _mdp;
	}

	public void set_mdp(MDP _mdp) {
		this._mdp = _mdp;
	}
	
	
}
