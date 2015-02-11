package crawler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

public class Main {

	static Logger log = Logger.getLogger(Main.class);

	ApacheMailPageParserImpl parser;
	MailDownloader mailDownloader;
	AppUtil appUtil = new AppUtil();

	public static void main(String[] args) {
		log.info("main method is started.");
		Main main = new Main();
		if (AppUtil.URL.equals("") & AppUtil.YEAR.equals("")) {
			log.debug("No url and year are set in configuration file.");
		} else {
			main.parser = new ApacheMailPageParserImpl();
			main.mailDownloader = new MailDownloaderImpl();
			 Map<String, String> pro =
			 main.parser.parseFirstPage(AppUtil.URL,AppUtil.YEAR);
			 Set<Entry<String, String>> entrySet = pro.entrySet();
			for (Entry<String, String> entry : entrySet) {
				main.createThreadForSecondPageParser(entry.getKey(),
						entry.getValue());
			}
		}
	}

	/**
	 * @param secondPageUrl
	 * @param folderName
	 */
	private void createThreadForSecondPageParser(final String folderName,
			final String secondPageUrl) {
		new Thread() {
			@Override
			public void run() {
				Map<String, String> subjectVSURL = parser
						.parseSecondPage(secondPageUrl);
				int count = 0;
				Set<Entry<String, String>> entrySet = subjectVSURL.entrySet();
				for (Entry<String, String> entry : entrySet) {
					for (int i = 1; i<= appUtil.NO_OF_CHECK_CONN; i++) {
						try {
							mailDownloader.downloadMail(entry.getValue(),
									folderName, entry.getKey());
							count++;
							break;
						} catch (IOException e) {
							log.error("Problem in downloading the file "
									+ entry.getKey(), e);
							try {
								sleep(3000);
							} catch (InterruptedException ie) {
								log.error("Problem in thread sleep ", ie);
							}
						}
					}
				}
				log.info("Total " + count
						+ " mails has been downloaded in " + folderName + " .");
			}
		}.start();
	}
}
