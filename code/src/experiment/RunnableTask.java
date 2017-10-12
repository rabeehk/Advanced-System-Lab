package experiment;

public interface RunnableTask {
	void execute(boolean isRunning);
	void regular();
	void terminate();
}
