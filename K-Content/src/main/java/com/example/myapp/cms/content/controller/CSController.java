package com.example.myapp.cms.content.controller;

import com.example.myapp.cms.content.dao.IGoodsRepository;
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

import java.util.ArrayList;
import java.util.Arrays;
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
    CrawlingExample crawlingExample;
    @GetMapping("/dashboard")
    public String getDashBoard() {
        String query = "태민";
        crawlingExample.process(query);
        return "cms/dashBoard";
    }

    @GetMapping("/contentmanage")
    public String getContentManage(Model model) {
        List<Content> result = contentService.getAllContent();
        model.addAttribute("content", result);

        return "cms/contentManage";
    }
    @GetMapping("/contentmanage/contentdetail")
    public String getAContent(Model model) {
        int id = 5;
        Content content = contentService.getAContent(id);
        model.addAttribute("content", content);

        List<String> contentList  = Arrays.stream(content.getCntntKwrd().split(",")).toList();
        model.addAttribute("contentList", contentList);

        //  contntID 알고 있으a
        List<CntntGoodsMapping> goodsIdByCntnt = cntntGoodsMappingService.getAllGoodsByContent(id);
        List<Goods> goodsList = new ArrayList<Goods>();
        for (int i=0; i<goodsIdByCntnt.size();  i++ ){
            goodsList.add(goodsService.getAGoods(goodsIdByCntnt.get(i).getGoodsId()));
        }
        model.addAttribute("goodsList", goodsList);
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

    @GetMapping("/contentdetail")
    public String getContentDetail() {
        return "cms/contentDetail";
    }


}
