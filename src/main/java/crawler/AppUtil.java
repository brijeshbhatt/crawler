package crawler;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 *
 * @author Brijesh Bhatt
 * @since 1.0
 *
 */
public class AppUtil {

	static Logger log = Logger.getLogger(AppUtil.class);
	public static final String HOME;
	public static final String FILESEPERATOR;
	public static final String MAIL_FOLDER;
	static String URL = "";
	static String YEAR = "";
	int NO_OF_CHECK_CONN = 50;
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
			log.error("Exception happen in static block ", ex);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					log.error(
							"Problem in closing stream of propert config file",
							e);
				}
			}
		}
	}

	public Connection getConnection(String url) {
		Connection conn = null;
		try {
			conn = Jsoup
					.connect(url)
					.userAgent(
							"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
					.timeout(10000).ignoreHttpErrors(true);
			org.jsoup.Connection.Response response = conn
					.ignoreHttpErrors(true).execute();
			if (response.statusCode() != 200) {
				conn = null;
			}
		} catch (IOException e) {
			log.error("Problem with internet, Not found the connection of \""
					+ url + "\"", e);
			conn = null;
		}
		return conn;
	}

}
