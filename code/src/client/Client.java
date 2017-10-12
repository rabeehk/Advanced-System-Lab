package client;


import responses.Response_CreateNewQueue;
import responses.Response_EraseQueue;
import responses.Response_Echo;
import responses.Response_Empty;
import responses.Response_WrongRequest;
import responses.Response_Text;
import responses.Response_Query;
import requests.Request;
import responses.Response;
import responses.Response_WriteMessage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;


public class Client {
	// socket, input and output stream for client 
	private Socket socket;
	DataInputStream in;
	DataOutputStream out;
	
	// create a new client
	public Client(String hostName, int portNumber) throws IOException {
		this(InetAddress.getByName(hostName),portNumber);
	}
	public Client(InetAddress inetAddress, int portNumber) throws IOException {
		this(new Socket(inetAddress, portNumber));
	}
	public Client(Socket socket) throws IOException {
		this.socket = socket;
		this.in = new DataInputStream(new BufferedInputStream(this.socket.getInputStream()));
		this.out = new DataOutputStream(new BufferedOutputStream(this.socket.getOutputStream()));
	}
		
	public Response send(Request req) throws IOException {
		byte[] data = req.getPayLoad();
		this.out.writeInt(data.length);
		this.out.write(data);
		this.out.flush();
		
		byte type = this.in.readByte();
		if (type == Response.DELETED_QUEUE){
			return new Response_EraseQueue(this.in.readBoolean());
		}else if (type == Response.SEND_VALID) {
			return new Response_WriteMessage(true);
		} else if (type == Response.SEND_NOT_VALID) {
			return new Response_WriteMessage(false);
		}else if (type == Response.EMPTY){
			return new Response_Empty();
		} else if (type == Response.TEXT){
			return new Response_Text(this.in.readUTF());
		} else if (type == Response.CREATED_QUEUE){
			return new Response_CreateNewQueue(this.in.readInt());
		} else if (type == Response.QUERY){
			ArrayList<Integer> queue_ids = new ArrayList<Integer>();
			int length = this.in.readInt();
			for (int i=0; i < length; i++) {
				queue_ids.add(this.in.readInt());
			}
			return new Response_Query(queue_ids);
		} else if (type == Response.ECHO_RESPONSE) {
			return new Response_Echo(this.in.readUTF());
		} else {
			return new Response_WrongRequest();
		}
	}
	
	// terminate the client: close the socket and input, and outputStreams
	public void terminate() {
		try {
			this.in.close();
			this.out.close();
			if (this.socket != null) {
				this.socket.close();
			}

		} catch (IOException e) {
		}
	}
}
