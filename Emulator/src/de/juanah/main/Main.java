package de.juanah.main;

import java.io.BufferedReader;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Random;
import java.util.UUID;

import de.juanah.communicationPagePacker.Packer;
import de.juanah.communicationPagePacker.UnPacker;
import de.juanah.communicationPakages.End;
import de.juanah.communicationPakages.Endk;
import de.juanah.communicationPakages.Init;
import de.juanah.communicationPakages.Initk;
import de.juanah.communicationPakages.MDP;
import de.juanah.communicationPakages.MIP;
import de.juanah.communicationPakages.MRP;
import de.juanah.communicationPakages.data.DataTypes;

public class Main {

	
	private static Console console = System.console();
	
	private static Socket _server;
	
	/**
	 * Emuliert einen Client
	 * @author jonas ahlf 20.09.1014
	 * @param args
	 */
	public static void main(String[] args) {

		Write("Tracking for my Bullen Time emulator 1.0");
		
		StartEmulation();
		
	}

	private static void StartEmulation()
	{
		//verbinde zum Server
		try {
			_server = new Socket("127.0.0.1",55566);
		} catch (Exception e) {
			Write(e.getMessage());
			return;
		}
		
		
		Boolean run = true;
		
		while(run)
		{
			String command = readLine();
			
			switch (command) {
			case "send -s":
				SimpleTrack();
				break;
			case "send -sa":
				SimpleTrackAuto();
				break;
			case "get -s":
				GetCoords();
				break;
			case "send -a":
				AdvancedTrack();
				break;
			case "reg -u":
				Register();
				break;
			case "login":
				Login();
				break;
			default:
				Write("Commands: 'reg -u';'login';'send -s' \n");
				break;
			}
		}
	}
	
	private static void Register()
	{
		Write("Bitte Usernamen eingeben: ");
		String username = readLine();
		Write("Bitte Passwort eingeben");
		String password = readLine();
		
		String emailStr = ""; 
		
		Write("Email auch angeben? n = nein;j = ja");
		
		boolean email = false;
		
		switch (readLine()) {
		case "j":
			email = true;
			break;
		case "n":
			email = false;
			break;
		default:
			Write("Falsche Eingabe, vorgang wurde abgebrochen");
			return;
		}
		
		if (email) {
			Write("Bitte email eingeben: ");
			emailStr = readLine();
			Register(username, password, emailStr);
		}else
		{
			Register(username, password);
		}
	}
	
	private static void Register(String username,String password,String email)
	{
		String dataString = "";
			
		dataString = username + "<" +  password + "<" + email;

		MDP tempMDP = new MDP(dataString.getBytes());
		
		SendPackage("register", tempMDP);
		
		MDP revicedMDP = RecivePacket();
		
		Write(new String(revicedMDP.getData()));
		
	}
	
	private static void Register(String username,String password)
	{
		String dataString = "";
			
		dataString = username + "<" +  password ;

		MDP tempMDP = new MDP(dataString.getBytes());
		
		SendPackage("register", tempMDP);
		
		MDP revicedMDP = RecivePacket();
		
		Write(new String(revicedMDP.getData()));
		
	}
	
	
	private static void Login()
	{
		//Username und Passwort abfrage
		
		Write("Bitte Usernamen eingeben: ");
		String username = readLine();
		Write("Bitte Passwort eingeben");
		String password = readLine();
		
		Login(username, password);
	}
	
	private static void Login(String username,String password)
	{
		
		String loginDataString = username + "<" + password;
		
		SendPackage("login", new MDP(loginDataString.getBytes()));
		
		MDP rMdp = RecivePacket();
		
		Write(new String(rMdp.getData()));
		
		
	}
	
	private static void SimpleTrackAuto()
	{
		int counter = 0;
		int befor = 0;
		while(true)
		{
			counter++;
			int sleepy = randInt(100, 300);
			
			String lat = randInt(50, 150) + "." + randInt(1000, 8000);
			String lon = randInt(50, 150) + "." + randInt(1000, 8000);
			long time1 = System.currentTimeMillis();
			SimpleTrack(lat, lon);
			
			long time2 = System.currentTimeMillis();
			
			double calc = time2 - time1;
			
			if ((counter - befor) > 10) {
				Write("Send Coords..." + counter + " one in:" + calc);
				befor = counter;
			}
			
			try {
				//Thread.sleep(sleepy);
			} catch (Exception e) {
				// TODO: handle exception
			}
			
		}
	}
	
	private static void SimpleTrack()
	{
		Write("Latitude: ");
		String lat = readLine();
		Write("Long: ");
		String lon = readLine();
		SimpleTrack(lat, lon);
	}
	
	private static void SimpleTrack(String lat,String lon)
	{

		
		String dataString = lat + ";" + lon;
		
		SendPackage("track", new MDP(dataString.getBytes()));
		
		MDP recivedMdp = RecivePacket();
		
		//Write(new String(recivedMdp.getData()));
		
		
	}
	
	private static void AdvancedTrack()
	{
		Write("Latitude: ");
		String lat = readLine();
		Write("Long: ");
		String lon = readLine();
		Write("username: ");
		String username = readLine();
		AdvancedTrack(lat, lon, username);
	}
	
	private static void AdvancedTrack(String lat,String lon,String username)
	{
		String dataString = lat + ";" + lon + "<" + username;
		
		SendPackage("track", new MDP(dataString.getBytes()));
		
		MDP recivedMdp = RecivePacket();
		
		Write(new String(recivedMdp.getData()));
	}
	
	private static void GetCoords()
	{
		Write("Latitude: ");
		String lat = readLine();
		Write("Long: ");
		String lon = readLine();
		
		GetCoords(lat, lon);
	}
	
	private static void GetCoords(String lat,String lon)
	{
		String dataString = lat + ";" + lon;
		
		SendPackage("getcoords", new MDP(dataString.getBytes()));
		
		MDP recivedMdp = RecivePacket();
		
		Write(new String(recivedMdp.getData()));
		
	}
	
	private static void SendPackage(String type,MDP mdp)
	{
		String uuid = UUID.randomUUID().toString();
		
		Init tempInit = new Init(uuid);
		
		MIP tempMip = new MIP("", 0, true,uuid, type, DataTypes.Datatype.string, 100);
		
		MDP tempMdp = mdp;
		
		//Schicke dem Server die Login daten
		
		byte[] tempInitPackage = Packer.GetInitPakage(tempInit);
		
		try {
			Send(_server,tempInitPackage);
			
			byte[] recivedBytes = ReadStream(_server);
			
			Object packet = UnPacker.GetPakage(recivedBytes);
			
			if (packet == null) {
				Write("Initk packet is null");
				return;
			}
			
			if (!(packet instanceof Initk)) {
				Write("Is not Initk");
				return;
			}
			
			if (!((Initk)packet).getMessageUUID().equals(uuid)) {
				Write("Initk got the wrong UUID");
				return;
			}
			
			Send(_server,Packer.GetMIPPakage(tempMip));
			
			recivedBytes = ReadStream(_server);
			
			packet = UnPacker.GetPakage(recivedBytes);
			
			if (packet == null || !(packet instanceof MRP)) {
				Write("failed got no MRP");
				return;
			}
			
			Send(_server,tempMdp.getData());
			
			Send(_server,Packer.GetEnd(new End(uuid)));
			
			recivedBytes = ReadStream(_server);
			
			packet = UnPacker.GetPakage(recivedBytes);
			
			if (packet == null || !(packet instanceof Endk)) {
				return;
			}
			
			if (!((Endk)packet).getMessageUUID().equals(uuid)) {
				Write("Endk was false");
			}
			
			
		} catch (IOException e) {
			Write(e.getMessage());
		}
	}
	
	
	private static MDP RecivePacket()
	{
		try {
			
			Init tempInit;
			
			MIP tempMip;
			
			MDP tempMdp;
			
			Object packet = Getpacket();
			
			if (packet == null || !(packet instanceof Init)) {
				return null;
			}
			
			tempInit = (Init)packet;
			
			Send(_server,Packer.GetInitkPakage(new Initk(tempInit.getMessageUUID())));
			
			packet = Getpacket();
			
			if (packet == null || !(packet instanceof MIP)) {
				Write("got no MIP");
			}
			
			tempMip = (MIP)packet;
			
			if (!tempMip.getMessageUUID().equals(tempInit.getMessageUUID())) {
				Write("MIP is wrong!");
				return null;
			}
			
			Send(_server,Packer.GetMRPPakage(new MRP(tempMip.getMessageUUID())));
			
			tempMdp = GetMDP();
			
			packet = Getpacket();
			
			if (packet == null || !(packet instanceof End)) {
				Write("got no End packet");
			}
			
			Send(_server,Packer.GetEndkPakage(new Endk(tempMip.getMessageUUID())));
			return tempMdp;
			
		} catch (Exception e) {
			Write(e.getMessage());
			return null;
		}
		
		
	}
	
	
	private static Object Getpacket()
	{
		try {
			byte[] recivedBytes = ReadStream(_server);
			
			Object packet = UnPacker.GetPakage(recivedBytes);
			return packet;
		} catch (Exception e) {
			Write(e.getMessage());
			return null;
		}

	}
	
	private static MDP GetMDP()
	{
		try {
			byte[] recivedBytes = ReadStream(_server);
			return new MDP(recivedBytes);
		} catch (Exception e) {
			Write(e.getMessage());
			return null;
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
    
    private static String readLine(){
    	
    	try {
            if (System.console() != null) {
                return System.console().readLine();
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    System.in));
            return reader.readLine();
		} catch (Exception e) {
			Write(e.getMessage());
			return "";
		}
    }
    
    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
    
    private static void Write(String text)
    {
    	System.out.println(text);
    }
    
	
}
