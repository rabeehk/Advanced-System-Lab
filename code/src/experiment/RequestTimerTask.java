package experiment;

import java.util.Timer;
import java.util.TimerTask;

public class RequestTimerTask extends TimerTask {

	RegularRateRunner runner;
	Timer requestTimer;
	Timer stateChangeTimer;
	Thread mainThread;

	
	public RequestTimerTask(RegularRateRunner bench, Timer requestTimer, Timer stateChangeTimer, Thread t) {
		this.runner = bench;
		this.requestTimer = requestTimer;
		this.stateChangeTimer = stateChangeTimer;
		this.mainThread = t;
	}
	
    @Override
    public void run() {
    	try {
    		this.runner.incrementRequests();
    		this.runner.execute();
        	try {
        		this.requestTimer.schedule(new RequestTimerTask(this.runner, this.requestTimer, this.stateChangeTimer, this.mainThread), 
        			this.runner.getNextRequestDate());
        	} catch(IllegalStateException e) {}
    	} catch(RuntimeException e) {
    		this.stateChangeTimer.cancel();
    		this.requestTimer.cancel();
    		this.mainThread.interrupt();
    	}

    }
}
