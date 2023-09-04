package com.example.myapp.cms.goods.service;

import com.example.myapp.cms.goods.model.Goods;

public interface IGoodsService {
    Goods getAGoods(int goodsId);

    Goods getAGoodsJFile(int goodsId);

    //일단 굿즈 파일이 하나라고 가정..
    Goods getGoodsJFileByGoodsId(int goodsId);


}
