package com.example.myapp.cms.goods.controller;

import com.example.myapp.cms.goods.model.Goods;
import com.example.myapp.cms.goods.service.IGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/cs/goods")
public class goodsController {
    @Autowired
    IGoodsService goodsService;

    @GetMapping("/")
    public String getPages() {
        return "cms/goods/goodsList";
    }

    //모든 상품 리스트 가져오기
    @GetMapping("/list")
    @ResponseBody
    public List<Goods> getAllGoods() {
        List<Goods> goodsList = goodsService.getAllGoodsJFile();

        return goodsList;
    }

    // 상품 검색 결과 출력
    @GetMapping("/search")
    @ResponseBody
    public List<Goods> getSearchGoods(String search) {
        List<Goods> goodsList = goodsService.getSearchGoodsJFile(search);
        return goodsList;
    }

}
