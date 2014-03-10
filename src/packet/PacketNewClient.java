package packet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import server.ClientSocket;
import client.Client;
import data.DataBase;
import data.DataCluster;
import data.DataString;

public class PacketNewClient extends Packet {
	
	public PacketNewClient(String clientName){
		super(Packet.TYPE_NEW_CLIENT);
		dataCluster.add(new DataString("name", clientName));
	}

	public PacketNewClient() {
		this("undefined");
	}

	public void serverRecived(ClientSocket clientSocket) throws IOException {
		super.serverRecived(clientSocket);
		clientSocket.server.clientHasSetName(((DataString)dataCluster.get("name")).data, clientSocket);
		clientSocket.server.sendToAllBut(this, clientSocket);
		for(int i=0;i<clientSocket.server.clientList.size();i++){
			ClientSocket c = clientSocket.server.clientList.get(i);
			if(c.getId() != clientSocket.getId()){
				clientSocket.server.sendTo(new PacketNewClient(c.name), clientSocket);
			}
		}
	}

	public void clientRecived(Client client) {
		super.clientRecived(client);
		client.newClient(((DataString)dataCluster.get("name")).data);
	}

}
