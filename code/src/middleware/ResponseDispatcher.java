package middleware;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import responses.Response;



public class ResponseDispatcher implements Runnable {
	private final static Logger LOGGER = Logger.getLogger(ResponseDispatcher.class.getName());
	private ClientConnection client;
	private Response resp;

	public ResponseDispatcher(ClientConnection con, Response resp) {
		this.client = con;
		this.resp = resp;
		LOGGER.fine("gets a new connection");
	}
	protected void cleanup() {
		this.client.close();
	}

	@Override
	public void run() {
			this.client.set_response_start_time();
			try {
				ByteArrayOutputStream oStream = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(oStream);
				resp.send(out);
				client.write(oStream);
			} catch (IOException e) {
				LOGGER.log(Level.SEVERE, "filesystem IO error happened", e);
			}
			this.client.set_response_completed_time();
			this.client.printTimes();
	}
}
