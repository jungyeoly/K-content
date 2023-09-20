package com.example.myapp.user.content.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import com.example.myapp.cms.content.model.CmsContent;
import com.example.myapp.cms.content.model.CntntGoodsMapping;
import com.example.myapp.cms.content.service.ICntntGoodsMappingService;
import com.example.myapp.cms.content.service.IContentService;
import com.example.myapp.cms.goods.model.Goods;
import com.example.myapp.cms.goods.service.IGoodsService;
import com.example.myapp.commoncode.model.CommonCode;
import com.example.myapp.commoncode.service.ICommonCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.myapp.user.content.model.Content;
import com.example.myapp.user.content.service.IContentUserService;

import javax.imageio.ImageIO;


@Controller
public class contentUserController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IContentUserService contentService;
    @Autowired
    IContentService csContentService;
    @Autowired
    ICommonCodeService commonCodeService;
    @Autowired
    ICntntGoodsMappingService cntntGoodsMappingService;
    @Autowired
    IGoodsService goodsService;

    @GetMapping("/user/content")
    public String selectUserContentList(@RequestParam(required = false, defaultValue = "All") String cate, @RequestParam(required = false, defaultValue = "1") Integer start, @RequestParam(required = false, defaultValue = "15") Integer end, Model model) {
        // 카테고리 별 조회

        List<Content> contentList = contentService.selectUserContent(cate, start, end);

        for (int i = 0; i < contentList.size(); i++) {
            // 유튜뷰 영상 썸네일 추출
            List<String> contentUrlSplit = List.of(contentList.get(i).getCntntUrl().split("/"));
            String partOfUrl = contentUrlSplit.get(3);
            List<String> partOfUrl2 = List.of(partOfUrl.split("="));
            String resultCode = partOfUrl2.get(1);
            contentList.get(i).setCntntThumnail("https://i.ytimg.com/vi/" + resultCode + "/hqdefault.jpg");
        }

        model.addAttribute("contentList", contentList);

        return "user/content/list";
    }
    @PostMapping("/user/content")
    public String searchUserContentList(@RequestParam String keyword, Model model) {

        List<Content> contentList = contentService.searchUserContent(keyword);

        for (int i = 0; i < contentList.size(); i++) {
            // 유튜뷰 영상 썸네일 추출
            List<String> contentUrlSplit = List.of(contentList.get(i).getCntntUrl().split("/"));
            String partOfUrl = contentUrlSplit.get(3);
            List<String> partOfUrl2 = List.of(partOfUrl.split("="));
            String resultCode = partOfUrl2.get(1);
            contentList.get(i).setCntntThumnail("https://i.ytimg.com/vi/" + resultCode + "/hqdefault.jpg");
        }

        model.addAttribute("contentList", contentList);

        return "user/content/list";
    }

    @GetMapping("/content/scroll")
    public @ResponseBody Map<String, Object> show(@RequestParam(required = false, defaultValue = "All") String cate, @RequestParam int start, @RequestParam int end, Model model) {
        List<Content> contentList = contentService.selectUserContent(cate, start, end);

        for (int i = 0; i < contentList.size(); i++) {
            // 유튜뷰 영상 썸네일 추출
            List<String> contentUrlSplit = List.of(contentList.get(i).getCntntUrl().split("/"));
            String partOfUrl = contentUrlSplit.get(3);
            List<String> partOfUrl2 = List.of(partOfUrl.split("="));
            String resultCode = partOfUrl2.get(1);
            contentList.get(i).setCntntThumnail("https://i.ytimg.com/vi/" + resultCode + "/hqdefault.jpg");
        }

        Map<String, Object> map = new HashMap<>();

        map.put("contentList", contentList);

        return map;
    }


    //여기 봐봐
    @GetMapping("/user/content/detail")
    public String getAContent(int targetContentIdF, Model model) {

        CmsContent content = csContentService.getAContent(targetContentIdF);
        model.addAttribute("content", content);

        CommonCode commonCodes = commonCodeService.findByCommonCode(content.getCntntCateCode());

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
        return "user/content/contentDetail";
    }


}
