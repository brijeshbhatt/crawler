package crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;

/**
 *
 * @author Brijesh Bhatt
 * @since 1.0
 *
 */
public class MailDownloaderImple implements MailDownloader {

	static Logger log = Logger.getLogger(ApacheMailPageParserImple.class.getName());

	/*
	 * (non-Javadoc)
	 * 
	 * @see crawler.MailDownloader#downloadMail(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void downloadMail(String mailURL, String folderName, String subject) {
		try {
			String rawMail = "";
			if (AppUtil.checkConnection(mailURL)) {
				rawMail = Jsoup
						.connect(mailURL)
						.userAgent(
								"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
						.timeout(10000).ignoreHttpErrors(true).execute().body();
			}

			log.info("Mail with subject " + subject
					+ " has been downloaded");
			File dir = new File(AppUtil.HOME + AppUtil.FILESEPERATOR
					+ AppUtil.MAIL_FOLDER + AppUtil.FILESEPERATOR + folderName);
			dir.mkdirs();
			subject = subject.replace("/", "A");
			File file = new File(dir, subject + ".txt");
			file.createNewFile();
			System.out.println(file.getName());
			FileWriter outFile = new FileWriter(file, true);
			PrintWriter out = new PrintWriter(outFile);
			out.append(rawMail);
			out.close();
			outFile.close();

		} catch (IOException e) {
			log.error(e.getMessage());
		}
	}

}
