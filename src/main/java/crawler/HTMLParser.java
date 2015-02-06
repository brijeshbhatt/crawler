package crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class HTMLParser {
	private String mainPageURL;
	private String year;
	private String home;
	private String separator;

	public HTMLParser(String url, String year) {
		super();
		this.mainPageURL = url;
		this.year = year;
	}

	{
		java.util.Properties properties = System.getProperties();
		home = properties.get("user.home").toString();
		separator = properties.get("file.separator").toString();
	}

	void parseMainDocument() {
		try {
			Document doc = Jsoup.connect(mainPageURL).get();
			Elements elements = doc.getElementsByClass("year");
			Iterator<Element> it = elements.iterator();
			while (it.hasNext()) {
				Element element = it.next();
				if (element.toString().contains("Year " + year)) {
					for(Element span : element.select("span.links")){
						String href  = span.select("a").first().attr("href");
						String folderName = href.replace("/thread","");
						createThreadForSecondPageParser(mainPageURL + href,folderName);
					}
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void createThreadForSecondPageParser(final String secondPageUrl,final  String folderName){
		new Thread(){
			@Override
			public void run() {
				parseSecondPage(secondPageUrl,folderName);
			}
		}.start();
	}
	private void parseSecondPage(String secondPageUrl, String folderName) {
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
				webClient.getOptions().setThrowExceptionOnFailingStatusCode(true);
				webClient.getOptions().setThrowExceptionOnScriptError(true);
				webClient.setAjaxController(new NicelyResynchronizingAjaxController());
				HtmlPage page = null;
				try {
					page = webClient.getPage(url);
					webClient.waitForBackgroundJavaScriptStartingBefore(5000);
					webClient.waitForBackgroundJavaScript(30 * 1000);
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
							downloadMail(secondPageUrl.replace("thread", "raw")
							 + "/"+element.getElementsByAttribute("href").first().attr("href"), folderName , element.text());
						}
					}
				}
				webClient.closeAllWindows();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (nextHit);
		System.out.println("Total "+count + " mails has been downloaded in " + folderName +" .");
	}

	private void downloadMail(String mailURL, String folderName, String subject) {
		try {
				String rawMail = Jsoup.connect(mailURL).execute().body();
				System.out.println("Mail with subject "+subject+" has been downloaded");
				File dir = new File(home + separator + "mail" + separator + folderName);
				dir.mkdirs();
				subject = subject.replace("/", ",");
				File file = new File(dir, subject + ".txt");
				if (file.exists()) {
					file.delete();
					file = new File(dir, subject + ".txt");
				}
				file.createNewFile();
				FileWriter outFile = new FileWriter(file, true);
				PrintWriter out = new PrintWriter(outFile);
				out.append(rawMail);
				out.close();
				outFile.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}

}
