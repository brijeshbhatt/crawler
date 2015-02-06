package crawler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Main {
	static Logger log = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) {
		log.info("Main class main method is started.");

		String url = "";
		String year = "";

		Properties prop = new Properties();
		InputStream input = null;
		try {
			String filename = "config.properties";
			input = Main.class.getClassLoader().getResourceAsStream(filename);
			if (input == null) {
				System.out.println("Sorry, unable to find " + filename);
				return;
			}
			prop.load(input);
			url = prop.getProperty("url");
			year = prop.getProperty("year");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		if (url.equals("") && year.equals("")) {
			log.debug("No url and year are set in configuration file.");
		} else {
			HTMLParser parser = new HTMLParser(url, year);
			parser.parseMainDocument();
		}

		log.info("Main class main method is ended.");

	}

}
