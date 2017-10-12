package middleware.jobs;

import responses.Response_Empty;
import responses.Response_Text;
import requests.Request_PopMessage;
import db.MessageTask;
import middleware.ClientConnection;
import middleware.DatabaseRunnable;
import middleware.ResponseDispatcher;
import middleware.Server;

public class MidTierPop extends DatabaseRunnable {
	private Request_PopMessage request;
	private ClientConnection client_connection;
	public MidTierPop(Request_PopMessage req, ClientConnection client) {
		this.request = req;
		this.client_connection = client;
	}
	@Override
	public void run() {
		this.client_connection.set_backEnd_start_time();
		MessageTask res = MessageTask.popMessage(con, this.request.getQueue(), this.request.getReceiver());
		this.client_connection.set_response_submit_time();
		if (res == null) {
			Server.frontend_pool.execute(new ResponseDispatcher(client_connection, new Response_Empty()));
		} else {
			Server.frontend_pool.execute(new ResponseDispatcher(client_connection, new Response_Text(res.getMessage())));
		}
	}
}
