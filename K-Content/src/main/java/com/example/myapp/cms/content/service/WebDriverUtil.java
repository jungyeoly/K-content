//package com.example.myapp.cms.content.service;
//
//import java.time.Duration;
//import java.util.ArrayList;
//import java.util.List;
//
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.chrome.ChromeDriver;
//import org.openqa.selenium.chrome.ChromeOptions;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.stereotype.Service;
//import org.springframework.util.ObjectUtils;
//
//@Component
//public class WebDriverUtil {
//    private static String WEB_DRIVER_PATH = "src/main/resources/chromedriver"; // WebDriver 경로
//
//    public static WebDriver getChromeDriver() {
//        if (ObjectUtils.isEmpty(System.getProperty("webdriver.chrome.driver"))) {
//            System.setProperty("webdriver.chrome.driver", WEB_DRIVER_PATH);
//        }
//
//        // webDriver 옵션 설정
//        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.setHeadless(true);
//        chromeOptions.addArguments("--lang=ko");
//        chromeOptions.addArguments("--no-sandbox");
//        chromeOptions.addArguments("--disable-dev-shm-usage");
//        chromeOptions.addArguments("--disable-gpu");
//        chromeOptions.setCapability("ignoreProtectedModeSettings", true);
//
//        WebDriver driver = new ChromeDriver(chromeOptions);
//        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
//
//        return driver;
//    }
//
//    public static void quit(WebDriver driver) {
//        if (!ObjectUtils.isEmpty(driver)) {
//            driver.quit();
//        }
//    }
//
//    public static void close(WebDriver driver) {
//        if (!ObjectUtils.isEmpty(driver)) {
//            driver.close();
//        }
//    }
//
//
//
//}
