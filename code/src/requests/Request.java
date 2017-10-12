package requests;
import java.io.IOException;

public interface Request {
	public static final byte SEND_NO_RECEIVER = 0;
	public static final byte SEND_TO_RECEIVER = 1;
	public static final int CREATE_QUEUE = 2;
	public static final int DELETE_QUEUE = 3;
	public static final byte PEEK_QUEUE = 4;
	public static final byte PEEK_SENDER = 5;
	public static final byte POP_QUEUE = 6;
	public static final int QUERY_QUEUES = 7;
	public static final int ECHO_REQUEST = 8;
	public byte[] getPayLoad() throws IOException;
}
