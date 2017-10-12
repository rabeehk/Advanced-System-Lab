package middleware;

import java.sql.Connection;

import db.ConnectionManager;

public class DatabaseConnectionThread extends Thread {
	Connection con;
	public DatabaseConnectionThread(Runnable r) {
		super(r);
		con = ConnectionManager.getConnection();
	}
	public Connection getConnection() {
		return con;
	}
}
