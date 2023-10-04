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
import com.example.myapp.user.bkmk.model.GoodsBkmk;
import com.example.myapp.user.bkmk.model.GoodsJFileJBklkList;
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
        List<GoodsJFileJBklkList> selectGoodsMkmkList = bkmkService.selectGoodsBkmkList(userId);
        
        for (int i = 0; i < selectCntntBkmkList.size(); i++) {
            List<String> contentUrlSplit = List.of(selectCntntBkmkList.get(i).getCntntUrl().split("/"));
            String partOfUrl = contentUrlSplit.get(3);
            List<String> partOfUrl2 = List.of(partOfUrl.split("="));
            String resultCode = partOfUrl2.get(1);
            selectCntntBkmkList.get(i).setCntntThumnail("https://i.ytimg.com/vi/" + resultCode + "/hqdefault.jpg");
        }

        model.addAttribute("cntntList", selectCntntBkmkList);
        model.addAttribute("goodsList", selectGoodsMkmkList);
        
        logger.info(selectCntntBkmkList.toString());
        logger.info(selectGoodsMkmkList.toString());
        
        return "user/bkmk/main";
    }
    //콘텐츠 좋아요
    @PostMapping("/bkmk")
    @ResponseBody
    public String insertCntntBkmk(Authentication authentication, @RequestParam(value = "contentId") int cntntId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();
//        TODO 예외 처리
        bkmkService.insertCntntBkmk(userId, cntntId);
        return "ok";
    }
//콘텐츠 좋아요 취소
    @DeleteMapping("/bkmk")
    @ResponseBody
    //컨트롤러 리턴타입이 void 면 뷰이름이 자도응로 설정됨
    public String deleteCntntBkmk(Authentication authentication, @RequestParam(value = "contentId") int cntntId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();
        bkmkService.deleteCntntBkmk(userId, cntntId);
        return "ok";
    }
    //상품 좋아요
    @PostMapping("/bkmk/goods")
    @ResponseBody
    public String inserGoodstBkmk(Authentication authentication, @RequestParam(value = "goodsId") int goodsId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();
//        예외 처리
        bkmkService.insertGoodsBkmk(userId, goodsId);
        return "ok";
    }
    //상품 좋아요 취소
    @DeleteMapping("/bkmk/goods")
    @ResponseBody
    public String deleteGoodstBkmk(Authentication authentication, @RequestParam(value = "goodsId") int goodsId) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String userId = userDetails.getUsername();
//        예외 처리
        bkmkService.deleteGoodstBkmk(userId, goodsId);
        return "ok";
    }
}
