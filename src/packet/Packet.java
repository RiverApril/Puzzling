package packet;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import client.Client;

import server.ClientSocket;

import data.DataCluster;



public class Packet {

	public static final byte TYPE_ERROR = 0;
	public static final byte TYPE_NEW_CLIENT = 1;
	
	public DataCluster dataCluster = new DataCluster("");
	private int type;

	public Packet(byte t) {
		type = t;
	}

	public static Packet readNew(DataInputStream stream) throws IOException {
		byte t = stream.readByte();
		Packet d = createNew(t);
		if(d!=null){
			d.read(stream);
		}
		return d;
	}

	private void read(DataInputStream stream) throws IOException {
		dataCluster.read(stream);
	}

	public void write(DataOutputStream stream) throws IOException {
		stream.writeByte(type);
		dataCluster.write(stream);
	}

	private static Packet createNew(byte newType) {
		switch(newType){
		case TYPE_NEW_CLIENT:{return new PacketNewClient();}
		default:{return new Packet(TYPE_ERROR);}
		}
	}

	public void serverRecived(ClientSocket clientSocket) throws IOException {
		System.out.println("Server Recived: ");
		System.out.println(dataCluster.convertToString());
		if(clientSocket.server.echoMode){
			write(clientSocket.clientCommunicatorThread.out);
		}
	}

	public void clientRecived(Client client) {
		System.out.println("Client Recived: ");
	}

}
