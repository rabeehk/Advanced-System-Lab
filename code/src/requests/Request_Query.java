package requests;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Request_Query implements Request {
	private int receiver;

	public Request_Query(int receiver) {
		this.receiver = receiver;
	}

	
	@Override
	public byte[] getPayLoad() throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream s = new DataOutputStream(bytes);
		s.writeByte(Request.QUERY_QUEUES);
		s.writeInt(receiver);
		s.flush();	
		return bytes.toByteArray();
	}

	public int getReceiver() {
		return this.receiver;
	}
}
