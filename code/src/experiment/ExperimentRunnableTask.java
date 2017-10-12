package experiment;


public class ExperimentRunnableTask implements RunnableTask {
	private ExperimentTask op;
	private ExperimentLog responseTimeLog;
		
	
	public ExperimentRunnableTask(ExperimentTask op, ExperimentLog responseTimeLog) {
		this.op = op;
		this.responseTimeLog = responseTimeLog;
	}
	
	@Override
	public void execute(boolean isRunning) {
		long start_time = System.nanoTime();
		this.op.execute();
		if (isRunning) {
			long end_time = System.nanoTime();
			this.responseTimeLog.appendLine(((end_time-start_time)/1000)+"");
			try {
				Thread.sleep(Configuration.THINK_TIME);
			} catch (InterruptedException e) {;
			}
		}
	}

	// here I just add OneBlock to the files to be able to find the duration of 
	// 1 sec for analysis
	@Override
	public void regular() {
		this.responseTimeLog.append("OneBlock");
		this.responseTimeLog.flush();
	}
	
	@Override
	public void terminate() {
		this.responseTimeLog.terminate();
	}

}
