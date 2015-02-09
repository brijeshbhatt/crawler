package crawler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * @author brijeshb
 *
 */
public class ApacheMailPageParserImple implements ApacheMailPageParser {
	static Logger log = Logger.getLogger(ApacheMailPageParserImple.class
			.getName());

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
		Map<String, String> pro = new HashMap<String, String>();
		try {
			Document doc = null;
			if (AppUtil.checkConnection(mainPageURL)){
				doc = Jsoup.connect(mainPageURL).get();
			}
			Elements elements = doc.getElementsByClass("year");
			Iterator<Element> it = elements.iterator();
			while (it.hasNext()) {
				Element element = it.next();
				if (element.toString().contains("Year " + year)) {
					for (Element span : element.select("span.links")) {
						String href = span.select("a").first().attr("href");
						String folderName = href.replace("/thread", "");
						pro.put(folderName, mainPageURL + href);
					}
					break;
				}
			}
		} catch (IOException ex) {
			log.error("Exception happen in parsing of FirstPage ", ex);
		}
		log.info("parseFirstPage is ended.");
		return pro;
	}

	/**
	 * @param secondPageUrl
	 * @param folderName
	 */
	void createThreadForSecondPageParser(final String secondPageUrl,
			final String folderName) {
		new Thread() {
			@Override
			public void run() {
				downloadMailFromSecondPage(secondPageUrl, folderName);
			}
		}.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * crawler.ApacheMailPageParser#downloadMailFromSecondPage(java.lang.String,
	 * java.lang.String)
	 */
	public void downloadMailFromSecondPage(String secondPageUrl,
			String folderName) {
		log.info("secondPageUrl is started.");
		MailDownloaderImple mailDownLoader = new MailDownloaderImple();
		boolean nextHit;
		int i = 0, count = 0;
		do {
			nextHit = false;
			String url = secondPageUrl + "?" + i;
			try {

				WebClient webClient = new WebClient(BrowserVersion.CHROME);
				webClient.getOptions().setJavaScriptEnabled(true);
				webClient.getOptions().setActiveXNative(true);
				webClient.getOptions().setAppletEnabled(false);
				webClient.getOptions().setCssEnabled(true);
				webClient.getOptions().setDoNotTrackEnabled(true);
				webClient.getOptions().setGeolocationEnabled(false);
				webClient.getOptions().setPopupBlockerEnabled(false);
				webClient.getOptions().setPrintContentOnFailingStatusCode(true);
				webClient.getOptions().setThrowExceptionOnFailingStatusCode(
						true);
				webClient.getOptions().setThrowExceptionOnScriptError(true);
				webClient
						.setAjaxController(new NicelyResynchronizingAjaxController());
				HtmlPage page = null;
				try {
					if (AppUtil.checkConnection(url)) {
						page = webClient.getPage(url);
						webClient
								.waitForBackgroundJavaScriptStartingBefore(5000);
						webClient.waitForBackgroundJavaScript(30 * 1000);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				String content = page.getWebResponse().getContentAsString();
				Document doc = Jsoup.parse(content);
				Elements tbodyList = doc.select("td.subject");
				if (!tbodyList.isEmpty()) {
					nextHit = true;
					i++;
					Iterator<Element> it = tbodyList.iterator();
					while (it.hasNext()) {
						Element element = it.next();
						if (!element.getElementsByAttribute("href").isEmpty()) {
							count++;
							mailDownLoader.downloadMail(
									secondPageUrl.replace("thread", "raw")
											+ "/"
											+ element.getElementsByAttribute("href").first().attr("href"), folderName,
											element.getElementsByAttribute("href").first().attr("href"));
						}
					}
				}
				webClient.closeAllWindows();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (nextHit);
		log.info("Total " + count + " mails has been downloaded in "
				+ folderName + " .");
		log.info("secondPageUrl is ended.");
	}

}
