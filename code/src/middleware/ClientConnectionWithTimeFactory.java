package middleware;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ClientConnectionWithTimeFactory {
	public ClientConnection create(SocketChannel channel, SelectionKey key) {
		return new ClientConnectionWithTime(channel, key);
	}
}
