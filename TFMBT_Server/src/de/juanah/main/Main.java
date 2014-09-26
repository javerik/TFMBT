package de.juanah.main;

import java.io.BufferedReader;
import java.io.Console;
import java.io.InputStreamReader;

import de.juanah.service.MainService;


/**
 * 22.09.2014
 * Version 0.2
 * @author jonas ahlf
 *
 */
public class Main {

	private static Thread mainThread;
	private static MainService mainService;
	
	/**
	 * @author jonas ahlf 15.09.2014
	 * @param args
	 */
	public static void main(String[] args) {
		Write("TFMBT Server 1.0BETA");
		WriteCommands();
		Boolean run = true;
		StartService();
		Console console = System.console();
		while (run) {
			String command = readLine();
		
			switch (command) {
			case "service stop":
				StopService();
				break;
			case "--help":
				WriteCommands();
				break;
			case "service restart":
				StopService();
				StartService();
				break;
			case "server shutdown":
				ServerShutdown();
				run = false;
				break;
			default:
				break;
			}
		}
	}
	
	
	private static void ServerShutdown()
	{
		StopService();
		Write("Server is shuttingdown!");
		
	}
	
	private static void StartService()
	{
		
		 mainService = new MainService();
			
		mainThread = new Thread(mainService);
		
		mainThread.start();
		Write("Server wurde gestartet!");
	}
	
	/**
	 * Stoppt den Hauptservice
	 */
	private static void StopService()
	{
		try {
			mainService.Continue = false;
			
			int errorCounter = 0;
			
			while(mainThread.isAlive())
			{
				errorCounter++;
				Write("Try for shutdown MainService:" + errorCounter);
				Thread.sleep(1000);
				if (errorCounter >= 10) {
					break;
				}
			}
			
			if (mainThread.isAlive()) {
				mainThread.destroy();
			}
			Write("Server wurde gestoppt!");
		} catch (Exception e) {
			Write("Could not stop the Mainservice:" + e.getMessage());
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
	
	private static void WriteCommands()
	{
		String commandStr = "service shutdown: 'service stop' \n"
				+"service restart: 'service restart'" +
				"help: '--help'" + "server shutdown: 'server shutdown'";
		Write(commandStr);
	}
	
	private static void Write(String txt)
	{
		System.out.println(txt);
	}

}
