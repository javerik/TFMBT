package de.juanah.database.databaseData;

import java.sql.Date;

import de.juanah.database.T_LoginHelper;

/**
 * Stellt eine virtuelle 
 * @author jonas ahlf 15.09.2014
 *
 */
public class T_Login implements TableInterface{

	//Class Variables
	private T_LoginHelper helper = new T_LoginHelper();
	
	private String UUID;
	
	private String DTime;
	
	
	public T_Login(String uuid,String datetime)
	{
		this.UUID = uuid;
		this.DTime = datetime;
	}
	
	
	//Getter Setter
	
	public String getUUID() {
		return UUID;
	}
	public String getDTime() {
		return DTime;
	}
	
	//Interface Methoden
	
	public boolean SaveChanges() {
		return helper.UpdateLogin(this);
	}
	public boolean Delete() {
		return helper.DeleteLogin(this);
	}





}
