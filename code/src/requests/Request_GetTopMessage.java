package requests;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Request_GetTopMessage implements Request {

	private int receiver;
	private int queue;
	
	public Request_GetTopMessage(int receiver, int queue) {
		super();
		this.receiver = receiver;
		this.queue = queue;
	}

	public int getReceiver() {
		return receiver;
	}

	public int getQueue() {
		return queue;
	}
	
	@Override
	public byte[] getPayLoad() throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream s = new DataOutputStream(bytes);
		s.writeByte(Request.PEEK_QUEUE);
		s.writeInt(this.receiver);
		s.writeInt(this.queue);		s.flush();	
		return bytes.toByteArray();
	}
}
