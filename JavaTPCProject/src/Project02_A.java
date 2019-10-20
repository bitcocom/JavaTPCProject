import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
public class Project02_A {
	public static void main(String[] args) {
      // Jsoup API
      String url="https://sports.news.naver.com/wfootball/index.nhn";
      Document doc=null;
      try {
		doc=Jsoup.connect(url).get();
	  } catch (Exception e) {
		e.printStackTrace();
	  }
      Elements element=doc.select("div.home_news");
      String title=element.select("h2").text().substring(0, 4);
      System.out.println("===============================");
      System.out.println(title);
      System.out.println("===============================");
      for(Element el : element.select("li")) {
    	  System.out.println(el.text());    	  
      }
      System.out.println("===============================");
	}
}
