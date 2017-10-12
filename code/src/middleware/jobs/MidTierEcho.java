package middleware.jobs;

import requests.Request_Echo;
import responses.Response_Echo;
import middleware.ClientConnection;
import middleware.DatabaseRunnable;
import middleware.ResponseDispatcher;
import middleware.Server;


public class MidTierEcho extends DatabaseRunnable {
	private ClientConnection client;
	private Request_Echo req;
	
	public MidTierEcho(ClientConnection client, Request_Echo req) {
		this.client = client;
		this.req = req;
	}
	
	@Override
	public void run() {
		client.set_backEnd_start_time();
		client.set_response_submit_time();
		Server.frontend_pool.execute(new ResponseDispatcher(this.client, new Response_Echo(this.req.getText())));
	}
}
