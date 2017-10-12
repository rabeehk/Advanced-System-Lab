package experiment.db;

import java.sql.Connection;
import java.util.List;
import java.util.Random;

import db.MessageTask;
import experiment.ExperimentTask;
import experiment.Configuration;

public class DatabasePopRequest implements ExperimentTask {
	private Connection connection;
	private static Random random = new Random();	
	private List<Integer> queue_ids;
	
	public DatabasePopRequest(Connection con, List<Integer> queues) {
		this.connection = con;
		this.queue_ids = queues;
	}

	@Override
	public void execute() {
		int receiver_id = random.nextInt(Configuration.USER_COUNT);
		MessageTask message = MessageTask.popMessage(connection,Configuration.getQueue(queue_ids, receiver_id),receiver_id);		
		if (message != null && message.getMessage().length() <= 0) {
			throw new RuntimeException("Message has invalid length");
		}
	}

}
