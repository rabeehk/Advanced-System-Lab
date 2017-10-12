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
import experiment.middleware.MiddlewarePeekRequest;
import experiment.middleware.MiddlewarePopRequest;
import experiment.middleware.MiddlewareQueryRequest;
import experiment.middleware.MiddlewareTextRequest;
import experiment.middleware.MiddlewareWriteRequest;
import experiment.ExperimentLog;
import experiment.ExperimentRunnableTask;
import experiment.ExperimentTask;
import experiment.Configuration;
import experiment.RunnableTask;
import experiment.FlowRequestRunner;

public class MiddlewareExperiment {
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
				System.out.println("queue "+id);		
			}
			// add messages to the queue
			for (final int qid : queue_ids) {
				Thread t = new Thread(new Runnable() {
					@Override
					public void run() {
						Connection con = ConnectionManager.getConnection();

						for (int i=0; i < 2000; i++) {
							MessageTask.sendMessage(con, random.nextInt(Configuration.USER_COUNT), qid, Configuration.getRandomText(200, random));
						}			
					}
				});
				t.start();
			}
			return queue_ids;
		}

		private static RunnableTask getRunnableTask(String hostName, int portName, String name, int nameID, int experiment_id, ArrayList<Integer> queue_ids) {		
			ExperimentTask task;
			if (name.equals("msend")) {
				task = new MiddlewareWriteRequest(hostName, portName, queue_ids);
			} else if (name.equals("mpeek")) {
				task = new MiddlewarePeekRequest(hostName, portName);
			} else if (name.equals("mpop")) {
				task = new MiddlewarePopRequest(hostName, portName, queue_ids);
			} else if (name.equals("mquery")) {
				task = new MiddlewareQueryRequest(hostName, portName);
			} else if (name.equals("mecho")) {
				task = new MiddlewareTextRequest(hostName, portName);
			} else {
				throw new IllegalArgumentException("Invalid benchmark operation `"+name+"`");
			}
			ExperimentLog log =new ExperimentLog(name+"-"+nameID+"/"+name+"-"+"response-time"+"-"+nameID, "file-"+experiment_id);
			return new ExperimentRunnableTask(task, log);
		}
		
		public static void main(String[] args) throws IOException, InterruptedException {
			
			if (args.length != 6) {
				System.out.println("Usage: experiment_id experiment_count task host port database_url");
				return;
			}
	        
			int benchmark_id = Integer.parseInt(args[0]);
			Configuration.USER_COUNT=benchmark_id;
			int experiment_count = Integer.parseInt(args[1]);
			String host = args[3];
			int port = Integer.parseInt(args[4]);
			ConnectionManager.setURL(args[5]);
			ArrayList<Integer> queues = prepareQueues();
			
			System.out.println("Executing "+experiment_count+ " experiments with id "+ benchmark_id+ " on host "+host+" and port "+port);
			
			for (int i=0; i < experiment_count; i++) {
				RunnableTask strat = getRunnableTask(host, port, args[2], benchmark_id, i, queues);
				Thread tre = new Thread(new FlowRequestRunner(strat, Configuration.WARMUP_TIME, Configuration.EXPERIMENT_TIME, Configuration.COOLDOWN_TIME));
				tre.start();
			}
		}
	}
