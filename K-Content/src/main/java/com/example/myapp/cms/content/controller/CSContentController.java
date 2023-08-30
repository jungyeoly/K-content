package com.example.myapp.cms.content.controller;
import java.util.List;
import java.lang.Integer;


import com.example.myapp.cms.content.model.YouTubeItem;
import com.example.myapp.cms.content.service.YouTubeApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
@RequestMapping("/cs")
public class CSContentController {
    @Autowired
    YouTubeApiService youTubeApiService;

    @GetMapping("/")
    public  String main(){
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
}
