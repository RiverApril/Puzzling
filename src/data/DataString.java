package data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class DataString extends DataBase {

	public String data = "";

	public DataString(String n, String d) {
		super(n, DataBase.TYPE_STRING);
		this.data  = d;
	}

	@Override
	public void write(DataOutputStream stream) throws IOException {
		super.write(stream);
		stream.writeUTF(data);
	}

	@Override
	public void read(DataInputStream stream) throws IOException {
		super.read(stream);
		data = stream.readUTF();
	}

	@Override
	public String convertToString(String prefix) {
		return prefix + (nameStringPrefix()+data);
	}

}
