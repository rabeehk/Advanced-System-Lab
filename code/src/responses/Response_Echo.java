package responses;

import java.io.DataOutputStream;
import java.io.IOException;

public class Response_Echo implements Response {
	private String text;
	public Response_Echo(String text) {
		this.text = text;
	}
	@Override
	public void send(DataOutputStream dos) throws IOException {
		dos.writeByte(Response.ECHO_RESPONSE);
		dos.writeUTF(this.text);
		dos.flush();
	}
	public String getText() {
		return this.text;
	}
}