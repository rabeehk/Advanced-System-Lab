package experiment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExperimentLog {
	private File file;
	private static File base_directory = new File(System.getProperty("user.home"), "bench-logs");
	private Writer writer;
	
	public ExperimentLog(String dirName, String fileName) {	
		File experiment_directory = new File(base_directory, dirName);
		if (!experiment_directory.exists()) {
			experiment_directory.mkdirs();
		}
		
		this.file = new File(experiment_directory, fileName);
		try {
			this.writer = new BufferedWriter(new FileWriter(this.file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void appendLine(String string) {
		try {
			this.writer.write(string+"\n");
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	public void append(List<String> items) {
		StringBuilder builder = new StringBuilder(new Date().toString());
		for (String s : items) {
			builder.append("\t");
			builder.append(s);
		}
		builder.append('\n');
		try {
			this.writer.write(builder.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void append(String s) {
		ArrayList<String> l = new ArrayList<String>();
		l.add(s);
		this.append(l);
	}
	
	public void flush() {
		try {
			this.writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void terminate() {
		try {
			this.writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
