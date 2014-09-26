package de.juanah.service.connection;

import de.juanah.communicationPakages.Init;
import de.juanah.communicationPakages.MDP;
import de.juanah.communicationPakages.MIP;

/**
 * Wird von der Klasse HandleClientQuestion an den HandleClient gesendet
 * es enthält die antwort, die den Client zurück gesendet wird
 * @author jonas ahlf 19.09.2014
 *
 */
public class MessageAnswer implements ConnectionInterface {

	/**
	 * Class Variablen
	 */
	private Init _init;
	private MIP _mip;
	private MDP _mdp;
	
	public MessageAnswer(Init init,MIP mip,MDP mdp)
	{
		this._init = init;
		this._mip = mip;
		this._mdp = mdp;
	}
	
	public Init getInit() {
		return _init;
	}

	public void setInit(Init _init) {
		this._init = _init;
	}
	public MIP getMIP() {
		return _mip;
	}

	public void setMIP(MIP _mip) {
		this._mip = _mip;
	}

	public MDP getMDP() {
		return _mdp;
	}

	public void setMDP(MDP _mdp) {
		this._mdp = _mdp;
	}

	public void Write(String text) {
		System.out.println(text);
	}

}
