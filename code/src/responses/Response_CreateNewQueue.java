package responses;

import java.io.DataOutputStream;
import java.io.IOException;

public class Response_CreateNewQueue implements Response {
	private int queue_id;
	public int getQueueID() {
		return this.queue_id;
	}
	public boolean isValid() {
		return this.queue_id > 0;
	}
	public Response_CreateNewQueue(int id) {
		this.queue_id = id;
	}
	@Override
	public void send(DataOutputStream dos) throws IOException {
		dos.writeByte(Response.CREATED_QUEUE);
		dos.writeInt(this.queue_id);
		dos.flush();
	}
}
