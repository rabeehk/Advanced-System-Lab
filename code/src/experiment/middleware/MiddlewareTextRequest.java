package experiment.middleware;

import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import requests.Request_Echo;
import responses.Response_Echo;
import responses.Response;
import client.Client;
import experiment.ExperimentTask;
import experiment.Configuration;


public class MiddlewareTextRequest implements ExperimentTask {
	private final static Logger LOGGER = Logger.getLogger(MiddlewareTextRequest.class.getName());
	private static Random random = new Random();
	private Client client;
	public MiddlewareTextRequest(String hName, int pName) {
		try {
			this.client = new Client(hName, pName);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void execute() {
		Request_Echo requset = new Request_Echo(Configuration.getRandomText(Configuration.TEXT_LENGTH, random));
		try {
			Response response = client.send(requset);
			if (response instanceof Response_Echo) {
				Response_Echo message = (Response_Echo) response;
				if (message.getText().length() != Configuration.TEXT_LENGTH) {
					LOGGER.severe("Length of the responses does not match");
				}
			} else {
				throw new RuntimeException("unkown response type");
			}
		} catch (IOException e) {
		    LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
