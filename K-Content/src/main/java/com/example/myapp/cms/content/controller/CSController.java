package com.example.myapp.cms.content.controller;

import com.example.myapp.cms.content.model.CntntGoodsMapping;
import com.example.myapp.cms.content.model.CmsContent;
import com.example.myapp.cms.content.model.CntntInsertForm;
import com.example.myapp.cms.content.model.YouTubeItem;
import com.example.myapp.cms.content.service.*;
import com.example.myapp.cms.goods.model.Goods;
import com.example.myapp.cms.goods.service.IGoodsService;
import com.example.myapp.commoncode.model.CommonCode;
import com.example.myapp.commoncode.service.ICommonCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

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
    @Autowired
    ICommonCodeService commonCodeService;

    @GetMapping("/dashboard")
    public String getDashBoard() {
        return "cms/dashBoard";
    }

    // 콘텐츠 추천 페이지
    @GetMapping("/recomm")
    public String showYouTube() {
        return "cms/cntnt/newcontentRecom";
    }

    //    @GetMapping("/recomm/main")
//    public List<YouTubeItem> showYouTubeMain(String search, @RequestParam(value = "items", required = false, defaultValue = "25") String items, Model model) {
//        int max = Integer.parseInt(items);
//        List<YouTubeItem> result = youTubeApiService.youTubeSearch(searchKeyword, max);
//
//        return result;
//
//    }
    @GetMapping("/getsearchyoutube")
    @ResponseBody
    public List<YouTubeItem> searchYouTube(@RequestParam(value = "searchKeyword", required = false) String searchKeyword, @RequestParam(value = "items", required = false, defaultValue = "20") String items) {
        int max = Integer.parseInt(items);
        List<YouTubeItem> result = youTubeApiService.youTubeSearch(searchKeyword, max);
        return result;
    }

    //콘텐츠 리스트 페이지
    @GetMapping("/contentmanage")
    public String getContentManage() {
        return "cms/cntnt/contentManage";
    }

    @GetMapping("/getallcntnt")
    @ResponseBody
    public List<CmsContent> getallcntnt() {
        List<CmsContent> result = contentService.getAllContent();

        for (int i = 0; i < result.size(); i++) {
            List<String> contentUrlSplit = List.of(result.get(i).getCntntUrl().split("/"));
            String partOfUrl = contentUrlSplit.get(3);
            List<String> partOfUrl2 = List.of(partOfUrl.split("="));
            String restultCode = partOfUrl2.get(1);
            result.get(i).setCntntThumnail("https://i.ytimg.com/vi/" + restultCode + "/hqdefault.jpg");

        }
        return result;
    }


    //콘텐츠 상세 페이지
    @GetMapping("/contentdetail")
    public String getAContent(int targetContentIdF, Model model) {
        CmsContent content = contentService.getAContent(targetContentIdF);
        model.addAttribute("content", content);

        CommonCode commonCodes = commonCodeService.findByCommonCode(content.getCntntCateCode());
        System.out.println("commonCodes:" + commonCodes);
        model.addAttribute("category", commonCodes);

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
        List<String> contentUrlSplit = List.of(targetContentIdF.split("/"));
        String partOfUrl = contentUrlSplit.get(3);
        List<String> partOfUrl2 = List.of(partOfUrl.split("="));
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
            //TODO encodedString만 보내고 태그는 자바사크립트에서 적기 @!!!!
            realImg.add("<img src=data:image/jpg;base64," + encodedString + " style=\"width: 200px; height: auto;\" >");
        }
        instagram_Selenium.chromeExit();
        return realImg;
    }

    //콘텐츠 생성 페이지
    @GetMapping("/makecontent/new")
    public String getMakeContentFormNew(Model model) {
        List<CommonCode> commonCodes = commonCodeService.findCommonCateCodeByUpperCommonCode("C03");
        model.addAttribute("category", commonCodes);
        return "cms/cntnt/newcontentMakeForm";
    }


    //콘텐츠 생성 url 넣고 출력 하면 타이틀 썸네일 뽑기
    @GetMapping("/makecontent/new/etc")
    @ResponseBody
    public CmsContent getMakeContentFormTHumTitle(@RequestParam(value = "url") String url) {
        CmsContent newcntnt = new CmsContent();
        List<String> contentUrlSplit = List.of(url.split("/"));
        String partOfUrl = contentUrlSplit.get(3);
        List<String> partOfUrl2 = List.of(partOfUrl.split("="));
        String restultCode = partOfUrl2.get(1);
        newcntnt.setCntntThumnail("https://i.ytimg.com/vi/" + restultCode + "/hqdefault.jpg");
//        newcntnt.setCntntTitle();
        return newcntnt;
    }


    //콘텐츠 생성 페이지 form 유튜브
    @GetMapping("/makecontent")
    public String getMakeContentForm(String cntntURL, String cntntTitle, Model model) {
        CmsContent cntnt = new CmsContent();
        cntnt.setCntntTitle(cntntTitle);
        cntnt.setCntntUrl(cntntURL);
        model.addAttribute("content", cntnt);
        List<CommonCode> commonCodes = commonCodeService.findCommonCateCodeByUpperCommonCode("C03");
        model.addAttribute("category", commonCodes);
        return "cms/cntnt/newcontentMakeForm";
    }

    @GetMapping("/makecontent/update")
    public String getUpdateContentForm(int targetContentIdF, Model model) {
        //기존 콘텐츠 데이터 뽑기
        CmsContent content = contentService.getAContent(targetContentIdF);
        model.addAttribute("content", content);

        //키워드
        List<String> keywordList = Arrays.stream(content.getCntntKwrd().split(",")).toList();
        model.addAttribute("keywordList", keywordList);

        //굿즈 & 파일
        List<CntntGoodsMapping> goodsIdByCntnt = cntntGoodsMappingService.getAllGoodsByContent(targetContentIdF);
        List<Goods> goodsJFileList = new ArrayList<Goods>();
        for (int i = 0; i < goodsIdByCntnt.size(); i++) {
            //일단 파일이 하나라고 가정....
            goodsJFileList.add(goodsService.getGoodsJFileByGoodsId(goodsIdByCntnt.get(i).getGoodsId()));
        }
        model.addAttribute("goodsJFileList", goodsJFileList);

        //카테고리
        List<CommonCode> commonCodes = commonCodeService.findCommonCateCodeByUpperCommonCode("C03");
        model.addAttribute("category", commonCodes);

        // 쿼리 앞에 키워드 가져와서 뽑기
        List<String> trendQueryList = new ArrayList<>();
        for (int i = 0; i < keywordList.size(); i++) {
            trendQueryList.add(keywordList.get(i));
        }
        model.addAttribute("trendQueryList", trendQueryList);
        return "cms/cntnt/newcontentMakeForm";
    }

    @PostMapping("/content/inputcntntform")
    @ResponseBody
    public void postCntntForm(@RequestBody CntntInsertForm receivedData) {
        CmsContent content = new CmsContent();

        content.setCntntUrl(receivedData.getCntntUrl());
        content.setCntntTitle(receivedData.getCntntTitle());
        content.setCntntCateCode(receivedData.getCntntCateCode());
        // 키워드 리스트를 하나의 문자열로

        String keywordlist = receivedData.getKeywordList().toString();

        String keyword = receivedData.getKeywordList().toString().substring(1, keywordlist.length() - 1);
        content.setCntntKwrd(keyword);

        List<Integer> goodsList = receivedData.getGoodsList();
        if (receivedData.getIs().equals("수정")) {
            content.setCntntId(receivedData.getCntntId());
            contentService.updateAContent(content, goodsList);
        } else if (receivedData.getIs().equals("생성")) {
            contentService.insertAContent(content, goodsList);
        }

        // 성공 여부 리턴
    }

    //콘텐츠 상세 콘텐츠 추천
    @GetMapping("/contentbykeyword")
    @ResponseBody
    public List<CmsContent> getContentBykeyword(@RequestParam(value = "trendQueryList") List<String> keywordList,
                                                @RequestParam(value = "cntntId") int cntntId) {
        List<CmsContent> result = contentService.getContentByKeyword(keywordList);
        for (int i = 0; i < result.size(); i++) {
            if (result.get(i).getCntntId() == cntntId) {
                result.remove(i);
            } else {
                continue;
            }
        }

        for (int i = 0; i < result.size(); i++) {
            List<String> contentUrlSplit = List.of(result.get(i).getCntntUrl().split("/"));
            String partOfUrl = contentUrlSplit.get(3);
            List<String> partOfUrl2 = List.of(partOfUrl.split("="));
            String restultCode = partOfUrl2.get(1);
            result.get(i).setCntntThumnail("https://i.ytimg.com/vi/" + restultCode + "/hqdefault.jpg");
        }
        return result;
    }

    @GetMapping("/getsearchcntnt")
    @ResponseBody
    public List<CmsContent> getContentBySearchKeyword(@RequestParam(value = "searchKeyword") String searchKeyword) {

        List<CmsContent> result = contentService.getContentByKeyword(Collections.singletonList(searchKeyword));
        for (int i = 0; i < result.size(); i++) {
            List<String> contentUrlSplit = List.of(result.get(i).getCntntUrl().split("/"));
            String partOfUrl = contentUrlSplit.get(3);
            List<String> partOfUrl2 = List.of(partOfUrl.split("="));
            String restultCode = partOfUrl2.get(1);
            result.get(i).setCntntThumnail("https://i.ytimg.com/vi/" + restultCode + "/hqdefault.jpg");
        }
        return result;
    }


    @GetMapping("/ma")
    public String getAllds() {
        return "include/admin-sideBar";
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
