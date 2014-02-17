package server;
import java.net.Socket;


public class ClientSocket {

	public Socket socket;
	public Server server;
	public ClientListenerThread clientCommunicatorThread;

	public ClientSocket(Socket s, Server server) {
		socket = s;
		this.server = server;
		clientCommunicatorThread = new ClientListenerThread(this);
		clientCommunicatorThread.start();
	}

	public void removeClient() {
		server.clientListenerThread.removeClient(this);
		
	}

}
