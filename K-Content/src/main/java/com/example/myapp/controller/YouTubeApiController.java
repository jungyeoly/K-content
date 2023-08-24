package com.example.myapp.controller;

import java.util.List;
import java.util.ArrayList;
import java.lang.Integer;


import com.example.myapp.model.YouTubeItem;
import com.example.myapp.service.YouTubeApiService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;


@Controller
public class YouTubeApiController {

    @Autowired
    YouTubeApiService youTubeApiService;

    //    @GetMapping("/google-youtube-api")
//    public @ResponseBody List<YouTubeItem> searchYouTube(
//            @RequestParam(value = "search", required = true) String search,
//            @RequestParam(value = "items", required = false, defaultValue = "25") String items) {
//
//        int max = Integer.parseInt(items);
//        List<YouTubeItem> result = youTubeApiService.youTubeSearch(search, max);
//        return result;
//    }
//    @GetMapping("/")
//    public String searchYouTube(
////        @RequestParam(value = "search", required = true) String search,
//            @RequestParam(value = "items", required = false, defaultValue = "25") String items, Model model
//    ) {
//        String search = "강형욱";
//        int max = Integer.parseInt(items);
//        List<YouTubeItem> result = youTubeApiService.youTubeSearch(search, max);
////        result.get(1).getThumbnail().ge
//        model.addAttribute("content", result);
//        return "user/main";
//    }

    @GetMapping("/test-dummy-api")
    public @ResponseBody List<YouTubeItem> testSearchYouTube(
            @RequestParam(value = "search", required = true) String search,
            @RequestParam(value = "items", required = false, defaultValue = "1") String items) {

        int max = Integer.parseInt(items);
        List<YouTubeItem> result = new ArrayList<>();
        result.add(new YouTubeItem("foobar", search, "far", "czar"));
        return result;
    }
}
