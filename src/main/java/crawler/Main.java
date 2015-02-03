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

		String url = getURLFromUser();
		System.out.println("Insert url is :" + url);
		parseDocument(url);
	}

	static String getURLFromUser() {
		System.out.println("Insert the url for crawling : ");
		Scanner in = new Scanner(System.in);
		String url = in.nextLine();
		return url;
	}
	
	static  void parseDocument(String url){
		try {
			Document doc = Jsoup.connect(url).get();
			
			System.out.println(doc);
//			System.out.println(doc.body());
//			System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@@22222222");
//			Elements elements = doc.getElementsByClass("year");
//			Iterator<Element> it = elements.iterator();
//			while(it.hasNext()){
//				Element element = it.next();
//				//System.out.println(element.toString());
//				if(element.toString().contains("Year 2014")){
//					System.out.println(element);
//					System.out.println("*************************");
//				}
//				
//			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
