package experiment.middleware;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import responses.Response_WrongRequest;
import requests.Request_Query;
import responses.Response_Query;
import responses.Response;
import client.Client;
import experiment.ExperimentTask;
import experiment.Configuration;


public class MiddlewareQueryRequest implements ExperimentTask {
	private final static Logger LOGGER = Logger.getLogger(MiddlewareQueryRequest.class.getName());
	private static Random random = new Random();
	private Client client;
	
	public MiddlewareQueryRequest(String host, int port) {
		try {
			this.client = new Client(host, port);
		} catch (IOException e) {
			this.client = null;
		}
	}
	
	@Override
	public void execute() {
		int receiver = random.nextInt(Configuration.USER_COUNT);
		
		try {
			Response response = client.send(new Request_Query(receiver));

			if (response instanceof Response_Query) {
				Response_Query message = (Response_Query) response;
				if (message.getIds().size() == 0) {
					LOGGER.warning("The resulted query_ids were empty");
				}
			} else if (response instanceof Response_WrongRequest) {
				throw new RuntimeException("unkwon response type");
			}
		} catch (IOException e) {
		    LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
