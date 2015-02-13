package crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.springframework.beans.factory.InitializingBean;

/**
 * Responsible for downloading of mails.
 * 
 * @author Brijesh Bhatt
 * @since 1.0
 *
 */
public class MailDownloaderImpl implements MailDownloader, InitializingBean {

	static Logger log = Logger.getLogger(ApacheMailPageLinkExtractorImpl.class);

	private ConnectionFactory connectionFactory;

	public ConnectionFactory getConnectionFactory() {
		return connectionFactory;
	}

	public void setConnectionFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	private String home;
	private String fileseperator;
	private String mailFolder;

	public String getHome() {
		return home;
	}

	public void setHome(String home) {
		this.home = home;
	}

	public String getFileseperator() {
		return fileseperator;
	}

	public void setFileseperator(String fileseperator) {
		this.fileseperator = fileseperator;
	}

	public String getMailFolder() {
		return mailFolder;
	}

	public void setMailFolder(String mailFolder) {
		this.mailFolder = mailFolder;
	}

	/**
	 * This method is responsible for downloading mail from passed mailURl and
	 * write mail content into file with name passed subject and save that file
	 * into passed folder of system.
	 * 
	 * @param mailURL
	 *            Mail link where raw content of mail will be exist.
	 * @param folderName
	 *            directory full path where mail will be download.
	 * @param subject
	 *            file name of download mail.
	 */
	public void downloadMail(String mailURL, String folderName, String subject)
			throws IOException {
		log.info("downloadMail is started.");
		Connection connection = connectionFactory.getConnection(mailURL);
		if (connection != null) {
			String rawMail = connection.execute().body();
			if (rawMail != null && !rawMail.equals("")) {
				log.info("Downloading Mail with subject " + subject);
				File dir = new File(getHome() + getFileseperator()
						+ getMailFolder() + getFileseperator() + folderName);
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
		} else {
			throw new IOException();
		}
		log.info("downloadMail is ended.");
	}

	public void afterPropertiesSet() throws Exception {
		java.util.Properties properties = System.getProperties();
		setHome(properties.get("user.home").toString());
		setFileseperator(properties.get("file.separator").toString());
	}

}
