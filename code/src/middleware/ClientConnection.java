package middleware;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientConnection {
	private final static Logger LOGGER = Logger.getLogger(Server.class.getName());

	private SelectionKey key;
	private SocketChannel channel;
	private ByteBuffer input_buffer;
	int receivedbytes;
	private int dataSize;
	private byte[] data;
	private int shift;
	
	public ClientConnection(SocketChannel channel, SelectionKey key) {
		this.channel = channel;
		this.key = key;
		this.input_buffer = ByteBuffer.allocate(2000);
		this.receivedbytes = 0;
		this.dataSize = -1;
		this.data = new byte[2000];
		this.shift = 0;
	}
	
	public SocketChannel getChannel() {
		return this.channel;
	}

	public void write(ByteArrayOutputStream os) throws IOException {
		byte[] array = os.toByteArray();
		ByteBuffer buf = ByteBuffer.allocate(array.length);
		buf.put(array);

		buf.flip();
		while(buf.hasRemaining()) {
		    this.channel.write(buf);
		}
	}

	public ByteBuffer getBuffer() {
		this.input_buffer.rewind();
		return this.input_buffer;
	}
	
	public byte[] read(SocketChannel channel) {
		try {
			int bytesRead = 0;
			while((bytesRead = channel.read(input_buffer)) > 0) {
				this.receivedbytes += bytesRead;
				input_buffer.flip();
				while (input_buffer.hasRemaining()) {
					data[shift] = input_buffer.get();
					shift++;
				}
				input_buffer.flip();
				if (this.dataSize == -1 && this.receivedbytes < 4) {
					continue;
				}
				if (this.dataSize == -1) {
					this.dataSize = ByteBuffer.wrap(data, 0, 4).getInt();
				}
				if (this.receivedbytes >= (this.dataSize+4)) {
					this.receivedbytes = 0;
					input_buffer.rewind();
					return data;
				}
			}
		} catch (IOException e) {
			LOGGER.log(Level.WARNING, "IO exception while reading", e);
		}
		return null;
	}

	public void cancel() {
		this.key.cancel();
	}
	
	public void close() {

	}

	public int getLength() {
		int l = this.dataSize;
		this.dataSize = -1;
		this.shift = 0;
		return 4+l;
	}
	// for measuring the times 
	public void set_frontEnd_submit_time(){};
	public void set_frontEnd_start_time(){};
	public void set_backEnd_submit_time(){};
	public void set_backEnd_start_time(){};
	public void set_response_submit_time(){};
	public void set_response_start_time(){};
	public void set_response_completed_time(){};
	public void printTimes(){};
}
