package crawler;

import java.util.Map;
import java.util.Properties;

/**
 *
 * @author Brijesh Bhatt
 * @since 1.0
 *
 */
public interface ApacheMailPageParser {

	/**
	 * This method is responsible for parsing the first page and to find out the
	 * relative URL of second page for every month of passed year.
	 * 
	 * @param pageURL
	 *            URL of first page
	 * @param year
	 *            Year for mail downloading
	 */
	Map<String, String> parseFirstPage(String pageURL, String year);

	/**
	 * This method is responsible for parsing the second page and to download
	 * the all mail for a particular month .
	 * 
	 * @param pageURL
	 *            URL of second page for any Months
	 * @param folderName
	 *            folder name where mail will be downloaded
	 * 
	 */
	void downloadMailFromSecondPage(String pageURL, String folderName);
}
