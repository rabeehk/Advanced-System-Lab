package middleware;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import db.ConnectionManager;


public class ServerMainWithTime {
	private final static Logger Log = Logger.getLogger(ServerMainWithTime.class.getName());
	public static void main(String args[]){
		try{
			ServerTimeLogs.experimentID=args[0];
			// we need serverInstance to be able to log for several
			// middleware instance together 
			ServerTimeLogs.serverInstance=Integer.parseInt(args[1]);
			int portNumber = Integer.parseInt(args[2]);
			ConnectionManager.setURL(args[3]);
			//	"jdbc:postgresql://localhost:5432/db?user=rabeehk&password=AllahoSamad@))%"
			ClientConnectionFactory.type="ServerMainWithTime";
			ServerSocketChannel ssc=ServerSocketChannel.open();
			ssc.socket().bind(new InetSocketAddress(portNumber));
			Server server=new Server(ssc, new ClientConnectionFactory());
			Log.info("Server with time get started");
			server.run();
		}catch(IOException e){
			Log.log(Level.SEVERE, "Error happend in ServerMainWithTime", e);
		}
	}
}
