package responses;

import java.io.DataOutputStream;
import java.io.IOException;

public interface Response {
	public static byte INVALID = 9;
	public static int ECHO_RESPONSE = 10;
	public static byte SEND_VALID = 11;
	public static byte SEND_NOT_VALID = 12;
	public static int QUERY = 13;
	public static byte TEXT = 14;
	public static byte EMPTY = 15;
	public static int DELETED_QUEUE = 16;
	public static int CREATED_QUEUE = 17;
	public void send(DataOutputStream s) throws IOException;
}