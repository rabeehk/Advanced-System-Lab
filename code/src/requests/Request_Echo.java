package requests;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Request_Echo implements Request {
	private String text;
	
	public Request_Echo(String s) {
		this.text = s;
	}

	@Override
	public byte[] getPayLoad() throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream s = new DataOutputStream(bytes);
		s.writeByte(Request.ECHO_REQUEST);
		s.writeUTF(this.text);
		s.flush();	
		return bytes.toByteArray();
	}
	public String getText() {
		return this.text;
	}
}
