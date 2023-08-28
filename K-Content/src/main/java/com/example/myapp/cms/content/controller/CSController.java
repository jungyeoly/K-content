package com.example.myapp.cms.content.controller;

import com.example.myapp.cms.content.model.YouTubeItem;
import com.example.myapp.cms.content.service.ICSService;
import com.example.myapp.cms.content.service.YouTubeApiService;
import com.example.myapp.cms.content.model.Goods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//url 카멜에 쓰지마!!!!
@Controller
@RequestMapping("/cs")
public class CSController {
    @Autowired
    YouTubeApiService youTubeApiService;
    @Autowired
    ICSService csService;

    @GetMapping("/dashboard")
    public String getDashBoard() {
        return "cms/dashBoard";
    }

    @GetMapping("/contentmanage")
    public String getContentManage(
//            String search,
            @RequestParam(value = "items", required = false, defaultValue = "25") String items, Model model) {

        String search = "프렌치불독";
        int max = Integer.parseInt(items);
        List<YouTubeItem> result = youTubeApiService.youTubeSearch(search, max);
//        result.get(1).getThumbnail().ge
        model.addAttribute("content", result);


        return "cms/contentManage";
    }

    @GetMapping("/goods")
    public String getAllGoods (Model model) {

        List<Goods> getAllGoods = csService.getAllGoods();
        model.addAttribute("goods",getAllGoods);

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
