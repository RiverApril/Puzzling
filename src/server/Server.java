package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import other.Settings;
import packet.Packet;
import data.DataBase;
import data.DataCluster;
import data.DataInt;


public class Server extends Settings {
	
	public ServerSocket serverSocket;
	public NewClientListenerThread newClientListenerThread;
	
	public ArrayList<ClientSocket> clientList = new ArrayList<ClientSocket>();

	public Server(String[] args) {
		settingsFile = new File("serverSettings.dat");
		settingsCluster.add(new DataInt("port", 1337));
		initSettingsFile();
		startServer(((DataInt)settingsCluster.get("port")).data);
	}

	private void startServer(int port) {
		try {
			serverSocket = new ServerSocket(port);
			newClientListenerThread = new NewClientListenerThread(this);
			newClientListenerThread.start();
			println("Server Started on port: "+port);
		} catch (BindException e) {
			exit("Failed to bind to port: "+port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void exit(String reason) {
		println("Exit Server: \""+reason+"\"");
		System.exit(0);
	}

	public static void main(String[] args){
		new Server(args);
	}

	public void newClientConnected(ClientSocket c) {
		println("Client Connected ("+c.getId()+"): "+c.socket.getInetAddress().getHostAddress()+":"+c.socket.getPort());
	}

	public void clientHasSetName(String name, ClientSocket from) {
		from.setName(name);
	}

	public void sendToAllBut(Packet packet, ClientSocket exclude) {
		for(int i=0;i<clientList.size();i++){
			ClientSocket c = clientList.get(i);
			if(exclude.getId() != c.getId()){
				sendTo(packet, c);
			}
		}
	}

	public void sendToAll(Packet packet) {
		for(int i=0;i<clientList.size();i++){
			ClientSocket c = clientList.get(i);
			sendTo(packet, c);
		}
	}

	public void sendTo(Packet packet, ClientSocket c) {
		try {
			packet.write(c.out);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void println(String string) {
		System.out.println("<Server> "+string);
	}
	
}
