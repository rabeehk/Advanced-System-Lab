package experiment.db;

import java.sql.Connection;
import java.util.List;
import java.util.Random;
import db.MessageTask;
import experiment.ExperimentTask;
import experiment.Configuration;

public class DatabaseWriteRequest implements ExperimentTask {
	
	private static Random random = new Random();	
	private Connection connection;
	private List<Integer> queues;
	public DatabaseWriteRequest(Connection con, List<Integer> queues) {
		this.connection = con;
		this.queues = queues;
	}
	
	@Override
	public void execute() {
		int sender_id = random.nextInt(Configuration.USER_COUNT);
		boolean res;
		if (random.nextFloat() < 0.6) {
			res = MessageTask.sendMessage(connection, sender_id, Configuration.getQueue(queues, sender_id),
				  Configuration.getRandomText(Configuration.TEXT_LENGTH, random));
		} else {
			res = MessageTask.sendMessage(connection, sender_id, Configuration.getReceiver(sender_id),
					Configuration.getQueue(queues, sender_id),
					Configuration.getRandomText(Configuration.TEXT_LENGTH, random));
		}
		if (!res) {
			throw new RuntimeException("Database write task failed");
		}
	}
}
