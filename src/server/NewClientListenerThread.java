package server;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


public class NewClientListenerThread extends Thread{
	
	ArrayList<ClientSocket> clientList = new ArrayList<ClientSocket>();

	private Server server;
	public boolean exit = false;

	public NewClientListenerThread(Server server) {
		this.server = server;
	}
	
	@Override
	public void run(){
		while(!exit){
			try {
				Socket s = server.serverSocket.accept();
				ClientSocket c = new ClientSocket(s, server);
				clientList.add(c);
				server.newClientConnected(c);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for(int i=0;i<clientList.size();i++){
			try {
				ClientSocket c = clientList.get(i);
				c.socket.close();
				c.clientCommunicatorThread.exit = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void removeClient(ClientSocket c) {
		clientList.remove(c);
	}

}
