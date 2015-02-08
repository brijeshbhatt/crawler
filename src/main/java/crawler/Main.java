package crawler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Main {

	static Logger log = Logger.getLogger(Main.class.getName());
	static String url = "";
	static String year = "";
	static {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			String filename = "config.properties";
			input = Main.class.getClassLoader().getResourceAsStream(filename);
			prop.load(input);
			url = prop.getProperty("url");
			year = prop.getProperty("year");
		} catch (IOException ex) {
			log.error("Exception happen in static block ",ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		log.info("main method is started.");

		if (url.equals("") && year.equals("")) {
			log.debug("No url and year are set in configuration file.");
		} else {
			ApacheMailPageParserImple parser = new ApacheMailPageParserImple();
			Map<String, String> pro = parser.parseFirstPage(url, year);
			Iterator<String> iterator = pro.keySet().iterator();
			while (iterator.hasNext()) {
				String folder = (String) iterator.next();
				parser.createThreadForSecondPageParser(pro.get(folder), folder);
			}
		}

		log.info("main method is ended.");

	}

}
