package crawler;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Map;

import org.junit.Test;

/**
 *
 * @author Brijesh Bhatt
 * @since 1.0
 *
 */
public class CrawlerTest {
	
	@Test
	public void testDownloadMail() throws Exception {
		new MailDownloaderImple()
				.downloadMail(
						"http://mail-archives.apache.org/mod_mbox/maven-users/201412.mbox/raw/%3C547C1A5F.7070709@uni-jena.de%3E",
						"201412.mbox", "Maven Plugin Refelction");
		System.out.println("success");

	}

	@Test
	public void testParseFirstPage() throws Exception {
		Map<String, String> map = new ApacheMailPageParserImple()
				.parseFirstPage(
						"http://mail-archives.apache.org/mod_mbox/maven-users/",
						"2014");
		assertSame("Value should be same.", 12, map.keySet().size());
		Iterator<String> iterator = map.keySet().iterator();
		while (iterator.hasNext()) {
			String folder = (String) iterator.next();
			System.out.println(folder + " ~~~ " + map.get(folder));
		}
		System.out.println("success");

	}

	@Test
	public void testDownloadMailFromSecondPage() throws Exception {
		new ApacheMailPageParserImple()
				.downloadMailFromSecondPage(
						"http://mail-archives.apache.org/mod_mbox/maven-users/201412.mbox/thread",
						"201412.mbox");
		System.out.println("success");

	}
}
