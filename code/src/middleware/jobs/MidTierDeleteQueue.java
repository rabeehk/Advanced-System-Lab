package middleware.jobs;

import responses.Response_EraseQueue;
import db.QueueTask;
import middleware.ClientConnection;
import middleware.DatabaseRunnable;
import middleware.ResponseDispatcher;
import middleware.Server;

public class MidTierDeleteQueue extends DatabaseRunnable {
	private ClientConnection client_connection;
	private int id;
	
	public MidTierDeleteQueue(ClientConnection client_connection, int id) {
		this.client_connection = client_connection;
		this.id = id;
	}
	
	@Override
	public void run() {
		client_connection.set_backEnd_start_time();
		boolean ok = QueueTask.deleteQueue(con, this.id);
		client_connection.set_response_submit_time();
		Server.frontend_pool.execute(new ResponseDispatcher(this.client_connection, new Response_EraseQueue(ok)));
	}
}
