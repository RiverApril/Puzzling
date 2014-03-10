package client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import packet.Packet;
import server.ClientSocket;


public class ServerListenerThread extends Thread {
	
	public boolean exit = false;
	private Client client;

	public ServerListenerThread(Client client) {
		this.client = client;
	}

	@Override
	public void run(){
		while(!exit){
			try {
				Packet packet = Packet.readNew(client.in);
				packet.clientRecived(client);
				
			} catch (SocketException e) {
				exit = true;
				e.printStackTrace();
			} catch (IOException e) {
				exit = true;
				e.printStackTrace();
			}
		}
		client.serverClosed();
	}
}
