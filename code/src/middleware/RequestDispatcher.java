package middleware;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import requests.Request_Echo;
import responses.Response_WrongRequest;
import requests.Request_GetTopMessage;
import requests.Request_GetTopMessageWithSender;
import requests.Request_PopMessage;
import requests.Request_Query;
import requests.Request;
import requests.Request_WriteMessage;
import middleware.jobs.MidTierCreateQueue;
import middleware.jobs.MidTierDeleteQueue;
import middleware.jobs.MidTierEcho;
import middleware.jobs.MidTierPeek;
import middleware.jobs.MidTierPeekSender;
import middleware.jobs.MidTierPop;
import middleware.jobs.MidTierQuery;
import middleware.jobs.MidTierSend;


public class RequestDispatcher implements Runnable {
	private final static Logger LOGGER = Logger.getLogger(RequestDispatcher.class.getName());
	private ClientConnection client;
	DataInputStream inStream;

	public RequestDispatcher(ClientConnection con, byte[] data, int length) {
		this.client = con;
		inStream = new DataInputStream(new ByteArrayInputStream(data, 4, length));
	}

	protected void cleanup() {
		this.client.close();
	}

	@Override
	public void run() {
		try {
			this.client.set_frontEnd_start_time();
			byte task = inStream.readByte();	
			Runnable r = null;
			if (task == Request.SEND_NO_RECEIVER) {
				Request req = new Request_WriteMessage(inStream.readInt(), -1, inStream.readInt(), inStream.readUTF());
				r = new MidTierSend((Request_WriteMessage) req, client);
			} else if (task == Request.PEEK_QUEUE) {
				Request_GetTopMessage req = new Request_GetTopMessage(inStream.readInt(), inStream.readInt());
				r = new MidTierPeek(req, client);
			} else 	if (task == Request.SEND_TO_RECEIVER) {
				Request_WriteMessage req = new Request_WriteMessage(inStream.readInt(), inStream.readInt(),inStream.readInt(),inStream.readUTF());
				r = new MidTierSend(req, client);
			}  else if (task == Request.PEEK_SENDER) {
				Request_GetTopMessageWithSender req = new Request_GetTopMessageWithSender(inStream.readInt(), inStream.readInt());
				r = new MidTierPeekSender(req, client);
			} else if (task == Request.POP_QUEUE) {
				Request_PopMessage req = new Request_PopMessage(inStream.readInt(), inStream.readInt());
				r = new MidTierPop(req, client);
			} else if (task == Request.CREATE_QUEUE) {
				r = new MidTierCreateQueue(client);
			} else if (task == Request.DELETE_QUEUE) {
				r = new MidTierDeleteQueue(client, inStream.readInt());
			} else if (task == Request.QUERY_QUEUES) {
				r = new MidTierQuery(new Request_Query(inStream.readInt()), client);
			} else if (task == Request.ECHO_REQUEST) {
				r = new MidTierEcho(client, new Request_Echo(inStream.readUTF()));
			}
			if (r != null) {
				this.client.set_backEnd_submit_time();
				Server.database_pool.execute(r);
			} else {
				LOGGER.warning("request is not known");
				ByteArrayOutputStream ooutputStream = new ByteArrayOutputStream();
				DataOutputStream out = new DataOutputStream(ooutputStream);
				(new Response_WrongRequest()).send(out);
				client.write(ooutputStream);
				this.client.close();
			}
		} catch (IOException e) {
			LOGGER.fine("client is not avialable anymore");
		}
	}
}
