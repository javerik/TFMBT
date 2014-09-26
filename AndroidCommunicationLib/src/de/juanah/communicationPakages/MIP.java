package de.juanah.communicationPakages;

import java.util.UUID;

import de.juanah.communicationPakages.data.DataTypes.Datatype;

/**
 * Diese Klasse definiert das MIP Paket
 * 
 * @author jonas ahlf 14.09.2014
 *
 *ChangeLog
 *MessageType von Enum zu String geändert
 */
public class MIP {
	
	//Class Variables
	private String Hostaddress;
	
	private int Hostport;
	
	private boolean Type;

	private String MessageUUID;
	
	private String MType;
	
	private Datatype DType;
	
	private int DataSize;
	
	
	//Construktor
	
	//Wird benutzt wenn ein neues MIP erstellt werden soll
	//Die MessageUUID wird hierbei generiert
	public MIP(String hostaddress,int hostport,boolean type,String mType,Datatype dType,int dataSize)
	{
		this.Hostaddress = hostaddress;
		this.Hostport = hostport;
		this.Type = type;
		this.MessageUUID = UUID.randomUUID().toString();
		this.MType = mType;
		this.DType = dType;
		this.DataSize = dataSize;
	}
	
	//Wird benutzt wenn ein vorhandenes MIP geprased wird und die MessageUUID schon vorhanden ist
	public MIP(String hostaddress,int hostport,boolean type,String messageUUID,String mType,Datatype dType,int dataSize)
	{
		this.Hostaddress = hostaddress;
		this.Hostport = hostport;
		this.Type = type;
		this.MessageUUID = messageUUID;
		this.MType = mType;
		this.DType = dType;
		this.DataSize = dataSize;
	}
	
	//Getter and Setter
	
	public String getHostaddress() {
		return Hostaddress;
	}


	public int getHostport() {
		return Hostport;
	}


	public boolean getType() {
		return Type;
	}


	public String getMessageUUID() {
		return MessageUUID;
	}


	public String getMType() {
		return MType;
	}


	public Datatype getDType() {
		return DType;
	}

	public int getDataSize() {
		return DataSize;
	}
}
