package crawler;

/**
*
* @author Brijesh Bhatt
* @since 1.0
*
*/
public interface MailDownloader {
	/**
	 * @param mailURL
	 * @param folderName
	 * @param subject
	 */
	void downloadMail(String mailURL, String folderName, String subject);
}
