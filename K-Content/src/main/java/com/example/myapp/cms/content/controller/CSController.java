package com.example.myapp.cms.content.controller;

import com.example.myapp.cms.content.model.CntntGoodsMapping;
import com.example.myapp.cms.content.model.Content;
import com.example.myapp.cms.content.service.*;
import com.example.myapp.cms.content.model.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

//url 카멜에 쓰지마!!!!
@Controller
@RequestMapping("/cs")
public class CSController {
    @Autowired
    YouTubeApiService youTubeApiService;

    @Autowired
    IContentService contentService;
    @Autowired
    ICSService csService;
    @Autowired
    ICntntGoodsMappingService cntntGoodsMappingService;
    @Autowired
    IGoodsService goodsService;

    @Autowired
    Instagram_Selenium instagram_Selenium;

    @GetMapping("/dashboard")
    public String getDashBoard() {

        return "cms/dashBoard";
    }

    @GetMapping("/contentmanage")
    public String getContentManage(Model model) {
        List<Content> result = contentService.getAllContent();
        model.addAttribute("content", result);

        return "cms/contentManage";
    }

    @GetMapping("/image")
    public ResponseEntity<byte[]> getImage(String s) {
        // 이미지 파일의 경로를 설정
        String imagePath = "https://scontent-ssn1-1.cdninstagram.com/v/t51.2885-15/366414072_261850193301231_7322515485343116845_n.jpg?stp=dst-jpg_e15&_nc_ht=scontent-ssn1-1.cdninstagram.com&_nc_cat=103&_nc_ohc=lCT3edsH6mcAX--_j8p&edm=AOUPxh0BAAAA&ccb=7-5&oh=00_AfDcuq9cHw0CvfHwcG3rCeueVg-DDJD2kwqN1MUcEf8SdA&oe=64EF90E2&_nc_sid=9dc660"; // 실제 이미지 파일 경로로 변경

        try {
            // 이미지 파일을 바이트 배열로 읽어옴
            Path path = Paths.get(imagePath);
            byte[] imageBytes = Files.readAllBytes(path);

            // HTTP 응답 헤더 설정 (이미지 타입에 따라 Content-Type 설정)
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG); // 이미지 타입에 따라 변경

            // ResponseEntity를 사용하여 이미지 데이터를 응답으로 반환
            return new ResponseEntity<>(imageBytes, headers, HttpStatus.OK);

        } catch (IOException e) {
            // 예외 처리: 이미지 파일을 읽을 수 없는 경우
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/contentdetail")
    public String getAContent(int targetContentIdF, Model model) throws IOException {
        Content content = contentService.getAContent(targetContentIdF);
        model.addAttribute("content", content);

        List<String> contentList = Arrays.stream(content.getCntntKwrd().split(",")).toList();
        model.addAttribute("contentList", contentList);

        List<CntntGoodsMapping> goodsIdByCntnt = cntntGoodsMappingService.getAllGoodsByContent(targetContentIdF);
        List<Goods> goodsList = new ArrayList<Goods>();
        for (int i = 0; i < goodsIdByCntnt.size(); i++) {
            goodsList.add(goodsService.getAGoods(goodsIdByCntnt.get(i).getGoodsId()));
        }
        model.addAttribute("goodsList", goodsList);
        // 쿼리 앞에 키워드 가져와서 뽑기
        List<String> trendQueryList = new ArrayList<>();
        for (int i = 0; i < contentList.size(); i++) {
            trendQueryList.add(contentList.get(i));
        }
        List<String> imgUrlList = new ArrayList<>();
        instagram_Selenium.instagram_Selenium();
        for (int i = 0; i < trendQueryList.size(); i++) {
            imgUrlList.add(instagram_Selenium.crawl(trendQueryList.get(i)));
        }


        List<String> realImg = new ArrayList<>();
        for (int i = 0; i < imgUrlList.size(); i++) {
            String oneUrl = instagram_Selenium.crawl(trendQueryList.get(i));
            URL urlInput = new URL(oneUrl);
            BufferedImage urlImg = ImageIO.read(urlInput);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(urlImg, "jpg", bos);
            Base64.Encoder encoder = Base64.getEncoder();
            String encodedString = encoder.encodeToString(bos.toByteArray());
            realImg.add("data:image/jpg;base64," + encodedString);
            System.out.println(realImg.size());
        }
        model.addAttribute("realImg", realImg);
        return "cms/contentDetail";
    }

    @GetMapping("/goods")
    public String getAllGoods(Model model) {

        List<Goods> getAllGoods = csService.getAllGoods();
        model.addAttribute("goods", getAllGoods);

        return "cms/goods";
    }

    @GetMapping("/usermanage")
    public String getUserManage() {
        return "cms/userManage";
    }

    @GetMapping("/commu")
    public String getCommu() {
        return "cms/commu";
    }

    @GetMapping("/inquiry")
    public String getInquiry() {
        return "cms/inquiry";
    }

    @GetMapping("/charts")
    public String getCharts() {
        return "cms/charts";
    }

//    @GetMapping("/contentdetail")
//    public String getContentDetail() {
//        return "cms/contentDetail";
//    }


}
