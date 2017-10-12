package requests;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Request_EraseQueue implements Request {
	
	private int id;
	
	public Request_EraseQueue(int id) {
		this.id = id;
	}

	@Override
	public byte[] getPayLoad() throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream s = new DataOutputStream(bytes);
		s.writeByte(Request.DELETE_QUEUE);
		s.writeInt(this.id);
		s.flush();	
		return bytes.toByteArray();
	}
}
