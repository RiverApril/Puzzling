package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import client.Client;
import packet.Packet;


public class ClientListenerThread extends Thread {

	public ClientSocket clientSocket;
	public boolean exit = false;

	public ClientListenerThread(ClientSocket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	@Override
	public void run(){
		while(!exit){
			try {
				Packet packet = Packet.readNew(clientSocket.in);
				if(packet!=null){
					packet.serverRecived(clientSocket);
				}else{
					clientSocket.server.println("Packet is null");
				}
			} catch (IOException e) {
				exit = true;
			}
		}
		clientSocket.server.println("Client Disconnected ("+clientSocket.getId()+"): "+clientSocket.socket.getInetAddress().getHostAddress()+":"+clientSocket.socket.getPort());
		clientSocket.removeClient();
	}

}
