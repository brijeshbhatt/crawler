package crawler;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

/**
 *
 * @author Brijesh Bhatt
 * @since 1.0
 *
 */
public class ConnectionFactory {

	static Logger log = Logger.getLogger(ConnectionFactory.class);

	private int noOfConnectionCheck;

	public int getNoOfConnectionCheck() {
		return noOfConnectionCheck;
	}

	public void setNoOfConnectionCheck(int noOfConnectionCheck) {
		this.noOfConnectionCheck = noOfConnectionCheck;
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
