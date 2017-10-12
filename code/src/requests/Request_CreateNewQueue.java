package requests;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Request_CreateNewQueue implements Request {
	
	@Override
	public byte[] getPayLoad() throws IOException {
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		DataOutputStream s = new DataOutputStream(bytes);
		s.writeByte(Request.CREATE_QUEUE);
		s.flush();	
		return bytes.toByteArray();
	}
}
