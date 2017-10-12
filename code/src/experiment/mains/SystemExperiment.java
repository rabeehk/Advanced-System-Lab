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
import experiment.middleware.MiddlewarePeekRequest;
import experiment.middleware.MiddlewarePopRequest;
import experiment.middleware.MiddlewareQueryRequest;
import experiment.middleware.MiddlewareWriteRequest;

public class SystemExperiment {
	
	private static ArrayList<Integer> prepareQueues(){
	    final Random random = new Random();
		 
		// first clear the queues 
		Connection con = ConnectionManager.getConnection();
	    
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
			System.out.println("Queue added with id "+id);		
		}
		// add messages to the queue
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

	private static RunnableTask getStrategy(String hostName, int portNumber, String name, String experiment_id, int fileID, ArrayList<Integer>queue_ids) {
		ExperimentTask task;
		if (name.equals("msend")) {
			task = new MiddlewareWriteRequest(hostName, portNumber, queue_ids);
		} else if (name.equals("mpeek")) {
			task = new MiddlewarePeekRequest(hostName, portNumber);
		} else if (name.equals("mpop")) {
			task = new MiddlewarePopRequest(hostName, portNumber, queue_ids);
		} else if (name.equals("mquery")) {
			task = new MiddlewareQueryRequest(hostName, portNumber);
				} else {
			throw new IllegalArgumentException("The operation type is unkown");
		}
		ExperimentLog log = new ExperimentLog("scalability-"+experiment_id+"/"+name+"-"+"expr-id"+"-"+experiment_id, "file-"+fileID);
		return new ExperimentRunnableTask(task, log);
	}
	
	public static void main(String[] args) throws IOException, InterruptedException {
		System.out.println(args.length);
		if (args.length != 9) {
			System.out.println("Usage: experiment_id  experiment_count host1 port1 host2 port2 database_url");
			return;
		}

		String experiment_id = args[0];
		int experiment_count = Integer.parseInt(args[1]);
		String host1 = args[2];
		int port1 = Integer.parseInt(args[3]);
		String host2 = args[4];
		int port2 = Integer.parseInt(args[5]);
		String host3 = args[6];
		int port3 = Integer.parseInt(args[7]);
		
		//Configuration.TEXT_LENGTH = Integer.parseInt(args[7]);
		ConnectionManager.setURL(args[8]);
		
		Configuration.USER_COUNT = 12*experiment_count;
		
		System.out.println("Experiment started");
		
		ArrayList<Integer> queue_ids=prepareQueues();
		
		for (int i=0; i < experiment_count; i++) {
			RunnableTask strat = getStrategy(host1, port1, "mpop", experiment_id,i, queue_ids);
			
			Thread tre = new Thread(
					new FlowRequestRunner(
							strat, Configuration.WARMUP_TIME, Configuration.EXPERIMENT_TIME, Configuration.COOLDOWN_TIME));
			tre.start();
		}
		
		for (int i=0; i < experiment_count; i++) {
			RunnableTask strat = getStrategy(host2, port2, "mpop", experiment_id, i+experiment_count, queue_ids);
			Thread tre = new Thread(
					new FlowRequestRunner(
							strat, Configuration.WARMUP_TIME, Configuration.EXPERIMENT_TIME, Configuration.COOLDOWN_TIME));
			tre.start();
		}
		
		for (int i=0; i < experiment_count; i++) {
			RunnableTask strat = getStrategy(host3, port3, "mpop", experiment_id,i+2*experiment_count, queue_ids);
			Thread tre = new Thread(
					new FlowRequestRunner(
							strat, Configuration.WARMUP_TIME, Configuration.EXPERIMENT_TIME, Configuration.COOLDOWN_TIME));
			tre.start();
		}
		
		for (int i=0; i < experiment_count; i++) {
			RunnableTask strat = getStrategy(host1, port1, "mpeek", experiment_id,  i,  queue_ids);
			Thread tre = new Thread(
					new FlowRequestRunner(
							strat, Configuration.WARMUP_TIME, Configuration.EXPERIMENT_TIME, Configuration.COOLDOWN_TIME));
			tre.start();
		}
		for (int i=0; i < experiment_count; i++) {
			RunnableTask strat = getStrategy(host2, port2, "mpeek", experiment_id,  i+experiment_count,  queue_ids);
			Thread tre = new Thread(
					new FlowRequestRunner(
							strat, Configuration.WARMUP_TIME, Configuration.EXPERIMENT_TIME, Configuration.COOLDOWN_TIME));
			tre.start();
		}

		for (int i=0; i < experiment_count; i++) {
			RunnableTask strat = getStrategy(host3, port3, "mpeek", experiment_id, i+2*experiment_count,  queue_ids);
			
			Thread tre = new Thread(
					new FlowRequestRunner(
							strat, Configuration.WARMUP_TIME, Configuration.EXPERIMENT_TIME, Configuration.COOLDOWN_TIME));
			tre.start();
		}

		
		for (int i=0; i < experiment_count; i++) {
			RunnableTask strat = getStrategy(host1, port1, "msend", experiment_id, i,  queue_ids);
			
			Thread tre = new Thread(
					new FlowRequestRunner(
							strat, Configuration.WARMUP_TIME, Configuration.EXPERIMENT_TIME, Configuration.COOLDOWN_TIME));
			tre.start();
		}
		
		for (int i=0; i < experiment_count; i++) {
			RunnableTask strat = getStrategy(host2, port2, "msend", experiment_id,  i+experiment_count,  queue_ids);
			
			Thread tre = new Thread(
					new FlowRequestRunner(
							strat, Configuration.WARMUP_TIME, Configuration.EXPERIMENT_TIME, Configuration.COOLDOWN_TIME));
			tre.start();
		}
		for (int i=0; i < experiment_count; i++) {
			RunnableTask strat = getStrategy(host3, port3, "msend", experiment_id, i+2*experiment_count,  queue_ids);
			
			Thread tre = new Thread(
					new FlowRequestRunner(
							strat, Configuration.WARMUP_TIME, Configuration.EXPERIMENT_TIME, Configuration.COOLDOWN_TIME));
			tre.start();
		}
		
		
		for (int i=0; i < experiment_count; i++) {
			RunnableTask strat = getStrategy(host1, port1, "mquery", experiment_id, i,  queue_ids);
			
			Thread tre = new Thread(
					new FlowRequestRunner(
							strat, Configuration.WARMUP_TIME, Configuration.EXPERIMENT_TIME, Configuration.COOLDOWN_TIME));
			tre.start();
		}
		for (int i=0; i < experiment_count; i++) {
			RunnableTask strat = getStrategy(host2, port2, "mquery", experiment_id, i+experiment_count,  queue_ids);
			
			Thread tre = new Thread(
					new FlowRequestRunner(
							strat, Configuration.WARMUP_TIME, Configuration.EXPERIMENT_TIME, Configuration.COOLDOWN_TIME));
			tre.start();
		}
		
		for (int i=0; i < experiment_count; i++) {
			RunnableTask strat = getStrategy(host3, port3, "mquery", experiment_id, i+2*experiment_count,  queue_ids);
			
			Thread tre = new Thread(
					new FlowRequestRunner(
							strat, Configuration.WARMUP_TIME, Configuration.EXPERIMENT_TIME, Configuration.COOLDOWN_TIME));
			tre.start();
		}
	}
}
