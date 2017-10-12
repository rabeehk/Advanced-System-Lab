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
import experiment.db.DatabasePopRequest;
import experiment.db.DatabaseWriteRequest;


public class DatabaseCombinationExperiment {

		private static RunnableTask getRunnableTask(String type, int id, int number, ArrayList<Integer> queue_ids) {
			ExperimentTask task;
			if (type.equals("send")){
				task = new DatabaseWriteRequest(ConnectionManager.getConnection(), queue_ids);
			} else if (type.equals("peek")){
				task = new DatabasePeekRequest(ConnectionManager.getConnection(), queue_ids);
			} else if (type.equals("pop")){
				task = new DatabasePopRequest(ConnectionManager.getConnection(), queue_ids);
			}  else {
				throw new IllegalArgumentException("The name was not valid experiment type");
			}
			ExperimentLog log=new ExperimentLog("combination-"+id+"/"+type+"-"+"expr-id"+"-"+id, "file-"+number);
			return new ExperimentRunnableTask(task, log);
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
						Connection con = ConnectionManager.getConnection();
						for (int i=0; i < 1000; i++) {
							MessageTask.sendMessage(con, random.nextInt(100), qid, Configuration.getRandomText(200, random));
						}			
					}
				});
				t.start();
			}
			return queue_ids;
		}
		
		public static void main(String[] args) throws IOException, InterruptedException, SQLException {
			
			int experiment_id = Integer.parseInt(args[0]);
			int experiment_count = Integer.parseInt(args[1]);

			System.out.println("Experiment "+experiment_id+ " with count "+experiment_count);
			ConnectionManager.setURL(args[2]);
			
			// prepare the database 
			ArrayList<Integer> queue_ids = prepareQueues();		
			
			for (int i=0; i < experiment_count; i++) {
				RunnableTask task = getRunnableTask("send", experiment_id,  i, queue_ids);
				Thread w_thread = new Thread( new FlowRequestRunner(
								task, Configuration.WARMUP_TIME, Configuration.EXPERIMENT_TIME, Configuration.COOLDOWN_TIME));
				w_thread.start();
			}

			for (int i=0; i < experiment_count; i++) {
				RunnableTask task = getRunnableTask("pop", experiment_id, i, queue_ids);
				Thread w_thread = new Thread( new FlowRequestRunner(
								task, Configuration.WARMUP_TIME, Configuration.EXPERIMENT_TIME, Configuration.COOLDOWN_TIME));
				w_thread.start();
			}
			
			for (int i=0; i < (2*experiment_count); i++) {
				RunnableTask task = getRunnableTask("peek", experiment_id,i, queue_ids);
				Thread w_thread = new Thread(new FlowRequestRunner(
								task, Configuration.WARMUP_TIME, Configuration.EXPERIMENT_TIME, Configuration.COOLDOWN_TIME));
				w_thread.start();
			}
		}
	}
