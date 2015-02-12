package crawler;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Main {

	static Logger log = Logger.getLogger(Main.class);

	private ApacheMailPageLinkExtractorImpl linkExtractor;
	private MailDownloader mailDownloader;
	private ConnectionFactory connectionFactory;

	public ApacheMailPageLinkExtractorImpl getLinkExtractor() {
		return linkExtractor;
	}

	public void setLinkExtractor(ApacheMailPageLinkExtractorImpl linkExtractor) {
		this.linkExtractor = linkExtractor;
	}

	public MailDownloader getMailDownloader() {
		return mailDownloader;
	}

	public void setMailDownloader(MailDownloader mailDownloader) {
		this.mailDownloader = mailDownloader;
	}

	public ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public static void main(String[] args) {
		log.info("main method is started.");
		if (args.length == 2) {
			String url = args[0];
			String year = args[1];
			if (checkArguments(url, year)) {
				ApplicationContext context = new ClassPathXmlApplicationContext(
						"beans.xml");
				Main main = (Main) context.getBean("main");
				if (url.equals("") && year.equals("")) {
					log.debug("No url and year are set in configuration file.");
				} else {
					Map<String, String> pro = main.linkExtractor
							.parseFirstPage(url, year);
					Set<Entry<String, String>> entrySet = pro.entrySet();
					for (Entry<String, String> entry : entrySet) {
						main.createThreadForSecondPageParser(entry.getKey(),
								entry.getValue());
					}
				}
			}
		} else {
			log.info("Pass URL and year as Application Arguments.");
		}
		log.info("main method is ended.");
	}

	static private boolean checkArguments(String url, String year) {
		try {
			Integer.parseInt(year);
		} catch (NumberFormatException e) {
			log.error("Pass correct year as second argument of main ", e);
		}
		return url.contains("http://");
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
				Map<String, String> subjectVSURL = linkExtractor
						.parseSecondPage(secondPageUrl);
				int count = 0;
				Set<Entry<String, String>> entrySet = subjectVSURL.entrySet();
				for (Entry<String, String> entry : entrySet) {
					for (int i = 1; i <= connectionFactory
							.getNoOfConnectionCheck(); i++) {
						try {
							mailDownloader.downloadMail(entry.getValue(),
									folderName, entry.getKey());
							count++;
							break;
						} catch (IOException e) {
							log.error("Problem in downloading the file "
									+ entry.getKey(), e);
							try {
								sleep(2000);
							} catch (InterruptedException ie) {
								log.error("Problem in thread sleep ", ie);
							}
						}
					}
				}
				log.info("Total " + count + " mails has been downloaded in "
						+ folderName + " .");
			}
		}.start();
	}
}
