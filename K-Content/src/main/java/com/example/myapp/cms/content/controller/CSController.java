package com.example.myapp.cms.content.controller;

import com.example.myapp.cms.content.model.CntntGoodsMapping;
import com.example.myapp.cms.content.model.Content;
import com.example.myapp.cms.content.model.YouTubeItem;
import com.example.myapp.cms.content.service.*;
import com.example.myapp.cms.content.model.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
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

    // 콘텐츠 추천 페이지
    @GetMapping("/recomm")
    public String searchYouTube(String search, @RequestParam(value = "items", required = false, defaultValue = "25") String items, Model model) {
        int max = Integer.parseInt(items);
        List<YouTubeItem> result = youTubeApiService.youTubeSearch(search, max);

        model.addAttribute("content", result);
        model.addAttribute("search", search);
        return "cms/cntnt/contentRecom";
    }

    //콘텐츠 리스트 페이지
    @GetMapping("/contentmanage")
    public String getContentManage(Model model) {
        List<Content> result = contentService.getAllContent();

        for(int i=0; i<result.size(); i++){
            List<String> contentUrlSplit = List.of(result.get(i).getCntntUrl().split("/"));
            String partOfUrl = contentUrlSplit.get(3);
            List<String> partOfUrl2 = List.of(partOfUrl.split("="));
            String restultCode = partOfUrl2.get(1);
            result.get(i).setCntntThumnail("https://i.ytimg.com/vi/"+restultCode+"/hqdefault.jpg");

        }
        model.addAttribute("content", result);

        return "cms/cntnt/contentManage";
    }

    //콘텐츠 상세 페이지
    @GetMapping("/contentdetail")
    public String getAContent(int targetContentIdF, Model model) {
        Content content = contentService.getAContent(targetContentIdF);

        model.addAttribute("content", content);

        List<String> keywordList = Arrays.stream(content.getCntntKwrd().split(",")).toList();
        model.addAttribute("keywordList", keywordList);


        List<CntntGoodsMapping> goodsIdByCntnt = cntntGoodsMappingService.getAllGoodsByContent(targetContentIdF);
        List<Goods> goodsJFileList = new ArrayList<Goods>();
        for (int i = 0; i < goodsIdByCntnt.size(); i++) {
            //일단 파일이 하나라고 가정....
            goodsJFileList.add(goodsService.getGoodsJFileByGoodsId(goodsIdByCntnt.get(i).getGoodsId()));
        }
        model.addAttribute("goodsJFileList", goodsJFileList);
        // 쿼리 앞에 키워드 가져와서 뽑기
        List<String> trendQueryList = new ArrayList<>();

        for (int i = 0; i < keywordList.size(); i++) {
            trendQueryList.add(keywordList.get(i));
        }
        model.addAttribute("trendQueryList", trendQueryList);

//        javascript비통기로 보내
        return "cms/cntnt/contentDetail";
    }

    //콘텐츠 상세페이지의 유튜브 영상 호출
    @GetMapping("/youtube/iframe")
    @ResponseBody
    public String getAIframe(@RequestParam(value = "targetContentIdF") String targetContentIdF) {
        System.out.println("targetContentIdFtargetContentIdF: " + targetContentIdF);
        List<String> contentUrlSplit = List.of(targetContentIdF.split("/"));
        String partOfUrl = contentUrlSplit.get(3);
        List<String> partOfUrl2 = List.of(partOfUrl.split("="));
        System.out.println("partOfUrl2: " + partOfUrl2);
        String restultCode = partOfUrl2.get(1);
        return restultCode;
    }

    //콘텐츠 상세 페이지인스타 크롤링
    @GetMapping("/instacrol")
    @ResponseBody
    public List<String> getInstaImg(@RequestParam(value = "trendQueryList") List<String> trendQueryList) throws IOException {

        instagram_Selenium.instagram_Selenium();


        List<String> realImg = new ArrayList<>();
        for (int i = 0; i < trendQueryList.size(); i++) {
            String oneUrl = instagram_Selenium.crawl(trendQueryList.get(i));
            URL urlInput = new URL(oneUrl);
            BufferedImage urlImg = ImageIO.read(urlInput);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(urlImg, "jpg", bos);
            Base64.Encoder encoder = Base64.getEncoder();
            String encodedString = encoder.encodeToString(bos.toByteArray());
            realImg.add("<img src=data:image/jpg;base64," + encodedString + " style=\"width: 200px; height: auto;\" >");
        }
        instagram_Selenium.chromeExit();
        return realImg;

    }
    //콘텐츠 생성 페이지
    @GetMapping("/makecontent")
    public String getMakeContentForm(String cntntURL,  String cntntTitle, Model model) {
       Content cntnt = new Content();
        cntnt.setCntntTitle(cntntTitle);
        cntnt.setCntntUrl(cntntURL);
        model.addAttribute("content",cntnt);

        System.out.println("cntntURL: "+cntntURL);
        System.out.println("cntntTitle: "+cntntTitle);
        return "cms/cntnt/contentMakeForm";
    }

    @GetMapping("/makecontent/update")
    public String getUpdateContentForm(int targetContentIdF, Model model) {
//        System.out.println("수정부 입니다: "+targetContentIdF);
        Content content = contentService.getAContent(targetContentIdF);

        model.addAttribute("content", content);

        List<String> keywordList = Arrays.stream(content.getCntntKwrd().split(",")).toList();
        model.addAttribute("keywordList", keywordList);


        List<CntntGoodsMapping> goodsIdByCntnt = cntntGoodsMappingService.getAllGoodsByContent(targetContentIdF);
        List<Goods> goodsJFileList = new ArrayList<Goods>();
        for (int i = 0; i < goodsIdByCntnt.size(); i++) {
            //일단 파일이 하나라고 가정....
            goodsJFileList.add(goodsService.getGoodsJFileByGoodsId(goodsIdByCntnt.get(i).getGoodsId()));
        }
        model.addAttribute("goodsJFileList", goodsJFileList);
        // 쿼리 앞에 키워드 가져와서 뽑기
        List<String> trendQueryList = new ArrayList<>();

        for (int i = 0; i < keywordList.size(); i++) {
            trendQueryList.add(keywordList.get(i));
        }
        model.addAttribute("trendQueryList", trendQueryList);

        return "cms/cntnt/contentMakeForm";
    }
    @GetMapping("/ma")
    public String getAllds() {
        return "include/admin-sideBar";
    }

    @GetMapping("/gird")
    public String getAlld() {
        return "contentDetail";
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
