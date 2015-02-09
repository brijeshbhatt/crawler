package crawler;

import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

public class Main {

	static Logger log = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) {
		log.info("main method is started.");

		if (AppUtil.URL.equals("") & AppUtil.YEAR.equals("")) {
			log.debug("No url and year are set in configuration file.");
		} else {
			ApacheMailPageParserImple parser = new ApacheMailPageParserImple();
			Map<String, String> pro = parser.parseFirstPage(AppUtil.URL, AppUtil.YEAR);
			Iterator<String> iterator = pro.keySet().iterator();
			while (iterator.hasNext()) {
				String folder = (String) iterator.next();
				parser.createThreadForSecondPageParser(pro.get(folder), folder);
			}
		}
	}
}
