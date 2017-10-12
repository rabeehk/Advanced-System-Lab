package middleware;

import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ClientConnectionWithTime extends ClientConnection{
	public ClientConnectionWithTime(SocketChannel channel, SelectionKey key) {
		super(channel, key);
		this.set_frontEnd_submit_time();
	}
	private long frontEnd_submit_time;
	private long frontEnd_start_time;
	private long backEnd_submit_time;
	private long backEnd_start_time;
	private long response_submit_time;
	private long response_start_time;
	private long response_completed_time;
	
	public void set_frontEnd_submit_time(){
		if(receivedbytes <= 0) 
			this.frontEnd_submit_time=System.nanoTime();
	}
	public void set_frontEnd_start_time(){
		this.frontEnd_start_time=System.nanoTime();
	}
	public void set_backEnd_submit_time(){
		this.backEnd_submit_time=System.nanoTime();
	}
	public void set_backEnd_start_time(){
		this.backEnd_start_time=System.nanoTime();
	}
	public void set_response_submit_time(){
		this.response_submit_time=System.nanoTime();
	}
	public void set_response_start_time(){
		this.response_start_time=System.nanoTime();
	}
	public void set_response_completed_time(){
		this.response_completed_time=System.nanoTime();
	}
	private final static int scale=1000;
	public void printTimes(){
		long frontEnd_waiting_time=(frontEnd_start_time-frontEnd_submit_time)/scale;
		long frontEnd_time=(backEnd_submit_time-frontEnd_start_time)/scale;
		long backEnd_waiting_time=(backEnd_start_time-backEnd_submit_time)/scale;
		long backEnd_time=(response_submit_time-backEnd_start_time)/scale;
		long response_waiting_time=(response_start_time-response_submit_time)/scale;
		long response_time=(response_completed_time-response_start_time)/scale;
		ServerTimeLogs.printTimes(frontEnd_waiting_time, frontEnd_time, backEnd_waiting_time, 
				backEnd_time, response_waiting_time, response_time);
	}
	
}

