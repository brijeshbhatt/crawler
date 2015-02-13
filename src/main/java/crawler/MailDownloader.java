package crawler;

import java.io.IOException;

/**
 * Responsible for downloading of mails.
 * 
 * @author Brijesh Bhatt
 * @since 1.0
 */
public interface MailDownloader {
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
	void downloadMail(String mailURL, String folderName, String subject)
			throws IOException;
}
