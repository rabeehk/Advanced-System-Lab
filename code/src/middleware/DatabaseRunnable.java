package middleware;

import java.sql.Connection;

public abstract class DatabaseRunnable implements Runnable {
	protected Connection con;
	@Override
	public abstract void run();
}
