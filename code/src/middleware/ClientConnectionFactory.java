package middleware;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ClientConnectionFactory {
	public static String type;
	public ClientConnection create(SocketChannel channel, SelectionKey key) {
			if(type.equals("ServerMain")){
				return new ClientConnection(channel, key);
			}else if(type.equals("ServerMainWithTime")) {
				return new ClientConnectionWithTime(channel, key);
			}else{
				return null;
			}
	}
}
