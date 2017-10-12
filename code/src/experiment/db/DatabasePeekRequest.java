package experiment.db;

import java.sql.Connection;
import java.util.List;
import java.util.Random;

import db.MessageTask;
import experiment.ExperimentTask;
import experiment.Configuration;

public class DatabasePeekRequest implements ExperimentTask {

	private static Random random = new Random();
	private Connection connection;
	private List<Integer> queue_ids;
	
	public DatabasePeekRequest(Connection connection, List<Integer> queue_ids) {
		this.connection = connection;
		this.queue_ids = queue_ids;
	}

	@Override
	public void execute() {
		int receiver = random.nextInt(Configuration.USER_COUNT);
		MessageTask msg = MessageTask.getTopMessage(connection, Configuration.getQueue(queue_ids, receiver), receiver);
		if (msg != null && msg.getMessage().length() <= 0) {
			throw new RuntimeException("Message has invalid length");
		}
	}
}
