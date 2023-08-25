package com.example.myapp.controller;

import com.example.myapp.model.YouTubeItem;
import com.example.myapp.service.YouTubeApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/cs")
public class CSController {
    @Autowired
    YouTubeApiService youTubeApiService;

    @GetMapping("/dashBoard")
    public String getDashBoard() {
        return "admin/dashBoard";
    }

    @GetMapping("/contentManage")
    public String getContentManage(
//            String search,
            @RequestParam(value = "items", required = false, defaultValue = "25") String items, Model model) {

        String search = "프렌치불독";
        int max = Integer.parseInt(items);
        List<YouTubeItem> result = youTubeApiService.youTubeSearch(search, max);
//        result.get(1).getThumbnail().ge
        model.addAttribute("content", result);


        return "admin/contentManage";
    }

    @GetMapping("/goods")
    public String getGoods() {
        return "admin/goods";
    }

    @GetMapping("/userManage")
    public String getUserManage() {
        return "admin/userManage";
    }

    @GetMapping("/commu")
    public String getCommu() {
        return "admin/commu";
    }

    @GetMapping("/inquiry")
    public String getInquiry() {
        return "admin/inquiry";
    }

    @GetMapping("/charts")
    public String getCharts() {
        return "admin/charts";
    }

    @GetMapping("/contentDetail")
    public String getContentDetail() {
        return "admin/contentDetail";
    }


}
