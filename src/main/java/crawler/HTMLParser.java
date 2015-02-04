package crawler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

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
									String href = tdOfMonth.attr("href")
											.replace("thread", "browser");
									System.out.println();
//									if ("http://mail-archives.apache.org/mod_mbox/maven-users/201412.mbox/browser"
//											.equals(url + href))
//										parseSecondPage(url + href);

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
			System.out.println(doc);
		} catch (IOException e) {

			e.printStackTrace();

		}
	}
}
