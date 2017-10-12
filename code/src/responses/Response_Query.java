package responses;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

public class Response_Query implements Response {
	private List<Integer> queue_ids;
	public Response_Query(List<Integer> queue_ids) {
		this.queue_ids = queue_ids;
	}
	public List<Integer> getIds() {
		return this.queue_ids;
	}
	@Override
	public void send(DataOutputStream s) throws IOException {
		s.writeByte(Response.QUERY);
		s.writeInt(this.queue_ids.size());
		for (int i : this.queue_ids) {
			s.writeInt(i);
		}
		s.flush();
	}
}
