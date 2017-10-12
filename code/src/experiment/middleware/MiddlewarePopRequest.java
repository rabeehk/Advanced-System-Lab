package experiment.middleware;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import responses.Response_Empty;
import responses.Response_WrongRequest;
import responses.Response_Text;
import requests.Request_PopMessage;
import responses.Response;
import client.Client;
import experiment.ExperimentTask;
import experiment.Configuration;


public class MiddlewarePopRequest implements ExperimentTask {
	private final static Logger LOGGER = Logger.getLogger(MiddlewarePopRequest.class.getName());
	
	private static Random random = new Random();
	private Client client;
	private List<Integer> queues;
	
	public MiddlewarePopRequest(String host, int port, List<Integer> queues) {
		this.queues = queues;
		try {
			this.client = new Client(host, port);
		} catch (IOException e) {
			this.client = null;
		}
	}
	
	@Override
	public void execute() {
		int receiver_id = random.nextInt(Configuration.USER_COUNT);
		Request_PopMessage request = new Request_PopMessage(receiver_id, Configuration.getQueue(queues, receiver_id));
		
		try {
			Response response = client.send(request);

			if (response instanceof Response_Text) {
				Response_Text msg = (Response_Text) response;
				if (msg.getText().length() != Configuration.TEXT_LENGTH) {
					LOGGER.severe("Length of the responses does not match");
				}
			} else if (response instanceof Response_Empty) {
				LOGGER.warning("The queue was empty");
			} else if (response instanceof Response_WrongRequest) {
				throw new RuntimeException("unkown response type");
			}
		} catch (IOException e) {
		    LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
