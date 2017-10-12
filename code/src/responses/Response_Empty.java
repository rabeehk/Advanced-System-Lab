package responses;

import java.io.DataOutputStream;
import java.io.IOException;

public class Response_Empty implements Response {
	@Override
	public void send(DataOutputStream dos) throws IOException {
		dos.writeByte(Response.EMPTY);
		dos.flush();
	}
}
