package packet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import client.Client;
import server.ClientSocket;
import server.Server;
import data.DataBase;
import data.DataCluster;



public class Packet {

	public static final byte TYPE_ERROR = 0;
	public static final byte TYPE_NEW_CLIENT = 1;
	
	public DataCluster dataCluster = new DataCluster("");
	protected int type;

	public Packet(byte t) {
		type = t;
	}

	public synchronized static Packet readNew(DataInputStream stream) throws IOException {
		byte[] bytes = new byte[1];
		stream.read(bytes);
		byte t = bytes[0];
		Packet d = createNew(t);
		if(d!=null){
			d.read(stream);
		}
		return d;
	}

	public synchronized void read(DataInputStream stream) throws IOException {
		dataCluster = (DataCluster) DataBase.readNew(stream);
	}

	public synchronized void write(DataOutputStream stream) throws IOException {
		stream.writeByte(type);
		dataCluster.write(stream);
		stream.flush();
	}

	private static Packet createNew(byte newType) {
		switch(newType){
		case TYPE_NEW_CLIENT:{return new PacketNewClient();}
		default:{return new Packet(TYPE_ERROR);}
		}
	}

	public void serverRecived(ClientSocket clientSocket) throws IOException {
		clientSocket.server.println("Server Recived Packet Type: "+type);
		//clientSocket.server.println("Server Recived: ");
		//clientSocket.server.println("Packet Data Tree: \n"+dataCluster.convertToString(""));
	}

	public void clientRecived(Client client) {
		client.println("Client Recived Packet Type: "+type);
		//client.println("Client Recived: ");
		//client.println("Packet Data Tree: \n"+dataCluster.convertToString(""));
	}

}
