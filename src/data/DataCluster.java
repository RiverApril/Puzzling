package data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import server.ClientListenerThread;
import server.ClientSocket;


public class DataCluster extends DataBase {
	
	private ArrayList<DataBase> data = new ArrayList<DataBase>();

	public DataCluster(String n) {
		super(n, DataBase.TYPE_CLUSTER);
	}

	@Override
	public void write(DataOutputStream stream) throws IOException {
		super.write(stream);
		stream.writeInt(data.size());
		for(int i=0;i<data.size();i++){
			data.get(i).write(stream);
		}
	}

	@Override
	public void read(DataInputStream stream) throws IOException {
		super.read(stream);
		int size = stream.readInt();
		for(int i=0;i<size;i++){
			data.add(DataBase.readNew(stream));
		}
	}

	public void add(DataBase d) {
		data.add(d);
	}

	public DataBase get(String n) {
		for(int i=0;i<data.size();i++){
			if(data.get(i).getName().equals(n)){
				return data.get(i);
			}
		}
		return null;
	}

	public String convertToString() {
		String s = nameStringPrefix();
		s+="\n";
		for(int i=0;i<data.size();i++){
			s += data.get(i).convertToString();
			s+="\n";
		}
		return s;
	}

}
