package middleware.jobs;

import responses.Response_Empty;
import responses.Response_Text;
import requests.Request_GetTopMessageWithSender;
import db.MessageTask;
import middleware.ClientConnection;
import middleware.DatabaseRunnable;
import middleware.ResponseDispatcher;
import middleware.Server;

public class MidTierPeekSender extends DatabaseRunnable {
	private Request_GetTopMessageWithSender request;
	private ClientConnection client_connection;
	public MidTierPeekSender(Request_GetTopMessageWithSender req, ClientConnection socket) {
		this.request = req;
		this.client_connection = socket;
	}
	@Override
	public void run() {
		this.client_connection.set_backEnd_start_time();
		MessageTask res = MessageTask.peekMessageFromSender(con, this.request.getSender(), this.request.getReceiver());
		this.client_connection.set_response_submit_time();
		if (res == null) {
			Server.frontend_pool.execute(new ResponseDispatcher(this.client_connection, new Response_Empty()));
		} else {
			Server.frontend_pool.execute(new ResponseDispatcher(this.client_connection, new Response_Text(res.getMessage())));
		}
	}
}
