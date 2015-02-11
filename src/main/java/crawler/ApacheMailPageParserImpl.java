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
public class ApacheMailPageParserImpl implements ApacheMailPageParser {
	static Logger log = Logger.getLogger(ApacheMailPageParserImpl.class);

	AppUtil appUtil = new AppUtil();

	/**
	 * Parse the first page and to find out the relative URL of second page and
	 * return the Properties object which contain folder as key and URL of
	 * second page as value.
	 * 
	 * @param pageURL
	 *            URL of first page
	 * @param year
	 *            Year for mail downloading
	 */
	public Map<String, String> parseFirstPage(String mainPageURL, String year) {
		log.info("parseFirstPage is started.");
		Map<String, String> dirVsURLMap = new HashMap<String, String>();
		try {
			Connection connection = null;
			for (int i = 1; i <= appUtil.NO_OF_CHECK_CONN; i++) {
				connection = appUtil.getConnection(mainPageURL);
				if (connection != null) {
					break;
				} else {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						log.error("Problem in thread sleep ", e);
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
		log.info("parseFirstPage is ended.");
		return dirVsURLMap;
	}

	@Override
	public Map<String, String> parseSecondPage(String secondPageUrl) {
		log.info("secondPageUrl is started.");
		Map<String, String> map = new HashMap<String, String>();
		boolean nextHit;
		int i = 0;
		do {
			nextHit = false;
			String url = secondPageUrl + "?" + i;
			try {
				Connection connection = null;
				for (int j = 1; j <= appUtil.NO_OF_CHECK_CONN; j++) {
					connection = appUtil.getConnection(url);
					if (connection != null) {
						break;
					} else {
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							log.error("Problem in thread sleep ", e);
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
				log.info("Exception happen in downloadMailFromSecondPage ", e);
			}
		} while (nextHit);

		log.info("secondPageUrl is ended.");
		return map;
	}

}
