package middleware.jobs;

import java.util.ArrayList;

import requests.Request_Query;
import responses.Response_Query;
import db.QueueTask;
import middleware.ClientConnection;
import middleware.DatabaseRunnable;
import middleware.ResponseDispatcher;
import middleware.Server;

public class MidTierQuery extends DatabaseRunnable {
	private Request_Query request;
	private ClientConnection client_connection;
	public MidTierQuery(Request_Query requset, ClientConnection client_connection) {
		this.request = requset;
		this.client_connection = client_connection;
	}
	
	@Override
	public void run() {
		this.client_connection.set_backEnd_start_time();
		ArrayList<Integer> queue_ids = QueueTask.queryQueues(con, this.request.getReceiver());
		this.client_connection.set_response_submit_time();
		Server.frontend_pool.execute(new ResponseDispatcher(this.client_connection, new Response_Query(queue_ids)));
	}
}
