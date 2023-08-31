package com.example.myapp.cms;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/as")
    public String getMain(Model model) {

//        타깃 되는(갈아 끼워짐을 당하는) html의 div 안에 들어갈 데이터들을 넣어주세요 start
        model.addAttribute("keyword", "a");

//        타깃 되는(갈아 끼워짐을 당하는) html의 div 안에 들어갈 데이터들을 넣어주세요 end


        // 타깃되는 html의 이름을 attrVal자리에 넣어주세요
        model.addAttribute("targetHtml", "/asyTarget");
        return "asyHesu";

    }

    @GetMapping("/as2")
    public String getMain2(Model model) {

//        타깃 되는(갈아 끼워짐을 당하는) html의 div 안에 들어갈 데이터들을 넣어주세요 start
        model.addAttribute("keyword", "a");

//        타깃 되는(갈아 끼워짐을 당하는) html의 div 안에 들어갈 데이터들을 넣어주세요 end


        // 타깃되는 html의 이름을 attrVal자리에 넣어주세요
        model.addAttribute("targetHtml", "/asyTarget2");
        return "asyHesu";

    }
}
