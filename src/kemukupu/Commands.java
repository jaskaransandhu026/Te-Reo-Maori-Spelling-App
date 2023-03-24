package kemukupu;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.ArrayList;

// This class contains all the helper functions that make bash calls
public class Commands {

	// run a bash command, returns an array of standard output
	public static ArrayList<String> bash(String cmd) {
		ArrayList<String> ret = new ArrayList<String>();

		try {
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
			Process process = builder.start();
			InputStream stdout = process.getInputStream();
			BufferedReader stdoutBuffered = new BufferedReader(new InputStreamReader(stdout));
			String line = null;

			// save stdout to method return
			while ((line = stdoutBuffered.readLine()) != null) {
				ret.add(line);
			}
		} catch (Exception e) {
		}

		return ret;

	}

	// use festival to speak a line in Maori at a given speed
	public static void speak(String speech, double speed) {
		try {
			// create scm file
			Writer fileWriter = new FileWriter(".festivalSceme.scm", false);
			fileWriter.write("\n(voice_akl_mi_pk06_cg)");
			fileWriter.write("(Parameter.set 'Duration_Stretch " + speed + ")");
			fileWriter.write("\n(SayText \"" + speech.replaceAll("-", " ") + "\")");
			fileWriter.write("\n(quit)");
			fileWriter.close();

			// run scm file
			String cmd = "festival .festivalSceme.scm";
			ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
			builder.start();

		} catch (Exception e) {
		}
	}

}