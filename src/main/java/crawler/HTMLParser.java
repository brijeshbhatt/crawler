package crawler;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.helper.HttpConnection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HTMLParser {
	private String mainPageURL;
	private String year;

	public HTMLParser(String url, String year) {
		super();
		this.mainPageURL = url;
		this.year = year;
	}

	void parseMainDocument() {
		try {
			Document doc = Jsoup.connect(mainPageURL).get();
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
									String folderName = href.replace("/thread",
											"");

									if ("http://mail-archives.apache.org/mod_mbox/maven-users/201412.mbox/thread"
											.equals(mainPageURL + href))
										parseSecondPage(mainPageURL + href,
												folderName);
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

	private void parseSecondPage(String secondPageUrl, String folderName) {
		// System.out.println(folderName + "  folderName");
		boolean nextHit;
		int i = 0, count = 0;
		;
		do {
			nextHit = false;
			String url = secondPageUrl + "?" + i;
			// System.out.println(url);
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
					page = webClient.getPage(url);
					webClient.waitForBackgroundJavaScriptStartingBefore(5000);
					webClient.waitForBackgroundJavaScript(30 * 1000);
				} catch (IOException e) {
					e.printStackTrace();
				}
				// WebResponse response = page.getWebResponse();
				// System.out.println("************");
				String content = page.getWebResponse().getContentAsString();
				// System.out.println(content);
				Document doc = Jsoup.parse(content);
				// System.out.println(doc);
				Elements tbodyList = doc.select("td.subject");
				if (!tbodyList.isEmpty()) {
					nextHit = true;
					i++;
					Iterator<Element> it = tbodyList.iterator();
					while (it.hasNext()) {
						Element element = it.next();
						if (!element.getElementsByAttribute("href").isEmpty()) {
							// element.getElementsByAttribute("href").first().attr("href");

							count++;
							// if (count == 1){
							//
							// downloadMail(
							// secondPageUrl.replace("thread", "raw")
							// + "/"
							// + element
							// .getElementsByAttribute(
							// "href").first()
							// .attr("href"),
							// folderName,element.text());
							// }

							// System.out.println(element
							// .getElementsByAttribute("href").first()
							// .attr("href")
							// + " " + count);
							// System.out.println();
						}

					}
				}
				webClient.closeAllWindows();
				// HtmlPage finalHTML =
				// com.gargoylesoftware.htmlunit.html.HTMLParser
				// .parseHtml(response, webClient.getCurrentWindow());
				// System.out.println(finalHTML.isHtmlPage());
				// (content, webClient.getCurrentWindow());
			} catch (Exception e) {

				e.printStackTrace();

			}
		} while (nextHit);

		System.out.println(count + "  count  " + folderName + "  folderName ");
	}

	private void downloadMail(String mailURL, String folderName, String subject) {
		// System.out.println(mailURL + " mailURL");
		try {
			String s = Jsoup.connect(mailURL).execute().body();
			System.out.println(s);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
