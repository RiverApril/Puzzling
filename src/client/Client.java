package client;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

import other.Settings;
import packet.PacketNewClient;
import server.NewClientListenerThread;
import server.Server;
import data.DataBase;
import data.DataCluster;
import data.DataInt;
import data.DataString;


public class Client extends Settings {
	
	public Socket socket;
	public ServerListenerThread serverListenerThread;
	
	public DataInputStream in;
	public DataOutputStream out;
	
	private ArrayList<OtherClient> clientList = new ArrayList<OtherClient>();
	
	private Random rand = new Random();

	public Client(String[] args) {
		settingsFile = new File("clientSettings.dat");
		
		settingsCluster.add(new DataInt("port", 1337));
		settingsCluster.add(new DataString("ip", "localhost"));
		initSettingsFile();
		boolean connected = joinServer(((DataString)settingsCluster.get("ip")).data, ((DataInt)settingsCluster.get("port")).data);
		
		if(!connected){
			exit("Failed to connect to server.");
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		try {
			new PacketNewClient("Client "+rand .nextInt()).write(out); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		println("Wrote a packet.");
	}

	@Override
	public void println(String string) {
		System.out.println("<Client> "+string);
	}

	public void exit(String reason) {
		println("Exit Client: "+reason);
		System.exit(0);
	}

	public void serverClosed() {
		socket = null;
		serverListenerThread = null;
		println("Server Closed");
	}
	
	private boolean joinServer(String ip, int port) {
		try {
			socket = new Socket(ip, port);
			try {
				in = new DataInputStream(socket.getInputStream());
				out = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
			serverListenerThread = new ServerListenerThread(this);
			serverListenerThread.start();
			println("Connected to Server: "+ip+":"+port);
			return true;
		} catch (Exception e) {
			println("Failed to connect to Server: "+ip+":"+port);
			return false;
		}
	}

	public static void main(String[] args){
		new Client(args);
	}


	public void newClient(String name) {
		clientList.add(new OtherClient(name));
		println("Other client: "+name);
	}
	
}
