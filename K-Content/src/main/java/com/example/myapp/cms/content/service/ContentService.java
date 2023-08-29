package com.example.myapp.cms.content.service;

import com.example.myapp.cms.content.dao.IContentRepository;
import com.example.myapp.cms.content.model.Content;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.Duration;
import java.util.List;

@Service
public class ContentService implements IContentService {

    @Autowired
    IContentRepository contentRepository;
//    @Autowired
//    WebDriverUtil WebDriverUtil;


    // 1. 드라이버 설치 경로
//    public static String WEB_DRIVER_ID = "webdriver.chrome.driver";
//    public static String WEB_DRIVER_PATH = "chromedriver";
//    String path = new ClassPathResource("chromedriver").getPath();


    @Override
    public List<Content> getAllContent() {
        return contentRepository.getAllContent();
    }

    @Override
    public Content getAContent(int id) {
        return contentRepository.getAContent(id);
    }



//    @Scheduled(cron="0/20 * * * * ?")
//    public void crol(){
//        WebDriver driver = WebDriverUtil.getChromeDriver();
////        WebElement> webElementList = new ArrayList<>();
//        String url = "https://www.instagram.com/nnirum_x/";
//        String query = "._acan";
//
//        if (!ObjectUtils.isEmpty(driver)) {
//            driver.get(url);
//            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
//            WebElement el = driver.findElement(By.cssSelector(query));
////            System.out.println(driver.findElements(By.cssSelector(query)));
//            el.click();
////            webElementList = driver.findElements(By.cssSelector(query));
//        }
////        WebElement parentElement = webElementList.get(0);
////        WebElement childElement =  parentElement.findElement(By.tagName("td"));
//
//
////        System.out.println(parentElement.getAttribute("class"));
////        System.out.println(parentElement.getCssValue("height"));
//
//
////        parentElement.click();
////        parentElement.submit();
//    }



}
