package duy.hw4.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Log {

	public void logMessage(String message, String email) {
		// System.out.println("["+ new
		// SimpleDateFormat("yyyy-MM-dd kk:mm:ss").format(new Date())
		// +"] Meesage from logger: "+message);

		try {
			String content = "["
			        + new SimpleDateFormat("yyyy-MM-dd kk:mm:ss")
			                .format(new Date()) + "] Meesage from " + email
			        + " : " + message;
			File file = new File("moneymanager-log.txt");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();
			System.out.println("Done       ");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}