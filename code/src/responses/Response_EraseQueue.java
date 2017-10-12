package responses;

import java.io.DataOutputStream;
import java.io.IOException;

public class Response_EraseQueue implements Response {
	private boolean validity;
	public Response_EraseQueue(boolean valid) {
		this.validity = valid;
	}
	public boolean isDone() {
		return this.validity;
	}
	@Override
	public void send(DataOutputStream dos) throws IOException {
		dos.writeByte(Response.DELETED_QUEUE);
		dos.writeBoolean(this.validity);
		dos.flush();
	}
}
