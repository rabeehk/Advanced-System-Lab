package experiment;
import java.util.List;
import java.util.Random;

public class Configuration {
	private static Random random = new Random();	
	private static final String RandChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
	
	public static int TEXT_LENGTH = 200;
	public static int QUEUE_COUNT = 20;
	public static int USER_COUNT = 32;
	public static long WARMUP_TIME = 1000; // 90000; //60000;
	public static long COOLDOWN_TIME = 1000; //90000; //60000;
	public static long EXPERIMENT_TIME = 240000; //180000; //300000;
	public static long THINK_TIME = 0; 

	
	public static String getRandomText(int len, Random rnd) {
		// StringBuilder is not safe for multi-threaded application
        char[] buffer = new char[len];
        for(int i=0; i<buffer.length; ++i) {
            buffer[i]= RandChars.charAt(rnd.nextInt(RandChars.length()));
        }
        return new String(buffer);
	}
	

	public static int getReceiver(int user) {
		if (user < (Configuration.USER_COUNT / 2)) {
			return random.nextInt(Configuration.USER_COUNT/2);
		} else {
			return (Configuration.USER_COUNT/2) + random.nextInt(Configuration.USER_COUNT/2);
		}
	}
	public static int getQueue(List<Integer> queues, int user) {
		if (user < (Configuration.USER_COUNT / 2)) {
			// first half of users use first half of queues
			return queues.get(random.nextInt(Configuration.QUEUE_COUNT/2));
		} else {
			// second half of users use second half of queues
			return queues.get(random.nextInt(
					(Configuration.QUEUE_COUNT/2)+Configuration.QUEUE_COUNT/2));
		}
	}
}
