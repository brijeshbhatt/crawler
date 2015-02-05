package crawler;

import java.io.IOException;
import java.util.Iterator;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Main {
	public static void main(String[] args) {
//		String secondPageUrl = "201412.mbox/thread";
//		String url = secondPageUrl.replace("/thread", "");
//		System.out.println(url);

//		String url = getURLFromUser();
//		System.out.println("Insert url is :" + url);
		
		
		String url = "http://mail-archives.apache.org/mod_mbox/maven-users/";
		String year = "2014";
		HTMLParser parser = new HTMLParser(url,year);
		parser.parseMainDocument();
	}

	static String getURLFromUser() {
		System.out.println("Insert the url for crawling : ");
		Scanner in = new Scanner(System.in);
		String url = in.nextLine();
		return url;
	}

}
