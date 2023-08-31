package com.example.myapp.cms.content.controller;

import com.example.myapp.cms.content.model.CntntGoodsMapping;
import com.example.myapp.cms.content.model.Content;
import com.example.myapp.cms.content.model.YouTubeItem;
import com.example.myapp.cms.content.service.*;
import com.example.myapp.cms.content.model.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    @GetMapping("/recomm")
    public String searchYouTube(String search, @RequestParam(value = "items", required = false, defaultValue = "25") String items, Model model) {
//        String search = "강형욱";
        int max = Integer.parseInt(items);
        List<YouTubeItem> result = youTubeApiService.youTubeSearch(search, max);
//        result.get(1).getThumbnail().ge
        model.addAttribute("content", result);
        return "cms/recommendContent";
    }

    @GetMapping("/contentmanage")
    public String getContentManage(Model model) {
        List<Content> result = contentService.getAllContent();
        model.addAttribute("content", result);

        return "cms/contentManage";
    }


    @GetMapping("/contentdetail")
    public String getAContent(int targetContentIdF, Model model) throws IOException {
        Content content = contentService.getAContent(targetContentIdF);
        model.addAttribute("content", content);

        List<String> keywordList = Arrays.stream(content.getCntntKwrd().split(",")).toList();
        model.addAttribute("keywordList", keywordList);

        List<CntntGoodsMapping> goodsIdByCntnt = cntntGoodsMappingService.getAllGoodsByContent(targetContentIdF);
        List<Goods> goodsList = new ArrayList<Goods>();
        for (int i = 0; i < goodsIdByCntnt.size(); i++) {
            goodsList.add(goodsService.getAGoods(goodsIdByCntnt.get(i).getGoodsId()));
        }
        model.addAttribute("goodsList", goodsList);
        // 쿼리 앞에 키워드 가져와서 뽑기
        List<String> trendQueryList = new ArrayList<>();
        for (int i = 0; i < keywordList.size(); i++) {
            trendQueryList.add(keywordList.get(i));
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
        }
        model.addAttribute("realImg", realImg);
        return "cms/contentDetail2";
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




}
