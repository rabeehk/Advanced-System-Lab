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
import experiment.RegularRateRunner;
import experiment.ExperimentRunnableTask;
import experiment.middleware.MiddlewarePeekRequest;
import experiment.middleware.MiddlewarePopRequest;
import experiment.middleware.MiddlewareQueryRequest;
import experiment.middleware.MiddlewareWriteRequest;

public class StabilityExperiment {
	private static ArrayList<Integer> queue_ids;
	
	private static RunnableTask getRunnableTask(String hostName, int portNumber, String name, int experiment_id, int file_id) {
		ExperimentTask op;
		if (name.equals("msend")) {
			op = new MiddlewareWriteRequest(hostName, portNumber, queue_ids);
		} else if (name.equals("mpeek")) {
			op = new MiddlewarePeekRequest(hostName, portNumber);
		} else if (name.equals("mpop")) {
			op = new MiddlewarePopRequest(hostName, portNumber, queue_ids);
		} else if (name.equals("mquery")) {
			op = new MiddlewareQueryRequest(hostName, portNumber);
				} else {
			throw new IllegalArgumentException("Invalid benchmark operation `"+name+"`");
		}
		ExperimentLog responseTimeLog = new ExperimentLog("stability-"+experiment_id+"/"+name+"-"+"expr-id"+"-"+experiment_id, "file-"+file_id);
		return new ExperimentRunnableTask(op, responseTimeLog);
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
			System.out.println("Added queue with id "+id);		
		}
		// add messages to the queue
		for (final int qid : queue_ids) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					Connection connection = ConnectionManager.getConnection();
					for (int i=0; i < 2000; i++) {
						MessageTask.sendMessage(connection, random.nextInt(Configuration.USER_COUNT), qid, Configuration.getRandomText(200, random));
					}			
				}
			});
			t.start();
		}
		return queue_ids;
	}

	
	public static void main(String[] args) throws IOException, InterruptedException {
		if (args.length != 7) {
			System.out.println("Usage: expriment_id  experiment_times host1 portNumber1 host2 portNumber2 database_url");
			return;
		}
		
		int experiment_id = Integer.parseInt(args[0]);
		int experiment_count = Integer.parseInt(args[1]);
		String host1 = args[2];
		int port1 = Integer.parseInt(args[3]);
		String host2 = args[4];
		int port2 = Integer.parseInt(args[5]);
		ConnectionManager.setURL(args[6]);
		Configuration.USER_COUNT=4*experiment_count;
        queue_ids=prepareQueues();
        int rate = 10;
        
		for (int i=0; i < experiment_count; i++) {
			RunnableTask strat = getRunnableTask(host1, port1, "mpop", experiment_id, i);	
			Thread tre = new Thread(
					new RegularRateRunner(
							strat, Configuration.WARMUP_TIME, Configuration.EXPERIMENT_TIME, Configuration.COOLDOWN_TIME, rate));
			tre.start();
		}
		
		for (int i=0; i < experiment_count; i++) {
			RunnableTask strat = getRunnableTask(host2, port2, "mpop", experiment_id,  i+experiment_count);
			Thread tre = new Thread(
					new RegularRateRunner(
							strat, Configuration.WARMUP_TIME, Configuration.EXPERIMENT_TIME, Configuration.COOLDOWN_TIME, rate));
			tre.start();
		}
	
		for (int i=0; i < experiment_count; i++) {
			RunnableTask strat = getRunnableTask(host1, port1, "msend", experiment_id, i);
			Thread tre = new Thread(
					new RegularRateRunner(
							strat, Configuration.WARMUP_TIME, Configuration.EXPERIMENT_TIME, Configuration.COOLDOWN_TIME, rate));
			tre.start();
		}
	
		for (int i=0; i < experiment_count; i++) {
			RunnableTask strat = getRunnableTask(host2, port2, "msend", experiment_id, i+experiment_count);	
			Thread tre = new Thread(
					new RegularRateRunner(
							strat, Configuration.WARMUP_TIME, Configuration.EXPERIMENT_TIME, Configuration.COOLDOWN_TIME, rate));
			tre.start();
		}		
	}
}
