package crawler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HTMLParser {
	private String url;
	private String year;

	public HTMLParser(String url, String year) {
		super();
		this.url = url;
		this.year = year;
	}

	void parseMainDocument() {
		try {
			Document doc = Jsoup.connect(url).get();
			Elements elements = doc.getElementsByClass("year");
			Iterator<Element> it = elements.iterator();
			while (it.hasNext()) {
				Element element = it.next();
				if (element.toString().contains("Year " + year)) {
					element.childNodeSize();
					List<Node> nodes = element.childNodes();
					Iterator<Node> nodeIte = nodes.iterator();
					while (nodeIte.hasNext()) {
						Node node = nodeIte.next();
						if (node.nodeName().equals("tbody")) {
							List<Node> tbodyNodes = node.childNodes();
							Iterator<Node> iteTBodyNodes = tbodyNodes
									.iterator();
							while (iteTBodyNodes.hasNext()) {
								Node tbodyNode = iteTBodyNodes.next();
								if (tbodyNode.nodeName().equals("tr")) {
									Node tdOfMonth = tbodyNode.childNode(3)
											.childNode(0).childNode(0);
									String href = tdOfMonth.attr("href");
									//System.out.println(url + href);
										//	.replace("thread", "browser");
									if ("http://mail-archives.apache.org/mod_mbox/maven-users/201412.mbox/thread"
											.equals(url + href))
										parseSecondPage(url + href);

								}
							}
						}

					}

				}

			}

		} catch (IOException e) {

			e.printStackTrace();

		}
	}

	private void parseSecondPage(String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			
			WebClient webClient  = new WebClient();
			// webClient = new WebClient(BrowserVersion.CHROME);
			webClient.getOptions().setJavaScriptEnabled(true);
			webClient.getOptions().setActiveXNative(true);
			webClient.getOptions().setAppletEnabled(false);
			webClient.getOptions().setCssEnabled(true);
			webClient.getOptions().setDoNotTrackEnabled(true);
			webClient.getOptions().setGeolocationEnabled(false);
			webClient.getOptions().setPopupBlockerEnabled(false);
			webClient.getOptions().setPrintContentOnFailingStatusCode(true);
			webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
			webClient.getOptions().setThrowExceptionOnScriptError(true);
			webClient.setAjaxController(new NicelyResynchronizingAjaxController());

			HtmlPage page = null;
			    try {
			        page = webClient.getPage(url);
			        webClient.waitForBackgroundJavaScriptStartingBefore(5000);
				    webClient.waitForBackgroundJavaScript(30 * 1000);
			    }  catch (IOException e) {
			        
			        e.printStackTrace();
			    }
			    System.out.println(page.getDocumentElement());
			    
			   // System.out.println(doc);
		} catch (IOException e) {

			e.printStackTrace();

		}
	}
}
