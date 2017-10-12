package experiment.db;

import java.sql.Connection;
import java.util.List;
import java.util.Random;

import db.QueueTask;
import experiment.ExperimentTask;
import experiment.Configuration;

public class DatabaseQueryRequest implements ExperimentTask {

	private static Random random = new Random();
	private Connection connection;
	public DatabaseQueryRequest(Connection connection) {
		this.connection = connection;
	}
	@Override
	public void execute() {
		List<Integer> q = QueueTask.queryQueues(connection, random.nextInt(Configuration.USER_COUNT));	
		if (q == null) {
			throw new RuntimeException("Queue was empty");
		} 
	}
}
