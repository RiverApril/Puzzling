package data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class DataBase {

	private static final byte TYPE_ERROR = 0;
	public static final byte TYPE_CLUSTER = 1;
	public static final byte TYPE_INT = 2;
	public static final byte TYPE_STRING = 3;
	public static final String[] types = {"ERROR", "Cluster", "Int", "String"};
	
	protected String name = "";
	private byte type = TYPE_ERROR;

	public DataBase(String n, byte t) {
		name = n;
		type = t;
	}

	public String getName() {
		return name;
	}

	public byte getType() {
		return type;
	}

	public void write(DataOutputStream stream) throws IOException {
		stream.writeByte(type);
		stream.writeUTF(name);
	}

	public void read(DataInputStream stream) throws IOException {
		
	}

	public static DataBase readNew(DataInputStream stream) throws IOException {
		byte t = stream.readByte();
		String s = stream.readUTF();
		DataBase d = createNew(s, t);
		d.read(stream);
		return d;
	}

	private static DataBase createNew(String name, byte newType) {
		switch(newType){
		case TYPE_CLUSTER:{return new DataCluster(name);}
		case TYPE_INT:{return new DataInt(name, 0);}
		case TYPE_STRING:{return new DataString(name, "");}
		default:{return new DataBase(name, TYPE_ERROR);}
		}
	}

	public String convertToString(String prefix) {
		return prefix + nameStringPrefix();
	}

	protected String nameStringPrefix() {
		return "("+DataBase.types[getType()]+")"+name+":";
	}

}
