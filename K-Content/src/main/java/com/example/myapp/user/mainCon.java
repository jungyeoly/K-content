package com.example.myapp.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class mainCon {
        @GetMapping("/")
    public String getUserState() {
        return "user/main";
//        return "include/admin-sideBar";

    }
}
