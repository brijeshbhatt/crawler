package crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;
import org.jsoup.Connection;

/**
 *
 * @author Brijesh Bhatt
 * @since 1.0
 *
 */
public class MailDownloaderImpl implements MailDownloader {

	static Logger log = Logger.getLogger(ApacheMailPageParserImpl.class);
	
	AppUtil appUtil = new AppUtil();

	/*
	 * (non-Javadoc)
	 * 
	 * @see crawler.MailDownloader#downloadMail(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void downloadMail(String mailURL, String folderName, String subject)
			throws IOException {
		Connection connection = appUtil.getConnection(mailURL);
		if (connection != null) {
			String rawMail = connection.execute().body();
			if (rawMail != null && !rawMail.equals("")) {
				log.info("Downloading Mail with subject " + subject);
				File dir = new File(AppUtil.HOME + AppUtil.FILESEPERATOR
						+ AppUtil.MAIL_FOLDER + AppUtil.FILESEPERATOR
						+ folderName);
				dir.mkdirs();
				subject = subject.replace("/", "A");
				File file = new File(dir, subject + ".txt");
				file.createNewFile();
				FileWriter outFile = new FileWriter(file, true);
				PrintWriter out = new PrintWriter(outFile);
				out.append(rawMail);
				out.close();
				outFile.close();
			}
		}else{
			throw new IOException();
		}
	}

}
