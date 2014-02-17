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

import packet.PacketNewClient;

import server.NewClientListenerThread;
import server.Server;
import data.DataBase;
import data.DataCluster;
import data.DataInt;
import data.DataString;


public class Client {
	private File settingsFile = new File("clientSettings.dat");
	private DataCluster settingsCluster = new DataCluster("");
	
	public Socket socket;
	public ServerListenerThread serverListenerThread;

	public Client(String[] args) {
		settingsCluster.add(new DataInt("port", 1337));
		settingsCluster.add(new DataString("ip", "localhost"));
		initSettingsFile();
		joinServer(((DataString)settingsCluster.get("ip")).data, ((DataInt)settingsCluster.get("port")).data);
		System.out.println("About to write...");
		try {
			new PacketNewClient().write(serverListenerThread.out);
			serverListenerThread.out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("Wrote.");
	}

	public void serverClosed() {
		socket = null;
		serverListenerThread = null;
	}
	
	private void joinServer(String ip, int port) {
		try {
			socket = new Socket(ip, port);
			serverListenerThread = new ServerListenerThread(this);
			serverListenerThread.start();
			System.out.println("Connected to Server: "+ip+":"+port);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Failed to connect to Server: "+ip+":"+port);
	}

	private void initSettingsFile() {
		if(settingsFile.exists()){
			loadSettingsFile();
		}else{
			saveSettingsFile();
		}
		
	}

	private boolean saveSettingsFile() {
		try{
			FileOutputStream s = new FileOutputStream(settingsFile);
			DataOutputStream ds = new DataOutputStream(s);
			settingsCluster.write(ds);
			ds.close();
			s.close();
			System.out.println("Saved "+settingsFile.getPath());
			return true;
		}catch(Exception e){
			System.out.println("Failed to Save "+settingsFile.getPath());
			e.printStackTrace();
			return false;
		}
	}

	private boolean loadSettingsFile() {
		try{
			FileInputStream s = new FileInputStream(settingsFile);
			DataInputStream ds = new DataInputStream(s);
			settingsCluster = (DataCluster) DataBase.readNew(ds);
			ds.close();
			s.close();
			System.out.println("Loaded "+settingsFile.getPath());
			return true;
		}catch(Exception e){
			System.out.println("Failed to Load "+settingsFile.getPath());
			e.printStackTrace();
			return false;
		}
	}

	public static void main(String[] args){
		new Client(args);
	}
}
