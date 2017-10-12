package responses;

import java.io.DataOutputStream;
import java.io.IOException;

public class Response_Text implements Response {
	private String text;
	public String getText() {
		return this.text;
	}
	public Response_Text(String text) {
		this.text = text;
	}
	
	@Override
	public void send(DataOutputStream s) throws IOException {
		s.writeByte(Response.TEXT);
		s.writeUTF(this.text);
		s.flush();
	}
}
