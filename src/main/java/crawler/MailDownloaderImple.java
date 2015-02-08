package crawler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import org.jsoup.Jsoup;

/**
*
* @author Brijesh Bhatt
* @since 1.0
*
*/
public class MailDownloaderImple implements MailDownloader {

	/*
	 * @see crawler.MailDownloader#downloadMail(java.lang.String,
	 * java.lang.String, java.lang.String)
	 */
	@Override
	public void downloadMail(String mailURL, String folderName, String subject) {
		try {
			String rawMail = Jsoup.connect(mailURL).execute().body();
			System.out.println("Mail with subject " + subject
					+ " has been downloaded");
			File dir = new File(AppConstant.HOME + AppConstant.FILESEPERATOR
					+ AppConstant.MAIL_FOLDER + AppConstant.FILESEPERATOR + folderName);
			dir.mkdirs();
			subject = subject.replace("/", ",");
			File file = new File(dir, subject + ".txt");
			if (file.exists()) {
				file.delete();
				file = new File(dir, subject + ".txt");
			}
			file.createNewFile();
			FileWriter outFile = new FileWriter(file, true);
			PrintWriter out = new PrintWriter(outFile);
			out.append(rawMail);
			out.close();
			outFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
