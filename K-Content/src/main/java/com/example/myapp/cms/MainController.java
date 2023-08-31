package com.example.myapp.cms;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/as")
    public String getMain(Model model) {

//        여기에
    model.addAttribute("keyword", "a");

        String t = "asyTarget";
        model.addAttribute("targetHtml", "/asyTarget");

        //여기에 model.addAttribute("keyword", "a")


        return "asyHesu";

    }
    @GetMapping("/as2")
    public String getMain2(Model model) {

//        여기에
        model.addAttribute("keyword", "a");

        String t = "asyTarget";
        model.addAttribute("targetHtml", "/asyTarget2");

        //여기에 model.addAttribute("keyword", "a")


        return "asyHesu";

    }
}
