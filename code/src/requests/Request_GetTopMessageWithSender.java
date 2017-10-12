package requests;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Request_GetTopMessageWithSender implements Request {

	private int sender;
	private int receiver;
	
	public Request_GetTopMessageWithSender(int sender, int queue) {
		super();
		this.sender = sender;
		this.receiver = queue;
	}

	public int getSender() {
		return sender;
	}

	public int getReceiver() {
		return receiver;
	}
	
	@Override
	public byte[] getPayLoad() throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream s = new DataOutputStream(bytes);
		s.writeByte(Request.PEEK_SENDER);
		s.writeInt(this.sender);
		s.writeInt(this.receiver);
		s.flush();	
		return bytes.toByteArray();
	}
}
