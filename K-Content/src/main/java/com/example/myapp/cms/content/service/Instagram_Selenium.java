package com.example.myapp.cms.content.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class Instagram_Selenium {
    //WebDriver
    private WebDriver driver;
    private WebElement element;
    //Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "src/main/resources/chromedriver";
//System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
//    WebDriver driver = new ChromeDriver();
//    base_url = "https://www.instagram.com/soodental9/";
    //크롤링 할 URL
    private String base_url;
    public void instagram_Selenium(String query) {

        //System Property SetUp
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
         driver = new ChromeDriver();
        base_url = "https://www.instagram.com/explore/tags/"+query;
    }

    public void crawl(String query) {
        try {
            instagram_Selenium(query);
            //get page (= 브라우저에서 url을 주소창에 넣은 후 request 한 것과 같다)
            int waittime=10;
            driver.get(base_url);
            JavascriptExecutor js = (JavascriptExecutor) driver;

            //System.out.println(driver.getPageSource());
            Document doc = Jsoup.parse(driver.getPageSource());
            // HTML 문서의 타이틀 추출하기
            System.out.println("HTML TITLE : " + doc.title());
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));


            WebElement posts  = driver.findElement(By.className("_aagv"));
            WebElement post= posts.findElement(By.tagName("img"));
            String imageUrl = post.getAttribute("src");
//            posts.click();
//            Elements tables = doc.select("._aagv");
            // class "_aagv" 내의 모든 이미지 가져오기
//            Elements images = doc.select("img._acf-");
            System.out.println("이미지 URL: " + imageUrl);
            // 이미지 출력 또는 저장
//            for (Element image : tables) {
//                String imageUrl = image.attr("src");
//                System.out.println("이미지 URL: " + imageUrl);
//            }
            int i=1;
//            for (Element page : tables) {
//                System.out.println("count : "+i++);
//                Element link = page.select("a").first();
//                String linkHref = link.attr("href");
//                System.out.println("a href : "+base_url+linkHref);
//                Element img = page.select("img").first();
//                String imgtag = img.outerHtml();
//                System.out.println("img : "+imgtag);
//            }
            System.out.println("======================================================");

        } catch (Exception e) {

            e.printStackTrace();

        } finally {
//            driver.close();
        }

    }
}
