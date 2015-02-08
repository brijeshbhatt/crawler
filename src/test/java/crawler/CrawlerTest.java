package crawler;

import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

/**
 *
 * @author Brijesh Bhatt
 * @since 1.0
 *
 */
public class CrawlerTest {

	String mainURL = "";
	String year = "";
	String secondPageURL = "";
	String mailURL = "";
	String folderName = "";
	String subject = "";
	{
		Properties prop = new Properties();
		InputStream input = null;
		try {
			String filename = "testConfig.properties";
			input = CrawlerTest.class.getClassLoader().getResourceAsStream(
					filename);
			prop.load(input);
			mainURL = prop.getProperty("mainURL");
			year = prop.getProperty("year");
			secondPageURL = prop.getProperty("secondPageURL");
			mailURL = prop.getProperty("mailURL");
			folderName = prop.getProperty("folderName");
			subject = prop.getProperty("subject");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	
	@Test
	public void testDownloadMail() throws Exception {
		new MailDownloaderImple().downloadMail(mailURL, folderName, subject);
	}

	@Test
	public void testParseFirstPage() throws Exception {
		Map<String, String> map = new ApacheMailPageParserImple()
				.parseFirstPage(mainURL, year);
		assertSame("Value should be same.", 12, map.keySet().size());
	}

	@Test
	public void testDownloadMailFromSecondPage() throws Exception {
		new ApacheMailPageParserImple().downloadMailFromSecondPage(
				secondPageURL, folderName);
	}
}
