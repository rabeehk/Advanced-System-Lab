package experiment;

import java.util.concurrent.atomic.AtomicInteger;


abstract public class ExperimentRunner implements Runnable {
	protected long warmup_time;
	protected long cooldown_time;
	protected long experiment_time;
	protected boolean isInExperimentTime;
	protected int requestRate;
	protected AtomicInteger count;

	protected RunnableTask strategy;
	
	public ExperimentRunner(RunnableTask strat, long warmup, long duration, long cooldown) {
		this.strategy = strat;
		this.warmup_time = warmup;
		this.cooldown_time = cooldown;
		this.experiment_time = duration;		
		this.isInExperimentTime = false;
		this.count = new AtomicInteger(0);
	}

	public void incrementRequests() {
		this.count.incrementAndGet();
	}

	public void execute() {
		this.strategy.execute(this.isInExperimentTime);
	}
	
	public boolean inRunning() {
		return this.isInExperimentTime;
	}
	
	
}
