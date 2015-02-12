package crawler;

import java.util.Map;

/**
 *
 * @author Brijesh Bhatt
 * @since 1.0
 *
 */
public interface ApacheMailPageLinkExtractor {

	/**
	 * This method is responsible for parsing the first page and to find out the
	 * relative URL of second page for every month of passed year.
	 * 
	 * @param pageURL
	 *            URL of first page
	 * @param year
	 *            Year for mail downloading
	 * @return
	 * 			A map  which key is folder where mail will be download for particular month
	 * 			and value is second page URL of second page for every month of passed year.
	 */
	Map<String, String> parseFirstPage(String pageURL, String year);

	/**
	 * This method is responsible for parsing the second page and return list
	 * which contain http URL of all mail for a particular month .
	 * 
	 * @param pageURL
	 *            URL of second page for any Months
	 * @param folderName
	 *            folder name where mail will be downloaded
	 * 
	 */
	Map<String, String> parseSecondPage(String pageURL);
}
