package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import packet.Packet;


public class ClientListenerThread extends Thread {

	public ClientSocket clientSocket;
	public boolean exit = false;
	public DataInputStream in;
	public DataOutputStream out;

	public ClientListenerThread(ClientSocket clientSocket) {
		this.clientSocket = clientSocket;
		try {
			in = new DataInputStream(clientSocket.socket.getInputStream());
			out = new DataOutputStream(clientSocket.socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run(){
		while(!exit){
			try {
				System.out.println("Reading a packet...");
				Packet packet = Packet.readNew(in);
				if(packet!=null){
					packet.serverRecived(clientSocket);
				}
			} catch (IOException e) {
				exit = true;
			}
		}
		System.out.println("Stopping Client Listener.");
		clientSocket.removeClient();
	}

}
