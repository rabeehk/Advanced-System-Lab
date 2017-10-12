package middleware;

import experiment.ExperimentLog;

public class ServerLog {
	ExperimentLog frontEnd_waiting_time_log;
	ExperimentLog frontEnd_time_log;
	ExperimentLog backEnd_waiting_time_log;
	ExperimentLog backEnd_time_log;
	ExperimentLog response_waiting_time_log;
	ExperimentLog response_time_log;
	
	ServerLog(String dirName, int instance){
		frontEnd_waiting_time_log=new ExperimentLog(dirName+"/frontEnd_waiting_time",
				"instance-"+instance+"-ID-"+Thread.currentThread().getId());
		frontEnd_time_log=new ExperimentLog(dirName+"/frontEnd_time", 
				"instance-"+instance+"-ID-"+Thread.currentThread().getId());
		backEnd_waiting_time_log=new ExperimentLog(dirName+"/backEnd_waiting_time", 
				"instance-"+instance+"-ID-"+Thread.currentThread().getId());
		backEnd_time_log=new ExperimentLog(dirName+"/backEnd_time", 
				"instance-"+instance+"-ID-"+Thread.currentThread().getId());
		response_waiting_time_log=new ExperimentLog(dirName+"/response_waiting_time", 
				"instance-"+instance+"-ID-"+Thread.currentThread().getId());
		response_time_log=new ExperimentLog(dirName+"/response_time", 
				"instance-"+instance+"-ID-"+Thread.currentThread().getId());
	}
	
	public void printTimes(long frontEnd_waiting_time,
						   long frontEnd_time, long backEnd_waiting_time,
						   long backEnd_time,  long response_waiting_time,
	                       long response_time){
		frontEnd_waiting_time_log.appendLine(frontEnd_waiting_time+"");
		frontEnd_time_log.appendLine(frontEnd_time+"");
		backEnd_waiting_time_log.appendLine(backEnd_waiting_time+"");
		backEnd_time_log.appendLine(backEnd_time+"");
		response_waiting_time_log.appendLine(response_waiting_time+"");
		response_time_log.appendLine(response_time+"");
	}
}
