package middleware.jobs;

import responses.Response_Empty;
import responses.Response_Text;
import requests.Request_GetTopMessage;
import db.MessageTask;
import middleware.ClientConnection;
import middleware.DatabaseRunnable;
import middleware.ResponseDispatcher;
import middleware.Server;

public class MidTierPeek extends DatabaseRunnable {
	private Request_GetTopMessage request;
	private ClientConnection client_connection;
	public MidTierPeek(Request_GetTopMessage request, ClientConnection client_connection) {
		this.request = request;
		this.client_connection = client_connection;
	}
	@Override
	public void run() {
		this.client_connection.set_backEnd_start_time();
		MessageTask res = MessageTask.getTopMessage(con, this.request.getQueue(), this.request.getReceiver());
		this.client_connection.set_response_submit_time();
		if (res == null) {
			Server.frontend_pool.execute(new ResponseDispatcher(this.client_connection, new Response_Empty()));
		} else {
			Server.frontend_pool.execute(new ResponseDispatcher(this.client_connection, new Response_Text(res.getMessage())));
		}
	}
}
