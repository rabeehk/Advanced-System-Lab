package db.tests;

import static org.junit.Assert.*;
import java.util.ArrayList;
import org.junit.After;
import org.junit.Before;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import db.ConnectionManager;
import db.MessageTask;
import db.QueueTask;

public class DatabaseTest {

	private Connection connection;
	private int queue_id;
	
    @Before
    public void setUp() {
        connection = ConnectionManager.getConnection();
		queue_id = QueueTask.createQueue(connection, 7);
		ConnectionManager.setURL("jdbc:postgresql://localhost:5432/db?user=rabeehk&password=AllahoSamad@))%");
    }
    
    @After
    public void tearDown() {
        Statement stmt;
		try {
			stmt = connection.createStatement();
	        stmt.execute("DELETE FROM message;");
	        stmt.execute("DELETE FROM queue;");
		} catch (SQLException e) {
			fail();
		}
    }
	
	@org.junit.Test
	public void testPopMessage() {		
		assertEquals(null, MessageTask.popMessage(connection, queue_id, 81));
	}

	@org.junit.Test
	public void testGetTopMessage() {
		String text = "Hey Rabeeh :)";
		int sender_id = 5;
		assertTrue(MessageTask.sendMessage(connection, sender_id, queue_id, text));
		
		MessageTask message = MessageTask.getTopMessage(connection, queue_id, 90);
		assertEquals(sender_id, message.getSenderID());
		assertEquals(text, message.getMessage());		
		message = MessageTask.getTopMessage(connection, queue_id, 1);
		assertEquals(sender_id, message.getSenderID());
	}
	
	
	@org.junit.Test
	public void testSendMessageWithReceiver() {
		String text = "Rabeeh again:)";
		int sender_id = 9;
		int receiver_id = 99;
		assertTrue(MessageTask.sendMessage(connection, sender_id, receiver_id, queue_id, text));
		
		MessageTask msg = MessageTask.popMessage(connection, queue_id, receiver_id);
		assertEquals(sender_id, msg.getSenderID());
		assertEquals(text, msg.getMessage());
	}
	
	
	@org.junit.Test
	public void testDeleteQueue() {
		int queue = QueueTask.createQueue(connection, 10);
		assertTrue(QueueTask.deleteQueue(connection, queue));
	}
	
	@org.junit.Test
	public void testQueryEmpty() {
		ArrayList<Integer> res = QueueTask.queryQueues(connection, 90);
		assertEquals(0, res.size());
	}	
}