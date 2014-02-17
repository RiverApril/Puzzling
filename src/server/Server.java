package server;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import data.DataBase;
import data.DataCluster;
import data.DataInt;


public class Server {
	
	private File settingsFile = new File("serverSettings.dat");
	private DataCluster settingsCluster = new DataCluster("");
	
	public ServerSocket serverSocket;
	public NewClientListenerThread clientListenerThread;
	
	public boolean echoMode = false;

	public Server(String[] args) {
		settingsCluster.add(new DataInt("port", 1337));
		initSettingsFile();
		startServer(((DataInt)settingsCluster.get("port")).data);
		echoMode = true;
	}

	private void startServer(int port) {
		try {
			serverSocket = new ServerSocket(port);
			clientListenerThread = new NewClientListenerThread(this);
			clientListenerThread.start();
			System.out.println("Server Started on port: "+port);
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		new Server(args);
	}

	public void newClientConnected(ClientSocket c) {
		Socket s = c.socket;
		System.out.println("Client Connected: "+s.getInetAddress().getHostAddress()+":"+s.getPort());
	}
	
}
