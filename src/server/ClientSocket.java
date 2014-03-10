package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class ClientSocket {

	public Socket socket;
	public Server server;
	public ClientListenerThread clientListenerThread;
	public DataInputStream in;
	public DataOutputStream out;
	public String name = "";
	private int id;

	public ClientSocket(Socket s, Server server, int id) {
		this.id = id;
		socket = s;
		this.server = server;
		try {
			in = new DataInputStream(socket.getInputStream());
			out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientListenerThread = new ClientListenerThread(this);
		clientListenerThread.start();
	}

	public void removeClient() {
		server.newClientListenerThread.removeClient(this);
		
	}

	public void setName(String name) {
		this.name = name;
		server.println("Client set name: "+name);
	}

	public int getId() {
		return id;
	}

}
