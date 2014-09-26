package de.juanah.trackingformybullentime.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
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

/**
 * Beinhaltet Grundlegende Funktionen die für den Datenaustausch wichtig sind
 * @author jonas ahlf 21.09.2014
 *
 */
public class BaseHelper {

	private static Socket _server = null;
	
	
	public BaseHelper()
	{
		GetConnection();
	}
	
	private static boolean GetConnection()
	{
		try {
			_server = new Socket("192.168.178.61",55567);
			return true;
		} catch (Exception e) {
			Write(e.getMessage());
			return false;
		}
		
	}
	
	/**
	 * Baut eine verbindung zum Server auf u
	 * @param _server
	 * @return gibt ein MDP Packet zurück oder null
	 */
	public static MDP RecivePacket()
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
			_server.close();
			_server = null;
			return tempMdp;
			
		} catch (Exception e) {
			Write(e.getMessage());
			return null;
		}
	}
	
	/**
	 * Sendet ein Packet an den Server
	 * @param type
	 * @param mdp
	 */
	public static void SendPackage(String type,MDP mdp)
	{
		if (_server == null) {
			if (!GetConnection()) {
				return;
			} 
		}
		
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
	
	/**
	 * Liest den Stream und baut daraus ein Packet
	 * @return
	 */
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
	
	/**
	 * Liest den Stream und baut daraus ein MDP Packet
	 * @return
	 */
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
	
		
	private static void Write(String text)
	{
		
	}
	
	//Methoden um den Stream zu lesen und zu schreiben
	
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
	
	
}
