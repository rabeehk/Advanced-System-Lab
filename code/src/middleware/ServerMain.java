package middleware;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import db.ConnectionManager;

public class ServerMain {
	private final static Logger LOGGER = Logger.getLogger(ServerMain.class
			.getName());

	public static void main(String args[]) {
		try {
			ConnectionManager.setURL(args[1]);
			int port = 	Integer.parseInt(args[0]);
			ServerSocketChannel ssc = ServerSocketChannel.open();
			ssc.socket().bind(new InetSocketAddress(port));
			ClientConnectionFactory.type="ServerMain";
			Server server = new Server(ssc, new ClientConnectionFactory());
			LOGGER.info("Server is initialized now");
			server.run();
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, "Error in ServerMain", e);
		}
	}
}
