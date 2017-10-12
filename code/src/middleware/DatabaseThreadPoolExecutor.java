package middleware;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DatabaseThreadPoolExecutor extends ThreadPoolExecutor {
	public DatabaseThreadPoolExecutor(int core_workers, int maximum_workers,
	    long alive_time, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
		super(core_workers, maximum_workers, alive_time, unit, workQueue, (ThreadFactory) new DatabaseThreadFactory());
	}
	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		DatabaseConnectionThread database_thread = (DatabaseConnectionThread) t;
		DatabaseRunnable database_runnable = (DatabaseRunnable) r;
		
		database_runnable.con = database_thread.con;
	}
}
