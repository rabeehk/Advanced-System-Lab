package requests;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Request_WriteMessage implements Request {
	private int sender_id;
	private int receiver_id;
	private int queue_id;
	private String message;
	
	public Request_WriteMessage(int sender, int receiver, int queue, String text) {
		super();
		this.sender_id = sender;
		this.receiver_id = receiver;
		this.queue_id = queue;
		this.message = text;
	}
	public int getSenderID() {
		return sender_id;
	}

	public int getReceiver() {
		return receiver_id;
	}

	public int getQueue() {
		return queue_id;
	}

	public String getText() {
		return message;
	}
	
	@Override
	public byte[] getPayLoad() throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream dos = new DataOutputStream(bytes);
		if (this.receiver_id == -1) {
			dos.writeByte(Request.SEND_NO_RECEIVER);
			dos.writeInt(this.sender_id);
			dos.writeInt(this.queue_id);
			dos.writeUTF(this.message);
		} else {
			dos.writeByte(Request.SEND_TO_RECEIVER);
			dos.writeInt(this.sender_id);
			dos.writeInt(this.receiver_id);
			dos.writeInt(this.queue_id);
			dos.writeUTF(this.message);
		}
		dos.flush();	
		return bytes.toByteArray();
	}
}