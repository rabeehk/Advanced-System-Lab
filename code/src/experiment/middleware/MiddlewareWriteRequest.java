package experiment.middleware;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import requests.Request_WriteMessage;
import responses.Response_WriteMessage;
import client.Client;
import experiment.ExperimentTask;
import experiment.Configuration;


public class MiddlewareWriteRequest implements ExperimentTask {
	private final static Logger LOGGER = Logger.getLogger(MiddlewareWriteRequest.class.getName());
	private static Random random = new Random();
	private Client client;
	private List<Integer> queues;
	public MiddlewareWriteRequest(String hName, int pName, List<Integer> q_ids) {
		this.queues = q_ids;
		try {
			this.client = new Client(hName, pName);
		} catch (IOException e) {
			this.client = null;
		}
	}
	
	@Override
	public void execute() {
		int sender = random.nextInt(Configuration.USER_COUNT);
		Request_WriteMessage requset;
		if (random.nextFloat() < 0.6) {
			requset = new Request_WriteMessage(sender, -1, Configuration.getQueue(queues, sender), Configuration.getRandomText(Configuration.TEXT_LENGTH, random));
		} else {
			requset = new Request_WriteMessage(sender, Configuration.getReceiver(sender), Configuration.getQueue(queues, sender), Configuration.getRandomText(Configuration.TEXT_LENGTH, random));
		}
		try {
			Response_WriteMessage sendresponse = (Response_WriteMessage) client.send(requset);
			if (!sendresponse.isValid()) {
				LOGGER.warning("response was wrong");
			}
		} catch (IOException e) {
			LOGGER.warning("file system exception occured");
		}
	}
}
