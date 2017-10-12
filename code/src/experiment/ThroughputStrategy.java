package experiment;

import java.util.concurrent.atomic.AtomicInteger;


public class ThroughputStrategy implements RunnableTask {
	private ExperimentTask op;
	private ExperimentLog throughputLog;
	private AtomicInteger number_of_operations;
	
	
	public ThroughputStrategy(ExperimentTask op, ExperimentLog throughputLog) {
		this.op = op;
		this.throughputLog = throughputLog;
		this.number_of_operations = new AtomicInteger(0);
	}
	
	public void incrementNumberOfOperations() {
		this.number_of_operations.incrementAndGet();
	}

	@Override
	public void execute(boolean isRunning) {
		this.op.execute();

		if (isRunning) {
			incrementNumberOfOperations();
			try {
				Thread.sleep(Configuration.THINK_TIME);
			} catch (InterruptedException e) {;
			}
		}
	}

	@Override
	public void regular() {
		this.throughputLog.append(this.number_of_operations+"");
		this.number_of_operations.set(0);
		this.throughputLog.flush();
	}
	
	@Override
	public void terminate() {
		this.throughputLog.terminate();
	}

}
