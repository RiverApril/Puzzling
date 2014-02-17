package client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import packet.Packet;

import server.ClientSocket;


public class ServerListenerThread extends Thread {
	
	public boolean exit = false;
	public DataInputStream in;
	public DataOutputStream out;
	private Client client;

	public ServerListenerThread(Client client) {
		this.client = client;
		try {
			in = new DataInputStream(client.socket.getInputStream());
			out = new DataOutputStream(client.socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run(){
		while(!exit){
			try {
				Packet packet = Packet.readNew(in);
				packet.clientRecived(client);
			} catch (IOException e) {
				exit = true;
				e.printStackTrace();
			}
		}
		client.serverClosed();
	}
}
