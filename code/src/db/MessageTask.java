package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class MessageTask {
	private final static Logger LOGGER = Logger.getLogger(MessageTask.class.getName());
	
	private int id;
	private int sender_id;
	private String message;
	
	private static String PEEK_FROM_SENDER = "SELECT * FROM peek_message_from_sender(?, ?)";
	private static String WRITE = "SELECT * FROM send_message(?, ?, ?)";
	private static String WRITE_WITH_RECEIVER = "SELECT * FROM send_message(?, ?, ?, ?)";
	private static String POP = "SELECT * FROM pop_message(?, ?)";
	private static String PEEK = "SELECT * FROM peek_message(?, ?)";
	
	public MessageTask(int id, int sender, String text) {
		this.id = id;
		this.sender_id = sender;
		this.message = text;
	}
	
	public int getId() {
		return this.id;
	}
	
	public int getSenderID() {
		return sender_id;
	}

	public String getMessage() {
		return message;
	}
	
	public String toString() {
		return "This is a message "+message+ " with id: "+id+ " and sender: "+sender_id;
	}
	
	
	
	public static MessageTask getTopMessage(Connection connection, int queue_id, int receiver_id) {
		try {
			PreparedStatement popStmt = connection.prepareStatement(PEEK);
			popStmt.setInt(1, queue_id);
			popStmt.setInt(2, receiver_id);
			ResultSet res = popStmt.executeQuery();
			if (res.next()) {
				return new MessageTask(res.getInt(1), res.getInt(2), res.getString(3));
			} else {
				return null;
			}
		} catch (SQLException e) {
			LOGGER.severe(e.getMessage());
		}
		return null;
	}
	
	public static MessageTask popMessage(Connection connection, int queue_id, int receiver_id) {
		try {
			PreparedStatement popStmt = connection.prepareStatement(POP);
			popStmt.setInt(1, queue_id);
			popStmt.setInt(2, receiver_id);
			ResultSet res = popStmt.executeQuery();
			if (res.next()) {
				return new MessageTask(res.getInt(1), res.getInt(2), res.getString(3));
			} else {
				return null;
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	public static MessageTask peekMessageFromSender(Connection con, int sender, int receiver) {
		try {
			PreparedStatement popStmt = con.prepareStatement(PEEK_FROM_SENDER);
			popStmt.setInt(1, sender);
			popStmt.setInt(2, receiver);
			ResultSet res = popStmt.executeQuery();
			
			if (res.next()) {
				return new MessageTask(res.getInt(1), res.getInt(2), res.getString(3));
			} else {
				return null;
			}
		} catch (SQLException e) {
			LOGGER.severe(e.getMessage());
		}
		return null;
	}
	
	public static boolean sendMessage(Connection con, int sender_id, int queue_id, String message) {
		try {
			PreparedStatement stmt = con.prepareStatement(WRITE);
			stmt.setInt(1, sender_id);
			stmt.setInt(2, queue_id);
			stmt.setString(3, message);
			stmt.execute();
			return true;
		} catch (SQLException e) {
			LOGGER.warning("Failed to send message"+e.getMessage());
			return false;
		}
	}
	
	public static boolean sendMessage(Connection con, int sender, int receiver, int queue, String text) {
		try {
			PreparedStatement stmt = con.prepareStatement(WRITE_WITH_RECEIVER);
			stmt.setInt(1, sender);
			stmt.setInt(2, receiver);
			stmt.setInt(3, queue);
			stmt.setString(4, text);
			stmt.execute();
			return true;
		} catch (SQLException e) {
			LOGGER.warning("error occured in the sendMessage"+e.getMessage());
			return false;
		}
	}

}