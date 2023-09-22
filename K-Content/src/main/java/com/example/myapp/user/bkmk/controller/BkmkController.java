package com.example.myapp.user.bkmk.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.myapp.user.bkmk.model.CntntBkmk;
import com.example.myapp.user.bkmk.service.IBkmkService;


@Controller
public class BkmkController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IBkmkService bkmkService;

    @GetMapping("/bkmk")
    public String selectBkmkList(Authentication authentication, Model model) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();
        List<CntntBkmk> selectCntntBkmkList = bkmkService.selectCntntBkmkList(userId);
        model.addAttribute("cntntList", selectCntntBkmkList);
        return "user/bkmk/main";
    }
    @PostMapping("/bkmk")
    @ResponseBody
    public String insertBkmkList(Authentication authentication, @RequestParam(value = "contentId") int cntntId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();
//        예외 처리
        bkmkService.insertCntntBkmkList(userId, cntntId);
        return "ok";
    }

    @DeleteMapping("/bkmk")
    @ResponseBody
    //컨트롤러 리턴타입이 void 면 뷰이름이 자도응로 설정됨
    public String deleteBkmkList(Authentication authentication, @RequestParam(value = "contentId") int cntntId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();

        bkmkService.deleteCntntBkmkList(userId, cntntId);
        return "ok";
    }

}
