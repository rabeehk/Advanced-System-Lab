package db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Logger;

public class QueueTask {
	private final static Logger LOGGER = Logger.getLogger(QueueTask.class
			.getName());
	
	public static boolean deleteQueue(Connection con, int queue_id) {
		try {
			Statement stmt = con.createStatement();
			stmt.execute("DELETE FROM queue WHERE id = "+queue_id);
			return true;
		} catch (SQLException e) {
		}
		return false;
	}
	
	public static int createQueue(Connection con, int queue_maker_id) {
		try {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery(
				"INSERT INTO queue(creator_id) VALUES ("+queue_maker_id+") RETURNING id"
			);
			if (res.next()) {
				return res.getInt(1);
			}
		} catch (SQLException e) {	
			
		}
		// it not successful queue_id -1 is returned 
		return -1;
	}
	

	public static ArrayList<Integer> queryQueues(Connection con, int receiver_id) {
		ArrayList<Integer> queue_ids = new ArrayList<Integer>();
		try {
			Statement stmt = con.createStatement();
			ResultSet res = stmt.executeQuery(
				"SELECT * FROM query_queues("+receiver_id+")"
			);
			while (res.next()) {
				queue_ids.add(res.getInt(1));
			}
		} catch (SQLException e) {
			
		}
		return queue_ids;
	}
}