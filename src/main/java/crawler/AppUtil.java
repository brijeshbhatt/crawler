package crawler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

/**
 *
 * @author Brijesh Bhatt
 * @since 1.0
 *
 */
public final class AppUtil {

	static Logger log = Logger.getLogger(AppUtil.class.getName());
	public static final String HOME;
	public static final String FILESEPERATOR;
	public static final String MAIL_FOLDER;
	static String URL = "";
	static String YEAR = "";

	static {
		java.util.Properties properties = System.getProperties();
		HOME = properties.get("user.home").toString();
		FILESEPERATOR = properties.get("file.separator").toString();
		MAIL_FOLDER = "mail";
	}
	
	
	static {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			String filename = "config.properties";
			input = Main.class.getClassLoader().getResourceAsStream(filename);
			prop.load(input);
			URL = prop.getProperty("mainURL");
			YEAR = prop.getProperty("year");
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

	public static boolean checkConnection(String url) {
		org.jsoup.Connection.Response response;
		try {
			response = Jsoup
					.connect(url)
					.userAgent(
							"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
					.timeout(10000).ignoreHttpErrors(true).execute();
			if (response.statusCode() == 200) {
				return true;
			}
		} catch (IOException e) {
			log.error("Problem with internet, Not found the connection of \""
					+ url + "\"");
			try {
				Thread.currentThread().sleep(1000);
				return checkConnection(url);
			} catch (InterruptedException e1) {
				log.error(e1.getMessage());
			}

		}
		return false;
	}
}
