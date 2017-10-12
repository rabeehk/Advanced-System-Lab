package middleware.jobs;

import responses.Response_CreateNewQueue;
import db.QueueTask;
import middleware.ClientConnection;
import middleware.DatabaseRunnable;
import middleware.ResponseDispatcher;
import middleware.Server;

public class MidTierCreateQueue extends DatabaseRunnable {

	private ClientConnection client;
	
	public MidTierCreateQueue(ClientConnection client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		this.client.set_backEnd_start_time();
		int id = QueueTask.createQueue(con, 0);
		this.client.set_response_submit_time();
		Server.frontend_pool.execute(new ResponseDispatcher(this.client, new Response_CreateNewQueue(id)));
	}
}
