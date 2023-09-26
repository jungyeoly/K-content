package com.example.myapp.cms.content.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class Instagram_Selenium {
    //WebDriver
    private WebDriver driver;
    //Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "src/main/resources/chromedriver";
    public static final String WEB_DRIVER_PATH_WIN = "src/main/resources/chromedriver_win.exe";
    private String base_url;

    public void instagram_Selenium() {
        //System Property SetUp
                String os = System.getProperty("os.name").toLowerCase();

        if (os.contains("win")) {
            System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH_WIN);
        } else if (os.contains("mac")) {
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);
        }

        driver = new ChromeDriver();
    }

    public String crawl(String query) {

        base_url = "https://www.instagram.com/explore/tags/" + query;
        try {
            driver.get(base_url);
            Document doc = Jsoup.parse(driver.getPageSource());
            // HTML 문서의 타이틀 추출하기
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            WebElement posts = driver.findElement(By.className("_aagv"));
            WebElement post = posts.findElement(By.tagName("img"));
            String imageUrl = post.getAttribute("src");
            return imageUrl;
        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            // 웹드라이버 한 번만 띄우려고 닫지 않았
//            driver.close();
        }
return "";
    }
    public void chromeExit(){
        driver.close();
    }
}
