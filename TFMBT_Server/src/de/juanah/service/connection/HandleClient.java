package de.juanah.service.connection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import de.juanah.communicationPagePacker.Packer;
import de.juanah.communicationPagePacker.UnPacker;
import de.juanah.communicationPakages.End;
import de.juanah.communicationPakages.Endk;
import de.juanah.communicationPakages.Init;
import de.juanah.communicationPakages.Initk;
import de.juanah.communicationPakages.MDP;
import de.juanah.communicationPakages.MIP;
import de.juanah.communicationPakages.MRP;

/**
 * Behandelt den übergebenden Client
 * @author jonas
 *
 */
public class HandleClient implements Runnable, ConnectionInterface {

	/**
	 * Class Variables
	 */
	
	private Socket _client;
	
	private HandleClientQuestion _clientHandler = new HandleClientQuestion();
	
	private final int _timeoutLimit = (60*1000);
	private final int _timeoutLimitExtreme = (200*1000);
	private int _timeoutCount = 0;
	private volatile boolean Busy = false;
	private Timer _timeoutTimer;
	
	
	
	//Solange diese Variable true ist, bleibt die Verbindung bestehen
	private volatile boolean _connectionLegal = true;
	
	/**
	 * Konstruktor, dem der Client übergeben wird
	 * @param client der behandelt werden soll
	 */
	public HandleClient(Socket client)
	{
		try {
			_client = client;
		} catch (Exception e) {
			Write("Could not mapp client");
		}
		
	}
	
	/**
	 * Startet die Behandlung des Clients
	 */
	public void run() {
		//TODO NUR im debug aus
		//StartTimer();
		
		//Variablen
		Init currentInit = null;
		
		MIP currentMIP = null;
		
		MDP currentMDP = null;
		
		boolean waitForMDP = false;
		
		while (_connectionLegal) {
			
			//Stream lesen
			try {
				byte[] recivedData = ReadStream(_client);
				
				//Prüfe ob ein MDP erwartet wird
				if (waitForMDP) {
					waitForMDP = false;
					currentMDP = new MDP(recivedData);
					continue;
				}
				
				//Erstelle das paket
				Object paket = UnPacker.GetPakage(recivedData);
				
				//Überprüfe, ob das paket nicht null ist
				if (paket != null) {
					
					//finde herraus um was für ein paket es sich handelt
					
					if (paket instanceof Init) {
						//Es handelt sich um ein Init paket also möchte der Client ein MessagePaket einleiten
						//Und speichere das Init ab
						Init tempInit = (Init)paket; 
						currentInit = tempInit;
						//Erstelle ein initk paket 
						Initk tempInitk = new Initk(tempInit.getMessageUUID());
						//Verpacke das paket damit es gesendet werden kann 
						byte[] initkBytePakage = Packer.GetInitkPakage(tempInitk);
						//Versende das initk Paket
						Send(_client, initkBytePakage);	
						continue;
					}
					if (paket instanceof MIP) {
						//Es handelt es um ein MIP paket also casten wir dieses
						// und schauen wir nach ob wir es auch erwarten
						MIP tempMIP = (MIP)paket;
						if (!tempMIP.getMessageUUID().equals( currentInit.getMessageUUID())) {
							//Dieses Paket erwarten wir aber garnicht :(
							//TODO Hier muss drauf reagiert werden!!!
							Write("ERROR: \n got:" + tempMIP.getMessageUUID()
									+ " but expect:" + currentInit.getMessageUUID());
							continue;
						}
						//Es ist das was wir erwarten also speichern wir uns das ab
						currentMIP = tempMIP;
						//Nun schicken wir dem client das MRP 
						MRP tempMRP = new MRP(tempMIP.getMessageUUID());
						//Das ganze wird gepackt und verschickt
						byte[] mrpPaket = Packer.GetMRPPakage(tempMRP);
						Send(_client, mrpPaket);
						waitForMDP = true;
						continue;
					}
					
					if (paket instanceof End) {
						//Es handelt sich um ein End paket 
						//unser client möchte also eine bestätigung das wir
						//das ende mitbekommen haben
						//End paket casten
						End tempEnd = (End)paket;
						
						//Wir prüfen nun ob der client und wir
						//über das selbe reden oder nicht
						if (!tempEnd.getMessageUUID().equals(currentMIP.getMessageUUID())) {
							//TODO Hier muss auch reagiert werden
							Write("client send end for:" + tempEnd.getMessageUUID() +
									" but we expect:" + currentMIP.getMessageUUID());
							
						}
						//Da wir vom gleichen reden, sagen wir dem das auch
						Endk tempEndk = new Endk(currentMIP.getMessageUUID());
						byte[] endkPaket = Packer.GetEndkPakage(tempEndk);
						//Senden und eigene sicherungen reseten
						Send(_client, endkPaket);
						//Write("Paket:" + currentInit.getMessageUUID() + " wurde beendet");
						
						MessageAnswer answer = _clientHandler.Handle(currentMIP, currentMDP);
						
						SendMessageAnswer(_client, answer);
						
						continue;
					}
				}else
				{
					//TODO Hier muss drauf reagiert werden
					Write("Error object is null");
					return;
				}
			} catch (IOException e) {
				Write("ERROR HandleClient" + e.getMessage());
				_connectionLegal = false;
			}
			
		}
		
		
	}

	/**
	 * Sendet die MessageAnswer zurück an den Client
	 * @param socket Socket an das, dass Objekt verschickt werden soll
	 * @param answer MessageAnswer Objekt 
	 */
	private  void SendMessageAnswer(Socket socket, MessageAnswer answer)
	{
		
		try {
			byte[] messagePacket = Packer.GetInitPakage(answer.getInit());
			
			Send(socket, messagePacket);
			
			byte[] recivedData = ReadStream(_client);
			
			Object packet = UnPacker.GetPakage(recivedData);
			
			if (packet == null) {
				return;
			}
			
			Initk tempInitk;
			
			if (packet instanceof Initk) {
				
				tempInitk = (Initk)packet;
			}else
			{
				return;
			}
			
			if (!tempInitk.getMessageUUID().equals(answer.getInit().getMessageUUID())) {
				return;
			}
			
			Send(socket,Packer.GetMIPPakage(answer.getMIP()));
			
			
			recivedData = ReadStream(socket);
			
			packet = UnPacker.GetPakage(recivedData);
			
			if (packet == null || !(packet instanceof MRP)) {
				return;
			}
			
			if (!((MRP)packet).getMessageUUID().equals(answer.getInit().getMessageUUID())) {
				return;
			}
			
			Send(socket,answer.getMDP().getData());
			
			Send(socket,Packer.GetEnd(new End(answer.getInit().getMessageUUID())));
			
			recivedData = ReadStream(socket);
			
			packet = UnPacker.GetPakage(recivedData);
			
			if (packet == null) {
				return;
			}
			
			Endk tempEndk;
			
			if (packet instanceof Endk) {
				
				tempEndk = (Endk)packet;
			}else
			{
				return;
			}
			
			if (!tempEndk.getMessageUUID().equals(answer.getInit().getMessageUUID())) {
				Write("HandleClient wrong UUID:" + tempEndk.getMessageUUID());
			}
			
		} catch (IOException e) {
			Write("SendMessageAnswer" + e.getMessage());
		}
		
	}
	
	
    private static byte[] ReadStream(java.net.Socket socket) throws IOException {
      	
        InputStream in = socket.getInputStream();
        DataInputStream dis = new DataInputStream(in);

        int len = dis.readInt();
        byte[] data = new byte[len];
        if (len > 0) {
            dis.readFully(data);
        }
        return data;
    	
    }
	
    private static void Send(java.net.Socket socket, byte[] data) throws IOException {
        OutputStream out = socket.getOutputStream(); 
        DataOutputStream dos = new DataOutputStream(out);
        
        dos.writeInt(data.length);
        if (data.length > 0) {
			dos.write(data);
		}
        
    }
    
	
	/**
	 * prüft jede Sekunde ob der Timeout erreicht ist
	 * ist dieser erreicht wird die connection geschlossen
	 * 
	 */
	private void StartTimer()
	{
		_timeoutTimer = new Timer();
		_timeoutTimer.schedule(new TimerTask() {
			  @Override
			  public void run() {
				  if (Busy) {
					_timeoutCount = 0;
				}else if(_timeoutCount > _timeoutLimitExtreme)
				{
					_connectionLegal = false;
				}
				  else
				{
					_timeoutCount++;
					if (_timeoutCount > _timeoutLimit) {
						_connectionLegal = false;
					}
				}
			  }
			},1000);
	}
	
	public void Write(String text) {
		System.out.println(text);
	}

	
}
