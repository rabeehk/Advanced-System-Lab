package responses;

import java.io.DataOutputStream;
import java.io.IOException;

public class Response_WriteMessage implements Response {
	private boolean validity;
	public boolean isValid() {
		return validity;
	}
	public Response_WriteMessage(boolean s) {
		this.validity = s;
	}
	@Override
	public void send(DataOutputStream s) throws IOException {
		if (validity) {
			s.writeByte(Response.SEND_VALID);
		} else {
			s.writeByte(Response.SEND_NOT_VALID);
		}
		s.flush();
	}
}