package middleware;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.Iterator;
import java.util.Set;

//useful tutorials
//http://rox-xmlrpc.sourceforge.net/niotut/src/NioServer.java
//http://tutorials.jenkov.com/java-nio/server-socket-channel.html
//http://www.java2s.com/Tutorial/Java/0320__Network/ServerSocketChannel.htm
//Design tutorial
//https://today.java.net/pub/a/today/2007/02/13/architecture-of-highly-scalable-nio-server.html


public class Server implements Runnable {
	private final static Logger LOGGER = Logger.getLogger(Server.class.getName());

	private ServerSocketChannel server_socket_channel;
	public static ExecutorService frontend_pool;
	public static ExecutorService database_pool;
	private ClientConnectionFactory clientConFactory;
	
	
	static int number_of_db_connections;
	public static void setNumberOfDBConnections(int number_of_db_connections){
		Server.number_of_db_connections = number_of_db_connections;
	}
	
	public Server(ServerSocketChannel ssc, ClientConnectionFactory ccf) {
		this.server_socket_channel = ssc;
		frontend_pool = Executors.newFixedThreadPool(15);
		//System.out.println(this.number_of_db_connections);
		database_pool = new DatabaseThreadPoolExecutor(10, 10, 0, TimeUnit.NANOSECONDS, new LinkedBlockingQueue<Runnable>());
		this.clientConFactory = ccf;
		LOGGER.fine("Server init done");
	}

	// http://rox-xmlrpc.sourceforge.net/niotut/src/NioServer.java
	public void run() {
			try {
			Selector selector = SelectorProvider.provider().openSelector();
			// Accept the connection and make it non-blocking
			server_socket_channel.configureBlocking(false);	
			// Register the new SocketChannel with our Selector, indicating
			// we'd like to be notified when there's data waiting to be read
			server_socket_channel.register(selector, SelectionKey.OP_ACCEPT);	
	
			while(true) {
				// wait for an event of the registered channels
				int count = selector.select();
				if(count == 0) continue;
				Set<SelectionKey> keys = selector.selectedKeys();
				// Iterate over the set of keys for which events are available
				Iterator<SelectionKey> iterator_key = keys.iterator();
				while(iterator_key.hasNext()) {
				    SelectionKey key = iterator_key.next();
				    if (!key.isValid()) {
				    	continue;
				    }
				    // check what event is available and deal with it
				    if(key.isAcceptable()) {
					    LOGGER.info("new connection is received now");
						ServerSocketChannel socket = (ServerSocketChannel) key.channel();
						SocketChannel channel = socket.accept();
						channel.configureBlocking(false);
						channel.register(selector, SelectionKey.OP_READ);
				    }
				    // Register the new SocketChannel with our Selector, indicating
					// we'd like to be notified when there's data waiting to be read 
				    else if (key.isReadable()) {
				    	ClientConnection con = (ClientConnection) key.attachment();
				    	if (con == null) {
				    		con = clientConFactory.create((SocketChannel) key.channel(), key);
				    		key.attach(con);
				    	}
				    	con.set_frontEnd_submit_time();
						byte[] data = con.read((SocketChannel) key.channel());
						if (data != null) {
							Server.frontend_pool.execute(new RequestDispatcher(con, data, con.getLength()));
						}
				    }
				    iterator_key.remove();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		// shut down and close the socket_channel	
		try {
			this.server_socket_channel.close();
		} catch (IOException e) {
		}
		frontend_pool.shutdown();
		database_pool.shutdown();
	}
}
