package experiment;

import java.sql.Date;
import java.util.Timer;
import java.util.TimerTask;


public class RegularRateRunner extends ExperimentRunner {
	private Date start_date;
	public RegularRateRunner(RunnableTask strat, long warmup, long duration,
			long cooldown, int requestRate) {
		super(strat, warmup, duration, cooldown);
		this.requestRate = requestRate;
	}
	public Date getNextRequestDate() {
		return new Date(this.start_date.getTime() + (this.count.get() * (1000 / this.requestRate)));
	}
	@Override
	public void run() {
		final RegularRateRunner outer = this;
		final Timer experimentStateTimer = new Timer();
		final Timer requestTimer = new Timer();
		experimentStateTimer.schedule(new TimerTask() {
		      @Override
		      public void run() {
		    	  outer.isInExperimentTime = true;
		          this.cancel();
		      }
		  }, this.warmup_time);
		
		experimentStateTimer.schedule(new TimerTask() {
		      @Override
		      public void run() {
		    	  outer.isInExperimentTime = false;
		          this.cancel();
		          //outer.strategy.done();
		      }
		  }, this.warmup_time+this.experiment_time);
		experimentStateTimer.schedule(new TimerTask() {
		      @Override
		      public void run() {
		          this.cancel();
		          requestTimer.cancel();
		          experimentStateTimer.cancel();
		      }
		  }, this.warmup_time+this.experiment_time+this.cooldown_time);
		experimentStateTimer.scheduleAtFixedRate(new TimerTask() {
		      @Override
		      public void run() {
		    	  outer.strategy.regular();
		      }
		  }, this.warmup_time+1000, 1000); 
		
		start_date = new Date(System.currentTimeMillis());
		requestTimer.schedule(new RequestTimerTask(this, requestTimer, experimentStateTimer, Thread.currentThread()), this.getNextRequestDate());
		try {
			Thread.sleep(this.warmup_time+this.cooldown_time+this.experiment_time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		outer.strategy.terminate();
	}
}
