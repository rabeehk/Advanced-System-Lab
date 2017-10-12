package client;

import java.io.IOException;
import java.util.Random;

import requests.Request_CreateNewQueue;
import responses.Response_CreateNewQueue;
import responses.Response;
import requests.Request_WriteMessage;
import responses.Response_WriteMessage;
import experiment.Configuration;

public class ClientMain {
	private static Random random = new Random();	

	public static void main(String[] args) throws IOException {
		Client client = new Client("localhost", 5555);
		Response resp = client.send(new Request_CreateNewQueue());
		int queue_id = ((Response_CreateNewQueue) resp).getQueueID();
		String text = Configuration.getRandomText(100, random);
		
		long start_time = System.currentTimeMillis();
		for (int i=0; i < 50; i++) {
			Response_WriteMessage sr = (Response_WriteMessage) client.send(new Request_WriteMessage(1, 1, queue_id, text));
			if (!sr.isValid()) {
				throw new RuntimeException("send was unsuccessful");
			}
		}
		long end_time = System.currentTimeMillis();
		System.out.println("sending 50 SendRequests took  "+(end_time-start_time) +" ms");		
	}
}
