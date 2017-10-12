package middleware;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import experiment.Configuration;

public class ServerTimeLogs {
	private static ServerTimeLogs instance;
	private static String dirName;
	public static String experimentID;
	private static Timer timer=new Timer();
	public static int serverInstance;
	private boolean inExperiment=false;
	private HashMap<Long, ServerLog> key_to_log;;	
	
	private ServerTimeLogs(){
		dirName="Middleware-"+experimentID;
		key_to_log=new HashMap<Long, ServerLog>();
		final ServerTimeLogs sample=this;
		// warmup phase 
		timer.schedule(new TimerTask(){
			public void run(){
				sample.inExperiment=true;
			}
		}, Configuration.WARMUP_TIME);
		// experiment phase 
		timer.scheduleAtFixedRate(new TimerTask(){
			@Override
			public void run() {
				for(ServerLog log: sample.key_to_log.values()){
					log.frontEnd_waiting_time_log.append("OneBlock");
					log.frontEnd_time_log.append("OneBlock");
					log.backEnd_waiting_time_log.append("OneBlock");
					log.backEnd_time_log.append("OneBlock");
					log.response_waiting_time_log.append("OneBlock");
					log.response_time_log.append("OneBlock");
					
					log.frontEnd_waiting_time_log.flush();
					log.frontEnd_time_log.flush();
					log.backEnd_waiting_time_log.flush();
					log.backEnd_time_log.flush();
					log.response_waiting_time_log.flush();
					log.response_time_log.flush();				
				}
			}
		}, Configuration.WARMUP_TIME+1000, 1000);
		
		//cooldown phase
		timer.schedule(new TimerTask(){
			@Override
			public void run() {
				sample.inExperiment=false;
				for(ServerLog log:sample.key_to_log.values()){
					timer.cancel();
					log.frontEnd_waiting_time_log.terminate();
					log.frontEnd_time_log.terminate();
					log.backEnd_waiting_time_log.terminate();
					log.backEnd_time_log.terminate();
					log.response_waiting_time_log.terminate();
					log.response_time_log.terminate();				
				}
			}
		}, Configuration.WARMUP_TIME+Configuration.EXPERIMENT_TIME);
	}

	private static ServerTimeLogs getInstance(){
		if(instance==null){
			instance=new ServerTimeLogs();
		}
		return instance;
	}
	private static ServerLog getServerLog(Thread thread){
		ServerTimeLogs logInstance=getInstance();
		if(!logInstance.key_to_log.containsKey(thread.getId())){
			synchronized(logInstance){
				logInstance.key_to_log.put(thread.getId(), new ServerLog(dirName, serverInstance));
			}
		}
		return logInstance.key_to_log.get(thread.getId());
	}
	public static void printTimes(long frontEnd_waiting_time,
								  long frontEnd_time,
								  long backEnd_waiting_time,
								  long backEnd_time,
								  long response_waiting_time,
								  long response_time){
		if(getInstance().inExperiment){
			ServerLog log=getServerLog(Thread.currentThread());
			log.printTimes(frontEnd_waiting_time, frontEnd_time, 
					backEnd_waiting_time, backEnd_time, response_waiting_time, response_time);
		}
	}
}
