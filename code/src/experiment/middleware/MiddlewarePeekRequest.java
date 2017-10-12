package experiment.middleware;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import responses.Response_Empty;
import responses.Response_WrongRequest;
import responses.Response_Text;
import requests.Request_GetTopMessageWithSender;
import responses.Response;
import client.Client;
import experiment.ExperimentTask;
import experiment.Configuration;


public class MiddlewarePeekRequest implements ExperimentTask {
	private final static Logger LOGGER = Logger.getLogger(MiddlewarePeekRequest.class.getName());
	private static Random random = new Random();
	private Client client;
	public MiddlewarePeekRequest(String host, int port) {
		try {
			this.client = new Client(host, port);
		} catch (IOException e) {
			this.client = null;
		}
	}
	
	@Override
	public void execute() {
		int receiver_id = random.nextInt(Configuration.USER_COUNT);
		Request_GetTopMessageWithSender req = new Request_GetTopMessageWithSender(receiver_id, random.nextInt(Configuration.USER_COUNT));
		
		try {
			Response resp = client.send(req);
			if (resp instanceof Response_Text) {
				Response_Text msg = (Response_Text) resp;
				if (msg.getText().length() != Configuration.TEXT_LENGTH) {
					LOGGER.severe("Message length is invalid");
				}
			} else if (resp instanceof Response_Empty) {
				LOGGER.warning("queue was empty");
			} else if (resp instanceof Response_WrongRequest) {
				throw new RuntimeException("unkown response");
			}
		} catch (IOException e) {
		    LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
