package middleware;

import java.util.concurrent.ThreadFactory;

public class DatabaseThreadFactory implements ThreadFactory {
	public Thread newThread(Runnable r) {
		return new DatabaseConnectionThread(r);  
	}
}
