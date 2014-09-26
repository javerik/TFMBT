package de.juanah.service;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import de.juanah.service.connection.HandleClient;

/**
 * Haupteinstiegspunkt, hier wird der Server gestartet und alle anderen Dienste
 * @author jonas ahlf 19.09.2014
 *
 */
public class MainService implements Runnable {

	
	/**
	 * Variablen
	 */
	private volatile ServerSocket _serverSocket;
	private volatile  int _port = 55567;
	public volatile Boolean Continue = true;
	
	public volatile ArrayList<Thread> _threads;
	
	/**
	 * Variablen END
	 */
	
	@Override
	public void run() {
		try {
			InitVariables();
			RunService();
		} catch (Exception e) {
			Write("Run Methode" + e.getMessage());
		}

	}

	
	/**
	 * Initialisiert alle wichtigen Variablen
	 */
	private void InitVariables()
	{
		try {
			//Socket Initialisieren
			_serverSocket = new ServerSocket(_port);
		} catch (Exception e) {
			Write("Init failed" + e.getMessage());
		}
	}
	
	/**
	 * Wartet auf Clients, nimmt diese an und behandelt sie in einem extra Thread
	 */
	private void RunService()
	{
		_threads = new ArrayList<>();
		while (Continue) {
			
			try {
				Socket client = _serverSocket.accept();
				
				HandleClient newClientHandler = new HandleClient(client);
				
				Thread newHandleThread = new Thread(newClientHandler);
				
//				_threads.add(newHandleThread);
				
				newHandleThread.start();
				
				//ThreadList aufräumen
//				for (Thread t : _threads) {
//					if (!t.isAlive()) {
//						_threads.remove(t);
//					}
//				}
				
			} catch (IOException e) {
				e.printStackTrace();
				Write("Service ERROR:" + e.getMessage());
			}
			
		}
	}
	
	/**
	 * Gibt text in der Konsole aus
	 * @param text
	 */
	private void Write(String text)
	{
		System.out.println(text);
	}
	
	
}
