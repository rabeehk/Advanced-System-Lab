package middleware.jobs;

import requests.Request_WriteMessage;
import responses.Response_WriteMessage;
import db.MessageTask;
import middleware.ClientConnection;
import middleware.DatabaseRunnable;
import middleware.ResponseDispatcher;
import middleware.Server;

public class MidTierSend extends DatabaseRunnable {
	private Request_WriteMessage request;
	private ClientConnection client_connection;
	
	public MidTierSend(Request_WriteMessage requset, ClientConnection client_connection) {
		this.request = requset;
		this.client_connection = client_connection;
	}
	
	@Override
	public void run() {
		this.client_connection.set_backEnd_start_time();
		boolean res;
		if (request.getReceiver() == -1) {
			res = MessageTask.sendMessage(con, request.getSenderID(), request.getQueue(), request.getText());
		} else {
			res = MessageTask.sendMessage(con, request.getSenderID(), request.getReceiver(), request.getQueue(), request.getText());
		}
		this.client_connection.set_response_submit_time();
		Server.frontend_pool.execute(new ResponseDispatcher(client_connection, new Response_WriteMessage(res)));
	}
}
