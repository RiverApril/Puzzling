package data;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;


public class DataInt extends DataBase {

	public int data = 0;

	public DataInt(String n, int d) {
		super(n, DataBase.TYPE_INT);
		this.data  = d;
	}

	@Override
	public void write(DataOutputStream stream) throws IOException {
		super.write(stream);
		stream.writeInt(data);
	}

	@Override
	public void read(DataInputStream stream) throws IOException {
		super.read(stream);
		data = stream.readInt();
	}

	@Override
	public String convertToString(String prefix) {
		return prefix + (nameStringPrefix()+data);
	}

}
