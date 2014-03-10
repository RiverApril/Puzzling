package server;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class NewClientListenerThread extends Thread{

	private Server server;
	public boolean exit = false;
	
	private int nextId = 1;

	public NewClientListenerThread(Server server) {
		this.server = server;
	}
	
	@Override
	public void run(){
		while(!exit){
			try {
				Socket s = server.serverSocket.accept();
				ClientSocket c = new ClientSocket(s, server, nextId );
				nextId++;
				server.newClientConnected(c);
				server.clientList.add(c);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for(int i=0;i<server.clientList.size();i++){
			try {
				ClientSocket c = server.clientList.get(i);
				c.socket.close();
				c.clientListenerThread.exit = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void removeClient(ClientSocket c) {
		server.clientList.remove(c);
	}

}
