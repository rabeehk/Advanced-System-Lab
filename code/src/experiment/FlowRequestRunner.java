package experiment;

import java.util.Timer;
import java.util.TimerTask;


public class FlowRequestRunner extends ExperimentRunner {
	private boolean is_warmpup_time;
	private boolean in_cooldown_time;
	
	public FlowRequestRunner(RunnableTask task, long warmup, long duration, long cooldown) {
		super(task, warmup, duration, cooldown);
		this.is_warmpup_time = true;
	}

	@Override
	public void run() {
		final FlowRequestRunner outer = this;
		this.is_warmpup_time = true;
		Timer experimentStateTimer = new Timer();
		experimentStateTimer.schedule(new TimerTask() {
		      @Override
		      public void run() {
		    	  outer.is_warmpup_time = false;
		          this.cancel();
		      }
		}, this.warmup_time);
		
		experimentStateTimer.schedule(new TimerTask() {
		      @Override
		      public void run() {
		    	  outer.isInExperimentTime = false;
		          this.cancel();
		      }
		  }, this.warmup_time+this.experiment_time);
		
		experimentStateTimer.schedule(new TimerTask() {
		      @Override
		      public void run() {
		    	  outer.in_cooldown_time = false;
		    	  this.cancel();
		      }
		  }, this.warmup_time+this.experiment_time+this.cooldown_time);
		
		TimerTask writeOneBlockTask = new TimerTask() {
		      @Override
		      public void run() {
		    	  outer.strategy.regular();
		      }
		  };
		experimentStateTimer.scheduleAtFixedRate(writeOneBlockTask, this.warmup_time+1000, 1000);
		// if it is in warm up then execute the operation
		while (this.is_warmpup_time) {
			this.execute();
		}
		// if it is in the experiment time then execute the code 
		this.isInExperimentTime = true;
		while (this.isInExperimentTime) {
			this.execute();
		}
		// if it is in cool down execute the code but no more write to the file
		writeOneBlockTask.cancel();
		this.in_cooldown_time = true;
		while (this.in_cooldown_time) {
			this.execute();
		}
		this.strategy.terminate();
        experimentStateTimer.cancel();
	}

}