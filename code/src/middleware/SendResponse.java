package middleware;

import java.io.DataOutputStream;
import java.io.IOException;

import responses.Response;

public class SendResponse implements Response {
	private boolean validity;
	public SendResponse(boolean s) {
		this.validity = s;
	}
	boolean isValid() {
		return validity;
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
