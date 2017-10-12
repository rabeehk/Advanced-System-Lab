package experiment.mains;

import static org.junit.Assert.fail;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import db.ConnectionManager;
import db.MessageTask;
import db.QueueTask;
import experiment.ExperimentLog;
import experiment.ExperimentTask;
import experiment.Configuration;
import experiment.RunnableTask;
import experiment.FlowRequestRunner;
import experiment.ExperimentRunnableTask;
import experiment.db.DatabasePeekRequest;
import experiment.db.DatabaseWriteRequest;

public class DatabaseExperiment {
	
	private static RunnableTask getRunnableTask(String name, int id, int fileID, ArrayList<Integer> queue_ids) {
		ExperimentTask task;
		if (name.equals("send")){
			task = new DatabaseWriteRequest(ConnectionManager.getConnection(), queue_ids);
		} else if (name.equals("peek")){
			task = new DatabasePeekRequest(ConnectionManager.getConnection(), queue_ids);
		} else {
			throw new IllegalArgumentException("The operation type is not known");
		}
		
		ExperimentLog responseTimeLog=new ExperimentLog(name+"-"+id+"/"+name+"-"+"expr-id"+"-"+id, "file-"+fileID);
		return new ExperimentRunnableTask(task, responseTimeLog);
	}
	
	
	private static ArrayList<Integer> prepareQueues(){
		// first clear the queues 
		Connection con = ConnectionManager.getConnection();
	    final Random random = new Random();

	    // clean the tables first
		Statement stmt;
		try {	
			stmt = con.createStatement();
	        stmt.execute("DELETE FROM message;");
	        stmt.execute("DELETE FROM queue;");
		}catch (SQLException e) {
			e.printStackTrace();
			fail();
		}	
		
		ArrayList<Integer> queue_ids = new ArrayList<Integer>();
        // now insert into queues 
		for(int i=1; i<=Configuration.QUEUE_COUNT; ++i) {
			int id = QueueTask.createQueue(con, i);
			queue_ids.add(id);
			System.out.println("queue: "+id);		
		}
		// add messages to the queue
		for (final int qid : queue_ids) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					Connection connection = ConnectionManager.getConnection();
					for (int i=0; i < 15000; i++) {
						MessageTask.sendMessage(connection, random.nextInt(Configuration.USER_COUNT), qid, Configuration.getRandomText(200, random));
					}			
				}
			});
			t.start();
		}
		return queue_ids;
	}
	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		
		if (args.length != 4) {
			System.out.println("Usage: experiment_id  experiment_times taskType database_url");
			return;
		}
		
		int experiment_id = Integer.parseInt(args[0]);
		int experiment_count = Integer.parseInt(args[1]);
		ConnectionManager.setURL(args[3]);
		
		ArrayList<Integer> queue_ids = prepareQueues();	
		
		// number of connections to the database is now num_parallel_benchmarks 
		// as each one get a connection to the database
		for (int i=0; i < experiment_count; i++) {
			RunnableTask strat = getRunnableTask(args[2], experiment_id, i, queue_ids);	
			Thread tre = new Thread(new FlowRequestRunner(
							strat, Configuration.WARMUP_TIME, Configuration.EXPERIMENT_TIME, Configuration.COOLDOWN_TIME));
			tre.start();
		}
		
	}
}
