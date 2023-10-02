package com.example.myapp.cms.goods.service;

import com.example.myapp.cms.goods.model.Goods;
import com.example.myapp.cms.goods.model.GoodsFile;

import java.util.List;

public interface IGoodsService {
    Goods getAGoods(int goodsId);

    Goods getAGoodsJFile(int goodsId);

    //일단 굿즈 파일이 하나라고 가정..
    Goods getGoodsJFileByGoodsId(int goodsId);

    List<Goods> getAllGoodsJFile(int page);

    List<Goods> getSearchGoodsJFile(String search);

    int totalGoods();

    int insertGoods(Goods goods, GoodsFile goodsFile);

    void updateDelYnGoods(int goodsId);

    int updateGoods(Goods goods, GoodsFile goodsFile);

    void updateGoods(Goods goods);
}
