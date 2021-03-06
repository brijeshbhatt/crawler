package crawler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author brijeshb
 *
 */
public class ApacheMailPageLinkExtractorImpl implements ApacheMailPageLinkExtractor {
	
	static Logger log = Logger.getLogger(ApacheMailPageLinkExtractorImpl.class);

	ConnectionFactory connectionFactory;

	public ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	/**
	 * This method is responsible for parsing the first page and to find out the
	 * relative URL of second page for every month of passed year.
	 * 
	 * @param pageURL
	 *            URL of first page
	 * @param year
	 *            Year for mail downloading
	 * @return A map which key is folder where mail will be download for
	 *         particular month and value is second page URL of second page for
	 *         every month of passed year.
	 */
	public Map<String, String> linkExtractFromFirstPage(String mainPageURL, String year) {
		log.info("linkExtractFromFirstPage is started.");
		Map<String, String> dirVsURLMap = new HashMap<String, String>();
		try {
			Connection connection = null;
			for (int i = 1; i <= connectionFactory.getNoOfConnectionCheck(); i++) {
				connection = connectionFactory.getConnection(mainPageURL);
				if (connection != null) {
					break;
				} else {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						log.error("Problem is in Thread sleep ", e);
					}
				}
			}
			if (connection != null) {
				Document doc = connection.get();
				if (doc != null) {
					Elements elements = doc.getElementsByClass("year");
					Iterator<Element> it = elements.iterator();
					while (it.hasNext()) {
						Element element = it.next();
						if (element.toString().contains("Year " + year)) {
							for (Element span : element.select("span.links")) {
								String href = span.select("a").first()
										.attr("href");
								String folderName = href.replace("/thread", "");
								dirVsURLMap.put(folderName, mainPageURL + href);
							}
							break;
						}
					}
				}
			}
		} catch (IOException ex) {
			log.error("Exception happen in parsing of FirstPage ", ex);
		}
		log.info("linkExtractFromFirstPage is ended.");
		return dirVsURLMap;
	}
	/**
	 * This method is responsible for parsing the second page and return list
	 * which contain http URL of all mail for a particular month .
	 * 
	 * @param pageURL
	 *            URL of second page for any Months
	 * @param folderName
	 *            folder name where mail will be download
	 * @return A map which key is file name of mail which will be download and
	 */
	public Map<String, String> linkExtractFromSecondPage(String secondPageUrl) {
		log.info("linkExtractFromSecondPage is started.");
		Map<String, String> map = new HashMap<String, String>();
		boolean nextHit;
		int i = 0;
		do {
			nextHit = false;
			String url = secondPageUrl + "?" + i;
			try {
				Connection connection = null;
				for (int j = 1; j <= getConnectionFactory().getNoOfConnectionCheck(); j++) {
					connection = getConnectionFactory().getConnection(url);
					if (connection != null) {
						break;
					} else {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							log.error("Problem is in thread sleep ", e);
						}
					}
				}
				if (connection != null) {
					Document doc = connection.get();
					if (doc != null) {
						Elements elements = doc.getElementsByClass("subject");
						if (!elements.isEmpty()) {
							nextHit = true;
							i++;
							Iterator<Element> it = elements.iterator();
							while (it.hasNext()) {
								Element element = it.next();
								if (!element.getElementsByAttribute("href")
										.isEmpty()) {
									String href = element
											.getElementsByAttribute("href")
											.first().attr("href");
									map.put(href,
											secondPageUrl.replace("thread",
													"raw") + "/" + href);
								}
							}
						}
					}
				}
			} catch (Exception e) {
				log.info("Exception happen in parsing of second page ", e);
			}
		} while (nextHit);

		log.info("linkExtractFromSecondPage is ended.");
		return map;
	}

}
